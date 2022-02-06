import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MoodHistoryComponent } from './list/mood-history.component';
import { MoodHistoryDetailComponent } from './detail/mood-history-detail.component';
import { MoodHistoryUpdateComponent } from './update/mood-history-update.component';
import { MoodHistoryDeleteDialogComponent } from './delete/mood-history-delete-dialog.component';
import { MoodHistoryRoutingModule } from './route/mood-history-routing.module';

@NgModule({
  imports: [SharedModule, MoodHistoryRoutingModule],
  declarations: [MoodHistoryComponent, MoodHistoryDetailComponent, MoodHistoryUpdateComponent, MoodHistoryDeleteDialogComponent],
  entryComponents: [MoodHistoryDeleteDialogComponent],
})
export class MoodHistoryModule {}
