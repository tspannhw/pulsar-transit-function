package dev.pulsarfunction.transit.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 *  * transcom
 *  *
 *  * {
 *  *   "title" : "Michie Stadium :football game",
 *  *   "description" : "TRANSCOM, Jersey City: football game on Michie Stadium at (Highlands) Air Force Vs Army, Saturday November 5th, 2022, 11:30 AM thru 02:30 PM",
 *  *   "pubDate" : "2022-08-04T11:19:31",
 *  *   "point" : "41.3873058180975,-73.9640593528748",
 *  *   "latitude" : "41.3873058180975",
 *  *   "ts" : "1666042376634",
 *  *   "longitude" : "-73.9640593528748",
 *  *   "uuid" : "62c1ea31-313a-4e76-a6e1-3228e7159111"
 *  * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transcom implements Serializable {
    private static final long serialVersionUID = 18L;
        @JsonProperty("title")
        public String getTitle() {
            return this.title; }
        public void setTitle(String title) {
            this.title = title; }
        String title;
        @JsonProperty("description")
        public String getDescription() {
            return this.description; }
        public void setDescription(String description) {
            this.description = description; }
        String description;

        @JsonProperty("pubDate")
        public String getPubDate() {
            return this.pubDate; }
        public void setPubDate(String pubDate) {
            this.pubDate = pubDate; }
        String pubDate;

    @JsonProperty("point")
    public String getPoint() {
        return this.point; }
    public void setPoint(String point) {
        this.point = point; }
    String point;

        @JsonProperty("ts")
        public String getTs() {
            return this.ts; }
        public void setTs(String ts) {
            this.ts = ts; }
        String ts;

        @JsonProperty("uuid")
        public String getUuid() {
            return this.uuid; }
        public void setUuid(String uuid) {
            this.uuid = uuid; }
        String uuid;

        @JsonProperty("latitude")
        public String getLatitude() {
            return this.latitude; }
        public void setLatitude(String latitude) {
            this.latitude = latitude; }
        String latitude;

    @JsonProperty("longitude")
    public String getLongitude() {
        return this.longitude; }
    public void setLongitude(String longitude) {
        this.longitude = longitude; }
    String longitude;

    public Transcom() {
        super();
    }

    public Transcom(String title, String description, String pubDate, String point, String ts, String uuid, String latitude, String longitude) {
        super();
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.point = point;
        this.ts = ts;
        this.uuid = uuid;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Transcom.class.getSimpleName() + "[", "]")
                .add("title='" + title + "'")
                .add("description='" + description + "'")
                .add("pubDate='" + pubDate + "'")
                .add("point='" + point + "'")
                .add("ts='" + ts + "'")
                .add("uuid='" + uuid + "'")
                .add("latitude='" + latitude + "'")
                .add("longitude='" + longitude + "'")
                .toString();
    }
}