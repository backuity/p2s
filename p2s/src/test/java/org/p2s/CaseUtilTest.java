package org.p2s;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CaseUtilTest {

    @Test
    public void camelCaseToDotCase() throws Exception {
        assertEquals("the.surname", CaseUtil.camelCaseToDotCase("theSurname"));
        assertEquals("mul.ti.ple", CaseUtil.camelCaseToDotCase("mulTiPle"));
        assertEquals("pdf.path", CaseUtil.camelCaseToDotCase("PDFPath"));
        assertEquals("the.pdf.path", CaseUtil.camelCaseToDotCase("thePDFPath"));
    }
}
