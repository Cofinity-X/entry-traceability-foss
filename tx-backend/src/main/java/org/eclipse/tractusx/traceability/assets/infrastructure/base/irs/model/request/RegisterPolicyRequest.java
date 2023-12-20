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

package org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.request;

import org.eclipse.tractusx.irs.edc.client.policy.Constraint;
import org.eclipse.tractusx.irs.edc.client.policy.Constraints;
import org.eclipse.tractusx.irs.edc.client.policy.OperatorType;
import org.eclipse.tractusx.irs.edc.client.policy.Permission;
import org.eclipse.tractusx.irs.edc.client.policy.PolicyType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RegisterPolicyRequest(
        String policyId,
        Instant validUntil,
        List<Permission> permissions
) {
    public static RegisterPolicyRequest from(String leftOperand, OperatorType operatorType, String rightOperand, String ttl) {
        return new RegisterPolicyRequest(
                UUID.randomUUID().toString(),
                Instant.parse(ttl),
                List.of(new Permission(
                        PolicyType.USE,
                        List.of(new Constraints(
                                List.of(new Constraint(leftOperand, operatorType, List.of(rightOperand))),
                                List.of(new Constraint(leftOperand, operatorType, List.of(rightOperand)))))
                )));
    }
}
