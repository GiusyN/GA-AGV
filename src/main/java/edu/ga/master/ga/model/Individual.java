/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model;

import edu.ga.master.ga.exceptions.BatteryException;
import edu.ga.master.ga.exceptions.GAInconsistencyException;
import edu.ga.master.ga.utils.Settings;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author sommovir
 */
public class Individual implements Comparable<Individual> {

    private String testcode;
    private boolean elite;
    private float fitness = -1f;
    private LinkedList<AssignedJob> jobs = new LinkedList<>();
    private boolean dirty_fitness = true;
    private boolean dirty_makespan = true;
    private int makespan = -1;
    private int numAGV;
    private Map<Integer, AGV> agvByIDMap = new HashMap<>();
    private Map<Integer, Integer> agvStartTimeMap = new HashMap<>();
    //    1      0
    //    2      0
    //    3      0
    //    4      0


    public boolean isElite() {
        return elite;
    }

    public void setElite(boolean elite) {
        this.elite = elite;
    }

    public static Individual generate(List<AssignedJob> assignedJobs) throws BatteryException, GAInconsistencyException {
        Individual individual = new Individual();
        individual.numAGV = getNumAGV(assignedJobs);
        individual.jobs.addAll(assignedJobs);
        //init avg map
        for (AssignedJob job : assignedJobs) {
            individual.agvByIDMap.put(job.getAgv().getId(), job.getAgv());
        }
        //individual.initMaps();


        return individual;
    }

    public void initMaps() {
        for (int i = 1; i <= this.numAGV; i++) {
            agvStartTimeMap.put(i, 0);
            AGV agv = null;
            try {
                agv = new AGV(i, Settings.getInstance().getBatteryCapacity());
            } catch (BatteryException e) {
                throw new RuntimeException(e);
            }
            agvByIDMap.put(agv.getId(), agv);
        }
    }

    public void setTestcode(String testcode) {
        this.testcode = testcode;
    }

    public String getTestcode() {
        return testcode;
    }

    private Individual() {

    }

    //ritorna il numero di differenti agv in una lista di assigned jobs
    private static int getNumAGV(List<AssignedJob> assignedJobs) {
        Set<Integer> agvIDs = new HashSet<>();
        for (AssignedJob assignedJob : assignedJobs) {
            agvIDs.add(assignedJob.getAgv().getId());
        }
        return agvIDs.size();
    }


    public Individual(int numAGV) throws BatteryException, GAInconsistencyException {
        this.numAGV = numAGV;
        LinkedList<WorkJob> workJobs = JobManager.getInstance().getJobs();

        //shuffle work jobs
        Collections.shuffle(workJobs);


        //init startTime per AGV


        for (int i = 1; i <= this.numAGV; i++) {
            agvStartTimeMap.put(i, 0);
            AGV agv = new AGV(i, Settings.getInstance().getBatteryCapacity());
            agvByIDMap.put(agv.getId(), agv);
        }

        //order jobs by time
        workJobs.sort(Comparator.reverseOrder());

        if(Settings.getInstance().isVerbose()) {
            for (WorkJob workJob : workJobs) {
                System.out.println(workJob);
            }
        }

        //assegna ciclicamente tutti i job ai vari AGV
        int i = 0;
        for (WorkJob workJob : workJobs) {
            i++;
            if (i > this.numAGV) {
                i = 1;
            }
            AGV agv = agvByIDMap.get(i);
            AssignedJob assignedJob = new AssignedJob(
                    workJob,
                    agv,
                    agvStartTimeMap.get(i),
                    agvStartTimeMap.get(i) + workJob.getTime()
            );
            agvStartTimeMap.put(i, agvStartTimeMap.get(i) + workJob.getTime());
            jobs.add(assignedJob);
        }

        //stampa tutti i job assegnati
        if(Settings.getInstance().isVerbose()) {
            for (AssignedJob job : jobs) {
                System.out.println(job);
            }
        }

    }

