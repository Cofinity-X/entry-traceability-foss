/********************************************************************************
 * Copyright (c) 2022, 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 * Copyright (c) 2022, 2023 ZF Friedrichshafen AG
 * Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.tractusx.traceability.assets.infrastructure.base.irs;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.github.resilience4j.retry.annotation.Retry;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.request.RegisterJobRequest;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.request.RegisterPolicyRequest;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.response.JobDetailResponse;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.response.PolicyResponse;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.response.RegisterJobResponse;

import org.eclipse.tractusx.traceability.common.config.IrsApiConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "irsApi",
        url = "${feign.irsApi.url}",
        configuration = {IrsApiConfig.class}
)
@Headers("X-API-KEY: {apiKey}")
public interface IRSApiClient {

    @RequestLine("POST /irs/jobs")
    RegisterJobResponse registerJob(@Param("apiKey") String apiKey, @RequestBody RegisterJobRequest request);

    @RequestLine("GET /irs/jobs/{id}")
    @Retry(name = "irs-get")
    JobDetailResponse getJobDetails(@Param("apiKey") String apiKey, @Param("id") String id);

    @RequestLine("POST /irs/policies")
    void registerPolicy(@Param("apiKey") String apiKey, @RequestBody RegisterPolicyRequest request);

    @RequestLine("GET /irs/policies")
    List<PolicyResponse> getPolicies(@Param("apiKey") String apiKey);

    @RequestLine("DELETE /irs/policies/{id}")
    void deletePolicy(@Param("apiKey") String apiKey, @Param("id") String id);
}
