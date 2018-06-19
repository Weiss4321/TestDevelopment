

SET CURRENT_SCHEMA = 'SITP'



select * from income_item;

select * from income_head  where inc_hea_no = 'P130000000110';



select * from income_item where inc_hea_id = '297103119';  

select * from income_type;

select * FROM income_subtype;

select * from income_head  where inc_typ_id = '1008282421' ;

select * from income_head  where inc_typ_id = '1008282421' and ih.pay_iban_or_acc_no =  'HR4524840081100004565';