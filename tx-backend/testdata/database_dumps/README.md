# Restoring a Database Dump

## Overview

This README provides step-by-step instructions on how to restore a database dump with pgAdmin. Database dumps are invaluable for data recovery in case of system issues or data corruption. This guide outlines the process for restoring a PostgreSQL database from a dump file.


## Restoring the Database

Follow these steps to restore a database from a dump file using pgAdmin:

1. Open the pgAdmin link from the ArgoCD application and login.

2. Click on the "trace" database.

3. Right-click on the database, and select "Restore."

4. Click on the folder icon next to the "Filename" field.

5. Click on the three dots (...) on the right, and select "Upload."

6. Drag and drop the database dump file from the "database dump" folder for the corresponding environment into the upload area and confirm the action.

7. "Process Failed" error message can be ignored

### Database dump

In this project, there are two database dumps available: `database_dump_a` and `database_dump_b`. The files `database_dump_a` and `database_dump_b` are associated with different environments. Below is a list indicating which file corresponds to which environment:

- `database_dump_a`
    - `dev`
    - `e2e-a`
    - `int-a`

- `database_dump_b`
    - `test`
    - `e2e-b`
    - `int-b`

#### How to create a database dump

To create a database dump, follow these steps:

1. Open pgAdmin and connect to the database server.

2. Right-click on the database you want to dump.

3. Select "Backup."

4. Choose the appropriate options, such as the filename for the dump file.

5. Click "Backup" to generate the dump file.
