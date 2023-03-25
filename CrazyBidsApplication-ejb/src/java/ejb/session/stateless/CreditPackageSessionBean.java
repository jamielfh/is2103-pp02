/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditPackage;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CreditPackageNotFoundException;
import util.exception.UnknownPersistenceException;

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
    public Long createCreditPackage(CreditPackage newCreditPackage) throws UnknownPersistenceException {
        try {
            em.persist(newCreditPackage);
            em.flush();
            
            return newCreditPackage.getId();
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public void updateCreditPackage(CreditPackage updatedCP) throws CreditPackageNotFoundException {
        CreditPackage cp = retrieveCreditPackagebyId(updatedCP.getId());
        cp.setCreditPackageAmount(updatedCP.getCreditPackageAmount());
        cp.setIsDisabled(updatedCP.getIsDisabled());
    }
    
    @Override
    public void deleteCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException {
        CreditPackage cp = retrieveCreditPackagebyId(creditPackageId);
        
        if (cp.getPurchaseTransactions().isEmpty()) {
            em.remove(cp);
        } else {
            cp.setIsDisabled(true); // or do we need to explicitly state that it is disabled and not deleted?
        }
    }

}
