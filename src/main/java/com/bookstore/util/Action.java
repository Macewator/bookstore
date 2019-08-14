package com.bookstore.util;

public enum Action {

    ACTION_SEARCH_BOOKS("search");

    private String action;

    Action(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
