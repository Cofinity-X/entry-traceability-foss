create or replace view contract_agreement_view as
SELECT *
FROM ((SELECT assets_as_built.id, contract_agreement_id, 'assets_as_built' as asset_type, created
       FROM assets_as_built
       where contract_agreement_id is not null)
      UNION ALL
      (SELECT assets_as_planned.id, contract_agreement_id, 'assets_as_planned' as asset_type, created
       FROM assets_as_planned
       where contract_agreement_id is not null)) results
ORDER BY created DESC;

