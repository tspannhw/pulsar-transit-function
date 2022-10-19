package dev.pulsarfunction.transit;

import dev.pulsarfunction.transit.TransitFunction;
import org.apache.pulsar.common.functions.FunctionConfig;
import org.apache.pulsar.common.io.SourceConfig;
import org.apache.pulsar.functions.LocalRunner;
import org.junit.Assert;
import org.junit.Test;
import org.apache.pulsar.functions.api.Context;

import java.util.Collections;

import static org.mockito.Mockito.mock;

public class TransitFunctionTest {

    public static String JSON_STRING = "{\"userInfo\":\"Tim Spann\",\"contactInfo\":\"Tim Spann, Developer Advocate @ StreamNative\",\"comment\":\"Apache Pulsar is the best ever\"}";

    protected Context ctx;

    protected void init(Context ctx) {
        this.ctx = ctx;
    }

    protected void log(String msg) {
        if (ctx != null && ctx.getLogger() != null) {
            ctx.getLogger().info(String.format("Function: [%s, id: %s, instanceId: %d of %d] %s",
                    ctx.getFunctionName(), ctx.getFunctionId(), ctx.getInstanceId(), ctx.getNumInstances(), msg));
        }
    }

    @Test
    public void testTransitFunction() {
        TransitFunction func = new TransitFunction();
        func.process(JSON_STRING.getBytes(), mock(Context.class));
    }

    /**
     * @param args
     * @throws Exception
     */
        public static void main(String[] args) throws Exception {

            FunctionConfig functionConfig = FunctionConfig.builder()
                    .className(TransitFunction.class.getName())
                    .inputs(Collections.singleton("persistent://public/default/transcom"))
                    .name("transitFunction")
                    .tenant("public")
                    .namespace("default")
                    .runtime(FunctionConfig.Runtime.JAVA)
                    .cleanupSubscription(true)
                    .build();

            LocalRunner localRunner = LocalRunner.builder()
                    .brokerServiceUrl("pulsar://pulsar1:6650")
                    .functionConfig(functionConfig)
                    .build();

            localRunner.start(false);

            Thread.sleep(30000);
            localRunner.stop();
            System.exit(0);
        }
}
