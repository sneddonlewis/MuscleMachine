import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStrengthWorkout } from 'app/shared/model/strength-workout.model';
import { getEntities } from './strength-workout.reducer';

export const StrengthWorkout = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const strengthWorkoutList = useAppSelector(state => state.strengthWorkout.entities);
  const loading = useAppSelector(state => state.strengthWorkout.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="strength-workout-heading" data-cy="StrengthWorkoutHeading">
        Strength Workouts
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/strength-workout/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Strength Workout
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {strengthWorkoutList && strengthWorkoutList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Time</th>
                <th>Exercise</th>
                <th>User</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {strengthWorkoutList.map((strengthWorkout, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/strength-workout/${strengthWorkout.id}`} color="link" size="sm">
                      {strengthWorkout.id}
                    </Button>
                  </td>
                  <td>{strengthWorkout.time ? <TextFormat type="date" value={strengthWorkout.time} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {strengthWorkout.exercise ? (
                      <Link to={`/exercise/${strengthWorkout.exercise.id}`}>{strengthWorkout.exercise.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{strengthWorkout.user ? strengthWorkout.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/strength-workout/${strengthWorkout.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/strength-workout/${strengthWorkout.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/strength-workout/${strengthWorkout.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Strength Workouts found</div>
        )}
      </div>
    </div>
  );
};

export default StrengthWorkout;
