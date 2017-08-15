select top 1 * from cc_transaction
order by cct_id asc

 cct_id    cct_transaction_id cct_status cct_request_server      cct_order_id          cct_order_descriptor cct_order_amount cct_order_queable cct_cc_account_id                                    cct_cc_auth_code cct_cc_zip cct_transaction_type cct_tender_type cct_merchant_descriptor cct_mai_processor cct_mai_user cct_mai_password cct_mai_partner cct_mai_vendor cct_date_created        cct_date_updated        ccg_status ccg_result_code ccg_result ccg_transaction_id ccg_auth_code ccg_avsaddr ccg_avszip ccg_error_message ccg_response_message ccg_date_responded      cct_mai_id cct_cc_number cct_cc_cardholder cct_result cct_message ccg_csc cct_original_frn_cct_transaction_id cct_deposit_status client_transaction_id                cct_merchant_phone instance_id cct_mai_instance_id cct_customer_ip ccg_batch_id application_agency_id application_agency_name ccg_card_type_indicator
 --------- ------------------ ---------- ----------------------- --------------------- -------------------- ---------------- ----------------- ---------------------------------------------------- ---------------- ---------- -------------------- --------------- ----------------------- ----------------- ------------ ---------------- --------------- -------------- ----------------------- ----------------------- ---------- --------------- ---------- ------------------ ------------- ----------- ---------- ----------------- -------------------- ----------------------- ---------- ------------- ----------------- ---------- ----------- ------- ----------------------------------- ------------------ ------------------------------------ ------------------ ----------- ------------------- --------------- ------------ --------------------- ----------------------- -----------------------
 280318134 psYntQQy00248094   COMPLETED  prod-amsweb-04e.ams.int oc120328ams001orderID                                0.0100                 0 14690798342000000001CLOQJKCYHRRMQPBCCSTOBFNETJBUWEXK NULL             92121      SALE                 CC              TestMerchantDescriptor  paymentech        AMSLiveTest                                                  2016-07-20 22:43:54.793 2016-07-20 22:43:55.387          0             100        100 psYntQQy00248094                 N           N                            Approved             2016-07-20 22:43:55.387        458 ....7970      STEVE SHERWOOD             0 Approved    Y       NULL                                DEPOSITED          570529C4-36AC-426B-831C-D5CA6A56EA24 NULL                         2                   1 NULL            NULL         NULL                  NULL                    CT01USANYNNNNNN


Elapsed Time:  0 hr, 0 min, 0 sec, 8 ms.


 cct_id    cct_transaction_id cct_status cct_request_server cct_order_id     cct_order_descriptor           cct_order_amount cct_order_queable cct_cc_account_id                                    cct_cc_auth_code cct_cc_zip cct_transaction_type cct_tender_type cct_merchant_descriptor cct_mai_processor cct_mai_user cct_mai_password cct_mai_partner cct_mai_vendor cct_date_created        cct_date_updated        ccg_status ccg_result_code ccg_result ccg_transaction_id ccg_auth_code ccg_avsaddr ccg_avszip ccg_error_message ccg_response_message ccg_date_responded cct_mai_id cct_cc_number cct_cc_cardholder cct_result cct_message               ccg_csc cct_original_frn_cct_transaction_id cct_deposit_status client_transaction_id                cct_merchant_phone instance_id cct_mai_instance_id cct_customer_ip ccg_batch_id application_agency_id application_agency_name ccg_card_type_indicator
 --------- ------------------ ---------- ------------------ ---------------- ------------------------------ ---------------- ----------------- ---------------------------------------------------- ---------------- ---------- -------------------- --------------- ----------------------- ----------------- ------------ ---------------- --------------- -------------- ----------------------- ----------------------- ---------- --------------- ---------- ------------------ ------------- ----------- ---------- ----------------- -------------------- ------------------ ---------- ------------- ----------------- ---------- ------------------------- ------- ----------------------------------- ------------------ ------------------------------------ ------------------ ----------- ------------------- --------------- ------------ --------------------- ----------------------- -----------------------
 432601156 qjeLCXOc39608918   STARTED    lvams03.ams.int    Receipt ID 30066 El Cajon Recreation Department          50.5200                 0 14912578179002089584WJCOMMBQGWNYAPQMKVHBFJJNVBCMHTCF NULL             92020      AUTH                 CC              ACT*City of El Cajon    paymentech        recnet                                                       2017-07-04 23:04:47.923 2017-07-04 23:04:47.923       NULL            NULL       NULL NULL                             NULL        NULL       NULL              NULL                 NULL                 40001940 ....7767      Janise Uhde               22 Transaction Not Attempted NULL    NULL                                UNKNOWN            17B04755-EB83-4FBA-B010-8F84DD7E1145 619-4411744                  1                   1 NULL            NULL         NULL                  NULL                    NULL


Elapsed Time:  0 hr, 0 min, 0 sec, 2 ms.

select distinct(cct_transaction_type) from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type in ('WX_NATIVE','WX_JSAPI','ALIPAY')

 cct_transaction_type
 --------------------
 SALE
 CREDIT

 --------
 13427735
 
SELECT COUNT(1) FROM cc_transaction


select count(*) from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type in ('WX_NATIVE','WX_JSAPI','ALIPAY')
and cct_result=0
and cct_transaction_type='CREDIT'
 
 -----
 15480
 -----
 12557 = 11397 SALE + 1160 CREDIT

