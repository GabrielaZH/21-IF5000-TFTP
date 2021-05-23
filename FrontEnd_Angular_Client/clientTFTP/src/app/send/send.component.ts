import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/client.service';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-send',
  templateUrl: './send.component.html',
  styleUrls: ['./send.component.css']
})
export class SendComponent implements OnInit {
  file:File;

  constructor(private clientService:ClientService) { }

  ngOnInit(): void {
  }

  selectFile(event:any) {
    this.file = event.target.files.item(0);
  }

  showLoading(){
    Swal.fire({
        title: 'Uploading...',
        allowEscapeKey: false,
        allowOutsideClick: false,
        timer: 100000,
        onOpen: () => {
          Swal.showLoading();
        }
    })
  };

  upload() {
    this.showLoading();
    this.clientService.upload(this.file).subscribe((result) => {
      if(result){
        Swal.fire({
          title: 'Upload',
          icon: 'success',
          timer: 1000,
        });
      }
    });
  }

}

