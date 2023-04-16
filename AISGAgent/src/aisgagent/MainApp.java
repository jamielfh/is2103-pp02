/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aisgagent;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import util.exception.InvalidLoginCredentialException;
import util.exception.InvalidRegistrationException;
import ws.soap.PremiumCustomer.Auction;
import ws.soap.PremiumCustomer.AuctionNotFoundException_Exception;
import ws.soap.PremiumCustomer.Bid;
import ws.soap.PremiumCustomer.BidNotFoundException_Exception;
import ws.soap.PremiumCustomer.Customer;
import ws.soap.PremiumCustomer.CustomerNotFoundException_Exception;
import ws.soap.PremiumCustomer.CustomerTierEnum;
import ws.soap.PremiumCustomer.InvalidBidException_Exception;
import ws.soap.PremiumCustomer.InvalidLoginCredentialException_Exception;
import ws.soap.PremiumCustomer.NotEnoughCreditException_Exception;
import ws.soap.PremiumCustomer.PremiumCustomerWebService;
import ws.soap.PremiumCustomer.SuccessfulAuction;
import ws.soap.PremiumCustomer.UpdateCustomerException_Exception;

/**
 *
 * @author Bransome Tan Yi Hao
 */
public class MainApp {

    private PremiumCustomerWebService premiumCustomerWebServicePort;
    private Customer currentPremiumCustomer;

    public MainApp() {
    }

