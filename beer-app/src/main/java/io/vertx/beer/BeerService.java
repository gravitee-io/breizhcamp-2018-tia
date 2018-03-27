package io.vertx.beer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import java.util.List;
import java.util.stream.Collectors;

public class BeerService extends AbstractVerticle {
    public static final String COLLECTION = "beers";
    private MongoClient mongo;

    @Override
    public void start(Future<Void> fut) {

        /*this.config()
            .put("http.port", 8000);*/
        // Create a Mongo client
        mongo = MongoClient.createShared(vertx, config());

        createSomeData((nothing) -> startWebApp((http) -> completeStartup(http, fut)), fut);
    }

    private void startWebApp(Handler<AsyncResult<HttpServer>> next) {
        // Create a router object.
        Router router = Router.router(vertx);

        // Bind "/" to our hello message.
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/html").end("<h1>Beer Service</h1>");
        });

        //router.route("/assets/*").handler(StaticHandler.create("assets"));

        router.get("/api/beers").handler(this::getAll);
        router.route("/api/beers*").handler(BodyHandler.create());
        router.post("/api/beers").handler(this::addOne);
        router.get("/api/beers/:id").handler(this::getOne);
        router.put("/api/beers/:id").handler(this::updateOne);
        router.delete("/api/beers/:id").handler(this::deleteOne);
        router.get("/api/beers/ping").handler(this::ping);

        // Create the HTTP server and pass the "accept" method to the request handler.
        vertx.createHttpServer().requestHandler(router::accept).listen(
                // Retrieve the port from the configuration,
                // default to 8080.
                config().getInteger("http.port", 8080), next::handle);
    }

    private void completeStartup(AsyncResult<HttpServer> http, Future<Void> fut) {
        if (http.succeeded()) {
            fut.complete();
        } else {
            fut.fail(http.cause());
        }
    }

    @Override
    public void stop() throws Exception {
        mongo.close();
    }

    private void addOne(RoutingContext routingContext) {
        final Beer beer = Json.decodeValue(routingContext.getBodyAsString(), Beer.class);

        mongo.insert(COLLECTION, beer.toJson(),
                r -> routingContext.response().setStatusCode(201)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(beer.setId(r.result()))));
    }

    private void getOne(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            mongo.findOne(COLLECTION, new JsonObject().put("_id", id), null, ar -> {
                if (ar.succeeded()) {
                    if (ar.result() == null) {
                        routingContext.response().setStatusCode(404).end();
                        return;
                    }
                    Beer beer = new Beer(ar.result());
                    routingContext.response().setStatusCode(200)
                            .putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(beer));
                } else {
                    routingContext.response().setStatusCode(404).end();
                }
            });
        }
    }

    private void updateOne(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        JsonObject json = routingContext.getBodyAsJson();
        if (id == null || json == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            mongo.update(COLLECTION, new JsonObject().put("_id", id), // Select a unique document
                    // The update syntax: {$set, the json object containing the fields to update}
                    new JsonObject().put("$set", json), v -> {
                        if (v.failed()) {
                            routingContext.response().setStatusCode(404).end();
                        } else {
                            routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                                    .end(Json.encodePrettily(
                                            new Beer(id, json.getString("name"), json.getString("origin"))));
                        }
                    });
        }
    }

    private void deleteOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            mongo.removeOne(COLLECTION, new JsonObject().put("_id", id),
                    ar -> routingContext.response().setStatusCode(204).end());
        }
    }

    private void getAll(RoutingContext routingContext) {
        mongo.find(COLLECTION, new JsonObject(), results -> {
            List<JsonObject> objects = results.result();
            List<Beer> beers = objects.stream().map(Beer::new).collect(Collectors.toList());
            routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(beers));
        });
    }

    private void ping(RoutingContext routingContext) {
        routingContext.response().end("pong");
    }

    private void createSomeData(Handler<AsyncResult<Void>> next, Future<Void> fut) {
        Beer karmeliet = new Beer("Karmeliet", "Blond", "Tripel");
        Beer kwak = new Beer("Kwak", "Ambrée", "Belgian Pale Ale");
        System.out.println(karmeliet.toJson());

        // Do we have data in the collection ?
        mongo.count(COLLECTION, new JsonObject(), count -> {
            if (count.succeeded()) {
                if (count.result() == 0) {
                    // no whiskies, insert data
                    mongo.insert(COLLECTION, karmeliet.toJson(), ar -> {
                        if (ar.failed()) {
                            fut.fail(ar.cause());
                        } else {
                            mongo.insert(COLLECTION, kwak.toJson(), ar2 -> {
                                if (ar2.failed()) {
                                    fut.failed();
                                } else {
                                    next.handle(Future.<Void>succeededFuture());
                                }
                            });
                        }
                    });
                } else {
                    next.handle(Future.<Void>succeededFuture());
                }
            } else {
                // report the error
                fut.fail(count.cause());
            }
        });
    }
}