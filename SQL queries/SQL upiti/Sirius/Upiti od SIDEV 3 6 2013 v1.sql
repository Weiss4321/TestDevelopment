SET CURRENT_SCHEMA = SIDEV;




   				SELECT
                        ex.tra_id,
                        ex.tra_typ_id,
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
                        ci.cus_id,
                        ci.code cus_code,
                        ci.register_no,
                        ci.name,
                        ci.surname,
                        ci.pay_list_flag,
                        cl.nnb_no_days,
                        cl.nnb_date,
                        cl.absorbing_status,
                        cl.abs_status_date,
                        cl.process_date cl_process_date,
                        t.tur_id,
                        t.eve_id,
                        t.value_date,
                        t.process_date,
                        t.process_timestamp,
                        t.cur_id,
                        t.fc_debit_amount,
                        t.fc_credit_amount,
                        t.debit_amount,
                        t.credit_amount,
                        t.turnover_desc,
                        tt.tur_typ_id,
                        tt.code tt_code,
                        tt.name tt_name,
                        tt.reg_income
                    FROM
                        turn_customer tc
                        INNER JOIN turnover t ON tc.tur_id = t.tur_id
                        INNER JOIN event_trx ex ON t.tra_id = ex.tra_id
                        INNER JOIN turnover_type tt ON t.tur_typ_id = tt.tur_typ_id
                        INNER JOIN customer_account ca ON tc.cus_acc_id = ca.cus_acc_id                
                        INNER JOIN citizen ci ON ca.cus_id = ci.cus_id
                        LEFT OUTER JOIN counter_list cl ON ( 
                                ca.cus_acc_id = cl.cus_acc_id 
                                AND cl.pro_met_code = 'HNB' 
                                AND cl.process_date = '18.10.2013' )
                    WHERE
                        ca.ban_pro_typ_id = 790661005
                        AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
                        AND ( (ca.cus_acc_status <> 'I') OR (ca.cus_acc_status = 'I' AND chg_stat_date >= '15.05.2013') )
                        AND ca.bank_sign = 'RB'
                        AND (ci.pay_list_flag = '1' OR cl.nnb_no_days >= 2)
                        AND tc.process_date >= '18.4.2013'
                        AND tc.process_date <= '18.4.2013'
                        AND t.credit_amount <> 0
                        AND t.tur_typ_id NOT IN (
                                203992005, 203991005, 203990005, 203989005,     /* Prometi preknjizenja */
                                272350005, 272356005, 272357005, 272366005,     /* Prometi preknjizenja po po domicilu, sektoru i komitentu */
                                1561010887, 1561012317, 1561039487, 1561040917, 1561055217, 1561070947, 1561128147, 1561129577, 1561133867, 1561135297, 1901718005, 1901719005,     /* Rezervacije */
                                3718517667, 3850379797,     /* Prijenosi na utuzeno potrazivanje */
                                3850379797)                 /* Preknjizenje duga po utuzenim */
                        AND (ex.tra_typ_id <> 1426436003   /* Kamate za Opci nalog */
                                OR t.tur_typ_id NOT IN (120348005, 120362005, 867912005, 867916005, 867920005, 867924005, 867928005, 
                                                        984509005, 984719005, 984722005, 984723005, 989237005, 1173130005, 989239005, 984721005,
                                                        4020407797, 4020408797, 4034626797, 4034629797, 4034630797, 4034632797, 4034660797))                     
                        AND ex.tra_typ_id NOT IN (
                                4680657704, 4680661704, 4670626704, 4680665704, 4680669704, 4680673704,
                                1685855003, 1685858003, 1685861003, 1685880003, 1685883003, 1685886003, 1685900003, 1685903003, 1685906003,
                                1700949003, 1700984003, 1700987003, 1701043003, 1701046003, 1701049003, 1700952003, 1700955003, 1700981003)     /* Kamate */
                      AND (ca.cus_acc_status <> 'W'
                                       OR t.tur_typ_id NOT IN(3994573797,3994572797))     /*  Prometi rasknjiženja i storna rasknjiženja za priljeve po WO raèunu */
                      AND gl_flag IN ('2','4')   /* 2 - poslano u eksternu glavnu knjigu,  4 - verificirani prometi i smiju iæi u eksternu glavnu knjigu */
                    ORDER BY
                        ca.cus_acc_no,
                        t.process_timestamp

