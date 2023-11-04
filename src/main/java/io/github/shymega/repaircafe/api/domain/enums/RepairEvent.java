package io.github.shymega.repaircafe.api.domain.enums;

public enum RepairEvent {
    /* Repair accepted. */
    ACCEPTED,
    /* Repair checked-in. */
    CHECKED_IN,
    /* Repair awaiting repairer */
    AWAITING,
    /* Repair currently in progress */
    IN_PROGRESS,
    /* Unable to repair item */
    UNABLE_REPAIR,
    /* Repair taken by Repairer for home repair */
    TAKEN_REPAIRER,
    /* Repair is delayed. */
    DELAYED,
    /* Item repaired and returned to guest. */
    REPAIRED,
}
