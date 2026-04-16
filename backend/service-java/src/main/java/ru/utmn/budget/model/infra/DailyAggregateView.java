package ru.utmn.budget.model.infra;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Immutable
@Table(name = "v_daily_aggregates")
@IdClass(DailyAggregateViewId.class)
public class DailyAggregateView {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Id
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "expense_total", precision = 19, scale = 8)
    private BigDecimal expenseTotal;

    @Column(name = "income_total", precision = 19, scale = 8)
    private BigDecimal incomeTotal;
}
