SET CURRENT_SCHEMA = 'SIP'

select * from turn_customer;


   --- ovakav upit nije ništa dohvatio
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
				 FROM turn_customer tc
                        INNER JOIN turnover t ON tc.tur_id = t.tur_id
                        INNER JOIN event_trx ex ON t.tra_id = ex.tra_id
                        INNER JOIN turnover_type tt ON t.tur_typ_id = tt.tur_typ_id
                        INNER JOIN customer_account ca ON tc.cus_acc_id = ca.cus_acc_id                
                        INNER JOIN citizen ci ON ca.cus_id = ci.cus_id
                        LEFT OUTER JOIN counter_list cl ON ( 
                                ca.cus_acc_id = cl.cus_acc_id 
                                AND cl.pro_met_code = 'HNB' 
                                AND cl.process_date = '2013-11-05')
                    WHERE
                        ca.ban_pro_typ_id = 790661005
                        AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
                        AND ( (ca.cus_acc_status <> 'I') OR (ca.cus_acc_status = 'I' AND chg_stat_date >= '2013-10-06') )
                        AND ca.bank_sign = 'RB'
                        AND (ci.pay_list_flag = '1' OR cl.nnb_no_days >= 2)
                        AND tc.process_date >= '2013-11-05' 
                        AND tc.process_date <= '2013-11-05'
                        AND t.credit_amount <> 0
                        AND t.tur_typ_id NOT IN (203992005, 203991005, 203990005, 203989005, 272350005, 272356005, 272357005, 272366005, 1561010887, 1561012317, 1561039487, 1561040917, 1561055217, 1561070947, 1561128147, 1561129577, 1561133867, 1561135297, 1901718005, 1901719005, 3718517667, 3850379797, 3850379797)
                        AND (ex.tra_typ_id <> 1426436003
                                OR t.tur_typ_id NOT IN (120348005, 120362005, 867912005, 867916005, 867920005, 867924005, 867928005, 984509005, 984719005, 984722005, 984723005, 989237005, 1173130005, 989239005, 984721005, 4020407797, 4020408797, 4034626797, 4034629797, 4034630797, 4034632797, 4034660797))                     
                        AND ex.tra_typ_id NOT IN (4680657704, 4680661704, 4670626704, 4680665704, 4680669704, 4680673704, 1685855003, 1685858003, 1685861003, 1685880003, 1685883003, 1685886003, 1685900003, 1685903003, 1685906003, 1700949003, 1700984003, 1700987003, 1701043003, 1701046003, 1701049003, 1700952003, 1700955003, 1700981003)
                      AND (ca.cus_acc_status <> 'W' OR t.tur_typ_id NOT IN(3994573797, 3994572797)) 
                      AND gl_flag IN ('2','4')   /* 2 - poslano u eksternu glavnu knjigu,  4 - verificirani prometi i smiju iæi u eksternu glavnu knjigu */
                    ORDER BY
                        ca.cus_acc_no,
                        t.process_timestamp with ur;



------------------------- SQL za bfR4 koji smo vraæali za prijavu 20341 sa dodatnim uvjetom za gl_flag

				SELECT *
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
                                AND cl.process_date = '2013-11-05')
                    WHERE
                        ca.ban_pro_typ_id = 790661005
                        AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
                        AND ( (ca.cus_acc_status <> 'I') OR (ca.cus_acc_status = 'I' AND chg_stat_date >= '2013-09-05') )
                        AND ca.bank_sign = 'RB'
                        AND (ci.pay_list_flag = '1' OR cl.nnb_no_days >= 2)
                        AND tc.process_date >= '2013-10-06' 
                        AND tc.process_date <= '2013-11-04'
                        AND t.credit_amount <> 0
                        AND t.tur_typ_id NOT IN (203992005, 203991005, 203990005, 203989005, 272350005, 272356005, 272357005, 272366005, 1561010887, 1561012317, 1561039487, 1561040917, 1561055217, 1561070947, 1561128147, 1561129577, 1561133867, 1561135297, 1901718005, 1901719005, 3718517667, 3850379797, 3850379797)
                        AND (ex.tra_typ_id <> 1426436003
                                OR t.tur_typ_id NOT IN (120348005, 120362005, 867912005, 867916005, 867920005, 867924005, 867928005, 984509005, 984719005, 984722005, 984723005, 989237005, 1173130005, 989239005, 984721005, 4020407797, 4020408797, 4034626797, 4034629797, 4034630797, 4034632797, 4034660797))                     
                        AND ex.tra_typ_id NOT IN (4680657704, 4680661704, 4670626704, 4680665704, 4680669704, 4680673704, 1685855003, 1685858003, 1685861003, 1685880003, 1685883003, 1685886003, 1685900003, 1685903003, 1685906003, 1700949003, 1700984003, 1700987003, 1701043003, 1701046003, 1701049003, 1700952003, 1700955003, 1700981003)
                      AND (ca.cus_acc_status <> 'W' OR t.tur_typ_id NOT IN(3994573797, 3994572797)) 
                      AND gl_flag IN ('2','4')   /* 2 - poslano u eksternu glavnu knjigu,  4 - verificirani prometi i smiju iæi u eksternu glavnu knjigu */
                    ORDER BY
                        ca.cus_acc_no,
                        t.process_timestamp with ur;

