/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model;

import edu.ga.master.ga.exceptions.NoGeneratedJobsException;
import edu.ga.master.ga.utils.Settings;
import edu.ga.master.ga.utils.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sommovir
 */
public class JobManager {
    
    private static JobManager _instance = null;
    private List<WorkJob> jobs = null;
    
    public static JobManager getInstance() {
        if (_instance == null) {
            _instance = new JobManager();
        }
        return _instance;
    }
    
    private JobManager() {
        super();
    }
    
    public void generateJobs(int n){
        jobs = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
           jobs.add(new WorkJob(
                   i,  //id
                   Utils.randomInRange(1, Settings.getInstance().getMaxTime()+1), //time
                   Utils.randomInRange(1, Settings.getInstance().getBatteryCapacity()+1) //energy
           ));
        }
    }

    public List<WorkJob> getJobs() {
        return jobs;
    }
    
    public void printJobs() throws NoGeneratedJobsException{
        if(this.jobs == null){
            throw new NoGeneratedJobsException();
        }
        for (WorkJob job : jobs) {
            System.out.println(job);
        }
    }
    
}
