/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model;

import edu.ga.master.ga.algo.GAEngine;
import edu.ga.master.ga.exceptions.BatteryException;
import edu.ga.master.ga.exceptions.GAInconsistencyException;
import edu.ga.master.ga.utils.Settings;
import edu.ga.master.ga.utils.Utils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sommovir
 */
public class Population {


    private Individual[] individuals;
    private int minimumAGV;
    private int maximumAGV;
    public static DISTRIBUTION distribution;


    public Pair<Individual, Individual> selectParents() throws GAInconsistencyException {

        //si suppone che l'array di individui sia già ordinato in base alla fitness in ordine decrecente


        Individual parent1 = null;
        Individual parent2 = null;
        switch (GAEngine.getInstance().getSelectionStrategy()) {
            case RANDOM:
                parent1 = individuals[Utils.randomInRange(0, individuals.length)].clone();
                parent2 = individuals[Utils.randomInRange(0, individuals.length)].clone();
                break;
            case TOURNAMENT:
                parent1 = individuals[Utils.randomInRange(0, individuals.length)];
                parent2 = individuals[Utils.randomInRange(0, individuals.length)];
                if (parent1.getFitness() < parent2.getFitness()) {
                    parent1 = parent2;
                }
                break;
            default:
                throw new GAInconsistencyException("Distribution is not set");
        }
        return Pair.of(parent1, parent2);
    }

    public Individual[] getIndividuals() {
        return individuals;
    }

    public void replace(List<Individual> children) {
        //si suppone che la lista di figli sia già ordinata in base alla fitness in ordine decrecente

        //se un figlio ha fitness minore di un individuo della popolazione, lo sostituisco
        for (Individual child : children) {
            for (int i = 0; i < individuals.length; i++) {
                if (child.getFitness() > individuals[i].getFitness()) {
                    individuals[i] = child;
                    break;
                }
            }
        }


    }

    public enum DISTRIBUTION {
        EQUAL, RANDOM
    };


    public void printWithManyJobs() {
        printSettings();
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%12s | %8s | %10s |", "INDIVIDUAL", "N. AGV", "FITNESS");
        System.out.println();
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        int size = Settings.getInstance().getPopulationSize();
        for (int i = 0; i < size; i++) {

            //format fitness in 2 decimali
            String fitness = String.format("%.2f", individuals[i].getFitness());
            System.out.printf("%12s | %8s | %10s |" , ""+i, individuals[i].getNumAGV(),""+fitness);
            System.out.println();
//            System.out.println(individuals[i].printJobs(withReloads) + " with "+individuals[i].getNumAGV()+" AGV");
        }
    }


    public void printSettings(){
        int size = Settings.getInstance().getPopulationSize();
        System.out.println(" ***************************************************************************************");
        System.out.println(" ***************                     POPULATION                          ***************" );
        System.out.println(" ***************************************************************************************");
        System.out.println("Numero di Jobs: "+Settings.getInstance().getNumberOfJobs());
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
    }

    //stampa tutti gli individui senza ricarica
    public void print(boolean withReloads){

        printSettings();
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%12s | %90s | %90s | %8s | %10s |", "INDIVIDUAL","JOB & RELOADS ASSIGNEMENTS", "AGV ASSIGNEMENT", "N. AGV", "FITNESS");
        System.out.println();
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        int size = Settings.getInstance().getPopulationSize();
        for (int i = 0; i < size; i++) {
//            System.out.print("Individual " + i + ": ");
            Pair<String, String> stringStringPair = individuals[i].printJobs(withReloads);
            String jobAssignement = stringStringPair.getLeft();
            String agvAssignement = stringStringPair.getRight();
            //format fitness in 2 decimali
            String fitness = String.format("%.2f", individuals[i].getFitness());
            System.out.printf("%12s | %90s | %90s | %8s | %10s |" , ""+i,jobAssignement, agvAssignement, individuals[i].getNumAGV(),""+fitness);
            System.out.println();
//            System.out.println(individuals[i].printJobs(withReloads) + " with "+individuals[i].getNumAGV()+" AGV");
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
                individuals[i].calculateReloads();
            } catch (GAInconsistencyException | BatteryException e) {
                throw new RuntimeException(e);
            }
        }
        //sort individuals by fitness
        sort();



    }

    private void sort() {
        //transform individuals array into a list
        List<Individual> list = new ArrayList<>();
        for (Individual individual : individuals) {
            list.add(individual);
        }
        //sort list
        list.sort((Individual o1, Individual o2) -> {
            if (o1.getFitness() > o2.getFitness()) {
                return 1;
            } else if (o1.getFitness() < o2.getFitness()) {
                return -1;
            } else {
                return 0;
            }
        });
        //update current individual array
        for (int i = 0; i < individuals.length; i++) {
            individuals[i] = list.get(i);
        }



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
            Settings.getInstance().setMinimumAGV(minimumAGV);
            return this;
        }

        public Builder maximumAGV(int maximumAGV) {
            this.maximumAGV = maximumAGV;
            Settings.getInstance().setMaximumAGV(maximumAGV);
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
