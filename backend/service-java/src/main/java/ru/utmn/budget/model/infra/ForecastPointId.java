package ru.utmn.budget.model.infra;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ForecastPointId implements Serializable {
    private Long run;
    private LocalDate date;
}