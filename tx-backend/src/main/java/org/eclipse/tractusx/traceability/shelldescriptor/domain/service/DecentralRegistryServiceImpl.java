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

package org.eclipse.tractusx.traceability.shelldescriptor.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.irs.component.assetadministrationshell.AssetAdministrationShellDescriptor;
import org.eclipse.tractusx.traceability.assets.domain.asbuilt.service.AssetAsBuiltServiceImpl;
import org.eclipse.tractusx.traceability.assets.domain.asplanned.service.AssetAsPlannedServiceImpl;
import org.eclipse.tractusx.traceability.common.config.AssetsAsyncConfig;
import org.eclipse.tractusx.traceability.common.properties.TraceabilityProperties;
import org.eclipse.tractusx.traceability.shelldescriptor.application.DecentralRegistryService;
import org.eclipse.tractusx.traceability.shelldescriptor.domain.repository.DecentralRegistryRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class DecentralRegistryServiceImpl implements DecentralRegistryService {

    private final AssetAsBuiltServiceImpl assetAsBuiltService;
    private final AssetAsPlannedServiceImpl assetAsPlannedService;
    private final TraceabilityProperties traceabilityProperties;
    private final DecentralRegistryRepository decentralRegistryRepository;

    @Override
    @Async(value = AssetsAsyncConfig.LOAD_SHELL_DESCRIPTORS_EXECUTOR)
    public void synchronizeAssets() {

        // TODO we will retrieve Collections<AssetAdministrationShellDescriptor> and need to filter for each semanticModelIdKey
        // Example of response: 	"idShort": "SingleLevelUsageAsBuilt",
        //					"id": "urn:uuid:3fa9cf02-1064-459f-a17e-1cbc819c2f2e",
        //					"semanticId": {
        //						"type": "ExternalReference",
        //						"keys": [
        //							{
        //								"type": "GlobalReference",
        //								"value": "urn:bamm:io.catenax.single_level_usage_as_built:2.0.0#SingleLevelUsageAsBuilt"
        //							}
        //						]
        //					},
        //					"supplementalSemanticId": [],
        //					"description": [],
        //					"displayName": []
        //				}
        //			]
        //		},
        // https://irs-aas-registry.dev.demo.catena-x.net/semantics/registry/api/v3.0/shell-descriptors
        // Result should be a list of globalAssetIds associcated with asBuilt and another list asPlanned



        List<String> globalAssetIdsForApplicationBpn = decentralRegistryRepository.retrieveShellDescriptorsByBpn(traceabilityProperties.getBpn().toString());
        globalAssetIdsForApplicationBpn
                .forEach(globalAssetId -> {
                    //TODO: differentiate if this is either as-planned or as-built. Otherwise we have twice the load here.
                    // DT-Library offers methods to requests additional info to get the bomlifecycle
                    assetAsPlannedService.synchronizeAssetsAsync(globalAssetId);
                    assetAsBuiltService.synchronizeAssetsAsync(globalAssetId);
                });
    }
}

