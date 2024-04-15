/********************************************************************************
 * Copyright (c) 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

import { Injectable } from '@angular/core';
import { Pagination } from '@core/model/pagination.model';
import { Ess } from '@page/ess/model/ess.model';
import { State } from '@shared/model/state';
import { View } from '@shared/model/view.model';
import { Observable } from 'rxjs';

@Injectable()
export class EssState {
  private readonly _essList$ = new State<View<Pagination<Ess>>>({ loader: true });
  private readonly _partsAsPlanned4Ess$ = new State<View<Pagination<Ess>>>({ loader: true });

  public get essList$(): Observable<View<Pagination<Ess>>> {
    return this._essList$.observable;
  }

  public set essList({ data, loader, error }: View<Pagination<Ess>>) {
    const partsView: View<Pagination<Ess>> = { data, loader, error };
    this._essList$.update(partsView);
  }

  public get essList(): View<Pagination<Ess>> {
    return this._essList$.snapshot;
  }

  public get partsAsPlanned4Ess$(): Observable<View<Pagination<Ess>>> {
    return this._partsAsPlanned4Ess$.observable;
  }

  public set partsAsPlanned4Ess({ data, loader, error }: View<Pagination<Ess>>) {
    const partsView: View<Pagination<Ess>> = { data, loader, error };
    this._partsAsPlanned4Ess$.update(partsView);
  }

  public get partsAsPlanned4Ess(): View<Pagination<Ess>> {
    return this._partsAsPlanned4Ess$.snapshot;
  }

}
