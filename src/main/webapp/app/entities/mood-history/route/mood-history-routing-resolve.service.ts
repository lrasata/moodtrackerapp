import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMoodHistory, MoodHistory } from '../mood-history.model';
import { MoodHistoryService } from '../service/mood-history.service';

@Injectable({ providedIn: 'root' })
export class MoodHistoryRoutingResolveService implements Resolve<IMoodHistory> {
  constructor(protected service: MoodHistoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMoodHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((moodHistory: HttpResponse<MoodHistory>) => {
          if (moodHistory.body) {
            return of(moodHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MoodHistory());
  }
}
