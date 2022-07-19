/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import daos.HostelDAO;
import dtos.Hostel;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utilities.Colors;

/**
 *
 * @author lekha
 */
public class hostelController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet hostelController</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet hostelController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String processingPath = (String) request.getAttribute("processingPath");
        String contextPath = request.getContextPath();

        String[] splitter = processingPath.split("/");
                
        if (splitter.length == 4) {
            response.setStatus(404);
            return;
        } else {
            request.setAttribute("hasTools", true);
            request.setAttribute("homeTools", "hostelTools.jsp");
            //request.setAttribute("rights", rights);
            
            HostelDAO hostelDao = new HostelDAO();
            Hostel hostel = hostelDao.getOne("hostel_slug", splitter[4]);
            if(hostel==null){
                response.setStatus(404);
                return;
            }
            if(splitter.length == 5){
                response.sendRedirect(contextPath+processingPath+"/hostel-details");
                return;
            }
            
            request.setAttribute("hostelSlug", hostel.getHostelSlug());
            
            String breadcrumb = (String)request.getAttribute("breadcrumb");
            request.setAttribute("breadcrumb", breadcrumb+"/"+hostel.getHostelSlug());
            
            switch (splitter[5]) {
                case "rooms":
                    System.out.println(Colors.YELLOW + "hostelController forward to roomController" + Colors.RESET);
                    request.setAttribute("hostelId", hostel.getHostelId());
                    request.getRequestDispatcher("roomController").forward(request, response);
                    return;
                
                case "hostel-details":
                    //System.out.println(Colors.YELLOW + "owningController forward to hostelController" + Colors.RESET);
                    request.setAttribute("pageTitle", "Hostel Details");
                    request.setAttribute("homeDetails", "hostel/hostelDetails.jsp");
                    String action = request.getParameter("action");
                    if (action != null) {
                        switch (request.getParameter("action")) {
                            case "getUpdateForm":
                                request.setAttribute("hostelDetailsContent", "hostelDetails-updateForm.jsp");
                                break;

                            case "getHostelDetails":
                                request.setAttribute("hostelDetailsContent", "hostelDetails-details.jsp");
                                break;
                        }
                    }
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    return;

                case "room-list":
                    request.setAttribute("pageTitle", "Room List");
                    request.setAttribute("homeDetails", "hostel/roomList.jsp");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    return;

                case "create-room":
                    request.setAttribute("pageTitle", "Create Room");
                    request.setAttribute("homeDetails", "hostel/createRoom.jsp");
                    request.setAttribute("createRoomContent", "createRoom-form.jsp");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    return;
            }
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String processingPath = (String) request.getAttribute("processingPath");
        String contextPath = request.getContextPath();

        String[] splitter = processingPath.split("/");

        if(splitter[5].equals("rooms")){
            System.out.println(Colors.YELLOW + "hostelController forward to roomController" + Colors.RESET);
            request.getRequestDispatcher("roomController").forward(request, response);
            return;
        }

        request.setAttribute("hasTools", true);
        request.setAttribute("homeTools", "hostelTools.jsp");
        System.out.println(request.getParameter("action"));
        switch (request.getParameter("action")) {
            case "createRoom":
                System.out.println(Colors.YELLOW + "hostelController forward to createRoomServlet" + Colors.RESET);
                request.getRequestDispatcher("createRoomServlet").forward(request, response);
                return;

            case "updateHostelDetails":
                System.out.println(Colors.YELLOW + "hostelController forward to updateHostelDetailsServlet" + Colors.RESET);
                request.getRequestDispatcher("updateHostelDetailsServlet").forward(request, response);
                return;
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
