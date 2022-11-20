
package edu.ga.master.ga.model;

/**
 *
 * @author sommovir
 */
public final class ReloadJob implements Job{
    
    private int id;
    private int energy;

    public ReloadJob(int id, int energy) {
        this.id = id;
        this.energy = energy;
    }

    
    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return "R"+this.id + " - Time: "+this.energy+", Energy: +"+this.energy+" ";
    }
    
}