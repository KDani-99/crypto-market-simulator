package cryptogame.model.services.session;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SessionImplementation implements Session {

    private long userId;

    public SessionImplementation() {}
    public SessionImplementation(long userId) {
        this.userId = userId;
    }

    @Override
    public long getActiveUserId() {
        return this.userId;
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }
}
