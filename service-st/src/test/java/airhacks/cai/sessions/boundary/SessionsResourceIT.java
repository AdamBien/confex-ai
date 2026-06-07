package airhacks.cai.sessions.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;

@QuarkusTest
class SessionsResourceIT {

    @Inject
    @RestClient
    SessionsResourceClient client;

    @Test
    void missingNameIsRejected() {
        var invalid = Json.createObjectBuilder()
                .add("description", "no title")
                .build();

        try (var response = this.client.add(invalid)) {
            assertThat(response.getStatus()).isEqualTo(400);
        }
    }

    @Test
    void endBeforeStartIsRejected() {
        var invalid = Json.createObjectBuilder()
                .add("name", "Time Travel")
                .add("startDate", "2026-09-15T11:00:00Z")
                .add("endDate", "2026-09-15T10:00:00Z")
                .build();

        try (var response = this.client.add(invalid)) {
            assertThat(response.getStatus()).isEqualTo(400);
        }
    }

    @Test
    void addAndListKeynote() {
        var duke = Json.createObjectBuilder()
                .add("name", "Duke")
                .add("email", "duke@java.net")
                .add("jobTitle", "Mascot")
                .add("affiliation", "Oracle");

        var keynote = Json.createObjectBuilder()
                .add("name", "Effective Java")
                .add("description", "Idiomatic patterns and pitfalls.")
                .add("startDate", "2026-09-15T10:00:00Z")
                .add("endDate", "2026-09-15T11:00:00Z")
                .add("performer", duke)
                .build();

        try (var response = this.client.add(keynote)) {
            assertThat(response.getStatus()).isEqualTo(202);
        }

        try (var response = this.client.all()) {
            assertThat(response.getStatus()).isEqualTo(200);
            var sessions = response.readEntity(JsonArray.class);
            assertThat(sessions).isNotEmpty();
            var last = sessions.getJsonObject(sessions.size() - 1);
            assertThat(last.getString("name")).isEqualTo("Effective Java");
            var performer = last.getJsonObject("performer");
            assertThat(performer.getString("name")).isEqualTo("Duke");
            assertThat(performer.getString("affiliation")).isEqualTo("Oracle");
        }
    }
}
