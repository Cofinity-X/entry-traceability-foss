/********************************************************************************
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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
package org.eclipse.tractusx.traceability.digitaltwinpart.domain.model;

import lombok.Builder;
import lombok.Data;
import org.eclipse.tractusx.traceability.common.model.BPN;

import java.time.LocalDateTime;

@Builder
@Data
public class DigitalTwinPartDetail {
    private String aasId;
    private Integer aasTTL;
    private LocalDateTime aasExpirationDate;
    private LocalDateTime nextLookup;

    private String globalAssetId;
    private Integer assetTTL;
    private LocalDateTime assetExpirationDate;
    private LocalDateTime nextSync;

    private String actor;
    private BPN bpn;
    private String digitalTwinType;
}
