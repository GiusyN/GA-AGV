package edu.ga.master.ga.algo;

import edu.ga.master.ga.exceptions.BatteryException;
import edu.ga.master.ga.exceptions.GAInconsistencyException;
import edu.ga.master.ga.gui.events.EventManager;
import edu.ga.master.ga.model.*;
import edu.ga.master.ga.utils.Settings;
import edu.ga.master.ga.utils.Utils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * @author sommovir
 * @version 1.0
 * <img src="https://www.java-injection.it/wp-content/uploads/2022/06/jlogo_gra-1024x195.png" alt="Genetic Algorithm">
 */
public class GAEngine {

    private static GAEngine instance;
    private int numberOfCycles = 300;
    private int currentCycle = 0;

    public enum SELECTION {
        TOURNAMENT, RANDOM
    }

    ;

    private SELECTION selectionStrategy = SELECTION.RANDOM;

    private GAEngine() {

    }

    public static GAEngine getInstance() {
        if (instance == null) {
            instance = new GAEngine();
        }
        return instance;
    }

    public int getCurrentCycle() {
        return currentCycle;
    }

    public void setCurrentCycle(int currentCycle) {
        this.currentCycle = currentCycle;
    }

    public int getNumberOfCycles() {
        return numberOfCycles;
    }

    public void setNumberOfCycles(int numberOfCicles) {
        this.numberOfCycles = numberOfCicles;
    }

