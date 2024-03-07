# #578 Consistent null values

| Key           | Value                                                                    |
|---------------|--------------------------------------------------------------------------|
| Author        | @ds-crehm                                                                |
| Creation date | 28.02.2024                                                               |
| Ticket Id     | [#578](https://github.com/eclipse-tractusx/traceability-foss/issues/578) |
| State         | Draft                                                                    |

# Table of Contents
1. [Overview](#overview)
2. [Requirements](#requirements)
3. [Out of scope](#out-of-scope)
4. [Concept](#concept)
5. [References](#references)
6. [Additional Details](#additional-details)


# Overview
In the frontend there are multiple versions of 'empty' values:

![example-values-frontend.png](example-values-frontend.png)

In the backend those values are as follows:

![example-values-backend.png](example-values-backend.png)

These should be consistent throughout Trace-X.

# Requirements
- Whenever a string value is saved or updated in the database and during data consumption from IRS:
  - [ ] Leading and trailing whitespace characters are trimmed.
  - [ ] Empty strings ("") are all converted to null.
- [ ] Null values are shown *empty* in the frontend.
- [ ] When sorting columns, empty values are shown all the way on the bottom regardless of the sort order.
- [ ] Empty values can be filtered and searched for. They are shown as "(Blank)" (de: "(Leer)") when filtering for them and can be searched by inputting the same term.
- [ ] Leading and trailing whitespace characters don't count as mandatory values during user input.

# Out of scope
Any further string validation is out of scope. For example values like "-", "--", ".", "..." will not be changed and instead written into the database and shown in the frontend **unchanged**.

# Concept
### Backend
Whenever a string is saved or updated in the database and during data consumption from IRS:
1. Trim leading and whitespace character
2. Convert empty strings ("") to null
3. Save/update value in database

### Frontend
Null values should be shown in the frontend as empty:

![null-value-display-empty.png](null-value-display-empty.png)

When sorting the values, empty ones should be always shown at the bottom regardless of the sort order.
Filtering and searching for empty values must be possible. In the filter box (Blank) (de: (Leer)) should be shown at the bottom.
To search for it, the user must type in the term "(Blank)" (de: (Leer)).

![null-value-filter.png](null-value-filter.png)

In order to reduce the amount of whitespace characters in the database, leading and trailing whitespaces should not be counted during user input.

![null-value-input-leading-and-trailing.png](null-value-input-leading-and-trailing.png)
In this example, the amount of counted characters should be 10 ("blank" + " " + "test") instead of 43.

![null-value-input-only-spaces.png](null-value-input-only-spaces.png)
Here, no characters should be counted and the input should not be accepted.

# References
https://miro.com/app/board/uXjVO5JVoho=/?moveToWidget=3458764580844404410&cot=10

# Additional Details
Given the dynamic nature of ongoing development, there might be variations between the conceptualization and the current implementation. For the latest status, refer to the documentation.
