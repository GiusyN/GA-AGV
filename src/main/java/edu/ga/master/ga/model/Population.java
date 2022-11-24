/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model;

/**
 *
 * @author sommovir
 */
public class Population {

    private int size;
    private Individual[] individuals;
    private int minimumAGV;
    private int maximumAGV;
    public static DISTRIBUTION distribution;

    public enum DISTRIBUTION {
        EQUAL, RANDOM
    };

//    public Population(int size) {
//        this.size = size;
//        individuals = new Individual[size];
//    }
    
    private Population(Builder builder){
        this.size = builder.size;
        this.minimumAGV = builder.minimumAGV;
        this.maximumAGV = builder.maximumAGV;
        this.distribution = builder.distribution;
        individuals = new Individual[size];
        if(distribution==DISTRIBUTION.EQUAL){
            int howMany = (this.maximumAGV - this.minimumAGV) / size;
            
        }else{
            //TODO
        }
    }

    public static class Builder {

        private int size;
        private int minimumAGV = 1;
        private int maximumAGV = 5;
        private DISTRIBUTION distribution = DISTRIBUTION.EQUAL;  


        public Builder(int size) {
            this.size = size;
        }

        public Builder minimumAGV(int minimumAGV) {
            this.minimumAGV = minimumAGV;
            return this;
        }

        public Builder maximumAGV(int maximumAGV) {
            this.maximumAGV = maximumAGV;
            return this;
        }

        public Builder distribution(DISTRIBUTION distribution) {
            this.distribution = distribution;
            return this;
        }
        
        public Population build(){
            return new Population(this);
        }
        
        
    }
}
