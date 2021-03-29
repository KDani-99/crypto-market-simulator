package cryptogame.model.session.utils;

import cryptogame.model.database.json.entities.User;

import java.time.Instant;

public class Session {

    private User activeUser;
    private long startTimestamp;

    public Session(User user) {
        this.activeUser = user;
        this.startTimestamp = Instant.now().getEpochSecond();
    }

}
