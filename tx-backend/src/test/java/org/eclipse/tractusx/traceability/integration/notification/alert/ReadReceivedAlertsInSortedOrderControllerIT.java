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
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

package org.eclipse.tractusx.traceability.integration.notification.alert;

import common.FilterAttribute;
import common.FilterValue;
import io.restassured.http.ContentType;
import notification.request.NotificationFilter;
import notification.request.NotificationRequest;
import org.eclipse.tractusx.traceability.common.model.SearchCriteriaOperator;
import org.eclipse.tractusx.traceability.common.model.SearchCriteriaStrategy;
import org.eclipse.tractusx.traceability.integration.IntegrationTestSpecification;
import org.eclipse.tractusx.traceability.integration.common.support.AlertNotificationsSupport;
import org.eclipse.tractusx.traceability.integration.common.support.BpnSupport;
import org.eclipse.tractusx.traceability.notification.domain.base.model.NotificationSide;
import org.eclipse.tractusx.traceability.notification.infrastructure.notification.model.NotificationMessageEntity;
import org.eclipse.tractusx.traceability.testdata.AlertTestDataFactory;
import org.hamcrest.Matchers;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.eclipse.tractusx.traceability.common.security.JwtRole.ADMIN;
import static org.hamcrest.Matchers.equalTo;

class ReadReceivedAlertsInSortedOrderControllerIT extends IntegrationTestSpecification {

    @Autowired
    AlertNotificationsSupport alertNotificationsSupport;

    @Autowired
    BpnSupport bpnSupport;

