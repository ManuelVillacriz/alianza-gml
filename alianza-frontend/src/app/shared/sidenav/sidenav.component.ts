import { Component } from '@angular/core';
import { MediaMatcher } from '@angular/cdk/layout';

@Component({
  selector: 'app-sidenav',
  standalone: false,
  
  templateUrl: './sidenav.component.html',
  styleUrl: './sidenav.component.css'
})
export class SidenavComponent {

  mobileQuery: MediaQueryList;

  menuNav = [    
   {name: "Cliente", route: "clientes", icon: "person"}
  ]

  constructor(media: MediaMatcher) {
   this.mobileQuery = media.matchMedia('(max-width: 600px)');
 }

}
