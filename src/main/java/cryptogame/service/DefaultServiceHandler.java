package cryptogame.service;

import cryptogame.common.Initializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefaultServiceHandler implements ServiceHandler {

    private final HashMap<Class<?>,Object> serviceCollection;

    public DefaultServiceHandler()
    {
        serviceCollection = new HashMap<>();
        this.addServiceInstance(ServiceHandler.class, this);
    }
    @Override
    public void addServiceInstance(Class<?> classInterface, Object instance)
    {
        this.serviceCollection.put(classInterface, instance);
    }
    @Override
    public void addService(Class<?> classInterface, Class<?> classImplementation) throws Exception {
        var serviceInstance = injectDependencies(classImplementation);

        this.serviceCollection.put(classInterface,
                serviceInstance);

        if(Initializable.class.isAssignableFrom(classInterface)) {
            ((Initializable) serviceInstance).initialize();
        }
    }
    @Override
    public Object injectDependencies(Class<?> implementation) throws Exception
    {
        var constructors = implementation.getConstructors();

        for(var ctor : constructors)
        {
            List<Object> dependencies = new ArrayList<>();
            boolean isValid = true;

            var parameters = ctor.getParameterTypes();

            if(constructors.length == 1 && parameters.length == 0) {
                return ctor.newInstance();
            }

            for(var param : ctor.getParameterTypes())
            {
                if(!serviceCollection.containsKey(param))
                {
                    isValid = false;
                    break;
                }
                else {
                    dependencies.add(serviceCollection.get(param));
                }
            }

            if(isValid) {
                return ctor.newInstance(dependencies.toArray());
            }
        }

        return null;
    }
    @Override
    public Object getInterfaceImplementation(Class<?> classInterface) {
        return serviceCollection.get(classInterface);
    }
}
