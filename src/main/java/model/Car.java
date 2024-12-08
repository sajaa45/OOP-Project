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
    private String car_type;

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
        this.car_type = "";
    }

    // Constructor2
    public Car(String model, int year, double price, int mileage,
               String fuelType, String transmission, double roadTax, double mpg, double engineSize, String car_type) {
        this.model = model;
        this.year = year;
        this.price = price;
        this.mileage = mileage;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.roadTax = roadTax;
        this.mpg = mpg;
        this.engineSize = engineSize;
        this.car_type = car_type;
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

    public String getCar_type() { return car_type; }
    public void setCar_type(String car_type) { this.car_type = car_type; }


    @Override
    public String toString() {
        return String.format("Car[Model=%s, Year=%d, Price=%.2f, Transmission=%s, Mileage=%d, FuelType=%s, RoadTax=%.2f, MPG=%.2f, EngineSize=%.2f, Car_Type=%s]",
                model, year, price, transmission, mileage, fuelType, roadTax, mpg, engineSize, car_type);
    }
}