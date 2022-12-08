/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.gui.events;

import edu.ga.master.ga.model.Individual;


/**
 *
 * @author sommovir
 */
public interface SolutionListener {
    
    public void start(float initialFitness);
    
    public void end(Individual bestone);
    
    public void newImprovement(Individual bestone, float newFitness);
    
    public void nextCycle(int cycle);
    
    public void newAVG(float avg);
    
    public void kalergi();
    
}
