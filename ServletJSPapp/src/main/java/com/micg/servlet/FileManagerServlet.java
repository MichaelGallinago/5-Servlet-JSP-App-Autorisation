package com.micg.servlet;

import com.micg.servlet.service.AccountService;
import com.micg.servlet.service.FileSystemItemsService;
import com.micg.servlet.utilities.ServletUtilities;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(urlPatterns = {"/manager"})
public class FileManagerServlet extends HttpServlet {
/*
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
        request.getRequestDispatcher("manager.jsp").forward(request, response);
    }
 */

    @Override
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException, ServletException {

        String login = (String)httpServletRequest.getSession().getAttribute("login");
        String pass = (String)httpServletRequest.getSession().getAttribute("pass");

        if (AccountService.getUserByLogin(login) == null || !AccountService.getUserByLogin(login).password().equals(pass)) {
            String currentURL = httpServletRequest.getRequestURL().toString();
            httpServletResponse.sendRedirect(ServletUtilities.makeNewUrl(currentURL, "/log"));
            return;
        }

        String currentDirPath;
        String pathToUserDir = "C:\\Users\\micha\\fileManager\\" + login;
        String pathFromRequest = httpServletRequest.getParameter("path");
        if (httpServletRequest.getParameter("path") != null && pathFromRequest.startsWith(pathToUserDir)) {
            currentDirPath = pathFromRequest;
        } else {
            currentDirPath = pathToUserDir;
        }

        httpServletRequest.setAttribute("currentDirPath", currentDirPath);
        httpServletRequest.setAttribute("list", FileSystemItemsService.GetItemsFromDirectory(currentDirPath));

        String parentDirPath = new File(currentDirPath).getParent();
        if (parentDirPath == null) {
            parentDirPath = currentDirPath;
        }
        httpServletRequest.setAttribute("parentDirPath", parentDirPath);

        Date generationDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

        httpServletRequest.setAttribute("generationTime", dateFormat.format(generationDate));
        httpServletRequest.getRequestDispatcher("manager.jsp").forward(httpServletRequest, httpServletResponse);
    }

    public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {

        var session = httpServletRequest.getSession();
        session.removeAttribute("login");
        session.removeAttribute("pass");
        String currentURL = httpServletRequest.getRequestURL().toString();
        httpServletResponse.sendRedirect(ServletUtilities.makeNewUrl(currentURL, "/log"));
    }
}