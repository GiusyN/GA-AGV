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
    private int agv;
    private int startTime;
    private int endTime;

    public AssignedJob() {
    }

    public AssignedJob(Job job, int agv, int startTime, int endTime) {
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

    public int getAgv() {
        return agv;
    }

    public void setAgv(int agv) {
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
        return "AssignedJob{" + "job=" + job + ", agv=" + agv + ", startTime=" + startTime + ", endTime=" + endTime + '}';
    }
   
}
