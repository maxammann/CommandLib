package com.p000ison.dev.commandlib;

/**
 * Represents a CommandListener
 */
public interface CommandListener {

    void onPreCommand(CallInformation info);

    void onPostCommand(CallInformation info);
}