    public LinkedList<AssignedJob> getAssignedJobs() {
        return jobs;
    }

    public int getNumAGV() {
        return numAGV;
    }

    public void calculateReloads() throws BatteryException, GAInconsistencyException {


        this.clearReloads();
        if (Settings.getInstance().isVerbose()) {
            System.out.println(" ------------------------ CALCULATE RELOADS ------------------------ ");
            System.out.println(" testcode: " + testcode);
            System.out.println("--------------------------------------------------------------------");
        }

        //throw exception if avg map is empty
        if (agvByIDMap.isEmpty()) {
            throw new GAInconsistencyException("AGV map is empty");
        }

        if (Settings.getInstance().isVerbose()) {
            System.out.println("                CURRENT PLAN IS: ");
            for (AssignedJob job : jobs) {
                System.out.println(job);
            }
            System.out.println("--------------------------------------------------------------------");
        }


        //fill all avg to max battery capacity
        for (AGV agv : agvByIDMap.values()) {
            agv.fill();
        }
        //fill all agv of jobs
        for (AssignedJob job : jobs) {
            job.getAgv().fill();
        }

        //reset starTime helper map
        for (int i = 1; i <= this.numAGV; i++) {
            agvStartTimeMap.put(i, 0);
        }

        ListIterator<AssignedJob> iterator = jobs.listIterator();
        int index = 0;
        List<Pair<AssignedJob, Integer>> reloads = new LinkedList<>();

        while (iterator.hasNext()) {
            AssignedJob assignedJob = iterator.next();
            AGV agv = assignedJob.getAgv();
            int energy = assignedJob.getJob().getEnergy();
            if (Settings.getInstance().isVerbose()) {
                System.out.println("the next job costs: " + energy + " and the agv n." + agv.getId() + " has: " + agv.getBatteryLevel());
            }

            if (agv.getBatteryLevel() < energy) {  //se NON ho energia sufficiente per il prossimo job
                //inserire il nodo di ricarica
                int energyMissingToFullCapacity = Settings.getInstance().getBatteryCapacity() - agv.getBatteryLevel();
                int minimumEnergyForNextStep = energy - agv.getBatteryLevel();
                if (Settings.getInstance().isVerbose()) {
                    System.out.println("Energy missing to full capacity: " + energyMissingToFullCapacity);
                    System.out.println("Minimum energy for next step: " + minimumEnergyForNextStep);
                }
                //lista contenente tutte le possibili ricariche che posso fare
                List<Integer> allReloadsPossibilities = new LinkedList<>();
                for (int i = minimumEnergyForNextStep; i <= energyMissingToFullCapacity; i++) {
                    allReloadsPossibilities.add(i);
                }
                if (Settings.getInstance().isVerbose()) {
                    System.out.println("All reloads possibilities: " + allReloadsPossibilities);
                }
                ReloadJob reloadJob = null;
                if (allReloadsPossibilities.size() == 1) {
                    //se ho solo una possibilità di ricarica, inserisco il job di ricarica
                    reloadJob = new ReloadJob(agv.getId(), allReloadsPossibilities.get(0));
                    if (Settings.getInstance().isVerbose()) {
                        System.out.println("only 1 possibility");
                        System.out.println(reloadJob);
                        System.out.println("-----------------");
                    }
                } else {
                    //ogni coppia è composta dalla quantità di ricarica e dal numero di task che riesce a fare con quella ricarica
                    List<Pair<Integer, Integer>> valutazioniRicariche = new LinkedList<>();
                    //         R1        1 task
                    //         R2        2 task
                    //         R3        2 task
                    // massimizzare in base al numero di task e minimizzare in base alla quantità di ricarica
                    for (Integer reloadPossibility : allReloadsPossibilities) {

                        int numTask = 1;
                        int energyLeft = agv.getBatteryLevel() + reloadPossibility;
                        int nextIndex = index + 1;
                        while (nextIndex < jobs.size() && jobs.get(nextIndex).getAgv().getId() == agv.getId() && energyLeft - jobs.get(nextIndex).getJob().getEnergy() >= jobs.get(nextIndex).getJob().getEnergy()) {
                            energyLeft -= jobs.get(nextIndex).getJob().getEnergy();
                            numTask++;
                            nextIndex++;
                        }
                        valutazioniRicariche.add(new ImmutablePair<>(reloadPossibility, numTask));
                    }
                    //stampa tutte le valutazioni ricariche:
                    if(Settings.getInstance().isVerbose()) {
                        for (Pair<Integer, Integer> valutazioneRicarica : valutazioniRicariche) {
                            System.out.println("Ricarica: " + valutazioneRicarica.getLeft() + " - Task: " + valutazioneRicarica.getRight());
                        }
                    }

                    //trova la coppia con il più alto numero di task e ricarica più bassa
                    Pair<Integer, Integer> bestReload = valutazioniRicariche.get(0);
                    for (Pair<Integer, Integer> valutazioneRicarica : valutazioniRicariche) {
                        if (valutazioneRicarica.getRight() > bestReload.getRight()) {
                            bestReload = valutazioneRicarica;
                        } else if (valutazioneRicarica.getRight() == bestReload.getRight()) {
                            if (valutazioneRicarica.getLeft() < bestReload.getLeft()) {
                                bestReload = valutazioneRicarica;
                            }
                        }
                    }
                    reloadJob = new ReloadJob(agv.getId(), bestReload.getLeft());
                    //stampa best reload
                    if(Settings.getInstance().isVerbose()) {
                        System.out.println("Best reload: " + bestReload.getLeft() + " - Task: " + bestReload.getRight());
                    }
                }

                reloads.add(new ImmutablePair<AssignedJob, Integer>(new AssignedJob(
                        reloadJob,
                        assignedJob.getAgv(),
                        -1,
                        -1),        //LEFT of PAIR
                        index)        //RIGHT of PAIR
                );
                agv.reload(reloadJob.getEnergy());
                agv.work(energy);
            } else {
                agv.work(energy);
            }
            index++;
        }

        if(Settings.getInstance().isVerbose()) {
            System.out.println("********* RELOADS *********");
            //print all reloads
            for (Pair<AssignedJob, Integer> reload : reloads) {
                System.out.println("Reload: " + reload.getLeft() + " - Index: " + reload.getRight());
            }
            System.out.println("***************************");
        }

        int shift = 0;
        for (Pair<AssignedJob, Integer> reload : reloads) {
            jobs.add(reload.getRight() + shift, reload.getLeft());
            shift++;
        }
        //reset Battery:
        for (AGV agv : agvByIDMap.values()) {
            agv.fill();
        }
        for(AssignedJob assignedJob : jobs) {
            assignedJob.getAgv().fill();
        }

        //print temporary solution with reloads
        if(Settings.getInstance().isVerbose()) {
            System.out.println("***************************************************");
            System.out.println("            TEMPORARY SOLUTION                 ");
            System.out.println("***************************************************");
            System.out.println("   BATTERY CAPACITY: " + Settings.getInstance().getBatteryCapacity());
            System.out.println("   AGV NUMBER: " + this.numAGV);
            System.out.println("   JOBS NUMBER: " + jobs.size());
            System.out.println("   AVG CONFIGURATION:");
            for (AGV agv : agvByIDMap.values()) {
                System.out.println("\t\tAGV " + agv.getId() + " - BATTERY: " + agv.getBatteryLevel() + (agv.getBatteryLevel() == Settings.getInstance().getBatteryCapacity() ? " (FULL)" : ""));
            }
            System.out.println("------------------------------------------------------");

            System.out.println("Temporary solution with reloads:");
            for (AssignedJob assignedJob : jobs) {
                System.out.println(assignedJob);
            }
            System.out.println("-------------------------------------------------");
        }

        if(Settings.getInstance().isVerbose()) {
            System.out.println(" SOLUTION WITH RELOADS AND CALCULATION OF BATTERY LEVELS");
            for (AssignedJob assignedJob : jobs) {
                AGV agv = agvByIDMap.get(assignedJob.getAgv().getId());
                int penalty = 0;
                if (assignedJob.getJob() instanceof ReloadJob) {
                    penalty = 1;
                    System.out.println(" -- agv n. "+agv.getId());
                    System.out.println( "-- reloading with " + ((ReloadJob) assignedJob.getJob()).getEnergy() + " energy, the avg battery level is now " + agv.getBatteryLevel());
                    agv.reload(assignedJob.getJob().getEnergy());
                } else {
                    System.out.println(" -- agv n. "+agv.getId());
                    System.out.println( "-- working with " + ((WorkJob) assignedJob.getJob()).getEnergy() + " energy, the avg battery level is now " + agv.getBatteryLevel());
                    agv.work(assignedJob.getJob().getEnergy());
                }
                int startTime = agvStartTimeMap.get(agv.getId());
                int endTime = agvStartTimeMap.get(agv.getId()) + assignedJob.getJob().getTime() + penalty;
                assignedJob.setStartTime(startTime);
                assignedJob.setEndTime(endTime);
                if (endTime > this.makespan) {
                    this.makespan = endTime;
                }
                agvStartTimeMap.put(agv.getId(), endTime);
                System.out.println(assignedJob);
            }
        }

        //stampa tutti i valori nella agv map
        if(Settings.getInstance().isVerbose()) {
            for (AGV agv : agvByIDMap.values()) {
                System.out.println("AGV " + agv.getId() + " - BATTERY: " + agv.getBatteryLevel() + (agv.getBatteryLevel() == Settings.getInstance().getBatteryCapacity() ? " (FULL)" : ""));
            }
        }
        System.out.println("--------------------- FINE CHECK MAPPE ----------------------------");

        for (AssignedJob assignedJob : jobs) {
//            AGV agv = assignedJob.getAgv();
//            System.out.println(agv);
//            int penalty = 0;
//            if (assignedJob.getJob() instanceof ReloadJob) {
//                penalty = 1;
//                System.out.println("ORA SUL AGV "+assignedJob.getAgv().getId()+" CI STA: " + assignedJob.getAgv().getBatteryLevel() + " e sto CARICANDO energia: "+assignedJob.getJob().getEnergy());
//                agv.reload(assignedJob.getJob().getEnergy());
//            } else {
//                System.out.println("ORA SUL AGV "+assignedJob.getAgv().getId()+" CI STA: " + assignedJob.getAgv().getBatteryLevel() + " e sto togliendo energia: "+assignedJob.getJob().getEnergy());
//                agv.work(assignedJob.getJob().getEnergy());
//            }
//            if(agvStartTimeMap==null) {
//                JOptionPane.showMessageDialog(null, "AGV START TIME MAP IS NULL");
//            }
//            if(agvStartTimeMap.isEmpty()) {
//                JOptionPane.showMessageDialog(null, "AGV START TIME MAP IS EMPTY");
//            }

//            int startTime = agvStartTimeMap.get(agv.getId());
//            int endTime = agvStartTimeMap.get(agv.getId()) + assignedJob.getJob().getTime() + penalty;
//            assignedJob.setStartTime(startTime);
//            assignedJob.setEndTime(endTime);
//            if (endTime > this.makespan) {
//                this.makespan = endTime;
//            }
//            agvStartTimeMap.put(agv.getId(), endTime);
            if(Settings.getInstance().isVerbose()) {
                System.out.println(assignedJob);
            }
        }
        dirty_fitness = true;

    }

