import { Component } from '@angular/core';
import { MainContentComponent } from "../../components/main-content/main-content.component";
import { ClientHeaderComponent } from "../../../../core/layout/client-header/client-header.component";

@Component({
  selector: 'app-exploration',
  standalone: true,
  imports: [MainContentComponent, ClientHeaderComponent],
  templateUrl: './exploration.component.html',
  styleUrl: './exploration.component.css'
})
export class ExplorationComponent {

}
