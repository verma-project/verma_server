package org.deraproject.apps.server.enums;

public enum RepairEventEnum {
    /* Repair awaiting repairer(s) */
    AWAITING,
    /* Repair currently in progress */
    IN_PROGRESS,
    /* Unable to repair item */
    UNABLE_REPAIR,
    /* Repair taken off-site by Repairer(s) for home repair */
    OFFSITE_REPAIR,
    /* Repair is delayed. */
    DELAYED,
    /* Item repaired and returned to guest. */
    REPAIRED,
    /* Item requires part(s) */
    PENDING_PARTS
}
