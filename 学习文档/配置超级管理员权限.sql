

 SELECT id INTO @user_id FROM sys_user  WHERE mobile='' AND is_deleted =0 AND platform_type =2;//填电话号码
 SELECT id INTO @role_id FROM sys_role WHERE NAME ="超级管理员" AND is_deleted =0;
 INSERT INTO `sys_user_role` ( `user_id`, `role_id`, `creator`, `gmt_create`, `modifier`, `gmt_modified`, `is_deleted`, `sys_code`)
 VALUES(@user_id,@role_id,'chenm',NOW(),'chenm',NOW(),'0', CONCAT(left(date_format(NOW(3),'%Y%m%d%H%i%S%f'),17),'_',FLOOR( 100000 + RAND() * (999999 - 100000)) ) ,(CEIL(RAND()*900000)+99999))
 );

 
 
 或者
 
  INSERT INTO `sys_user_role` ( `user_id`, `role_id`, `creator`, `gmt_create`, `modifier`, `gmt_modified`, `is_deleted`, `sys_code`)
SELECT 
(SELECT id FROM sys_user u   WHERE mobile='18689495704' AND is_deleted =0 AND platform_type =2),
(SELECT id FROM sys_role r   WHERE NAME ="超级管理员" AND is_deleted =0),
'chenm',
NOW(),
'chenm',
NOW(),
'0',
 CONCAT(left(date_format(NOW(3),'%Y%m%d%H%i%S%f'),17),'_',FLOOR( 100000 + RAND() * (999999 - 100000)) ) 
 FROM DUAL 
 
 
