package ru.savin.currencyhub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "rate_correction_coefficients")
public class RateCorrectionCoefficient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('rate_correction_coefficients_id_seq'::regclass)")
    @Column(name = "id")
    private Long id;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @ColumnDefault("false")
    @Column(name = "archived")
    private Boolean archived;

    @Column(name = "source_code")
    private String sourceCode;

    @Column(name = "destination_code")
    private String destinationCode;

    @Column(name = "multiplier")
    private BigDecimal multiplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_code")
    private RateProvider providerCode;

    @Column(name = "creator")
    private String creator;

    @Column(name = "modifier")
    private String modifier;

    @Column(name = "date_from")
    private LocalDate dateFrom;

    @Column(name = "date_to")
    private LocalDate dateTo;

    @Column(name = "profile_type", length = 50)
    private String profileType;

}