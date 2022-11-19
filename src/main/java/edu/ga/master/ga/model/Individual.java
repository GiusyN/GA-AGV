/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model;

import edu.ga.master.ga.utils.Settings;
import edu.ga.master.ga.utils.Utils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 *
 * @author sommovir
 */
public class Individual {

    private LinkedList<AssignedJob> jobs = new LinkedList<>();

    public Individual() {

        int agvQuantity = Settings.getInstance().getAgvQuantity();
        List<WorkJob> workJobs = JobManager.getInstance().getJobs();

        //init startTime per AGV
        Map<Integer, Integer> agvStartTimeMap = new HashMap<>();
        //    1      0
        //    2      0
        //    3      0
        //    4      0

        for (int i = 1; i <= agvQuantity; i++) {
            agvStartTimeMap.put(i, 0);
        }

        for (WorkJob workJob : workJobs) {
            //TODO: assicurarsi che tutti i robottini siano stati usati nella randomizzazione iniziale
            int randomAGV = Utils.randomInRange(1, agvQuantity + 1);
            int startTime = agvStartTimeMap.get(randomAGV);
            int endTime = agvStartTimeMap.get(randomAGV) + workJob.getTime();
            jobs.add(new AssignedJob(
                    workJob,
                    randomAGV,
                    startTime,
                    endTime
            ));
            agvStartTimeMap.put(randomAGV, endTime);
        }

        //adding reloading events
        //   agv    capacity
        Map<Integer, Integer> energyMap = new HashMap<>();
        //    1      3
        //    2      3
        //    3      3
        //    4      3

        for (int i = 1; i <= agvQuantity; i++) {
            energyMap.put(i, Settings.getInstance().getBatteryCapacity());
        }

        //reset starTime helper map
        for (int i = 1; i <= agvQuantity; i++) {
            agvStartTimeMap.put(i, 0);
        }

        ListIterator<AssignedJob> iterator = jobs.listIterator();
        int index = 0;
        while (iterator.hasNext()) {
            AssignedJob assignedJob = iterator.next();
            int agv = assignedJob.getAgv();
            int energy = assignedJob.getJob().getEnergy();

            if (energyMap.get(agv) < energy) {  //se NON ho energia sufficiente per il prossimo job
                //inserire il nodo di ricarica
                int deltaEnergy = Settings.getInstance().getBatteryCapacity() - energyMap.get(agv);
                int randomQuantityEnergyToReload = Utils.randomInRange(1, deltaEnergy + 1);
                ReloadJob reloadJob = new ReloadJob(agv, randomQuantityEnergyToReload);
//                int startTime = agvStartTimeMap.get(agv);
//                int endTime = agvStartTimeMap.get(agv) + reloadJob.getTime();
                int startTime = agvStartTimeMap.get(agv);
                int endTime = agvStartTimeMap.get(agv) + reloadJob.getTime();

                iterator.add(new AssignedJob(
                        reloadJob,
                        agv,
                        startTime,
                        endTime)
                );
                agvStartTimeMap.put(agv, endTime);
            } else {
                if (assignedJob.getJob() instanceof ReloadJob) {
                    energyMap.put(agv, energyMap.get(agv) + energy);
                } else {
                    energyMap.put(agv, energyMap.get(agv) - energy);
                }
                int startTime = agvStartTimeMap.get(agv);
                int endTime = agvStartTimeMap.get(agv) + assignedJob.getJob().getTime();
                assignedJob.setStartTime(startTime);
                assignedJob.setEndTime(endTime);
                agvStartTimeMap.put(agv, endTime);
            }
            index++;

        }

    }

    public void print() {
        System.out.println("INDIVIDUAL: ");
        for (AssignedJob job : jobs) {
            System.out.println(job);
        }
        System.out.println("----------------------------------");
    }

}
