# Concept #621: Edit Parts to notifications

| Key           | Value                                                                    |
|---------------|--------------------------------------------------------------------------|
| Autor         | Martin Maul                                                              |
| Creation date | 08.02.2024                                                               |
| Ticket Id     | [#621](https://github.com/eclipse-tractusx/traceability-foss/issues/621) |
| State         | DRAFT                                                                    |

# Table of Contents
1. [Overview](#overview)
2. [Summary](#summary)
3. [Problem Statement](#problem-statement)
4. [Requirements](#requirements)
5. [NFR](#nfr)
6. [Out of scope](#out-of-scope)
7. [Assumptions](#assumptions)
8. [Concept](#concept)
9. [Glossary](#glossary)
10. [References](#references)
11. [Additional Details](#additional-details)


# Overview
The existing notification update API should allow to update partIds, as long as the status of the notification is within queued state.
# Summary
Notifications should be editable when they are not sent
# Problem Statement
Currently notifications cannot be edited when created and not sent

# Requirements
Notifications should be editable when they are not sent
For this we need a API Endpoint that can handle this.
# NFR

# Out of scope

# Assumptions

# Frontend Concept
See Miro Board for UI Mock: https://miro.com/app/board/uXjVO5JVoho=/?moveToWidget=3458764578246228901&cot=10

# Backend Concept

The Endpoint should replace the partIds of a notification by the provided partIdList.

## Endpoint:

Existing APIs will be used: **/api/alerts/:id/update** Or **/api/investigations/:id/update**
- PUT Request Method
- expected Request body:
  ```
  {

  "partIds": ["example-id1", "example-id2", ... ] // list of strings that consist of partIds

  }
  ```
### Validation:

- Correct request structure (otherwise 400 bad Request)
- Notification exists (Otherwise 404 Not found)
- notification is in status CREATED (Otherwise 400 Bad Request)
- Notification is from owner (Otherwise status 403 Forbidden)
- Only supervisor is allowed to update the partIds of notifications.
- All partIds in request are:
    - Atleast one item in partlistId list (Otherwise 400 Bad Request)
    - If at least one partId is missing (400 - bad request will be thrown).
    - Existing (Otherwise 404 Not found)
    - in importState PERSISTENT (Otherwise 400 Bad Request)
    - In bomLifeCycle asBuilt (Otherwise 400 Bad Request)
    - If alert -> only own Parts (Otherwise 403 Forbidden)
    - If investigation -> only other Parts (Otherwise 403 Forbidden)

### Succes Response
Update success: 204 No Content

# Glossary

| Abbreviation | Name | Description   |
|--------------|------|---------------|
|              |      |               |
|              |      |               |

# References

# Additional Details
Given the dynamic nature of ongoing development, there might be variations between the conceptualization and the current implementation. For the latest status, refer to the documentation.
