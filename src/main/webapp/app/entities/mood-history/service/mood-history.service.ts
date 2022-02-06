import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMoodHistory, getMoodHistoryIdentifier } from '../mood-history.model';

export type EntityResponseType = HttpResponse<IMoodHistory>;
export type EntityArrayResponseType = HttpResponse<IMoodHistory[]>;

@Injectable({ providedIn: 'root' })
export class MoodHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mood-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(moodHistory: IMoodHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moodHistory);
    return this.http
      .post<IMoodHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(moodHistory: IMoodHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moodHistory);
    return this.http
      .put<IMoodHistory>(`${this.resourceUrl}/${getMoodHistoryIdentifier(moodHistory) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(moodHistory: IMoodHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moodHistory);
    return this.http
      .patch<IMoodHistory>(`${this.resourceUrl}/${getMoodHistoryIdentifier(moodHistory) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMoodHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMoodHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMoodHistoryToCollectionIfMissing(
    moodHistoryCollection: IMoodHistory[],
    ...moodHistoriesToCheck: (IMoodHistory | null | undefined)[]
  ): IMoodHistory[] {
    const moodHistories: IMoodHistory[] = moodHistoriesToCheck.filter(isPresent);
    if (moodHistories.length > 0) {
      const moodHistoryCollectionIdentifiers = moodHistoryCollection.map(moodHistoryItem => getMoodHistoryIdentifier(moodHistoryItem)!);
      const moodHistoriesToAdd = moodHistories.filter(moodHistoryItem => {
        const moodHistoryIdentifier = getMoodHistoryIdentifier(moodHistoryItem);
        if (moodHistoryIdentifier == null || moodHistoryCollectionIdentifiers.includes(moodHistoryIdentifier)) {
          return false;
        }
        moodHistoryCollectionIdentifiers.push(moodHistoryIdentifier);
        return true;
      });
      return [...moodHistoriesToAdd, ...moodHistoryCollection];
    }
    return moodHistoryCollection;
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

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((moodHistory: IMoodHistory) => {
        moodHistory.submissionDate = moodHistory.submissionDate ? dayjs(moodHistory.submissionDate) : undefined;
      });
    }
    return res;
  }
}
