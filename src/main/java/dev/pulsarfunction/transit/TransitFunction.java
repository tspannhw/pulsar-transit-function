package dev.pulsarfunction.transit;

import dev.pulsarfunction.transit.models.Result;
import dev.pulsarfunction.transit.models.Transcom;
import dev.pulsarfunction.transit.models.Transit;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.impl.schema.JSONSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Transit Serverless Application
 */
public class TransitFunction implements Function<byte[], Void> {

    private static final String TRANSIT_TOPIC = "persistent://public/default/transit";

    /** PROCESS */
    @Override
    public Void process(byte[] input, Context context) {
        if ( input == null) {
            return null;
        }

        String outputTopic = "persistent://public/default/transitresult";

        if (context != null && context.getLogger() != null && context.getLogger().isDebugEnabled()) {
            context.getLogger().debug("LOG:" + input.toString());
            System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
            context.getLogger().debug("Available processors (cores): " +
                    Runtime.getRuntime().availableProcessors());

            /* Total amount of free memory available to the JVM */
            context.getLogger().debug("Free memory (bytes): " +
                    Runtime.getRuntime().freeMemory());

            /* This will return Long.MAX_VALUE if there is no preset limit */
            long maxMemory = Runtime.getRuntime().maxMemory();

            /* Maximum amount of memory the JVM will attempt to use */
            context.getLogger().debug("Maximum memory (bytes): " +
                    (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));

            /* Total memory currently available to the JVM */
            context.getLogger().debug("Total memory available to JVM (bytes): " +
                    Runtime.getRuntime().totalMemory());

            context.getLogger().debug("Function: " + context.getFunctionName() +
                    "," + context.getOutputSchemaType().toString() );
        }

       TransitService transitService = new TransitService();
        Optional<String> topicName = null;
        try {
            if ( context != null  && context.getCurrentRecord() != null) {
                topicName = context.getCurrentRecord().getTopicName();
            }
        } catch (Exception e) {
            if ( context != null && context.getLogger() != null) {
                context.getLogger().error("Can't get topic", e);
            }
        }

        if (context != null && context.getLogger() != null && context.getLogger().isDebugEnabled()) {
            context.getLogger().debug("LOG: Input: " + new String(input));
            context.getLogger().debug("LOG: Topic:" + topicName);
        }

        Result result = null;
        try {
            result = transitService.deserialize(input, topicName);
        } catch (Throwable e) {
            if ( context != null && context.getLogger() != null) {
                context.getLogger().error("Can't deserialize", e);
            }
        }

        try {
            if ( context != null && context.getTenant() != null  && result != null) {
                String theEventKey = UUID.randomUUID().toString();

                if ( result.isTransit() ) {
                    MessageId sendResult = context.newOutputMessage(result.getOutputTopic(), JSONSchema.of(Transit.class))
                                .key(theEventKey)
                                .property("language", "Java")
                                .property("processor", "transit")
                                .value(result.getTransit())
                                .send();

                    if ( context.getLogger() != null  && context.getLogger().isDebugEnabled() ) {
                        context.getLogger().debug("Transit Sent: "  + sendResult.toString() +
                                " value: " + result.getTransit().getPubDate() +
                                "," + result.getTransit().getServicename() + " to " +
                                result.getOutputTopic());
                    }

                    sendToTransit(result.getTransit(), context);
                }
                else {
                    NLPService nlpService = new NLPService();
                    String location = nlpService.getNER(result.getTranscom().getDescription());
                    if ( location == null || location.trim().length() <= 0) {
                        location = "";
                    }

                    MessageId sendResult = context.newOutputMessage(result.getOutputTopic(), JSONSchema.of(Transcom.class))
                            .key(theEventKey)
                            .property("language", "Java")
                            .property("processor", "transcom")
                            .property("location", location)
                            .property("latitude", result.getTranscom().getLatitude())
                            .property("longitude", result.getTranscom().getLongitude())
                            .value(result.getTranscom())
                            .send();
                    if ( context.getLogger() != null  && context.getLogger().isDebugEnabled() ) {
                        context.getLogger().debug("Transcom Sent: "  + sendResult.toString() +
                                " value: " + result.getTransit().getPubDate() + " to " +
                                result.getOutputTopic());
                    }
                    sendToTransit( transitService.combiner(result.getTranscom()), context);
                }

            }
            else {
                if ( result != null) {
                    System.out.println("Null context, assuming local test run. " + result.toString());
                }
            }
        } catch (Throwable e) {
            if (context != null  && context.getLogger() != null) {
                context.getLogger().error("ERROR:" + e.getLocalizedMessage());
            }
            else {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * send everything to transit
     * @param transit
     * @param context
     */
    private void sendToTransit(Transit transit, Context context) {
        try {
            if ( transit == null ) {
                return;
            }
            NLPService nlpService = new NLPService();
            String location = nlpService.getNER(transit.getDescription());
            if ( location == null || location.trim().length() <= 0) {
                location = "";
            }
            transit.setGuid(location);

            MessageId sendResult = context.newOutputMessage(TRANSIT_TOPIC, JSONSchema.of(Transit.class))
                    .key(transit.getUuid())
                    .property("language", "Java")
                    .property("processor", "transit")
                    .property( "location", "")
                    .value(transit)
                    .send();

            if ( context.getLogger() != null  && context.getLogger().isDebugEnabled() ) {
                context.getLogger().debug("Transit: "  + sendResult.toString() +
                        " value: " + transit.getPubDate() );
            }
        } catch (PulsarClientException e) {
            throw new RuntimeException(e);
        }
    }
}