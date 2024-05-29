import { PaginationResponse } from '@core/model/pagination.model';
import { OperatorType, Policy, PolicyAction } from '@page/policies/model/policy.model';

/********************************************************************************
 * Copyright (c) 2022, 2023, 2024 Contributors to the Eclipse Foundation
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
// For now Mocks are built by current response (any). This is because Policy Model changes frequently
export const getPolicies = (): PaginationResponse<Policy> => {
  return {
    page: 0,
    pageCount: 0,
    pageSize: 10,
    totalItems: 2,

    content: [
      {
        bpnSelection: [ 'BPN10000000OEM0A', 'BPN10000000OEM0B' ],
        policyName: 'Mocked_Policy_Name_1',
        policyId: 'Mocked_Policy_1',
        accessType: PolicyAction.ACCESS,
        createdOn: '2024-02-26T13:38:07+01:00',
        validUntil: '2024-02-26T13:38:07+01:00',
        constraints: [ 'Membership = active', 'AND FrameworkAgreement.traceability in [active]', 'AND PURPOSE = ID 3.1 Trace' ],
        permissions: [
          {
            action: PolicyAction.USE,
            constraint: {
              and: [
                {
                  leftOperand: 'PURPOSE',
                  operator: {
                    id: OperatorType.EQ,
                  },
                  rightOperand: 'ID 3.0 Trace',
                },
              ],
              or: [
                {
                  leftOperand: 'PURPOSE',
                  operator: {
                    id: OperatorType.EQ,
                  },
                  rightOperand: 'ID 3.0 Trace',
                },
              ],
            },
          },
        ],
      },
      {
        bpnSelection: [ 'BPN10000000OEM0A', 'BPN10000000OEM0B' ],
        policyName: 'Mocked_Policy_Name_2',
        policyId: 'Mocked_Policy_2',
        accessType: PolicyAction.USE,
        createdOn: '2024-02-26T13:38:07+01:00',
        validUntil: '2024-02-26T13:38:07+01:00',
        constraints: [ 'PURPOSE = ID 3.1 Trace', 'OR PURPOSE = ID 3.0 Trace' ],
        permissions: [
          {
            action: PolicyAction.USE,
            constraint: {
              and: [
                {
                  leftOperand: 'PURPOSE',
                  operator: {
                    id: OperatorType.IN,
                  },
                  rightOperand: 'BMW',
                },
              ],
              or: [
                {
                  leftOperand: 'PURPOSE',
                  operator: {
                    id: OperatorType.EQ,
                  },
                  rightOperand: 'ID 3.0 Trace',
                },
              ],
            },
          },
        ],
      },

    ],

  };
};

