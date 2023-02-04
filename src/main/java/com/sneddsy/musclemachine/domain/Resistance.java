package com.sneddsy.musclemachine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Resistance.
 */
@Entity
@Table(name = "resistance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Resistance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "band")
    private Float band;

    @Column(name = "cable")
    private Float cable;

    @Column(name = "free_weight")
    private Float freeWeight;

    @JsonIgnoreProperties(value = { "resistance", "strengthWorkout" }, allowSetters = true)
    @OneToOne(mappedBy = "resistance")
    private TrainingSet trainingSet;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Resistance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getBand() {
        return this.band;
    }

    public Resistance band(Float band) {
        this.setBand(band);
        return this;
    }

    public void setBand(Float band) {
        this.band = band;
    }

    public Float getCable() {
        return this.cable;
    }

    public Resistance cable(Float cable) {
        this.setCable(cable);
        return this;
    }

    public void setCable(Float cable) {
        this.cable = cable;
    }

    public Float getFreeWeight() {
        return this.freeWeight;
    }

    public Resistance freeWeight(Float freeWeight) {
        this.setFreeWeight(freeWeight);
        return this;
    }

    public void setFreeWeight(Float freeWeight) {
        this.freeWeight = freeWeight;
    }

    public TrainingSet getTrainingSet() {
        return this.trainingSet;
    }

    public void setTrainingSet(TrainingSet trainingSet) {
        if (this.trainingSet != null) {
            this.trainingSet.setResistance(null);
        }
        if (trainingSet != null) {
            trainingSet.setResistance(this);
        }
        this.trainingSet = trainingSet;
    }

    public Resistance trainingSet(TrainingSet trainingSet) {
        this.setTrainingSet(trainingSet);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resistance)) {
            return false;
        }
        return id != null && id.equals(((Resistance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Resistance{" +
            "id=" + getId() +
            ", band=" + getBand() +
            ", cable=" + getCable() +
            ", freeWeight=" + getFreeWeight() +
            "}";
    }
}
