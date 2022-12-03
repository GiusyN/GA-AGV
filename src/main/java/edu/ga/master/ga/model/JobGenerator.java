/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.model;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sommovir
 */
public interface JobGenerator {

    public LinkedList<WorkJob> generate(int n);

}
