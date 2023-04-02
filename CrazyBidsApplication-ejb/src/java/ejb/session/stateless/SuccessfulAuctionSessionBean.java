/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Address;
import entity.Auction;
import entity.Customer;
import entity.SuccessfulAuction;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AddressNotFoundException;
import util.exception.AuctionNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.DeliveryAddressExistException;
import util.exception.SuccessfulAuctionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateDeliveryAddressException;

/**
 *
 * @author Bransome Tan Yi Hao
 */
@Stateless
public class SuccessfulAuctionSessionBean implements SuccessfulAuctionSessionBeanRemote, SuccessfulAuctionSessionBeanLocal {

    @EJB
    private AuctionSessionBeanLocal auctionSessionBeanLocal;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private AddressSessionBeanLocal addressSessionBeanLocal;

    @PersistenceContext(unitName = "CrazyBidsApplication-ejbPU")
    private EntityManager em;
    
    @Override
    public SuccessfulAuction retrieveSuccessfulAuctionbyId(Long successfulAuctionId) throws SuccessfulAuctionNotFoundException {
        SuccessfulAuction successfulAuction = em.find(SuccessfulAuction.class, successfulAuctionId);
        if (successfulAuction!= null) {
            return successfulAuction;
        } else {
            throw new SuccessfulAuctionNotFoundException("Successful Auction ID " + successfulAuctionId + " not found in system!");
        }
    }
    
    @Override
    public List<SuccessfulAuction> retrieveAllSuccessfulAuctions() {
        Query query = em.createQuery("SELECT sa FROM SuccessfulAuction sa");
        return query.getResultList();
    }
    
    @Override
    public List<SuccessfulAuction> retrieveAllSuccessfulAuctionByCustomerId(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerSessionBeanLocal.retrieveCustomerbyId(customerId);
        customer.getSuccessfulAuctions().size();
        List<SuccessfulAuction> customerSuccessfulAuction = customer.getSuccessfulAuctions();
        return customerSuccessfulAuction;
    }

    @Override
    public Long createNewSuccessfulAuction(SuccessfulAuction newSuccessfulAuction, Long customerId, Long auctionId) throws UnknownPersistenceException, CustomerNotFoundException, AuctionNotFoundException {
        try {
             
            Customer customer = customerSessionBeanLocal.retrieveCustomerbyId(customerId);
            Auction auction = auctionSessionBeanLocal.retrieveAuctionbyId(auctionId);
            
            //Set relationship association
            newSuccessfulAuction.setCustomer(customer);
            newSuccessfulAuction.setAuction(auction);
            customer.getSuccessfulAuctions().add(newSuccessfulAuction);
            auction.setSuccessfulAuction(newSuccessfulAuction);
            em.persist(newSuccessfulAuction);
            em.flush();
            
            return newSuccessfulAuction.getId();
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }
    
    @Override
    public void updateDeliveryAddress(Long successfulAuctionId, Long addressId) throws DeliveryAddressExistException, UpdateDeliveryAddressException, SuccessfulAuctionNotFoundException, AddressNotFoundException
    {
        if(successfulAuctionId != null && addressId != null)
        {
            SuccessfulAuction successfulAuctionToUpdate = retrieveSuccessfulAuctionbyId(successfulAuctionId);
            Address address = addressSessionBeanLocal.retrieveAddressbyId(addressId);
            
            if(successfulAuctionToUpdate.getAddress() == null)
            {
                successfulAuctionToUpdate.setAddress(address);
                String deliveryAddress = address.getAddressLine1() + " " + address.getAddressLine2() + ", Postal Code: " + address.getPostalCode();
                successfulAuctionToUpdate.setSuccessfulAuctionDeliveryAddress(deliveryAddress);
            }
            else
            {
                throw new DeliveryAddressExistException("You have already set the delivery address. You are not allowed to change the delivery address again.");
            }
        }
        else
        {
            throw new UpdateDeliveryAddressException("Successful Auction Id and Address Id not provided for delivery address to be updated");
        }
    }
}
