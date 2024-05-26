package com.example.donation.model;

public class UserResponse {
    private User user;
    private String errorMessage;
    private Boolean success = true;

    public UserResponse(User user, String errorMessage) {
        this.user = user;
        this.errorMessage = errorMessage;
        if(!errorMessage.isEmpty()){
            this.success = false;
        }
    }
    public UserResponse(User user, String errorMessage, Boolean success) {
        this.user = user;
        this.errorMessage = errorMessage;
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}