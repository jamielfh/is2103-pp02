/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Address;
import java.util.List;
import javax.ejb.Local;
import util.exception.AddressIsAssociatedWithWinningBidException;
import util.exception.AddressIsDisabledException;
import util.exception.AddressNotFoundException;
import util.exception.AuctionIsDisabledException;
import util.exception.CustomerNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateAddressException;

/**
 *
 * @author Bransome Tan Yi Hao
 */
@Local
public interface AddressSessionBeanLocal {

    Address retrieveAddressbyId(Long addressId) throws AddressNotFoundException;
    
    List<Address> retrieveAllAddressesByCustomerId(Long customerId) throws CustomerNotFoundException;

    void updateAddress(Address updatedAddress) throws AddressNotFoundException, UpdateAddressException;

    Long createAddress(Address newAddress, Long customerId) throws UnknownPersistenceException, CustomerNotFoundException;

    void disableAddress(Long addressId) throws AddressNotFoundException, AddressIsDisabledException;

    void deleteAddress(Long addressId, Long customerId) throws AddressNotFoundException, AddressIsAssociatedWithWinningBidException, CustomerNotFoundException;
    
}
