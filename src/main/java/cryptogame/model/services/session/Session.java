package cryptogame.model.services.session;

/**
 * The {@link Session} contains the data of the logged in user.
 */
public interface Session {
    /**
     * Returns the logged in user's id.
     *
     * @return the user's id
     */
    long getActiveUserId();

    /**
     * Sets the logged in user's id.
     *
     * @param userId the user's id
     */
    void setUserId(long userId);
}
