package com.sneddsy.muscle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A WorkSet.
 */
@Entity
@Table(name = "work_set")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    @Column(name = "repetitions")
    private Integer repetitions;

    @Column(name = "time_under_load")
    private Integer timeUnderLoad;

    @Column(name = "band_resistance")
    private Double bandResistance;

    @Column(name = "cable_resistance")
    private Double cableResistance;

    @Column(name = "free_weight_resistance")
    private Double freeWeightResistance;

    @OneToOne
    @JoinColumn(unique = true)
    private Exercise exercise;

    @ManyToOne
    @JsonIgnoreProperties(value = { "workSets", "user" }, allowSetters = true)
    private StrengthWorkout strengthWorkout;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkSet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSetNumber() {
        return this.setNumber;
    }

    public WorkSet setNumber(Integer setNumber) {
        this.setSetNumber(setNumber);
        return this;
    }

    public void setSetNumber(Integer setNumber) {
        this.setNumber = setNumber;
    }

    public Integer getRepetitions() {
        return this.repetitions;
    }

    public WorkSet repetitions(Integer repetitions) {
        this.setRepetitions(repetitions);
        return this;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public Integer getTimeUnderLoad() {
        return this.timeUnderLoad;
    }

    public WorkSet timeUnderLoad(Integer timeUnderLoad) {
        this.setTimeUnderLoad(timeUnderLoad);
        return this;
    }

    public void setTimeUnderLoad(Integer timeUnderLoad) {
        this.timeUnderLoad = timeUnderLoad;
    }

    public Double getBandResistance() {
        return this.bandResistance;
    }

    public WorkSet bandResistance(Double bandResistance) {
        this.setBandResistance(bandResistance);
        return this;
    }

    public void setBandResistance(Double bandResistance) {
        this.bandResistance = bandResistance;
    }

    public Double getCableResistance() {
        return this.cableResistance;
    }

    public WorkSet cableResistance(Double cableResistance) {
        this.setCableResistance(cableResistance);
        return this;
    }

    public void setCableResistance(Double cableResistance) {
        this.cableResistance = cableResistance;
    }

    public Double getFreeWeightResistance() {
        return this.freeWeightResistance;
    }

    public WorkSet freeWeightResistance(Double freeWeightResistance) {
        this.setFreeWeightResistance(freeWeightResistance);
        return this;
    }

    public void setFreeWeightResistance(Double freeWeightResistance) {
        this.freeWeightResistance = freeWeightResistance;
    }

    public Exercise getExercise() {
        return this.exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public WorkSet exercise(Exercise exercise) {
        this.setExercise(exercise);
        return this;
    }

    public StrengthWorkout getStrengthWorkout() {
        return this.strengthWorkout;
    }

    public void setStrengthWorkout(StrengthWorkout strengthWorkout) {
        this.strengthWorkout = strengthWorkout;
    }

    public WorkSet strengthWorkout(StrengthWorkout strengthWorkout) {
        this.setStrengthWorkout(strengthWorkout);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkSet)) {
            return false;
        }
        return id != null && id.equals(((WorkSet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkSet{" +
            "id=" + getId() +
            ", setNumber=" + getSetNumber() +
            ", repetitions=" + getRepetitions() +
            ", timeUnderLoad=" + getTimeUnderLoad() +
            ", bandResistance=" + getBandResistance() +
            ", cableResistance=" + getCableResistance() +
            ", freeWeightResistance=" + getFreeWeightResistance() +
            "}";
    }
}
