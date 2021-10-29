import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketsCardRowComponent } from './tickets-card-row.component';

describe('TicketsCardRowComponent', () => {
  let component: TicketsCardRowComponent;
  let fixture: ComponentFixture<TicketsCardRowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TicketsCardRowComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TicketsCardRowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
