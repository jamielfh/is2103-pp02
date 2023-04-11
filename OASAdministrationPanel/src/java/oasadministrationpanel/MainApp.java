/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasadministrationpanel;

import ejb.session.stateless.AuctionSessionBeanRemote;
import ejb.session.stateless.CreditPackageSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.SuccessfulAuctionSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author jamielee
 */
public class MainApp {
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private CreditPackageSessionBeanRemote creditPackageSessionBeanRemote;
    private AuctionSessionBeanRemote auctionSessionBeanRemote;
    private SuccessfulAuctionSessionBeanRemote successfulAuctionSessionBeanRemote;
    
    private SystemAdminOperationModule systemAdminOperationModule;
    private FinanceOperationModule financeOperationModule;
    private SalesOperationModule salesOperationModule;
   
    private Employee currentEmployee;
    
    public MainApp() 
    {        
    }

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, CreditPackageSessionBeanRemote creditPackageSessionBeanRemote, AuctionSessionBeanRemote auctionSessionBeanRemote, SuccessfulAuctionSessionBeanRemote successfulAuctionSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.creditPackageSessionBeanRemote = creditPackageSessionBeanRemote;
        this.auctionSessionBeanRemote = auctionSessionBeanRemote;
        this.successfulAuctionSessionBeanRemote = successfulAuctionSessionBeanRemote;
    }
    
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n*** Welcome to Crazy Bids OAS Administration Panel ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1)
                {
                    try
                    {
                        doLogin();
                        System.out.println("\nLogin successful!\n");

                        systemAdminOperationModule = new SystemAdminOperationModule(employeeSessionBeanRemote, currentEmployee);
                        financeOperationModule = new FinanceOperationModule(creditPackageSessionBeanRemote);
                        salesOperationModule = new SalesOperationModule(auctionSessionBeanRemote, successfulAuctionSessionBeanRemote);
                        
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("\nInvalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2) {
                    currentEmployee = null;
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 2)
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
        
        System.out.println("\n*** Crazy Bids OAS Administration Panel :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentEmployee = employeeSessionBeanRemote.employeeLogin(username, password);
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }     
     
    private void menuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n*** Welcome to Crazy Bids OAS Administration Panel ***\n");
            System.out.println("You are login as " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName() + "\n");
            System.out.println("1: View and Manage Operations");
            System.out.println("2: Change Password");
            System.out.println("3: Logout\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    AccessRightEnum accessRight = currentEmployee.getAccessRightEnum();
                    
                    if (accessRight == AccessRightEnum.SYSTEMADMIN)
                    {
                        systemAdminOperationModule.menuSystemAdminOperation();
                    }
                    else if (accessRight == AccessRightEnum.FINANCESTAFF)
                    {
                        financeOperationModule.menuFinanceOperation();
                    }
                    else if (accessRight == AccessRightEnum.SALESSTAFF)
                    {
                        salesOperationModule.menuSalesOperation();
                    }
                    else
                    {
                        System.out.println("No access rights have been given to this employee!");
                    }
                }
                else if(response == 2)
                {
                    doChangePassword();
                }
                else if (response == 3)
                {
                    currentEmployee = null;
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
    
    private void doChangePassword()
    {
        Scanner scanner = new Scanner(System.in);
        String currentPassword = "";
        String newPassword = "";
        String confirmNewPassword = "";
        
        try
        {
            while(true) 
            {
                System.out.println("\n*** Crazy Bids OAS Administration Panel :: Change Password ***\n");
                
                // Need to update currentemployee record
                currentEmployee = employeeSessionBeanRemote.retrieveEmployeebyId(currentEmployee.getId());
                
                System.out.print("Enter current password> ");
                currentPassword = scanner.nextLine().trim();

                if (currentPassword.equals(currentEmployee.getPassword()))
                {
                    System.out.print("Enter new password> ");
                    newPassword = scanner.nextLine().trim();

                    System.out.print("Confirm new password> ");
                    confirmNewPassword = scanner.nextLine().trim();

                    if (newPassword.equals(confirmNewPassword)) 
                    {
                        employeeSessionBeanRemote.changePassword(currentEmployee.getId(), newPassword);
                        currentEmployee = employeeSessionBeanRemote.retrieveEmployeebyId(currentEmployee.getId());
                        System.out.println("\nPassword successfully changed!");
                        break;
                    } 
                    else 
                    {
                        System.out.println("\nPasswords do not match, please try again!");
                    }
                }
                else
                {
                    System.out.println("\nPassword entered does not match existing password!");
                }
            }
        }
        catch (EmployeeNotFoundException ex)
        {
            System.out.println("\nAn error has occurred while changing password: " + ex.getMessage() + "\n");
        }
    }
}
