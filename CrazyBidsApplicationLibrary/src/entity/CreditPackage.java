/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Bransome Tan Yi Hao
 */
@Entity
public class CreditPackage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal creditPackageAmount;
    @Column(nullable = false)
    private Boolean isDisabled;
    
    @OneToMany(mappedBy = "creditPackage")
    private List<CreditTransaction> creditTransactions;

    public CreditPackage() {
        creditTransactions = new ArrayList<>();
    }

    public CreditPackage(BigDecimal creditPackageAmount, Boolean isDisabled) {
        this();
        
        this.creditPackageAmount = creditPackageAmount;
        this.isDisabled = isDisabled;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CreditPackage)) {
            return false;
        }
        CreditPackage other = (CreditPackage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CreditPackage[ id=" + id + " ]";
    }

    /**
     * @return the creditPackageAmount
     */
    public BigDecimal getCreditPackageAmount() {
        return creditPackageAmount;
    }

    /**
     * @param creditPackageAmount the creditPackageAmount to set
     */
    public void setCreditPackageAmount(BigDecimal creditPackageAmount) {
        this.creditPackageAmount = creditPackageAmount;
    }

    /**
     * @return the isDisabled
     */
    public Boolean getIsDisabled() {
        return isDisabled;
    }

    /**
     * @param isDisabled the isDisabled to set
     */
    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    /**
     * @return the creditTransactions
     */
    public List<CreditTransaction> getCreditTransactions() {
        return creditTransactions;
    }

    /**
     * @param creditTransactions the creditTransactions to set
     */
    public void setCreditTransactions(List<CreditTransaction> creditTransactions) {
        this.creditTransactions = creditTransactions;
    }
    
}
