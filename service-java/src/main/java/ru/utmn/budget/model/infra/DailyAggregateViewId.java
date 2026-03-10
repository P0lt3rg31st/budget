package ru.utmn.budget.model.infra;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DailyAggregateViewId implements Serializable {
    private Long userId;
    private LocalDate date;
    private String currency;
}