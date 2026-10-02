import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class Auth {

  constructor(private readonly http: HttpClient) {}

  login(email: string, password: string) {

    return this.http.post(
      'http://localhost:8080/api/auth/login',
      {
        email: email,
        password: password
      },
      {
        responseType: 'text'
      }
    );

  }

  getToken() {
    return localStorage.getItem('token');
  }

  getDecodedToken(): any {

    const token = this.getToken();

    if (!token) {
      return null;
    }

    return jwtDecode(token);
  }

  getUserEmail(): string {

    const decoded = this.getDecodedToken();

    return decoded ? decoded.sub : '';

  }

  getRole(): string {

    const decoded = this.getDecodedToken();

    return decoded ? decoded.role : '';

  }

}