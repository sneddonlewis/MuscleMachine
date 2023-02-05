import dayjs from 'dayjs';
import { IWorkSet } from 'app/shared/model/work-set.model';
import { IUser } from 'app/shared/model/user.model';

export interface IStrengthWorkout {
  id?: number;
  time?: string;
  workSets?: IWorkSet[] | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IStrengthWorkout> = {};
