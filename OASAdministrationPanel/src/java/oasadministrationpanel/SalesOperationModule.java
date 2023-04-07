/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasadministrationpanel;

import ejb.session.stateless.AuctionSessionBeanRemote;
import ejb.session.stateless.SuccessfulAuctionSessionBeanRemote;
import entity.Auction;
import entity.Bid;
import entity.Customer;
import entity.SuccessfulAuction;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.AuctionAssignedNoWinnerException;
import util.exception.AuctionHasBidsException;
import util.exception.AuctionIsDisabledException;
import util.exception.AuctionNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidAuctionCreationException;
import util.exception.GeneralException;
import util.exception.UpdateAuctionException;

/**
 *
 * @author jamielee
 */
public class SalesOperationModule {
    
    private AuctionSessionBeanRemote auctionSessionBeanRemote;
    private SuccessfulAuctionSessionBeanRemote successfulAuctionSessionBeanRemote;

    public SalesOperationModule() {
    }

    public SalesOperationModule(AuctionSessionBeanRemote auctionSessionBeanRemote, SuccessfulAuctionSessionBeanRemote successfulAuctionSessionBeanRemote) {
        this.auctionSessionBeanRemote = auctionSessionBeanRemote;
        this.successfulAuctionSessionBeanRemote = successfulAuctionSessionBeanRemote;
    }
    
    public void menuSalesOperation() throws ParseException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n*** Crazy Bids OAS Administration Panel :: Sales Staff Operation ***\n");
            System.out.println("1: Create New Auction Listing");
            System.out.println("2: View Auction Listing Details");
            System.out.println("3: View All Auction Listings");
            System.out.println("4: View All Auction Listings with Bids but Below Reserve Price");
            System.out.println("5: Back\n");
            response = 0;
            
