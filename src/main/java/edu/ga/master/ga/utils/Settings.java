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
    private int agvQuantity = 5;
    private int batteryCapacity = 3;
    private int maxTime; //tempo massimo di elaborazione di un job
    private int reloadPenalty = 1;
    public static final float TETHA = 250f;//217.1f;
    public static final float K1 = 2f/3f; //costante per il tempo
    public static final float K2 = 1f/3f; //costante per il numero di AGV
    private int populationSize = 100;
    private int numberOfJobs = 6;

    private float crossoverProbability = 0.2f; //percentuale di crossover da applicare alla popolazione
    private float  mutationProbability = 0.2f; //20%
    private int elitism = 10; //numero di individui da mantenere nella popolazione
    private boolean verbose = true;

    
    public static Settings getInstance() {
        if (_instance == null) {
            _instance = new Settings();
        }
        return _instance;
    }
    
    private Settings() {
        super();
    }

    public void setReloadPenalty(int reloadPenalty) {
        this.reloadPenalty = reloadPenalty;
    }

    public int getReloadPenalty() {
        return reloadPenalty;
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

    public int getNumberOfJobs() {
        return numberOfJobs;
    }

    public void setNumberOfJobs(int jobCount) {
        this.numberOfJobs = jobCount;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setCrossoverProbability(float crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    public float getCrossoverProbability() {
        return crossoverProbability;
    }

    public void setMutationProbability(float mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    public float getMutationProbability() {
        return mutationProbability;
    }

    public void setElitism(int elitism) {
        this.elitism = elitism;
    }
    public int getElitism() {
        return elitism;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public boolean isVerbose() {
        return verbose;
    }
}
