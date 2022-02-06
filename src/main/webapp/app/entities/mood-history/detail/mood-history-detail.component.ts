import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMoodHistory } from '../mood-history.model';

@Component({
  selector: 'jhi-mood-history-detail',
  templateUrl: './mood-history-detail.component.html',
})
export class MoodHistoryDetailComponent implements OnInit {
  moodHistory: IMoodHistory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ moodHistory }) => {
      this.moodHistory = moodHistory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