    public void print() {
        System.out.println("----------------------------------");
        System.out.println("MAKESPAN: " + this.getMakespan());
        System.out.println(" FITNESS: " + this.getFitness());
        System.out.println("----------------------------------");
    }

    private void calculateMakespan() {
        this.makespan = 0;
        for (AssignedJob assignedJob : jobs) {
            if (assignedJob.getEndTime() > this.makespan) {
                this.makespan = assignedJob.getEndTime();
            }
        }
    }

    public int getMakespan() {
        if (dirty_makespan) {
            calculateMakespan();
        }
        return this.makespan;
    }

    public void calculateFitness() {
        this.fitness = Settings.K1 * (float) getMakespan() + Settings.K2 * Settings.TETHA * (float) this.numAGV;
        dirty_fitness = false;
    }

    public float getFitness() {
        if (dirty_fitness) {
            calculateFitness();
        }
        return this.fitness;
    }

    public Pair<String, String> printJobs(boolean withReload) {
        StringBuilder sb_jobs = new StringBuilder();
        StringBuilder sb_agv = new StringBuilder();
        for (AssignedJob job : jobs) {
            if (job.getJob() instanceof ReloadJob && withReload) {
                sb_jobs.append("E").append(((ReloadJob) job.getJob()).getEnergy()).append(" ");
            } else {
                sb_jobs.append("J").append(job.getJob().getId()).append(" ");
            }

        }
        String stinrg_jobs = sb_jobs.toString();
        for (AssignedJob job : jobs) {
            if (job.getJob() instanceof ReloadJob && withReload) {
                sb_agv.append("R").append(job.getJob().getId()).append(" ");
            } else {
                sb_agv.append("A").append(job.getAgv().getId()).append(" ");
            }

        }
        String string_agv = sb_agv.toString();
        return new ImmutablePair<String, String>(stinrg_jobs, string_agv);
    }

