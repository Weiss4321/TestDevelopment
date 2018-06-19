SET CURRENT_SCHEMA = 'SIP'


select * from batch_log;


-- obrade su se pokrenule 2 puta zbog prelaska sa ljetnog na zimsko raèunanje vremena
select * from batch_log where bat_def_id IN (6253448704,6259167704, 6266762704) and recording_time between '27.10.2013' and '28.10.2013';

---- koliko je ukupno obrada puštano u ovom preiodu
select * from batch_log where  recording_time between '2013-10-27 02:00:00.000000' and '2013-10-27 02:59:59.000000' order by bat_def_id;
--- 33 zapis u ovom periodu

-------- dan prije
select * from batch_log where  recording_time between '2013-10-26 02:00:00.000000' and '2013-10-26 02:59:59.000000' order by bat_def_id;
---  19



 		