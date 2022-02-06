import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMoodHistory } from '../mood-history.model';
import { MoodHistoryService } from '../service/mood-history.service';

@Component({
  templateUrl: './mood-history-delete-dialog.component.html',
})
export class MoodHistoryDeleteDialogComponent {
  moodHistory?: IMoodHistory;

  constructor(protected moodHistoryService: MoodHistoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.moodHistoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
