insert into t_sys_menu(id,version,menu_name,pid,menu_url,menu_level,menu_order,description) values (11,0,'外部税务检查',null,'/workflow/outertaxcheck/listpage',1,1,'外部税务检查列表');
insert into t_sys_role_menu_ref(role_id,menu_id,version) values (2,11,0);

insert into t_sys_menu(id,version,menu_name,pid,menu_url,menu_level,menu_order,description) values (12,0,'外部税务检查报告',null,'/workflow/outertaxcheck/listpage',1,1,'外部税务检查报告列表');
insert into t_sys_role_menu_ref(role_id,menu_id,version) values (2,12,0);