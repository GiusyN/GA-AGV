package edu.ga.master.ga.algo;


import edu.ga.master.ga.exceptions.BatteryException;
import edu.ga.master.ga.model.*;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GAEngineTest {


    //check if the child has duplicate jobs
    private boolean checkDuplicate(LinkedList<AssignedJob> child) {
        for (int i = 0; i < child.size(); i++) {
            for (int j = i + 1; j < child.size(); j++) {
                if (child.get(i).getJob().getId() == child.get(j).getJob().getId()) {
                    return true;
                }
            }
        }
        return false;
    }


    @Test
    void fixDuplicate() {

        LinkedList<AssignedJob> child1 = new LinkedList<>();
        LinkedList<AssignedJob> child2 = new LinkedList<>();
        try {
            child1.add(new AssignedJob(new WorkJob(3, 1, 4), new AGV(2, 3), 0, 0));
            child1.add(new AssignedJob(new WorkJob(6, 7, 3), new AGV(1, 3), 0, 0));
            child1.add(new AssignedJob(new WorkJob(2, 5, 2), new AGV(2, 3), 0, 0));
            child1.add(new AssignedJob(new WorkJob(8, 1, 4), new AGV(2, 3), 0, 0));
            child1.add(new AssignedJob(new WorkJob(5, 9, 5), new AGV(2, 3), 0, 0));
            child1.add(new AssignedJob(new WorkJob(1, 2, 2), new AGV(1, 3), 0, 0));
            child1.add(new AssignedJob(new WorkJob(4, 2, 2), new AGV(1, 3), 0, 0));
            child1.add(new AssignedJob(new WorkJob(7, 2, 2), new AGV(1, 3), 0, 0));

            child2.add(new AssignedJob(new WorkJob(6, 2, 1), new AGV(1, 3), 0, 0));
            child2.add(new AssignedJob(new WorkJob(8, 9, 5), new AGV(1, 3), 0, 0));
            child2.add(new AssignedJob(new WorkJob(7, 2, 1), new AGV(2, 3), 0, 0));
            child2.add(new AssignedJob(new WorkJob(1, 5, 2), new AGV(1, 3), 0, 0));
            child2.add(new AssignedJob(new WorkJob(3, 7, 3), new AGV(2, 3), 0, 0));
            child2.add(new AssignedJob(new WorkJob(2, 2, 2), new AGV(2, 3), 0, 0));
            child2.add(new AssignedJob(new WorkJob(4, 2, 2), new AGV(1, 3), 0, 0));
            child2.add(new AssignedJob(new WorkJob(5, 2, 2), new AGV(2, 3), 0, 0));

            //print all elements of child1
            System.out.println("---------------------- child1 ----------------------");
            for (int i = 0; i < child1.size(); i++) {
                System.out.println(child1.get(i));
            }
            System.out.println("---------------------- child2 ----------------------");
            for (int i = 0; i < child2.size(); i++) {
                System.out.println(child2.get(i));
            }

            int startCrossPoint =    child1.size() / 3;
            int endCrossPoint   = 2*(child1.size() / 3);
            GAEngine.getInstance().fixDuplicate(child1, child2, startCrossPoint, endCrossPoint);
            GAEngine.getInstance().fixDuplicate(child2, child1, startCrossPoint, endCrossPoint);

            System.out.println("AFTER FIX DUPLICATE");
            System.out.println("---------------------- child1 ----------------------");
            for (int i = 0; i < child1.size(); i++) {
                System.out.println(child1.get(i));
            }
            System.out.println("---------------------- child2 ----------------------");
            for (int i = 0; i < child2.size(); i++) {
                System.out.println(child2.get(i));
            }

            assertFalse(checkDuplicate(child1));
            assertFalse(checkDuplicate(child2));


        } catch (BatteryException e) {
            assertTrue(false, "non dovresti essere qui . .");
        }

    }

    @Test
    void crossover() {
        LinkedList<AssignedJob> dad_assignedJobs = new LinkedList<>();
        LinkedList<AssignedJob> mum_assignedJobs = new LinkedList<>();

        try {
            dad_assignedJobs.add(new AssignedJob(new WorkJob(3, 1, 4), new AGV(2, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(6, 7, 3), new AGV(1, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(2, 5, 2), new AGV(2, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(8, 1, 4), new AGV(2, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(5, 9, 5), new AGV(2, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(1, 2, 2), new AGV(1, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(4, 2, 2), new AGV(1, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(7, 2, 2), new AGV(1, 3), 0, 0));

            mum_assignedJobs.add(new AssignedJob(new WorkJob(6, 2, 1), new AGV(1, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(8, 9, 5), new AGV(1, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(7, 2, 1), new AGV(2, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(1, 5, 2), new AGV(1, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(3, 7, 3), new AGV(2, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(2, 2, 2), new AGV(2, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(4, 2, 2), new AGV(1, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(5, 2, 2), new AGV(2, 3), 0, 0));

            Individual dad = Individual.generate(dad_assignedJobs);
            Individual mum = Individual.generate(mum_assignedJobs);

            Pair<Individual, Individual> result = GAEngine.getInstance().crossover(dad, mum);
            Individual kid1 = result.getLeft();
            Individual kid2 = result.getRight();


            System.out.println("AFTER CROSSOVER");

            List<AGV> agvKID1 = new ArrayList<>();
            List<AGV> agvKID2 = new ArrayList<>();


            System.out.println("---------------------- dad ----------------------");
            for (int i = 0; i < dad.getAssignedJobs().size(); i++) {
                System.out.println(dad.getAssignedJobs().get(i));

            }
            System.out.println("---------------------- mum ----------------------");
            for (int i = 0; i < mum.getAssignedJobs().size(); i++) {
                System.out.println(mum.getAssignedJobs().get(i));
            }
            System.out.println("---------------------- kid1 ----------------------");
            for (int i = 0; i < kid1.getAssignedJobs().size(); i++) {
                System.out.println(kid1.getAssignedJobs().get(i));
                agvKID1.add(kid1.getAssignedJobs().get(i).getAgv());
            }
            System.out.println("---------------------- kid2 ----------------------");
            for (int i = 0; i < kid2.getAssignedJobs().size(); i++) {
                System.out.println(kid2.getAssignedJobs().get(i));
                agvKID2.add(kid2.getAssignedJobs().get(i).getAgv());
            }


            List<AGV> agvSolutionKID1 = List.of(
                    new AGV(2, 3),
                    new AGV(1, 3),
                    new AGV(2, 3),
                    new AGV(2, 3),
                    new AGV(2, 3),
                    new AGV(2, 3),
                    new AGV(1, 3),
                    new AGV(2, 3));

            //check if the agv of the kid are the same of the solution in the same exact order
            assertEquals(agvSolutionKID1, agvKID1);

            int [] solutionJobIdKID1 = {3,6,7,1,5,8,4,2};
            int [] actualJObIdKID1 = new int[8];
            //fill actual job id
            for (int i = 0; i < kid1.getAssignedJobs().size(); i++) {
                actualJObIdKID1[i] = kid1.getAssignedJobs().get(i).getJob().getId();
            }
            //check if the job id of the kid are the same of the solution in the same exact order
            assertArrayEquals(solutionJobIdKID1, actualJObIdKID1);


            List<AGV> agvSolutionKID2 = List.of(
                    new AGV(1, 3),
                    new AGV(1, 3),
                    new AGV(2, 3),
                    new AGV(1, 3),
                    new AGV(2, 3),
                    new AGV(1, 3),
                    new AGV(1, 3),
                    new AGV(1, 3));


            //check if the agv of the kid are the same of the solution in the same exact order
            assertEquals(agvSolutionKID2, agvKID2);

            int [] solutionJobIdKID2 = {6,1,2,8,3,7,4,5};
            int [] actualJObIdKID2 = new int[8];
            //fill actual job id
            for (int i = 0; i < kid2.getAssignedJobs().size(); i++) {
                actualJObIdKID2[i] = kid2.getAssignedJobs().get(i).getJob().getId();
            }
            //check if the job id of the kid are the same of the solution in the same exact order
            assertArrayEquals(solutionJobIdKID2, actualJObIdKID2);
        } catch (BatteryException e) {
            throw new RuntimeException(e);
        }

    }



    @Test
    void crossover2() {
        LinkedList<AssignedJob> dad_assignedJobs = new LinkedList<>();
        LinkedList<AssignedJob> mum_assignedJobs = new LinkedList<>();

        try {
            dad_assignedJobs.add(new AssignedJob(new WorkJob(2, 1, 4), new AGV(2, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(5, 7, 3), new AGV(1, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(8, 5, 2), new AGV(2, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(9, 1, 4), new AGV(2, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(11, 9, 5), new AGV(2, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(3,3, 2), new AGV(1, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(7, 2, 2), new AGV(1, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(10, 2, 2), new AGV(1, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(4, 2, 2), new AGV(2, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(1, 2, 2), new AGV(2, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(12, 2, 2), new AGV(1, 3), 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(6, 2, 2), new AGV(1, 3), 0, 0));

            mum_assignedJobs.add(new AssignedJob(new WorkJob(3, 2, 1), new AGV(1, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(8, 9, 5), new AGV(1, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(9, 2, 1), new AGV(1, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(11, 5, 2), new AGV(2, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(10, 7, 3), new AGV(2, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(2, 2, 2), new AGV(2, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(4, 2, 2), new AGV(1, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(7, 2, 2), new AGV(2, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(12, 2, 2), new AGV(1, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(1, 2, 2), new AGV(1, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(5, 2, 2), new AGV(2, 3), 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(6, 2, 2), new AGV(2, 3), 0, 0));

            Individual dad = Individual.generate(dad_assignedJobs);
            Individual mum = Individual.generate(mum_assignedJobs);

            Pair<Individual, Individual> result = GAEngine.getInstance().crossover(dad, mum);
            Individual kid1 = result.getLeft();
            Individual kid2 = result.getRight();


            System.out.println("AFTER CROSSOVER");

            List<AGV> agvKID1 = new ArrayList<>();
            List<AGV> agvKID2 = new ArrayList<>();


            System.out.println("---------------------- dad ----------------------");
            for (int i = 0; i < dad.getAssignedJobs().size(); i++) {
                System.out.println(dad.getAssignedJobs().get(i));

            }
            System.out.println("---------------------- mum ----------------------");
            for (int i = 0; i < mum.getAssignedJobs().size(); i++) {
                System.out.println(mum.getAssignedJobs().get(i));
            }
            System.out.println("---------------------- kid1 ----------------------");
            for (int i = 0; i < kid1.getAssignedJobs().size(); i++) {
                System.out.println(kid1.getAssignedJobs().get(i));
                agvKID1.add(kid1.getAssignedJobs().get(i).getAgv());
            }
            System.out.println("---------------------- kid2 ----------------------");
            for (int i = 0; i < kid2.getAssignedJobs().size(); i++) {
                System.out.println(kid2.getAssignedJobs().get(i));
                agvKID2.add(kid2.getAssignedJobs().get(i).getAgv());
            }


            List<AGV> agvSolutionKID1 = List.of(
                    new AGV(2, 3),
                    new AGV(1, 3),
                    new AGV(2, 3),
                    new AGV(2, 3),
                    new AGV(2, 3),
                    new AGV(1, 3),

                    new AGV(1, 3),
                    new AGV(2, 3),
                    new AGV(1, 3),
                    new AGV(1, 3),
                    new AGV(2, 3),
                    new AGV(2, 3));




            //check if the agv of the kid are the same of the solution in the same exact order
            assertEquals(agvSolutionKID1, agvKID1);

            int [] solutionJobIdKID1 = {3,5,8,9,10,2,4,7,11,1,12,6};
            int [] actualJObIdKID1 = new int[12];
            //fill actual job id
            for (int i = 0; i < kid1.getAssignedJobs().size(); i++) {
                actualJObIdKID1[i] = kid1.getAssignedJobs().get(i).getJob().getId();
            }
            //print actualJobIdkID1
            System.out.println("SOLUTION KID 1");
            for (int i = 0; i < actualJObIdKID1.length; i++) {
                System.out.print(actualJObIdKID1[i]+ ", ");
            }


            //check if the job id of the kid are the same of the solution in the same exact order
            assertArrayEquals(solutionJobIdKID1, actualJObIdKID1);


            List<AGV> agvSolutionKID2 = List.of(
                    new AGV(1, 3),
                    new AGV(1, 3),
                    new AGV(1, 3),
                    new AGV(2, 3),
                    new AGV(2, 3),
                    new AGV(2, 3),

                    new AGV(1, 3),
                    new AGV(1, 3),
                    new AGV(2, 3),
                    new AGV(2, 3),
                    new AGV(1, 3),
                    new AGV(1, 3));


            //check if the agv of the kid are the same of the solution in the same exact order
            assertEquals(agvSolutionKID2, agvKID2);

            int [] solutionJobIdKID2 = {2,8,9,4,11,3,7,10,12,1,5,6};
            int [] actualJObIdKID2 = new int[12];
            //fill actual job id
            for (int i = 0; i < kid2.getAssignedJobs().size(); i++) {
                actualJObIdKID2[i] = kid2.getAssignedJobs().get(i).getJob().getId();
            }

            //print actualJobIdkID2
            System.out.println("SOLUTION KID 2");
            for (int i = 0; i < actualJObIdKID2.length; i++) {
                System.out.print(actualJObIdKID2[i]+ ", ");
            }
            //check if the job id of the kid are the same of the solution in the same exact order
            assertArrayEquals(solutionJobIdKID2, actualJObIdKID2);
        } catch (BatteryException e) {
            throw new RuntimeException(e);
        }

    }
}