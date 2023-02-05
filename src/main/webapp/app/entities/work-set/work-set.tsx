import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IWorkSet } from 'app/shared/model/work-set.model';
import { getEntities } from './work-set.reducer';

export const WorkSet = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const workSetList = useAppSelector(state => state.workSet.entities);
  const loading = useAppSelector(state => state.workSet.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="work-set-heading" data-cy="WorkSetHeading">
        Work Sets
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/work-set/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Work Set
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {workSetList && workSetList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Set Number</th>
                <th>Repetitions</th>
                <th>Time Under Load</th>
                <th>Band Resistance</th>
                <th>Cable Resistance</th>
                <th>Free Weight Resistance</th>
                <th>Exercise</th>
                <th>Strength Workout</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {workSetList.map((workSet, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/work-set/${workSet.id}`} color="link" size="sm">
                      {workSet.id}
                    </Button>
                  </td>
                  <td>{workSet.setNumber}</td>
                  <td>{workSet.repetitions}</td>
                  <td>{workSet.timeUnderLoad}</td>
                  <td>{workSet.bandResistance}</td>
                  <td>{workSet.cableResistance}</td>
                  <td>{workSet.freeWeightResistance}</td>
                  <td>{workSet.exercise ? <Link to={`/exercise/${workSet.exercise.id}`}>{workSet.exercise.id}</Link> : ''}</td>
                  <td>
                    {workSet.strengthWorkout ? (
                      <Link to={`/strength-workout/${workSet.strengthWorkout.id}`}>{workSet.strengthWorkout.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/work-set/${workSet.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/work-set/${workSet.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/work-set/${workSet.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Work Sets found</div>
        )}
      </div>
    </div>
  );
};

export default WorkSet;
