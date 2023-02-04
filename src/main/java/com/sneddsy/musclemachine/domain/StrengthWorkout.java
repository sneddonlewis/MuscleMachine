package com.sneddsy.musclemachine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StrengthWorkout.
 */
@Entity
@Table(name = "strength_workout")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StrengthWorkout implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "time", nullable = false)
    private ZonedDateTime time;

    @JsonIgnoreProperties(value = { "strengthWorkout" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Exercise exercise;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "strengthWorkout")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "resistance", "strengthWorkout" }, allowSetters = true)
    private Set<TrainingSet> trainingSets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StrengthWorkout id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTime() {
        return this.time;
    }

    public StrengthWorkout time(ZonedDateTime time) {
        this.setTime(time);
        return this;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public Exercise getExercise() {
        return this.exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public StrengthWorkout exercise(Exercise exercise) {
        this.setExercise(exercise);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StrengthWorkout user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<TrainingSet> getTrainingSets() {
        return this.trainingSets;
    }

    public void setTrainingSets(Set<TrainingSet> trainingSets) {
        if (this.trainingSets != null) {
            this.trainingSets.forEach(i -> i.setStrengthWorkout(null));
        }
        if (trainingSets != null) {
            trainingSets.forEach(i -> i.setStrengthWorkout(this));
        }
        this.trainingSets = trainingSets;
    }

    public StrengthWorkout trainingSets(Set<TrainingSet> trainingSets) {
        this.setTrainingSets(trainingSets);
        return this;
    }

    public StrengthWorkout addTrainingSet(TrainingSet trainingSet) {
        this.trainingSets.add(trainingSet);
        trainingSet.setStrengthWorkout(this);
        return this;
    }

    public StrengthWorkout removeTrainingSet(TrainingSet trainingSet) {
        this.trainingSets.remove(trainingSet);
        trainingSet.setStrengthWorkout(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StrengthWorkout)) {
            return false;
        }
        return id != null && id.equals(((StrengthWorkout) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StrengthWorkout{" +
            "id=" + getId() +
            ", time='" + getTime() + "'" +
            "}";
    }
}
