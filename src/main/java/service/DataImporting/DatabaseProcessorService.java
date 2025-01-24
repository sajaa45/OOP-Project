package service.DataImporting;
import java.util.List;

public class DatabaseProcessorService {
    public static void loadDataFromDatabase(String dbUrl, String dbUsername, String dbPassword, String query, List<List<String>> data) {
        DataIngestionService.loadDataFromDatabase(dbUrl, dbUsername, dbPassword, query, data);
    }
}

