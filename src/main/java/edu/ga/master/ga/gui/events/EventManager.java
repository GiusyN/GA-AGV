/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.gui.events;

import edu.ga.master.ga.model.Individual;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sommovir
 */
public class EventManager {

    private static EventManager _instance = null;
    private List<EventListener> listeners = new LinkedList<>();
    private List<SolutionListener> solutionListeners = new LinkedList<SolutionListener>();

    public static EventManager getInstance() {
        if (_instance == null) {
            _instance = new EventManager();
        }
        return _instance;
    }

    private EventManager() {
        super();
    }

    public void addSolutionListener(SolutionListener listener) {
        this.solutionListeners.add(listener);
    }

    public void addEventListener(EventListener listener) {
        this.listeners.add(listener);
    }

    public void startsSimulation(float initialFitness) {
        for (SolutionListener listener : solutionListeners) {
            listener.start(initialFitness);
        }
    }

    public void end(Individual bestone) {
        for (SolutionListener listener : solutionListeners) {
            listener.end(bestone);
        }
    }

    public void newImprovement(Individual bestone, float newFitness) {
        for (SolutionListener listener : solutionListeners) {
            listener.newImprovement(bestone,newFitness);
        }
    }

    public void nextCycle(int cycle) {
        for (SolutionListener listener : solutionListeners) {
            listener.nextCycle(cycle);
        }
    }

    public void settingsChanged() {
        for (EventListener listener : listeners) {
            listener.settingsChanged();
        }
    }

    public void generationStarted() {
        for (EventListener listener : listeners) {
            listener.generationStarted();
        }
    }

    public void generationEnded() {
        for (EventListener listener : listeners) {
            listener.generationEnded();
        }
    }

    public void backToDefault() {
        for (EventListener listener : listeners) {
            listener.backToDefault();
        }
    }
    
    public void newAVG(float avg){
        for (SolutionListener solutionListener : solutionListeners) {
            solutionListener.newAVG(avg);
        }
    }
    
    public void kalergi(){
        for (SolutionListener solutionListener : solutionListeners) {
            solutionListener.kalergi();
        }
    }

}
