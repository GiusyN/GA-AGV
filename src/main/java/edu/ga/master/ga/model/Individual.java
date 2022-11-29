/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model;

import edu.ga.master.ga.exceptions.BatteryException;
import edu.ga.master.ga.utils.Settings;
import edu.ga.master.ga.utils.Utils;

import java.util.*;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author sommovir
 */
public class Individual {
    
    private float fitness = -1f;
    private LinkedList<AssignedJob> jobs = new LinkedList<>();
    private boolean dirty = false;
    private int makespan = -1;
    private int numAGV;
    private Map<Integer, AGV> agvByIDMap = new HashMap<>();
    private Map<Integer, Integer> agvStartTimeMap = new HashMap<>();
    //    1      0
    //    2      0
    //    3      0
    //    4      0




    public static Individual generate(List<AssignedJob> assignedJobs){
        Individual individual = new Individual();
        individual.numAGV = getNumAGV(assignedJobs);
        individual.jobs.addAll(assignedJobs);
        individual.dirty = false;
        return individual;
    }

    private Individual(){

    }

    //ritorna il numero di differenti agv in una lista di assigned jobs
    private static int getNumAGV(List<AssignedJob> assignedJobs){
        Set<Integer> agvIDs = new HashSet<>();
        for(AssignedJob assignedJob : assignedJobs){
            agvIDs.add(assignedJob.getAgv().getId());
        }
        return agvIDs.size();
    }


