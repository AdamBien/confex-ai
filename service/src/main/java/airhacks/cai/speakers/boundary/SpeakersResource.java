package airhacks.cai.speakers.boundary;

import airhacks.cai.speakers.control.Speakers;
import airhacks.cai.speakers.entity.Speaker;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("speakers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SpeakersResource {

    @Inject
    Speakers speakers;

    @GET
    public Response all() {
        var array = this.speakers.all().stream()
                .map(Speaker::toJSON)
                .collect(JsonCollectors.toJsonArray());
        return Response.ok(array).build();
    }

    @POST
    public Response add(JsonObject json) {
        var speaker = Speaker.fromJSON(json);
        this.speakers.add(speaker);
        return Response.accepted(speaker.toJSON()).build();
    }
}
