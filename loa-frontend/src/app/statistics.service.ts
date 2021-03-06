import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {
  constructor(private http: HttpClient) {
  }

  getStatistics(): Observable<any> {
    return this.http.get('/dashboard/statistics');
  }
}
