package service.MachineLearning;


import utils.DataCleaningUtils;
import org.apache.commons.math3.linear.*;


import java.util.*;
import java.util.logging.*;


public class DataPreprocessor {
    private static final String MISSING_VALUE_PLACEHOLDER = "N/A";
    private static final Logger LOGGER = Logger.getLogger(DataPreprocessor.class.getName());


    private Object[][] rawData;


    // Constructor that accepts the raw data
    public DataPreprocessor(Object[][] rawData) {
        if (rawData == null) {
            throw new IllegalArgumentException("Raw data cannot be null");
        }
        this.rawData = rawData;
    }


    // Fixed split percentages
    private static final double TRAIN_RATIO = 0.8;
    private static final double VALIDATION_RATIO = 0.1;
    private static final double TEST_RATIO = 0.1;


    // Instance variables for storing intermediate results
    private Object[][] cleanedData;
    private Object[][] encodedData;
    private Object[][] normalizedData;
    private Object[][] reducedData;


    // One-hot encoding maps for categorical variables
    private static final Map<String, Integer[]> carTypeMap = createOneHotMap(new String[]{"audi", "bmw", "ford", "hyundi", "merc", "skoda", "toyota"});
    private static final Map<String, Integer[]> modelMap = createOneHotMap(new String[]{ " E Class",  " C-HR",  " S-MAX",  " 2 Series",  " I20",  " M5",  " Rapid",  " X6",  " Grand Tourneo Connect",  " Fusion",  " A5",  " GLE Class",  " M3",  " G Class", "220",  " 7 Series",  " C-MAX", "180",  " SL CLASS",  " Accent",  " S3",  " Aygo",  " Puma",  " TT",  " Ka+",  " I10",  " X4",  " GLB Class",  " Avensis",  " SLK",  " Superb",  " CLS Class", "200",  " SQ7",  " CLA Class",  " I40",  " S Class",  " X-CLASS",  " A7",  " Transit Tourneo",  " B-MAX",  " M2",  " R Class",  " Fabia",  " Getz",  " X1",  " Yaris",  " 3 Series",  " 4 Series",  " IX20",  " Terracan",  " S8",  " 5 Series",  " Fiesta",  " i8",  " Z4",  " M4",  " Q8",  " C Class",  " Mustang",  " R8",  " i3",  " A3",  " Tourneo Custom",  " RS5",  " GL Class",  " KA",  " Corolla",  " Citigo",  " RS4",  " Kuga",  " Kamiq",  " X3",  " X7",  " RS7",  " Tucson",  " Mondeo",  " M Class",  " CLK",  " Ioniq",  " M Class",  " CLK",  " Ioniq",  " A4",  " IQ",  " GT86",  " Escort",  " Q3",  " Streetka",  " I800",  " Veloster",  " X5",  " Santa Fe",  " V Class",  " Focus",  " S4",  " Verso-S",  " EcoSport", "230",  " SQ5",  " A1",  " Ranger",  " Land Cruiser",  " Grand C-MAX",  " Urban Cruiser",  " GLA Class",  " GLC Class",  " A2",  " Tourneo Connect",  " Scala",  " Kona",  " Octavia",  " Yeti Outdoor",  " Z3",  " CLC Class",  " M6",  " A Class",  " Camry",  " A8",  " Galaxy",  " 1 Series",  " X2",  " Q7",  " Edge",  " Auris",  " Verso",  " Hilux",  " Yeti",  " 8 Series",  " RS3",  " GLS Class",  " RS6",  " 6 Series",  " Kodiaq",  " I30",  " Karoq",  " IX35",  " RAV4",  " PROACE VERSO",  " Q5",  " A6",  " S5",  " Amica",  " Roomster",  " Prius",  " Supra",  " CL Class",  " B Class",  " Q2"});
    private static final Map<String, Integer[]> fuelTypeMap = createOneHotMap(new String[]{"Petrol", "Diesel", "Electric", "Hybrid", "Other"});
    private static final Map<String, Integer[]> transmissionMap = createOneHotMap(new String[]{"Manual", "Semi-Auto", "Automatic", "Other"});

