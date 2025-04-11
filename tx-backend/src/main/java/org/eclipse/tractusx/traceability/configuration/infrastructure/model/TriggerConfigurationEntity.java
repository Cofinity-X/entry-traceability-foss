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

package org.eclipse.tractusx.traceability.configuration.infrastructure.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.tractusx.traceability.configuration.domain.model.TriggerConfiguration;

@Entity
@Table(name = "trigger_configuration")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TriggerConfigurationEntity extends ConfigurationEntity {

    private static final String DEFAULT_CRON_EXPRESSION_AAS_CLEANUP = "0 0 1 * * ?";
    private static final String DEFAULT_CRON_EXPRESSION_AAS_LOOKUP = "0 0 3 * * ?";
    private static final String DEFAULT_CRON_EXPRESSION_PUBLISH_ASSETS = "0 0 5 * * ?";
    private static final String DEFAULT_CRON_EXPRESSION_MAP_COMPLETED_ORDERS = "0 0 0/4 * * ?";
    private static final String DEFAULT_CRON_EXPRESSION_REGISTER_ORDER_TTL_REACHED = "0 0 0/4 * * ?";
    private static final int DEFAULT_AAS_LIMIT = 1000;
    private static final int DEFAULT_AAS_TTL = 2629536;
    private static final int DEFAULT_PART_TTL = 2629536;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cron_expression_register_order_ttl_reached")
    private String cronExpressionRegisterOrderTTLReached;

    @Column(name = "cron_expression_map_completed_orders")
    private String cronExpressionMapCompletedOrders;

    @Column(name = "cron_expression_aas_lookup_ttl_reached")
    private String cronExpressionAASLookup;

    @Column(name = "cron_expression_aas_cleanup_ttl_reached")
    private String cronExpressionAASCleanup;

    @Column(name = "cron_expression_publish_assets")
    private String cronExpressionPublishAssets;

    @Column(name = "part_ttl")
    private Integer partTTL;

    @Column(name = "aas_ttl")
    private Integer aasTTL;

    @Column(name = "aas_limit")
    private Integer aasLimit;

    public static TriggerConfiguration toDomain(TriggerConfigurationEntity entity) {
        if (entity == null) {
            return null;
        }
        return TriggerConfiguration.builder()
                .id(entity.getId())
                .aasTTL(entity.getAasTTL())
                .partTTL(entity.getPartTTL())
                .cronExpressionMapCompletedOrders(entity.getCronExpressionMapCompletedOrders())
                .cronExpressionRegisterOrderTTLReached(entity.getCronExpressionRegisterOrderTTLReached())
                .cronExpressionAASLookup(entity.getCronExpressionAASLookup())
                .cronExpressionAASCleanup(entity.getCronExpressionAASCleanup())
                .cronExpressionPublishAssets(entity.getCronExpressionPublishAssets())
                .aasLimit(entity.getAasLimit())
                .build();
    }

    public static TriggerConfigurationEntity toEntity(TriggerConfiguration domain) {
        return TriggerConfigurationEntity.builder()
                .id(domain.getId())
                .aasTTL(domain.getAasTTL())
                .partTTL(domain.getPartTTL())
                .cronExpressionMapCompletedOrders(domain.getCronExpressionMapCompletedOrders())
                .cronExpressionRegisterOrderTTLReached(domain.getCronExpressionRegisterOrderTTLReached())
                .cronExpressionAASLookup(domain.getCronExpressionAASLookup())
                .cronExpressionAASCleanup(domain.getCronExpressionAASCleanup())
                .cronExpressionPublishAssets(domain.getCronExpressionPublishAssets())
                .aasLimit(domain.getAasLimit())
                .build();
    }


    public static TriggerConfigurationEntity defaultTriggerConfigurationEntity() {
        return TriggerConfigurationEntity.builder()
                .cronExpressionAASCleanup(DEFAULT_CRON_EXPRESSION_AAS_CLEANUP)
                .cronExpressionRegisterOrderTTLReached(DEFAULT_CRON_EXPRESSION_REGISTER_ORDER_TTL_REACHED)
                .cronExpressionMapCompletedOrders(DEFAULT_CRON_EXPRESSION_MAP_COMPLETED_ORDERS)
                .cronExpressionAASLookup(DEFAULT_CRON_EXPRESSION_AAS_LOOKUP)
                .cronExpressionPublishAssets(DEFAULT_CRON_EXPRESSION_PUBLISH_ASSETS)
                .aasTTL(DEFAULT_AAS_TTL)
                .partTTL(DEFAULT_PART_TTL)
                .aasLimit(DEFAULT_AAS_LIMIT)
                .build();
    }

}
