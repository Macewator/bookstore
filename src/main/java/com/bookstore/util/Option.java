package com.bookstore.util;

public enum Option {

    OPTION_ACCOUNT("account"),
    OPTION_ORDER("order"),
    OPTION_HISTORY("history"),
    OPTION_PAYMENT("payment"),
    OPTION_FAVORITES("favorites");


    private String option;

    Option(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }

    public static Option getSelectedOption(String selectedOption){
        Option option = null;
        for (Option o: Option.values()) {
            if(o.getOption().equals(selectedOption)){
                option = o;
            }
        }
        return option;
    }
}
