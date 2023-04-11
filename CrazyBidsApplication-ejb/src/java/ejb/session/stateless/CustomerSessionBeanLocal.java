/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditTransaction;
import entity.Customer;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import util.exception.AuctionNotFoundException;
import util.exception.BidNotFoundException;
import util.exception.CreditPackageNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.GeneralException;
import util.exception.InvalidBidException;
import util.exception.NotEnoughCreditException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author jamielee
 */
@Local
public interface CustomerSessionBeanLocal {

    Customer retrieveCustomerbyId(Long customerId) throws CustomerNotFoundException;

    Customer retrieveCustomerbyUsername(String username) throws CustomerNotFoundException;

    Customer customerLogin(String username, String password) throws InvalidLoginCredentialException;

    List<Customer> retrieveAllCustomers();

    Long createNewCustomer(Customer newCustomer) throws GeneralException;

    void updateCustomer(Customer customer) throws CustomerNotFoundException, UpdateCustomerException;

    List<CreditTransaction> retrieveAllCreditTransactionByCustomerId(Long customerId) throws CustomerNotFoundException;

    void purchaseCreditPackage(Long creditPackageId, Long customerId, Long quantity) throws CustomerNotFoundException, CreditPackageNotFoundException;

    void placeABid(Long auctionId, Long customerId, BigDecimal bidAmount) throws CustomerNotFoundException, AuctionNotFoundException, NotEnoughCreditException, InvalidBidException, BidNotFoundException;

    void customerLogout(Long customerId) throws CustomerNotFoundException;
    
}
