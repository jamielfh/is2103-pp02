/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.soap;

import ejb.session.stateless.AuctionSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.SuccessfulAuctionSessionBeanLocal;
import entity.Auction;
import entity.Bid;
import entity.Customer;
import entity.ProxyBid;
import entity.Snipe;
import entity.SuccessfulAuction;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.AuctionNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.SuccessfulAuctionNotFoundException;
import util.exception.UpdateCustomerException;
import java.math.BigDecimal;
import java.util.Collections;
import util.exception.BidNotFoundException;
import util.exception.InvalidBidException;
import util.exception.NotEnoughCreditException;

/**
 *
 * @author Bransome Tan Yi Hao
 */
@WebService(serviceName = "PremiumCustomerWebService")
@Stateless()
public class PremiumCustomerWebService {

    @EJB
    private SuccessfulAuctionSessionBeanLocal successfulAuctionSessionBeanLocal;

    @EJB
    private AuctionSessionBeanLocal auctionSessionBeanLocal;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    
    @PersistenceContext(unitName = "CrazyBidsApplication-ejbPU")
    private EntityManager em;

    /**
     * This is a sample web service operation
     */
    
    public PremiumCustomerWebService() {
    }
    
    public Customer retrieveCustomerbyId(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerSessionBeanLocal.retrieveCustomerbyId(customerId);
        
        detachCustomer(customer);
      
        return customer;
    }
    
    public Customer retrieveCustomerbyUsername(String username) throws CustomerNotFoundException {
        Customer customer = customerSessionBeanLocal.retrieveCustomerbyUsername(username);
        
        detachCustomer(customer);
        
        return customer;
    }

    public Customer customerLogin(String username, String password) throws InvalidLoginCredentialException, CustomerNotFoundException, UpdateCustomerException {
        Customer customer = customerSessionBeanLocal.customerLogin(username, password);
        customer.setIsLogin(true);
        updatePremiumCustomer(customer);
        em.flush();
        
        customer = customerSessionBeanLocal.retrieveCustomerbyId(customer.getId());
        
        detachCustomer(customer);
        
        return customer;
    }
    
    public void customerLogout(Long customerId) throws CustomerNotFoundException {
        customerSessionBeanLocal.customerLogout(customerId);
        em.flush();
    }
   
    public Auction retrieveAuctionbyId(Long auctionId) throws AuctionNotFoundException {
        Auction auction = auctionSessionBeanLocal.retrieveAuctionbyId(auctionId);
        
        detachAuction(auction);
        
        return auction;
    }


    public List<Auction> retrieveAllAuctions() {
        List<Auction> auctions = auctionSessionBeanLocal.retrieveAllAuctions();
        
        for (Auction auction : auctions) {
            detachAuction(auction);
        }
        
        return auctions;
    }
    
   
    public List<Auction> retrieveAllActiveAuctions() {
        List<Auction> auctions = auctionSessionBeanLocal.retrieveAllActiveAuctions();
        
        for (Auction auction : auctions) {
            detachAuction(auction);
        }
        return auctions;
    }
    
    public SuccessfulAuction retrieveSuccessfulAuctionbyId(Long successfulAuctionId) throws SuccessfulAuctionNotFoundException {
        SuccessfulAuction successfulAuction = successfulAuctionSessionBeanLocal.retrieveSuccessfulAuctionbyId(successfulAuctionId);
        
        detachSuccessfulAuction(successfulAuction);
        
        return successfulAuction;
    }
    
    public List<SuccessfulAuction> retrieveAllSuccessfulAuctions() {
        List<SuccessfulAuction> successfulAuctions = successfulAuctionSessionBeanLocal.retrieveAllSuccessfulAuctions();
        
        for (SuccessfulAuction successfulAuction : successfulAuctions) {
            detachSuccessfulAuction(successfulAuction);
        }
        
        return successfulAuctions;
    }
    
   
    public List<SuccessfulAuction> retrieveAllSuccessfulAuctionByCustomerId(Long customerId) throws CustomerNotFoundException {
        List<SuccessfulAuction> successfulAuctions = successfulAuctionSessionBeanLocal.retrieveAllSuccessfulAuctionByCustomerId(customerId);
        
        for (SuccessfulAuction successfulAuction : successfulAuctions) {
            detachSuccessfulAuction(successfulAuction);
        }
        
        return successfulAuctions;
    }
    
