/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.pojo.entities;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
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
@Table(name = "payment", catalog = "finance", schema = "budget")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Payment.findAll", query = "SELECT p FROM Payment p"),
    @NamedQuery(name = "Payment.findByPaymentId", query = "SELECT p FROM Payment p WHERE p.paymentId = :paymentId"),
    @NamedQuery(name = "Payment.findByPaymentDueDate", query = "SELECT p FROM Payment p WHERE p.paymentDueDate = :paymentDueDate"),
    @NamedQuery(name = "Payment.findByPaymentFilingDate", query = "SELECT p FROM Payment p WHERE p.paymentFilingDate = :paymentFilingDate"),
    @NamedQuery(name = "Payment.findByPaymentPaidDate", query = "SELECT p FROM Payment p WHERE p.paymentPaidDate = :paymentPaidDate")})
public class Payment implements Serializable {
    @Column(name = "payment_last_notification_date")
    @Temporal(TemporalType.DATE)
    private Date paymentLastNotificationDate;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "payment_id", nullable = false, length = 36)
    private String paymentId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "payment_due_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date paymentDueDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "payment_filing_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date paymentFilingDate;
    @Column(name = "payment_paid_date")
    @Temporal(TemporalType.DATE)
    private Date paymentPaidDate;
    @JoinColumn(name = "payment_bill_id", referencedColumnName = "bill_id", nullable = false)
    @ManyToOne(optional = false)
    private Bill paymentBillId;
    @JoinColumn(name = "payment_user_id", referencedColumnName = "user_id", nullable = false)
    @ManyToOne(optional = false)
    private User paymentUserId;

    public Payment() {
    }

    public Payment(String paymentId) {
        this.paymentId = paymentId;
    }
    
    public Payment(User user, Bill bill, Date billDueDate, Date fileDate) {
        Preconditions.checkNotNull(user, user);
        Preconditions.checkNotNull(bill, bill);
        Preconditions.checkNotNull(billDueDate, billDueDate);
        Preconditions.checkNotNull(fileDate, fileDate);
        StringBuilder sb = new StringBuilder(user.getUserId());
        sb.append(bill.getBillId());
        sb.append(fileDate.getTime());
        this.paymentId = String.valueOf(UUID.fromString(sb.toString()));
        this.paymentDueDate = billDueDate;
        this.paymentFilingDate = fileDate;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Date getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(Date paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public Date getPaymentFilingDate() {
        return paymentFilingDate;
    }

    public void setPaymentFilingDate(Date paymentFilingDate) {
        this.paymentFilingDate = paymentFilingDate;
    }

    public Date getPaymentPaidDate() {
        return paymentPaidDate;
    }

    public void setPaymentPaidDate(Date paymentPaidDate) {
        this.paymentPaidDate = paymentPaidDate;
    }

    public Bill getPaymentBillId() {
        return paymentBillId;
    }

    public void setPaymentBillId(Bill paymentBillId) {
        this.paymentBillId = paymentBillId;
    }

    public User getPaymentUserId() {
        return paymentUserId;
    }

    public void setPaymentUserId(User paymentUserId) {
        this.paymentUserId = paymentUserId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paymentId != null ? paymentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) object;
        return Objects.equals(paymentId, other.paymentId);
    }

    @Override
    public String toString() {
        return "com.mac.entities.Payment[ paymentId=" + paymentId + " ]";
    }

    public Date getPaymentLastNotificationDate() {
        return paymentLastNotificationDate;
    }

    public void setPaymentLastNotificationDate(Date paymentLastNotificationDate) {
        this.paymentLastNotificationDate = paymentLastNotificationDate;
    }
    
}
