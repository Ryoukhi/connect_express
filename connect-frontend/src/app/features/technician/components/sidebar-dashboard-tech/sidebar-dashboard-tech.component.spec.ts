import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SidebarDashboardTechComponent } from './sidebar-dashboard-tech.component';

describe('SidebarDashboardTechComponent', () => {
  let component: SidebarDashboardTechComponent;
  let fixture: ComponentFixture<SidebarDashboardTechComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SidebarDashboardTechComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SidebarDashboardTechComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
