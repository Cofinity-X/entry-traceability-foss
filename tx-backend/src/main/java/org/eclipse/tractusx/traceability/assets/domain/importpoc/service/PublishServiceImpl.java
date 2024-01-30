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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.traceability.assets.application.importpoc.PublishService;
import org.eclipse.tractusx.traceability.assets.domain.asbuilt.repository.AssetAsBuiltRepository;
import org.eclipse.tractusx.traceability.assets.domain.asplanned.repository.AssetAsPlannedRepository;
import org.eclipse.tractusx.traceability.assets.domain.base.model.AssetBase;
import org.eclipse.tractusx.traceability.assets.domain.base.model.ImportState;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PublishServiceImpl implements PublishService {

    private final AssetAsPlannedRepository assetAsPlannedRepository;
    private final AssetAsBuiltRepository assetAsBuiltRepository;

    @Override
    public void publishAssets(String policyId, List<String> assetIds) {
        //Update assets with policy id
        List<AssetBase> assetAsPlannedList = assetAsPlannedRepository.getAssetsById(assetIds).stream()
                .filter(assetAsPlanned -> ImportState.TRANSIENT.equals(assetAsPlanned.getImportState()))
                .peek(assetAsPlanned -> assetAsPlanned.setImportState(ImportState.IN_SYNCHRONIZATION))
                .peek(assetAsPlanned -> assetAsPlanned.setPolicyId(policyId))
                .toList();
        assetAsPlannedRepository.saveAll(assetAsPlannedList);

        List<AssetBase> assetAsBuiltList = assetAsBuiltRepository.getAssetsById(assetIds).stream()
                .filter(assetAsBuilt -> ImportState.TRANSIENT.equals(assetAsBuilt.getImportState()))
                .peek(assetAsBuilt -> assetAsBuilt.setImportState(ImportState.IN_SYNCHRONIZATION))
                .peek(assetAsBuilt -> assetAsBuilt.setPolicyId(policyId))
                .toList();
        assetAsBuiltRepository.saveAll(assetAsBuiltList);
    }
}
