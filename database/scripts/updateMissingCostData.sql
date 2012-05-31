insert into costdata(gatewayid, timestamp, meterReadDay, meterReadMonth, meterReadYear, fixedCost, minCost)
select distinct gatewayId, timestamp, 1, 5, 2012,0,0
from energydata
where not exists (select * from costdata where costdata.gatewayId = energydata.gatewayId and costdata.timestamp = energydata.timestamp)
 





