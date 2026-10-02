import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class User {

  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  private getHeaders() {

    const token = localStorage.getItem('token');

    return {
      headers: new HttpHeaders({
        Authorization: 'Bearer ' + token
      })
    };

  }

  getUserByEmail(email: string) {

    return this.http.get<any>(
      `${this.apiUrl}/email/${email}`,
      this.getHeaders()
    );

  }

  getUsers() {

    return this.http.get<any[]>(
      this.apiUrl,
      this.getHeaders()
    );

  }


  deleteUser(id: number) {

  return this.http.delete(

    `${this.apiUrl}/${id}`,

    this.getHeaders()

  );

}

}