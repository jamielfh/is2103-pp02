/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasadministrationpanel;

import ejb.session.stateless.CreditPackageSessionBeanRemote;
import entity.CreditPackage;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import util.exception.CreditPackageIsDisabledException;
import util.exception.CreditPackageIsUsedException;
import util.exception.CreditPackageNotFoundException;
import util.exception.InvalidCreditPackageCreationException;
import util.exception.GeneralException;
import util.exception.UpdateCreditPackageException;

/**
 *
 * @author jamielee
 */
public class FinanceOperationModule {
    
    private CreditPackageSessionBeanRemote creditPackageSessionBeanRemote;

    public FinanceOperationModule() {
    }

    public FinanceOperationModule(CreditPackageSessionBeanRemote creditPackageSessionBeanRemote) {
        this.creditPackageSessionBeanRemote = creditPackageSessionBeanRemote;
    }
    
    public void menuFinanceOperation() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n*** Crazy Bids OAS Administration Panel :: Finance Operations ***\n");
            System.out.println("1: Create New Credit Package");
            System.out.println("2: View Credit Package Details");
            System.out.println("3: View All Credit Packages");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1)
                {
                    try
                    {
                        doCreateNewCreditPackage();
                    }
                    catch (InvalidCreditPackageCreationException ex)
                    {
                        System.out.println("\nAn error has occurred while creating the new credit package: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    doViewCreditPackageDetails();
                }
                else if (response == 3)
                {
                    doViewAllCreditPackages();
                }
                else if (response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if (response == 4)
            {
                break;
            }
        }
    }
    
    private void doCreateNewCreditPackage() throws InvalidCreditPackageCreationException
    {
        Scanner scanner = new Scanner(System.in);
        BigDecimal creditPackageAmount;
        
        System.out.println("\n*** Crazy Bids OAS Administration Panel :: Create New Credit Package ***\n");
        System.out.print("Enter credit package amount (up to 4 decimal places)> ");
        creditPackageAmount = new BigDecimal(scanner.nextLine().trim());

        if (creditPackageAmount.compareTo(new BigDecimal("0.05")) != -1)
        {
            CreditPackage newCreditPackage = new CreditPackage(creditPackageAmount, false);

            try
            {
                Long creditPackageId = creditPackageSessionBeanRemote.createCreditPackage(newCreditPackage);
                System.out.println("\nCredit package created successfully!: " + creditPackageId + "\n");
            }
            catch (GeneralException ex)
            {
                System.out.println("\nAn unknown error has occurred while creating the new credit package: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            throw new InvalidCreditPackageCreationException("Credit package amount must be at least 0.05!");
        }
    }
    
    private void doViewCreditPackageDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("\n*** Crazy Bids OAS Administration Panel :: View Credit Package Details ***\n");
        System.out.print("Enter Credit Package ID> ");
        Long creditPackageId = scanner.nextLong();
        
        try
        {
            CreditPackage creditPackage = creditPackageSessionBeanRemote.retrieveCreditPackagebyId(creditPackageId);
            
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%10s%20s%20s\n", "Package ID", "Amount", "Is Disabled");
            System.out.printf("%10s%20s%20b\n", creditPackage.getId().toString(), creditPackage.getCreditPackageAmount().toString(), creditPackage.getIsDisabled());
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("1: Update Credit Package");
            System.out.println("2: Delete Credit Package");
            System.out.println("3: Back\n");
            
            while (response < 1 || response > 3)
            {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1)
                {
                    try 
                    {
                        doUpdateCreditPackage(creditPackage);
                    } 
                    catch (InvalidCreditPackageCreationException ex) 
                    {
                        System.out.println("\nAn error has occurred while updating credit package: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    doDeleteCreditPackage(creditPackage);
                }
                else if (response == 3)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
        }
        catch(CreditPackageNotFoundException ex)
        {
            System.out.println("\nAn error has occurred while retrieving credit package: " + ex.getMessage() + "\n");
        }
    }
    
    private void doViewAllCreditPackages()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n*** Crazy Bids OAS Administration Panel :: View All Credit Packages ***\n");
        
        List<CreditPackage> creditPackages;
        
        creditPackages = creditPackageSessionBeanRemote.retrieveAllCreditPackages();
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");

        if (!creditPackages.isEmpty())
        {
            System.out.printf("%10s%20s%20s\n", "Package ID", "Amount", "Is Disabled");
            for(CreditPackage creditPackage : creditPackages)
            {
                System.out.printf("%10s%20s%20b\n", creditPackage.getId().toString(), creditPackage.getCreditPackageAmount().toString(), creditPackage.getIsDisabled());
            }
        }
        else
        {
            System.out.println("No credit packages found!");
        }
        
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    private void doUpdateCreditPackage(CreditPackage updateCreditPackage) throws InvalidCreditPackageCreationException {
        Scanner scanner = new Scanner(System.in);
        BigDecimal newAmount;
        
        System.out.println("\n*** Crazy Bids OAS Administration Panel :: Update Credit Package ***\n");
        System.out.print("Enter credit package amount (up to 4 decimal places)> ");
        newAmount = new BigDecimal(scanner.nextLine().trim());

        try
        {
            if (newAmount.compareTo(new BigDecimal("0.05")) != -1)
            {
                creditPackageSessionBeanRemote.updateCreditPackage(updateCreditPackage, newAmount);
                System.out.println("\nCredit package updated successfully!\n");
            }
            else
            {
                throw new InvalidCreditPackageCreationException("Credit package amount must be at least 0.05!");
            }
        }
        catch (CreditPackageNotFoundException | UpdateCreditPackageException ex) 
        {
            System.out.println("\nAn error has occurred while updating credit package: " + ex.getMessage() + "\n");
        }
    }
    
    private void doDeleteCreditPackage(CreditPackage deleteCreditPackage) {
        Scanner scanner = new Scanner(System.in);     
        String input = "";
        
        System.out.println("\n*** Crazy Bids Auction Client System :: Delete Credit Package ***\n");
        System.out.printf("Confirm Delete Credit Package (Credit Package ID: %s) (Enter 'Y' to Delete)> ", deleteCreditPackage.getId());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try 
            {
                creditPackageSessionBeanRemote.deleteCreditPackage(deleteCreditPackage.getId());
                System.out.println("\nCredit package deleted successfully!\n");
            } 
            catch (CreditPackageNotFoundException ex) 
            {
                System.out.println("\nAn error has occurred while deleting the credit package: " + ex.getMessage() + "\n");
            }
            catch (CreditPackageIsUsedException ex) 
            {
                System.out.printf("\nCannot delete credit package (Credit Package ID: %s) as it has been used before! Do you want to disable the credit package instead? (Enter 'Y' to Disable))> ", deleteCreditPackage.getId());
                input = scanner.nextLine().trim();
                
                if (input.equals("Y")) {
                    try
                    {
                        creditPackageSessionBeanRemote.disableCreditPackage(deleteCreditPackage.getId());
                        System.out.println("Credit package disabled successfully!\n");
                    } 
                    catch (CreditPackageNotFoundException | CreditPackageIsDisabledException ex1) {
                        System.out.println("\nAn error has occurred while disabling the credit package: " + ex1.getMessage() + "\n");
                    }
                } else {
                    System.out.println("\nCredit package NOT disabled!\n");
                }
            }
        }
        else
        {
            System.out.println("\nCredit package NOT deleted!\n");
        }
    }
}
