/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Auction;
import entity.Bid;
import entity.Customer;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AuctionHasBidsException;
import util.exception.AuctionIsDisabledException;
import util.exception.AuctionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateAuctionException;

/**
 *
 * @author jamielee
 */
@Stateless
public class AuctionSessionBean implements AuctionSessionBeanRemote, AuctionSessionBeanLocal {

    @PersistenceContext(unitName = "CrazyBidsApplication-ejbPU")
    private EntityManager em;

    public AuctionSessionBean() {
    }

    @Override
    public Auction retrieveAuctionbyId(Long auctionId) throws AuctionNotFoundException {
        Auction auction = em.find(Auction.class, auctionId);
        if (auction != null) {
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
        // Not sure if this is correct but can try out
        Query query = em.createQuery("SELECT a FROM Auction a JOIN a.bids b WHERE b.bidAmount < a.reservePrice ORDER BY a.startDateTime ASC");
        return query.getResultList();
    }
    

    @Override
    public Long createAuction(Auction newAuction) throws UnknownPersistenceException {
        try {
            em.persist(newAuction);
            em.flush();
            
            return newAuction.getId();
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public void updateAuction(Auction updatedAuction) throws AuctionNotFoundException, UpdateAuctionException {
         
        if(updatedAuction != null && updatedAuction.getId() != null)
        {
            Auction auctionToUpdate = retrieveAuctionbyId(updatedAuction.getId());
            
            if(auctionToUpdate.getName().equals(updatedAuction.getName()))
            {
                auctionToUpdate.setName(updatedAuction.getName());
                auctionToUpdate.setDetails(updatedAuction.getDetails());
                auctionToUpdate.setReservePrice(updatedAuction.getReservePrice());
                //auctionToUpdate.setStartDateTime(updatedAuction.getStartDateTime());
                //auctionToUpdate.setEndDateTime(updatedAuction.getEndDateTime());
                //auctionToUpdate.setStartingBid(updatedAuction.getStartingBid());
                //auctionToUpdate.setIsDisabled(updatedAuction.getIsDisabled());
                //auctionToUpdate.setManualIntervention(updatedAuction.getManualIntervention());
                
                // Only allow to update the general details of auction in this business method.
                // Not allowed to change starting bid and startDateTime if a customer has already bidded?
                // ManualIntervention is set automatically when highest bid is the same as or below the reserve price (when timer runs out)
                // isDisabled is set under disabledAuction
            }
            else
            {
                throw new UpdateAuctionException("Name of auction record to be updated does not match the existing record");
            }
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
            throw new AuctionHasBidsException("Auction have existing bids and you are not allowed to delete it. You can opt to disable it.");
        }
    }
    
    @Override
    public void disableAuction(Long auctionId) throws AuctionNotFoundException, AuctionIsDisabledException{
        Auction auction = retrieveAuctionbyId(auctionId);
        
        if (!auction.getIsDisabled()) {
             // if active listing is disabled, refund credits to all customers
            // Create new credit transactions with enum type : REFUND for each customer
            auction.setIsDisabled(true);
        } else {
             throw new AuctionIsDisabledException("Auction is already disabled.");
        }
     
    }
    
}
