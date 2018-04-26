package ch.uzh.supersede.feedbacklibrary.beans;

import android.database.Cursor;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

import ch.uzh.supersede.feedbacklibrary.utils.Enums;

public class LocalFeedbackBean implements Serializable {
    private long creationDate;
    private long votedDate;
    private long subscribedDate;
    private long respondedDate;
    private int owner;
    private int voted;
    private int subscribed;
    private int votes;
    private int responded;
    private UUID feedbackUid;
    private String title;
    private Enums.FEEDBACK_STATUS status;
    private int responses;

    public LocalFeedbackBean(Cursor cursor) {
        this.feedbackUid = UUID.fromString(cursor.getString(0));
        this.title = cursor.getString(1);
        this.votes = cursor.getInt(2);
        this.owner = cursor.getInt(3);
        this.creationDate = cursor.getLong(4);
        this.voted = cursor.getInt(5);
        this.votedDate = cursor.getLong(6);
        this.subscribed = cursor.getInt(7);
        this.subscribedDate = cursor.getLong(8);
        this.responded = cursor.getInt(9);
        this.respondedDate = cursor.getLong(10);
        this.status = Enums.FEEDBACK_STATUS.OPEN; //FIXME remove
        this.responses = new Random().nextInt(100); //FIXME remove
    }

    public long getCreationDate() {
        return creationDate;
    }

    public long getVotedDate() {
        return votedDate;
    }

    public long getSubscribedDate() {
        return subscribedDate;
    }

    public int getOwner() {
        return owner;
    }

    public int getVoted() {
        return voted;
    }

    public int getSubscribed() {
        return subscribed;
    }

    public int getVotes() {
        return votes;
    }

    public UUID getFeedbackUid() {
        return feedbackUid;
    }

    public String getTitle() {
        return title;
    }

    public long getRespondedDate() {
        return respondedDate;
    }

    public int getResponded() {
        return responded;
    }

    public Enums.FEEDBACK_STATUS getStatus() {
        return status;
    }

    public void setStatus(Enums.FEEDBACK_STATUS status) {
        this.status = status;
    }

    public int getResponses() {
        return responses;
    }
}
