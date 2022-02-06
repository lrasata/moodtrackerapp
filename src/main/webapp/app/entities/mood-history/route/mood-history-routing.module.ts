import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MoodHistoryComponent } from '../list/mood-history.component';
import { MoodHistoryDetailComponent } from '../detail/mood-history-detail.component';
import { MoodHistoryUpdateComponent } from '../update/mood-history-update.component';
import { MoodHistoryRoutingResolveService } from './mood-history-routing-resolve.service';

const moodHistoryRoute: Routes = [
  {
    path: '',
    component: MoodHistoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MoodHistoryDetailComponent,
    resolve: {
      moodHistory: MoodHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MoodHistoryUpdateComponent,
    resolve: {
      moodHistory: MoodHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MoodHistoryUpdateComponent,
    resolve: {
      moodHistory: MoodHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(moodHistoryRoute)],
  exports: [RouterModule],
})
export class MoodHistoryRoutingModule {}
