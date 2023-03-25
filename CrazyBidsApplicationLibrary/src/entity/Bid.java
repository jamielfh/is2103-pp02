/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Bransome Tan Yi Hao
 */
@Entity
public class Bid implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal bidAmount;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date bidDateTime;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Auction auction;
    
    @OneToOne(cascade = CascadeType.ALL, optional = false) //one to one both mandatory
    @JoinColumn(name = "bidTransactionId", referencedColumnName = "bidId")
    private BidTransaction bidTransaction;
    
    @OneToOne(mappedBy = "bid")
    private RefundTransaction refundTransaction;

    public Bid() {
        
    }

    public Bid(BigDecimal bidAmount, Date bidDateTime) {
        this.bidAmount = bidAmount;
        this.bidDateTime = bidDateTime;
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
        if (!(object instanceof Bid)) {
            return false;
        }
        Bid other = (Bid) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Bid[ id=" + id + " ]";
    }

    /**
     * @return the bidAmount
     */
    public BigDecimal getBidAmount() {
        return bidAmount;
    }

    /**
     * @param bidAmount the bidAmount to set
     */
    public void setBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }

    /**
     * @return the bidDateTime
     */
    public Date getBidDateTime() {
        return bidDateTime;
    }

    /**
     * @param bidDateTime the bidDateTime to set
     */
    public void setBidDateTime(Date bidDateTime) {
        this.bidDateTime = bidDateTime;
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
     * @return the auction
     */
    public Auction getAuction() {
        return auction;
    }

    /**
     * @param auction the auction to set
     */
    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    /**
     * @return the bidTransaction
     */
    public BidTransaction getBidTransaction() {
        return bidTransaction;
    }

    /**
     * @param bidTransaction the bidTransaction to set
     */
    public void setBidTransaction(BidTransaction bidTransaction) {
        this.bidTransaction = bidTransaction;
    }

    /**
     * @return the refundTransaction
     */
    public RefundTransaction getRefundTransaction() {
        return refundTransaction;
    }

    /**
     * @param refundTransaction the refundTransaction to set
     */
    public void setRefundTransaction(RefundTransaction refundTransaction) {
        this.refundTransaction = refundTransaction;
    }
    
}
