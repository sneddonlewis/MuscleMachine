import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StrengthWorkout from './strength-workout';
import StrengthWorkoutDetail from './strength-workout-detail';
import StrengthWorkoutUpdate from './strength-workout-update';
import StrengthWorkoutDeleteDialog from './strength-workout-delete-dialog';

const StrengthWorkoutRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StrengthWorkout />} />
    <Route path="new" element={<StrengthWorkoutUpdate />} />
    <Route path=":id">
      <Route index element={<StrengthWorkoutDetail />} />
      <Route path="edit" element={<StrengthWorkoutUpdate />} />
      <Route path="delete" element={<StrengthWorkoutDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StrengthWorkoutRoutes;
