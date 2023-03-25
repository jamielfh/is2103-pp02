/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditPackage;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreditPackageNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author jamielee
 */
@Local
public interface CreditPackageSessionBeanLocal {

    CreditPackage retrieveCreditPackagebyId(Long creditPackageId) throws CreditPackageNotFoundException;

    Long createCreditPackage(CreditPackage newCreditPackage) throws UnknownPersistenceException;

    void deleteCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException;

    List<CreditPackage> retrieveAllCreditPackages();

    void updateCreditPackage(CreditPackage updatedCP) throws CreditPackageNotFoundException;
    
}
