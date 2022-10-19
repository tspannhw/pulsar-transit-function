## pulsar-transit-function

## Developer Workspace

* Using JDK 8.
* Using Python 3.7.
* Using Mac OS Monterey 12.0.1
* Apache Pulsar 2.10.1

## setup

````

bin/pulsar-admin topics create persistent://public/default/transcom

bin/pulsar-admin topics create persistent://public/default/chatresult

bin/pulsar-admin functions delete --name SentimentAnalysis --namespace default --tenant public

bin/pulsar-admin functions create --auto-ack true --jar sentiment-1.0.jar --classname "dev.pulsarfunction.sentiment.SentimentFunction" --dead-letter-topic "persistent://public/default/chatdead" --inputs "persistent://public/default/chat" --log-topic "persistent://public/default/sentimentlog" --name SentimentAnalysis --namespace default  --tenant public --max-message-retries 5

bin/pulsar-client consume "persistent://public/default/chatresult" -s "fnchatresultreader" -n 0

bin/pulsar-client consume "persistent://public/default/chat" -s "fnchatreader" -n 0


````

## references

