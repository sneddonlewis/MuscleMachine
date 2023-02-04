import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITrainingSet } from 'app/shared/model/training-set.model';
import { getEntities as getTrainingSets } from 'app/entities/training-set/training-set.reducer';
import { IResistance } from 'app/shared/model/resistance.model';
import { getEntity, updateEntity, createEntity, reset } from './resistance.reducer';

export const ResistanceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const trainingSets = useAppSelector(state => state.trainingSet.entities);
  const resistanceEntity = useAppSelector(state => state.resistance.entity);
  const loading = useAppSelector(state => state.resistance.loading);
  const updating = useAppSelector(state => state.resistance.updating);
  const updateSuccess = useAppSelector(state => state.resistance.updateSuccess);

  const handleClose = () => {
    navigate('/resistance');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTrainingSets({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...resistanceEntity,
      ...values,
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
          ...resistanceEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="muscleMachineApp.resistance.home.createOrEditLabel" data-cy="ResistanceCreateUpdateHeading">
            Create or edit a Resistance
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="resistance-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Band" id="resistance-band" name="band" data-cy="band" type="text" />
              <ValidatedField label="Cable" id="resistance-cable" name="cable" data-cy="cable" type="text" />
              <ValidatedField label="Free Weight" id="resistance-freeWeight" name="freeWeight" data-cy="freeWeight" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/resistance" replace color="info">
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

export default ResistanceUpdate;
