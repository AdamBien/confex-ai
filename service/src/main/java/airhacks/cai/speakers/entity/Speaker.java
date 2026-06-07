package airhacks.cai.speakers.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

/**
 * Conference speaker modeled after <a href="https://schema.org/Person">schema.org/Person</a>.
 *
 * @param name        <a href="https://schema.org/name">schema.org/name</a>
 * @param email       <a href="https://schema.org/email">schema.org/email</a>
 * @param jobTitle    <a href="https://schema.org/jobTitle">schema.org/jobTitle</a>
 * @param affiliation <a href="https://schema.org/affiliation">schema.org/affiliation</a> (organization name)
 * @param url         <a href="https://schema.org/url">schema.org/url</a> (homepage)
 * @param description <a href="https://schema.org/description">schema.org/description</a> (bio)
 */
public record Speaker(
        String name,
        String email,
        String jobTitle,
        String affiliation,
        String url,
        String description) {

    public JsonObject toJSON() {
        var builder = Json.createObjectBuilder().add("name", this.name);
        addIfPresent(builder, "email", this.email);
        addIfPresent(builder, "jobTitle", this.jobTitle);
        addIfPresent(builder, "affiliation", this.affiliation);
        addIfPresent(builder, "url", this.url);
        addIfPresent(builder, "description", this.description);
        return builder.build();
    }

    public static Speaker fromJSON(JsonObject json) {
        return new Speaker(
                json.getString("name"),
                json.getString("email", null),
                json.getString("jobTitle", null),
                json.getString("affiliation", null),
                json.getString("url", null),
                json.getString("description", null));
    }

    static void addIfPresent(JsonObjectBuilder builder, String key, String value) {
        if (value != null) {
            builder.add(key, value);
        }
    }
}
