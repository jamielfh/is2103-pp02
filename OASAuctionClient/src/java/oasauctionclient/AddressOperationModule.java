/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasauctionclient;

import ejb.session.stateless.AddressSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.Address;
import entity.Customer;
import java.util.List;
import java.util.Scanner;
import util.exception.AddressIsAssociatedWithWinningBidException;
import util.exception.AddressIsDisabledException;
import util.exception.AddressNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidAddressCreationException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.UpdateAddressException;

/**
 *
 * @author Bransome Tan Yi Hao
 */
public class AddressOperationModule {
    
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private AddressSessionBeanRemote addressSessionBeanRemote;
    private Customer currentCustomer;
      
    public AddressOperationModule() {
    }

    public AddressOperationModule(CustomerSessionBeanRemote customerSessionBeanRemote, AddressSessionBeanRemote addressSessionBeanRemote, Customer currentCustomer) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.addressSessionBeanRemote = addressSessionBeanRemote;
        this.currentCustomer = currentCustomer;
    }
    
    public void menuAddressOperation() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Crazy Bids Auction Client System :: Address Operation ***\n");
            System.out.println("1: Create Address");
            System.out.println("2: View Address Details");
            System.out.println("3: View All Addresses");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try {
                        doCreateAddress();
                    } catch (InvalidAddressCreationException ex) {
                       System.out.println("\nInvalid Address Credential: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 2)
                {
                    doViewAddressDetails();
                }
                else if(response == 3)
                {
                    doViewAllAddresses();
                }
                else if(response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }

    private void doCreateAddress() throws InvalidAddressCreationException {
        Scanner scanner = new Scanner(System.in);
        String addressLine1 = "";
        String addressLine2 = "";
        String postalCode = "";
        
        System.out.println("*** Crazy Bids Auction Client System :: Create New Address ***\n");
        System.out.print("Enter Address Line 1> ");
        addressLine1 = scanner.nextLine().trim();
        System.out.print("Enter Address Line 2> ");
        addressLine2 = scanner.nextLine().trim();
        System.out.print("Enter Postal Code> ");
        postalCode = scanner.nextLine().trim();
       
        if(addressLine1.length() > 0 && addressLine2.length() > 0 && postalCode.length() > 0)
        {
            Address newAddress = new Address(addressLine1, addressLine2, postalCode, false);
            
            try 
            {
                Long addressId = addressSessionBeanRemote.createAddress(newAddress, currentCustomer.getId());
                System.out.println("\nNew Address created successfully!: " + addressId + "\n");
        
            } catch (GeneralException ex) {
                System.out.println("\nAn unknown error has occurred while creating the new address!: " + ex.getMessage() + "\n");
            } catch (CustomerNotFoundException | InputDataValidationException ex) {
                System.out.println(ex.getMessage());
            }
        }
        else
        {
            throw new InvalidAddressCreationException("\nMissing Address credential!");
        }
        
    }

    private void doViewAddressDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Crazy Bids Auction Client System :: View Address Details ***\n");
        System.out.print("Enter Address ID> ");
        Long addressId = scanner.nextLong();
        
        
        try
        {
            Address address = addressSessionBeanRemote.retrieveAddressbyId(addressId);
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%10s%20s%20s%20s%20s\n", "Address ID", "Address Line 1", "Address Line 2", "Postal Code", "isDisabled");
            System.out.printf("%10s%20s%20s%20s%20b\n", address.getId().toString(), address.getAddressLine1(), address.getAddressLine2(), address.getPostalCode(), address.getIsDisabled());
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("1: Update Address");
            System.out.println("2: Delete Address");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if(response == 1)
            {
                doUpdateAddress(address);
            }
            else if(response == 2)
            {
                doDeleteAddress(address);
            }
        }
        catch(AddressNotFoundException ex)
        {
            System.out.println("\nAn error has occurred while retrieving address: " + ex.getMessage() + "\n");
        }
    }

    private void doViewAllAddresses() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Crazy Bids Auction Client System :: View All My Addresses ***\n");
        
        List<Address> addresses;
        
        try {
            addresses = addressSessionBeanRemote.retrieveAllAddressesByCustomerId(currentCustomer.getId());
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%10s%20s%20s%20s%20s\n", "Address ID", "Address Line 1", "Address Line 2", "Postal Code", "isDisabled");

            for(Address address: addresses)
            {
                System.out.printf("%10s%20s%20s%20s%20b\n", address.getId().toString(), address.getAddressLine1(), address.getAddressLine2(), address.getPostalCode(), address.getIsDisabled());
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.print("Press any key to continue...> ");
            scanner.nextLine();
        } 
        catch (CustomerNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
       
    }

    private void doUpdateAddress(Address updateAddress) {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** Crazy Bids Auction Client System :: View Address Details :: Update Address ***\n");
        System.out.print("Enter Address Line 1 (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            updateAddress.setAddressLine1(input);
        }
        
        System.out.print("Enter Address Line 2 (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            updateAddress.setAddressLine2(input);
        }
        
        System.out.print("Enter Postal Code (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            updateAddress.setPostalCode(input);
        }              
        
        try
        {
            addressSessionBeanRemote.updateAddress(updateAddress);
            System.out.println("\nAddress updated successfully!\n");
        }
        catch (AddressNotFoundException | UpdateAddressException | InputDataValidationException ex) 
        {
            System.out.println("\nAn error has occurred while updating address: " + ex.getMessage() + "\n");
        }
    }

    private void doDeleteAddress(Address deleteAddress) {
        Scanner scanner = new Scanner(System.in);     
        String input;
        String input2;
        
        System.out.println("*** Crazy Bids Auction Client System :: View Address Details :: Delete Address ***\n");
        System.out.printf("Confirm Delete Address (Address ID: %s) (Enter 'Y' to Delete)> ", deleteAddress.getId());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try 
            {
                addressSessionBeanRemote.deleteAddress(deleteAddress.getId(), currentCustomer.getId());
                System.out.println("\nAddress deleted successfully!\n");
            } 
            catch (AddressNotFoundException ex) 
            {
                System.out.println("\nAn error has occurred while deleting the address: " + ex.getMessage() + "\n");
            }
            catch (AddressIsAssociatedWithWinningBidException ex) {
                System.out.printf("\nCannot Delete Address (Address ID: %s) as it is associated with a winning bid! Do you want to disable the address instead? (Enter 'Y' to Disable))> ", deleteAddress.getId());
                input2 = scanner.nextLine().trim();
                if (input2.equals("Y")) {
                    try {
                        addressSessionBeanRemote.disableAddress(deleteAddress.getId());
                    } catch (AddressNotFoundException | AddressIsDisabledException ex1) {
                        System.out.println("\nAn error has occurred while disabling the address: " + ex1.getMessage() + "\n");
                    }
                } else {
                    System.out.println("\nAddress NOT disabled!\n");
                }
            } catch (CustomerNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
        else
        {
            System.out.println("Address NOT deleted!\n");
        }
    }
    
    
}
