insert into act_id_user (ID_,REV_,FIRST_,PWD_)
select convert(id,char(50)),1,username,password from t_sys_user;

insert into act_id_group (ID_,REV_,NAME_)
select convert(id,char(50)),1,role_name from t_sys_role;

insert into act_id_membership
select convert(user_id,char(50)),convert(role_id,char(50)) from t_sys_user_role_ref;