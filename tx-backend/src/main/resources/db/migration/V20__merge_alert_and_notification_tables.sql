DROP VIEW IF EXISTS assets_as_built_view;


DROP TABLE public.asset_as_built_alert_notifications;
DROP TABLE public.assets_as_built_notifications;
DROP TABLE public.investigation_notification;
DROP TABLE public.assets_as_built_investigations;
DROP TABLE public.alert_notification;
DROP TABLE public.assets_as_built_alerts;
DROP TABLE public.alert;
DROP TABLE public.investigation;
DROP SEQUENCE public.alert_id_seq;
DROP SEQUENCE public.investigation_id_seq;


-- DROP TABLE notification;
CREATE TABLE public.notification
(
    id             int8          NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE),
    title          varchar(255)  NULL,
    bpn            varchar(255)  NULL,
    close_reason   varchar(1000) NULL,
    created        timestamp     NULL,
    description    varchar(1000) NULL,
    status         varchar(50)   NULL,
    updated        timestamp     NULL,
    side           varchar(50)   NULL,
    accept_reason  varchar(1000) NULL,
    decline_reason varchar(1000) NULL,
    type           varchar(50)   NULL,
    CONSTRAINT notification_pk PRIMARY KEY (id)
);

CREATE TABLE public.notification_message
(
    id                        varchar(255) NOT NULL,
    contract_agreement_id     varchar(255) NULL,
    edc_url                   varchar(255) NULL,
    notification_reference_id varchar(255) NULL,
    send_to                   varchar(255) NULL,
    created_by                varchar(255) NULL,
    notification_id           int8         NULL,
    target_date               timestamp    NULL,
    severity                  varchar(255) NULL,
    created_by_name           varchar(255) NULL,
    send_to_name              varchar(255) NULL,
    edc_notification_id       varchar(255) NULL,
    status                    varchar(255) NULL,
    created                   timestamptz  NULL,
    updated                   timestamptz  NULL,
    error_message             varchar      NULL,
    message_id                varchar(255) NULL,
    is_initial                bool         NULL,
    CONSTRAINT notification_message_pkey PRIMARY KEY (id),
    CONSTRAINT fk_notification_message_notification FOREIGN KEY (notification_id) REFERENCES public.notification (id)
);

CREATE TABLE public.assets_as_planned_notifications
(
    notification_id int8         NOT NULL,
    asset_id        varchar(255) NOT NULL
);
CREATE TABLE public.assets_as_planned_notification_messages
(
    notification_message_id varchar(255) NOT NULL,
    asset_id                varchar(255) NOT NULL
);

CREATE TABLE public.assets_as_built_notifications
(
    notification_id int8         NOT NULL,
    asset_id        varchar(255) NOT NULL,
    CONSTRAINT fk_asset_entity FOREIGN KEY (asset_id) REFERENCES public.assets_as_built (id),
    CONSTRAINT fk_assets_as_built_notifications FOREIGN KEY (notification_id) REFERENCES public.notification (id)
);



CREATE TABLE public.assets_as_built_notification_messages
(
    notification_message_id varchar(255) NOT NULL,
    asset_id                varchar(255) NOT NULL,
    CONSTRAINT fk_notification FOREIGN KEY (notification_message_id) REFERENCES public.notification_message (id)
);