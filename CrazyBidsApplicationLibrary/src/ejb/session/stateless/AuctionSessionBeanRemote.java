/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Auction;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AuctionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author jamielee
 */
@Remote
public interface AuctionSessionBeanRemote {

    Auction retrieveAuctionbyId(Long auctionId) throws AuctionNotFoundException;

    List<Auction> retrieveAllAuctions();

    Long createAuction(Auction newAuction) throws UnknownPersistenceException;

    void updateAuction(Auction updatedAuction) throws AuctionNotFoundException;

    void deleteAuction(Long auctionId) throws AuctionNotFoundException;
    
}
