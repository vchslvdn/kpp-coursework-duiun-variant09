package ua.edu.nupp.kpp.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

public class MeasureServlet extends HttpServlet {

    private final MeasureService service = new MeasureService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws IOException {
        try {
            writeJson(resp, service.getAll());
        } catch (Exception e) {
            writeError(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            String action = req.getParameter("action");

            if ("add".equals(action)) {
                service.add(Long.parseLong(req.getParameter("auditId")));

            } else if ("update".equals(action)) {
                service.update(id(req), Long.parseLong(req.getParameter("auditId")));

            } else if ("transition".equals(action)) {
                String stateValue = req.getParameter("state");

                if (stateValue == null || stateValue.isBlank()) {
                    throw new IllegalArgumentException("Measure state is not specified");
                }

                service.changeState(id(req), MeasureState.valueOf(stateValue));

            } else if ("delete".equals(action)) {
                service.delete(id(req));

            } else {
                throw new IllegalArgumentException("Unknown operation");
            }

            writeJson(resp, service.getAll());

        } catch (Exception e) {
            writeError(resp, e);
        }
    }

    private void writeJson(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        mapper.writeValue(resp.getWriter(), data);
    }

    private void writeError(HttpServletResponse resp, Exception e) throws IOException {
        boolean clientError = e instanceof IllegalArgumentException;
        int status = clientError ? HttpServletResponse.SC_BAD_REQUEST : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        String message = clientError ? (e.getMessage() == null ? "Invalid request" : e.getMessage()) : "Internal server error";

        resp.setStatus(status);
        writeJson(resp, Map.of("error", message));
    }

    private long id(HttpServletRequest req) {
        return Long.parseLong(req.getParameter("id"));
    }
}