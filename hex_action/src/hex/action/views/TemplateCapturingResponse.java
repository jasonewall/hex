package hex.action.views;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by jason on 14-12-21.
 */
public class TemplateCapturingResponse extends HttpServletResponseWrapper {
    private StringWriter writer = new StringWriter();

    public TemplateCapturingResponse(HttpServletResponse response) {
        super(response);
    }

    public String getContent() {
        writer.flush();
        return writer.getBuffer().toString();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public void flushBuffer() throws IOException {
        writer.flush();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(writer);
    }
}
