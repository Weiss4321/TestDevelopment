

SET CURRENT_SCHEMA = SIDEV


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
                                AND cl.process_date = '2013-07-07')
                    WHERE
                        ca.ban_pro_typ_id = 790661005
                        AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
                        AND ( (ca.cus_acc_status <> 'I') OR (ca.cus_acc_status = 'I' AND chg_stat_date >= '2013-05-08') )
                        AND ca.bank_sign = 'RB'
                        AND (ci.pay_list_flag = '1' OR cl.nnb_no_days >= 2)
                        AND tc.process_date >= '2013-07-07' 
                        AND tc.process_date <= '2013-07-07'
                        AND t.credit_amount <> 0
                        AND t.tur_typ_id NOT IN (
                                203992005, 203991005, 203990005, 203989005,
                                272350005, 272356005, 272357005, 272366005,
                                1561010887, 1561012317, 1561039487, 1561040917, 1561055217, 1561070947, 1561128147, 1561129577, 1561133867, 1561135297, 1901718005, 1901719005,
                                3718517667, 3850379797,
                                3850379797)
                        AND (ex.tra_typ_id <> 1426436003
                                OR t.tur_typ_id NOT IN (120348005, 120362005, 867912005, 867916005, 867920005, 867924005, 867928005, 
                                                        984509005, 984719005, 984722005, 984723005, 989237005, 1173130005, 989239005, 984721005,
                                                        4020407797, 4020408797, 4034626797, 4034629797, 4034630797, 4034632797, 4034660797))                     
                        AND ex.tra_typ_id NOT IN (
                                4680657704, 4680661704, 4670626704, 4680665704, 4680669704, 4680673704,
                                1685855003, 1685858003, 1685861003, 1685880003, 1685883003, 1685886003, 1685900003, 1685903003, 1685906003,
                                1700949003, 1700984003, 1700987003, 1701043003, 1701046003, 1701049003, 1700952003, 1700955003, 1700981003)
                      AND (ca.cus_acc_status <> 'W'
                                       OR t.tur_typ_id NOT IN(3994573797,3994572797)) 
                    ORDER BY
                        ca.cus_acc_no,
                        t.process_timestamp with ur;


SELECT * FROM turn_customer tc
                        INNER JOIN turnover t ON tc.tur_id = t.tur_id
                        INNER JOIN event_trx ex ON t.tra_id = ex.tra_id
                        INNER JOIN turnover_type tt ON t.tur_typ_id = tt.tur_typ_id
                        INNER JOIN customer_account ca ON tc.cus_acc_id = ca.cus_acc_id                
                        INNER JOIN citizen ci ON ca.cus_id = ci.cus_id
                        LEFT OUTER JOIN counter_list cl ON ( 
                                ca.cus_acc_id = cl.cus_acc_id 
                                AND cl.pro_met_code = 'HNB' 
                                AND cl.process_date = '2013-07-07')
                    WHERE
                        ca.ban_pro_typ_id = 790661005
                        AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
                        AND ( (ca.cus_acc_status <> 'I') OR (ca.cus_acc_status = 'I' AND chg_stat_date >= '2013-05-08') )
                        AND ca.bank_sign = 'RB'
                        AND (ci.pay_list_flag = '1' OR cl.nnb_no_days >= 2)
                        AND tc.process_date >= '2013-07-07' 
                        AND tc.process_date <= '2013-07-07'
                        AND t.credit_amount <> 0
                        AND t.tur_typ_id NOT IN (203992005, 203991005, 203990005, 203989005, 272350005, 272356005, 272357005, 272366005, 1561010887, 1561012317, 1561039487, 1561040917, 1561055217, 1561070947, 1561128147, 1561129577, 1561133867, 1561135297, 1901718005, 1901719005, 3718517667, 3850379797, 3850379797)
                        AND (ex.tra_typ_id <> 1426436003
                                OR t.tur_typ_id NOT IN (120348005, 120362005, 867912005, 867916005, 867920005, 867924005, 867928005, 984509005, 984719005, 984722005, 984723005, 989237005, 1173130005, 989239005, 984721005, 4020407797, 4020408797, 4034626797, 4034629797, 4034630797, 4034632797, 4034660797))                     
                        AND ex.tra_typ_id NOT IN (4680657704, 4680661704, 4670626704, 4680665704, 4680669704, 4680673704, 1685855003, 1685858003, 1685861003, 1685880003, 1685883003, 1685886003, 1685900003, 1685903003, 1685906003, 1700949003, 1700984003, 1700987003, 1701043003, 1701046003, 1701049003, 1700952003, 1700955003, 1700981003)
                      AND (ca.cus_acc_status <> 'W' OR t.tur_typ_id NOT IN(3994573797, 3994572797)) 
                    ORDER BY
                        ca.cus_acc_no,
                        t.process_timestamp with ur;


select * from Cusacc_risk;


select * FROM customer_account where ban_pro_typ_id = '790661005' and cus_acc_status = 'A';

select * FROM customer_account  where cus_acc_no = '3205003215';




                        
