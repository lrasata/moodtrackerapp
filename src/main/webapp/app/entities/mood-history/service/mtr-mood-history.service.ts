import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IMoodHistory } from '../mood-history.model';

export type EntityResponseType = HttpResponse<IMoodHistory>;

@Injectable({ providedIn: 'root' })
export class MtrMoodHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/track-mood-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(moodHistory: IMoodHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moodHistory);
    return this.http
      .post<IMoodHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  protected convertDateFromClient(moodHistory: IMoodHistory): IMoodHistory {
    return Object.assign({}, moodHistory, {
      submissionDate: moodHistory.submissionDate?.isValid() ? moodHistory.submissionDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.submissionDate = res.body.submissionDate ? dayjs(res.body.submissionDate) : undefined;
    }
    return res;
  }
}
