package service.MachineLearning;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.math3.linear.RealMatrix;
import utils.DataCleaningUtils;
import org.apache.commons.math3.linear.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    //for debugging
    // Helper method to print the first n rows of a 2D array
    private void printFirstNRows(Object[][] data, int n, String stepName) {
        if (data == null || data.length == 0) {
            System.out.println(stepName + ": Data is null or empty.");
            return;
        }

        System.out.println("First " + n + " rows of " + stepName + ":");
        for (int i = 0; i < Math.min(n, data.length); i++) {
            System.out.println(Arrays.toString(data[i]));
        }
        System.out.println();
    }

    // helper method to extract target variable
    public Object[] extractTargetVariable(Object[][] data) {
        Object[] targetVariable = new Object[data.length];
        for (int i = 0; i < data.length; i++) {
            targetVariable[i] = data[i][4]; // Extract the target variable (price) at index 4
        }
        return targetVariable;
    }


    public Object[][] preprocessData(Object[][] rawData, int nComponents) {
        // Step 1: Clean the data
        Object[][] cleanedData = DataCleaningUtils.cleanData(rawData, MISSING_VALUE_PLACEHOLDER);


        // Step 2: Encode categorical variables (model, fuelType, transmission, car_type)
        int carTypeIndex = 6; // Column index for "car_type"
        int modelIndex = 7;   // Column index for "model"
        int fuelTypeIndex = 8; // Column index for "fuelType"
        int transmissionIndex = 5; // Column index for "transmission"
        Object[][] encodedData = encodeCategoricalVariables(cleanedData, carTypeIndex, modelIndex, fuelTypeIndex, transmissionIndex);


        // Step 3: Normalize numerical data (price, mileage, engine size, etc.)
        Object[][] normalizedData = applyNormalization(encodedData);


        // Step 4: Apply PCA for dimensionality reduction
        Object[][] reducedData = applyPCA(normalizedData, nComponents);


        return reducedData;
    }


    public void preprocessAndDisplayData(Object[][] data, double varianceThreshold) {
        // Step 1: Clean the data
        this.cleanedData = DataCleaningUtils.cleanData(data, MISSING_VALUE_PLACEHOLDER);
        printFirstNRows(this.cleanedData, 3, "cleanedData");

        // Step 2: Encode categorical variables
        int carTypeIndex = 6; // Column index for "car_type"
        int modelIndex = 7;   // Column index for "model"
        int fuelTypeIndex = 8; // Column index for "fuelType"
        int transmissionIndex = 5; // Column index for "transmission"
        this.encodedData = encodeCategoricalVariables(this.cleanedData, carTypeIndex, modelIndex, fuelTypeIndex, transmissionIndex);
        printFirstNRows(this.encodedData, 3, "encodedData");

        // Step 3: Normalize numerical data
        this.normalizedData = applyNormalization(this.encodedData);
        printFirstNRows(this.normalizedData, 3, "normalizedData");

        // Step 4: Extract the target variable (price)
        Object[] targetVariable = extractTargetVariable(this.normalizedData);

        // Step 5: Apply PCA
        double[][] numericData = convertToDoubleArray(this.normalizedData);
        int nComponents = determineNumberOfComponents(numericData, varianceThreshold);
        this.reducedData = applyPCA(this.normalizedData, nComponents);
        printFirstNRows(this.reducedData, 3, "reducedData");

        // Step 6: Split the reduced data and target variable into training, validation, and test sets
        Map<String, Object[][]> splits = splitData(this.reducedData, targetVariable);
    }


    public Object[][] getNormalizedData() {
        return this.normalizedData;
    }


    public Object[][] getReducedData() {
        return this.reducedData;
    }

    // Method to split the reduced data into training, validation, and test sets
    public Map<String, Object[][]> splitData(Object[][] reducedData, Object[] targetVariable) {
        if (reducedData == null || reducedData.length == 0 || targetVariable == null || targetVariable.length == 0) {
            throw new IllegalArgumentException("Input data cannot be null or empty.");
        }

        int totalRows = reducedData.length;
        int trainSize = (int) (totalRows * TRAIN_RATIO);
        int validationSize = (int) (totalRows * VALIDATION_RATIO);
        int testSize = totalRows - trainSize - validationSize;

        // Shuffle the data for randomization
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < totalRows; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices, new Random());

        // Split the reduced data (features)
        Object[][] trainFeatures = new Object[trainSize][];
        Object[][] validationFeatures = new Object[validationSize][];
        Object[][] testFeatures = new Object[testSize][];

        // Split the target variable
        Object[] trainTarget = new Object[trainSize];
        Object[] validationTarget = new Object[validationSize];
        Object[] testTarget = new Object[testSize];

        for (int i = 0; i < trainSize; i++) {
            trainFeatures[i] = reducedData[indices.get(i)];
            trainTarget[i] = targetVariable[indices.get(i)];
        }
        for (int i = 0; i < validationSize; i++) {
            validationFeatures[i] = reducedData[indices.get(trainSize + i)];
            validationTarget[i] = targetVariable[indices.get(trainSize + i)];
        }
        for (int i = 0; i < testSize; i++) {
            testFeatures[i] = reducedData[indices.get(trainSize + validationSize + i)];
            testTarget[i] = targetVariable[indices.get(trainSize + validationSize + i)];
        }

        // Store the splits in a map for easy access
        Map<String, Object[][]> splitDataMap = new HashMap<>();
        splitDataMap.put("train_features", trainFeatures);
        splitDataMap.put("train_target", convertTo2DArray(trainTarget));  // Proper conversion to 2D array
        splitDataMap.put("validation_features", validationFeatures);
        splitDataMap.put("validation_target", convertTo2DArray(validationTarget));  // Proper conversion to 2D array
        splitDataMap.put("test_features", testFeatures);
        splitDataMap.put("test_target", convertTo2DArray(testTarget));  // Proper conversion to 2D array

        return splitDataMap;
    }

    // Helper method to convert 1D array to 2D array
    private Object[][] convertTo2DArray(Object[] array) {
        Object[][] result = new Object[array.length][1];
        for (int i = 0; i < array.length; i++) {
            result[i][0] = array[i];
        }
        return result;
    }


    // Method to encode categorical variables (car_type, model, transmission, fuelType)
    private Object[][] encodeCategoricalVariables(Object[][] data, int carTypeIndex, int modelIndex, int fuelTypeIndex, int transmissionIndex) {
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

                    // Determine which map to use based on the column index
                    if (j == carTypeIndex) {
                        encodedRow.addAll(encodeValue(stringValue, carTypeMap));
                    } else if (j == modelIndex) {
                        encodedRow.addAll(encodeValue(stringValue, modelMap));
                    } else if (j == fuelTypeIndex) {
                        encodedRow.addAll(encodeValue(stringValue, fuelTypeMap));
                    } else if (j == transmissionIndex) {
                        encodedRow.addAll(encodeValue(stringValue, transmissionMap));
                    } else {
                        // Preserve non-categorical strings (e.g., other string columns)
                        encodedRow.add(value);
                    }
                } else {
                    encodedRow.add(value); // Preserve non-categorical values
                }
            }
            encodedDataList.add(encodedRow.toArray());
        }


        return encodedDataList.toArray(new Object[0][0]);
    }
    // Helper method to encode a categorical value using the appropriate map
    private List<Object> encodeValue(String value, Map<String, Integer[]> map) {
        if (map.containsKey(value)) {
            return Arrays.asList(map.get(value)); // Use existing encoding
        } else {
            // Handle unknown categorical values by mapping them to a default encoding (all zeros)
            Integer[] defaultEncoding = new Integer[map.size()];
            Arrays.fill(defaultEncoding, 0);
            return Arrays.asList(defaultEncoding);
        }
    }

    // Method to use stored means and stdDevs
    public static Object[][] applyNormalization(Object[][] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Input data cannot be null or empty.");
        }


        int numRows = data.length;
        int numCols = data[0].length;
        Object[][] normalizedData = new Object[numRows][numCols];

        // Indices of numerical columns excluding price (mileage=0, year=1, engineSize=2, mpg=3)
        int[] numericalColumns = {0, 1, 2, 3};
        // Normalize only numerical columns
        for (int col : numericalColumns) {
            double mean = calculateMean(data, col);
            double stdDev = calculateStdDev(data, col, mean);

            LOGGER.info("Normalizing column " + col + " with mean=" + mean + " and stdDev=" + stdDev);
            for (int row = 0; row < numRows; row++) {
                Object value = data[row][col];
                double numericValue;

                // Handle cases where the value is a string or a number
                if (value instanceof Number) {
                    numericValue = ((Number) value).doubleValue();
                } else if (value instanceof String) {
                    try {
                        numericValue = Double.parseDouble((String) value);
                    } catch (NumberFormatException e) {
                        LOGGER.warning("Non-numeric value found in column " + col + ": " + value);
                        numericValue = 0; // Default to 0 if parsing fails
                    }
                } else {
                    LOGGER.warning("Non-numeric value found in column " + col + ": " + value);
                    numericValue = 0; // Default to 0 for other non-numeric types
                }

                normalizedData[row][col] = (stdDev == 0) ? 0 : (numericValue - mean) / stdDev; // Avoid division by zero
            }
        }

        // Copy non-numerical columns (categorical and target) as is
        for (int col = 0; col < numCols; col++) {
            if (!contains(numericalColumns, col)) { // Skip numerical columns
                for (int row = 0; row < numRows; row++) {
                    normalizedData[row][col] = data[row][col]; // Copy as is
                }
            }
        }
        return normalizedData;
    }
    // Helper method to check if an array contains a value
    private static boolean contains(int[] array, int value) {
        for (int i : array) {
            if (i == value) {
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
            Object value = row[col];
            double numericValue;

            if (value instanceof Number) {
                numericValue = ((Number) value).doubleValue();
            } else if (value instanceof String) {
                try {
                    numericValue = Double.parseDouble((String) value);
                } catch (NumberFormatException e) {
                    LOGGER.warning("Non-numeric value found in column " + col + ": " + value);
                    continue; // Skip this value
                }
            } else {
                LOGGER.warning("Non-numeric value found in column " + col + ": " + value);
                continue; // Skip this value
            }

            sum += numericValue;
            count++;
        }
        return count == 0 ? 0 : sum / count;
    }

    private static double calculateStdDev(Object[][] data, int col, double mean) {
        double sumSquaredDiff = 0;
        int count = 0;
        for (Object[] row : data) {
            Object value = row[col];
            double numericValue;
            if (value instanceof Number) {
                numericValue = ((Number) value).doubleValue();
            } else if (value instanceof String) {
                try {
                    numericValue = Double.parseDouble((String) value);
                } catch (NumberFormatException e) {
                    LOGGER.warning("Non-numeric value found in column " + col + ": " + value);
                    continue; // Skip this value
                }
            } else {
                LOGGER.warning("Non-numeric value found in column " + col + ": " + value);
                continue; // Skip this value
            }

            sumSquaredDiff += Math.pow(numericValue - mean, 2);
            count++;
        }
        return count == 0 ? 0 : Math.sqrt(sumSquaredDiff / count); // Avoid division by zero
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
    public Object[][] applyPCA(Object[][] data, int nComponents) {
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
            for (int j = 0, k = 0; j < data[i].length; j++) {
                if (j == 4) {
                    continue; // Skip the target variable column (index 4)
                }
                if (data[i][j] instanceof Number) {
                    result[i][k] = ((Number) data[i][j]).doubleValue();
                } else {
                    result[i][k] = 0.0; // Handle non-numeric entries gracefully
                }
                k++;
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
