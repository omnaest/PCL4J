package org.omnaest.microbiome.pcl.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.omnaest.utils.JSONHelper;
import org.omnaest.utils.MapUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Species
{
    @JsonProperty("k")
    private String kingdom;
    @JsonProperty("p")
    private String phylum;
    @JsonProperty("c")
    private String classDef;
    @JsonProperty("o")
    private String order;
    @JsonProperty("f")
    private String family;
    @JsonProperty("g")
    private String genus;
    @JsonProperty("s")
    private String species;
    @JsonProperty("t")
    private String type;

    public Species()
    {
        super();
    }

    @JsonIgnore
    public String getId()
    {
        return this.toMap()
                   .entrySet()
                   .stream()
                   .map(entry -> entry.getKey() + "__" + entry.getValue())
                   .collect(Collectors.joining("|"));
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> toMap()
    {
        return MapUtils.toMapWithFilteredKeysHavingNullValue((Map<String, String>) JSONHelper.toMap(this));
    }

    @JsonIgnore
    public ClassificationLevel getClassificationLevel()
    {
        return Arrays.asList(ClassificationLevel.values())
                     .stream()
                     .filter(level -> level.matches(this))
                     .findFirst()
                     .orElse(null);
    }

    public String getKingdom()
    {
        return this.kingdom;
    }

    public String getType()
    {
        return this.type;
    }

    public Species setType(String type)
    {
        this.type = type;
        return this;
    }

    public Species setKingdom(String kingdom)
    {
        this.kingdom = kingdom;
        return this;
    }

    public String getPhylum()
    {
        return this.phylum;
    }

    public Species setPhylum(String phylum)
    {
        this.phylum = phylum;
        return this;
    }

    public String getClassDef()
    {
        return this.classDef;
    }

    public Species setClassDef(String classDef)
    {
        this.classDef = classDef;
        return this;
    }

    public String getOrder()
    {
        return this.order;
    }

    public Species setOrder(String order)
    {
        this.order = order;
        return this;
    }

    public String getFamily()
    {
        return this.family;
    }

    public Species setFamily(String family)
    {
        this.family = family;
        return this;
    }

    public String getGenus()
    {
        return this.genus;
    }

    public Species setGenus(String genus)
    {
        this.genus = genus;
        return this;
    }

    public String getSpecies()
    {
        return this.species;
    }

    public Species setSpecies(String species)
    {
        this.species = species;
        return this;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.classDef == null) ? 0 : this.classDef.hashCode());
        result = prime * result + ((this.family == null) ? 0 : this.family.hashCode());
        result = prime * result + ((this.genus == null) ? 0 : this.genus.hashCode());
        result = prime * result + ((this.kingdom == null) ? 0 : this.kingdom.hashCode());
        result = prime * result + ((this.order == null) ? 0 : this.order.hashCode());
        result = prime * result + ((this.phylum == null) ? 0 : this.phylum.hashCode());
        result = prime * result + ((this.species == null) ? 0 : this.species.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (this.getClass() != obj.getClass())
        {
            return false;
        }
        Species other = (Species) obj;
        if (this.classDef == null)
        {
            if (other.classDef != null)
            {
                return false;
            }
        }
        else if (!this.classDef.equals(other.classDef))
        {
            return false;
        }
        if (this.family == null)
        {
            if (other.family != null)
            {
                return false;
            }
        }
        else if (!this.family.equals(other.family))
        {
            return false;
        }
        if (this.genus == null)
        {
            if (other.genus != null)
            {
                return false;
            }
        }
        else if (!this.genus.equals(other.genus))
        {
            return false;
        }
        if (this.kingdom == null)
        {
            if (other.kingdom != null)
            {
                return false;
            }
        }
        else if (!this.kingdom.equals(other.kingdom))
        {
            return false;
        }
        if (this.order == null)
        {
            if (other.order != null)
            {
                return false;
            }
        }
        else if (!this.order.equals(other.order))
        {
            return false;
        }
        if (this.phylum == null)
        {
            if (other.phylum != null)
            {
                return false;
            }
        }
        else if (!this.phylum.equals(other.phylum))
        {
            return false;
        }
        if (this.species == null)
        {
            if (other.species != null)
            {
                return false;
            }
        }
        else if (!this.species.equals(other.species))
        {
            return false;
        }
        if (this.type == null)
        {
            if (other.type != null)
            {
                return false;
            }
        }
        else if (!this.type.equals(other.type))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Species [kingdom=" + this.kingdom + ", phylum=" + this.phylum + ", classDef=" + this.classDef + ", order=" + this.order + ", family="
                + this.family + ", genus=" + this.genus + ", species=" + this.species + ", type=" + this.type + "]";
    }

}
