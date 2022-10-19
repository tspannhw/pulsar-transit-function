package dev.pulsarfunction.transit.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transit implements Serializable {
    private static final long serialVersionUID = 8L;
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
        @JsonProperty("link")
        public String getLink() {
            return this.link; }
        public void setLink(String link) {
            this.link = link; }
        String link;
        @JsonProperty("guid")
        public String getGuid() {
            return this.guid; }
        public void setGuid(String guid) {
            this.guid = guid; }
        String guid;
        @JsonProperty("advisoryAlert")
        public String getAdvisoryAlert() {
            return this.advisoryAlert; }
        public void setAdvisoryAlert(String advisoryAlert) {
            this.advisoryAlert = advisoryAlert; }
        String advisoryAlert;
        @JsonProperty("pubDate")
        public String getPubDate() {
            return this.pubDate; }
        public void setPubDate(String pubDate) {
            this.pubDate = pubDate; }
        String pubDate;
        @JsonProperty("ts")
        public String getTs() {
            return this.ts; }
        public void setTs(String ts) {
            this.ts = ts; }
        String ts;
        @JsonProperty("companyname")
        public String getCompanyname() {
            return this.companyname; }
        public void setCompanyname(String companyname) {
            this.companyname = companyname; }
        String companyname;
        @JsonProperty("uuid")
        public String getUuid() {
            return this.uuid; }
        public void setUuid(String uuid) {
            this.uuid = uuid; }
        String uuid;
        @JsonProperty("servicename")
        public String getServicename() {
            return this.servicename; }
        public void setServicename(String servicename) {
            this.servicename = servicename; }
        String servicename;


    public Transit() {
        super();
    }

    public Transit(String title, String description, String link, String guid, String advisoryAlert, String pubDate, String ts, String companyname, String uuid, String servicename) {
        super();
        this.title = title;
        this.description = description;
        this.link = link;
        this.guid = guid;
        this.advisoryAlert = advisoryAlert;
        this.pubDate = pubDate;
        this.ts = ts;
        this.companyname = companyname;
        this.uuid = uuid;
        this.servicename = servicename;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Transit.class.getSimpleName() + "[", "]")
                .add("title='" + title + "'")
                .add("description='" + description + "'")
                .add("link='" + link + "'")
                .add("guid='" + guid + "'")
                .add("advisoryAlert=" + advisoryAlert)
                .add("pubDate='" + pubDate + "'")
                .add("ts='" + ts + "'")
                .add("companyname='" + companyname + "'")
                .add("uuid='" + uuid + "'")
                .add("servicename='" + servicename + "'")
                .toString();
    }
}
