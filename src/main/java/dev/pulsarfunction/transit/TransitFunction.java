package dev.pulsarfunction.transit;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.pulsarfunction.transit.models.Chat;
import dev.pulsarfunction.transit.models.Result;
import dev.pulsarfunction.transit.models.Transcom;
import dev.pulsarfunction.transit.models.Transit;
import org.apache.pulsar.client.impl.schema.JSONSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 *
 */
public class TransitFunction implements Function<byte[], Void> {

    /** PROCESS */
    @Override
    public Void process(byte[] input, Context context) {
        if ( input == null) {
            return null;
        }

        Collection<String> inputTopics = context.getInputTopics();

        System.out.println("Inputtopics:"+ inputTopics.toString());

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
        }

       TransitService transitService = new TransitService();

        Optional<String> topicName = null;
        try {
            if ( context != null  && context.getCurrentRecord() != null) {
                topicName = context.getCurrentRecord().getTopicName();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                context.getLogger().error(e.getLocalizedMessage());
            }
        }

        if ( result != null ) {
            if ( context != null && context.getLogger() != null) {
                context.getLogger().debug("TRANSIT:" + result.isTransit());
                context.getLogger().debug("Result:" + result.toString());
            }
        }

        try {
            if ( context != null && context.getTenant() != null  && result != null) {

                String theEventKey = UUID.randomUUID().toString();

                if ( result.isTransit() ) {
                    System.out.println("Sending Transit to " + result.getOutputTopic() + ":" +
                            result.getTransit().toString());
                    context.newOutputMessage(result.getOutputTopic(), JSONSchema.of(Transit.class))
                            .key(theEventKey)
                            .property("language", "Java")
                            .property("processor", "transit")
                            .value(result.getTransit())
                            .send();
                }
                else {
                    System.out.println("Sending Transcom to " + result.getOutputTopic() + ":" +
                            result.getTranscom().toString());
                    context.newOutputMessage(result.getOutputTopic(), JSONSchema.of(Transcom.class))
                            .key(theEventKey)
                            .property("language", "Java")
                            .property("processor", "transcom")
                            .value(result.getTranscom())
                            .send();
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
}