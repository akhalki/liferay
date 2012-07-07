package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.liferay.portal.kernel.captcha.CaptchaMaxChallengesException;
import com.liferay.portal.kernel.captcha.CaptchaTextException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PrefsParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.webform.util.WebFormUtil;
import javax.portlet.ActionRequest;
import javax.portlet.PortletPreferences;

public final class configuration_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(7);
    _jspx_dependants.add("/init.jsp");
    _jspx_dependants.add("/WEB-INF/tld/c.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-portlet.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-aui.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-theme.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-ui.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-util.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletConfiguration_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffieldset;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fcssClass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dlocalized_0026_005fxml_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dlocalized_0026_005fxml_005ftype_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel_005fcssClass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005fresourceURL_0026_005fvar_005fportletName;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonclick_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletName;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_002drow;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletConfiguration;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletConfiguration_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffieldset = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fcssClass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dlocalized_0026_005fxml_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dlocalized_0026_005fxml_005ftype_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel_005fcssClass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005fresourceURL_0026_005fvar_005fportletName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonclick_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_002drow = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletConfiguration = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletConfiguration_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.release();
    _005fjspx_005ftagPool_005faui_005ffieldset.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fcssClass.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dlocalized_0026_005fxml_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dlocalized_0026_005fxml_005ftype_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel_005fcssClass.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005fresourceURL_0026_005fvar_005fportletName.release();
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonclick_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletName.release();
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fbutton_002drow.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletConfiguration.release();
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
      out.write("\n");
      out.write("\n");
      out.write("\n");
      //  portlet:defineObjects
      com.liferay.taglib.portlet.DefineObjectsTag _jspx_th_portlet_005fdefineObjects_005f0 = (com.liferay.taglib.portlet.DefineObjectsTag) _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.get(com.liferay.taglib.portlet.DefineObjectsTag.class);
      _jspx_th_portlet_005fdefineObjects_005f0.setPageContext(_jspx_page_context);
      _jspx_th_portlet_005fdefineObjects_005f0.setParent(null);
      int _jspx_eval_portlet_005fdefineObjects_005f0 = _jspx_th_portlet_005fdefineObjects_005f0.doStartTag();
      if (_jspx_th_portlet_005fdefineObjects_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.reuse(_jspx_th_portlet_005fdefineObjects_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.reuse(_jspx_th_portlet_005fdefineObjects_005f0);
      javax.portlet.ActionRequest actionRequest = null;
      javax.portlet.ActionResponse actionResponse = null;
      javax.portlet.EventRequest eventRequest = null;
      javax.portlet.EventResponse eventResponse = null;
      javax.portlet.PortletConfig portletConfig = null;
      java.lang.String portletName = null;
      javax.portlet.PortletPreferences portletPreferences = null;
      java.util.Map portletPreferencesValues = null;
      javax.portlet.PortletSession portletSession = null;
      java.util.Map portletSessionScope = null;
      javax.portlet.RenderRequest renderRequest = null;
      javax.portlet.RenderResponse renderResponse = null;
      javax.portlet.ResourceRequest resourceRequest = null;
      javax.portlet.ResourceResponse resourceResponse = null;
      actionRequest = (javax.portlet.ActionRequest) _jspx_page_context.findAttribute("actionRequest");
      actionResponse = (javax.portlet.ActionResponse) _jspx_page_context.findAttribute("actionResponse");
      eventRequest = (javax.portlet.EventRequest) _jspx_page_context.findAttribute("eventRequest");
      eventResponse = (javax.portlet.EventResponse) _jspx_page_context.findAttribute("eventResponse");
      portletConfig = (javax.portlet.PortletConfig) _jspx_page_context.findAttribute("portletConfig");
      portletName = (java.lang.String) _jspx_page_context.findAttribute("portletName");
      portletPreferences = (javax.portlet.PortletPreferences) _jspx_page_context.findAttribute("portletPreferences");
      portletPreferencesValues = (java.util.Map) _jspx_page_context.findAttribute("portletPreferencesValues");
      portletSession = (javax.portlet.PortletSession) _jspx_page_context.findAttribute("portletSession");
      portletSessionScope = (java.util.Map) _jspx_page_context.findAttribute("portletSessionScope");
      renderRequest = (javax.portlet.RenderRequest) _jspx_page_context.findAttribute("renderRequest");
      renderResponse = (javax.portlet.RenderResponse) _jspx_page_context.findAttribute("renderResponse");
      resourceRequest = (javax.portlet.ResourceRequest) _jspx_page_context.findAttribute("resourceRequest");
      resourceResponse = (javax.portlet.ResourceResponse) _jspx_page_context.findAttribute("resourceResponse");
      out.write('\n');
      out.write('\n');
      //  liferay-theme:defineObjects
      com.liferay.taglib.theme.DefineObjectsTag _jspx_th_liferay_002dtheme_005fdefineObjects_005f0 = (com.liferay.taglib.theme.DefineObjectsTag) _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.get(com.liferay.taglib.theme.DefineObjectsTag.class);
      _jspx_th_liferay_002dtheme_005fdefineObjects_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dtheme_005fdefineObjects_005f0.setParent(null);
      int _jspx_eval_liferay_002dtheme_005fdefineObjects_005f0 = _jspx_th_liferay_002dtheme_005fdefineObjects_005f0.doStartTag();
      if (_jspx_th_liferay_002dtheme_005fdefineObjects_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.reuse(_jspx_th_liferay_002dtheme_005fdefineObjects_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.reuse(_jspx_th_liferay_002dtheme_005fdefineObjects_005f0);
      com.liferay.portal.theme.ThemeDisplay themeDisplay = null;
      com.liferay.portal.model.Company company = null;
      com.liferay.portal.model.Account account = null;
      com.liferay.portal.model.User user = null;
      com.liferay.portal.model.User realUser = null;
      com.liferay.portal.model.Contact contact = null;
      com.liferay.portal.model.Layout layout = null;
      java.util.List layouts = null;
      java.lang.Long plid = null;
      com.liferay.portal.model.LayoutTypePortlet layoutTypePortlet = null;
      java.lang.Long scopeGroupId = null;
      com.liferay.portal.security.permission.PermissionChecker permissionChecker = null;
      java.util.Locale locale = null;
      java.util.TimeZone timeZone = null;
      com.liferay.portal.model.Theme theme = null;
      com.liferay.portal.model.ColorScheme colorScheme = null;
      com.liferay.portal.theme.PortletDisplay portletDisplay = null;
      java.lang.Long portletGroupId = null;
      themeDisplay = (com.liferay.portal.theme.ThemeDisplay) _jspx_page_context.findAttribute("themeDisplay");
      company = (com.liferay.portal.model.Company) _jspx_page_context.findAttribute("company");
      account = (com.liferay.portal.model.Account) _jspx_page_context.findAttribute("account");
      user = (com.liferay.portal.model.User) _jspx_page_context.findAttribute("user");
      realUser = (com.liferay.portal.model.User) _jspx_page_context.findAttribute("realUser");
      contact = (com.liferay.portal.model.Contact) _jspx_page_context.findAttribute("contact");
      layout = (com.liferay.portal.model.Layout) _jspx_page_context.findAttribute("layout");
      layouts = (java.util.List) _jspx_page_context.findAttribute("layouts");
      plid = (java.lang.Long) _jspx_page_context.findAttribute("plid");
      layoutTypePortlet = (com.liferay.portal.model.LayoutTypePortlet) _jspx_page_context.findAttribute("layoutTypePortlet");
      scopeGroupId = (java.lang.Long) _jspx_page_context.findAttribute("scopeGroupId");
      permissionChecker = (com.liferay.portal.security.permission.PermissionChecker) _jspx_page_context.findAttribute("permissionChecker");
      locale = (java.util.Locale) _jspx_page_context.findAttribute("locale");
      timeZone = (java.util.TimeZone) _jspx_page_context.findAttribute("timeZone");
      theme = (com.liferay.portal.model.Theme) _jspx_page_context.findAttribute("theme");
      colorScheme = (com.liferay.portal.model.ColorScheme) _jspx_page_context.findAttribute("colorScheme");
      portletDisplay = (com.liferay.portal.theme.PortletDisplay) _jspx_page_context.findAttribute("portletDisplay");
      portletGroupId = (java.lang.Long) _jspx_page_context.findAttribute("portletGroupId");
      out.write('\n');
      out.write('\n');

String currentURL = PortalUtil.getCurrentURL(request);

PortletPreferences preferences = null;

if (renderRequest != null) {
	preferences = renderRequest.getPreferences();
}

String portletResource = ParamUtil.getString(request, "portletResource");

if (Validator.isNotNull(portletResource)) {
	preferences = PortletPreferencesFactoryUtil.getPortletSetup(request, portletResource);
}

      out.write('\n');
      out.write('\n');

String redirect = ParamUtil.getString(renderRequest, "redirect");

String titleXml = LocalizationUtil.getLocalizationXmlFromPreferences(preferences, renderRequest, "title");
String descriptionXml = LocalizationUtil.getLocalizationXmlFromPreferences(preferences, renderRequest, "description");
boolean requireCaptcha = PrefsParamUtil.getBoolean(preferences, renderRequest, "requireCaptcha");
String successURL = PrefsParamUtil.getString(preferences, renderRequest, "successURL");

boolean sendAsEmail = PrefsParamUtil.getBoolean(preferences, renderRequest, "sendAsEmail");
String subject = PrefsParamUtil.getString(preferences, renderRequest, "subject");
String emailAddress = PrefsParamUtil.getString(preferences, renderRequest, "emailAddress");

boolean saveToDatabase = PrefsParamUtil.getBoolean(preferences, renderRequest, "saveToDatabase");
String databaseTableName = preferences.getValue("databaseTableName", StringPool.BLANK);

boolean saveToFile = PrefsParamUtil.getBoolean(preferences, renderRequest, "saveToFile");
String fileName = PrefsParamUtil.getString(preferences, renderRequest, "fileName");

boolean fieldsEditingDisabled = false;

if (WebFormUtil.getTableRowsCount(company.getCompanyId(), databaseTableName) > 0) {
	fieldsEditingDisabled = true;
}

      out.write('\n');
      out.write('\n');
      //  liferay-portlet:actionURL
      com.liferay.taglib.portlet.ActionURLTag _jspx_th_liferay_002dportlet_005factionURL_005f0 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletConfiguration_005fnobody.get(com.liferay.taglib.portlet.ActionURLTag.class);
      _jspx_th_liferay_002dportlet_005factionURL_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dportlet_005factionURL_005f0.setParent(null);
      // /configuration.jsp(44,0) name = portletConfiguration type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dportlet_005factionURL_005f0.setPortletConfiguration(true);
      // /configuration.jsp(44,0) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dportlet_005factionURL_005f0.setVar("configurationURL");
      int _jspx_eval_liferay_002dportlet_005factionURL_005f0 = _jspx_th_liferay_002dportlet_005factionURL_005f0.doStartTag();
      if (_jspx_th_liferay_002dportlet_005factionURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletConfiguration_005fnobody.reuse(_jspx_th_liferay_002dportlet_005factionURL_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletConfiguration_005fnobody.reuse(_jspx_th_liferay_002dportlet_005factionURL_005f0);
      java.lang.String configurationURL = null;
      configurationURL = (java.lang.String) _jspx_page_context.findAttribute("configurationURL");
      out.write('\n');
      out.write('\n');
      //  aui:form
      com.liferay.taglib.aui.FormTag _jspx_th_aui_005fform_005f0 = (com.liferay.taglib.aui.FormTag) _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction.get(com.liferay.taglib.aui.FormTag.class);
      _jspx_th_aui_005fform_005f0.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fform_005f0.setParent(null);
      // /configuration.jsp(46,0) name = action type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fform_005f0.setAction( configurationURL );
      // /configuration.jsp(46,0) null
      _jspx_th_aui_005fform_005f0.setDynamicAttribute(null, "method", new String("post"));
      // /configuration.jsp(46,0) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fform_005f0.setName("fm");
      int _jspx_eval_aui_005fform_005f0 = _jspx_th_aui_005fform_005f0.doStartTag();
      if (_jspx_eval_aui_005fform_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_aui_005fform_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_aui_005fform_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_aui_005fform_005f0.doInitBody();
        }
        do {
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f0 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /configuration.jsp(47,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f0.setName( Constants.CMD );
          // /configuration.jsp(47,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f0.setType("hidden");
          // /configuration.jsp(47,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f0.setValue( Constants.UPDATE );
          int _jspx_eval_aui_005finput_005f0 = _jspx_th_aui_005finput_005f0.doStartTag();
          if (_jspx_th_aui_005finput_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f1 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f1.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /configuration.jsp(48,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f1.setName("redirect");
          // /configuration.jsp(48,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f1.setType("hidden");
          // /configuration.jsp(48,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f1.setValue( redirect );
          int _jspx_eval_aui_005finput_005f1 = _jspx_th_aui_005finput_005f1.doStartTag();
          if (_jspx_th_aui_005finput_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  liferay-ui:panel-container
          com.liferay.taglib.ui.PanelContainerTag _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0 = (com.liferay.taglib.ui.PanelContainerTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended.get(com.liferay.taglib.ui.PanelContainerTag.class);
          _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /configuration.jsp(50,1) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setId("webFormConfiguration");
          // /configuration.jsp(50,1) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setExtended( Boolean.TRUE );
          // /configuration.jsp(50,1) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setPersistState( true );
          int _jspx_eval_liferay_002dui_005fpanel_002dcontainer_005f0 = _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.doStartTag();
          if (_jspx_eval_liferay_002dui_005fpanel_002dcontainer_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_liferay_002dui_005fpanel_002dcontainer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.doInitBody();
            }
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  liferay-ui:panel
              com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f0 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.get(com.liferay.taglib.ui.PanelTag.class);
              _jspx_th_liferay_002dui_005fpanel_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005fpanel_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
              // /configuration.jsp(51,2) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_005f0.setId("webFormGeneral");
              // /configuration.jsp(51,2) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_005f0.setTitle( LanguageUtil.get(pageContext, "form-information") );
              // /configuration.jsp(51,2) name = collapsible type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_005f0.setCollapsible( true );
              // /configuration.jsp(51,2) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_005f0.setPersistState( true );
              // /configuration.jsp(51,2) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_005f0.setExtended( true );
              int _jspx_eval_liferay_002dui_005fpanel_005f0 = _jspx_th_liferay_002dui_005fpanel_005f0.doStartTag();
              if (_jspx_eval_liferay_002dui_005fpanel_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_liferay_002dui_005fpanel_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_liferay_002dui_005fpanel_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_liferay_002dui_005fpanel_005f0.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:fieldset
                  com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f0 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset.get(com.liferay.taglib.aui.FieldsetTag.class);
                  _jspx_th_aui_005ffieldset_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005ffieldset_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f0);
                  int _jspx_eval_aui_005ffieldset_005f0 = _jspx_th_aui_005ffieldset_005f0.doStartTag();
                  if (_jspx_eval_aui_005ffieldset_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005ffieldset_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005ffieldset_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005ffieldset_005f0.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  liferay-ui:error
                      com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f0 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                      _jspx_th_liferay_002dui_005ferror_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ferror_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
                      // /configuration.jsp(53,4) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ferror_005f0.setKey("titleRequired");
                      // /configuration.jsp(53,4) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ferror_005f0.setMessage("please-enter-a-title");
                      int _jspx_eval_liferay_002dui_005ferror_005f0 = _jspx_th_liferay_002dui_005ferror_005f0.doStartTag();
                      if (_jspx_th_liferay_002dui_005ferror_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f0);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  aui:field-wrapper
                      com.liferay.taglib.aui.FieldWrapperTag _jspx_th_aui_005ffield_002dwrapper_005f0 = (com.liferay.taglib.aui.FieldWrapperTag) _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fcssClass.get(com.liferay.taglib.aui.FieldWrapperTag.class);
                      _jspx_th_aui_005ffield_002dwrapper_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005ffield_002dwrapper_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
                      // /configuration.jsp(55,4) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005ffield_002dwrapper_005f0.setCssClass("lfr-input-text-container");
                      // /configuration.jsp(55,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005ffield_002dwrapper_005f0.setLabel("title");
                      int _jspx_eval_aui_005ffield_002dwrapper_005f0 = _jspx_th_aui_005ffield_002dwrapper_005f0.doStartTag();
                      if (_jspx_eval_aui_005ffield_002dwrapper_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_aui_005ffield_002dwrapper_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_aui_005ffield_002dwrapper_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_aui_005ffield_002dwrapper_005f0.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  liferay-ui:input-localized
                          com.liferay.taglib.ui.InputLocalizedTag _jspx_th_liferay_002dui_005finput_002dlocalized_005f0 = (com.liferay.taglib.ui.InputLocalizedTag) _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dlocalized_0026_005fxml_005fname_005fnobody.get(com.liferay.taglib.ui.InputLocalizedTag.class);
                          _jspx_th_liferay_002dui_005finput_002dlocalized_005f0.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dui_005finput_002dlocalized_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f0);
                          // /configuration.jsp(56,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005finput_002dlocalized_005f0.setName("title");
                          // /configuration.jsp(56,5) name = xml type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005finput_002dlocalized_005f0.setXml( titleXml );
                          int _jspx_eval_liferay_002dui_005finput_002dlocalized_005f0 = _jspx_th_liferay_002dui_005finput_002dlocalized_005f0.doStartTag();
                          if (_jspx_th_liferay_002dui_005finput_002dlocalized_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dlocalized_0026_005fxml_005fname_005fnobody.reuse(_jspx_th_liferay_002dui_005finput_002dlocalized_005f0);
                            return;
                          }
                          _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dlocalized_0026_005fxml_005fname_005fnobody.reuse(_jspx_th_liferay_002dui_005finput_002dlocalized_005f0);
                          out.write("\n");
                          out.write("\t\t\t\t");
                          int evalDoAfterBody = _jspx_th_aui_005ffield_002dwrapper_005f0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_aui_005ffield_002dwrapper_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.popBody();
                        }
                      }
                      if (_jspx_th_aui_005ffield_002dwrapper_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fcssClass.reuse(_jspx_th_aui_005ffield_002dwrapper_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fcssClass.reuse(_jspx_th_aui_005ffield_002dwrapper_005f0);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  aui:field-wrapper
                      com.liferay.taglib.aui.FieldWrapperTag _jspx_th_aui_005ffield_002dwrapper_005f1 = (com.liferay.taglib.aui.FieldWrapperTag) _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fcssClass.get(com.liferay.taglib.aui.FieldWrapperTag.class);
                      _jspx_th_aui_005ffield_002dwrapper_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005ffield_002dwrapper_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
                      // /configuration.jsp(59,4) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005ffield_002dwrapper_005f1.setCssClass("lfr-textarea-container");
                      // /configuration.jsp(59,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005ffield_002dwrapper_005f1.setLabel("description");
                      int _jspx_eval_aui_005ffield_002dwrapper_005f1 = _jspx_th_aui_005ffield_002dwrapper_005f1.doStartTag();
                      if (_jspx_eval_aui_005ffield_002dwrapper_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_aui_005ffield_002dwrapper_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_aui_005ffield_002dwrapper_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_aui_005ffield_002dwrapper_005f1.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  liferay-ui:input-localized
                          com.liferay.taglib.ui.InputLocalizedTag _jspx_th_liferay_002dui_005finput_002dlocalized_005f1 = (com.liferay.taglib.ui.InputLocalizedTag) _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dlocalized_0026_005fxml_005ftype_005fname_005fnobody.get(com.liferay.taglib.ui.InputLocalizedTag.class);
                          _jspx_th_liferay_002dui_005finput_002dlocalized_005f1.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dui_005finput_002dlocalized_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f1);
                          // /configuration.jsp(60,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005finput_002dlocalized_005f1.setName("description");
                          // /configuration.jsp(60,5) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005finput_002dlocalized_005f1.setType("textarea");
                          // /configuration.jsp(60,5) name = xml type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005finput_002dlocalized_005f1.setXml( descriptionXml );
                          int _jspx_eval_liferay_002dui_005finput_002dlocalized_005f1 = _jspx_th_liferay_002dui_005finput_002dlocalized_005f1.doStartTag();
                          if (_jspx_th_liferay_002dui_005finput_002dlocalized_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dlocalized_0026_005fxml_005ftype_005fname_005fnobody.reuse(_jspx_th_liferay_002dui_005finput_002dlocalized_005f1);
                            return;
                          }
                          _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dlocalized_0026_005fxml_005ftype_005fname_005fnobody.reuse(_jspx_th_liferay_002dui_005finput_002dlocalized_005f1);
                          out.write("\n");
                          out.write("\t\t\t\t");
                          int evalDoAfterBody = _jspx_th_aui_005ffield_002dwrapper_005f1.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_aui_005ffield_002dwrapper_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.popBody();
                        }
                      }
                      if (_jspx_th_aui_005ffield_002dwrapper_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fcssClass.reuse(_jspx_th_aui_005ffield_002dwrapper_005f1);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fcssClass.reuse(_jspx_th_aui_005ffield_002dwrapper_005f1);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f2 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f2.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
                      // /configuration.jsp(63,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f2.setName("requireCaptcha");
                      // /configuration.jsp(63,4) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f2.setType("checkbox");
                      // /configuration.jsp(63,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f2.setValue( requireCaptcha );
                      int _jspx_eval_aui_005finput_005f2 = _jspx_th_aui_005finput_005f2.doStartTag();
                      if (_jspx_th_aui_005finput_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f2);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f2);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f3 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f3.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
                      // /configuration.jsp(65,4) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f3.setCssClass("lfr-input-text-container");
                      // /configuration.jsp(65,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f3.setLabel("redirect-url-on-success");
                      // /configuration.jsp(65,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f3.setName("successURL");
                      // /configuration.jsp(65,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f3.setValue( HtmlUtil.toInputSafe(successURL) );
                      int _jspx_eval_aui_005finput_005f3 = _jspx_th_aui_005finput_005f3.doStartTag();
                      if (_jspx_th_aui_005finput_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f3);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f3);
                      out.write("\n");
                      out.write("\t\t\t");
                      int evalDoAfterBody = _jspx_th_aui_005ffieldset_005f0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_aui_005ffieldset_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_aui_005ffieldset_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f0);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_liferay_002dui_005fpanel_005f0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_liferay_002dui_005fpanel_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_liferay_002dui_005fpanel_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.reuse(_jspx_th_liferay_002dui_005fpanel_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.reuse(_jspx_th_liferay_002dui_005fpanel_005f0);
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  liferay-ui:panel
              com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f1 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.get(com.liferay.taglib.ui.PanelTag.class);
              _jspx_th_liferay_002dui_005fpanel_005f1.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005fpanel_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
              // /configuration.jsp(69,2) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_005f1.setId("webFormData");
              // /configuration.jsp(69,2) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_005f1.setTitle( LanguageUtil.get(pageContext, "handling-of-form-data") );
              // /configuration.jsp(69,2) name = collapsible type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_005f1.setCollapsible( true );
              // /configuration.jsp(69,2) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_005f1.setPersistState( true );
              // /configuration.jsp(69,2) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_005f1.setExtended( true );
              int _jspx_eval_liferay_002dui_005fpanel_005f1 = _jspx_th_liferay_002dui_005fpanel_005f1.doStartTag();
              if (_jspx_eval_liferay_002dui_005fpanel_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_liferay_002dui_005fpanel_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_liferay_002dui_005fpanel_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_liferay_002dui_005fpanel_005f1.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:fieldset
                  com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f1 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel_005fcssClass.get(com.liferay.taglib.aui.FieldsetTag.class);
                  _jspx_th_aui_005ffieldset_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005ffieldset_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f1);
                  // /configuration.jsp(70,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005ffieldset_005f1.setCssClass("handle-data");
                  // /configuration.jsp(70,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005ffieldset_005f1.setLabel("email");
                  int _jspx_eval_aui_005ffieldset_005f1 = _jspx_th_aui_005ffieldset_005f1.doStartTag();
                  if (_jspx_eval_aui_005ffieldset_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005ffieldset_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005ffieldset_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005ffieldset_005f1.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  liferay-ui:error
                      com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f1 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                      _jspx_th_liferay_002dui_005ferror_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ferror_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f1);
                      // /configuration.jsp(71,4) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ferror_005f1.setKey("subjectRequired");
                      // /configuration.jsp(71,4) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ferror_005f1.setMessage("please-enter-a-subject");
                      int _jspx_eval_liferay_002dui_005ferror_005f1 = _jspx_th_liferay_002dui_005ferror_005f1.doStartTag();
                      if (_jspx_th_liferay_002dui_005ferror_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f1);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f1);
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  liferay-ui:error
                      com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f2 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                      _jspx_th_liferay_002dui_005ferror_005f2.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ferror_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f1);
                      // /configuration.jsp(72,4) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ferror_005f2.setKey("handlingRequired");
                      // /configuration.jsp(72,4) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ferror_005f2.setMessage("please-select-an-action-for-the-handling-of-form-data");
                      int _jspx_eval_liferay_002dui_005ferror_005f2 = _jspx_th_liferay_002dui_005ferror_005f2.doStartTag();
                      if (_jspx_th_liferay_002dui_005ferror_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f2);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f2);
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  liferay-ui:error
                      com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f3 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                      _jspx_th_liferay_002dui_005ferror_005f3.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ferror_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f1);
                      // /configuration.jsp(73,4) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ferror_005f3.setKey("emailAddressInvalid");
                      // /configuration.jsp(73,4) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ferror_005f3.setMessage("please-enter-a-valid-email-address");
                      int _jspx_eval_liferay_002dui_005ferror_005f3 = _jspx_th_liferay_002dui_005ferror_005f3.doStartTag();
                      if (_jspx_th_liferay_002dui_005ferror_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f3);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f3);
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  liferay-ui:error
                      com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f4 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                      _jspx_th_liferay_002dui_005ferror_005f4.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ferror_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f1);
                      // /configuration.jsp(74,4) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ferror_005f4.setKey("emailAddressRequired");
                      // /configuration.jsp(74,4) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ferror_005f4.setMessage("please-enter-an-email-address");
                      int _jspx_eval_liferay_002dui_005ferror_005f4 = _jspx_th_liferay_002dui_005ferror_005f4.doStartTag();
                      if (_jspx_th_liferay_002dui_005ferror_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f4);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f4);
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  liferay-ui:error
                      com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f5 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                      _jspx_th_liferay_002dui_005ferror_005f5.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ferror_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f1);
                      // /configuration.jsp(75,4) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ferror_005f5.setKey("fileNameInvalid");
                      // /configuration.jsp(75,4) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ferror_005f5.setMessage("please-enter-a-valid-path-and-filename");
                      int _jspx_eval_liferay_002dui_005ferror_005f5 = _jspx_th_liferay_002dui_005ferror_005f5.doStartTag();
                      if (_jspx_th_liferay_002dui_005ferror_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f5);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f5);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f4 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f4.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f1);
                      // /configuration.jsp(77,4) name = inlineLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f4.setInlineLabel("left");
                      // /configuration.jsp(77,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f4.setLabel("send-as-email");
                      // /configuration.jsp(77,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f4.setName("sendAsEmail");
                      // /configuration.jsp(77,4) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f4.setType("checkbox");
                      // /configuration.jsp(77,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f4.setValue( sendAsEmail );
                      int _jspx_eval_aui_005finput_005f4 = _jspx_th_aui_005finput_005f4.doStartTag();
                      if (_jspx_th_aui_005finput_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f4);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f4);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f5 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f5.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f1);
                      // /configuration.jsp(79,4) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f5.setCssClass("lfr-input-text-container");
                      // /configuration.jsp(79,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f5.setName("subject");
                      // /configuration.jsp(79,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f5.setValue( subject );
                      int _jspx_eval_aui_005finput_005f5 = _jspx_th_aui_005finput_005f5.doStartTag();
                      if (_jspx_th_aui_005finput_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f5);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f5);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f6 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f6.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f1);
                      // /configuration.jsp(81,4) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f6.setCssClass("lfr-input-text-container");
                      // /configuration.jsp(81,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f6.setName("emailAddress");
                      // /configuration.jsp(81,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f6.setValue( emailAddress );
                      int _jspx_eval_aui_005finput_005f6 = _jspx_th_aui_005finput_005f6.doStartTag();
                      if (_jspx_th_aui_005finput_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f6);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f6);
                      out.write("\n");
                      out.write("\t\t\t");
                      int evalDoAfterBody = _jspx_th_aui_005ffieldset_005f1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_aui_005ffieldset_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_aui_005ffieldset_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel_005fcssClass.reuse(_jspx_th_aui_005ffieldset_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel_005fcssClass.reuse(_jspx_th_aui_005ffieldset_005f1);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:fieldset
                  com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f2 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel_005fcssClass.get(com.liferay.taglib.aui.FieldsetTag.class);
                  _jspx_th_aui_005ffieldset_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005ffieldset_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f1);
                  // /configuration.jsp(84,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005ffieldset_005f2.setCssClass("handle-data");
                  // /configuration.jsp(84,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005ffieldset_005f2.setLabel("database");
                  int _jspx_eval_aui_005ffieldset_005f2 = _jspx_th_aui_005ffieldset_005f2.doStartTag();
                  if (_jspx_eval_aui_005ffieldset_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005ffieldset_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005ffieldset_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005ffieldset_005f2.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f7 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f7.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f2);
                      // /configuration.jsp(85,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f7.setName("saveToDatabase");
                      // /configuration.jsp(85,4) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f7.setType("checkbox");
                      // /configuration.jsp(85,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f7.setValue( saveToDatabase );
                      int _jspx_eval_aui_005finput_005f7 = _jspx_th_aui_005finput_005f7.doStartTag();
                      if (_jspx_th_aui_005finput_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f7);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f7);
                      out.write("\n");
                      out.write("\t\t\t");
                      int evalDoAfterBody = _jspx_th_aui_005ffieldset_005f2.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_aui_005ffieldset_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_aui_005ffieldset_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel_005fcssClass.reuse(_jspx_th_aui_005ffieldset_005f2);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel_005fcssClass.reuse(_jspx_th_aui_005ffieldset_005f2);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:fieldset
                  com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f3 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel_005fcssClass.get(com.liferay.taglib.aui.FieldsetTag.class);
                  _jspx_th_aui_005ffieldset_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005ffieldset_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f1);
                  // /configuration.jsp(88,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005ffieldset_005f3.setCssClass("handle-data");
                  // /configuration.jsp(88,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005ffieldset_005f3.setLabel("file");
                  int _jspx_eval_aui_005ffieldset_005f3 = _jspx_th_aui_005ffieldset_005f3.doStartTag();
                  if (_jspx_eval_aui_005ffieldset_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005ffieldset_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005ffieldset_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005ffieldset_005f3.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f8 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f8.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f3);
                      // /configuration.jsp(89,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f8.setName("saveToFile");
                      // /configuration.jsp(89,4) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f8.setType("checkbox");
                      // /configuration.jsp(89,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f8.setValue( saveToFile );
                      int _jspx_eval_aui_005finput_005f8 = _jspx_th_aui_005finput_005f8.doStartTag();
                      if (_jspx_th_aui_005finput_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f8);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f8);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f9 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f9.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f3);
                      // /configuration.jsp(91,4) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f9.setCssClass("lfr-input-text-container");
                      // /configuration.jsp(91,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f9.setLabel("path-and-file-name");
                      // /configuration.jsp(91,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f9.setName("filename");
                      // /configuration.jsp(91,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f9.setValue( fileName );
                      int _jspx_eval_aui_005finput_005f9 = _jspx_th_aui_005finput_005f9.doStartTag();
                      if (_jspx_th_aui_005finput_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f9);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f9);
                      out.write("\n");
                      out.write("\t\t\t");
                      int evalDoAfterBody = _jspx_th_aui_005ffieldset_005f3.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_aui_005ffieldset_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_aui_005ffieldset_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel_005fcssClass.reuse(_jspx_th_aui_005ffieldset_005f3);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel_005fcssClass.reuse(_jspx_th_aui_005ffieldset_005f3);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_liferay_002dui_005fpanel_005f1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_liferay_002dui_005fpanel_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_liferay_002dui_005fpanel_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.reuse(_jspx_th_liferay_002dui_005fpanel_005f1);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.reuse(_jspx_th_liferay_002dui_005fpanel_005f1);
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  liferay-ui:panel
              com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f2 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.get(com.liferay.taglib.ui.PanelTag.class);
              _jspx_th_liferay_002dui_005fpanel_005f2.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005fpanel_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
              // /configuration.jsp(95,2) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_005f2.setId("webFormFields");
              // /configuration.jsp(95,2) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_005f2.setTitle( LanguageUtil.get(pageContext, "form-fields") );
              // /configuration.jsp(95,2) name = collapsible type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_005f2.setCollapsible( true );
              // /configuration.jsp(95,2) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_005f2.setPersistState( true );
              // /configuration.jsp(95,2) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_005f2.setExtended( true );
              int _jspx_eval_liferay_002dui_005fpanel_005f2 = _jspx_th_liferay_002dui_005fpanel_005f2.doStartTag();
              if (_jspx_eval_liferay_002dui_005fpanel_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_liferay_002dui_005fpanel_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_liferay_002dui_005fpanel_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_liferay_002dui_005fpanel_005f2.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:fieldset
                  com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f4 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass.get(com.liferay.taglib.aui.FieldsetTag.class);
                  _jspx_th_aui_005ffieldset_005f4.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005ffieldset_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
                  // /configuration.jsp(96,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005ffieldset_005f4.setCssClass("rows-container webFields");
                  int _jspx_eval_aui_005ffieldset_005f4 = _jspx_th_aui_005ffieldset_005f4.doStartTag();
                  if (_jspx_eval_aui_005ffieldset_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005ffieldset_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005ffieldset_005f4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005ffieldset_005f4.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                      // /configuration.jsp(97,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f0.setTest( fieldsEditingDisabled );
                      int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
                      if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t<div class=\"portlet-msg-alert\">\n");
                          out.write("\t\t\t\t\t\t");
                          if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_th_c_005fif_005f0, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\t\t\t\t\t</div>\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  liferay-portlet:resourceURL
                          com.liferay.taglib.portlet.ResourceURLTag _jspx_th_liferay_002dportlet_005fresourceURL_005f0 = (com.liferay.taglib.portlet.ResourceURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005fresourceURL_0026_005fvar_005fportletName.get(com.liferay.taglib.portlet.ResourceURLTag.class);
                          _jspx_th_liferay_002dportlet_005fresourceURL_005f0.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dportlet_005fresourceURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
                          // /configuration.jsp(102,5) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dportlet_005fresourceURL_005f0.setVar("exportURL");
                          // /configuration.jsp(102,5) name = portletName type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dportlet_005fresourceURL_005f0.setPortletName( portletResource );
                          int _jspx_eval_liferay_002dportlet_005fresourceURL_005f0 = _jspx_th_liferay_002dportlet_005fresourceURL_005f0.doStartTag();
                          if (_jspx_eval_liferay_002dportlet_005fresourceURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_liferay_002dportlet_005fresourceURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dportlet_005fresourceURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dportlet_005fresourceURL_005f0.doInitBody();
                            }
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f0 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005fresourceURL_005f0);
                              // /configuration.jsp(103,6) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f0.setName( Constants.CMD );
                              // /configuration.jsp(103,6) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f0.setValue("export");
                              int _jspx_eval_portlet_005fparam_005f0 = _jspx_th_portlet_005fparam_005f0.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dportlet_005fresourceURL_005f0.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_liferay_002dportlet_005fresourceURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                            }
                          }
                          if (_jspx_th_liferay_002dportlet_005fresourceURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fliferay_002dportlet_005fresourceURL_0026_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005fresourceURL_005f0);
                            return;
                          }
                          _005fjspx_005ftagPool_005fliferay_002dportlet_005fresourceURL_0026_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005fresourceURL_005f0);
                          java.lang.String exportURL = null;
                          exportURL = (java.lang.String) _jspx_page_context.findAttribute("exportURL");
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t");

					String taglibExport = "submitForm(document.hrefFm, '" + exportURL + "');";
					
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  aui:button
                          com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f0 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonclick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                          _jspx_th_aui_005fbutton_005f0.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005fbutton_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
                          // /configuration.jsp(110,5) null
                          _jspx_th_aui_005fbutton_005f0.setDynamicAttribute(null, "onclick",  taglibExport );
                          // /configuration.jsp(110,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005fbutton_005f0.setValue("export-data");
                          int _jspx_eval_aui_005fbutton_005f0 = _jspx_th_aui_005fbutton_005f0.doStartTag();
                          if (_jspx_th_aui_005fbutton_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonclick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonclick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  liferay-portlet:actionURL
                          com.liferay.taglib.portlet.ActionURLTag _jspx_th_liferay_002dportlet_005factionURL_005f1 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletName.get(com.liferay.taglib.portlet.ActionURLTag.class);
                          _jspx_th_liferay_002dportlet_005factionURL_005f1.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dportlet_005factionURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
                          // /configuration.jsp(112,5) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dportlet_005factionURL_005f1.setVar("deleteURL");
                          // /configuration.jsp(112,5) name = portletName type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dportlet_005factionURL_005f1.setPortletName( portletResource );
                          int _jspx_eval_liferay_002dportlet_005factionURL_005f1 = _jspx_th_liferay_002dportlet_005factionURL_005f1.doStartTag();
                          if (_jspx_eval_liferay_002dportlet_005factionURL_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_liferay_002dportlet_005factionURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dportlet_005factionURL_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dportlet_005factionURL_005f1.doInitBody();
                            }
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f1 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005factionURL_005f1);
                              // /configuration.jsp(113,6) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f1.setName( ActionRequest.ACTION_NAME );
                              // /configuration.jsp(113,6) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f1.setValue("deleteData");
                              int _jspx_eval_portlet_005fparam_005f1 = _jspx_th_portlet_005fparam_005f1.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f2 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005factionURL_005f1);
                              // /configuration.jsp(114,6) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f2.setName("redirect");
                              // /configuration.jsp(114,6) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f2.setValue( currentURL );
                              int _jspx_eval_portlet_005fparam_005f2 = _jspx_th_portlet_005fparam_005f2.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dportlet_005factionURL_005f1.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_liferay_002dportlet_005factionURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                            }
                          }
                          if (_jspx_th_liferay_002dportlet_005factionURL_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005factionURL_005f1);
                            return;
                          }
                          _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005factionURL_005f1);
                          java.lang.String deleteURL = null;
                          deleteURL = (java.lang.String) _jspx_page_context.findAttribute("deleteURL");
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t");

					String taglibDelete = "submitForm(document." + renderResponse.getNamespace() + "fm, '" + deleteURL + "');";
					
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  aui:button
                          com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f1 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonclick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                          _jspx_th_aui_005fbutton_005f1.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005fbutton_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
                          // /configuration.jsp(121,5) null
                          _jspx_th_aui_005fbutton_005f1.setDynamicAttribute(null, "onclick",  taglibDelete );
                          // /configuration.jsp(121,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005fbutton_005f1.setValue("delete-data");
                          int _jspx_eval_aui_005fbutton_005f1 = _jspx_th_aui_005fbutton_005f1.doStartTag();
                          if (_jspx_th_aui_005fbutton_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonclick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f1);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonclick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f1);
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t<br /><br />\n");
                          out.write("\t\t\t\t");
                          int evalDoAfterBody = _jspx_th_c_005fif_005f0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                      }
                      if (_jspx_th_c_005fif_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f10 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f10.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                      // /configuration.jsp(126,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f10.setName("updateFields");
                      // /configuration.jsp(126,4) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f10.setType("hidden");
                      // /configuration.jsp(126,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f10.setValue( !fieldsEditingDisabled );
                      int _jspx_eval_aui_005finput_005f10 = _jspx_th_aui_005finput_005f10.doStartTag();
                      if (_jspx_th_aui_005finput_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f10);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f10);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");

				String formFieldsIndexesParam = ParamUtil.getString(renderRequest, "formFieldsIndexes") ;

				int[] formFieldsIndexes = null;

				if (Validator.isNotNull(formFieldsIndexesParam)) {
					formFieldsIndexes = StringUtil.split(formFieldsIndexesParam, 0);
				}
				else {
					formFieldsIndexes = new int[0];

					for (int i = 1; true; i++) {
						String fieldLabel = PrefsParamUtil.getString(preferences, request, "fieldLabel" + i);

						if (Validator.isNull(fieldLabel)) {
							break;
						}

						formFieldsIndexes = ArrayUtil.append(formFieldsIndexes, i);
					}

					if (formFieldsIndexes.length == 0) {
						formFieldsIndexes = ArrayUtil.append(formFieldsIndexes, -1);
					}
				}

				int index = 1;

				for (int formFieldsIndex : formFieldsIndexes) {
					request.setAttribute("configuration.jsp-index", String.valueOf(index));
					request.setAttribute("configuration.jsp-formFieldsindex", String.valueOf(formFieldsIndex));
					request.setAttribute("configuration.jsp-fieldsEditingDisabled", String.valueOf(fieldsEditingDisabled));
				
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t<div class=\"lfr-form-row\" id=\"");
                      if (_jspx_meth_portlet_005fnamespace_005f0(_jspx_th_aui_005ffieldset_005f4, _jspx_page_context))
                        return;
                      out.write("fieldset");
                      out.print( formFieldsIndex );
                      out.write("\">\n");
                      out.write("\t\t\t\t\t\t<div class=\"row-fields\">\n");
                      out.write("\t\t\t\t\t\t\t");
                      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "edit_field.jsp", out, false);
                      out.write("\n");
                      out.write("\t\t\t\t\t\t</div>\n");
                      out.write("\t\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t");

					index++;
				}
				
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t");
                      int evalDoAfterBody = _jspx_th_aui_005ffieldset_005f4.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_aui_005ffieldset_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_aui_005ffieldset_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass.reuse(_jspx_th_aui_005ffieldset_005f4);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass.reuse(_jspx_th_aui_005ffieldset_005f4);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_liferay_002dui_005fpanel_005f2.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_liferay_002dui_005fpanel_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_liferay_002dui_005fpanel_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.reuse(_jspx_th_liferay_002dui_005fpanel_005f2);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.reuse(_jspx_th_liferay_002dui_005fpanel_005f2);
              out.write('\n');
              out.write('	');
              int evalDoAfterBody = _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_liferay_002dui_005fpanel_002dcontainer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended.reuse(_jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended.reuse(_jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
          out.write('\n');
          out.write('\n');
          out.write('	');
          if (_jspx_meth_aui_005fbutton_002drow_005f0(_jspx_th_aui_005fform_005f0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_aui_005fform_005f0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_aui_005fform_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
        }
      }
      if (_jspx_th_aui_005fform_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction.reuse(_jspx_th_aui_005fform_005f0);
        return;
      }
      _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction.reuse(_jspx_th_aui_005fform_005f0);
      out.write('\n');
      out.write('\n');

String modules = "aui-base";

if (!fieldsEditingDisabled) {
	modules += ",liferay-auto-fields";
}

      out.write('\n');
      out.write('\n');
      //  aui:script
      com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
      _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fscript_005f0.setParent(null);
      // /configuration.jsp(190,0) name = use type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fscript_005f0.setUse( modules );
      int _jspx_eval_aui_005fscript_005f0 = _jspx_th_aui_005fscript_005f0.doStartTag();
      if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_aui_005fscript_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_aui_005fscript_005f0.doInitBody();
        }
        do {
          out.write("\n");
          out.write("\tvar toggleOptions = function(event) {\n");
          out.write("\t\tvar select = this;\n");
          out.write("\n");
          out.write("\t\tvar formRow = select.ancestor('.lfr-form-row');\n");
          out.write("\t\tvar value = select.val();\n");
          out.write("\n");
          out.write("\t\tvar optionsDiv = formRow.one('.options');\n");
          out.write("\n");
          out.write("\t\tif (value == 'options' || value == 'radio') {\n");
          out.write("\t\t\toptionsDiv.all('label').show();\n");
          out.write("\t\t\toptionsDiv.show();\n");
          out.write("\t\t}\n");
          out.write("\t\telse if (value == 'paragraph') {\n");
          out.write("\n");
          out.write("\t\t\t// Show just the text field and not the labels since there\n");
          out.write("\t\t\t// are multiple choice inputs\n");
          out.write("\n");
          out.write("\t\t\toptionsDiv.all('label').hide();\n");
          out.write("\t\t\toptionsDiv.show();\n");
          out.write("\t\t}\n");
          out.write("\t\telse {\n");
          out.write("\t\t\toptionsDiv.hide();\n");
          out.write("\t\t}\n");
          out.write("\n");
          out.write("\t\tvar optionalControl = formRow.one('.optional-control');\n");
          out.write("\t\tvar labelName = formRow.one('.label-name');\n");
          out.write("\n");
          out.write("\t\tif (value == 'paragraph') {\n");
          out.write("\t\t\tvar inputName = labelName.one('input');\n");
          out.write("\n");
          out.write("\t\t\tinputName.val('");
          if (_jspx_meth_liferay_002dui_005fmessage_005f1(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("');\n");
          out.write("\t\t\tinputName.fire('change');\n");
          out.write("\n");
          out.write("\t\t\tlabelName.hide();\n");
          out.write("\t\t\toptionalControl.hide();\n");
          out.write("\n");
          out.write("\t\t\toptionalControl.all('input[type=\"checkbox\"]').attr('checked', 'true');\n");
          out.write("\t\t\toptionalControl.all('input[type=\"hidden\"]').attr('value', 'true');\n");
          out.write("\t\t}\n");
          out.write("\t\telse {\n");
          out.write("\t\t\toptionalControl.show();\n");
          out.write("\t\t\tlabelName.show();\n");
          out.write("\t\t}\n");
          out.write("\t};\n");
          out.write("\n");
          out.write("\tvar toggleValidationOptions = function(event) {\n");
          out.write("\t\tthis.next().toggle();\n");
          out.write("\t};\n");
          out.write("\n");
          out.write("\tvar webFields = A.one('.webFields');\n");
          out.write("\n");
          out.write("\twebFields.all('select').each(toggleOptions);\n");
          out.write("\n");
          out.write("\t");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          // /configuration.jsp(244,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f1.setTest( !fieldsEditingDisabled );
          int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
          if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\tA.delegate('change', toggleOptions, webFields, 'select');\n");
              out.write("\t\tA.delegate('click', toggleValidationOptions, webFields, '.validation-link');\n");
              out.write("\n");
              out.write("\t\tA.delegate(\n");
              out.write("\t\t\t'change',\n");
              out.write("\t\t\tfunction(event) {\n");
              out.write("\t\t\t\tvar input = event.currentTarget;\n");
              out.write("\t\t\t\tvar row = input.ancestor('.field-row');\n");
              out.write("\t\t\t\tvar label = row.one('.field-title');\n");
              out.write("\n");
              out.write("\t\t\t\tif (label) {\n");
              out.write("\t\t\t\t\tlabel.html(input.get('value'));\n");
              out.write("\t\t\t\t}\n");
              out.write("\t\t\t},\n");
              out.write("\t\t\twebFields,\n");
              out.write("\t\t\t'.label-name input'\n");
              out.write("\t\t);\n");
              out.write("\n");
              out.write("\t\t");
              //  liferay-portlet:renderURL
              com.liferay.taglib.portlet.RenderURLTag _jspx_th_liferay_002dportlet_005frenderURL_005f0 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletConfiguration.get(com.liferay.taglib.portlet.RenderURLTag.class);
              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
              // /configuration.jsp(263,2) name = portletConfiguration type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setPortletConfiguration(true);
              // /configuration.jsp(263,2) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setVar("editFieldURL");
              // /configuration.jsp(263,2) name = windowState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setWindowState( LiferayWindowState.EXCLUSIVE.toString() );
              int _jspx_eval_liferay_002dportlet_005frenderURL_005f0 = _jspx_th_liferay_002dportlet_005frenderURL_005f0.doStartTag();
              if (_jspx_eval_liferay_002dportlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_liferay_002dportlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_liferay_002dportlet_005frenderURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_liferay_002dportlet_005frenderURL_005f0.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f3 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f0);
                  // /configuration.jsp(264,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f3.setName( Constants.CMD );
                  // /configuration.jsp(264,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f3.setValue( Constants.ADD );
                  int _jspx_eval_portlet_005fparam_005f3 = _jspx_th_portlet_005fparam_005f3.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_liferay_002dportlet_005frenderURL_005f0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_liferay_002dportlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_liferay_002dportlet_005frenderURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletConfiguration.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletConfiguration.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f0);
              java.lang.String editFieldURL = null;
              editFieldURL = (java.lang.String) _jspx_page_context.findAttribute("editFieldURL");
              out.write("\n");
              out.write("\n");
              out.write("\t\tnew Liferay.AutoFields(\n");
              out.write("\t\t\t{\n");
              out.write("\t\t\t\tcontentBox: webFields,\n");
              out.write("\t\t\t\tfieldIndexes: '");
              if (_jspx_meth_portlet_005fnamespace_005f1(_jspx_th_c_005fif_005f1, _jspx_page_context))
                return;
              out.write("formFieldsIndexes',\n");
              out.write("\t\t\t\tsortable: true,\n");
              out.write("\t\t\t\tsortableHandle: '.field-label',\n");
              out.write("\t\t\t\turl: '");
              out.print( editFieldURL );
              out.write("'\n");
              out.write("\t\t\t}\n");
              out.write("\t\t).render();\n");
              out.write("\t");
              int evalDoAfterBody = _jspx_th_c_005fif_005f1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
          out.write('\n');
          int evalDoAfterBody = _jspx_th_aui_005fscript_005f0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
        }
      }
      if (_jspx_th_aui_005fscript_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f0);
        return;
      }
      _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f0);
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

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
    // /configuration.jsp(99,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("there-is-existing-form-data-please-export-and-delete-it-before-making-changes-to-the-fields");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f0 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
    int _jspx_eval_portlet_005fnamespace_005f0 = _jspx_th_portlet_005fnamespace_005f0.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_002drow_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button-row
    com.liferay.taglib.aui.ButtonRowTag _jspx_th_aui_005fbutton_002drow_005f0 = (com.liferay.taglib.aui.ButtonRowTag) _005fjspx_005ftagPool_005faui_005fbutton_002drow.get(com.liferay.taglib.aui.ButtonRowTag.class);
    _jspx_th_aui_005fbutton_002drow_005f0.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_002drow_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
    int _jspx_eval_aui_005fbutton_002drow_005f0 = _jspx_th_aui_005fbutton_002drow_005f0.doStartTag();
    if (_jspx_eval_aui_005fbutton_002drow_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_005fbutton_002drow_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_005fbutton_002drow_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_005fbutton_002drow_005f0.doInitBody();
      }
      do {
        out.write('\n');
        out.write('	');
        out.write('	');
        if (_jspx_meth_aui_005fbutton_005f2(_jspx_th_aui_005fbutton_002drow_005f0, _jspx_page_context))
          return true;
        out.write('\n');
        out.write('	');
        int evalDoAfterBody = _jspx_th_aui_005fbutton_002drow_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_aui_005fbutton_002drow_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_aui_005fbutton_002drow_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_002drow.reuse(_jspx_th_aui_005fbutton_002drow_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_002drow.reuse(_jspx_th_aui_005fbutton_002drow_005f0);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fbutton_002drow_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button
    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f2 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
    _jspx_th_aui_005fbutton_005f2.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f0);
    // /configuration.jsp(178,2) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f2.setType("submit");
    int _jspx_eval_aui_005fbutton_005f2 = _jspx_th_aui_005fbutton_005f2.doStartTag();
    if (_jspx_th_aui_005fbutton_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody.reuse(_jspx_th_aui_005fbutton_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody.reuse(_jspx_th_aui_005fbutton_005f2);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f1 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    // /configuration.jsp(221,18) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f1.setKey("paragraph");
    int _jspx_eval_liferay_002dui_005fmessage_005f1 = _jspx_th_liferay_002dui_005fmessage_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f1 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f1.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
    int _jspx_eval_portlet_005fnamespace_005f1 = _jspx_th_portlet_005fnamespace_005f1.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f1);
    return false;
  }
}
