package cryptogame.model.services;

public interface ServiceHandler {

    void addService(Class<?> classInterface, Class<?> classImplementation) throws Exception;
    void addServiceInstance(Class<?> classInterface, Object instance);
    //<T> T getInterfaceImplementation(Class<?> classInterface);
    Object getInterfaceImplementation(Class<?> classInterface);
    Object injectDependencies(Class<?> implementation) throws Exception;

}
