package org.omnaest.microbiome.pcl.domain;

import java.util.function.Predicate;

public enum ClassificationLevel
{
    KINGDOM(species -> species.getKingdom() != null && species.getPhylum() == null),
    PHYLUM(species -> species.getPhylum() != null && species.getClassDef() == null),
    CLASSDEF(species -> species.getClassDef() != null && species.getOrder() == null),
    ORDER(species -> species.getOrder() != null && species.getFamily() == null),
    FAMILY(species -> species.getFamily() != null && species.getGenus() == null),
    GENUS(species -> species.getGenus() != null && species.getSpecies() == null),
    SPECIES(species -> species.getSpecies() != null && species.getType() == null),
    TYPE(species -> species.getType() != null);

    private Predicate<Species> criteria;

    private ClassificationLevel(Predicate<Species> criteria)
    {
        this.criteria = criteria;

    }

    public boolean matches(Species species)
    {
        return this.criteria.test(species);
    }
}