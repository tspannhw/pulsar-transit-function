package dev.pulsarfunction.transit;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 *
 * Class: dog
 * Probabilties: 0.82268184
 * Coord:83.82353, 179.13997, 206.63783, 476.78754
 * @author tspann
 */
public class Result implements Serializable {

    private double probability;
    private double probabilityPercentage;

    private double probabilityNegative;
    private double probabilityNegativePercentage;

    private String sentimentValue;



    public String getSentimentValue() {
        return sentimentValue;
    }

    public void setSentimentValue(String sentimentValue) {
        this.sentimentValue = sentimentValue;
    }

    private String rawClassification;

    public Result(double probability, double probabilityPercentage, double probabilityNegative, double probabilityNegativePercentage, String rawClassification) {
        super();
        this.probability = probability;
        this.probabilityPercentage = probabilityPercentage;
        this.probabilityNegative = probabilityNegative;
        this.probabilityNegativePercentage = probabilityNegativePercentage;
        this.rawClassification = rawClassification;
    }

    public String getRawClassification() {
        return rawClassification;
    }

    public void setRawClassification(String rawClassification) {
        this.rawClassification = rawClassification;
    }

    public Result(double probability, double probabilityPercentage, double probabilityNegative, double probabilityNegativePercentage, String sentimentValue, String rawClassification) {
        super();
        this.probability = probability;
        this.probabilityPercentage = probabilityPercentage;
        this.probabilityNegative = probabilityNegative;
        this.probabilityNegativePercentage = probabilityNegativePercentage;
        this.sentimentValue = sentimentValue;
        this.rawClassification = rawClassification;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Result.class.getSimpleName() + "[", "]")
                .add("probability=" + probability)
                .add("probabilityPercentage=" + probabilityPercentage)
                .add("probabilityNegative=" + probabilityNegative)
                .add("probabilityNegativePercentage=" + probabilityNegativePercentage)
                .add("sentimentValue='" + sentimentValue + "'")
                .add("rawClassification='" + rawClassification + "'")
                .toString();
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public double getProbabilityPercentage() {
        return probabilityPercentage;
    }

    public void setProbabilityPercentage(double probabilityPercentage) {
        this.probabilityPercentage = probabilityPercentage;
    }

    public double getProbabilityNegative() {
        return probabilityNegative;
    }

    public void setProbabilityNegative(double probabilityNegative) {
        this.probabilityNegative = probabilityNegative;
    }

    public double getProbabilityNegativePercentage() {
        return probabilityNegativePercentage;
    }

    public void setProbabilityNegativePercentage(double probabilityNegativePercentage) {
        this.probabilityNegativePercentage = probabilityNegativePercentage;
    }

    public Result() {
        super();
    }

    public Result(double probability, double probabilityPercentage, double probabilityNegative, double probabilityNegativePercentage) {
        super();
        this.probability = probability;
        this.probabilityPercentage = probabilityPercentage;
        this.probabilityNegative = probabilityNegative;
        this.probabilityNegativePercentage = probabilityNegativePercentage;
    }

}
