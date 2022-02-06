import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMoodHistory, MoodHistory } from '../mood-history.model';
import { MoodHistoryService } from '../service/mood-history.service';
import { MoodStatus } from 'app/entities/enumerations/mood-status.model';

@Component({
  selector: 'jhi-mood-history-update',
  templateUrl: './mood-history-update.component.html',
})
export class MoodHistoryUpdateComponent implements OnInit {
  isSaving = false;
  moodStatusValues = Object.keys(MoodStatus);

  editForm = this.fb.group({
    id: [],
    identifier: [null, [Validators.required]],
    submissionDate: [null, [Validators.required]],
    moodStatus: [null, [Validators.required]],
    moodDetails: [null, [Validators.maxLength(350)]],
  });

  constructor(protected moodHistoryService: MoodHistoryService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ moodHistory }) => {
      this.updateForm(moodHistory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const moodHistory = this.createFromForm();
    if (moodHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.moodHistoryService.update(moodHistory));
    } else {
      this.subscribeToSaveResponse(this.moodHistoryService.create(moodHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMoodHistory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(moodHistory: IMoodHistory): void {
    this.editForm.patchValue({
      id: moodHistory.id,
      identifier: moodHistory.identifier,
      submissionDate: moodHistory.submissionDate,
      moodStatus: moodHistory.moodStatus,
      moodDetails: moodHistory.moodDetails,
    });
  }

  protected createFromForm(): IMoodHistory {
    return {
      ...new MoodHistory(),
      id: this.editForm.get(['id'])!.value,
      identifier: this.editForm.get(['identifier'])!.value,
      submissionDate: this.editForm.get(['submissionDate'])!.value,
      moodStatus: this.editForm.get(['moodStatus'])!.value,
      moodDetails: this.editForm.get(['moodDetails'])!.value,
    };
  }
}
