import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Resistance from './resistance';
import ResistanceDetail from './resistance-detail';
import ResistanceUpdate from './resistance-update';
import ResistanceDeleteDialog from './resistance-delete-dialog';

const ResistanceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Resistance />} />
    <Route path="new" element={<ResistanceUpdate />} />
    <Route path=":id">
      <Route index element={<ResistanceDetail />} />
      <Route path="edit" element={<ResistanceUpdate />} />
      <Route path="delete" element={<ResistanceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ResistanceRoutes;
