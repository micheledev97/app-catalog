import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminRoutingModule } from './admin-routing.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { ProductCrudComponent } from './product-crud/product-crud.component';

@NgModule({
  declarations: [ProductCrudComponent],
  imports: [CommonModule, ReactiveFormsModule, FormsModule, AdminRoutingModule]
})
export class AdminModule {}
