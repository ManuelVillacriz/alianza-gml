import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClienteComponent } from './components/cliente/cliente.component';
import { ClienteFormComponent } from './components/cliente/cliente-form.component';

const routes: Routes = [
  {path: '', pathMatch: 'full', redirectTo: 'clientes'},
  {path:'clientes', component: ClienteComponent},
  {path:'clientes/form', component: ClienteFormComponent},
  {path:'clientes/form/:id', component: ClienteFormComponent},
]
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
