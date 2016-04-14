package at.hoenisch.parser.helpers;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * Created by Philipp Hoenisch on 13/04/16.
 */
public class FormIdExtractorTest {

    @Test
    public void regexTestsFormAtTheBeginning() {
        String source = "arbitraryForm:field:button";
        String found = FormIdExtractor.extract(source);
        assertThat(found, equalTo("arbitraryForm"));
    }

    @Test
    public void regexTestFormSingle() {
        String source = "arbitraryForm";
        String found = FormIdExtractor.extract(source);
        assertThat(found, equalTo("arbitraryForm"));
    }

    @Test
    public void regexTestFormInTheMiddle() {
        String source = "bananeCkrrr:arbitraryForm:awesome:btn";
        String found = FormIdExtractor.extract(source);
        assertThat(found, equalTo("arbitraryForm"));
    }

    @Test
    public void regexTestFormInTheEnd() {
        String source = "bananeCkrrr:awesome:arbitraryForm";
        String found = FormIdExtractor.extract(source);
        assertThat(found, equalTo("arbitraryForm"));
    }

    @Test
    public void testMultipleFormsSelectLast() {
        String source = "FirstForm:awesome:arbitraryForm:btn";
        String found = FormIdExtractor.extract(source);
        assertThat(found, equalTo("arbitraryForm"));
    }

    @Test
    public void testNoForm() {
        String source = "There:is:nothing";
        String found = FormIdExtractor.extract(source);
        assertThat(found, equalTo(""));
    }
}