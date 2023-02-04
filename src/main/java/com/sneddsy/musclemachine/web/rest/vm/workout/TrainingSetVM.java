package com.sneddsy.musclemachine.web.rest.vm.workout;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrainingSetVM {

    @JsonProperty("set-number")
    private int setNumber;

    private Integer repetitions;

    @JsonProperty("time-under-load")
    private Integer timeUnderLoad;

    private ResistanceVM resistance;

    public TrainingSetVM() {}

    public TrainingSetVM(int setNumber, Integer repetitions, Integer timeUnderLoad, ResistanceVM resistance) {
        this.setNumber = setNumber;
        this.repetitions = repetitions;
        this.timeUnderLoad = timeUnderLoad;
        this.resistance = resistance;
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

    public ResistanceVM getResistance() {
        return resistance;
    }

    public void setResistance(ResistanceVM resistance) {
        this.resistance = resistance;
    }

    @Override
    public String toString() {
        return (
            "TrainingSetVM{" +
            "setNumber=" +
            setNumber +
            ", repetitions=" +
            repetitions +
            ", timeUnderLoad=" +
            timeUnderLoad +
            ", resistance=" +
            resistance +
            '}'
        );
    }
}
