import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class Reservation {

  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {

    const token = localStorage.getItem('token');

    return new HttpHeaders({
      Authorization: 'Bearer ' + token
    });

  }

  reserveBook(userId: number, bookId: number) {

    return this.http.post(

      `${this.apiUrl}/reservations?userId=${userId}&bookId=${bookId}`,

      {},

      {
        headers: this.getHeaders()
      }

    );

  }

}