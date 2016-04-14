package at.hoenisch.parser.helpers;

import at.hoenisch.parser.exceptions.ElementNotFoundException;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by Philipp Hoenisch on 14/04/16.
 */
public class SubmitButtonFinderTest extends AbstractAuditParserTest {

    @Test
    public void findButtonWithExistingId() throws Exception, ElementNotFoundException {
        Element submitButton = SubmitButtonFinder.find(mainDocument, "searchCustomerForm", "searchCustomerForm:searchRequest:searchButton");
        assertThat(submitButton.text(), equalTo("Kunde suchen"));
    }

    @Test(expected = ElementNotFoundException.class)
    public void findButtonWithNonExistingId() throws Exception, ElementNotFoundException {
        SubmitButtonFinder.find(mainDocument, "searchCustomerForm", "searchCustomerForm:searchRequest:invalidButtonId");
    }

    @Test(expected = ElementNotFoundException.class)
    public void findButtonWithIdInJavaScript_scriptNotFound() throws ElementNotFoundException {
        SubmitButtonFinder.find(mainDocument, "retailSalesForm", "retailSalesForm:j_idt237");
    }

    @Test
    public void findButtonWithIdInJavaScript() throws Exception, ElementNotFoundException {
        Element submitButton = SubmitButtonFinder.find(mainDocumentWizzard, "retailSalesForm", "retailSalesForm:j_idt237");
        assertThat(submitButton.text(), equalTo("Weiter zum n?chsten Schritt"));
    }

}