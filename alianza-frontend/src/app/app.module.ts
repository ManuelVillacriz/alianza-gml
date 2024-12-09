import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration, withEventReplay } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SharedModule } from './shared/shared.module';
import { HttpClientModule, provideHttpClient, withFetch } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { ClienteComponent } from './components/cliente/cliente.component';
import { ClienteFormComponent } from './components/cliente/cliente-form.component';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTreeModule } from '@angular/material/tree';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDivider } from '@angular/material/divider';
import { MatTableModule } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';

@NgModule({
  declarations: [
    AppComponent,
    ClienteComponent,
    ClienteFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatSnackBarModule,
    SharedModule,
    HttpClientModule,
    FormsModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatTreeModule,
    MatIconModule,
    MatButtonModule,
    MatDivider,    
    MatTableModule,
    MatSort

  ],
  providers: [
    provideClientHydration(withEventReplay()),
    provideHttpClient(withFetch()),
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
