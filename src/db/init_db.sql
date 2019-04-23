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

 Date: 04/23/2019 20:57:03 PM
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
) ENGINE=InnoDB AUTO_INCREMENT=258 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `comment`
-- ----------------------------
BEGIN;
INSERT INTO `comment` VALUES ('1', '201903301545content', '132', '2', '2019-03-30 15:46:38', null, null, null), ('2', '201903301548content', '132', '2', '2019-03-30 15:49:08', null, null, null), ('3', '201903301603content', '139', '2', '2019-03-30 16:04:37', null, null, null), ('4', '201903301605content', '139', '2', '2019-03-30 16:05:36', null, null, null), ('5', '201903301605content', '138', '2', '2019-03-30 16:06:14', null, null, null), ('149', 'E4FD1F38-F573-4269-AB91-7748E5AEBFDB', '892', '1', '2019-04-12 16:23:28', null, null, null), ('209', '286D74AA-ADDC-4718-A65D-CD714933DA82', '982', '1', '2019-04-12 19:34:52', null, null, null), ('221', 'CCA33097-2F46-4C63-9139-FD9DDDCFB184', '982', '1', '2019-04-12 19:56:23', null, null, null), ('222', '4C022534-CE61-4404-8BF9-804709831A88', '982', '1', '2019-04-12 19:56:23', null, null, null), ('223', '1EF9CDF7-937E-4905-B53C-F5999C234D2F', '982', '1', '2019-04-12 19:56:42', null, null, null), ('224', '1D64E7A9-BB8F-46F3-8D22-34FDFF1079B8', '982', '1', '2019-04-12 19:56:42', null, null, null), ('225', '84109BF2-59EA-4596-AEB4-C3E84F3242AE', '982', '1', '2019-04-12 19:56:42', null, null, null), ('226', '6AF2935A-797C-4DD7-9CC5-2F4A74AB3E53', '982', '1', '2019-04-12 19:56:42', null, null, null), ('227', '422B876F-7848-43D3-B452-B5CF4CDC8715', '982', '1', '2019-04-12 19:56:42', null, null, null), ('228', 'A9DB67AB-865D-4268-A308-5728748D8342', '982', '1', '2019-04-12 19:56:42', null, null, null), ('229', '7B50AECE-A21B-4C50-9C33-7665BDA1B305', '982', '1', '2019-04-12 19:56:42', null, null, null), ('230', 'E703260E-7695-49D3-AD37-71EF9BCAF531', '982', '1', '2019-04-12 19:56:43', null, null, null), ('231', 'CD937684-3254-4508-975B-DF4EF7216723', '982', '1', '2019-04-12 19:56:43', null, null, null), ('232', 'FF550935-ABBD-48C4-83FD-4591DE84F37A', '982', '1', '2019-04-12 19:56:43', null, null, null), ('233', '8753C9F9-7EFB-4D58-AA00-65E93DC68770', '982', '1', '2019-04-12 19:58:17', null, null, null), ('234', 'F20E05A2-C35E-4072-9DE4-C13B40BD51A6', '982', '1', '2019-04-12 19:58:17', null, null, null), ('235', 'E974446C-1D50-40F6-BE76-6D604F60A5A5', '982', '1', '2019-04-12 19:58:17', null, null, null), ('236', '8C3C5AD0-88A5-46D5-91FC-444598846125', '982', '1', '2019-04-12 19:58:17', null, null, null), ('237', 'E65E0220-2F95-4BA4-910A-1A613F4D0290', '982', '1', '2019-04-12 19:58:17', null, null, null), ('238', '6011ADD7-FD55-4667-9F72-F3D0553D44BC', '982', '1', '2019-04-12 19:58:17', null, null, null), ('239', 'C89FC4A7-D82D-4E29-AB1B-696388B8AC44', '982', '1', '2019-04-12 19:58:17', null, null, null), ('240', '193F762F-0B65-40AF-AD89-B691F023D010', '982', '1', '2019-04-12 19:58:17', null, null, null), ('241', 'AC7512EC-FA63-4C89-BA38-BF8DDC67EEDE', '982', '1', '2019-04-12 19:58:17', null, null, null), ('242', 'B8BAB00E-AD4C-44B1-B653-EE4CC21F7BB5', '982', '1', '2019-04-12 19:58:17', null, null, null), ('243', 'F5330128-0FCF-4E98-87DA-DD684F4252C6', '1011', '1', '2019-04-13 19:09:48', null, null, null), ('244', '77669B45-6CB2-4187-A74A-13414D4797D9', '1011', '1', '2019-04-13 19:09:48', null, null, null), ('245', '596AB60A-8B28-4069-A62D-16D48B0E545F', '1011', '1', '2019-04-13 19:09:48', null, null, null), ('246', 'DF6AEF6A-6AC9-4C14-90BB-ACC4988CFB4D', '1011', '1', '2019-04-13 19:09:48', null, null, null), ('247', '94A58E7A-B5B5-4F1F-94CF-BD14D9CF5731', '1011', '1', '2019-04-13 19:09:48', null, null, null), ('248', 'C9DA8BFC-1D1F-445A-BC83-4688B0080193', '1011', '1', '2019-04-13 19:09:48', null, null, null), ('249', 'A197CEEE-BBBD-4CAA-B911-A9F894B6D701', '1011', '1', '2019-04-13 19:09:48', null, null, null), ('250', '6232F077-203E-4B0C-BE2D-E09974596618', '1011', '1', '2019-04-13 19:09:48', null, null, null), ('251', '9266FDC8-F297-43BD-9DD5-7FFD65738F07', '1011', '1', '2019-04-13 19:09:48', null, null, null), ('252', '11304D34-FACA-4852-8F18-5AF616143D61', '1011', '1', '2019-04-13 19:09:48', null, null, null), ('253', 'C65DD7F7-0A9D-48D5-AB30-21737DFEC4A8', '1010', '1', '2019-04-13 19:25:08', null, null, null), ('254', 'D2696206-718D-411B-897C-54CD48191C7D', '1010', '1', '2019-04-13 19:25:08', null, null, null), ('255', '62A5FFC6-3A68-4373-B025-B9691EB27358', '1010', '1', '2019-04-13 19:25:08', null, null, null), ('256', 'FAE27F4B-3B57-408B-8584-F881C2CBFF76', '1010', '1', '2019-04-13 19:25:08', null, null, null), ('257', '331604C7-8C14-4BA9-A82F-C5F513DE85CF', '1010', '1', '2019-04-13 19:25:08', null, null, null);
COMMIT;

