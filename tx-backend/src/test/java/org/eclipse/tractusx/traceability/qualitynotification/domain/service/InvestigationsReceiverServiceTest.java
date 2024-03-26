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

package org.eclipse.tractusx.traceability.qualitynotification.domain.service;

import org.eclipse.tractusx.traceability.common.mapper.NotificationMessageMapper;
import org.eclipse.tractusx.traceability.common.mapper.QualityNotificationMapper;
import org.eclipse.tractusx.traceability.common.model.BPN;
import org.eclipse.tractusx.traceability.qualitynotification.domain.notification.repository.NotificationRepository;
import org.eclipse.tractusx.traceability.qualitynotification.domain.base.model.QualityNotification;
import org.eclipse.tractusx.traceability.qualitynotification.domain.base.model.QualityNotificationAffectedPart;
import org.eclipse.tractusx.traceability.qualitynotification.domain.base.model.QualityNotificationMessage;
import org.eclipse.tractusx.traceability.qualitynotification.domain.base.model.QualityNotificationSeverity;
import org.eclipse.tractusx.traceability.qualitynotification.domain.base.model.QualityNotificationStatus;
import org.eclipse.tractusx.traceability.qualitynotification.domain.base.model.QualityNotificationType;
import org.eclipse.tractusx.traceability.qualitynotification.domain.notification.service.NotificationReceiverService;
import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.edc.model.EDCNotification;
import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.edc.model.EDCNotificationFactory;
import org.eclipse.tractusx.traceability.testdata.InvestigationTestDataFactory;
import org.eclipse.tractusx.traceability.testdata.NotificationTestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class InvestigationsReceiverServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMessageMapper mockNotificationMapper;

    @Mock
    private QualityNotificationMapper mockQualityNotificationMapper;


    @InjectMocks
    private NotificationReceiverService service;


    @Test
    @DisplayName("Test testhandleReceiveValidSentNotification sent is valid")
    void testhandleReceiveValidSentNotification() {

        // Given
        List<QualityNotificationAffectedPart> affectedParts = List.of(new QualityNotificationAffectedPart("partId"));
        QualityNotificationType notificationType = QualityNotificationType.INVESTIGATION;
        QualityNotificationMessage notification = QualityNotificationMessage.builder()
                .id("123")
                .notificationReferenceId("id123")
                .createdBy("senderBPN")
                .createdByName("senderManufacturerName")
                .sendTo("recipientBPN")
                .sendToName("receiverManufacturerName")
                .contractAgreementId("agreement")
                .description("123")
                .notificationStatus(QualityNotificationStatus.SENT)
                .affectedParts(affectedParts)
                .severity(QualityNotificationSeverity.MINOR)
                .edcNotificationId("123")
                .type(notificationType)
                .targetDate(Instant.now())
                .messageId("messageId")
                .build();


        QualityNotification investigationTestData = InvestigationTestDataFactory.createInvestigationTestData(QualityNotificationStatus.RECEIVED, "recipientBPN");
        QualityNotificationMessage notificationTestData = NotificationTestDataFactory.createNotificationTestData();
        EDCNotification edcNotification = EDCNotificationFactory.createEdcNotification(
                "it", notification);

        when(mockNotificationMapper.toNotification(edcNotification, notificationType)).thenReturn(notificationTestData);
        when(mockQualityNotificationMapper.toQualityNotification(any(BPN.class), anyString(), any(QualityNotificationMessage.class), any(QualityNotificationType.class))).thenReturn(investigationTestData);

        // When
        service.handleReceive(edcNotification, notificationType);
        // Then
        Mockito.verify(notificationRepository).saveQualityNotificationEntity(investigationTestData);
    }

    @Test
    @DisplayName("Test testHandleNotificationUpdateValidAcknowledgeNotificationTransition is valid")
    void testHandleNotificationUpdateValidAcknowledgeNotificationTransition() {

        // Given
        List<QualityNotificationAffectedPart> affectedParts = List.of(new QualityNotificationAffectedPart("partId"));

        QualityNotificationType notificationType = QualityNotificationType.INVESTIGATION;
        QualityNotificationMessage notification = QualityNotificationMessage.builder()
                .id("123")
                .notificationReferenceId("id123")
                .createdBy("senderBPN")
                .createdByName("senderManufacturerName")
                .sendTo("recipientBPN")
                .sendToName("receiverManufacturerName")
                .contractAgreementId("agreement")
                .description("123")
                .notificationStatus(QualityNotificationStatus.ACKNOWLEDGED)
                .affectedParts(affectedParts)
                .type(notificationType)
                .severity(QualityNotificationSeverity.MINOR)
                .edcNotificationId("123")
                .targetDate(Instant.now())
                .messageId("messageId")
                .build();


        QualityNotification investigationTestData = InvestigationTestDataFactory.createInvestigationTestData(QualityNotificationStatus.RECEIVED, "recipientBPN");
        QualityNotificationMessage notificationTestData = NotificationTestDataFactory.createNotificationTestData();
        EDCNotification edcNotification = EDCNotificationFactory.createEdcNotification(
                "it", notification);

        when(mockNotificationMapper.toNotification(edcNotification, notificationType)).thenReturn(notificationTestData);
        when(notificationRepository.findByEdcNotificationId(edcNotification.getNotificationId())).thenReturn(Optional.of(investigationTestData));

        // When
        service.handleUpdate(edcNotification, notificationType);
        // Then
        Mockito.verify(notificationRepository).updateQualityNotificationEntity(investigationTestData);
    }

    @Test
    @DisplayName("Test testhandleUpdateValidDeclineNotificationTransition is valid")
    void testhandleUpdateValidDeclineNotificationTransition() {

        // Given
        List<QualityNotificationAffectedPart> affectedParts = List.of(new QualityNotificationAffectedPart("partId"));
        QualityNotificationType notificationType = QualityNotificationType.INVESTIGATION;
        QualityNotificationMessage notification = QualityNotificationMessage.builder()
                .id("123")
                .notificationReferenceId("id123")
                .createdBy("senderBPN")
                .createdByName("senderManufacturerName")
                .sendTo("recipientBPN")
                .sendToName("receiverManufacturerName")
                .contractAgreementId("agreement")
                .description("123")
                .notificationStatus(QualityNotificationStatus.DECLINED)
                .affectedParts(affectedParts)
                .severity(QualityNotificationSeverity.MINOR)
                .edcNotificationId("123")
                .type(notificationType)
                .targetDate(Instant.now())
                .messageId("messageId")
                .build();

        QualityNotification investigationTestData = InvestigationTestDataFactory.createInvestigationTestData(QualityNotificationStatus.ACKNOWLEDGED, "recipientBPN");
        QualityNotificationMessage notificationTestData = NotificationTestDataFactory.createNotificationTestData();
        EDCNotification edcNotification = EDCNotificationFactory.createEdcNotification(
                "it", notification);

        when(mockNotificationMapper.toNotification(edcNotification, notificationType)).thenReturn(notificationTestData);
        when(notificationRepository.findByEdcNotificationId(edcNotification.getNotificationId())).thenReturn(Optional.of(investigationTestData));

        // When
        service.handleUpdate(edcNotification, notificationType);
        // Then
        Mockito.verify(notificationRepository).updateQualityNotificationEntity(investigationTestData);
    }

    @Test
    @DisplayName("Test testhandleUpdateValidAcknowledgeNotification is valid")
    void testhandleUpdateValidAcceptedNotificationTransition() {

        // Given
        List<QualityNotificationAffectedPart> affectedParts = List.of(new QualityNotificationAffectedPart("partId"));
        QualityNotificationType notificationType = QualityNotificationType.INVESTIGATION;
        QualityNotificationMessage notification = QualityNotificationMessage.builder()
                .id("123")
                .notificationReferenceId("id123")
                .createdBy("senderBPN")
                .createdByName("senderManufacturerName")
                .sendTo("recipientBPN")
                .sendToName("receiverManufacturerName")
                .contractAgreementId("agreement")
                .description("123")
                .notificationStatus(QualityNotificationStatus.ACCEPTED)
                .affectedParts(affectedParts)
                .severity(QualityNotificationSeverity.MINOR)
                .edcNotificationId("123")
                .type(notificationType)
                .targetDate(Instant.now())
                .messageId("messageId")
                .build();

        QualityNotification investigationTestData = InvestigationTestDataFactory.createInvestigationTestData(QualityNotificationStatus.ACKNOWLEDGED, "recipientBPN");
        QualityNotificationMessage notificationTestData = NotificationTestDataFactory.createNotificationTestData();
        EDCNotification edcNotification = EDCNotificationFactory.createEdcNotification(
                "it", notification);

        when(mockNotificationMapper.toNotification(edcNotification, notificationType)).thenReturn(notificationTestData);
        when(notificationRepository.findByEdcNotificationId(edcNotification.getNotificationId())).thenReturn(Optional.of(investigationTestData));

        // When
        service.handleUpdate(edcNotification, notificationType);
        // Then
        Mockito.verify(notificationRepository).updateQualityNotificationEntity(investigationTestData);
    }

    @Test
    @DisplayName("Test testhandleUpdateValidAcknowledgeNotification is valid")
    void testhandleUpdateValidCloseNotificationTransition() {

        // Given
        List<QualityNotificationAffectedPart> affectedParts = List.of(new QualityNotificationAffectedPart("partId"));
        QualityNotificationType notificationType = QualityNotificationType.INVESTIGATION;
        QualityNotificationMessage notification = QualityNotificationMessage.builder()
                .id("123")
                .notificationReferenceId("id123")
                .createdBy("senderBPN")
                .createdByName("senderManufacturerName")
                .sendTo("recipientBPN")
                .sendToName("receiverManufacturerName")
                .contractAgreementId("agreement")
                .description("123")
                .notificationStatus(QualityNotificationStatus.CLOSED)
                .affectedParts(affectedParts)
                .severity(QualityNotificationSeverity.MINOR)
                .edcNotificationId("123")
                .type(notificationType)
                .targetDate(Instant.now())
                .messageId("messageId")
                .build();

        QualityNotification investigationTestData = InvestigationTestDataFactory.createInvestigationTestData(QualityNotificationStatus.ACKNOWLEDGED, "senderBPN");
        QualityNotificationMessage notificationTestData = NotificationTestDataFactory.createNotificationTestData();
        EDCNotification edcNotification = EDCNotificationFactory.createEdcNotification(
                "it", notification);

        when(mockNotificationMapper.toNotification(edcNotification, notificationType)).thenReturn(notificationTestData);
        when(notificationRepository.findByEdcNotificationId(edcNotification.getNotificationId())).thenReturn(Optional.of(investigationTestData));

        // When
        service.handleUpdate(edcNotification, notificationType);
        // Then
        Mockito.verify(notificationRepository).updateQualityNotificationEntity(investigationTestData);
    }

}

