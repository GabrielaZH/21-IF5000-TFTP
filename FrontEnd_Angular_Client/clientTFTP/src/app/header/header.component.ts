import { Component, OnInit } from '@angular/core';
import { ClientService} from '../services/client.service';
import { Router } from '@angular/router';
import { Client } from '../models/client.model';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

 
  title = 'Clients';

   client:Client = new Client(); //revisar


  constructor(private clientService: ClientService, private router:Router) { }

  ngOnInit(): void {
    this.client = this.clientService.client;
    console.log( this.clientService.client);
  }

  logout(){
    this.clientService.logout();
    this.router.navigate(['/login']);
  }
}
