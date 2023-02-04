import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './work-set.reducer';

export const WorkSetDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const workSetEntity = useAppSelector(state => state.workSet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="workSetDetailsHeading">Work Set</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{workSetEntity.id}</dd>
          <dt>
            <span id="setNumber">Set Number</span>
          </dt>
          <dd>{workSetEntity.setNumber}</dd>
          <dt>
            <span id="repetitions">Repetitions</span>
          </dt>
          <dd>{workSetEntity.repetitions}</dd>
          <dt>
            <span id="timeUnderLoad">Time Under Load</span>
          </dt>
          <dd>{workSetEntity.timeUnderLoad}</dd>
          <dt>
            <span id="bandResistance">Band Resistance</span>
          </dt>
          <dd>{workSetEntity.bandResistance}</dd>
          <dt>
            <span id="cableResistance">Cable Resistance</span>
          </dt>
          <dd>{workSetEntity.cableResistance}</dd>
          <dt>
            <span id="freeWeightResistance">Free Weight Resistance</span>
          </dt>
          <dd>{workSetEntity.freeWeightResistance}</dd>
          <dt>Strength Workout</dt>
          <dd>{workSetEntity.strengthWorkout ? workSetEntity.strengthWorkout.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/work-set" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/work-set/${workSetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default WorkSetDetail;