select sum(cct_order_amount) as total_amount from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type in ('CAPTURE','SALE')
and cct_mai_id in
(
select ma_id from merchant_accounts
where ma_frn_currency_id=163
)
and cct_result=0

 total_amount_sale
 -------------
 1129087593.2400
 1.12908759324E9
select sum(cct_order_amount) as total_amount from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type='CREDIT'
and cct_mai_id in
(
select ma_id from merchant_accounts
where ma_frn_currency_id=163
)
and cct_result=0
 
 total_amount_credit
 -------------
 45876505.3000

select sum(cct_order_amount) as total_amount from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type in ('CAPTURE','SALE')
and cct_mai_id in
(
select ma_id from merchant_accounts
where ma_frn_currency_id=163
)
and cct_transaction_id in
(
select cct_cc_auth_code from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type='VOID'
and cct_result=0
and cct_cc_auth_code is not null
and cct_mai_id in
(
select ma_id from merchant_accounts
where ma_frn_currency_id=163
)
)
and cct_result=0

 total_amount_void_sale
 ------------
  907517.7000

select sum(cct_order_amount) as total_amount from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type='CREDIT'
and cct_mai_id in
(
select ma_id from merchant_accounts
where ma_frn_currency_id=163
)
and cct_transaction_id in
(
select cct_cc_auth_code from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type='VOID'
and cct_result=0
and cct_cc_auth_code is not null
and cct_mai_id in
(
select ma_id from merchant_accounts
where ma_frn_currency_id=163
)
)
and cct_result=0 
  
 total_amount_void_credit
 ------------
   50061.9000

select 1129087593.2400-45876505.3000-907517.7000+50061.9000

 1082353632.1400
   
select cct_tender_type, cct_transaction_type, count(*) as total,
sum(case when cct_result=0 then 1 else 0 end ) as approved,
sum(case when cct_result=0 then case when cct_order_amount is null then 0 else cct_order_amount end else 0 end ) as approved_amount
from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_mai_id in
(
select ma_id from merchant_accounts
where ma_frn_currency_id=163
)
group by cct_tender_type, cct_transaction_type
order by cct_tender_type, cct_transaction_type, total
 

select cct_tender_type, cct_transaction_type, count(*) as total,
sum(case when cct_result=0 then 1 else 0 end ) as approved,
sum(case when cct_result=0 then case when cct_order_amount is null then 0 else cct_order_amount end else 0 end ) as approved_amount
from cc_transaction
where cct_transaction_id in
(
select cct_cc_auth_code from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type='VOID'
and cct_result=0
and cct_cc_auth_code is not null
and cct_mai_id in
(
select ma_id from merchant_accounts
where ma_frn_currency_id=51
)
)
group by cct_tender_type, cct_transaction_type
order by cct_tender_type, cct_transaction_type, total




select sum(cct_order_amount) from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type in ('WX_NATIVE','WX_JSAPI','ALIPAY')
and cct_result=0
and cct_transaction_type='SALE'
 
 3402815.3700 SALE
 351624.9800 CREDIT

 cct_tender_type
 ---------------
 DEBITEMVF
 STX_ELAVON
 WX_NATIVE
 ECP
 CCEMV
 END_ELAVON
 DEBITEMV
 CCMANUAL
 ALIPAY
 OXXO
 WX_JSAPI
 CCEMVF
 CC
 CCSWIPE
 DEBITSWIPE
 EUDD
 START
 CC_RETAIL
 COMPLETE
 DEBIT

 select distinct(cct_transaction_type) from cc_transaction
 where cct_tender_type='COMPLETE'
 and cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
 
select top 10 * from cc_transaction
where cct_tender_type='COMPLETE' and cct_transaction_type = 'SAVE'




select distinct(cct_transaction_type) from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'

 cct_transaction_type
 --------------------
 COM3DS
 SALE
 SAVE
 VOID
 AUTH
 CREDIT
 INIT3DS
 CAPTURE

select top 100 * from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type='STX_ELAVON'
and cct_transaction_type='SAVE'


select top 100 * from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type='END_ELAVON'
and cct_transaction_type='SAVE'


