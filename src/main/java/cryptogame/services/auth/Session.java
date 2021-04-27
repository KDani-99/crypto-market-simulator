package cryptogame.services.auth;

import cryptogame.models.UserModel;

import java.time.Instant;

public class Session {

    private UserModel activeUser;
    private long startTimestamp;

    public Session(UserModel user) {
        this.activeUser = user;
        this.startTimestamp = Instant.now().getEpochSecond();
    }

}
