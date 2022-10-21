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

#### Presto / Trino / Pulsar SQL

````

bin/pulsar sql
use pulsar."public/default";

presto:public/default> show tables;
                Table
-------------------------------------

presto:public/default> describe transit;
      Column       |   Type    | Extra |                                   Comment
-------------------+-----------+-------+------------------------------------------------------------------
 advisoryalert     | varchar   |       | ["null","string"]
 companyname       | varchar   |       | ["null","string"]
 description       | varchar   |       | ["null","string"]
 guid              | varchar   |       | ["null","string"]
 link              | varchar   |       | ["null","string"]
 pubdate           | varchar   |       | ["null","string"]
 servicename       | varchar   |       | ["null","string"]
 title             | varchar   |       | ["null","string"]
 ts                | varchar   |       | ["null","string"]
 uuid              | varchar   |       | ["null","string"]
 __partition__     | integer   |       | The partition number which the message belongs to
 __event_time__    | timestamp |       | Application defined timestamp in milliseconds of when the event o
 __publish_time__  | timestamp |       | The timestamp in milliseconds of when event as published
 __message_id__    | varchar   |       | The message ID of the message used to generate this row
 __sequence_id__   | bigint    |       | The sequence ID of the message used to generate this row
 __producer_name__ | varchar   |       | The name of the producer that publish the message used to generat
 __key__           | varchar   |       | The partition key for the topic
 __properties__    | varchar   |       | User defined properties
(18 rows)

presto:public/default> select * from transit;

presto:public/default> select __publish_time__, __key__, __producer_name__, servicename, description, title, ts, uuid, pubdate, link, guid from transit;
    __publish_time__     |               __key__                | __producer_name__ | servicename |
-------------------------+--------------------------------------+-------------------+-------------+-------
 2022-10-21 12:01:17.294 | ec632b86-d81b-4f97-96a3-86237f3e94f1 | standalone-2-444  | transcom    | NJ PAC
 2022-10-21 12:01:17.304 | eacbf7da-9a24-4a45-960a-bd1d065c03d9 | standalone-2-444  | transcom    | Barcla
 2022-10-21 12:01:17.314 | 73b5f73d-39c9-4c0b-ab49-e296465a09bd | standalone-2-444  | transcom    | NJ DOT
 2022-10-21 12:01:17.324 | 28e788e3-4722-4d11-9d04-7ce178c99cf3 | standalone-2-444  | transcom    | CT DOT
 2022-10-21 12:01:17.333 | f06c0b9a-29b0-4ab3-bda2-759539426089 | standalone-2-444  | transcom    | Barcla
 2022-10-21 12:01:17.345 | 7420ef47-b1ce-4495-bde9-d61b9e150148 | standalone-2-444  | transcom    | Barcla
 2022-10-21 12:01:17.354 | d3782d30-ce93-424f-9cf8-e9c858a594d1 | standalone-2-444  | transcom    | Barcla
 2022-10-21 12:01:17.364 | 4875b8dd-49b1-44b8-ae6c-4b392fd3f3bc | standalone-2-444  | transcom    | Barcla
 2022-10-21 12:01:17.374 | 2390bdd6-f858-4f7d-b86e-9bb99d0ab808 | standalone-2-444  | transcom    | NYSDOT
 2022-10-21 12:01:17.384 | cf7a733d-bb47-41db-9734-9578d1758011 | standalone-2-444  | transcom    | NJ PAC
 2022-10-21 12:01:17.395 | 42fc9792-35d6-4023-8de8-c276419d939e | standalone-2-444  | transcom    | NYSDOT
 2022-10-21 12:01:17.404 | c544bbb9-17bf-4595-bc95-72a2ed682286 | standalone-2-444  | transcom    | NJ Tur



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


describe `transit`;

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

select servicename, description, title, pubDate, ts, uuid, guid, link
from transit /*+ OPTIONS('scan.startup.mode' = 'earliest') */;

````

#### References

* https://github.com/tspannhw/FLiP-Transit
* https://github.com/tspannhw/SmartTransit


#### Future Transportation Feeds

* https://transport.rest/
* https://developer.here.com/documentation/public-transit/dev_guide/index.html
* https://new.mta.info/developers
* https://developers.google.com/transit/gtfs-realtime/
* https://gtfs.org/realtime/language-bindings/python/
* http://bt.mta.info/wiki/Developers/SIRIIntro
* https://www.mbta.com/developers/gtfs-realtime
* https://developers.google.com/transit/gtfs-realtime/examples/java-sample
* https://data.cityofnewyork.us/
* https://dev.socrata.com/foundry/data.cityofnewyork.us/i4gi-tjb9
* https://www3.septa.org/#/Real%20Time%20Data/NoMessageAlert
* https://github.com/septadev/GTFS/releases