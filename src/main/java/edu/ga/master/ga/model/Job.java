/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model;

/**
 *
 * @author giusy
 */
public class Job {
    
    private int id;
    private int time;
    private int energy;

    public Job(int id, int time, int energy) {
        this.id = id;
        this.time = time;
        this.energy = energy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    @Override
    public String toString() {
        return "J"+this.id + " - Time: "+this.time+", Energy: "+this.energy;
    }
    
}
