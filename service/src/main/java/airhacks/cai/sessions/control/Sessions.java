package airhacks.cai.sessions.control;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import airhacks.cai.sessions.entity.Session;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Sessions {

    private final List<Session> sessions = new CopyOnWriteArrayList<>();

    public void add(Session session) {
        this.sessions.add(session);
    }

    public List<Session> all() {
        return List.copyOf(this.sessions);
    }
}
