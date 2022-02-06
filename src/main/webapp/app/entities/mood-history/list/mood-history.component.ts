import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMoodHistory } from '../mood-history.model';
import { MoodHistoryService } from '../service/mood-history.service';
import { MoodHistoryDeleteDialogComponent } from '../delete/mood-history-delete-dialog.component';

@Component({
  selector: 'jhi-mood-history',
  templateUrl: './mood-history.component.html',
})
export class MoodHistoryComponent implements OnInit {
  moodHistories?: IMoodHistory[];
  isLoading = false;

  constructor(protected moodHistoryService: MoodHistoryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.moodHistoryService.query().subscribe({
      next: (res: HttpResponse<IMoodHistory[]>) => {
        this.isLoading = false;
        this.moodHistories = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMoodHistory): number {
    return item.id!;
  }

  delete(moodHistory: IMoodHistory): void {
    const modalRef = this.modalService.open(MoodHistoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.moodHistory = moodHistory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
