/********************************************************************************
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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
package qualitynotification.base.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
@Data
@Builder
public class QualityNotificationMessageResponse {

    private String id;
    private String createdBy;
    private String createdByName;
    private String sendTo;
    private String sendToName;
    private String contractAgreementId;
    private String notificationReferenceId;
    private Instant targetDate;
    @Enumerated(EnumType.STRING)
    private QualityNotificationSeverityResponse severity;
    private String edcNotificationId;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String messageId;
    @Enumerated(EnumType.STRING)
    private QualityNotificationStatusResponse status;
    @Schema(example = "EDC not reachable", maxLength = 255)
    @Size(max = 255)
    private String errorMessage;
}
