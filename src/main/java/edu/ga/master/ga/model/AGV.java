/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model;

import edu.ga.master.ga.exceptions.BatteryException;
import edu.ga.master.ga.exceptions.OverCapacityBatteryLevelException;
import edu.ga.master.ga.exceptions.TooLowBatteryLevelException;
import edu.ga.master.ga.utils.Settings;

/**
 *
 * @author sommovir
 */
public class AGV {

    private int id;
    private int batteryLevel;

    public AGV() {
    }

    public AGV(int id, int batteryLevel) throws BatteryException {
        this.id = id;
        setBatteryLevel(batteryLevel);
    }
    
    public synchronized void work(int energyConsumed) throws BatteryException{
        int newBatteryLevel = this.batteryLevel - energyConsumed;
        this.setBatteryLevel(newBatteryLevel);
    }
    
    public synchronized void reload(int amountOfEnergy) throws BatteryException{
        int newBatteryLevel = this.batteryLevel + amountOfEnergy;
        this.setBatteryLevel(newBatteryLevel);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public synchronized int getBatteryLevel() {
        return batteryLevel;
    }

    public synchronized final void setBatteryLevel(int batteryLevel) throws TooLowBatteryLevelException, OverCapacityBatteryLevelException {
        if(batteryLevel<0){
            System.out.println("AGV WITH ERROR: "+this.id+" BATTERY LEVEL: "+batteryLevel);
            throw new TooLowBatteryLevelException(batteryLevel);
        }else if(batteryLevel > Settings.getInstance().getBatteryCapacity()){
            System.out.println("AGV WITH ERROR: "+this.id+" and current battery level: "+batteryLevel);
            throw new OverCapacityBatteryLevelException(this.id,batteryLevel);
        }
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


    public void fill() {
        System.out.println( " -------------------- MANUAL FILLING ---------------------");
        this.batteryLevel = Settings.getInstance().getBatteryCapacity();
    }
}
