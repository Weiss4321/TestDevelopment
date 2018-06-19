SET CURRENT_SCHEMA = 'SIDEV';

							SELECT 
							ih.inc_hea_id IncHeaID
							, ih.inc_typ_id IncTypID
							, ih.total_cur_id
							, it.inc_typ_code
							, it.inc_typ_desc
							, ih.inc_hea_no
							, ih.file_date
							, ih.total_item_count
							, ih.total_amount
							, cur.code_char
							, ih.package_status
							, ih.pckg_fin_status
							, ih.error_code
							, ih.error_desc, ih.value_date
							, CURRENT TIMESTAMP userLock, ih.payor_register_no
							, cur.code_num
							, ih.payor_code
							, ih.payor_name
							, ih.posting_time
							, ou.code 
							, ih.closing_date
				            , ih.payor_bank_cou_id,
				            ih.PAYOR_TAX_NUM,
				            ih.INC_FILE_SOURCE,
				            ih.EXEC_TYPE,
				            ih.PAYOR_IBAN,
				            ih.MIDPAYOR_TAX_NUM
						FROM 
							INCOME_HEAD ih 
							LEFT OUTER JOIN INCOME_TYPE it
								ON ih.inc_typ_id = it.inc_typ_id
								LEFT OUTER JOIN CURRENCY cur
									ON cur.cur_id = ih.total_cur_id
									LEFT OUTER JOIN ORGANIZATION_UNIT ou
										ON ou.org_uni_id = ih.org_uni_id
						WHERE 
							ih.bank_sign = 'RB'
							AND ih.inc_typ_id = :(incTypID)
							AND ih.inc_hea_no like :(incHeaNo)
							AND ih.inc_typ_id NOT IN ( :(incTypIdNotIn) )
							AND (:(isRecordingUseIdEntered) = 0 OR ih.recording_use_id = :(recordingUseID))
							AND (:(isRegisterNoEntered) = 0 OR ih.payor_register_no = :(registerNo))
							AND (:(isCusCodeEntered) = 0 OR ih.payor_code = :(cusCode))
							AND (:(isCusNameEntered) = 0 OR ((:(isCusNameEntered) = 1 AND ih.payor_name = :(cusName)) 
																		OR (:(isCusNameEntered) = 2 AND ih.payor_name LIKE :(cusName))))
						ORDER BY ih.inc_hea_no DESC;


select * from ORGANIZATION_UNIT;  -- 
--- tu koristim  org_uni_id   ---  npr 025 - PODRUŽNICA ZAGREB
select * from CURRENCY;
--- tu koristim cur_id     -- npr 63999     HRK
select * from INCOME_TYPE;
--- tu koristim inc_typ_id     --- 56322019    - plaæe

----- upit bez presjeka na neke druge tablice radi ok  ----   

							SELECT 					
							 ih.inc_typ_id 
							, ih.total_cur_id
							, ih.inc_hea_no
							, ih.file_date
							, ih.total_item_count
							, ih.total_amount
							, ih.package_status
							, ih.pckg_fin_status
							, ih.error_code
							, ih.error_desc,
							ih.value_date
							, ih.payor_register_no
							, ih.payor_code
							, ih.payor_name
							, ih.posting_time
							, ih.closing_date
				           	 , ih.payor_bank_cou_id,
				           	 ih.PAYOR_TAX_NUM,
				            	ih.INC_FILE_SOURCE,
				            	ih.EXEC_TYPE,
				            	ih.PAYOR_IBAN,
				            	ih.MIDPAYOR_TAX_NUM
						FROM 
							INCOME_HEAD ih 
						WHERE 
							ih.bank_sign = 'RB';


						