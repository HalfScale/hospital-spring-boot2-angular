import { Injectable, Inject } from '@angular/core';
import { ReplaySubject, Observable, forkJoin } from 'rxjs';
import { DOCUMENT } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class LazyLoaderService {
  private loadedLibraries: { [url: string]: ReplaySubject<any> } = {};

  constructor(@Inject(DOCUMENT) private readonly document: any) { }

  load(): Observable<any> {
    return forkJoin([
      this.loadScript('assets/js/main.js'),
      // this.loadScript('assets/js/jquery-2.1.0.min.js'),
      // this.loadScript('assets/js/popper.js'),
      // this.loadScript('assets/js/bootstrap.min.js'),
      // this.loadScript('assets/js/scrollreveal.min.js'),
      // this.loadScript('assets/js/waypoints.min.js'),
      // this.loadScript('assets/js/jquery.counterup.min.js'),
      // this.loadScript('assets/js/imgfix.min.js'),
      // this.loadScript('assets/js/custom.js'),
    ]);
  }

  private loadScript(url: string): Observable<any> {
    if(this.loadedLibraries[url]) {
      return this.loadedLibraries[url].asObservable();
    }

    this.loadedLibraries[url] = new ReplaySubject();

    const script = this.document.createElement('script');
    script.type = 'text/javascript';
    script.async = true;
    script.src = url;
    script.onLoad = () => {
      this.loadedLibraries[url].next();
      this.loadedLibraries[url].complete();
    };

    this.document.body.appendChild(script);

    return this.loadedLibraries[url].asObservable();
  }
}
