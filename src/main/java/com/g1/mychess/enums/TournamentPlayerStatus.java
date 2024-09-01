package com.g1.mychess.enums;

public enum TournamentPlayerStatus {
    ACTIVE,         // Currently participating
    ELIMINATED,     // Eliminated from the tournament
    WINNER,         // Winner of the tournament
    RUNNER_UP,      // Finished in second place
    THIRD_PLACE,    // Finished in third place
    DISQUALIFIED,   // Disqualified from the tournament
    WITHDRAWN,      // Voluntarily withdrew or retired from the tournament
    FINALIST        // Reached the final round
}
