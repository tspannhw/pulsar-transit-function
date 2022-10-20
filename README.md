## pulsar-transit-function

## Developer Workspace

* Using JDK 8.
* Using Python 3.7.
* Using Mac OS Monterey 12.0.1
* Apache Pulsar 2.10.1

## setup

````

bin/pulsar-admin functions stop --name TransitParser --namespace default --tenant public
bin/pulsar-admin functions delete --name TransitParser --namespace default --tenant public
bin/pulsar-admin functions create --auto-ack true --jar /opt/demo/java/pulsar-transit-function/target/transit-1.0.jar --classname "dev.pulsarfunction.transit.TransitFunction" --dead-letter-topic "persistent://public/default/transitdead" --inputs "persistent://public/default/transcom,persistent://public/default/newjerseybus,persistent://public/default/newjerseylightrail,persistent://public/default/newjerseyrail" --log-topic "persistent://public/default/transitlog" --name TransitParser --namespace default --output "persistent://public/default/transitresult" --tenant public --max-message-retries 5


````

## references

* https://github.com/tspannhw/FLiP-Transit
* https://github.com/tspannhw/SmartTransit
