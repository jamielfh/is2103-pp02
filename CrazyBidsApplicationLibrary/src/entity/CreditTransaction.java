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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import util.enumeration.CreditTransactionEnum;

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
    @Column(nullable = false, precision = 18, scale = 4)
    @NotNull
    @Digits(integer = 14, fraction = 4)
    @DecimalMin("0.05")
    private BigDecimal transactionAmount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private CreditTransactionEnum creditTransactionEnum;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date transactionDateTime;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;
    
    @ManyToOne
    private CreditPackage creditPackage;
    
    @OneToOne
    private Bid bid;
    
    @OneToOne
    private Bid refundedBid;

    public CreditTransaction() {
        
    }

    public CreditTransaction(BigDecimal transactionAmount, CreditTransactionEnum creditTransactionEnum, Date transactionDateTime) {
        this.transactionAmount = transactionAmount;
        this.creditTransactionEnum = creditTransactionEnum;
        this.transactionDateTime = transactionDateTime;
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

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the creditTransactionEnum
     */
    public CreditTransactionEnum getCreditTransactionEnum() {
        return creditTransactionEnum;
    }

    /**
     * @param creditTransactionEnum the creditTransactionEnum to set
     */
    public void setCreditTransactionEnum(CreditTransactionEnum creditTransactionEnum) {
        this.creditTransactionEnum = creditTransactionEnum;
    }

    /**
     * @return the creditPackage
     */
    public CreditPackage getCreditPackage() {
        return creditPackage;
    }

    /**
     * @param creditPackage the creditPackage to set
     */
    public void setCreditPackage(CreditPackage creditPackage) {
        this.creditPackage = creditPackage;
    }

    /**
     * @return the bid
     */
    public Bid getBid() {
        return bid;
    }

    /**
     * @param bid the bid to set
     */
    public void setBid(Bid bid) {
        this.bid = bid;
    }

    /**
     * @return the refundedBid
     */
    public Bid getRefundedBid() {
        return refundedBid;
    }

    /**
     * @param refundedBid the refundedBid to set
     */
    public void setRefundedBid(Bid refundedBid) {
        this.refundedBid = refundedBid;
    }
    
}
