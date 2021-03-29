package cryptogame.model.database;

import cryptogame.model.database.json.entities.User;

import java.sql.SQLException;
import java.util.Optional;

public interface DatabaseService {

    void connect() throws Exception;

}
