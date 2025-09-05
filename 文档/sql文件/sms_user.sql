/*
 Navicat Premium Data Transfer

 Source Server         : Window127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80041
 Source Host           : localhost:3306
 Source Schema         : sms_user

 Target Server Type    : MySQL
 Target Server Version : 80041
 File Encoding         : 65001

 Date: 02/09/2025 10:43:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for s_user
-- ----------------------------
DROP TABLE IF EXISTS `s_user`;
CREATE TABLE `s_user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `phone` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of s_user
-- ----------------------------
INSERT INTO `s_user` VALUES (1, 15834356385);
INSERT INTO `s_user` VALUES (2, 13753804085);
INSERT INTO `s_user` VALUES (3, 18234805566);
INSERT INTO `s_user` VALUES (4, 15845236547);

-- ----------------------------
-- Table structure for sms
-- ----------------------------
DROP TABLE IF EXISTS `sms`;
CREATE TABLE `sms`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `thread_id` bigint NULL DEFAULT NULL COMMENT '线程id',
  `sender` bigint NOT NULL COMMENT '发送者手机号，目前统一为学校号码',
  `receiver` bigint NULL DEFAULT NULL COMMENT '接收者手机号（单个接收者时）',
  `subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主题，可以没有',
  `type` int NOT NULL COMMENT '类型（1：接收，2：发送，3：草稿）',
  `status` int NOT NULL COMMENT '状态（0-发送中，1-发送成功，2-发送失败，3-已送达（对于接收的短信，此字段可忽略或设为已送达）',
  `dateSend` datetime NULL DEFAULT NULL COMMENT '发送时间（从学生系统，从sms系统）',
  `dateReceive` datetime NULL DEFAULT NULL COMMENT '接收时间（到sms系统，到用户系统）',
  `read` int NOT NULL COMMENT '是否已读 (0-未读，1-已读)',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '短信内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms
-- ----------------------------
INSERT INTO `sms` VALUES (48, NULL, 88888888, 18234805566, NULL, 1, 3, '2025-08-20 13:09:00', '2025-08-20 13:09:00', 1, '孙八，新的公告在2025-08-20 21:09:00发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (49, NULL, 88888888, 18234806385, NULL, 1, 3, '2025-08-20 13:09:00', '2025-08-20 13:09:00', 1, '张三，新的公告在2025-08-20 21:09:00发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (50, NULL, 88888888, 13753804085, NULL, 1, 3, '2025-08-20 13:09:00', '2025-08-20 13:09:00', 1, '王五，新的公告在2025-08-20 21:09:00发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (51, NULL, 88888888, 15845236547, NULL, 1, 3, '2025-08-20 13:09:00', '2025-08-20 13:09:00', 1, '周九，新的公告在2025-08-20 21:09:00发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (52, NULL, 88888888, 18234805566, NULL, 1, 3, '2025-09-01 12:04:13', '2025-09-01 12:04:13', 1, '孙八，新的公告在2025-09-01 20:04:12发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (53, NULL, 88888888, 15834356385, NULL, 1, 3, '2025-09-01 12:04:13', '2025-09-01 12:04:13', 1, '张三，新的公告在2025-09-01 20:04:12发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (54, NULL, 88888888, 15845236547, NULL, 1, 3, '2025-09-01 12:04:13', '2025-09-01 12:04:13', 0, '周九，新的公告在2025-09-01 20:04:12发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (55, NULL, 88888888, 13753804085, NULL, 1, 3, '2025-09-01 12:04:13', '2025-09-01 12:04:13', 1, '王五，新的公告在2025-09-01 20:04:12发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (56, NULL, 88888888, 13753804085, NULL, 1, 3, '2025-09-01 12:05:25', '2025-09-01 12:05:25', 1, '王五，新的公告在2025-09-01 20:05:24发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (57, NULL, 88888888, 18234805566, NULL, 1, 3, '2025-09-01 12:05:25', '2025-09-01 12:05:25', 1, '孙八，新的公告在2025-09-01 20:05:24发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (58, NULL, 88888888, 15834356385, NULL, 1, 3, '2025-09-01 12:05:25', '2025-09-01 12:05:25', 1, '张三，新的公告在2025-09-01 20:05:24发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (59, NULL, 88888888, 15845236547, NULL, 1, 3, '2025-09-01 12:05:25', '2025-09-01 12:05:25', 0, '周九，新的公告在2025-09-01 20:05:24发布，请前往查看，避免错漏重要信息哦！');

SET FOREIGN_KEY_CHECKS = 1;
