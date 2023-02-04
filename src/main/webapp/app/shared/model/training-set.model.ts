import { IResistance } from 'app/shared/model/resistance.model';
import { IStrengthWorkout } from 'app/shared/model/strength-workout.model';

export interface ITrainingSet {
  id?: number;
  setNumber?: number;
  repetitions?: number | null;
  timeUnderLoad?: number | null;
  resistance?: IResistance | null;
  strengthWorkout?: IStrengthWorkout | null;
}

export const defaultValue: Readonly<ITrainingSet> = {};
