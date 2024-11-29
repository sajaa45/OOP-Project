package org.example.Model;

public class Car {
    private String make;
    private String model;
    private int year;
    private double price;
    private int mileage;
    private String fuelType;
    private String transmission;
    private double roadTax;
    private double mpg;
    private double engineSize;

    // constructor1
    public Car() {
        this.make = "";
        this.model = "";
        this.year = 0;
        this.price = 0.0;
        this.mileage = 0;
        this.fuelType = "";
        this.transmission = "";
        this.roadTax = 0.0;
        this.mpg = 0.0;
        this.engineSize = 0.0;
    }

    // Constructor2
    public Car(String make, String model, int year, double price, int mileage,
               String fuelType, String transmission, double roadTax, double mpg, double engineSize) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.mileage = mileage;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.roadTax = roadTax;
        this.mpg = mpg;
        this.engineSize = engineSize;
    }
    // getters and setters
    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getMileage() { return mileage; }
    public void setMileage(int mileage) { this.mileage = mileage; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }

    public double getRoadTax() { return roadTax; }
    public void setRoadTax(double roadTax) { this.roadTax = roadTax; }

    public double getMpg() { return mpg; }
    public void setMpg(double mpg) { this.mpg = mpg; }

    public double getEngineSize() { return engineSize; }
    public void setEngineSize(double engineSize) { this.engineSize = engineSize; }

    @Override
    public String toString() {
        return String.format("Car[Make=%s, Model=%s, Year=%d, Price=%.2f, Mileage=%d, FuelType=%s, Transmission=%s, RoadTax=%.2f, MPG=%.2f, EngineSize=%.2f]",
                make, model, year, price, mileage, fuelType, transmission, roadTax, mpg, engineSize);
    }
}
