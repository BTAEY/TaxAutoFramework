insert into act_id_user (ID_,REV_,LAST_,PWD_,EMAIL_)
values ('adminUI',1,'Administrator','test','adminUI');

insert into act_id_group(ID_,REV_,NAME_,TYPE_)
values ('ROLE_ADMIN',1,'Superusers','security-role');

insert into act_id_membership(USER_ID_,GROUP_ID_)
values ('adminUI','ROLE_ADMIN');

insert into act_id_user (ID_,REV_,FIRST_,PWD_)
select convert(id,char(50)),1,username,password from t_sys_user;

insert into act_id_group (ID_,REV_,NAME_)
select convert(id,char(50)),1,role_name from t_sys_role;

insert into act_id_membership
select convert(user_id,char(50)),convert(role_id,char(50)) from t_sys_user_role_ref;