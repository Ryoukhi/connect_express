import { Component } from '@angular/core';
import { HeaderComponent } from "../../../../core/layout/header/header.component";
import { MainContentComponent } from "../../components/main-content/main-content.component";
import { ClientHeaderComponent } from "../../../../core/layout/client-header/client-header.component";

@Component({
  selector: 'app-exploration',
  imports: [MainContentComponent, ClientHeaderComponent],
  templateUrl: './exploration.component.html',
  styleUrl: './exploration.component.css'
})
export class ExplorationComponent {

}
