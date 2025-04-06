package ru.savin.currencyhub.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "conversion_rates")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversionRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('conversion_rates_id_seq'::regclass)")
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_code", referencedColumnName = "code")
    private Currency sourceCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_code", referencedColumnName = "code")
    private Currency destinationCode;

    @ColumnDefault("now()")
    @Column(name = "rate_begin_time")
    private LocalDateTime rateBeginTime;

    @Column(name = "rate_end_time")
    private LocalDateTime rateEndTime;

    @Column(name = "rate")
    private BigDecimal rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_code")
    private RateProvider providerCode;

    @Column(name = "multiplier")
    private BigDecimal multiplier;

    @Column(name = "system_rate")
    private BigDecimal systemRate;

}