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
package org.eclipse.tractusx.traceability.integration.notification.alert;

import io.restassured.http.ContentType;
import notification.request.*;
import org.eclipse.tractusx.traceability.common.request.OwnPageable;
import org.eclipse.tractusx.traceability.common.request.PageableFilterRequest;
import org.eclipse.tractusx.traceability.common.request.SearchCriteriaRequestParam;
import org.eclipse.tractusx.traceability.common.security.JwtRole;
import org.eclipse.tractusx.traceability.integration.IntegrationTestSpecification;
import org.eclipse.tractusx.traceability.integration.common.support.ForbiddenMatcher;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * Integration test for checking correct role-based authorization of
 * the endpoints of {@link org.eclipse.tractusx.traceability.qualitynotification.application.alert.rest.AlertController}.
 */
class AlertControllerAuthorizationIT extends IntegrationTestSpecification {

    private static final String ROOT = "/api/notifications";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @MethodSource("org.eclipse.tractusx.traceability.integration.common.support.RoleSupport#supervisorAndUserRolesAllowed")
    void shouldAllowCreateEndpointOnlyForSpecificRoles(JwtRole role, boolean isAllowed) throws JoseException, JsonProcessingException {
        List<String> partIds = List.of(
                "urn:uuid:0733946c-59c6-41ae-9570-cb43a6e4da01"  // BPN: BPNL00000003CML1
        );
        String description = "at least 15 characters long investigation description";
        NotificationSeverityRequest severity = NotificationSeverityRequest.MINOR;
        String receiverBpn = "BPNL00000003CML1";

        var request = StartNotificationRequest.builder()
                .type(NotificationTypeRequest.ALERT)
                .affectedPartIds(partIds)
                .description(description)
                .severity(severity)
                .receiverBpn(receiverBpn)
                .build();

        given()
                .header(oAuth2Support.jwtAuthorizationWithOptionalRole(role))
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post(ROOT)
                .then()
                .assertThat()
                .statusCode(new ForbiddenMatcher(isAllowed));
    }

    @ParameterizedTest
    @MethodSource("org.eclipse.tractusx.traceability.integration.common.support.RoleSupport#allRolesAllowed")
    void shouldAllowGetCreatedEndpointOnlyForSpecificRoles(JwtRole role, boolean isAllowed) throws JoseException {
        String filterString = "channel,EQUAL,SENDER,AND";

        given()
                .header(oAuth2Support.jwtAuthorizationWithOptionalRole(role))
                .body(new PageableFilterRequest(new OwnPageable(0, 10, List.of()), new SearchCriteriaRequestParam(List.of(filterString))))
                .contentType(ContentType.JSON)
                .when()
                .post(ROOT + "/filter")
                .then()
                .log().all()
                .assertThat()
                .statusCode(new ForbiddenMatcher(isAllowed));
    }

    @ParameterizedTest
    @MethodSource("org.eclipse.tractusx.traceability.integration.common.support.RoleSupport#allRolesAllowed")
    void shouldAllowGetReceivedEndpointOnlyForSpecificRoles(JwtRole role, boolean isAllowed) throws JoseException {
        String filterString = "channel,EQUAL,RECEIVER,AND";

        given()
                .header(oAuth2Support.jwtAuthorizationWithOptionalRole(role))
                .body(new PageableFilterRequest(new OwnPageable(0, 10, List.of()), new SearchCriteriaRequestParam(List.of(filterString))))
                .contentType(ContentType.JSON)
                .when()
                .post(ROOT + "/filter")
                .then()
                .log().all()
                .assertThat()
                .statusCode(new ForbiddenMatcher(isAllowed));
    }

    @ParameterizedTest
    @MethodSource("org.eclipse.tractusx.traceability.integration.common.support.RoleSupport#allRolesAllowed")
    void shouldAllowGetAlertEndpointOnlyForSpecificRoles(JwtRole role, boolean isAllowed) throws JoseException {

        given()
                .header(oAuth2Support.jwtAuthorizationWithOptionalRole(role))
                .contentType(ContentType.JSON)
                .when()
                .get(ROOT + "/123")
                .then()
                .assertThat()
                .statusCode(new ForbiddenMatcher(isAllowed));
    }

    @ParameterizedTest
    @MethodSource("org.eclipse.tractusx.traceability.integration.common.support.RoleSupport#supervisorRoleAllowed")
    void shouldAllowApproveEndpointOnlyForSpecificRoles(JwtRole role, boolean isAllowed) throws JoseException {

        given()
                .header(oAuth2Support.jwtAuthorizationWithOptionalRole(role))
                .contentType(ContentType.JSON)
                .when()
                .post(ROOT + "/123/approve")
                .then()
                .assertThat()
                .statusCode(new ForbiddenMatcher(isAllowed));
    }

    @ParameterizedTest
    @MethodSource("org.eclipse.tractusx.traceability.integration.common.support.RoleSupport#supervisorAndUserRolesAllowed")
    void shouldAllowCancelEndpointOnlyForSpecificRoles(JwtRole role, boolean isAllowed) throws JoseException {

        given()
                .header(oAuth2Support.jwtAuthorizationWithOptionalRole(role))
                .contentType(ContentType.JSON)
                .when()
                .post(ROOT + "/123/cancel")
                .then()
                .assertThat()
                .statusCode(new ForbiddenMatcher(isAllowed));
    }

    @ParameterizedTest
    @MethodSource("org.eclipse.tractusx.traceability.integration.common.support.RoleSupport#supervisorRoleAllowed")
    void shouldAllowCloseEndpointOnlyForSpecificRoles(JwtRole role, boolean isAllowed) throws JoseException, JsonProcessingException {
        var request = new CloseNotificationRequest();
        request.setReason("reason for closing");

        given()
                .header(oAuth2Support.jwtAuthorizationWithOptionalRole(role))
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post(ROOT + "/123/close")
                .then()
                .assertThat()
                .statusCode(new ForbiddenMatcher(isAllowed));
    }

    @ParameterizedTest
    @MethodSource("org.eclipse.tractusx.traceability.integration.common.support.RoleSupport#supervisorAndUserRolesAllowed")
    void shouldAllowUpdateEndpointOnlyForSpecificRoles(JwtRole role, boolean isAllowed) throws JoseException, JsonProcessingException {

        var request = new UpdateNotificationStatusTransitionRequest();
        request.setStatus(UpdateNotificationStatusRequest.ACCEPTED);
        request.setReason("reason for acceptanace");

        given()
                .header(oAuth2Support.jwtAuthorizationWithOptionalRole(role))
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post(ROOT + "/123/update")
                .then()
                .assertThat()
                .statusCode(new ForbiddenMatcher(isAllowed));
    }
}