    public MainApp(PremiumCustomerWebService premiumCustomerWebServicePort) {
        this.premiumCustomerWebServicePort = premiumCustomerWebServicePort;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to Premium Bidding cum Sniping Agent System ***\n");
            System.out.println("1: Premium Registration");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doPremiumRegister();
                    } catch (InvalidRegistrationException ex) {
                        System.out.println("\nInvalid Register credential: " + ex.getMessage() + "\n");
                    }

                } else if (response == 2) {
                    try {
                        doLogin();

                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("\nInvalid login credential: " + ex.getMessage() + "\n");
                    } catch (InvalidLoginCredentialException_Exception ex) {
                        System.out.println("\nInvalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 3) {
                break;
            }
        }
    }

    private void doLogin() throws InvalidLoginCredentialException, InvalidLoginCredentialException_Exception {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** Premium Bidding cum Sniping Agent System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            try {
                Customer pCustomer = premiumCustomerWebServicePort.retrieveCustomerbyUsername(username);
                if (pCustomer.getCustomerTierEnum().equals(CustomerTierEnum.STANDARD)) {
                    System.out.println("\nSorry, only Premium Customers are allowed to login to use our system!\n");
                } else {
                    currentPremiumCustomer = premiumCustomerWebServicePort.customerLogin(username, password);
                    System.out.println("\nLogin successful!\n");
                    menuMain();
                }
            } catch (CustomerNotFoundException_Exception | UpdateCustomerException_Exception ex) {
                 System.out.println("An error has occurred while retrieving customer: " + ex.getMessage() + "\n");
            }
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void doPremiumRegister() throws InvalidRegistrationException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** Premium Bidding cum Sniping Agent System :: Premium Registration ***\n");
        System.out.print("To Upgrade to Premium, Enter Your Customer Username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter Your Password for Verification Purposes> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            try {
                Customer pCustomer = premiumCustomerWebServicePort.retrieveCustomerbyUsername(username);
                if (!pCustomer.getPassword().equals(password)) {
                    System.out.println("\nSorry, your password does not match with the account!\n");
                } else if (pCustomer.getCustomerTierEnum().equals(CustomerTierEnum.PREMIUM)) {
                    System.out.println("\nYou are already a Premium Member!\n");
                } else {
                    pCustomer.setCustomerTierEnum(CustomerTierEnum.PREMIUM);
                    premiumCustomerWebServicePort.updatePremiumCustomer(pCustomer);
                    System.out.println("\nYou are registered as Premium Member successfully!\n");
                }
            } catch (CustomerNotFoundException_Exception ex) {
                System.out.println("\n" + ex.getMessage() + "\n");
            } catch (UpdateCustomerException_Exception ex) {
                System.out.println("An error has occurred while updating: " + ex.getMessage() + "\n");
            }
        } else {
            throw new InvalidRegistrationException("Missing Register credential!");
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to Premium Bidding cum Sniping Agent System ***\n");
            System.out.println("You are login as " + currentPremiumCustomer.getFirstName() + " " + currentPremiumCustomer.getLastName() + "\n");
            System.out.println("1: View Credit Balance");
            System.out.println("2: View Auction Listing Details");
            System.out.println("3: Browse All Auction Listings");
            System.out.println("4: View Won Auction Listings");
            System.out.println("5: Logout\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doViewCreditBalance();
                } else if (response == 2) {
                    doViewAuctionListingDetails();
                } else if (response == 3) {
                    doBrowseAllAuctionListings();
                } else if (response == 4) {
                    doViewWonAuctionListings();
                }
                else if (response == 5)
                {
                    try
                    {
                        premiumCustomerWebServicePort.customerLogout(currentPremiumCustomer.getId());
                        currentPremiumCustomer = null;
                    }
                    catch (CustomerNotFoundException_Exception ex)
                    {
                        System.out.println("An error occurred while logging out customer: " + ex.getMessage() + "\n");
                    }
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 5) {
                break;
            }
        }
    }

    private void doViewCreditBalance() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** Premium Bidding cum Sniping Agent System :: View My Credit Balance ***\n");

        try {
            Customer pCustomer = premiumCustomerWebServicePort.retrieveCustomerbyId(currentPremiumCustomer.getId());
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%20s\n", "Your Credit Balance");
            System.out.printf("%20s\n", NumberFormat.getCurrencyInstance().format(pCustomer.getCreditBalance()));
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        } catch (CustomerNotFoundException_Exception ex) {
            System.out.println("An error has occurred while retrieving customer: " + ex.getMessage() + "\n");
        }

        System.out.print("Press any key to go back...>");
        scanner.nextLine();

    }

    private void doViewAuctionListingDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        long auctionId;

        System.out.println("*** Premium Bidding cum Sniping Agent System :: View Auction Listing Details ***\n");

        System.out.print("Enter Auction ID> ");
        auctionId = scanner.nextLong();

        try {
            Auction auction = premiumCustomerWebServicePort.retrieveAuctionbyId(auctionId);
            if (auction.isIsDisabled() || auction.isManualIntervention() || auction.isIsClosed()) {
                System.out.println("\nThis auction is not available for viewing as it is already closed!\n");
            } else {
                List<Bid> bids = auction.getBids();
                System.out.println("Auction");
                System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("%10s%20s%20s%30s%30s%25s%25s\n", "Auction ID", "Auction Name", "Auction Details", "Auction Start Date Time", "Auction End Date Time", "Auction Starting Bid", "Auction Reserve Price");
                if (auction.getReservePrice() == null) {
                    System.out.printf("%10s%20s%20s%30s%30s%25s%25s\n", auction.getId().toString(), auction.getName(), auction.getDetails(), auction.getStartDateTime().toString(), auction.getEndDateTime().toString(), NumberFormat.getCurrencyInstance().format(auction.getStartingBid()), "-");
                } else {
                    System.out.printf("%10s%20s%20s%30s%30s%25s%25s\n", auction.getId().toString(), auction.getName(), auction.getDetails(), auction.getStartDateTime().toString(), auction.getEndDateTime().toString(), NumberFormat.getCurrencyInstance().format(auction.getStartingBid()), NumberFormat.getCurrencyInstance().format(auction.getReservePrice()));
                }
                System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("Bids (Number of Bids: " + bids.size() + ")");
                System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("%10s%20s%20s%30s\n", "Bid ID", "Bid Amount", "Bid Date", "Bidder Username");
                for (Bid bid : bids) {
                    System.out.printf("%10s%20s%30s%20s\n", bid.getId().toString(), NumberFormat.getCurrencyInstance().format(bid.getBidAmount()), bid.getBidDateTime().toString(), bid.getCustomer().getUsername());
                }
                System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("1: Configure Proxy Bidding for Auction Listing");
                System.out.println("2: Configure Sniping for Auction Listing");
                System.out.println("3: Back\n");
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    doProxyBidding(auction);
                } else if (response == 2) {
                    doSniping(auction);
                }
            }
        } catch (AuctionNotFoundException_Exception ex) {
            System.out.println("An error has occurred while retrieving auction: " + ex.getMessage() + "\n");
        }
    }

    private void doBrowseAllAuctionListings() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** Premium Bidding cum Sniping Agent System :: Browse All Auction Listing ***\n");

        List<Auction> auctions = premiumCustomerWebServicePort.retrieveAllActiveAuctions();
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%10s%20s%20s%30s%30s%25s%25s\n", "Auction ID", "Auction Name", "Auction Details", "Auction Start Date Time", "Auction End Date Time", "Auction Starting Bid", "Auction Reserve Price");

        for (Auction auction : auctions) {
            if (auction.getReservePrice() == null) {
                System.out.printf("%10s%20s%20s%30s%30s%25s%25s\n", auction.getId().toString(), auction.getName(), auction.getDetails(), auction.getStartDateTime().toString(), auction.getEndDateTime().toString(), NumberFormat.getCurrencyInstance().format(auction.getStartingBid()), "-");
            } else {
                System.out.printf("%10s%20s%20s%30s%30s%25s%25s\n", auction.getId().toString(), auction.getName(), auction.getDetails(), auction.getStartDateTime().toString(), auction.getEndDateTime().toString(), NumberFormat.getCurrencyInstance().format(auction.getStartingBid()), NumberFormat.getCurrencyInstance().format(auction.getReservePrice()));
            }
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        if (auctions.isEmpty()) {
            System.out.println("There are currently no auctions available");
        }

        System.out.print("Press any key to go back...> ");
        scanner.nextLine();
    }

    private void doViewWonAuctionListings() {
        Scanner scanner = new Scanner(System.in);
        int response = 0;

         System.out.println("*** Premium Bidding cum Sniping Agent System :: View All Won Auction Listing ***\n");

        try {

            List<SuccessfulAuction> successfulAuctions = premiumCustomerWebServicePort.retrieveAllSuccessfulAuctionByCustomerId(currentPremiumCustomer.getId());
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%10s%30s%30s%20s\n", "Successful Auction ID", "Successful Auction Name", "Sucessful Auction Details", "Delivery Address");

            for (SuccessfulAuction successfulAuction : successfulAuctions) {
                System.out.printf("%10s%30s%30s%45s\n", successfulAuction.getId(), successfulAuction.getSuccessfulAuctionName(), successfulAuction.getSuccessfulAuctionDetails(), successfulAuction.getSuccessfulAuctionDeliveryAddress());
            }

            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");

            if (successfulAuctions.isEmpty()) {
                System.out.println("You do not have any won auction listings available");
            }

        } catch (CustomerNotFoundException_Exception ex) {
            System.out.println("An error has occurred while retrieving successful auction: " + ex.getMessage() + "\n");
        }

        System.out.print("\nPress any key to go back...> ");
        scanner.nextLine();
    }

    private void doProxyBidding(Auction auction) {
        Scanner scanner = new Scanner(System.in);

        BigDecimal maxAmount;
        BigDecimal highestBidAmount;
        BigDecimal bidIncrement = premiumCustomerWebServicePort.bidConverter(auction);
        
        System.out.println("*** Premium Bidding cum Sniping Agent System :: Configure Proxy Bidding for Auction Listing ***\n");
        
        if (auction.getBids().isEmpty()) {
            // if no bids yet, print starting bid
            System.out.println("The Starting Bid Price is: " + NumberFormat.getCurrencyInstance().format(auction.getStartingBid()));
            System.out.println("The Minimum Bid Increment Required: " + NumberFormat.getCurrencyInstance().format(bidIncrement));
            highestBidAmount = auction.getStartingBid();
        } else {
            // if there are bids, print highest bidding price
            Bid highestBid = premiumCustomerWebServicePort.getHighestBid(auction);
            BigDecimal highestAmount = highestBid.getBidAmount();
            System.out.println("The Current Highest Bid Price is: " + NumberFormat.getCurrencyInstance().format(highestAmount));
            System.out.println("The Minimum Bid Increment Required: " + NumberFormat.getCurrencyInstance().format(bidIncrement));
            highestBidAmount = highestAmount;
        }

        System.out.print("\nEnter Your Desired Maximum Amount For This Auction> ");
        maxAmount = scanner.nextBigDecimal();
        
        try {
            Customer premiumCustomer = premiumCustomerWebServicePort.retrieveCustomerbyId(currentPremiumCustomer.getId());
            // If customer bid amount > his/her own credit balance, throw this error
            if (maxAmount.doubleValue() > premiumCustomer.getCreditBalance().doubleValue()) {
                System.out.println("\nYou do not have sufficient credit balance to place this maximum amount!\n");
            } else if (maxAmount.doubleValue() - bidIncrement.doubleValue() < highestBidAmount.doubleValue()) {
                System.out.println("\nYour maximum amount needs to be higher than the minimum bid increment rate!\n");
            } else {
                try {
                    premiumCustomerWebServicePort.proxyBidding(auction, maxAmount, premiumCustomer);
                    premiumCustomerWebServicePort.placeABid(auction.getId(), currentPremiumCustomer.getId(), highestBidAmount.add(bidIncrement));
                    System.out.println("\n*** Congratulations! Your Proxy Bidding has been submitted. You do not need to remain login to the agent. ***\n");
                } catch (AuctionNotFoundException_Exception | CustomerNotFoundException_Exception ex) {
                    System.out.println("\nAn error occurred while creating proxy bid: " + ex.getMessage() + "\n");
                } catch (BidNotFoundException_Exception ex) {
                    System.out.println("\nAn error occurred while creating proxy bid: " + ex.getMessage() + "\n");
                } catch (InvalidBidException_Exception ex) {
                    System.out.println("\nAn error occurred while creating proxy bid: " + ex.getMessage() + "\n");
                } catch (NotEnoughCreditException_Exception ex) {
                    System.out.println("\nAn error occurred while creating proxy bid: " + ex.getMessage() + "\n");
                }
            }
        } catch (CustomerNotFoundException_Exception ex) {
            System.out.println("\nAn error occurred while creating proxy bid: " + ex.getMessage() + "\n");
        }
        
        System.out.print("Press any key to go back...> \n");
        scanner.nextLine();
    }

    private void doSniping(Auction auction) {
        Scanner scanner = new Scanner(System.in);
        Integer timeDuration;
        
        System.out.println("*** Premium Bidding cum Sniping Agent System :: Configure Sniping for Auction Listing ***\n");
        Date endDateTime = auction.getEndDateTime().toGregorianCalendar().getTime();
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        String formattedDate  = formatter.format(endDateTime);
        System.out.println("\n This auction listing will end on: " + endDateTime);
        
        while(true)
        {
            System.out.print("\nEnter desired time duration before end of auction to snipe in minutes> ");
            timeDuration = scanner.nextInt();
            
            if (timeDuration < 1)
            {
                System.out.println("Time duration must be at least 1 minute!");
            }
            else
            {
                break;
            }
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDateTime);
        calendar.add(Calendar.MINUTE, -timeDuration);
        Date snipeDateTime = calendar.getTime();
        
        try
        {
            Customer premiumCustomer = premiumCustomerWebServicePort.retrieveCustomerbyId(currentPremiumCustomer.getId());
           
            try
            {
                GregorianCalendar c = new GregorianCalendar();
                c.setTime(snipeDateTime);
                XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
                try
                {
                    premiumCustomerWebServicePort.sniping(auction, date, premiumCustomer);
                }
                catch (AuctionNotFoundException_Exception | CustomerNotFoundException_Exception ex)
                {
                    System.out.println("\nAn error occurred while creating snipe: " + ex.getMessage() + "\n");
                }
            }
            catch (DatatypeConfigurationException ex)
            {
                ex.printStackTrace();
            }
        }
        catch (CustomerNotFoundException_Exception ex)
        {
            System.out.println("\nAn error occurred while creating snipe: " + ex.getMessage() + "\n");
        }
    }
}
