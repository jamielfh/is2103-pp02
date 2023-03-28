/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEmployeeException;

/**
 *
 * @author jamielee
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "CrazyBidsApplication-ejbPU")
    private EntityManager em;

    public EmployeeSessionBean() {
    }

    @Override
    public Employee retrieveEmployeebyId(Long employeeId) throws EmployeeNotFoundException {
        Employee employee = em.find(Employee.class, employeeId);
        if (employee != null) {
            return employee;
        } else {
            throw new EmployeeNotFoundException("Employee ID " + employeeId + " not found in system!");
        }
    }

    @Override
    public Employee retrieveEmployeebyUsername(String username) throws EmployeeNotFoundException {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :inputUsername");
        query.setParameter("inputUsername", username);
        try {
            return (Employee) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EmployeeNotFoundException("Employee username " + username + " not found in system!");
        }
    }

    @Override
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            Employee employee = retrieveEmployeebyUsername(username);

            if (employee.getPassword().equals(password)) {
                return employee;
            } else {
                throw new InvalidLoginCredentialException("Password is incorrect, please try again!");
            }
        } catch (EmployeeNotFoundException ex) {
            throw new InvalidLoginCredentialException(ex.getMessage());
        } catch (InvalidLoginCredentialException ex) {
            throw ex;
        }
    }

    @Override
    public List<Employee> retrieveAllEmployees() {
        Query query = em.createQuery("SELECT e FROM Employee e");

        return query.getResultList();
    }

    @Override
    public Long createNewEmployee(Employee newEmployee) throws UnknownPersistenceException {
        try {
            em.persist(newEmployee);
            em.flush();

            return newEmployee.getId();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new UnknownPersistenceException("Employee with username " + newEmployee.getUsername() + " already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public void changePassword(Long employeeId, String newPassword) throws EmployeeNotFoundException {
        Employee employee = retrieveEmployeebyId(employeeId);
        if (employee != null) {
            employee.setPassword(newPassword);
        }
    }

    @Override
    public void updateEmployee(Employee updatedEmployee) throws EmployeeNotFoundException, UpdateEmployeeException {
       
        if(updatedEmployee!= null && updatedEmployee.getId() != null)
        {
            Employee employeeToUpdate = retrieveEmployeebyId(updatedEmployee.getId());
            
            if(employeeToUpdate.getUsername().equals(updatedEmployee.getUsername()))
            {
                employeeToUpdate.setFirstName(updatedEmployee.getFirstName());
                employeeToUpdate.setLastName(updatedEmployee.getLastName());
                employeeToUpdate.setAccessRightEnum(updatedEmployee.getAccessRightEnum());
                
                // Username and Password are deliberately NOT updated to demonstrate that client is not allowed to update account credential through this business method
            }
            else
            {
                throw new UpdateEmployeeException("Username of employee record to be updated does not match the existing record");
            }
        }
        else
        {
            throw new EmployeeNotFoundException("Employee ID not provided for employee to be updated");
        }
    }

    @Override
    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException {
        Employee employee = retrieveEmployeebyId(employeeId);
        em.remove(employee);
    }

}
