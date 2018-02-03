package org.omnaest.microbiome.pcl;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Ignore;
import org.junit.Test;
import org.omnaest.microbiome.pcl.domain.ClassificationLevel;
import org.omnaest.microbiome.pcl.domain.PCLData;
import org.omnaest.microbiome.pcl.domain.SampleType;
import org.omnaest.microbiome.pcl.domain.Species;
import org.omnaest.utils.ComparatorUtils;
import org.omnaest.utils.JSONHelper;
import org.omnaest.utils.MathUtils.AverageAndStandardDeviation;

public class PCLUtilsTest
{
    @Test
    @Ignore
    public void testLoad() throws Exception
    {
        PCLData data = PCLUtils.load()
                               .usingLocalCache()
                               .from(new File("C:\\Z\\databases\\microbiome\\hmp1-II_metaphlan2-mtd-qcd.pcl"))
                               .get();

        Map<Species, AverageAndStandardDeviation> speciesAverageExpression = data.getAnalzer()
                                                                                 .calculateSpeciesAverageExpressionFor(SampleType.ORAL,
                                                                                                                       ClassificationLevel.GENUS);

        TreeMap<Species, AverageAndStandardDeviation> treeMap = new TreeMap<>(ComparatorUtils.builder()
                                                                                             .of(Species.class)
                                                                                             .with(species -> speciesAverageExpression.get(species)
                                                                                                                                      .getAverage())
                                                                                             .reverse()
                                                                                             .build());
        treeMap.putAll(speciesAverageExpression);
        System.out.println(JSONHelper.prettyPrint(treeMap));
    }

    @Test
    //    @Ignore
    public void testLoad2() throws Exception
    {
        PCLData data = PCLUtils.load()
                               .usingLocalCache()
                               .from(new File("C:\\Z\\databases\\microbiome\\hmp1-II_metaphlan2-mtd-qcd.pcl"))
                               .get();

        Map<Species, AverageAndStandardDeviation> speciesAverageExpression = data.getAnalzer()
                                                                                 .calculateSpeciesAverageExpressionFor(SampleType.GUT,
                                                                                                                       ClassificationLevel.GENUS,
                                                                                                                       new Species().setKingdom("Bacteria"));

        TreeMap<Species, AverageAndStandardDeviation> treeMap = new TreeMap<>(ComparatorUtils.builder()
                                                                                             .of(Species.class)
                                                                                             .with(species -> speciesAverageExpression.get(species)
                                                                                                                                      .getAverage())
                                                                                             .reverse()
                                                                                             .build());
        treeMap.putAll(speciesAverageExpression);
        System.out.println(JSONHelper.prettyPrint(treeMap));

        System.out.println(speciesAverageExpression.values()
                                                   .stream()
                                                   .mapToDouble(average -> average.getAverage())
                                                   .sum());
    }

}
