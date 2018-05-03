select count(1) from htmdata
where link like 'read.php?tid=%'

824920

select fid, count(1) from htmdata
group by fid
order by count(1) desc

select max(id) from htmdata
where link like 'read.php?tid=%'
group by link


delete from htmdata
where
where id not in
(
select max(id) from htmdata
where link like 'read.php?tid=%'
group by link
order by max(id)
)

select min(id) from htmdata

select * from htmdata
where title like '%波多野结衣%'

select count(1) from htmdata
where data like '%波多野结衣%'

where data like '%波多野结衣%'

where length(title) > 228

where length(data) > 19990

limit 100

select max(length(link)) from htmdata

select max(length(title)) from htmdata

select max(length(data)) from htmdata

select * from htmdata limit 100;

select * from htmdata
where data_length > 209999

select count(distinct(data_length)) from htmdata

select max(id), min(id) from htmdata
