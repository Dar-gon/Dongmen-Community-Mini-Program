-- ============================================================
-- 社区党群便民服务小程序 - 数据库初始化脚本
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------------
-- 1. 用户表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
  id BIGINT NOT NULL AUTO_INCREMENT,
  openid VARCHAR(100) DEFAULT NULL,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(255) DEFAULT NULL,
  phone VARCHAR(20) DEFAULT NULL,
  real_name VARCHAR(50) DEFAULT NULL,
  avatar VARCHAR(500) DEFAULT NULL,
  gender TINYINT DEFAULT 0,
  role VARCHAR(20) NOT NULL DEFAULT 'resident',
  status TINYINT DEFAULT 1,
  points INT DEFAULT 0,
  volunteer_hours INT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_username (username),
  KEY idx_openid (openid),
  KEY idx_phone (phone),
  KEY idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- -----------------------------------------------------------
-- 2. 政策公告表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS t_policy;
CREATE TABLE t_policy (
  id BIGINT NOT NULL AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  content TEXT,
  summary VARCHAR(500),
  cover_img VARCHAR(500),
  category INT DEFAULT 1,
  category_name VARCHAR(50),
  top TINYINT DEFAULT 0,
  view_count INT DEFAULT 0,
  status TINYINT DEFAULT 0,
  publish_time DATETIME,
  publisher_id BIGINT,
  publisher_name VARCHAR(50),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  PRIMARY KEY (id),
  KEY idx_category (category),
  KEY idx_status (status),
  KEY idx_top (top),
  KEY idx_publish_time (publish_time),
  KEY idx_publisher_id (publisher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='政策公告表';

-- -----------------------------------------------------------
-- 3. 民情工单表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS t_petition;
CREATE TABLE t_petition (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_no VARCHAR(50) NOT NULL,
  title VARCHAR(200) NOT NULL,
  content TEXT,
  category VARCHAR(50),
  images TEXT,
  address VARCHAR(500),
  longitude DOUBLE,
  latitude DOUBLE,
  contact_phone VARCHAR(20),
  is_anonymous TINYINT DEFAULT 0,
  user_id BIGINT,
  user_name VARCHAR(50),
  status TINYINT DEFAULT 0,
  assigner_id BIGINT,
  assigner_name VARCHAR(50),
  handler_id BIGINT,
  handler_name VARCHAR(50),
  resolved_time DATETIME,
  resolved_note TEXT,
  rating INT,
  rating_content TEXT,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_order_no (order_no),
  KEY idx_user_id (user_id),
  KEY idx_status (status),
  KEY idx_category (category),
  KEY idx_handler_id (handler_id),
  KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='民情工单表';

-- -----------------------------------------------------------
-- 4. 工单日志表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS t_petition_log;
CREATE TABLE t_petition_log (
  id BIGINT NOT NULL AUTO_INCREMENT,
  petition_id BIGINT NOT NULL,
  action VARCHAR(50),
  operator_id BIGINT,
  operator_name VARCHAR(50),
  content TEXT,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_petition_id (petition_id),
  KEY idx_operator_id (operator_id),
  KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单日志表';

-- -----------------------------------------------------------
-- 5. 志愿活动表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS t_activity;
CREATE TABLE t_activity (
  id BIGINT NOT NULL AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  cover_img VARCHAR(500),
  activity_time DATETIME,
  activity_end_time DATETIME,
  location VARCHAR(500),
  max_participants INT DEFAULT 0,
  current_participants INT DEFAULT 0,
  integral_reward INT DEFAULT 0,
  status TINYINT DEFAULT 0,
  publisher_id BIGINT,
  publisher_name VARCHAR(50),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  PRIMARY KEY (id),
  KEY idx_status (status),
  KEY idx_activity_time (activity_time),
  KEY idx_publisher_id (publisher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='志愿活动表';

-- -----------------------------------------------------------
-- 6. 活动报名表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS t_activity_signup;
CREATE TABLE t_activity_signup (
  id BIGINT NOT NULL AUTO_INCREMENT,
  activity_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  user_name VARCHAR(50),
  user_phone VARCHAR(20),
  check_in_status TINYINT DEFAULT 0,
  check_in_time DATETIME,
  earned_points INT DEFAULT 0,
  status TINYINT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_activity_user (activity_id, user_id),
  KEY idx_user_id (user_id),
  KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动报名表';

-- -----------------------------------------------------------
-- 7. 志愿小队表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS t_volunteer_team;
CREATE TABLE t_volunteer_team (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  description TEXT,
  leader_id BIGINT,
  leader_name VARCHAR(50),
  member_count INT DEFAULT 0,
  status TINYINT DEFAULT 1,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  PRIMARY KEY (id),
  KEY idx_leader_id (leader_id),
  KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='志愿小队表';

-- -----------------------------------------------------------
-- 8. 志愿小队成员表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS t_volunteer_team_member;
CREATE TABLE t_volunteer_team_member (
  id BIGINT NOT NULL AUTO_INCREMENT,
  team_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  user_name VARCHAR(50),
  role VARCHAR(20) DEFAULT 'member',
  status TINYINT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_team_user (team_id, user_id),
  KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='志愿小队成员表';

-- -----------------------------------------------------------
-- 9. 积分账户表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS t_points_account;
CREATE TABLE t_points_account (
  user_id BIGINT NOT NULL,
  balance INT DEFAULT 0,
  total_earned INT DEFAULT 0,
  total_spent INT DEFAULT 0,
  version INT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分账户表';

-- -----------------------------------------------------------
-- 10. 积分流水表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS t_points_transaction;
CREATE TABLE t_points_transaction (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  type VARCHAR(20) NOT NULL,
  amount INT NOT NULL,
  source_type VARCHAR(50),
  source_id BIGINT,
  balance_after INT,
  description VARCHAR(500),
  operator_id BIGINT,
  operator_name VARCHAR(50),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_user_id (user_id),
  KEY idx_type (type),
  KEY idx_source (source_type, source_id),
  KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分流水表';

-- -----------------------------------------------------------
-- 11. 团购商品表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS t_product;
CREATE TABLE t_product (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  description TEXT,
  cover_img VARCHAR(500),
  images TEXT,
  price DECIMAL(10,2),
  original_price DECIMAL(10,2),
  unit VARCHAR(20),
  stock INT DEFAULT 0,
  sales INT DEFAULT 0,
  points_deduction INT DEFAULT 0,
  category VARCHAR(50),
  merchant_id BIGINT,
  merchant_name VARCHAR(100),
  merchant_status TINYINT DEFAULT 0,
  status TINYINT DEFAULT 1,
  sort INT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  PRIMARY KEY (id),
  KEY idx_category (category),
  KEY idx_status (status),
  KEY idx_merchant_id (merchant_id),
  KEY idx_sort (sort)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='团购商品表';

-- -----------------------------------------------------------
-- 12. 团购订单表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS t_order;
CREATE TABLE t_order (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_no VARCHAR(50) NOT NULL,
  user_id BIGINT NOT NULL,
  user_name VARCHAR(50),
  user_phone VARCHAR(20),
  total_amount DECIMAL(10,2),
  points_deduction_amount DECIMAL(10,2) DEFAULT 0,
  actual_amount DECIMAL(10,2),
  used_points INT DEFAULT 0,
  address VARCHAR(500),
  receiver VARCHAR(50),
  receiver_phone VARCHAR(20),
  remark TEXT,
  status TINYINT DEFAULT 0,
  pay_time DATETIME,
  complete_time DATETIME,
  cancel_time DATETIME,
  cancel_reason VARCHAR(500),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_order_no (order_no),
  KEY idx_user_id (user_id),
  KEY idx_status (status),
  KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='团购订单表';

-- -----------------------------------------------------------
-- 13. 订单明细表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS t_order_item;
CREATE TABLE t_order_item (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  product_name VARCHAR(200),
  product_img VARCHAR(500),
  price DECIMAL(10,2),
  quantity INT DEFAULT 1,
  subtotal DECIMAL(10,2),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_order_id (order_id),
  KEY idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- -----------------------------------------------------------
-- 14. 积分兑换商品表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS t_exchange_goods;
CREATE TABLE t_exchange_goods (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  description TEXT,
  cover_img VARCHAR(500),
  integral_price INT NOT NULL,
  stock INT DEFAULT 0,
  exchange_count INT DEFAULT 0,
  category VARCHAR(50),
  status TINYINT DEFAULT 1,
  sort INT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  PRIMARY KEY (id),
  KEY idx_category (category),
  KEY idx_status (status),
  KEY idx_sort (sort)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分兑换商品表';

-- -----------------------------------------------------------
-- 15. 积分兑换记录表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS t_exchange_record;
CREATE TABLE t_exchange_record (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  user_name VARCHAR(50),
  goods_id BIGINT NOT NULL,
  goods_name VARCHAR(200),
  used_points INT NOT NULL,
  status TINYINT DEFAULT 0,
  receive_time DATETIME,
  receiver VARCHAR(50),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_user_id (user_id),
  KEY idx_goods_id (goods_id),
  KEY idx_status (status),
  KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分兑换记录表';


-- ============================================================
-- 初始化数据
-- ============================================================

-- -----------------------------------------------------------
-- 初始化积分账户
-- -----------------------------------------------------------
INSERT INTO t_points_account (user_id, balance, total_earned, total_spent, version) VALUES
(1, 500, 500, 0, 0),
(2, 200, 200, 0, 0),
(3, 300, 300, 0, 0);

-- -----------------------------------------------------------
-- 测试用户数据
-- -----------------------------------------------------------

-- 密码说明:
-- admin123 -> $2a$10$iz6pXsb5zrV5XWyDGzlqt.t1rvRa4uFxm1uDZ39nzKg.Uh/mjtQUC
-- 123456   -> $2a$10$d/n55DAZKAvNqBXACbsd0O2VPr1S/Pe/76iL/Xw4E7XgUWr8M35mO

INSERT INTO t_user (id, openid, username, password, phone, real_name, avatar, gender, role, status, points, volunteer_hours) VALUES
(1, 'admin_openid_001', 'admin', '$2a$10$iz6pXsb5zrV5XWyDGzlqt.t1rvRa4uFxm1uDZ39nzKg.Uh/mjtQUC', '13800000001', '系统管理员', NULL, 1, 'admin', 1, 500, 0),
(2, 'resident_openid_001', 'resident', '$2a$10$d/n55DAZKAvNqBXACbsd0O2VPr1S/Pe/76iL/Xw4E7XgUWr8M35mO', '13800000002', '张三', NULL, 1, 'resident', 1, 200, 0),
(3, 'volunteer_openid_001', 'volunteer', '$2a$10$d/n55DAZKAvNqBXACbsd0O2VPr1S/Pe/76iL/Xw4E7XgUWr8M35mO', '13800000003', '李四', NULL, 2, 'volunteer', 1, 300, 56);

-- -----------------------------------------------------------
-- 测试政策公告数据
-- -----------------------------------------------------------

INSERT INTO t_policy (id, title, content, summary, cover_img, category, category_name, top, view_count, status, publish_time, publisher_id, publisher_name) VALUES
(1,
 '关于开展2024年社区环境整治行动的通知',
 '<p>各位居民朋友们：</p><p>为深入贯彻落实习近平生态文明思想，进一步改善社区人居环境，提升居民生活品质，经社区党委研究决定，将于2024年3月起在全社区范围内开展环境整治行动。</p><p>一、整治范围：社区内所有公共区域、楼道、绿化带等。</p><p>二、整治内容：清理卫生死角、规范车辆停放、拆除违章搭建等。</p><p>三、时间安排：2024年3月1日至2024年6月30日。</p><p>请广大居民积极配合，共同营造整洁优美的社区环境。</p>',
 '为改善社区人居环境，提升居民生活品质，将于2024年3月起开展环境整治行动。',
 NULL, 1, '社区通知', 1, 128, 1, '2024-02-25 10:00:00', 1, '系统管理员'),
(2,
 '社区老年人健康体检通知',
 '<p>尊敬的社区老年居民：</p><p>为关爱社区老年人健康，社区卫生服务中心将为65周岁以上老年人提供免费健康体检服务。</p><p>一、体检对象：凡在本社区居住、年满65周岁及以上的老年居民。</p><p>二、体检时间：2024年4月15日至2024年5月15日（工作日 上午8:00-11:00）。</p><p>三、体检地点：社区卫生服务中心一楼体检科。</p><p>四、携带材料：身份证、医保卡。</p><p>请符合条件的老年居民届时前往体检。</p>',
 '社区卫生服务中心将为65周岁以上老年人提供免费健康体检服务。',
 NULL, 2, '健康资讯', 0, 85, 1, '2024-03-10 09:30:00', 1, '系统管理员'),
(3,
 '关于调整社区志愿服务积分奖励方案的公告',
 '<p>各位志愿者：</p><p>为进一步激发社区居民参与志愿服务的积极性，经社区党委研究决定，对志愿服务积分奖励方案进行调整。</p><p>一、基础积分：参加一次志愿活动积10分。</p><p>二、时长积分：每服务1小时额外积2分。</p><p>三、奖励积分：获得服务对象好评积5分，被评为优秀志愿者积50分。</p><p>四、积分用途：可在积分商城兑换生活用品，或用于社区团购抵扣。</p><p>本方案自2024年4月1日起执行。</p>',
 '调整社区志愿服务积分奖励方案，进一步激发居民参与志愿服务的积极性。',
 NULL, 3, '志愿风采', 0, 66, 1, '2024-03-20 14:00:00', 1, '系统管理员'),
(4,
 '社区消防安全知识宣传',
 '<p>各位居民：</p><p>近期天气干燥，火灾风险增大。为提高居民消防安全意识，社区将开展消防安全知识宣传活动。</p><p>1. 不在楼道内堆放杂物，保持消防通道畅通。</p><p>2. 不私拉乱接电线，不超负荷使用电器。</p><p>3. 出门前检查燃气阀门和电器开关。</p><p>4. 教育儿童不玩火，了解基本消防常识。</p><p>5. 熟悉小区消防设施位置和逃生路线。</p><p>如遇火情，请立即拨打119报警电话。</p>',
 '近期天气干燥，火灾风险增大，请广大居民提高消防安全意识。',
 NULL, 1, '社区通知', 0, 93, 1, '2024-04-01 08:00:00', 1, '系统管理员'),
(5,
 '关于举办社区邻里文化节的通知',
 '<p>亲爱的社区居民：</p><p>为增进邻里感情，丰富居民文化生活，社区将举办第三届邻里文化节。</p><p>一、活动时间：2024年5月1日至2024年5月3日。</p><p>二、活动地点：社区文化广场。</p><p>三、活动内容：文艺汇演、趣味运动会、美食分享、跳蚤市场、亲子游戏等。</p><p>四、报名方式：通过社区小程序在线报名或到社区服务中心现场报名。</p><p>欢迎广大居民踊跃参与！</p>',
 '社区将举办第三届邻里文化节，内容包含文艺汇演、趣味运动会等。',
 NULL, 4, '活动预告', 0, 210, 1, '2024-04-10 11:00:00', 1, '系统管理员');

-- -----------------------------------------------------------
-- 测试志愿活动数据
-- -----------------------------------------------------------

INSERT INTO t_activity (id, title, description, cover_img, activity_time, activity_end_time, location, max_participants, current_participants, integral_reward, status, publisher_id, publisher_name) VALUES
(1,
 '关爱孤寡老人志愿服务活动',
 '<p>活动详情：</p><p>为弘扬中华民族尊老敬老的传统美德，志愿者将前往社区孤寡老人家中，为他们提供陪伴聊天、打扫卫生、代购生活用品等服务。</p><p>集合时间：2024年4月20日上午8:30</p><p>集合地点：社区服务中心门口</p><p>服务对象：社区内5位孤寡老人</p><p>预计服务时长：3小时</p>',
 NULL, '2024-04-20 09:00:00', '2024-04-20 12:00:00', '幸福社区各孤寡老人住所', 20, 8, 30, 1, 1, '系统管理员'),
(2,
 '社区环境美化志愿行动',
 '<p>活动详情：</p><p>为建设美丽社区，志愿者将对社区公共区域进行环境美化，包括清理绿化带垃圾、擦洗公共设施、粉刷楼道墙面、美化宣传栏等。</p><p>集合时间：2024年4月27日上午8:00</p><p>集合地点：社区广场</p><p>预计服务时长：4小时</p><p>提供工具和劳保用品</p>',
 NULL, '2024-04-27 08:30:00', '2024-04-27 12:30:00', '幸福社区广场', 30, 12, 40, 1, 1, '系统管理员'),
(3,
 '爱心义诊进社区活动',
 '<p>活动详情：</p><p>联合社区卫生服务中心开展爱心义诊活动，为社区居民提供免费血压测量、血糖检测、健康咨询等服务。</p><p>活动时间：2024年5月4日上午9:00-11:30</p><p>活动地点：社区文化广场</p><p>服务内容：血压测量、血糖检测、中医把脉、健康咨询</p>',
 NULL, '2024-05-04 09:00:00', '2024-05-04 11:30:00', '幸福社区文化广场', 50, 5, 25, 1, 1, '系统管理员');

-- -----------------------------------------------------------
-- 测试团购商品数据
-- -----------------------------------------------------------

INSERT INTO t_product (id, name, description, cover_img, images, price, original_price, unit, stock, sales, points_deduction, category, merchant_id, merchant_name, merchant_status, status, sort) VALUES
(1,
 '有机新鲜蔬菜套餐',
 '精选本地农场有机蔬菜，包含西红柿、黄瓜、青菜、胡萝卜、土豆等5种新鲜蔬菜，每份约2斤，当天采摘当天配送，新鲜到家。',
 NULL, NULL, 29.90, 39.90, '份', 100, 56, 50, '生鲜蔬果', 1, '绿色农场', 1, 1, 1),
(2,
 '五常稻花香大米 5kg',
 '正宗五常稻花香大米，颗粒饱满，口感香糯，自然清香。真空包装，保质期12个月。产地直发，品质保证。',
 NULL, NULL, 49.90, 69.90, '袋', 80, 34, 80, '粮油副食', 1, '绿色农场', 1, 1, 2),
(3,
 '社区定制鲜牛奶 1L装',
 '本地牧场直供鲜牛奶，巴氏杀菌，冷链配送。每日新鲜到货，蛋白质含量≥3.3g/100ml。',
 NULL, NULL, 12.80, 16.80, '瓶', 200, 128, 20, '乳品饮料', 2, '阳光牧场', 1, 1, 3),
(4,
 '手工现做老面馒头 10个装',
 '传统老面发酵工艺，手工揉制，不加任何添加剂。松软有嚼劲，麦香浓郁。每日限量供应。',
 NULL, NULL, 15.00, 20.00, '袋', 60, 45, 30, '面点烘焙', 3, '面点工坊', 1, 1, 4),
(5,
 '本地土鸡蛋 30枚装',
 '散养土鸡蛋，蛋黄颜色深，营养丰富。每枚约50g，无抗生素，无激素，适合老人小孩食用。',
 NULL, NULL, 35.00, 45.00, '盒', 50, 22, 60, '生鲜蔬果', 1, '绿色农场', 1, 1, 5);

-- -----------------------------------------------------------
-- 测试积分兑换商品数据
-- -----------------------------------------------------------

INSERT INTO t_exchange_goods (id, name, description, cover_img, integral_price, stock, exchange_count, category, status, sort) VALUES
(1,
 '定制环保购物袋',
 '社区定制款帆布环保购物袋，厚实耐用，可重复使用。正面印有社区Logo，背面印有环保标语。',
 NULL, 100, 200, 15, '生活用品', 1, 1),
(2,
 '品牌洗衣液 2L',
 '知名品牌洗衣液，温和不伤手，去污力强，持久留香。适用于各种面料，低泡易漂洗。',
 NULL, 500, 100, 8, '生活用品', 1, 2),
(3,
 '社区定制保温杯',
 '304不锈钢内胆，食品级材质，保温时效12小时以上。杯身印有社区名称，是志愿服务的荣誉纪念。',
 NULL, 800, 50, 3, '纪念礼品', 1, 3);

-- -----------------------------------------------------------
-- 志愿者申请表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_volunteer_apply` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '申请人ID',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `credential_img` varchar(255) DEFAULT NULL COMMENT '证件照片URL',
  `reason` text COMMENT '申请理由',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '0待审核 1通过 2驳回',
  `reviewer_id` bigint DEFAULT NULL COMMENT '审核人ID',
  `review_remark` varchar(500) DEFAULT NULL COMMENT '审核备注',
  `review_time` datetime DEFAULT NULL COMMENT '审核时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='志愿者申请表';

-- -----------------------------------------------------------
-- 政策收藏表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_policy_favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `policy_id` bigint NOT NULL COMMENT '政策ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_policy` (`user_id`,`policy_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='政策收藏表';

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 初始化完成
-- ============================================================

