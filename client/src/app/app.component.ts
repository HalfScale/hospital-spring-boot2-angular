import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { LazyLoaderService } from './services/lazy-loader.service';

declare var Lazy: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'client';

  constructor(private readonly svc: LazyLoaderService,
    @Inject(DOCUMENT) private readonly document:any) {}

  ngOnInit() {
    this.svc.load().subscribe(_ => {
      if(!Lazy) {
        Lazy = this.document.defaultView.Lazy;
      }
      this.setupLazy();
    });
  }

  setupLazy() {
    if(!Lazy) {
      return;
    }
  }
}
