import dayjs from 'dayjs/esm';
import { MoodStatus } from 'app/entities/enumerations/mood-status.model';

export interface IMoodHistory {
  id?: number;
  identifier?: string;
  submissionDate?: dayjs.Dayjs;
  moodStatus?: MoodStatus;
  moodDetails?: string | null;
}

export class MoodHistory implements IMoodHistory {
  constructor(
    public id?: number,
    public identifier?: string,
    public submissionDate?: dayjs.Dayjs,
    public moodStatus?: MoodStatus,
    public moodDetails?: string | null
  ) {}
}

export function getMoodHistoryIdentifier(moodHistory: IMoodHistory): number | undefined {
  return moodHistory.id;
}
