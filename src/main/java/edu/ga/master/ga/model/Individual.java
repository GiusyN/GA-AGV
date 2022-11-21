/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model;

import edu.ga.master.ga.exceptions.BatteryException;
import edu.ga.master.ga.utils.Settings;
import edu.ga.master.ga.utils.Utils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
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

    public Individual(int numAGV) throws BatteryException {
        this.numAGV = numAGV;
        List<WorkJob> workJobs = JobManager.getInstance().getJobs();

        //init startTime per AGV
        Map<Integer, Integer> agvStartTimeMap = new HashMap<>();
        //    1      0
        //    2      0
        //    3      0
        //    4      0
        
        Map<Integer, AGV> agvByIDMap = new HashMap<>();

        for (int i = 1; i <= this.numAGV; i++) {
            agvStartTimeMap.put(i, 0);
            AGV agv = new AGV(i, Settings.getInstance().getBatteryCapacity());
            agvByIDMap.put(agv.getId(), agv);
        }
        
        
        
        for (WorkJob workJob : workJobs) {
            //TODO: assicurarsi che tutti i robottini siano stati usati nella randomizzazione iniziale
            int randomAGV = Utils.randomInRange(1, this.numAGV + 1);
            int startTime = agvStartTimeMap.get(randomAGV);
            int endTime = agvStartTimeMap.get(randomAGV) + workJob.getTime();
            AGV agv = agvByIDMap.get(randomAGV);
            jobs.add(new AssignedJob(
                    workJob,
                    agv,
                    startTime,
                    endTime
            ));
            agvStartTimeMap.put(randomAGV, endTime);
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

            if (agv.getBatteryLevel() < energy) {  //se NON ho energia sufficiente per il prossimo job
                //inserire il nodo di ricarica
                int energyMissingToFullCapacity = Settings.getInstance().getBatteryCapacity() - agv.getBatteryLevel();
                int minimumEnergyForNextStep = energy - agv.getBatteryLevel();
                int randomQuantityEnergyToReload = Utils.randomInRange(1, energyMissingToFullCapacity + 1);
                int finalReload = randomQuantityEnergyToReload < minimumEnergyForNextStep ? minimumEnergyForNextStep : randomQuantityEnergyToReload;
                ReloadJob reloadJob = new ReloadJob(agv.getId(), finalReload);
//                int startTime = agvStartTimeMap.get(agv);
//                int endTime = agvStartTimeMap.get(agv) + reloadJob.getTime();

                reloads.add(new ImmutablePair<AssignedJob, Integer>(new AssignedJob(
                        reloadJob,
                        assignedJob.getAgv(),
                        -1,
                        -1),        //LEFT of PAIR
                        index)        //RIGHT of PAIR
                );
                    agv.reload(finalReload);
                    agv.work(energy);
                    
//                agvStartTimeMap.put(agv, endTime);
            } else {
//                if (assignedJob.getJob() instanceof ReloadJob) {
//                    agv.reload(energy);
//                } else {
                    agv.work(energy);
//                }
//                int startTime = agvStartTimeMap.get(agv);
//                int endTime = agvStartTimeMap.get(agv) + assignedJob.getJob().getTime();
//                assignedJob.setStartTime(startTime);
//                assignedJob.setEndTime(endTime);
//                agvStartTimeMap.put(agv, endTime);
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

}
