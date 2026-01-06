import { Component } from '@angular/core';
import { HeroComponent } from "../../components/hero/hero.component";
import { CategoriesComponent } from "../../components/categories/categories.component";
import { TechniciansComponent } from "../../components/technicians/technicians.component";
import { TestimonialsComponent } from "../../components/testimonials/testimonials.component";
import { HowItWorksComponent } from "../../components/how-it-works/how-it-works.component";
import { HeaderComponent } from "../../../../core/layout/header/header.component";
import { FooterComponent } from "../../../../core/layout/footer/footer.component";

@Component({
  selector: 'app-home-page',
  imports: [HeroComponent, CategoriesComponent, TechniciansComponent, TestimonialsComponent, HowItWorksComponent, HeaderComponent, FooterComponent],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})
export class HomePageComponent {

}
