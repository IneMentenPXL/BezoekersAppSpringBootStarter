package be.pxl.ja2.bezoekersapp.servlet;

import be.pxl.ja2.bezoekersapp.model.Bezoeker;
import be.pxl.ja2.bezoekersapp.service.BezoekersService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/afdeling")
public class AfdelingServlet extends HttpServlet {
    @Autowired
    private BezoekersService bezoekersService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        String code = req.getParameter("code");
        List<Bezoeker> visitorsForWard = bezoekersService.getBezoekersVoorAfdeling(code);
        try (PrintWriter writer = resp.getWriter()) {
            writer.println("<html>");
            writer.println("<body>");
            for (Bezoeker visitor : visitorsForWard) {
                writer.println("<p>");
                writer.println("Tijdstip: " + visitor.getTijdstip());
                writer.println(" Patiënt: " + visitor.getPatient().getCode());
                writer.println(" Bezoeker: " + visitor.getInfo());
                writer.println("</p>");
            }
            writer.println("</body>");
            writer.println("</html>");
        }
    }
}

