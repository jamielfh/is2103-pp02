/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditPackage;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CreditPackageIsDisabledException;
import util.exception.CreditPackageIsUsedException;
import util.exception.CreditPackageNotFoundException;
import util.exception.GeneralException;
import util.exception.UpdateCreditPackageException;

/**
 *
 * @author jamielee
 */
@Stateless
public class CreditPackageSessionBean implements CreditPackageSessionBeanRemote, CreditPackageSessionBeanLocal {

    @PersistenceContext(unitName = "CrazyBidsApplication-ejbPU")
    private EntityManager em;

    public CreditPackageSessionBean() {
    }

    @Override
    public CreditPackage retrieveCreditPackagebyId(Long creditPackageId) throws CreditPackageNotFoundException {
        CreditPackage cp = em.find(CreditPackage.class, creditPackageId);
        if (cp != null) {
            return cp;
        } else {
            throw new CreditPackageNotFoundException("Credit package ID " + creditPackageId + " not found in system!");
        }
    }
    
    @Override
    public List<CreditPackage> retrieveAllCreditPackages() {
        Query query = em.createQuery("SELECT cp from CreditPackage cp ORDER BY cp.creditPackageAmount ASC");
        
        return query.getResultList();
    }
    
    @Override
    public List<CreditPackage> retrieveAllActiveCreditPackages() {
        Query query = em.createQuery("SELECT cp from CreditPackage cp WHERE cp.isDisabled = false ORDER BY cp.creditPackageAmount ASC");
        
        return query.getResultList();
    }

    @Override
    public Long createCreditPackage(CreditPackage newCreditPackage) throws GeneralException {
        try {
            em.persist(newCreditPackage);
            em.flush();
            
            return newCreditPackage.getId();
        } catch (PersistenceException ex) {
            throw new GeneralException(ex.getMessage());
        }
    }

    @Override
    public void updateCreditPackage(CreditPackage updateCP, BigDecimal newAmount) throws CreditPackageNotFoundException, UpdateCreditPackageException
    {
        if(updateCP != null && updateCP.getId() != null)
        {
            CreditPackage cpToUpdate = retrieveCreditPackagebyId(updateCP.getId());
            
            if(updateCP.getCreditPackageAmount().equals(cpToUpdate.getCreditPackageAmount()))
            {
                cpToUpdate.setCreditPackageAmount(newAmount);

                //cp.setIsDisabled(updatedCP.getIsDisabled());
                // Disable Credit Package are deliberately NOT updated to demonstrate that client is not allowed to update account credential through this business method
            }
            else
            {
                throw new UpdateCreditPackageException("Amount of credit package to be updated does not match the existing record");
            }
        }
        else
        {
            throw new CreditPackageNotFoundException("Credit Package ID not provided for credit package to be updated");
        }
    }
    
    @Override
    public void deleteCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException, CreditPackageIsUsedException{
        CreditPackage cp = retrieveCreditPackagebyId(creditPackageId);
        
        if (cp.getCreditTransactions().isEmpty()) {
            em.remove(cp);
        } else {
            throw new CreditPackageIsUsedException("Credit Package is in use and you are not allowed to delete it. You can opt to disable it.");
        }
    }
    
    @Override
    public void disableCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException, CreditPackageIsDisabledException {
        CreditPackage cp = retrieveCreditPackagebyId(creditPackageId);
        if (!cp.getIsDisabled()) {
            cp.setIsDisabled(true);
        } else {
            throw new CreditPackageIsDisabledException("Credit Package is already disabled.");
        }
    }
    
    

}
