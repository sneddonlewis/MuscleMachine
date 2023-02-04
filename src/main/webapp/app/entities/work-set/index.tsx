import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import WorkSet from './work-set';
import WorkSetDetail from './work-set-detail';
import WorkSetUpdate from './work-set-update';
import WorkSetDeleteDialog from './work-set-delete-dialog';

const WorkSetRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<WorkSet />} />
    <Route path="new" element={<WorkSetUpdate />} />
    <Route path=":id">
      <Route index element={<WorkSetDetail />} />
      <Route path="edit" element={<WorkSetUpdate />} />
      <Route path="delete" element={<WorkSetDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default WorkSetRoutes;
