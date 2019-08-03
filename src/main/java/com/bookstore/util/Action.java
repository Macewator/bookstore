package com.bookstore.util;

public enum Action {

    ACTION_CONFIRM_ORDER("confirm"),
    ACTION_CLEAR_ORDER ("clear"),
    ACTION_SORT_BOOKS("sort"),
    ACTION_SEARCH_BOOKS("search");

    private String action;

    Action(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
