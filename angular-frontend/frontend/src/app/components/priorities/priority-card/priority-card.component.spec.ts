import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PriorityCardComponent } from './priority-card.component';

describe('PriorityCardComponent', () => {
  let component: PriorityCardComponent;
  let fixture: ComponentFixture<PriorityCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PriorityCardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PriorityCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
