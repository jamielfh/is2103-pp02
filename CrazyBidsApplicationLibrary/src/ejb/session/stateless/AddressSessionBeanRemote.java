/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Address;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AddressIsAssociatedWithWinningBidException;
import util.exception.AddressIsDisabledException;
import util.exception.AddressNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateAddressException;

/**
 *
 * @author Bransome Tan Yi Hao
 */
@Remote
public interface AddressSessionBeanRemote {
    void deleteAddress(Long addressId) throws AddressNotFoundException, AddressIsAssociatedWithWinningBidException;

    Address retrieveAddressbyId(Long addressId) throws AddressNotFoundException;

    List<Address> retrieveAllAddresses();

    void updateAddress(Address updatedAddress) throws AddressNotFoundException, UpdateAddressException;

    Long createAddress(Address newAddress) throws UnknownPersistenceException;
    
    void disableAddress(Long addressId) throws AddressNotFoundException, AddressIsDisabledException;
}
