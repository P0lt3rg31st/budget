--liquibase formatted sql

--changeset petrichor:015-recreate-daily-aggregates-view-after-transaction-refactor
CREATE VIEW v_daily_aggregates AS
SELECT
    a.user_id                                                  AS user_id,
    (t.occurred_at AT TIME ZONE 'UTC')::DATE                   AS date,
    a.currency                                                 AS currency,
    SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END) AS expense_total,
    SUM(CASE WHEN t.type = 'INCOME'  THEN t.amount ELSE 0 END) AS income_total
FROM transactions t
         JOIN accounts a ON a.id = t.account_id
GROUP BY
    a.user_id,
    (t.occurred_at AT TIME ZONE 'UTC')::DATE,
    a.currency;