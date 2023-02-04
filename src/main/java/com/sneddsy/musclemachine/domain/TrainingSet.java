package com.sneddsy.musclemachine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TrainingSet.
 */
@Entity
@Table(name = "training_set")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrainingSet implements Serializable {

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

    @JsonIgnoreProperties(value = { "trainingSet" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Resistance resistance;

    @ManyToOne
    @JsonIgnoreProperties(value = { "exercise", "user", "trainingSets" }, allowSetters = true)
    private StrengthWorkout strengthWorkout;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TrainingSet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSetNumber() {
        return this.setNumber;
    }

    public TrainingSet setNumber(Integer setNumber) {
        this.setSetNumber(setNumber);
        return this;
    }

    public void setSetNumber(Integer setNumber) {
        this.setNumber = setNumber;
    }

    public Integer getRepetitions() {
        return this.repetitions;
    }

    public TrainingSet repetitions(Integer repetitions) {
        this.setRepetitions(repetitions);
        return this;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public Integer getTimeUnderLoad() {
        return this.timeUnderLoad;
    }

    public TrainingSet timeUnderLoad(Integer timeUnderLoad) {
        this.setTimeUnderLoad(timeUnderLoad);
        return this;
    }

    public void setTimeUnderLoad(Integer timeUnderLoad) {
        this.timeUnderLoad = timeUnderLoad;
    }

    public Resistance getResistance() {
        return this.resistance;
    }

    public void setResistance(Resistance resistance) {
        this.resistance = resistance;
    }

    public TrainingSet resistance(Resistance resistance) {
        this.setResistance(resistance);
        return this;
    }

    public StrengthWorkout getStrengthWorkout() {
        return this.strengthWorkout;
    }

    public void setStrengthWorkout(StrengthWorkout strengthWorkout) {
        this.strengthWorkout = strengthWorkout;
    }

    public TrainingSet strengthWorkout(StrengthWorkout strengthWorkout) {
        this.setStrengthWorkout(strengthWorkout);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrainingSet)) {
            return false;
        }
        return id != null && id.equals(((TrainingSet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingSet{" +
            "id=" + getId() +
            ", setNumber=" + getSetNumber() +
            ", repetitions=" + getRepetitions() +
            ", timeUnderLoad=" + getTimeUnderLoad() +
            "}";
    }
}
