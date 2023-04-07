/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Auction;
import entity.Bid;
import java.util.List;
import javax.ejb.Local;
import util.exception.AuctionAssignedNoWinnerException;
import util.exception.AuctionHasBidsException;
import util.exception.AuctionIsDisabledException;
import util.exception.AuctionNotFoundException;
import util.exception.GeneralException;
import util.exception.UpdateAuctionException;

/**
 *
 * @author jamielee
 */
@Local
public interface AuctionSessionBeanLocal {

    Auction retrieveAuctionbyId(Long auctionId) throws AuctionNotFoundException;

    List<Auction> retrieveAllAuctions();

    Long createAuction(Auction newAuction) throws GeneralException;

    void updateAuction(Auction updatedAuction) throws AuctionNotFoundException, UpdateAuctionException;

    void deleteAuction(Long auctionId) throws AuctionNotFoundException, AuctionHasBidsException;

    void disableAuction(Long auctionId) throws AuctionNotFoundException, AuctionIsDisabledException;

    List<Auction> retrieveAllAuctionsWithBidsBelowReservePrice();

    List<Auction> retrieveAllActiveAuctions();

    Bid getHighestBid(Auction auction);

    void assignNoWinner(Long auctionId) throws AuctionNotFoundException, AuctionAssignedNoWinnerException;
    
}