-- ----------------------------
--  Table structure for `contest`
-- ----------------------------
DROP TABLE IF EXISTS `contest`;
CREATE TABLE `contest` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contest_id` int(11) NOT NULL,
  `discuss_status` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `contest`
-- ----------------------------
BEGIN;
INSERT INTO `contest` VALUES ('1', '1', '0');
COMMIT;

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
  `contest_creator_id` int(11) DEFAULT NULL COMMENT 'contest的创建者id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `title` (`title`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1012 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `topic`
-- ----------------------------
BEGIN;
INSERT INTO `topic` VALUES ('1', 'test title', 'test title', '2019-03-22 18:11:11', '2019-03-22 20:44:47', '1', '3', null, '0', '6', null, '1', '1', null), ('132', '201903270904title', '201903270904title', '2019-03-27 09:04:30', null, '1', '7', '', '0', '18', '10001', '1', '1', null), ('135', '201903291001title', '201903291001title', '2019-03-29 10:03:54', '2019-03-29 10:39:17', '2', '1', '1', '1', '18', '10001', '1', '1', null), ('136', '201903291015title', '201903291015title', '2019-03-29 10:20:38', null, '2', '30', null, '0', '6', null, null, '1', null), ('138', '201903301121title', '201903301121title', '2019-03-30 11:21:58', null, '2', '8', '1', '1', '18', '10001', '1', '1', null), ('139', '201903301204title', '201903301204title', '2019-03-30 12:05:04', '2019-03-30 12:05:21', '2', '7', null, '0', '6', null, null, '1', null), ('502', '66DBC856-7B47-4AF1-9235-E4A0A8DB0967', '66DBC856-7B47-4AF1-9235-E4A0A8DB0967', '2019-04-12 10:52:33', null, '1', '10', null, '0', '6', '10001', '1', '1', null), ('892', 'E7BFF08C-3474-4CE7-96F7-F69D1BEE6F83', 'E7BFF08C-3474-4CE7-96F7-F69D1BEE6F83', '2019-04-12 15:30:47', null, '1', '7', null, '0', '5', '10001', '1', '1', null), ('940', '9DF16E0D-71C6-414B-850E-7AFEC088B88B', '9DF16E0D-71C6-414B-850E-7AFEC088B88B', '2019-04-12 16:23:41', null, '1', '2', null, '0', '4', '10001', '1', '1', null), ('941', 'DFA881F1-1D96-4737-B9C8-4CF87CBBA07E', 'DFA881F1-1D96-4737-B9C8-4CF87CBBA07E', '2019-04-12 16:23:41', null, '1', '1', null, '0', '4', '10001', '1', '1', null), ('982', 'D4BA48B5-8C07-4855-BD52-CD5301C208A6', 'D4BA48B5-8C07-4855-BD52-CD5301C208A6', '2019-04-12 16:39:29', null, '1', '23', null, '0', '3', '10001', '1', '1', null), ('992', 'AC7512EC-FA63-4C89-BA38-BF8DDC67EEDE', 'AC7512EC-FA63-4C89-BA38-BF8DDC67EEDE', '2019-04-12 19:58:17', null, '1', '0', null, '0', '2', '10001', '1', '1', null), ('993', 'C89FC4A7-D82D-4E29-AB1B-696388B8AC44', 'C89FC4A7-D82D-4E29-AB1B-696388B8AC44', '2019-04-12 19:58:17', null, '1', '0', null, '0', '2', '10001', '1', '1', null), ('994', 'F20E05A2-C35E-4072-9DE4-C13B40BD51A6', 'F20E05A2-C35E-4072-9DE4-C13B40BD51A6', '2019-04-12 19:58:17', null, '1', '0', null, '0', '2', '10001', '1', '1', null), ('995', '8C3C5AD0-88A5-46D5-91FC-444598846125', '8C3C5AD0-88A5-46D5-91FC-444598846125', '2019-04-12 19:58:17', null, '1', '0', null, '0', '2', '10001', '1', '1', null), ('996', '6011ADD7-FD55-4667-9F72-F3D0553D44BC', '6011ADD7-FD55-4667-9F72-F3D0553D44BC', '2019-04-12 19:58:17', null, '1', '0', null, '0', '2', '10001', '1', '1', null), ('997', '193F762F-0B65-40AF-AD89-B691F023D010', '193F762F-0B65-40AF-AD89-B691F023D010', '2019-04-12 19:58:17', null, '1', '0', null, '0', '2', '10001', '1', '1', null), ('998', 'B8BAB00E-AD4C-44B1-B653-EE4CC21F7BB5', 'B8BAB00E-AD4C-44B1-B653-EE4CC21F7BB5', '2019-04-12 19:58:17', null, '1', '0', null, '0', '2', '10001', '1', '1', null), ('999', '8753C9F9-7EFB-4D58-AA00-65E93DC68770', '8753C9F9-7EFB-4D58-AA00-65E93DC68770', '2019-04-12 19:58:17', null, '1', '0', null, '0', '2', '10001', '1', '1', null), ('1000', 'E65E0220-2F95-4BA4-910A-1A613F4D0290', 'E65E0220-2F95-4BA4-910A-1A613F4D0290', '2019-04-12 19:58:17', null, '1', '0', null, '0', '2', '10001', '1', '1', null), ('1001', 'E974446C-1D50-40F6-BE76-6D604F60A5A5', 'E974446C-1D50-40F6-BE76-6D604F60A5A5', '2019-04-12 19:58:17', null, '1', '0', null, '0', '2', '10001', '1', '1', null), ('1002', 'CEE25422-76DB-4091-9FCF-728F71A85F8F', 'CEE25422-76DB-4091-9FCF-728F71A85F8F', '2019-04-13 19:05:49', null, '1', '0', null, '0', '2', null, null, '1', null), ('1003', '1F2C3D74-5652-4F89-884B-FA0BA3B11A7C', '1F2C3D74-5652-4F89-884B-FA0BA3B11A7C', '2019-04-13 19:05:49', null, '1', '0', null, '0', '2', null, null, '1', null), ('1004', '2918BFBD-847C-469F-983B-262BD135413C', '2918BFBD-847C-469F-983B-262BD135413C', '2019-04-13 19:05:49', null, '1', '0', null, '0', '2', null, null, '1', null), ('1005', '2CBBF824-5A82-417F-8AFA-49C9A616A9C3', '2CBBF824-5A82-417F-8AFA-49C9A616A9C3', '2019-04-13 19:05:49', null, '1', '0', null, '0', '2', null, null, '1', null), ('1006', '9A1787EA-1842-474A-8AA9-E311027B3FB2', '9A1787EA-1842-474A-8AA9-E311027B3FB2', '2019-04-13 19:05:49', null, '1', '0', null, '0', '2', null, null, '1', null), ('1007', 'DF662B8D-6AD3-43BD-86DB-1C06E031FC97', 'DF662B8D-6AD3-43BD-86DB-1C06E031FC97', '2019-04-13 19:05:49', null, '1', '0', null, '0', '2', null, null, '1', null), ('1008', 'FD39BE67-D1B7-4C75-B5AC-BAE1DFA04992', 'FD39BE67-D1B7-4C75-B5AC-BAE1DFA04992', '2019-04-13 19:05:49', null, '1', '0', null, '0', '2', null, null, '1', null), ('1009', '40296E79-BEB4-4F38-AE53-8426EB2FBC5B', '40296E79-BEB4-4F38-AE53-8426EB2FBC5B', '2019-04-13 19:05:49', null, '1', '0', null, '0', '2', null, null, '1', null), ('1010', '4C4AA1AB-2910-4872-BEA3-A12C2027844D', '4C4AA1AB-2910-4872-BEA3-A12C2027844D', '2019-04-13 19:05:50', null, '1', '5', null, '0', '2', null, null, '1', null), ('1011', '0DEC9460-BDEB-4FCF-A7C7-4DEB5249421F', '0DEC9460-BDEB-4FCF-A7C7-4DEB5249421F', '2019-04-13 19:05:50', null, '1', '10', null, '0', '2', null, null, '1', null);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
