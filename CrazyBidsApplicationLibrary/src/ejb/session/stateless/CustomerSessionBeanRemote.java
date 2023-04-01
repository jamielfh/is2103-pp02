/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditTransaction;
import entity.Customer;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CreditPackageNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author jamielee
 */
@Remote
public interface CustomerSessionBeanRemote {
    Customer retrieveCustomerbyId(Long customerId) throws CustomerNotFoundException;

    Customer retrieveCustomerbyUsername(String username) throws CustomerNotFoundException;

    Customer customerLogin(String username, String password) throws InvalidLoginCredentialException;

    List<Customer> retrieveAllCustomers();

    Long createNewCustomer(Customer newCustomer) throws UnknownPersistenceException;

    void updateCustomer(Customer customer) throws CustomerNotFoundException, UpdateCustomerException;
    
    List<CreditTransaction> retrieveAllCreditTransactionByCustomerId(Long customerId) throws CustomerNotFoundException;
    
    void purchaseCreditPackage(Long creditPackageId, Long customerId, Long quantity) throws CustomerNotFoundException, CreditPackageNotFoundException;
}
