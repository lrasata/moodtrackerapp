import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subject } from 'rxjs';
import { finalize, takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { MtrMoodHistoryService } from 'app/entities/mood-history/service/mtr-mood-history.service';
import { IMoodHistory, MoodHistory } from 'app/entities/mood-history/mood-history.model';
import { MoodStatus } from 'app/entities/enumerations/mood-status.model';
import dayjs from 'dayjs/esm';
import { HttpResponse } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import { DATE_FORMAT } from 'app/config/input.constants';

// @author lrasata

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  currentMoodStatus = MoodStatus.HAPPY;
  toggleButtonMap = new Map([
    ['HAPPY', true],
    ['NORMAL', false],
    ['MEH', false],
    ['GRUMPY', false],
    ['STRESSED', false],
  ]);
  textValue = '';
  moodHistory = new MoodHistory();
  isSaved = false;

  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private mtrMoodHistoryService: MtrMoodHistoryService,
    private cookieService: CookieService
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onSubmit(): void {
    if (this.isUserAllowedToSubmit()) {
      this.moodHistory.identifier = this.getIdentifier();
      this.moodHistory.submissionDate = dayjs();
      this.moodHistory.moodStatus = this.currentMoodStatus;
      this.moodHistory.moodDetails = this.textValue;

      this.subscribeToSaveResponse(this.mtrMoodHistoryService.create(this.moodHistory));
    }
  }

  isUserAllowedToSubmit(): boolean {
    const cookieSubmissionDate = this.cookieService.get('submissionDate');
    const cookieMoodStatus = this.cookieService.get('moodStatus');

    if (cookieMoodStatus && cookieSubmissionDate === dayjs().format(DATE_FORMAT).toString()) {
      return false;
    }
    return true;
  }
  setMoodStatus(moodString: string): void {
    this.onToggleButton(moodString);
    switch (moodString) {
      case 'HAPPY':
        this.currentMoodStatus = MoodStatus.HAPPY;
        break;
      case 'NORMAL':
        this.currentMoodStatus = MoodStatus.NORMAL;
        break;
      case 'MEH':
        this.currentMoodStatus = MoodStatus.MEH;
        break;
      case 'GRUMPY':
        this.currentMoodStatus = MoodStatus.GRUMPY;
        break;
      case 'STRESSED':
        this.currentMoodStatus = MoodStatus.STRESSED;
        break;
    }
  }

  onToggleButton(keyToggled: string): void {
    this.toggleButtonMap.forEach((_value: boolean, key: string) => {
      this.toggleButtonMap.set(key, false);
    });
    this.toggleButtonMap.set(keyToggled, true);
  }

  getIdentifier(): string {
    const identifier = this.cookieService.get('identifier');
    if (identifier) {
      return identifier;
    } else {
      const currentStringDate = Date.now().toString(36);
      const randomStringNumber = Math.random().toString(36).substring(2, 8);
      return currentStringDate + '-' + randomStringNumber;
    }
  }

  selectedButton(): string {
    return 'btn btn-lg active';
  }

  unselectedButton(): string {
    return 'btn btn-lg';
  }

  setCookieService(): void {
    if (this.moodHistory.identifier) {
      this.cookieService.set('identifier', this.moodHistory.identifier);
    }
    if (this.moodHistory.submissionDate) {
      this.cookieService.set('submissionDate', this.moodHistory.submissionDate.format(DATE_FORMAT));
    }
    if (this.moodHistory.moodStatus) {
      this.cookieService.set('moodStatus', this.moodHistory.moodStatus.toString());
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMoodHistory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.textValue = '';
    this.isSaved = true;
    this.setCookieService();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    // Api for inheritance.
  }

  // TODO

  // quand je clique,
  // je save dans le cookie : un identifier, je récupère la date, le mood, et le text

  // + : cookie-ribbon for authorization
}
