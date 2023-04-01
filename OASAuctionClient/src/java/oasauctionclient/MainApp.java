/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasauctionclient;

import ejb.session.stateless.AddressSessionBeanRemote;
import ejb.session.stateless.AuctionSessionBeanRemote;
import ejb.session.stateless.CreditPackageSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.SuccessfulAuctionSessionBeanRemote;
import entity.Customer;
import java.math.BigDecimal;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;
import util.exception.InvalidRegisterCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Bransome Tan Yi Hao
 */
public class MainApp {
    private AuctionSessionBeanRemote auctionSessionBeanRemote;
    private SuccessfulAuctionSessionBeanRemote successfulAuctionSessionBeanRemote;
    private AddressSessionBeanRemote addressSessionBeanRemote;
    private CreditPackageSessionBeanRemote creditPackageSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    
    private ProfileOperationModule profileOperationModule;
    private AddressOperationModule addressOperationModule;
    private CreditOperationModule creditOperationModule;
    private AuctionOperationModule auctionOperationModule;
    
    private Customer currentCustomer;
    
    public MainApp() 
    {        
    }

    public MainApp(AuctionSessionBeanRemote auctionSessionBeanRemote, SuccessfulAuctionSessionBeanRemote successfulAuctionSessionBeanRemote, AddressSessionBeanRemote addressSessionBeanRemote, CreditPackageSessionBeanRemote creditPackageSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote) {
        this.auctionSessionBeanRemote = auctionSessionBeanRemote;
        this.successfulAuctionSessionBeanRemote = successfulAuctionSessionBeanRemote;
        this.addressSessionBeanRemote = addressSessionBeanRemote;
        this.creditPackageSessionBeanRemote = creditPackageSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
    }
    
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Crazy Bids OAS Auction Client System ***\n");
            System.out.println("1: Register");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doRegister();
                        System.out.println("\nRegistered successful!\n");
                    }
                    catch(InvalidRegisterCredentialException ex) 
                    {
                        System.out.println("\nInvalid Register credential: " + ex.getMessage() + "\n");
                    }
                    catch(UnknownPersistenceException ex) 
                    {
                        System.out.println(ex.getMessage() + "\n");
                    }
                    
                }
                else if (response == 2)
                {
                    try
                    {
                        doLogin();
                        System.out.println("\nLogin successful!\n");
                        
                        profileOperationModule = new ProfileOperationModule(customerSessionBeanRemote, currentCustomer);
                        addressOperationModule = new AddressOperationModule(customerSessionBeanRemote, addressSessionBeanRemote, currentCustomer);
                        creditOperationModule = new CreditOperationModule(customerSessionBeanRemote, creditPackageSessionBeanRemote, currentCustomer);
                        auctionOperationModule = new AuctionOperationModule(customerSessionBeanRemote, auctionSessionBeanRemote, successfulAuctionSessionBeanRemote, currentCustomer);
                        
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("\nInvalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 3) {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 3)
            {
                break;
            }
        }
    }
    
    
     private void doLogin() throws InvalidLoginCredentialException
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** Crazy Bids OAS Client System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentCustomer = customerSessionBeanRemote.customerLogin(username, password);
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
     private void doRegister() throws InvalidRegisterCredentialException, UnknownPersistenceException
    {
        Scanner scanner = new Scanner(System.in);
        String firstName = "";
        String lastName = "";
        String email = "";
        String mobileNumber = "";
        String username = "";
        String password = "";
        BigDecimal creditBalance = new BigDecimal(0);
        
        System.out.println("*** Crazy Bids OAS Client System :: Register ***\n");
        System.out.print("Enter First Name> ");
        firstName = scanner.nextLine().trim();
        System.out.print("Enter Last Name> ");
        lastName = scanner.nextLine().trim();
        System.out.print("Enter Email> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter Mobile Number> ");
        mobileNumber = scanner.nextLine().trim();
        System.out.print("Enter Username (Note: You cannot change your Username after registration!) > ");
        username = scanner.nextLine().trim();
        System.out.print("Enter Password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0 && firstName.length() > 0 && lastName.length() > 0 && email.length() > 0 && mobileNumber.length() > 0)
        {
            Customer newCustomer = new Customer(firstName, lastName, username, password, email, mobileNumber, creditBalance);
            customerSessionBeanRemote.createNewCustomer(newCustomer);
        }
        else
        {
            throw new InvalidRegisterCredentialException("Missing Register credential!");
        }
    }
     
     
    private void menuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Crazy Bids OAS Auction Client System ***\n");
            System.out.println("You are login as " + currentCustomer.getFirstName() + " " + currentCustomer.getLastName() + "\n");
            System.out.println("1: View and Manage Your Profile");
            System.out.println("2: View and Manage Your Addresses");
            System.out.println("3: View and Manage Your Credits");
            System.out.println("4: View and Manage Auctions");
            System.out.println("5: Logout\n");
            response = 0;
            
            while(response < 1 || response > 5)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    profileOperationModule.menuProfileOperation();
                }
                else if(response == 2)
                {
                    addressOperationModule.menuAddressOperation();
                }
                else if (response == 3)
                {
                    creditOperationModule.menuCreditOperation();
                }
                else if (response == 4)
                {
                    auctionOperationModule.menuAuctionOperation();
                }
                else if (response == 5)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 5)
            {
                break;
            }
        }
    }
    
    
}
