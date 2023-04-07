/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Auction;
import entity.Bid;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AuctionAssignedNoWinnerException;
import util.exception.AuctionHasBidsException;
import util.exception.AuctionIsDisabledException;
import util.exception.AuctionNotFoundException;
import util.exception.BidNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.UpdateAuctionException;

/**
 *
 * @author jamielee
 */
@Remote
public interface AuctionSessionBeanRemote {

    Auction retrieveAuctionbyId(Long auctionId) throws AuctionNotFoundException;

    List<Auction> retrieveAllAuctions();

    Long createAuction(Auction newAuction) throws GeneralException;

    void updateAuction(Auction updatedAuction) throws AuctionNotFoundException, UpdateAuctionException;

    void deleteAuction(Long auctionId) throws AuctionNotFoundException, AuctionHasBidsException;

    void disableAuction(Long auctionId) throws AuctionNotFoundException, AuctionIsDisabledException, BidNotFoundException, CustomerNotFoundException;
    
    List<Auction> retrieveAllAuctionsWithBidsBelowReservePrice();
    
    List<Auction> retrieveAllActiveAuctions();

    Bid getHighestBid(Auction auction);

    void assignNoWinner(Long auctionId) throws AuctionNotFoundException, AuctionAssignedNoWinnerException;
    
    void doRefund(Bid bid) throws BidNotFoundException, CustomerNotFoundException;
    
    public BigDecimal bidConverter(Auction auction);
    
}
