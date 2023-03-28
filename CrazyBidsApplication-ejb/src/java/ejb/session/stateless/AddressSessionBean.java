/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Address;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AddressIsAssociatedWithWinningBidException;
import util.exception.AddressIsDisabledException;
import util.exception.AddressNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateAddressException;

/**
 *
 * @author Bransome Tan Yi Hao
 */
@Stateless
public class AddressSessionBean implements AddressSessionBeanRemote, AddressSessionBeanLocal {

    @PersistenceContext(unitName = "CrazyBidsApplication-ejbPU")
    private EntityManager em;

    public AddressSessionBean() {
    }

    @Override
    public Address retrieveAddressbyId(Long addressId) throws AddressNotFoundException {
        Address address= em.find(Address.class, addressId);
        if (address != null) {
            return address;
        } else {
            throw new AddressNotFoundException("Address ID " + addressId + " not found in system!");
        }
    }

    @Override
    public List<Address> retrieveAllAddresses() {
        Query query = em.createQuery("SELECT a from Address a");
        return query.getResultList();
    }

    @Override
    public Long createAddress(Address newAddress) throws UnknownPersistenceException {
        try {
            em.persist(newAddress);
            em.flush();
            
            return newAddress.getId();
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public void updateAddress(Address updatedAddress) throws AddressNotFoundException, UpdateAddressException {
         
        if(updatedAddress != null && updatedAddress.getId() != null)
        {
            Address addressToUpdate = retrieveAddressbyId(updatedAddress.getId());
            
            if(addressToUpdate.getPostalCode().equals(updatedAddress.getPostalCode()))
            {
                addressToUpdate.setAddressLine1(updatedAddress.getAddressLine1());
                addressToUpdate.setAddressLine2(updatedAddress.getAddressLine2());
                addressToUpdate.setPostalCode(updatedAddress.getPostalCode());
                
                // Only allow to update the general details of address in this business method.
            }
            else
            {
                throw new UpdateAddressException("Name of address record to be updated does not match the existing record");
            }
        }
        else
        {
            throw new AddressNotFoundException("Address ID not provided for address to be updated");
        }
    }

    @Override
    public void deleteAddress(Long addressId) throws AddressNotFoundException, AddressIsAssociatedWithWinningBidException{
        Address address = retrieveAddressbyId(addressId);
        
        if (address.getSuccessfulAuctions().isEmpty()) {
            em.remove(address);
        } else {
            // if address has successful auctions, disable it
            throw new AddressIsAssociatedWithWinningBidException("Address is associated with winning bids and you are not allowed to delete it. You can opt to disable it.");
        }
    }
    
    @Override
    public void disableAddress(Long addressId) throws AddressNotFoundException, AddressIsDisabledException{
        Address address = retrieveAddressbyId(addressId);
           if (!address.getIsDisabled()) {
               address.setIsDisabled(true);
           } else {
               throw new AddressIsDisabledException("Address is already disabled.");
           }
    }
}



