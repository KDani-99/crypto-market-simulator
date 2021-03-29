package cryptogame.model.database.json.entities;

import com.google.gson.annotations.SerializedName;

@lombok.Data
public final class User {

    @SerializedName("id")
    private final String userId;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    @SerializedName("settings")
    private Settings settings;

    public User(String userId, String username, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.settings = new Settings();
    }


}