    public List<AssignedJob> getReloadJobs() {
        List<AssignedJob> reloadJobs = new LinkedList<>();
        for (AssignedJob job : jobs) {
            if (job.getJob() instanceof ReloadJob) {
                reloadJobs.add(job);
            }
        }
        return reloadJobs;
    }

    public List<AssignedJob> getWorkJobs() {
        List<AssignedJob> workJobs = new LinkedList<>();
        for (AssignedJob job : jobs) {
            if (job.getJob() instanceof WorkJob) {
                workJobs.add(job);
            }
        }
        return workJobs;
    }

    public void clearReloads() {
        List<AssignedJob> reloadJobs = getReloadJobs();
        //sprint ize of reloads
        if(Settings.getInstance().isVerbose()) {
            System.out.println("SIZE OF RELOAD JOBS: " + reloadJobs.size());
        }
        for (AssignedJob reloadJob : reloadJobs) {
            jobs.remove(reloadJob);
        }
        //refill all avg batteries
        for (AGV agv : agvByIDMap.values()) {
            agv.fill();
        }
    }

    @Override
    public int compareTo(@NotNull Individual o) {
        //compare by fitness
        if (this.getFitness() < o.getFitness()) {
            return -1;
        } else if (this.getFitness() > o.getFitness()) {
            return 1;
        } else {
            return 0;
        }
    }

    public Individual mutate() throws BatteryException, GAInconsistencyException {
        this.clearReloads();
        int randomJobIndex = ThreadLocalRandom.current().nextInt(0, jobs.size());
        AssignedJob randomJob = jobs.get(randomJobIndex);
        if (randomJob.getJob() instanceof WorkJob) {
            int randomAGVIndex = ThreadLocalRandom.current().nextInt(0, agvByIDMap.size());
            AGV randomAGV = agvByIDMap.get(randomAGVIndex);
            randomJob.setAgv(randomAGV);
        }
        this.calculateReloads();
        return this;
    }

    //clone individual
    public Individual clone() {
        Individual clone = null;
        try {
            //clone the list of assigned jobs
            List<AssignedJob> cloneJobs = new LinkedList<>();
            for (AssignedJob job : jobs) {
                cloneJobs.add(job.clone());
            }
            clone = Individual.generate(cloneJobs);
        } catch (BatteryException e) {
            throw new RuntimeException(e);
        } catch (GAInconsistencyException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }
}
