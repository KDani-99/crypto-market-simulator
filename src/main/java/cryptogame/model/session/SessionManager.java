package cryptogame.model.session;

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


public class SessionManager {

    private final UserDao userDao;
    private final SessionDao sessionDao;

    private Session activeSession;

    public SessionManager(UserDao userDao,SessionDao sessionDao) {
        this.userDao = userDao;
        this.sessionDao = sessionDao;
        this.activeSession = null;
    }

    public void register(String username, String email,String password, String password2) throws Exception {

        if(!Validation.validateUsername(username)) {
            throw new InvalidUsernameException();
        }

        if(!Validation.validateEmail(email)) {
            throw new InvalidEmailException();
        }

        if(!Validation.validatePassword(password)) {
            throw new InvalidPasswordException();
        }

        if(!password.equals(password2)) {
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

        System.out.println("Settings: "+tmp.get().getSettings());

        var session = new Session();
        session.setUserId(tmp.get().getId());

        this.sessionDao.persistEntity(
                session
        );

        //this.activeSession = new Session(tmp.get());
        // successful
    }

    public void logout() throws Exception {
        if(activeSession == null) {
            throw new Exception("User is not logged in");
        }

        activeSession = null;
    }
}
