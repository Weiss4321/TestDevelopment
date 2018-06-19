SET CURRENT_SCHEMA = SIDEV

select * from cusacc_external where register_no = '1100';     --- Gavriloviæ

select * from customer where name like '%Gavrilo%';    ---- staviti æu tax_number 54789654123

-----------------------------------------------
select * from customer where cus_id = 822352;

select tax_number,name from customer where cus_id = 822352;

update customer set tax_number = '63019353660' where cus_id = 822352;   -- cus_acc_ext_no = 2484008250001757

commit;




						SELECT 
                                ii.inc_ite_id
                                , ii.item_ord_no
                                , ii.benef_cus_code
                                , cu.name
                                , ii.benef_cus_acc_no
                                , cr.code_char
                                , ii.amount
                                , ii.item_status
                                , ii.financial_status
                                , ii.error_code
                                , ii.error_desc
                                , CURRENT TIMESTAMP ct
                                , ii.eve_id
                                , ii.external_flag
                                , ii.iban
                            FROM 
                                currency cr
                                , system_code_value scv1
                                , system_code_value scv2
                                , income_item ii 
                                LEFT OUTER JOIN customer cu
                                    ON ii.benef_cus_id = cu.cus_id
                            WHERE 
                                ii.inc_hea_id = 1010485421
                                AND cr.cur_id = ii.cur_id
                                AND scv1.sys_code_value = ii.item_status
                                AND scv1.sys_cod_id = 'item_status       '
                                AND scv2.sys_code_value = ii.financial_status
                                AND scv2.sys_cod_id = 'pckg_i_status     '
                                AND (ii.benef_cus_acc_no like '%' OR (ii.iban_or_acc_no like 'HR4624840083205000286'))
                                AND ((1=0) OR (ii.iban like 'HR4624840083205000286' OR ii.iban_or_acc_no like 'HR4624840083205000286'))                            
                                AND ((1=0) OR ii.inc_sub_type_code = 100)
                                AND ii.item_status like '%'
                                AND ii.financial_status like '%'
                                AND ii.bank_sign = RB
                            ORDER BY ii.item_ord_no;


  select * from income_subtype;

  select * from income_subtype  order by inc_sub_typ_code ;
 


