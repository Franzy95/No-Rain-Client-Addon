package com.franzy.norainclient;

import api.BTWAddon;
import api.AddonHandler;

public class NoRainClientAddon extends BTWAddon {

    public NoRainClientAddon() {
        super();
    }

    @Override
    public String getName() {
        return "No Rain Addon";
    }

    @Override
    public String getVersionString() {
        return "1.0.1";
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");

    }
}






