package ru.utmn.budget.model.infra;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.utmn.budget.model.domain.ForecastRun;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "forecast_points")
@IdClass(ForecastPointId.class)
public class ForecastPoint {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "run_id", nullable = false)
    private ForecastRun run;

    @Id
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "expense_p50", precision = 19, scale = 8)
    private BigDecimal expenseP50;

    @Column(name = "expense_p90", precision = 19, scale = 8)
    private BigDecimal expenseP90;

    @Column(name = "income_p50", precision = 19, scale = 8)
    private BigDecimal incomeP50;

    @Column(name = "income_p90", precision = 19, scale = 8)
    private BigDecimal incomeP90;
}