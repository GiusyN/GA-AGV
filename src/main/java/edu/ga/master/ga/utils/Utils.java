/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.utils;

import java.util.Random;

/**
 *
 * @author sommovir
 */
public class Utils {
    /**
     * Restituisce un numero intero casuale tra min (compreso) e max (non compreso) .
     *
     * @param min
     * il valore minimo entro il quale sarà generato il numero casuale
     * MIN COMPRESO
     * @param max
     * il valore massimo entro il quale sarà generato il numero casuale
     * MAX NON COMPRESO
     * @return un numero intero casuale tra min e max
     */
    public static int randomInRange(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }
    
    
    public static int max(int a, int b){
        return a >= b ? a : b;
    }
}
