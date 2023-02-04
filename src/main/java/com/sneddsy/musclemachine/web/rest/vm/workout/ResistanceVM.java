package com.sneddsy.musclemachine.web.rest.vm.workout;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResistanceVM {

    private float band = 0.0f;
    private float cable = 0.0f;

    @JsonProperty("free-weight")
    private float freeWeight = 0.0f;

    public ResistanceVM() {}

    public ResistanceVM(float band, float cable, float freeWeight) {
        this.band = band;
        this.cable = cable;
        this.freeWeight = freeWeight;
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
        return "ResistanceVM{" + "band=" + band + ", cable=" + cable + ", freeWeight=" + freeWeight + '}';
    }
}
