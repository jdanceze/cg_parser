package javax.servlet.jsp;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/HttpJspPage.class */
public interface HttpJspPage extends JspPage {
    void _jspService(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException;
}
