package timeOptimizer;

public class FlightId {
    String airportName;

    public FlightId(String airportName) {
        this.airportName = airportName;
    }

    @Override
    public String toString() {
        return airportName;
    }
}
