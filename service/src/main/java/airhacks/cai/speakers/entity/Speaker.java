package airhacks.cai.speakers.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Speaker(String name, String topic) {

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("name", this.name)
                .add("topic", this.topic)
                .build();
    }

    public static Speaker fromJSON(JsonObject json) {
        return new Speaker(json.getString("name"), json.getString("topic"));
    }
}
