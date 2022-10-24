#### pulsar-transit-function

#### Developer Workspace

* JDK 8 and 17
* Python 3.7, 3.9 and 3.10
* Mac OS Monterey 12.0.1
* Ubuntu
* Apache Pulsar 2.10.1
* Java Pulsar Function
* Apache Maven
* SDKMan


#### Consume Raw NJ Transit and TRANSCOM Feeds

* Apache NiFi (https://github.com/tspannhw/FLiP-Transit)

https://www.datainmotion.dev/2021/01/flank-real-time-transit-information-for.html


![FLOW](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/transitflow.jpg)


![Transcomoverview](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/transcomoverview.jpg)

![TRANSCOM](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/transcomcallflow.jpg)


![NiFi0](https://1.bp.blogspot.com/-UEryo6V5aBQ/X_n0JdLIihI/AAAAAAAAcSY/fAEro2oxQ3sdJoPzTHPXgeZVJZb7rSP_ACLcBGAsYHQ/w640-h370/flow1.png)

![NiFi1](https://1.bp.blogspot.com/-A5Otxw_i_So/X_n0JeXvd_I/AAAAAAAAcSU/t-_17LUmq-wmF4IGNSUnLmqRkV_dYaOHwCLcBGAsYHQ/w560-h819/flow2.png)

![NiFi2](https://1.bp.blogspot.com/-q6fqFBPLVkc/X_n0JYzTRLI/AAAAAAAAcSc/CctjPWBS94Ydh9gBUaCOyn-jHN0gO1ksQCLcBGAsYHQ/w640-h294/flow3.png)


![NiFi3](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/NIFItoPulsar.jpg)

![ROUTE](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/nifiroute.jpg)

![REST](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/nifitransitrest.jpg)

![TransComToPulsar](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/transcomtopulsar.jpg)


![Add Fields](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/transitaddfields.jpg)


![Data](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/transitdata.jpg)



![NiFiToPulsar](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/NIFItoPulsar.jpg)

![Fields](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/transcomaddfields.jpg)




#### Pulsar Manager


![NiFi2](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/pulsmantransittopic.jpg)

![NiFi2](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/transittopicdetails.jpg)



#### Consume Messages

````

bin/pulsar-client consume "persistent://public/default/transit" -s ts-reader -n 0  --subscription-type "Shared" --subscription-position "Earliest" --subscription-mode "Durable" --schema-type "auto_consume"


----- got message -----
key:[e5b94097-ac5a-4378-95a9-a8479e0a0cd2], properties:[language=Java, processor=transit], content:{companyname=transcom, advisoryAlert=NULL, link=https://www.511nj.org/home, description=TRANSCOM, Jersey City: hockey game on UBS Arena at (Hempstead) New York Islanders vs. Seattle Kraken, Tuesday February 7th, 2023, 07:30 PM thru 10:30 PM, guid=40.711673,-73.726249, servicename=transcom, title=UBS Arena :hockey game, pubDate=2022-08-16T10:42:45, uuid=e5b94097-ac5a-4378-95a9-a8479e0a0cd2, ts=1666291837242}
----- got message -----
key:[b778a1de-519a-4f60-bd34-c1b212784971], properties:[language=Java, processor=transit], content:{companyname=transcom, advisoryAlert=NULL, link=https://www.511nj.org/home, description=NYSDOT - Region 3: Construction , bridge work on NY 5 eastbound between I-481 SB Ramp (De Witt) and I-481 NB Ramp (De Witt) 1 Right lane of 2 lanes closed until 3:00 PM, guid=43.034178,-76.062942, servicename=transcom, title=NY 5 eastbound:Construction, pubDate=2022-10-20T09:00:06, uuid=b778a1de-519a-4f60-bd34-c1b212784971, ts=1666291837244}

````


#### Deploy Function (see script) 

````

bin/pulsar-admin functions stop --name TransitParser --namespace default --tenant public

bin/pulsar-admin functions delete --name TransitParser --namespace default --tenant public

bin/pulsar-admin functions create --auto-ack true --jar /opt/demo/java/pulsar-transit-function/target/transit-1.0.jar --classname "dev.pulsarfunction.transit.TransitFunction" --dead-letter-topic "persistent://public/default/transitdead" --inputs "persistent://public/default/transcom,persistent://public/default/newjerseybus,persistent://public/default/newjerseylightrail,persistent://public/default/newjerseyrail" --log-topic "persistent://public/default/transitlog" --name TransitParser --namespace default --output "persistent://public/default/transitresult" --tenant public --max-message-retries 5


````

#### Status

````
bin/pulsar-admin functions status --name TransitParser

{
  "numInstances" : 1,
  "numRunning" : 1,
  "instances" : [ {
    "instanceId" : 0,
    "status" : {
      "running" : true,
      "error" : "",
      "numRestarts" : 0,
      "numReceived" : 1421,
      "numSuccessfullyProcessed" : 1421,
      "numUserExceptions" : 0,
      "latestUserExceptions" : [ ],
      "numSystemExceptions" : 0,
      "latestSystemExceptions" : [ ],
      "averageLatency" : 13.630545629838124,
      "lastInvocationTime" : 1666388452170,
      "workerId" : "c-standalone-fw-127.0.0.1-8080"
    }
  } ]
}
r

````

#### Presto / Trino / Pulsar SQL

````

bin/pulsar sql
use pulsar."public/default";

presto:public/default> show tables;
                Table
-------------------------------------
 transcom
 transcom-clean
 transit
 transitlog


describe "transcom-clean"
                    -> ;
      Column       |   Type    | Extra |                                   Comment
-------------------+-----------+-------+------------------------------------------------------------------
 description       | varchar   |       | ["null","string"]
 latitude          | varchar   |       | ["null","string"]
 longitude         | varchar   |       | ["null","string"]
 point             | varchar   |       | ["null","string"]
 pubdate           | varchar   |       | ["null","string"]
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



describe "newjerseybus-clean";
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

describe "newjerseyrail-clean";
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

describe "newjerseylightrail-clean";
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

![PulsarSQLRun](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/pulsarsqllastrun.jpg)

![PulsarSQLRun2](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/pulsarsqlclusteroverview.jpg)

![PulsarSQLRun3](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/pulsarsql1.jpg)

![PulsarSQLRun4](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/pulsarsql3.jpg)

![PulsarSQLRun5](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/pulsarsqlquery.jpg)

![PulsarSQLRun6](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/transcompulsarsql.jpg)

![DBeaver](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/pulsarsqlddldbeaver.jpg)

![SQLPLAN](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/pulsarsqlplan.jpg)

![transit](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/pulsarsqltransitdescr.jpg)


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


![FLINK](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/transcomflinkrow.jpg)

![FLINK](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/flinksqllightrail.jpg)

![FLINK](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/flinksqlpages.jpg)

![FLINK](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/flinksqlrail.jpg)

![FLINK](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/flinksqltransit.jpg)


#### HTML + JQuery + DataTables + Websockets -> Pulsar Toics

![HTML1](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/transitandweatherhtml.jpg)

![HTML2](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/transithtml1.jpg)

![HTML3](https://github.com/tspannhw/pulsar-transit-function/blob/main/images/transithtml3.jpg)


#### TRANSCOM Data Notice

````

TRANSCOM is a coalition of 16 transportation and public safety agencies
 in the New York - New Jersey - Connecticut metropolitan region. 
It was created in 1986 to provide a cooperative, coordinated approach to 
regional transportation management.This is a free service that allows 
various user groups (i.e. the general public, commercial vendors, 
transportation agencies, researchers, media and others) to access 
TRANSCOM real-time event and link (travel time) data for use in their 
applications. The data feed contains 'real-time' event information 
provided by member agencies of 
TRANSCOM listed below:

TRANSCOM Member Agencies
Connecticut Department of TransportationMetropolitan Transportation AuthorityMTA – Bridges and TunnelsMTA – New York City TransitNew Jersey Department of TransportationNew Jersey TransitNew York City Department of TransportationNew York City Police DepartmentNew York State Bridge AuthorityNew York State Department of TransportationNew York State PoliceNew York State Thruway AuthorityPort Authority Trans-Hudson Corp (PATH)The Port Authority of New York and New Jersey
Register for Access
For full access to the documentation and data please register.After your registration has been processed you will have access to the full sites content!For organizations, we request that you only request a single account for all users, and share this information internally. If there are valid reasons for multiple accounts, please explain the circumstances in the comments section of the registration form. Please ensure you have included the domain data.xcmdata.org on any SPAM filters exceptions so you will receive the confirmations and any announcements. Duplicate requests will be ignored - if you have questions use the link below!To register for accessClick here


https://data.xcmdata.org/DEWeb/Pages/index

````

#### Apply NLP / ML / Named Entity Recognition to Text

* Added OpenNLP 2.0.0
* For Transit topic records, added the results of OpenNLP 2.0's Named Entity Resolution NLP for Locations using the English 1.5.0 model.
* Future thoughts on ONNX and Pytorch via OpenNLP DL

````

all Transit Sent: 231603:1746:0:0 value: Oct 24, 2022 10:43:43 AM service:bus guid: Plateau Avenue, Central Avenue, Fort Lee, Edgewater Road, Palisades Avenue, Main Street, Lemoine Avenue/Palisades Avenue, Edgewater Road, Anderson Avenue 


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
* https://github.com/tspannhw/pulsar-transit-function/blob/main/sources.md