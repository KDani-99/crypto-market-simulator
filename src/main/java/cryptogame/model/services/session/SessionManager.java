package cryptogame.model.services.session;

import cryptogame.model.dao.IDao;
import cryptogame.model.dao.SessionDao;
import cryptogame.model.dao.UserDao;
import cryptogame.model.database.jpa.entities.User;
import cryptogame.model.database.jpa.entities.Session;
import cryptogame.model.database.jpa.entities.UserSettings;
import cryptogame.model.exceptions.InvalidEmailException;
import cryptogame.model.exceptions.InvalidPasswordException;
import cryptogame.model.exceptions.InvalidUsernameException;
import cryptogame.model.session.utils.Auth;
import cryptogame.model.session.utils.Validation;


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

        if(!Validation.validateUsername(username)) {
            throw new InvalidUsernameException(4,50);
        }

        if(!Validation.validateEmail(email)) {
            throw new InvalidEmailException();
        }

        if(!Validation.validatePassword(password)) {
            throw new InvalidPasswordException();
        }

        if(this.userDao.getEntityBy("username",username).isPresent()) {
            throw new Exception("Username is already taken");
        }

        if(this.userDao.getEntityBy("email",email).isPresent()) {
            throw new Exception("Email is already taken");
        }

        var user = new User(username,email,Auth.generatePasswordHash(password));
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
