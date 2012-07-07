package org.apache.jsp.html.portlet.control_005fpanel_005fmenu;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.kernel.captcha.CaptchaMaxChallengesException;
import com.liferay.portal.kernel.captcha.CaptchaTextException;
import com.liferay.portal.kernel.bean.BeanParamUtil;
import com.liferay.portal.kernel.bean.BeanPropertiesUtil;
import com.liferay.portal.kernel.cal.Recurrence;
import com.liferay.portal.kernel.configuration.Filter;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.DAOParamUtil;
import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.dao.search.ResultRow;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.dao.search.ScoreSearchEntry;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.dao.search.SearchEntry;
import com.liferay.portal.kernel.dao.search.TextSearchEntry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.io.unsync.UnsyncStringReader;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.language.LanguageWrapper;
import com.liferay.portal.kernel.language.UnicodeLanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.log.LogUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.portlet.DynamicRenderRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletMode;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.servlet.BrowserSnifferUtil;
import com.liferay.portal.kernel.servlet.ImageServletTokenUtil;
import com.liferay.portal.kernel.servlet.ServletContextUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.servlet.StringServletResponse;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.BooleanWrapper;
import com.liferay.portal.kernel.util.BreadcrumbsUtil;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.CalendarUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.KeyValuePairComparator;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.LongWrapper;
import com.liferay.portal.kernel.util.MathUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.ObjectValuePairComparator;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderedProperties;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PrefsParamUtil;
import com.liferay.portal.kernel.util.PropertiesParamUtil;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.Randomizer;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.ServerDetector;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.SortedProperties;
import com.liferay.portal.kernel.util.StackTraceUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.TimeZoneUtil;
import com.liferay.portal.kernel.util.UnicodeFormatter;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.language.LanguageImpl;
import com.liferay.portal.model.*;
import com.liferay.portal.model.impl.*;
import com.liferay.portal.security.auth.AuthTokenUtil;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.*;
import com.liferay.portal.service.permission.GroupPermissionUtil;
import com.liferay.portal.service.permission.LayoutPermissionUtil;
import com.liferay.portal.service.permission.LayoutPrototypePermissionUtil;
import com.liferay.portal.service.permission.LayoutSetPrototypePermissionUtil;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.struts.StrutsUtil;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.ContentUtil;
import com.liferay.portal.util.CookieKeys;
import com.liferay.portal.util.JavaScriptBundleUtil;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletCategoryKeys;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.SessionClicks;
import com.liferay.portal.util.SessionTreeJSClicks;
import com.liferay.portal.util.ShutdownUtil;
import com.liferay.portal.util.WebAppPool;
import com.liferay.portal.util.WebKeys;
import com.liferay.portal.util.comparator.PortletCategoryComparator;
import com.liferay.portal.util.comparator.PortletTitleComparator;
import com.liferay.portlet.InvokerPortlet;
import com.liferay.portlet.PortalPreferences;
import com.liferay.portlet.PortletConfigFactoryUtil;
import com.liferay.portlet.PortletInstanceFactoryUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.PortletResponseImpl;
import com.liferay.portlet.PortletSetupUtil;
import com.liferay.portlet.PortletURLImpl;
import com.liferay.portlet.PortletURLUtil;
import com.liferay.portlet.RenderParametersPool;
import com.liferay.portlet.RenderRequestFactory;
import com.liferay.portlet.RenderRequestImpl;
import com.liferay.portlet.RenderResponseFactory;
import com.liferay.portlet.RenderResponseImpl;
import com.liferay.portlet.UserAttributes;
import com.liferay.portlet.portletconfiguration.util.PortletConfigurationUtil;
import com.liferay.util.CreditCard;
import com.liferay.util.Encryptor;
import com.liferay.util.JS;
import com.liferay.util.PKParser;
import com.liferay.util.PwdGenerator;
import com.liferay.util.State;
import com.liferay.util.StateUtil;
import com.liferay.util.TextFormatter;
import com.liferay.util.UniqueList;
import com.liferay.util.format.PhoneNumberUtil;
import com.liferay.util.log4j.Levels;
import com.liferay.util.mail.InternetAddressUtil;
import com.liferay.util.portlet.PortletRequestUtil;
import com.liferay.util.servlet.DynamicServletRequest;
import com.liferay.util.servlet.SessionParameters;
import com.liferay.util.servlet.UploadException;
import com.liferay.util.xml.XMLFormatter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Stack;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.portlet.MimeResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceURL;
import javax.portlet.UnavailableException;
import javax.portlet.ValidatorException;
import javax.portlet.WindowState;
import org.apache.commons.math.util.MathUtils;
import org.apache.struts.Globals;

