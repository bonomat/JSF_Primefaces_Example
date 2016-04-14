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
public class PartialResponseUpdaterTest extends AbstractAuditParserTest{

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