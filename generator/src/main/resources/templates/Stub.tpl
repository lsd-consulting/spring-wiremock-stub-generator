package {{model.packageName}};

import com.fasterxml.jackson.databind.ObjectMapper;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.String.format;
import java.net.URLEncoder;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("com.lsdconsulting.stub.plugin.ControllerProcessor")
public class {{model.stubClassName}} extends StubBase {

    private static final int OK = 200;
    public static final int ONCE = 1;

    private ObjectMapper objectMapper;

    public {{model.stubClassName}}(ObjectMapper objectMapper) {
        super(objectMapper);
        this.objectMapper = objectMapper;
    }

    private static final String {{model.methodName.toUpperCase()}}_URL = "{{model.rootResource}}{{model.subResource}}";

    public void get{{model.methodName}}({{model.responseType}} response) {
        buildGet(format({{model.methodName.toUpperCase()}}_URL), OK, buildBody(response));
    }

    public void get{{model.methodName}}(int status, {{model.responseType}} response) {
        buildGet(format({{model.methodName.toUpperCase()}}_URL), status, buildBody(response));
    }

    public void verifyGet{{model.methodName}}() {
        verifyGet{{model.methodName}}(ONCE);
    }

    public void verifyGet{{model.methodName}}(final int times) {
        verify(times, getRequestedFor(urlEqualTo(format({{model.methodName.toUpperCase()}}_URL))));
    }
}
