/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package edu.ga.master.ga;

import edu.ga.master.ga.exceptions.BatteryException;
import edu.ga.master.ga.exceptions.NoGeneratedJobsException;
import edu.ga.master.ga.model.Individual;
import edu.ga.master.ga.model.JobManager;
import edu.ga.master.ga.model.impl.FakeJobGenerator;
import edu.ga.master.ga.utils.Settings;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author snast
 */
public class MasterThesisGa {

    public static void main(String[] args) throws BatteryException {
        try {
            System.out.println("Hello World!");
            System.out.println("Ciao Luca come va tutt'appost ?");
            
            Settings.getInstance().setBatteryCapacity(3);
            Settings.getInstance().setMaxTime(5);
            JobManager.getInstance().init(new FakeJobGenerator());
            JobManager.getInstance().generateJobs(10);
            try {
                JobManager.getInstance().printJobs();
            } catch (NoGeneratedJobsException ex) {
                ex.printStackTrace();
            }
            System.out.println("------------------------------------------");
            System.out.println("Current Battery Capacity = "+Settings.getInstance().getBatteryCapacity());
            System.out.println("------------------------------------------");
            Individual individual = new Individual();
            individual.print();
            
        } catch (BatteryException ex) {
            ex.printStackTrace();
        }

    }

}
//prova
