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
    public void fixDuplicate(LinkedList<AssignedJob> child1, LinkedList<AssignedJob> child2, int start, int end){
        System.out.println("start: " + start + " end: " + end);
        for (int i = start; i <= end; i++) {
            while(true) {
                int duplicates = 0;
                for (int j = 0; j < child1.size(); j++) {
                    //continue if j is between j interval
                    if (j >= start && j <= end) {
                        continue;
                    }
                    if (child1.get(i).getJob().getId() == child1.get(j).getJob().getId()) {
                        child1.get(j).setJob(child2.get(i).getJob());
                        duplicates++;
                    }
                }
                if(duplicates == 0){
                    break;
                }
            }
        }
        //CHILD 1
        System.out.println("---------------------- child1 ----------------------");
        for (AssignedJob job: child1) {
            System.out.println("JOB: "+job);

        }
        //CHILD 2
        System.out.println("---------------------- child2 ----------------------");
        for (AssignedJob job: child2) {
            System.out.println("JOB: "+job);
        }

        System.out.println("----------------------------------------------------");

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

        //---------------------------------CROSSOVER JOBS---------------------------------
        LinkedList<AssignedJob> daddyAssignedJobs = dad.getAssignedJobs();
        LinkedList<AssignedJob> mummyAssignedJobs = mum.getAssignedJobs();

        int startCrossPoint =    daddyAssignedJobs.size() / 3;
        int endCrossPoint   = 2*(daddyAssignedJobs.size() / 3);

        LinkedList<AssignedJob> child1AssignedJobs = new LinkedList<>();
        LinkedList<AssignedJob> child2AssignedJobs = new LinkedList<>();

        //calcola la sottostringa centrale
        for (int i = 0; i < daddyAssignedJobs.size(); i++) {
            if(i >= startCrossPoint && i <= endCrossPoint){
                child1AssignedJobs.add(mummyAssignedJobs.get(i));
                child2AssignedJobs.add(daddyAssignedJobs.get(i));
            }else{
                child1AssignedJobs.add(daddyAssignedJobs.get(i));
                child2AssignedJobs.add(mummyAssignedJobs.get(i));
            }
        }

        //TODO test swap ^

        fixDuplicate(child1AssignedJobs,child2AssignedJobs,startCrossPoint,endCrossPoint);
        fixDuplicate(child2AssignedJobs,child1AssignedJobs,startCrossPoint,endCrossPoint);

        Individual kid1 = Individual.generate(child1AssignedJobs);
        Individual kid2 = Individual.generate(child2AssignedJobs);

        //------------------------------------------------------------------------------------------
        //opero il crossover sugli agv
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


    public static void main(String[] args) {
        System.out.println("8/3 = " + 8/3);
        System.out.println("2*(8/3) = " + 2*(8/3));
    }


}
