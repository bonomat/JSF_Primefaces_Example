package at.hoenisch.parser.helpers;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

/**
 * Created by Philipp Hoenisch on 13/04/16.
 */
public class PartialResponseUpdaterTest {


    private static Document mainDocument;
    private static String validPartialResponse;
    private static String inValidPartialResponse;
    private static String secondValidPartialResponse;
    private static String invalidContent;

    @BeforeClass
    public static void loadSamplePage() throws IOException {
        ClassLoader classLoader = PartialResponseUpdaterTest.class.getClassLoader();
        String samplePage = IOUtils.toString(classLoader.getResourceAsStream("sample-page.xhtml"), "UTF-8");
        validPartialResponse = IOUtils.toString(classLoader.getResourceAsStream("partial-update-valid.xml"), "UTF-8");
        inValidPartialResponse = IOUtils.toString(classLoader.getResourceAsStream("partial-update-invalid.xml"),
                "UTF-8");
        secondValidPartialResponse = IOUtils.toString(classLoader.getResourceAsStream("partial-update-valid-two.xml"),
                "UTF-8");
        invalidContent = IOUtils.toString(classLoader.getResourceAsStream("partial-update-invalid-content.xml"),
                "UTF-8");
        mainDocument = Jsoup.parse(samplePage);

    }

    @Test
    public void updatePageValidPartialResponse() throws Exception {
        Document document = PartialResponseUpdater.updatePage(mainDocument, validPartialResponse);
        assertThat(document.html(), containsString("awesome"));
    }

    @Test
    public void updatePageInValidPartialResponse() throws Exception {
        Document document = PartialResponseUpdater.updatePage(mainDocument, inValidPartialResponse);
        assertFalse(document.html().contains("shouldNotBeHere"));
    }

    @Test
    public void updatePageTwoPartialResponsesInOne() throws Exception {
        Document document = PartialResponseUpdater.updatePage(mainDocument, secondValidPartialResponse);
        assertThat(document.html(), containsString("terrific"));
    }

    @Test
    public void updatePagePartialResponses_TwoInARow() throws Exception {
        mainDocument = PartialResponseUpdater.updatePage(mainDocument, validPartialResponse);
        assertThat(mainDocument.html(), containsString("awesome"));
        mainDocument = PartialResponseUpdater.updatePage(mainDocument, secondValidPartialResponse);
        assertThat(mainDocument.html(), containsString("terrific"));
    }

    @Test
    public void updatePageInValidContent() throws Exception {
        Document document = PartialResponseUpdater.updatePage(mainDocument, invalidContent);
        assertFalse(document.html().contains("shouldNotBeHere"));
    }


}