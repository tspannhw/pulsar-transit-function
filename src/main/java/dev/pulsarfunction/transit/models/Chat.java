package dev.pulsarfunction.transit.models;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 *

 {"userInfo":"Tim Spann","contactInfo":"Tim Spann, Developer Advocate @ StreamNative","comment":"What is StreamNative the best thing in the world?"}


 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *  row['id'] = str(msg_id)
 *         row['sentiment'] = str(sentimentVal)
 *         row['userInfo'] = str(fields["userInfo"])
 *         row['comment'] = str(fields["comment"])
 *         row['contactInfo'] = str(fields["contactInfo"])
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Chat implements Serializable {
    private static final long serialVersionUID = 7L;
    public String userInfo;
    public String contactInfo;
    public String comment;
    public String sentiment;
    public String id;

    @Override
    public String toString() {
        return new StringJoiner(", ", Chat.class.getSimpleName() + "[", "]")
                .add("userInfo='" + userInfo + "'")
                .add("contactInfo='" + contactInfo + "'")
                .add("comment='" + comment + "'")
                .add("sentiment='" + sentiment + "'")
                .add("id='" + id + "'")
                .toString();
    }

    public Chat(String userInfo, String contactInfo, String comment, String sentiment, String id) {
        super();
        this.userInfo = userInfo;
        this.contactInfo = contactInfo;
        this.comment = comment;
        this.sentiment = sentiment;
        this.id = id;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Chat() {
        super();
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
