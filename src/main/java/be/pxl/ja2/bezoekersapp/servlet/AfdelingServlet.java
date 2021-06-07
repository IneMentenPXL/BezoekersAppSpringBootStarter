package be.pxl.ja2.bezoekersapp.servlet;

import be.pxl.ja2.bezoekersapp.model.Bezoeker;
import be.pxl.ja2.bezoekersapp.service.BezoekersService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/afdeling")
public class AfdelingServlet extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(AfdelingServlet.class);

    @Autowired
    private BezoekersService bezoekersService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        String code = req.getParameter("code");
        List<Bezoeker> visitorsForWard = bezoekersService.getBezoekersVoorAfdeling(code).stream().sorted(Comparator.comparing(Bezoeker::getTijdstip)).collect(Collectors.toList());
        try (PrintWriter writer = resp.getWriter()) {
            writer.println("<html>");
            writer.println("<body>");
            for (Bezoeker visitor : visitorsForWard) {
                writer.println("<div>");
                writer.println("Tijdstip: " + visitor.getTijdstip());
                writer.println(" Patiënt: " + visitor.getPatient().getCode());
                writer.println(" Bezoeker: " + visitor.getInfo());
                writer.println("</div>");
            }
            writer.println("</body>");
            writer.println("</html>");
        }
    }
}

