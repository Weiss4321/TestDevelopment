SET CURRENT_SCHEMA = 'SIP'


select * from counter_list;

 select * from counter_list where cus_acc_no = 3208260147; 

 select * from counter_list where cus_acc_no = 3200125745;

 select * from counter_list where cus_acc_no = 3208260147 and process_date between '01.08.2013' and '01.10.2013'; 

 select * from counter_list where cus_acc_no = 3200125745 and process_date between '01.08.2013' and '01.10.2013';  -- od 7.9.2013 nema podataka u counter_listi