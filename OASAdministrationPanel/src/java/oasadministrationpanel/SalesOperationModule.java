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
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.AuctionAlreadyClosedException;
import util.exception.AuctionHasBidsException;
import util.exception.AuctionIsDisabledException;
import util.exception.AuctionNotFoundException;
import util.exception.BidNotFoundException;
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
    
    public void menuSalesOperation() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true)
        {
            System.out.println("\n*** Crazy Bids OAS Administration Panel :: Sales Operations ***\n");
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
                        System.out.println("\nAn error has occurred while creating the new auction: " + ex.getMessage() + "\n");
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
                    doViewAllAuctionListingsWithBidsButBelowReservePrice();
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
        
        System.out.println("\n*** Crazy Bids OAS Administration Panel :: Create New Auction Listing ***\n");
        System.out.print("Enter auction name> ");
        name = scanner.nextLine().trim();
        System.out.print("Enter auction details> ");
        details = scanner.nextLine().trim();
        
        startDateTime = createStartDateTime();
        
        while (true)
        {
            endDateTime = createEndDateTime();
            
            if (!endDateTime.after(startDateTime))
            {
                System.out.println("\nEnd date time must be later than start date time!\n");
            }
            else
            {
                break;
            }
        }
        
        while (true)
        {
            System.out.print("Enter starting bid (up to 4 decimal places)> ");
            startingBid = new BigDecimal(scanner.nextLine().trim());
            
            if (startingBid.compareTo(new BigDecimal("0.01")) != -1)
            {
                break;
            }
            else
            {
                System.out.println("Starting bid must be at least 0.01!");
            }
        }
        
        System.out.printf("Enter reserve price? (Enter 'Y' to confirm, any other key to skip)> ");
        String input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            while (true)
            {
                System.out.print("Enter reserve price> ");
                reservePrice = new BigDecimal(scanner.nextLine().trim());
                
                if (reservePrice.compareTo(new BigDecimal("0.01")) != -1)
                {
                    break;
                }
                else
                {
                    System.out.println("Reserve price must be at least 0.01!");
                }
            }
        }
        else
        {
            reservePrice = null;
        }
        
        if (name.length() > 0 && details.length() > 0)
        {
            
            Auction newAuction = new Auction(name, details, startDateTime, endDateTime, startingBid, reservePrice, false, false, false);

            try
            {
                Long auctionId = auctionSessionBeanRemote.createAuction(newAuction);
                System.out.println("\nAuction listing created successfully!: " + auctionId + "\n");
            }
            catch (GeneralException ex)
            {
                System.out.println("\nAn unknown error has occurred while creating the new auction listing: " + ex.getMessage() + "\n");
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
            if (startHour.equals("") | Integer.parseInt(startHour) < 0 || Integer.parseInt(startHour) > 23)
            {
                System.out.println("Please enter a valid hour!");
                continue;
            }
            
            System.out.print("Enter start minute (00 to 59)> ");
            String startMinute = scanner.nextLine().trim();
            if (startMinute.equals("") | Integer.parseInt(startMinute) < 0 || Integer.parseInt(startMinute) > 59)
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
            if (Integer.parseInt(endHour) < 0 || Integer.parseInt(endHour) > 23)
            {
                System.out.println("Please enter a valid hour!");
                continue;
            }
            
            System.out.print("Enter end minute (00 to 59)> ");
            String endMinute = scanner.nextLine().trim();
            if (Integer.parseInt(endMinute) < 0 || Integer.parseInt(endMinute) > 59)
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
        
        System.out.println("\n*** Crazy Bids OAS Administration Panel :: View Auction Listing Details ***\n");
        System.out.print("Enter Auction ID> ");
        Long auctionId = scanner.nextLong();
        
        try
        {
            Auction auction = auctionSessionBeanRemote.retrieveAuctionbyId(auctionId);
            
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%10s%20s%20s%30s%30s%20s%20s%15s%25s%15s\n", "Auction ID", "Auction Name", "Auction Details", "Start Date & Time", "End Date & Time", "Starting Bid", "Reserve Price", "Disabled", "Manual Intervention", "Closed");
            
            String reservePrice = auction.getReservePrice() == null ? "-" : NumberFormat.getCurrencyInstance().format(auction.getReservePrice());
            System.out.printf("%10s%20s%20s%30s%30s%20s%20s%15s%25s%15s\n", auction.getId().toString(), auction.getName(),auction.getDetails(), auction.getStartDateTime().toString(), auction.getEndDateTime().toString(), NumberFormat.getCurrencyInstance().format(auction.getStartingBid()), reservePrice, auction.getIsDisabled(), auction.getManualIntervention(), auction.getIsClosed());
            
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
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
        
        System.out.println("\n*** Crazy Bids OAS Administration Panel :: View All Auction Listings ***\n");
        
        List<Auction> auctions = auctionSessionBeanRemote.retrieveAllAuctions();
        
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        if (!auctions.isEmpty())
        {
            System.out.printf("%10s%20s%20s%30s%30s%20s%20s%15s%25s%15s\n", "Auction ID", "Auction Name", "Auction Details", "Start Date & Time", "End Date & Time", "Starting Bid", "Reserve Price", "Disabled", "Manual Intervention", "Closed");

            for(Auction auction : auctions)
            {
                String reservePrice = auction.getReservePrice() == null ? "-" : NumberFormat.getCurrencyInstance().format(auction.getReservePrice());
                System.out.printf("%10s%20s%20s%30s%30s%20s%20s%15s%25s%15s\n", auction.getId().toString(), auction.getName(),auction.getDetails(), auction.getStartDateTime().toString(), auction.getEndDateTime().toString(), NumberFormat.getCurrencyInstance().format(auction.getStartingBid()), reservePrice, auction.getIsDisabled(), auction.getManualIntervention(), auction.getIsClosed());
            }
        }
        else
        {
            System.out.println("No auction listings found!");
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    
    private void doViewAllAuctionListingsWithBidsButBelowReservePrice()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n*** Crazy Bids OAS Administration Panel :: View All Auction Listings With Bids But Below Reserve Price ***\n");
        
        List<Auction> auctions = auctionSessionBeanRemote.retrieveAllAuctionsWithBidsButBelowReservePrice();
        
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
        
        if (!auctions.isEmpty())
        {
            for(Auction auction : auctions)
            {
                System.out.printf("%10s%20s%20s%30s%30s%20s%20s\n", "Auction ID", "Auction Name", "Auction Details", "Start Date & Time", "End Date & Time", "Starting Bid", "Reserve Price");
                
                String reservePrice = auction.getReservePrice() == null ? "-" : NumberFormat.getCurrencyInstance().format(auction.getReservePrice());
                System.out.printf("%10s%20s%20s%30s%30s%20s%20s\n", auction.getId().toString(), auction.getName(),auction.getDetails(), auction.getStartDateTime().toString(), auction.getEndDateTime().toString(), NumberFormat.getCurrencyInstance().format(auction.getStartingBid()), reservePrice);
                
                System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
                
                Bid highestBid = auctionSessionBeanRemote.getHighestBid(auction);
                Customer winner = highestBid.getCustomer();
                System.out.println("\n*** Highest Bid ***");
                System.out.printf("%10s%25s%20s\n", "Bid ID", "Customer Username", "Bid Amount");
                System.out.printf("%10s%25s%20s\n", highestBid.getId(), winner.getUsername(), highestBid.getBidAmount());
                
                while (true)
                {
                    System.out.println("\nMark highest bid as winning bid?");
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
                            try
                            {
                                auctionSessionBeanRemote.manuallyCloseAuction(auction.getId());
                                System.out.println("\nWinning bid assigned successfully! Successful auction created with ID: " + successfulAuctionId + "\n");
                            }
                            catch (AuctionAlreadyClosedException | AuctionNotFoundException ex1)
                            {
                                System.out.println("An error occurred while creating successful auction: " + ex1.getMessage());
                            }
                        }
                        catch(GeneralException | CustomerNotFoundException | AuctionNotFoundException ex)
                        {
                            System.out.println("An error occurred while creating successful auction: " + ex.getMessage());
                        }
                        finally
                        {
                            break;
                        }
                    }
                    else if (input.equals("N"))
                    {
                        try
                        {
                            auctionSessionBeanRemote.manuallyCloseAuction(auction.getId());
                            auctionSessionBeanRemote.doRefund(auctionSessionBeanRemote.getHighestBid(auction));
                            System.out.println("Auction listing successfully marked as having no winning bid!");
                            break;
                        }
                        catch(AuctionNotFoundException | AuctionAlreadyClosedException | BidNotFoundException | CustomerNotFoundException ex)
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
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
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
            System.out.printf("Change start date & time? (Enter 'Y' to confirm, any other key to skip)> ");
            input = scanner.nextLine().trim();
        
            if(input.equals("Y"))
            {
                Date startDateTime = createStartDateTime();
                updateAuction.setStartDateTime(startDateTime);
            }
            
            System.out.printf("Change end date & time? (Enter 'Y' to change, any other key to skip)> ");
            input = scanner.nextLine().trim();
        
            if(input.equals("Y"))
            {
                while (true)
                {
                    Date endDateTime = createEndDateTime();

                    if (!endDateTime.after(updateAuction.getStartDateTime()))
                    {
                        System.out.println("\nEnd date time must be later than start date time!\n");
                    }
                    else
                    {
                        updateAuction.setEndDateTime(endDateTime);
                        break;
                    }
                }
            }
            
            System.out.printf("Change starting bid? (Enter 'Y' to change, any other key to skip)> ");
            input = scanner.nextLine().trim();
            
            if(input.equals("Y"))
            {
                while (true)
                {
                    System.out.print("Enter starting bid (up to 4 decimal places)> ");
                    BigDecimal input1 = new BigDecimal(scanner.nextLine().trim());

                    if (input1.compareTo(new BigDecimal("0.01")) != -1)
                    {
                        updateAuction.setStartingBid(input1);
                        break;
                    }
                    else
                    {
                        System.out.println("Starting bid must be at least 0.01!");
                    }
                }
            }
            
            System.out.printf("Change reserve price? (Enter 'Y' to change, any other key to skip)> ");
            input = scanner.nextLine().trim();
            
            if(input.equals("Y"))
            {
                while (true)
                {
                    System.out.print("Enter reserve price (up to 4 decimal places)> ");
                    BigDecimal input1 = new BigDecimal(scanner.nextLine().trim());

                    if (input1.compareTo(new BigDecimal("0.01")) != -1)
                    {
                        updateAuction.setReservePrice(input1);
                        break;
                    }
                    else
                    {
                        System.out.println("Reserve price must be at least 0.01!");
                    }
                }
            }
        }
        else
        {
            System.out.println("Start date & time, end date & time, starting bid and reserve price cannot be changed as there are bids associated with auction!");
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
        System.out.printf("Confirm Delete Auction Listing (Auction ID: %s) (Enter 'Y' to Delete)> ", deleteAuction.getId());
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
                    } catch (BidNotFoundException ex1) {
                        System.out.println("\nAn error has occurred while disabling the auction listing: " + ex1.getMessage() + "\n");
                    } catch (CustomerNotFoundException ex1) {
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
