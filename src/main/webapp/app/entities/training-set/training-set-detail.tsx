import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './training-set.reducer';

export const TrainingSetDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const trainingSetEntity = useAppSelector(state => state.trainingSet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="trainingSetDetailsHeading">Training Set</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{trainingSetEntity.id}</dd>
          <dt>
            <span id="setNumber">Set Number</span>
          </dt>
          <dd>{trainingSetEntity.setNumber}</dd>
          <dt>
            <span id="repetitions">Repetitions</span>
          </dt>
          <dd>{trainingSetEntity.repetitions}</dd>
          <dt>
            <span id="timeUnderLoad">Time Under Load</span>
          </dt>
          <dd>{trainingSetEntity.timeUnderLoad}</dd>
          <dt>Resistance</dt>
          <dd>{trainingSetEntity.resistance ? trainingSetEntity.resistance.id : ''}</dd>
          <dt>Strength Workout</dt>
          <dd>{trainingSetEntity.strengthWorkout ? trainingSetEntity.strengthWorkout.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/training-set" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/training-set/${trainingSetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TrainingSetDetail;
