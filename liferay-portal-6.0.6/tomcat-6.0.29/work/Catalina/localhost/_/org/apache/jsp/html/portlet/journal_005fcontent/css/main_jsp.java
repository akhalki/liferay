package org.apache.jsp.html.portlet.journal_005fcontent.css;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.model.Theme;
import com.liferay.portal.service.ThemeLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

public final class main_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(2);
    _jspx_dependants.add("/html/portlet/css_init.jsp");
    _jspx_dependants.add("/WEB-INF/tld/liferay-util.tld");
  }

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;


/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

      out.write('\n');
      out.write('\n');

/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");

long companyId = PortalUtil.getCompanyId(request);

String themeId = ParamUtil.getString(request, "themeId");

Theme theme = ThemeLocalServiceUtil.getTheme(companyId, themeId, false);

String themeContextPath = PortalUtil.getPathContext();

if (theme.isWARFile()) {
	themeContextPath = theme.getContextPath();
}

String cdnHost = PortalUtil.getCDNHost(request.isSecure());

String themeImagesPath = cdnHost + themeContextPath + theme.getImagesPath();

response.addHeader(HttpHeaders.CONTENT_TYPE, ContentTypes.TEXT_CSS);

      out.write("\n");
      out.write("\n");
      out.write(".portlet-journal-content .icon-actions {\n");
      out.write("\tfloat: left;\n");
      out.write("\tmargin: 1px 10px 1px 1px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-content .icons-container {\n");
      out.write("\tclear: both;\n");
      out.write("\theight: auto;\n");
      out.write("\tmargin-top: 1em;\n");
      out.write("\toverflow: hidden;\n");
      out.write("\twidth: auto;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".ie6 .portlet-journal-content .icons-container {\n");
      out.write("\theight: 1%;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-content .journal-content-article {\n");
      out.write("\tclear: right;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-content .journal-content-article:after {\n");
      out.write("\tclear: both;\n");
      out.write("\tcontent: \"\";\n");
      out.write("\tdisplay: block;\n");
      out.write("\theight: 0;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".ie .portlet-journal-content .journal-content-article {\n");
      out.write("\tzoom: 1;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-content .taglib-discussion {\n");
      out.write("\tmargin-top: 18px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-content .taglib-ratings-wrapper {\n");
      out.write("\tmargin-top: 1em;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-content .aui-tabview-list {\n");
      out.write("\tmargin: 18px 0;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-content .user-actions {\n");
      out.write("\tpadding-bottom: 2.5em;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-content .user-actions .export-actions, .portlet-journal-content .user-actions .print-action, .portlet-journal-content .user-actions .locale-actions {\n");
      out.write("\tfloat: right;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-content .user-actions .print-action {\n");
      out.write("\tmargin-left: 1em;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-content .user-actions .locale-separator {\n");
      out.write("\tborder-right: 1px solid #CCC;\n");
      out.write("\tfloat: right;\n");
      out.write("\tmargin-right: 1em;\n");
      out.write("\tpadding: 0.8em 0.5em;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-configuration .displaying-article-id.modified {\n");
      out.write("\tcolor: #4DCF0C;\n");
      out.write("}");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
