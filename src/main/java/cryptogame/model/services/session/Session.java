package cryptogame.model.services.session;

public interface Session {
    long getActiveUserId();
    void setUserId(long userId);
}
