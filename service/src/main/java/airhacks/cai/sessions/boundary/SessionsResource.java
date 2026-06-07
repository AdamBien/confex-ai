package airhacks.cai.sessions.boundary;

import airhacks.cai.sessions.control.Sessions;
import airhacks.cai.sessions.entity.Session;
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

@Path("sessions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SessionsResource {

    @Inject
    Sessions sessions;

    @GET
    public Response all() {
        var array = this.sessions.all().stream()
                .map(Session::toJSON)
                .collect(JsonCollectors.toJsonArray());
        return Response.ok(array).build();
    }

    @POST
    public Response add(JsonObject json) {
        var session = Session.fromJSON(json);
        this.sessions.add(session);
        return Response.accepted(session.toJSON()).build();
    }
}
