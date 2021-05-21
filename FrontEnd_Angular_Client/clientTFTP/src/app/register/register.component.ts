import { Component, OnInit, Input} from '@angular/core';
import { ClientService } from '../services/client.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Client } from '../models/client.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  client:Client = new Client();

  constructor(  
                 private router:Router,
                 private clientService:ClientService,
                 private route: ActivatedRoute) { }

  ngOnInit() {
  }

  addClient() {
    this.clientService.addClient(this.client).subscribe((result) => {
      if(result){
        this.modal('/login','Registered'); 
       } else{
        this.modal('/register','Try again'); 
       }
                
    });


     
  }

  cancel() {
    this.router.navigate(['/login']);
  }

  modal( url:string | '', message:String){
    let timerInterval:any
        Swal.fire({
        title: message,
        html: '',
        timer: 1000,
        didOpen: () => {
          Swal.showLoading()
          timerInterval = setInterval(() => {
          }, 50)
        },
        willClose: () => {
        clearInterval(timerInterval)
        }
        }).then((result) => {
          if (result.dismiss === Swal.DismissReason.timer) {
            this.router.navigateByUrl(url);
          }
        })     
  }

}
