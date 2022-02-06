import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { MoodStatus } from 'app/entities/enumerations/mood-status.model';
import { IMoodHistory, MoodHistory } from '../mood-history.model';

import { MoodHistoryService } from './mood-history.service';

describe('MoodHistory Service', () => {
  let service: MoodHistoryService;
  let httpMock: HttpTestingController;
  let elemDefault: IMoodHistory;
  let expectedResult: IMoodHistory | IMoodHistory[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MoodHistoryService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      identifier: 'AAAAAAA',
      submissionDate: currentDate,
      moodStatus: MoodStatus.HAPPY,
      moodDetails: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          submissionDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a MoodHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          submissionDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          submissionDate: currentDate,
        },
        returnedFromService
      );

      service.create(new MoodHistory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MoodHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          identifier: 'BBBBBB',
          submissionDate: currentDate.format(DATE_FORMAT),
          moodStatus: 'BBBBBB',
          moodDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          submissionDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MoodHistory', () => {
      const patchObject = Object.assign(
        {
          identifier: 'BBBBBB',
          submissionDate: currentDate.format(DATE_FORMAT),
          moodStatus: 'BBBBBB',
          moodDetails: 'BBBBBB',
        },
        new MoodHistory()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          submissionDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MoodHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          identifier: 'BBBBBB',
          submissionDate: currentDate.format(DATE_FORMAT),
          moodStatus: 'BBBBBB',
          moodDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          submissionDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a MoodHistory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMoodHistoryToCollectionIfMissing', () => {
      it('should add a MoodHistory to an empty array', () => {
        const moodHistory: IMoodHistory = { id: 123 };
        expectedResult = service.addMoodHistoryToCollectionIfMissing([], moodHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moodHistory);
      });

      it('should not add a MoodHistory to an array that contains it', () => {
        const moodHistory: IMoodHistory = { id: 123 };
        const moodHistoryCollection: IMoodHistory[] = [
          {
            ...moodHistory,
          },
          { id: 456 },
        ];
        expectedResult = service.addMoodHistoryToCollectionIfMissing(moodHistoryCollection, moodHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MoodHistory to an array that doesn't contain it", () => {
        const moodHistory: IMoodHistory = { id: 123 };
        const moodHistoryCollection: IMoodHistory[] = [{ id: 456 }];
        expectedResult = service.addMoodHistoryToCollectionIfMissing(moodHistoryCollection, moodHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moodHistory);
      });

      it('should add only unique MoodHistory to an array', () => {
        const moodHistoryArray: IMoodHistory[] = [{ id: 123 }, { id: 456 }, { id: 43098 }];
        const moodHistoryCollection: IMoodHistory[] = [{ id: 123 }];
        expectedResult = service.addMoodHistoryToCollectionIfMissing(moodHistoryCollection, ...moodHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const moodHistory: IMoodHistory = { id: 123 };
        const moodHistory2: IMoodHistory = { id: 456 };
        expectedResult = service.addMoodHistoryToCollectionIfMissing([], moodHistory, moodHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moodHistory);
        expect(expectedResult).toContain(moodHistory2);
      });

      it('should accept null and undefined values', () => {
        const moodHistory: IMoodHistory = { id: 123 };
        expectedResult = service.addMoodHistoryToCollectionIfMissing([], null, moodHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moodHistory);
      });

      it('should return initial array if no MoodHistory is added', () => {
        const moodHistoryCollection: IMoodHistory[] = [{ id: 123 }];
        expectedResult = service.addMoodHistoryToCollectionIfMissing(moodHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(moodHistoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
