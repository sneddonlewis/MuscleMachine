import dayjs from 'dayjs';
import { IExercise } from 'app/shared/model/exercise.model';
import { IUser } from 'app/shared/model/user.model';
import { ITrainingSet } from 'app/shared/model/training-set.model';
import { IWorkSet } from 'app/shared/model/work-set.model';

export interface IStrengthWorkout {
  id?: number;
  time?: string;
  exercise?: IExercise | null;
  user?: IUser | null;
  trainingSets?: ITrainingSet[] | null;
  workSets?: IWorkSet[] | null;
}

export const defaultValue: Readonly<IStrengthWorkout> = {};
