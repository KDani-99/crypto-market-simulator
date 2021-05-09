package cryptogame.model;

import cryptogame.model.services.Service;
import cryptogame.model.services.ServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class ServiceTest {

    private Service service;

    @BeforeEach
    public void reset() {
        this.service = new ServiceImplementation(null,null,null,null);
    }

    @Test
    public void testServiceDestroySessionShouldReturnNull() {
        // Given
        service.destroyActiveSession();
        // When
        var session = service.getSession();
        // Then
        Assert.isNull(session);
    }

}
