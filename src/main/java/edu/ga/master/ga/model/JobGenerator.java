/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model;

import edu.ga.master.ga.exceptions.NoGeneratedJobsException;
import edu.ga.master.ga.utils.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sommovir
 */
public class JobGenerator {
    
    private static JobGenerator _instance = null;
    private List<Job> jobs = null;
    
    public static JobGenerator getInstance() {
        if (_instance == null) {
            _instance = new JobGenerator();
        }
        return _instance;
    }
    
    private JobGenerator() {
        super();
    }
    
    public void generateJobs(int n, int maxTime, int capacity){
        jobs = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
           jobs.add(new Job(
                   i,  //id
                   Utils.randomInRange(1, maxTime+1), //time
                   Utils.randomInRange(1, capacity+1) //energy
           ));
        }
    }
    
    public void printJobs() throws NoGeneratedJobsException{
        if(this.jobs == null){
            throw new NoGeneratedJobsException();
        }
        for (Job job : jobs) {
            System.out.println(job);
        }
    }
    
}
