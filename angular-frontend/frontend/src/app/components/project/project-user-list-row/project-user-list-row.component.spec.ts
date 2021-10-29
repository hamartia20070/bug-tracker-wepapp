import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectUserListRowComponent } from './project-user-list-row.component';

describe('ProjectUserListRowComponent', () => {
  let component: ProjectUserListRowComponent;
  let fixture: ComponentFixture<ProjectUserListRowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProjectUserListRowComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectUserListRowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
