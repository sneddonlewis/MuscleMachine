import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './strength-workout.reducer';

export const StrengthWorkoutDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const strengthWorkoutEntity = useAppSelector(state => state.strengthWorkout.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="strengthWorkoutDetailsHeading">Strength Workout</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{strengthWorkoutEntity.id}</dd>
          <dt>
            <span id="time">Time</span>
          </dt>
          <dd>
            {strengthWorkoutEntity.time ? <TextFormat value={strengthWorkoutEntity.time} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>User</dt>
          <dd>{strengthWorkoutEntity.user ? strengthWorkoutEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/strength-workout" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/strength-workout/${strengthWorkoutEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default StrengthWorkoutDetail;
