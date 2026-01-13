import { Component } from '@angular/core';
import { SidebarDashboardTechComponent } from "../../components/sidebar-dashboard-tech/sidebar-dashboard-tech.component";
import { HeaderDashboardTechComponent } from "../../components/header-dashboard-tech/header-dashboard-tech.component";
import { NavDashboardTechComponent } from "../../components/nav-dashboard-tech/nav-dashboard-tech.component";
import { FormSkillComponent } from "../../components/form-skill/form-skill.component";

@Component({
  selector: 'app-tech-skills',
  imports: [SidebarDashboardTechComponent, HeaderDashboardTechComponent, NavDashboardTechComponent, FormSkillComponent],
  templateUrl: './tech-skills.component.html',
  styleUrl: './tech-skills.component.css'
})
export class TechSkillsComponent {

}
