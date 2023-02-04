import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StrengthWorkout from './strength-workout';
import Exercise from './exercise';
import TrainingSet from './training-set';
import Resistance from './resistance';
import WorkSet from './work-set';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="strength-workout/*" element={<StrengthWorkout />} />
        <Route path="exercise/*" element={<Exercise />} />
        <Route path="training-set/*" element={<TrainingSet />} />
        <Route path="resistance/*" element={<Resistance />} />
        <Route path="work-set/*" element={<WorkSet />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
