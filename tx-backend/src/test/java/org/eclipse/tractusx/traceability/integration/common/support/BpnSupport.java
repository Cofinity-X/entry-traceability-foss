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
package org.eclipse.tractusx.traceability.integration.common.support;

import org.eclipse.tractusx.traceability.assets.domain.base.model.AssetBase;
import org.eclipse.tractusx.traceability.bpn.infrastructure.repository.BpnRepository;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Condition.matchesUri;

@Component
public class BpnSupport {

    @Autowired
    BpnRepository bpnRepository;
    @Autowired
    AssetRepositoryProvider assetRepositoryProvider;


    @Autowired
    RestitoProvider restitoProvider;

    @Value("${traceability.bpn}")
    String bpn = null;

    public void cachedBpnsForDefaultAssets() {
        providesBpdmLookup();
        List<String> assetIds = assetRepositoryProvider.testdataProvider().readAndConvertAssetsForTests().stream().map(AssetBase::getManufacturerId).toList();
        Map<String, String> bpnMappings = new HashMap<>();

        for (String assetId : assetIds) {
            bpnMappings.put(assetId, "Manufacturer Name $i");
        }

        bpnRepository.updateManufacturers(bpnMappings);
    }

    public void cachedBpnsForAsPlannedAssets() {
        List<String> assetIds = assetRepositoryProvider.testdataProvider().readAndConvertAssetsAsPlannedForTests().stream().map(AssetBase::getManufacturerId).toList();
        Map<String, String> bpnMappings = new HashMap<>();

        for (String assetId : assetIds) {
            bpnMappings.put(assetId, "Manufacturer Name $i");
        }

        bpnRepository.updateManufacturers(bpnMappings);
    }

    public String testBpn() {
        return bpn;
    }

    public void providesBpdmLookup() {
        whenHttp(restitoProvider.stubServer()).match(
                matchesUri(Pattern.compile("/api/catena/legal-entities/.+"))
        ).then(
                status(HttpStatus.OK_200),
                restitoProvider.jsonResponseFromFile("stubs/bpdm/response_200.json")
        );
    }

    public void returnsBpdmLookup401Unauthorized() {
        whenHttp(restitoProvider.stubServer()).match(
                matchesUri(Pattern.compile("/api/catena/legal-entities/.+"))
        ).then(
                status(HttpStatus.UNAUTHORIZED_401)
        );
    }
}
