/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.Employee;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author jamielee
 */
@Singleton
@LocalBean
@Startup

public class AdminDataInitSessionBean {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    
    @PersistenceContext(unitName = "CrazyBidsApplication-ejbPU")
    private EntityManager em;

    public AdminDataInitSessionBean() {
    }
    
    @PostConstruct
    public void PostConstruct() {
        if(em.find(Employee.class, 1l) == null) {
            try {
                employeeSessionBeanLocal.createNewEmployee(new Employee("System", "Admin 1", AccessRightEnum.SYSTEMADMIN, "systemadmin1", "password"));
            } catch (UnknownPersistenceException ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
