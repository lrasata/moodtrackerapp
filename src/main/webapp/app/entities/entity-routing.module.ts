import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'mood-history',
        data: { pageTitle: 'MoodHistories' },
        loadChildren: () => import('./mood-history/mood-history.module').then(m => m.MoodHistoryModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
