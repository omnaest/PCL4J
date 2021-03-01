package org.omnaest.microbiome.pcl.domain;

import java.util.List;

public interface PCLData
{
    public PCLDataAnalyzer getAnalzer();

    List<Species> getSpecies();
}
