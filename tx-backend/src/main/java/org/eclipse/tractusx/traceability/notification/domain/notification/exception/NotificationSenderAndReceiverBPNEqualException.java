/********************************************************************************
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.tractusx.traceability.notification.domain.notification.exception;


public class NotificationSenderAndReceiverBPNEqualException extends IllegalArgumentException {

    public NotificationSenderAndReceiverBPNEqualException(String bpn) {
        super("Quality notification cannot be created. Sender BPN %s is same as receiver BPN.".formatted(bpn));
    }

    public NotificationSenderAndReceiverBPNEqualException(String bpn, Long notificationId) {
        super("Quality notification with id %s cannot be edited. Sender BPN %s is same as receiver BPN.".formatted(notificationId, bpn));
    }
}
