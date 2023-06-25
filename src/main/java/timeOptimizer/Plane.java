package timeOptimizer;

import infoFromFiles.FlightLog;

import java.time.Duration;
import java.util.List;

public class Plane {
    public static int counter = 0;
    Duration time = Duration.ofHours(24);
    final int id = ++counter;

    public Duration getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Plane{" +
                "time=" + time +
                ", id=" + id +
                '}';
    }

    public void performOneFlight(Duration timeTakenForOneWave, FlightId airportName, List<FlightLog> log) {
        if (time.compareTo(timeTakenForOneWave) >= 0) {
            time = time.minus(timeTakenForOneWave);
            log.add(new FlightLog(getId(),
                    airportName.toString(),
                    timeTakenForOneWave,
                     getTime()));
        }
    }

}