/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Bransome Tan Yi Hao
 */
@Entity
public class CreditTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal transactionAmount;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date transactionDateTime;

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
        if (!(object instanceof CreditTransaction)) {
            return false;
        }
        CreditTransaction other = (CreditTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CreditTransaction[ id=" + id + " ]";
    }

    /**
     * @return the transactionAmount
     */
    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * @param transactionAmount the transactionAmount to set
     */
    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    /**
     * @return the transactionDateTime
     */
    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    /**
     * @param transactionDateTime the transactionDateTime to set
     */
    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }
    
}
