package org.apache.jsp.html.portlet.image_005fgallery.css;

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
      out.write(".lfr-image-gallery-actions {\n");
      out.write("\tfont-size: 11px;\n");
      out.write("\ttext-align: right;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-image-gallery .lfr-asset-attributes {\n");
      out.write("\tclear: both;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-image-gallery .folder-search {\n");
      out.write("\tfloat: right;\n");
      out.write("\tmargin: 0 0 0.5em 0.5em;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-image-gallery .image-score {\n");
      out.write("\tdisplay: block;\n");
      out.write("\tmargin: 0 0 5px 35px;\n");
      out.write("\tpadding-top: 3px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-image-gallery .image-thumbnail {\n");
      out.write("\tborder: 2px solid #eee;\n");
      out.write("\tfloat: left;\n");
      out.write("\tmargin: 20px 4px 0;\n");
      out.write("\tpadding: 5px 5px 0;\n");
      out.write("\ttext-align: center;\n");
      out.write("\ttext-decoration: none;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-image-gallery .image-thumbnail:hover {\n");
      out.write("\tborder-color: #ccc;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-image-gallery .taglib-webdav {\n");
      out.write("\tmargin-top: 3em;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-image-gallery .image-title {\n");
      out.write("\tdisplay: block;\n");
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
