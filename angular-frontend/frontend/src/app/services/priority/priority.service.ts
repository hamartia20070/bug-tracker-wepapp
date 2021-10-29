import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Priority } from 'src/app/interfaces/priority';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PriorityService {
  private apiServerUrl = environment.apiBaseUrl;
  constructor(private http: HttpClient) { }

  public getPriorities(): Observable<Priority[]>{
    return this.http.get<Priority[]>(`${this.apiServerUrl}/api/priorities/`);
  }
  public updatePriority(newPriority:Priority): Observable<Priority>{
    let header = new HttpHeaders().set('content-type','application/json')
    let body = JSON.stringify(newPriority);
    return this.http.patch<Priority>(`${this.apiServerUrl}/api/priorities/`,body,{headers:header});
  }
  public addPriority(newPriority:Priority): Observable<Priority>{
    let header = new HttpHeaders().set('content-type','application/json')
    let body = JSON.stringify(newPriority);
    return this.http.post<Priority>(`${this.apiServerUrl}/api/priorities/`,body,{headers:header});
  }
}
