import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketsCardComponent } from './tickets-card.component';

describe('TicketsCardComponent', () => {
  let component: TicketsCardComponent;
  let fixture: ComponentFixture<TicketsCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TicketsCardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TicketsCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
