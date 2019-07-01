spring_distributed_locks_integration
------------------------------------

1. Create database orders

   CREATE DATABASE orders;

2. Create table

    CREATE TABLE reservation (id INT, name VARCHAR(50));

3. Execute all the scripts found in src/resources/schema-mysql.sql

4. Run the application twice then try

    http://localhost:{port}/update/1/user01/30000 in one tab.
    http://localhost:{port}/update/1/user02/10000 in another tab.

    Finally you can see only one request can update the data.