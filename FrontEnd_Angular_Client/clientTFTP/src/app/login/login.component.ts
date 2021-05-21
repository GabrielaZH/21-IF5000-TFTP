import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { ClientService } from '../services/client.service';
import { Client } from '../models/client.model';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  client:Client = new Client();

  constructor(  
                 private router:Router,
                 private clientService:ClientService,
                 private route: ActivatedRoute) { }

  ngOnInit() {
  }

  login() {
    this.clientService.login(this.client).subscribe((result) => {
      if( result)   {
        this.client = result;
        this.modal('/home','Welcome: '+ (result.name));            
       }else{
        this.modal('/login','Credentials incorrect')
       } 
    });
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

