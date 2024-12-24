package model;

public class Vehicle {
    private String model;
    private int year;
    private double price;
    private String transmission;
    private int mileage;
    private String fuelType;

    // Default constructor
    public Vehicle() {
        this.model = "";
        this.year = 0;
        this.price = 0.0;
        this.transmission = "";
        this.mileage = 0;
        this.fuelType = "";
    }

    // Parameterized constructor
    public Vehicle(String model, int year, double price, String transmission, int mileage, String fuelType) {
        this.model = model;
        this.year = year;
        this.price = price;
        this.transmission = transmission;
        this.mileage = mileage;
        this.fuelType = fuelType;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return String.format("Vehicle[Model=%s, Year=%d, Price=%.2f, Transmission=%s, Mileage=%d, FuelType=%s]",
                model, year, price, transmission, mileage, fuelType);
    }

}

