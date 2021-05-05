package cryptogame.model.services.session;

public class SessionImplementation implements Session {

    private final long userId;

    public SessionImplementation(long userId) {
        this.userId = userId;
    }

    @Override
    public long getActiveUserId() {
        return this.userId;
    }
}
