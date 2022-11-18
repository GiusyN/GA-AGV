/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package edu.ga.master.ga;

import org.jenetics.BitChromosome;
import org.jenetics.BitGene;
import org.jenetics.Genotype;
import org.jenetics.engine.Engine;
import org.jenetics.util.Factory;

/**
 *
 * @author snast
 */
public class MasterThesisGa {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println("Ciao Luca come va tutt'appost ?");

        Factory<Genotype<BitGene>> gtf = Genotype.of(BitChromosome.of(10, 0.5));

     
    }

    public Integer eval(Genotype<BitGene> gt) {
        return gt.getChromosome().as(BitChromosome.class).bitCount();
    }
}
//prova
