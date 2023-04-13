/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasadministrationpanel;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import entity.Employee;
import java.util.List;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidEmployeeCreationException;
import util.exception.GeneralException;
import util.exception.UpdateEmployeeException;

/**
 *
 * @author jamielee
 */
public class SystemAdminOperationModule {
    
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private Employee currentEmployee;

    public SystemAdminOperationModule() {
    }

    public SystemAdminOperationModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, Employee currentEmployee) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void menuSystemAdminOperation() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n*** Crazy Bids OAS Administration Panel :: System Administration Operation ***\n");
            System.out.println("1: Create New Employee");
            System.out.println("2: View Employee Details");
            System.out.println("3: View All Employees");
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
                        doCreateNewEmployee();
                    }
                    catch (InvalidEmployeeCreationException ex)
                    {
                        System.out.println("\nAn error has occurred while creating the new employee: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    doViewEmployeeDetails();
                }
                else if (response == 3)
                {
                    doViewAllEmployees();
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
    
    private void doCreateNewEmployee() throws InvalidEmployeeCreationException
    {
        Scanner scanner = new Scanner(System.in);
        String firstName = "";
        String lastName = "";
        AccessRightEnum accessRightEnum = null;
        String username = "";
        String password = "";
        
        System.out.println("\n*** Crazy Bids OAS Administration Panel :: Create New Employee ***\n");
        System.out.print("Enter first name> ");
        firstName = scanner.nextLine().trim();
        System.out.print("Enter last name> ");
        lastName = scanner.nextLine().trim();
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        Integer response = 0;
        
        while (response < 1 || response > 3) 
        {
            System.out.println("Select employee access right:");
            System.out.println("1: SYSTEMADMIN");
            System.out.println("2: FINANCESTAFF");
            System.out.println("3: SALESSTAFF");
            System.out.println("> ");
            response = scanner.nextInt();
            
            if (response == 1) 
            {
                accessRightEnum = AccessRightEnum.SYSTEMADMIN;
            } 
            else if (response == 2) 
            {
                accessRightEnum = AccessRightEnum.FINANCESTAFF;
            }
            else if (response == 3)
            {
                accessRightEnum = AccessRightEnum.SALESSTAFF;
            } 
            else 
            {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        
        if (firstName.length() > 0 && lastName.length() > 0 && accessRightEnum != null && username.length() > 0 && password.length() > 0)
        {
            Employee newEmployee = new Employee(firstName, lastName, accessRightEnum, username, password);
            
            try
            {
                Long employeeId = employeeSessionBeanRemote.createNewEmployee(newEmployee);
                System.out.println("\nEmployee created successfully!: " + employeeId);
            }
            catch (GeneralException ex)
            {
                System.out.println("\nAn error has occurred while creating the new employee: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            throw new InvalidEmployeeCreationException("Missing employee details!");
        }
    }
    
    private void doViewEmployeeDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("\n*** Crazy Bids OAS Administration Panel :: View Employee Details ***\n");
        System.out.print("Enter Employee ID> ");
        Long employeeId = scanner.nextLong();
        
        try
        {
            Employee employee = employeeSessionBeanRemote.retrieveEmployeebyId(employeeId);
            
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%15s%20s%20s%20s%20s\n", "Employee ID", "First Name", "Last Name", "Access Right", "Username");
            System.out.printf("%15s%20s%20s%20s%20s\n", employee.getId().toString(), employee.getFirstName(),employee.getLastName(), employee.getAccessRightEnum().toString(), employee.getUsername());
            // For security reasons, system administrators are disallowed from viewing employees' account passwords.
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("1: Update Employee");
            System.out.println("2: Delete Employee");
            System.out.println("3: Back\n");
            
            while (response < 1 || response > 3)
            {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1)
                {
                    doUpdateEmployee(employee);
                }
                else if (response == 2)
                {
                    if (currentEmployee.equals(employee))
                    {
                        System.out.println("\nCannot delete employee while employee is logged in!");
                    }
                    else
                    {
                        doDeleteEmployee(employee);
                    }
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
        catch(EmployeeNotFoundException ex)
        {
            System.out.println("\nAn error has occurred while retrieving employee: " + ex.getMessage() + "\n");
        }
    }
    
    private void doViewAllEmployees()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n*** Crazy Bids OAS Administration Panel :: View All Employees ***\n");
        
        List<Employee> employees;
        
        employees = employeeSessionBeanRemote.retrieveAllEmployees();
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%15s%20s%20s%20s%20s\n", "Employee ID", "First Name", "Last Name", "Access Right", "Username");
        // For security reasons, system administrators are disallowed from viewing employees' account passwords.

        for(Employee employee: employees)
        {
            System.out.printf("%15s%20s%20s%20s%20s\n", employee.getId().toString(), employee.getFirstName(),employee.getLastName(), employee.getAccessRightEnum().toString(), employee.getUsername());
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    private void doUpdateEmployee(Employee updateEmployee) {
        Scanner scanner = new Scanner(System.in);        
        String input = "";
        
        System.out.println("\n*** Crazy Bids OAS Administration Panel :: Update Employee ***\n");
        System.out.print("Enter first name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            updateEmployee.setFirstName(input);
        }
        
        System.out.print("Enter last name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            updateEmployee.setLastName(input);
        }
        
        System.out.println("Select employee access right:");
        System.out.println("0: No change");
        System.out.println("1: SYSTEMADMIN");
        System.out.println("2: FINANCESTAFF");
        System.out.println("3: SALESSTAFF");
        Integer response = -1;
        
        while (response < 0 || response > 3) 
        {
            System.out.println("> ");
            response = scanner.nextInt();
            
            if (response == 0)
            {
                continue;
            }
            if (response == 1) 
            {
                updateEmployee.setAccessRightEnum(AccessRightEnum.SYSTEMADMIN);
            } 
            else if (response == 2) 
            {
                updateEmployee.setAccessRightEnum(AccessRightEnum.FINANCESTAFF);
            }
            else if (response == 3)
            {
                updateEmployee.setAccessRightEnum(AccessRightEnum.SALESSTAFF);
            } 
            else 
            {
                System.out.println("Invalid option, please try again!\n");
            }
        }           
        
        try
        {
            employeeSessionBeanRemote.updateEmployee(updateEmployee);
            
            if (currentEmployee.equals(updateEmployee))
            {
                currentEmployee = employeeSessionBeanRemote.retrieveEmployeebyId(currentEmployee.getId());
            }
            
            System.out.println("\nEmployee updated successfully!\n");
        }
        catch (EmployeeNotFoundException | UpdateEmployeeException ex) 
        {
            System.out.println("\nAn error has occurred while updating employee: " + ex.getMessage() + "\n");
        }
    }
    
    private void doDeleteEmployee(Employee deleteEmployee) {
        Scanner scanner = new Scanner(System.in);     
        String input = "";
        
        System.out.println("\n*** Crazy Bids OAS Administration Panel :: Delete Employee ***\n");
        System.out.printf("Confirm Delete Employee (Employee ID: %s) (Enter 'Y' to Delete)> ", deleteEmployee.getId());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try 
            {
                employeeSessionBeanRemote.deleteEmployee(deleteEmployee.getId());
                System.out.println("\nEmployee deleted successfully!\n");
            } 
            catch (EmployeeNotFoundException ex) 
            {
                System.out.println("\nAn error has occurred while deleting the employee: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            System.out.println("\nEmployee NOT deleted!\n");
        }
    }
}
