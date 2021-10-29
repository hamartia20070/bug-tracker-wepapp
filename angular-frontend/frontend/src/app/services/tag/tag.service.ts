import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Tag } from 'src/app/interfaces/tag';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TagService {

  private apiServerUrl = environment.apiBaseUrl;
  constructor(private http: HttpClient) { }


  public getTags(): Observable<Tag[]>{
    return this.http.get<Tag[]>(`${this.apiServerUrl}/api/tags/`);
  }
  public updateTag(newTag:Tag): Observable<Tag>{
    let header = new HttpHeaders().set('content-type','application/json')
    let body = JSON.stringify(newTag);
    return this.http.patch<Tag>(`${this.apiServerUrl}/api/tags/`,body,{headers:header});
  }
  public addTag(newTag:Tag): Observable<Tag>{
    let header = new HttpHeaders().set('content-type','application/json')
    let body = JSON.stringify(newTag);
    return this.http.post<Tag>(`${this.apiServerUrl}/api/tags/`,body,{headers:header});
  }
}
