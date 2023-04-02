/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasadministrationpanel;

import ejb.session.stateless.AuctionSessionBeanRemote;
import ejb.session.stateless.CreditPackageSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import java.text.ParseException;
import javax.ejb.EJB;

/**
 *
 * @author jamielee
 */
public class Main {
    
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    
    @EJB
    private static CreditPackageSessionBeanRemote creditPackageSessionBeanRemote;
    
    @EJB
    private static AuctionSessionBeanRemote auctionSessionBeanRemote;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException {
        MainApp mainApp = new MainApp(employeeSessionBeanRemote, creditPackageSessionBeanRemote, auctionSessionBeanRemote);
        mainApp.runApp();
    }
    
}
