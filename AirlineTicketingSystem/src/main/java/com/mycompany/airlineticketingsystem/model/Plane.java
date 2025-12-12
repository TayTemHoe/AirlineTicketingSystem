package com.mycompany.airlineticketingsystem.model;

/**
 * Entity class for Plane.
 * Represents a physical aircraft in the inventory.
 */
public class Plane {

    private String planeId;  // Maps to DB column: plane_id
    private String model;    // Maps to DB column: model
    private int capacity;    // Maps to DB column: capacity

    // Default Constructor (Useful for frameworks)
    public Plane() {
    }

    // Parameterized Constructor
    public Plane(String planeId, String model, int capacity) {
        this.planeId = planeId;
        this.model = model;
        this.capacity = capacity;
    }

    // --- Getters and Setters ---

    public String getPlaneId() {
        return planeId;
    }

    public void setPlaneId(String planeId) {
        this.planeId = planeId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    // --- toString for Debugging/Logging ---
    
    @Override
    public String toString() {
        return "Plane{" + "ID='" + planeId + '\'' + ", Model='" + model + '\'' + ", Capacity=" + capacity + '}';
    }
}
