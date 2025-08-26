import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService, Product } from '../../services/product.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html'
})
export class ProductDetailComponent implements OnInit {
  product?: Product; qty = 1;
  constructor(private route: ActivatedRoute, private api: ProductService, public cart: CartService) {}
  ngOnInit(){
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.api.get(id).subscribe(p => this.product = p);
  }
}
