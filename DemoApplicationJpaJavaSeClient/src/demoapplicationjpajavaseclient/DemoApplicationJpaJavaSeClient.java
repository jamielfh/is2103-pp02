/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demoapplicationjpajavaseclient;

import java.util.List;
import ws.soap.record.Record;
import ws.soap.record.RecordVersion;
import ws.soap.record.RecordWebService;
import ws.soap.record.RecordWebService_Service;

/**
 *
 * @author Bransome Tan Yi Hao
 */
public class DemoApplicationJpaJavaSeClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        RecordWebService_Service recordWebService_Service = new RecordWebService_Service();
        RecordWebService recordWebServicePort = recordWebService_Service.getRecordWebServicePort();
        List<Record> records = recordWebServicePort.retrieveAllRecords();
        for (Record record: records) {
            System.out.println("Record: " + record.getRecordId() + "; " + record.getRecordName());
            System.out.println("Current Record Version: " + record.getCurrRecordVersion().getRecordVersionDescription());
            
            for (RecordVersion recordVersion: record.getRecordVersions()) {
                System.out.println("Record Version: " + recordVersion.getRecordVersionDescription());
            }
        }
    }
    
}
