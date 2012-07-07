package org.apache.jsp.html.js.editor;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.util.TextFormatter;

public final class ckeditor_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


public static final String DEFAULT_INIT_METHOD = "initEditor";

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

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

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");

long plid = ParamUtil.getLong(request, "p_l_id");
String mainPath = ParamUtil.getString(request, "p_main_path");
String doAsUserId = ParamUtil.getString(request, "doAsUserId");
String initMethod =	ParamUtil.getString(request, "initMethod", DEFAULT_INIT_METHOD);
String onChangeMethod = ParamUtil.getString(request, "onChangeMethod");
String toolbarSet = ParamUtil.getString(request, "toolbarSet", "liferay");
String cssPath = ParamUtil.getString(request, "cssPath");
String cssClasses = ParamUtil.getString(request, "cssClasses");

      out.write("\n");
      out.write("\n");
      out.write("<html>\n");
      out.write("\n");
      out.write("<head>\n");
      out.write("\t<style type=\"text/css\">\n");
      out.write("\t\ttable.cke_dialog {\n");
      out.write("\t\t\tposition: absolute !important;\n");
      out.write("\t\t}\n");
      out.write("\t</style>\n");
      out.write("\n");
      out.write("\t<script type=\"text/javascript\" src=\"ckeditor/ckeditor.js\"></script>\n");
      out.write("\n");
      out.write("\t<script type=\"text/javascript\">\n");
      out.write("\t\tfunction initCkArea() {\n");
      out.write("\t\t\tvar textArea = document.getElementById(\"CKEditor1\");\n");
      out.write("\t\t\tvar ckEditor = CKEDITOR.instances.CKEditor1;\n");
      out.write("\n");
      out.write("\t\t\tckEditor.setData(parent.");
      out.print( HtmlUtil.escape(initMethod) );
      out.write("());\n");
      out.write("\n");
      out.write("\t\t\tCKEDITOR.config.toolbar = '");
      out.print( TextFormatter.format(HtmlUtil.escape(toolbarSet), TextFormatter.M) );
      out.write("';\n");
      out.write("\t\t\tCKEDITOR.config.customConfig = '");
      out.print( request.getContextPath() );
      out.write("/html/js/editor/ckeditor/ckconfig.jsp?p_l_id=");
      out.print( plid );
      out.write("&p_main_path=");
      out.print( HttpUtil.encodeURL(mainPath) );
      out.write("&doAsUserId=");
      out.print( HttpUtil.encodeURL(doAsUserId) );
      out.write("&cssPath=");
      out.print( HttpUtil.encodeURL(cssPath) );
      out.write("&cssClasses=");
      out.print( HttpUtil.encodeURL(cssClasses) );
      out.write("';\n");
      out.write("\n");
      out.write("\t\t\tckEditor.on(\n");
      out.write("\t\t\t\t'instanceReady',\n");
      out.write("\t\t\t\tfunction() {\n");
      out.write("\t\t\t\t\tsetInterval(\n");
      out.write("\t\t\t\t\t\tfunction() {\n");
      out.write("\t\t\t\t\t\t\ttry {\n");
      out.write("\t\t\t\t\t\t\t\tonChangeCallback();\n");
      out.write("\t\t\t\t\t\t\t}\n");
      out.write("\t\t\t\t\t\t\tcatch(e) {\n");
      out.write("\t\t\t\t\t\t\t}\n");
      out.write("\t\t\t\t\t\t},\n");
      out.write("\t\t\t\t\t\t300\n");
      out.write("\t\t\t\t\t);\n");
      out.write("\t\t\t\t}\n");
      out.write("\t\t\t);\n");
      out.write("\t\t}\n");
      out.write("\n");
      out.write("\t\tfunction getCkData() {\n");
      out.write("\t\t\tvar data = CKEDITOR.instances.CKEditor1.getData();\n");
      out.write("\n");
      out.write("\t\t\tif (CKEDITOR.env.gecko && (CKEDITOR.tools.trim(data) == '<br />')) {\n");
      out.write("\t\t\t\tdata = '';\n");
      out.write("\t\t\t}\n");
      out.write("\n");
      out.write("\t\t\treturn data;\n");
      out.write("\t\t}\n");
      out.write("\n");
      out.write("\t\tfunction getHTML() {\n");
      out.write("\t\t\treturn getCkData();\n");
      out.write("\t\t}\n");
      out.write("\n");
      out.write("\t\tfunction getText() {\n");
      out.write("\t\t\treturn getCkData();\n");
      out.write("\t\t}\n");
      out.write("\n");
      out.write("\t\tfunction onChangeCallback() {\n");
      out.write("\n");
      out.write("\t\t\t");

			if (Validator.isNotNull(onChangeMethod)) {
			
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\tvar ckEditor = CKEDITOR.instances.CKEditor1;\n");
      out.write("\t\t\t\tvar dirty = ckEditor.checkDirty();\n");
      out.write("\n");
      out.write("\t\t\t\tif (dirty) {\n");
      out.write("\t\t\t\t\tparent.");
      out.print( HtmlUtil.escape(onChangeMethod) );
      out.write("(getText());\n");
      out.write("\n");
      out.write("\t\t\t\t\tckEditor.resetDirty();\n");
      out.write("\t\t\t\t}\n");
      out.write("\n");
      out.write("\t\t\t");

			}
			
      out.write("\n");
      out.write("\n");
      out.write("\t\t}\n");
      out.write("\t</script>\n");
      out.write("</head>\n");
      out.write("\n");
      out.write("<body>\n");
      out.write("\n");
      out.write("<textarea id=\"CKEditor1\" name=\"CKEditor1\"></textarea>\n");
      out.write("\n");
      out.write("<script type=\"text/javascript\">\n");
      out.write("\n");
      out.write("\t");

	String connectorURL = HttpUtil.encodeURL(mainPath + "/portal/fckeditor?p_l_id=" + plid + "&doAsUserId=" + HttpUtil.encodeURL(doAsUserId));
	
      out.write("\n");
      out.write("\n");
      out.write("\tCKEDITOR.replace(\n");
      out.write("\t\t'CKEditor1',\n");
      out.write("\t\t{\n");
      out.write("\t\t\tfilebrowserBrowseUrl: '");
      out.print( request.getContextPath() );
      out.write("/html/js/editor/ckeditor/editor/filemanager/browser/liferay/browser.html?Connector=");
      out.print( connectorURL );
      out.write("',\n");
      out.write("\t\t\tfilebrowserUploadUrl: '");
      out.print( request.getContextPath() );
      out.write("/html/js/editor/ckeditor/editor/filemanager/browser/liferay/frmupload.html?Connector=");
      out.print( connectorURL );
      out.write("'\n");
      out.write("\t\t}\n");
      out.write("\t);\n");
      out.write("\n");
      out.write("\tif (parent.AUI) {\n");
      out.write("\t\tparent.AUI().on('domready', initCkArea);\n");
      out.write("\t}\n");
      out.write("</script>\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("\n");
      out.write("</html>\n");
      out.write("\n");
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
