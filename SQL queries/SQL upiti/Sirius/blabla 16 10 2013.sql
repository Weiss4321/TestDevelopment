SET CURRENT_SCHEMA = 'SIP'


select * from turnover;

select * from turnover where tur_id =  1535041606321;    -- promet ima datum knjiženja 07-10-2013


			select 
					tur_id,
                        eve_id,
                        value_date,
                        process_date,
                        process_timestamp,
                        cur_id,
                        fc_debit_amount,
                        fc_credit_amount,
                        debit_amount,
                        credit_amount,
                        turnover_desc
			from
					turnover 
			where
					tur_id = 1535041606321;
			
                        