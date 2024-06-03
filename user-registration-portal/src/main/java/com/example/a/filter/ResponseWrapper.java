package com.example.a.filter;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ResponseWrapper extends HttpServletResponseWrapper {
    private CharArrayWriter charArrayWriter = new CharArrayWriter();
    private PrintWriter writer = new PrintWriter(charArrayWriter);

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    public String getCapturedResponseBody() {
        return charArrayWriter.toString();
    }
}

