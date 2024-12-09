import { Component, OnInit } from '@angular/core';
import { Cliente } from '../../models/cliente';
import { ClienteService } from '../../services/cliente.service';
import { CommonFormComponent } from '../common-form.component';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-cliente-form',
  standalone: false,
  
  templateUrl: './cliente-form.component.html',
  styleUrl: './cliente-form.component.css'
})
export class ClienteFormComponent extends CommonFormComponent<Cliente, ClienteService> implements OnInit {

  constructor(service: ClienteService,
    router: Router,
    route: ActivatedRoute,
    private _snackBar: MatSnackBar) {
    super(service, router, route);
    this.titulo = "Crear Clientes";
    this.model = new Cliente();
    this.redirect = '/clientes';
    this.nombreModel = Cliente.name;
  }  


}

