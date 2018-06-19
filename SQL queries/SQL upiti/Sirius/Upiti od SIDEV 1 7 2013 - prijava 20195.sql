SET CURRENT_SCHEMA = SIDEV


select * from event where eve_id = 1124976519303;

Select * from event_type where eve_typ_id = 333;   -- to oznaèava gotovinsku uplatu

Select * from event_type;


select * from batch_log  with ur

select * from batch_log  where EXEC_START_TIME = '11-12-2009 13:09:39 350000' with ur

select * from batch_log  where EXEC_START_TIME between  '2009-12-11 13:09:39.350000' and '2009-12-11 13:09:39.360000' with ur

select * from batch_log  where EXEC_START_TIME between  '2009-12-16 11:16:13.693' and '2009-12-16 11:16:13.694' with ur

select * from batch_file_log where bat_log_id = 6444081704 with ur;

select * from batch_file_log ;

--  dohvatiti sve swift kodove austrijskih banaka

select * from bank;

select * from customer;


select SWIFT_ADDRESS from bank where cou_id = 76999;

select * from country;  --cou_id 76999


select * from batch_log  with ur;

select * from BATCH_DEF  where BAT_DEF_NAME like 'bf%'  with ur;


select * from event;


select * from customer_account;


