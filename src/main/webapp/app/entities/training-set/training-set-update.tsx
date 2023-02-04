import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IResistance } from 'app/shared/model/resistance.model';
import { getEntities as getResistances } from 'app/entities/resistance/resistance.reducer';
import { IStrengthWorkout } from 'app/shared/model/strength-workout.model';
import { getEntities as getStrengthWorkouts } from 'app/entities/strength-workout/strength-workout.reducer';
import { ITrainingSet } from 'app/shared/model/training-set.model';
import { getEntity, updateEntity, createEntity, reset } from './training-set.reducer';

export const TrainingSetUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const resistances = useAppSelector(state => state.resistance.entities);
  const strengthWorkouts = useAppSelector(state => state.strengthWorkout.entities);
  const trainingSetEntity = useAppSelector(state => state.trainingSet.entity);
  const loading = useAppSelector(state => state.trainingSet.loading);
  const updating = useAppSelector(state => state.trainingSet.updating);
  const updateSuccess = useAppSelector(state => state.trainingSet.updateSuccess);

  const handleClose = () => {
    navigate('/training-set');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getResistances({}));
    dispatch(getStrengthWorkouts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...trainingSetEntity,
      ...values,
      resistance: resistances.find(it => it.id.toString() === values.resistance.toString()),
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
          ...trainingSetEntity,
          resistance: trainingSetEntity?.resistance?.id,
          strengthWorkout: trainingSetEntity?.strengthWorkout?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="muscleMachineApp.trainingSet.home.createOrEditLabel" data-cy="TrainingSetCreateUpdateHeading">
            Create or edit a Training Set
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="training-set-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Set Number"
                id="training-set-setNumber"
                name="setNumber"
                data-cy="setNumber"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Repetitions" id="training-set-repetitions" name="repetitions" data-cy="repetitions" type="text" />
              <ValidatedField
                label="Time Under Load"
                id="training-set-timeUnderLoad"
                name="timeUnderLoad"
                data-cy="timeUnderLoad"
                type="text"
              />
              <ValidatedField id="training-set-resistance" name="resistance" data-cy="resistance" label="Resistance" type="select">
                <option value="" key="0" />
                {resistances
                  ? resistances.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="training-set-strengthWorkout"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/training-set" replace color="info">
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

export default TrainingSetUpdate;
