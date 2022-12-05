/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ga.master.ga.exceptions;

import edu.ga.master.ga.utils.Settings;

/**
 *
 * @author sommovir
 */
public class OverCapacityBatteryLevelException extends BatteryException{

    public OverCapacityBatteryLevelException(int agv_id, int capacity) {
        super("[AGV "+agv_id+"] La batteria non può superare [tua capacity: "+capacity+"]  la capacità: "+Settings.getInstance().getBatteryCapacity());
    }
}
