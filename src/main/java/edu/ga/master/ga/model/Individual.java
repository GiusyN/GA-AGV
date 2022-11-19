/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model;

import edu.ga.master.ga.utils.Settings;
import edu.ga.master.ga.utils.Utils;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sommovir
 */
public class Individual {
    
    private List<AssignedJob> jobs = new LinkedList<>();

    public Individual() {
        
        int agvQuantity = Settings.getInstance().getAgvQuantity();
        List<WorkJob> workJobs = JobManager.getInstance().getJobs();
        
        //init startTime per AGV
        Map<Integer,Integer> agvStartTimeMap = new HashMap<>();
        //    1      0
        //    2      0
        //    3      0
        //    4      0
        
        for (int i = 1; i <= agvQuantity; i++) {
            agvStartTimeMap.put(i, 0);
        }
        
        for (WorkJob workJob : workJobs) {
            //TODO: assicurarsi che tutti i robottini siano stati usati nella randomizzazione iniziale
            int randomAGV = Utils.randomInRange(1, agvQuantity+1);
            int startTime = agvStartTimeMap.get(randomAGV);
            int endTime   = agvStartTimeMap.get(randomAGV) + workJob.getTime();
            jobs.add(new AssignedJob(
                    workJob,
                    randomAGV,
                    startTime,
                    endTime
            ));
            agvStartTimeMap.put(randomAGV, endTime);
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
