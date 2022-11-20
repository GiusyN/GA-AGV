
package edu.ga.master.ga.model;

/**
 *
 * @author sommovir
 */
public final class ReloadJob implements Job{
    
    private int agvID;
    private int energy;

    public ReloadJob(int agvID, int energy) {
        this.agvID = agvID;
        this.energy = energy;
    }

    
    @Override
    public int getId() {
        return agvID;
    }

    public void setId(int id) {
        this.agvID = id;
    }

    @Override
    public int getTime() {
        return this.energy;
    }


    @Override
    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    @Override
    public String toString() {
        return "R"+this.agvID + " - Time: "+this.energy+", Energy: +"+this.energy+" ";
    }
    
}