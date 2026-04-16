--liquibase formatted sql

--changeset petrichor:013-1-drop-daily-aggregates-view-before-transaction-refactor
DROP VIEW IF EXISTS v_daily_aggregates;