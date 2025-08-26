import { Component, inject } from '@angular/core';
import { FormBuilder, Validators, FormControl, FormGroup } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { finalize, take } from 'rxjs/operators';

type LoginForm = {
  username: FormControl<string>;
  password: FormControl<string>;
};

type RegisterForm = {
  username: FormControl<string>;
  email: FormControl<string>;
  password: FormControl<string>;
};

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {
  // ✅ inject invece del constructor → niente TS2729 con i field initializers
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);

  tab: 'login' | 'register' = 'login';
  loading = false;
  error?: string;

  // ✅ typed reactive forms (nonNullable)
  formLogin: FormGroup<LoginForm> = this.fb.group({
    username: this.fb.control('', { nonNullable: true, validators: [Validators.required] }),
    password: this.fb.control('', { nonNullable: true, validators: [Validators.required] })
  });

  formReg: FormGroup<RegisterForm> = this.fb.group({
    username: this.fb.control('', { nonNullable: true, validators: [Validators.required] }),
    email: this.fb.control('', { nonNullable: true, validators: [Validators.required, Validators.email] }),
    password: this.fb.control('', { nonNullable: true, validators: [Validators.required, Validators.minLength(6)] })
  });

  submitLogin() {
    if (this.formLogin.invalid) {
      this.formLogin.markAllAsTouched();
      return;
    }
    this.error = undefined;
    this.loading = true;

    const { username, password } = this.formLogin.getRawValue();
    this.auth.login(username.trim(), password)
      .pipe(take(1), finalize(() => (this.loading = false)))
      .subscribe({
        next: a => { this.auth.saveAuth(a); this.router.navigate(['/']); },
        error: e => this.error = e?.error?.message ?? 'Credenziali non valide'
      });
  }

  submitReg() {
    if (this.formReg.invalid) {
      this.formReg.markAllAsTouched();
      return;
    }
    this.error = undefined;
    this.loading = true;

    const { username, email, password } = this.formReg.getRawValue();
    this.auth.register(username.trim(), email.trim(), password)
      .pipe(take(1), finalize(() => (this.loading = false)))
      .subscribe({
        next: a => { this.auth.saveAuth(a); this.router.navigate(['/']); },
        error: e => this.error = e?.error?.message ?? 'Registrazione fallita'
      });
  }
}
