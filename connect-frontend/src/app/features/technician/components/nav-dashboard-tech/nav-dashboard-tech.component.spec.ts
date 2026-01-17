import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavDashboardTechComponent } from './nav-dashboard-tech.component';

describe('NavDashboardTechComponent', () => {
  let component: NavDashboardTechComponent;
  let fixture: ComponentFixture<NavDashboardTechComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavDashboardTechComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NavDashboardTechComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
