package dev.pulsarfunction.transit.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 *
 * @author tspann
 */
public class Result implements Serializable {

    private boolean isTransit;

    public Result(boolean isTransit, String topicName, Transcom transcom, Transit transit, String rawString, byte[] rawInput, String extraInfo, Throwable error) {
        super();
        this.isTransit = isTransit;
        this.topicName = topicName;
        this.transcom = transcom;
        this.transit = transit;
        this.rawString = rawString;
        this.rawInput = rawInput;
        this.extraInfo = extraInfo;
        this.error = error;
    }

    private String outputTopic;

    public String getOutputTopic() {
        return outputTopic;
    }

    public Result(boolean isTransit, String outputTopic, String topicName, Transcom transcom, Transit transit, String rawString, byte[] rawInput, String extraInfo, Throwable error) {
        super();
        this.isTransit = isTransit;
        this.outputTopic = outputTopic;
        this.topicName = topicName;
        this.transcom = transcom;
        this.transit = transit;
        this.rawString = rawString;
        this.rawInput = rawInput;
        this.extraInfo = extraInfo;
        this.error = error;
    }

    public void setOutputTopic(String outputTopic) {
        this.outputTopic = outputTopic;
    }

    public boolean isTransit() {
        return isTransit;
    }

    public void setIsTransit(boolean transit) {
        isTransit = transit;
    }

    public void setTransitFalse() { isTransit = false; }
    public void setTransitTrue() { isTransit = true; }

    private String topicName;

    private Transcom transcom;

    private Transit transit;

    private String rawString;

    private byte[] rawInput;

    private String extraInfo;

    private Throwable error;


    @Override
    public String toString() {
        return new StringJoiner(", ", Result.class.getSimpleName() + "[", "]")
                .add("isTransit=" + isTransit)
                .add("outputTopic='" + outputTopic + "'")
                .add("topicName='" + topicName + "'")
                .add("transcom=" + transcom)
                .add("transit=" + transit)
                .add("rawString='" + rawString + "'")
                .add("rawInput=" + Arrays.toString(rawInput))
                .add("extraInfo='" + extraInfo + "'")
                .add("error=" + error)
                .toString();
    }

    public Result() {
        super();
    }

    public Result(String topicName, Transcom transcom, Transit transit, String rawString, byte[] rawInput, String extraInfo, Throwable error) {
        super();
        this.topicName = topicName;
        this.transcom = transcom;
        this.transit = transit;
        this.rawString = rawString;
        this.rawInput = rawInput;
        this.extraInfo = extraInfo;
        this.error = error;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Transcom getTranscom() {
        return transcom;
    }

    public void setTranscom(Transcom transcom) {
        this.transcom = transcom;
    }

    public Transit getTransit() {
        return transit;
    }

    public void setTransit(Transit transit) {
        this.transit = transit;
    }

    public String getRawString() {
        return rawString;
    }

    public void setRawString(String rawString) {
        this.rawString = rawString;
    }

    public byte[] getRawInput() {
        return rawInput;
    }

    public void setRawInput(byte[] rawInput) {
        this.rawInput = rawInput;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
