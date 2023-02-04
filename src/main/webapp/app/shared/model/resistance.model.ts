import { ITrainingSet } from 'app/shared/model/training-set.model';

export interface IResistance {
  id?: number;
  band?: number | null;
  cable?: number | null;
  freeWeight?: number | null;
  trainingSet?: ITrainingSet | null;
}

export const defaultValue: Readonly<IResistance> = {};
