import { Component } from '@angular/core';
import { ListReservationsComponent } from "../../components/list-reservations/list-reservations.component";
import { NavDashboardTechComponent } from "../../components/nav-dashboard-tech/nav-dashboard-tech.component";
import { HeaderDashboardTechComponent } from "../../components/header-dashboard-tech/header-dashboard-tech.component";
import { SidebarDashboardTechComponent } from "../../components/sidebar-dashboard-tech/sidebar-dashboard-tech.component";

@Component({
  selector: 'app-today-reservations',
  imports: [ListReservationsComponent, NavDashboardTechComponent, HeaderDashboardTechComponent, SidebarDashboardTechComponent],
  templateUrl: './today-reservations.component.html',
  styleUrl: './today-reservations.component.css'
})
export class TodayReservationsComponent {

}
