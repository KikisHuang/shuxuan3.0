package com.gxdingo.sg.bean;

import com.google.gson.annotations.SerializedName;

public class IdCardOCRBean {


    @SerializedName("backResult")
    private BackResultDTO backResult;
    @SerializedName("frontResult")
    private FrontResultDTO frontResult;

    public BackResultDTO getBackResult() {
        return backResult;
    }

    public void setBackResult(BackResultDTO backResult) {
        this.backResult = backResult;
    }

    public FrontResultDTO getFrontResult() {
        return frontResult;
    }

    public void setFrontResult(FrontResultDTO frontResult) {
        this.frontResult = frontResult;
    }

    public static class BackResultDTO {
        @SerializedName("endDate")
        private String endDate;
        @SerializedName("issue")
        private String issue;
        @SerializedName("startDate")
        private String startDate;

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getIssue() {
            return issue;
        }

        public void setIssue(String issue) {
            this.issue = issue;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }
    }

    public static class FrontResultDTO {
        @SerializedName("birthDate")
        private String birthDate;
        @SerializedName("gender")
        private String gender;
        @SerializedName("address")
        private String address;
        @SerializedName("nationality")
        private String nationality;
        @SerializedName("name")
        private String name;
        @SerializedName("idnumber")
        private String idnumber;

        public String getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(String birthDate) {
            this.birthDate = birthDate;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIdnumber() {
            return idnumber;
        }

        public void setIdnumber(String idnumber) {
            this.idnumber = idnumber;
        }
    }
}
