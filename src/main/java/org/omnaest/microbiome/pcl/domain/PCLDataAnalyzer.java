package org.omnaest.microbiome.pcl.domain;

import java.util.Map;

import org.omnaest.utils.MathUtils.AverageAndStandardDeviation;

public interface PCLDataAnalyzer
{
    public Map<Species, AverageAndStandardDeviation> calculateSpeciesAverageExpressionFor(SampleType sampleType);

    public Map<Species, AverageAndStandardDeviation> calculateSpeciesAverageExpressionFor(SampleType sampleType, ClassificationLevel classificationLevel);

    public Map<Species, AverageAndStandardDeviation> calculateSpeciesAverageExpressionFor(SampleType sampleType, Species speciesMatcher);

    public Map<Species, AverageAndStandardDeviation> calculateSpeciesAverageExpressionFor(SampleType sampleType, ClassificationLevel classificationLevel,
                                                                                          Species speciesMatcher);
}
