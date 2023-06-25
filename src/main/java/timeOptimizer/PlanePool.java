package timeOptimizer;

import infoFromFiles.FlightLog;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlanePool {
    List<Plane> pool = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlanePool planePool)) return false;
        return Objects.equals(pool, planePool.pool);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pool);
    }

    @Override
    public String toString() {
        return "PlanePool{" +
                "pool=" + pool +
                '}';
    }

    public void performOneFlight(Duration flightDuration, FlightId airportName, List<FlightLog> log ) {
        var planeOptional = pool.stream()
                .filter(plane -> plane.getTime().compareTo(flightDuration) >= 0)
                .findFirst();
        if (planeOptional.isPresent()) {
            planeOptional.get().performOneFlight(flightDuration, airportName,log);
        } else {
            pool.add(new Plane());
            performOneFlight(flightDuration,airportName,log);
        }
    }

}