package org.omnaest.microbiome.pcl.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SpeciesTest
{

    @Test
    public void testGetId() throws Exception
    {
        assertEquals("", new Species().getId());
    }

}
