package ru.savin.currencyhub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "rate_providers")
@AllArgsConstructor
@NoArgsConstructor
public class RateProvider {
    @Id
    @Column(name = "provider_code")
    private String providerCode;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "provider_name")
    private String providerName;

    @Column(name = "description")
    private String description;

    @Column(name = "priority")
    private Integer priority;

    @ColumnDefault("true")
    @Column(name = "active")
    private Boolean active;

    @ColumnDefault("1.0")
    @Column(name = "default_multiplier")
    private BigDecimal defaultMultiplier;

}