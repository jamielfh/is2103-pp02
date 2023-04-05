/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasauctionclient;

import ejb.session.stateless.AuctionSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.SuccessfulAuctionSessionBeanRemote;
import entity.Auction;
import entity.Customer;
import entity.SuccessfulAuction;
import java.text.NumberFormat;
import java.util.List;
import java.util.Scanner;
import util.exception.AddressNotFoundException;
import util.exception.AuctionNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.DeliveryAddressExistException;
import util.exception.SuccessfulAuctionNotFoundException;
import util.exception.UpdateDeliveryAddressException;

/**
 *
 * @author Bransome Tan Yi Hao
 */
public class AuctionOperationModule {
    
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private AuctionSessionBeanRemote auctionSessionBeanRemote;
    private SuccessfulAuctionSessionBeanRemote successfulAuctionSessionBeanRemote;
    private Customer currentCustomer;
    
    public AuctionOperationModule() {
    }

    public AuctionOperationModule(CustomerSessionBeanRemote customerSessionBeanRemote, AuctionSessionBeanRemote auctionSessionBeanRemote, SuccessfulAuctionSessionBeanRemote successfulAuctionSessionBeanRemote, Customer currentCustomer) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.auctionSessionBeanRemote = auctionSessionBeanRemote;
        this.successfulAuctionSessionBeanRemote = successfulAuctionSessionBeanRemote;
        this.currentCustomer = currentCustomer;
    }
    
    public void menuAuctionOperation() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Crazy Bids Auction Client System :: Auction Operation ***\n");
            System.out.println("1: Browse All Auction Listings");
            System.out.println("2: View Auction Listing Details");
            System.out.println("3: Browse Won Auction Listings");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doBrowseAuctionListings();
                }
                else if(response == 2)
                {
                    doViewAuctionListingDetails(null);
                }
                else if(response == 3)
                {
                    doBrowseWonAuctionListings();
                }
                else if(response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }

    private void doBrowseAuctionListings() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Crazy Bids Auction Client System :: Browse All Auction Listings ***\n");
         
        List<Auction> auctions = auctionSessionBeanRemote.retrieveAllActiveAuctions();
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%10s%20s%20s%20s%20s%20s%20s\n", "Auction ID", "Auction Name", "Auction Details", "Auction Start Date Time", "Auction End Date Time", "Auction Starting Bid", "Auction Reserve Price");

        for(Auction auction: auctions)
        {
            System.out.printf("%10s%20s%20s%20s%20s%20s%20s\n", auction.getId(), auction.getName(), auction.getDetails(), auction.getStartDateTime(), auction.getEndDateTime(), NumberFormat.getCurrencyInstance().format(auction.getStartingBid()), NumberFormat.getCurrencyInstance().format(auction.getReservePrice()));
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        if (auctions.isEmpty()) {
            System.out.println("There are currently no auctions available");
        } 
        
        System.out.print("Press any key to go back...> ");
        scanner.nextLine();
    }

    private void doViewAuctionListingDetails(Long auctionId) {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Crazy Bids Auction Client System :: View Auction Listing Details ***\n");
        
        // Purpose of this is to check if refresh bids is initiaited
        if(auctionId == null) {
            System.out.print("Enter Auction ID> ");
            auctionId = scanner.nextLong();
        } else {
            System.out.println("*** Successfully Refresh Auction Listing Bids! ***");
        }
      
        try
        {
            Auction auction = auctionSessionBeanRemote.retrieveAuctionbyId(auctionId);
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%10s%20s%20s%20s%20s%20s%20s\n", "Auction ID", "Auction Name", "Auction Details", "Auction Start Date Time", "Auction End Date Time", "Auction Starting Bid", "Auction Reserve Price");
            System.out.printf("%10s%20s%20s%20s%20s%20s%20s\n", auction.getId(), auction.getName(), auction.getDetails(), auction.getStartDateTime(), auction.getEndDateTime(), NumberFormat.getCurrencyInstance().format(auction.getStartingBid()), NumberFormat.getCurrencyInstance().format(auction.getReservePrice()));
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("1: Place New Bid");
            System.out.println("2: Refresh Auction Listing Bids");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if(response == 1)
            {
                doPlaceNewBid(auction);
            }
            else if(response == 2)
            {
                doRefreshAuctionListingBids(auction);
                System.out.println("Auction Listing Bids are refreshed successfully");
            }
        }
        catch(AuctionNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving auction: " + ex.getMessage() + "\n");
        }
    }

    private void doBrowseWonAuctionListings() {
        Scanner scanner = new Scanner(System.in);
        int response = 0;
        
        System.out.println("*** Crazy Bids Auction Client System :: Browse Won Auction Listings ***\n");
         
        try {
            
            List<SuccessfulAuction> successfulAuctions = successfulAuctionSessionBeanRemote.retrieveAllSuccessfulAuctionByCustomerId(currentCustomer.getId());
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%10s%30s%30s%20s\n", "Successful Auction ID", "Successful Auction Name", "Sucessful Auction Details", "Delivery Address");
            
            for(SuccessfulAuction successfulAuction: successfulAuctions)
            {
                System.out.printf("%10s%30s%30s%20s\n", successfulAuction.getId(), successfulAuction.getSuccessfulAuctionName(), successfulAuction.getSuccessfulAuctionDetails(), successfulAuction.getSuccessfulAuctionDeliveryAddress());
            }
            
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");

            if (successfulAuctions.isEmpty()) {
                System.out.println("You do not have any won auction listings available");
            } else {
             
                System.out.println("1: Select Delivery Address for a Won Auction Listing");
                System.out.println("2: Back\n");
                System.out.print(">");
                response = scanner.nextInt();

                if(response == 1)
                {
                    doSelectDeliveryAddress();
                }
            } 
            
        } catch (CustomerNotFoundException ex) {
            System.out.println("An error has occurred while retrieving successful auction: " + ex.getMessage() + "\n");
        }
        
    }

    private void doPlaceNewBid(Auction auction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doRefreshAuctionListingBids(Auction auction) {
        doViewAuctionListingDetails(auction.getId());
    }

    private void doSelectDeliveryAddress() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Crazy Bids Auction Client System :: Browse Won Auction Listings :: Select Delivery Address for Won Auction Listing ***\n");
        System.out.print("Enter Won Auction ID> ");
        Long auctionId = scanner.nextLong();
        System.out.print("Enter Delivery Address ID to Link to Won Auction> ");
        Long addressId = scanner.nextLong();
        
        try {
            successfulAuctionSessionBeanRemote.updateDeliveryAddress(auctionId, addressId);
            System.out.println("\nDelivery Address for Won Auction Listing is successfully linked! \n");
        } catch (DeliveryAddressExistException ex) {
            System.out.println("\n" + ex.getMessage() + "\n");
        } catch (UpdateDeliveryAddressException ex) {
            System.out.println("\n" + ex.getMessage() + "\n");
        } catch (SuccessfulAuctionNotFoundException ex) {
            System.out.println("\n" + ex.getMessage() + "\n");
        } catch (AddressNotFoundException ex) {
            System.out.println("\n" + ex.getMessage() + "\n");
        }
        
    }
}
