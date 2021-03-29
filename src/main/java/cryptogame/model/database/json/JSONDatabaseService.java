package cryptogame.model.database.json;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import cryptogame.model.database.DatabaseService;
import cryptogame.model.database.json.entities.Database;
import cryptogame.model.database.json.entities.User;

import javax.json.JsonReader;
import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public final class JSONDatabaseService implements DatabaseService {

    private final Path dbPath;
    private Database database;

    public JSONDatabaseService() {
        this.dbPath = Path.of(System.getProperty("user.dir") + "/db.json");//getClass().getResource("/db.json");
        this.database = null;
    }

    private String getDatabaseString() {
        return new Gson().toJson(this.database);
    }

    private void createDatabase() throws IOException {
        this.database = new Database();
        Files.writeString(this.dbPath,this.getDatabaseString());
    }

    private void loadDatabase() throws IOException {

        if(!new File(this.dbPath.toUri()).exists()) {
            this.createDatabase();
            return;
        }

        this.database = new Gson().fromJson(
                Files.readString(this.dbPath),
                Database.class
        );
    }

    private void save() {

        try(var asyncSave = AsynchronousFileChannel.open(this.dbPath, StandardOpenOption.WRITE)) {

            var buffer = ByteBuffer.wrap(this.getDatabaseString().getBytes());

            asyncSave.write(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    // log
                    attachment.clear();
                }
                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    // log error
                }
            });

        } catch (Exception ex) {
            // log error
        }

        new Thread(()->{

        }).start();
    }

    @Override
    public void connect() throws Exception {
        this.loadDatabase();
    }
}
