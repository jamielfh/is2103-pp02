/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @NotNull
    @Size(min = 1, max = 32)
    private String name;
    @Column(nullable = false, length = 128)
    @NotNull
    @Size(min = 1, max = 128)
    private String details;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date startDateTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date endDateTime;
    @Column(nullable = false, precision = 18, scale = 4)
    @NotNull
    @Digits(integer = 14, fraction = 4)
    @DecimalMin("0.01")
    private BigDecimal startingBid;
    @Column(nullable = true, precision = 18, scale = 4)
    @Digits(integer = 14, fraction = 4)
    @DecimalMin("0.01")
    private BigDecimal reservePrice;
    @Column(nullable = false)
    @NotNull
    private Boolean isDisabled;
    @Column(nullable = false)
    @NotNull
    private Boolean manualIntervention;
    @Column(nullable = false)
    @NotNull
    private Boolean isClosed;
    
    @OneToOne(mappedBy = "auction")
    private SuccessfulAuction successfulAuction;
    
    @OneToMany(mappedBy = "auction")
    private List<Bid> bids;

    public Auction() {
        bids = new ArrayList<>();
    }

    public Auction(String name, String details, Date startDateTime, Date endDateTime, BigDecimal startingBid, BigDecimal reservePrice, Boolean isDisabled, Boolean manualIntervention, Boolean isClosed) {
        this();
        
        this.name = name;
        this.details = details;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.startingBid = startingBid;
        this.reservePrice = reservePrice;
        this.isDisabled = isDisabled;
        this.manualIntervention = manualIntervention;
        this.isClosed = isClosed;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return the startDateTime
     */
    public Date getStartDateTime() {
        return startDateTime;
    }

    /**
     * @param startDateTime the startDateTime to set
     */
    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * @return the endDateTime
     */
    public Date getEndDateTime() {
        return endDateTime;
    }

    /**
     * @param endDateTime the endDateTime to set
     */
    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
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
     * @return the details
     */
    public String getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * @return the successfulAuction
     */
    public SuccessfulAuction getSuccessfulAuction() {
        return successfulAuction;
    }

    /**
     * @param successfulAuction the successfulAuction to set
     */
    public void setSuccessfulAuction(SuccessfulAuction successfulAuction) {
        this.successfulAuction = successfulAuction;
    }

    /**
     * @return the bids
     */
    public List<Bid> getBids() {
        return bids;
    }

    /**
     * @param bids the bids to set
     */
    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    /**
     * @return the isClosed
     */
    public Boolean getIsClosed() {
        return isClosed;
    }

    /**
     * @param isClosed the isClosed to set
     */
    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }
    
}
