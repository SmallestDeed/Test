 

-- 订单表
CREATE TABLE sales_order (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  order_no varchar(50) NOT NULL COMMENT '订单号',
  platform_type int(1) NOT NULL COMMENT '平台类型:1:选装网,2:企业小程序',
  ref_model_id bigint not null comment '选装网的店铺id,或企业小程序的企业id',
  buyer_id bigint(20) default '0' comment '客户信息_客户ID',
  buyer_nick_name varchar(128) not null default "" comment '客户信息_客户名称',
  seller_id bigint(20) default '0' comment '卖家UIN',
  seller_nick_name varchar(128) not null default "" comment '卖家信息_卖家昵称',
  status int(2) DEFAULT NULL COMMENT '订单正向状态',
  reverse_status  int(2) DEFAULT NULL COMMENT '订单逆向状态',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表';

-- 订单商品明细表
CREATE TABLE sales_order_item (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  order_id bigint(20) not NULL COMMENT '订单id',
  item_id varchar(512) DEFAULT NULL COMMENT '商品id',
  item_sku varchar(255) DEFAULT NULL COMMENT '商品Sku',
  item_name varchar(255) DEFAULT NULL COMMENT '商品名称',
  item_description text COMMENT '商品描述',
  item_original_price decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '原价',
  item_discount_amount decimal(12,4) DEFAULT '0.0000' COMMENT '折扣价',
  item_price decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '成交价',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单商品明细表';


-- 订单地址表
CREATE TABLE sales_order_address (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  order_id bigint(20) not NULL COMMENT '订单id',
  country int(6) not NULL COMMENT '国家',
  province int(6) not NULL COMMENT '省份',
  city int(6) not NULL COMMENT '城市',
  area int(6) not NULL COMMENT '区域',
  address varchar(512) not NULL COMMENT '详细地址',
  zipcode varchar(32) DEFAULT NULL COMMENT '邮编',
  consignee varchar(64) not NULL COMMENT '收件人',
  mobile varchar(32) not NULL COMMENT '联系电话',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单地址表';

-- 订单日志表
CREATE TABLE sales_order_log (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  order_id bigint(20) not NULL COMMENT '订单id',
  content text DEFAULT NULL COMMENT '内容',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单日志表';



-- 付款单表
CREATE TABLE pay_payment (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  payment_no varchar(50) NOT NULL COMMENT '付款单号',
  platform_type int(1) NOT NULL COMMENT '平台类型:1:选装网,2:企业小程序',
  ref_model_id bigint not null comment '选装网的店铺id,或企业小程序的企业id',
  order_id bigint(20) not NULL COMMENT '订单id',
  order_no varchar(50) NOT NULL COMMENT '订单号',
  platform_type int(1) NOT NULL COMMENT '平台类型:1:选装网,2:企业小程序',
  ref_model_id bigint not null comment '选装网的店铺id,或企业小程序的企业id',
  buyer_id bigint(20) default '0' comment '客户信息_客户ID',
  buyer_nick_name varchar(128) not null default "" comment '客户信息_客户名称',
  seller_id bigint(20) default '0' comment '卖家UIN',
  seller_nick_name varchar(128) not null default "" comment '卖家信息_卖家昵称',
  amount_paid decimal(12,4) DEFAULT NULL COMMENT '支付金额',
  pay_time timestamp not NULL COMMENT '支付时间',
  status int(2) DEFAULT NULL COMMENT '支付状态',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='付款单表';

-- 付款商品明细表
CREATE TABLE pay_payment_item (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  payment_id bigint(20) not NULL COMMENT '付款单id',
  item_id varchar(512) DEFAULT NULL COMMENT '商品id',
  item_sku varchar(255) DEFAULT NULL COMMENT '商品Sku',
  item_name varchar(255) DEFAULT NULL COMMENT '商品名称',
  item_description text COMMENT '商品描述',
  item_original_price decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '原价',
  item_discount_amount decimal(12,4) DEFAULT '0.0000' COMMENT '折扣价',
  item_price decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '成交价',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='付款商品明细表';


-- 支付款项明细表
CREATE TABLE pay_payment_details (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  payment_id bigint(20) not NULL COMMENT '付款单id',
  pay_method varchar(128) DEFAULT NULL COMMENT '支付方式',
  amount_paid decimal(12,4) DEFAULT NULL COMMENT '支付金额',
  pay_time timestamp NOT NULL COMMENT '支付时间',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付款项明细表';


-- 支付日志表
CREATE TABLE pay_payment_log (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  payment_id bigint(20) not NULL COMMENT '付款单id',
  content text DEFAULT NULL COMMENT '内容',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付日志表';


-- 发货单
CREATE TABLE sales_shipment (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  shipment_no varchar(50) NOT NULL COMMENT '发货单号',
  platform_type int(1) NOT NULL COMMENT '平台类型:1:选装网,2:企业小程序',
  ref_model_id bigint not null comment '选装网的店铺id,或企业小程序的企业id',
  order_id bigint(20) not NULL COMMENT '订单id',
  order_no varchar(50) NOT NULL COMMENT '订单号',
  platform_type int(1) NOT NULL COMMENT '平台类型:1:选装网,2:企业小程序',
  ref_model_id bigint not null comment '选装网的店铺id,或企业小程序的企业id',
  buyer_id bigint(20) default '0' comment '客户信息_客户ID',
  buyer_nick_name varchar(128) not null default "" comment '客户信息_客户名称',
  seller_id bigint(20) default '0' comment '卖家UIN',
  seller_nick_name varchar(128) not null default "" comment '卖家信息_卖家昵称',
  total_weight decimal(12,4) DEFAULT NULL COMMENT '总重量',
  total_qty decimal(12,4) DEFAULT NULL COMMENT '总数量',
  shipping_amount decimal(12,4) DEFAULT NULL COMMENT '运费',
  status int(2) DEFAULT NULL COMMENT '发货状态',
  remark text COMMENT '备注',
  carrier_code varchar(32) DEFAULT NULL COMMENT '物流单号',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发货单表';

-- 发货单商品明细表
CREATE TABLE sales_shipment_item (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  shipment_id bigint(20) not NULL COMMENT '发货单id',
  item_id varchar(512) DEFAULT NULL COMMENT '商品id',
  item_sku varchar(255) DEFAULT NULL COMMENT '商品Sku',
  item_name varchar(255) DEFAULT NULL COMMENT '商品名称',
  item_description text COMMENT '商品描述',
  item_original_price decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '原价',
  item_discount_amount decimal(12,4) DEFAULT '0.0000' COMMENT '折扣价',
  item_price decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '成交价',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发货单商品明细表';


-- 发货地址表
CREATE TABLE sales_shipment_address (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  shipment_id bigint(20) not NULL COMMENT '发货单id',
  country int(6) not NULL COMMENT '国家',
  province int(6) not NULL COMMENT '省份',
  city int(6) not NULL COMMENT '城市',
  area int(6) not NULL COMMENT '区域',
  address varchar(512) not NULL COMMENT '详细地址',
  zipcode varchar(32) DEFAULT NULL COMMENT '邮编',
  recv_name varchar(64) not NULL COMMENT '收件人',
  mobile varchar(32) not NULL COMMENT '联系电话',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发货地址表';

-- 发货单日志表
CREATE TABLE sales_shipment_log (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  shipment_id bigint(20) not NULL COMMENT '付款单id',
  content text DEFAULT NULL COMMENT '内容',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发货单日志表';
  
 


-- 退货退款申请表
CREATE TABLE sales_return_apply (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  return_apply_no varchar(50) NOT NULL COMMENT '申请单号',
  platform_type int(1) NOT NULL COMMENT '平台类型:1:选装网,2:企业小程序',
  ref_model_id bigint not null comment '选装网的店铺id,或企业小程序的企业id',
  order_id bigint(20) not NULL COMMENT '订单id',
  order_no varchar(50) NOT NULL COMMENT '订单号',
  platform_type int(1) NOT NULL COMMENT '平台类型:1:选装网,2:企业小程序',
  ref_model_id bigint not null comment '选装网的店铺id,或企业小程序的企业id',
  buyer_id bigint(20) default '0' comment '客户信息_客户ID',
  buyer_nick_name varchar(128) not null default "" comment '客户信息_客户名称',
  seller_id bigint(20) default '0' comment '卖家UIN',
  seller_nick_name varchar(128) not null default "" comment '卖家信息_卖家昵称',
  apply_type int(2) DEFAULT NULL COMMENT '申请类型:1:退货,2:退款',
  status int(2) DEFAULT NULL COMMENT '申请状态',
  apply_time timestamp NOT NULL COMMENT '申请时间',
  reason text COMMENT '申请退货或退款原因',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退货退款申请表';

--  退货退款申请图片资源
CREATE TABLE sales_return_apply_pic(
  id bigint(20) NOT NULL AUTO_INCREMENT,
  return_apply_id bigint(20) not NULL COMMENT '申请单id',
  pic_code varchar(96) DEFAULT NULL COMMENT '图片编码',
  pic_name varchar(96) DEFAULT NULL COMMENT '图片名称',
  pic_file_name varchar(96) DEFAULT NULL COMMENT '图片文件名称',
  pic_type varchar(32) DEFAULT NULL COMMENT '图片类型',
  pic_size int(11) DEFAULT NULL COMMENT '图片大小',
  pic_weight varchar(16) DEFAULT NULL COMMENT '图片长',
  pic_high varchar(16) DEFAULT NULL COMMENT '图片高',
  pic_suffix varchar(16) DEFAULT NULL COMMENT '图片后缀',
  pic_level varchar(16) DEFAULT NULL COMMENT '图片级别',
  pic_format varchar(32) DEFAULT NULL COMMENT '图片格式',
  pic_path varchar(512) NOT NULL COMMENT '图片路径',
  pic_desc varchar(512) DEFAULT NULL COMMENT '图片描述',
  sys_code varchar(120) DEFAULT NULL,
  creator varchar(32) DEFAULT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modifier varchar(32) DEFAULT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退货退款申请图片资源';


-- 申请退回商品明细
CREATE TABLE sales_return_apply_item (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  return_apply_id bigint(20) not NULL COMMENT '申请单id',
  item_id varchar(512) DEFAULT NULL COMMENT '商品id',
  item_sku varchar(255) DEFAULT NULL COMMENT '商品Sku',
  item_name varchar(255) DEFAULT NULL COMMENT '商品名称',
  item_description text COMMENT '商品描述',
  item_original_price decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '原价',
  item_discount_amount decimal(12,4) DEFAULT '0.0000' COMMENT '折扣价',
  item_price decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '成交价',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='申请退回商品明细';


-- 申请单日志表
CREATE TABLE sales_return_apply_log (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  return_apply_id bigint(20) not NULL COMMENT '申请单id',
  content text DEFAULT NULL COMMENT '内容',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='申请单日志表';



-- 退货单 
CREATE TABLE sales_return_order (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  return_apply_id bigint(20) not NULL COMMENT '申请单id',
  order_id bigint(20) not NULL COMMENT '订单id',
  order_no varchar(50) NOT NULL COMMENT '订单号',
  platform_type int(1) NOT NULL COMMENT '平台类型:1:选装网,2:企业小程序',
  ref_model_id bigint not null comment '选装网的店铺id,或企业小程序的企业id',
  buyer_id bigint(20) default '0' comment '客户信息_客户ID',
  buyer_nick_name varchar(128) not null default "" comment '客户信息_客户名称',
  seller_id bigint(20) default '0' comment '卖家UIN',
  seller_nick_name varchar(128) not null default "" comment '卖家信息_卖家昵称',
  status int(2) DEFAULT NULL COMMENT '退货单状态',
  refund_amount decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '退款金额',
  remark text COMMENT '备注',
  reason text COMMENT '退货原因',
  carrier_code varchar(32) DEFAULT NULL COMMENT '物流单号',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退货退款申请表';

-- 退货商品表
CREATE TABLE sales_return_order_item (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  return_order_id bigint(20) not NULL COMMENT '退货单id',
  item_id varchar(512) DEFAULT NULL COMMENT '商品id',
  item_sku varchar(255) DEFAULT NULL COMMENT '商品Sku',
  item_name varchar(255) DEFAULT NULL COMMENT '商品名称',
  item_description text COMMENT '商品描述',
  item_original_price decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '原价',
  item_discount_amount decimal(12,4) DEFAULT '0.0000' COMMENT '折扣价',
  item_price decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '成交价',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退货商品表';

-- 退货地址表
CREATE TABLE sales_return_order_address (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  return_order_id bigint(20) NOT NULL COMMENT '退货单id',
  country int(6) NOT NULL COMMENT '国家',
  province int(6) NOT NULL COMMENT '省份',
  city int(6) NOT NULL COMMENT '城市',
  area int(6) NOT NULL COMMENT '区域',
  address varchar(512) NOT NULL COMMENT '详细地址',
  zipcode varchar(32) DEFAULT NULL COMMENT '邮编',
  recv_name varchar(64) NOT NULL COMMENT '收件人',
  mobile varchar(32) NOT NULL COMMENT '联系电话',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退货地址表';


-- 退货单日志表
CREATE TABLE sales_return_order_log (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  return_order_id bigint(20) not NULL COMMENT '退货单id',
  content text DEFAULT NULL COMMENT '内容',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退货单日志表';

-- 退款单
CREATE TABLE pay_refundment (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  refundment_no varchar(50) NOT NULL COMMENT '退款单号',
  platform_type int(1) NOT NULL COMMENT '平台类型:1:选装网,2:企业小程序',
  ref_model_id bigint not null comment '选装网的店铺id,或企业小程序的企业id',
  return_apply_id bigint(20) not NULL COMMENT '申请单id',
  return_order_id bigint(20) not NULL COMMENT '退货单id',
  order_id bigint(20) not NULL COMMENT '订单id',
  order_no varchar(50) NOT NULL COMMENT '订单号',
  buyer_id bigint(20) default '0' comment '客户信息_客户ID',
  buyer_nick_name varchar(128) not null default "" comment '客户信息_客户名称',
  seller_id bigint(20) default '0' comment '卖家UIN',
  seller_nick_name varchar(128) not null default "" comment '卖家信息_卖家昵称',
  amount_paid decimal(12,4) DEFAULT NULL COMMENT '支付金额',
  pay_time timestamp not NULL COMMENT '支付时间',
  status int(2) DEFAULT NULL COMMENT '退款单状态',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退款单表';

-- 退款商品表
CREATE TABLE pay_refundment_item (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  refundment_id bigint(20) not NULL COMMENT '退款单id',
  item_id varchar(512) DEFAULT NULL COMMENT '商品id',
  item_sku varchar(255) DEFAULT NULL COMMENT '商品Sku',
  item_name varchar(255) DEFAULT NULL COMMENT '商品名称',
  item_description text COMMENT '商品描述',
  item_original_price decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '原价',
  item_discount_amount decimal(12,4) DEFAULT '0.0000' COMMENT '折扣价',
  item_price decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '成交价',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退款商品表';

-- 退回款项明细表
CREATE TABLE pay_refundment_details (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  refundment_id bigint(20) not NULL COMMENT '付款单id',
  pay_method varchar(128) DEFAULT NULL COMMENT '支付方式',
  amount_paid decimal(12,4) DEFAULT NULL COMMENT '支付金额',
  pay_time timestamp NOT NULL COMMENT '支付时间',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退回款项明细表';


-- 退款单日志表
CREATE TABLE pay_refundment_log (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  refundment_id bigint(20) not NULL COMMENT '退款单id',
  content text DEFAULT NULL COMMENT '内容',
  request text DEFAULT NULL COMMENT '请求报文',
  response text DEFAULT NULL COMMENT '响应报文',
  sys_code varchar(512) DEFAULT NULL COMMENT '系统编码',
  creator varchar(512) NOT NULL COMMENT '创建者',
  gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier varchar(512) NOT NULL COMMENT '修改人',
  gmt_modified timestamp NOT NULL COMMENT '修改时间',
  is_deleted int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退款单日志表';









