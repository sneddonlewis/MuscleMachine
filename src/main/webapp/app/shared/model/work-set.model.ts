import { IStrengthWorkout } from 'app/shared/model/strength-workout.model';

export interface IWorkSet {
  id?: number;
  setNumber?: number;
  repetitions?: number | null;
  timeUnderLoad?: number | null;
  bandResistance?: number | null;
  cableResistance?: number | null;
  freeWeightResistance?: number | null;
  strengthWorkout?: IStrengthWorkout | null;
}

export const defaultValue: Readonly<IWorkSet> = {};
