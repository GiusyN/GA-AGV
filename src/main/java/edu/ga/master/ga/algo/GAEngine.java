package edu.ga.master.ga.algo;

import edu.ga.master.ga.model.AGV;
import edu.ga.master.ga.model.AssignedJob;
import edu.ga.master.ga.model.Individual;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//singleton
public class GAEngine {

    private static GAEngine instance;

    private GAEngine(){

    }

    public static GAEngine getInstance(){
        if(instance == null){
            instance = new GAEngine();
        }
        return instance;
    }

    public void run(){

    }

    //controlla che una lista di AssignedJob non contenga duplicati
    public void fixDuplicate(LinkedList<AssignedJob> child1, LinkedList<AssignedJob> child2){
        for (int i = 0; i < child1.size(); i++) {
            for (int j = i+1; j < child1.size(); j++) {
                if(child1.get(i).getJob().getId() == child1.get(j).getJob().getId()){
                    child1.get(j).setJob(child2.get(i).getJob());
                }
            }
        }
    }

    public Pair<Individual,Individual> crossover(@NotNull Individual dad, @NotNull Individual mum){

        List<AGV> agvsDad = new ArrayList<>(dad.getAssignedJobs().size()*2);
        List<AGV> agvsMum = new ArrayList<>(mum.getAssignedJobs().size()*2);

        //fill agvs with all agvs
        for (int i = 0; i < dad.getAssignedJobs().size(); i++) {
            agvsDad.add(dad.getAssignedJobs().get(i).getAgv());
        }
        for (int i = 0; i < mum.getAssignedJobs().size(); i++) {
            agvsMum.add(mum.getAssignedJobs().get(i).getAgv());
        }

        LinkedList<AssignedJob> daddyAssignedJobs = dad.getAssignedJobs();
        LinkedList<AssignedJob> mummyAssignedJobs = mum.getAssignedJobs();

        int startCrossPoint =    daddyAssignedJobs.size() / 3;
        int endCrossPoint   = 2*(daddyAssignedJobs.size() / 3);

        LinkedList<AssignedJob> child1AssignedJobs = new LinkedList<>();
        LinkedList<AssignedJob> child2AssignedJobs = new LinkedList<>();

        for (int i = 0; i < daddyAssignedJobs.size(); i++) {
            if(i >= startCrossPoint && i <= endCrossPoint){
                child1AssignedJobs.add(daddyAssignedJobs.get(i));
                child2AssignedJobs.add(mummyAssignedJobs.get(i));
            }else{
                child1AssignedJobs.add(mummyAssignedJobs.get(i));
                child2AssignedJobs.add(daddyAssignedJobs.get(i));
            }
        }

        fixDuplicate(child1AssignedJobs,child2AssignedJobs);
        fixDuplicate(child2AssignedJobs,child1AssignedJobs);

        Individual kid1 = Individual.generate(child1AssignedJobs);
        Individual kid2 = Individual.generate(child2AssignedJobs);

        int swapPoint = child1AssignedJobs.size()/2;

        //swap the first half of assigned agv from the kid1 to the kid2
        System.out.println("swapPoint: " + swapPoint);

        for (int i = 0; i < swapPoint; i++) {
            kid1.getAssignedJobs().get(i).setAgv(agvsDad.get(i));
            kid2.getAssignedJobs().get(i).setAgv(agvsMum.get(i));
        }
        for (int i = swapPoint; i < child1AssignedJobs.size(); i++) {
            kid1.getAssignedJobs().get(i).setAgv(agvsMum.get(i));
            kid2.getAssignedJobs().get(i).setAgv(agvsDad.get(i));
        }

        System.out.println("************************** END CROSSOVER **************************");
        return new ImmutablePair<>(kid1,kid2);
    }


}