-----------------------------------------------------------------------------

				SELECT COUNT (DISTINCT t.tur_id)
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
                                AND cl.process_date = '2013-11-05')
                    WHERE
                        ca.ban_pro_typ_id = 790661005
                        AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
                        AND ( (ca.cus_acc_status <> 'I') OR (ca.cus_acc_status = 'I' AND chg_stat_date >= '2013-09-05') )
                        AND ca.bank_sign = 'RB'
                        AND (ci.pay_list_flag = '1' OR cl.nnb_no_days >= 9)
                        AND tc.process_date >= '2013-10-06' 
                        AND tc.process_date <= '2013-11-04'
                        AND t.credit_amount <> 0
                        AND t.tur_typ_id NOT IN (203992005, 203991005, 203990005, 203989005, 272350005, 272356005, 272357005, 272366005, 1561010887, 1561012317, 1561039487, 1561040917, 1561055217, 1561070947, 1561128147, 1561129577, 1561133867, 1561135297, 1901718005, 1901719005, 3718517667, 3850379797, 3850379797)
                        AND (ex.tra_typ_id <> 1426436003
                                OR t.tur_typ_id NOT IN (120348005, 120362005, 867912005, 867916005, 867920005, 867924005, 867928005, 984509005, 984719005, 984722005, 984723005, 989237005, 1173130005, 989239005, 984721005, 4020407797, 4020408797, 4034626797, 4034629797, 4034630797, 4034632797, 4034660797))                     
                        AND ex.tra_typ_id NOT IN (4680657704, 4680661704, 4670626704, 4680665704, 4680669704, 4680673704, 1685855003, 1685858003, 1685861003, 1685880003, 1685883003, 1685886003, 1685900003, 1685903003, 1685906003, 1700949003, 1700984003, 1700987003, 1701043003, 1701046003, 1701049003, 1700952003, 1700955003, 1700981003)
                      AND (ca.cus_acc_status <> 'W' OR t.tur_typ_id NOT IN(3994573797, 3994572797)) 
                      AND gl_flag IN ('2','4')   /* 2 - poslano u eksternu glavnu knjigu,  4 - verificirani prometi i smiju iæi u eksternu glavnu knjigu */

                    --- distinct tur_id ova je dohvaæeno 120521

-------------- drugi upit bez distinct   -- dohvaæeno je 121618 


---- upit je dohvatio 119322 tur_ida  -  6.11.2013

               SELECT COUNT (t.tur_id)
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
                                AND cl.process_date = '2013-11-05')
                    WHERE
                        ca.ban_pro_typ_id = 790661005
                        AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
                        AND ( (ca.cus_acc_status <> 'I') OR (ca.cus_acc_status = 'I' AND chg_stat_date >= '2013-09-05') )
                        AND ca.bank_sign = 'RB'
                        AND (ci.pay_list_flag = '1' OR cl.nnb_no_days >= 9)
                        AND tc.process_date >= '2013-10-06' 
                        AND tc.process_date <= '2013-11-05'
                        AND t.credit_amount <> 0
                        AND t.tur_typ_id NOT IN (203992005, 203991005, 203990005, 203989005, 272350005, 272356005, 272357005, 272366005, 1561010887, 1561012317, 1561039487, 1561040917, 1561055217, 1561070947, 1561128147, 1561129577, 1561133867, 1561135297, 1901718005, 1901719005, 3718517667, 3850379797, 3850379797)
                        AND (ex.tra_typ_id <> 1426436003
                                OR t.tur_typ_id NOT IN (120348005, 120362005, 867912005, 867916005, 867920005, 867924005, 867928005, 984509005, 984719005, 984722005, 984723005, 989237005, 1173130005, 989239005, 984721005, 4020407797, 4020408797, 4034626797, 4034629797, 4034630797, 4034632797, 4034660797))                     
                        AND ex.tra_typ_id NOT IN (4680657704, 4680661704, 4670626704, 4680665704, 4680669704, 4680673704, 1685855003, 1685858003, 1685861003, 1685880003, 1685883003, 1685886003, 1685900003, 1685903003, 1685906003, 1700949003, 1700984003, 1700987003, 1701043003, 1701046003, 1701049003, 1700952003, 1700955003, 1700981003)
                      AND (ca.cus_acc_status <> 'W' OR t.tur_typ_id NOT IN(3994573797, 3994572797)) 
                      AND gl_flag IN ('2','4')   /* 2 - poslano u eksternu glavnu knjigu,  4 - verificirani prometi i smiju iæi u eksternu glavnu knjigu */     

       ---- ponovno puštam gore napisani upit 7.11.2013 -- traje cca 10 minuta      -- 119322 podataka
       -- broj slogova je jednak jer ako izbrišemo identiène slogove dobijemo 119331  -- slog što je ok
       
       

  			---- istražujem koliko 
			select * from turnover where system_timestamp > '01.11.2013';


                  select * from turnover where tur_id = 5880477824521;

