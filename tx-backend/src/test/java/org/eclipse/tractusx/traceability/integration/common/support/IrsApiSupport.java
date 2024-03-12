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
package org.eclipse.tractusx.traceability.integration.common.support;

import org.glassfish.grizzly.http.util.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.header;
import static com.xebialabs.restito.semantics.Action.ok;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Condition.get;
import static com.xebialabs.restito.semantics.Condition.post;
import static com.xebialabs.restito.semantics.Condition.withHeader;

@Component
public class IrsApiSupport {

    @Autowired
    RestitoProvider restitoProvider;

    public void irsApiTriggerJob() {
        whenHttp(restitoProvider.stubServer()).match(
                post("/irs/jobs"),
                withHeader(HttpHeaders.AUTHORIZATION)
        ).then(
                ok(),
                header("Content-Type", "application/json"),
                restitoProvider.jsonResponseFromFile("./stubs/irs/post/jobs/response_200.json")
        );
    }

    public void irsJobDetailsAsPlanned() {
        whenHttp(restitoProvider.stubServer()).match(
                        get("/irs/jobs/ebb79c45-7bba-4169-bf17-SUCCESSFUL_AS_PLANNED")
                )
                .then(
                        ok(),
                        header("Content-Type", "application/json"),

                        restitoProvider.jsonResponseFromFile("./stubs/irs/get/jobs/id/response_200_downward_asPlanned.json"));

    }

    public void irsApiReturnsJobDetails() {
        whenHttp(restitoProvider.stubServer()).match(
                        get("/irs/jobs/ebb79c45-7bba-4169-bf17-3e719989ab54")
                )
                .then(
                        ok(),
                        header("Content-Type", "application/json"),
                        restitoProvider.jsonResponseFromFile("./stubs/irs/get/jobs/id/response_200.json")
                );
    }

    public void irsApiReturnsJobInRunningState() {
        whenHttp(restitoProvider.stubServer()).match(
                        get("/irs/jobs/ebb79c45-7bba-4169-bf17-3e719989ab54"),
                        withHeader(HttpHeaders.AUTHORIZATION)
                )
                .then(
                        ok(),
                        header("Content-Type", "application/json"),
                        restitoProvider.jsonResponseFromFile("./stubs/irs/get/jobs/id/running_job_response_200.json")
                );
    }

    public void irsJobDetailsApiFailed() {
        whenHttp(restitoProvider.stubServer()).match(
                        get("/irs/jobs/ebb79c45-7bba-4169-bf17-3e719989ab54"),
                        withHeader(HttpHeaders.AUTHORIZATION)
                )
                .then(
                        status(HttpStatus.INTERNAL_SERVER_ERROR_500),
                        header("Content-Type", "application/json"),
                        restitoProvider.jsonResponseFromFile("./stubs/irs/get/jobs/id/response_200.json")
                );
    }

    public void irsApiReturnsPolicies() {
        whenHttp(restitoProvider.stubServer()).match(
                        get("/irs/policies")
                )
                .then(
                        status(HttpStatus.OK_200),
                        header("Content-Type", "application/json"),
                        restitoProvider.jsonResponseFromFile("./stubs/irs/policies/response_200_get_policies.json")
                );
    }
}
