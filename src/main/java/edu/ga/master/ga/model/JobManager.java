/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model;

import edu.ga.master.ga.exceptions.NoGeneratedJobsException;
import edu.ga.master.ga.model.impl.FakeJobGenerator;
import edu.ga.master.ga.utils.Settings;
import edu.ga.master.ga.utils.Utils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sommovir
 */
public class JobManager {
    
    private static JobManager _instance = null;
    private LinkedList<WorkJob> jobs = null;
    private JobGenerator jobGenerator = null;
    
    public static JobManager getInstance() {
        if (_instance == null) {
            _instance = new JobManager();
        }
        return _instance;
    }
    
    private JobManager() {
        super();
    }

    //DEPENDENCY INJECTION HERE
    public void init(JobGenerator jobGenerator) {
        this.jobGenerator = jobGenerator;
    }
    
    
    public void generateJobs(int n){
        if(this.jobGenerator == null){
            this.jobGenerator = new FakeJobGenerator();
        }
        this.jobs = jobGenerator.generate(n);
    }

    public LinkedList<WorkJob> getJobs() {
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
