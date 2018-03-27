FROM vertx/vertx3

ENV VERTICLE_NAME io.vertx.beer.BeerService
ENV VERTICLE_FILE target/beer-app-1.0-SNAPSHOT-fat.jar
#ENV VERTICLE_CONF_FILEs target/beer-app-1.0-SNAPSHOT-fat.jar

ENV VERTICLE_HOME /usr/verticles

EXPOSE 8080

COPY $VERTICLE_FILE $VERTICLE_HOME/

WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec vertx run $VERTICLE_NAME -cp $VERTICLE_HOME/* -conf '{\"connection_string\":\"mongodb://mongodb:27017\"'}''"]