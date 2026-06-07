package airhacks.cai.sessions.entity;

import java.time.Instant;

import airhacks.cai.speakers.entity.Speaker;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.BadRequestException;

/**
 * Conference session modeled after <a href="https://schema.org/Event">schema.org/Event</a>.
 *
 * @param name        <a href="https://schema.org/name">schema.org/name</a> (talk title)
 * @param description <a href="https://schema.org/description">schema.org/description</a> (abstract)
 * @param startDate   <a href="https://schema.org/startDate">schema.org/startDate</a> (ISO-8601 instant)
 * @param endDate     <a href="https://schema.org/endDate">schema.org/endDate</a> (ISO-8601 instant)
 * @param performer   <a href="https://schema.org/performer">schema.org/performer</a> (the {@link Speaker})
 */
public record Session(
        String name,
        String description,
        Instant startDate,
        Instant endDate,
        Speaker performer) {

    public Session {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("session name must not be blank");
        }
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new BadRequestException("endDate %s is before startDate %s".formatted(endDate, startDate));
        }
    }

    public JsonObject toJSON() {
        var builder = Json.createObjectBuilder().add("name", this.name);
        addIfPresent(builder, "description", this.description);
        addInstantIfPresent(builder, "startDate", this.startDate);
        addInstantIfPresent(builder, "endDate", this.endDate);
        if (this.performer != null) {
            builder.add("performer", this.performer.toJSON());
        }
        return builder.build();
    }

    public static Session fromJSON(JsonObject json) {
        return new Session(
                json.getString("name", null),
                json.getString("description", null),
                parseInstant(json.getString("startDate", null)),
                parseInstant(json.getString("endDate", null)),
                parsePerformer(json));
    }

    static Speaker parsePerformer(JsonObject json) {
        if (!json.containsKey("performer") || json.isNull("performer")) {
            return null;
        }
        return Speaker.fromJSON(json.getJsonObject("performer"));
    }

    static Instant parseInstant(String value) {
        return value == null ? null : Instant.parse(value);
    }

    static void addIfPresent(JsonObjectBuilder builder, String key, String value) {
        if (value != null) {
            builder.add(key, value);
        }
    }

    static void addInstantIfPresent(JsonObjectBuilder builder, String key, Instant value) {
        if (value != null) {
            builder.add(key, value.toString());
        }
    }
}
