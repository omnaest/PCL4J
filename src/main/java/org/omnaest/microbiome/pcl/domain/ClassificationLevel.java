package org.omnaest.microbiome.pcl.domain;

import java.util.function.Function;
import java.util.function.Predicate;

public enum ClassificationLevel
{
    KINGDOM(Species::getKingdom, Species::getPhylum),
    PHYLUM(Species::getPhylum, Species::getClassDef),
    CLASSDEF(Species::getClassDef, Species::getOrder),
    ORDER(Species::getOrder, Species::getFamily),
    FAMILY(Species::getFamily, Species::getGenus),
    GENUS(Species::getGenus, Species::getSpecies),
    SPECIES(Species::getSpecies, Species::getType),
    TYPE(Species::getType, species -> null);

    private Predicate<Species>        criteria;
    private Function<Species, String> currentLevelValueResolver;

    private ClassificationLevel(Function<Species, String> currentLevelValueResolver, Function<Species, String> nextLevelValueResolver)
    {
        this.currentLevelValueResolver = currentLevelValueResolver;
        this.criteria = ((Predicate<Species>) species -> currentLevelValueResolver.apply(species) != null).and(species -> nextLevelValueResolver.apply(species) == null);

    }

    public boolean matches(Species species)
    {
        return this.criteria.test(species);
    }

    public String resolveValue(Species species)
    {
        return this.currentLevelValueResolver.apply(species);
    }

}