#!/bin/bash
sqlplus system/oracle@localhost:1521/xe.oracle.docker <<FIN!
column owner format a40
set serveroutput off
--set linesize 300 
--column name format a40
--column type format a40
--set linesize 300 
set trimspool on
spool /home/lxuserb/Volunteering/out.txt
--select table_name, owner from all_tables order by table_name, owner;

desc address

desc application

desc jhi_authority

desc document

desc jhi_link

desc link_type

desc offer

desc offer_details

desc program

desc provider

desc requirement

desc status_type

desc term

desc jhi_user

desc user_type

select dbms_xdb.gethttpport from dual;

select dbms_xdb.gethttpsport from dual;

select dbms_xdb.getftpport from dual;

select sys_context('userenv','instance_name') from dual;

select sys_context('userenv','sid') from dual;
	
select sys_context('userenv','db_name') from dual;

select sys_context('userenv', 'server_host') from dual;

select ora_database_name from dual;

select * from global_name;


select 
	user0_.id as id1_8_0_, 
	authority2_.name as name1_3_1_, 
	user0_.created_by as created_by2_8_0_, 
	user0_.created_date as created_date3_8_0_, 
	user0_.last_modified_by as last_modified_by4_8_0_, 
	user0_.last_modified_date as last_modified_date5_8_0_, 
	user0_.activated as activated6_8_0_, 
	user0_.activation_key as activation_key7_8_0_, 
	user0_.address_id as address_id17_8_0_, 
	user0_.email as email8_8_0_, 
	user0_.first_name as first_name9_8_0_, 
	user0_.image_url as image_url10_8_0_, 
	user0_.lang_key as lang_key11_8_0_, 
	user0_.last_name as last_name12_8_0_, 
	user0_.login as login13_8_0_, 
	user0_.password_hash as password_hash14_8_0_, 
	user0_.reset_date as reset_date15_8_0_, 
	user0_.reset_key as reset_key16_8_0_, 
	user0_.user_type_id as user_type_id18_8_0_, 
	authoritie1_.user_id as user_id1_9_0__, 
	authoritie1_.authority_name as authority_name2_9_0__ 
from 
	jhi_user user0_ 
left outer join 
	jhi_user_authority authoritie1_ 
on 
	user0_.id=authoritie1_.user_id 
left outer join 
	jhi_authority authority2_ 
on authoritie1_.authority_name=authority2_.name; 


spool off;
exit;

FIN!

gedit out.txt
