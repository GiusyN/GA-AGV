package edu.ga.master.ga.algo;

import edu.ga.master.ga.exceptions.BatteryException;
import edu.ga.master.ga.exceptions.GAInconsistencyException;
import edu.ga.master.ga.model.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//singleton
public class GAEngine {

    private static GAEngine instance;

    private GAEngine() {

    }

    public static GAEngine getInstance() {
        if (instance == null) {
            instance = new GAEngine();
        }
        return instance;
    }

    public void run() {

    }

    //check if a list of assigned job contains job id ducplicates
    public boolean checkDuplicateJobId(@NotNull List<AssignedJob> assignedJobs) {
        List<Integer> jobIds = new ArrayList<>();
        for (AssignedJob assignedJob : assignedJobs) {
            if (jobIds.contains(assignedJob.getJob().getId())) {
                return true;
            }
            jobIds.add(assignedJob.getJob().getId());
        }
        return false;
    }


    //controlla che una lista di AssignedJob non contenga duplicati
    public void fixDuplicate(LinkedList<AssignedJob> child1, LinkedList<AssignedJob> child2, int start, int end) {
        System.out.println("start: " + start + " end: " + end);

        do {
            for (int i = start; i < end; i++) {
                for (int k = 0; k < child1.size(); k++) {

                    //continue if j is between j interval
                    if (k >= start && k < end) {
                        continue;
                    }
                    if (child1.get(i).getJob().getId() == child1.get(k).getJob().getId()) {
                        child1.get(k).setJob(child2.get(i).getJob());
                        break;
                    }
                }
            }
        }while (checkDuplicateJobId(child1));
        //check if there are duplicates

//        for (int i = 0; i < child1.size(); i++) {
//            for (int j = 0; j < child1.size(); j++) {
//                if (i == j) {
//                    continue;
//                }
//                if (child1.get(i).getJob().getId() == child1.get(j).getJob().getId()) {
//                    System.out.println("duplicate found");
//                    child1.get(i).setJob(child2.get(j).getJob());
//                }
//            }
//        }


        //CHILD 1
        System.out.println("---------------------- child1 ----------------------");
        for (AssignedJob job : child1) {
            System.out.println("JOB: " + job);

        }
        //CHILD 2
        System.out.println("---------------------- child2 ----------------------");
        for (AssignedJob job : child2) {
            System.out.println("JOB: " + job);
        }

        System.out.println("----------------------------------------------------");

    }

    public Pair<Individual, Individual> crossover(@NotNull Individual dad, @NotNull Individual mum) {

        List<AssignedJob> dadWorkJobs = dad.getWorkJobs();
        List<AssignedJob> mumWorkJobs = mum.getWorkJobs();


        List<AGV> agvsDad = new ArrayList<>(dadWorkJobs.size() * 2);
        List<AGV> agvsMum = new ArrayList<>(mumWorkJobs.size() * 2);

        //fill agvs with all agvs
        for (int i = 0; i < dad.getAssignedJobs().size(); i++) {
            agvsDad.add(dad.getAssignedJobs().get(i).getAgv());
        }
        for (int i = 0; i < mumWorkJobs.size(); i++) {
            agvsMum.add(mum.getAssignedJobs().get(i).getAgv());
        }

        //---------------------------------CROSSOVER JOBS---------------------------------
//        List<AssignedJob> daddyAssignedJobs = dad.getAssignedJobs();
//        List<AssignedJob> mummyAssignedJobs = mumWorkJobs;

        int startCrossPoint = dadWorkJobs.size() / 3;
        int endCrossPoint = 2 * (dadWorkJobs.size() / 3);

        LinkedList<AssignedJob> child1AssignedJobs = new LinkedList<>();
        LinkedList<AssignedJob> child2AssignedJobs = new LinkedList<>();

        //calcola la sottostringa centrale
        for (int i = 0; i < dadWorkJobs.size(); i++) {
            if (i >= startCrossPoint && i < endCrossPoint) {
                child1AssignedJobs.add(mumWorkJobs.get(i));
                child2AssignedJobs.add(dadWorkJobs.get(i));
            } else {
                child1AssignedJobs.add(dadWorkJobs.get(i));
                child2AssignedJobs.add(mumWorkJobs.get(i));
            }
        }

        //print child1
        System.out.println("---------------------- child1 AFTER SWAP ----------------------");
        for (AssignedJob job : child1AssignedJobs) {
            System.out.println("JOB: " + job);
        }
        //print child2
        System.out.println("---------------------- child2 AFTER SWAP ----------------------");
        for (AssignedJob job : child2AssignedJobs) {
            System.out.println("JOB: " + job);
        }

        //TODO test swap ^

        fixDuplicate(child1AssignedJobs, child2AssignedJobs, startCrossPoint, endCrossPoint);
        fixDuplicate(child2AssignedJobs, child1AssignedJobs, startCrossPoint, endCrossPoint);

        Individual kid1 = Individual.generate(child1AssignedJobs);
        Individual kid2 = Individual.generate(child2AssignedJobs);

        //------------------------------------------------------------------------------------------
        //opero il crossover sugli agv
        int swapPoint = child1AssignedJobs.size() / 2;

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


        try {
            kid1.setTestcode("KID1");
            kid2.setTestcode("KID2");

            kid1.calculateReloads();
            kid2.calculateReloads();
        } catch (BatteryException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (GAInconsistencyException e) {
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println("************************** END CROSSOVER **************************");
        return new ImmutablePair<>(kid1, kid2);
    }


    public static void main(String[] args) {
        System.out.println("8/3 = " + 8 / 3);
        System.out.println("2*(8/3) = " + 2 * (8 / 3));
    }


}
