/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.pojo.entities;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mac
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Entity
@Table(name = "user", catalog = "finance", schema = "budget")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByUserId", query = "SELECT u FROM User u WHERE u.userId = :userId"),
    @NamedQuery(name = "User.findByUserFname", query = "SELECT u FROM User u WHERE u.userFname = :userFname"),
    @NamedQuery(name = "User.findByUserLname", query = "SELECT u FROM User u WHERE u.userLname = :userLname"),
    @NamedQuery(name = "User.findByUserPhone", query = "SELECT u FROM User u WHERE u.userPhone = :userPhone"),
    @NamedQuery(name = "User.findByUserEmail", query = "SELECT u FROM User u WHERE u.userEmail = :userEmail"),
    @NamedQuery(name = "User.findByUserPreferredContact", query = "SELECT u FROM User u WHERE u.userPreferredContact = :userPreferredContact")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "user_fname", nullable = false, length = 128)
    private String userFname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "user_lname", nullable = false, length = 128)
    private String userLname;
    @Size(max = 10)
    @Column(name = "user_phone", length = 10)
    private String userPhone;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "user_email", nullable = false, length = 2147483647)
    private String userEmail;
    @Size(max = 5)
    @Column(name = "user_preferred_contact", length = 5)
    private String userPreferredContact;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "incomeUserId")
    private List<Income> incomeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "billOwner")
    private List<Bill> billList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentUserId")
    private List<Payment> paymentList;
    @JoinColumn(name = "user_address", referencedColumnName = "address_id")
    @ManyToOne
    private Address userAddress;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paycheckOwner")
    private List<Paycheck> paycheckList;

    public User() {
    }

    public User(String userFname, String userLname, String userEmail) {
        Preconditions.checkNotNull(userFname, userFname);
        Preconditions.checkNotNull(userLname, userLname);
        Preconditions.checkNotNull(userEmail, userEmail);
        this.userFname = userFname;
        this.userLname = userLname;
        this.userEmail = userEmail;
        StringBuilder sb = new StringBuilder(userFname);
        sb.append(userLname);
        sb.append(userEmail);
        sb.trimToSize();
        this.userId = String.valueOf(UUID.fromString(sb.toString()));
    }

    public String getUserId() {
        Preconditions.checkNotNull(userFname, userFname);
        Preconditions.checkNotNull(userLname, userLname);
        Preconditions.checkNotNull(userEmail, userEmail);
        StringBuilder sb = new StringBuilder(userFname);
        sb.append(userLname);
        sb.append(userEmail);
        sb.trimToSize();
        this.userId = String.valueOf(UUID.fromString(sb.toString()));
        return userId;
    }

    public String getUserFname() {
        Preconditions.checkNotNull(userFname, userFname);
        return userFname;
    }

    public void setUserFname(String userFname) {
        Preconditions.checkNotNull(userFname, userFname);
        this.userFname = userFname;
    }

    public String getUserLname() {
        Preconditions.checkNotNull(userLname, userLname);
        return userLname;
    }

    public void setUserLname(String userLname) {
        Preconditions.checkNotNull(userLname, userLname);
        this.userLname = userLname;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        Preconditions.checkNotNull(userEmail, userEmail);
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        Preconditions.checkNotNull(userEmail, userEmail);
        this.userEmail = userEmail;
    }

    public String getUserPreferredContact() {
        return userPreferredContact;
    }

    public void setUserPreferredContact(String userPreferredContact) {
        this.userPreferredContact = userPreferredContact;
    }

    @XmlTransient
    public List<Income> getIncomeList() {
        return incomeList;
    }

    public void setIncomeList(List<Income> incomeList) {
        this.incomeList = incomeList;
    }

    @XmlTransient
    public List<Bill> getBillList() {
        return billList;
    }

    public void setBillList(List<Bill> billList) {
        this.billList = billList;
    }

    @XmlTransient
    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public Address getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(Address userAddress) {
        this.userAddress = userAddress;
    }

    @XmlTransient
    public List<Paycheck> getPaycheckList() {
        return paycheckList;
    }

    public void setPaycheckList(List<Paycheck> paycheckList) {
        this.paycheckList = paycheckList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        Preconditions.checkNotNull(userId);                
        if (!(object instanceof User)) {
            return false;
        }
        Preconditions.checkNotNull(object);
        User other = (User) object;
        Preconditions.checkNotNull(other.userId);
        return Objects.equals(userId, other.userId);
    }

    @Override
    public String toString() {
        return "com.mac.entities.User[ userId=" + userId + " ]";
    }

}
