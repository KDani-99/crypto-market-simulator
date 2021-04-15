package cryptogame.model.services.session;

import cryptogame.model.common.validation.Validation;
import cryptogame.model.dao.IDao;
import cryptogame.model.database.jpa.entities.User;
import cryptogame.model.database.jpa.entities.Session;
import cryptogame.model.session.utils.Auth;

import java.util.HashSet;
import java.util.Set;


public class SessionManager implements ISession {

    private final IDao<User,String> userDao;
    private final IDao<Session,String> sessionDao;

    private Session activeSession;

    public SessionManager(IDao<User,String> userDao,IDao<Session,String> sessionDao) {
        this.userDao = userDao;
        this.sessionDao = sessionDao;
        this.activeSession = null;
    }


    public void register(String username, String email,String password) throws Exception {

       /* if(!Validation.validateUsername(username)) {
            throw new InvalidUsernameException(4,50);
        }

        if(!Validation.validateEmail(email)) {
            throw new InvalidEmailException();
        }

        if(!Validation.validatePassword(password)) {
            throw new InvalidPasswordException();
        }*/
        var user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        var validationResult = Validation.validateObject(user);

        if(validationResult.size() > 0) {
            // TODO: Throw exception
            return;
        }

        // TODO: Move this to controller
        if(this.userDao.getEntityBy("username",username).isPresent()) {
            throw new Exception("Username is already taken");
        }

        if(this.userDao.getEntityBy("email",email).isPresent()) {
            throw new Exception("Email is already taken");
        }

        user.setPassword(Auth.generatePasswordHash(password));

        this.userDao.persistEntity(user);
    }

    public void login(String username, String password) throws Exception {

        var tmp = this.userDao.getEntityBy("username",username);

        if(tmp.isEmpty()) {
            throw new Exception("Invalid username or password");
        }

        if(!Auth.comparePasswords(tmp.get().getPassword(),password)) {
            throw new Exception("Invalid username or password");
        }

        var session = new Session();
        session.setUserId(tmp.get().getId());

        this.sessionDao.persistEntity(
                session
        );
    }

    public void logout() throws Exception {
        if(activeSession == null) {
            throw new Exception("User is not logged in");
        }

        activeSession = null;
    }

    public Session getActiveSession() {
        return this.activeSession;
    }
}
