package io.vertx.beer;

import io.vertx.core.json.JsonObject;
import org.bson.codecs.CollectibleCodec;

public class Beer {

    private String id;

    private String name;
    private String style;
    private String color;

    public Beer(String name, String color, String style) {
        this.name = name;
        this.color = color;
        this.style = style;
    }

    public Beer(JsonObject json) {
        this.name = json.getString("name");
        this.color = json.getString("color");
        this.style = json.getString("style");

        this.id = json.getString("_id");
    }

    public Beer() {
        this.id = "";
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject().put("name", name).put("color", color).put("style", style);
        if (id != null && !id.isEmpty()) {
            json.put("_id", id);
        }
        return json;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getId() {
        return id;
    }

    public String getStyle() {
        return style;
    }

    public Beer setName(String name) {
        this.name = name;
        return this;
    }

    public Beer setOrigin(String color) {
        this.color = color;
        return this;
    }

    public Beer setId(String id) {
        this.id = id;
        return this;
    }

    public Beer setStyle(String style) {
        this.style = style;
        return this;
    }
}