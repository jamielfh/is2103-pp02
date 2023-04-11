/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Auction;
import entity.Bid;
import entity.Customer;
import entity.Snipe;
import entity.SuccessfulAuction;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AuctionNotFoundException;
import util.exception.BidNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidBidException;
import util.exception.NotEnoughCreditException;

/**
 *
 * @author jamielee
 */
@Stateless
public class AuctionTimerSessionBean implements AuctionTimerSessionBeanRemote, AuctionTimerSessionBeanLocal {

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private AuctionSessionBeanLocal auctionSessionBeanLocal;

    @EJB
    private SuccessfulAuctionSessionBeanLocal successfulAuctionSessionBeanLocal;

    @PersistenceContext(unitName = "CrazyBidsApplication-ejbPU")
    private EntityManager em;

    public AuctionTimerSessionBean() {
    }

    @Override
    @Schedule(hour = "*", minute = "*", info = "CloseAuctionsTimer")
    public void checkForClosedAuctionsTimer() {
        Query query = em.createQuery("SELECT a from Auction a WHERE a.endDateTime <= :currentDateTime AND a.isClosed = false AND a.manualIntervention = false AND a.isDisabled = false");
        Date currentDateTime = new Date();
        query.setParameter("currentDateTime", currentDateTime);
        List<Auction> auctions = query.getResultList();
        
        for (Auction auction : auctions) 
        {
            if (auction.getBids().isEmpty())
            {
                auction.setIsClosed(true);
                continue;
            }
            
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
            successfulAuction.setSuccessfulAuctionDeliveryAddress("-");
            Customer winner = highestBid.getCustomer();
            
            try
            {
                Long successfulAuctionId = successfulAuctionSessionBeanLocal.createNewSuccessfulAuction(successfulAuction, winner.getId(), auction.getId());
                auction.setIsClosed(true);
                auction.setSuccessfulAuction(successfulAuction);
                System.out.println("Successful auction with ID: " + successfulAuctionId + " created successfully!");
            }
            catch(GeneralException | CustomerNotFoundException | AuctionNotFoundException ex)
            {
                System.out.println("An error occurred while creating successful auction: " + ex.getMessage());
            }
            
        }
    }

    @Override
    @Schedule(hour = "*", minute = "*", info = "CheckForSnipesTimer")
    public void checkForSnipeTimer() {
        Query query = em.createQuery("SELECT s from Snipe s WHERE s.snipeDateTime >= :startWindow AND s.snipeDateTime <= :endWindow");
        Date currentDateTime = new Date();
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDateTime);
        calendar.add(Calendar.SECOND, -1);
        Date bufferDateTime = calendar.getTime();
        
        query.setParameter("startWindow", bufferDateTime);
        query.setParameter("endWindow", currentDateTime);
        List<Snipe> snipes = query.getResultList();
        
        for (Snipe snipe : snipes)
        {
            Customer customer = snipe.getCustomer();
            
            if (customer.getIsLogin() == false)
            {
                System.out.println("Customer is not logged into the system!");
                continue;
            }
            
            Auction auction = snipe.getAuction();
            BigDecimal nextBidIncrement = auctionSessionBeanLocal.bidConverter(auction);
            BigDecimal snipeBidAmount;
            
            if (auction.getBids().isEmpty()) {
                // if no bids yet, use starting bid
                snipeBidAmount = auction.getStartingBid().add(nextBidIncrement);
            } else {
                // if there are bids, use highest bidding price
                snipeBidAmount = auctionSessionBeanLocal.getHighestBid(auction).getBidAmount().add(nextBidIncrement);
            }       
            
            try
            {
                customerSessionBeanLocal.placeABid(auction.getId(), customer.getId(), snipeBidAmount);
            }
            catch (AuctionNotFoundException | BidNotFoundException | CustomerNotFoundException | InvalidBidException | NotEnoughCreditException ex)
            {
                System.out.println("\nAn error occurring while placing bid for snipe: " + ex.getMessage() + "\n");
            }
        }
    }
}
