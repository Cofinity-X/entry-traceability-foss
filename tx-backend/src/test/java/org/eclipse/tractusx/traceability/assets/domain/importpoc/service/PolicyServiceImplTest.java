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
package org.eclipse.tractusx.traceability.assets.domain.importpoc.service;

import policies.response.PolicyResponse;
import org.eclipse.tractusx.traceability.policies.domain.PolicyRepository;
import policies.response.IrsPolicyResponse;
import org.eclipse.tractusx.traceability.common.properties.TraceabilityProperties;
import org.eclipse.tractusx.traceability.policies.domain.PolicyServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.tractusx.traceability.testdata.PolicyTestDataFactory.createIrsPolicyResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PolicyServiceImplTest {
    @InjectMocks
    private PolicyServiceImpl policyService;
    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private TraceabilityProperties traceabilityProperties;

    @Test
    void testGetPolicy() {
        // GIVEN
        String policyId = "policy123";
        OffsetDateTime createdOn = OffsetDateTime.parse("2023-07-03T16:01:05.309Z");
        IrsPolicyResponse irsPolicyResponse = createIrsPolicyResponse(policyId, createdOn, "", "", "", "");
        Map<String, List<IrsPolicyResponse>> acceptedPolicies = Map.of("key", List.of(irsPolicyResponse));

        // WHEN
        when(policyRepository.getPolicies()).thenReturn(acceptedPolicies);
        List<PolicyResponse> allPolicies = policyService.getPolicies();

        // THEN
        assertNotNull(allPolicies);
        assertEquals(policyId, allPolicies.get(0).policyId());
        assertEquals(createdOn, allPolicies.get(0).createdOn());
    }

    @Test
    void getPolicyByConstraintRightOperand() {
        // Given
        IrsPolicyResponse firstPolicyResponse = createIrsPolicyResponse("test", OffsetDateTime.now(), "my-left-constraint1", "", "", "my-constraint1");
        IrsPolicyResponse secondPolicyResponse = createIrsPolicyResponse("test2", OffsetDateTime.now(), "my-left-constraint2", "", "", "my-constraint2");
        IrsPolicyResponse thirdPolicyResponse = createIrsPolicyResponse("test3", OffsetDateTime.now(), "my-left-constraint3", "", "", "my-constraint3");
        IrsPolicyResponse fourthPolicyResponse = createIrsPolicyResponse("test4", OffsetDateTime.now(), "my-left-constraint4", "", "my-constraint4", "");
        Map<String, List<IrsPolicyResponse>> policyResponseMap = Map.of(
                "key1", List.of(firstPolicyResponse),
                "key2", List.of(secondPolicyResponse),
                "key3", List.of(thirdPolicyResponse),
                "key4", List.of(fourthPolicyResponse)
        );
        when(policyRepository.getPolicies()).thenReturn(policyResponseMap);
        Map<String, Optional<IrsPolicyResponse>> policyMap = new HashMap<>();
        policyMap.put("bpn123", Optional.of(fourthPolicyResponse));

        when(policyRepository.getPolicy(anyString())).thenReturn(policyMap);

        when(traceabilityProperties.getRightOperand()).thenReturn("my-constraint4");
        when(traceabilityProperties.getLeftOperand()).thenReturn("my-left-constraint4");

        // When
        Optional<PolicyResponse> policyResult = policyService.getFirstPolicyMatchingApplicationConstraint();

        // Then
        assertThat(policyResult).isPresent();
        assertThat(policyResult.get().policyId()).isEqualTo("test4");
    }

    @Test
    void getPolicyByConstraintRightOperandNotFound() {
        // Given
        IrsPolicyResponse firstPolicyResponse = createIrsPolicyResponse("test", OffsetDateTime.now(), "my-constraint1", "", "", "");
        IrsPolicyResponse secondPolicyResponse = createIrsPolicyResponse("test2", OffsetDateTime.now(), "my-constraint2", "", "", "");
        IrsPolicyResponse thirdPolicyResponse = createIrsPolicyResponse("test3", OffsetDateTime.now(), "my-constraint3", "", "", "");
        IrsPolicyResponse fourthPolicyResponse = createIrsPolicyResponse("test4", OffsetDateTime.now(), "my-constraint4", "", "", "");
        Map<String, List<IrsPolicyResponse>> policyResponseMap = Map.of(
                "key1", List.of(firstPolicyResponse),
                "key2", List.of(secondPolicyResponse),
                "key3", List.of(thirdPolicyResponse),
                "key4", List.of(fourthPolicyResponse)
        );
        when(policyRepository.getPolicies()).thenReturn(policyResponseMap);
        when(traceabilityProperties.getRightOperand()).thenReturn("not-exists");

        // When
        Optional<PolicyResponse> policyResult = policyService.getFirstPolicyMatchingApplicationConstraint();

        // Then
        assertThat(policyResult).isEmpty();
    }
}
