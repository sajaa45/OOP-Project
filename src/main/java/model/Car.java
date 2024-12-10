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
    // constructor1
    public Car() {
        this.model = "";
        this.year = 0;
        this.price = 0.0;
        this.transmission = ""; // Updated position
        this.mileage = 0;       // Updated position
        this.fuelType = "";
        this.roadTax = 0.0;
        this.mpg = 0.0;
        this.engineSize = 0.0;
    }
    // Constructor2
    public Car(String model, int year, double price, String transmission, int mileage,
               String fuelType,  double roadTax, double mpg, double engineSize) {
        this.model = model;
        this.year = year;
        this.price = price;
        this.transmission = transmission;
        this.mileage = mileage;
        this.fuelType = fuelType;
        this.roadTax = roadTax;
        this.mpg = mpg;
        this.engineSize = engineSize;
    }

    // getters and setters
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

    @Override
    public String toString() {
        return String.format("Car[Model=%s, Year=%d, Price=%.2f, Transmission=%s, Mileage=%d, FuelType=%s, RoadTax=%.2f, MPG=%.2f, EngineSize=%.2f]",
                model, year, price, transmission, mileage, fuelType, roadTax, mpg, engineSize);
    }
}