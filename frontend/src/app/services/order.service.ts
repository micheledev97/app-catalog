import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../enviroments/environment';
import {CartService} from './cart.service';

@Injectable({providedIn: 'root'})
export class OrderService {
  constructor(private http: HttpClient, private cart: CartService) {
  }

  checkout() {
    const items = this.cart.items.map(i => ({productId: i.product.id, quantity: i.quantity}));
    return this.http.post(`${environment.apiUrl}/orders`, {items});
  }

  myOrders(page = 0, size = 10) {
    return this.http.get(`${environment.apiUrl}/orders`, {params: {page, size}});
  }
}
