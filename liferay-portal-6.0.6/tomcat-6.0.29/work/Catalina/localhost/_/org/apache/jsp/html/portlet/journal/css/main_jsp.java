package org.apache.jsp.html.portlet.journal.css;

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
      out.write(".journal-article-component-list {\n");
      out.write("\tmargin: 10px 0 0 0;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .add-permission-button-row {\n");
      out.write("\tfloat: left;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .article-separator {\n");
      out.write("\tclear: both;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .subscribe-link {\n");
      out.write("\tfloat: right;\n");
      out.write("\tmargin-bottom: 1em;\n");
      out.write("\tmargin-top: 2em;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-template-error .scroll-pane {\n");
      out.write("\tborder: 1px solid #BFBFBF;\n");
      out.write("\tmax-height: 200px;\n");
      out.write("\tmin-height: 50px;\n");
      out.write("\toverflow: auto;\n");
      out.write("\twidth: 100%;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-template-error .scroll-pane .inner-scroll-pane {\n");
      out.write("\tmin-width: 104%;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-template-error .scroll-pane .error-line {\n");
      out.write("\tbackground: #fdd;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-template-error .scroll-pane pre {\n");
      out.write("\tmargin: 0;\n");
      out.write("\twhite-space: pre;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-template-error .scroll-pane pre span {\n");
      out.write("\tbackground: #B5BFC4;\n");
      out.write("\tborder-right: 1px solid #BFBFBF;\n");
      out.write("\tdisplay: block;\n");
      out.write("\tfloat: left;\n");
      out.write("\tmargin-right: 4px;\n");
      out.write("\tpadding-right: 4px;\n");
      out.write("\ttext-align: right;\n");
      out.write("\twidth: 40px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .lfr-panel-basic .lfr-panel-title {\n");
      out.write("\tborder-bottom: 1px solid #ccc;\n");
      out.write("\tfloat: none;\n");
      out.write("\tposition: relative;\n");
      out.write("\ttop: -0.5em;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .lfr-panel-basic .lfr-panel-title span {\n");
      out.write("\tbackground: #fff;\n");
      out.write("\tpadding: 0 8px 0 4px;\n");
      out.write("\tposition: relative;\n");
      out.write("\ttop: 0.55em;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .lfr-panel-basic .lfr-panel-content {\n");
      out.write("\tbackground-color: #F0F5F7;\n");
      out.write("\tpadding: 10px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .lfr-panel-basic .lfr-tag-selector-input {\n");
      out.write("\twidth: 100%;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-extras {\n");
      out.write("\tborder-width: 0;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-extras .lfr-panel {\n");
      out.write("\tmargin-bottom: 1em;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-article-container ul {\n");
      out.write("\tmargin: 0;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal li {\n");
      out.write("\tlist-style: none;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .structure-tree {\n");
      out.write("\tposition: relative;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-edit-mode .structure-tree li.structure-field.repeated-field {\n");
      out.write("\tbackground: #F7FAFB;\n");
      out.write("\tborder: 1px dashed #C6D9F0;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .structure-tree li.structure-field.repeatable-border {\n");
      out.write("\tbackground: #F7FAFB;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-edit-mode .structure-tree li.structure-field.repeated-field.selected {\n");
      out.write("\tborder: 1px dashed #C3E7CC;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-edit-mode .structure-tree span.folder, .portlet-journal-edit-mode .structure-tree span.file {\n");
      out.write("\tdisplay: block;\n");
      out.write("\tpadding: 10px;\n");
      out.write("\tpadding-top: 0;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .structure-tree .placeholder {\n");
      out.write("\tdisplay: none;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .component-group .aui-tree-placeholder {\n");
      out.write("\tdisplay: none;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-edit-mode .structure-tree .aui-placeholder, .portlet-journal-edit-mode .structure-tree .aui-tree-placeholder, .portlet-journal-edit-mode .structure-tree .aui-tree-sub-placeholder {\n");
      out.write("\t-ms-filter: alpha(opacity=75);\n");
      out.write("\tbackground: #fff;\n");
      out.write("\tborder: 1px #cdcdcd dashed;\n");
      out.write("\tfilter: alpha(opacity=75);\n");
      out.write("\theight: 100px;\n");
      out.write("\topacity: 0.75;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .structure-tree .aui-tree-sub-placeholder {\n");
      out.write("\tmargin-top: 10px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .structure-tree li {\n");
      out.write("\tborder-top: 1px solid #CCC;\n");
      out.write("\tmargin: 10px;\n");
      out.write("\tpadding-top: 5px;\n");
      out.write("\tposition: relative;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-edit-mode .structure-tree li {\n");
      out.write("\tbackground: url(");
      out.print( themeImagesPath );
      out.write("/journal/form_builder_bg.png);\n");
      out.write("\tborder: 1px #C6D9F0 solid;\n");
      out.write("\tmargin: 15px;\n");
      out.write("\tpadding: 10px 10px 10px 22px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-edit-mode .structure-tree li.parent-structure-field {\n");
      out.write("\tbackground: none;\n");
      out.write("\tbackground-color: #FFFFE6;\n");
      out.write("\tborder: 1px dotted #FFE67F;\n");
      out.write("\tpadding-bottom: 30px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".ie .portlet-journal .structure-tree li {\n");
      out.write("\tzoom: 1;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .structure-tree .folder .field-container .journal-article-required-message {\n");
      out.write("\tdisplay: none;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .structure-tree .folder .field-container.required-field .journal-article-required-message {\n");
      out.write("\tdisplay: block;\n");
      out.write("\tmargin: 0;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-article-localized-checkbox {\n");
      out.write("\tdisplay: block;\n");
      out.write("\tmargin-top: 10px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .localization-disabled .journal-article-language-options, .portlet-journal .localization-disabled .structure-field .journal-article-localized-checkbox, .portlet-journal-edit-mode .portlet-journal .structure-field .journal-article-localized-checkbox {\n");
      out.write("\tdisplay: none;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-article-header-edit .journal-article-localized-checkbox {\n");
      out.write("\tmargin-bottom: 10px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-edit-mode .structure-tree li.structure-field.selected {\n");
      out.write("\tbackground: #EBFFEE;\n");
      out.write("\tborder: 1px #C3E7CC solid;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .structure-tree .journal-article-field-label {\n");
      out.write("\tdisplay: block;\n");
      out.write("\tfont-weight: bold;\n");
      out.write("\tmargin-left: 3px;\n");
      out.write("\tpadding-bottom: 5px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .structure-tree .journal-subfield input {\n");
      out.write("\tfloat: left;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .structure-tree .journal-subfield .journal-article-field-label {\n");
      out.write("\tfloat: left;\n");
      out.write("\tfont-weight: normal;\n");
      out.write("\tpadding: 0 0 0 3px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .structure-tree .journal-article-move-handler {\n");
      out.write("\tdisplay: none;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-edit-mode .structure-tree li.structure-field.yui3-dd-draggable .journal-article-move-handler {\n");
      out.write("\tbackground: transparent url(");
      out.print( themeImagesPath );
      out.write("/application/handle_sort_vertical.png) no-repeat scroll right 50%;\n");
      out.write("\tcursor: move;\n");
      out.write("\tdisplay: block;\n");
      out.write("\theight: 20px;\n");
      out.write("\tleft: 7px;\n");
      out.write("\tposition: absolute;\n");
      out.write("\ttop: 8px;\n");
      out.write("\twidth: 16px;\n");
      out.write("\tz-index: 420;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .structure-tree .journal-article-localized {\n");
      out.write("\tfont-size: 13px;\n");
      out.write("\tpadding-top: 5px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-edit-mode .structure-tree li.structure-field .journal-article-buttons {\n");
      out.write("\tdisplay: block;\n");
      out.write("\theight: 27px;\n");
      out.write("\tmargin-top: 18px;\n");
      out.write("\ttext-align: right;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-article-buttons, .portlet-journal .structure-tree li.structure-field.repeated-field .journal-article-variable-name {\n");
      out.write("\tdisplay: none;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-article-buttons .edit-button, .portlet-journal .journal-article-buttons .repeatable-button {\n");
      out.write("\tfloat: left;\n");
      out.write("\tmargin-left: 3px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-article-variable-name .aui-field-label {\n");
      out.write("\tfont-weight: normal;\n");
      out.write("\tmargin-right: 5px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .component-group-title {\n");
      out.write("\tfont-size: 12px;\n");
      out.write("\tfont-weight: bold;\n");
      out.write("\ttext-decoration: underline;\n");
      out.write("\tpadding: 4px 0 0;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-article-component-container {\n");
      out.write("\tmargin: 3px;\n");
      out.write("\toverflow: hidden;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-component {\n");
      out.write("\tcolor: #0E3F6F;\n");
      out.write("\tcursor: move;\n");
      out.write("\tline-height: 25px;\n");
      out.write("\tpadding-left: 30px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-component.dragging {\n");
      out.write("\tfont-weight: bold;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-article-instructions-container {\n");
      out.write("\tdisplay: normal;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-component {\n");
      out.write("\tbackground: transparent url() no-repeat scroll 3px 3px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-component-text {\n");
      out.write("\tbackground-image: url(");
      out.print( themeImagesPath );
      out.write("/journal/text_field.png);\n");
      out.write("\tbackground-position: 3px 9px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-component-textbox {\n");
      out.write("\tbackground-image: url(");
      out.print( themeImagesPath );
      out.write("/journal/textbox.png);\n");
      out.write("\tbackground-position: 3px 6px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-component-textarea {\n");
      out.write("\tbackground-image: url(");
      out.print( themeImagesPath );
      out.write("/journal/textarea.png);\n");
      out.write("\tbackground-position: 3px 4px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-component-image {\n");
      out.write("\tbackground-image: url(");
      out.print( themeImagesPath );
      out.write("/journal/image_uploader.png);\n");
      out.write("\tbackground-position: 3px 7px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-component-imagegallery {\n");
      out.write("\tbackground-image: url(");
      out.print( themeImagesPath );
      out.write("/journal/image_gallery.png);\n");
      out.write("\tbackground-position: 3px 5px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-component-documentlibrary {\n");
      out.write("\tbackground-image: url(");
      out.print( themeImagesPath );
      out.write("/journal/document_library.png);\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-component-boolean {\n");
      out.write("\tbackground-image: url(");
      out.print( themeImagesPath );
      out.write("/journal/checkbox.png);\n");
      out.write("\tbackground-position: 3px 7px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-component-options {\n");
      out.write("\tbackground-image: url(");
      out.print( themeImagesPath );
      out.write("/journal/options.png);\n");
      out.write("\tbackground-position: 3px 5px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-component-list {\n");
      out.write("\tbackground-image: url(");
      out.print( themeImagesPath );
      out.write("/journal/selectbox.png);\n");
      out.write("\tbackground-position: 3px 9px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-component-multilist {\n");
      out.write("\tbackground-image: url(");
      out.print( themeImagesPath );
      out.write("/journal/multiselection_list.png);\n");
      out.write("\tbackground-position: 3px 4px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-component-linktolayout {\n");
      out.write("\tbackground-image: url(");
      out.print( themeImagesPath );
      out.write("/journal/link_to_page.png);\n");
      out.write("\tbackground-position: 3px 9px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-component-formgroup {\n");
      out.write("\tbackground-image: url(");
      out.print( themeImagesPath );
      out.write("/journal/form_group.png);\n");
      out.write("\tbackground-position: 3px 5px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-component-selectionbreak {\n");
      out.write("\tbackground-image: url(");
      out.print( themeImagesPath );
      out.write("/journal/selection_break.png);\n");
      out.write("\tbackground-position: 3px 12px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-helper .journal-component {\n");
      out.write("\theight: 25px;\n");
      out.write("\tline-height: 25px;\n");
      out.write("\tmargin-left: 5px;\n");
      out.write("\tpadding-left: 25px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".component-group .component-dragging {\n");
      out.write("\tbackground-color: #fff !important;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".component-group.form-controls {\n");
      out.write("\tborder-top: 1px solid #E0ECFF;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-field-template {\n");
      out.write("\tdisplay: none;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-fieldmodel-container {\n");
      out.write("\tdisplay: none;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-icon-button {\n");
      out.write("\tcursor: pointer;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-edit-mode .structure-tree li.structure-field .journal-article-close {\n");
      out.write("\tbackground: url(");
      out.print( themeImagesPath );
      out.write("/journal/form_builder_close.png);\n");
      out.write("\tcursor: pointer;\n");
      out.write("\tdisplay: block;\n");
      out.write("\theight: 29px;\n");
      out.write("\tposition: absolute;\n");
      out.write("\tright: -10px;\n");
      out.write("\ttop: -9px;\n");
      out.write("\twidth: 29px;\n");
      out.write("\tz-index: 420;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-helper {\n");
      out.write("\tbackground: #dedede;\n");
      out.write("\tborder: 1px #555 dashed;\n");
      out.write("\tcursor: move;\n");
      out.write("\topacity: 0.8;\n");
      out.write("\tposition: absolute;\n");
      out.write("\tvisibility: hidden;\n");
      out.write("\twidth: 100px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".ie .journal-article-helper {\n");
      out.write("\t-ms-filter: alpha(opacity=80);\n");
      out.write("}\n");
      out.write("\n");
      out.write(".ie6 .journal-article-helper, .ie7 .journal-article-helper {\n");
      out.write("\tfilter: alpha(opacity=80);\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-helper.aui-draggable-dragging {\n");
      out.write("\tfont-size: 15px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-helper.not-intersecting .forbidden-action {\n");
      out.write("\tbackground: url(");
      out.print( themeImagesPath );
      out.write("/application/forbidden_action.png) no-repeat;\n");
      out.write("\theight: 32px;\n");
      out.write("\tposition: absolute;\n");
      out.write("\tright: -15px;\n");
      out.write("\ttop: -15px;\n");
      out.write("\twidth: 32px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".ie .journal-article-helper.not-intersecting .forbidden-action {\n");
      out.write("\tright: 2px;\n");
      out.write("\ttop: 2px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .portlet-section-header td {\n");
      out.write("\tbackground: #CFE5FF;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-form-presentation-label {\n");
      out.write("\tcolor: #0E3F6F;\n");
      out.write("\tpadding-top: 3px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-edit-field-control, .portlet-journal .journal-list-subfield .journal-icon-button {\n");
      out.write("\tdisplay: none;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-edit-mode .structure-tree li.structure-field .journal-edit-field-control {\n");
      out.write("\tdisplay: block;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-edit-mode .journal-list-subfield .journal-icon-button {\n");
      out.write("\tdisplay: inline;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-icon-button span img {\n");
      out.write("\tmargin-bottom: 3px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-article-instructions-message {\n");
      out.write("\tmargin: 5px 0 0 0;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".ie .journal-article-edit-field-wrapper form {\n");
      out.write("\twidth: auto;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-edit-field-wrapper.aui-overlaycontextpanel-container,\n");
      out.write(".journal-article-edit-field-wrapper .aui-overlaycontextpanel-container {\n");
      out.write("\n");
      out.write("\tbackground-color: #EBFFEE;\n");
      out.write("\tborder-color: #C3E7CC;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-edit-field-wrapper.aui-overlaycontextpanel {\n");
      out.write("\tmargin: 0 13px 0 0;\n");
      out.write("\tpadding: 0;\n");
      out.write("\tposition: relative;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-edit-field-wrapper.aui-overlaycontextpanel-arrow-tl .aui-overlaycontextpanel-pointer-inner {\n");
      out.write("\tborder-bottom: 10px solid #EBFFEE;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-edit-field-wrapper .container-close {\n");
      out.write("\tdisplay: none;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-edit-field {\n");
      out.write("\tpadding: 5px;\n");
      out.write("\tposition: relative;\n");
      out.write("\twidth: 180px;\n");
      out.write("\tz-index: 420;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-edit-field-wrapper .cancel-button, .journal-article-edit-field-wrapper .save-button {\n");
      out.write("\tdisplay: none;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-edit-field-wrapper.save-mode .close-button {\n");
      out.write("\tdisplay: none;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-edit-field-wrapper.save-mode .save-button, .journal-article-edit-field-wrapper.save-mode .cancel-button {\n");
      out.write("\tdisplay: inline;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-edit-field .aui-field {\n");
      out.write("\tpadding: 0;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-edit-field strong {\n");
      out.write("\tfont-size: 14px;\n");
      out.write("\ttext-decoration: underline;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-edit-field .journal-edit-label {\n");
      out.write("\tmargin-top: 10px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-edit-field .aui-field .textarea {\n");
      out.write("\theight: 6em;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".structure-message {\n");
      out.write("\tmargin-top: 5px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".structure-name-label {\n");
      out.write("\tfont-weight: bold;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".save-structure-template-dialog textarea {\n");
      out.write("\theight: 150px;\n");
      out.write("\twidth: 450px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".save-structure-name {\n");
      out.write("\twidth: 470px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".save-structure-description {\n");
      out.write("\theight: 150px;\n");
      out.write("\twidth: 470px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .structure-controls {\n");
      out.write("\tmargin-top: 5px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .structure-links {\n");
      out.write("\tdisplay: block;\n");
      out.write("\tmargin-top: 5px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .structure-links a {\n");
      out.write("\tmargin-right: 10px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .default-link {\n");
      out.write("\tfont-size: 0.9em;\n");
      out.write("\tfont-weight: normal;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .journal-image-preview {\n");
      out.write("\tborder: 1px dotted;\n");
      out.write("\tmargin-top: 2px;\n");
      out.write("\toverflow: scroll;\n");
      out.write("\tpadding: 4px;\n");
      out.write("\twidth: 500px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-edit-field-wrapper .user-instructions {\n");
      out.write("\tborder-width: 0;\n");
      out.write("\tpadding: 0;\n");
      out.write("\tmargin-bottom: 1em;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".journal-article-edit-field-wrapper .button-holder {\n");
      out.write("\tmargin-top: 1.5em;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .repeatable-field-image {\n");
      out.write("\tcursor: pointer;\n");
      out.write("\tposition: absolute;\n");
      out.write("\tright: 0;\n");
      out.write("\ttop: 0;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal .lfr-textarea {\n");
      out.write("\twidth: 350px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-edit-mode .journal-article-header-edit, .portlet-journal-edit-mode #journalAbstractPanel, .portlet-journal-edit-mode #journalCategorizationPanel, .portlet-journal-edit-mode #journalSchedulePanel, .portlet-journal-edit-mode .journal-article-button-row, .portlet-journal-edit-mode .panel-page-menu, .portlet-journal-edit-mode .journal-article-permissions, .portlet-journal-edit-mode .repeatable-field-image, .portlet-journal-edit-mode .structure-tree li.structure-field .journal-article-instructions-container, .portlet-journal-edit-mode .structure-tree li.parent-structure-field .journal-article-close, .portlet-journal-edit-mode .structure-tree li.parent-structure-field .journal-delete-field, .portlet-journal-edit-mode .structure-tree li.repeated-field .journal-edit-field-control, .portlet-journal-edit-mode .structure-tree li.repeated-field .journal-delete-field {\n");
      out.write("\tdisplay: none;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".portlet-journal-edit-mode .panel-page-application {\n");
      out.write("\tfloat: none;\n");
      out.write("\twidth: 100%;\n");
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
