package cryptogame.service.auth;

import cryptogame.models.User;

import java.time.Instant;

public class Session {

    private User activeUser;
    private long startTimestamp;

    public Session(User user) {
        this.activeUser = user;
        this.startTimestamp = Instant.now().getEpochSecond();
    }

}
