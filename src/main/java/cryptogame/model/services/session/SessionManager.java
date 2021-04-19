package cryptogame.model.services.session;

import cryptogame.common.validation.Validation;
import cryptogame.common.validation.ValidationError;
import cryptogame.dao.IDao;
import cryptogame.entities.User;
import cryptogame.entities.Session;
import cryptogame.service.auth.AuthService;
import cryptogame.service.exception.ValidationException;

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

        var user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        var validationResult = Validation.validateObject(user);

        if(validationResult.size() > 0) {
            throw new ValidationException(validationResult);
        }

        // TODO: Move this to controller
        if(this.userDao.getEntityBy("username",username).isPresent()) {
            validationResult.add(new ValidationError("username","Username is already in use"));
            throw new ValidationException(validationResult);
        }

        if(this.userDao.getEntityBy("email",email).isPresent()) {
            validationResult.add(new ValidationError("email","Email address is already in use"));
            throw new ValidationException(validationResult);
        }

        user.setPassword(AuthService.generatePasswordHash(password));

        this.userDao.persistEntity(user);
    }

    public void login(String username, String password) throws Exception {

        var tmp = this.userDao.getEntityBy("username",username);

        if(tmp.isEmpty()) {
            throw new Exception("Invalid username or password");
        }

        if(!AuthService.comparePasswords(tmp.get().getPassword(),password)) {
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