    public void run(Population population) throws BatteryException, GAInconsistencyException {
        //init population


        float best_fitness = population.getIndividuals()[0].getFitness();
        EventManager.getInstance().startsSimulation(best_fitness);
        JOptionPane.showMessageDialog(null, "Start simulation and best fitness is: " + best_fitness);
        EventManager.getInstance().newImprovement(population.getIndividuals()[0], best_fitness);

        //cicle until max cycle
        int cycle = 0;
        if (Settings.getInstance().isViewResults()) {
            JOptionPane.showMessageDialog(null, "CICLI: " + numberOfCycles);
        }
        while (cycle < numberOfCycles) {
            if (Settings.getInstance().isViewResults()) {
                System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
                System.out.println("                     C Y C L E  " + cycle + "                     ");
                System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            }

//            best_fitness = population.getIndividuals()[0].getFitness();
            Individual bestone = population.getIndividuals()[0];
            if (population.getIndividuals()[0].getFitness() < best_fitness) {
                best_fitness = population.getIndividuals()[0].getFitness();

                EventManager.getInstance().newImprovement(bestone, best_fitness);
                // JOptionPane.showMessageDialog(null, "NEW IMPROVEMENT OF FITNESS: " + population.getIndividuals()[0].getFitness(),"EUREKA", JOptionPane.WARNING_MESSAGE);
            }

            float worst_fitness = population.getIndividuals()[population.getIndividuals().length - 1].getFitness();
            float average_fitness = 0;
            for (Individual individual : population.getIndividuals()) {
                average_fitness += individual.getFitness();
            }
            average_fitness /= population.getIndividuals().length;
            if (Settings.getInstance().isViewResults()) {
                System.out.println("BEST FITNESS: " + best_fitness);
                System.out.println("WORST FITNESS: " + worst_fitness);
                System.out.println("AVERAGE FITNESS: " + average_fitness);
                System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            }

            EventManager.getInstance().newAVG(average_fitness);
            EventManager.getInstance().nextCycle(cycle);


            List<Pair<Individual, Individual>> crossoverCandidates = new ArrayList<>();
            int howManyCrossoverCandidates = (int) (Settings.getInstance().getPopulationSize() * Settings.getInstance().getCrossoverProbability());


            for (int i = 0; i < howManyCrossoverCandidates; i++) {
                Pair<Individual, Individual> parents = population.selectParents();
                crossoverCandidates.add(parents);
            }
            List<Individual> children = new ArrayList<>();
            for (Pair<Individual, Individual> parents : crossoverCandidates) {
                Pair<Individual, Individual> childrenPair = crossover(parents.getLeft(), parents.getRight());
                children.add(childrenPair.getLeft());
                children.add(childrenPair.getRight());
                if (childrenPair.getLeft().getFitness() < worst_fitness) {
//                    JOptionPane.showMessageDialog(null, "WORST FITNESS: " + worst_fitness + " CHILD FITNESS: " + childrenPair.getLeft().getFitness(),"NEW CHILD IN POPULATION", JOptionPane.INFORMATION_MESSAGE);
                }
                if (childrenPair.getLeft().getFitness() < best_fitness) {
//                    JOptionPane.showMessageDialog(null, "BEST FITNESS: " + best_fitness + " CHILD FITNESS: " + childrenPair.getRight().getFitness(), "NEW CHILD IN POPULATION", JOptionPane.WARNING_MESSAGE);

                }
                if (childrenPair.getRight().getFitness() < worst_fitness) {
//                    JOptionPane.showMessageDialog(null, "WORST FITNESS: " + worst_fitness + " CHILD FITNESS: " + childrenPair.getRight().getFitness(),"NEW CHILD IN POPULATION", JOptionPane.INFORMATION_MESSAGE);
                }
                if (childrenPair.getRight().getFitness() < best_fitness) {
//                    JOptionPane.showMessageDialog(null, "BEST FITNESS: " + best_fitness + " CHILD FITNESS: " + childrenPair.getRight().getFitness(), "NEW CHILD IN POPULATION", JOptionPane.WARNING_MESSAGE);

                }

            }

            //mutation
            List<Individual> mutatedChildren = new ArrayList<>();
            float mutationProbability = Settings.getInstance().getMutationProbability();
            //mutation over new children
            for (Individual child : children) {
                if (Math.random() < mutationProbability) {
                    mutatedChildren.add(mutation(child));
                }
            }
            //mutation over old population excluding elites
            int eliteIndex = Settings.getInstance().getElitism();
            for (int i = eliteIndex; i < Settings.getInstance().getPopulationSize(); i++) {
                if (Math.random() < mutationProbability) {
                    mutatedChildren.add(mutation(population.getIndividuals()[i]));
                }
            }

            //merge all children and mutated children and old population in a same list
            List<Individual> allIndividuals = new ArrayList<>();
            for (int i = 0; i < population.getIndividuals().length; i++) {
                allIndividuals.add(population.getIndividuals()[i]);
            }
            allIndividuals.addAll(children);
            allIndividuals.addAll(mutatedChildren);
            //sort all individuals by fitness
            allIndividuals.sort((o1, o2) -> {
                if (o1.getFitness() > o2.getFitness()) {
                    return 1;
                } else if (o1.getFitness() < o2.getFitness()) {
                    return -1;
                } else {
                    return 0;
                }
            });

            //remove the worst individuals
            int howManyToRemove = allIndividuals.size() - Settings.getInstance().getPopulationSize();
            for (int i = 0; i < howManyToRemove; i++) {
                allIndividuals.remove(allIndividuals.size() - 1);
            }


            //increment cycle
            cycle++;

            //extract best individual
            Individual bestIndividual = allIndividuals.get(0);
            if (Settings.getInstance().isViewResults()) {
                System.out.println("kalergi %: " + Settings.getInstance().getKalergi());
                System.out.println("Total population: " + allIndividuals.size());
            }
            //kalergi count calculation  (population : 100 = x : 0.4 )  ---
            int kalergiNumberOfIndividuals = (int) ((float) Settings.getInstance().getKalergi() * (float) allIndividuals.size());
            //generate kalergi individuals
            List<Individual> kalergiIndividuals = new ArrayList<>();
            for (int i = 0; i < kalergiNumberOfIndividuals; i++) {
                //generate random number of avg between min and max
                int numberOfAVG = Utils.randomInRange(Settings.getInstance().getMinimumAGV(), Settings.getInstance().getMaximumAGV());

                Individual kalergiIndividual = new Individual(numberOfAVG);
                kalergiIndividual.setKalergi(true);
                kalergiIndividual.calculateReloads();
                kalergiIndividuals.add(kalergiIndividual);
            }


            //set new population
            Individual[] newPopulation = new Individual[allIndividuals.size()];
            for (int i = 0; i < allIndividuals.size(); i++) {
                newPopulation[i] = allIndividuals.get(i);
            }

            //add kalergi individuals to population
            for (int i = 0; i < kalergiIndividuals.size(); i++) {
                newPopulation[newPopulation.length - i - 1] = kalergiIndividuals.get(i);
            }


            population.setIndividuals(newPopulation);

            if (Settings.getInstance().isViewResults()) {
                //print iteration
                System.out.println("Iteration: " + cycle + " Best fitness: " + bestIndividual.getFitness());
                //print best individual
                System.out.println("Best individual: " + bestIndividual);
                System.out.println("-------------------------------------------------------------------------------------");

                population.printWithManyJobs();
            }

            GAEngine.getInstance().setCurrentCycle(cycle);

            //calcola la media delle fitness di tutti gli individui
//            float avgFitness = 0;
//            for (int i = 0; i < population.getIndividuals().length; i++) {
//                avgFitness += population.getIndividuals()[i].getFitness();
//            }
//            avgFitness = avgFitness / population.getIndividuals().length;
//            System.out.println("AVG FITNESS: " + avgFitness);
//            EventManager.getInstance().newAVG(avgFitness);
//            EventManager.getInstance().nextCycle(cycle);
        }


    }

