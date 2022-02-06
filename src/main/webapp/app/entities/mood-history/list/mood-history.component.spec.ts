import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MoodHistoryService } from '../service/mood-history.service';

import { MoodHistoryComponent } from './mood-history.component';

describe('MoodHistory Management Component', () => {
  let comp: MoodHistoryComponent;
  let fixture: ComponentFixture<MoodHistoryComponent>;
  let service: MoodHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MoodHistoryComponent],
    })
      .overrideTemplate(MoodHistoryComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MoodHistoryComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MoodHistoryService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.moodHistories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
