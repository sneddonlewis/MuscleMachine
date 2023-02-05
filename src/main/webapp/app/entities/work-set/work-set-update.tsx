import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IExercise } from 'app/shared/model/exercise.model';
import { getEntities as getExercises } from 'app/entities/exercise/exercise.reducer';
import { IStrengthWorkout } from 'app/shared/model/strength-workout.model';
import { getEntities as getStrengthWorkouts } from 'app/entities/strength-workout/strength-workout.reducer';
import { IWorkSet } from 'app/shared/model/work-set.model';
import { getEntity, updateEntity, createEntity, reset } from './work-set.reducer';

export const WorkSetUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const exercises = useAppSelector(state => state.exercise.entities);
  const strengthWorkouts = useAppSelector(state => state.strengthWorkout.entities);
  const workSetEntity = useAppSelector(state => state.workSet.entity);
  const loading = useAppSelector(state => state.workSet.loading);
  const updating = useAppSelector(state => state.workSet.updating);
  const updateSuccess = useAppSelector(state => state.workSet.updateSuccess);

  const handleClose = () => {
    navigate('/work-set');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getExercises({}));
    dispatch(getStrengthWorkouts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...workSetEntity,
      ...values,
      exercise: exercises.find(it => it.id.toString() === values.exercise.toString()),
      strengthWorkout: strengthWorkouts.find(it => it.id.toString() === values.strengthWorkout.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...workSetEntity,
          exercise: workSetEntity?.exercise?.id,
          strengthWorkout: workSetEntity?.strengthWorkout?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="muscleMachineApp.workSet.home.createOrEditLabel" data-cy="WorkSetCreateUpdateHeading">
            Create or edit a Work Set
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="work-set-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Set Number"
                id="work-set-setNumber"
                name="setNumber"
                data-cy="setNumber"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Repetitions" id="work-set-repetitions" name="repetitions" data-cy="repetitions" type="text" />
              <ValidatedField
                label="Time Under Load"
                id="work-set-timeUnderLoad"
                name="timeUnderLoad"
                data-cy="timeUnderLoad"
                type="text"
              />
              <ValidatedField
                label="Band Resistance"
                id="work-set-bandResistance"
                name="bandResistance"
                data-cy="bandResistance"
                type="text"
              />
              <ValidatedField
                label="Cable Resistance"
                id="work-set-cableResistance"
                name="cableResistance"
                data-cy="cableResistance"
                type="text"
              />
              <ValidatedField
                label="Free Weight Resistance"
                id="work-set-freeWeightResistance"
                name="freeWeightResistance"
                data-cy="freeWeightResistance"
                type="text"
              />
              <ValidatedField id="work-set-exercise" name="exercise" data-cy="exercise" label="Exercise" type="select">
                <option value="" key="0" />
                {exercises
                  ? exercises.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="work-set-strengthWorkout"
                name="strengthWorkout"
                data-cy="strengthWorkout"
                label="Strength Workout"
                type="select"
              >
                <option value="" key="0" />
                {strengthWorkouts
                  ? strengthWorkouts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/work-set" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default WorkSetUpdate;
