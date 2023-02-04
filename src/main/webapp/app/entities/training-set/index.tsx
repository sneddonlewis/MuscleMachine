import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TrainingSet from './training-set';
import TrainingSetDetail from './training-set-detail';
import TrainingSetUpdate from './training-set-update';
import TrainingSetDeleteDialog from './training-set-delete-dialog';

const TrainingSetRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TrainingSet />} />
    <Route path="new" element={<TrainingSetUpdate />} />
    <Route path=":id">
      <Route index element={<TrainingSetDetail />} />
      <Route path="edit" element={<TrainingSetUpdate />} />
      <Route path="delete" element={<TrainingSetDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TrainingSetRoutes;