public final class view_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(26);
    _jspx_dependants.add("/html/portlet/init.jsp");
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/init-ext.jsp");
    _jspx_dependants.add("/WEB-INF/tld/displaytag.tld");
    _jspx_dependants.add("/WEB-INF/tld/c.tld");
    _jspx_dependants.add("/WEB-INF/tld/fmt.tld");
    _jspx_dependants.add("/WEB-INF/tld/fn.tld");
    _jspx_dependants.add("/WEB-INF/tld/sql.tld");
    _jspx_dependants.add("/WEB-INF/tld/x.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-portlet.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-aui.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-portlet-ext.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-security.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-theme.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-ui.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-util.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-bean.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-bean-el.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-html.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-html-el.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-logic.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-logic-el.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-nested.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-tiles.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-tiles-el.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dfloating_002dcontainer_0026_005ftrigger_005fpaging_005fid;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dfloating_002dcontainer_0026_005ftrigger;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcssClass_005fcollapsible;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fportletName_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fsrc_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dfloating_002dcontainer_0026_005ftrigger_005fpaging_005fid = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dfloating_002dcontainer_0026_005ftrigger = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcssClass_005fcollapsible = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fportletName_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fsrc_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dfloating_002dcontainer_0026_005ftrigger_005fpaging_005fid.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dfloating_002dcontainer_0026_005ftrigger.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcssClass_005fcollapsible.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fportletName_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fsrc_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fotherwise.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.release();
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
      response.setContentType("text/html; charset=UTF-8");
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

WindowState windowState = null;
PortletMode portletMode = null;

PortletURL currentURLObj = null;

if (renderRequest != null) {
	windowState = renderRequest.getWindowState();
	portletMode = renderRequest.getPortletMode();

	currentURLObj = PortletURLUtil.getCurrent(renderRequest, renderResponse);
}
else if (resourceRequest != null) {
	windowState = resourceRequest.getWindowState();
	portletMode = resourceRequest.getPortletMode();

	currentURLObj = PortletURLUtil.getCurrent(resourceRequest, resourceResponse);
}

String currentURL = currentURLObj.toString();
//String currentURL = PortalUtil.getCurrentURL(request);

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
      out.write("<h1 class=\"user-greeting\">\n");
      out.write("\t");
      if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_page_context))
        return;
      out.write("\n");
      out.write("</h1>\n");
      out.write("\n");
      out.write("<div class=\"portal-add-content\">\n");
      out.write("\t");
      //  liferay-ui:panel-container
      com.liferay.taglib.ui.PanelContainerTag _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0 = (com.liferay.taglib.ui.PanelContainerTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended.get(com.liferay.taglib.ui.PanelContainerTag.class);
      _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setParent(null);
      // /html/portlet/control_panel_menu/view.jsp(24,1) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setExtended( true );
      // /html/portlet/control_panel_menu/view.jsp(24,1) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setId("addContentPanelContainer");
      // /html/portlet/control_panel_menu/view.jsp(24,1) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setPersistState( true );
      int _jspx_eval_liferay_002dui_005fpanel_002dcontainer_005f0 = _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.doStartTag();
      if (_jspx_eval_liferay_002dui_005fpanel_002dcontainer_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_liferay_002dui_005fpanel_002dcontainer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.doInitBody();
        }
        do {
          out.write("\n");
          out.write("\n");
          out.write("\t\t");

		String ppid = GetterUtil.getString((String)request.getAttribute("control_panel.jsp-ppid"), layoutTypePortlet.getStateMaxPortletId());

		String category = PortalUtil.getControlPanelCategory(ppid, themeDisplay);

		for (String curCategory : PortletCategoryKeys.ALL) {
			List<Portlet> portlets = PortalUtil.getControlPanelPortlets(curCategory, themeDisplay);

			if (portlets.isEmpty()) {
				continue;
			}

			List<Layout> scopeLayouts = new ArrayList<Layout>();

			String curGroupLabel = null;
			Group curGroup = null;

			String title = null;

			if (curCategory.equals(PortletCategoryKeys.MY)) {
				title = HtmlUtil.escape(StringUtil.shorten(user.getFullName(), 25));
			}
			else if (curCategory.equals(PortletCategoryKeys.CONTENT)) {
				Layout scopeLayout = null;

				curGroup = themeDisplay.getScopeGroup();

				if (curGroup.isLayout()) {
					scopeLayout = LayoutLocalServiceUtil.getLayout(curGroup.getClassPK());

					curGroup = scopeLayout.getGroup();
				}

				String curGroupName = null;

				if (curGroup.isCompany()) {
					curGroupName = LanguageUtil.get(pageContext, "global");
				}
				else if (curGroup.isUser() && (curGroup.getClassPK() == user.getUserId())) {
					curGroupName = LanguageUtil.get(pageContext, "my-community");
				}
				else {
					curGroupName = curGroup.getDescriptiveName();
				}

				if (category.equals(PortletCategoryKeys.CONTENT)) {
					PortalUtil.addPortletBreadcrumbEntry(request, curGroupName, null);
				}

				if (scopeLayout == null) {
					curGroupLabel = LanguageUtil.get(pageContext, "default");
				}
				else {
					curGroupLabel = scopeLayout.getName(locale);

					if (category.equals(PortletCategoryKeys.CONTENT)) {
						PortalUtil.addPortletBreadcrumbEntry(request, curGroupLabel, null);
					}
				}

				List<Layout> curGroupLayouts = new ArrayList<Layout>();

				curGroupLayouts.addAll(LayoutLocalServiceUtil.getLayouts(curGroup.getGroupId(), false));
				curGroupLayouts.addAll(LayoutLocalServiceUtil.getLayouts(curGroup.getGroupId(), true));

				for (Layout curGroupLayout : curGroupLayouts) {
					if (curGroupLayout.hasScopeGroup()) {
						scopeLayouts.add(curGroupLayout);
					}
				}

				title = "<a href=\"javascript:;\" class=\"lfr-floating-trigger lfr-group-selector\">" + HtmlUtil.escape(StringUtil.shorten(curGroupName, 25)) + "</a>";
			}
			else if (curCategory.equals(PortletCategoryKeys.PORTAL) && (CompanyLocalServiceUtil.getCompaniesCount(false) > 1)) {
				title = HtmlUtil.escape(company.getName());
			}
			else {
				title = LanguageUtil.get(pageContext, "category." + curCategory);
			}
		
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
          // /html/portlet/control_panel_menu/view.jsp(107,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f0.setTest( curCategory.equals(PortletCategoryKeys.CONTENT) );
          int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
          if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t\t\t");
              //  liferay-ui:panel-floating-container
              com.liferay.taglib.ui.PanelFloatingContainerTag _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0 = (com.liferay.taglib.ui.PanelFloatingContainerTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dfloating_002dcontainer_0026_005ftrigger_005fpaging_005fid.get(com.liferay.taglib.ui.PanelFloatingContainerTag.class);
              _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
              // /html/portlet/control_panel_menu/view.jsp(108,4) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0.setId("groupSelectorPanel");
              // /html/portlet/control_panel_menu/view.jsp(108,4) name = paging type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0.setPaging( true );
              // /html/portlet/control_panel_menu/view.jsp(108,4) name = trigger type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0.setTrigger(".lfr-group-selector");
              int _jspx_eval_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0 = _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0.doStartTag();
              if (_jspx_eval_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");

					List<Group> manageableGroups = GroupServiceUtil.getManageableGroups(ActionKeys.MANAGE_LAYOUTS, PropsValues.CONTROL_PANEL_NAVIGATION_MAX_COMMUNITIES);
					List<Organization> manageableOrganizations = OrganizationServiceUtil.getManageableOrganizations(ActionKeys.MANAGE_LAYOUTS, PropsValues.CONTROL_PANEL_NAVIGATION_MAX_ORGANIZATIONS);
					
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0);
                  // /html/portlet/control_panel_menu/view.jsp(115,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f1.setTest( !manageableGroups.isEmpty() );
                  int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
                  if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  liferay-ui:panel
                      com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f0 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.get(com.liferay.taglib.ui.PanelTag.class);
                      _jspx_th_liferay_002dui_005fpanel_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fpanel_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
                      // /html/portlet/control_panel_menu/view.jsp(116,6) name = collapsible type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_005f0.setCollapsible( true );
                      // /html/portlet/control_panel_menu/view.jsp(116,6) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_005f0.setExtended( true );
                      // /html/portlet/control_panel_menu/view.jsp(116,6) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_005f0.setId("communitiesPanel");
                      // /html/portlet/control_panel_menu/view.jsp(116,6) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_005f0.setPersistState( true );
                      // /html/portlet/control_panel_menu/view.jsp(116,6) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_005f0.setTitle( LanguageUtil.get(pageContext, "communities") );
                      int _jspx_eval_liferay_002dui_005fpanel_005f0 = _jspx_th_liferay_002dui_005fpanel_005f0.doStartTag();
                      if (_jspx_eval_liferay_002dui_005fpanel_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_liferay_002dui_005fpanel_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_liferay_002dui_005fpanel_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_liferay_002dui_005fpanel_005f0.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t<ul>\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t");

								for (int i = 0; i < manageableGroups.size(); i++) {
									Group group = manageableGroups.get(i);
								
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t");
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f0);
                          // /html/portlet/control_panel_menu/view.jsp(124,9) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f2.setTest( (i != 0) && (i % 7 == 0 ) );
                          int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
                          if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t</ul>\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t<ul>\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
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
                          out.write("\t\t\t\t\t\t\t\t\t<li>\n");
                          out.write("\t\t\t\t\t\t\t\t\t\t<a href=\"");
                          out.print( HttpUtil.setParameter(PortalUtil.getCurrentURL(request), "doAsGroupId", group.getGroupId()) );
                          out.write('"');
                          out.write('>');
                          out.print( (group.isUser() && (group.getClassPK() == user.getUserId())) ? LanguageUtil.get(pageContext, "my-community") : HtmlUtil.escape(group.getDescriptiveName()) );
                          out.write("</a>\n");
                          out.write("\t\t\t\t\t\t\t\t\t</li>\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t");

								}
								
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t</ul>\n");
                          out.write("\t\t\t\t\t\t");
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
                      out.write("\t\t\t\t\t");
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
                  out.write("\t\t\t\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0);
                  // /html/portlet/control_panel_menu/view.jsp(141,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f3.setTest( !manageableOrganizations.isEmpty() );
                  int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
                  if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  liferay-ui:panel
                      com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f1 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.get(com.liferay.taglib.ui.PanelTag.class);
                      _jspx_th_liferay_002dui_005fpanel_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fpanel_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
                      // /html/portlet/control_panel_menu/view.jsp(142,6) name = collapsible type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_005f1.setCollapsible( true );
                      // /html/portlet/control_panel_menu/view.jsp(142,6) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_005f1.setExtended( true );
                      // /html/portlet/control_panel_menu/view.jsp(142,6) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_005f1.setId("communitiesPanel");
                      // /html/portlet/control_panel_menu/view.jsp(142,6) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_005f1.setPersistState( true );
                      // /html/portlet/control_panel_menu/view.jsp(142,6) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_005f1.setTitle( LanguageUtil.get(pageContext, "organizations") );
                      int _jspx_eval_liferay_002dui_005fpanel_005f1 = _jspx_th_liferay_002dui_005fpanel_005f1.doStartTag();
                      if (_jspx_eval_liferay_002dui_005fpanel_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_liferay_002dui_005fpanel_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_liferay_002dui_005fpanel_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_liferay_002dui_005fpanel_005f1.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t<ul>\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t");

								for (int i = 0; i < manageableOrganizations.size(); i++) {
									Organization organization = manageableOrganizations.get(i);
								
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t");
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f1);
                          // /html/portlet/control_panel_menu/view.jsp(150,9) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f4.setTest( (i != 0) && (i % 7 == 0 ) );
                          int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
                          if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t</ul>\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t<ul>\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
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
                          out.write("\t\t\t\t\t\t\t\t\t<li>\n");
                          out.write("\t\t\t\t\t\t\t\t\t\t<a href=\"");
                          out.print( HttpUtil.setParameter(PortalUtil.getCurrentURL(request), "doAsGroupId", organization.getGroup().getGroupId()) );
                          out.write('"');
                          out.write('>');
                          out.print( HtmlUtil.escape(organization.getName()) );
                          out.write("</a>\n");
                          out.write("\t\t\t\t\t\t\t\t\t</li>\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t");

								}
								
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t</ul>\n");
                          out.write("\t\t\t\t\t\t");
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
                      out.write("\t\t\t\t\t");
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
                  out.write("\t\t\t\t\t");

					boolean showGlobal = permissionChecker.isCompanyAdmin();
					boolean showMyCommunity = user.getGroup().hasPrivateLayouts() || user.getGroup().hasPublicLayouts();
					
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f5 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0);
                  // /html/portlet/control_panel_menu/view.jsp(172,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f5.setTest( showGlobal || showMyCommunity );
                  int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
                  if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  liferay-ui:panel
                      com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f2 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.get(com.liferay.taglib.ui.PanelTag.class);
                      _jspx_th_liferay_002dui_005fpanel_005f2.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fpanel_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
                      // /html/portlet/control_panel_menu/view.jsp(173,6) name = collapsible type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_005f2.setCollapsible( true );
                      // /html/portlet/control_panel_menu/view.jsp(173,6) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_005f2.setExtended( true );
                      // /html/portlet/control_panel_menu/view.jsp(173,6) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_005f2.setId("sharedPanel");
                      // /html/portlet/control_panel_menu/view.jsp(173,6) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_005f2.setPersistState( true );
                      // /html/portlet/control_panel_menu/view.jsp(173,6) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_005f2.setTitle( LanguageUtil.get(pageContext, "other[plural]") );
                      int _jspx_eval_liferay_002dui_005fpanel_005f2 = _jspx_th_liferay_002dui_005fpanel_005f2.doStartTag();
                      if (_jspx_eval_liferay_002dui_005fpanel_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_liferay_002dui_005fpanel_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_liferay_002dui_005fpanel_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_liferay_002dui_005fpanel_005f2.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t<ul>\n");
                          out.write("\t\t\t\t\t\t\t\t");
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f6 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f6.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
                          // /html/portlet/control_panel_menu/view.jsp(175,8) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f6.setTest( showGlobal );
                          int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
                          if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t<li>\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t<a href=\"");
                              out.print( HttpUtil.setParameter(PortalUtil.getCurrentURL(request), "doAsGroupId", themeDisplay.getCompanyGroupId()) );
                              out.write('"');
                              out.write('>');
                              if (_jspx_meth_liferay_002dui_005fmessage_005f1(_jspx_th_c_005fif_005f6, _jspx_page_context))
                              return;
                              out.write("</a>\n");
                              out.write("\t\t\t\t\t\t\t\t\t</li>\n");
                              out.write("\t\t\t\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fif_005f6.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fif_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f6);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f6);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t");
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f7 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f7.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
                          // /html/portlet/control_panel_menu/view.jsp(180,8) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f7.setTest( showMyCommunity );
                          int _jspx_eval_c_005fif_005f7 = _jspx_th_c_005fif_005f7.doStartTag();
                          if (_jspx_eval_c_005fif_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t<li>\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t<a href=\"");
                              out.print( HttpUtil.setParameter(PortalUtil.getCurrentURL(request), "doAsGroupId", user.getGroup().getGroupId()) );
                              out.write('"');
                              out.write('>');
                              if (_jspx_meth_liferay_002dui_005fmessage_005f2(_jspx_th_c_005fif_005f7, _jspx_page_context))
                              return;
                              out.write("</a>\n");
                              out.write("\t\t\t\t\t\t\t\t\t</li>\n");
                              out.write("\t\t\t\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fif_005f7.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fif_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f7);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f7);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t</ul>\n");
                          out.write("\t\t\t\t\t\t");
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
                      out.write("\n");
                      out.write("\t\t\t\t\t");
                      int evalDoAfterBody = _jspx_th_c_005fif_005f5.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fif_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f5);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f5);
                  out.write("\n");
                  out.write("\t\t\t\t");
                  int evalDoAfterBody = _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dfloating_002dcontainer_0026_005ftrigger_005fpaging_005fid.reuse(_jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dfloating_002dcontainer_0026_005ftrigger_005fpaging_005fid.reuse(_jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f0);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f8 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f8.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
              // /html/portlet/control_panel_menu/view.jsp(190,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f8.setTest( !scopeLayouts.isEmpty() );
              int _jspx_eval_c_005fif_005f8 = _jspx_th_c_005fif_005f8.doStartTag();
              if (_jspx_eval_c_005fif_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  //  liferay-ui:panel-floating-container
                  com.liferay.taglib.ui.PanelFloatingContainerTag _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f1 = (com.liferay.taglib.ui.PanelFloatingContainerTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dfloating_002dcontainer_0026_005ftrigger.get(com.liferay.taglib.ui.PanelFloatingContainerTag.class);
                  _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
                  // /html/portlet/control_panel_menu/view.jsp(191,5) name = trigger type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f1.setTrigger(".lfr-scope-selector");
                  int _jspx_eval_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f1 = _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f1.doStartTag();
                  if (_jspx_eval_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f1.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  liferay-ui:panel
                      com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f3 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle.get(com.liferay.taglib.ui.PanelTag.class);
                      _jspx_th_liferay_002dui_005fpanel_005f3.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fpanel_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f1);
                      // /html/portlet/control_panel_menu/view.jsp(192,6) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_005f3.setTitle("");
                      int _jspx_eval_liferay_002dui_005fpanel_005f3 = _jspx_th_liferay_002dui_005fpanel_005f3.doStartTag();
                      if (_jspx_eval_liferay_002dui_005fpanel_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_liferay_002dui_005fpanel_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_liferay_002dui_005fpanel_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_liferay_002dui_005fpanel_005f3.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t<ul>\n");
                          out.write("\t\t\t\t\t\t\t\t<li>\n");
                          out.write("\t\t\t\t\t\t\t\t\t<a href=\"");
                          out.print( HttpUtil.setParameter(PortalUtil.getCurrentURL(request), "doAsGroupId", curGroup.getGroupId()) );
                          out.write('"');
                          out.write('>');
                          if (_jspx_meth_liferay_002dui_005fmessage_005f3(_jspx_th_liferay_002dui_005fpanel_005f3, _jspx_page_context))
                            return;
                          out.write("</a>\n");
                          out.write("\t\t\t\t\t\t\t\t</li>\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t");

								for (Layout curScopeLayout : scopeLayouts) {
								
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t<li>\n");
                          out.write("\t\t\t\t\t\t\t\t\t\t<a href=\"");
                          out.print( HttpUtil.setParameter(PortalUtil.getCurrentURL(request), "doAsGroupId", curScopeLayout.getScopeGroup().getGroupId()) );
                          out.write('"');
                          out.write('>');
                          out.print( HtmlUtil.escape(curScopeLayout.getName(locale)) );
                          out.write("</a>\n");
                          out.write("\t\t\t\t\t\t\t\t\t</li>\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t");

								}
								
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t</ul>\n");
                          out.write("\t\t\t\t\t\t");
                          int evalDoAfterBody = _jspx_th_liferay_002dui_005fpanel_005f3.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_liferay_002dui_005fpanel_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.popBody();
                        }
                      }
                      if (_jspx_th_liferay_002dui_005fpanel_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle.reuse(_jspx_th_liferay_002dui_005fpanel_005f3);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle.reuse(_jspx_th_liferay_002dui_005fpanel_005f3);
                      out.write("\n");
                      out.write("\t\t\t\t\t");
                      int evalDoAfterBody = _jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dfloating_002dcontainer_0026_005ftrigger.reuse(_jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dfloating_002dcontainer_0026_005ftrigger.reuse(_jspx_th_liferay_002dui_005fpanel_002dfloating_002dcontainer_005f1);
                  out.write("\n");
                  out.write("\t\t\t\t");
                  int evalDoAfterBody = _jspx_th_c_005fif_005f8.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fif_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f8);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f8);
              out.write("\n");
              out.write("\t\t\t");
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
          out.write("\t\t\t");
          //  liferay-ui:panel
          com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f4 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcssClass_005fcollapsible.get(com.liferay.taglib.ui.PanelTag.class);
          _jspx_th_liferay_002dui_005fpanel_005f4.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005fpanel_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
          // /html/portlet/control_panel_menu/view.jsp(216,3) name = collapsible type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_005f4.setCollapsible( true );
          // /html/portlet/control_panel_menu/view.jsp(216,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_005f4.setCssClass("lfr-component panel-page-category");
          // /html/portlet/control_panel_menu/view.jsp(216,3) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_005f4.setExtended( true );
          // /html/portlet/control_panel_menu/view.jsp(216,3) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_005f4.setId( "panel-manage-" + curCategory );
          // /html/portlet/control_panel_menu/view.jsp(216,3) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_005f4.setPersistState( true );
          // /html/portlet/control_panel_menu/view.jsp(216,3) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_005f4.setTitle( title );
          int _jspx_eval_liferay_002dui_005fpanel_005f4 = _jspx_th_liferay_002dui_005fpanel_005f4.doStartTag();
          if (_jspx_eval_liferay_002dui_005fpanel_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_liferay_002dui_005fpanel_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_liferay_002dui_005fpanel_005f4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_liferay_002dui_005fpanel_005f4.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\t\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f9 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f9.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f4);
              // /html/portlet/control_panel_menu/view.jsp(217,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f9.setTest( !scopeLayouts.isEmpty() && curCategory.equals(PortletCategoryKeys.CONTENT) );
              int _jspx_eval_c_005fif_005f9 = _jspx_th_c_005fif_005f9.doStartTag();
              if (_jspx_eval_c_005fif_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t<span class=\"nobr lfr-title-scope-selector\">\n");
                  out.write("\t\t\t\t\t\t");
                  if (_jspx_meth_liferay_002dui_005fmessage_005f4(_jspx_th_c_005fif_005f9, _jspx_page_context))
                    return;
                  out.write(" <a href=\"javascript:;\" class=\"lfr-scope-selector\">");
                  out.print( curGroupLabel );
                  out.write("</a>\n");
                  out.write("\t\t\t\t\t</span>\n");
                  out.write("\t\t\t\t");
                  int evalDoAfterBody = _jspx_th_c_005fif_005f9.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fif_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f9);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f9);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t<ul class=\"category-portlets\">\n");
              out.write("\n");
              out.write("\t\t\t\t\t");

					for (Portlet portlet : portlets) {
						if (portlet.isActive() && !portlet.isInstanceable()) {
					
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t<li class=\"");
              out.print( ppid.equals(portlet.getPortletId()) ? "selected-portlet" : "" );
              out.write("\">\n");
              out.write("\t\t\t\t\t\t\t\t<a href=\"");
              //  liferay-portlet:renderURL
              com.liferay.taglib.portlet.RenderURLTag _jspx_th_liferay_002dportlet_005frenderURL_005f0 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fportletName_005fnobody.get(com.liferay.taglib.portlet.RenderURLTag.class);
              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f4);
              // /html/portlet/control_panel_menu/view.jsp(231,17) name = windowState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setWindowState( WindowState.MAXIMIZED.toString() );
              // /html/portlet/control_panel_menu/view.jsp(231,17) name = portletName type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setPortletName( portlet.getRootPortletId() );
              int _jspx_eval_liferay_002dportlet_005frenderURL_005f0 = _jspx_th_liferay_002dportlet_005frenderURL_005f0.doStartTag();
              if (_jspx_th_liferay_002dportlet_005frenderURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fportletName_005fnobody.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fportletName_005fnobody.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f0);
              out.write("\">\n");
              out.write("\t\t\t\t\t\t\t\t\t");
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f0 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f0.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f4);
              int _jspx_eval_c_005fchoose_005f0 = _jspx_th_c_005fchoose_005f0.doStartTag();
              if (_jspx_eval_c_005fchoose_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f0 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
                  // /html/portlet/control_panel_menu/view.jsp(233,10) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f0.setTest( Validator.isNull(portlet.getIcon()) );
                  int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t\t\t\t");
                      //  liferay-ui:icon
                      com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f0 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fsrc_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                      _jspx_th_liferay_002dui_005ficon_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ficon_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
                      // /html/portlet/control_panel_menu/view.jsp(234,11) name = src type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f0.setSrc( themeDisplay.getPathContext() + "/html/icons/default.png" );
                      int _jspx_eval_liferay_002dui_005ficon_005f0 = _jspx_th_liferay_002dui_005ficon_005f0.doStartTag();
                      if (_jspx_th_liferay_002dui_005ficon_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fsrc_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fsrc_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t\t\t");
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
                  out.write("\t\t\t\t\t\t\t\t\t\t");
                  //  c:otherwise
                  org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                  _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
                  int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
                  if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t\t\t\t");
                      //  liferay-portlet:icon-portlet
                      com.liferay.taglib.portletext.IconPortletTag _jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0 = (com.liferay.taglib.portletext.IconPortletTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody.get(com.liferay.taglib.portletext.IconPortletTag.class);
                      _jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
                      // /html/portlet/control_panel_menu/view.jsp(237,11) name = portlet type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0.setPortlet( portlet );
                      int _jspx_eval_liferay_002dportlet_005ficon_002dportlet_005f0 = _jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0.doStartTag();
                      if (_jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002dportlet_0026_005fportlet_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002dportlet_005f0);
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t\t\t");
                      int evalDoAfterBody = _jspx_th_c_005fotherwise_005f0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fotherwise_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t");
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
              out.write("\t\t\t\t\t\t\t\t\t");
              out.print( PortalUtil.getPortletTitle(portlet, application, locale) );
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t</a>\n");
              out.write("\t\t\t\t\t\t\t</li>\n");
              out.write("\n");
              out.write("\t\t\t\t\t");

						}
					}
					
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t</ul>\n");
              out.write("\t\t\t");
              int evalDoAfterBody = _jspx_th_liferay_002dui_005fpanel_005f4.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_liferay_002dui_005fpanel_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_liferay_002dui_005fpanel_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcssClass_005fcollapsible.reuse(_jspx_th_liferay_002dui_005fpanel_005f4);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcssClass_005fcollapsible.reuse(_jspx_th_liferay_002dui_005fpanel_005f4);
          out.write("\n");
          out.write("\n");
          out.write("\t\t");

		}
		
          out.write('\n');
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
      out.write("\n");
      out.write("</div>\n");
      out.write("\n");
      if (_jspx_meth_aui_005fscript_005f0(_jspx_page_context))
        return;
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

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent(null);
    // /html/portlet/control_panel_menu/view.jsp(20,1) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("control-panel");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f1 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
    // /html/portlet/control_panel_menu/view.jsp(177,133) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f1.setKey("global");
    int _jspx_eval_liferay_002dui_005fmessage_005f1 = _jspx_th_liferay_002dui_005fmessage_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f2 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f2.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
    // /html/portlet/control_panel_menu/view.jsp(182,129) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f2.setKey("my-community");
    int _jspx_eval_liferay_002dui_005fmessage_005f2 = _jspx_th_liferay_002dui_005fmessage_005f2.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f3 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f3.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f3);
    // /html/portlet/control_panel_menu/view.jsp(195,121) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f3.setKey("default");
    int _jspx_eval_liferay_002dui_005fmessage_005f3 = _jspx_th_liferay_002dui_005fmessage_005f3.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f9, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f4 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f4.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f9);
    // /html/portlet/control_panel_menu/view.jsp(219,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f4.setKey("scope");
    int _jspx_eval_liferay_002dui_005fmessage_005f4 = _jspx_th_liferay_002dui_005fmessage_005f4.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
    return false;
  }

  private boolean _jspx_meth_aui_005fscript_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:script
    com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
    _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fscript_005f0.setParent(null);
    // /html/portlet/control_panel_menu/view.jsp(260,0) name = use type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fscript_005f0.setUse("lfr-panel-floating");
    int _jspx_eval_aui_005fscript_005f0 = _jspx_th_aui_005fscript_005f0.doStartTag();
    if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_005fscript_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_005fscript_005f0.doInitBody();
      }
      do {
        out.write("\n");
        out.write("\tvar groupSelectorPanel = Liferay.Panel.get('groupSelectorPanel');\n");
        out.write("\n");
        out.write("\tif (groupSelectorPanel) {\n");
        out.write("\t\tgroupSelectorPanel.get('trigger').swallowEvent('mousedown');\n");
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
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f0);
    return false;
  }
}
