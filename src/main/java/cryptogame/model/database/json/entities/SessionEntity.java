package cryptogame.model.database.json.entities;

import com.google.gson.annotations.SerializedName;

@lombok.Data
public final class SessionEntity {
    @SerializedName("user_id")
    private String userId;
    @SerializedName("start_time")
    private long startTime;
    @SerializedName("end_time")
    private long endTime;
}
