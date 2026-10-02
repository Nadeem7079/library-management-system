import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import  {Auth} from '../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  email = '';

  password = '';

 handleLogin() {
  console.log("Email:", this.email);
  console.log("Password:", this.password);

  this.authService.login(this.email, this.password)
    .subscribe({
      next: (token) => {
        console.log("Success:", token);

        localStorage.setItem('token', token);
        window.location.reload();

        alert("Login Successful!");
      },
      error: (err) => {
        console.error("Login failed", err);
        alert("Invalid Email or Password");
      }
    });
}

  constructor(private authService: Auth){}
}    