package aisgagent;


import aisgagent.MainApp;
import ws.soap.PremiumCustomer.PremiumCustomerWebService;
import ws.soap.PremiumCustomer.PremiumCustomerWebService_Service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jamielee
 */
public class AISGAgent {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        PremiumCustomerWebService_Service premiumCustomerWebService_Service = new PremiumCustomerWebService_Service();
        PremiumCustomerWebService premiumCustomerWebServicePort = premiumCustomerWebService_Service.getPremiumCustomerWebServicePort();
        MainApp mainApp = new MainApp(premiumCustomerWebServicePort);
        mainApp.runApp();
    }
    
}
