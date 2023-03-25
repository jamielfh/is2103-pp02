/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Auction;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AuctionNotFoundException;
import util.exception.UnknownPersistenceException;

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
        Query query = em.createQuery("SELECT a from Auction a ORDER BY a.auctionStartDateTime ASC");
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
    public void updateAuction(Auction updatedAuction) throws AuctionNotFoundException {
        Auction auction = retrieveAuctionbyId(updatedAuction.getId());
        auction.setName(updatedAuction.getName());
        auction.setDetails(updatedAuction.getDetails());
        auction.setStartDateTime(updatedAuction.getStartDateTime());
        auction.setEndDateTime(updatedAuction.getEndDateTime());
        auction.setStartingBid(updatedAuction.getStartingBid());
        auction.setReservePrice(updatedAuction.getReservePrice());
        auction.setIsDisabled(updatedAuction.getIsDisabled());
        auction.setManualIntervention(updatedAuction.getManualIntervention());
    }

    @Override
    public void deleteAuction(Long auctionId) throws AuctionNotFoundException {
        Auction auction = retrieveAuctionbyId(auctionId);
        
        if (auction.getBids().isEmpty()) {
            em.remove(auction);
        } else {
            // if active listing, refund credits to customers
            auction.setIsDisabled(true);
        }
    }
    
}
