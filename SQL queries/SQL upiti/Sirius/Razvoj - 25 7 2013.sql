SET CURRENT_SCHEMA = 'SIDEV'




SELECT 
 				ca.cus_acc_id,
                    ca.cus_acc_no,
                    ca.old_cus_acc_no,
                    ca.iban_acc_no,
                    ca.cus_acc_name,
                    ca.org_uni_id,
                    ca.currency_type,
                    ca.acc_cur_id,
                    ca.opening_date,
                    ca.closing_date,
                    ca.cus_acc_status,
                    ca.chg_stat_reason,
                    ca.chg_stat_date,
                    ca.warn_note_status,
                    ca.law_block_status,
                    ca.collection_status,
                    ca.court_proc_status,
                    ca.distraint_flag,
                    ca.ban_rel_typ_id,
                    ca.ban_pro_typ_id,
                    ca.pur_sub_id,
                    ca.pro_cat_id,
                    ci1.cus_id,
                    ci1.code cus_code,
                    ci1.register_no,
                    ci1.name,
                    ci1.surname,
                    cl.nnb_no_days,
                    cl.nnb_date,
                    cl.absorbing_status,
                    cl.abs_status_date,
                    cl.process_date cl_process_date
FROM
	citizen ci1,
	customer_account ca
		LEFT OUTER JOIN counter_list cl ON cl.cus_acc_id = ca.cus_acc_id AND cl.pro_met_code = 'HNB' AND cl.process_date = '2013-08-01'
WHERE 
	ca.cus_id = ci1.cus_id
	AND ci1.pay_list_flag = '1'
	AND ca.ban_pro_typ_id = 790661005
	AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
	AND ca.opening_date <= '2013-06-01'
	AND ( ( ca.cus_acc_status <> 'I' AND ((ca.closing_date IS NULL) OR (ca.closing_date >= '2013-06-01')) )
		OR ( (ca.cus_acc_status = 'I' AND ca.chg_stat_date >= '2013-06-01') ) )
	AND ca.bank_sign = 'RB'
ORDER BY ca.cus_acc_id, cl.nnb_no_days desc;


select * from counter_list;


 SELECT
                    pa.PARAMETER_VALUE
              
                FROM
                    PARAM_APPLICATION pa,
                    PARAMS_DICTIONARY pd
                WHERE
                    pa.PAR_DIC_ID = pd.PAR_DIC_ID
                    AND pd.NAME = 'interface_flag'





