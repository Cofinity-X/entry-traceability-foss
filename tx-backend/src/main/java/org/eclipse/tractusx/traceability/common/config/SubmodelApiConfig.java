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

package org.eclipse.tractusx.traceability.common.config;

import feign.codec.Decoder;
import feign.codec.StringDecoder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.eclipse.tractusx.traceability.common.properties.FeignDefaultProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


@Slf4j
@Configuration
public class SubmodelApiConfig {
    @Bean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new StringDecoder());
    }

    @Bean
    public OkHttpClient catenaApiOkHttpClient(@Autowired FeignDefaultProperties feignDefaultProperties) {
        return new OkHttpClient.Builder()
                .connectTimeout(feignDefaultProperties.getConnectionTimeoutMillis(), TimeUnit.MILLISECONDS)
                .readTimeout(feignDefaultProperties.getReadTimeoutMillis(), TimeUnit.MILLISECONDS)
                .connectionPool(
                        new ConnectionPool(
                                feignDefaultProperties.getMaxIdleConnections(),
                                feignDefaultProperties.getKeepAliveDurationMinutes(),
                                TimeUnit.MINUTES
                        )
                )
                .build();
    }

    @Bean
    public SubmodelServerRequestInterceptor submodelServerRequestInterceptor() {
        return new SubmodelServerRequestInterceptor();
    }

}
