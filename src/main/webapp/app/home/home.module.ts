import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { CookieService } from 'ngx-cookie-service';
import { MtrMoodHistoryService } from 'app/entities/mood-history/service/mtr-mood-history.service';

// @author lrasata

@NgModule({
  imports: [SharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent],
  providers: [CookieService, MtrMoodHistoryService],
})
export class HomeModule {}
