package com.fbxmtjqj.pushserver.common.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.model.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

@Slf4j
public class SecurityHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException except)
            throws IOException, ServletException {
        String path = getRequestPath(request);
        String trackCode = UUID.randomUUID().toString();

        printLog(path, trackCode, ErrorCode.ACCESS_DENIED, except);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.ACCESS_DENIED)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(errorResponse);
        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(json);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException except)
            throws IOException, ServletException {
        String path = getRequestPath(request);
        String trackCode = UUID.randomUUID().toString();
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        printLog(path, trackCode, errorCode, except);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(errorCode)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(errorResponse);
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(json);
    }

    private String getRequestPath(HttpServletRequest request) {
        String servletPath = (String) request.getAttribute((RequestDispatcher.FORWARD_SERVLET_PATH));
        String query = (String) request.getAttribute((RequestDispatcher.FORWARD_QUERY_STRING));
        if (StringUtils.hasText(query)) {
            return servletPath + "?" + query;
        }
        return servletPath;
    }

    private void printLog(final String path, final String trackCode, final ErrorCode code, final Exception except) {
        StringWriter writer = new StringWriter();
        except.printStackTrace(new PrintWriter(writer));

        log.error("\r\n" + "=== Error Info ===\r\n" + "Path: " + (path != null ? path : "Unknown") + "\r\n"
                + "TrackCode: " + trackCode + "\r\n" + "ServerCode: " + code.getHttpStatus() + "\r\n" + "Description: "
                + code.getMessage() + "\r\n" + "Exception: " + except.getClass().getName() + "\r\n"
                + writer);
    }
}
