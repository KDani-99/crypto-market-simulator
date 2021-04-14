package cryptogame.model.services.session;

public interface ISession {

    void login(String username, String password) throws Exception;
    void register(String username, String email, String password) throws Exception;
    void logout() throws Exception;

}