select count(*) from cc_transaction

 45545489

 select * from currencies
 
 
 select * from merchant_accounts
 where ma_frn_currency_id=33
  ma_id    ma_name                ma_username         ma_password                      ma_frn_pay_proc_id ma_frn_currency_id ma_login1 ma_login2 ma_login3 ma_login4 ma_frn_paymentech_login ma_active ma_enable_charge_descriptor_prefix ma_charge_descriptor_prefix instance_id mcc  amex_merchant_se is_merchant_of_record ma_frn_country_id suppress_avs supports_payment_devices pd_merchant_number pd_client_number language ecp_direct_to_bank merchant_ownership_type allow_fraud_checking auto_approve_passcode moneris_store_id moneris_api_token csc_response_flag csc_decline_threshold_amount avs_zip_response_flag avs_zip_decline_threshold_amount override_merchant_name_for_ecp locked_time             redirect_credentials                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          hashbyte
 -------- ---------------------- ------------------- -------------------------------- ------------------ ------------------ --------- --------- --------- --------- ----------------------- --------- ---------------------------------- --------------------------- ----------- ---- ---------------- --------------------- ----------------- ------------ ------------------------ ------------------ ---------------- -------- ------------------ ----------------------- -------------------- --------------------- ---------------- ----------------- ----------------- ---------------------------- --------------------- -------------------------------- ------------------------------ ----------------------- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- ----------------------------------
 20004733 ROL433417_AUTHORIZENET R635810620406766194 82A856FBB94B76A82FE2770DC51A9899                  9                 33 AHTMM     AHTMM     NULL      NULL                            0         1                                  0 NULL                                  1 NULL NULL                                 0              NULL false        false                    NULL               NULL             NULL     false                                    1 false                false                 NULL             NULL              false                                     NULL false                                             NULL false                          NULL                    NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          0x3ebf81d624660100cc666edd6219599d
 40000685 Endurance - 7399 CNY   EnduranceCNY        C32A0C2979C9E19FDDDC16F6446A90ED                  3                 33 265282    NULL      NULL      NULL                            1         1                                  1 ACT*                                  1                                           0               223 false        false                                                        EN       false                                    0 false                false                                                    false                                     NULL false                                             NULL false                          2016-01-28 13:20:02.677 <Credential><drwp><mid>1006996187</mid><username>activenetworks</username><password>gZtg53c5qERT309AaTA9</password><currency>CNY</currency><keystore>1006996187.jks</keystore><keystorepwd>Active123</keystorepwd><keyname>merchant</keyname><certname>ngcert</certname><country>CN</country><language>zh</language><method>30</method></drwp><wechat><appid>wx2d3c88835e61b286</appid><appSecret>6f4829ab07642b67dc6668a58e68cc59</appSecret><mchid>1307868501</mchid><key>34wmfHpDENB8fICcZ0Zjeu7nIGizroMS</key><sslCertFileName>apiclient_cert_1307868501.p12</sslCertFileName><sslCertPassword>1307868501</sslCertPassword></wechat><drwp_unionpay><mid>1006996187</mid><username>activenetworks</username><password>gZtg53c5qERT309AaTA9</password><currency>CNY</currency></drwp_unionpay></Credential> 0x77bd812621813945a2981ed52c2f522a
 40000690 AW Endurance CNY Test  awendurcny          3584056DF8427FCED327EFBC389AA88F                  3                 33 265282    NULL      NULL      NULL                            1         1                                  1 ACT*                                  1                                           0               223 false        false                                                        EN       false                                    0 false                false                                                    false                                     NULL false                                             NULL false                          NULL                    NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          0xb5d4e8b32582eaaba21677641561dc57

select count(*) from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_mai_id=40000685
and cct_result=0
and cct_transaction_type='CREDIT'

 cct_transaction_type
 --------------------
 SALE 13905  4801290.5700
 CREDIT 1582 519876.3600
 15487

select sum(cct_order_amount) from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_mai_id=40000685
and cct_result=0
and cct_transaction_type='SALE'
 
 
select * from merchant_accounts
where ma_frn_currency_id=112

 ma_id    ma_name            ma_username     ma_password                      ma_frn_pay_proc_id ma_frn_currency_id ma_login1      ma_login2            ma_login3  ma_login4 ma_frn_paymentech_login ma_active ma_enable_charge_descriptor_prefix ma_charge_descriptor_prefix instance_id mcc amex_merchant_se is_merchant_of_record ma_frn_country_id suppress_avs supports_payment_devices pd_merchant_number pd_client_number language ecp_direct_to_bank merchant_ownership_type allow_fraud_checking auto_approve_passcode moneris_store_id moneris_api_token csc_response_flag csc_decline_threshold_amount avs_zip_response_flag avs_zip_decline_threshold_amount override_merchant_name_for_ecp locked_time redirect_credentials                                                                                                                                              hashbyte
 -------- ------------------ --------------- -------------------------------- ------------------ ------------------ -------------- -------------------- ---------- --------- ----------------------- --------- ---------------------------------- --------------------------- ----------- --- ---------------- --------------------- ----------------- ------------ ------------------------ ------------------ ---------------- -------- ------------------ ----------------------- -------------------- --------------------- ---------------- ----------------- ----------------- ---------------------------- --------------------- -------------------------------- ------------------------------ ----------- ----------------------------------------------------------------------------------------------------------------------------------------------------------------- ----------------------------------
     1846 WGW-FBMXN          wingatewebFB484 65C0D9840521D9FC0AF87F641F8E3FE7                  3                112 213186                                                                         1         1                                  1 ACT*                                  1                                          0               223 NULL         NULL                     NULL               NULL             NULL     NULL                                     0 NULL                 NULL                  NULL             NULL              NULL                                      NULL NULL                                              NULL NULL                           NULL        NULL                                                                                                                                                              0x3cf309812445861d4cc702e23ca3e696
 20003857 AW - EnduranceMXN  AWEnduranceMXN  56D0FDB3CD2AAC11C71932F193FEDF45                  6                112 activenetworks gZtg53c5qERT309AaTA9 1591441978                                 0         1                                  1 ACT*                                  1                                          0               223 false        false                                                        EN       false                                    1 false                false                                                    false                                     NULL NULL                                              NULL NULL                           NULL        NULL                                                                                                                                                              0x41af7dd0e1ee6e6ebd9b0b43566e94eb
 20003971 DR - DRWP          DRWPProd        597C999ECB7479019E13FE8BBAB74A9C                  6                112 activenetworks gZtg53c5qERT309AaTA9 1394541028                                 0         1                                  1 ACT*                                  1                                          0               223 false        false                                                        EN       false                                    1 false                false                                                    false                                     NULL NULL                                              NULL NULL                           NULL        NULL                                                                                                                                                              0x04b105c295e7061cb431536bbf056ad6
 40000953 AW - Endurance MXN EnduranceMXN    042203AB452CB317C8D6CFA07C31E002                  3                112 267733         NULL                 NULL       NULL                            1         1                                  1 ACT*                                  1                                          0               223 false        false                                                        EN       false                                    0 false                false                                                    false                                     NULL false                                             NULL false                          NULL        <Credential><oxxo><merchantCode>ACTIVENETWORKM1</merchantCode><xmlPassword>Se^enele^en1</xmlPassword><currency>MXN</currency><max>20000</max></oxxo></Credential> 0xca5f830f4f7c03a1eff6c18b1ba8c2c5
 40001174 Gartner MXN        gartnerMXN      6C0906ED2D4C6E653DD99745A87AFFC3                  3                112 269978         NULL                 NULL       NULL                            1         1                                  1 ACT*                                  1                                          0               223 false        false                                                        EN       false                                    1 false                false                                                    false                                     NULL false                                             NULL false                          NULL        NULL                                                                                                                                                              0x1a996055676a0cc878181bc03f7bee0f

