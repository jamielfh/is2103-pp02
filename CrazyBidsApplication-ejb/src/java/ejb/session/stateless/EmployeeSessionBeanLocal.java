/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.GeneralException;
import util.exception.UpdateEmployeeException;

/**
 *
 * @author jamielee
 */
@Local
public interface EmployeeSessionBeanLocal {

    Employee retrieveEmployeebyId(Long employeeId) throws EmployeeNotFoundException;

    Employee retrieveEmployeebyUsername(String username) throws EmployeeNotFoundException;

    Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;

    Long createNewEmployee(Employee newEmployee) throws GeneralException;

    void changePassword(Long employeeId, String newPassword) throws EmployeeNotFoundException;

    void deleteEmployee(Long deletedEmployee) throws EmployeeNotFoundException;

    List<Employee> retrieveAllEmployees();

    void updateEmployee(Employee updatedEmployee) throws EmployeeNotFoundException, UpdateEmployeeException;
    
}
