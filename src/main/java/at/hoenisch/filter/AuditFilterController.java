package at.hoenisch.filter;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philipp Hoenisch, Senacor on 09/04/16.
 */
@Named
@SessionScoped
public class AuditFilterController implements Serializable {
    private List<String> responses = new ArrayList<>();

    public void addRequest(Long id, String type, String content) {
        responses.add(content);
    }

    public List<String> getResponses() {
        return responses;
    }
}
