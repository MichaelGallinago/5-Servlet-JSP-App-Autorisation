package com.micg.servlet;

import com.micg.servlet.service.FileSystemItemsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(urlPatterns = {"/index"})
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathFromRequest = request.getParameter("path");
        String currentDirPath = (pathFromRequest == null ?
                new File(".").getCanonicalPath() : pathFromRequest).replace('\\', '/');

        request.setAttribute("currentDirPath", currentDirPath);
        request.setAttribute("list", FileSystemItemsService.GetItemsFromDirectory(currentDirPath));

        String parentDirPath = new File(currentDirPath).getParent();
        request.setAttribute("parentDirPath", parentDirPath == null ? currentDirPath : parentDirPath);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

        request.setAttribute("generationTime", dateFormat.format(new Date()));
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}