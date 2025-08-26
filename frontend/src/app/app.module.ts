import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';
import {ReactiveFormsModule, FormsModule} from '@angular/forms';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';

import {CatalogComponent} from './components/catalog/catalog.component';
import {ProductDetailComponent} from './components/product-detail/product-detail.component';
import {LoginComponent} from './components/login/login.component';
import {CartComponent} from './components/cart/cart.component';
import {CheckoutComponent} from './components/checkout/checkout.component';

import {AuthInterceptor} from './interceptors/auth.interceptor';


@NgModule({
  declarations: [
    AppComponent, CatalogComponent, ProductDetailComponent, LoginComponent, CartComponent, CheckoutComponent
  ],
  imports: [BrowserModule, HttpClientModule, ReactiveFormsModule, FormsModule, AppRoutingModule],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
