/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model;

/**
 *
 * @author sommovir
 */
public class AssignedJob {
    
    private Job job;
    private AGV agv;
    private int startTime;
    private int endTime;

    public AssignedJob() {
    }

    public AssignedJob(Job job, AGV agv, int startTime, int endTime) {
        this.job = job;
        this.agv = agv;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public AGV getAgv() {
        return agv;
    }

    public void setAgv(AGV agv) {
        this.agv = agv;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        String type = this.job instanceof ReloadJob ? "RELOAD" : "WORK";
        return "AssignedJob{" + "job=" + job + "["+type+"], \tagv = " + agv + ", startTime=" + startTime + ", endTime=" + endTime + '}';
    }


    //clone method
    public AssignedJob clone(){
        return new AssignedJob(this.job, this.agv, this.startTime, this.endTime);
    }
}
