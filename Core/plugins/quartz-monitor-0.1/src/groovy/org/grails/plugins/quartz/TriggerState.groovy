package org.grails.plugins.quartz

enum TriggerState {
    BLOCKED(4), COMPLETE(2), ERROR(3), NONE(-1), NORMAL(0), PAUSED(1)

    TriggerState(int value) { this.value = value }

    private final int value

    public int value() { return value }
}