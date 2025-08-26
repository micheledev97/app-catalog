import {Injectable} from '@angular/core';
import {Product} from './product.service';

export interface CartItem {
  product: Product;
  quantity: number;
}

@Injectable({providedIn: 'root'})
export class CartService {
  private key = 'cart_items';
  items: CartItem[] = [];

  constructor() {
    const raw = localStorage.getItem(this.key);
    this.items = raw ? JSON.parse(raw) : [];
  }

  persist() {
    localStorage.setItem(this.key, JSON.stringify(this.items));
  }

  add(product: Product, qty: number = 1) {
    const idx = this.items.findIndex(i => i.product.id === product.id);
    if (idx >= 0) this.items[idx].quantity += qty; else this.items.push({product, quantity: qty});
    this.persist();
  }

  remove(productId: number) {
    this.items = this.items.filter(i => i.product.id !== productId);
    this.persist();
  }

  clear() {
    this.items = [];
    this.persist();
  }

  total() {
    return this.items.reduce((s, i) => s + i.product.price * i.quantity, 0);
  }

  count() {
    return this.items.reduce((s, i) => s + i.quantity, 0);
  }
}
