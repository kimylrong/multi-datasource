-- branch schema
create table sys_branch(
	id number(8,0), 
	id_parent number(8,0), -- 父机构ID
	code varchar2(24), -- 机构代码
	name varchar2(256),  -- 机构名称
	province varchar2(64), -- 省份
	city varchar2(64), -- 城市
	address varchar2(256),  -- 地址
	telephone varchar2(24),  -- 联系电话
	fax_number varchar2(24),  -- 传真
	contact_person varchar2(64), -- 联系人
	primary key (id)
);
create sequence seq_sys_branch minvalue 2 maxvalue 9999999999 increment by 1 start with 1 cache 20;
insert into sys_branch (id, code, name) values (1, '1001', '浦发银行');

-- user schema, for authorization
create table sys_user(
	id number(8,0),
	name varchar2(24), -- 登陆名 
	nick varchar2(24),  -- 昵称,用于客户端显示
	password varchar2(48), -- 加密后的密码
	salt varchar2(48), -- 加密因子
	branch_id number(8,0), -- 机构ID
	status varchar2(16),  -- 状态(A:Active, D: Disable)
	primary key (id)
);
create sequence seq_sys_user minvalue 2 maxvalue 9999999999 increment by 1 start with 2 cache 20 ;
create index ind_user_name on sys_user(name);
insert into sys_user values (1, 'admin', 'Admin', 'da24e55759cbd0dcf8f7f5c32d2935d60c94bb92', 'ba10eeefc1445bf10313cd53d204c420', 1, 'A');
 
-- role schema, for authentication
create table sys_role(
	id number(8,0), 
	name varchar2(48), -- 角色名称
	description varchar2(256), -- 角色描述 
	branch_id number(8,0),  -- 机构ID
	primary key (id)
);
create sequence seq_sys_role minvalue 1 maxvalue 9999999999 increment by 1 start with 1 cache 20;

-- privilege schema, for authentication
create table sys_privilege(
	id number(8,0), 
	category varchar2(48),  -- 类别
	code varchar2(48),   -- 代码, 由大到小，用冒号分隔，例如: sys:user:edit。
	name varchar2(48), -- 名称
	description varchar2(256), --  描述信息
	primary key (id)
);
create sequence seq_sys_privilege  minvalue 1 maxvalue 9999999999 increment by 1 start with 1 cache 20;

-- relationship between user and role 
create table sys_user_role(
 	user_id number(8,0), 
	role_id number(8,0)
);
alter table sys_user_role add constraint uc_user_role unique (user_id, role_id);
create index ind_user_role on sys_user_role(user_id);

-- relationship between role and privilege 
create table sys_role_privilege (
	role_id number(8,0), 
	privilege_id number(8,0)
);
alter table sys_role_privilege add constraint uc_role_privilege unique (role_id, privilege_id);
create index ind_role_privilege on sys_role_privilege(role_id);