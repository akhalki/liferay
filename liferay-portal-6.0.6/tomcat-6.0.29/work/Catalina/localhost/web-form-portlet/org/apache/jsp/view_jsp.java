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

public final class view_jsp extends org.apache.jasper.runtime.HttpJspBase
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
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fwrap_005fvalue_005ftype_005fname_005flabel_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005fname_005flabel_005fcssClass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fchecked_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005fcssClass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fresourceURL_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fcaptcha_0026_005furl_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fwrap_005fvalue_005ftype_005fname_005flabel_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005fname_005flabel_005fcssClass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fchecked_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005fcssClass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fresourceURL_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fcaptcha_0026_005furl_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.release();
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fwrap_005fvalue_005ftype_005fname_005flabel_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005fname_005flabel_005fcssClass.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fchecked_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005fcssClass.release();
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected.release();
    _005fjspx_005ftagPool_005fportlet_005fresourceURL_0026_005fvar.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fcaptcha_0026_005furl_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.release();
    _005fjspx_005ftagPool_005fc_005fotherwise.release();
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

String title = LocalizationUtil.getPreferencesValue(preferences, "title", themeDisplay.getLanguageId());
String description = LocalizationUtil.getPreferencesValue(preferences, "description", themeDisplay.getLanguageId());
boolean requireCaptcha = GetterUtil.getBoolean(preferences.getValue("requireCaptcha", StringPool.BLANK));
String successURL = preferences.getValue("successURL", StringPool.BLANK);

      out.write('\n');
      out.write('\n');
      //  portlet:actionURL
      com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f0 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.get(com.liferay.taglib.portlet.ActionURLTag.class);
      _jspx_th_portlet_005factionURL_005f0.setPageContext(_jspx_page_context);
      _jspx_th_portlet_005factionURL_005f0.setParent(null);
      // /view.jsp(26,0) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_portlet_005factionURL_005f0.setVar("saveDataURL");
      int _jspx_eval_portlet_005factionURL_005f0 = _jspx_th_portlet_005factionURL_005f0.doStartTag();
      if (_jspx_eval_portlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_portlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_portlet_005factionURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_portlet_005factionURL_005f0.doInitBody();
        }
        do {
          out.write('\n');
          out.write('	');
          //  portlet:param
          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f0 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
          _jspx_th_portlet_005fparam_005f0.setPageContext(_jspx_page_context);
          _jspx_th_portlet_005fparam_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
          // /view.jsp(27,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_portlet_005fparam_005f0.setName( ActionRequest.ACTION_NAME );
          // /view.jsp(27,1) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_portlet_005fparam_005f0.setValue("saveData");
          int _jspx_eval_portlet_005fparam_005f0 = _jspx_th_portlet_005fparam_005f0.doStartTag();
          if (_jspx_th_portlet_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
          out.write('\n');
          int evalDoAfterBody = _jspx_th_portlet_005factionURL_005f0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_portlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
        }
      }
      if (_jspx_th_portlet_005factionURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.reuse(_jspx_th_portlet_005factionURL_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.reuse(_jspx_th_portlet_005factionURL_005f0);
      java.lang.String saveDataURL = null;
      saveDataURL = (java.lang.String) _jspx_page_context.findAttribute("saveDataURL");
      out.write('\n');
      out.write('\n');
      //  aui:form
      com.liferay.taglib.aui.FormTag _jspx_th_aui_005fform_005f0 = (com.liferay.taglib.aui.FormTag) _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction.get(com.liferay.taglib.aui.FormTag.class);
      _jspx_th_aui_005fform_005f0.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fform_005f0.setParent(null);
      // /view.jsp(30,0) name = action type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fform_005f0.setAction( saveDataURL );
      // /view.jsp(30,0) null
      _jspx_th_aui_005fform_005f0.setDynamicAttribute(null, "method", new String("post"));
      // /view.jsp(30,0) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
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
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /view.jsp(31,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f0.setTest( Validator.isNull(successURL) );
          int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
          if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  aui:input
              com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f0 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
              _jspx_th_aui_005finput_005f0.setPageContext(_jspx_page_context);
              _jspx_th_aui_005finput_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
              // /view.jsp(32,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f0.setName("redirect");
              // /view.jsp(32,2) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f0.setType("hidden");
              // /view.jsp(32,2) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f0.setValue( currentURL );
              int _jspx_eval_aui_005finput_005f0 = _jspx_th_aui_005finput_005f0.doStartTag();
              if (_jspx_th_aui_005finput_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
                return;
              }
              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
              out.write('\n');
              out.write('	');
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
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  aui:fieldset
          com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f0 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel.get(com.liferay.taglib.aui.FieldsetTag.class);
          _jspx_th_aui_005ffieldset_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005ffieldset_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /view.jsp(35,1) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005ffieldset_005f0.setLabel( HtmlUtil.escape(title) );
          int _jspx_eval_aui_005ffieldset_005f0 = _jspx_th_aui_005ffieldset_005f0.doStartTag();
          if (_jspx_eval_aui_005ffieldset_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_aui_005ffieldset_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_aui_005ffieldset_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_aui_005ffieldset_005f0.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\t\t<em class=\"description\">");
              out.print( HtmlUtil.escape(description) );
              out.write("</em>\n");
              out.write("\n");
              out.write("\t\t");
              if (_jspx_meth_liferay_002dui_005fsuccess_005f0(_jspx_th_aui_005ffieldset_005f0, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  liferay-ui:error
              com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f0 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
              _jspx_th_liferay_002dui_005ferror_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005ferror_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
              // /view.jsp(40,2) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ferror_005f0.setException( CaptchaMaxChallengesException.class );
              // /view.jsp(40,2) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ferror_005f0.setMessage("maximum-number-of-captcha-attempts-exceeded");
              int _jspx_eval_liferay_002dui_005ferror_005f0 = _jspx_th_liferay_002dui_005ferror_005f0.doStartTag();
              if (_jspx_th_liferay_002dui_005ferror_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f0);
              out.write('\n');
              out.write('	');
              out.write('	');
              //  liferay-ui:error
              com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f1 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
              _jspx_th_liferay_002dui_005ferror_005f1.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005ferror_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
              // /view.jsp(41,2) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ferror_005f1.setException( CaptchaTextException.class );
              // /view.jsp(41,2) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ferror_005f1.setMessage("text-verification-failed");
              int _jspx_eval_liferay_002dui_005ferror_005f1 = _jspx_th_liferay_002dui_005ferror_005f1.doStartTag();
              if (_jspx_th_liferay_002dui_005ferror_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f1);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f1);
              out.write('\n');
              out.write('	');
              out.write('	');
              //  liferay-ui:error
              com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f2 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
              _jspx_th_liferay_002dui_005ferror_005f2.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005ferror_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
              // /view.jsp(42,2) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ferror_005f2.setKey("error");
              // /view.jsp(42,2) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ferror_005f2.setMessage("an-error-occurred-while-sending-the-form-information");
              int _jspx_eval_liferay_002dui_005ferror_005f2 = _jspx_th_liferay_002dui_005ferror_005f2.doStartTag();
              if (_jspx_th_liferay_002dui_005ferror_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f2);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f2);
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
              // /view.jsp(44,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f1.setTest( WebFormUtil.VALIDATION_SCRIPT_ENABLED && SessionErrors.contains(renderRequest, "validation-script-error") );
              int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
              if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_liferay_002dutil_005finclude_005f0(_jspx_th_c_005fif_005f1, _jspx_page_context))
                    return;
                  out.write('\n');
                  out.write('	');
                  out.write('	');
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
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

		int i = 1;

		String fieldName = "field" + i;
		String fieldLabel = LocalizationUtil.getPreferencesValue(preferences, "fieldLabel" + i, themeDisplay.getLanguageId());
		boolean fieldOptional = PrefsParamUtil.getBoolean(preferences, request, "fieldOptional" + i, false);
		String fieldValue = ParamUtil.getString(request, fieldName);
		String[] options = null;

		while ((i == 1) || Validator.isNotNull(fieldLabel)) {
			String fieldType = preferences.getValue("fieldType" + i, "text");
			String fieldOptions = LocalizationUtil.getPreferencesValue(preferences, "fieldOptions" + i, themeDisplay.getLanguageId());
			String fieldValidationScript = preferences.getValue("fieldValidationScript" + i, StringPool.BLANK);
			String fieldValidationErrorMessage = preferences.getValue("fieldValidationErrorMessage" + i, StringPool.BLANK);
		
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t");
              //  liferay-ui:error
              com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f3 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
              _jspx_th_liferay_002dui_005ferror_005f3.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005ferror_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
              // /view.jsp(64,3) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ferror_005f3.setKey( "error" + fieldLabel );
              // /view.jsp(64,3) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ferror_005f3.setMessage( fieldValidationErrorMessage );
              int _jspx_eval_liferay_002dui_005ferror_005f3 = _jspx_th_liferay_002dui_005ferror_005f3.doStartTag();
              if (_jspx_th_liferay_002dui_005ferror_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f3);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f3);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
              // /view.jsp(66,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f2.setTest( Validator.isNotNull(fieldValidationScript) );
              int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
              if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t<div class=\"aui-helper-hidden\" id=\"");
                  if (_jspx_meth_portlet_005fnamespace_005f0(_jspx_th_c_005fif_005f2, _jspx_page_context))
                    return;
                  out.write("validationError");
                  out.print( fieldLabel );
                  out.write("\">\n");
                  out.write("\t\t\t\t\t<span class=\"portlet-msg-error\">");
                  out.print( fieldValidationErrorMessage );
                  out.write("</span>\n");
                  out.write("\t\t\t\t</div>\n");
                  out.write("\t\t\t");
                  int evalDoAfterBody = _jspx_th_c_005fif_005f2.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fif_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
              // /view.jsp(72,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f3.setTest( !fieldOptional );
              int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
              if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t<div class=\"aui-helper-hidden\" id=\"");
                  if (_jspx_meth_portlet_005fnamespace_005f1(_jspx_th_c_005fif_005f3, _jspx_page_context))
                    return;
                  out.write("fieldOptionalError");
                  out.print( fieldLabel );
                  out.write("\">\n");
                  out.write("\t\t\t\t\t<span class=\"portlet-msg-error\">");
                  if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_th_c_005fif_005f3, _jspx_page_context))
                    return;
                  out.write("</span>\n");
                  out.write("\t\t\t\t</div>\n");
                  out.write("\t\t\t");
                  int evalDoAfterBody = _jspx_th_c_005fif_005f3.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fif_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t");
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f0 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f0.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
              int _jspx_eval_c_005fchoose_005f0 = _jspx_th_c_005fchoose_005f0.doStartTag();
              if (_jspx_eval_c_005fchoose_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f0 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
                  // /view.jsp(79,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f0.setTest( fieldType.equals("paragraph") );
                  int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t<p class=\"lfr-webform\" id=\"");
                      if (_jspx_meth_portlet_005fnamespace_005f2(_jspx_th_c_005fwhen_005f0, _jspx_page_context))
                        return;
                      out.print( fieldName );
                      out.write('"');
                      out.write('>');
                      out.print( fieldOptions );
                      out.write("</p>\n");
                      out.write("\t\t\t\t");
                      int evalDoAfterBody = _jspx_th_c_005fwhen_005f0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fwhen_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
                  out.write("\n");
                  out.write("\t\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f1 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
                  // /view.jsp(82,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f1.setTest( fieldType.equals("text") );
                  int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t");
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f1 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                      // /view.jsp(83,5) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f1.setCssClass( fieldOptional ? "optional" : StringPool.BLANK );
                      // /view.jsp(83,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f1.setLabel( HtmlUtil.escape(fieldLabel) );
                      // /view.jsp(83,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f1.setName( fieldName );
                      // /view.jsp(83,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f1.setValue( HtmlUtil.escape(fieldValue) );
                      int _jspx_eval_aui_005finput_005f1 = _jspx_th_aui_005finput_005f1.doStartTag();
                      if (_jspx_th_aui_005finput_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
                      out.write("\n");
                      out.write("\t\t\t\t");
                      int evalDoAfterBody = _jspx_th_c_005fwhen_005f1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fwhen_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);
                  out.write("\n");
                  out.write("\t\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f2 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
                  // /view.jsp(85,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f2.setTest( fieldType.equals("textarea") );
                  int _jspx_eval_c_005fwhen_005f2 = _jspx_th_c_005fwhen_005f2.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t");
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f2 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fwrap_005fvalue_005ftype_005fname_005flabel_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f2.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                      // /view.jsp(86,5) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f2.setCssClass( fieldOptional ? "optional" : StringPool.BLANK );
                      // /view.jsp(86,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f2.setLabel( HtmlUtil.escape(fieldLabel) );
                      // /view.jsp(86,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f2.setName( fieldName );
                      // /view.jsp(86,5) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f2.setType("textarea");
                      // /view.jsp(86,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f2.setValue( HtmlUtil.escape(fieldValue) );
                      // /view.jsp(86,5) null
                      _jspx_th_aui_005finput_005f2.setDynamicAttribute(null, "wrap", new String("soft"));
                      int _jspx_eval_aui_005finput_005f2 = _jspx_th_aui_005finput_005f2.doStartTag();
                      if (_jspx_th_aui_005finput_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fwrap_005fvalue_005ftype_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f2);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fwrap_005fvalue_005ftype_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f2);
                      out.write("\n");
                      out.write("\t\t\t\t");
                      int evalDoAfterBody = _jspx_th_c_005fwhen_005f2.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fwhen_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f2);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f2);
                  out.write("\n");
                  out.write("\t\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f3 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
                  // /view.jsp(88,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f3.setTest( fieldType.equals("checkbox") );
                  int _jspx_eval_c_005fwhen_005f3 = _jspx_th_c_005fwhen_005f3.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t");
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f3 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f3.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
                      // /view.jsp(89,5) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f3.setCssClass( fieldOptional ? "optional" : StringPool.BLANK );
                      // /view.jsp(89,5) name = inlineLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f3.setInlineLabel("left");
                      // /view.jsp(89,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f3.setLabel( HtmlUtil.escape(fieldLabel) );
                      // /view.jsp(89,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f3.setName( fieldName );
                      // /view.jsp(89,5) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f3.setType("checkbox");
                      // /view.jsp(89,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f3.setValue( GetterUtil.getBoolean(fieldValue) );
                      int _jspx_eval_aui_005finput_005f3 = _jspx_th_aui_005finput_005f3.doStartTag();
                      if (_jspx_th_aui_005finput_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f3);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f3);
                      out.write("\n");
                      out.write("\t\t\t\t");
                      int evalDoAfterBody = _jspx_th_c_005fwhen_005f3.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fwhen_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f3);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f3);
                  out.write("\n");
                  out.write("\t\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f4 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f4.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
                  // /view.jsp(91,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f4.setTest( fieldType.equals("radio") );
                  int _jspx_eval_c_005fwhen_005f4 = _jspx_th_c_005fwhen_005f4.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t");
                      //  aui:field-wrapper
                      com.liferay.taglib.aui.FieldWrapperTag _jspx_th_aui_005ffield_002dwrapper_005f0 = (com.liferay.taglib.aui.FieldWrapperTag) _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005fname_005flabel_005fcssClass.get(com.liferay.taglib.aui.FieldWrapperTag.class);
                      _jspx_th_aui_005ffield_002dwrapper_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005ffield_002dwrapper_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                      // /view.jsp(92,5) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005ffield_002dwrapper_005f0.setCssClass( fieldOptional ? "optional" : StringPool.BLANK );
                      // /view.jsp(92,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005ffield_002dwrapper_005f0.setLabel( HtmlUtil.escape(fieldLabel) );
                      // /view.jsp(92,5) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005ffield_002dwrapper_005f0.setName( fieldName );
                      int _jspx_eval_aui_005ffield_002dwrapper_005f0 = _jspx_th_aui_005ffield_002dwrapper_005f0.doStartTag();
                      if (_jspx_eval_aui_005ffield_002dwrapper_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_aui_005ffield_002dwrapper_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_aui_005ffield_002dwrapper_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_aui_005ffield_002dwrapper_005f0.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t ");

						options = WebFormUtil.split(fieldOptions);

						for (int j = 0; j < options.length; j++) {
							String optionValue = options[j];
						
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  aui:input
                          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f4 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fchecked_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                          _jspx_th_aui_005finput_005f4.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005finput_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f0);
                          // /view.jsp(101,7) name = checked type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f4.setChecked( fieldValue.equals(optionValue) );
                          // /view.jsp(101,7) name = inlineLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f4.setInlineLabel("left");
                          // /view.jsp(101,7) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f4.setLabel( HtmlUtil.escape(optionValue) );
                          // /view.jsp(101,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f4.setName( fieldName );
                          // /view.jsp(101,7) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f4.setType("radio");
                          // /view.jsp(101,7) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f4.setValue( HtmlUtil.escape(optionValue) );
                          int _jspx_eval_aui_005finput_005f4 = _jspx_th_aui_005finput_005f4.doStartTag();
                          if (_jspx_th_aui_005finput_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fchecked_005fnobody.reuse(_jspx_th_aui_005finput_005f4);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineLabel_005fchecked_005fnobody.reuse(_jspx_th_aui_005finput_005f4);
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t");

						}
						
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          int evalDoAfterBody = _jspx_th_aui_005ffield_002dwrapper_005f0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_aui_005ffield_002dwrapper_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.popBody();
                        }
                      }
                      if (_jspx_th_aui_005ffield_002dwrapper_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005fname_005flabel_005fcssClass.reuse(_jspx_th_aui_005ffield_002dwrapper_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005fname_005flabel_005fcssClass.reuse(_jspx_th_aui_005ffield_002dwrapper_005f0);
                      out.write("\n");
                      out.write("\t\t\t\t");
                      int evalDoAfterBody = _jspx_th_c_005fwhen_005f4.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fwhen_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f4);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f4);
                  out.write("\n");
                  out.write("\t\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f5 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f5.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
                  // /view.jsp(109,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f5.setTest( fieldType.equals("options") );
                  int _jspx_eval_c_005fwhen_005f5 = _jspx_th_c_005fwhen_005f5.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t");

					options = WebFormUtil.split(fieldOptions);
					
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t");
                      //  aui:select
                      com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f0 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005fcssClass.get(com.liferay.taglib.aui.SelectTag.class);
                      _jspx_th_aui_005fselect_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005fselect_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
                      // /view.jsp(115,5) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005fselect_005f0.setCssClass( fieldOptional ? "optional" : StringPool.BLANK );
                      // /view.jsp(115,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005fselect_005f0.setLabel( HtmlUtil.escape(fieldLabel) );
                      // /view.jsp(115,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005fselect_005f0.setName( fieldName );
                      int _jspx_eval_aui_005fselect_005f0 = _jspx_th_aui_005fselect_005f0.doStartTag();
                      if (_jspx_eval_aui_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_aui_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_aui_005fselect_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_aui_005fselect_005f0.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t");

						for (int j = 0; j < options.length; j++) {
							String optionValue = options[j];
						
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  aui:option
                          com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f0 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected.get(com.liferay.taglib.aui.OptionTag.class);
                          _jspx_th_aui_005foption_005f0.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005foption_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f0);
                          // /view.jsp(122,7) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005foption_005f0.setSelected( fieldValue.equals(optionValue) );
                          // /view.jsp(122,7) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005foption_005f0.setValue( HtmlUtil.escape(optionValue) );
                          int _jspx_eval_aui_005foption_005f0 = _jspx_th_aui_005foption_005f0.doStartTag();
                          if (_jspx_eval_aui_005foption_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_aui_005foption_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_aui_005foption_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_aui_005foption_005f0.doInitBody();
                            }
                            do {
                              out.print( HtmlUtil.escape(optionValue) );
                              int evalDoAfterBody = _jspx_th_aui_005foption_005f0.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_aui_005foption_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                            }
                          }
                          if (_jspx_th_aui_005foption_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected.reuse(_jspx_th_aui_005foption_005f0);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected.reuse(_jspx_th_aui_005foption_005f0);
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t");

						}
						
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          int evalDoAfterBody = _jspx_th_aui_005fselect_005f0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_aui_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.popBody();
                        }
                      }
                      if (_jspx_th_aui_005fselect_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005fcssClass.reuse(_jspx_th_aui_005fselect_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005fcssClass.reuse(_jspx_th_aui_005fselect_005f0);
                      out.write("\n");
                      out.write("\t\t\t\t");
                      int evalDoAfterBody = _jspx_th_c_005fwhen_005f5.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fwhen_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f5);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f5);
                  out.write("\n");
                  out.write("\t\t\t");
                  int evalDoAfterBody = _jspx_th_c_005fchoose_005f0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fchoose_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

			i++;

			fieldName = "field" + i;
			fieldLabel = LocalizationUtil.getPreferencesValue(preferences, "fieldLabel" + i, themeDisplay.getLanguageId());
			fieldOptional = PrefsParamUtil.getBoolean(preferences, request, "fieldOptional" + i, false);
			fieldValue = ParamUtil.getString(request, fieldName);
		}
		
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
              // /view.jsp(142,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f4.setTest( requireCaptcha );
              int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
              if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:resourceURL
                  com.liferay.taglib.portlet.ResourceURLTag _jspx_th_portlet_005fresourceURL_005f0 = (com.liferay.taglib.portlet.ResourceURLTag) _005fjspx_005ftagPool_005fportlet_005fresourceURL_0026_005fvar.get(com.liferay.taglib.portlet.ResourceURLTag.class);
                  _jspx_th_portlet_005fresourceURL_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fresourceURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
                  // /view.jsp(143,3) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fresourceURL_005f0.setVar("captchaURL");
                  int _jspx_eval_portlet_005fresourceURL_005f0 = _jspx_th_portlet_005fresourceURL_005f0.doStartTag();
                  if (_jspx_eval_portlet_005fresourceURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_portlet_005fresourceURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_portlet_005fresourceURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_portlet_005fresourceURL_005f0.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  portlet:param
                      com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f1 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                      _jspx_th_portlet_005fparam_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005fparam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005fresourceURL_005f0);
                      // /view.jsp(144,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f1.setName( Constants.CMD );
                      // /view.jsp(144,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f1.setValue("captcha");
                      int _jspx_eval_portlet_005fparam_005f1 = _jspx_th_portlet_005fparam_005f1.doStartTag();
                      if (_jspx_th_portlet_005fparam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                      out.write("\n");
                      out.write("\t\t\t");
                      int evalDoAfterBody = _jspx_th_portlet_005fresourceURL_005f0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_portlet_005fresourceURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_portlet_005fresourceURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fresourceURL_0026_005fvar.reuse(_jspx_th_portlet_005fresourceURL_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fresourceURL_0026_005fvar.reuse(_jspx_th_portlet_005fresourceURL_005f0);
                  java.lang.String captchaURL = null;
                  captchaURL = (java.lang.String) _jspx_page_context.findAttribute("captchaURL");
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  liferay-ui:captcha
                  com.liferay.taglib.ui.CaptchaTag _jspx_th_liferay_002dui_005fcaptcha_005f0 = (com.liferay.taglib.ui.CaptchaTag) _005fjspx_005ftagPool_005fliferay_002dui_005fcaptcha_0026_005furl_005fnobody.get(com.liferay.taglib.ui.CaptchaTag.class);
                  _jspx_th_liferay_002dui_005fcaptcha_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fcaptcha_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
                  // /view.jsp(147,3) name = url type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fcaptcha_005f0.setUrl( captchaURL );
                  int _jspx_eval_liferay_002dui_005fcaptcha_005f0 = _jspx_th_liferay_002dui_005fcaptcha_005f0.doStartTag();
                  if (_jspx_th_liferay_002dui_005fcaptcha_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fcaptcha_0026_005furl_005fnobody.reuse(_jspx_th_liferay_002dui_005fcaptcha_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fcaptcha_0026_005furl_005fnobody.reuse(_jspx_th_liferay_002dui_005fcaptcha_005f0);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_c_005fif_005f4.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fif_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              if (_jspx_meth_aui_005fbutton_005f0(_jspx_th_aui_005ffieldset_005f0, _jspx_page_context))
                return;
              out.write('\n');
              out.write('	');
              int evalDoAfterBody = _jspx_th_aui_005ffieldset_005f0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_aui_005ffieldset_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_aui_005ffieldset_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel.reuse(_jspx_th_aui_005ffieldset_005f0);
            return;
          }
          _005fjspx_005ftagPool_005faui_005ffieldset_0026_005flabel.reuse(_jspx_th_aui_005ffieldset_005f0);
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
      //  aui:script
      com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
      _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fscript_005f0.setParent(null);
      // /view.jsp(154,0) name = use type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fscript_005f0.setUse("aui-base,selector-css3");
      int _jspx_eval_aui_005fscript_005f0 = _jspx_th_aui_005fscript_005f0.doStartTag();
      if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_aui_005fscript_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_aui_005fscript_005f0.doInitBody();
        }
        do {
          out.write("\n");
          out.write("\tvar form = A.one('#");
          if (_jspx_meth_portlet_005fnamespace_005f3(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("fm');\n");
          out.write("\n");
          out.write("\tif (form) {\n");
          out.write("\t\tform.on(\n");
          out.write("\t\t\t'submit',\n");
          out.write("\t\t\tfunction(event) {\n");
          out.write("\t\t\t\tvar keys = [];\n");
          out.write("\t\t\t\tvar fieldLabels = {};\n");
          out.write("\t\t\t\tvar fieldOptional = {};\n");
          out.write("\t\t\t\tvar fieldValidationErrorMessages = {};\n");
          out.write("\t\t\t\tvar fieldValidationFunctions = {};\n");
          out.write("\t\t\t\tvar fieldsMap = {};\n");
          out.write("\n");
          out.write("\t\t\t\t");

				int fieldIndex = 1;
				String fieldLabel = preferences.getValue("fieldLabel" + fieldIndex, StringPool.BLANK);

				while ((fieldIndex == 1) || Validator.isNotNull(fieldLabel)) {
					boolean fieldOptional = PrefsParamUtil.getBoolean(preferences, request, "fieldOptional" + fieldIndex, false);
					String fieldType = preferences.getValue("fieldType" + fieldIndex, "text");
					String fieldValidationScript = preferences.getValue("fieldValidationScript" + fieldIndex, StringPool.BLANK);
					String fieldValidationErrorMessage = preferences.getValue("fieldValidationErrorMessage" + fieldIndex, StringPool.BLANK);
				
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t\tvar key = \"");
          out.print( HtmlUtil.escape(fieldLabel) );
          out.write("\";\n");
          out.write("\n");
          out.write("\t\t\t\t\tkeys[");
          out.print( fieldIndex );
          out.write("] = key;\n");
          out.write("\n");
          out.write("\t\t\t\t\tfieldLabels[key] = \"");
          out.print( HtmlUtil.escape(fieldLabel) );
          out.write("\";\n");
          out.write("\t\t\t\t\tfieldValidationErrorMessages[key] = \"");
          out.print( fieldValidationErrorMessage );
          out.write("\";\n");
          out.write("\n");
          out.write("\t\t\t\t\tfunction fieldValidationFunction");
          out.print( fieldIndex );
          out.write("(currentFieldValue, fieldsMap) {\n");
          out.write("\t\t\t\t\t\t");
          //  c:choose
          org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
          _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
          _jspx_th_c_005fchoose_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
          if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t\t\t\t\t\t");
              //  c:when
              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f6 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
              _jspx_th_c_005fwhen_005f6.setPageContext(_jspx_page_context);
              _jspx_th_c_005fwhen_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
              // /view.jsp(188,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fwhen_005f6.setTest( Validator.isNotNull(fieldValidationScript) );
              int _jspx_eval_c_005fwhen_005f6 = _jspx_th_c_005fwhen_005f6.doStartTag();
              if (_jspx_eval_c_005fwhen_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t");
                  out.print( fieldValidationScript );
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t");
                  int evalDoAfterBody = _jspx_th_c_005fwhen_005f6.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fwhen_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f6);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f6);
              out.write("\n");
              out.write("\t\t\t\t\t\t\t");
              if (_jspx_meth_c_005fotherwise_005f0(_jspx_th_c_005fchoose_005f1, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t\t\t");
              int evalDoAfterBody = _jspx_th_c_005fchoose_005f1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fchoose_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f1);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f1);
          out.write("\n");
          out.write("\t\t\t\t\t};\n");
          out.write("\n");
          out.write("\t\t\t\t\tfieldOptional[key] = ");
          out.print( fieldOptional );
          out.write(";\n");
          out.write("\t\t\t\t\tfieldValidationFunctions[key] = fieldValidationFunction");
          out.print( fieldIndex );
          out.write(";\n");
          out.write("\n");
          out.write("\t\t\t\t\t");
          //  c:choose
          org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f2 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
          _jspx_th_c_005fchoose_005f2.setPageContext(_jspx_page_context);
          _jspx_th_c_005fchoose_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          int _jspx_eval_c_005fchoose_005f2 = _jspx_th_c_005fchoose_005f2.doStartTag();
          if (_jspx_eval_c_005fchoose_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t\t\t\t\t");
              //  c:when
              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f7 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
              _jspx_th_c_005fwhen_005f7.setPageContext(_jspx_page_context);
              _jspx_th_c_005fwhen_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
              // /view.jsp(201,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fwhen_005f7.setTest( fieldType.equals("radio") );
              int _jspx_eval_c_005fwhen_005f7 = _jspx_th_c_005fwhen_005f7.doStartTag();
              if (_jspx_eval_c_005fwhen_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\tvar radioButton = A.one('input[name=");
                  if (_jspx_meth_portlet_005fnamespace_005f4(_jspx_th_c_005fwhen_005f7, _jspx_page_context))
                    return;
                  out.write("field");
                  out.print( fieldIndex );
                  out.write("]:checked');\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\tfieldsMap[key] = '';\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\tif (radioButton) {\n");
                  out.write("\t\t\t\t\t\t\t\tfieldsMap[key] = radioButton.val();\n");
                  out.write("\t\t\t\t\t\t\t}\n");
                  out.write("\t\t\t\t\t\t");
                  int evalDoAfterBody = _jspx_th_c_005fwhen_005f7.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fwhen_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f7);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f7);
              out.write("\n");
              out.write("\t\t\t\t\t\t");
              //  c:otherwise
              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f1 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
              _jspx_th_c_005fotherwise_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fotherwise_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
              int _jspx_eval_c_005fotherwise_005f1 = _jspx_th_c_005fotherwise_005f1.doStartTag();
              if (_jspx_eval_c_005fotherwise_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\tvar inputField = A.one('#");
                  if (_jspx_meth_portlet_005fnamespace_005f5(_jspx_th_c_005fotherwise_005f1, _jspx_page_context))
                    return;
                  out.write("field");
                  out.print( fieldIndex );
                  out.write("');\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\tfieldsMap[key] = (inputField && inputField.val()) || '';\n");
                  out.write("\t\t\t\t\t\t");
                  int evalDoAfterBody = _jspx_th_c_005fotherwise_005f1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fotherwise_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f1);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f1);
              out.write("\n");
              out.write("\t\t\t\t\t");
              int evalDoAfterBody = _jspx_th_c_005fchoose_005f2.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fchoose_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f2);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f2);
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t");

					fieldIndex++;
					fieldLabel = preferences.getValue("fieldLabel" + fieldIndex, "");
				}
				
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\tvar validationErrors = false;\n");
          out.write("\n");
          out.write("\t\t\t\tfor (var i = 1; i < keys.length; i++) {\n");
          out.write("\t\t\t\t\tvar key = keys [i];\n");
          out.write("\n");
          out.write("\t\t\t\t\tvar currentFieldValue = fieldsMap[key];\n");
          out.write("\n");
          out.write("\t\t\t\t\tvar optionalFieldError = A.one('#");
          if (_jspx_meth_portlet_005fnamespace_005f6(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("fieldOptionalError' + fieldLabels[key]);\n");
          out.write("\t\t\t\t\tvar validationError = A.one('#");
          if (_jspx_meth_portlet_005fnamespace_005f7(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("validationError' + fieldLabels[key]);\n");
          out.write("\n");
          out.write("\t\t\t\t\tif (!fieldOptional[key] && currentFieldValue.match(/^\\s*$/)) {\n");
          out.write("\t\t\t\t\t\tvalidationErrors = true;\n");
          out.write("\n");
          out.write("\t\t\t\t\t\tA.all('.portlet-msg-success').hide();\n");
          out.write("\n");
          out.write("\t\t\t\t\t\tif (optionalFieldError) {\n");
          out.write("\t\t\t\t\t\t\toptionalFieldError.show();\n");
          out.write("\t\t\t\t\t\t}\n");
          out.write("\t\t\t\t\t}\n");
          out.write("\t\t\t\t\telse if (!fieldValidationFunctions[key](currentFieldValue, fieldsMap)) {\n");
          out.write("\t\t\t\t\t\tvalidationErrors = true;\n");
          out.write("\n");
          out.write("\t\t\t\t\t\tA.all('.portlet-msg-success').hide();\n");
          out.write("\n");
          out.write("\t\t\t\t\t\tif (optionalFieldError) {\n");
          out.write("\t\t\t\t\t\t\toptionalFieldError.hide();\n");
          out.write("\t\t\t\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\t\t\t\tif (validationError) {\n");
          out.write("\t\t\t\t\t\t\tvalidationError.show();\n");
          out.write("\t\t\t\t\t\t}\n");
          out.write("\t\t\t\t\t}\n");
          out.write("\t\t\t\t\telse {\n");
          out.write("\t\t\t\t\t\tif (optionalFieldError) {\n");
          out.write("\t\t\t\t\t\t\toptionalFieldError.hide();\n");
          out.write("\t\t\t\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\t\t\t\tif (validationError) {\n");
          out.write("\t\t\t\t\t\t\tvalidationError.hide();\n");
          out.write("\t\t\t\t\t\t}\n");
          out.write("\t\t\t\t\t}\n");
          out.write("\t\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\t\tif (validationErrors) {\n");
          out.write("\t\t\t\t\tevent.halt();\n");
          out.write("\t\t\t\t\tevent.stopImmediatePropagation();\n");
          out.write("\t\t\t\t}\n");
          out.write("\t\t\t}\n");
          out.write("\t\t);\n");
          out.write("\t}\n");
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

  private boolean _jspx_meth_liferay_002dui_005fsuccess_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:success
    com.liferay.taglib.ui.SuccessTag _jspx_th_liferay_002dui_005fsuccess_005f0 = (com.liferay.taglib.ui.SuccessTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.SuccessTag.class);
    _jspx_th_liferay_002dui_005fsuccess_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fsuccess_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
    // /view.jsp(38,2) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fsuccess_005f0.setKey("success");
    // /view.jsp(38,2) name = message type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fsuccess_005f0.setMessage("the-form-information-was-sent-successfully");
    int _jspx_eval_liferay_002dui_005fsuccess_005f0 = _jspx_th_liferay_002dui_005fsuccess_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fsuccess_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f0 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
    // /view.jsp(45,3) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f0.setPage("/script_error.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f0 = _jspx_th_liferay_002dutil_005finclude_005f0.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f0 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
    int _jspx_eval_portlet_005fnamespace_005f0 = _jspx_th_portlet_005fnamespace_005f0.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f1 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f1.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
    int _jspx_eval_portlet_005fnamespace_005f1 = _jspx_th_portlet_005fnamespace_005f1.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f1);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
    // /view.jsp(74,37) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("this-field-is-mandatory");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f2 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f2.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
    int _jspx_eval_portlet_005fnamespace_005f2 = _jspx_th_portlet_005fnamespace_005f2.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f2);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button
    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f0 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
    _jspx_th_aui_005fbutton_005f0.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
    // /view.jsp(150,2) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f0.setType("submit");
    // /view.jsp(150,2) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f0.setOnClick("");
    // /view.jsp(150,2) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f0.setValue("send");
    int _jspx_eval_aui_005fbutton_005f0 = _jspx_th_aui_005fbutton_005f0.doStartTag();
    if (_jspx_th_aui_005fbutton_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f3 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f3.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f3 = _jspx_th_portlet_005fnamespace_005f3.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f3);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
    int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("\t\t\t\t\t\t\t\treturn true;\n");
        out.write("\t\t\t\t\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f4 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f4.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f7);
    int _jspx_eval_portlet_005fnamespace_005f4 = _jspx_th_portlet_005fnamespace_005f4.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f4);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f5 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f5.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
    int _jspx_eval_portlet_005fnamespace_005f5 = _jspx_th_portlet_005fnamespace_005f5.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f5);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f6 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f6.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f6 = _jspx_th_portlet_005fnamespace_005f6.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f7 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f7.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f7 = _jspx_th_portlet_005fnamespace_005f7.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f7);
    return false;
  }
}
