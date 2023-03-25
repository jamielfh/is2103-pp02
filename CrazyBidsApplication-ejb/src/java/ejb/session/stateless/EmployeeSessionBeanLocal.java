/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Local;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author jamielee
 */
@Local
public interface EmployeeSessionBeanLocal {

    Employee retrieveEmployeebyId(Long employeeId) throws EmployeeNotFoundException;

    Employee retrieveEmployeebyUsername(String username) throws EmployeeNotFoundException;

    Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;

    Long createNewEmployee(Employee newEmployee) throws UnknownPersistenceException;

    void changePassword(Long employeeId, String newPassword) throws EmployeeNotFoundException;

    void deleteEmployee(Long employeeId) throws EmployeeNotFoundException;
    
}
