/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.SuccessfulAuction;
import java.util.List;
import javax.ejb.Local;
import util.exception.AddressNotFoundException;
import util.exception.DeliveryAddressExistException;
import util.exception.SuccessfulAuctionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateDeliveryAddressException;

/**
 *
 * @author Bransome Tan Yi Hao
 */
@Local
public interface SuccessfulAuctionSessionBeanLocal {

    SuccessfulAuction retrieveSuccessfulAuctionbyId(Long successfulAuctionId) throws SuccessfulAuctionNotFoundException;

    List<SuccessfulAuction> retrieveAllSuccessfulAuction();

    Long createNewSuccessfulAuction(SuccessfulAuction newSuccessfulAuction) throws UnknownPersistenceException;

    void updateDeliveryAddress(Long successfulAuctionId, Long addressId) throws DeliveryAddressExistException, UpdateDeliveryAddressException, SuccessfulAuctionNotFoundException, AddressNotFoundException;
    
}
