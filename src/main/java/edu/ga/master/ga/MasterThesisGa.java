/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package edu.ga.master.ga;

import edu.ga.master.ga.cli.ConsoleColors;
import edu.ga.master.ga.exceptions.BatteryException;
import edu.ga.master.ga.exceptions.GAInconsistencyException;
import edu.ga.master.ga.exceptions.NoGeneratedJobsException;
import edu.ga.master.ga.model.Individual;
import edu.ga.master.ga.model.JobManager;
import edu.ga.master.ga.model.Population;
import edu.ga.master.ga.model.impl.FakeJobGenerator;
import edu.ga.master.ga.model.impl.RealJobGenerator;
import edu.ga.master.ga.utils.Settings;
import org.fusesource.jansi.AnsiConsole;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author snast
 */
public class MasterThesisGa {

    public static void main(String[] args) throws BatteryException {
        AnsiConsole.systemInstall();
        try {
            System.out.println(ConsoleColors.ANSI_YELLOW+"Hello World!"+ConsoleColors.ANSI_RESET);
            System.out.println("Ciao Luca come va tutt'appost ?");

            Settings.getInstance().setBatteryCapacity(20);
            Settings.getInstance().setMaxTime(5);
            Settings.getInstance().setPopulationSize(100);
            Settings.getInstance().setNumberOfJobs(150);
            JobManager.getInstance().init(new RealJobGenerator());
            JobManager.getInstance().generateJobs(150); //TODO FIX DUPLICATE SETTINGS ENTRY
            try {
                JobManager.getInstance().printJobs();
            } catch (NoGeneratedJobsException ex) {
                ex.printStackTrace();
            }
            System.out.println("------------------------------------------");
            System.out.println("Current Battery Capacity = " + Settings.getInstance().getBatteryCapacity());
            System.out.println("------------------------------------------");
            Individual individual = new Individual(Settings.getInstance().getAgvQuantity());
            individual.calculateFitness();
            individual.print();

            System.out.println("********************************************");
            System.out.println(" creation of population of size 100");
            System.out.println("********************************************");
            Population population = new Population.Builder() //il size è settato in settings
                    .distribution(Population.DISTRIBUTION.EQUAL)
                    .minimumAGV(2)
                    .maximumAGV(6)
                    .build();

            if(Settings.getInstance().getNumberOfJobs() <= 15){
                population.print(true);
            }else{
                population.printWithManyJobs();
            }


        } catch (BatteryException ex) {
            ex.printStackTrace();
        } catch (GAInconsistencyException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

}
//prova
