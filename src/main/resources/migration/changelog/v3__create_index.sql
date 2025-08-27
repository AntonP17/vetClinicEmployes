--liquibase formatted sql
--changeset antoxakon:3  -- Формат: author:id
--comment: create index in table

create index if not exists employe_uuid_index
    on employes (employee_id);

create index if not exists employe_role_index
    on employes (role);