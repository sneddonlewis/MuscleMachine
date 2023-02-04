import { IStrengthWorkout } from 'app/shared/model/strength-workout.model';

export interface IExercise {
  id?: number;
  name?: string;
  strengthWorkout?: IStrengthWorkout | null;
}

export const defaultValue: Readonly<IExercise> = {};
