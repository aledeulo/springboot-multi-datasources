Configure database to activate preparing transaction feature:

```
/var/lib/postgresql/data/postgresql.conf
---------------------------------------------------------------
max_prepared_transactions = 64      # zero disables the feature
```