/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasauctionclient;

import ejb.session.stateless.CreditPackageSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.CreditPackage;
import entity.CreditTransaction;
import entity.Customer;
import java.text.NumberFormat;
import java.util.List;
import java.util.Scanner;
import util.exception.CreditPackageNotFoundException;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author Bransome Tan Yi Hao
 */
public class CreditOperationModule {
    
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private CreditPackageSessionBeanRemote creditPackageSessionBeanRemote;
    private Customer currentCustomer;
            
    public CreditOperationModule() {
    }

    public CreditOperationModule(CustomerSessionBeanRemote customerSessionBeanRemote, CreditPackageSessionBeanRemote creditPackageSessionBeanRemote, Customer currentCustomer) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.creditPackageSessionBeanRemote = creditPackageSessionBeanRemote;
        this.currentCustomer = currentCustomer;
    }
    
    public void menuCreditOperation() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Crazy Bids Auction Client System :: Credit Operation ***\n");
            System.out.println("1: View Credit Balance");
            System.out.println("2: View Credit Transaction History");
            System.out.println("3: Purchase Credit Package");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doViewCreditBalance();
                }
                else if(response == 2)
                {
                    doCreditTransactionHistory();
                }
                else if(response == 3)
                {
                    doPurchaseCreditPackage();
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

    private void doViewCreditBalance() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Crazy Bids Auction Client System :: View My Credit Balance ***\n");
 
        try
        {
            Customer customer = customerSessionBeanRemote.retrieveCustomerbyId(currentCustomer.getId());
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%20s\n", "Your Credit Balance");
            System.out.printf("%20s\n",  NumberFormat.getCurrencyInstance().format(customer.getCreditBalance()));     
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        }
        catch(CustomerNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving customer: " + ex.getMessage() + "\n");
        }
        
        System.out.print("Press any key to go back...>");
        scanner.nextLine();
    }

    private void doCreditTransactionHistory() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Crazy Bids Auction Client System :: View My Credit Transaction History ***\n");
        
        List<CreditTransaction> creditTransactions;
        
        try {
            creditTransactions = customerSessionBeanRemote.retrieveAllCreditTransactionByCustomerId(currentCustomer.getId());
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%10s%30s%20s%20s\n", "Credit Transaction ID", "Credit Transaction Amount", "Transaction Type", "Transaction Date");

            for(CreditTransaction creditTransaction: creditTransactions)
            {
                System.out.printf("%10s%30s%20s%20s\n", creditTransaction.getId().toString(), NumberFormat.getCurrencyInstance().format(creditTransaction.getTransactionAmount()), creditTransaction.getCreditTransactionEnum().toString(), creditTransaction.getTransactionDateTime());
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        } 
        catch (CustomerNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
        System.out.print("Press any key to go back...> ");
        scanner.nextLine();
    }

    private void doPurchaseCreditPackage() {
        Scanner scanner = new Scanner(System.in);
        Long creditPackageId;
        Long quantity;
        
        System.out.println("*** Crazy Bids Auction Client System :: Purchase Credit Package ***\n");
         
        List<CreditPackage> creditPackages = creditPackageSessionBeanRemote.retrieveAllActiveCreditPackages();
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%10s%30s\n", "Credit Package ID", "Credit Package Amount");

        for(CreditPackage creditPackage: creditPackages)
        {
            System.out.printf("%10s%30s\n", creditPackage.getId().toString(), NumberFormat.getCurrencyInstance().format(creditPackage.getCreditPackageAmount()));
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        if (creditPackages.isEmpty()) {
            System.out.println("\nThere are currently no credit packages available\n");
        } else {
            System.out.print("Enter the Credit Package ID to purchase> ");
            creditPackageId = scanner.nextLong();
            try {
                creditPackageSessionBeanRemote.retrieveCreditPackagebyId(creditPackageId);
                System.out.printf("Enter quantity to purchase Credit Package (ID: %s)> ", creditPackageId);
                quantity = scanner.nextLong();
                customerSessionBeanRemote.purchaseCreditPackage(creditPackageId, currentCustomer.getId(), quantity);
                System.out.printf("\nYou have successfully purchase Credit Package (ID: %s) of quantity %s\n", creditPackageId, quantity);
            } catch (CreditPackageNotFoundException ex) {
                System.out.printf("Credit Package (ID: %s) does not exist!> ", creditPackageId);
            } catch (CustomerNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
    }
}
