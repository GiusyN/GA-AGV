/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model;

/**
 *
 * @author sommovir
 */
public class AGV {

    private int id;
    private int batteryLevel;

    public AGV() {
    }

    public AGV(int id, int batteryLevel) {
        this.id = id;
        this.batteryLevel = batteryLevel;
    }
    
    public void work(int energyConsumed){
        this.batteryLevel-=energyConsumed;
    }
    
    public void reload(int amountOfEnergy){
        this.batteryLevel+=amountOfEnergy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AGV other = (AGV) obj;
        if (this.id != other.id) {
            return false;
        }
        return this.batteryLevel == other.batteryLevel;
    }

    @Override
    public String toString() {
        return id + ",\t batteryLevel=" + batteryLevel + " ";
    }

    
    
    
}
