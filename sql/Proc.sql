# 定义存储过程
DELIMITER $$
CREATE PROCEDURE create_table_batch()
BEGIN
	# 定义循环次数
	SET @i = 1;
	SET @tablename = '';
	SET @sql = '';
	WHILE @i <= 31 DO
		SET @tablename = CONCAT('table_', CAST(@i AS CHAR));
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = @tablename) THEN
			SET @sql = CONCAT('CREATE TABLE ', @tablename,'(
                id int,
				name VARCHAR(20),
                create_time datetime
			)');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END IF;
		SET @i = @i + 1;
END WHILE;
END$$
DELIMITER;

call create_table_batch();