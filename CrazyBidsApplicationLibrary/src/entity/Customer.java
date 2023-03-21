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
 * @author jamielee
 */
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 32)
    private String firstName;
    @Column(nullable = false, length = 32)
    private String lastName;
    @Column(nullable = false, unique = true, length = 32)
    private String username;
    @Column(nullable = false, length = 32)
    private String password;
    @Column(nullable = false, length = 32)
    private String email;
    @Column(nullable = false, length = 32)
    private String mobileNumber;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal creditBalance;
    
    @OneToMany
    private List<Address> addresses;
    
    @OneToMany(mappedBy = "customer")
    private List<SuccessfulAuction> successfulAuctions;
    
    @OneToMany(mappedBy = "customer")
    private List<Bid> bids;
    
    @OneToMany(mappedBy = "customer")
    private List<CreditTransaction> creditTransactions;

    public Customer() {
         addresses = new ArrayList<>();
         successfulAuctions = new ArrayList<>();
         bids = new ArrayList<>();
         creditTransactions = new ArrayList<>();
    }

    public Customer(String firstName, String lastName, String username, String password, String email, String mobileNumber, BigDecimal creditBalance) {
        this();
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.creditBalance = creditBalance;
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
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + id + " ]";
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the mobileNumber
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * @param mobileNumber the mobileNumber to set
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * @return the creditBalance
     */
    public BigDecimal getCreditBalance() {
        return creditBalance;
    }

    /**
     * @param creditBalance the creditBalance to set
     */
    public void setCreditBalance(BigDecimal creditBalance) {
        this.creditBalance = creditBalance;
    }

    /**
     * @return the addresses
     */
    public List<Address> getAddresses() {
        return addresses;
    }

    /**
     * @param addresses the addresses to set
     */
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    /**
     * @return the successfulAuctions
     */
    public List<SuccessfulAuction> getSuccessfulAuctions() {
        return successfulAuctions;
    }

    /**
     * @param successfulAuctions the successfulAuctions to set
     */
    public void setSuccessfulAuctions(List<SuccessfulAuction> successfulAuctions) {
        this.successfulAuctions = successfulAuctions;
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
