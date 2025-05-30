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

package org.eclipse.tractusx.traceability.assets.infrastructure.asbuilt.repository;

import org.eclipse.tractusx.traceability.assets.domain.base.model.ImportState;
import org.eclipse.tractusx.traceability.assets.domain.base.model.Owner;
import org.eclipse.tractusx.traceability.assets.infrastructure.asbuilt.model.AssetAsBuiltEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaAssetAsBuiltRepository extends JpaRepository<AssetAsBuiltEntity, String>, JpaSpecificationExecutor<AssetAsBuiltEntity> {
    List<AssetAsBuiltEntity> findByIdIn(List<String> assetIds);

    @Query("SELECT COUNT(asset) FROM AssetAsBuiltEntity asset WHERE asset.owner = :owner")
    long countAssetsByOwner(@Param("owner") Owner owner);

    List<AssetAsBuiltEntity> findByImportStateIn(ImportState... importState);

    @Query(value = "SELECT * FROM assets_as_built a " +
            "WHERE a.expiration_date < CURRENT_TIMESTAMP OR a.expiration_date IS NULL " +
            "ORDER BY CASE a.import_state WHEN 'PERSISTED' THEN 0 ELSE 1 END " +
            "LIMIT :fetchLimit",
            nativeQuery = true)
    List<AssetAsBuiltEntity> findAllExpired(@Param("fetchLimit") Integer fetchLimit);
}
