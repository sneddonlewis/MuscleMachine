import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITrainingSet } from 'app/shared/model/training-set.model';
import { getEntities } from './training-set.reducer';

export const TrainingSet = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const trainingSetList = useAppSelector(state => state.trainingSet.entities);
  const loading = useAppSelector(state => state.trainingSet.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="training-set-heading" data-cy="TrainingSetHeading">
        Training Sets
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/training-set/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Training Set
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {trainingSetList && trainingSetList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Set Number</th>
                <th>Repetitions</th>
                <th>Time Under Load</th>
                <th>Resistance</th>
                <th>Strength Workout</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {trainingSetList.map((trainingSet, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/training-set/${trainingSet.id}`} color="link" size="sm">
                      {trainingSet.id}
                    </Button>
                  </td>
                  <td>{trainingSet.setNumber}</td>
                  <td>{trainingSet.repetitions}</td>
                  <td>{trainingSet.timeUnderLoad}</td>
                  <td>
                    {trainingSet.resistance ? <Link to={`/resistance/${trainingSet.resistance.id}`}>{trainingSet.resistance.id}</Link> : ''}
                  </td>
                  <td>
                    {trainingSet.strengthWorkout ? (
                      <Link to={`/strength-workout/${trainingSet.strengthWorkout.id}`}>{trainingSet.strengthWorkout.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/training-set/${trainingSet.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/training-set/${trainingSet.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/training-set/${trainingSet.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Training Sets found</div>
        )}
      </div>
    </div>
  );
};

export default TrainingSet;
