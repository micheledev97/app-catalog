import { Component, OnInit } from '@angular/core';
import { ProductService, Product, Page } from '../../services/product.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html'
})
export class CatalogComponent implements OnInit {
  page?: Page<Product>;
  name=''; category=''; minPrice?: number; maxPrice?: number;
  size=12; pageIndex=0;
  protected readonly Math = Math;
  constructor(private api: ProductService, public cart: CartService){}
  ngOnInit(){ this.load(); }
  load(){
    this.api.list({page: this.pageIndex, size: this.size, name: this.name, category: this.category, minPrice: this.minPrice, maxPrice: this.maxPrice})
      .subscribe(p => this.page = p);
  }
  next(){ if(this.page && (this.page.number+1)*this.page.size < this.page.totalElements){ this.pageIndex++; this.load(); } }
  prev(){ if(this.pageIndex>0){ this.pageIndex--; this.load(); } }
}
