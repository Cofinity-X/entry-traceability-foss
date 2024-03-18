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
import org.eclipse.tractusx.irs.component.Shell;
import org.eclipse.tractusx.irs.component.assetadministrationshell.AssetAdministrationShellDescriptor;
import org.eclipse.tractusx.traceability.assets.domain.asbuilt.service.AssetAsBuiltServiceImpl;
import org.eclipse.tractusx.traceability.assets.domain.asplanned.service.AssetAsPlannedServiceImpl;
import org.eclipse.tractusx.traceability.common.config.AssetsAsyncConfig;
import org.eclipse.tractusx.traceability.common.properties.TraceabilityProperties;
import org.eclipse.tractusx.traceability.shelldescriptor.application.DecentralRegistryService;
import org.eclipse.tractusx.traceability.shelldescriptor.domain.repository.DecentralRegistryRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

import static org.eclipse.tractusx.traceability.assets.domain.base.model.SemanticDataModel.BATCH;
import static org.eclipse.tractusx.traceability.assets.domain.base.model.SemanticDataModel.JUSTINSEQUENCE;
import static org.eclipse.tractusx.traceability.assets.domain.base.model.SemanticDataModel.PARTASPLANNED;
import static org.eclipse.tractusx.traceability.assets.domain.base.model.SemanticDataModel.SERIALPART;

@RequiredArgsConstructor
@Slf4j
@Component
public class DecentralRegistryServiceImpl implements DecentralRegistryService {

    private final AssetAsBuiltServiceImpl assetAsBuiltService;
    private final AssetAsPlannedServiceImpl assetAsPlannedService;
    private final TraceabilityProperties traceabilityProperties;
    private final DecentralRegistryRepository decentralRegistryRepository;

    private static final List<String> AS_BUILT_ASPECT_TYPES = List.of(SERIALPART.getValue(), BATCH.getValue(), JUSTINSEQUENCE.getValue());
    private static final List<String> AS_PLANNED_ASPECT_TYPES = List.of(PARTASPLANNED.getValue());

    @Override
    @Async(value = AssetsAsyncConfig.LOAD_SHELL_DESCRIPTORS_EXECUTOR)
    public void synchronizeAssets() {
        Collection<Shell> shellDescriptors = decentralRegistryRepository.retrieveShellDescriptorsByBpn(traceabilityProperties.getBpn().toString());
        Collection<AssetAdministrationShellDescriptor> asBuiltShellDescriptors = shellDescriptors.stream().map(Shell::payload).filter(this::isAsBuilt).toList();
        Collection<AssetAdministrationShellDescriptor> asPlannedShellDescriptors = shellDescriptors.stream().map(Shell::payload).filter(this::isAsPlanned).toList();

        asBuiltShellDescriptors.forEach(shellDescriptor -> assetAsBuiltService.synchronizeAssetsAsync(shellDescriptor.getGlobalAssetId()));
        asPlannedShellDescriptors.forEach(shellDescriptor -> assetAsPlannedService.synchronizeAssetsAsync(shellDescriptor.getGlobalAssetId()));
    }


    // TODO: consider creating support method on AssetAdministrationShellDescriptor.is(BomLifecycle lifecycle) that will be usable on our code
    // IRS already have BomLifecycle in their domain so we can use it there also
    private boolean isAsBuilt(AssetAdministrationShellDescriptor shellDescriptor) {
        return !shellDescriptor.filterDescriptorsByAspectTypes(AS_BUILT_ASPECT_TYPES).isEmpty();
    }

    private boolean isAsPlanned(AssetAdministrationShellDescriptor shellDescriptor) {
        return !shellDescriptor.filterDescriptorsByAspectTypes(AS_PLANNED_ASPECT_TYPES).isEmpty();
    }
}

