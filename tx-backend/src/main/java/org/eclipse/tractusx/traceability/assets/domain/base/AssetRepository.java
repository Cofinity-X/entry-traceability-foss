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

package org.eclipse.tractusx.traceability.assets.domain.base;

import org.eclipse.tractusx.traceability.assets.domain.base.model.AssetBase;
import org.eclipse.tractusx.traceability.assets.domain.base.model.ImportState;
import org.eclipse.tractusx.traceability.assets.domain.base.model.Owner;
import org.eclipse.tractusx.traceability.configuration.domain.model.TriggerConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AssetRepository {
    AssetBase getAssetById(String assetId);

    Optional<AssetBase> findById(final String assetId);

    void deleteAssetById(String assetId);

    List<AssetBase> getAssetsById(List<String> assetIds);

    boolean existsById(String assetId);

    AssetBase getAssetByChildId(String childId);

    List<AssetBase> getAssets();

    AssetBase save(AssetBase asset);

    List<AssetBase> saveAll(List<AssetBase> assets);

    List<AssetBase> saveAllIfNotInIRSSyncAndUpdateImportStateAndNote(List<AssetBase> assets);

    long countAssets();

    long countAssetsByOwner(Owner owner);

    List<String> getFieldValues(String fieldName, List<String> startsWith, Integer resultLimit, Owner owner, List<String> inAssetIds);

    List<AssetBase> findByImportStateIn(ImportState... importStates);

    void updateImportStateAndNoteForAssets(ImportState importState, String importNote, List<String> assetIds);

    List<AssetBase> findAll();

    List<AssetBase> findAllExpired(Integer fetchLimit);

}
