package common; /********************************************************************************
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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CustomOffsetDateTimeSerializerTest {


    private CustomOffsetDateTimeSerializer serializer;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @BeforeEach
    public void setup() {
        serializer = new CustomOffsetDateTimeSerializer();
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void test_serialize() throws IOException {
        //GIVEN
        OffsetDateTime offsetDateTime = OffsetDateTime.of(2023, 10, 13, 14, 30, 45, 0, ZoneOffset.UTC);
        String expectedFormattedDateTime = "2023-10-13T14:30:45Z";

        //WHEN
        serializer.serialize(offsetDateTime, jsonGenerator, serializerProvider);

        //THEN
        verify(jsonGenerator, times(1)).writeString(expectedFormattedDateTime);
    }

}
