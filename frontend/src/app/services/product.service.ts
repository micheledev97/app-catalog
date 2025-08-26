import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../enviroments/environment';

export interface Product {
  id: number;
  name: string;
  description: string;
  category: string;
  price: number;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  number: number;
  size: number;
}

@Injectable({providedIn: 'root'})
export class ProductService {
  constructor(private http: HttpClient) {
  }

  list(params: { page?: number, size?: number, sort?: string, name?: string, category?: string, minPrice?: number, maxPrice?: number }) {
    let p = new HttpParams();
    Object.entries(params).forEach(([k, v]) => {
      if (v !== undefined && v !== null && `${v}` !== '') p = p.set(k, `${v}`);
    });
    return this.http.get<Page<Product>>(`${environment.apiUrl}/products`, {params: p});
  }

  get(id: number) {
    return this.http.get<Product>(`${environment.apiUrl}/products/${id}`);
  }

  create(prod: Partial<Product>) {
    return this.http.post<Product>(`${environment.apiUrl}/products`, prod);
  }

  update(id: number, prod: Partial<Product>) {
    return this.http.put<Product>(`${environment.apiUrl}/products/${id}`, prod);
  }

  delete(id: number) {
    return this.http.delete(`${environment.apiUrl}/products/${id}`);
  }
}
