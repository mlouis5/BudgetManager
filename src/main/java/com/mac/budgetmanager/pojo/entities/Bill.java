/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.pojo.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "bill", catalog = "finance", schema = "budget")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bill.findAll", query = "SELECT b FROM Bill b"),
    @NamedQuery(name = "Bill.findByBillId", query = "SELECT b FROM Bill b WHERE b.billId = :billId"),
    @NamedQuery(name = "Bill.findByBillName", query = "SELECT b FROM Bill b WHERE b.billName = :billName"),
    @NamedQuery(name = "Bill.findByBillSource", query = "SELECT b FROM Bill b WHERE b.billSource = :billSource"),
    @NamedQuery(name = "Bill.findByBillType", query = "SELECT b FROM Bill b WHERE b.billType = :billType"),
    @NamedQuery(name = "Bill.findByBillDueDate", query = "SELECT b FROM Bill b WHERE b.billDueDate = :billDueDate"),
    @NamedQuery(name = "Bill.findByBillIsRevolving", query = "SELECT b FROM Bill b WHERE b.billIsRevolving = :billIsRevolving"),
    @NamedQuery(name = "Bill.findByBillNumPayments", query = "SELECT b FROM Bill b WHERE b.billNumPayments = :billNumPayments"),
    @NamedQuery(name = "Bill.findByBillLateFeeAmount", query = "SELECT b FROM Bill b WHERE b.billLateFeeAmount = :billLateFeeAmount"),
    @NamedQuery(name = "Bill.findByBillInterestRate", query = "SELECT b FROM Bill b WHERE b.billInterestRate = :billInterestRate"),
    @NamedQuery(name = "Bill.findByBillGracePeriod", query = "SELECT b FROM Bill b WHERE b.billGracePeriod = :billGracePeriod"),
    @NamedQuery(name = "Bill.findByBillWebsite", query = "SELECT b FROM Bill b WHERE b.billWebsite = :billWebsite"),
    @NamedQuery(name = "Bill.findByBillSiteUserId", query = "SELECT b FROM Bill b WHERE b.billSiteUserId = :billSiteUserId"),
    @NamedQuery(name = "Bill.findByBillSitePwd", query = "SELECT b FROM Bill b WHERE b.billSitePwd = :billSitePwd"),
    @NamedQuery(name = "Bill.findByBillIsSatisfied", query = "SELECT b FROM Bill b WHERE b.billIsSatisfied = :billIsSatisfied")})
public class Bill implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "bill_id", nullable = false, length = 36)
    private String billId;
    @Size(max = 128)
    @Column(name = "bill_name", length = 128)
    private String billName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "bill_source", nullable = false, length = 128)
    private String billSource;
    @Size(max = 128)
    @Column(name = "bill_type", length = 128)
    private String billType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "bill_due_date", nullable = false)
    private int billDueDate;
    @Column(name = "bill_is_revolving")
    private Boolean billIsRevolving;
    @Column(name = "bill_num_payments")
    private Integer billNumPayments;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "bill_late_fee_amount", precision = 17, scale = 17)
    private Double billLateFeeAmount;
    @Column(name = "bill_interest_rate", precision = 17, scale = 17)
    private Double billInterestRate;
    @Column(name = "bill_grace_period")
    private Integer billGracePeriod;
    @Size(max = 2147483647)
    @Column(name = "bill_website", length = 2147483647)
    private String billWebsite;
    @Size(max = 2147483647)
    @Column(name = "bill_site_user_id", length = 2147483647)
    private String billSiteUserId;
    @Size(max = 2147483647)
    @Column(name = "bill_site_pwd", length = 2147483647)
    private String billSitePwd;
    @Column(name = "bill_is_satisfied")
    private Boolean billIsSatisfied;
    @JoinColumn(name = "bill_mail_address", referencedColumnName = "address_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Address billMailAddress;
    @JoinColumn(name = "bill_owner", referencedColumnName = "user_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User billOwner;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentBillId", fetch = FetchType.EAGER)
    private List<Payment> paymentList;

    public Bill() {
    }

    public Bill(String billId) {
        this.billId = billId;
    }

    public Bill(String billId, String billSource, int billDueDate) {
        this.billId = billId;
        this.billSource = billSource;
        this.billDueDate = billDueDate;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getBillSource() {
        return billSource;
    }

    public void setBillSource(String billSource) {
        this.billSource = billSource;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public int getBillDueDate() {
        return billDueDate;
    }

    public void setBillDueDate(int billDueDate) {
        this.billDueDate = billDueDate;
    }

    public Boolean getBillIsRevolving() {
        return billIsRevolving;
    }

    public void setBillIsRevolving(Boolean billIsRevolving) {
        this.billIsRevolving = billIsRevolving;
    }

    public Integer getBillNumPayments() {
        return billNumPayments;
    }

    public void setBillNumPayments(Integer billNumPayments) {
        this.billNumPayments = billNumPayments;
    }

    public Double getBillLateFeeAmount() {
        return billLateFeeAmount;
    }

    public void setBillLateFeeAmount(Double billLateFeeAmount) {
        this.billLateFeeAmount = billLateFeeAmount;
    }

    public Double getBillInterestRate() {
        return billInterestRate;
    }

    public void setBillInterestRate(Double billInterestRate) {
        this.billInterestRate = billInterestRate;
    }

    public Integer getBillGracePeriod() {
        return billGracePeriod;
    }

    public void setBillGracePeriod(Integer billGracePeriod) {
        this.billGracePeriod = billGracePeriod;
    }

    public String getBillWebsite() {
        return billWebsite;
    }

    public void setBillWebsite(String billWebsite) {
        this.billWebsite = billWebsite;
    }

    public String getBillSiteUserId() {
        return billSiteUserId;
    }

    public void setBillSiteUserId(String billSiteUserId) {
        this.billSiteUserId = billSiteUserId;
    }

    public String getBillSitePwd() {
        return billSitePwd;
    }

    public void setBillSitePwd(String billSitePwd) {
        this.billSitePwd = billSitePwd;
    }

    public Boolean getBillIsSatisfied() {
        return billIsSatisfied;
    }

    public void setBillIsSatisfied(Boolean billIsSatisfied) {
        this.billIsSatisfied = billIsSatisfied;
    }

    public Address getBillMailAddress() {
        return billMailAddress;
    }

    public void setBillMailAddress(Address billMailAddress) {
        this.billMailAddress = billMailAddress;
    }

    public User getBillOwner() {
        return billOwner;
    }

    public void setBillOwner(User billOwner) {
        this.billOwner = billOwner;
    }

    @XmlTransient
    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billId != null ? billId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bill)) {
            return false;
        }
        Bill other = (Bill) object;
        if ((this.billId == null && other.billId != null) || (this.billId != null && !this.billId.equals(other.billId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mac.budgetmanager.pojo.entities.Bill[ billId=" + billId + " ]";
    }
    
}
