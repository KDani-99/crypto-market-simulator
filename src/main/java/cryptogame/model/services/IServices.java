package cryptogame.model.services;

import cryptogame.controller.IController;
import cryptogame.model.common.Initializable;

import java.net.URL;

public interface IServices {

    void addService(Class<?> classInterface, Class<?> classImplementation) throws Exception;
    void addServiceInstance(Class<?> classInterface, Object instance);
    Object injectDependencies(Class<?> implementation) throws Exception;

}
