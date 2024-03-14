/********************************************************************************
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

package org.eclipse.tractusx.traceability.qualitynotification.domain.alert.service;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.traceability.common.properties.TraceabilityProperties;
import org.eclipse.tractusx.traceability.qualitynotification.domain.alert.model.exception.AlertNotFoundException;
import org.eclipse.tractusx.traceability.qualitynotification.domain.base.AlertRepository;
import org.eclipse.tractusx.traceability.qualitynotification.domain.base.service.AbstractQualityNotificationService;
import org.eclipse.tractusx.traceability.qualitynotification.domain.base.service.NotificationPublisherService;
import org.eclipse.tractusx.traceability.qualitynotification.domain.repository.QualityNotificationRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service("alertServiceImpl")
public class AlertServiceImpl extends AbstractQualityNotificationService {


    private final AlertRepository alertRepository;

    public AlertServiceImpl(TraceabilityProperties traceabilityProperties, AlertRepository alertRepository, NotificationPublisherService notificationPublisherService) {
        super(traceabilityProperties, notificationPublisherService);
        this.alertRepository = alertRepository;
    }

    @Override
    protected QualityNotificationRepository getQualityNotificationRepository() {
        return alertRepository;
    }

    @Override
    public RuntimeException getNotFoundException(String message) {
        return new AlertNotFoundException(message);
    }
}
