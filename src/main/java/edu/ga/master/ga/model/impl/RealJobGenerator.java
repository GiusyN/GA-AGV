/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model.impl;

import edu.ga.master.ga.model.Job;
import edu.ga.master.ga.model.JobGenerator;
import edu.ga.master.ga.model.WorkJob;
import edu.ga.master.ga.utils.Settings;
import edu.ga.master.ga.utils.Utils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sommovir
 */
public class RealJobGenerator implements JobGenerator{

    @Override
    public LinkedList<WorkJob> generate(int n) {
        LinkedList<WorkJob> jobs = new LinkedList<>();
        for (int i = 0; i < n; i++) {
           jobs.add(new WorkJob(
                   i,  //id
                   Utils.randomInRange(
                           1,
                           Settings.getInstance().getMaxTime()+1), //time
                   Utils.randomInRange(
                           Settings.getInstance().getMinimumEnergyOfJob(),
                           Settings.getInstance().getMaximumEnergyOfJob()+1) //energy
           ));
        }
        return jobs;
    }
    
}
