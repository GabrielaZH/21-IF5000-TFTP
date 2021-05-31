import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/client.service';

@Component({
  selector: 'app-receive',
  templateUrl: './receive.component.html',
  styleUrls: ['./receive.component.css']
})
export class ReceiveComponent implements OnInit {

  public images:any = [];

  constructor(private clientService:ClientService) { }

  ngOnInit(): void {
    this.clientService.getImages().subscribe(response =>{
      this.images = response;
    });
  }

}
