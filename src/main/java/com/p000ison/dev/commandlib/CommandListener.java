package com.p000ison.dev.commandlib;

/**
 * Represents a CommandListener
 */
public interface CommandListener {

    void onPreCommand(CallInformation info);

    void onPostCommand(CallInformation info);

   void onDisplayCommandHelp(CallInformation info);

  void onCommandNotFound(CallInformation info);

    void onPermissionFailed(CallInformation info);

}
