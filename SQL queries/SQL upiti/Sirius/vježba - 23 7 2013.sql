SET CURRENT_SCHEMA = 'SIDEV'

select * from income_item;

select distinct * from income_item;

select all * from income_item;

select * from bank_note where code like 'SEK%';

select * from bank_note_inkasso;

select * from batch_log where use_id > '2' and exec_status = 1 and EXEC_RETURN_CODE = '100';

and user_lock > '2013-07-15';