import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Role } from 'src/app/interfaces/role';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  private apiServerUrl = environment.apiBaseUrl;
  constructor(private http: HttpClient) { }

  public getRoles(): Observable<Role[]>{
    return this.http.get<Role[]>(`${this.apiServerUrl}/api/roles/`);
  }

  public getRolesByAcessLevel(accessLevel:number, pageNo:number): Observable<Role[]>{
    let params = new HttpParams().set("pageNo",pageNo);
    return this.http.get<Role[]>(`${this.apiServerUrl}/api/roles/accesslevel/${accessLevel}`,{params:params});
  }
  public updateRole(newRole:Role): Observable<Role>{
    let header = new HttpHeaders().set('content-type','application/json')
    let body = JSON.stringify(newRole);
    return this.http.patch<Role>(`${this.apiServerUrl}/api/roles/`,body,{headers:header});
  }
  public addRole(newRole:Role): Observable<Role>{
    let header = new HttpHeaders().set('content-type','application/json')
    let body = JSON.stringify(newRole);
    return this.http.post<Role>(`${this.apiServerUrl}/api/roles/`,body,{headers:header});
  }
}
