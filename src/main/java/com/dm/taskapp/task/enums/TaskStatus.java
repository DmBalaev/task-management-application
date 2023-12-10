package com.dm.taskapp.task.enums;

public enum TaskStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED,
    CLOSE;

    public static boolean isTaskStatus(String status) {
        try {
            TaskStatus.valueOf(status.toUpperCase());
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}
