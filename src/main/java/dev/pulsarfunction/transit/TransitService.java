package dev.pulsarfunction.transit;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.pulsarfunction.transit.models.Result;
import dev.pulsarfunction.transit.models.Transcom;
import dev.pulsarfunction.transit.models.Transit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * transcom
 *
 * {
 *   "title" : "Michie Stadium :football game",
 *   "description" : "TRANSCOM, Jersey City: football game on Michie Stadium at (Highlands) Air Force Vs Army, Saturday November 5th, 2022, 11:30 AM thru 02:30 PM",
 *   "pubDate" : "2022-08-04T11:19:31",
 *   "point" : "41.3873058180975,-73.9640593528748",
 *   "latitude" : "41.3873058180975",
 *   "ts" : "1666042376634",
 *   "longitude" : "-73.9640593528748",
 *   "uuid" : "62c1ea31-313a-4e76-a6e1-3228e7159111"
 * }
 *
 * newjerseylightrail
 * [ {
 *   "title" : "Nov 19, 2021 02:28:25 PM",
 *   "description" : "Newark Penn Station: Light Rail Escalator 35 Temporarily Out of Service – Effective Immediately",
 *   "link" : "https://www.njtransit.com/node/1436941",
 *   "guid" : "https://www.njtransit.com/node/1436941",
 *   "advisoryAlert" : null,
 *   "pubDate" : "Nov 19, 2021 02:28:25 PM",
 *   "ts" : "1666142877790",
 *   "companyname" : "newjersey",
 *   "uuid" : "18d8d677-b0d5-43dd-953a-7a812904d4b0",
 *   "servicename" : "lightrail"
 * } ]
 *
 * newjerseybus
 * [{"title":"BUS 811 - Oct 19, 2022 12:12:58 PM",
 * "description":"Bus Detour for No. 811 in Milltown – Effective Immediately through Friday, October 21, 2022",
 * "link":"https://www.njtransit.com/node/1541260",
 * "guid":"https://www.njtransit.com/node/1541260",
 * "advisoryAlert":null,
 * "pubDate":"Oct 19, 2022 12:12:58 PM",
 * "ts":"1666205616806",
 * "companyname":"newjersey",
 * "uuid":"7f6d3ebb-20f4-47ce-9ecd-09f533f9812b",
 * "servicename":"bus"}]
 *
 * newjerseyrail
 *
 * [{"title":"Jun 09, 2022 01:34:40 PM",
 * "description":"Perth Amboy Station: Station Enhancement Project Begins - Effective Immediately",
 * "link":"https://www.njtransit.com/node/1522105",
 * "guid":"https://www.njtransit.com/node/1522105",
 * "advisoryAlert":null,
 * "pubDate":"Jun 09, 2022 01:34:40 PM",
 * "ts":"1666205701621",
 * "companyname":"newjersey",
 * "uuid":"40c03f6e-7832-4a6d-9500-b2ddb70cbcef",
 * "servicename":"rail"}]
 */
public class TransitService {
    private static final Logger log = LoggerFactory.getLogger(TransitService.class);

    /**
     * parse raw transit JSON message
     *
     * @param input
     * @return Result
     */
    private Result parseMessage(byte[] input, String topicName) {
        Result result = null;
        if (input == null || topicName == null) {
            result.setError(new NullPointerException());
            return result;
        }
        String message = new String(input);

        try {
            if (message.trim().length() > 0) {
                ObjectMapper mapper = new ObjectMapper();

                result = new Result();

                if ( topicName.toLowerCase().contains("transcom")) {
                    Transcom transcom = mapper.readValue(message, Transcom.class);
                    result.setTranscom(transcom);
                    result.setTopicName(topicName);
                    result.setRawString(message);
                    result.setRawInput(input);
                    result.setTransitFalse();
                    result.setOutputTopic(topicName + "-clean");
                }
                else {
                    // should this just be /transit-clean
                    Transit transit = mapper.readValue(message, Transit.class);
                    result.setTopicName(topicName);
                    result.setRawString(message);
                    result.setRawInput(input);
                    result.setTransit(transit);
                    result.setTransitTrue();
                    result.setOutputTopic("persistent://public/default/" + transit.getCompanyname() +
                            transit.getServicename() + "-clean");
                }
            }
        } catch (Throwable t) {
            result.setError(t);
            t.printStackTrace();
        }

        // if empty
        if (result == null) {
            result = new Result();
            result.setError(new NullPointerException());
        }
        return result;
    }

    /**
     * return a clean transit record
     *byte[]
     * @return  object
     */
    public Result deserialize(byte[] input, Optional<String> topicName) {
        if (input == null) {
            return null;
        }
        else {
            return parseMessage(input, topicName.orElse("transcom"));
        }
    }

    public Transit combiner(Transcom transcom) {
        Transit transit = new Transit();
        if ( transcom == null ) {
            return transit;
        }

        transit.setCompanyname("transcom");
        transit.setServicename("transcom");
        transit.setAdvisoryAlert(null);
        transit.setDescription( transcom.getDescription());
        transit.setGuid( transcom.getPoint() );
        transit.setLink( "https://www.511nj.org/home" );
        transit.setPubDate( transcom.getPubDate());
        transit.setTitle( transcom.getTitle());
        transit.setTs( transcom.getTs());
        transit.setUuid( transcom.getUuid() );

        return transit;
    }
}
