import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StrengthWorkout from './strength-workout';
import WorkSet from './work-set';
import Exercise from './exercise';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="strength-workout/*" element={<StrengthWorkout />} />
        <Route path="work-set/*" element={<WorkSet />} />
        <Route path="exercise/*" element={<Exercise />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
