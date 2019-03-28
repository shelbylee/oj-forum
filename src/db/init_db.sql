/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost
 Source Database       : oj_forum

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : utf-8

 Date: 03/26/2019 18:14:29 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `comment`
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` longtext NOT NULL,
  `topic_id` int(11) NOT NULL COMMENT '主题id',
  `user_id` int(11) NOT NULL COMMENT '评论者id',
  `create_time` datetime NOT NULL COMMENT '评论创建时间',
  `modify_time` datetime DEFAULT NULL,
  `comment_id` int(11) DEFAULT NULL COMMENT '楼中楼对象的id',
  `up_ids` text,
  PRIMARY KEY (`id`),
  KEY `topic_id` (`topic_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `topic`
-- ----------------------------
DROP TABLE IF EXISTS `topic`;
CREATE TABLE `topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '帖子主题',
  `content` longtext COMMENT '帖子内容',
  `create_time` datetime NOT NULL COMMENT '帖子创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '帖子修改时间',
  `user_id` int(11) NOT NULL COMMENT '创建者id',
  `comment_count` int(11) NOT NULL DEFAULT '0' COMMENT '评论数',
  `up_ids` text COMMENT '点赞用户id',
  `like_count` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `view_count` int(11) NOT NULL DEFAULT '0' COMMENT '浏览量',
  `problem_id` int(11) DEFAULT NULL COMMENT '关联的problem id',
  `contest_id` int(11) DEFAULT NULL COMMENT '关联的contest id',
  `visible` int(2) DEFAULT '1' COMMENT '讨论区是否可见，1为可见，0为不可见',
  PRIMARY KEY (`id`),
  UNIQUE KEY `title` (`title`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `topic`
-- ----------------------------
BEGIN;
INSERT INTO `topic` VALUES ('131', 'test title', 'test edit content', '2019-03-22 18:11:11', '2019-03-22 20:44:47', '1', '0', null, '0', '0', null, null, '1');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
