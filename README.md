#### pulsar-transit-function

#### Developer Workspace

* JDK 8 and 17
* Python 3.7, 3.9 and 3.10
* Mac OS Monterey 12.0.1
* Ubuntu
* Apache Pulsar 2.10.1
* Apache Maven
* SDKMan

#### Deploy Function (see script)

````

bin/pulsar-admin functions stop --name TransitParser --namespace default --tenant public
bin/pulsar-admin functions delete --name TransitParser --namespace default --tenant public
bin/pulsar-admin functions create --auto-ack true --jar /opt/demo/java/pulsar-transit-function/target/transit-1.0.jar --classname "dev.pulsarfunction.transit.TransitFunction" --dead-letter-topic "persistent://public/default/transitdead" --inputs "persistent://public/default/transcom,persistent://public/default/newjerseybus,persistent://public/default/newjerseylightrail,persistent://public/default/newjerseyrail" --log-topic "persistent://public/default/transitlog" --name TransitParser --namespace default --output "persistent://public/default/transitresult" --tenant public --max-message-retries 5


````


#### Status

````

````

#### Flink SQL

````
CREATE CATALOG pulsar WITH (
   'type' = 'pulsar',
   'service-url' = 'pulsar://pulsar1:6650',
   'admin-url' = 'http://pulsar1:8080',
   'format' = 'json'
);

USE CATALOG pulsar;

set table.dynamic-table-options.enabled = true;

SHOW TABLES;

describe `transcom-clean`;
+-------------+--------+------+-----+--------+-----------+
|        name |   type | null | key | extras | watermark |
+-------------+--------+------+-----+--------+-----------+
| description | STRING | true |     |        |           |
|    latitude | STRING | true |     |        |           |
|   longitude | STRING | true |     |        |           |
|       point | STRING | true |     |        |           |
|     pubDate | STRING | true |     |        |           |
|       title | STRING | true |     |        |           |
|          ts | STRING | true |     |        |           |
|        uuid | STRING | true |     |        |           |
+-------------+--------+------+-----+--------+-----------+
8 rows in set

describe `newjerseybus-clean`;
+---------------+--------+------+-----+--------+-----------+
|          name |   type | null | key | extras | watermark |
+---------------+--------+------+-----+--------+-----------+
| advisoryAlert | STRING | true |     |        |           |
|   companyname | STRING | true |     |        |           |
|   description | STRING | true |     |        |           |
|          guid | STRING | true |     |        |           |
|          link | STRING | true |     |        |           |
|       pubDate | STRING | true |     |        |           |
|   servicename | STRING | true |     |        |           |
|         title | STRING | true |     |        |           |
|            ts | STRING | true |     |        |           |
|          uuid | STRING | true |     |        |           |
+---------------+--------+------+-----+--------+-----------+
10 rows in set

desc `newjerseylightrail-clean`;
+---------------+--------+------+-----+--------+-----------+
|          name |   type | null | key | extras | watermark |
+---------------+--------+------+-----+--------+-----------+
| advisoryAlert | STRING | true |     |        |           |
|   companyname | STRING | true |     |        |           |
|   description | STRING | true |     |        |           |
|          guid | STRING | true |     |        |           |
|          link | STRING | true |     |        |           |
|       pubDate | STRING | true |     |        |           |
|   servicename | STRING | true |     |        |           |
|         title | STRING | true |     |        |           |
|            ts | STRING | true |     |        |           |
|          uuid | STRING | true |     |        |           |
+---------------+--------+------+-----+--------+-----------+
10 rows in set

desc `newjerseyrail-clean`;
+---------------+--------+------+-----+--------+-----------+
|          name |   type | null | key | extras | watermark |
+---------------+--------+------+-----+--------+-----------+
| advisoryAlert | STRING | true |     |        |           |
|   companyname | STRING | true |     |        |           |
|   description | STRING | true |     |        |           |
|          guid | STRING | true |     |        |           |
|          link | STRING | true |     |        |           |
|       pubDate | STRING | true |     |        |           |
|   servicename | STRING | true |     |        |           |
|         title | STRING | true |     |        |           |
|            ts | STRING | true |     |        |           |
|          uuid | STRING | true |     |        |           |
+---------------+--------+------+-----+--------+-----------+
10 rows in set

select description, pubDate, title, ts from `newjerseyrail-clean` /*+ OPTIONS('scan.startup.mode' = 'earliest') */ ;

select description, pubDate, title, latitude, longitude, ts from `transcom-clean` /*+ OPTIONS('scan.startup.mode' = 'earliest') */ ;

select *
FROM  `newjerseyrail-clean` LEFT JOIN `transcom-clean`
ON aircraft.lat = aircraftweather.latitude
and aircraft.lon = aircraftweather.longitude;

select description, pubDate, title, ts, servicename
FROM  `newjerseyrail-clean`
UNION
select description, pubDate, title, ts, servicename
FROM  `newjerseylightrail-clean`
UNION
select description, pubDate, title, ts, servicename
FROM  `newjerseybus-clean`;


select servicename, description, title, pubDate, ts
FROM  `newjerseyrail-clean` /*+ OPTIONS('scan.startup.mode' = 'earliest') */
UNION
select servicename, description, title,pubDate, ts
FROM  `newjerseylightrail-clean` /*+ OPTIONS('scan.startup.mode' = 'earliest') */
UNION
select servicename, description, title,pubDate,  ts
FROM  `newjerseybus-clean` /*+ OPTIONS('scan.startup.mode' = 'earliest') */
UNION
SELECT 'transcom' as servicename, description, title, pubDate,  ts
FROM `transcom-clean` /*+ OPTIONS('scan.startup.mode' = 'earliest') */;


````

#### References

* https://github.com/tspannhw/FLiP-Transit
* https://github.com/tspannhw/SmartTransit
