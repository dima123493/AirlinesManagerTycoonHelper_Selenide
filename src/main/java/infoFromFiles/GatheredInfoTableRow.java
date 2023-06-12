package infoFromFiles;

public record GatheredInfoTableRow(String destinationHubValue,
                                   String economyDemandValue,
                                   String businessDemandValue,
                                   String firstDemandValue,
                                   String cargoDemandValue,
                                   String economyPriceValue,
                                   String businessPriceValue,
                                   String firstPriceValue,
                                   String cargoPriceValue,
                                   String distanceToAirport) {
}
