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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Bransome Tan Yi Hao
 */
@Entity
public class ProxyBid implements Comparable<ProxyBid>, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, precision = 18, scale = 4)
    @NotNull
    @Digits(integer = 14, fraction = 4)
    @DecimalMin("0.01")
    private BigDecimal maximumBidAmount;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date creationDateTime;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Auction auction;

    public ProxyBid() {
    }

    public ProxyBid(BigDecimal maximumBidAmount, Date creationDateTime) {
        this.maximumBidAmount = maximumBidAmount;
        this.creationDateTime = creationDateTime;
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
        if (!(object instanceof ProxyBid)) {
            return false;
        }
        ProxyBid other = (ProxyBid) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProxyBid[ id=" + id + " ]";
    }
    
    @Override
    public int compareTo(ProxyBid other) {
        if (other.getMaximumBidAmount().compareTo(this.getMaximumBidAmount()) != 0) {
            return other.getMaximumBidAmount().compareTo(this.getMaximumBidAmount());
        } else {
            return this.getCreationDateTime().compareTo(other.getCreationDateTime());
        }
        
    }
    /**
     * @return the maximumBidAmount
     */
    public BigDecimal getMaximumBidAmount() {
        return maximumBidAmount;
    }

    /**
     * @param maximumBidAmount the maximumBidAmount to set
     */
    public void setMaximumBidAmount(BigDecimal maximumBidAmount) {
        this.maximumBidAmount = maximumBidAmount;
    }

    /**
     * @return the creationDateTime
     */
    public Date getCreationDateTime() {
        return creationDateTime;
    }

    /**
     * @param creationDateTime the creationDateTime to set
     */
    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
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
    
}
