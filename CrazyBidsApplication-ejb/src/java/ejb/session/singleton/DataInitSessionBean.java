/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AuctionSessionBeanLocal;
import ejb.session.stateless.CreditPackageSessionBeanLocal;
import ejb.session.stateless.SuccessfulAuctionSessionBeanLocal;
import entity.Auction;
import entity.CreditPackage;
import entity.SuccessfulAuction;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.AuctionNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Bransome Tan Yi Hao
 */
@Singleton
@LocalBean
@Startup

public class DataInitSessionBean {

    @EJB
    private SuccessfulAuctionSessionBeanLocal successfulAuctionSessionBeanLocal;

    @EJB
    private AuctionSessionBeanLocal auctionSessionBeanLocal;

    @EJB
    private CreditPackageSessionBeanLocal creditPackageSessionBeanLocal;
    
    @PersistenceContext(unitName = "CrazyBidsApplication-ejbPU")
    private EntityManager em;

    public DataInitSessionBean() {
    }

    @PostConstruct
    public void postConstruct()
    {
        // Get the current date
        Date currentDate = new Date();

        // Create a Calendar instance and set it to the current date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // Add 2 hours to the calendar
        calendar.add(Calendar.HOUR_OF_DAY, 2);

        // Get the new date with 2 hours added
        Date newDate = calendar.getTime();
        
        if(em.find(Auction.class, 1l) == null) {
            try {
                auctionSessionBeanLocal.createAuction(new Auction("book1", "life of book 1", currentDate, newDate, new BigDecimal(0), new BigDecimal(50), false, false));
                auctionSessionBeanLocal.createAuction(new Auction("book2", "life of book 2", currentDate, newDate, new BigDecimal(10), new BigDecimal(60), false, false));
                auctionSessionBeanLocal.createAuction(new Auction("book3", "life of book 3", currentDate, newDate, new BigDecimal(20), new BigDecimal(70), false, false));
                auctionSessionBeanLocal.createAuction(new Auction("book4", "life of book 4", currentDate, newDate, new BigDecimal(30), new BigDecimal(80), false, false));
                auctionSessionBeanLocal.createAuction(new Auction("book5", "life of book 5", currentDate, newDate, new BigDecimal(40), new BigDecimal(90), false, false));
            } catch (UnknownPersistenceException ex) {
                ex.printStackTrace();
            }
        }
       
        if(em.find(CreditPackage.class, 1l) == null) {
            try {
                creditPackageSessionBeanLocal.createCreditPackage(new CreditPackage(new BigDecimal(100), false));
                creditPackageSessionBeanLocal.createCreditPackage(new CreditPackage(new BigDecimal(200), false));
                creditPackageSessionBeanLocal.createCreditPackage(new CreditPackage(new BigDecimal(300), false));
                creditPackageSessionBeanLocal.createCreditPackage(new CreditPackage(new BigDecimal(400), false));
                creditPackageSessionBeanLocal.createCreditPackage(new CreditPackage(new BigDecimal(500), false));
            } catch (UnknownPersistenceException ex) {
                ex.printStackTrace();
            }
        }
        
        if(em.find(SuccessfulAuction.class, 1l) == null) {
            try {
                successfulAuctionSessionBeanLocal.createNewSuccessfulAuction(new SuccessfulAuction("book1", "life of book1", "-"), new Long(1), new Long(1));
                successfulAuctionSessionBeanLocal.createNewSuccessfulAuction(new SuccessfulAuction("book2", "life of book2", "-"), new Long(1), new Long(2));
            } catch (UnknownPersistenceException ex) {
                 ex.printStackTrace();
            } catch (CustomerNotFoundException ex) {
                 ex.printStackTrace();
            } catch (AuctionNotFoundException ex) {
                 ex.printStackTrace();
            }
        }
    }

    
    
}