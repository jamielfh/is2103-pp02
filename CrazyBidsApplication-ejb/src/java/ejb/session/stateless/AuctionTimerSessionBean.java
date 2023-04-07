/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Auction;
import entity.Bid;
import entity.Customer;
import entity.SuccessfulAuction;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AuctionNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author jamielee
 */
@Stateless
public class AuctionTimerSessionBean implements AuctionTimerSessionBeanRemote, AuctionTimerSessionBeanLocal {

    @EJB
    private AuctionSessionBeanLocal auctionSessionBeanLocal;

    @EJB
    private SuccessfulAuctionSessionBeanLocal successfulAuctionSessionBeanLocal;

    @PersistenceContext(unitName = "CrazyBidsApplication-ejbPU")
    private EntityManager em;

    public AuctionTimerSessionBean() {
    }

    @Override
    @Schedule(hour = "*", minute = "*", info = "checkForClosedAuctionsTimer")
    public void checkForClosedAuctionsTimer() {
        Query query = em.createQuery("SELECT a from Auction a WHERE a.endDateTime < :currentDateTime AND NOT a.getBids IS EMPTY AND a.successfulAuction = null AND a.manualIntervention = false AND a.assignedNoWinner = false");
        Date currentDateTime = new Date();
        query.setParameter("currentDateTime", currentDateTime);
        List<Auction> auctions = query.getResultList();
        
        for (Auction auction : auctions) 
        {
            Bid highestBid = auctionSessionBeanLocal.getHighestBid(auction);
            
            if (auction.getReservePrice() != null)
            {
                if (highestBid.getBidAmount().compareTo(auction.getReservePrice()) < 1)
                {
                    auction.setManualIntervention(true);
                    continue;
                }
            }
            
            SuccessfulAuction successfulAuction = new SuccessfulAuction();
            successfulAuction.setSuccessfulAuctionName(auction.getName());
            successfulAuction.setSuccessfulAuctionDetails(auction.getDetails());
            Customer winner = highestBid.getCustomer();
            
            try
            {
                Long successfulAuctionId = successfulAuctionSessionBeanLocal.createNewSuccessfulAuction(successfulAuction, winner.getId(), auction.getId());
                System.out.println("Successful auction with ID: " + successfulAuctionId + " created successfully!");
            }
            catch(GeneralException | CustomerNotFoundException | AuctionNotFoundException ex)
            {
                System.out.println("An error occurred while creating successful auction: " + ex.getMessage());
            }
            
        }
    }
    
}
