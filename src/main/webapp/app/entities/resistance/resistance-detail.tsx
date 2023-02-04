import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './resistance.reducer';

export const ResistanceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const resistanceEntity = useAppSelector(state => state.resistance.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="resistanceDetailsHeading">Resistance</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{resistanceEntity.id}</dd>
          <dt>
            <span id="band">Band</span>
          </dt>
          <dd>{resistanceEntity.band}</dd>
          <dt>
            <span id="cable">Cable</span>
          </dt>
          <dd>{resistanceEntity.cable}</dd>
          <dt>
            <span id="freeWeight">Free Weight</span>
          </dt>
          <dd>{resistanceEntity.freeWeight}</dd>
        </dl>
        <Button tag={Link} to="/resistance" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/resistance/${resistanceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ResistanceDetail;
