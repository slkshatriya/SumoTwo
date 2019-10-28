package com.one4all.sumotwo;

import java.io.Serializable;

public class Users implements Serializable {

        String uid;
        String mdisplayName;
        String emailAddress;
        String uri;

    public Users(String uid, String mdisplayName, String emailAddress, String uri) {
            this.uid = uid;
            this.mdisplayName = mdisplayName;
            this.emailAddress = emailAddress;
            this.uri = uri;
        }

    public Users() {
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getMdisplayName() {
            return mdisplayName;
        }

        public void setMdisplayName(String mdisplayName) {
            this.mdisplayName = mdisplayName;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

