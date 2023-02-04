package com.sneddsy.musclemachine.web.rest.vm.workout;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkSetVM {

    @JsonProperty("set-number")
    private int setNumber;

    private Integer repetitions;

    @JsonProperty("time-under-load")
    private Integer timeUnderLoad;

    private float band = 0.0f;
    private float cable = 0.0f;

    @JsonProperty("free-weight")
    private float freeWeight = 0.0f;

    public WorkSetVM() {}

    public WorkSetVM(int setNumber, Integer repetitions, Integer timeUnderLoad, float band, float cable, float freeWeight) {
        this.setNumber = setNumber;
        this.repetitions = repetitions;
        this.timeUnderLoad = timeUnderLoad;
        this.band = band;
        this.cable = cable;
        this.freeWeight = freeWeight;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public Integer getTimeUnderLoad() {
        return timeUnderLoad;
    }

    public void setTimeUnderLoad(Integer timeUnderLoad) {
        this.timeUnderLoad = timeUnderLoad;
    }

    public float getBand() {
        return band;
    }

    public void setBand(float band) {
        this.band = band;
    }

    public float getCable() {
        return cable;
    }

    public void setCable(float cable) {
        this.cable = cable;
    }

    public float getFreeWeight() {
        return freeWeight;
    }

    public void setFreeWeight(float freeWeight) {
        this.freeWeight = freeWeight;
    }

    @Override
    public String toString() {
        return (
            "WorkSetVM{" +
            "setNumber=" +
            setNumber +
            ", repetitions=" +
            repetitions +
            ", timeUnderLoad=" +
            timeUnderLoad +
            ", band=" +
            band +
            ", cable=" +
            cable +
            ", freeWeight=" +
            freeWeight +
            '}'
        );
    }
}
