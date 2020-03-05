/*
Navicat MySQL Data Transfer

Source Server         : MySQL-本地
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : tenement

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2020-03-05 16:41:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for biz_house
-- ----------------------------
DROP TABLE IF EXISTS `biz_house`;
CREATE TABLE `biz_house` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一的主键ID',
  `img_src` varchar(255) NOT NULL COMMENT '房源图片',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `house_type` varchar(255) NOT NULL COMMENT '房型/房源描述',
  `orientation` varchar(255) NOT NULL COMMENT '房源，朝向',
  `sum_price` decimal(10,2) NOT NULL COMMENT '总价',
  `unit_price` decimal(10,2) NOT NULL COMMENT '单价',
  `community` varchar(255) NOT NULL COMMENT '所属小区的名字',
  `house_area` int(4) NOT NULL COMMENT '房间面积',
  `floor` varchar(255) NOT NULL COMMENT '楼层',
  `fitment` varchar(255) NOT NULL COMMENT '装修',
  `heating` varchar(255) NOT NULL COMMENT '供暖',
  `house_year` varchar(255) NOT NULL COMMENT '年代',
  `floor_type` varchar(255) NOT NULL COMMENT '楼层类型',
  `comment` varchar(255) NOT NULL COMMENT '经纪人房评',
  `create_by` int(11) NOT NULL COMMENT '房源创建人',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `del_flag` int(1) NOT NULL DEFAULT '0' COMMENT '是否删除，0否1是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for biz_house_label
-- ----------------------------
DROP TABLE IF EXISTS `biz_house_label`;
CREATE TABLE `biz_house_label` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `house_id` int(11) NOT NULL COMMENT '房源ID',
  `title` varchar(255) NOT NULL COMMENT '标签的值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `user_name` varchar(255) NOT NULL COMMENT '用户名',
  `phone` varchar(255) NOT NULL COMMENT '联系方式',
  `open_id` varchar(255) NOT NULL COMMENT '微信的open_id',
  `img_src` varchar(255) NOT NULL COMMENT '用户头像',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `del_flag` int(1) NOT NULL DEFAULT '0' COMMENT '是否删除，0否1是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
