SET CURRENT_SCHEMA = SITP

select * from income_subtype;


 			SELECT          
                inc_sub_typ_desc                                
                FROM    
                income_subtype
                WHERE  inc_sub_typ_code = '100'
                AND current_date BETWEEN date_from and date_until;

 --------------------------


                select * from customer;

                 select * from customer where cus_typ_id = 2998;     ---- ove za testirati -- firme ili strane pravne osobe
                 --- 980191  -- TUI DEUTSCHLAND GMBH
                 --- 990206 -- IBM WORLD TRADE CORPORATE

                 
                 
                 select * from customer where cus_typ_id = 1998;     ---- strane fizièke osobe
                 ---  165612
                 ---  165615
                 --- 185198
                 -- 185201
                 -- 185219
                 

                 --- testiram sa sljedeæim raèunima
                 --- 

                 select * from customer where cus_typ_id = 1998;     ---- 

                 -- register_no 990206 - IBM world trade corporate

                select * from customer_type;  -- cus_typ_id  - odreðuje kakav je tip komitenta  1998 strana fizièka osoba ili mi treba 2998 strana pravna osoba da testiram 5805

                   select * from customer where register_no  = '2001519   ' ;  --- ???
                   


                 select * from customer where register_no = '990384';  -- za testiranje validacije  Koli

                 --- tax_number je OIB koji mi nije popunjen za komitenta

                 select * from customer where register_no = '990001';  -- za testiranje validacije Samsung ima popunjen OIB ili TAX_NUMBER

--- 7147143  -- benef_cus_id

select * from customer;  -- on ima cus_typ_id 



select * from customer_account;

select * from customer_account where  cus_acc_status = 'I' and ban_rel_typ_id = '787172005' ;

select * from income_item;

select * from income_head where inc_hea_id = 1015118421;

                 