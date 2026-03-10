package ru.utmn.budget.model.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import ru.utmn.budget.enam.forecast.ForecastModelType;
import ru.utmn.budget.enam.forecast.ForecastRunStatus;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "forecast_runs")
public class ForecastRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ForecastRunStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "model_type", nullable = false, length = 20)
    private ForecastModelType modelType;

    @Column(name = "horizon_days", nullable = false)
    private Integer horizonDays;

    @Column(name = "history_days")
    private Integer historyDays;

    @Column(name = "include_income", nullable = false)
    private boolean includeIncome = true;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "confidence_levels", columnDefinition = "jsonb")
    private List<Double> confidenceLevels;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metrics", columnDefinition = "jsonb")
    private Map<String, Object> metrics;

    @Column(name = "error_message", columnDefinition = "text")
    private String errorMessage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}