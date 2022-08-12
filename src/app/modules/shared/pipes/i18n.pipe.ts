/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import { Pipe, PipeTransform } from '@angular/core';
import { I18nMessage } from '@shared/model/i18n-message';
import { I18NextPipe, PipeOptions } from 'angular-i18next';

@Pipe({ name: 'i18n', pure: false })
export class I18nPipe implements PipeTransform {
  constructor(private readonly i18NextPipe: I18NextPipe) {}

  public transform(key: I18nMessage, options?: PipeOptions): string {
    if (!key) {
      return '';
    }

    if (typeof key !== 'string') {
      options = key.values;
      key = key.id;
    }

    return this.i18NextPipe.transform(key, options);
  }
}
