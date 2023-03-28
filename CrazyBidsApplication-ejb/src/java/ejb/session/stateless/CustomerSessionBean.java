/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author jamielee
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "CrazyBidsApplication-ejbPU")
    private EntityManager em;

    public CustomerSessionBean() {
    }
    
    @Override
    public Customer retrieveCustomerbyId(Long customerId) throws CustomerNotFoundException {
        Customer customer = em.find(Customer.class, customerId);
        if (customer != null) {
            return customer;
        } else {
            throw new CustomerNotFoundException("Customer ID " + customerId + " not found in system!");
        }
    }

    @Override
    public Customer retrieveCustomerbyUsername(String username) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.username = :inputUsername");
        query.setParameter("inputUsername", username);
        try {
            return (Customer) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer username " + username + " not found in system!");
        }
    }
    
    @Override
    public Customer customerLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            Customer customer = retrieveCustomerbyUsername(username);

            if (customer.getPassword().equals(password)) {
                return customer;
            } else {
                throw new InvalidLoginCredentialException("Password is incorrect, please try again!");
            }
        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException(ex.getMessage());
        } catch (InvalidLoginCredentialException ex) {
            throw ex;
        }
    }

    @Override
    public List<Customer> retrieveAllCustomers() {
        Query query = em.createQuery("SELECT c FROM Customer c");

        return query.getResultList();
    }

    @Override
    public Long createNewCustomer(Customer newCustomer) throws UnknownPersistenceException {
        try {
            em.persist(newCustomer);
            em.flush();

            return newCustomer.getId();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new UnknownPersistenceException("Customer with username " + newCustomer.getUsername() + " already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public void updateCustomer(Customer updatedCustomer) throws CustomerNotFoundException, UpdateCustomerException
    {
        if(updatedCustomer != null && updatedCustomer.getId() != null)
        {
            Customer customerToUpdate = retrieveCustomerbyId(updatedCustomer.getId());
            
            if(customerToUpdate.getUsername().equals(updatedCustomer.getUsername()))
            {
                customerToUpdate.setFirstName(updatedCustomer.getFirstName());
                customerToUpdate.setLastName(updatedCustomer.getLastName());
                customerToUpdate.setEmail(updatedCustomer.getEmail());
                customerToUpdate.setMobileNumber(updatedCustomer.getMobileNumber());
                customerToUpdate.setPassword(updatedCustomer.getPassword());
                
                // Username and Credit Balance are deliberately NOT updated to demonstrate that client is not allowed to update account credential through this business method
                // Credit balance is updated when customer buy credit packages or bid in auction or refunded from auction
            }
            else
            {
                throw new UpdateCustomerException("Username of customer record to be updated does not match the existing record");
            }
        }
        else
        {
            throw new CustomerNotFoundException("Customer ID not provided for customer to be updated");
        }
    }
    
    
}
