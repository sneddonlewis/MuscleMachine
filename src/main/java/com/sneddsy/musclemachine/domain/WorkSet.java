package com.sneddsy.musclemachine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkSet.
 */
@Entity
@Table(name = "work_set")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Float bandResistance;

    @Column(name = "cable_resistance")
    private Float cableResistance;

    @Column(name = "free_weight_resistance")
    private Float freeWeightResistance;

    @ManyToOne
    @JsonIgnoreProperties(value = { "exercise", "user", "trainingSets", "workSets" }, allowSetters = true)
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

    public Float getBandResistance() {
        return this.bandResistance;
    }

    public WorkSet bandResistance(Float bandResistance) {
        this.setBandResistance(bandResistance);
        return this;
    }

    public void setBandResistance(Float bandResistance) {
        this.bandResistance = bandResistance;
    }

    public Float getCableResistance() {
        return this.cableResistance;
    }

    public WorkSet cableResistance(Float cableResistance) {
        this.setCableResistance(cableResistance);
        return this;
    }

    public void setCableResistance(Float cableResistance) {
        this.cableResistance = cableResistance;
    }

    public Float getFreeWeightResistance() {
        return this.freeWeightResistance;
    }

    public WorkSet freeWeightResistance(Float freeWeightResistance) {
        this.setFreeWeightResistance(freeWeightResistance);
        return this;
    }

    public void setFreeWeightResistance(Float freeWeightResistance) {
        this.freeWeightResistance = freeWeightResistance;
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
