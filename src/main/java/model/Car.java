package model;

public class Car extends Vehicle{
    private double roadTax;
    private double mpg;
    private double engineSize;
    private String typeCar;

    // Default constructor
    public Car() {
        super(); // Calls the Vehicle default constructor
        this.roadTax = 0.0;
        this.mpg = 0.0;
        this.engineSize = 0.0;
        this.typeCar = "";
    }

    // Parameterized constructor
    public Car(String model, int year, double price, String transmission, int mileage, String fuelType,
               double roadTax, double mpg, double engineSize, String typeCar) {
        super(model, year, price, transmission, mileage, fuelType); // Calls the Vehicle parameterized constructor
        this.roadTax = roadTax;
        this.mpg = mpg;
        this.engineSize = engineSize;
        this.typeCar = typeCar;
    }

    // Getters and Setters
    public double getRoadTax() { return roadTax; }
    public void setRoadTax(double roadTax) { this.roadTax = roadTax; }

    public double getMpg() { return mpg; }
    public void setMpg(double mpg) { this.mpg = mpg; }

    public double getEngineSize() { return engineSize; }
    public void setEngineSize(double engineSize) { this.engineSize = engineSize; }

    public String getCarType() { return typeCar; }
    public void setCarType(String car_type) { this.typeCar = typeCar; }

    @Override
    public String toString() {
        return String.format("Car[%s, RoadTax=%.2f, MPG=%.2f, EngineSize=%.2f, Car_Type=%s]",
                super.toString(), roadTax, mpg, engineSize, typeCar);
    }

}