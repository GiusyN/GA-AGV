/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.exceptions;

/**
 *
 * @author sommovir
 */
public class TooLowBatteryLevelException extends BatteryException{

    public TooLowBatteryLevelException(int newBatteryLevel) {
        super("La batteria non può scendere a numeri negativi: "+newBatteryLevel);
    }
}
