package org.omnaest.microbiome.pcl.domain;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sample
{
    @JsonProperty("SN")
    private String sampleNumber;
    @JsonProperty("RANDSID")
    private String randomSampleId;
    @JsonProperty("VISNO")
    private String visualizationNumber;
    @JsonProperty("STArea")
    private String area;
    @JsonProperty("STSite")
    private String site;
    @JsonProperty("SNPRNT")
    private String prnt;
    @JsonProperty("Gender")
    private String gender;
    @JsonProperty("WMSPhase")
    private String wmsPhase;
    @JsonProperty("SRS")
    private String srs;

    public boolean hasSampleType(SampleType sampleType)
    {
        return StringUtils.equalsIgnoreCase(sampleType.name(), this.area);
    }

    public String getSampleNumber()
    {
        return this.sampleNumber;
    }

    public void setSampleNumber(String sampleNumber)
    {
        this.sampleNumber = sampleNumber;
    }

    public String getRandomSampleId()
    {
        return this.randomSampleId;
    }

    public void setRandomSampleId(String randomSampleId)
    {
        this.randomSampleId = randomSampleId;
    }

    public String getVisualizationNumber()
    {
        return this.visualizationNumber;
    }

    public void setVisualizationNumber(String visualizationNumber)
    {
        this.visualizationNumber = visualizationNumber;
    }

    public String getArea()
    {
        return this.area;
    }

    public void setArea(String area)
    {
        this.area = area;
    }

    public String getSite()
    {
        return this.site;
    }

    public void setSite(String site)
    {
        this.site = site;
    }

    public String getPrnt()
    {
        return this.prnt;
    }

    public void setPrnt(String prnt)
    {
        this.prnt = prnt;
    }

    public String getGender()
    {
        return this.gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getWmsPhase()
    {
        return this.wmsPhase;
    }

    public void setWmsPhase(String wmsPhase)
    {
        this.wmsPhase = wmsPhase;
    }

    public String getSrs()
    {
        return this.srs;
    }

    public void setSrs(String srs)
    {
        this.srs = srs;
    }

    @Override
    public String toString()
    {
        return "Sample [sampleNumber=" + this.sampleNumber + ", randomSampleId=" + this.randomSampleId + ", visualizationNumber=" + this.visualizationNumber
                + ", area=" + this.area + ", site=" + this.site + ", prnt=" + this.prnt + ", gender=" + this.gender + ", wmsPhase=" + this.wmsPhase + ", srs="
                + this.srs + "]";
    }

}
