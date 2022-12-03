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
public class FakeJobGenerator implements JobGenerator{

    @Override
    public LinkedList<WorkJob> generate(int n) {
        System.out.println("[FAKE GENERATION] ON");
        System.out.println(" - n parameter will be ignored.");
        return List.of(
                new WorkJob(0, 3, 3),
                new WorkJob(1, 3, 3),
                new WorkJob(2, 2, 1),
                new WorkJob(3, 3, 3),
                new WorkJob(4, 5, 3),
                new WorkJob(5, 2, 1),
                new WorkJob(6, 2, 3),
                new WorkJob(7, 2, 2),
                new WorkJob(8, 3, 2),
                new WorkJob(9, 1, 2)
        ).stream().collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
    }
    
}
