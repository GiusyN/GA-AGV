package edu.ga.master.ga.algo;


import edu.ga.master.ga.exceptions.BatteryException;
import edu.ga.master.ga.exceptions.GAInconsistencyException;
import edu.ga.master.ga.model.*;
import edu.ga.master.ga.model.impl.FakeJobGenerator;
import edu.ga.master.ga.model.impl.RealJobGenerator;
import edu.ga.master.ga.utils.Settings;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GAEngineTest {

    //execute this to initialize
    @BeforeAll
    static void init() {
        System.out.println("---- INIT TEST SUITE ----");
        Settings.getInstance().setBatteryCapacity(5);
        Settings.getInstance().setMaxTime(5);
        JobManager.getInstance().init(new FakeJobGenerator());
        JobManager.getInstance().generateJobs(10);
    }

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
            AGV agv1 = new AGV(1, Settings.getInstance().getBatteryCapacity());
            AGV agv2 = new AGV(2, Settings.getInstance().getBatteryCapacity());

            child1.add(new AssignedJob(new WorkJob(3, 1, 4), agv2, 0, 0));
            child1.add(new AssignedJob(new WorkJob(6, 7, 3), agv1, 0, 0));
            child1.add(new AssignedJob(new WorkJob(2, 5, 2), agv2, 0, 0));
            child1.add(new AssignedJob(new WorkJob(8, 1, 4), agv2, 0, 0));
            child1.add(new AssignedJob(new WorkJob(5, 9, 5), agv2, 0, 0));
            child1.add(new AssignedJob(new WorkJob(1, 2, 2), agv1, 0, 0));
            child1.add(new AssignedJob(new WorkJob(4, 2, 2), agv1, 0, 0));
            child1.add(new AssignedJob(new WorkJob(7, 2, 2), agv1, 0, 0));

            child2.add(new AssignedJob(new WorkJob(6, 2, 1), agv1, 0, 0));
            child2.add(new AssignedJob(new WorkJob(8, 9, 5), agv1, 0, 0));
            child2.add(new AssignedJob(new WorkJob(7, 2, 1), agv2, 0, 0));
            child2.add(new AssignedJob(new WorkJob(1, 5, 2), agv1, 0, 0));
            child2.add(new AssignedJob(new WorkJob(3, 7, 3), agv2, 0, 0));
            child2.add(new AssignedJob(new WorkJob(2, 2, 2), agv2, 0, 0));
            child2.add(new AssignedJob(new WorkJob(4, 2, 2), agv1, 0, 0));
            child2.add(new AssignedJob(new WorkJob(5, 2, 2), agv2, 0, 0));

            //print all elements of child1
            System.out.println("---------------------- child1 ----------------------");
            for (int i = 0; i < child1.size(); i++) {
                System.out.println(child1.get(i));
            }
            System.out.println("---------------------- child2 ----------------------");
            for (int i = 0; i < child2.size(); i++) {
                System.out.println(child2.get(i));
            }

            int startCrossPoint = child1.size() / 3;
            int endCrossPoint = 2 * (child1.size() / 3);
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
        } catch (GAInconsistencyException e) {
            assertTrue(false, "non dovresti essere qui . .");
        }

    }

    @Test
    void crossover() {
        LinkedList<AssignedJob> dad_assignedJobs = new LinkedList<>();
        LinkedList<AssignedJob> mum_assignedJobs = new LinkedList<>();

        try {
            AGV agv1 = new AGV(1, Settings.getInstance().getBatteryCapacity());
            AGV agv2 = new AGV(2, Settings.getInstance().getBatteryCapacity());

            dad_assignedJobs.add(new AssignedJob(new WorkJob(3, 1, 4), agv2, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(6, 7, 3), agv1, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(2, 5, 2), agv2, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(8, 1, 4), agv2, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(5, 9, 5), agv2, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(1, 2, 2), agv1, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(4, 2, 2), agv1, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(7, 2, 2), agv1, 0, 0));

            mum_assignedJobs.add(new AssignedJob(new WorkJob(6, 2, 1), agv1, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(8, 9, 5), agv1, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(7, 2, 1), agv2, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(1, 5, 2), agv1, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(3, 7, 3), agv2, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(2, 2, 2), agv2, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(4, 2, 2), agv1, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(5, 2, 2), agv2, 0, 0));

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
                    new AGV(2, 5),
                    new AGV(1, 5),
                    new AGV(2, 5),
                    new AGV(2, 5),
                    new AGV(2, 5),
                    new AGV(2, 5),
                    new AGV(1, 5),
                    new AGV(2, 5));

            //extract all workjob from kid1
            List<AssignedJob> workJobsKid1 = kid1.getWorkJobs();
            //array of all agv id of workjob in kid1
            int[] agvIdKid1 = new int[workJobsKid1.size()];
            for (int i = 0; i < workJobsKid1.size(); i++) {
                agvIdKid1[i] = workJobsKid1.get(i).getAgv().getId();
            }

            int [] agvSolutionKID1Ids = agvSolutionKID1.stream().map(AGV::getId).mapToInt(Integer::intValue).toArray();

            //check if the agv of the kid are the same of the solution in the same exact order
            assertArrayEquals(agvIdKid1, agvSolutionKID1Ids);

            int[] solutionJobIdKID1 = {3, 6, 7, 1, 5, 8, 4, 2};
            int[] actualJObIdKID1 = new int[workJobsKid1.size()];
            //fill actual job id
            for (int i = 0; i < workJobsKid1.size(); i++) {
                actualJObIdKID1[i] = workJobsKid1.get(i).getJob().getId();
            }
            //check if the job id of the kid are the same of the solution in the same exact order
            assertArrayEquals(solutionJobIdKID1, actualJObIdKID1);


            List<AGV> agvSolutionKID2 = List.of(
                    new AGV(1, 5),
                    new AGV(1, 5),
                    new AGV(2, 5),
                    new AGV(1, 5),
                    new AGV(2, 5),
                    new AGV(1, 5),
                    new AGV(1, 5),
                    new AGV(1, 5));


            //extract all workjob from kid2
            List<AssignedJob> workJobsKid2 = kid2.getWorkJobs();
            //array of all agv id of workjob in kid2
            int[] agvIdKid2 = new int[workJobsKid2.size()];
            for (int i = 0; i < workJobsKid2.size(); i++) {
                agvIdKid2[i] = workJobsKid2.get(i).getAgv().getId();
            }

            int [] agvSolutionKID2Ids = agvSolutionKID2.stream().map(AGV::getId).mapToInt(Integer::intValue).toArray();

            //check if the agv of the kid are the same of the solution in the same exact order
            assertArrayEquals(agvIdKid2, agvSolutionKID2Ids);


            int[] solutionJobIdKID2 = {6, 1, 2, 8, 3, 7, 4, 5};
            int[] actualJObIdKID2 = new int[8];
            //fill actual job id
            for (int i = 0; i < workJobsKid2.size(); i++) {
                actualJObIdKID2[i] = workJobsKid2.get(i).getJob().getId();
            }
            //check if the job id of the kid are the same of the solution in the same exact order
            assertArrayEquals(solutionJobIdKID2, actualJObIdKID2);
        } catch (BatteryException e) {
            throw new RuntimeException(e);
        } catch (GAInconsistencyException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    void crossover2() {
        LinkedList<AssignedJob> dad_assignedJobs = new LinkedList<>();
        LinkedList<AssignedJob> mum_assignedJobs = new LinkedList<>();

        try {
            AGV agv1 = new AGV(1, Settings.getInstance().getBatteryCapacity());
            AGV agv2 = new AGV(2, Settings.getInstance().getBatteryCapacity());

            dad_assignedJobs.add(new AssignedJob(new WorkJob(2, 1, 4), agv2, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(5, 7, 3), agv1, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(8, 5, 2),  agv2, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(9, 1, 4),  agv2, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(11, 9, 5), agv2, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(3, 3, 2), agv1, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(7, 2, 2), agv1, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(10, 2, 2), agv1, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(4, 2, 2), agv2, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(1, 2, 2), agv2, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(12, 2, 2), agv1, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(6, 2, 2),  agv1, 0, 0));

            mum_assignedJobs.add(new AssignedJob(new WorkJob(3, 2, 1), agv1, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(8, 9, 5), agv1, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(9, 2, 1), agv1, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(11, 5, 2), agv2, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(10, 7, 3), agv2, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(2, 2, 2),  agv2, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(4, 2, 2), agv1, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(7, 2, 2), agv2, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(12, 2, 2), agv1, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(1, 2, 2),  agv1, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(5, 2, 2), agv2, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(6, 2, 2), agv2, 0, 0));

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
                    new AGV(2, 5),
                    new AGV(1, 5),
                    new AGV(2, 5),
                    new AGV(2, 5),
                    new AGV(2, 5),
                    new AGV(1, 5),

                    new AGV(1, 5),
                    new AGV(2, 5),
                    new AGV(1, 5),
                    new AGV(1, 5),
                    new AGV(2, 5),
                    new AGV(2, 5));

            List<AssignedJob> workJobsKid1 = kid1.getWorkJobs();
            //array of all agv id of workjob in kid1
            int[] agvIdKid1 = new int[workJobsKid1.size()];
            for (int i = 0; i < workJobsKid1.size(); i++) {
                agvIdKid1[i] = workJobsKid1.get(i).getAgv().getId();
            }

            int [] agvSolutionKID1Ids = agvSolutionKID1.stream().map(AGV::getId).mapToInt(Integer::intValue).toArray();

            //check if the agv of the kid are the same of the solution in the same exact order
            assertArrayEquals(agvIdKid1, agvSolutionKID1Ids);

            int[] solutionJobIdKID1 = {3, 5, 8, 9, 10, 2, 4, 7, 11, 1, 12, 6};
            int[] actualJObIdKID1 = new int[workJobsKid1.size()];
            //fill actual job id
            for (int i = 0; i < workJobsKid1.size(); i++) {
                actualJObIdKID1[i] = workJobsKid1.get(i).getJob().getId();
            }
            //print actualJobIdkID1
            System.out.println("SOLUTION KID 1");
            for (int i = 0; i < actualJObIdKID1.length; i++) {
                System.out.print(actualJObIdKID1[i] + ", ");
            }

            //check if the job id of the kid are the same of the solution in the same exact order
            assertArrayEquals(solutionJobIdKID1, actualJObIdKID1);

            List<AGV> agvSolutionKID2 = List.of(
                    new AGV(1, 5),
                    new AGV(1, 5),
                    new AGV(1, 5),
                    new AGV(2, 5),
                    new AGV(2, 5),
                    new AGV(2, 5),

                    new AGV(1, 5),
                    new AGV(1, 5),
                    new AGV(2, 5),
                    new AGV(2, 5),
                    new AGV(1, 5),
                    new AGV(1, 5));


            List<AssignedJob> workJobsKid2 = kid2.getWorkJobs();
            //array of all agv id of workjob in kid2
            int[] agvIdKid2 = new int[workJobsKid2.size()];
            for (int i = 0; i < workJobsKid2.size(); i++) {
                agvIdKid2[i] = workJobsKid2.get(i).getAgv().getId();
            }

            int [] agvSolutionKID2Ids = agvSolutionKID2.stream().map(AGV::getId).mapToInt(Integer::intValue).toArray();

            //check if the agv of the kid are the same of the solution in the same exact order
            assertArrayEquals(agvIdKid2, agvSolutionKID2Ids);

            int[] solutionJobIdKID2 = {2, 8, 9, 4, 11, 3, 7, 10, 12, 1, 5, 6};
            int[] actualJObIdKID2 = new int[workJobsKid2.size()];
            //fill actual job id
            for (int i = 0; i < workJobsKid2.size(); i++) {
                actualJObIdKID2[i] = workJobsKid2.get(i).getJob().getId();
            }

            //print actualJobIdkID2
            System.out.println("SOLUTION KID 2");
            for (int i = 0; i < actualJObIdKID2.length; i++) {
                System.out.print(actualJObIdKID2[i] + ", ");
            }
            //check if the job id of the kid are the same of the solution in the same exact order
            assertArrayEquals(solutionJobIdKID2, actualJObIdKID2);
        } catch (BatteryException e) {
            throw new RuntimeException(e);
        } catch (GAInconsistencyException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    public void testCrossoverCOMPLETE() {
        LinkedList<AssignedJob> dad_assignedJobs = new LinkedList<>();
        LinkedList<AssignedJob> mum_assignedJobs = new LinkedList<>();

        try {
            AGV agv1 = new AGV(1, Settings.getInstance().getBatteryCapacity());
            AGV agv2 = new AGV(2, Settings.getInstance().getBatteryCapacity());

            dad_assignedJobs.add(new AssignedJob(new WorkJob(3,2 ,2 ), agv2, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(5,5 ,4 ), agv2, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(2,3 ,3 ), agv1, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(1,4 ,2 ), agv2, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(4,1 ,1 ), agv1, 0, 0));
            dad_assignedJobs.add(new AssignedJob(new WorkJob(6,6 ,1 ), agv2, 0, 0));

            mum_assignedJobs.add(new AssignedJob(new WorkJob(2,3 ,3 ), agv1, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(6,6 ,1 ), agv1, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(1,4 ,2 ), agv1, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(3,2 ,2 ), agv2, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(5,5 ,4 ), agv2, 0, 0));
            mum_assignedJobs.add(new AssignedJob(new WorkJob(4,1 ,1 ), agv2, 0, 0));

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
                    new AGV(2, 5),
                    new AGV(2, 5),
                    new AGV(1, 5),
                    new AGV(2, 5),
                    new AGV(2, 5),
                    new AGV(2, 5));


            List<AssignedJob> workJobsKid1 = kid1.getWorkJobs();
            //array of all agv id of workjob in kid1
            int[] agvIdKid1 = new int[workJobsKid1.size()];
            for (int i = 0; i < workJobsKid1.size(); i++) {
                agvIdKid1[i] = workJobsKid1.get(i).getAgv().getId();
            }

            int [] agvSolutionKID1Ids = agvSolutionKID1.stream().map(AGV::getId).mapToInt(Integer::intValue).toArray();


            assertArrayEquals(agvIdKid1, agvSolutionKID1Ids);


            int[] solutionJobIdKID1 = {2,5,1,3,4,6};
            int[] actualJObIdKID1 = new int[workJobsKid1.size()];
            //fill actual job id
            for (int i = 0; i < workJobsKid1.size(); i++) {
                actualJObIdKID1[i] = workJobsKid1.get(i).getJob().getId();
            }
            //print actualJobIdkID1
            System.out.println("SOLUTION KID 1");
            for (int i = 0; i < actualJObIdKID1.length; i++) {
                System.out.print(actualJObIdKID1[i] + ", ");
            }

            //CHECK KID 2 now

            //check if the job id of the kid are the same of the solution in the same exact order
            assertArrayEquals(solutionJobIdKID1, actualJObIdKID1);

            List<AGV> agvSolutionKID2 = List.of(
                    new AGV(1, 5),
                    new AGV(1, 5),
                    new AGV(1, 5),
                    new AGV(2, 5),
                    new AGV(1, 5),
                    new AGV(2, 5));


            List<AssignedJob> workJobsKid2 = kid2.getWorkJobs();
            //array of all agv id of workjob in kid2
            int[] agvIdKid2 = new int[workJobsKid2.size()];
            for (int i = 0; i < workJobsKid2.size(); i++) {
                agvIdKid2[i] = workJobsKid2.get(i).getAgv().getId();
            }

            int [] agvSolutionKID2Ids = agvSolutionKID2.stream().map(AGV::getId).mapToInt(Integer::intValue).toArray();

            //check if the agv of the kid are the same of the solution in the same exact order
            assertArrayEquals(agvIdKid2, agvSolutionKID2Ids);

            int[] solutionJobIdKID2 = {3,6,2,1,5,4};
            int[] actualJObIdKID2 = new int[workJobsKid2.size()];
            //fill actual job id
            for (int i = 0; i < workJobsKid2.size(); i++) {
                actualJObIdKID2[i] = workJobsKid2.get(i).getJob().getId();
            }

            //print actualJobIdkID2
            System.out.println("SOLUTION KID 2");
            for (int i = 0; i < actualJObIdKID2.length; i++) {
                System.out.print(actualJObIdKID2[i] + ", ");
            }
            //check if the job id of the kid are the same of the solution in the same exact order
            assertArrayEquals(solutionJobIdKID2, actualJObIdKID2);

            int[] startTimes_kid1 = {0,6,0,15,17,20};
            int[] endTimes_kid1 = {3,11,4,17,18,26};

            //print all endtimes of kid1
            System.out.println("\n:END TIMES KID 1");
            for (int i = 0; i <  workJobsKid1.size(); i++) {
                System.out.print( workJobsKid1.get(i).getEndTime() + ", ");
            }
            System.out.println("\n----------------------");

            for (int i = 0; i < workJobsKid1.size(); i++) {
                assertEquals(startTimes_kid1[i], workJobsKid1.get(i).getStartTime());
                assertEquals(endTimes_kid1[i], workJobsKid1.get(i).getEndTime());
            }

            int[] startTimes_kid2 = {0,2,10,0,18,4};
            int[] endTimes_kid2 = {2,8,13,4,23,5};

            for (int i = 0; i < workJobsKid2.size(); i++) {
                assertEquals(startTimes_kid2[i], workJobsKid2.get(i).getStartTime());
                assertEquals(endTimes_kid2[i], workJobsKid2.get(i).getEndTime());
            }

        } catch (BatteryException e) {
            throw new RuntimeException(e);
        } catch (GAInconsistencyException e) {
            throw new RuntimeException(e);
        }
    }


    //test if clearReloads works
    @Test
    public void testClearReloads(){
        try {
            Individual individual = new Individual(2);
            individual.calculateReloads();
            assertTrue(individual.getReloadJobs().size() != 0);
            individual.clearReloads();
            assertTrue(individual.getReloadJobs().size() == 0);
        } catch (GAInconsistencyException | BatteryException e) {
            assertTrue(false);
        }

    }

}