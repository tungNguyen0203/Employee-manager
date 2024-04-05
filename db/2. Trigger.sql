-- =============================================
-- a.	Khi insert / update / delete account thì sẽ tự động update size của department
-- =============================================
-- insert account
DROP TRIGGER IF EXISTS trigger_insert_account;
DELIMITER $$
	CREATE TRIGGER trigger_insert_account
    AFTER INSERT ON `account`
    FOR EACH ROW
    BEGIN
		DECLARE old_member_size INT;
		
        IF NEW.department_id IS NOT NULL THEN
			--  tang so luong member_size cua department
			SELECT 	member_size INTO old_member_size
			FROM 	department
			WHERE 	id = NEW.department_id;
		
			UPDATE 	department
			SET 	member_size = (old_member_size + 1)
			WHERE 	id = NEW.department_id;
		END IF;
    END $$
DELIMITER ;

-- update account
DROP TRIGGER IF EXISTS trigger_update_account;
DELIMITER $$
	CREATE TRIGGER trigger_update_account
	AFTER UPDATE ON `account`
	FOR EACH ROW
		BEGIN
			DECLARE old_member_size INT;
            -- update so luong member_size cua department
			SELECT 	member_size INTO old_member_size
			FROM 	department
			WHERE 	id = NEW.department_id;
                
			UPDATE 	department
			SET 	member_size = (old_member_size + 1)
			WHERE 	id = NEW.department_id;
			
	END $$
DELIMITER ;

-- delete account
DROP TRIGGER IF EXISTS trigger_delete_account;
DELIMITER $$
	CREATE TRIGGER trigger_delete_account
	AFTER DELETE ON `account`
	FOR EACH ROW
		BEGIN
				DECLARE old_member_size INT;
				
                -- giam so luong member_size cua department
				SELECT 	member_size INTO old_member_size
				FROM 	department
				WHERE 	id = OLD.department_id;
		
				UPDATE 	department
				SET 	member_size = (old_member_size - 1)
				WHERE 	id = OLD.department_id;
		END $$
DELIMITER ;

-- =============================================
-- b.	Khi insert / update / delete account thì sẽ tự động update size của group
-- =============================================
-- insert group_account
DROP TRIGGER IF EXISTS trigger_insert_group_account;
DELIMITER $$
	CREATE TRIGGER trigger_insert_group_account
    AFTER INSERT ON group_account
    FOR EACH ROW
    BEGIN
		DECLARE old_member_size SMALLINT;
		
        -- tang so luong member_size cua group
        SELECT 	member_size INTO old_member_size
        FROM 	`group`
        WHERE 	id = NEW.group_id;
    
		UPDATE 	`group`
        SET 	member_size = (old_member_size + 1)
        WHERE 	id = NEW.group_id;
    
    END $$
DELIMITER ;

-- delete group_account
DROP TRIGGER IF EXISTS trigger_delete_group_account;
DELIMITER $$
	CREATE TRIGGER trigger_delete_group_account
	AFTER DELETE ON group_account
	FOR EACH ROW
		BEGIN
				DECLARE old_member_size SMALLINT;
                
				-- giam so luong member_size cua group
				SELECT 	member_size INTO old_member_size
				FROM 	`group`
				WHERE 	id = OLD.group_id;
		
				UPDATE 	`group`
				SET 	member_size = (old_member_size - 1)
				WHERE 	id = OLD.group_id;
		END $$
DELIMITER ;