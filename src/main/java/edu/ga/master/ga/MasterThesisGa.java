/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package edu.ga.master.ga;

import edu.ga.master.ga.exceptions.NoGeneratedJobsException;
import edu.ga.master.ga.model.JobGenerator;
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
//
//        Factory<Genotype<BitGene>> gtf = Genotype.of(BitChromosome.of(10, 0.5));
//        Engine<BitGene, Integer> engine
//                = Engine.builder(MasterThesisGa::eval, gtf).build();
//        
//        System.out.println("GTF "+gtf);
//
//        Genotype<BitGene> result = engine.stream()
//                .limit(500)
//                .collect(EvolutionResult.toBestGenotype());
//        
//        System.out.println("SOLUZIONE:\n" + result);
//        System.out.println("finito tutto");



        JobGenerator.getInstance().generateJobs(10, 5, 3);
        try {
            JobGenerator.getInstance().printJobs();
        } catch (NoGeneratedJobsException ex) {
            ex.printStackTrace();
        }

    }

    public static Integer eval(Genotype<BitGene> gt) {
        int n = gt.getChromosome().as(BitChromosome.class).bitCount();
        //System.out.println("N = " + n);
        return n;
    }
}
//prova
