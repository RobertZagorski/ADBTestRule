package com.rzagorski.adbtestrule.annotations;

/**
 * Created by Robert Zagórski on 2016-10-25.
 */

public enum PermissionOperation {
    GRANT("grant"),
    REVOKE("revoke");

    private final String text;

    /**
     * @param text
     */
    private PermissionOperation(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }

    public String get() {
        return toString();
    }
}
