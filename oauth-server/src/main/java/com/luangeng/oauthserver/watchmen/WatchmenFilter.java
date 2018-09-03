package com.luangeng.oauthserver.watchmen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@WebFilter(urlPatterns = "/*")
public class WatchmenFilter extends OncePerRequestFilter {

    @Autowired
    WatchService watchService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        RequestDesc desc = RequestDesc.newFrom(request);
        RequestDescContextHolder.set(desc);

        if (watchService.fibbden(desc)) {
            errorResponse(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void errorResponse(HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println("<HTML>");
            out.println("<HEAD><TITLE>Error</TITLE></HEAD>");
            out.println("<BODY>");
            out.println("已进入黑名单，请联系管理员");
            out.println("</BODY>");
            out.println("</HTML>");
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
