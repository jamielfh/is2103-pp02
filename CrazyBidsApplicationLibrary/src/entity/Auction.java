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
public class Auction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 32)
    private String auctionName;
    @Column(nullable = false, length = 32)
    private String auctionDetails;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date auctionStartDateTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date auctionEndDateTime;
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal startingBid;
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal reservePrice;
    @Column(nullable = false)
    private Boolean isDisabled;
    @Column(nullable = false)
    private Boolean manualIntervention;

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
        if (!(object instanceof Auction)) {
            return false;
        }
        Auction other = (Auction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Auction[ id=" + id + " ]";
    }

    /**
     * @return the auctionName
     */
    public String getAuctionName() {
        return auctionName;
    }

    /**
     * @param auctionName the auctionName to set
     */
    public void setAuctionName(String auctionName) {
        this.auctionName = auctionName;
    }


    /**
     * @return the auctionStartDateTime
     */
    public Date getAuctionStartDateTime() {
        return auctionStartDateTime;
    }

    /**
     * @param auctionStartDateTime the auctionStartDateTime to set
     */
    public void setAuctionStartDateTime(Date auctionStartDateTime) {
        this.auctionStartDateTime = auctionStartDateTime;
    }

    /**
     * @return the auctionEndDateTime
     */
    public Date getAuctionEndDateTime() {
        return auctionEndDateTime;
    }

    /**
     * @param auctionEndDateTime the auctionEndDateTime to set
     */
    public void setAuctionEndDateTime(Date auctionEndDateTime) {
        this.auctionEndDateTime = auctionEndDateTime;
    }

    /**
     * @return the startingBid
     */
    public BigDecimal getStartingBid() {
        return startingBid;
    }

    /**
     * @param startingBid the startingBid to set
     */
    public void setStartingBid(BigDecimal startingBid) {
        this.startingBid = startingBid;
    }

    /**
     * @return the reservePrice
     */
    public BigDecimal getReservePrice() {
        return reservePrice;
    }

    /**
     * @param reservePrice the reservePrice to set
     */
    public void setReservePrice(BigDecimal reservePrice) {
        this.reservePrice = reservePrice;
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
     * @return the manualIntervention
     */
    public Boolean getManualIntervention() {
        return manualIntervention;
    }

    /**
     * @param manualIntervention the manualIntervention to set
     */
    public void setManualIntervention(Boolean manualIntervention) {
        this.manualIntervention = manualIntervention;
    }

    /**
     * @return the auctionDetails
     */
    public String getAuctionDetails() {
        return auctionDetails;
    }

    /**
     * @param auctionDetails the auctionDetails to set
     */
    public void setAuctionDetails(String auctionDetails) {
        this.auctionDetails = auctionDetails;
    }
    
}
