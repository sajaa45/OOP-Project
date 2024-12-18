package model;

public class Car {
    private String model;
    private int year;
    private double price;
    private String transmission;
    private int mileage;
    private String fuelType;
    private double roadTax;
    private double mpg;
    private double engineSize;
    private String typeCar; // New field for type_car

    // Constructor1
    public Car() {
        this.model = "";
        this.year = 0;
        this.price = 0.0;
        this.transmission = "";
        this.mileage = 0;
        this.fuelType = "";
        this.roadTax = 0.0;
        this.mpg = 0.0;
        this.engineSize = 0.0;
        this.typeCar = ""; // Initialize type_car
    }

    // Constructor2
    public Car(String model, int year, double price, int mileage,
               String fuelType, String transmission, double roadTax, double mpg, double engineSize, String typeCar) {
        this.model = model;
        this.year = year;
        this.price = price;
        this.mileage = mileage;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.roadTax = roadTax;
        this.mpg = mpg;
        this.engineSize = engineSize;
        this.typeCar = typeCar; // Set type_car
    }

    // Getters and setters
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }

    public int getMileage() { return mileage; }
    public void setMileage(int mileage) { this.mileage = mileage; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public double getRoadTax() { return roadTax; }
    public void setRoadTax(double roadTax) { this.roadTax = roadTax; }

    public double getMpg() { return mpg; }
    public void setMpg(double mpg) { this.mpg = mpg; }

    public double getEngineSize() { return engineSize; }
    public void setEngineSize(double engineSize) { this.engineSize = engineSize; }

    public String getTypeCar() { return typeCar; } // Getter for type_car
    public void setTypeCar(String typeCar) { this.typeCar = typeCar; } // Setter for type_car

    @Override
    public String toString() {
        return String.format("Car[Model=%s, Year=%d, Price=%.2f, Transmission=%s, Mileage=%d, FuelType=%s, RoadTax=%.2f, MPG=%.2f, EngineSize=%.2f, TypeCar=%s]",
                model, year, price, transmission, mileage, fuelType, roadTax, mpg, engineSize, typeCar); // Include type_car
    }
}