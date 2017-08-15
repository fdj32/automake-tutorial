
select top 10 sum(cct_order_amount) as amount_total, cct_mai_id, cct_mai_instance_id from cc_transaction
where cct_result=0
and cct_date_created > '2017-01-01'
and (cct_transaction_type = 'SALE' OR cct_transaction_type = 'AUTH')
and cct_date_created < '2017-02-01'
group by cct_mai_id,cct_mai_instance_id
order by amount_total desc

select top 10 sum(cct_order_amount) as amount_total, cct_mai_id from cc_transaction
where cct_result=0
and cct_date_created > '2017-01-01'
and (cct_transaction_type = 'SALE' OR cct_transaction_type = 'AUTH')
and cct_date_created < '2017-02-01'
group by cct_mai_id
order by amount_total desc

select top 10 sum(cct_order_amount) as amount_total, cct_mai_id, cct_mai_instance_id from cc_transaction
where cct_result=0
and cct_date_created > '2017-01-01'
and cct_transaction_type = 'CREDIT'
and cct_date_created < '2017-02-01'
group by cct_mai_id, cct_mai_instance_id
order by amount_total desc


select top 10 ma.ma_name, sum(cct.cct_order_amount) as amount_total from cc_transaction cct
inner join merchant_accounts ma on (cct.cct_mai_id=ma.ma_id and cct.cct_mai_instance_id=ma.instance_id)
where cct.cct_result=0
and cct.cct_date_created > '2017-01-01'
and cct.cct_transaction_type = 'CREDIT'
and cct.cct_date_created < '2017-02-01'
group by ma.ma_name
order by amount_total desc


select ma.ma_name as ma_name, count(1) as total, 
count(case when cct.cct_result = 0 then 1 else NULL end) as approve 
from cc_transaction cct
inner join merchant_accounts ma on (cct.cct_mai_id=ma.ma_id and cct.cct_mai_instance_id=ma.instance_id)
where cct.cct_date_created > '2017-01-01'
and cct.cct_date_created < '2017-02-01'
group by ma.ma_name
order by total desc

select ma.ma_name as ma_name, count(1) as total, 
count(case when cct.cct_result = 0 then 1 else NULL end) as approve,
convert(decimal(5,2), sum(case when cct.cct_result = 0 then 1 else 0 end) * 100.00/ count(*)) as rate
from cc_transaction cct
inner join merchant_accounts ma on (cct.cct_mai_id=ma.ma_id and cct.cct_mai_instance_id=ma.instance_id)
where cct.cct_date_created > '2017-01-01'
and cct.cct_date_created < '2017-02-01'
group by ma.ma_name
order by rate desc




SELECT TOP 100 ma_name,approve,total, approve*1.0/total AS rate FROM (
	select ma.ma_name, count(1) as total, 
	count(case when cct.cct_result = 0 then 1 else NULL end) as approve
	from cc_transaction cct
	inner join merchant_accounts ma on (cct.cct_mai_id=ma.ma_id and cct.cct_mai_instance_id=ma.instance_id)
	where cct.cct_date_created > '2017-01-01'
	and cct.cct_date_created < '2017-02-01'
	group by ma.ma_name
) merchant ORDER BY rate ASC 






select count(1) as total, cct.cct_merchant_descriptor from cc_transaction cct
where cct.cct_date_created > '2017-01-01'
and cct.cct_date_created < '2017-02-01'
group by cct.cct_merchant_descriptor



select cct.cct_mai_processor, ma.ma_name, cct.cct_result, cct.ccg_result, count(cct.cct_id) as t_count from cc_transaction cct
inner join merchant_accounts ma on (cct.cct_mai_id=ma.ma_id and cct.cct_mai_instance_id=ma.instance_id)
where cct.cct_date_created > '2017-05-01'
and cct.cct_date_created < '2017-06-01'
group by cct.cct_mai_processor, ma.ma_name, cct.cct_result, cct.ccg_result



select ma.ma_name, cct.ccg_result, count(cct.cct_id) as t_count from cc_transaction cct
inner join merchant_accounts ma on (cct.cct_mai_id=ma.ma_id and cct.cct_mai_instance_id=ma.instance_id)
where cct.cct_date_created > '2017-05-01'
and cct.cct_date_created < '2017-06-01'
and cct.cct_mai_processor='paymentech'
and cct.cct_result<>0
group by ma.ma_name, cct.ccg_result



select cct.ccg_result, count(cct.cct_id) as t_count from cc_transaction cct
where cct.cct_date_created > '2017-05-01'
and cct.cct_date_created < '2017-06-01'
and cct.cct_mai_processor='paymentech'
and cct.cct_result<>0
group by cct.ccg_result
order by ccg_result







select count(distinct(cct.cct_mai_id)) from cc_transaction cct
where cct.cct_date_created > '2016-01-01'

 
 ----
 3643

 select count(distinct(cct.cct_mai_user)) from cc_transaction cct
where cct.cct_date_created > '2016-01-01'
 ----
 2212

 
 select count(distinct(cct.cct_mai_id)) from cc_transaction cct
where cct.cct_date_created > '2016-01-01'
and cct.cct_mai_id not in
(select ma_id from merchant_accounts)
 
 
select count(*) from merchant_accounts
where ma_name like 'ROL%'
  
 -----
 10893
 
 select count(*) from merchant_accounts
where ma_name like 'Recnet%'
  
 ----
 2365


   select count(*) from merchant_accounts
where ma_name like 'H&F%'
  
 --
 61
 
    select count(*) from merchant_accounts
where ma_name like 'Thriva%'
  
 --
 82
 
select * from merchant_accounts
where ma_name not like 'ROL%'
and ma_name not like 'Active %'
and ma_name not like 'Active.com%'
and ma_name not like 'ActiveAdvantage%'
and ma_name not like 'ActiveAdvantage%'
and ma_name not like 'activeleague%'
and ma_name not like 'ActiveNet%'
and ma_name not like 'ActiveTrainer%'
and ma_name not like 'ActiveWorks%'
and ma_name not like 'AMS%'
and ma_name not like 'AN%'
and ma_name not like 'AW%'
and ma_name not like 'Bolder%'
and ma_name not like 'ClassHPS%'
and ma_name not like 'Clubspace%'
and ma_name not like 'Conf%'
and ma_name not like 'cs%'
and ma_name not like 'DoIt%'
and ma_name not like 'endurance%'
and ma_name not like 'Europe Events%'
and ma_name not like 'Hybrid%'
and ma_name not like 'H&F%'
and ma_name not like 'League%'
and ma_name not like 'NJB%'
and ma_name not like 'Recnet%'
and ma_name not like 'Recent%'
and ma_name not like 'Recware%'
and ma_name not like 'RegCenter%'
and ma_name not like 'RegOnline%'
and ma_name not like 'SalesFource%'
and ma_name not like 'test%'
and ma_name not like 'test%'
and ma_name not like 'Thriva%'
and ma_name not like 'USTA%'
and ma_name not like 'USTA%'
and ma_name not like 'WannaDo%'
and ma_name not like 'WGW%'
and ma_name not like 'Workday%'
and ma_username not like '%disabled'
order by ma_name
 
 
 select top 10 * from cc_transaction
 where cct_mai_id in (
 select ma_id from merchant_accounts
 where ma_username like '%disabled'
 )

select count(1) from merchant_accounts




