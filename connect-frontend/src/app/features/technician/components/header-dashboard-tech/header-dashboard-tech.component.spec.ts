import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderDashboardTechComponent } from './header-dashboard-tech.component';

describe('HeaderDashboardTechComponent', () => {
  let component: HeaderDashboardTechComponent;
  let fixture: ComponentFixture<HeaderDashboardTechComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderDashboardTechComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeaderDashboardTechComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
