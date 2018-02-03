package org.omnaest.microbiome.pcl.domain.raw;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.omnaest.microbiome.pcl.domain.ClassificationLevel;
import org.omnaest.microbiome.pcl.domain.PCLDataAnalyzer;
import org.omnaest.microbiome.pcl.domain.SampleType;
import org.omnaest.microbiome.pcl.domain.Species;
import org.omnaest.utils.CollectorUtils;
import org.omnaest.utils.MapUtils;
import org.omnaest.utils.MapperUtils;
import org.omnaest.utils.MathUtils;
import org.omnaest.utils.MathUtils.AverageAndStandardDeviation;

public class PCLDataAnalyzerImpl implements PCLDataAnalyzer
{
    private PCLDataImpl data;

    public PCLDataAnalyzerImpl(PCLDataImpl rawPCLData)
    {
        this.data = rawPCLData;
    }

    @Override
    public Map<Species, AverageAndStandardDeviation> calculateSpeciesAverageExpressionFor(SampleType sampleType)
    {
        Set<String> filteredSampleNumbers = this.data.sampleNumberToSample.entrySet()
                                                                          .stream()
                                                                          .filter(entry -> entry.getValue()
                                                                                                .hasSampleType(sampleType))
                                                                          .map(entry -> entry.getKey())
                                                                          .collect(Collectors.toSet());

        Map<Integer, Set<Double>> speciesIdToExpressions = new HashMap<>();
        filteredSampleNumbers.stream()
                             .map(sampleNumber -> this.data.sampleNumberToSpeciesIdToExpression.getOrDefault(sampleNumber, Collections.emptyMap()))
                             .forEach(speciesIdToExpression ->
                             {
                                 for (int speciesId : speciesIdToExpression.keySet())
                                 {
                                     speciesIdToExpressions.computeIfAbsent(speciesId, sid -> new HashSet<>())
                                                           .add(speciesIdToExpression.get(speciesId));
                                 }
                             });

        return speciesIdToExpressions.entrySet()
                                     .stream()
                                     .collect(Collectors.toMap(entry -> this.data.speciesIdToSpecies.get(entry.getKey()),
                                                               entry -> MathUtils.calculateAverage(entry.getValue())));

    }

    @Override
    public Map<Species, AverageAndStandardDeviation> calculateSpeciesAverageExpressionFor(SampleType sampleType, ClassificationLevel classificationLevel)
    {
        Map<Species, AverageAndStandardDeviation> speciesAverageExpression = this.calculateSpeciesAverageExpressionFor(sampleType);

        Map<Species, AverageAndStandardDeviation> filteredSpeciesToAverageExpression = speciesAverageExpression.entrySet()
                                                                                                               .stream()
                                                                                                               .filter(entry -> classificationLevel.equals(entry.getKey()
                                                                                                                                                                .getClassificationLevel()))
                                                                                                               .collect(CollectorUtils.appendToMap(new HashMap<>()));
        return this.normalize(filteredSpeciesToAverageExpression);
    }

    private Map<Species, AverageAndStandardDeviation> normalize(Map<Species, AverageAndStandardDeviation> speciesToAverageExpression)
    {
        double correctionFactor = 1.0 / speciesToAverageExpression.values()
                                                                  .stream()
                                                                  .mapToDouble(v -> v.getAverage())
                                                                  .sum();
        return speciesToAverageExpression.entrySet()
                                         .stream()
                                         .map(MapperUtils.mapEntryValue(value -> new AverageAndStandardDeviation(value.getAverage() * correctionFactor,
                                                                                                                 value.getStandardDeviation()
                                                                                                                         * correctionFactor)))
                                         .collect(CollectorUtils.toMap());
    }

    @Override
    public Map<Species, AverageAndStandardDeviation> calculateSpeciesAverageExpressionFor(SampleType sampleType, Species speciesMatcher)
    {
        Map<Species, AverageAndStandardDeviation> speciesAverageExpression = this.calculateSpeciesAverageExpressionFor(sampleType);

        return speciesAverageExpression.entrySet()
                                       .stream()
                                       .filter(entry -> MapUtils.containsAll(entry.getKey()
                                                                                  .toMap(),
                                                                             speciesMatcher.toMap()))
                                       .collect(CollectorUtils.appendToMap(new HashMap<>()));
    }

    @Override
    public Map<Species, AverageAndStandardDeviation> calculateSpeciesAverageExpressionFor(SampleType sampleType, ClassificationLevel classificationLevel,
                                                                                          Species speciesMatcher)
    {
        Map<Species, AverageAndStandardDeviation> speciesAverageExpression = this.calculateSpeciesAverageExpressionFor(sampleType, classificationLevel);

        return speciesAverageExpression.entrySet()
                                       .stream()
                                       .filter(entry -> MapUtils.containsAll(entry.getKey()
                                                                                  .toMap(),
                                                                             speciesMatcher.toMap()))
                                       .collect(CollectorUtils.appendToMap(new HashMap<>()));
    }

}
