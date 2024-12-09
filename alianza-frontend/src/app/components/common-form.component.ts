import { Component, Directive, OnInit } from '@angular/core';
import { Generic } from '../models/generic';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { CommonService } from '../services/common.service';

@Directive()
export class CommonFormComponent<E extends Generic,S extends CommonService<E>> implements OnInit {

  titulo: string;
  model: E;
  error: any;
  protected redirect: string;
  protected nombreModel: string;

  constructor(protected service: S,
    protected router: Router,
    protected route: ActivatedRoute){}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id: string | null = params.get('id');
      console.log(id);
      if(id){
        this.service.ver(id).subscribe(model => {
          this.model = model;
          this.titulo = 'Editar ' + this.nombreModel;
        });
      }
    })
  }

  public crear(): void{
    this.service.crear(this.model).subscribe(model => {
      console.log(model);
      Swal.fire('Nuevo:',`${this.nombreModel} creado con exito`,'success');
      this.router.navigate([this.redirect]);
    }, err => {
      console.log(this.error);
      if(err.status === 400){
        this.error=err.error;
        console.log(this.error);
      }

      if(err.status === 409){
        const errorMessage = err.error?.message || 'Error desconocido';
        Swal.fire('Error:', errorMessage, 'error');
      }

    });
  }

  public editar(): void{
    this.service.editar(this.model).subscribe(model => {
      console.log(model);
      Swal.fire('Modificado:',`${this.nombreModel} editado con exito`,'success');
      this.router.navigate([this.redirect]);
    }, err => {
      console.log(this.error);
      if(err.status === 400){
        this.error=err.error;
        console.log(this.error);
      }
    });
  }


}
