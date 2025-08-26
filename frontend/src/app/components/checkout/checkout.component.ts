import {Component} from '@angular/core';
import {OrderService} from '../../services/order.service';
import {CartService} from '../../services/cart.service';

@Component({selector: 'app-checkout', templateUrl: './checkout.component.html'})
export class CheckoutComponent {
  ok?: boolean;
  msg?: string;
  cardNumber = '';
  name = '';
  cvv = '';

  constructor(private orders: OrderService, public cart: CartService) {
  }

  submit() {
    this.orders.checkout().subscribe({
      next: () => {
        this.ok = true;
        this.msg = 'Ordine creato e pagamento fittizio eseguito.';
        this.cart.clear();
      },
      error: e => {
        this.ok = false;
        this.msg = e.error?.message || 'Errore ordine.';
      }
    })
  }
}
