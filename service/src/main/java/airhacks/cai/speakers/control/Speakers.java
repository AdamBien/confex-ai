package airhacks.cai.speakers.control;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import airhacks.cai.speakers.entity.Speaker;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Speakers {

    private final List<Speaker> speakers = new CopyOnWriteArrayList<>();

    public void add(Speaker speaker) {
        this.speakers.add(speaker);
    }

    public List<Speaker> all() {
        return List.copyOf(this.speakers);
    }
}
