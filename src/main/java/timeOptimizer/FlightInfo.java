package timeOptimizer;

import java.time.Duration;
import java.util.Objects;

public class FlightInfo {
    int numberOfWaves;
    Duration duration;

    public int getNumberOfWaves() {
        return numberOfWaves;
    }

    public void decrementNumberOfWaves() {
        numberOfWaves--;
    }

    public Duration getDuration() {
        return duration;
    }

    public FlightInfo(int numberOfWaves, Duration duration) {
        this.numberOfWaves = numberOfWaves;
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlightInfo that)) return false;
        return numberOfWaves == that.numberOfWaves && duration == that.duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfWaves, duration);
    }

    @Override
    public String toString() {
        return "FlightInfo{" +
                "numberOfWaves=" + numberOfWaves +
                ", duration=" + duration +
                '}';
    }
}
