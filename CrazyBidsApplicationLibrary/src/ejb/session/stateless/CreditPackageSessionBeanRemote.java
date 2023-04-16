/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditPackage;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CreditPackageIsDisabledException;
import util.exception.CreditPackageIsUsedException;
import util.exception.CreditPackageNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCreditPackageException;

/**
 *
 * @author jamielee
 */
@Remote
public interface CreditPackageSessionBeanRemote {

    CreditPackage retrieveCreditPackagebyId(Long creditPackageId) throws CreditPackageNotFoundException;

    Long createCreditPackage(CreditPackage newCreditPackage) throws GeneralException, InputDataValidationException;

    void deleteCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException, CreditPackageIsUsedException;

    List<CreditPackage> retrieveAllCreditPackages();

    void updateCreditPackage(CreditPackage updateCP, BigDecimal newAmount) throws CreditPackageNotFoundException, UpdateCreditPackageException;
    
    void disableCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException, CreditPackageIsDisabledException;
    
     List<CreditPackage> retrieveAllActiveCreditPackages();
    
}
