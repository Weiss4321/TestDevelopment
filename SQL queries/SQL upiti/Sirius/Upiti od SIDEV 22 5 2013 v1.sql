SET CURRENT_SCHEMA = SIDEV




SELECT * FROM  batch_log WHERE exec_status  = 'P';

SELECT USE_ID FROM  app_user WHERE user_name  = 'Vinko Æelepiroviæ';

SELECT * FROM  app_user WHERE user_name  = 'Vinko Æelepiroviæ';

SELECT EXEC_STATUS FROM batch_log WHERE USE_ID = 46003;  -- dobijam samo user_name  tu radim

SELECT * FROM  app_user WHERE use_id;

SELECT * FROM  batch_log;

SELECT * FROM  app_user;

SELECT EXEC_STATUS,EXEC_STAT_EXPL FROM batch_log WHERE USE_ID = 46003;

----------------------------------------------------------------------------------
                                  SELECT
							log.bat_log_id,
							log.not_before_time,
							log.exec_start_time,
							log.param_value,
							RTRIM(use.user_name) user_name ,
							log.exec_status,
							log.exec_return_code,
						FROM
							batch_log log , app_user use
						WHERE log.bat_def_id = 1754861003
						AND	log.bank_sign = :(tc.getBankSign())
						AND	use.use_id = log.use_id
						AND	('2007-06-01' = 0 OR log.not_before_time >= :'2007-01-01')
						AND	('2007-06-01' = 0 OR log.not_before_time <= :'2007-01-01')
						AND	('VU00222' = 0 OR use.login = :(BatLogBG20Q_txtAppUserLogin))                 
						AND	log.exec_status LIKE :(BatLogBG20Q_txtExecStatus)                              
						AND	(100 = 0 OR log.exec_return_code LIKE :(fldExecReturnCode))            
						ORDER BY
							log.not_before_time		DESC , log.exec_start_time	DESC;

							  ------ login je VU00222
							    ------ exec status je 1, S, D
							-------- exec_return_code je 100,104,108 it itd


SELECT login FROM  app_user WHERE USE_ID = 46003;

SELECT exec_status FROM  batch_log WHERE USE_ID = 46003;

SELECT exec_return_code FROM  batch_log WHERE USE_ID = 46003;

SELECT * FROM batch_log;



SELECT log.bat_log_id, log.not_before_time, log.exec_start_time, log.param_value, RTRIM(use.user_name) user_name, log.exec_status, log.exec_return_code
FROM batch_log log, app_user use
WHERE log.bat_def_id = 1585138003
	AND log.bank_sign = 'RB'
	AND use.use_id = log.use_id
	AND log.param_value LIKE 'RB#RET%'
ORDER BY log.not_before_time DESC, log.exec_start_time	DESC	


SELECT log.bat_log_id, log.not_before_time, log.exec_start_time, log.param_value, RTRIM(use.user_name) user_name, log.exec_status, log.exec_return_code
FROM batch_log log, app_user use
WHERE log.bat_def_id = 1585138003
	AND log.bank_sign = 'RB'
     AND use.use_id = log.use_id
     AND log.param_value LIKE 'RB#RET%'
ORDER BY log.not_before_time DESC, log.exec_start_time DESC;

update BATCH_LOG set exec_status = 'D' where bat_log_id = 1754861003 and exec_status = '1';    --  setiranje nove vrijednosti

update BATCH_LOG set exec_status = '1' where bat_log_id = 1754861003 and exec_status = 'D';    --  setiranje nove vrijednosti

commit;   -- za aktiviranje novog statusa u bazi



