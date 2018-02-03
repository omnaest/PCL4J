package org.omnaest.microbiome.pcl.domain.raw;

import java.util.HashMap;
import java.util.Map;

import org.omnaest.microbiome.pcl.domain.Sample;
import org.omnaest.microbiome.pcl.domain.Species;
import org.omnaest.utils.CollectorUtils;
import org.omnaest.utils.MapperUtils;

public class ModifiablePCLDataImpl extends PCLDataImpl
{
    protected Map<Species, Integer> speciesToSpeciesId = new HashMap<>();

    public synchronized void addSample(Sample sample)
    {
        this.sampleNumberToSample.put(sample.getSampleNumber(), sample);
    }

    public synchronized void addSpeciesExpression(String sampleNumber, Map<Species, Double> speciesToExpression)
    {
        this.sampleNumberToSpeciesIdToExpression.put(sampleNumber, speciesToExpression.entrySet()
                                                                                      .stream()
                                                                                      .map(MapperUtils.mapEntryToBiElement())
                                                                                      .map(biElement -> biElement.applyToFirstArgument(species ->
                                                                                      {
                                                                                          Integer id = this.speciesToSpeciesId.computeIfAbsent(species,
                                                                                                                                               sp -> this.speciesToSpeciesId.size());
                                                                                          this.speciesIdToSpecies.computeIfAbsent(id, i -> species);
                                                                                          return id;
                                                                                      }))
                                                                                      .collect(CollectorUtils.toMapByBiElement()));
    }
}
