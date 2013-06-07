package com.p000ison.dev.commandlib;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Same as {@link CommandExecutor}, but is able to handle commands asynchronously
 */
public abstract class AsyncCommandExecutor extends CommandExecutor implements Runnable {
    private final Queue<CallInformation> queue;

    protected AsyncCommandExecutor() {
        this.queue = new LinkedList<CallInformation>();
    }

    @Override
    protected CommandExecutor.CallResult executeCommand(CallInformation info) {
        if (info.getCommand().isAsynchronous()) {
            queue.add(info);
            return CallResult.SUCCESS;
        } else {
            return super.executeCommand(info);
        }
    }

    @Override
    public void run() {
        CallInformation info;
        while ((info = queue.poll()) != null) {
            super.executeCommand(info);
        }
    }
}
