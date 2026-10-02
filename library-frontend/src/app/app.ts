import { Component } from '@angular/core';
import { Login } from './login/login';
import { Dashboard } from './dashboard/dashboard';


@Component({
  selector: 'app-root',
  imports: [Login, Dashboard],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  isLoggedIn = !!localStorage.getItem('token');

}