import {Component} from '@angular/core';
import {CartService} from '../../services/cart.service';
import {Router} from '@angular/router';

@Component({selector: 'app-cart', templateUrl: './cart.component.html'})
export class CartComponent {
  constructor(public cart: CartService, private router: Router) {
  }

  goCheckout() {
    this.router.navigate(['/checkout']);
  }
}
