package org.omnaest.microbiome.pcl.domain.raw;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.omnaest.microbiome.pcl.domain.PCLData;
import org.omnaest.microbiome.pcl.domain.PCLDataAnalyzer;
import org.omnaest.microbiome.pcl.domain.Sample;
import org.omnaest.microbiome.pcl.domain.Species;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PCLDataImpl implements PCLData
{
    @JsonProperty
    protected Map<String, Sample> sampleNumberToSample = new LinkedHashMap<>();

    @JsonProperty
    protected Map<String, Map<Integer, Double>> sampleNumberToSpeciesIdToExpression = new HashMap<>();

    @JsonProperty
    protected Map<Integer, Species> speciesIdToSpecies = new HashMap<>();

    @Override
    public PCLDataAnalyzer getAnalzer()
    {
        return new PCLDataAnalyzerImpl(this);
    }

    public Map<String, Sample> getSampleNumberToSample()
    {
        return this.sampleNumberToSample;
    }

    public Map<String, Map<Integer, Double>> getSampleNumberToSpeciesIdToExpression()
    {
        return this.sampleNumberToSpeciesIdToExpression;
    }

    public Map<Integer, Species> getSpeciesIdToSpecies()
    {
        return this.speciesIdToSpecies;
    }

    @Override
    public List<Species> getSpecies()
    {
        return this.speciesIdToSpecies.values()
                                      .stream()
                                      .collect(Collectors.toList());
    }
}
