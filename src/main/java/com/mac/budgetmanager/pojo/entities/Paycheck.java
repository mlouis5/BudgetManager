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
@Table(name = "paycheck", catalog = "finance", schema = "budget")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Paycheck.findAll", query = "SELECT p FROM Paycheck p"),
    @NamedQuery(name = "Paycheck.findByPaycheckId", query = "SELECT p FROM Paycheck p WHERE p.paycheckId = :paycheckId"),
    @NamedQuery(name = "Paycheck.findByPaycheckSource", query = "SELECT p FROM Paycheck p WHERE p.paycheckSource = :paycheckSource"),
    @NamedQuery(name = "Paycheck.findByPaycheckOccurrence", query = "SELECT p FROM Paycheck p WHERE p.paycheckOccurrence = :paycheckOccurrence"),
    @NamedQuery(name = "Paycheck.findByPaycheckGrossAmount", query = "SELECT p FROM Paycheck p WHERE p.paycheckGrossAmount = :paycheckGrossAmount"),
    @NamedQuery(name = "Paycheck.findByPaycheckNetAmount", query = "SELECT p FROM Paycheck p WHERE p.paycheckNetAmount = :paycheckNetAmount"),
    @NamedQuery(name = "Paycheck.findByPaycheckFilingStatus", query = "SELECT p FROM Paycheck p WHERE p.paycheckFilingStatus = :paycheckFilingStatus"),
    @NamedQuery(name = "Paycheck.findByPaycheckTotalAllowances", query = "SELECT p FROM Paycheck p WHERE p.paycheckTotalAllowances = :paycheckTotalAllowances"),
    @NamedQuery(name = "Paycheck.findByPaycheckSourceState", query = "SELECT p FROM Paycheck p WHERE p.paycheckSourceState = :paycheckSourceState"),
    @NamedQuery(name = "Paycheck.findByPaycheck401kDeduction", query = "SELECT p FROM Paycheck p WHERE p.paycheck401kDeduction = :paycheck401kDeduction"),
    @NamedQuery(name = "Paycheck.findByPaycheckIs401kPercentage", query = "SELECT p FROM Paycheck p WHERE p.paycheckIs401kPercentage = :paycheckIs401kPercentage"),
    @NamedQuery(name = "Paycheck.findByPaycheckIs401kPreTax", query = "SELECT p FROM Paycheck p WHERE p.paycheckIs401kPreTax = :paycheckIs401kPreTax"),
    @NamedQuery(name = "Paycheck.findByPaycheckTotalPreTaxDeduction", query = "SELECT p FROM Paycheck p WHERE p.paycheckTotalPreTaxDeduction = :paycheckTotalPreTaxDeduction"),
    @NamedQuery(name = "Paycheck.findByPaycheckPreTaxDeductionLabels", query = "SELECT p FROM Paycheck p WHERE p.paycheckPreTaxDeductionLabels = :paycheckPreTaxDeductionLabels"),
    @NamedQuery(name = "Paycheck.findByPaycheckTotalPostTaxDeduction", query = "SELECT p FROM Paycheck p WHERE p.paycheckTotalPostTaxDeduction = :paycheckTotalPostTaxDeduction"),
    @NamedQuery(name = "Paycheck.findByPaycheckPostTaxDeductionLabels", query = "SELECT p FROM Paycheck p WHERE p.paycheckPostTaxDeductionLabels = :paycheckPostTaxDeductionLabels"),
    @NamedQuery(name = "Paycheck.findByPaycheckSourceObservedHolidays", query = "SELECT p FROM Paycheck p WHERE p.paycheckSourceObservedHolidays = :paycheckSourceObservedHolidays")})
