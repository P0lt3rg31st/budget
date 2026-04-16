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
import org.hibernate.type.SqlTypes;
import ru.utmn.budget.util.ScenarioType;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "scenarios")
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 40)
    private ScenarioType type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "parameters", nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> parameters;

    @Column(name = "run_forecast", nullable = false)
    private boolean runForecast = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_forecast_run_id")
    private ForecastRun resultForecastRun;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}