select sum(cct_order_amount) from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_result=0
and cct_mai_id in (1846, 20003857, 20003971, 40000953, 40001174)
and cct_transaction_type='CREDIT'

 594 40000953
 30 40001174

 cct_transaction_type
 --------------------
 SALE 689856.8200
 CREDIT  840.0000
 
 
 
select * from merchant_accounts
where ma_frn_currency_id=23
 


select count(*) from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_mai_id in (1850, 20001297, 20001336, 20004690, 20004691)

select * from currencies
where currency_id in
(
select distinct(ma_frn_currency_id) from merchant_accounts
where ma_id in 
(
select distinct(cct_mai_id) from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_result=0
)
)

select * from currencies

 currency_id currency_code currency_name        paymentech_currency_code hashbyte
 ----------- ------------- -------------------- ------------------------ ----------------------------------
          29 CAD           Canadian Dollar      124                      0x88b4438dddfa74dd41755ff1a599984f
          66 HKD           Hong Kong Dollar     344                      0x5995c7e09408bf0b3bcf0253bb610e41
         112 MXN           Mexican Peso         484                      0xf519e597eb2480f6555accd9a4314341
         113 MYR           Malaysian Ringgit    458                      0x430408f6047121c27c801bd81dc0e6ab
         121 NZD           New Zealand Dollar   554                      0x64df8aca0760d59218492bd1681e8046
          81 JPY           Yen                  392                      0x873b01c08adea4fc0f6fee00e4731944
          10 AUD           Australian Dollar    036                      0x40ebba3e393e0a227fe5f21a418b6e7c
         119 NOK           Norwegian Krone      578                      0x45db58f443e5f5c0c56c291b31f5908c
          42 DKK           Danish Krone         208                      0x7774a0e69ce215ee9a818957c9a51274
          56 GBP           Pound Sterling       826                      0x5de47d0b4dec3f1872f6299fd462ab3d
         139 SEK           Swedish Krona        752                      0x0906e61601bd460e73e03beba47367bc
          33 CNY           Yuan Renminbi        156                      0xce93a947d396f482d178ae4751c13e38
         140 SGD           Singapore Dollar     702                      0x38e3c94c07985cc82ff2a35d8079645b
         163 USD           United States Dollar 840                      0xc176590eb2a3a2428f2ef380c16d3422
          31 CHF           Swiss Franc          756                      0xc668ffb46a9423c2f3dcee42ba8efde0
         177 ZAR           Rand                 710                      0xf3b556eba28b854dcc58e47b0e13443f
          51 EUR           Euro                 978                      0x8c6b4f9c26ddab46d506c0fb46263ed5
         151 THB           Baht                 764                      0x4d75e1f163a220b61cd1e81a02fecaba

select cct_transaction_type, count(1) as total from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_result=0
group by cct_transaction_type

select * from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_result=0
and cct_transaction_type='SAVE'

select top 100 * from cc_transaction
where cct_order_amount=56.50
and cct_order_descriptor='HUMBER COLLEGE'
and cct_cc_number='....3489'

select top 100 * from cc_transaction
where cct_order_amount=8.00
and cct_order_descriptor='City of Santa Monica'
and cct_cc_number='....8049'

select top 100 * from cc_transaction
where cct_order_amount=45.00
and cct_order_descriptor='University of Guelph'
and cct_cc_number='....1655'

select top 100 * from cc_transaction
where cct_order_amount=546.00
and cct_order_descriptor='City of Mountain View'
and cct_cc_number='....8893'

