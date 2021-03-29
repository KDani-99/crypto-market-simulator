package cryptogame.model.database.json.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {

    @SerializedName("users")
    private final HashMap<String, User> users;

    @SerializedName("sessions")
    private final ArrayList<SessionEntity> sessions;

    public Database() {
        this.users = new HashMap<>();
        this.sessions = new ArrayList<>();
    }

    public User getUserById(String id) {
        if(!users.containsKey(id)) {
            return null;
        }
        return users.get(id);
    }

    public HashMap<String, User> getUsers() {
        return this.users;
    }

}
