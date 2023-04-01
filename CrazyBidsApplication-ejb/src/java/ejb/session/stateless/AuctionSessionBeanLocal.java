/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Auction;
import java.util.List;
import javax.ejb.Local;
import util.exception.AuctionHasBidsException;
import util.exception.AuctionIsDisabledException;
import util.exception.AuctionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateAuctionException;

/**
 *
 * @author jamielee
 */
@Local
public interface AuctionSessionBeanLocal {

    Auction retrieveAuctionbyId(Long auctionId) throws AuctionNotFoundException;

    List<Auction> retrieveAllAuctions();

    Long createAuction(Auction newAuction) throws UnknownPersistenceException;

    void updateAuction(Auction updatedAuction) throws AuctionNotFoundException, UpdateAuctionException;

    void deleteAuction(Long auctionId) throws AuctionNotFoundException, AuctionHasBidsException;

    public void disableAuction(Long auctionId) throws AuctionNotFoundException, AuctionIsDisabledException;

    public List<Auction> retrieveAllAuctionsWithBidsBelowReservePrice();
    
}
