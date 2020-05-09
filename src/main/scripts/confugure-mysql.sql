-- Use to run mysql db docker image
-- docker run --name mysqldb -p 3306:3306 -e MYSQL_EMPTY_PASSWORD=yes -d mysql

-- connect to mysql and run as root user
--Create DATABASE
Create DATABASE emrys_dev;
create DATABASE emrys_prod;

create user 'emrys_dev_user'@'localhost' IDENTIFIED BY 'rojay';
create user 'emrys_prod_user'@'localhost' IDENTIFIED BY 'rojaya';
create user 'emrys_dev_user'@'%' IDENTIFIED BY 'rojaya';
create user 'emrys_prod_user'@'%' IDENTIFIED BY 'rojaya';

--Database grants
Grant Select ON emrys_dev.* to 'emrys_dev_user'@'localhost';
Grant insert ON emrys_dev.* to 'emrys_dev_user'@'localhost';
Grant delete ON emrys_dev.* to 'emrys_dev_user'@'localhost';
Grant update ON emrys_dev.* to 'emrys_dev_user'@'localhost';

GRANT select on emrys_prod.* to 'emrys_prod_user'@'localhost';
GRANT insert on emrys_prod.* to 'emrys_prod_user'@'localhost';
GRANT delete on emrys_prod.* to 'emrys_prod_user'@'localhost';
GRANT update on emrys_prod.* to 'emrys_prod_user'@'localhost';


grant select on emrys_dev.* to 'emrys_dev_user'@'%';
grant insert on emrys_dev.* to 'emrys_dev_user'@'%';
grant delete on emrys_dev.* to 'emrys_dev_user'@'%';
grant update on emrys_dev.* to 'emrys_dev_user'@'%';

grant select on emrys_prod.* to 'emrys_prod_user'@'%';
grant insert on emrys_prod.* to 'emrys_prod_user'@'%';
grant delete on emrys_prod.* to 'emrys_prod_user'@'%';
grant update on emrys_prod.* to 'emrys_prod_user'@'%';
