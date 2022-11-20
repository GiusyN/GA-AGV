/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package edu.ga.master.ga;

import edu.ga.master.ga.exceptions.NoGeneratedJobsException;
import edu.ga.master.ga.model.Individual;
import edu.ga.master.ga.model.JobManager;
import edu.ga.master.ga.model.impl.FakeJobGenerator;
import edu.ga.master.ga.utils.Settings;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jenetics.BitChromosome;
import org.jenetics.BitGene;
import org.jenetics.Genotype;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;

/**
 *
 * @author snast
 */
public class MasterThesisGa {

    public static void main(String[] args) {
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
        
        Individual individual = new Individual();
        
        individual.print();

    }

}
//prova
