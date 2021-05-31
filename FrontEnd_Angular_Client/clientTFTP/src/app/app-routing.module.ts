import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { ReceiveComponent } from './receive/receive.component';
import { SendComponent } from './send/send.component';
import { AuthGuard } from './guards/auth.guard';
import { RegisterComponent } from './register/register.component';

const routes: Routes = [
 
  { path: 'register', component: RegisterComponent },
  { path: 'home'   , component: HomeComponent,canActivate:[ AuthGuard] },
  { path: '',redirectTo:'home', pathMatch:'full' },
  { path: 'login', component: LoginComponent },
  { path: 'receive', component: ReceiveComponent },
  { path: 'send', component: SendComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
