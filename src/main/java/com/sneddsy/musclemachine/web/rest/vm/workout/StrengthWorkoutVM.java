package com.sneddsy.musclemachine.web.rest.vm.workout;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;

public class StrengthWorkoutVM {

    @JsonProperty("exercise-id")
    private Long exerciseId;

    private ZonedDateTime time;

    @JsonProperty("training-sets")
    private List<TrainingSetVM> trainingSets;

    public StrengthWorkoutVM() {}

    public StrengthWorkoutVM(Long exerciseId, ZonedDateTime time, List<TrainingSetVM> trainingSets) {
        this.exerciseId = exerciseId;
        this.time = time;
        this.trainingSets = trainingSets;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public List<TrainingSetVM> getTrainingSets() {
        return trainingSets;
    }

    public void setTrainingSets(List<TrainingSetVM> trainingSets) {
        this.trainingSets = trainingSets;
    }

    @Override
    public String toString() {
        return "StrengthWorkoutVM{" + "exerciseId=" + exerciseId + ", time=" + time + ", trainingSets=" + trainingSets + '}';
    }
}