select top 100 * from cc_transaction
where cct_cc_account_id in 
(
select top 100 cct_cc_account_id from cc_transaction
where cct_transaction_type='VOID'
and cct_cc_auth_code is null
and cct_result=0
)








 cct_id    cct_transaction_id cct_status cct_request_server cct_order_id cct_order_descriptor cct_order_amount cct_order_queable cct_cc_account_id                                    cct_cc_auth_code cct_cc_zip cct_transaction_type cct_tender_type cct_merchant_descriptor cct_mai_processor cct_mai_user cct_mai_password cct_mai_partner cct_mai_vendor cct_date_created        cct_date_updated        ccg_status ccg_result_code ccg_result ccg_transaction_id ccg_auth_code ccg_avsaddr ccg_avszip ccg_error_message ccg_response_message ccg_date_responded      cct_mai_id cct_cc_number cct_cc_cardholder cct_result cct_message ccg_csc cct_original_frn_cct_transaction_id cct_deposit_status client_transaction_id                cct_merchant_phone instance_id cct_mai_instance_id cct_customer_ip ccg_batch_id application_agency_id application_agency_name ccg_card_type_indicator
 --------- ------------------ ---------- ------------------ ------------ -------------------- ---------------- ----------------- ---------------------------------------------------- ---------------- ---------- -------------------- --------------- ----------------------- ----------------- ------------ ---------------- --------------- -------------- ----------------------- ----------------------- ---------- --------------- ---------- ------------------ ------------- ----------- ---------- ----------------- -------------------- ----------------------- ---------- ------------- ----------------- ---------- ----------- ------- ----------------------------------- ------------------ ------------------------------------ ------------------ ----------- ------------------- --------------- ------------ --------------------- ----------------------- -----------------------
 397976978 p2l1zwFB38702269   COMPLETED  lvams02.ams.int    122139                                    134.8700                 0 14743128896000489386OZZJECLSFQWVBIKETDFCWOFTHTRQCWRH NULL                        VOID                 DEBITEMV        ACT*Town of Tburg       ptcadebit         tillsnbrg                                                    2016-09-19 12:21:29.623 2016-09-19 12:21:29.887          0               0          0 00000019                         NULL        NULL       NULL              APPROVED             2016-09-19 12:21:29.883   20004532 ....8709      NULL                       0 Approved    NULL    NULL                                UNKNOWN            564D2173-546B-4BD8-88EC-60F74F8172A8 NULL                         1                   1 NULL            NULL         NULL                  NULL                    NULL



where cct_cc_auth_code is not null


where cct_transaction_type='VOID'



select top 100 * from cc_transaction
where cct_order_descriptor='HUMBER COLLEGE'
and cct_order_amount=56.50

 397979241 p2l9ceh442952463   COMPLETED  lvams03.ams.int    Receipt ID 2375 HUMBER COLLEGE                56.5000                 0 14743140162000496428RALLTVIHWZYLWGSSAARYRWCSFERTOCWV NULL                        VOID                 DEBITEMV        ACT*Humber Athletics    ptcadebit         recnet                                                       2016-09-19 12:40:16.26  2016-09-19 12:40:16.63           0               0          0 00000012                         NULL        NULL       NULL                APPROVED             2016-09-19 12:40:16.613   20004845 ....3489      NULL                       0 Approved    NULL    NULL                                UNKNOWN            4C5CA921-2610-4963-B232-FAABB87C22AE NULL                         1                   1 NULL            NULL         NULL                  NULL                    NULL

select top 100 * from cc_transaction
where cct_cc_account_id='14743140162000496428RALLTVIHWZYLWGSSAARYRWCSFERTOCWV'
and cct_order_amount=56.50
 
where cct_date_created > '2016-09-18'
and cct_date_created < '2016-09-20'
and cct_order_amount=56.50
and cct_transaction_type





select sum(cct_order_amount) as total_amount from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type='CREDIT'
and cct_mai_id in
(
select ma_id from merchant_accounts
where ma_frn_currency_id=66
)
and cct_transaction_id in
(
select cct_cc_auth_code from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type='VOID'
and cct_result=0
and cct_cc_auth_code is not null
and cct_mai_id in
(
select ma_id from merchant_accounts
where ma_frn_currency_id=66
)
)
and cct_result=0 


select sum(case when cct_order_amount is null then 0 else cct_order_amount end) as total_amount from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type='CREDIT'
and cct_mai_id in
(
select ma_id from merchant_accounts
where ma_frn_currency_id=29
)
and cct_transaction_id in
(
select cct_cc_auth_code from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type='VOID'
and cct_result=0
and cct_cc_auth_code is not null
and cct_mai_id in
(
select ma_id from merchant_accounts
where ma_frn_currency_id=29
)
)
and cct_result=0 


select cct_order_amount from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type='CREDIT'
and cct_mai_id in
(
select ma_id from merchant_accounts
where ma_frn_currency_id=66
)
and cct_transaction_id in
(
select cct_cc_auth_code from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type='VOID'
and cct_result=0
and cct_cc_auth_code is not null
and cct_mai_id in
(
select ma_id from merchant_accounts
where ma_frn_currency_id=66
)
)
and cct_result=0 






select cct_tender_type, cct_transaction_type, count(*) as total_count,
sum(case when cct_result=0 then 1 else 0 end) as approved_count,
sum(case when cct_result=0 then cct_order_amount else 0 end) as approved_amount
from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
group by cct_tender_type, cct_transaction_type
order by cct_tender_type, cct_transaction_type



select 1230223.82 + 1456969.30 - 167051.83 + 1402067.53 - 63055.30 + 543778.54 - 121517.85


select 670306.82 + 18710.00




THB:       97299.00 * 0.02934	2854.7526600
CAD:  select 118633036.00 * 0.7705 91406754.238000
HKD:   select  8455058.52 * 0.128  1082247.49056
MXN:   select   689016.82 * 0.05471 37696.1102222
MYR:   select   414549.00 * 0.2328 96507.007200
NZD:  select   2149160.87 * 0.7284 1565448.777708
JPY:  select    235278.00 * 0.008814  2073.74029200
AUD:  select  12803451.66 * 0.7577  9701175.322782
NOK:  select    780048.02 * 0.1196  93293.743192
DKK:   select   402821.23 * 0.1536  61873.340928
GBP:  select  11833762.75 * 1.2968  15346023.534200
SEK:   select  3961371.26 * 0.1185  469422.494310
CNY:  select   4281414.21 * 0.1471 629796.030291
SGD:  select   3738424.44 * 0.7232 2703628.555008
USD:  1082353632.14 * 1
CHF:   select  1017994.97 * 1.0411 1059834.563267
ZAR:  select   4531467.65 * 0.07445 337367.7665425
EUR:  select  12928990.70 * 1.1419 14763614.480330




