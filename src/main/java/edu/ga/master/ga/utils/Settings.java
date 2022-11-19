/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.utils;

/**
 *
 * @author sommovir
 */
public class Settings {
    
    private static Settings _instance = null;
    private int agvQuantity = 2;
    private int batteryCapacity = 3;
    private int maxTime; //tempo massimo di elaborazione di un job
    
    public static Settings getInstance() {
        if (_instance == null) {
            _instance = new Settings();
        }
        return _instance;
    }
    
    private Settings() {
        super();
    }

    public int getAgvQuantity() {
        return agvQuantity;
    }

    public void setAgvQuantity(int agvQuantity) {
        this.agvQuantity = agvQuantity;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }
    
    
    
    
    
}