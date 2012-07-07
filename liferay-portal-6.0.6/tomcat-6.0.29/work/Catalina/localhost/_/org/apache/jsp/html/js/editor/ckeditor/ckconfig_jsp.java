package org.apache.jsp.html.js.editor.ckeditor;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;

public final class ckconfig_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

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

String cssPath = ParamUtil.getString(request, "cssPath");

      out.write("\n");
      out.write("\n");
      out.write("CKEDITOR.addStylesSet(\n");
      out.write("\t'liferayStyles',\n");
      out.write("\t[\n");
      out.write("\n");
      out.write("\t// Block Styles\n");
      out.write("\n");
      out.write("\t{name: 'Normal', element : 'p'},\n");
      out.write("\t{name: 'Heading 1', element : 'h1'},\n");
      out.write("\t{name: 'Heading 2', element : 'h2'},\n");
      out.write("\t{name: 'Heading 3', element : 'h3'},\n");
      out.write("\t{name: 'Heading 4', element : 'h4'},\n");
      out.write("\n");
      out.write("\t//Special classes\n");
      out.write("\n");
      out.write("\t{name: 'Preformatted Text', element:'pre'},\n");
      out.write("\t{name: 'Cited Work', element:'cite'},\n");
      out.write("\t{name: 'Computer Code', element:'code'},\n");
      out.write("\n");
      out.write("\t//Custom styles\n");
      out.write("\n");
      out.write("\t{name : 'Info Message', element : 'div', attributes : {'class' : 'portlet-msg-info'}},\n");
      out.write("\t{name : 'Alert Message', element : 'div', attributes : {'class' : 'portlet-msg-alert'}},\n");
      out.write("\t{name : 'Error Message', element : 'div', attributes : {'class' : 'portlet-msg-error'}}\n");
      out.write("\t]\n");
      out.write(");\n");
      out.write("\n");
      out.write("CKEDITOR.config.contentsCss = '");
      out.print( HtmlUtil.escape(cssPath) );
      out.write("/main.css';\n");
      out.write("\n");
      out.write("CKEDITOR.config.entities = false;\n");
      out.write("\n");
      out.write("CKEDITOR.config.resize_enabled = false;\n");
      out.write("\n");
      out.write("CKEDITOR.config.height = 265;\n");
      out.write("\n");
      out.write("CKEDITOR.config.stylesCombo_stylesSet = 'liferayStyles';\n");
      out.write("\n");
      out.write("CKEDITOR.config.toolbar_liferay = [\n");
      out.write("\t['Styles', 'FontSize', '-', 'TextColor', 'BGColor'],\n");
      out.write("\t['Bold', 'Italic', 'Underline', 'StrikeThrough'],\n");
      out.write("\t['Subscript', 'Superscript'],\n");
      out.write("\t'/',\n");
      out.write("\t['Undo', 'Redo', '-', 'Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'SelectAll', 'RemoveFormat'],\n");
      out.write("\t['Find', 'Replace', 'SpellCheck'],\n");
      out.write("\t['OrderedList', 'UnorderedList', '-', 'Outdent', 'Indent'],\n");
      out.write("\t['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'],\n");
      out.write("\t'/',\n");
      out.write("\t['Source'],\n");
      out.write("\t['Link', 'Unlink', 'Anchor'],\n");
      out.write("\t['Image', 'Flash', 'Table', '-', 'Smiley', 'SpecialChar']\n");
      out.write("];\n");
      out.write("\n");
      out.write("CKEDITOR.config.toolbar_liferayArticle = [\n");
      out.write("\t['Styles', 'FontSize', '-', 'TextColor', 'BGColor'],\n");
      out.write("\t['Bold', 'Italic', 'Underline', 'StrikeThrough'],\n");
      out.write("\t['Subscript', 'Superscript'],\n");
      out.write("\t'/',\n");
      out.write("\t['Undo', 'Redo', '-', 'Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'SelectAll', 'RemoveFormat'],\n");
      out.write("\t['Find', 'Replace', 'SpellCheck'],\n");
      out.write("\t['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],\n");
      out.write("\t['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'],\n");
      out.write("\t'/',\n");
      out.write("\t['Source'],\n");
      out.write("\t['Link', 'Unlink', 'Anchor'],\n");
      out.write("\t['Image', 'Flash', 'Table', '-', 'Smiley', 'SpecialChar', 'LiferayPageBreak']\n");
      out.write("];\n");
      out.write("\n");
      out.write("CKEDITOR.config.toolbar_editInPlace = [\n");
      out.write("\t['Styles'],\n");
      out.write("\t['Bold', 'Italic', 'Underline', 'StrikeThrough'],\n");
      out.write("\t['Subscript', 'Superscript', 'SpecialChar'],\n");
      out.write("\t['Undo', 'Redo'],\n");
      out.write("\t['SpellCheck'],\n");
      out.write("\t['OrderedList', 'UnorderedList', '-', 'Outdent', 'Indent'], ['Source', 'RemoveFormat'],\n");
      out.write("];\n");
      out.write("\n");
      out.write("CKEDITOR.config.toolbar_email = [\n");
      out.write("\t['FontSize', 'TextColor', 'BGColor', '-', 'Bold', 'Italic', 'Underline', 'StrikeThrough'],\n");
      out.write("\t['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'],\n");
      out.write("\t['SpellCheck'],\n");
      out.write("\t'/',\n");
      out.write("\t['Undo', 'Redo', '-', 'Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'SelectAll', 'RemoveFormat'],\n");
      out.write("\t['Source'],\n");
      out.write("\t['Link', 'Unlink'],\n");
      out.write("\t['Image']\n");
      out.write("];");
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
