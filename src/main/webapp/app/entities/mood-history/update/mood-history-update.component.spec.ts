import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MoodHistoryService } from '../service/mood-history.service';
import { IMoodHistory, MoodHistory } from '../mood-history.model';

import { MoodHistoryUpdateComponent } from './mood-history-update.component';

describe('MoodHistory Management Update Component', () => {
  let comp: MoodHistoryUpdateComponent;
  let fixture: ComponentFixture<MoodHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let moodHistoryService: MoodHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MoodHistoryUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MoodHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MoodHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    moodHistoryService = TestBed.inject(MoodHistoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const moodHistory: IMoodHistory = { id: 456 };

      activatedRoute.data = of({ moodHistory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(moodHistory));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MoodHistory>>();
      const moodHistory = { id: 123 };
      jest.spyOn(moodHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moodHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moodHistory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(moodHistoryService.update).toHaveBeenCalledWith(moodHistory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MoodHistory>>();
      const moodHistory = new MoodHistory();
      jest.spyOn(moodHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moodHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moodHistory }));
      saveSubject.complete();

      // THEN
      expect(moodHistoryService.create).toHaveBeenCalledWith(moodHistory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MoodHistory>>();
      const moodHistory = { id: 123 };
      jest.spyOn(moodHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moodHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(moodHistoryService.update).toHaveBeenCalledWith(moodHistory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
