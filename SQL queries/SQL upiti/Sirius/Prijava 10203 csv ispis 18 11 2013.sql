SET CURRENT_SCHEMA = 'SIDEV'

select * from batch_log order by exec_start_time asc;

select * from income_file where file_name like  'UN_TEST_BROJ_JamnicaABC%' ;

select * from income_file where inc_fil_id = 1015719421;

select * from batch_def where bat_def_name like 'bl02%';  -- BAR_DEF_ID 1,692,075,003  

select * from income_type;

