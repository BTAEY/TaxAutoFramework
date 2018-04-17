CREATE TABLE if not exists `t_sys_organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `createdate` datetime DEFAULT NULL,
  `createman` varchar(255) DEFAULT NULL,
  `updatedate` datetime DEFAULT NULL,
  `updateman` varchar(255) DEFAULT NULL,
  `org_name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_sys_organization(id,version,createman,createdate,org_name,description) values (1,0,'admin',sysdate(),'总行','根组织机构');

CREATE TABLE if not exists `t_sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `createdate` datetime DEFAULT NULL,
  `createman` varchar(255) DEFAULT NULL,
  `updatedate` datetime DEFAULT NULL,
  `updateman` varchar(255) DEFAULT NULL,
  `enable` bit(1) DEFAULT NULL,
  `login_count` int(11) DEFAULT NULL,
  `login_status` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `header_img` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_sys_user(id,username,password,enable,login_count,version) values (1,'admin','5f4dcc3b5aa765d61d8327deb882cf99',1,0,0);
insert into t_sys_user(id,username,password,enable,login_count,version) values (2,'general','5f4dcc3b5aa765d61d8327deb882cf99',1,0,0);
insert into t_sys_user(id,username,password,enable,login_count,version) values (3,'submitter','5f4dcc3b5aa765d61d8327deb882cf99',1,0,0);
insert into t_sys_user(id,username,password,enable,login_count,version) values (4,'maker','5f4dcc3b5aa765d61d8327deb882cf99',1,0,0);
insert into t_sys_user(id,username,password,enable,login_count,version) values (5,'approver','5f4dcc3b5aa765d61d8327deb882cf99',1,0,0);

CREATE TABLE if NOT EXISTS `t_sys_role` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT,
 `version` int(11) NOT NULL,
 `createdate` datetime DEFAULT NULL,
 `createman` varchar(255) DEFAULT NULL,
 `updatedate` datetime DEFAULT NULL,
 `updateman` varchar(255) DEFAULT NULL,
 `role_name` varchar(255) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- 超级管理员角色
insert into t_sys_role(id,role_name,version,createman,createdate) values (1,'role_admin',0,1,sysdate());
-- 普通角色
insert into t_sys_role(id,role_name,version,createman,createdate) values (2,'role_general',0,1,sysdate());

CREATE TABLE IF NOT EXISTS `t_sys_org_user_ref` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

insert into t_sys_org_user_ref(org_id,user_id,version) values (1,1,0);
insert into t_sys_org_user_ref(org_id,user_id,version) values (1,2,0);
insert into t_sys_org_user_ref(org_id,user_id,version) values (1,3,0);
insert into t_sys_org_user_ref(org_id,user_id,version) values (1,4,0);
insert into t_sys_org_user_ref(org_id,user_id,version) values (1,5,0);

CREATE TABLE IF NOT EXISTS `t_sys_user_role_ref` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT,
 `version` int(11) NOT NULL,
 `user_id` bigint(20) DEFAULT NULL,
 `role_id` bigint(20) DEFAULT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

insert into t_sys_user_role_ref(user_id,role_id,version) values (1,1,0);
insert into t_sys_user_role_ref(user_id,role_id,version) values (2,2,0);
insert into t_sys_user_role_ref(user_id,role_id,version) values (3,2,0);
insert into t_sys_user_role_ref(user_id,role_id,version) values (4,2,0);
insert into t_sys_user_role_ref(user_id,role_id,version) values (5,2,0);

CREATE TABLE if not EXISTS `t_sys_permission` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT,
 `version` int(11) NOT NULL,
 `createdate` datetime DEFAULT NULL,
 `createman` varchar(255) DEFAULT NULL,
 `updatedate` datetime DEFAULT NULL,
 `updateman` varchar(255) DEFAULT NULL,
 `description` varchar(255) DEFAULT NULL,
 `name` varchar(255) DEFAULT NULL,
 `pid` bigint(20) DEFAULT NULL,
 `url` varchar(255) DEFAULT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

insert into t_sys_permission(id,name,description,url,pid,version) values (1,'general_permission','general user permission','/',null,0);
insert into t_sys_permission(id,name,description,url,pid,version) values (2,'admin_permission','admin user permission','/admin',null,0);

CREATE TABLE IF NOT EXISTS `t_sys_role_permission_ref` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT,
 `version` int(11) NOT NULL,
 `permission_id` bigint(20) DEFAULT NULL,
 `role_id` bigint(20) DEFAULT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_sys_role_permission_ref(role_id,permission_id,version) values (1,1,0);
insert into t_sys_role_permission_ref(role_id,permission_id,version) values (1,2,0);
insert into t_sys_role_permission_ref(role_id,permission_id,version) values (2,1,0);

CREATE TABLE if not EXISTS `t_sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `menu_name` varchar(255) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `menu_url` varchar(255) DEFAULT NULL,
  `menu_level` int(11) DEFAULT NULL,
  `menu_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

insert into t_sys_menu(id,version,menu_name,pid,menu_url,menu_level,menu_order,description) values (1,0,'系统管理',NULL,'/admin/menu/system',1,1,'系统管理菜单');
insert into t_sys_menu(id,version,menu_name,pid,menu_url,menu_level,menu_order,description) values (2,0,'用户管理',1,'/admin/menu/user',2,1,'用户管理菜单');
insert into t_sys_menu(id,version,menu_name,pid,menu_url,menu_level,menu_order,description) values (3,0,'权限管理',1,'/admin/menu/permission',2,2,'权限管理菜单');
insert into t_sys_menu(id,version,menu_name,pid,menu_url,menu_level,menu_order,description) values (4,0,'角色管理',1,'/admin/menu/role',2,3,'角色管理菜单');
insert into t_sys_menu(id,version,menu_name,pid,menu_url,menu_level,menu_order,description) values (5,0,'组织管理',1,'/admin/menu/organization',2,4,'组织管理菜单');
insert into t_sys_menu(id,version,menu_name,pid,menu_url,menu_level,menu_order,description) values (6,0,'工作流管理',NULL,'',1,2,'工作流管理');
insert into t_sys_menu(id,version,menu_name,pid,menu_url,menu_level,menu_order,description) values (7,0,'流程仓库',6,'/workflowlist',1,2,'工作流定义仓库');
insert into t_sys_menu(id,version,menu_name,pid,menu_url,menu_level,menu_order,description) values (8,0,'已完成流程',6,'/workflow/completed',2,1,'已完成流程菜单');
insert into t_sys_menu(id,version,menu_name,pid,menu_url,menu_level,menu_order,description) values (9,0,'待办事项',6,'/workflow/pending',2,2,'待办事项菜单');
insert into t_sys_menu(id,version,menu_name,pid,menu_url,menu_level,menu_order,description) values (10,0,'未完成流程',6,'/workflow/undone',1,2,'正在进行的工作流');

CREATE TABLE if not EXISTS `t_sys_role_menu_ref` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `menu_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

