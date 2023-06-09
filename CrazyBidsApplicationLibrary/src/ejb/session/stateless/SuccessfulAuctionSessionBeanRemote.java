/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.SuccessfulAuction;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AddressIsDisabledException;
import util.exception.AddressNotFoundException;
import util.exception.AuctionNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.DeliveryAddressExistException;
import util.exception.SuccessfulAuctionNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.UpdateDeliveryAddressException;

/**
 *
 * @author Bransome Tan Yi Hao
 */
@Remote
public interface SuccessfulAuctionSessionBeanRemote {
    SuccessfulAuction retrieveSuccessfulAuctionbyId(Long successfulAuctionId) throws SuccessfulAuctionNotFoundException;

    List<SuccessfulAuction> retrieveAllSuccessfulAuctions();

    Long createNewSuccessfulAuction(SuccessfulAuction newSuccessfulAuction, Long customerId, Long auctionId) throws GeneralException, CustomerNotFoundException, AuctionNotFoundException, InputDataValidationException;

    void updateDeliveryAddress(Long successfulAuctionId, Long addressId) throws DeliveryAddressExistException, UpdateDeliveryAddressException, SuccessfulAuctionNotFoundException, AddressNotFoundException, AddressIsDisabledException;
    
    List<SuccessfulAuction> retrieveAllSuccessfulAuctionByCustomerId(Long customerId) throws CustomerNotFoundException;
}
