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
package org.eclipse.tractusx.traceability.policies.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.irs.edc.client.policy.Constraint;
import org.eclipse.tractusx.irs.edc.client.policy.Constraints;
import org.eclipse.tractusx.irs.edc.client.policy.Permission;
import org.eclipse.tractusx.traceability.policies.domain.PolicyRepository;
import policies.response.CreatePolicyResponse;
import policies.response.IrsPolicyResponse;
import org.eclipse.tractusx.traceability.common.properties.TraceabilityProperties;
import org.springframework.stereotype.Service;
import policies.request.RegisterPolicyRequest;
import policies.request.UpdatePolicyRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyRepositoryImpl implements PolicyRepository {

    private final PolicyClient policyClient;
    private final TraceabilityProperties traceabilityProperties;

    @Override
    public Map<String, List<IrsPolicyResponse>> getPolicies() {
        return policyClient.getPolicies();
    }

    @Override
    public Map<String, Optional<IrsPolicyResponse>> getPolicy(String policyId) {
        Map<String, Optional<IrsPolicyResponse>> result = new HashMap<>();

        getPolicies().forEach((key, value) -> {
            Optional<IrsPolicyResponse> policyResponse = value.stream()
                    .filter(irsPolicyResponse -> irsPolicyResponse.payload().policyId().equals(policyId))
                    .findFirst();
            if (policyResponse.isPresent()) {
                result.put(key, policyResponse);
            }
        });

        return result;
    }



    @Override
    public void createPolicyBasedOnAppConfig() {
        log.info("Check if irs policy exists");
        final Map<String, List<IrsPolicyResponse>> irsPolicies = this.policyClient.getPolicies();
        final List<String> irsPoliciesIds = irsPolicies.values().stream()
                .flatMap(List::stream)
                .map(irsPolicyResponse -> irsPolicyResponse.payload().policyId())
                .toList();        log.info("Irs has following policies: {}", irsPoliciesIds);

        log.info("Required constraints - 2 -");
        log.info("First constraint requirements: leftOperand {} operator {} and rightOperand {}", traceabilityProperties.getLeftOperand(), traceabilityProperties.getOperatorType(), traceabilityProperties.getRightOperand());
        log.info("Second constraint requirements: leftOperand {} operator {} and rightOperand {}", traceabilityProperties.getLeftOperandSecond(), traceabilityProperties.getOperatorTypeSecond(), traceabilityProperties.getRightOperandSecond());

        IrsPolicyResponse matchingPolicy = findMatchingPolicy(irsPolicies);

        if (matchingPolicy == null) {
            createMissingPolicies();
        } else {
            checkAndUpdatePolicy(matchingPolicy);
        }
    }

    @Override
    public void deletePolicy(String policyId) {
        this.policyClient.deletePolicy(policyId);
    }

    @Override
    public void updatePolicy(UpdatePolicyRequest updatePolicyRequest) {
        this.policyClient.updatePolicy(updatePolicyRequest);
    }

    @Override
    public CreatePolicyResponse createPolicy(RegisterPolicyRequest registerPolicyRequest) {
       return this.policyClient.createPolicy(registerPolicyRequest);
    }



    private IrsPolicyResponse findMatchingPolicy(Map<String, List<IrsPolicyResponse>> irsPolicies) {
        return irsPolicies.values().stream()
                .flatMap(List::stream)
                .filter(this::checkConstraints)
                .findFirst()
                .orElse(null);
    }

    private boolean checkConstraints(IrsPolicyResponse irsPolicy) {
        boolean firstConstraintExists = checkConstraint(irsPolicy, traceabilityProperties.getRightOperand());
        boolean secondConstraintExists = checkConstraint(irsPolicy, traceabilityProperties.getRightOperandSecond());
        return firstConstraintExists && secondConstraintExists;
    }

    private boolean checkConstraint(IrsPolicyResponse irsPolicy, String rightOperand) {
        return emptyIfNull(irsPolicy.payload().policy().getPermissions()).stream()
                .flatMap(this::getConstraintsStream)
                .anyMatch(constraint -> constraint.getRightOperand().equals(rightOperand));
    }

    private Stream<Constraint> getConstraintsStream(Permission permission) {
        Constraints constraint = permission.getConstraint();
        if (constraint == null) {
            return Stream.empty();
        }
        return Stream.concat(
                emptyIfNull(constraint.getAnd()).stream(),
                emptyIfNull(constraint.getOr()).stream()
        );
    }




    private void createMissingPolicies() {
        log.info("Irs policy does not exist creating {}", traceabilityProperties.getRightOperand());
        this.policyClient.createPolicyFromAppConfig();
    }

    private void checkAndUpdatePolicy(IrsPolicyResponse requiredPolicy) {
        if (isPolicyExpired(requiredPolicy)) {
            log.info("IRS Policy {} has outdated validity updating new ttl", traceabilityProperties.getRightOperand());
            this.policyClient.deletePolicy(traceabilityProperties.getRightOperand());
            this.policyClient.createPolicyFromAppConfig();
        }
    }

    private boolean isPolicyExpired(IrsPolicyResponse requiredPolicy) {
        return traceabilityProperties.getValidUntil().isAfter(requiredPolicy.validUntil());
    }

}
