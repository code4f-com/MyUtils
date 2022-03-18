/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuanpla.utils.common;

/**
 *
 * @author TUANPLA
 */
public class TpsCounter {

    int counter;
    long startTime;
    long currentTime;
    int tps;

    public TpsCounter(int tps) {
        startTime = System.currentTimeMillis();
        this.tps = tps;
    }

    public long currentTps() {
        long distance = currentTime - startTime;
        if (distance <= 1000) {
            return (tps * distance) / 1000;
        } else {
            startTime = System.currentTimeMillis();
            distance = distance - 1000;
            return (tps * distance) / 1000;
        }

    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void setCounter() {
        this.counter = counter++;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

}
