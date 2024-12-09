import { Injectable } from '@angular/core';
import { CommonService } from './common.service';
import { Cliente } from '../models/cliente';
import { BASE_ENDPOINT } from '../config/app';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { saveAs } from 'file-saver';

@Injectable({
  providedIn: 'root'
})
export class ClienteService extends CommonService<Cliente>{

  protected override  baseEndPoint = BASE_ENDPOINT + '/api/clientes';

  constructor(http: HttpClient) {
   super(http);
   }  

  public advancedSearch(filters: { [key: string]: string }, page: string, size: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);
  
    // Agregar los filtros dinámicamente
    Object.keys(filters).forEach(key => {
      if (filters[key]) {
        params = params.set(key, filters[key]);
      }
    });
  
    return this.http.get<any>(`${this.baseEndPoint}/search`, { params });
  }
  

  downloadClientesCSV() {
    this.http.get(`${this.baseEndPoint}/export`, { responseType: 'blob' })
      .subscribe((data: Blob) => {
        const blob = new Blob([data], { type: 'text/csv;charset=utf-8;' });
        saveAs(blob, 'clientes.csv');
      }, (error) => {
        console.error('Error al descargar el archivo CSV', error);
      });
  }
}
