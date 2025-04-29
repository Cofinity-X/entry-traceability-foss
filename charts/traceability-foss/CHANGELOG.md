# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
## Unreleased
- add trusted port 8181
- cpu request increase to 250m
- feature flag for flyway variable to apply test data via repeatable script
- removal of old BPDM auth approach via oauth2
- adding of goldenRecordUrl for EDC and BPN for golden record provider identification to negotiate BP explorer asset

## [1.4.1] - 2025-02-26
### No changes

## [1.4.0] - 2025-02-18
- #50 added the discovery type configurable, with a default value of bpnl under Traceability (DISCOVERY_TYPE).
- #55 renamed `backend.edc.apiKey` to `backend.edc.consumerApiKey`
- #99 added configurable publish asset job

## [1.3.44] - 2024-08-16
### No changes

## [1.3.43] - 2024-07-19
### Changed
- #1222 enabled read-only filesystem by default for backend

### Added
- added /tmp volume to backend container

## [1.3.42] - 2024-07-19
### No changes

## [1.3.41] - 2024-07-10
### No changes

## [1.3.40] - 2024-05-29
### No changes

## [1.3.39] - 2024-05-29
### No changes

## [1.3.38] - 2024-05-22

### No changes

## [1.3.36] - 2024-04-17
### No changes

## [1.3.35] - 2024-04-17
### No changes

## [1.3.34] - 2024-04-04
### No changes

## [1.3.33] - 2024-04-03
### No changes

## [1.3.32] - 2024-03-04

### No changes

## [1.3.31] - 2024-03-04
### No changes

## [1.3.30] - 2024-02-19
### No changes

## [1.3.29] - 2024-02-19
### No changes

## [1.3.28] - 2024-02-05
### No changes

## [1.3.27] - 2024-01-23
### No changes

## [1.3.26] - 2024-01-22
### No changes

## [1.3.16] - 2023-10-16
### Added
- Init pods on backend startup to check dependant services readiness status ( irs, edc-controlplane )


### Changed


### Removed


## [1.3.12] - 2023-08-22

### Added
- Initialization of chart changelogs


