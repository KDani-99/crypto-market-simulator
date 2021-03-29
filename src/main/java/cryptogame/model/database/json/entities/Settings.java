package cryptogame.model.database.json.entities;

import com.google.gson.annotations.SerializedName;

@lombok.Data
public class Settings {
    @SerializedName("is2FAEnabled")
    private boolean is2FAEnabled;
}