--------------------------------------


	SELECT
                        ex.tra_id,
                        ex.tra_typ_id,
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
                        ci.cus_id,
                        ci.code cus_code,
                        ci.register_no,
                        ci.name,
                        ci.surname,
                        ci.pay_list_flag,
                        cl.nnb_no_days,
                        cl.nnb_date,
                        cl.absorbing_status,
                        cl.abs_status_date,
                        cl.process_date cl_process_date,
                        t.tur_id,
                        t.eve_id,
                        t.value_date,
                        t.process_date,
                        t.process_timestamp,
                        t.cur_id,
                        t.fc_debit_amount,
                        t.fc_credit_amount,
                        t.debit_amount,
                        t.credit_amount,
                        t.turnover_desc,
                        tt.tur_typ_id,
                        tt.code tt_code,
                        tt.name tt_name,
                        tt.reg_income
                    FROM
                        turn_customer tc
                        INNER JOIN turnover t ON tc.tur_id = t.tur_id
                        INNER JOIN event_trx ex ON t.tra_id = ex.tra_id
                        INNER JOIN turnover_type tt ON t.tur_typ_id = tt.tur_typ_id
                        INNER JOIN customer_account ca ON tc.cus_acc_id = ca.cus_acc_id                
                        INNER JOIN citizen ci ON ca.cus_id = ci.cus_id
                        LEFT OUTER JOIN counter_list cl ON ( 
                                ca.cus_acc_id = cl.cus_acc_id 
                                AND cl.pro_met_code = 'HNB' 
                                AND cl.process_date = '18.10.2013' )
                    WHERE
                        ca.ban_pro_typ_id = 790661005
                        AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
                        AND ( (ca.cus_acc_status <> 'I') OR (ca.cus_acc_status = 'I' AND chg_stat_date >= '15.05.2013') )
                        AND ca.bank_sign = 'RB'                                      
                        AND t.credit_amount <> 0
                        AND t.tur_typ_id NOT IN (
                                203992005, 203991005, 203990005, 203989005,     /* Prometi preknjizenja */
                                272350005, 272356005, 272357005, 272366005,     /* Prometi preknjizenja po po domicilu, sektoru i komitentu */
                                1561010887, 1561012317, 1561039487, 1561040917, 1561055217, 1561070947, 1561128147, 1561129577, 1561133867, 1561135297, 1901718005, 1901719005,     /* Rezervacije */
                                3718517667, 3850379797,     /* Prijenosi na utuzeno potrazivanje */
                                3850379797)                 /* Preknjizenje duga po utuzenim */
                        AND (ex.tra_typ_id <> 1426436003   /* Kamate za Opci nalog */
                                OR t.tur_typ_id NOT IN (120348005, 120362005, 867912005, 867916005, 867920005, 867924005, 867928005, 
                                                        984509005, 984719005, 984722005, 984723005, 989237005, 1173130005, 989239005, 984721005,
                                                        4020407797, 4020408797, 4034626797, 4034629797, 4034630797, 4034632797, 4034660797))                     
                        AND ex.tra_typ_id NOT IN (
                                4680657704, 4680661704, 4670626704, 4680665704, 4680669704, 4680673704,
                                1685855003, 1685858003, 1685861003, 1685880003, 1685883003, 1685886003, 1685900003, 1685903003, 1685906003,
                                1700949003, 1700984003, 1700987003, 1701043003, 1701046003, 1701049003, 1700952003, 1700955003, 1700981003)     /* Kamate */
                      AND (ca.cus_acc_status <> 'W'
                                       OR t.tur_typ_id NOT IN(3994573797,3994572797))     /*  Prometi rasknjiženja i storna rasknjiženja za priljeve po WO raèunu */
                      AND gl_flag IN ('2','4')   /* 2 - poslano u eksternu glavnu knjigu,  4 - verificirani prometi i smiju iæi u eksternu glavnu knjigu */
                    ORDER BY
                        ca.cus_acc_no,
                        t.process_timestamp


------------------


select * from income_file;


                        