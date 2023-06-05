package tech.jazz.apianalisecredito.infrastructure.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Immutable
@Table(name = "CREDIT_ANALYSIS")
public class CreditAnalysisEntity {
    @Id
    UUID id;
    Boolean approved;
    BigDecimal approvedLimit;
    BigDecimal requestedAmount;
    BigDecimal withdraw;
    Float annualInterest;
    UUID clientId;
    @Column(name = "localDate")
    LocalDateTime date;
    @CreationTimestamp
    @Column(name = "createdAt")
    LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updatedAt")
    LocalDateTime updatedAt;

    @Builder
    public CreditAnalysisEntity(
            Boolean approved, BigDecimal approvedLimit, BigDecimal requestedAmount, BigDecimal withdraw,
            Float annualInterest, UUID clientId, LocalDateTime date) {
        this.id = UUID.randomUUID();
        this.approved = approved;
        this.approvedLimit = approvedLimit;
        this.requestedAmount = requestedAmount;
        this.withdraw = withdraw;
        this.annualInterest = annualInterest;
        this.clientId = clientId;
        this.date = date;
    }

    public CreditAnalysisEntity() {
    }

    public UUID getId() {
        return id;
    }

    public Boolean getApproved() {
        return approved;
    }

    public BigDecimal getApprovedLimit() {
        return approvedLimit;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public BigDecimal getWithdraw() {
        return withdraw;
    }

    public Float getAnnualInterest() {
        return annualInterest;
    }

    public UUID getClientId() {
        return clientId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
