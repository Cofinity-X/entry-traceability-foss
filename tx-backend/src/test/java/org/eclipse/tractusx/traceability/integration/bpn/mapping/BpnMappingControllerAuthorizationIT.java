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
package org.eclipse.tractusx.traceability.integration.bpn.mapping;

import io.restassured.http.ContentType;
import org.eclipse.tractusx.traceability.bpn.application.rest.BpnMappingController;
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
 * the endpoints of {@link BpnMappingController}.
 */
class BpnMappingControllerAuthorizationIT extends IntegrationTestSpecification {

    private static final String ROOT = "/api/bpn-config";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @MethodSource("org.eclipse.tractusx.traceability.integration.common.support.RoleSupport#supervisorAndAdminRolesAllowed")
    void shouldAllowGetEndpointOnlyForSpecificRoles(JwtRole role, boolean isAllowed) throws JoseException {
        given()
                .header(oAuth2Support.jwtAuthorizationWithOptionalRole(role))
                .contentType(ContentType.JSON)
                .when()
                .get(ROOT)
                .then()
                .assertThat()
                .statusCode(new ForbiddenMatcher(isAllowed));
    }

    @ParameterizedTest
    @MethodSource("org.eclipse.tractusx.traceability.integration.common.support.RoleSupport#supervisorAndAdminRolesAllowed")
    void shouldAllowPostEndpointOnlyForSpecificRoles(JwtRole role, boolean isAllowed) throws JoseException, JsonProcessingException {
        given()
                .header(oAuth2Support.jwtAuthorizationWithOptionalRole(role))
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(List.of()))
                .when()
                .post(ROOT)
                .then()
                .assertThat()
                .statusCode(new ForbiddenMatcher(isAllowed));
    }

    @ParameterizedTest
    @MethodSource("org.eclipse.tractusx.traceability.integration.common.support.RoleSupport#supervisorAndAdminRolesAllowed")
    void shouldAllowPutEndpointOnlyForSpecificRoles(JwtRole role, boolean isAllowed) throws JoseException, JsonProcessingException {
        given()
                .header(oAuth2Support.jwtAuthorizationWithOptionalRole(role))
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(List.of()))
                .when()
                .put(ROOT)
                .then()
                .assertThat()
                .statusCode(new ForbiddenMatcher(isAllowed));
    }

    @ParameterizedTest
    @MethodSource("org.eclipse.tractusx.traceability.integration.common.support.RoleSupport#supervisorAndAdminRolesAllowed")
    void shouldAllowDeleteEndpointOnlyForSpecificRoles(JwtRole role, boolean isAllowed) throws JoseException {
        given()
                .header(oAuth2Support.jwtAuthorizationWithOptionalRole(role))
                .contentType(ContentType.JSON)
                .when()
                .delete(ROOT + "/123")
                .then()
                .assertThat()
                .statusCode(new ForbiddenMatcher(isAllowed));
    }
}