    // Helper method to generate one-hot encoding for a category
    private static Map<String, Integer[]> createOneHotMap(String[] categories) {
        Map<String, Integer[]> oneHotMap = new LinkedHashMap<>();
        int numCategories = categories.length;


        for (int i = 0; i < numCategories; i++) {
            Integer[] encoding = new Integer[numCategories];
            Arrays.fill(encoding, 0);
            encoding[i] = 1;
            oneHotMap.put(categories[i], encoding);
        }
        return oneHotMap;
    }


    public Object[][] preprocessData(Object[][] rawData, int nComponents) {
        // Step 1: Clean the data
        Object[][] cleanedData = DataCleaningUtils.cleanData(rawData, MISSING_VALUE_PLACEHOLDER);


        // Step 2: Encode categorical variables (model, fuelType, transmission, car_type)
        Object[][] encodedData = encodeCategoricalVariables(cleanedData);


        // Step 3: Normalize numerical data (price, mileage, engine size, etc.)
        Object[][] normalizedData = applyNormalization(encodedData);


        // Step 4: Apply PCA for dimensionality reduction
        Object[][] reducedData = applyPCA(normalizedData, nComponents);


        return reducedData;
    }


    public void preprocessAndDisplayData(Object[][] data, double varianceThreshold) {
        // Step 1: Clean the data
        this.cleanedData = DataCleaningUtils.cleanData(data, MISSING_VALUE_PLACEHOLDER);
        for (int i = 0; i < 10 && i < cleanedData.length; i++) {
            System.out.println(Arrays.toString(cleanedData[i]));
        }


        // Step 2: Encode categorical variables
        this.encodedData = encodeCategoricalVariables(this.cleanedData);
        for (int i = 0; i < 10 && i < encodedData.length; i++) {
            System.out.println(Arrays.toString(encodedData[i]));
        }


        // Step 3: Normalize numerical data
        this.normalizedData = applyNormalization(this.encodedData);
        for (int i = 0; i < 10 && i < normalizedData.length; i++) {
            System.out.println(Arrays.toString(normalizedData[i]));
        }


        // Step 4: Apply PCA
        double[][] numericData = convertToDoubleArray(this.normalizedData);
        int nComponents = determineNumberOfComponents(numericData, varianceThreshold);
        this.reducedData = applyPCA(this.normalizedData, nComponents);


        // Optionally display the reduced data
        for (int i = 0; i < 10 && i < reducedData.length; i++) {
            System.out.println(Arrays.toString(reducedData[i]));
        }


        displayData(this.reducedData);
    }


    public Object[][] getNormalizedData() {
        return this.normalizedData;
    }


    public Object[][] getReducedData() {
        return this.reducedData;
    }


    // Utility method to display data
    public void displayData(Object[][] data) {
        for (Object[] row : data) {
            System.out.println(Arrays.toString(row));
        }
    }


    // Method to encode categorical variables (car_type, model, transmission, fuelType)
    private Object[][] encodeCategoricalVariables(Object[][] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Input data cannot be null or empty.");
        }


        List<Object[]> encodedDataList = new ArrayList<>();


        for (Object[] row : data) {
            List<Object> encodedRow = new ArrayList<>();
            for (int j = 0; j < row.length; j++) {
                Object value = row[j];
                if (value instanceof String) {
                    String stringValue = (String) value;


                    if (carTypeMap.containsKey(stringValue)) {
                        encodedRow.addAll(Arrays.asList(carTypeMap.get(stringValue))); // One-hot encode "car_type"
                    } else if (modelMap.containsKey(stringValue)) {
                        encodedRow.addAll(Arrays.asList(modelMap.get(stringValue))); // One-hot encode "model"
                    } else if (fuelTypeMap.containsKey(stringValue)) {
                        encodedRow.addAll(Arrays.asList(fuelTypeMap.get(stringValue))); // One-hot encode "fuelType"
                    } else if (transmissionMap.containsKey(stringValue)) {
                        encodedRow.addAll(Arrays.asList(transmissionMap.get(stringValue)));
                    }
                } else {
                    encodedRow.add(value); // Preserve non-categorical values
                }
            }
            encodedDataList.add(encodedRow.toArray());
        }


