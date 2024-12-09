import { Component, OnInit, ViewChild } from '@angular/core';
import { Cliente } from '../../models/cliente';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { ClienteService } from '../../services/cliente.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-cliente',
  standalone: false,
  
  templateUrl: './cliente.component.html',
  styleUrl: './cliente.component.css'
})

export class ClienteComponent implements OnInit {
  titulo: string = 'Listado Clientes';
  nombreModel: string = Cliente.name;
  lista: Cliente[] = [];
  
  // Filtros avanzados
  filterSharedKey: string = '';
  filterName: string = '';
  filterLastName: string = '';
  filterEmail: string = '';
  filterPhone: string = '';

  paginaActual: number = 0;
  totalRegistros: number = 0;
  totalPorPagina: number = 5;

  dataSource: MatTableDataSource<Cliente> = new MatTableDataSource<Cliente>();
  @ViewChild(MatPaginator) paginator: MatPaginator;
  pageSizeOptions: number[] = [5, 10, 20, 50];
  mostrarColumnas: string[] = ['sharedKey', 'name', 'lastName', 'email', 'phone', 'dataAdded', 'acciones'];

  constructor(private service: ClienteService) {
    this.dataSource.filterPredicate = (data: Cliente, filter: string) => {
      return data.sharedKey.toLowerCase().includes(filter);
    };
  }

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator;
    this.calcularRangos();
  }

  paginar(event: PageEvent): void {
    this.paginaActual = event.pageIndex;
    this.totalPorPagina = event.pageSize;
    this.calcularRangos();
  }

  filter(event: Event, field: keyof Cliente): void {
    const input = event.target as HTMLInputElement;
    this[field] = input.value;
    this.paginaActual = 0;
    this.calcularRangos();
  }

  calcularRangos(): void {
    const filters = {
      sharedKey: this.filterSharedKey,
      name: this.filterName,
      lastName: this.filterLastName,
      email: this.filterEmail,
      phone: this.filterPhone
    };

    this.service.advancedSearch(filters, this.paginaActual.toString(), this.totalPorPagina.toString())
      .subscribe(p => {
        this.lista = p.content;
        this.totalRegistros = p.totalElements;
        this.dataSource.data = this.lista;
      });
  } 

  public eliminar(cliente: Cliente): void {
    Swal.fire({
      title: 'Cuidado',
      text: `¿Seguro que desea eliminar a ${cliente.sharedKey}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Si, Eliminar!'
    }).then((result) => {
      if (result.isConfirmed) {
        this.service.eliminar(cliente.id).subscribe(() => {
          this.calcularRangos();
          Swal.fire('Eliminado:', `${this.nombreModel} ${cliente.sharedKey} eliminado con exito`, 'success');
        });
      }
    });
  }

  downloadCSV(): void {
    this.service.downloadClientesCSV();
  }
}