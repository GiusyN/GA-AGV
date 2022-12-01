/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model;

import edu.ga.master.ga.exceptions.BatteryException;
import edu.ga.master.ga.utils.Settings;
import edu.ga.master.ga.utils.Utils;

/**
 *
 * @author sommovir
 */
public class Population {


    private Individual[] individuals;
    private int minimumAGV;
    private int maximumAGV;
    public static DISTRIBUTION distribution;

    public enum DISTRIBUTION {
        EQUAL, RANDOM
    };

    //stampa tutti gli individui senza ricarica
    public void print(boolean withReloads){
        int size = Settings.getInstance().getPopulationSize();
        System.out.println(" ***************************************************************************************");
        System.out.println(" ***************                     POPULATION                          ***************" );
        System.out.println(" ***************************************************************************************");
        System.out.println("Dettagli popolazione: ");
        System.out.println("Dimensione: " + size);
        System.out.println("Numero minimo di AGV: " + minimumAGV);
        System.out.println("Numero massimo di AGV: " + maximumAGV);
        System.out.println("Distribuzione: " + distribution);
        System.out.println("Capacity batteria: " + Settings.getInstance().getBatteryCapacity());
        System.out.println("Penalità ricarica: " + Settings.getInstance().getReloadPenalty());
        System.out.println("Tempo massimo di durata di un job: " + Settings.getInstance().getMaxTime());
        System.out.println("Numero di job: " + Settings.getInstance().getNumberOfJobs());
        System.out.println("***************************************************************************************");
        for (int i = 0; i < size; i++) {
            System.out.print("Individual " + i + ": ");
            System.out.println(individuals[i].printJobs(withReloads) + " with "+individuals[i].getNumAGV()+" AGV");
        }
    }


    
    private Population(Builder builder){
        int size = Settings.getInstance().getPopulationSize();
        this.minimumAGV = builder.minimumAGV;
        this.maximumAGV = builder.maximumAGV;
        this.distribution = builder.distribution;
        individuals = new Individual[size];
        for (int i = 0; i < size; i++) {
            try {
                individuals[i] = new Individual(Utils.randomInRange(minimumAGV, maximumAGV));
            } catch (BatteryException e) {
                throw new RuntimeException(e);
            }
        }
//        if(distribution==DISTRIBUTION.EQUAL){
//            int howMany = (this.maximumAGV - this.minimumAGV) / size;
//
//        }else{
//            //TODO
//        }
    }

    public static class Builder {

        private int minimumAGV = 1;
        private int maximumAGV = 5;
        private DISTRIBUTION distribution = DISTRIBUTION.EQUAL;
        private Individual[] individuals;


        public Builder() {
            this.individuals = new Individual[Settings.getInstance().getPopulationSize()];
        }

        public Builder minimumAGV(int minimumAGV) {
            this.minimumAGV = minimumAGV;
            return this;
        }

        public Builder maximumAGV(int maximumAGV) {
            this.maximumAGV = maximumAGV;
            return this;
        }

        public Builder distribution(DISTRIBUTION distribution) {
            this.distribution = distribution;
            return this;
        }
        
        public Population build(){
            return new Population(this);
        }


    }
}
