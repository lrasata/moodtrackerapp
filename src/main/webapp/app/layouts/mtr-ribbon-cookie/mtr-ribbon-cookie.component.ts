import { Component, OnInit } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';

/*
 * @author lrasata
 */
@Component({
  selector: 'jhi-mtr-ribbon-cookie',
  templateUrl: './mtr-ribbon-cookie.component.html',
})
export class MtrRibbonCookieComponent implements OnInit {
  ribbonDisplay = '';
  ribbonKey = 'cookienotice';

  constructor(private cookieService: CookieService) {}

  ngOnInit(): void {
    const keyValue = this.getCookie(this.ribbonKey);
    if (keyValue && keyValue === '1') {
      this.ribbonDisplay = 'none';
    }
  }

  closeRibbon(): void {
    this.ribbonDisplay = 'none';
    this.cookieService.set(this.ribbonKey, '1');
  }

  getCookie(key: string): string {
    return this.cookieService.get(key);
  }
}
