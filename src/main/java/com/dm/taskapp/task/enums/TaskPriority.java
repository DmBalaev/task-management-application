package com.dm.taskapp.task.enums;

public enum TaskPriority {
    HIGH,
    MEDIUM,
    LOW;

    public static boolean isPriority(String priority) {
        try {
            TaskPriority.valueOf(priority.toUpperCase());
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}