-------------------------------------------------------------------------------------------------
                   select * from turn_customer where tur_id = 5880477824521;

				select * from turnover where tra_id like '5889988292521';

				 select * from turnover where tur_id =  144515938000;

				select * from turn_customer where tur_id like  '%14286%';

		select * from customer_account;

		   select * from customer_account  where cus_acc_no = '3320019616';

		    select * from customer_account  where cus_acc_no like '%3320019616';



			--- dohvat spornih raèuna


		   ------------------------- provjera po counter listi
			select * from customer_account 
			
			where cus_acc_id = 

  


                
						select
								*
							from counter_list cl, customer_account ca
							where ca.cus_acc_id = cl.cus_acc_id
							AND cl.pro_met_code = 'HNB'
							AND cl.process_date = '2013-11-05' with ur;


		   
			---------------------------   Test

			select * FROM turnover;



  				SELECT 
                      ca.cus_acc_id,
                      ca.cus_id,
                      ca.cus_acc_no,
                      ca.iban_acc_no,
                      ca.contract_no,
                      ca.cus_acc_name,
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
                      cl.nnb_no_days,
                      cl.nnb_date,                    
                      cl.process_date 			
				FROM
                        turn_customer tc
                        INNER JOIN turnover t ON tc.tur_id = 142865819200
                        INNER JOIN event_trx ex ON t.tra_id = ex.tra_id
                        INNER JOIN turnover_type tt ON t.tur_typ_id = tt.tur_typ_id
                        INNER JOIN customer_account ca ON tc.cus_acc_id = ca.cus_acc_id                
                        INNER JOIN citizen ci ON ca.cus_id = ci.cus_id
                        LEFT OUTER JOIN counter_list cl ON ( 
                                ca.cus_acc_id = cl.cus_acc_id 
                                AND cl.pro_met_code = 'HNB' 
                                AND cl.process_date = '2013-11-05')
                    WHERE
                        ca.ban_pro_typ_id = 790661005
                        AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
                        AND ( (ca.cus_acc_status = 'I') OR (ca.cus_acc_status = 'I' AND chg_stat_date >= '2013-09-05') )
                        AND ca.bank_sign = 'RB'
                        AND (ci.pay_list_flag = '1' OR cl.nnb_no_days >= 9)
                        AND tc.process_date >= '2013-10-06' 
                        AND tc.process_date <= '2013-11-05'
                        AND t.credit_amount <> 0
                        AND t.tur_typ_id NOT IN (203992005, 203991005, 203990005, 203989005, 272350005, 272356005, 272357005, 272366005, 1561010887, 1561012317, 1561039487, 1561040917, 1561055217, 1561070947, 1561128147, 1561129577, 1561133867, 1561135297, 1901718005, 1901719005, 3718517667, 3850379797, 3850379797)
                        AND (ex.tra_typ_id <> 1426436003
                                OR t.tur_typ_id NOT IN (120348005, 120362005, 867912005, 867916005, 867920005, 867924005, 867928005, 984509005, 984719005, 984722005, 984723005, 989237005, 1173130005, 989239005, 984721005, 4020407797, 4020408797, 4034626797, 4034629797, 4034630797, 4034632797, 4034660797))                     
                        AND ex.tra_typ_id NOT IN (4680657704, 4680661704, 4670626704, 4680665704, 4680669704, 4680673704, 1685855003, 1685858003, 1685861003, 1685880003, 1685883003, 1685886003, 1685900003, 1685903003, 1685906003, 1700949003, 1700984003, 1700987003, 1701043003, 1701046003, 1701049003, 1700952003, 1700955003, 1700981003)
                      AND (ca.cus_acc_status <> 'W' OR t.tur_typ_id NOT IN(3994573797, 3994572797)) 
                      AND gl_flag IN ('2','4') with ur;   /* 2 - poslano u eksternu glavnu knjigu,  4 - verificirani prometi i smiju iæi u eksternu glavnu knjigu */  

  ---- java.lang.OutOfMemoryError: GC overhead limit exceeded  previše podataka dohvatim 

