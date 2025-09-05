/*
 Navicat Premium Data Transfer

 Source Server         : Window127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80041
 Source Host           : localhost:3306
 Source Schema         : easy_enroll

 Target Server Type    : MySQL
 Target Server Version : 80041
 File Encoding         : 65001

 Date: 02/09/2025 10:43:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公告标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告内容',
  `publish_time` datetime NOT NULL COMMENT '发布时间',
  `public_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布人',
  `status` enum('PUBLISHED','DRAFT','DELETED') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DRAFT' COMMENT '发布状态（已发布、草稿、已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of announcement
-- ----------------------------
INSERT INTO `announcement` VALUES (1, '体育-篮球选课通知', '将于2025年9月20日开始课程选择，维持一周，请大家留意时间，积极选课！啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊', '2025-08-20 12:55:03', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (12, 'aaaaaaaa', '的方式给用户体验和啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv', '2025-08-05 10:08:44', 'admin', 'DRAFT');
INSERT INTO `announcement` VALUES (13, '', '会偶可以今天已经有', '2025-08-20 07:10:04', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (14, '', '亏咯iu也可以能提供贴吧', '2025-08-05 11:53:19', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (15, '', 'sfsafsfsfsfs', '2025-08-20 06:29:31', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (16, '', 'dfggkyukyujyhty', '2025-08-05 13:34:17', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (17, '', 'hjjjjjjjjjjjjjjjjjjjjjjjjjjjj', '2025-08-05 13:34:25', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (18, '', '啊啊啊啊啊啊啊啊啊', '2025-08-05 13:41:47', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (19, '', '啊啊啊啊啊啊啊啊', '2025-08-19 09:17:18', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (20, '', '过年回家也会让个人', '2025-08-19 08:47:02', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (21, '', '54537343', '2025-08-05 13:54:56', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (22, '', '的风格大风范围', '2025-08-20 06:17:43', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (24, '', '啊实打实大苏打', '2025-08-19 08:43:18', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (25, '', '发顺丰的吩咐我', '2025-08-06 04:42:22', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (29, '', '大师赛单打', '2025-08-20 06:21:55', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (30, '', '撒旦撒大大', '2025-08-06 04:45:27', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (31, '撒啊', '撒大苏打撒旦', '2025-08-06 04:45:35', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (32, '啊啊啊', '啊实打实大大萨达萨达的啊哇哇哇顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊呱呱呱呱呱呱呱呱呱呱呱呱呱呱呱古古怪怪哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦', '2025-08-06 06:01:36', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (33, '', '撒大苏打撒旦', '2025-08-06 04:50:05', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (34, '222', '222', '2025-08-06 05:31:19', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (35, '', '撒SASAD', '2025-08-06 05:35:58', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (36, 'NEW', 'NEW', '2025-08-19 08:23:28', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (37, '11', '1111', '2025-08-19 08:25:17', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (38, '刘老师说', '啊啊啊', '2025-08-12 09:24:50', '刘老师', 'PUBLISHED');
INSERT INTO `announcement` VALUES (39, '张老师说', '啊啊大苏打伟大的哇', '2025-08-19 09:26:34', '张老师', 'DRAFT');
INSERT INTO `announcement` VALUES (40, '111', 'adadasdas', '2025-09-01 12:04:12', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (41, '12222', 'adasdawdwd', '2025-08-19 15:00:00', 'admin', 'DRAFT');
INSERT INTO `announcement` VALUES (42, '333', '3333', '2025-08-26 16:00:00', 'admin', 'DRAFT');
INSERT INTO `announcement` VALUES (43, '刘老师的公告', '刘老师说撒大苏打实打实大苏打', '2025-08-20 08:06:45', '刘老师', 'PUBLISHED');
INSERT INTO `announcement` VALUES (44, '刘老师666', '的撒达娃大无畏', '2025-08-19 08:30:15', '刘老师', 'PUBLISHED');
INSERT INTO `announcement` VALUES (45, '大撒大撒', '的撒旦撒旦', '2025-08-20 07:10:45', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (46, 'test', 'TEST', '2025-08-20 07:18:03', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (47, '撒大大', '撒大苏打', '2025-08-20 07:25:55', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (48, '大师傅风格化法国', '法国人发文单位', '2025-08-20 07:27:17', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (49, '工行股份', '倒萨阿萨大大', '2025-08-20 10:12:15', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (50, 'sdadasd', 'dfsdfsdf', '2025-08-20 10:16:14', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (51, 'wrtertrtytr', 'erfewr', '2025-08-20 10:17:52', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (52, 'fdsfsdfs', 'dsfdsfsdfsd', '2025-08-20 11:31:05', 'admin', 'DELETED');
INSERT INTO `announcement` VALUES (53, '32165', '351498', '2025-08-20 11:31:03', 'admin', 'DELETED');
INSERT INTO `announcement` VALUES (54, 'zcsdfsfsf', 'dsfsdfsdfsf', '2025-08-20 11:31:01', 'admin', 'DELETED');
INSERT INTO `announcement` VALUES (55, 'sadsfsdf', 'fdsfdsfsfs', '2025-08-20 11:30:58', 'admin', 'DELETED');
INSERT INTO `announcement` VALUES (56, 'sfdsfsdf', 'dsfdsfsdf', '2025-08-20 11:30:56', 'admin', 'DELETED');
INSERT INTO `announcement` VALUES (57, 'dgfdgdf', 'gfdgdfgdf', '2025-08-20 11:31:11', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (59, '撒大苏打', '撒大苏打', '2024-08-23 14:40:42', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (60, 'TEST', 'TEST', '2025-08-20 13:09:00', 'admin', 'PUBLISHED');
INSERT INTO `announcement` VALUES (62, '111', '111', '2025-09-01 07:44:49', '刘老师', 'PUBLISHED');
INSERT INTO `announcement` VALUES (63, '测试1', '测试1', '2025-09-01 11:51:41', '刘老师', 'PUBLISHED');
INSERT INTO `announcement` VALUES (64, '11', '11', '2025-09-01 12:05:25', '刘老师', 'PUBLISHED');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `reviewer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论者姓名',
  `reviewer_no` bigint NOT NULL COMMENT '评论者编号',
  `like_count` bigint NOT NULL COMMENT '点赞数默认0',
  `related_post_id` int NOT NULL COMMENT '相关的贴子id',
  `related_comment_id` int NULL DEFAULT NULL COMMENT '回复的评论id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 192 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (189, '确实不错', '张三', 20221614001, 1, 11, NULL);
INSERT INTO `comment` VALUES (191, '1', '刘老师', 6666001, 0, 13, NULL);
INSERT INTO `comment` VALUES (192, '2', '刘老师', 6666001, 1, 13, 191);

-- ----------------------------
-- Table structure for course_rating
-- ----------------------------
DROP TABLE IF EXISTS `course_rating`;
CREATE TABLE `course_rating`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程名',
  `course_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程代码',
  `rating` double NULL DEFAULT NULL COMMENT '综合负载评价',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 674 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_rating
-- ----------------------------
INSERT INTO `course_rating` VALUES (685, '数据结构', '8888001', 4.5);
INSERT INTO `course_rating` VALUES (686, '体育-篮球', '8888002', 0);
INSERT INTO `course_rating` VALUES (687, '面向对象', '8888003', 3);
INSERT INTO `course_rating` VALUES (688, '体育-羽毛球', '8888004', 0);
INSERT INTO `course_rating` VALUES (689, '体育-乒乓球', '8888005', 0);
INSERT INTO `course_rating` VALUES (690, '计算机网络', '8888006', 0);
INSERT INTO `course_rating` VALUES (691, '操作系统', '8888007', 0);
INSERT INTO `course_rating` VALUES (692, '软件工程导论', '8888008', 0);
INSERT INTO `course_rating` VALUES (693, '体育-网球', '8888009', 0);
INSERT INTO `course_rating` VALUES (694, '计算机组成原理', '8888010', 0);

-- ----------------------------
-- Table structure for courses
-- ----------------------------
DROP TABLE IF EXISTS `courses`;
CREATE TABLE `courses`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程代码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程名称',
  `credit` double NOT NULL COMMENT '课程学分',
  `course_type` enum('REQUIRED','ELECTIVE','GENERAL') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程类型（必修/选修/通识）',
  `college` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '开课学院',
  `capacity` int NULL DEFAULT NULL COMMENT '容量',
  `remain` int NULL DEFAULT NULL COMMENT '剩余名额',
  `is_seckill` int NOT NULL DEFAULT 0 COMMENT '是否是秒杀课程，是则为1，否则为0',
  `semester` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '开课学期',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '课程描述',
  `teacher_id` int NOT NULL COMMENT '教师id',
  `class_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上课时间（eg：每周一，第一、二节课）',
  `class_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上课地点（ge：勤学楼A-602）',
  `week_range` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '周次范围i（eg：1-16周）',
  `pre_course` int NULL DEFAULT NULL COMMENT '前置课程id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of courses
-- ----------------------------
INSERT INTO `courses` VALUES (1, '8888001', '数据结构', 2.5, 'REQUIRED', '软件学院', NULL, NULL, 0, '2025年上学期', NULL, 1, '每周一，第3、4节', '勤学楼7A602', '1-16周', NULL);
INSERT INTO `courses` VALUES (2, '8888002', '体育-篮球', 2.5, 'ELECTIVE', '软件学院', 100, 99, 1, '2025年下学期', NULL, 1, '每周三，第5、6节', '大操场', '1-8周', NULL);
INSERT INTO `courses` VALUES (5, '8888003', '面向对象', 2.5, 'REQUIRED', '软件学院', NULL, NULL, 0, '2025年上学期', '', 4, '每周一，第1、2节', '勤学楼7B101', '1-16周', 1);
INSERT INTO `courses` VALUES (6, '8888004', '体育-羽毛球', 1.5, 'ELECTIVE', '软件学院', 100, 100, 1, '2025年上学期', '', 4, '每周三，第3、4节', '大操场', '1-16周', NULL);
INSERT INTO `courses` VALUES (8, '8888005', '体育-乒乓球', 1.5, 'ELECTIVE', '软件学院', 100, 100, 1, '2025年上学期', '', 4, '每周四，第7、8节', '大操场', '1-16周', NULL);
INSERT INTO `courses` VALUES (9, '8888006', '计算机网络', 2.5, 'REQUIRED', '软件学院', NULL, NULL, 0, '2025年上学期', '', 6, '每周二，第5、6节', '勤学楼7B-206', '1-16周', NULL);
INSERT INTO `courses` VALUES (10, '8888007', '操作系统', 2.5, 'REQUIRED', '软件学院', NULL, NULL, 0, '2025年上学期', '', 1, '每周五，第7、8节', '勤学楼7A305', '1-16周', NULL);
INSERT INTO `courses` VALUES (11, '8888008', '软件工程导论', 2.5, 'REQUIRED', '软件学院', NULL, NULL, 0, '2025年下学期', '', 8, '每周四，第7、8节', '勤学楼7B202', '5-8周', NULL);
INSERT INTO `courses` VALUES (12, '8888009', '体育-网球', 1.5, 'ELECTIVE', '软件学院', 50, 49, 1, '2025年上学期', '', 8, '每周四，第1、2节', '大操场', '1-16周', NULL);
INSERT INTO `courses` VALUES (13, '8888010', '计算机组成原理', 1.5, 'REQUIRED', '软件学院', NULL, NULL, 0, '2025年下学期', '', 2, '每周二，第5、6节', '勤学楼7A-303', '1-16周', NULL);

-- ----------------------------
-- Table structure for exam
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `exam_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '考试代码',
  `course_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程代码',
  `start_time` datetime NOT NULL COMMENT '考试开始时间',
  `end_time` datetime NOT NULL COMMENT '考试结束时间',
  `state` int NOT NULL COMMENT '考试状态（1正常，0取消，2延迟,3结束）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam
-- ----------------------------
INSERT INTO `exam` VALUES (8, '88880011756713280637', '8888001', '2025-09-02 07:48:44', '2025-09-02 09:48:47', 3);

-- ----------------------------
-- Table structure for grade
-- ----------------------------
DROP TABLE IF EXISTS `grade`;
CREATE TABLE `grade`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程代码',
  `sno` bigint NOT NULL COMMENT '学生学号',
  `grade` double NULL DEFAULT NULL COMMENT '成绩',
  `state` int NOT NULL COMMENT '考试结果状态（1正常，0缺考，2作弊）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of grade
-- ----------------------------
INSERT INTO `grade` VALUES (3, '8888001', 20221614001, 90, 1);
INSERT INTO `grade` VALUES (4, '8888001', 20221614002, 95.5, 1);
INSERT INTO `grade` VALUES (5, '8888001', 20221614003, 80, 1);

-- ----------------------------
-- Table structure for like
-- ----------------------------
DROP TABLE IF EXISTS `like`;
CREATE TABLE `like`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `liker_no` bigint NOT NULL COMMENT '点赞者编号',
  `comment_id` int NOT NULL COMMENT '点赞评论的id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of like
-- ----------------------------
INSERT INTO `like` VALUES (12, 20221614001, 189);
INSERT INTO `like` VALUES (13, 20221614001, 192);

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `account` bigint NOT NULL COMMENT '留言者账号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '留言者姓名',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '留言内容',
  `create_time` datetime NOT NULL COMMENT '留言时间',
  `course_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '留言课程',
  `type` int NOT NULL COMMENT '留言类型(学生是1，教师是0)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (3, 6666001, '刘老师', '1', '2025-08-29 10:01:05', '8888001', 0);
INSERT INTO `message` VALUES (4, 20221614001, '张三', '1', '2025-08-29 10:02:10', '8888001', 1);
INSERT INTO `message` VALUES (5, 20221614001, '张三', '2', '2025-08-30 09:06:12', '8888001', 1);

-- ----------------------------
-- Table structure for notify_time
-- ----------------------------
DROP TABLE IF EXISTS `notify_time`;
CREATE TABLE `notify_time`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `draft_id` int NOT NULL COMMENT '草稿箱公告id',
  `time` int NULL DEFAULT NULL COMMENT '默认为0不提前发送短信，单位为分钟',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notify_time
-- ----------------------------

-- ----------------------------
-- Table structure for payment
-- ----------------------------
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '缴费id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '缴费项目名',
  `amount` decimal(10, 2) NOT NULL COMMENT '缴费金额',
  `start_time` datetime NOT NULL COMMENT '缴费开始时间',
  `end_time` datetime NOT NULL COMMENT '缴费结束时间',
  `paid_count` int NULL DEFAULT NULL COMMENT '已缴费人数',
  `payable_count` int NOT NULL COMMENT '应缴费人数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment
-- ----------------------------
INSERT INTO `payment` VALUES (1, '教材费', 0.80, '2025-08-15 10:22:40', '2025-08-31 10:22:44', 1, 50);
INSERT INTO `payment` VALUES (2, '公寓费', 0.60, '2025-08-15 10:23:24', '2025-08-30 10:23:26', 0, 50);
INSERT INTO `payment` VALUES (3, '学费啊', 1600.00, '2026-08-19 16:00:00', '2026-08-27 16:00:00', 0, 10000);
INSERT INTO `payment` VALUES (4, '测试费用1', 10.00, '2025-08-16 05:59:34', '2025-08-29 16:00:00', 0, 100);
INSERT INTO `payment` VALUES (5, '测试费用2', 100.00, '2025-08-16 05:59:52', '2025-08-29 16:00:00', 0, 100);
INSERT INTO `payment` VALUES (9, '测试费用3', 100.00, '2025-09-01 10:25:57', '2025-09-29 16:00:00', 2, 100);

-- ----------------------------
-- Table structure for payment_selection
-- ----------------------------
DROP TABLE IF EXISTS `payment_selection`;
CREATE TABLE `payment_selection`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `pay_id` int NOT NULL COMMENT '缴费id',
  `payer_id` bigint NOT NULL COMMENT '缴费人id（此处为学生学号）',
  `pay_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '缴费方式（支付宝支付/微信支付）',
  `pay_time` datetime NOT NULL COMMENT '缴费时间',
  `pay_status` int NOT NULL COMMENT '缴费状态：0是未缴费，1是已缴费',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment_selection
-- ----------------------------
INSERT INTO `payment_selection` VALUES (9, 1, 20221614001, '支付宝', '2025-08-16 15:13:54', 1);
INSERT INTO `payment_selection` VALUES (37, 9, 20221614001, '支付宝', '2025-09-01 10:28:49', 1);
INSERT INTO `payment_selection` VALUES (41, 9, 20221614002, '支付宝', '2025-09-01 10:35:37', 1);

-- ----------------------------
-- Table structure for post_page
-- ----------------------------
DROP TABLE IF EXISTS `post_page`;
CREATE TABLE `post_page`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `mentioned_course_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联课程名',
  `mentioned_course_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联课程代码',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '贴子具体内容',
  `publish_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发布人',
  `publish_sno` bigint NOT NULL COMMENT '发布人编号',
  `publish_time` datetime NOT NULL COMMENT '发布时间',
  `comment_count` bigint NOT NULL COMMENT '评论数默认为0',
  `type` int NOT NULL COMMENT '贴子类型（评价贴1，任意贴0）',
  `rating` double NOT NULL COMMENT '综合评价（默认为0）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post_page
-- ----------------------------
INSERT INTO `post_page` VALUES (11, '强烈安利！一门能真正学到东西且给分超好的课', '数据结构', '8888001', '老师魅力满分： 刘老师不是照本宣科的类型，她的知识储备极其深厚，讲课引经据典、生动有趣，仿佛带你穿越回古代，和文人墨客对话。每节课都像在听故事，完全不会走神。\n\n收获巨大： 这门课彻底改变了我对古文的看法。以前觉得枯燥，现在能主动去欣赏诗词背后的美和情感。老师会推荐很多拓展书目，读起来也很有意思，不知不觉就读了很多书。\n\n考核方式合理： 没有期末考试！评分由一篇小论文（50%）+一次小组Presentation（30%）+平时出勤和互动（20%）组成。只要认真参与了，压力不大。\n\n给分情况： 刘老师是公认的“人美心善给分好”，非常鼓励学生的独立思考，只要你的作业有自己独特的见解，分数都很不错。我们班平均分大概在88左右。', '李四', 20221614002, '2025-08-29 02:48:08', 1, 1, 5);
INSERT INTO `post_page` VALUES (12, '慎选！【面向对象】一些真实的体验供参考', '面向对象', '8888003', '教学方式问题： 老师讲课速度非常快，基本上是照着PPT念，对于难点推导过程经常一笔带过，课后需要花大量时间自学才能跟上。课堂互动几乎为零，气氛比较沉闷。\n\n任务量繁重： 每周一次课后作业，题量很大且难度很高，需要耗费大量时间。此外还有两次随堂小测，都计入总成绩，学习压力比较大。\n\n考核与给分： 期末考试难度极大，平均分据说不到70。平时分给得很模糊，感觉没有明确的标准。整体给分偏低，想刷高GPA的同学请慎重。\n\n主要矛盾点： 感觉课程的重点是“考倒学生”而不是“教会学生”，学习体验比较挫折。', '李四', 20221614002, '2025-08-29 02:49:47', 0, 1, 3);
INSERT INTO `post_page` VALUES (13, '【数据结构】深度评价：优点突出，缺点也明显', '数据结构', '8888001', '(课程优点)\n\n内容前沿： 刘老师有很多业界的实践经验，讲的案例都非常新，而且是真实发生的，能帮助我们理解理论知识如何在现实中应用，这一点很棒。\n\n视野开阔： 课程会邀请很多企业高管来做分享，能接触到行业一线的最新动态，这是其他课程没有的福利。\n\n(课程缺点 / 注意事项)\n\n节奏安排紧张： 因为有很多嘉宾演讲，有时会打乱教学进度，导致核心理论部分讲得有些仓促，需要自己课后花时间消化。\n\n小组作业是核心： 分数很大比例压在一个大规模的小组项目上，非常考验团队协作能力和队友的靠谱程度。如果遇到“划水”的队友，体验会很差。\n\n给分中规中矩： 小组项目拿高分很难，个人作业和考试区分度不大，所以最后总分基本在82-88之间，想拿90+需要付出极大努力并有亮眼表现。\n\n(总结与建议)\n这是一门“上限很高，下限也不低”的课程。\n\n适合这样的同学： 自律性强、善于团队合作、希望拓展业界人脉、对市场营销有浓厚兴趣。\n\n不适合这样的同学： 喜欢独自学习、不擅长小组作业、只想轻松拿高分的同学。\n\n如果你愿意接受挑战，这门课会给你带来远超课本的收获。建议选课前先找好靠谱的队友！', '张三', 20221614001, '2025-08-29 02:50:35', 2, 1, 4);

-- ----------------------------
-- Table structure for pre_selection
-- ----------------------------
DROP TABLE IF EXISTS `pre_selection`;
CREATE TABLE `pre_selection`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL COMMENT '学生id',
  `course_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程代码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pre_selection
-- ----------------------------
INSERT INTO `pre_selection` VALUES (1, 21, '8888009');
INSERT INTO `pre_selection` VALUES (2, 21, '8888011');
INSERT INTO `pre_selection` VALUES (24, 1, '8888002');

-- ----------------------------
-- Table structure for seckill_tasks
-- ----------------------------
DROP TABLE IF EXISTS `seckill_tasks`;
CREATE TABLE `seckill_tasks`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_id` int NOT NULL COMMENT '课程id（只对秒杀课程有效）',
  `stock` bigint NOT NULL COMMENT '容量',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `status` enum('NOT_STARTED','ONGOING','FINISHED') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'FINISHED' COMMENT '秒杀状态（未开始、正在抢课中、已结束）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of seckill_tasks
-- ----------------------------
INSERT INTO `seckill_tasks` VALUES (6, 8, 100, '2025-08-09 04:58:20', '2025-08-21 16:00:00', 'FINISHED');
INSERT INTO `seckill_tasks` VALUES (7, 6, 100, '2025-08-09 07:01:00', '2025-08-30 05:46:54', 'FINISHED');
INSERT INTO `seckill_tasks` VALUES (8, 2, 99, '2025-09-01 08:45:26', '2025-09-30 08:44:27', 'ONGOING');
INSERT INTO `seckill_tasks` VALUES (9, 12, 49, '2025-08-31 13:34:20', '2025-09-06 16:00:00', 'ONGOING');

-- ----------------------------
-- Table structure for selections
-- ----------------------------
DROP TABLE IF EXISTS `selections`;
CREATE TABLE `selections`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL COMMENT '学生id',
  `course_id` int NOT NULL COMMENT '课程id',
  `semester` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学期',
  `selection_time` datetime NOT NULL COMMENT '选课时间',
  `status` enum('NORMAL','DROPPED') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'NORMAL' COMMENT '选课状态（正常/已退课）默认为normal',
  `is_seckill` int NOT NULL DEFAULT 0 COMMENT '是否是秒杀课程，是则为1，否则为0，默认为0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 141 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of selections
-- ----------------------------
INSERT INTO `selections` VALUES (141, 1, 1, '2025年上学期', '2025-09-01 07:29:50', 'NORMAL', 0);
INSERT INTO `selections` VALUES (142, 1, 5, '2025年上学期', '2025-09-01 07:29:52', 'NORMAL', 0);
INSERT INTO `selections` VALUES (143, 1, 9, '2025年上学期', '2025-09-01 07:29:53', 'NORMAL', 0);
INSERT INTO `selections` VALUES (144, 1, 10, '2025年上学期', '2025-09-01 07:29:54', 'NORMAL', 0);
INSERT INTO `selections` VALUES (145, 1, 11, '2025年下学期', '2025-09-01 07:29:55', 'NORMAL', 0);
INSERT INTO `selections` VALUES (147, 2, 1, '2025年上学期', '2025-09-01 07:34:32', 'NORMAL', 0);
INSERT INTO `selections` VALUES (148, 2, 5, '2025年上学期', '2025-09-01 07:34:32', 'NORMAL', 0);
INSERT INTO `selections` VALUES (149, 2, 9, '2025年上学期', '2025-09-01 07:34:32', 'NORMAL', 0);
INSERT INTO `selections` VALUES (150, 2, 10, '2025年上学期', '2025-09-01 07:34:32', 'NORMAL', 0);
INSERT INTO `selections` VALUES (151, 2, 11, '2025年下学期', '2025-09-01 07:34:32', 'NORMAL', 0);
INSERT INTO `selections` VALUES (152, 2, 13, '2025年下学期', '2025-09-01 07:34:32', 'NORMAL', 0);
INSERT INTO `selections` VALUES (153, 5, 1, '2025年上学期', '2025-09-01 07:34:32', 'NORMAL', 0);
INSERT INTO `selections` VALUES (154, 5, 5, '2025年上学期', '2025-09-01 07:34:32', 'NORMAL', 0);
INSERT INTO `selections` VALUES (155, 5, 9, '2025年上学期', '2025-09-01 07:34:32', 'NORMAL', 0);
INSERT INTO `selections` VALUES (156, 5, 10, '2025年上学期', '2025-09-01 07:34:32', 'NORMAL', 0);
INSERT INTO `selections` VALUES (157, 5, 11, '2025年下学期', '2025-09-01 07:34:32', 'NORMAL', 0);
INSERT INTO `selections` VALUES (158, 5, 13, '2025年下学期', '2025-09-01 07:34:32', 'NORMAL', 0);
INSERT INTO `selections` VALUES (163, 1, 12, '2025年上学期', '2025-09-01 08:42:08', 'NORMAL', 1);
INSERT INTO `selections` VALUES (164, 1, 2, '2025年下学期', '2025-09-01 08:52:42', 'NORMAL', 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms
-- ----------------------------
INSERT INTO `sms` VALUES (54, NULL, 88888888, 18234805566, NULL, 2, 1, '2025-08-20 13:09:00', '2025-08-20 13:09:00', 0, '孙八，新的公告在2025-08-20 21:09:00发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (55, NULL, 88888888, 15845236547, NULL, 2, 1, '2025-08-20 13:09:00', '2025-08-20 13:09:00', 0, '周九，新的公告在2025-08-20 21:09:00发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (56, NULL, 88888888, 13753804085, NULL, 2, 1, '2025-08-20 13:09:00', '2025-08-20 13:09:00', 0, '王五，新的公告在2025-08-20 21:09:00发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (57, NULL, 88888888, 18234806385, NULL, 2, 1, '2025-08-20 13:09:00', '2025-08-20 13:09:00', 0, '张三，新的公告在2025-08-20 21:09:00发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (58, NULL, 88888888, 18234805566, NULL, 2, 1, '2025-09-01 12:04:13', '2025-09-01 12:04:13', 0, '孙八，新的公告在2025-09-01 20:04:12发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (59, NULL, 88888888, 13753804085, NULL, 2, 1, '2025-09-01 12:04:13', '2025-09-01 12:04:13', 0, '王五，新的公告在2025-09-01 20:04:12发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (60, NULL, 88888888, 15845236547, NULL, 2, 1, '2025-09-01 12:04:13', '2025-09-01 12:04:13', 0, '周九，新的公告在2025-09-01 20:04:12发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (61, NULL, 88888888, 15834356385, NULL, 2, 1, '2025-09-01 12:04:13', '2025-09-01 12:04:13', 0, '张三，新的公告在2025-09-01 20:04:12发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (62, NULL, 88888888, 15834356385, NULL, 2, 1, '2025-09-01 12:05:25', '2025-09-01 12:05:25', 0, '张三，新的公告在2025-09-01 20:05:24发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (63, NULL, 88888888, 13753804085, NULL, 2, 1, '2025-09-01 12:05:25', '2025-09-01 12:05:25', 0, '王五，新的公告在2025-09-01 20:05:24发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (64, NULL, 88888888, 18234805566, NULL, 2, 1, '2025-09-01 12:05:25', '2025-09-01 12:05:25', 0, '孙八，新的公告在2025-09-01 20:05:24发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms` VALUES (65, NULL, 88888888, 15845236547, NULL, 2, 1, '2025-09-01 12:05:25', '2025-09-01 12:05:25', 0, '周九，新的公告在2025-09-01 20:05:24发布，请前往查看，避免错漏重要信息哦！');

-- ----------------------------
-- Table structure for sms_template
-- ----------------------------
DROP TABLE IF EXISTS `sms_template`;
CREATE TABLE `sms_template`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板类型：“教师模板teacherTemplate”，“学生模板studentTemplate”',
  `status` int NOT NULL COMMENT '选中状态，默认为0',
  `component` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '最多255字节，约85个中文/255个英文',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_template
-- ----------------------------
INSERT INTO `sms_template` VALUES (5, 'TeacherTemplate', 0, '{{name}}，您有公告需要发布，请前往查看！');
INSERT INTO `sms_template` VALUES (7, 'TeacherTemplate', 1, '您好{{name}}，您有公告预计在{{time}}时发布，请前往查看！');
INSERT INTO `sms_template` VALUES (8, 'StudentTemplate', 1, '{{name}}，新的公告在{{time}}发布，请前往查看，避免错漏重要信息哦！');
INSERT INTO `sms_template` VALUES (9, 'StudentTemplate', 0, '新公告在{{time}}发布，请前往查看');

-- ----------------------------
-- Table structure for students
-- ----------------------------
DROP TABLE IF EXISTS `students`;
CREATE TABLE `students`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '管理users表的id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学生姓名',
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学生性别',
  `age` int NULL DEFAULT NULL COMMENT '学生年龄',
  `sno` bigint NOT NULL COMMENT '学号，关联users表的account',
  `college` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学院',
  `major` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '专业',
  `credit` double NOT NULL COMMENT '学分',
  `phone` bigint NULL DEFAULT NULL COMMENT '联系方式',
  `input_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '入学年月',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of students
-- ----------------------------
INSERT INTO `students` VALUES (1, 1, '张三', '男', 21, 20221614001, '软件学院', '软件工程', 3.8, 15834356385, '2022年9月');
INSERT INTO `students` VALUES (2, 2, '李四', '男', 21, 20221614002, '软件学院', '软件工程', 4, NULL, '2022年9月');
INSERT INTO `students` VALUES (5, 8, '王五', '男', 21, 20221614003, '软件学院', '软件工程', 4.3, 13753804085, '2022年9月');
INSERT INTO `students` VALUES (7, 16, '张三', '', NULL, 20221614004, '软件学院', '软件工程', 3, NULL, '2022年9月');
INSERT INTO `students` VALUES (8, 17, '赵六', '', NULL, 20221614005, '软件学院', '软件工程', 3.8, NULL, '2022年9月');
INSERT INTO `students` VALUES (9, 18, '钱七', '', NULL, 20221614006, '软件学院', '软件工程', 3.9, NULL, '2022年9月');
INSERT INTO `students` VALUES (21, 39, '孙八', '男', 21, 20221614007, '软件学院', '软件工程', 3.8, 18234805566, '2022年9月');
INSERT INTO `students` VALUES (22, 40, '周九', '男', 21, 20221614008, '软件学院', '软件工程', 4, 15845236547, '2022年9月');

-- ----------------------------
-- Table structure for teachers
-- ----------------------------
DROP TABLE IF EXISTS `teachers`;
CREATE TABLE `teachers`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '关联users表的id',
  `tno` bigint NOT NULL COMMENT '工号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '教师姓名',
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师性别',
  `age` int NULL DEFAULT NULL COMMENT '教师年龄',
  `college` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '教师所属学院',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师职称',
  `phone` bigint NULL DEFAULT NULL COMMENT '教师联系方式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teachers
-- ----------------------------
INSERT INTO `teachers` VALUES (1, 4, 6666001, '刘老师', '男', 50, '软件学院', '院长', 15834356385);
INSERT INTO `teachers` VALUES (2, 5, 6666002, '张老师', '女', 38, '林学院', '特级教师', 15834356385);
INSERT INTO `teachers` VALUES (4, 11, 6666003, '王老师', '', NULL, '软件学院', '', NULL);
INSERT INTO `teachers` VALUES (6, 13, 6666004, '李老师', '', NULL, '软件学院', '', NULL);
INSERT INTO `teachers` VALUES (7, 14, 6666005, '钱老师', '', NULL, '软件学院', '', NULL);
INSERT INTO `teachers` VALUES (8, 15, 6666006, '孙老师', '', NULL, '软件学院', '', NULL);
INSERT INTO `teachers` VALUES (9, 41, 6666007, '周老师', '男', NULL, '软件学院', NULL, NULL);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `account` bigint NOT NULL COMMENT '登录账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录密码',
  `role` enum('STUDENT','TEACHER','ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色',
  `is_active` int NOT NULL COMMENT '默认为1，表示活跃，否则为0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 20221614001, '20221614001', 'STUDENT', 1);
INSERT INTO `users` VALUES (2, 20221614002, '20221614002', 'STUDENT', 1);
INSERT INTO `users` VALUES (3, 123456, '123456', 'ADMIN', 1);
INSERT INTO `users` VALUES (4, 6666001, '6666001', 'TEACHER', 1);
INSERT INTO `users` VALUES (5, 6666002, '6666002', 'TEACHER', 1);
INSERT INTO `users` VALUES (8, 20221614003, '20221614003', 'STUDENT', 1);
INSERT INTO `users` VALUES (11, 6666003, '6666003', 'TEACHER', 1);
INSERT INTO `users` VALUES (13, 6666004, '6666004', 'TEACHER', 1);
INSERT INTO `users` VALUES (14, 6666005, '6666005', 'TEACHER', 1);
INSERT INTO `users` VALUES (15, 6666006, '6666006', 'TEACHER', 1);
INSERT INTO `users` VALUES (16, 20221614004, '20221614004', 'STUDENT', 1);
INSERT INTO `users` VALUES (17, 20221614005, '20221614005', 'STUDENT', 1);
INSERT INTO `users` VALUES (18, 20221614006, '20221614006', 'STUDENT', 1);
INSERT INTO `users` VALUES (39, 20221614007, '20221614007', 'STUDENT', 1);
INSERT INTO `users` VALUES (41, 6666007, '6666007', 'TEACHER', 1);
INSERT INTO `users` VALUES (46, 20221614008, '20221614008', 'STUDENT', 1);

SET FOREIGN_KEY_CHECKS = 1;
