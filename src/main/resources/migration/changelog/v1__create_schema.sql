--liquibase formatted sql
--changeset antoxakon:1  -- Формат: author:id
--comment: create schema currentSchema=vet_doctors

create schema if not exists vet_doctors;

