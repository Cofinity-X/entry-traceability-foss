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
package org.eclipse.tractusx.traceability.testdata;

import org.eclipse.tractusx.irs.edc.client.policy.Constraint;
import org.eclipse.tractusx.irs.edc.client.policy.Constraints;
import org.eclipse.tractusx.irs.edc.client.policy.Operator;
import org.eclipse.tractusx.irs.edc.client.policy.OperatorType;
import org.eclipse.tractusx.irs.edc.client.policy.Permission;
import org.eclipse.tractusx.irs.edc.client.policy.Policy;
import org.eclipse.tractusx.irs.edc.client.policy.PolicyType;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.response.IrsPolicyResponse;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.response.Payload;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.List;

public class PolicyTestDataFactory {

    @NotNull
    public static IrsPolicyResponse createIrsPolicyResponse(String policyId, OffsetDateTime createdOn, String orRightOperand, String andRightOperand) {
        return
                IrsPolicyResponse.builder()
                        .validUntil(OffsetDateTime.now())
                        .payload(
                                Payload.builder()
                                        .policyId(policyId)
                                        .policy(
                                                Policy.builder()
                                                        .policyId(policyId)
                                                        .createdOn(createdOn)
                                                        .permissions(List.of(
                                                                Permission.builder()
                                                                        .action(PolicyType.USE)
                                                                        .constraint(Constraints.builder()
                                                                                .and(List.of(new Constraint("", new Operator(OperatorType.EQ), andRightOperand)))
                                                                                .or(List.of(new Constraint("", new Operator(OperatorType.EQ), orRightOperand)))
                                                                                .build())
                                                                        .build()))
                                                        .build())
                                        .build())
                        .build();
    }
}
