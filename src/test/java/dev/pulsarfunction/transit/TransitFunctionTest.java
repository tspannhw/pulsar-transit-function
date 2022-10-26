package dev.pulsarfunction.transit;

import dev.pulsarfunction.transit.models.Transcom;
import dev.pulsarfunction.transit.models.Transit;
import org.apache.pulsar.common.functions.FunctionConfig;
import org.apache.pulsar.functions.LocalRunner;
import org.apache.pulsar.functions.api.Context;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class TransitFunctionTest {

    public static String JSON_STRING = "{\"title\" : \"Michie Stadium :football game\", \"description\" : \"TRANSCOM, Jersey City: football game on Michie Stadium at (Highlands) Air Force Vs Army, Saturday November 5th, 2022, 11:30 AM thru 02:30 PM\", \"pubDate\" : \"2022-08-04T11:19:31\",\"point\" : \"41.3873058180975,-73.9640593528748\", \"latitude\" : \"41.3873058180975\", \"ts\" : \"1666042376634\", \"longitude\" : \"-73.9640593528748\", \"uuid\" : \"62c1ea31-313a-4e76-a6e1-3228e7159111\"}\n";

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

    /**
     *  *  * {
     *  *  *   "title" : "Michie Stadium :football game",
     *  *  *   "description" : "TRANSCOM, Jersey City: football game on Michie Stadium at (Highlands) Air Force Vs Army, Saturday November 5th, 2022, 11:30 AM thru 02:30 PM",
     *  *  *   "pubDate" : "2022-08-04T11:19:31",
     *  *  *   "point" : "41.3873058180975,-73.9640593528748",
     *  *  *   "latitude" : "41.3873058180975",
     *  *  *   "ts" : "1666042376634",
     *  *  *   "longitude" : "-73.9640593528748",
     *  *  *   "uuid" : "62c1ea31-313a-4e76-a6e1-3228e7159111"
     *  *  * }
     */
    @Test
    public void testTransitServiceCombiner() {
        TransitService serv = new TransitService();
        Transcom transcom = new Transcom();
        transcom.setTitle("Michie Stadium :football game");
        transcom.setPubDate("2022-08-04T11:19:31");
        transcom.setDescription("TRANSCOM, Jersey City: football game on Michie Stadium at (Highlands) Air Force Vs Army");
        transcom.setLatitude("41.3873058180975");
        transcom.setLongitude("-73.9640593528748");
        transcom.setPoint("41.3873058180975,-73.9640593528748");
        transcom.setUuid("62c1ea31-313a-4e76-a6e1-3228e7159111");
        transcom.setTs("1666042376634");
        Transit transit = serv.combiner(transcom);

        assertNotNull(transit);
        assertEquals(transit.getServicename(),"transcom");
        assertEquals(transit.getCompanyname(),"transcom");
        assertEquals(transcom.getPubDate(), transit.getPubDate());
        assertEquals(transcom.getTitle(), transit.getTitle());
        //System.out.println("transit: " + transit.toString());
    }

    @Test
    public void testNL() {
        NLPService nlp = new NLPService();
        assertNotNull( nlp.getNER("Tim Spann TRANSCOM, Jersey City in New Jersey, USA: football game on Michie Stadium at (Highlands) Air Force Vs Army on Route 100"));
    }

    @Test
    public void testTransitFunction() {
        TransitFunction func = new TransitFunction();
        func.process(JSON_STRING.getBytes(StandardCharsets.UTF_8), mock(Context.class));
    }

    /**
     * @param args
     * @throws Exception
     */
        public static void main(String[] args) throws Exception {
            Collection<String> topics = new ArrayList<String>();
            topics.add("persistent://public/default/transcom");
            topics.add("persistent://public/default/newjerseybus");
            topics.add("persistent://public/default/newjerseylightrail");
            topics.add("persistent://public/default/newjerseyrail");

            FunctionConfig functionConfig = FunctionConfig.builder()
                    .className(TransitFunction.class.getName())
                    .inputs(topics)
                    .name("TransitParser")
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