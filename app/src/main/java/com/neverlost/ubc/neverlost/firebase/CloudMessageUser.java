package com.neverlost.ubc.neverlost.firebase;

public class CloudMessageUser {

    private final String name;
    private String firebaseClientToken;
    private String facebookId;

    public CloudMessageUser(String name, String firebaseClientToken, String facebookId) {
        this.name = name;
        this.firebaseClientToken = firebaseClientToken;
        this.facebookId = facebookId;
    }

    public String getName() {
        return name;
    }

    public String getFirebaseClientToken() {
        return firebaseClientToken;
    }

    public void setFirebaseClientToken(String firebaseClientToken) {
        this.firebaseClientToken = firebaseClientToken;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
}