--- pokušaj dohvata priljeva po nekom drugom tur_id-u

--- samo su dva puštanja bfr4 
select * from batch_log where bat_def_id = 6266762704;

--- samo su dva puštanja bfr3 
select * from batch_log where bat_def_id = 6259167704;

select * from turnover where tur_id like '5916157816521';





   select * from customer_account where 









               SELECT COUNT (t.tur_id)
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
                                AND cl.process_date = '2013-11-05')
                    WHERE
                        ca.ban_pro_typ_id = 790661005
                        AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
                        AND ( (ca.cus_acc_status = 'I') OR (ca.cus_acc_status = 'I' AND chg_stat_date >= '2013-09-05') )
                        AND ca.bank_sign = 'RB'
                        AND (ci.pay_list_flag = '1' OR cl.nnb_no_days >= 9)
                        AND tc.process_date >= '2013-10-06' 
                        AND tc.process_date <= '2013-11-05'
                        AND t.credit_amount <> 0
                        AND t.tur_typ_id NOT IN (203992005, 203991005, 203990005, 203989005, 272350005, 272356005, 272357005, 272366005, 1561010887, 1561012317, 1561039487, 1561040917, 1561055217, 1561070947, 1561128147, 1561129577, 1561133867, 1561135297, 1901718005, 1901719005, 3718517667, 3850379797, 3850379797)
                        AND (ex.tra_typ_id <> 1426436003
                                OR t.tur_typ_id NOT IN (120348005, 120362005, 867912005, 867916005, 867920005, 867924005, 867928005, 984509005, 984719005, 984722005, 984723005, 989237005, 1173130005, 989239005, 984721005, 4020407797, 4020408797, 4034626797, 4034629797, 4034630797, 4034632797, 4034660797))                     
                        AND ex.tra_typ_id NOT IN (4680657704, 4680661704, 4670626704, 4680665704, 4680669704, 4680673704, 1685855003, 1685858003, 1685861003, 1685880003, 1685883003, 1685886003, 1685900003, 1685903003, 1685906003, 1700949003, 1700984003, 1700987003, 1701043003, 1701046003, 1701049003, 1700952003, 1700955003, 1700981003)
                      AND (ca.cus_acc_status <> 'W' OR t.tur_typ_id NOT IN(3994573797, 3994572797)) 
                      AND gl_flag IN ('2','4')   /* 2 - poslano u eksternu glavnu knjigu,  4 - verificirani prometi i smiju iæi u eksternu glavnu knjigu */     




------

	select * from turn_customer;


               SELECT 
					tc.cus_acc_no
               
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
                                AND cl.process_date = '2013-11-05')
                    WHERE
                        ca.ban_pro_typ_id = 790661005
                        AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
                        AND ( (ca.cus_acc_status = 'I') OR (ca.cus_acc_status = 'I' AND chg_stat_date >= '2013-09-05') )
                        AND ca.bank_sign = 'RB'
                        AND (ci.pay_list_flag = '1' OR cl.nnb_no_days >= 9)
                        AND tc.process_date >= '2013-10-06' 
                        AND tc.process_date <= '2013-11-05'
                        AND t.credit_amount <> 0
                        AND t.tur_typ_id NOT IN (203992005, 203991005, 203990005, 203989005, 272350005, 272356005, 272357005, 272366005, 1561010887, 1561012317, 1561039487, 1561040917, 1561055217, 1561070947, 1561128147, 1561129577, 1561133867, 1561135297, 1901718005, 1901719005, 3718517667, 3850379797, 3850379797)
                        AND (ex.tra_typ_id <> 1426436003
                                OR t.tur_typ_id NOT IN (120348005, 120362005, 867912005, 867916005, 867920005, 867924005, 867928005, 984509005, 984719005, 984722005, 984723005, 989237005, 1173130005, 989239005, 984721005, 4020407797, 4020408797, 4034626797, 4034629797, 4034630797, 4034632797, 4034660797))                     
                        AND ex.tra_typ_id NOT IN (4680657704, 4680661704, 4670626704, 4680665704, 4680669704, 4680673704, 1685855003, 1685858003, 1685861003, 1685880003, 1685883003, 1685886003, 1685900003, 1685903003, 1685906003, 1700949003, 1700984003, 1700987003, 1701043003, 1701046003, 1701049003, 1700952003, 1700955003, 1700981003)
                      AND (ca.cus_acc_status <> 'W' OR t.tur_typ_id NOT IN(3994573797, 3994572797)) 
                      AND gl_flag IN ('2','4')   /* 2 - poslano u eksternu glavnu knjigu,  4 - verificirani prometi i smiju iæi u eksternu glavnu knjigu */     



select * from customer_account where cus_acc_no = '3228511312';

			
                       