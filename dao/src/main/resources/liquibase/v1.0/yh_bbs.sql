--liquibase formatted sql
--changeset RenTianshuai:2019-09-27-v1.0

/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50547
Source Host           : localhost:3306
Source Database       : yh_bbs

Target Server Type    : MYSQL
Target Server Version : 50547
File Encoding         : 65001

Date: 2019-09-27 10:00:30
*/


-- ----------------------------
-- Table structure for posts
-- ----------------------------
DROP TABLE IF EXISTS `posts`;
CREATE TABLE `posts` (
  `id` varchar(32) NOT NULL,
  `label_id` varchar(50) NOT NULL COMMENT '标签ID',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `content` longtext NOT NULL COMMENT '详细描述',
  `experience` int(10) DEFAULT NULL COMMENT '悬赏飞吻',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `read_count` int(10) DEFAULT NULL COMMENT '阅读数',
  `top` tinyint(1) DEFAULT 0 COMMENT '置顶',
  `essence` tinyint(1) DEFAULT 0 COMMENT '精华',
  `status` tinyint(2) NOT NULL COMMENT '状态（0-新增 2-提交 3-打回 4-发布）',
  `score` tinyint(3) DEFAULT NULL COMMENT '得分',
  `approver` varchar(32) DEFAULT NULL COMMENT '审批人',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx1`(`label_id`) USING BTREE,
  INDEX `idx2`(`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '帖子表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for posts_collection
-- ----------------------------
DROP TABLE IF EXISTS `posts_collection`;
CREATE TABLE `posts_collection` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL COMMENT '收藏人ID',
  `posts_id` varchar(32) NOT NULL COMMENT '帖子ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx1`(`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '收藏表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for posts_column
-- ----------------------------
DROP TABLE IF EXISTS `posts_label`;
CREATE TABLE `posts_label` (
  `id` varchar(50) NOT NULL COMMENT '标签编码',
  `name` varchar(50) NOT NULL COMMENT '标签名称',
  `sort` int(5) DEFAULT NULL COMMENT '排序',
  `usable` char(1) DEFAULT NULL COMMENT '是否可用',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标记',
  `is_approve` char(1) DEFAULT '0' COMMENT '是否审批',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '标签表' ROW_FORMAT = Dynamic;

INSERT INTO `posts_label` VALUES ('communication', '交流', '10', '1', '0', '0');
INSERT INTO `posts_label` VALUES ('article', '美文', '20', '1', '0', '0');
INSERT INTO `posts_label` VALUES ('diary', '日记', '30', '1', '0', '1');


-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` varchar(50) NOT NULL COMMENT '角色编码',
  `name` varchar(50) NOT NULL COMMENT '角色名称',
  `usable` char(1) DEFAULT NULL COMMENT '是否可用',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表' ROW_FORMAT = Dynamic;

INSERT INTO `role` VALUES ('sys_admin', '论坛管理员', '1', '0');
INSERT INTO `role` VALUES ('school_admin', '学校管理员', '1', '0');
INSERT INTO `role` VALUES ('teacher', '老师', '1', '0');
INSERT INTO `role` VALUES ('student', '学生', '1', '0');
INSERT INTO `role` VALUES ('vip', '重要会员', '1', '0');
INSERT INTO `role` VALUES ('member', '普通会员', '1', '0');



-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `realname` varchar(100) DEFAULT NULL COMMENT '真实名称',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `sex` varchar(3) DEFAULT NULL COMMENT '性别',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `portrait` varchar(250) DEFAULT NULL COMMENT '头像',
  `salt` varchar(100) DEFAULT NULL,
  `email` varchar(20) DEFAULT NULL COMMENT '邮件',
  `email_activate` varchar(1) DEFAULT NULL COMMENT '邮箱激活',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `role_id` varchar(50) DEFAULT NULL COMMENT '角色ID',
  `auth` varchar(1) DEFAULT NULL,
  `auth_info` varchar(250) DEFAULT NULL COMMENT '认证信息',
  `experience` int(10) DEFAULT NULL COMMENT '飞吻',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `school` varchar(50) DEFAULT NULL COMMENT '学校',
  `class_name` varchar(50) DEFAULT NULL COMMENT '班级',
  `signature` varchar(250) DEFAULT NULL COMMENT '签名',
  `status` varchar(255) DEFAULT NULL COMMENT '系统用户的状态',
  `register_date` datetime DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `del_flag` char(1) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_username` (`username`) USING BTREE,
  UNIQUE KEY `idx_user_email` (`email`) USING BTREE,
  UNIQUE KEY `idx_user_phone` (`phone`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表' ROW_FORMAT = Dynamic;

INSERT INTO `yh_bbs`.`user`(`id`, `realname`, `username`, `sex`, `password`, `portrait`, `salt`, `email`, `email_activate`, `phone`, `role_id`, `auth`, `auth_info`, `experience`, `city`, `school`, `class_name`, `signature`, `status`, `register_date`, `remarks`, `del_flag`) VALUES ('631432778313371648', NULL, '红豆生南国', 'M', 'a060d937974c074b013e4ab8f0ad0ed0', '/images/avatar/00.jpg', '58db687fce5b1911b54e31c83df2c852', 'rtswyd@163.com', 'Y', NULL, 'sys_admin', NULL, NULL, 0, NULL, NULL, NULL, '人生就像一场修行', NULL, '2019-10-01 17:29:33', NULL, '0');

DROP TABLE IF EXISTS `posts_reply`;
CREATE TABLE `posts_reply` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL COMMENT '收藏人ID',
  `posts_id` varchar(32) NOT NULL COMMENT '帖子ID',
  `content` text NOT NULL COMMENT '详细描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx1`(`user_id`) USING BTREE,
  INDEX `idx2`(`posts_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '回复表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NULL COMMENT '接收人',
  `message` text NULL COMMENT '消息内容',
  `create_time` datetime(0) NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '消息表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization`  (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(200) NULL COMMENT '名称',
  `parent_id` varchar(32) NULL COMMENT '父节点',
  `create_time` datetime(0) NULL COMMENT '创建时间',
  `del_flag` char(1) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '组织表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `posts_approve_log`;
CREATE TABLE `posts_approve_log` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL COMMENT '审批人ID',
  `posts_id` varchar(32) NOT NULL COMMENT '帖子ID',
  `content` varchar(2048) DEFAULT NULL COMMENT '评语',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx1`(`posts_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '日记审批历史表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `user_like_log`;
CREATE TABLE `user_like_log` (
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户ID',
  `posts_id` varchar(32) NOT NULL COMMENT '帖子ID',
  `zan` tinyint(1) DEFAULT NULL COMMENT '赞',
  `operater_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`user_id`,`posts_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '用户点赞历史表' ROW_FORMAT = Dynamic;