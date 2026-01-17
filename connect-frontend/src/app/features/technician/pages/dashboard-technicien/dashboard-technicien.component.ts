import { Component } from '@angular/core';
import { SidebarDashboardTechComponent } from "../../components/sidebar-dashboard-tech/sidebar-dashboard-tech.component";
import { MainDashboardTechComponent } from "../../components/main-dashboard-tech/main-dashboard-tech.component";
import { NavDashboardTechComponent } from "../../components/nav-dashboard-tech/nav-dashboard-tech.component";
import { HeaderDashboardTechComponent } from "../../components/header-dashboard-tech/header-dashboard-tech.component";

@Component({
  selector: 'app-dashboard-technicien',
  imports: [SidebarDashboardTechComponent, MainDashboardTechComponent, NavDashboardTechComponent, HeaderDashboardTechComponent],
  templateUrl: './dashboard-technicien.component.html',
  styleUrl: './dashboard-technicien.component.css'
})
export class DashboardTechnicienComponent {

}
