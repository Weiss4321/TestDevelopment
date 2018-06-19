SET CURRENT_SCHEMA = SIDEV;







----- update podataka za testiranje Upita po uzorku na CL011  --- ovo dolje radi kako treba

select * from income_head;

select * from income_head where inc_hea_no = 'P130000000072';  -- 

UPDATE income_head set payor_tax_num = '4444444' where inc_hea_no = 'P130000000072';

commit;


SELECT
ih.inc_hea_id IncHeaID ,
ih.inc_typ_id IncTypID,
ih.total_cur_id,
it.inc_typ_code,
it.inc_typ_desc,
ih.inc_hea_no,
ih.file_date,
ih.total_item_count,
ih.total_amount,
cur.code_char,
ih.package_status,
ih.pckg_fin_status,
ih.error_code,
ih.error_desc,
ih.value_date,
CURRENT TIMESTAMP userLock,
ih.payor_register_no,
cur.code_num,
ih.payor_code,
ih.payor_name,
ih.posting_time,
ou.code ,
ih.closing_date ,
ih.payor_bank_cou_id,
ih.PAYOR_TAX_NUM,
ih.INC_FILE_SOURCE,
ih.EXEC_TYPE,
ih.PAYOR_IBAN,
ih.MIDPAYOR_TAX_NUM
FROM INCOME_HEAD ih LEFT OUTER
JOIN INCOME_TYPE it ON ih.inc_typ_id = it.inc_typ_id LEFT OUTER
JOIN CURRENCY cur ON cur.cur_id = ih.total_cur_id LEFT OUTER
JOIN ORGANIZATION_UNIT ou ON ou.org_uni_id = ih.org_uni_id
WHERE ih.bank_sign = 'RB'
AND it.inc_typ_id IN
(
   120675019,
   2999,
   3999,
   1999,
   53998019,
   56322019,
   69845019,
   120670019,
   120671019,
   120672019,
   120673019,
   120674019,
   153467019,
   1008282421
)
AND ih.package_status IN ('0','0','0','0','0')
ORDER BY ih.inc_hea_no DESC

SELECT
ih.inc_hea_id IncHeaID ,
ih.inc_typ_id IncTypID,
ih.total_cur_id,
it.inc_typ_code,
it.inc_typ_desc,
ih.inc_hea_no,
ih.file_date,
ih.total_item_count,
ih.total_amount,
cur.code_char,
ih.package_status,
ih.pckg_fin_status,
ih.error_code,
ih.error_desc,
ih.value_date,
CURRENT TIMESTAMP userLock,
ih.payor_register_no,
cur.code_num,
ih.payor_code,
ih.payor_name,
ih.posting_time,
ou.code ,
ih.closing_date ,
ih.payor_bank_cou_id,
ih.PAYOR_TAX_NUM,
ih.INC_FILE_SOURCE,
ih.EXEC_TYPE,
ih.PAYOR_IBAN,
ih.MIDPAYOR_TAX_NUM
FROM INCOME_HEAD ih LEFT OUTER
JOIN INCOME_TYPE it ON ih.inc_typ_id = it.inc_typ_id LEFT OUTER
JOIN CURRENCY cur ON cur.cur_id = ih.total_cur_id LEFT OUTER
JOIN ORGANIZATION_UNIT ou ON ou.org_uni_id = ih.org_uni_id
WHERE ih.bank_sign = 'RB'
AND it.inc_typ_id IN
(
   120675019,
   2999,
   3999,
   1999,
   53998019,
   56322019,
   69845019,
   120670019,
   120671019,
   120672019,
   120673019,
   120674019,
   153467019,
   1008282421
)

AND ih.midpayor_tax_num = '03790746'
ORDER BY ih.inc_hea_no DESC;


select * from income_head  where inc_hea_no = 'P130000000102';

select * from income_head where midpayor_tax_num like '%03790746%';  -- to je payor code 

select * from income_head where payor_code like '%03790746%';  -- to je payor code - OIB uplatitelja

select * from income_head  where inc_hea_no = 'P130000000072';


SELECT
ih.inc_hea_id IncHeaID ,
ih.inc_typ_id IncTypID,
ih.total_cur_id,
it.inc_typ_code,
it.inc_typ_desc,
ih.inc_hea_no,
ih.file_date,
ih.total_item_count,
ih.total_amount,
cur.code_char,
ih.package_status,
ih.pckg_fin_status,
ih.error_code,
ih.error_desc,
ih.value_date,
CURRENT TIMESTAMP userLock,
ih.payor_register_no,
cur.code_num,
ih.payor_code,
ih.payor_name,
ih.posting_time,
ou.code ,
ih.closing_date ,
ih.payor_bank_cou_id,
ih.PAYOR_TAX_NUM,
ih.INC_FILE_SOURCE,
ih.EXEC_TYPE,
ih.PAYOR_IBAN,
ih.MIDPAYOR_TAX_NUM
FROM INCOME_HEAD ih LEFT OUTER
JOIN INCOME_TYPE it ON ih.inc_typ_id = it.inc_typ_id LEFT OUTER
JOIN CURRENCY cur ON cur.cur_id = ih.total_cur_id LEFT OUTER
JOIN ORGANIZATION_UNIT ou ON ou.org_uni_id = ih.org_uni_id
WHERE ih.bank_sign = 'RB'
AND it.inc_typ_id IN
(
   120675019,
   2999,
   3999,
   1999,
   53998019,
   56322019,
   69845019,
   120670019,
   120671019,
   120672019,
   120673019,
   120674019,
   153467019,
   1008282421
)
AND ih.package_status IN
(
   '0','0','0','0','null'
)
AND ih.midpayor_tax_num LIKE '%7777777777'
ORDER BY ih.inc_hea_no DESC


select * from income_head;

select * FROM income_item;   -- inc_sub_type_code




