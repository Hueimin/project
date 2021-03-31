package com.example.fireproject01.data;

import java.util.ArrayList;

public class Storage {

    private ArrayList<Float> maxFingerAngle, maxFingerSpeed, maxMpuAngle, minFingerAngle, minMpuAngle, maxMpuSpeed;

    private Storage() {}

    public ArrayList<Float> getMaxFingerAngle() {
        return maxFingerAngle;
    }

    public void setMaxFingerAngle(ArrayList<Float> maxFingerAngle) {
        this.maxFingerAngle = maxFingerAngle;
    }

    public ArrayList<Float> getMaxFingerSpeed() {
        return maxFingerSpeed;
    }

    public void setMaxFingerSpeed(ArrayList<Float> maxFingerSpeed) {
        this.maxFingerSpeed = maxFingerSpeed;
    }

    public ArrayList<Float> getMaxMpuAngle() {
        return maxMpuAngle;
    }

    public void setMaxMpuAngle(ArrayList<Float> maxMpuAngle) {
        this.maxMpuAngle = maxMpuAngle;
    }

    public ArrayList<Float> getMinFingerAngle() {
        return minFingerAngle;
    }

    public void setMinFingerAngle(ArrayList<Float> minFingerAngle) {
        this.minFingerAngle = minFingerAngle;
    }

    public ArrayList<Float> getMinMpuAngle() {
        return minMpuAngle;
    }

    public void setMinMpuAngle(ArrayList<Float> minMpuAngle) {
        this.minMpuAngle = minMpuAngle;
    }

    public ArrayList<Float> getMaxMpuSpeed() {
        return maxMpuSpeed;
    }

    public void setMaxMpuSpeed(ArrayList<Float> maxMpuSpeed) {
        this.maxMpuSpeed = maxMpuSpeed;
    }
}
