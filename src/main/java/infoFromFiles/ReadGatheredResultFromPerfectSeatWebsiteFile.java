package infoFromFiles;

import lombok.With;

public record ReadGatheredResultFromPerfectSeatWebsiteFile(@With String numberOfWavesNeeded,
                                                           String airportName,
                                                           String economySeatsAmountNeeded,
                                                           String businessSeatsAmountNeeded,
                                                           String firstSeatsAmountNeeded,
                                                           String cargoSeatsAmountNeeded,
                                                           String economyPriceForRoute,
                                                           String businessPriceForRoute,
                                                           String firstPriceForRoute,
                                                           String cargoPriceForRoute,
                                                           String timeTakenForOneWave) { }