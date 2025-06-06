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

package org.eclipse.tractusx.traceability.digitaltwinpart.application.mapper;

import org.eclipse.tractusx.traceability.common.model.BaseRequestFieldMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DigitalTwinPartFieldMapper extends BaseRequestFieldMapper {

    private static final Map<String, String> SUPPORTED_DIGITALTWINPART_FIELD_MAPPERS = Map.ofEntries(
            Map.entry("aasId", "aasId"),
            Map.entry("globalAssetId", "globalAssetId"),
            Map.entry("bpn", "bpn"),
            Map.entry("digitalTwinType", "digitalTwinType"),
            Map.entry("assetExpirationDate", "assetExpirationDate")
    );

    @Override
    public Map<String, String> getSupportedFields() {
        return SUPPORTED_DIGITALTWINPART_FIELD_MAPPERS;
    }
}
