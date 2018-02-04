package org.omnaest.microbiome.pcl.domain;

import java.util.Map;

import org.omnaest.utils.MathUtils.AverageAndStandardDeviation;
import org.omnaest.utils.element.bi.BiElement;

public interface PCLDataAnalyzer
{
    public Map<Species, AverageAndStandardDeviation> calculateSpeciesAverageExpressionFor(SampleType sampleType);

    public Map<String, BiElement<Species, AverageAndStandardDeviation>> calculateSpeciesAverageExpressionFor(SampleType sampleType,
                                                                                                             ClassificationLevel classificationLevel);

    public Map<Species, AverageAndStandardDeviation> calculateSpeciesAverageExpressionFor(SampleType sampleType, Species speciesMatcher);

    public Map<Species, AverageAndStandardDeviation> calculateSpeciesAverageExpressionFor(SampleType sampleType, ClassificationLevel classificationLevel,
                                                                                          Species speciesMatcher);
}