select cct_tender_type, cct_transaction_type, count(*) as total_count,
sum(case when cct_result=0 then 1 else 0 end) as approved_count
from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
group by cct_tender_type, cct_transaction_type
order by cct_tender_type, cct_transaction_type


select sum(cct_order_amount) from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type in ('WX_NATIVE','WX_JSAPI','ALIPAY')
and cct_transaction_type = 'SALE'
and cct_result=0

 ------------
 3402815.3700

select sum(cct_order_amount) from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type in ('WX_NATIVE','WX_JSAPI','ALIPAY')
and cct_transaction_type = 'CREDIT'
and cct_result=0

 -----------
 351624.9800
 
 
select sum(cct_order_amount) from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type='OXXO'
and cct_transaction_type = 'SALE'
and cct_result=0

 
 ----------
 18710.0000


select sum(cct_order_amount) from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type='OXXO'
and cct_transaction_type = 'CREDIT'
and cct_result=0







 currency_code cct_transaction_type total_amount
 ------------- -------------------- ---------------
 AUD           CREDIT                   261869.1500
 AUD           SALE                   13065320.8100
 CAD           CAPTURE                16209617.9200
 CAD           CREDIT                  4326380.8500
 CAD           SALE                  106787111.0500
 CHF           CREDIT                    13422.7000
 CHF           SALE                    1031837.8700
 CNY           CREDIT                   168251.3800
 CNY           SALE                    1398475.2000
 DKK           CREDIT                   255321.0000
 DKK           SALE                     658142.2300
 EUR           CREDIT                   410046.6200
 EUR           SALE                   13341613.7200
 GBP           CREDIT                   370114.7700
 GBP           SALE                   12204376.6200
 HKD           CREDIT                    45675.0100
 HKD           SALE                    8501403.7300
 JPY           CREDIT                   150900.0000
 JPY           SALE                     386178.0000
 MXN           CREDIT                      840.0000
 MXN           SALE                     671146.8200
 MYR           CREDIT                      387.0000
 MYR           SALE                     414936.0000
 NOK           CREDIT                    13930.0000
 NOK           SALE                     793978.0200
 NZD           CREDIT                    70336.0000
 NZD           SALE                    2219496.8700
 SEK           CREDIT                   198167.4000
 SEK           SALE                    4159538.6600
 SGD           CREDIT                    34103.7700
 SGD           SALE                    3772528.2100
 THB           CREDIT                      960.0000
 THB           SALE                      98259.0000
 USD           CAPTURE               128718365.7000
 USD           CREDIT                 45876505.3000
 USD           SALE                 1000369227.5400
 ZAR           CREDIT                    68588.1300
 ZAR           SALE                    4603355.8800


Elapsed Time:  0 hr, 0 min, 0 sec, 2 ms.



select cur.currency_code, cct.cct_transaction_type, sum(case when cct.cct_order_amount is null then 0 else cct.cct_order_amount end) as total_amount from cc_transaction cct
join merchant_accounts ma on cct.cct_mai_id=ma.ma_id and cct.cct_mai_instance_id=ma.instance_id
join currencies cur on ma.ma_frn_currency_id= cur.currency_id
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type in ('CAPTURE', 'CREDIT', 'SALE')
and cct_transaction_id in
(
select cct_cc_auth_code from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type='VOID'
and cct_result=0
and cct_cc_auth_code is not null
)
and cct_result=0 
group by cur.currency_code, cct.cct_transaction_type
order by cur.currency_code, cct.cct_transaction_type



 currency_code cct_transaction_type total_amount
 ------------- -------------------- ------------
 CAD           CAPTURE                  794.1500
 CAD           CREDIT                  8454.1900
 CAD           SALE                   44972.1600
 CHF           SALE                     420.2000
 EUR           SALE                    2576.4000
 GBP           SALE                     499.1000
 HKD           SALE                     670.2000
 USD           CAPTURE                41339.7600
 USD           CREDIT                 50061.9000
 USD           SALE                  866177.9400
 ZAR           SALE                    3300.1000


select 128718365.70+1000369227.54-45876505.30-41339.76+50061.90-866177.94
1082353632.14


select cur.currency_code, sum(cct.cct_order_amount) as total_amount from cc_transaction cct
join merchant_accounts ma on cct.cct_mai_id=ma.ma_id and cct.cct_mai_instance_id=ma.instance_id
join currencies cur on ma.ma_frn_currency_id= cur.currency_id
where cct.cct_date_created > '2017-04-01'
and cct.cct_date_created < '2017-07-01'
and cct.cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct.cct_transaction_type in ('CAPTURE','SALE')
and cct.cct_result=0
group by cur.currency_code
order by total_amount



