package com.drrs.util;

import java.io.Serializable;
import java.util.Date;

public class Log implements Serializable {
    private String serverName;
    private String userID;
    private Date date;
    private String requestType;
    private String requestParameters;
    private String successStatus;
    private String serverResponse;
    private long timeStamp;

    public Log(String serverName, String userID, Date date, String requestType, String requestParameters, String successStatus, String serverResponse) {
        this.userID = userID;
        this.date = date;
        this.requestType = requestType;
        this.requestParameters = requestParameters;
        this.successStatus = successStatus;
        this.serverResponse = serverResponse;
        this.serverName = serverName;
        this.timeStamp = System.currentTimeMillis();
    }

    public Date getDate() {
        return date;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getRequestParameters() {
        return requestParameters;
    }

    public String getSuccessStatus() {
        return successStatus;
    }

    public String getServerResponse() {
        return serverResponse;
    }

    public String getUserID() {
        return userID;
    }

    public String getServerName() {
        return serverName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TimeStamp: " + this.timeStamp + "\n");
        sb.append("Request Type: " + this.requestType + "\n");
        sb.append("Request Parameters:" +this.requestParameters + "\n");
        sb.append("Success Status: " + this.successStatus + "\n");
        sb.append("Server Response: " + this.serverResponse + "\n");

        return sb.toString();
    }
}
