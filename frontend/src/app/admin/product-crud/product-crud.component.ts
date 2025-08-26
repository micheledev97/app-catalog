import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, Validators, FormControl, FormGroup } from '@angular/forms';
import { ProductService, Product } from '../../services/product.service';
import { finalize } from 'rxjs/operators';

type ProductForm = {
  id: FormControl<number | null>;
  name: FormControl<string>;
  description: FormControl<string>;
  category: FormControl<string>;
  price: FormControl<number>;
};

@Component({
  selector: 'app-product-crud',
  templateUrl: './product-crud.component.html'
})
export class ProductCrudComponent implements OnInit {
  private fb = inject(FormBuilder);
  private api = inject(ProductService);

  products: Product[] = [];

  // ✅ niente uso di this.fb prima dell’inizializzazione, niente TS2729
  form: FormGroup<ProductForm> = this.fb.group({
    id: this.fb.control<number | null>(null),
    name: this.fb.control('', { nonNullable: true, validators: [Validators.required] }),
    description: this.fb.control('', { nonNullable: true }),
    category: this.fb.control('', { nonNullable: true, validators: [Validators.required] }),
    price: this.fb.control(0, { nonNullable: true, validators: [Validators.required, Validators.min(0)] })
  });

  ngOnInit() {
    this.reload();
  }

  reload() {
    this.api.list({ page: 0, size: 100 }).subscribe(res => (this.products = res.content));
  }

  edit(p: Product) {
    this.form.setValue({
      id: p.id ?? null,
      name: p.name ?? '',
      description: p.description ?? '',
      category: p.category ?? '',
      price: p.price ?? 0
    });
  }

  reset() {
    this.form.reset({ id: null, name: '', description: '', category: '', price: 0 });
  }

  save() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    // getRawValue mantiene i tipi non-null delle control nonNullable
    const v = this.form.getRawValue();
    const payload = {
      ...v,
      name: v.name.trim(),
      description: v.description.trim(),
      category: v.category.trim()
    };

    const req$ = v.id != null ? this.api.update(v.id, payload) : this.api.create(payload);

    req$.pipe(
      finalize(() => {
        this.reset();
        this.reload();
      })
    ).subscribe();
  }

  remove(id: number) {
    this.api.delete(id).pipe(finalize(() => this.reload())).subscribe();
  }

  // Utile per *ngFor
  trackById = (_: number, p: Product) => p.id;
}
