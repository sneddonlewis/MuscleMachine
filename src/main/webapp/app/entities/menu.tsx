import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/strength-workout">
        Strength Workout
      </MenuItem>
      <MenuItem icon="asterisk" to="/exercise">
        Exercise
      </MenuItem>
      <MenuItem icon="asterisk" to="/training-set">
        Training Set
      </MenuItem>
      <MenuItem icon="asterisk" to="/resistance">
        Resistance
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
