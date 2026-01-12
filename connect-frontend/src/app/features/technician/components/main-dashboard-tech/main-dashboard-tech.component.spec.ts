import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MainDashboardTechComponent } from './main-dashboard-tech.component';

describe('MainDashboardTechComponent', () => {
  let component: MainDashboardTechComponent;
  let fixture: ComponentFixture<MainDashboardTechComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MainDashboardTechComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MainDashboardTechComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
