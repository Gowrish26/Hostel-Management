/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import daos.AccountDAO;
import dtos.Account;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static utilities.PasswordHash.validatePassword;

/**
 *
 * @author lekha
 */
public class loginServlet extends HttpServlet {

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
            String indexValue = request.getParameter("indexValue");
            String password   = request.getParameter("password");
            
            HttpSession session = request.getSession();
            
            System.out.println(request.getParameter("remember-me"));
            
            String indexNames[] = {"username", "email", "phone"};
            try{
                if(indexValue != null && password != null && !indexValue.equals("") && !password.equals("")){
                    Account acc = AccountDAO.getAccount(indexNames, indexValue);
                    if(acc!=null){
                        if(validatePassword(password, acc.getHashedPassword())){
                            System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFF");
                            session.setAttribute("loginedAccount", acc.getAccountId());
                            response.sendRedirect("/HostelManagement_main/home");
                            return;
                        }
                        else request.setAttribute("ERROR", "Wrong Password!");
                    }
                    else request.setAttribute("ERROR", "Account not found! Please check your username, email or phone number again");
                }
                else request.setAttribute("ERROR", "Please enter all the required fields!");
                request.setAttribute("pageTitle", "Login");
                request.setAttribute("pageContent", "loginForm.jsp");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
            catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | ServletException e){
                e.printStackTrace();
            }
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
        processRequest(request, response);
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
        processRequest(request, response);
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
