/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Auction;
import entity.Bid;
import entity.CreditPackage;
import entity.CreditTransaction;
import entity.Customer;
import entity.ProxyBid;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CreditTransactionEnum;
import util.exception.AuctionNotFoundException;
import util.exception.BidNotFoundException;
import util.exception.CreditPackageNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.InvalidBidException;
import util.exception.NotEnoughCreditException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author jamielee
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @EJB
    private AuctionSessionBeanLocal auctionSessionBeanLocal;

    @EJB
    private CreditPackageSessionBeanLocal creditPackageSessionBeanLocal;

    @PersistenceContext(unitName = "CrazyBidsApplication-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CustomerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
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
    public void customerLogout(Long customerId) throws CustomerNotFoundException {
        Customer customer = retrieveCustomerbyId(customerId);
        customer.setIsLogin(false);
    }

    @Override
    public List<Customer> retrieveAllCustomers() {
        Query query = em.createQuery("SELECT c FROM Customer c");

        return query.getResultList();
    }

    @Override
    public Long createNewCustomer(Customer newCustomer) throws GeneralException, InputDataValidationException {
        Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(newCustomer);
        
        if(constraintViolations.isEmpty())
        {
            try {
                em.persist(newCustomer);
                em.flush();

                return newCustomer.getId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new GeneralException("Customer with username " + newCustomer.getUsername() + " already exists!");
                    } else {
                        throw new GeneralException(ex.getMessage());
                    }
                } else {
                    throw new GeneralException(ex.getMessage());
                }
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public void updateCustomer(Customer updatedCustomer) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException {
        Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(updatedCustomer);
        
        if(constraintViolations.isEmpty())
        {
            if (updatedCustomer != null && updatedCustomer.getId() != null) {
                Customer customerToUpdate = retrieveCustomerbyId(updatedCustomer.getId());

                if (customerToUpdate.getUsername().equals(updatedCustomer.getUsername())) {
                    customerToUpdate.setFirstName(updatedCustomer.getFirstName());
                    customerToUpdate.setLastName(updatedCustomer.getLastName());
                    customerToUpdate.setEmail(updatedCustomer.getEmail());
                    customerToUpdate.setMobileNumber(updatedCustomer.getMobileNumber());
                    customerToUpdate.setPassword(updatedCustomer.getPassword());

                    // Username and Credit Balance are deliberately NOT updated to demonstrate that client is not allowed to update account credential through this business method
                    // Credit balance is updated when customer buy credit packages or bid in auction or refunded from auction
                } else {
                    throw new UpdateCustomerException("Username of customer record to be updated does not match the existing record");
                }
            } else {
                throw new CustomerNotFoundException("Customer ID not provided for customer to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<CreditTransaction> retrieveAllCreditTransactionByCustomerId(Long customerId) throws CustomerNotFoundException {
        Customer customer = retrieveCustomerbyId(customerId);
        customer.getCreditTransactions().size();
        customer.getBids().size();
        List<CreditTransaction> customerCreditTransactions = customer.getCreditTransactions();
        return customerCreditTransactions;
    }

    @Override
    public void purchaseCreditPackage(Long creditPackageId, Long customerId, Long quantity) throws CustomerNotFoundException, CreditPackageNotFoundException {
        Customer customer = retrieveCustomerbyId(customerId);
        CreditPackage creditPackage = creditPackageSessionBeanLocal.retrieveCreditPackagebyId(creditPackageId);

        //update new credit balance immediately
        customer.setCreditBalance(customer.getCreditBalance().add(creditPackage.getCreditPackageAmount().multiply(new BigDecimal(quantity))));
        //create new credit transaction of purchase type
        CreditTransaction newCreditTransaction = new CreditTransaction(creditPackage.getCreditPackageAmount().multiply(new BigDecimal(quantity)), CreditTransactionEnum.PURCHASE, new Date());

        //relationship association
        creditPackage.getCreditTransactions().add(newCreditTransaction);
        customer.getCreditTransactions().add(newCreditTransaction);
        newCreditTransaction.setCreditPackage(creditPackage);
        newCreditTransaction.setCustomer(customer);

        em.persist(newCreditTransaction);
    }

    @Override
    public void placeABid(Long auctionId, Long customerId, BigDecimal bidAmount) throws CustomerNotFoundException, AuctionNotFoundException, NotEnoughCreditException, InvalidBidException, BidNotFoundException {
        Customer customer = retrieveCustomerbyId(customerId);
        Auction auction = auctionSessionBeanLocal.retrieveAuctionbyId(auctionId);
        BigDecimal bidIncrement = auctionSessionBeanLocal.bidConverter(auction);
        
        Bid highestBid;
        BigDecimal highestBidAmount;
        
        // If customer bid amount > his/her own credit balance, throw this error
        if (bidAmount.doubleValue() > customer.getCreditBalance().doubleValue()) {
            throw new NotEnoughCreditException("You do not have sufficient credit balance to place this bid amount!");
        }
        
        
        // If there are ongoing bids, check if the bid amount is higher than the highest bid amount + bid increment rate
        if (!auction.getBids().isEmpty()) {
            List<Bid> bids = auction.getBids();
            Collections.sort(bids);
            highestBid = bids.get(0);
            highestBidAmount = highestBid.getBidAmount();

            if (bidAmount.doubleValue() - bidIncrement.doubleValue() < highestBidAmount.doubleValue()) {
                throw new NotEnoughCreditException("You need to bid higher than the minimum bid increment rate!");
            } 
            
            if (highestBid.getCustomer().equals(customer)) {
                throw new InvalidBidException("You can only bid again if someone bids higher than you!");
            }

            auctionSessionBeanLocal.doRefund(highestBid);

        } // If there are no bids yet, check if the bid amount is higher than the starting bid amount + bid increment rate
        else 
        {
            if (bidAmount.doubleValue() - bidIncrement.doubleValue() < auction.getStartingBid().doubleValue()) {
                throw new NotEnoughCreditException("You need to bid higher than the minimum bid increment rate!");
            }
            highestBidAmount = auction.getStartingBid();
        }

        Date currentDate = new Date();
        
        Bid newBid = new Bid(bidAmount, new Date());
        newBid.setCustomer(customer);
        customer.getBids().add(newBid);
        newBid.setAuction(auction);
        auction.getBids().add(newBid);

        CreditTransaction newBidTransaction = new CreditTransaction(bidAmount, CreditTransactionEnum.BID, currentDate);
        newBidTransaction.setBid(newBid);
        newBid.setBidTransaction(newBidTransaction);
        newBidTransaction.setCustomer(customer);
        
        customer.setCreditBalance(customer.getCreditBalance().subtract(bidAmount));
        customer.getCreditTransactions().add(newBidTransaction);
        
        em.persist(newBid);
        em.persist(newBidTransaction);
        em.flush();
        
        if (!auction.getProxyBids().isEmpty()) {
   
            List<ProxyBid> proxyBids = auction.getProxyBids();
            Collections.sort(proxyBids);
            ProxyBid highestProxyBid = proxyBids.get(0);
            BigDecimal maxProxyBidAmount = highestProxyBid.getMaximumBidAmount();
            
            Customer proxyCustomer = highestProxyBid.getCustomer();
            
            if (!proxyCustomer.equals(customer) && maxProxyBidAmount.doubleValue() > bidAmount.doubleValue()) {
                Auction nextAuction = auctionSessionBeanLocal.retrieveAuctionbyId(auctionId);
                BigDecimal nextBidIncrement = auctionSessionBeanLocal.bidConverter(auction);
                placeABid(nextAuction.getId(), proxyCustomer.getId(), bidAmount.add(nextBidIncrement));
            }
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Customer>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
}
