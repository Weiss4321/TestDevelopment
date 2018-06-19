


SET CURRENT_SCHEMA = 'SIDEV'


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
						WHERE ih.bank_sign = 'RB' fetch first 500 rows only with ur;
																				


--- ORDER BY ih.inc_hea_no DESC;

select * from income_head;

select * from income_head  where inctypid = 56322019;

select * from income_type;



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
AND ih.inc_typ_id = 04
ORDER BY ih.inc_hea_no DESC
;


select payor_code, payor_register_no from income_head;



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
ORDER BY ih.inc_hea_no DESC;

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
ORDER BY ih.inc_hea_no DESC;