    public Individual(int numAGV) throws BatteryException {
        this.numAGV = numAGV;
        List<WorkJob> workJobs = JobManager.getInstance().getJobs();

        //init startTime per AGV


        for (int i = 1; i <= this.numAGV; i++) {
            agvStartTimeMap.put(i, 0);
            AGV agv = new AGV(i, Settings.getInstance().getBatteryCapacity());
            agvByIDMap.put(agv.getId(), agv);
        }

        //order jobs by time
        workJobs.sort(Comparator.reverseOrder());

        for (WorkJob workJob : workJobs) {
            System.out.println(workJob);
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
        for (AssignedJob job : jobs) {
            System.out.println(job);
        }

    }

    public LinkedList<AssignedJob> getAssignedJobs() {
        return jobs;
    }

    public int getNumAGV() {
        return numAGV;
    }

    public void calculateReloads() throws BatteryException {

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

            if (agv.getBatteryLevel() < energy) {  //se NON ho energia sufficiente per il prossimo job
                //inserire il nodo di ricarica
                int energyMissingToFullCapacity = Settings.getInstance().getBatteryCapacity() - agv.getBatteryLevel();
                int minimumEnergyForNextStep = energy - agv.getBatteryLevel();

                //lista contenente tutte le possibili ricariche che posso fare
                List<Integer> allReloadsPossibilities = new LinkedList<>();
                for (int i = minimumEnergyForNextStep; i <= energyMissingToFullCapacity; i++) {
                    allReloadsPossibilities.add(i);
                }
                ReloadJob reloadJob = null;
                if(allReloadsPossibilities.size() == 1){
                    //se ho solo una possibilità di ricarica, inserisco il job di ricarica
                    reloadJob = new ReloadJob(agv.getId(), allReloadsPossibilities.get(0));
                }else{
                    //ogni coppia è composta dalla quantità di ricarica e dal numero di task che riesce a fare con quella ricarica
                    List<Pair<Integer,Integer>> valutazioniRicariche = new LinkedList<>();
                    //         R1        1 task
                    //         R2        2 task
                    //         R3        2 task
                    // massimizzare in base al numero di task e minimizzare in base alla quantità di ricarica
                    for (Integer reloadPossibility : allReloadsPossibilities) {
                        int numTask = 1;
                        int energyLeft = agv.getBatteryLevel() + reloadPossibility;
                        int nextIndex = index + 1;
                        while (nextIndex < jobs.size() && energyLeft >= jobs.get(nextIndex).getJob().getEnergy()) {
                            energyLeft -= jobs.get(nextIndex).getJob().getEnergy();
                            numTask++;
                            nextIndex++;
                        }
                        valutazioniRicariche.add(new ImmutablePair<>(reloadPossibility, numTask));
                    }
                    //stampa tutte le valutazioni ricariche:
                    for (Pair<Integer, Integer> valutazioneRicarica : valutazioniRicariche) {
                        System.out.println("Ricarica: " + valutazioneRicarica.getLeft() + " - Task: " + valutazioneRicarica.getRight());
                    }

                    //trova la coppia con il più alto numero di task e ricarica più bassa
                    Pair<Integer, Integer> bestReload = valutazioniRicariche.get(0);
                    for (Pair<Integer, Integer> valutazioneRicarica : valutazioniRicariche) {
                        if(valutazioneRicarica.getRight() > bestReload.getRight()){
                            bestReload = valutazioneRicarica;
                        }else if(valutazioneRicarica.getRight() == bestReload.getRight()){
                            if(valutazioneRicarica.getLeft() < bestReload.getLeft()){
                                bestReload = valutazioneRicarica;
                            }
                        }
                    }
                    reloadJob = new ReloadJob(agv.getId(), bestReload.getLeft());
                    //stampa best reload
                    System.out.println("Best reload: " + bestReload.getLeft() + " - Task: " + bestReload.getRight());
                }


//                int startTime = agvStartTimeMap.get(agv);
//                int endTime = agvStartTimeMap.get(agv) + reloadJob.getTime();

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


        int shift = 0;
        for (Pair<AssignedJob, Integer> reload : reloads) {
            jobs.add(reload.getRight() + shift, reload.getLeft());
            shift++;
        }
        //reset Battery:
        for (AGV agv : agvByIDMap.values()) {
            agv.setBatteryLevel(Settings.getInstance().getBatteryCapacity());
        }

        for (AssignedJob assignedJob : jobs) {
            AGV agv = assignedJob.getAgv();
            int penalty = 0;
            if(assignedJob.getJob() instanceof ReloadJob){
                penalty=1;
                agv.reload(assignedJob.getJob().getEnergy());
            }else{
                agv.work(assignedJob.getJob().getEnergy());
            }
            int startTime = agvStartTimeMap.get(agv.getId());
            int endTime = agvStartTimeMap.get(agv.getId()) + assignedJob.getJob().getTime() + penalty;
            assignedJob.setStartTime(startTime);
            assignedJob.setEndTime(endTime);
            if(endTime > this.makespan){
                this.makespan = endTime;
            }
            agvStartTimeMap.put(agv.getId(), endTime);
            System.out.println(assignedJob);
        }
        
    }

    public void print() {
        System.out.println("----------------------------------");
        System.out.println("MAKESPAN: "+this.getMakespan());
        System.out.println(" FITNESS: "+this.getFitness());
        System.out.println("----------------------------------");
    }
    
    private void calculateMakespan(){
        //ricalcola il makespan
        //TODO
        this.dirty = false;
    }
    
    public int getMakespan(){
        if(dirty){
           calculateMakespan();
        }
        return this.makespan;
    }
    
    public void calculateFitness(){
        this.fitness = Settings.K1 * (float)getMakespan() + Settings.K2*Settings.TETHA*(float)this.numAGV;
    }
    
    public float getFitness(){
        return this.fitness;
    }

    public String printJobs(boolean withReload){
        StringBuilder sb = new StringBuilder();
        for(AssignedJob job : jobs){
            if(job.getJob() instanceof ReloadJob && !withReload){
                continue;
            }
            sb.append("J").append(job.getJob().getId()).append(" ");
        }
        sb.append(" | ");
        for(AssignedJob job : jobs){
            if(job.getJob() instanceof ReloadJob && !withReload){
                continue;
            }
            sb.append("A").append(job.getAgv().getId()).append(" ");
        }
        return sb.toString();
    }

    public List<AssignedJob> getReloadJobs() {
        List<AssignedJob> reloadJobs = new LinkedList<>();
        for(AssignedJob job : jobs){
            if(job.getJob() instanceof ReloadJob){
                reloadJobs.add(job);
            }
        }
        return reloadJobs;
    }

    public List<AssignedJob> getWorkJobs(){
        List<AssignedJob> workJobs = new LinkedList<>();
        for(AssignedJob job : jobs){
            if(job.getJob() instanceof WorkJob){
                workJobs.add(job);
            }
        }
        return workJobs;
    }
}
