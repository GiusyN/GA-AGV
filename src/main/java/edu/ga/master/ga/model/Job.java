/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model;

/**
 *
 * @author sommovir
 */
public sealed interface Job permits WorkJob,ReloadJob{
    
    public int getId();
    public int getTime();
    public int getEnergy();
    
}
