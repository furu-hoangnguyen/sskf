use ssk_accounts_receivable;

DROP EVENT IF EXISTS reset_settlement_number;
CREATE EVENT reset_settlement_number
ON SCHEDULE EVERY 1 DAY
    STARTS (TIMESTAMP(CURRENT_DATE) + INTERVAL 1 DAY)
DO
	UPDATE mst_request_types SET number = 1;