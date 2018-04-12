package com.ey.tax;

import com.ey.tax.entity.AttachmentStore;
import com.ey.tax.entity.TaxPayment;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        AttachmentStore s1 = new AttachmentStore();
        s1.setId(1L);
        AttachmentStore s2 = new AttachmentStore();
        s2.setId(1L);
        List<AttachmentStore> attachmentStores = new ArrayList<>();
        attachmentStores.add(s1);
        attachmentStores.add(s2);

//        String attachmentIds = attachmentStores.stream().map(a -> a.getId().toString()).collect(Collectors.joining(","));
        String attachmentIds = attachmentStores.stream().map(a -> a.getId().toString()).reduce(",",String::concat);

        System.out.println(attachmentIds);
    }
}