    public void updatePremiumCustomer(Customer updatedCustomer) throws CustomerNotFoundException, UpdateCustomerException {
        if (updatedCustomer != null && updatedCustomer.getId() != null) {
            Customer customerToUpdate = customerSessionBeanLocal.retrieveCustomerbyId(updatedCustomer.getId());

            if (customerToUpdate.getUsername().equals(updatedCustomer.getUsername())) {
                customerToUpdate.setCustomerTierEnum(updatedCustomer.getCustomerTierEnum());
                customerToUpdate.setIsLogin(updatedCustomer.getIsLogin());
            } else {
                throw new UpdateCustomerException("Username of customer record to be updated does not match the existing record");
            }
        } else {
            throw new CustomerNotFoundException("Customer ID not provided for customer to be updated");
        }
    }
    
    public Bid getHighestBid(Auction auction) {
        List<Bid> bids = auction.getBids();
        Collections.sort(bids);
        Bid highestBid = bids.get(0);
        return highestBid;
    }
    
    public void proxyBidding(Auction auction, BigDecimal maxAmount, Customer customer) throws AuctionNotFoundException, CustomerNotFoundException  {
        Auction currAuction = auctionSessionBeanLocal.retrieveAuctionbyId(auction.getId());
        Customer premiumCustomer = customerSessionBeanLocal.retrieveCustomerbyId(customer.getId());
        
        ProxyBid proxyBid = new ProxyBid(maxAmount, new Date());
        
        //Set Relationship
        proxyBid.setAuction(auction);
        proxyBid.setCustomer(customer);
        currAuction.getProxyBids().add(proxyBid);
        premiumCustomer.getProxyBids().add(proxyBid);
        
        em.persist(proxyBid);
    }
    
    public void sniping(Auction auction, Date snipeDateTime, Customer customer) throws AuctionNotFoundException, CustomerNotFoundException {
        Auction currAuction = auctionSessionBeanLocal.retrieveAuctionbyId(auction.getId());
        Customer premiumCustomer = customerSessionBeanLocal.retrieveCustomerbyId(customer.getId());
        
        Snipe snipe = new Snipe(new Date(), snipeDateTime);
        
        //Set Relationship
        snipe.setAuction(auction);
        snipe.setCustomer(customer);
        currAuction.getSnipes().add(snipe);
        premiumCustomer.getSnipes().add(snipe);
        
        em.persist(snipe);
    }
    
    public void placeABid(Long auctionId, Long customerId, BigDecimal bidAmount) throws CustomerNotFoundException, AuctionNotFoundException, NotEnoughCreditException, InvalidBidException, BidNotFoundException {
        customerSessionBeanLocal.placeABid(auctionId, customerId, bidAmount);
    }
    
    public BigDecimal bidConverter(Auction auction) {
        BigDecimal bidConverter = auctionSessionBeanLocal.bidConverter(auction);
        return bidConverter;
    }
    
    public void detachCustomer(Customer customer) {
        em.detach(customer);
        
        customer.getAddresses().clear();
        customer.getBids().clear();
        customer.getCreditTransactions().clear();
        customer.getProxyBids().clear();
        customer.getSnipes().clear();
        customer.getSuccessfulAuctions().clear(); 
        
    }

    public void detachAuction(Auction auction) {
        em.detach(auction);
        
        auction.setSuccessfulAuction(null);
        auction.getSnipes().clear();
        auction.getProxyBids().clear();
                
        for (Bid bid : auction.getBids()) {
            em.detach(bid);
            //bid.setCustomer(null);
            detachCustomer(bid.getCustomer());
            bid.setBidTransaction(null);
            bid.setRefundTransaction(null);
            bid.setAuction(null);
        }
        
        for (Snipe snipe : auction.getSnipes()) {
            em.detach(snipe);
            snipe.setCustomer(null);
            snipe.setAuction(null);
        }
         
        for (ProxyBid proxyBid : auction.getProxyBids()) {
            em.detach(proxyBid);
            proxyBid.setCustomer(null);
            proxyBid.setAuction(null);
        }
        
    }
    
    public void detachSuccessfulAuction(SuccessfulAuction successfulAuction) {
        em.detach(successfulAuction);
        
        successfulAuction.setAddress(null);
        successfulAuction.setCustomer(null);
        successfulAuction.setAuction(null);
        
    } 

}
