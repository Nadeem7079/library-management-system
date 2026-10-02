import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class Book {

  private apiUrl = 'http://localhost:8080/api/books';

  constructor(private http: HttpClient) { }



  private getHeaders(): HttpHeaders {

  const token = localStorage.getItem('token');

  return new HttpHeaders({
    Authorization: 'Bearer ' + token
  });

}



  getBooks() {

    

    return this.http.get<any[]>(this.apiUrl, { headers: this.getHeaders() });

  }


  addBook(book: any) {

  return this.http.post(
    'http://localhost:8080/api/books/add',
    book,
    {headers: this.getHeaders() }
  );

}

deleteBook(id: number) {

  return this.http.delete(
    `http://localhost:8080/api/books/delete/${id}`,
    { headers: this.getHeaders() }
  );

}

updateBook(id: number, book: any) {

  return this.http.put(

    `http://localhost:8080/api/books/${id}`,

    book,

    {
      headers: this.getHeaders()
    }

  );

}



}