public class Paycheck implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "paycheck_id", nullable = false, length = 36)
    private String paycheckId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "paycheck_source", nullable = false, length = 256)
    private String paycheckSource;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paycheck_occurrence", nullable = false)
    private int paycheckOccurrence;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paycheck_gross_amount", nullable = false)
    private double paycheckGrossAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paycheck_net_amount", nullable = false)
    private double paycheckNetAmount;
    @Column(name = "paycheck_filing_status")
    private Integer paycheckFilingStatus;
    @Column(name = "paycheck_total_allowances")
    private Integer paycheckTotalAllowances;
    @Size(max = 2)
    @Column(name = "paycheck_source_state", length = 2)
    private String paycheckSourceState;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "paycheck_401k_deduction", precision = 17, scale = 17)
    private Double paycheck401kDeduction;
    @Column(name = "paycheck_is_401k_percentage")
    private Boolean paycheckIs401kPercentage;
    @Column(name = "paycheck_is_401k_pre_tax")
    private Boolean paycheckIs401kPreTax;
    @Column(name = "paycheck_total_pre_tax_deduction", precision = 17, scale = 17)
    private Double paycheckTotalPreTaxDeduction;
    @Size(max = 2147483647)
    @Column(name = "paycheck_pre_tax_deduction_labels", length = 2147483647)
    private String paycheckPreTaxDeductionLabels;
    @Column(name = "paycheck_total_post_tax_deduction", precision = 17, scale = 17)
    private Double paycheckTotalPostTaxDeduction;
    @Size(max = 2147483647)
    @Column(name = "paycheck_post_tax_deduction_labels", length = 2147483647)
    private String paycheckPostTaxDeductionLabels;
    @Size(max = 2147483647)
    @Column(name = "paycheck_source_observed_holidays", length = 2147483647)
    private String paycheckSourceObservedHolidays;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "incomePaycheckId", fetch = FetchType.EAGER)
    private List<Income> incomeList;
    @JoinColumn(name = "paycheck_owner", referencedColumnName = "user_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User paycheckOwner;

    public Paycheck() {
    }

    public Paycheck(String paycheckId) {
        this.paycheckId = paycheckId;
    }

    public Paycheck(String paycheckId, String paycheckSource, int paycheckOccurrence, double paycheckGrossAmount, double paycheckNetAmount) {
        this.paycheckId = paycheckId;
        this.paycheckSource = paycheckSource;
        this.paycheckOccurrence = paycheckOccurrence;
        this.paycheckGrossAmount = paycheckGrossAmount;
        this.paycheckNetAmount = paycheckNetAmount;
    }

    public String getPaycheckId() {
        return paycheckId;
    }

    public void setPaycheckId(String paycheckId) {
        this.paycheckId = paycheckId;
    }

    public String getPaycheckSource() {
        return paycheckSource;
    }

    public void setPaycheckSource(String paycheckSource) {
        this.paycheckSource = paycheckSource;
    }

    public int getPaycheckOccurrence() {
        return paycheckOccurrence;
    }

    public void setPaycheckOccurrence(int paycheckOccurrence) {
        this.paycheckOccurrence = paycheckOccurrence;
    }

    public double getPaycheckGrossAmount() {
        return paycheckGrossAmount;
    }

    public void setPaycheckGrossAmount(double paycheckGrossAmount) {
        this.paycheckGrossAmount = paycheckGrossAmount;
    }

    public double getPaycheckNetAmount() {
        return paycheckNetAmount;
    }

    public void setPaycheckNetAmount(double paycheckNetAmount) {
        this.paycheckNetAmount = paycheckNetAmount;
    }

    public Integer getPaycheckFilingStatus() {
        return paycheckFilingStatus;
    }

    public void setPaycheckFilingStatus(Integer paycheckFilingStatus) {
        this.paycheckFilingStatus = paycheckFilingStatus;
    }

    public Integer getPaycheckTotalAllowances() {
        return paycheckTotalAllowances;
    }

    public void setPaycheckTotalAllowances(Integer paycheckTotalAllowances) {
        this.paycheckTotalAllowances = paycheckTotalAllowances;
    }

    public String getPaycheckSourceState() {
        return paycheckSourceState;
    }

    public void setPaycheckSourceState(String paycheckSourceState) {
        this.paycheckSourceState = paycheckSourceState;
    }

    public Double getPaycheck401kDeduction() {
        return paycheck401kDeduction;
    }

    public void setPaycheck401kDeduction(Double paycheck401kDeduction) {
        this.paycheck401kDeduction = paycheck401kDeduction;
    }

    public Boolean getPaycheckIs401kPercentage() {
        return paycheckIs401kPercentage;
    }

    public void setPaycheckIs401kPercentage(Boolean paycheckIs401kPercentage) {
        this.paycheckIs401kPercentage = paycheckIs401kPercentage;
    }

    public Boolean getPaycheckIs401kPreTax() {
        return paycheckIs401kPreTax;
    }

    public void setPaycheckIs401kPreTax(Boolean paycheckIs401kPreTax) {
        this.paycheckIs401kPreTax = paycheckIs401kPreTax;
    }

    public Double getPaycheckTotalPreTaxDeduction() {
        return paycheckTotalPreTaxDeduction;
    }

    public void setPaycheckTotalPreTaxDeduction(Double paycheckTotalPreTaxDeduction) {
        this.paycheckTotalPreTaxDeduction = paycheckTotalPreTaxDeduction;
    }

    public String getPaycheckPreTaxDeductionLabels() {
        return paycheckPreTaxDeductionLabels;
    }

    public void setPaycheckPreTaxDeductionLabels(String paycheckPreTaxDeductionLabels) {
        this.paycheckPreTaxDeductionLabels = paycheckPreTaxDeductionLabels;
    }

    public Double getPaycheckTotalPostTaxDeduction() {
        return paycheckTotalPostTaxDeduction;
    }

    public void setPaycheckTotalPostTaxDeduction(Double paycheckTotalPostTaxDeduction) {
        this.paycheckTotalPostTaxDeduction = paycheckTotalPostTaxDeduction;
    }

    public String getPaycheckPostTaxDeductionLabels() {
        return paycheckPostTaxDeductionLabels;
    }

    public void setPaycheckPostTaxDeductionLabels(String paycheckPostTaxDeductionLabels) {
        this.paycheckPostTaxDeductionLabels = paycheckPostTaxDeductionLabels;
    }

    public String getPaycheckSourceObservedHolidays() {
        return paycheckSourceObservedHolidays;
    }

    public void setPaycheckSourceObservedHolidays(String paycheckSourceObservedHolidays) {
        this.paycheckSourceObservedHolidays = paycheckSourceObservedHolidays;
    }

    @XmlTransient
    public List<Income> getIncomeList() {
        return incomeList;
    }

    public void setIncomeList(List<Income> incomeList) {
        this.incomeList = incomeList;
    }

    public User getPaycheckOwner() {
        return paycheckOwner;
    }

    public void setPaycheckOwner(User paycheckOwner) {
        this.paycheckOwner = paycheckOwner;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paycheckId != null ? paycheckId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paycheck)) {
            return false;
        }
        Paycheck other = (Paycheck) object;
        if ((this.paycheckId == null && other.paycheckId != null) || (this.paycheckId != null && !this.paycheckId.equals(other.paycheckId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mac.budgetmanager.pojo.entities.Paycheck[ paycheckId=" + paycheckId + " ]";
    }
    
}
