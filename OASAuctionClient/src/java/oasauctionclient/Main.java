/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasauctionclient;

import ejb.session.stateless.AddressSessionBeanRemote;
import ejb.session.stateless.AuctionSessionBeanRemote;
import ejb.session.stateless.CreditPackageSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.SuccessfulAuctionSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author jamielee
 */
public class Main {

    @EJB
    private static AuctionSessionBeanRemote auctionSessionBeanRemote;

    @EJB
    private static SuccessfulAuctionSessionBeanRemote successfulAuctionSessionBeanRemote;

    @EJB
    private static AddressSessionBeanRemote addressSessionBeanRemote;

    @EJB
    private static CreditPackageSessionBeanRemote creditPackageSessionBeanRemote;

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(auctionSessionBeanRemote, successfulAuctionSessionBeanRemote, addressSessionBeanRemote, creditPackageSessionBeanRemote, customerSessionBeanRemote);
        mainApp.runApp();
    }
    
}
