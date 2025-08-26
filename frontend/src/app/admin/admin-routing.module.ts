import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductCrudComponent } from './product-crud/product-crud.component';

const routes: Routes = [{ path: '', component: ProductCrudComponent }];

@NgModule({ imports: [RouterModule.forChild(routes)], exports: [RouterModule] })
export class AdminRoutingModule {}
