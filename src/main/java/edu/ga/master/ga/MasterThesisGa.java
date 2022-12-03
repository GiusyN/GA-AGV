/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package edu.ga.master.ga;

import edu.ga.master.ga.cli.ConsoleColors;
import edu.ga.master.ga.exceptions.BatteryException;
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

            Settings.getInstance().setBatteryCapacity(5);
            Settings.getInstance().setMaxTime(5);
            Settings.getInstance().setPopulationSize(100);
            Settings.getInstance().setNumberOfJobs(10);
            JobManager.getInstance().init(new RealJobGenerator());
            JobManager.getInstance().generateJobs(10);
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
                    .minimumAGV(1)
                    .maximumAGV(6)
                    .build();

            population.print(true);


        } catch (BatteryException ex) {
            ex.printStackTrace();
        }

    }

}
//prova
