/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Bransome Tan Yi Hao
 */
@Entity
public class SuccessfulAuction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 32)
    private String successfulAuctionName;
    @Column(nullable = false, length = 32)
    private String successfulAuctionDetails;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;
    
    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private Address address;
    
    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private Auction auction;

    public SuccessfulAuction() {
        
    }

    public SuccessfulAuction(String successfulAuctionName, String successfulAuctionDetails) {
        this.successfulAuctionName = successfulAuctionName;
        this.successfulAuctionDetails = successfulAuctionDetails;
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
        if (!(object instanceof SuccessfulAuction)) {
            return false;
        }
        SuccessfulAuction other = (SuccessfulAuction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SuccessfulAuction[ id=" + id + " ]";
    }

    /**
     * @return the successfulAuctionName
     */
    public String getSuccessfulAuctionName() {
        return successfulAuctionName;
    }

    /**
     * @param successfulAuctionName the successfulAuctionName to set
     */
    public void setSuccessfulAuctionName(String successfulAuctionName) {
        this.successfulAuctionName = successfulAuctionName;
    }

    /**
     * @return the successfulAuctionDetails
     */
    public String getSuccessfulAuctionDetails() {
        return successfulAuctionDetails;
    }

    /**
     * @param successfulAuctionDetails the successfulAuctionDetails to set
     */
    public void setSuccessfulAuctionDetails(String successfulAuctionDetails) {
        this.successfulAuctionDetails = successfulAuctionDetails;
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
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(Address address) {
        this.address = address;
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
