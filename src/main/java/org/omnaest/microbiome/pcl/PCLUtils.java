package org.omnaest.microbiome.pcl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.omnaest.microbiome.pcl.domain.PCLData;
import org.omnaest.microbiome.pcl.domain.Sample;
import org.omnaest.microbiome.pcl.domain.Species;
import org.omnaest.microbiome.pcl.domain.raw.ModifiablePCLDataImpl;
import org.omnaest.microbiome.pcl.domain.raw.PCLDataImpl;
import org.omnaest.utils.CacheUtils;
import org.omnaest.utils.CollectorUtils;
import org.omnaest.utils.JSONHelper;
import org.omnaest.utils.MapUtils;
import org.omnaest.utils.NumberUtils;
import org.omnaest.utils.PredicateUtils;
import org.omnaest.utils.SetUtils;
import org.omnaest.utils.SetUtils.SetDelta;
import org.omnaest.utils.StreamUtils;
import org.omnaest.utils.StringUtils;
import org.omnaest.utils.csv.CSVUtils;
import org.omnaest.utils.element.bi.BiElement;
import org.omnaest.utils.stream.FirstElementFilterCapture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PCLUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(PCLUtils.class);

    public static interface PCLDataLoader
    {
        public Supplier<PCLData> from(File file) throws FileNotFoundException;

        public Supplier<PCLData> from(Reader reader);

        public Supplier<PCLData> fromUTF8(InputStream inputStream);

        public Supplier<PCLData> from(InputStream inputStream, Charset encoding);

        public Supplier<PCLData> fromCacheOnly();

        public PCLDataLoader usingLocalCache();

        public PCLDataLoader usingFileCache(File cacheFile);
    }

    public static PCLDataLoader load()
    {
        return new PCLDataLoader()
        {
            private Supplier<PCLDataImpl> rawResolver = () -> null;
            private Supplier<PCLData>     resolver    = () -> this.resolve();

            private PCLDataImpl resolve()
            {
                return this.rawResolver.get();
            }

            @Override
            public PCLDataLoader usingLocalCache()
            {
                return this.usingFileCache(new File("cache/pclRepository.json"));
            }

            @Override
            public PCLDataLoader usingFileCache(File cacheFile)
            {
                this.resolver = () -> CacheUtils.toFileCachedElement(PCLDataImpl.class, cacheFile, false, () -> this.resolve())
                                                .get();
                return this;
            }

            @Override
            public Supplier<PCLData> from(File file) throws FileNotFoundException
            {
                return this.fromUTF8(new FileInputStream(file));
            }

            @Override
            public Supplier<PCLData> from(Reader reader)
            {
                this.rawResolver = () ->
                {
                    ModifiablePCLDataImpl pclData = new ModifiablePCLDataImpl();
                    try
                    {
                        LOG.info("Loading sample data...");

                        Stream<Map<String, String>> stream = CSVUtils.parse()
                                                                     .from(reader)
                                                                     .withFormat(CSVFormat.TDF)
                                                                     .get();

                        List<Map<String, String>> rows = this.invertColumnsAndRows(stream);

                        Set<String> sampleKeys = JSONHelper.toMap(new Sample())
                                                           .keySet();

                        AtomicInteger counter = new AtomicInteger();
                        rows.stream()
                            .parallel()
                            .forEach(map ->
                            {
                                LOG.info("  (" + counter.incrementAndGet() + "/" + rows.size() + ")");
                                System.out.println(counter.getAndIncrement());
                                SetDelta<String> delta = SetUtils.delta(sampleKeys, map.keySet());

                                Sample sample = JSONHelper.toObjectWithType(MapUtils.toKeyFilteredMap(map, sampleKeys), Sample.class);
                                pclData.addSample(sample);

                                Set<String> added = delta.getAdded();
                                Map<Species, Double> speciesToExpression = this.parseSpeciesExpression(added, map);
                                pclData.addSpeciesExpression(sample.getSampleNumber(), speciesToExpression);
                            });

                        LOG.info("...done");
                    }
                    catch (IOException e)
                    {
                        throw new IllegalStateException(e);
                    }
                    return pclData;
                };
                return this.resolver;
            }

            private Map<Species, Double> parseSpeciesExpression(Set<String> speciesSet, Map<String, String> map)
            {
                return speciesSet.stream()
                                 .map(sp ->
                                 {
                                     Map<String, String> speciesMap = StringUtils.splitToStream(sp, "|")
                                                                                 .map(token ->
                                                                                 {
                                                                                     String[] typeAndValue = org.apache.commons.lang3.StringUtils.splitByWholeSeparatorPreserveAllTokens(token,
                                                                                                                                                                                         "__");
                                                                                     return typeAndValue.length == 2
                                                                                             ? BiElement.of(typeAndValue[0], typeAndValue[1])
                                                                                             : null;
                                                                                 })
                                                                                 .filter(PredicateUtils.notNull())
                                                                                 .collect(CollectorUtils.toMapByBiElement());

                                     Species species = JSONHelper.toObjectWithType(speciesMap, Species.class);
                                     double expression = NumberUtils.toDouble(map.get(sp));
                                     return BiElement.of(species, expression);
                                 })
                                 .collect(CollectorUtils.toMapByBiElement());
            }

            private List<Map<String, String>> invertColumnsAndRows(Stream<Map<String, String>> stream)
            {
                List<Map<String, String>> list = stream.collect(Collectors.toList());

                Stream<List<String>> inverse = list.stream()
                                                   .flatMap(map -> map.keySet()
                                                                      .stream())
                                                   .distinct()
                                                   .map(key -> list.stream()
                                                                   .map(map -> map.get(key))
                                                                   .collect(Collectors.toList()));

                FirstElementFilterCapture<List<String>> firstElementFilterCapture = PredicateUtils.firstElementFilterCapture();

                return inverse.filter(firstElementFilterCapture)
                              .map(row ->
                              {
                                  Map<String, String> retmap = new LinkedHashMap<>();
                                  StreamUtils.merge(firstElementFilterCapture.get()
                                                                             .stream(),
                                                    row.stream())
                                             .forEach(lar -> retmap.put(lar.getLeft(), lar.getRight()));
                                  return retmap;
                              })
                              .collect(Collectors.toList());
            }

            @Override
            public Supplier<PCLData> fromUTF8(InputStream inputStream)
            {
                return this.from(inputStream, StandardCharsets.UTF_8);
            }

            @Override
            public Supplier<PCLData> from(InputStream inputStream, Charset encoding)
            {
                return this.from(new InputStreamReader(new BufferedInputStream(inputStream), encoding));
            }

            @Override
            public Supplier<PCLData> fromCacheOnly()
            {
                return this.resolver;
            }

        };
    }
}
