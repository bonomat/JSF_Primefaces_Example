package at.hoenisch.parser.helpers;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;

import java.io.IOException;

/**
 * Created by Philipp Hoenisch on 14/04/16.
 */
public class AbstractAuditParserTest {

    protected Document mainDocument;
    protected Document mainDocumentWizzard;
    protected String validPartialResponse;
    protected String inValidPartialResponse;
    protected String secondValidPartialResponse;
    protected String invalidContent;

    @Before
    public void loadSamplePage() throws IOException {
        ClassLoader classLoader = PartialResponseUpdaterTest.class.getClassLoader();
        String samplePage = IOUtils.toString(classLoader.getResourceAsStream("sample-page.xhtml"), "UTF-8");
        String wizzardPage = IOUtils.toString(classLoader.getResourceAsStream("sample-page-wizzard.xhtml"), "UTF-8");
        validPartialResponse = IOUtils.toString(classLoader.getResourceAsStream("partial-update-valid.xml"), "UTF-8");
        inValidPartialResponse = IOUtils.toString(classLoader.getResourceAsStream("partial-update-invalid.xml"),
                "UTF-8");
        secondValidPartialResponse = IOUtils.toString(classLoader.getResourceAsStream("partial-update-valid-two.xml"),
                "UTF-8");
        invalidContent = IOUtils.toString(classLoader.getResourceAsStream("partial-update-invalid-content.xml"),
                "UTF-8");
        mainDocument = Jsoup.parse(samplePage);
        mainDocumentWizzard = Jsoup.parse(wizzardPage);
    }
}
