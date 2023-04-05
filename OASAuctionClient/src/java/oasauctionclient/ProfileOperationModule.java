/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasauctionclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.Customer;
import java.util.Scanner;
import util.exception.CustomerNotFoundException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author Bransome Tan Yi Hao
 */
public class ProfileOperationModule {
    
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private Customer currentCustomer;
    
    public ProfileOperationModule() {
    }

    public ProfileOperationModule(CustomerSessionBeanRemote customerSessionBeanRemote, Customer currentCustomer) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.currentCustomer = currentCustomer;
    }
    
    public void menuProfileOperation() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Crazy Bids Auction Client System :: Profile Operation ***\n");
            System.out.println("1: View My Customer Profile");
            System.out.println("2: View Other Customer Profile");
            System.out.println("3: Update My Customer Profile");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doViewMyCustomerProfile();
                }
                else if(response == 2)
                {
                    doViewOtherCustomerProfile();
                }
                else if(response == 3)
                {
                    doUpdateCustomerProfile();
                }
                else if(response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("\nInvalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }
    
    private void doViewMyCustomerProfile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Crazy Bids Auction Client System :: View My Customer Profile ***\n");
 
        try
        {
            Customer customer = customerSessionBeanRemote.retrieveCustomerbyId(currentCustomer.getId());
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%8s%20s%20s%20s%20s%20s%20s\n", "Customer ID", "First Name", "Last Name", "Username", "Password", "Email", "Mobile Number");
            System.out.printf("%8s%20s%20s%20s%20s%20s%20s\n", customer.getId().toString(), customer.getFirstName(), customer.getLastName(), customer.getUsername(), customer.getPassword(), customer.getEmail(), customer.getMobileNumber());         
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.print("Press any key to go back...>");
            scanner.nextLine();

        }
        catch(CustomerNotFoundException ex)
        {
            System.out.println("\nAn error has occurred while retrieving customer: " + ex.getMessage() + "\n");
        }
       
         
    }
    private void doViewOtherCustomerProfile() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Crazy Bids Auction Client System :: View Other Customer Profile ***\n");
        System.out.print("Enter Customer Username> ");
        String customerUsername =  scanner.nextLine().trim();
        
        try
        {
            Customer customer = customerSessionBeanRemote.retrieveCustomerbyUsername(customerUsername);
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%8s%20s%20s%20s%20s\n", "Customer ID", "First Name", "Last Name", "Email", "Mobile Number");
            System.out.printf("%8s%20s%20s%20s%20s\n", customer.getId().toString(), customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getMobileNumber());         
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.print("Press any key to go back...>");
            scanner.nextLine();

        }
        catch(CustomerNotFoundException ex)
        {
            System.out.println("\nAn error has occurred while retrieving customer: " + ex.getMessage() + "\n");
        }
    }

    private void doUpdateCustomerProfile() {
        Scanner scanner = new Scanner(System.in);
        String input;
        
        System.out.println("*** Crazy Bids Auction Client System :: Update Customer Profile ***\n");
        
        try 
        {
            Customer updateCustomer = customerSessionBeanRemote.retrieveCustomerbyId(currentCustomer.getId());
            
            System.out.print("Enter First Name (blank if no change)> ");
            input = scanner.nextLine().trim();
            if(input.length() > 0)
            {
                updateCustomer.setFirstName(input);
            }

            System.out.print("Enter Last Name (blank if no change)> ");
            input = scanner.nextLine().trim();
            if(input.length() > 0)
            {
                updateCustomer.setLastName(input);
            }

            System.out.print("Enter Email (blank if no change)> ");
            input = scanner.nextLine().trim();
            if(input.length() > 0)
            {
                updateCustomer.setEmail(input);
            }

            System.out.print("Enter Mobile Number (blank if no change)> ");
            input = scanner.nextLine().trim();
            if(input.length() > 0)
            {
                updateCustomer.setMobileNumber(input);
            }

            System.out.print("Enter Password (blank if no change)> ");
            input = scanner.nextLine().trim();
            if(input.length() > 0)
            {
                updateCustomer.setPassword(input);
            }
            
            customerSessionBeanRemote.updateCustomer(updateCustomer);
            System.out.println("\nCustomer profile updated successfully!\n");
            
        } 
        catch (CustomerNotFoundException ex) 
        {
            System.out.println("\nAn error has occurred while retrieving customer: " + ex.getMessage() + "\n");
        }
        catch (UpdateCustomerException ex) 
        {
            System.out.println("\nAn error has occurred while updating customer: " + ex.getMessage() + "\n");
        }
       
    }
  
}
