>ActivityStack.java

```
private void completePauseLocked(boolean resumeNext, ActivityRecord resuming) {
    ...
    if (resumeNext) {
            final ActivityStack topStack = mStackSupervisor.getFocusedStack();
            if (!mService.isSleepingOrShuttingDownLocked()) {
                //-----
                mStackSupervisor.resumeFocusedStackTopActivityLocked(topStack, prev, null);
            } else {
                mStackSupervisor.checkReadyForSleepLocked();
                ActivityRecord top = topStack.topRunningActivityLocked();
                if (top == null || (prev != null && top != prev)) {
                    // If there are no more activities available to run, do resume anyway to start
                    // something. Also if the top activity on the stack is not the just paused
                    // activity, we need to go ahead and resume it to ensure we complete an
                    // in-flight app switch.
                    mStackSupervisor.resumeFocusedStackTopActivityLocked();
                }
            }
        }
    ...
 }
```

经过了一系列的逻辑之后，又调用了resumeFocusedStackTopActivityLocked方法

