package cryptogame.model.services.session;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The implementation of the {@link Session} interface.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SessionImplementation implements Session {

    /**
     * The id of the logged in user.
     */
    private long userId;

    /**
     * A no-args constructor
     */
    public SessionImplementation() {}

    /**
     * A constructor that accepts the logged in user's id.
     *
     * @param userId the id of the logged in user
     */
    public SessionImplementation(long userId) {
        this.userId = userId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getActiveUserId() {
        return this.userId;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }
}