    private Individual mutation(Individual child) throws BatteryException, GAInconsistencyException {
        return child.mutate();
    }

    public int getMaxCycle() {
        return numberOfCycles;
    }

    public void setMaxCycle(int maxCycle) {
        this.numberOfCycles = maxCycle;
    }

    //check if a list of assigned job contains job id duplicates
    public boolean checkDuplicateJobId(@NotNull List<AssignedJob> assignedJobs) {
        List<Integer> jobIds = new ArrayList<>();
        for (AssignedJob assignedJob : assignedJobs) {
            if (jobIds.contains(assignedJob.getJob().getId())) {
                return true;
            }
            jobIds.add(assignedJob.getJob().getId());
        }
        return false;
    }


    //controlla che una lista di AssignedJob non contenga duplicati
    public void fixDuplicate(LinkedList<AssignedJob> child1, LinkedList<AssignedJob> child2, int start, int end) {
        do {
            for (int i = start; i < end; i++) {
                for (int k = 0; k < child1.size(); k++) {
                    if (k >= start && k < end) {
                        continue;
                    }
                    if (child1.get(i).getJob().getId() == child1.get(k).getJob().getId()) {
                        child1.get(k).setJob(child2.get(i).getJob());
                        break;
                    }
                }
            }
        } while (checkDuplicateJobId(child1));
        //check if there are duplicates


        if (Settings.getInstance().isVerbose()) {
            //CHILD 1
            System.out.println("---------------------- child1 ----------------------");
            for (AssignedJob job : child1) {
                System.out.println("JOB: " + job);

            }
            //CHILD 2
            System.out.println("---------------------- child2 ----------------------");
            for (AssignedJob job : child2) {
                System.out.println("JOB: " + job);
            }

            System.out.println("----------------------------------------------------");
        }

    }