        return encodedDataList.toArray(new Object[0][0]);
    }


    public static Object[][] applyNormalization(Object[][] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Input data cannot be null or empty.");
        }


        int numRows = data.length;
        int numCols = data[0].length;
        Object[][] normalizedData = new Object[numRows][numCols];


        for (int col = 0; col < numCols; col++) {
            if (isNumericColumn(data, col)) {
                double mean = calculateMean(data, col);
                double stdDev = calculateStdDev(data, col, mean);
                for (int row = 0; row < numRows; row++) {
                    Object value = data[row][col];
                    if (value instanceof Number) {
                        double numericValue = ((Number) value).doubleValue();
                        normalizedData[row][col] = (stdDev == 0) ? 0 : (numericValue - mean) / stdDev; // Avoid division by zero
                    } else {
                        normalizedData[row][col] = value; // Preserve non-numeric values
                    }
                }
            } else {
                for (int row = 0; row < numRows; row++) {
                    normalizedData[row][col] = data[row][col]; // Copy non-numeric columns as is
                }
            }
        }
        return normalizedData;
    }


    // Helper method to check if a column is numeric
    private static boolean isNumericColumn(Object[][] data, int col) {
        // Ensure that the column index is valid
        if (col >= data[0].length) {
            LOGGER.severe("Column index out of bounds: " + col);
            return false;
        }
        for (Object[] row : data) {
            if (row[col] instanceof Number) {
                return true;
            }
        }
        return false;
    }


    // Helper methods for mean and standard deviation
    private static double calculateMean(Object[][] data, int col) {
        double sum = 0;
        int count = 0;
        for (Object[] row : data) {
            if (row[col] instanceof Number) {
                sum += ((Number) row[col]).doubleValue();
                count++;
            }
        }
        return count == 0 ? 0 : sum / count;
    }


    private static double calculateStdDev(Object[][] data, int col, double mean) {
        double sumSquaredDiff = 0;
        int count = 0;
        for (Object[] row : data) {
            if (row[col] instanceof Number) {
                double value = ((Number) row[col]).doubleValue();
                sumSquaredDiff += Math.pow(value - mean, 2);
                count++;
            }
        }
        return count == 0 ? 1 : Math.sqrt(sumSquaredDiff / count); // Avoid division by zero
    }


    private int determineNumberOfComponents(double[][] data, double varianceThreshold) {
        // Convert data to RealMatrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);


        // Perform Singular Value Decomposition
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        double[] singularValues = svd.getSingularValues();


        // Calculate the total variance (sum of squared singular values)
        double totalVariance = 0;
        for (double value : singularValues) {
            totalVariance += Math.pow(value, 2);
        }


        // Calculate cumulative explained variance
        double cumulativeVariance = 0;
        int numComponents = 0;
        for (double value : singularValues) {
            cumulativeVariance += Math.pow(value, 2) / totalVariance;
            numComponents++;
            if (cumulativeVariance >= varianceThreshold) {
                break;
            }
        }


        LOGGER.info("Number of components: " + numComponents);
        return numComponents;
    }


    // Apply PCA to reduce dimensionality of data
    private Object[][] applyPCA(Object[][] data, int nComponents) {
        // Convert the data to a double[][] for PCA
        double[][] numericData = convertToDoubleArray(data);


        // Perform Singular Value Decomposition (SVD)
        RealMatrix matrix = MatrixUtils.createRealMatrix(numericData);
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        RealMatrix U = svd.getU();
        RealMatrix S = svd.getS();


        // Create reduced data matrix by selecting the first 'nComponents' principal components
        RealMatrix reducedDataMatrix = U.getSubMatrix(0, U.getRowDimension() - 1, 0, nComponents - 1);


        // Convert the reduced data matrix back to Object[][] for compatibility
        return convertToObjectArray(reducedDataMatrix);
    }


    // Convert data to double array for PCA
    private static double[][] convertToDoubleArray(Object[][] data) {
        int rows = data.length;
        int cols = data[0].length;
        double[][] result = new double[rows][cols];


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (data[i][j] instanceof Number) {
                    result[i][j] = ((Number) data[i][j]).doubleValue();
                } else {
                    result[i][j] = 0.0; // Handle non-numeric entries gracefully
                }
            }
        }
        return result;
    }


    // Convert a RealMatrix to Object[][] format
    private static Object[][] convertToObjectArray(RealMatrix matrix) {
        int rows = matrix.getRowDimension();
        int cols = matrix.getColumnDimension();
        Object[][] result = new Object[rows][cols];


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = matrix.getEntry(i, j);
            }
        }
        return result;
    }
}
