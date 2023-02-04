import strengthWorkout from 'app/entities/strength-workout/strength-workout.reducer';
import exercise from 'app/entities/exercise/exercise.reducer';
import trainingSet from 'app/entities/training-set/training-set.reducer';
import resistance from 'app/entities/resistance/resistance.reducer';
import workSet from 'app/entities/work-set/work-set.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  strengthWorkout,
  exercise,
  trainingSet,
  resistance,
  workSet,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
