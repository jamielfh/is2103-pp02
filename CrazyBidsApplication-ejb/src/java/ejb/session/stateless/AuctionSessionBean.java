/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Auction;
import entity.Bid;
import entity.CreditTransaction;
import entity.Customer;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.enumeration.CreditTransactionEnum;
import util.exception.AuctionAssignedNoWinnerException;
import util.exception.AuctionHasBidsException;
import util.exception.AuctionIsDisabledException;
import util.exception.AuctionNotFoundException;
import util.exception.BidNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidBidException;
import util.exception.UpdateAuctionException;

/**
 *
 * @author jamielee
 */
@Stateless
public class AuctionSessionBean implements AuctionSessionBeanRemote, AuctionSessionBeanLocal {

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName = "CrazyBidsApplication-ejbPU")
    private EntityManager em;

    public AuctionSessionBean() {
    }

    @Override
    public Auction retrieveAuctionbyId(Long auctionId) throws AuctionNotFoundException {
        Auction auction = em.find(Auction.class, auctionId);
        if (auction != null) {
            auction.getBids().size();
            return auction;
        } else {
            throw new AuctionNotFoundException("Auction ID " + auctionId + " not found in system!");
        }
    }

    @Override
    public List<Auction> retrieveAllAuctions() {
        Query query = em.createQuery("SELECT a from Auction a ORDER BY a.startDateTime ASC");
        return query.getResultList();
    }
    
    @Override
    public List<Auction> retrieveAllActiveAuctions() {
        Query query = em.createQuery("SELECT a from Auction a WHERE a.startDateTime <= :inStartDate AND a.endDateTime >= :inEndDate AND a.isDisabled = false ORDER BY a.startDateTime ASC");
        Date currentDate = new Date();
        query.setParameter("inStartDate", currentDate);
        query.setParameter("inEndDate", currentDate);
        return query.getResultList();
    }
    
    @Override
    public List<Auction> retrieveAllAuctionsWithBidsBelowReservePrice() {
        Query query = em.createQuery("SELECT a FROM Auction a WHERE a.manualIntervention = true ORDER BY a.endDateTime ASC");
        return query.getResultList();
    }

    @Override
    public Long createAuction(Auction newAuction) throws GeneralException {
        try {
            em.persist(newAuction);
            em.flush();
            
            return newAuction.getId();
        } catch (PersistenceException ex) {
            throw new GeneralException(ex.getMessage());
        }
    }

    @Override
    public void updateAuction(Auction updatedAuction) throws AuctionNotFoundException, UpdateAuctionException {
         
        if(updatedAuction != null && updatedAuction.getId() != null)
        {
            Auction auctionToUpdate = retrieveAuctionbyId(updatedAuction.getId());
            
            
                // Only allowed to update the general details of auction in this business method.
                auctionToUpdate.setName(updatedAuction.getName());
                auctionToUpdate.setDetails(updatedAuction.getDetails());
                
                // Not allowed to change startDateTime, endDateTime, startingBid and reservePrice if a customer has already bidded.
                if(auctionToUpdate.getBids().isEmpty())
                {
                    auctionToUpdate.setStartDateTime(updatedAuction.getStartDateTime());
                    auctionToUpdate.setEndDateTime(updatedAuction.getEndDateTime());
                    auctionToUpdate.setStartingBid(updatedAuction.getStartingBid());
                    auctionToUpdate.setReservePrice(updatedAuction.getReservePrice());
                }
                
                //auctionToUpdate.setIsDisabled(updatedAuction.getIsDisabled());
                //auctionToUpdate.setManualIntervention(updatedAuction.getManualIntervention());
                
                // ManualIntervention is set automatically when highest bid is the same as or below the reserve price (when timer runs out).
                // isDisabled is set under disableAuction.
            
            
        }
        else
        {
            throw new AuctionNotFoundException("Auction ID not provided for auction to be updated");
        }
    }

    @Override
    public void deleteAuction(Long auctionId) throws AuctionNotFoundException, AuctionHasBidsException {
        Auction auction = retrieveAuctionbyId(auctionId);
     
        if (auction.getBids().isEmpty()) {
            em.remove(auction);
        } else {
            // if auction already have bids, throw an error to tell users they cannot delete the auction
            throw new AuctionHasBidsException("Auction has existing bids and you are not allowed to delete it. You can opt to disable it.");
        }
    }
    
    @Override
    public void disableAuction(Long auctionId) throws AuctionNotFoundException, AuctionIsDisabledException, BidNotFoundException, CustomerNotFoundException {
        Auction auction = retrieveAuctionbyId(auctionId);
        
        if (!auction.getIsDisabled()) {
            // refund credit to customer with highest bid (other bids should have been refunded already)
            Bid highestBid = getHighestBid(auction);
            doRefund(highestBid);
            
            auction.setIsDisabled(true);
            
        } else {
             throw new AuctionIsDisabledException("Auction is already disabled.");
        }
     
    }
    
    @Override
    public void assignNoWinner(Long auctionId) throws AuctionNotFoundException, AuctionAssignedNoWinnerException {
        Auction auction = retrieveAuctionbyId(auctionId);
        
        if (auction.getAssignedNoWinner() == true)
        {
            throw new AuctionAssignedNoWinnerException("Auction has already been marked as having no winning bid!");
        }
        else
        {
            auction.setAssignedNoWinner(true);
            auction.setManualIntervention(false);
        }
    }
    
    @Override
    public Bid getHighestBid(Auction auction) {
        List<Bid> bids = auction.getBids();
        Collections.sort(bids);
        Bid highestBid = bids.get(0);
        return highestBid;
    }
    
    public void doRefund(Bid bid) throws BidNotFoundException, CustomerNotFoundException
    {
        
        Bid refundBid = em.find(Bid.class, bid.getId());
        
        if (bid == null) {
           throw new BidNotFoundException("Bid ID " + bid.getId() + " not found in system!");
        }     
        
        Customer customer = customerSessionBeanLocal.retrieveCustomerbyId(refundBid.getCustomer().getId());
        
        BigDecimal bidAmount = refundBid.getBidAmount();

        Date currentDate = new Date();

        CreditTransaction newRefundTransaction = new CreditTransaction(bidAmount, CreditTransactionEnum.REFUND, currentDate);
        newRefundTransaction.setBid(refundBid);
        bid.setRefundTransaction(newRefundTransaction);

        BigDecimal customerBalance = customer.getCreditBalance();
        customer.setCreditBalance(customerBalance.add(bidAmount));

        newRefundTransaction.setCustomer(customer);
        List<CreditTransaction> customerTransactions = customer.getCreditTransactions();
        customerTransactions.add(newRefundTransaction);
        customer.setCreditTransactions(customerTransactions);
        
        em.persist(newRefundTransaction);
    }
    
    @Override
    public BigDecimal bidConverter(Auction auction) {
        // Bid Converter
        BigDecimal bidAmount;
        BigDecimal bidIncrement;
        
        if (auction.getBids().isEmpty()) {
            // if no bids yet, get starting bids
            bidAmount = auction.getStartingBid();
        } else {
            // if there are bids, get the highest bidding price
            List<Bid> bids = auction.getBids();
            Collections.sort(bids);
            bidAmount = bids.get(0).getBidAmount();
        }
        
        if (bidAmount.doubleValue() >= 0.01 && bidAmount.doubleValue() <= 0.99) {
            bidIncrement = new BigDecimal(0.05);
        } else if (bidAmount.doubleValue() >= 1.00 && bidAmount.doubleValue() <= 4.99) {
            bidIncrement = new BigDecimal(0.25);
        } else if (bidAmount.doubleValue() >= 5.00 && bidAmount.doubleValue() <= 24.99) {
            bidIncrement = new BigDecimal(0.50);
        } else if (bidAmount.doubleValue() >= 25.00 && bidAmount.doubleValue() <= 99.99) {
            bidIncrement = new BigDecimal(1.00);
        } else if (bidAmount.doubleValue() >= 100.00 && bidAmount.doubleValue() <= 249.99) {
            bidIncrement = new BigDecimal(2.50);
        } else if (bidAmount.doubleValue() >= 250.00 && bidAmount.doubleValue() <= 499.99) {
            bidIncrement = new BigDecimal(5.00);
        } else if (bidAmount.doubleValue() >= 500.00 && bidAmount.doubleValue() <= 999.99) {
            bidIncrement = new BigDecimal(10.00);
        } else if (bidAmount.doubleValue() >= 1000 && bidAmount.doubleValue() <= 2499.99) {
            bidIncrement = new BigDecimal(25.00);
        } else if (bidAmount.doubleValue() >= 2500 && bidAmount.doubleValue() <= 4999.99) {
            bidIncrement = new BigDecimal(50.00);
        } else {
            bidIncrement = new BigDecimal(100.00);
        } 
        
        return bidIncrement;
    }

}
