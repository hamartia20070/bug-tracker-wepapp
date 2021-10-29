import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MiniTicketsMenuCardComponent } from './mini-tickets-menu-card.component';

describe('MiniTicketsMenuCardComponent', () => {
  let component: MiniTicketsMenuCardComponent;
  let fixture: ComponentFixture<MiniTicketsMenuCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MiniTicketsMenuCardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MiniTicketsMenuCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
