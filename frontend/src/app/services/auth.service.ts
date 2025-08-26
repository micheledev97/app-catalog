import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../enviroments/environment';

type AuthResp = { token: string, role: string, username: string };

@Injectable({ providedIn: 'root' })
export class AuthService {
  private key = 'auth_token';
  private roleKey = 'auth_role';
  private userKey = 'auth_username';

  constructor(private http: HttpClient){}

  login(username: string, password: string){
    return this.http.post<AuthResp>(`${environment.apiUrl}/auth/login`, {username,password});
  }
  register(username: string, email: string, password: string){
    return this.http.post<AuthResp>(`${environment.apiUrl}/auth/register`, {username,email,password});
  }

  saveAuth(a: AuthResp){
    localStorage.setItem(this.key, a.token);
    localStorage.setItem(this.roleKey, a.role);
    localStorage.setItem(this.userKey, a.username);
  }

  logout(){ localStorage.removeItem(this.key); localStorage.removeItem(this.roleKey); localStorage.removeItem(this.userKey); }

  token(){ return localStorage.getItem(this.key); }
  username(){ return localStorage.getItem(this.userKey); }
  isAuthenticated(){ return !!this.token(); }
  hasRole(r: string){ return (localStorage.getItem(this.roleKey) || '') === r; }
}
