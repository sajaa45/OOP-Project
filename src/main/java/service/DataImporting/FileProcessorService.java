package service.DataImporting;

import java.io.File;
import java.util.List;

public class FileProcessorService {
    public static List<List<String>> processFile(File file) throws Exception {
        if (file.getName().endsWith(".csv")) {
            return service.DataImporting.DataIngestionService.loadDataFromCSV(file.getAbsolutePath());
        } else if (file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx")) {
            return DataIngestionService.loadDataFromExcel(file.getAbsolutePath());
        } else {
            throw new IllegalArgumentException("Unsupported file format");
        }
    }
}


