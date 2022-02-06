import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MoodHistoryDetailComponent } from './mood-history-detail.component';

describe('MoodHistory Management Detail Component', () => {
  let comp: MoodHistoryDetailComponent;
  let fixture: ComponentFixture<MoodHistoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MoodHistoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ moodHistory: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MoodHistoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MoodHistoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load moodHistory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.moodHistory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