select cur.currency_code, sum(cct.cct_order_amount) as total_amount from cc_transaction cct
join merchant_accounts ma on cct.cct_mai_id=ma.ma_id and cct.cct_mai_instance_id=ma.instance_id
join currencies cur on ma.ma_frn_currency_id= cur.currency_id
where cct.cct_date_created > '2017-04-01'
and cct.cct_date_created < '2017-07-01'
and cct.cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct.cct_transaction_type='CREDIT'
and cct.cct_result=0
group by cur.currency_code
order by total_amount


select cur.currency_code, sum(case when cct.cct_order_amount is null then 0 else cct.cct_order_amount end) as total_amount from cc_transaction cct
join merchant_accounts ma on cct.cct_mai_id=ma.ma_id and cct.cct_mai_instance_id=ma.instance_id
join currencies cur on ma.ma_frn_currency_id= cur.currency_id
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type='CREDIT'
and cct_transaction_id in
(
select cct_cc_auth_code from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type='VOID'
and cct_result=0
and cct_cc_auth_code is not null
)
and cct_result=0 
group by cur.currency_code
order by total_amount


select cur.currency_code, sum(case when cct.cct_order_amount is null then 0 else cct.cct_order_amount end) as total_amount from cc_transaction cct
join merchant_accounts ma on cct.cct_mai_id=ma.ma_id and cct.cct_mai_instance_id=ma.instance_id
join currencies cur on ma.ma_frn_currency_id= cur.currency_id
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type in ('SALE', 'CAPTURE')
and cct_transaction_id in
(
select cct_cc_auth_code from cc_transaction
where cct_date_created > '2017-04-01'
and cct_date_created < '2017-07-01'
and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO')
and cct_transaction_type='VOID'
and cct_result=0
and cct_cc_auth_code is not null
)
and cct_result=0 
group by cur.currency_code
order by total_amount

 currency_code total_amount
 ------------- ------------
 CAD              8454.1900
 USD             50061.9000


select 1129087593.24-45876505.30-907517.70+50061.90





select cur.currency_code, cct.cct_transaction_type, sum(cct.cct_order_amount) as total_amount from cc_transaction cct join merchant_accounts ma on cct.cct_mai_id=ma.ma_id and cct.cct_mai_instance_id=ma.instance_id join currencies cur on ma.ma_frn_currency_id= cur.currency_id where cct.cct_date_created > '2017-04-01' and cct.cct_date_created < '2017-07-01' and cct.cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO','STX_ELAVON','START') and cct.cct_transaction_type in ('CAPTURE', 'CREDIT', 'SALE') and cct.cct_result=0 group by cur.currency_code, cct.cct_transaction_type order by cur.currency_code, cct.cct_transaction_type

 currency_code cct_transaction_type total_amount
 ------------- -------------------- ---------------
 AUD           CREDIT                   261869.1500
 AUD           SALE                   13065320.8100
 CAD           CAPTURE                16209617.9200
 CAD           CREDIT                  4326383.1100
 CAD           SALE                  106787113.3100
 CHF           CREDIT                    13422.7000
 CHF           SALE                    1031837.8700
 CNY           CREDIT                   168251.3800
 CNY           SALE                    1398475.2000
 DKK           CREDIT                   255321.0000
 DKK           SALE                     658142.2300
 EUR           CREDIT                   410046.6200
 EUR           SALE                   13341613.7200
 GBP           CREDIT                   370114.7700
 GBP           SALE                   12204376.6200
 HKD           CREDIT                    45675.0100
 HKD           SALE                    8501403.7300
 JPY           CREDIT                   150900.0000
 JPY           SALE                     386178.0000
 MXN           CREDIT                      840.0000
 MXN           SALE                     671146.8200
 MYR           CREDIT                      387.0000
 MYR           SALE                     414936.0000
 NOK           CREDIT                    13930.0000
 NOK           SALE                     793978.0200
 NZD           CREDIT                    70336.0000
 NZD           SALE                    2219496.8700
 SEK           CREDIT                   198167.4000
 SEK           SALE                    4159538.6600
 SGD           CREDIT                    34103.7700
 SGD           SALE                    3772528.2100
 THB           CREDIT                      960.0000
 THB           SALE                      98259.0000
 USD           CAPTURE               128718365.7000
 USD           CREDIT                 45876503.0400
 USD           SALE                 1000369225.2800
 ZAR           CREDIT                    68588.1300
 ZAR           SALE                    4603355.8800


Elapsed Time:  0 hr, 0 min, 0 sec, 2 ms.



select cur.currency_code, cct.cct_transaction_type, sum(case when cct.cct_order_amount is null then 0 else cct.cct_order_amount end) as total_amount from cc_transaction cct join merchant_accounts ma on cct.cct_mai_id=ma.ma_id and cct.cct_mai_instance_id=ma.instance_id join currencies cur on ma.ma_frn_currency_id= cur.currency_id where cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO','STX_ELAVON','START') and cct_transaction_type in ('CAPTURE', 'CREDIT', 'SALE') and cct_transaction_id in (    select cct_cc_auth_code from cc_transaction    where cct_date_created > '2017-04-01'   and cct_date_created < '2017-07-01'   and cct_tender_type not in ('WX_NATIVE','WX_JSAPI','ALIPAY','OXXO','STX_ELAVON','START')    and cct_transaction_type='VOID'    and cct_result=0    and cct_cc_auth_code is not null) and cct_result=0 group by cur.currency_code, cct.cct_transaction_type order by cur.currency_code, cct.cct_transaction_type






