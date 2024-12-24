package interfaces;

import java.io.File;
import java.util.List;

public interface DataUploadInterface {
    void processFile(File file);
    void loadDataFromDatabase(String dbUrl, String dbUsername, String dbPassword, String query);
    List<List<String>> getData();
}

