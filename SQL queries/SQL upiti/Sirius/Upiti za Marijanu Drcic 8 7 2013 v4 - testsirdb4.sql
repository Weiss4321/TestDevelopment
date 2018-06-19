SET CURRENT_SCHEMA = 'SIP'

--- upiti za TestSirdb4 pokrenuti na RBA Razvojnom testu

 SELECT MAX(value_date) FROM EVENT WHERE eve_typ_id = 6266764704 AND value_date IS NOT NULL AND ext_event_num IS NOT NULL

   SELECT MAX(value_date) FROM EVENT WHERE eve_typ_id = 6259192704 AND value_date IS NOT NULL AND ext_event_num IS NOT NULL

   SELECT * FROM EVENT WHERE eve_typ_id = 6266764704 AND value_date IS NOT NULL AND ext_event_num IS NOT NULL ORDER BY value_date desc