CHF     1031837.87           0.00       13422.70         420.20           0.00           0.00     1017994.97
HKD     8501403.73           0.00       45675.01         670.20           0.00           0.00     8455058.52
MXN      671146.82           0.00         840.00           0.00           0.00           0.00      670306.82
EUR    13341613.72           0.00      410046.62        2576.40           0.00           0.00    12928990.70
DKK      658142.23           0.00      255321.00           0.00           0.00           0.00      402821.23
CAD   106787113.31    16209617.92     4326383.11       44972.16         794.15        8454.19   118633036.00
MYR      414936.00           0.00         387.00           0.00           0.00           0.00      414549.00
USD  1000369225.28   128718365.70    45876503.04      866177.94       41339.76       50061.90  1082353632.14
ZAR     4603355.88           0.00       68588.13        3300.10           0.00           0.00     4531467.65
NOK      793978.02           0.00       13930.00           0.00           0.00           0.00      780048.02
CNY     1398475.20           0.00      168251.38           0.00           0.00           0.00     1230223.82
THB       98259.00           0.00         960.00           0.00           0.00           0.00       97299.00
AUD    13065320.81           0.00      261869.15           0.00           0.00           0.00    12803451.66
SGD     3772528.21           0.00       34103.77           0.00           0.00           0.00     3738424.44
JPY      386178.00           0.00      150900.00           0.00           0.00           0.00      235278.00
GBP    12204376.62           0.00      370114.77         499.10           0.00           0.00    11833762.75
SEK     4159538.66           0.00      198167.40           0.00           0.00           0.00     3961371.26
NZD     2219496.87           0.00       70336.00           0.00           0.00           0.00     2149160.87

CHF->USD     1074246.41           0.00       13974.37         437.47           0.00           0.00     1059834.56
HKD->USD     1088179.68           0.00        5846.40          85.79           0.00           0.00     1082247.49
MXN->USD       36718.44           0.00          45.96           0.00           0.00           0.00       36672.49
EUR->USD    15234788.71           0.00      468232.24        2941.99           0.00           0.00    14763614.48
DKK->USD      101090.65           0.00       39217.31           0.00           0.00           0.00       61873.34
CAD->USD    82279470.81    12489510.61     3333478.19       34651.05         611.89        6513.95    91406754.24
MYR->USD       96597.10           0.00          90.09           0.00           0.00           0.00       96507.01
USD->USD  1000369225.28   128718365.70    45876503.04      866177.94       41339.76       50061.90  1082353632.14
ZAR->USD      342719.85           0.00        5106.39         245.69           0.00           0.00      337367.77
NOK->USD       94959.77           0.00        1666.03           0.00           0.00           0.00       93293.74
CNY->USD      205715.70           0.00       24749.78           0.00           0.00           0.00      180965.92
THB->USD        2882.92           0.00          28.17           0.00           0.00           0.00        2854.75
AUD->USD     9899593.58           0.00      198418.25           0.00           0.00           0.00     9701175.32
SGD->USD     2728292.40           0.00       24663.85           0.00           0.00           0.00     2703628.56
JPY->USD        3403.77           0.00        1330.03           0.00           0.00           0.00        2073.74
GBP->USD    15826635.60           0.00      479964.83         647.23           0.00           0.00    15346023.53
SEK->USD      492905.33           0.00       23482.84           0.00           0.00           0.00      469422.49
NZD->USD     1616681.52           0.00       51232.74           0.00           0.00           0.00     1565448.78


 cct_tender_type cct_transaction_type total_count approved_count approved_amount
 --------------- -------------------- ----------- -------------- ---------------
 ALIPAY          CREDIT                       603            603     167051.8300
 ALIPAY          SALE                        5720           4443    1456969.3000
 OXXO            SALE                         119             43      20720.0000
 WX_JSAPI        CREDIT                       244            224      63055.3000
 WX_JSAPI        SALE                        6831           5446    1402067.5300
 WX_NATIVE       CREDIT                       348            333     121517.8500
 WX_NATIVE       SALE                        1734           1508     543778.5400


Elapsed Time:  0 hr, 0 min, 0 sec, 2 ms.

select 543778.54+1402067.53+1456969.30-167051.83-63055.30-121517.85+1230223.82
 4281414.21
 
select 20720+670306.82
  691026.82
  
[
    {
        "_id": "THB",
        "income": 97299
    },
    {
        "_id": "MYR",
        "income": 414549
    },
    {
        "_id": "MXN",
        "income": 691026.82
    },
    {
        "_id": "JPY",
        "income": 235278
    },
    {
        "_id": "ZAR",
        "income": 4531467.65
    },
    {
        "_id": "USD",
        "income": 1082353632.14
    },
    {
        "_id": "CNY",
        "income": 4281414.21
    },
    {
        "_id": "NZD",
        "income": 2149160.87
    },
    {
        "_id": "CAD",
        "income": 118633036
    },
    {
        "_id": "SGD",
        "income": 3738424.44
    },
    {
        "_id": "EUR",
        "income": 12928990.7
    },
    {
        "_id": "CHF",
        "income": 1017994.97
    },
    {
        "_id": "GBP",
        "income": 11833762.75
    },
    {
        "_id": "SEK",
        "income": 3961371.26
    },
    {
        "_id": "DKK",
        "income": 402821.23
    },
    {
        "_id": "NOK",
        "income": 780048.02
    },
    {
        "_id": "AUD",
        "income": 12803451.66
    },
    {
        "_id": "HKD",
        "income": 8455058.52
    }
]