    @Test
    void givenSortByCreatedDateProvided_whenGetAlerts_thenReturnAlertsProperlySorted() throws JoseException {
        // Given
        String sortString = "createdDate,desc";
        String testBpn = bpnSupport.testBpn();

        NotificationMessageEntity[] alertNotificationEntities = AlertTestDataFactory.createReceiverMajorityAlertNotificationEntitiesTestData(testBpn);
        alertNotificationsSupport.storedAlertNotifications(alertNotificationEntities);

        NotificationFilter filter = NotificationFilter.builder()
                .channel(FilterAttribute.builder()
                        .value(List.of(FilterValue.builder().value(NotificationSide.RECEIVER.name()).strategy(SearchCriteriaStrategy.EQUAL.name()).build()))
                        .operator(SearchCriteriaOperator.AND.name())
                        .build())
                .build();

        // Then
        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .body(NotificationRequest.builder().page(0).size(10).sort(List.of(sortString)).notificationFilter(filter).build())
                .contentType(ContentType.JSON)
                .when()
                .post("/api/notifications/filter")
                .then()
                .log().all()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4))
                .body("content.description",
                        Matchers.containsInRelativeOrder("Second Alert on Asset2", "Fourth Alert on Asset4",
                                "Third Alert on Asset3", "First Alert on Asset1"));
    }

    @Test
    void givenSortByDescriptionProvided_whenGetAlerts_thenReturnAlertsProperlySorted() throws JoseException {
        // Given
        String sortString = "description,desc";
        String testBpn = bpnSupport.testBpn();

        NotificationMessageEntity[] alertNotificationEntities = AlertTestDataFactory.createReceiverMajorityAlertNotificationEntitiesTestData(testBpn);
        alertNotificationsSupport.storedAlertNotifications(alertNotificationEntities);

        NotificationFilter filter = NotificationFilter.builder()
                .channel(FilterAttribute.builder()
                        .value(List.of(FilterValue.builder().value(NotificationSide.RECEIVER.name()).strategy(SearchCriteriaStrategy.EQUAL.name()).build()))
                        .operator(SearchCriteriaOperator.AND.name())
                        .build())
                .build();

        // Then
        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .body(NotificationRequest.builder().page(0).size(10).sort(List.of(sortString)).notificationFilter(filter).build())
                .contentType(ContentType.JSON)
                .when()
                .post("/api/notifications/filter")
                .then()
                .log().all()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4))
                .body("content.description",
                        Matchers.containsInRelativeOrder("Third Alert on Asset3",
                                "Second Alert on Asset2", "Fourth Alert on Asset4", "First Alert on Asset1"));
    }

    @Test
    void givenSortByStatusProvided_whenGetAlerts_thenReturnAlertsProperlySorted() throws JoseException {
        // Given
        String sortString = "status,asc";
        String testBpn = bpnSupport.testBpn();

        NotificationMessageEntity[] alertNotificationEntities = AlertTestDataFactory.createReceiverMajorityAlertNotificationEntitiesTestData(testBpn);
        alertNotificationsSupport.storedAlertNotifications(alertNotificationEntities);

        NotificationFilter filter = NotificationFilter.builder()
                .channel(FilterAttribute.builder()
                        .value(List.of(FilterValue.builder().value(NotificationSide.RECEIVER.name()).strategy(SearchCriteriaStrategy.EQUAL.name()).build()))
                        .operator(SearchCriteriaOperator.AND.name())
                        .build())
                .build();

        // Then
        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .body(NotificationRequest.builder().page(0).size(10).sort(List.of(sortString)).notificationFilter(filter).build())
                .contentType(ContentType.JSON)
                .when()
                .post("/api/notifications/filter")
                .then()
                .log().all()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4))
                .body("content.status", Matchers.containsInRelativeOrder("ACCEPTED", "ACCEPTED", "ACKNOWLEDGED", "RECEIVED"));
    }

    @Test
    void givenSortBySeverityProvided_whenGetAlerts_thenReturnAlertsProperlySorted() throws JoseException {
        // Given
        String sortString = "severity,asc";
        String testBpn = bpnSupport.testBpn();

        NotificationMessageEntity[] alertNotificationEntities = AlertTestDataFactory.createReceiverMajorityAlertNotificationEntitiesTestData(testBpn);
        alertNotificationsSupport.storedAlertNotifications(alertNotificationEntities);

        NotificationFilter filter = NotificationFilter.builder()
                .channel(FilterAttribute.builder()
                        .value(List.of(FilterValue.builder().value(NotificationSide.RECEIVER.name()).strategy(SearchCriteriaStrategy.EQUAL.name()).build()))
                        .operator(SearchCriteriaOperator.AND.name())
                        .build())
                .build();

        // Then
        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .body(NotificationRequest.builder().page(0).size(10).sort(List.of(sortString)).notificationFilter(filter).build())
                .contentType(ContentType.JSON)
                .when()
                .post("/api/notifications/filter")
                .then()
                .log().all()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4))
                .body("content.severity", Matchers.containsInRelativeOrder("CRITICAL", "LIFE-THREATENING", "MAJOR", "MINOR"));
    }

    @Test
    void givenInvalidSort_whenGetCreated_thenBadRequest() throws JoseException {
        // Given
        String sortString = "createdDate,failure";

        NotificationFilter filter = NotificationFilter.builder()
                .channel(FilterAttribute.builder()
                        .value(List.of(FilterValue.builder().value(NotificationSide.RECEIVER.name()).strategy(SearchCriteriaStrategy.EQUAL.name()).build()))
                        .operator(SearchCriteriaOperator.AND.name())
                        .build())
                .build();

        // Then
        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .body(NotificationRequest.builder().page(0).size(10).sort(List.of(sortString)).notificationFilter(filter).build())
                .contentType(ContentType.JSON)
                .when()
                .post("/api/notifications/filter")
                .then()
                .statusCode(400)
                .body("message", Matchers.is(
                        "Invalid sort param provided sort=createdDate,failure expected format is following sort=parameter,order"
                ));
    }

    @Test
    void givenSortBySendToProvided_whenGetAlerts_thenReturnAlertsProperlySorted() throws JoseException {
        // Given
        String sortString = "sendTo,desc";
        String testBpn = bpnSupport.testBpn();

        NotificationMessageEntity[] alertNotificationEntities = AlertTestDataFactory.createReceiverMajorityAlertNotificationEntitiesTestData(testBpn);
        alertNotificationsSupport.storedAlertNotifications(alertNotificationEntities);

        NotificationFilter filter = NotificationFilter.builder()
                .channel(FilterAttribute.builder()
                        .value(List.of(FilterValue.builder().value(NotificationSide.RECEIVER.name()).strategy(SearchCriteriaStrategy.EQUAL.name()).build()))
                        .operator(SearchCriteriaOperator.AND.name())
                        .build())
                .build();

        // Then
        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .body(NotificationRequest.builder().page(0).size(10).sort(List.of(sortString)).notificationFilter(filter).build())
                .contentType(ContentType.JSON)
                .when()
                .post("/api/notifications/filter")
                .then()
                .log().all()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4))
                .body("content.messages*.sendTo.flatten()", Matchers.containsInRelativeOrder("BPNL000000000003", "BPNL000000000002", "BPNL000000000001", "BPNL000000000001"));
    }

    @Test
    void givenSortByTargetDateProvided_whenGetAlerts_thenReturnAlertsProperlySorted() throws JoseException {
        // Given
        String sortString = "targetDate,asc";
        String testBpn = bpnSupport.testBpn();

        NotificationMessageEntity[] alertNotificationEntities = AlertTestDataFactory.createReceiverMajorityAlertNotificationEntitiesTestData(testBpn);
        alertNotificationsSupport.storedAlertNotifications(alertNotificationEntities);

        NotificationFilter filter = NotificationFilter.builder()
                .channel(FilterAttribute.builder()
                        .value(List.of(FilterValue.builder().value(NotificationSide.RECEIVER.name()).strategy(SearchCriteriaStrategy.EQUAL.name()).build()))
                        .operator(SearchCriteriaOperator.AND.name())
                        .build())
                .build();

        // Then
        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .body(NotificationRequest.builder().page(0).size(10).sort(List.of(sortString)).notificationFilter(filter).build())
                .contentType(ContentType.JSON)
                .when()
                .post("/api/notifications/filter")
                .then()
                .log().all()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4))
                .body("content.sendToName[0]", Matchers.anyOf(equalTo("OEM1"), equalTo("OEM2")))
                .body("content.sendToName[1]", Matchers.anyOf(equalTo("OEM1"), equalTo("OEM2")))
                .body("content.sendToName[2]", Matchers.anyOf(equalTo("OEM1"), equalTo("OEM3")))
                .body("content.sendToName[3]", Matchers.anyOf(equalTo("OEM1"), equalTo("OEM3")));
    }
}
