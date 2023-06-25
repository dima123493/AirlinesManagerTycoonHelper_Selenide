package infoFromFiles;

import java.time.Duration;

public record FlightLog(
        int planeId,
        String airportName,
        Duration timeTaken,
        Duration timeLeft
){}