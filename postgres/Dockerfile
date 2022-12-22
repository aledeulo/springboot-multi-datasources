FROM postgres:13.6-alpine
COPY ./bootstrap/create_db_and_user.sh /docker-entrypoint-initdb.d/
RUN sed -i -e 's/\r$//' docker-entrypoint-initdb.d/*.sh