insert into t_sys_role_menu_ref(role_id,menu_id,version) values (1,1,0);
insert into t_sys_role_menu_ref(role_id,menu_id,version) values (1,2,0);
insert into t_sys_role_menu_ref(role_id,menu_id,version) values (1,3,0);
insert into t_sys_role_menu_ref(role_id,menu_id,version) values (1,4,0);
insert into t_sys_role_menu_ref(role_id,menu_id,version) values (1,5,0);
insert into t_sys_role_menu_ref(role_id,menu_id,version) values (1,6,0);
insert into t_sys_role_menu_ref(role_id,menu_id,version) values (1,7,0);
insert into t_sys_role_menu_ref(role_id,menu_id,version) values (1,8,0);
insert into t_sys_role_menu_ref(role_id,menu_id,version) values (1,9,0);
insert into t_sys_role_menu_ref(role_id,menu_id,version) values (1,10,0);
insert into t_sys_role_menu_ref(role_id,menu_id,version) values (2,6,0);
insert into t_sys_role_menu_ref(role_id,menu_id,version) values (2,7,0);
insert into t_sys_role_menu_ref(role_id,menu_id,version) values (2,8,0);
insert into t_sys_role_menu_ref(role_id,menu_id,version) values (2,9,0);
insert into t_sys_role_menu_ref(role_id,menu_id,version) values (2,10,0);
