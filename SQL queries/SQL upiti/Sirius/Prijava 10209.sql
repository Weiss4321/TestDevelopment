SET CURRENT_SCHEMA = 'SIP'

select * from batch_def;


select * from batch_log order by recording_time desc;

select * from batch_def where bat_def_id = 1692075003;  -- to je obrada bl02 - Ispis rekapitulacije učitavanja datoteke priljeva

select * from customer_account;