    public Pair<Individual, Individual> crossover(@NotNull Individual dad, @NotNull Individual mum) {
        //TEST
        //print dad
        if (Settings.getInstance().isVerbose()) {

            System.out.println(" BEFORE CROSSOVER DELLA MINCHIA");
            System.out.println("----------------- DAD -------------");
            System.out.println("dad: ");
            Pair<String, String> printDadStringedJobs = dad.printJobs(true);
            String s_dad_jobAssignement = printDadStringedJobs.getLeft();
            String s_dad_agvAssignement = printDadStringedJobs.getRight();
            //format fitness in 2 decimali
            String dad_fitness = String.format("%.2f", dad.getFitness());
            System.out.printf("%12s | %90s | %90s | %8s | %10s |", "XY", s_dad_jobAssignement, s_dad_agvAssignement, dad.getNumAGV(), "" + dad_fitness);
            System.out.println("dad fitness: " + dad.getFitness());
            System.out.println("-----------------------------------");
            //print mum
            System.out.println("----------------- MUM -------------");
            System.out.println("mum: ");
            dad.printJobs(true);
            Pair<String, String> printMumStringedJobs = mum.printJobs(true);
            String s_mum_jobAssignement = printMumStringedJobs.getLeft();
            String s_mum_agvAssignement = printMumStringedJobs.getRight();
            //format fitness in 2 decimali
            String mum_fitness = String.format("%.2f", mum.getFitness());
            System.out.printf("%12s | %90s | %90s | %8s | %10s |", "XX", s_mum_jobAssignement, s_mum_agvAssignement, mum.getNumAGV(), "" + mum_fitness);
            System.out.println("mum fitness: " + mum.getFitness());
            System.out.println("-----------------------------------");
        }


        dad.clearReloads();
        mum.clearReloads();


        List<AssignedJob> dadWorkJobs = dad.getWorkJobs();
        List<AssignedJob> mumWorkJobs = mum.getWorkJobs();


        List<AGV> agvsDad = new ArrayList<>(dadWorkJobs.size() * 2);
        List<AGV> agvsMum = new ArrayList<>(mumWorkJobs.size() * 2);

        //fill agvs with all agvs
        for (int i = 0; i < dad.getAssignedJobs().size(); i++) {
            agvsDad.add(dad.getAssignedJobs().get(i).getAgv());
        }
        for (int i = 0; i < mumWorkJobs.size(); i++) {
            agvsMum.add(mum.getAssignedJobs().get(i).getAgv());
        }

        //---------------------------------CROSSOVER JOBS---------------------------------
//        List<AssignedJob> daddyAssignedJobs = dad.getAssignedJobs();
//        List<AssignedJob> mummyAssignedJobs = mumWorkJobs;

        int startCrossPoint = dadWorkJobs.size() / 3;
        int endCrossPoint = 2 * (dadWorkJobs.size() / 3);

        LinkedList<AssignedJob> child1AssignedJobs = new LinkedList<>();
        LinkedList<AssignedJob> child2AssignedJobs = new LinkedList<>();

        //calcola la sottostringa centrale
        for (int i = 0; i < dadWorkJobs.size(); i++) {
            if (i >= startCrossPoint && i < endCrossPoint) {
                child1AssignedJobs.add(mumWorkJobs.get(i));
                child2AssignedJobs.add(dadWorkJobs.get(i));
            } else {
                child1AssignedJobs.add(dadWorkJobs.get(i));
                child2AssignedJobs.add(mumWorkJobs.get(i));
            }
        }

        if (Settings.getInstance().isVerbose()) {
            //print child1
            System.out.println("---------------------- child1 AFTER SWAP ----------------------");
            for (AssignedJob job : child1AssignedJobs) {
                System.out.println("JOB: " + job);
            }
            //print child2
            System.out.println("---------------------- child2 AFTER SWAP ----------------------");
            for (AssignedJob job : child2AssignedJobs) {
                System.out.println("JOB: " + job);
            }
        }


        fixDuplicate(child1AssignedJobs, child2AssignedJobs, startCrossPoint, endCrossPoint);
        fixDuplicate(child2AssignedJobs, child1AssignedJobs, startCrossPoint, endCrossPoint);

        Individual kid1 = null;
        Individual kid2 = null;
        try {
            kid1 = Individual.generate(child1AssignedJobs);
            kid2 = Individual.generate(child2AssignedJobs);


            if (Settings.getInstance().isVerbose()) {
                //TEST 2
                System.out.println(" AFTER CROSSOVER DELLA MINCHIA");
                System.out.println("----------------- kid1 -------------");
                System.out.println("kid1: ");
                Pair<String, String> printDadStringedJobs2 = kid1.printJobs(true);
                String s_dad_jobAssignement2 = printDadStringedJobs2.getLeft();
                String s_dad_agvAssignement2 = printDadStringedJobs2.getRight();
                //format fitness in 2 decimali
                String dad_fitness2 = String.format("%.2f", kid1.getFitness());
                System.out.printf("%12s | %90s | %90s | %8s | %10s |", "XY", s_dad_jobAssignement2, s_dad_agvAssignement2, kid1.getNumAGV(), "" + dad_fitness2);
                System.out.println("-----------------------------------");
                //print mum
                System.out.println("----------------- kid2 -------------");
                System.out.println("kid2: ");
                dad.printJobs(true);
                Pair<String, String> printMumStringedJobs2 = kid2.printJobs(true);
                String s_mum_jobAssignement2 = printMumStringedJobs2.getLeft();
                String s_mum_agvAssignement2 = printMumStringedJobs2.getRight();
                //format fitness in 2 decimali
                String mum_fitness2 = String.format("%.2f", kid2.getFitness());
                System.out.printf("%12s | %90s | %90s | %8s | %10s |", "XX", s_mum_jobAssignement2, s_mum_agvAssignement2, kid2.getNumAGV(), "" + mum_fitness2);
                System.out.println("-----------------------------------");

            }


            //------------------------------------------------------------------------------------------
            //opero il crossover sugli agv
            int swapPoint = child1AssignedJobs.size() / 2;

            //swap the first half of assigned agv from the kid1 to the kid2

            for (int i = 0; i < swapPoint; i++) {
                kid1.getAssignedJobs().get(i).setAgv(agvsDad.get(i));
                kid2.getAssignedJobs().get(i).setAgv(agvsMum.get(i));
            }
            for (int i = swapPoint; i < child1AssignedJobs.size(); i++) {
                kid1.getAssignedJobs().get(i).setAgv(agvsMum.get(i));
                kid2.getAssignedJobs().get(i).setAgv(agvsDad.get(i));
            }


            try {
                kid1.setTestcode("KID1");
                kid2.setTestcode("KID2");

                kid1.calculateReloads();
                kid2.calculateReloads();
            } catch (BatteryException e) {
                e.printStackTrace();
                System.exit(0);
            } catch (GAInconsistencyException e) {
                e.printStackTrace();
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Settings.getInstance().isVerbose()) {
            System.out.println("************************** END CROSSOVER **************************");
        }
        return new ImmutablePair<>(kid1, kid2);
    }


    public void setSelectionStrategy(SELECTION selectionStrategy) {
        this.selectionStrategy = selectionStrategy;
    }

    public SELECTION getSelectionStrategy() {
        return selectionStrategy;
    }

    public static void main(String[] args) {
        System.out.println("8/3 = " + 8 / 3);
        System.out.println("2*(8/3) = " + 2 * (8 / 3));
    }


}
