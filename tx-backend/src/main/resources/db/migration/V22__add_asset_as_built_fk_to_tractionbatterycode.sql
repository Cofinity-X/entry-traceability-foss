ALTER TABLE traction_battery_code_subcomponent
    ADD COLUMN "asset_as_built_id" varchar(255) NULL;
ALTER TABLE traction_battery_code_subcomponent
    ADD CONSTRAINT fk_asset_traction_battery_code_subcomponent FOREIGN KEY (asset_as_built_id) REFERENCES assets_as_built (id) on delete cascade;

ALTER TABLE assets_as_built
    DROP COLUMN traction_battery_code CASCADE;
