SET CURRENT_SCHEMA = SIP


select * from batch_log where bat_def_id = 1569874003 and exec_start_time > '2013-08-22 12:43:04.640504'  with ur;   --- RC 100



select * from batch_def where bat_def_name like '%bf55%'  -- Generiranje CSV datoteke iz rokovnika

select * from batch_log where bat_def_id = 1569874003 and exec_return_code = 112 with ur;

select * from batch_log where bat_def_id = 1569874003 and exec_return_code = 100 AND recording_time > '2013-08-26 00:00:00.000000' with ur;


select * from batch_log where bat_def_id = 1569874003 AND PARAM_VALUE = 'RB#PAY;Cp1250;1;X;X;X;X;X;01.07.2013;19.08.2013;X;X;X;X;X';-- za poèetak je status obrade 0 - pozadinska obrada rasporeðena  -- i poslije je bilo ok

select * from batch_log where bat_def_id = 1569874003 AND PARAM_VALUE = 'RB#PAY;Cp1250;1;X;X;X;X;X;X;X;X;X;01.07.2013;19.08.2013;X';  -- zadana obrada sa ok paramterima kao iz prijave 

select * from batch_log where bat_def_id = 1569874003 AND BAT_LOG_ID = 1145866910303 with ur;  