            while(response < 1 || response > 5)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1)
                {
                    try
                    {
                        doCreateNewAuctionListing();
                    }
                    catch (InvalidAuctionCreationException ex)
                    {
                        System.out.println("\nInvalid Auction Creation: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    doViewAuctionListingDetails();
                }
                else if (response == 3)
                {
                    doViewAllAuctionListings();
                }
                else if (response == 4)
                {
                    doViewAllAuctionListingsWithBidsBelowReservePrice();
                }
                else if (response == 5)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if (response == 5)
            {
                break;
            }
        }
    }
    
    private void doCreateNewAuctionListing() throws InvalidAuctionCreationException
    {
        Scanner scanner = new Scanner(System.in);
        String name;
        String details;
        Date startDateTime;
        Date endDateTime;
        BigDecimal startingBid;
        BigDecimal reservePrice;
        
        System.out.println("*** Crazy Bids OAS Administration Panel :: Create New Auction Listing ***\n");
        System.out.print("Enter auction name> ");
        name = scanner.nextLine().trim();
        System.out.print("Enter auction details> ");
        details = scanner.nextLine().trim();
        
        startDateTime = createStartDateTime();
        endDateTime = createEndDateTime();
        
        System.out.print("Enter starting bid> ");
        startingBid = scanner.nextBigDecimal();
        
        System.out.print("Enter reserve price (enter 0 if no reserve price)> ");
        reservePrice = scanner.nextBigDecimal();
        
        if (name.length() > 0 && details.length() > 0 && startingBid.compareTo(new BigDecimal(0.01)) != -1 && reservePrice.compareTo(new BigDecimal(0)) != -1)
        {
            if (reservePrice.compareTo(new BigDecimal(0)) == 0)
            {
                reservePrice = null;
            }
            
            Auction newAuction = new Auction(name, details, startDateTime, endDateTime, startingBid, reservePrice, false, false, false);

            try
            {
                Long auctionId = auctionSessionBeanRemote.createAuction(newAuction);
                System.out.println("\nAuction listing created successfully!: " + auctionId + "\n");
            }
            catch (GeneralException ex)
            {
                System.out.println("\nAn unknown error has occurred while creating the new auction listing!: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            throw new InvalidAuctionCreationException("\nMissing details to create auction!");
        }
    }
    
    private Date createStartDateTime() {
        Scanner scanner = new Scanner(System.in);
        
        while (true)
        {
            System.out.print("Enter start date (in the format DD-MM-YYYY)> ");
            String startDate = scanner.nextLine().trim();
            
            System.out.print("Enter start hour (00 to 23)> ");
            String startHour = scanner.nextLine().trim();
            if (Integer.parseInt(startHour) > 23)
            {
                System.out.println("Please enter a valid hour!");
                continue;
            }
            
            System.out.print("Enter start minute (00 to 59)> ");
            String startMinute = scanner.nextLine().trim();
            if (Integer.parseInt(startMinute) > 59)
            {
                System.out.println("Please enter a valid minute!");
                continue;
            }
            
            try
            {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date startDateTime = formatter.parse(startDate + " " + startHour + ":" + startMinute);
                return startDateTime;
            }
            catch (ParseException ex)
            {
                System.out.println("Start date & time could not be parsed! Please enter date & time in given format.");
            }
        }
    }
    
    private Date createEndDateTime() {
        Scanner scanner = new Scanner(System.in);
        
        while (true)
        {
            System.out.print("Enter end date (in the format DD-MM-YYYY)> ");
            String endDate = scanner.nextLine().trim();
            
            System.out.print("Enter end hour (00 to 23)> ");
            String endHour = scanner.nextLine().trim();
            if (Integer.parseInt(endHour) > 23)
            {
                System.out.println("Please enter a valid hour!");
                continue;
            }
            
            System.out.print("Enter end minute (00 to 59)> ");
            String endMinute = scanner.nextLine().trim();
            if (Integer.parseInt(endMinute) > 59)
            {
                System.out.println("Please enter a valid minute!");
                continue;
            }
            
            try
            {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date endDateTime = formatter.parse(endDate + " " + endHour + ":" + endMinute);
                return endDateTime;
            }
            catch (ParseException ex)
            {
                System.out.println("End date & time could not be parsed! Please enter date & time in given format.");
            }
        }
    }
    
    private void doViewAuctionListingDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Crazy Bids OAS Administration Panel :: View Auction Listing Details ***\n");
        System.out.print("Enter Auction ID> ");
        Long auctionId = scanner.nextLong();
        
        try
        {
            Auction auction = auctionSessionBeanRemote.retrieveAuctionbyId(auctionId);
            
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%10s%20s%20s%20s%20s%20s%20s%20s%20s%20s\n", "Auction ID", "Auction Name", "Details", "Start Date & Time", "End Date & Time", "Starting Bid", "Reserve Price", "Is Disabled", "Manual Intervention", "Has Winner");
            
            String reservePrice = auction.getReservePrice() == null ? "-" : auction.getReservePrice().toString();
            Boolean hasWinner = auction.getSuccessfulAuction() != null;
            System.out.printf("%10s%20s%20s%20s%20s%20s%20s%20s%20s%20s\n", auction.getId().toString(), auction.getName(),auction.getDetails(), auction.getStartDateTime().toString(), auction.getEndDateTime().toString(), auction.getStartingBid().toString(), reservePrice, auction.getIsDisabled(), auction.getManualIntervention(), hasWinner);
            
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("1: Update Auction Listing");
            System.out.println("2: Delete Auction Listing");
            System.out.println("3: Back\n");
            
            while (response < 1 || response > 3)
            {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1)
                {
                    doUpdateAuctionListing(auction);
                }
                else if (response == 2)
                {
                    doDeleteAuctionListing(auction);
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
        catch(AuctionNotFoundException ex)
        {
            System.out.println("\nAn error has occurred while retrieving auction: " + ex.getMessage() + "\n");
        }
    }
    
    private void doViewAllAuctionListings()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Crazy Bids OAS Administration Panel :: View All Auction Listings ***\n");
        
        List<Auction> auctions = auctionSessionBeanRemote.retrieveAllAuctions();
        
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        if (!auctions.isEmpty())
        {
            System.out.printf("%10s%20s%20s%20s%20s%20s%20s%20s%20s%20s\n", "Auction ID", "Auction Name", "Details", "Start Date & Time", "End Date & Time", "Starting Bid", "Reserve Price", "Is Disabled", "Manual Intervention", "Has Winner");

            for(Auction auction : auctions)
            {
                String reservePrice = auction.getReservePrice() == null ? "-" : auction.getReservePrice().toString();
                Boolean hasWinner = auction.getSuccessfulAuction() != null;
                System.out.printf("%10s%20s%20s%20s%20s%20s%20s%20s%20s%20s\n", auction.getId().toString(), auction.getName(),auction.getDetails(), auction.getStartDateTime().toString(), auction.getEndDateTime().toString(), auction.getStartingBid().toString(), reservePrice, auction.getIsDisabled(), auction.getManualIntervention(), hasWinner);
            }
        }
        else
        {
            System.out.println("No auction listings found!");
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    
    private void doViewAllAuctionListingsWithBidsBelowReservePrice()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Crazy Bids OAS Administration Panel :: View All Auction Listings With Bids But Below Reserve Price ***\n");
        
        List<Auction> auctions = auctionSessionBeanRemote.retrieveAllAuctionsWithBidsBelowReservePrice();
        
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        
        if (!auctions.isEmpty())
        {
            for(Auction auction : auctions)
            {
                System.out.printf("%10s%20s%20s%20s%20s%20s%20s%20s\n", "Auction ID", "Auction Name", "Details", "Start Date & Time", "End Date & Time", "Starting Bid", "Reserve Price", "Is Disabled");
                
                String reservePrice = auction.getReservePrice() == null ? "-" : auction.getReservePrice().toString();
                System.out.printf("%10s%20s%20s%20s%20s%20s%20s%20s\n", auction.getId().toString(), auction.getName(),auction.getDetails(), auction.getStartDateTime().toString(), auction.getEndDateTime().toString(), auction.getStartingBid().toString(), reservePrice, auction.getIsDisabled());
                
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
                
                Bid highestBid = auctionSessionBeanRemote.getHighestBid(auction);
                Customer winner = highestBid.getCustomer();
                System.out.println("*** Highest Bid ***");
                System.out.printf("%10s%20s%20s\n", "Bid ID", "Customer Username", "Bid Amount");
                System.out.printf("%10s%20s%20s\n", highestBid.getId(), winner.getUsername(), highestBid.getBidAmount());
                
                while(true)
                {
                    System.out.println("Mark highest bid as winning bid?");
                    System.out.print("Enter 'Y' to assign winner, 'N' to mark auction listing as having no winning bid> ");
                    String input = scanner.nextLine().trim();

                    if (input.equals("Y"))
                    {
                        SuccessfulAuction successfulAuction = new SuccessfulAuction();
                        successfulAuction.setSuccessfulAuctionName(auction.getName());
                        successfulAuction.setSuccessfulAuctionDetails(auction.getDetails());
                        
                        try
                        {
                            Long successfulAuctionId = successfulAuctionSessionBeanRemote.createNewSuccessfulAuction(successfulAuction, winner.getId(), auction.getId());
                            System.out.println("\nWinning bid assigned successfully! Successful auction created with ID: " + successfulAuctionId + "\n");
                        }
                        catch(GeneralException | CustomerNotFoundException | AuctionNotFoundException ex)
                        {
                            System.out.println("An error occurred while creating successful auction: " + ex.getMessage());
                        }
                    }
                    else if (input.equals("N"))
                    {
                        try
                        {
                            auctionSessionBeanRemote.assignNoWinner(auction.getId());
                            System.out.println("Auction listing successfully marked as having no winning bid!");
                        }
                        catch(AuctionNotFoundException | AuctionAssignedNoWinnerException ex)
                        {
                            System.out.println("An error occurred while marking auction listing as having no winning bid: " + ex.getMessage());
                        }
                    }
                    else
                    {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }
            }
        }
        else
        {
            System.out.println("No auction listings with bids but below reserve price found!");
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    private void doUpdateAuctionListing(Auction updateAuction) {
        Scanner scanner = new Scanner(System.in);        
        String input = "";
        
        System.out.println("\n*** Crazy Bids OAS Administration Panel :: Update Auction Listing ***\n");
        System.out.print("Enter name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            updateAuction.setName(input);
        }
        
        System.out.print("Enter details (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            updateAuction.setDetails(input);
        }
        
        if(updateAuction.getBids().isEmpty())
        {
            System.out.printf("Change start date & time? (Enter 'Y' to confirm, 'N' to skip)> ");
            input = scanner.nextLine().trim();
        
            if(input.equals("Y"))
            {
                Date startDateTime = createStartDateTime();
                updateAuction.setStartDateTime(startDateTime);
            }
            
            System.out.printf("Change end date & time? (Enter 'Y' to change, 'N' to skip)> ");
            input = scanner.nextLine().trim();
        
            if(input.equals("Y"))
            {
                Date endDateTime = createEndDateTime();
                updateAuction.setEndDateTime(endDateTime);
            }
            
            System.out.print("Enter starting bid (enter 0 if no change)> ");
            BigDecimal input1 = scanner.nextBigDecimal();

            if (input1.compareTo(new BigDecimal(0)) == 1)
            {
                updateAuction.setStartingBid(input1);
            }
                   
            System.out.print("Enter reserve price (enter 0 if no change)> ");
            input1 = scanner.nextBigDecimal();

            if (input1.compareTo(new BigDecimal(0)) == 1)
            {
                updateAuction.setReservePrice(input1);
            }
        }
        
        try
        {
            auctionSessionBeanRemote.updateAuction(updateAuction);
            System.out.println("\nAuction listing updated successfully!\n");
        }
        catch (AuctionNotFoundException | UpdateAuctionException ex) 
        {
            System.out.println("\nAn error has occurred while updating auction: " + ex.getMessage() + "\n");
        }
    }
    
    private void doDeleteAuctionListing(Auction deleteAuction) {
        Scanner scanner = new Scanner(System.in);     
        String input = "";
        
        System.out.println("\n*** Crazy Bids Auction Client System :: Delete Auction Listing ***\n");
        System.out.printf("Confirm Delete Auction Listing (Address ID: %s) (Enter 'Y' to Delete)> ", deleteAuction.getId());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try 
            {
                auctionSessionBeanRemote.deleteAuction(deleteAuction.getId());
                System.out.println("\nAuction listing deleted successfully!\n");
            } 
            catch (AuctionNotFoundException ex) 
            {
                System.out.println("\nAn error has occurred while deleting the auction listing: " + ex.getMessage() + "\n");
            }
            catch (AuctionHasBidsException ex) 
            {
                System.out.printf("\nCannot delete auction listing (Auction ID: %s) as there are bids associated! Do you want to disable the auction listing instead? (Enter 'Y' to Disable))> ", deleteAuction.getId());
                input = scanner.nextLine().trim();
                
                if (input.equals("Y")) {
                    try
                    {
                        auctionSessionBeanRemote.disableAuction(deleteAuction.getId());
                        System.out.println("\nAuction listing disabled successfully!\n");
                    } 
                    catch (AuctionNotFoundException | AuctionIsDisabledException ex1) {
                        System.out.println("\nAn error has occurred while disabling the auction listing: " + ex1.getMessage() + "\n");
                    }
                } else {
                    System.out.println("\nAuction listing NOT disabled!\n");
                }
            }
        }
        else
        {
            System.out.println("Auction listing NOT deleted!\n");
        }
    }
}
