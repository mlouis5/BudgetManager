/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.pojo.entities;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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

    @Basic(optional = false)
    @NotNull
    @Column(name = "bill_amount", nullable = false)
    private double billAmount;
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
    @XmlTransient
    private transient String billSiteUserId;
    @Size(max = 2147483647)
    @Column(name = "bill_site_pwd", length = 2147483647)
    @XmlTransient
    private transient String billSitePwd;
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

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        if (Objects.isNull(this.billId) || this.billId.isEmpty()) {
            this.billId = billId;
        }
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
        generateId();
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
        generateId();
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

    @XmlTransient
    public void setBillOwner(User billOwner) {
        this.billOwner = billOwner;
        generateId();
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    @Override
    public int hashCode() {
        HashFunction hf = Hashing.md5();
        HashCode hc = hf.newHasher().putString(billId, Charsets.UTF_8).hash();
        return hc.asInt();
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payment)) {
            return false;
        }
        Bill other = (Bill) object;
        return Objects.equals(this.billId, other.billId);
    }

    @Override
    public String toString() {
        return "com.mac.budgetmanager.pojo.entities.Bill[ billId=" + billId + " ]";
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
        generateId();
    }

    private void generateId() {
        if (Objects.nonNull(billDueDate) && Objects.nonNull(billSource)
                && Objects.nonNull(billOwner)
                && !billSource.isEmpty()
                && billAmount > 0
                && Objects.nonNull(billOwner.getUserEmail())
                && Objects.nonNull(billOwner.getUserFname())
                && Objects.nonNull(billOwner.getUserLname())) {
            HashFunction hf = Hashing.md5();
            HashCode hc = hf.newHasher()
                    .putDouble(billAmount)
                    .putString(billSource, Charsets.UTF_8)
                    .putInt(billDueDate)
                    .putString(billOwner.getUserEmail(), Charsets.UTF_8)
                    .putString(billOwner.getUserFname(), Charsets.UTF_8)
                    .putString(billOwner.getUserLname(), Charsets.UTF_8)
                    .putInt(billOwner.hashCode()).hash();
            this.billId = hc.toString();
        }
    }
}
