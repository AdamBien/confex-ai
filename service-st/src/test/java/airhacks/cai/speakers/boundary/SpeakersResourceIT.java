package airhacks.cai.speakers.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;

@QuarkusTest
class SpeakersResourceIT {

    @Inject
    @RestClient
    SpeakersResourceClient client;

    @Test
    void addAndListDuke() {
        var duke = Json.createObjectBuilder()
                .add("name", "Duke")
                .add("email", "duke@java.net")
                .add("jobTitle", "Mascot")
                .add("affiliation", "Oracle")
                .add("url", "https://dev.java")
                .add("description", "The official Java mascot.")
                .build();

        try (var response = this.client.add(duke)) {
            assertThat(response.getStatus()).isEqualTo(202);
        }

        try (var response = this.client.all()) {
            assertThat(response.getStatus()).isEqualTo(200);
            var speakers = response.readEntity(JsonArray.class);
            assertThat(speakers).isNotEmpty();
            var last = speakers.getJsonObject(speakers.size() - 1);
            assertThat(last.getString("name")).isEqualTo("Duke");
            assertThat(last.getString("affiliation")).isEqualTo("Oracle");
        }
    }
}
