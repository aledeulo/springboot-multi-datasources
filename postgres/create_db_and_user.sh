#! /bin/bash -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE USER user WITH password 'P@ssw0rd';
    ALTER USER user WITH SUPERUSER;
    ALTER USER user WITH createrole;
    ALTER USER user WITH createdb;
    CREATE DATABASE db WITH OWNER user;
EOSQL