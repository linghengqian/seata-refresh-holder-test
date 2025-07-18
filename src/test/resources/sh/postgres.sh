#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE DATABASE demo_ds_0;
  CREATE DATABASE demo_ds_1;
  CREATE DATABASE demo_ds_2;
EOSQL

for i in "demo_ds_0" "demo_ds_1" "demo_ds_2"
do
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$i" <<-EOSQL
  CREATE TABLE IF NOT EXISTS public.undo_log
  (
      id            SERIAL       NOT NULL,
      branch_id     BIGINT       NOT NULL,
      xid           VARCHAR(128) NOT NULL,
      context       VARCHAR(128) NOT NULL,
      rollback_info BYTEA        NOT NULL,
      log_status    INT          NOT NULL,
      log_created   TIMESTAMP(0) NOT NULL,
      log_modified  TIMESTAMP(0) NOT NULL,
      CONSTRAINT pk_undo_log PRIMARY KEY (id),
      CONSTRAINT ux_undo_log UNIQUE (xid, branch_id)
      );
  CREATE INDEX ix_log_created ON undo_log(log_created);
  COMMENT ON TABLE public.undo_log IS 'AT transaction mode undo table';
  COMMENT ON COLUMN public.undo_log.branch_id IS 'branch transaction id';
  COMMENT ON COLUMN public.undo_log.xid IS 'global transaction id';
  COMMENT ON COLUMN public.undo_log.context IS 'undo_log context,such as serialization';
  COMMENT ON COLUMN public.undo_log.rollback_info IS 'rollback info';
  COMMENT ON COLUMN public.undo_log.log_status IS '0:normal status,1:defense status';
  COMMENT ON COLUMN public.undo_log.log_created IS 'create datetime';
  COMMENT ON COLUMN public.undo_log.log_modified IS 'modify datetime';
  CREATE SEQUENCE IF NOT EXISTS undo_log_id_seq INCREMENT BY 1 MINVALUE 1 ;
EOSQL
done
