/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package edu.ga.master.ga;

import edu.ga.master.ga.algo.GAEngine;
import edu.ga.master.ga.cli.ConsoleColors;
import edu.ga.master.ga.exceptions.BatteryException;
import edu.ga.master.ga.exceptions.GAInconsistencyException;
import edu.ga.master.ga.exceptions.NoGeneratedJobsException;
import edu.ga.master.ga.model.Individual;
import edu.ga.master.ga.model.JobManager;
import edu.ga.master.ga.model.Population;
import edu.ga.master.ga.model.impl.RealJobGenerator;
import edu.ga.master.ga.utils.Settings;
import org.fusesource.jansi.AnsiConsole;
import com.formdev.flatlaf.FlatIntelliJLaf;
import edu.ga.master.ga.gui.ProcessViewerFrame;

/**
 *
 * @author snast
 */
public class MasterThesisGa {

    
    private static void launchGUI(){
         try {

            FlatIntelliJLaf.installLafInfo();
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                System.out.println("---- lf -> " + info.getName());
                if (ProcessViewerFrame.LIGHT_THEME.equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ProcessViewerFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProcessViewerFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProcessViewerFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProcessViewerFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ProcessViewerFrame().setVisible(true);
            }
        });
    }
    
    public static void main(String[] args) throws BatteryException {
        AnsiConsole.systemInstall();
        FlatIntelliJLaf.installLafInfo();
        launchGUI();
        
        
//        try {
            System.out.println(ConsoleColors.ANSI_YELLOW+"Hello World!"+ConsoleColors.ANSI_RESET);
            System.out.println("Ciao Luca come va tutt'appost ?");
            Settings.getInstance().setElitism(10);
            GAEngine.getInstance().setMaxCycle(15000);
            Settings.getInstance().setVerbose(false);
            Settings.getInstance().setBatteryCapacity(12);
            Settings.getInstance().setMaxTime(50);
            Settings.getInstance().setPopulationSize(100);
            Settings.getInstance().setNumberOfJobs(150);
            Settings.getInstance().setKalergi(0.4f);
            JobManager.getInstance().init(new RealJobGenerator());
            JobManager.getInstance().generateJobs();
            try {
                JobManager.getInstance().printJobs();
            } catch (NoGeneratedJobsException ex) {
                ex.printStackTrace();
            }
            System.out.println("------------------------------------------");
            System.out.println("Current Battery Capacity = " + Settings.getInstance().getBatteryCapacity());
            System.out.println("------------------------------------------");
//            Individual individual = new Individual(Settings.getInstance().getAgvQuantity());
//            individual.calculateFitness();
//            individual.print();

            System.out.println("********************************************");
            System.out.println(" creation of population of size 100");
            System.out.println("********************************************");
            Population population = new Population.Builder() //il size è settato in settings
                    .distribution(Population.DISTRIBUTION.EQUAL)
                    .minimumAGV(2)
                    .maximumAGV(6)
                    .build();

            if(Settings.getInstance().getNumberOfJobs() <= 15){
                population.print(false);
            }else{
                population.printWithManyJobs();
            }
            try {
                System.out.printf("RUNNIGN ALGORITHM WITH %d CYCLES %n", GAEngine.getInstance().getNumberOfCycles());

                GAEngine.getInstance().run(population);

                System.out.println("********************************************");
                System.out.println(" population after algorithm:");
            } catch (GAInconsistencyException ex) {
                ex.printStackTrace();
            }

            if(Settings.getInstance().getNumberOfJobs() <= 15){
                population.print(true);
            }else{
                population.printWithManyJobs();
            }

//        } catch (BatteryException ex) {
//            ex.printStackTrace();
//        } catch (GAInconsistencyException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }

    }

}
//prova
