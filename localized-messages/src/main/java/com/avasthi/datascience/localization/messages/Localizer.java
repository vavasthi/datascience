package com.avasthi.datascience.localization.messages;

import java.util.Locale;

public class Localizer {
    public static final Localizer INSTANCE = new Localizer();
    private Locale currentLocale;
    private Localizer() {
        currentLocale = Locale.US;
    }
    public String localize(int msg, Object... args) {
        return "";
    }
}
