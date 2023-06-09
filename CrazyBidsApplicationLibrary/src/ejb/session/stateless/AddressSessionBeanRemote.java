/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Address;
import entity.Auction;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AddressIsAssociatedWithWinningBidException;
import util.exception.AddressIsDisabledException;
import util.exception.AddressNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.UpdateAddressException;

/**
 *
 * @author Bransome Tan Yi Hao
 */
@Remote
public interface AddressSessionBeanRemote {
    
    Address retrieveAddressbyId(Long addressId) throws AddressNotFoundException;

    List<Address> retrieveAllAddressesByCustomerId(Long customerId) throws CustomerNotFoundException;
    
    void updateAddress(Address updatedAddress) throws AddressNotFoundException, UpdateAddressException, InputDataValidationException;

    Long createAddress(Address newAddress, Long customerId) throws GeneralException, CustomerNotFoundException, InputDataValidationException;

    void disableAddress(Long addressId) throws AddressNotFoundException, AddressIsDisabledException;
    
    void deleteAddress(Long addressId, Long customerId) throws AddressNotFoundException, AddressIsAssociatedWithWinningBidException, CustomerNotFoundException;
}
