package org.apache.jsp.html.common.themes;

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

public final class portlet_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(28);
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/common/themes/portlet_content_wrapper.jspf");
    _jspx_dependants.add("/html/common/themes/portlet_content.jspf");
    _jspx_dependants.add("/html/common/themes/portlet_messages.jspf");
    _jspx_dependants.add("/html/common/themes/portlet_facebook.jspf");
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
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005ftiles_005fuseAttribute_0026_005fname_005fignore_005fid_005fclassname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dtheme_005fwrap_002dportlet_0026_005fpage;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002doptions_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005ftiles_005fuseAttribute_0026_005fname_005fignore_005fid_005fclassname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fwrap_002dportlet_0026_005fpage = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002doptions_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005ftiles_005fuseAttribute_0026_005fname_005fignore_005fid_005fclassname_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fc_005fotherwise.release();
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fwrap_002dportlet_0026_005fpage.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002doptions_005fnobody.release();
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

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
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
      //  tiles:useAttribute
      org.apache.struts.taglib.tiles.UseAttributeTag _jspx_th_tiles_005fuseAttribute_005f0 = (org.apache.struts.taglib.tiles.UseAttributeTag) _005fjspx_005ftagPool_005ftiles_005fuseAttribute_0026_005fname_005fignore_005fid_005fclassname_005fnobody.get(org.apache.struts.taglib.tiles.UseAttributeTag.class);
      _jspx_th_tiles_005fuseAttribute_005f0.setPageContext(_jspx_page_context);
      _jspx_th_tiles_005fuseAttribute_005f0.setParent(null);
      // /html/common/themes/portlet.jsp(21,0) name = id type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_tiles_005fuseAttribute_005f0.setId("tilesPortletContent");
      // /html/common/themes/portlet.jsp(21,0) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_tiles_005fuseAttribute_005f0.setName("portlet_content");
      // /html/common/themes/portlet.jsp(21,0) name = classname type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_tiles_005fuseAttribute_005f0.setClassname("java.lang.String");
      // /html/common/themes/portlet.jsp(21,0) name = ignore type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_tiles_005fuseAttribute_005f0.setIgnore(true);
      int _jspx_eval_tiles_005fuseAttribute_005f0 = _jspx_th_tiles_005fuseAttribute_005f0.doStartTag();
      if (_jspx_th_tiles_005fuseAttribute_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005ftiles_005fuseAttribute_0026_005fname_005fignore_005fid_005fclassname_005fnobody.reuse(_jspx_th_tiles_005fuseAttribute_005f0);
        return;
      }
      _005fjspx_005ftagPool_005ftiles_005fuseAttribute_0026_005fname_005fignore_005fid_005fclassname_005fnobody.reuse(_jspx_th_tiles_005fuseAttribute_005f0);
      java.lang.String tilesPortletContent = null;
      tilesPortletContent = (java.lang.String) _jspx_page_context.findAttribute("tilesPortletContent");
      out.write('\n');
      //  tiles:useAttribute
      org.apache.struts.taglib.tiles.UseAttributeTag _jspx_th_tiles_005fuseAttribute_005f1 = (org.apache.struts.taglib.tiles.UseAttributeTag) _005fjspx_005ftagPool_005ftiles_005fuseAttribute_0026_005fname_005fignore_005fid_005fclassname_005fnobody.get(org.apache.struts.taglib.tiles.UseAttributeTag.class);
      _jspx_th_tiles_005fuseAttribute_005f1.setPageContext(_jspx_page_context);
      _jspx_th_tiles_005fuseAttribute_005f1.setParent(null);
      // /html/common/themes/portlet.jsp(22,0) name = id type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_tiles_005fuseAttribute_005f1.setId("tilesPortletDecorate");
      // /html/common/themes/portlet.jsp(22,0) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_tiles_005fuseAttribute_005f1.setName("portlet_decorate");
      // /html/common/themes/portlet.jsp(22,0) name = classname type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_tiles_005fuseAttribute_005f1.setClassname("java.lang.String");
      // /html/common/themes/portlet.jsp(22,0) name = ignore type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_tiles_005fuseAttribute_005f1.setIgnore(true);
      int _jspx_eval_tiles_005fuseAttribute_005f1 = _jspx_th_tiles_005fuseAttribute_005f1.doStartTag();
      if (_jspx_th_tiles_005fuseAttribute_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005ftiles_005fuseAttribute_0026_005fname_005fignore_005fid_005fclassname_005fnobody.reuse(_jspx_th_tiles_005fuseAttribute_005f1);
        return;
      }
      _005fjspx_005ftagPool_005ftiles_005fuseAttribute_0026_005fname_005fignore_005fid_005fclassname_005fnobody.reuse(_jspx_th_tiles_005fuseAttribute_005f1);
      java.lang.String tilesPortletDecorate = null;
      tilesPortletDecorate = (java.lang.String) _jspx_page_context.findAttribute("tilesPortletDecorate");
      out.write('\n');
      //  tiles:useAttribute
      org.apache.struts.taglib.tiles.UseAttributeTag _jspx_th_tiles_005fuseAttribute_005f2 = (org.apache.struts.taglib.tiles.UseAttributeTag) _005fjspx_005ftagPool_005ftiles_005fuseAttribute_0026_005fname_005fignore_005fid_005fclassname_005fnobody.get(org.apache.struts.taglib.tiles.UseAttributeTag.class);
      _jspx_th_tiles_005fuseAttribute_005f2.setPageContext(_jspx_page_context);
      _jspx_th_tiles_005fuseAttribute_005f2.setParent(null);
      // /html/common/themes/portlet.jsp(23,0) name = id type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_tiles_005fuseAttribute_005f2.setId("tilesPortletPadding");
      // /html/common/themes/portlet.jsp(23,0) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_tiles_005fuseAttribute_005f2.setName("portlet_padding");
      // /html/common/themes/portlet.jsp(23,0) name = classname type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_tiles_005fuseAttribute_005f2.setClassname("java.lang.String");
      // /html/common/themes/portlet.jsp(23,0) name = ignore type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_tiles_005fuseAttribute_005f2.setIgnore(true);
      int _jspx_eval_tiles_005fuseAttribute_005f2 = _jspx_th_tiles_005fuseAttribute_005f2.doStartTag();
      if (_jspx_th_tiles_005fuseAttribute_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005ftiles_005fuseAttribute_0026_005fname_005fignore_005fid_005fclassname_005fnobody.reuse(_jspx_th_tiles_005fuseAttribute_005f2);
        return;
      }
      _005fjspx_005ftagPool_005ftiles_005fuseAttribute_0026_005fname_005fignore_005fid_005fclassname_005fnobody.reuse(_jspx_th_tiles_005fuseAttribute_005f2);
      java.lang.String tilesPortletPadding = null;
      tilesPortletPadding = (java.lang.String) _jspx_page_context.findAttribute("tilesPortletPadding");
      out.write('\n');
      out.write('\n');

Portlet portlet = (Portlet)request.getAttribute(WebKeys.RENDER_PORTLET);

PortletPreferences portletSetup = PortletPreferencesFactoryUtil.getLayoutPortletSetup(layout, portletDisplay.getId());

RenderResponseImpl renderResponseImpl = (RenderResponseImpl)PortletResponseImpl.getPortletResponseImpl(renderResponse);

// Portlet decorate

boolean tilesPortletDecorateBoolean = GetterUtil.getBoolean(tilesPortletDecorate, true);

boolean portletDecorateDefault = false;

if (tilesPortletDecorateBoolean) {
	portletDecorateDefault = GetterUtil.getBoolean(theme.getSetting("portlet-setup-show-borders-default"), PropsValues.THEME_PORTLET_DECORATE_DEFAULT);
}

boolean portletDecorate = GetterUtil.getBoolean(portletSetup.getValue("portlet-setup-show-borders", String.valueOf(portletDecorateDefault)));

Boolean portletDecorateObj = (Boolean)renderRequest.getAttribute(WebKeys.PORTLET_DECORATE);

if (portletDecorateObj != null) {
	portletDecorate = portletDecorateObj.booleanValue();

	request.removeAttribute(WebKeys.PORTLET_DECORATE);
}

// Portlet title

String portletTitle = PortletConfigurationUtil.getPortletTitle(portletSetup, themeDisplay.getLanguageId());

if (portletDisplay.isAccess() && portletDisplay.isActive()) {
	if (Validator.isNull(portletTitle)) {
		portletTitle = renderResponseImpl.getTitle();
	}
}

ResourceBundle resourceBundle = portletConfig.getResourceBundle(locale);

if (Validator.isNull(portletTitle)) {
	portletTitle = resourceBundle.getString(JavaConstants.JAVAX_PORTLET_TITLE);
}

portletDisplay.setTitle(portletTitle);

// Portlet description

String portletDescription = StringPool.BLANK;

try {
	portletDescription = resourceBundle.getString(JavaConstants.JAVAX_PORTLET_DESCRIPTION);
}
catch (MissingResourceException mre) {
}

if (Validator.isNull(portletDescription)) {
	portletDescription = PortalUtil.getPortletDescription(portlet.getPortletId(), locale);
}

portletDisplay.setDescription(portletDescription);

Group group = layout.getGroup();

boolean wsrp = ParamUtil.getBoolean(request, "wsrp");

      out.write('\n');
      out.write('\n');
      //  c:choose
      org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f0 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
      _jspx_th_c_005fchoose_005f0.setPageContext(_jspx_page_context);
      _jspx_th_c_005fchoose_005f0.setParent(null);
      int _jspx_eval_c_005fchoose_005f0 = _jspx_th_c_005fchoose_005f0.doStartTag();
      if (_jspx_eval_c_005fchoose_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('\n');
          out.write('	');
          //  c:when
          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f0 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
          _jspx_th_c_005fwhen_005f0.setPageContext(_jspx_page_context);
          _jspx_th_c_005fwhen_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
          // /html/common/themes/portlet.jsp(92,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fwhen_005f0.setTest( wsrp );
          int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
          if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t<liferay-wsrp-portlet>\n");
              out.write("\t\t\t");

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
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              // /html/common/themes/portlet_content_wrapper.jspf(17,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f0.setTest( !themeDisplay.isStateExclusive() );
              int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
              if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t<div class=\"portlet-body\">\n");
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
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
              if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write('\n');
                  out.write('	');
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f1 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                  // /html/common/themes/portlet_content_wrapper.jspf(22,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f1.setTest( portletDisplay.isActive() );
                  int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write('\n');
                      out.write('	');
                      out.write('	');
                      //  c:choose
                      org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f2 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                      _jspx_th_c_005fchoose_005f2.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fchoose_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                      int _jspx_eval_c_005fchoose_005f2 = _jspx_th_c_005fchoose_005f2.doStartTag();
                      if (_jspx_eval_c_005fchoose_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t");
                          //  c:when
                          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f2 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                          _jspx_th_c_005fwhen_005f2.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fwhen_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
                          // /html/common/themes/portlet_content_wrapper.jspf(24,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fwhen_005f2.setTest( themeDisplay.isStateExclusive() );
                          int _jspx_eval_c_005fwhen_005f2 = _jspx_th_c_005fwhen_005f2.doStartTag();
                          if (_jspx_eval_c_005fwhen_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t");

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
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f3 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                              int _jspx_eval_c_005fchoose_005f3 = _jspx_th_c_005fchoose_005f3.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f3 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
                              // /html/common/themes/portlet_content.jspf(18,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f3.setTest( Validator.isNull(tilesPortletContent) );
                              int _jspx_eval_c_005fwhen_005f3 = _jspx_th_c_005fwhen_005f3.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");

		pageContext.getOut().print(renderRequest.getAttribute(WebKeys.PORTLET_CONTENT));
		
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
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
                              out.write('\n');
                              out.write('	');
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
                              int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  liferay-util:include
                              com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f0 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
                              _jspx_th_liferay_002dutil_005finclude_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dutil_005finclude_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
                              // /html/common/themes/portlet_content.jspf(26,2) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dutil_005finclude_005f0.setPage( StrutsUtil.TEXT_HTML_DIR + tilesPortletContent );
                              int _jspx_eval_liferay_002dutil_005finclude_005f0 = _jspx_th_liferay_002dutil_005finclude_005f0.doStartTag();
                              if (_jspx_th_liferay_002dutil_005finclude_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f0);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f0);
                              out.write('\n');
                              out.write('	');
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
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f3.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f3);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f3);
                              out.write("\n");
                              out.write("\t\t\t");
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
                          out.write("\t\t\t");
                          //  c:when
                          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f4 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                          _jspx_th_c_005fwhen_005f4.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fwhen_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
                          // /html/common/themes/portlet_content_wrapper.jspf(27,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fwhen_005f4.setTest( portletDisplay.isAccess() );
                          int _jspx_eval_c_005fwhen_005f4 = _jspx_th_c_005fwhen_005f4.doStartTag();
                          if (_jspx_eval_c_005fwhen_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                              // /html/common/themes/portlet_content_wrapper.jspf(28,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f1.setTest( !tilesPortletContent.endsWith("/error.jsp") );
                              int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
                              if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t");

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

Group liveGroup = group;

boolean inStaging = false;

if (group.isControlPanel()) {
	long doAsGroupId = ParamUtil.getLong(request, "doAsGroupId");

	if (doAsGroupId > 0) {
		try {
			liveGroup = GroupLocalServiceUtil.getGroup(doAsGroupId);

			if (liveGroup.isStagingGroup()) {
				liveGroup = liveGroup.getLiveGroup();

				inStaging = true;
			}
		}
		catch (Exception e) {
		}
	}
}
else if (group.isStagingGroup()) {
	liveGroup = group.getLiveGroup();

	inStaging = true;
}

                              out.write('\n');
                              out.write('\n');
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
                              // /html/common/themes/portlet_messages.jspf(46,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f2.setTest( liveGroup.isStaged() && !liveGroup.isStagedPortlet(portlet.getRootPortletId()) );
                              int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
                              if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f4 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
                              int _jspx_eval_c_005fchoose_005f4 = _jspx_th_c_005fchoose_005f4.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f5 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f5.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f4);
                              // /html/common/themes/portlet_messages.jspf(48,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f5.setTest( !liveGroup.isStagedRemotely() && inStaging );
                              int _jspx_eval_c_005fwhen_005f5 = _jspx_th_c_005fwhen_005f5.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t<div class=\"portlet-msg-alert\">\n");
                              out.write("\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_th_c_005fwhen_005f5, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t</div>\n");
                              out.write("\t\t");
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
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f6 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f6.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f4);
                              // /html/common/themes/portlet_messages.jspf(53,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f6.setTest( liveGroup.isStagedRemotely() && themeDisplay.isSignedIn() );
                              int _jspx_eval_c_005fwhen_005f6 = _jspx_th_c_005fwhen_005f6.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t<div class=\"portlet-msg-alert\">\n");
                              out.write("\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f1(_jspx_th_c_005fwhen_005f6, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t</div>\n");
                              out.write("\t\t");
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
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f4.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f4);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f4);
                              out.write('\n');
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
                              out.write('\n');
                              out.write('\n');
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
                              // /html/common/themes/portlet_messages.jspf(61,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f3.setTest( SessionMessages.contains(renderRequest, "request_processed") );
                              int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
                              if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t<div class=\"portlet-msg-success\">\n");
                              out.write("\n");
                              out.write("\t\t");

		String successMessage = (String)SessionMessages.get(renderRequest, "request_processed");
		
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f5 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f5.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
                              int _jspx_eval_c_005fchoose_005f5 = _jspx_th_c_005fchoose_005f5.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f7 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f7.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f5);
                              // /html/common/themes/portlet_messages.jspf(69,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f7.setTest( Validator.isNotNull(successMessage) && !successMessage.equals("request_processed") );
                              int _jspx_eval_c_005fwhen_005f7 = _jspx_th_c_005fwhen_005f7.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t");
                              out.print( successMessage );
                              out.write("\n");
                              out.write("\t\t\t");
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
                              out.write("\t\t\t");
                              if (_jspx_meth_c_005fotherwise_005f1(_jspx_th_c_005fchoose_005f5, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f5.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f5);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f5);
                              out.write("\n");
                              out.write("\t</div>\n");
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
                              out.write('\n');
                              out.write('\n');
                              //  liferay-ui:success
                              com.liferay.taglib.ui.SuccessTag _jspx_th_liferay_002dui_005fsuccess_005f0 = (com.liferay.taglib.ui.SuccessTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.SuccessTag.class);
                              _jspx_th_liferay_002dui_005fsuccess_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsuccess_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
                              // /html/common/themes/portlet_messages.jspf(79,0) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f0.setKey( portletName + ".doConfigure" );
                              // /html/common/themes/portlet_messages.jspf(79,0) name = message type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f0.setMessage("you-have-successfully-updated-the-setup");
                              int _jspx_eval_liferay_002dui_005fsuccess_005f0 = _jspx_th_liferay_002dui_005fsuccess_005f0.doStartTag();
                              if (_jspx_th_liferay_002dui_005fsuccess_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f0);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f0);
                              out.write('\n');
                              //  liferay-ui:success
                              com.liferay.taglib.ui.SuccessTag _jspx_th_liferay_002dui_005fsuccess_005f1 = (com.liferay.taglib.ui.SuccessTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.SuccessTag.class);
                              _jspx_th_liferay_002dui_005fsuccess_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsuccess_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
                              // /html/common/themes/portlet_messages.jspf(80,0) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f1.setKey( portletName + ".doEdit" );
                              // /html/common/themes/portlet_messages.jspf(80,0) name = message type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f1.setMessage("you-have-successfully-updated-your-preferences");
                              int _jspx_eval_liferay_002dui_005fsuccess_005f1 = _jspx_th_liferay_002dui_005fsuccess_005f1.doStartTag();
                              if (_jspx_th_liferay_002dui_005fsuccess_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f1);
                              out.write('\n');
                              out.write('\n');
                              //  liferay-ui:error
                              com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f0 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                              _jspx_th_liferay_002dui_005ferror_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ferror_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
                              int _jspx_eval_liferay_002dui_005ferror_005f0 = _jspx_th_liferay_002dui_005ferror_005f0.doStartTag();
                              if (_jspx_th_liferay_002dui_005ferror_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f0);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f0);
                              out.write("\n");
                              out.write("\t\t\t\t");
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
                              out.write("\t\t\t\t");
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f6 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f6.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                              int _jspx_eval_c_005fchoose_005f6 = _jspx_th_c_005fchoose_005f6.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f8 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f8.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f6);
                              // /html/common/themes/portlet_content_wrapper.jspf(33,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f8.setTest( group.isLayoutPrototype() && layoutTypePortlet.hasPortletId(portletDisplay.getId()) );
                              int _jspx_eval_c_005fwhen_005f8 = _jspx_th_c_005fwhen_005f8.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t<div class=\"portlet-msg-info\">\n");
                              out.write("\t\t\t\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f3(_jspx_th_c_005fwhen_005f8, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t\t\t\t</div>\n");
                              out.write("\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f8.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f8);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f8);
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f2 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f6);
                              int _jspx_eval_c_005fotherwise_005f2 = _jspx_th_c_005fotherwise_005f2.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");

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
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f7 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f7.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f2);
                              int _jspx_eval_c_005fchoose_005f7 = _jspx_th_c_005fchoose_005f7.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f9 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f9.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f7);
                              // /html/common/themes/portlet_content.jspf(18,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f9.setTest( Validator.isNull(tilesPortletContent) );
                              int _jspx_eval_c_005fwhen_005f9 = _jspx_th_c_005fwhen_005f9.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");

		pageContext.getOut().print(renderRequest.getAttribute(WebKeys.PORTLET_CONTENT));
		
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f9.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f9);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f9);
                              out.write('\n');
                              out.write('	');
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f3 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f7);
                              int _jspx_eval_c_005fotherwise_005f3 = _jspx_th_c_005fotherwise_005f3.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  liferay-util:include
                              com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f1 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
                              _jspx_th_liferay_002dutil_005finclude_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dutil_005finclude_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f3);
                              // /html/common/themes/portlet_content.jspf(26,2) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dutil_005finclude_005f1.setPage( StrutsUtil.TEXT_HTML_DIR + tilesPortletContent );
                              int _jspx_eval_liferay_002dutil_005finclude_005f1 = _jspx_th_liferay_002dutil_005finclude_005f1.doStartTag();
                              if (_jspx_th_liferay_002dutil_005finclude_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f1);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f3.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fotherwise_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f3);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f3);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f7.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f7);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f7);
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f2.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fotherwise_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f2);
                              out.write("\n");
                              out.write("\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f6.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f6);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f6);
                              out.write("\n");
                              out.write("\t\t\t");
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
                          out.write("\t\t\t");
                          if (_jspx_meth_c_005fotherwise_005f4(_jspx_th_c_005fchoose_005f2, _jspx_page_context))
                            return;
                          out.write('\n');
                          out.write('	');
                          out.write('	');
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
                      out.write('\n');
                      out.write('	');
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
                  out.write('\n');
                  out.write('	');
                  if (_jspx_meth_c_005fotherwise_005f5(_jspx_th_c_005fchoose_005f1, _jspx_page_context))
                    return;
                  out.write('\n');
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
              out.write('\n');
              out.write('\n');
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              // /html/common/themes/portlet_content_wrapper.jspf(53,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f4.setTest( !themeDisplay.isStateExclusive() );
              int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
              if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t</div>\n");
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
              out.write("\t\t</liferay-wsrp-portlet>\n");
              out.write("\t");
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
          out.write('\n');
          out.write('	');
          //  c:when
          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f10 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
          _jspx_th_c_005fwhen_005f10.setPageContext(_jspx_page_context);
          _jspx_th_c_005fwhen_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
          // /html/common/themes/portlet.jsp(97,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fwhen_005f10.setTest( themeDisplay.isFacebook() );
          int _jspx_eval_c_005fwhen_005f10 = _jspx_th_c_005fwhen_005f10.doStartTag();
          if (_jspx_eval_c_005fwhen_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\n');
              out.write('	');
              out.write('	');

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
              out.write("<fb:title>");
              out.print( portletTitle );
              out.write("</fb:title>\n");
              out.write("\n");
              out.write("<fb:header>");
              out.print( portletTitle );
              out.write("</fb:header>\n");
              out.write("\n");
              out.write("<link href=\"");
              out.print( PortalUtil.getStaticResourceURL(request, themeDisplay.getPortalURL() + "/html/portal/css.jsp", "themeId=classic&colorSchemeId=01") );
              out.write("\" rel=\"stylesheet\" type=\"text/css\" />\n");
              out.write("<link href=\"");
              out.print( PortalUtil.getStaticResourceURL(request, themeDisplay.getPortalURL() + "/html/themes/classic/css/portlet.css") );
              out.write("\" rel=\"stylesheet\" type=\"text/css\" />\n");
              out.write("\n");

Set<String> headerPortalCssSet = new LinkedHashSet<String>();

for (String headerPortalCss : portlet.getHeaderPortalCss()) {
	if (!HttpUtil.hasProtocol(headerPortalCss)) {
		headerPortalCss = PortalUtil.getStaticResourceURL(request, request.getContextPath() + headerPortalCss, portlet.getTimestamp());
	}

	if (!headerPortalCssSet.contains(headerPortalCss)) {
		headerPortalCssSet.add(headerPortalCss);

              out.write("\n");
              out.write("\n");
              out.write("\t\t<link href=\"");
              out.print( HtmlUtil.escape(headerPortalCss) );
              out.write("\" rel=\"stylesheet\" type=\"text/css\" />\n");
              out.write("\n");

	}
}

Set<String> headerPortletCssSet = new LinkedHashSet<String>();

for (String headerPortletCss : portlet.getHeaderPortletCss()) {
	if (!HttpUtil.hasProtocol(headerPortletCss)) {
		headerPortletCss = PortalUtil.getStaticResourceURL(request, portlet.getContextPath() + headerPortletCss, portlet.getTimestamp());
	}

	if (!headerPortletCssSet.contains(headerPortletCss)) {
		headerPortletCssSet.add(headerPortletCss);

              out.write("\n");
              out.write("\n");
              out.write("\t\t<link href=\"");
              out.print( HtmlUtil.escape(headerPortletCss) );
              out.write("\" rel=\"stylesheet\" type=\"text/css\" />\n");
              out.write("\n");

	}
}

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
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f5 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f10);
              // /html/common/themes/portlet_content_wrapper.jspf(17,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f5.setTest( !themeDisplay.isStateExclusive() );
              int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
              if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t<div class=\"portlet-body\">\n");
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
              out.write('\n');
              out.write('\n');
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f8 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f8.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f10);
              int _jspx_eval_c_005fchoose_005f8 = _jspx_th_c_005fchoose_005f8.doStartTag();
              if (_jspx_eval_c_005fchoose_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write('\n');
                  out.write('	');
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f11 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f11.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f8);
                  // /html/common/themes/portlet_content_wrapper.jspf(22,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f11.setTest( portletDisplay.isActive() );
                  int _jspx_eval_c_005fwhen_005f11 = _jspx_th_c_005fwhen_005f11.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write('\n');
                      out.write('	');
                      out.write('	');
                      //  c:choose
                      org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f9 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                      _jspx_th_c_005fchoose_005f9.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fchoose_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f11);
                      int _jspx_eval_c_005fchoose_005f9 = _jspx_th_c_005fchoose_005f9.doStartTag();
                      if (_jspx_eval_c_005fchoose_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t");
                          //  c:when
                          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f12 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                          _jspx_th_c_005fwhen_005f12.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fwhen_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f9);
                          // /html/common/themes/portlet_content_wrapper.jspf(24,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fwhen_005f12.setTest( themeDisplay.isStateExclusive() );
                          int _jspx_eval_c_005fwhen_005f12 = _jspx_th_c_005fwhen_005f12.doStartTag();
                          if (_jspx_eval_c_005fwhen_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t");

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
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f10 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f10.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f12);
                              int _jspx_eval_c_005fchoose_005f10 = _jspx_th_c_005fchoose_005f10.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f13 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f13.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f10);
                              // /html/common/themes/portlet_content.jspf(18,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f13.setTest( Validator.isNull(tilesPortletContent) );
                              int _jspx_eval_c_005fwhen_005f13 = _jspx_th_c_005fwhen_005f13.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");

		pageContext.getOut().print(renderRequest.getAttribute(WebKeys.PORTLET_CONTENT));
		
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f13.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f13);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f13);
                              out.write('\n');
                              out.write('	');
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f6 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f6.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f10);
                              int _jspx_eval_c_005fotherwise_005f6 = _jspx_th_c_005fotherwise_005f6.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  liferay-util:include
                              com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f4 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
                              _jspx_th_liferay_002dutil_005finclude_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dutil_005finclude_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f6);
                              // /html/common/themes/portlet_content.jspf(26,2) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dutil_005finclude_005f4.setPage( StrutsUtil.TEXT_HTML_DIR + tilesPortletContent );
                              int _jspx_eval_liferay_002dutil_005finclude_005f4 = _jspx_th_liferay_002dutil_005finclude_005f4.doStartTag();
                              if (_jspx_th_liferay_002dutil_005finclude_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f4);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f4);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f6.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fotherwise_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f6);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f6);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f10.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f10);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f10);
                              out.write("\n");
                              out.write("\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f12.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fwhen_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f12);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f12);
                          out.write("\n");
                          out.write("\t\t\t");
                          //  c:when
                          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f14 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                          _jspx_th_c_005fwhen_005f14.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fwhen_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f9);
                          // /html/common/themes/portlet_content_wrapper.jspf(27,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fwhen_005f14.setTest( portletDisplay.isAccess() );
                          int _jspx_eval_c_005fwhen_005f14 = _jspx_th_c_005fwhen_005f14.doStartTag();
                          if (_jspx_eval_c_005fwhen_005f14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f6 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f6.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f14);
                              // /html/common/themes/portlet_content_wrapper.jspf(28,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f6.setTest( !tilesPortletContent.endsWith("/error.jsp") );
                              int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
                              if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t");

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

Group liveGroup = group;

boolean inStaging = false;

if (group.isControlPanel()) {
	long doAsGroupId = ParamUtil.getLong(request, "doAsGroupId");

	if (doAsGroupId > 0) {
		try {
			liveGroup = GroupLocalServiceUtil.getGroup(doAsGroupId);

			if (liveGroup.isStagingGroup()) {
				liveGroup = liveGroup.getLiveGroup();

				inStaging = true;
			}
		}
		catch (Exception e) {
		}
	}
}
else if (group.isStagingGroup()) {
	liveGroup = group.getLiveGroup();

	inStaging = true;
}

                              out.write('\n');
                              out.write('\n');
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f7 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f7.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
                              // /html/common/themes/portlet_messages.jspf(46,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f7.setTest( liveGroup.isStaged() && !liveGroup.isStagedPortlet(portlet.getRootPortletId()) );
                              int _jspx_eval_c_005fif_005f7 = _jspx_th_c_005fif_005f7.doStartTag();
                              if (_jspx_eval_c_005fif_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f11 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f11.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
                              int _jspx_eval_c_005fchoose_005f11 = _jspx_th_c_005fchoose_005f11.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f15 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f15.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f11);
                              // /html/common/themes/portlet_messages.jspf(48,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f15.setTest( !liveGroup.isStagedRemotely() && inStaging );
                              int _jspx_eval_c_005fwhen_005f15 = _jspx_th_c_005fwhen_005f15.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t<div class=\"portlet-msg-alert\">\n");
                              out.write("\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f4(_jspx_th_c_005fwhen_005f15, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t</div>\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f15.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f15);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f15);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f16 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f16.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f11);
                              // /html/common/themes/portlet_messages.jspf(53,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f16.setTest( liveGroup.isStagedRemotely() && themeDisplay.isSignedIn() );
                              int _jspx_eval_c_005fwhen_005f16 = _jspx_th_c_005fwhen_005f16.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f16 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t<div class=\"portlet-msg-alert\">\n");
                              out.write("\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f5(_jspx_th_c_005fwhen_005f16, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t</div>\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f16.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f16);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f16);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f11.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f11);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f11);
                              out.write('\n');
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
                              out.write('\n');
                              out.write('\n');
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f8 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f8.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
                              // /html/common/themes/portlet_messages.jspf(61,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f8.setTest( SessionMessages.contains(renderRequest, "request_processed") );
                              int _jspx_eval_c_005fif_005f8 = _jspx_th_c_005fif_005f8.doStartTag();
                              if (_jspx_eval_c_005fif_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t<div class=\"portlet-msg-success\">\n");
                              out.write("\n");
                              out.write("\t\t");

		String successMessage = (String)SessionMessages.get(renderRequest, "request_processed");
		
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f12 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f12.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
                              int _jspx_eval_c_005fchoose_005f12 = _jspx_th_c_005fchoose_005f12.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f17 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f17.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f12);
                              // /html/common/themes/portlet_messages.jspf(69,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f17.setTest( Validator.isNotNull(successMessage) && !successMessage.equals("request_processed") );
                              int _jspx_eval_c_005fwhen_005f17 = _jspx_th_c_005fwhen_005f17.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f17 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t");
                              out.print( successMessage );
                              out.write("\n");
                              out.write("\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f17.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f17);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f17);
                              out.write("\n");
                              out.write("\t\t\t");
                              if (_jspx_meth_c_005fotherwise_005f7(_jspx_th_c_005fchoose_005f12, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f12.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f12);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f12);
                              out.write("\n");
                              out.write("\t</div>\n");
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
                              out.write('\n');
                              out.write('\n');
                              //  liferay-ui:success
                              com.liferay.taglib.ui.SuccessTag _jspx_th_liferay_002dui_005fsuccess_005f2 = (com.liferay.taglib.ui.SuccessTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.SuccessTag.class);
                              _jspx_th_liferay_002dui_005fsuccess_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsuccess_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
                              // /html/common/themes/portlet_messages.jspf(79,0) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f2.setKey( portletName + ".doConfigure" );
                              // /html/common/themes/portlet_messages.jspf(79,0) name = message type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f2.setMessage("you-have-successfully-updated-the-setup");
                              int _jspx_eval_liferay_002dui_005fsuccess_005f2 = _jspx_th_liferay_002dui_005fsuccess_005f2.doStartTag();
                              if (_jspx_th_liferay_002dui_005fsuccess_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f2);
                              out.write('\n');
                              //  liferay-ui:success
                              com.liferay.taglib.ui.SuccessTag _jspx_th_liferay_002dui_005fsuccess_005f3 = (com.liferay.taglib.ui.SuccessTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.SuccessTag.class);
                              _jspx_th_liferay_002dui_005fsuccess_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsuccess_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
                              // /html/common/themes/portlet_messages.jspf(80,0) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f3.setKey( portletName + ".doEdit" );
                              // /html/common/themes/portlet_messages.jspf(80,0) name = message type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f3.setMessage("you-have-successfully-updated-your-preferences");
                              int _jspx_eval_liferay_002dui_005fsuccess_005f3 = _jspx_th_liferay_002dui_005fsuccess_005f3.doStartTag();
                              if (_jspx_th_liferay_002dui_005fsuccess_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f3);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f3);
                              out.write('\n');
                              out.write('\n');
                              //  liferay-ui:error
                              com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f1 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                              _jspx_th_liferay_002dui_005ferror_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ferror_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
                              int _jspx_eval_liferay_002dui_005ferror_005f1 = _jspx_th_liferay_002dui_005ferror_005f1.doStartTag();
                              if (_jspx_th_liferay_002dui_005ferror_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f1);
                              out.write("\n");
                              out.write("\t\t\t\t");
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
                              out.write("\n");
                              out.write("\t\t\t\t");
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f13 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f13.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f14);
                              int _jspx_eval_c_005fchoose_005f13 = _jspx_th_c_005fchoose_005f13.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f18 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f18.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f13);
                              // /html/common/themes/portlet_content_wrapper.jspf(33,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f18.setTest( group.isLayoutPrototype() && layoutTypePortlet.hasPortletId(portletDisplay.getId()) );
                              int _jspx_eval_c_005fwhen_005f18 = _jspx_th_c_005fwhen_005f18.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f18 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t<div class=\"portlet-msg-info\">\n");
                              out.write("\t\t\t\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f7(_jspx_th_c_005fwhen_005f18, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t\t\t\t</div>\n");
                              out.write("\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f18.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f18);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f18);
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f8 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f8.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f13);
                              int _jspx_eval_c_005fotherwise_005f8 = _jspx_th_c_005fotherwise_005f8.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");

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
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f14 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f14.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f8);
                              int _jspx_eval_c_005fchoose_005f14 = _jspx_th_c_005fchoose_005f14.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f19 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f19.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f14);
                              // /html/common/themes/portlet_content.jspf(18,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f19.setTest( Validator.isNull(tilesPortletContent) );
                              int _jspx_eval_c_005fwhen_005f19 = _jspx_th_c_005fwhen_005f19.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f19 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");

		pageContext.getOut().print(renderRequest.getAttribute(WebKeys.PORTLET_CONTENT));
		
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f19.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f19);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f19);
                              out.write('\n');
                              out.write('	');
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f9 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f9.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f14);
                              int _jspx_eval_c_005fotherwise_005f9 = _jspx_th_c_005fotherwise_005f9.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  liferay-util:include
                              com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f5 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
                              _jspx_th_liferay_002dutil_005finclude_005f5.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dutil_005finclude_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f9);
                              // /html/common/themes/portlet_content.jspf(26,2) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dutil_005finclude_005f5.setPage( StrutsUtil.TEXT_HTML_DIR + tilesPortletContent );
                              int _jspx_eval_liferay_002dutil_005finclude_005f5 = _jspx_th_liferay_002dutil_005finclude_005f5.doStartTag();
                              if (_jspx_th_liferay_002dutil_005finclude_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f5);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f5);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f9.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fotherwise_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f9);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f9);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f14.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f14);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f14);
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f8.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fotherwise_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f8);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f8);
                              out.write("\n");
                              out.write("\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f13.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f13);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f13);
                              out.write("\n");
                              out.write("\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f14.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fwhen_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f14);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f14);
                          out.write("\n");
                          out.write("\t\t\t");
                          if (_jspx_meth_c_005fotherwise_005f10(_jspx_th_c_005fchoose_005f9, _jspx_page_context))
                            return;
                          out.write('\n');
                          out.write('	');
                          out.write('	');
                          int evalDoAfterBody = _jspx_th_c_005fchoose_005f9.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                      }
                      if (_jspx_th_c_005fchoose_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f9);
                        return;
                      }
                      _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f9);
                      out.write('\n');
                      out.write('	');
                      int evalDoAfterBody = _jspx_th_c_005fwhen_005f11.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fwhen_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f11);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f11);
                  out.write('\n');
                  out.write('	');
                  if (_jspx_meth_c_005fotherwise_005f11(_jspx_th_c_005fchoose_005f8, _jspx_page_context))
                    return;
                  out.write('\n');
                  int evalDoAfterBody = _jspx_th_c_005fchoose_005f8.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fchoose_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f8);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f8);
              out.write('\n');
              out.write('\n');
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f9 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f9.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f10);
              // /html/common/themes/portlet_content_wrapper.jspf(53,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f9.setTest( !themeDisplay.isStateExclusive() );
              int _jspx_eval_c_005fif_005f9 = _jspx_th_c_005fif_005f9.doStartTag();
              if (_jspx_eval_c_005fif_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t</div>\n");
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
              out.write('\n');
              out.write('\n');

Set<String> footerPortalCssSet = new LinkedHashSet<String>();

for (String footerPortalCss : portlet.getFooterPortalCss()) {
	if (!HttpUtil.hasProtocol(footerPortalCss)) {
		footerPortalCss = PortalUtil.getStaticResourceURL(request, request.getContextPath() + footerPortalCss, portlet.getTimestamp());
	}

	if (!footerPortalCssSet.contains(footerPortalCss)) {
		footerPortalCssSet.add(footerPortalCss);

              out.write("\n");
              out.write("\n");
              out.write("\t\t<link href=\"");
              out.print( HtmlUtil.escape(footerPortalCss) );
              out.write("\" rel=\"stylesheet\" type=\"text/css\" />\n");
              out.write("\n");

	}
}

Set<String> footerPortletCssSet = new LinkedHashSet<String>();

for (String footerPortletCss : portlet.getFooterPortletCss()) {
	if (!HttpUtil.hasProtocol(footerPortletCss)) {
		footerPortletCss = PortalUtil.getStaticResourceURL(request, portlet.getContextPath() + footerPortletCss, portlet.getTimestamp());
	}

	if (!footerPortletCssSet.contains(footerPortletCss)) {
		footerPortletCssSet.add(footerPortletCss);

              out.write("\n");
              out.write("\n");
              out.write("\t\t<link href=\"");
              out.print( HtmlUtil.escape(footerPortletCss) );
              out.write("\" rel=\"stylesheet\" type=\"text/css\" />\n");
              out.write("\n");

	}
}

              out.write('\n');
              out.write('	');
              int evalDoAfterBody = _jspx_th_c_005fwhen_005f10.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fwhen_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f10);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f10);
          out.write('\n');
          out.write('	');
          //  c:when
          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f20 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
          _jspx_th_c_005fwhen_005f20.setPageContext(_jspx_page_context);
          _jspx_th_c_005fwhen_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
          // /html/common/themes/portlet.jsp(100,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fwhen_005f20.setTest( themeDisplay.isStateExclusive() );
          int _jspx_eval_c_005fwhen_005f20 = _jspx_th_c_005fwhen_005f20.doStartTag();
          if (_jspx_eval_c_005fwhen_005f20 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\n');
              out.write('	');
              out.write('	');

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
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f10 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f10.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f20);
              // /html/common/themes/portlet_content_wrapper.jspf(17,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f10.setTest( !themeDisplay.isStateExclusive() );
              int _jspx_eval_c_005fif_005f10 = _jspx_th_c_005fif_005f10.doStartTag();
              if (_jspx_eval_c_005fif_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t<div class=\"portlet-body\">\n");
                  int evalDoAfterBody = _jspx_th_c_005fif_005f10.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fif_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f10);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f10);
              out.write('\n');
              out.write('\n');
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f15 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f15.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f20);
              int _jspx_eval_c_005fchoose_005f15 = _jspx_th_c_005fchoose_005f15.doStartTag();
              if (_jspx_eval_c_005fchoose_005f15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write('\n');
                  out.write('	');
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f21 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f21.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f15);
                  // /html/common/themes/portlet_content_wrapper.jspf(22,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f21.setTest( portletDisplay.isActive() );
                  int _jspx_eval_c_005fwhen_005f21 = _jspx_th_c_005fwhen_005f21.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f21 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write('\n');
                      out.write('	');
                      out.write('	');
                      //  c:choose
                      org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f16 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                      _jspx_th_c_005fchoose_005f16.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fchoose_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f21);
                      int _jspx_eval_c_005fchoose_005f16 = _jspx_th_c_005fchoose_005f16.doStartTag();
                      if (_jspx_eval_c_005fchoose_005f16 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t");
                          //  c:when
                          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f22 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                          _jspx_th_c_005fwhen_005f22.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fwhen_005f22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f16);
                          // /html/common/themes/portlet_content_wrapper.jspf(24,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fwhen_005f22.setTest( themeDisplay.isStateExclusive() );
                          int _jspx_eval_c_005fwhen_005f22 = _jspx_th_c_005fwhen_005f22.doStartTag();
                          if (_jspx_eval_c_005fwhen_005f22 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t");

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
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f17 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f17.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f22);
                              int _jspx_eval_c_005fchoose_005f17 = _jspx_th_c_005fchoose_005f17.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f17 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f23 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f23.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f17);
                              // /html/common/themes/portlet_content.jspf(18,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f23.setTest( Validator.isNull(tilesPortletContent) );
                              int _jspx_eval_c_005fwhen_005f23 = _jspx_th_c_005fwhen_005f23.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f23 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");

		pageContext.getOut().print(renderRequest.getAttribute(WebKeys.PORTLET_CONTENT));
		
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f23.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f23);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f23);
                              out.write('\n');
                              out.write('	');
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f12 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f12.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f17);
                              int _jspx_eval_c_005fotherwise_005f12 = _jspx_th_c_005fotherwise_005f12.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  liferay-util:include
                              com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f8 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
                              _jspx_th_liferay_002dutil_005finclude_005f8.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dutil_005finclude_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f12);
                              // /html/common/themes/portlet_content.jspf(26,2) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dutil_005finclude_005f8.setPage( StrutsUtil.TEXT_HTML_DIR + tilesPortletContent );
                              int _jspx_eval_liferay_002dutil_005finclude_005f8 = _jspx_th_liferay_002dutil_005finclude_005f8.doStartTag();
                              if (_jspx_th_liferay_002dutil_005finclude_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f8);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f8);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f12.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fotherwise_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f12);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f12);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f17.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f17);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f17);
                              out.write("\n");
                              out.write("\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f22.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fwhen_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f22);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f22);
                          out.write("\n");
                          out.write("\t\t\t");
                          //  c:when
                          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f24 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                          _jspx_th_c_005fwhen_005f24.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fwhen_005f24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f16);
                          // /html/common/themes/portlet_content_wrapper.jspf(27,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fwhen_005f24.setTest( portletDisplay.isAccess() );
                          int _jspx_eval_c_005fwhen_005f24 = _jspx_th_c_005fwhen_005f24.doStartTag();
                          if (_jspx_eval_c_005fwhen_005f24 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f11 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f11.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f24);
                              // /html/common/themes/portlet_content_wrapper.jspf(28,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f11.setTest( !tilesPortletContent.endsWith("/error.jsp") );
                              int _jspx_eval_c_005fif_005f11 = _jspx_th_c_005fif_005f11.doStartTag();
                              if (_jspx_eval_c_005fif_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t");

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

Group liveGroup = group;

boolean inStaging = false;

if (group.isControlPanel()) {
	long doAsGroupId = ParamUtil.getLong(request, "doAsGroupId");

	if (doAsGroupId > 0) {
		try {
			liveGroup = GroupLocalServiceUtil.getGroup(doAsGroupId);

			if (liveGroup.isStagingGroup()) {
				liveGroup = liveGroup.getLiveGroup();

				inStaging = true;
			}
		}
		catch (Exception e) {
		}
	}
}
else if (group.isStagingGroup()) {
	liveGroup = group.getLiveGroup();

	inStaging = true;
}

                              out.write('\n');
                              out.write('\n');
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f12 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f12.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f11);
                              // /html/common/themes/portlet_messages.jspf(46,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f12.setTest( liveGroup.isStaged() && !liveGroup.isStagedPortlet(portlet.getRootPortletId()) );
                              int _jspx_eval_c_005fif_005f12 = _jspx_th_c_005fif_005f12.doStartTag();
                              if (_jspx_eval_c_005fif_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f18 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f18.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f12);
                              int _jspx_eval_c_005fchoose_005f18 = _jspx_th_c_005fchoose_005f18.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f18 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f25 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f25.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f18);
                              // /html/common/themes/portlet_messages.jspf(48,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f25.setTest( !liveGroup.isStagedRemotely() && inStaging );
                              int _jspx_eval_c_005fwhen_005f25 = _jspx_th_c_005fwhen_005f25.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f25 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t<div class=\"portlet-msg-alert\">\n");
                              out.write("\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f8(_jspx_th_c_005fwhen_005f25, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t</div>\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f25.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f25);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f25);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f26 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f26.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f18);
                              // /html/common/themes/portlet_messages.jspf(53,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f26.setTest( liveGroup.isStagedRemotely() && themeDisplay.isSignedIn() );
                              int _jspx_eval_c_005fwhen_005f26 = _jspx_th_c_005fwhen_005f26.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f26 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t<div class=\"portlet-msg-alert\">\n");
                              out.write("\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f9(_jspx_th_c_005fwhen_005f26, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t</div>\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f26.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f26);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f26);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f18.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f18);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f18);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fif_005f12.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fif_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f12);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f12);
                              out.write('\n');
                              out.write('\n');
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f13 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f13.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f11);
                              // /html/common/themes/portlet_messages.jspf(61,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f13.setTest( SessionMessages.contains(renderRequest, "request_processed") );
                              int _jspx_eval_c_005fif_005f13 = _jspx_th_c_005fif_005f13.doStartTag();
                              if (_jspx_eval_c_005fif_005f13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t<div class=\"portlet-msg-success\">\n");
                              out.write("\n");
                              out.write("\t\t");

		String successMessage = (String)SessionMessages.get(renderRequest, "request_processed");
		
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f19 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f19.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f13);
                              int _jspx_eval_c_005fchoose_005f19 = _jspx_th_c_005fchoose_005f19.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f19 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f27 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f27.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f19);
                              // /html/common/themes/portlet_messages.jspf(69,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f27.setTest( Validator.isNotNull(successMessage) && !successMessage.equals("request_processed") );
                              int _jspx_eval_c_005fwhen_005f27 = _jspx_th_c_005fwhen_005f27.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f27 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t");
                              out.print( successMessage );
                              out.write("\n");
                              out.write("\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f27.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f27);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f27);
                              out.write("\n");
                              out.write("\t\t\t");
                              if (_jspx_meth_c_005fotherwise_005f13(_jspx_th_c_005fchoose_005f19, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f19.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f19);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f19);
                              out.write("\n");
                              out.write("\t</div>\n");
                              int evalDoAfterBody = _jspx_th_c_005fif_005f13.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fif_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f13);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f13);
                              out.write('\n');
                              out.write('\n');
                              //  liferay-ui:success
                              com.liferay.taglib.ui.SuccessTag _jspx_th_liferay_002dui_005fsuccess_005f4 = (com.liferay.taglib.ui.SuccessTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.SuccessTag.class);
                              _jspx_th_liferay_002dui_005fsuccess_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsuccess_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f11);
                              // /html/common/themes/portlet_messages.jspf(79,0) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f4.setKey( portletName + ".doConfigure" );
                              // /html/common/themes/portlet_messages.jspf(79,0) name = message type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f4.setMessage("you-have-successfully-updated-the-setup");
                              int _jspx_eval_liferay_002dui_005fsuccess_005f4 = _jspx_th_liferay_002dui_005fsuccess_005f4.doStartTag();
                              if (_jspx_th_liferay_002dui_005fsuccess_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f4);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f4);
                              out.write('\n');
                              //  liferay-ui:success
                              com.liferay.taglib.ui.SuccessTag _jspx_th_liferay_002dui_005fsuccess_005f5 = (com.liferay.taglib.ui.SuccessTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.SuccessTag.class);
                              _jspx_th_liferay_002dui_005fsuccess_005f5.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsuccess_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f11);
                              // /html/common/themes/portlet_messages.jspf(80,0) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f5.setKey( portletName + ".doEdit" );
                              // /html/common/themes/portlet_messages.jspf(80,0) name = message type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f5.setMessage("you-have-successfully-updated-your-preferences");
                              int _jspx_eval_liferay_002dui_005fsuccess_005f5 = _jspx_th_liferay_002dui_005fsuccess_005f5.doStartTag();
                              if (_jspx_th_liferay_002dui_005fsuccess_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f5);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f5);
                              out.write('\n');
                              out.write('\n');
                              //  liferay-ui:error
                              com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f2 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                              _jspx_th_liferay_002dui_005ferror_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ferror_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f11);
                              int _jspx_eval_liferay_002dui_005ferror_005f2 = _jspx_th_liferay_002dui_005ferror_005f2.doStartTag();
                              if (_jspx_th_liferay_002dui_005ferror_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f2);
                              out.write("\n");
                              out.write("\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fif_005f11.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fif_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f11);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f11);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t");
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f20 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f20.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f24);
                              int _jspx_eval_c_005fchoose_005f20 = _jspx_th_c_005fchoose_005f20.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f20 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f28 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f28.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f20);
                              // /html/common/themes/portlet_content_wrapper.jspf(33,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f28.setTest( group.isLayoutPrototype() && layoutTypePortlet.hasPortletId(portletDisplay.getId()) );
                              int _jspx_eval_c_005fwhen_005f28 = _jspx_th_c_005fwhen_005f28.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f28 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t<div class=\"portlet-msg-info\">\n");
                              out.write("\t\t\t\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f11(_jspx_th_c_005fwhen_005f28, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t\t\t\t</div>\n");
                              out.write("\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f28.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f28);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f28);
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f14 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f14.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f20);
                              int _jspx_eval_c_005fotherwise_005f14 = _jspx_th_c_005fotherwise_005f14.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");

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
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f21 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f21.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f14);
                              int _jspx_eval_c_005fchoose_005f21 = _jspx_th_c_005fchoose_005f21.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f21 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f29 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f29.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f29.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f21);
                              // /html/common/themes/portlet_content.jspf(18,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f29.setTest( Validator.isNull(tilesPortletContent) );
                              int _jspx_eval_c_005fwhen_005f29 = _jspx_th_c_005fwhen_005f29.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f29 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");

		pageContext.getOut().print(renderRequest.getAttribute(WebKeys.PORTLET_CONTENT));
		
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f29.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f29.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f29);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f29);
                              out.write('\n');
                              out.write('	');
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f15 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f15.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f21);
                              int _jspx_eval_c_005fotherwise_005f15 = _jspx_th_c_005fotherwise_005f15.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  liferay-util:include
                              com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f9 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
                              _jspx_th_liferay_002dutil_005finclude_005f9.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dutil_005finclude_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f15);
                              // /html/common/themes/portlet_content.jspf(26,2) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dutil_005finclude_005f9.setPage( StrutsUtil.TEXT_HTML_DIR + tilesPortletContent );
                              int _jspx_eval_liferay_002dutil_005finclude_005f9 = _jspx_th_liferay_002dutil_005finclude_005f9.doStartTag();
                              if (_jspx_th_liferay_002dutil_005finclude_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f9);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f9);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f15.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fotherwise_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f15);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f15);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f21.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f21);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f21);
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f14.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fotherwise_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f14);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f14);
                              out.write("\n");
                              out.write("\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f20.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f20);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f20);
                              out.write("\n");
                              out.write("\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f24.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fwhen_005f24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f24);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f24);
                          out.write("\n");
                          out.write("\t\t\t");
                          if (_jspx_meth_c_005fotherwise_005f16(_jspx_th_c_005fchoose_005f16, _jspx_page_context))
                            return;
                          out.write('\n');
                          out.write('	');
                          out.write('	');
                          int evalDoAfterBody = _jspx_th_c_005fchoose_005f16.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                      }
                      if (_jspx_th_c_005fchoose_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f16);
                        return;
                      }
                      _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f16);
                      out.write('\n');
                      out.write('	');
                      int evalDoAfterBody = _jspx_th_c_005fwhen_005f21.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fwhen_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f21);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f21);
                  out.write('\n');
                  out.write('	');
                  if (_jspx_meth_c_005fotherwise_005f17(_jspx_th_c_005fchoose_005f15, _jspx_page_context))
                    return;
                  out.write('\n');
                  int evalDoAfterBody = _jspx_th_c_005fchoose_005f15.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fchoose_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f15);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f15);
              out.write('\n');
              out.write('\n');
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f14 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f14.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f20);
              // /html/common/themes/portlet_content_wrapper.jspf(53,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f14.setTest( !themeDisplay.isStateExclusive() );
              int _jspx_eval_c_005fif_005f14 = _jspx_th_c_005fif_005f14.doStartTag();
              if (_jspx_eval_c_005fif_005f14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t</div>\n");
                  int evalDoAfterBody = _jspx_th_c_005fif_005f14.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fif_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f14);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f14);
              out.write('\n');
              out.write('	');
              int evalDoAfterBody = _jspx_th_c_005fwhen_005f20.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fwhen_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f20);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f20);
          out.write('\n');
          out.write('	');
          //  c:when
          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f30 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
          _jspx_th_c_005fwhen_005f30.setPageContext(_jspx_page_context);
          _jspx_th_c_005fwhen_005f30.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
          // /html/common/themes/portlet.jsp(103,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fwhen_005f30.setTest( themeDisplay.isStatePopUp() );
          int _jspx_eval_c_005fwhen_005f30 = _jspx_th_c_005fwhen_005f30.doStartTag();
          if (_jspx_eval_c_005fwhen_005f30 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t<div class=\"portlet-body\">\n");
              out.write("\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f15 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f15.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f30);
              // /html/common/themes/portlet.jsp(105,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f15.setTest( Validator.isNotNull(tilesPortletContent) );
              int _jspx_eval_c_005fif_005f15 = _jspx_th_c_005fif_005f15.doStartTag();
              if (_jspx_eval_c_005fif_005f15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f16 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f16.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f15);
                  // /html/common/themes/portlet.jsp(106,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f16.setTest( !tilesPortletContent.endsWith("/error.jsp") );
                  int _jspx_eval_c_005fif_005f16 = _jspx_th_c_005fif_005f16.doStartTag();
                  if (_jspx_eval_c_005fif_005f16 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t");

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

Group liveGroup = group;

boolean inStaging = false;

if (group.isControlPanel()) {
	long doAsGroupId = ParamUtil.getLong(request, "doAsGroupId");

	if (doAsGroupId > 0) {
		try {
			liveGroup = GroupLocalServiceUtil.getGroup(doAsGroupId);

			if (liveGroup.isStagingGroup()) {
				liveGroup = liveGroup.getLiveGroup();

				inStaging = true;
			}
		}
		catch (Exception e) {
		}
	}
}
else if (group.isStagingGroup()) {
	liveGroup = group.getLiveGroup();

	inStaging = true;
}

                      out.write('\n');
                      out.write('\n');
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f17 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f17.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f16);
                      // /html/common/themes/portlet_messages.jspf(46,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f17.setTest( liveGroup.isStaged() && !liveGroup.isStagedPortlet(portlet.getRootPortletId()) );
                      int _jspx_eval_c_005fif_005f17 = _jspx_th_c_005fif_005f17.doStartTag();
                      if (_jspx_eval_c_005fif_005f17 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write('\n');
                          out.write('	');
                          //  c:choose
                          org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f22 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                          _jspx_th_c_005fchoose_005f22.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fchoose_005f22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f17);
                          int _jspx_eval_c_005fchoose_005f22 = _jspx_th_c_005fchoose_005f22.doStartTag();
                          if (_jspx_eval_c_005fchoose_005f22 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f31 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f31.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f31.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f22);
                              // /html/common/themes/portlet_messages.jspf(48,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f31.setTest( !liveGroup.isStagedRemotely() && inStaging );
                              int _jspx_eval_c_005fwhen_005f31 = _jspx_th_c_005fwhen_005f31.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f31 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t<div class=\"portlet-msg-alert\">\n");
                              out.write("\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f12(_jspx_th_c_005fwhen_005f31, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t</div>\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f31.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f31.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f31);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f31);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f32 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f32.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f32.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f22);
                              // /html/common/themes/portlet_messages.jspf(53,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f32.setTest( liveGroup.isStagedRemotely() && themeDisplay.isSignedIn() );
                              int _jspx_eval_c_005fwhen_005f32 = _jspx_th_c_005fwhen_005f32.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f32 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t<div class=\"portlet-msg-alert\">\n");
                              out.write("\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f13(_jspx_th_c_005fwhen_005f32, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t</div>\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f32.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f32.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f32);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f32);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f22.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fchoose_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f22);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f22);
                          out.write('\n');
                          int evalDoAfterBody = _jspx_th_c_005fif_005f17.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                      }
                      if (_jspx_th_c_005fif_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f17);
                        return;
                      }
                      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f17);
                      out.write('\n');
                      out.write('\n');
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f18 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f18.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f16);
                      // /html/common/themes/portlet_messages.jspf(61,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f18.setTest( SessionMessages.contains(renderRequest, "request_processed") );
                      int _jspx_eval_c_005fif_005f18 = _jspx_th_c_005fif_005f18.doStartTag();
                      if (_jspx_eval_c_005fif_005f18 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t<div class=\"portlet-msg-success\">\n");
                          out.write("\n");
                          out.write("\t\t");

		String successMessage = (String)SessionMessages.get(renderRequest, "request_processed");
		
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t");
                          //  c:choose
                          org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f23 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                          _jspx_th_c_005fchoose_005f23.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fchoose_005f23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f18);
                          int _jspx_eval_c_005fchoose_005f23 = _jspx_th_c_005fchoose_005f23.doStartTag();
                          if (_jspx_eval_c_005fchoose_005f23 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f33 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f33.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f33.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f23);
                              // /html/common/themes/portlet_messages.jspf(69,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f33.setTest( Validator.isNotNull(successMessage) && !successMessage.equals("request_processed") );
                              int _jspx_eval_c_005fwhen_005f33 = _jspx_th_c_005fwhen_005f33.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f33 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t");
                              out.print( successMessage );
                              out.write("\n");
                              out.write("\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f33.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f33.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f33);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f33);
                              out.write("\n");
                              out.write("\t\t\t");
                              if (_jspx_meth_c_005fotherwise_005f18(_jspx_th_c_005fchoose_005f23, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f23.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fchoose_005f23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f23);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f23);
                          out.write("\n");
                          out.write("\t</div>\n");
                          int evalDoAfterBody = _jspx_th_c_005fif_005f18.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                      }
                      if (_jspx_th_c_005fif_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f18);
                        return;
                      }
                      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f18);
                      out.write('\n');
                      out.write('\n');
                      //  liferay-ui:success
                      com.liferay.taglib.ui.SuccessTag _jspx_th_liferay_002dui_005fsuccess_005f6 = (com.liferay.taglib.ui.SuccessTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.SuccessTag.class);
                      _jspx_th_liferay_002dui_005fsuccess_005f6.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fsuccess_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f16);
                      // /html/common/themes/portlet_messages.jspf(79,0) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fsuccess_005f6.setKey( portletName + ".doConfigure" );
                      // /html/common/themes/portlet_messages.jspf(79,0) name = message type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fsuccess_005f6.setMessage("you-have-successfully-updated-the-setup");
                      int _jspx_eval_liferay_002dui_005fsuccess_005f6 = _jspx_th_liferay_002dui_005fsuccess_005f6.doStartTag();
                      if (_jspx_th_liferay_002dui_005fsuccess_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f6);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f6);
                      out.write('\n');
                      //  liferay-ui:success
                      com.liferay.taglib.ui.SuccessTag _jspx_th_liferay_002dui_005fsuccess_005f7 = (com.liferay.taglib.ui.SuccessTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.SuccessTag.class);
                      _jspx_th_liferay_002dui_005fsuccess_005f7.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fsuccess_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f16);
                      // /html/common/themes/portlet_messages.jspf(80,0) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fsuccess_005f7.setKey( portletName + ".doEdit" );
                      // /html/common/themes/portlet_messages.jspf(80,0) name = message type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fsuccess_005f7.setMessage("you-have-successfully-updated-your-preferences");
                      int _jspx_eval_liferay_002dui_005fsuccess_005f7 = _jspx_th_liferay_002dui_005fsuccess_005f7.doStartTag();
                      if (_jspx_th_liferay_002dui_005fsuccess_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f7);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f7);
                      out.write('\n');
                      out.write('\n');
                      //  liferay-ui:error
                      com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f3 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                      _jspx_th_liferay_002dui_005ferror_005f3.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ferror_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f16);
                      int _jspx_eval_liferay_002dui_005ferror_005f3 = _jspx_th_liferay_002dui_005ferror_005f3.doStartTag();
                      if (_jspx_th_liferay_002dui_005ferror_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f3);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f3);
                      out.write("\n");
                      out.write("\t\t\t\t");
                      int evalDoAfterBody = _jspx_th_c_005fif_005f16.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fif_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f16);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f16);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t");
                  //  liferay-util:include
                  com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f12 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
                  _jspx_th_liferay_002dutil_005finclude_005f12.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dutil_005finclude_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f15);
                  // /html/common/themes/portlet.jsp(110,4) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dutil_005finclude_005f12.setPage( StrutsUtil.TEXT_HTML_DIR + tilesPortletContent );
                  int _jspx_eval_liferay_002dutil_005finclude_005f12 = _jspx_th_liferay_002dutil_005finclude_005f12.doStartTag();
                  if (_jspx_th_liferay_002dutil_005finclude_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f12);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f12);
                  out.write("\n");
                  out.write("\t\t\t");
                  int evalDoAfterBody = _jspx_th_c_005fif_005f15.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fif_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f15);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f15);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f19 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f19.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f30);
              // /html/common/themes/portlet.jsp(113,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f19.setTest( Validator.isNull(tilesPortletContent) );
              int _jspx_eval_c_005fif_005f19 = _jspx_th_c_005fif_005f19.doStartTag();
              if (_jspx_eval_c_005fif_005f19 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t");

				pageContext.getOut().print(renderRequest.getAttribute(WebKeys.PORTLET_CONTENT));
				
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  int evalDoAfterBody = _jspx_th_c_005fif_005f19.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fif_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f19);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f19);
              out.write("\n");
              out.write("\t\t</div>\n");
              out.write("\t");
              int evalDoAfterBody = _jspx_th_c_005fwhen_005f30.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fwhen_005f30.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f30);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f30);
          out.write('\n');
          out.write('	');
          //  c:otherwise
          org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f19 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
          _jspx_th_c_005fotherwise_005f19.setPageContext(_jspx_page_context);
          _jspx_th_c_005fotherwise_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
          int _jspx_eval_c_005fotherwise_005f19 = _jspx_th_c_005fotherwise_005f19.doStartTag();
          if (_jspx_eval_c_005fotherwise_005f19 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

		Boolean renderPortletResource = (Boolean)request.getAttribute(WebKeys.RENDER_PORTLET_RESOURCE);

		boolean runtimePortlet = (renderPortletResource != null) && renderPortletResource.booleanValue();

		boolean freeformPortlet = themeDisplay.isFreeformLayout() && !runtimePortlet && !layoutTypePortlet.hasStateMax();

		String containerStyles = StringPool.BLANK;

		if (freeformPortlet) {
			Properties freeformStyleProps = PropertiesUtil.load(portletSetup.getValue("portlet-freeform-styles", StringPool.BLANK));

			containerStyles = "style=\"height: ".concat(GetterUtil.getString(freeformStyleProps.getProperty("height"), "300px")).concat("; overflow: auto;\"");
		}
		else {
			containerStyles = "style=\"\"";
		}
		
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f24 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f24.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f19);
              int _jspx_eval_c_005fchoose_005f24 = _jspx_th_c_005fchoose_005f24.doStartTag();
              if (_jspx_eval_c_005fchoose_005f24 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f34 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f34.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f34.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f24);
                  // /html/common/themes/portlet.jsp(144,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f34.setTest( portletDecorate );
                  int _jspx_eval_c_005fwhen_005f34 = _jspx_th_c_005fwhen_005f34.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f34 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  liferay-theme:wrap-portlet
                      com.liferay.taglib.theme.WrapPortletTag _jspx_th_liferay_002dtheme_005fwrap_002dportlet_005f0 = (com.liferay.taglib.theme.WrapPortletTag) _005fjspx_005ftagPool_005fliferay_002dtheme_005fwrap_002dportlet_0026_005fpage.get(com.liferay.taglib.theme.WrapPortletTag.class);
                      _jspx_th_liferay_002dtheme_005fwrap_002dportlet_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dtheme_005fwrap_002dportlet_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f34);
                      // /html/common/themes/portlet.jsp(145,4) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dtheme_005fwrap_002dportlet_005f0.setPage("portlet.jsp");
                      int _jspx_eval_liferay_002dtheme_005fwrap_002dportlet_005f0 = _jspx_th_liferay_002dtheme_005fwrap_002dportlet_005f0.doStartTag();
                      if (_jspx_eval_liferay_002dtheme_005fwrap_002dportlet_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_liferay_002dtheme_005fwrap_002dportlet_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_liferay_002dtheme_005fwrap_002dportlet_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_liferay_002dtheme_005fwrap_002dportlet_005f0.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t<div class=\"");
                          out.print( portletDisplay.isStateMin() ? "aui-helper-hidden" : "" );
                          out.write(" portlet-content-container\" ");
                          out.print( containerStyles );
                          out.write(">\n");
                          out.write("\t\t\t\t\t\t");

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
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f20 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f20.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dtheme_005fwrap_002dportlet_005f0);
                          // /html/common/themes/portlet_content_wrapper.jspf(17,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f20.setTest( !themeDisplay.isStateExclusive() );
                          int _jspx_eval_c_005fif_005f20 = _jspx_th_c_005fif_005f20.doStartTag();
                          if (_jspx_eval_c_005fif_005f20 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t<div class=\"portlet-body\">\n");
                              int evalDoAfterBody = _jspx_th_c_005fif_005f20.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fif_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f20);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f20);
                          out.write('\n');
                          out.write('\n');
                          //  c:choose
                          org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f25 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                          _jspx_th_c_005fchoose_005f25.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fchoose_005f25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dtheme_005fwrap_002dportlet_005f0);
                          int _jspx_eval_c_005fchoose_005f25 = _jspx_th_c_005fchoose_005f25.doStartTag();
                          if (_jspx_eval_c_005fchoose_005f25 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write('\n');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f35 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f35.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f35.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f25);
                              // /html/common/themes/portlet_content_wrapper.jspf(22,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f35.setTest( portletDisplay.isActive() );
                              int _jspx_eval_c_005fwhen_005f35 = _jspx_th_c_005fwhen_005f35.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f35 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f26 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f26.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f35);
                              int _jspx_eval_c_005fchoose_005f26 = _jspx_th_c_005fchoose_005f26.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f26 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f36 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f36.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f36.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f26);
                              // /html/common/themes/portlet_content_wrapper.jspf(24,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f36.setTest( themeDisplay.isStateExclusive() );
                              int _jspx_eval_c_005fwhen_005f36 = _jspx_th_c_005fwhen_005f36.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f36 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t");

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
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f27 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f27.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f36);
                              int _jspx_eval_c_005fchoose_005f27 = _jspx_th_c_005fchoose_005f27.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f27 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f37 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f37.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f37.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f27);
                              // /html/common/themes/portlet_content.jspf(18,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f37.setTest( Validator.isNull(tilesPortletContent) );
                              int _jspx_eval_c_005fwhen_005f37 = _jspx_th_c_005fwhen_005f37.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f37 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");

		pageContext.getOut().print(renderRequest.getAttribute(WebKeys.PORTLET_CONTENT));
		
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f37.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f37.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f37);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f37);
                              out.write('\n');
                              out.write('	');
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f20 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f20.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f27);
                              int _jspx_eval_c_005fotherwise_005f20 = _jspx_th_c_005fotherwise_005f20.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f20 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  liferay-util:include
                              com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f13 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
                              _jspx_th_liferay_002dutil_005finclude_005f13.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dutil_005finclude_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f20);
                              // /html/common/themes/portlet_content.jspf(26,2) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dutil_005finclude_005f13.setPage( StrutsUtil.TEXT_HTML_DIR + tilesPortletContent );
                              int _jspx_eval_liferay_002dutil_005finclude_005f13 = _jspx_th_liferay_002dutil_005finclude_005f13.doStartTag();
                              if (_jspx_th_liferay_002dutil_005finclude_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f13);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f13);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f20.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fotherwise_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f20);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f20);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f27.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f27);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f27);
                              out.write("\n");
                              out.write("\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f36.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f36.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f36);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f36);
                              out.write("\n");
                              out.write("\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f38 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f38.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f38.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f26);
                              // /html/common/themes/portlet_content_wrapper.jspf(27,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f38.setTest( portletDisplay.isAccess() );
                              int _jspx_eval_c_005fwhen_005f38 = _jspx_th_c_005fwhen_005f38.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f38 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f21 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f21.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f38);
                              // /html/common/themes/portlet_content_wrapper.jspf(28,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f21.setTest( !tilesPortletContent.endsWith("/error.jsp") );
                              int _jspx_eval_c_005fif_005f21 = _jspx_th_c_005fif_005f21.doStartTag();
                              if (_jspx_eval_c_005fif_005f21 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t");

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

Group liveGroup = group;

boolean inStaging = false;

if (group.isControlPanel()) {
	long doAsGroupId = ParamUtil.getLong(request, "doAsGroupId");

	if (doAsGroupId > 0) {
		try {
			liveGroup = GroupLocalServiceUtil.getGroup(doAsGroupId);

			if (liveGroup.isStagingGroup()) {
				liveGroup = liveGroup.getLiveGroup();

				inStaging = true;
			}
		}
		catch (Exception e) {
		}
	}
}
else if (group.isStagingGroup()) {
	liveGroup = group.getLiveGroup();

	inStaging = true;
}

                              out.write('\n');
                              out.write('\n');
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f22 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f22.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f21);
                              // /html/common/themes/portlet_messages.jspf(46,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f22.setTest( liveGroup.isStaged() && !liveGroup.isStagedPortlet(portlet.getRootPortletId()) );
                              int _jspx_eval_c_005fif_005f22 = _jspx_th_c_005fif_005f22.doStartTag();
                              if (_jspx_eval_c_005fif_005f22 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f28 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f28.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f22);
                              int _jspx_eval_c_005fchoose_005f28 = _jspx_th_c_005fchoose_005f28.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f28 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f39 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f39.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f39.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f28);
                              // /html/common/themes/portlet_messages.jspf(48,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f39.setTest( !liveGroup.isStagedRemotely() && inStaging );
                              int _jspx_eval_c_005fwhen_005f39 = _jspx_th_c_005fwhen_005f39.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f39 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t<div class=\"portlet-msg-alert\">\n");
                              out.write("\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f15(_jspx_th_c_005fwhen_005f39, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t</div>\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f39.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f39.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f39);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f39);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f40 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f40.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f40.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f28);
                              // /html/common/themes/portlet_messages.jspf(53,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f40.setTest( liveGroup.isStagedRemotely() && themeDisplay.isSignedIn() );
                              int _jspx_eval_c_005fwhen_005f40 = _jspx_th_c_005fwhen_005f40.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f40 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t<div class=\"portlet-msg-alert\">\n");
                              out.write("\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f16(_jspx_th_c_005fwhen_005f40, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t</div>\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f40.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f40.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f40);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f40);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f28.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f28);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f28);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fif_005f22.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fif_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f22);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f22);
                              out.write('\n');
                              out.write('\n');
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f23 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f23.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f21);
                              // /html/common/themes/portlet_messages.jspf(61,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f23.setTest( SessionMessages.contains(renderRequest, "request_processed") );
                              int _jspx_eval_c_005fif_005f23 = _jspx_th_c_005fif_005f23.doStartTag();
                              if (_jspx_eval_c_005fif_005f23 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t<div class=\"portlet-msg-success\">\n");
                              out.write("\n");
                              out.write("\t\t");

		String successMessage = (String)SessionMessages.get(renderRequest, "request_processed");
		
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f29 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f29.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f29.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f23);
                              int _jspx_eval_c_005fchoose_005f29 = _jspx_th_c_005fchoose_005f29.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f29 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f41 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f41.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f41.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f29);
                              // /html/common/themes/portlet_messages.jspf(69,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f41.setTest( Validator.isNotNull(successMessage) && !successMessage.equals("request_processed") );
                              int _jspx_eval_c_005fwhen_005f41 = _jspx_th_c_005fwhen_005f41.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f41 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t");
                              out.print( successMessage );
                              out.write("\n");
                              out.write("\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f41.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f41.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f41);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f41);
                              out.write("\n");
                              out.write("\t\t\t");
                              if (_jspx_meth_c_005fotherwise_005f21(_jspx_th_c_005fchoose_005f29, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f29.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f29.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f29);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f29);
                              out.write("\n");
                              out.write("\t</div>\n");
                              int evalDoAfterBody = _jspx_th_c_005fif_005f23.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fif_005f23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f23);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f23);
                              out.write('\n');
                              out.write('\n');
                              //  liferay-ui:success
                              com.liferay.taglib.ui.SuccessTag _jspx_th_liferay_002dui_005fsuccess_005f8 = (com.liferay.taglib.ui.SuccessTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.SuccessTag.class);
                              _jspx_th_liferay_002dui_005fsuccess_005f8.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsuccess_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f21);
                              // /html/common/themes/portlet_messages.jspf(79,0) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f8.setKey( portletName + ".doConfigure" );
                              // /html/common/themes/portlet_messages.jspf(79,0) name = message type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f8.setMessage("you-have-successfully-updated-the-setup");
                              int _jspx_eval_liferay_002dui_005fsuccess_005f8 = _jspx_th_liferay_002dui_005fsuccess_005f8.doStartTag();
                              if (_jspx_th_liferay_002dui_005fsuccess_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f8);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f8);
                              out.write('\n');
                              //  liferay-ui:success
                              com.liferay.taglib.ui.SuccessTag _jspx_th_liferay_002dui_005fsuccess_005f9 = (com.liferay.taglib.ui.SuccessTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.SuccessTag.class);
                              _jspx_th_liferay_002dui_005fsuccess_005f9.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsuccess_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f21);
                              // /html/common/themes/portlet_messages.jspf(80,0) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f9.setKey( portletName + ".doEdit" );
                              // /html/common/themes/portlet_messages.jspf(80,0) name = message type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f9.setMessage("you-have-successfully-updated-your-preferences");
                              int _jspx_eval_liferay_002dui_005fsuccess_005f9 = _jspx_th_liferay_002dui_005fsuccess_005f9.doStartTag();
                              if (_jspx_th_liferay_002dui_005fsuccess_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f9);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f9);
                              out.write('\n');
                              out.write('\n');
                              //  liferay-ui:error
                              com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f4 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                              _jspx_th_liferay_002dui_005ferror_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ferror_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f21);
                              int _jspx_eval_liferay_002dui_005ferror_005f4 = _jspx_th_liferay_002dui_005ferror_005f4.doStartTag();
                              if (_jspx_th_liferay_002dui_005ferror_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f4);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f4);
                              out.write("\n");
                              out.write("\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fif_005f21.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fif_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f21);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f21);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t");
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f30 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f30.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f30.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f38);
                              int _jspx_eval_c_005fchoose_005f30 = _jspx_th_c_005fchoose_005f30.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f30 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f42 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f42.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f42.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f30);
                              // /html/common/themes/portlet_content_wrapper.jspf(33,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f42.setTest( group.isLayoutPrototype() && layoutTypePortlet.hasPortletId(portletDisplay.getId()) );
                              int _jspx_eval_c_005fwhen_005f42 = _jspx_th_c_005fwhen_005f42.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f42 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t<div class=\"portlet-msg-info\">\n");
                              out.write("\t\t\t\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f18(_jspx_th_c_005fwhen_005f42, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t\t\t\t</div>\n");
                              out.write("\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f42.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f42.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f42);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f42);
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f22 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f22.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f30);
                              int _jspx_eval_c_005fotherwise_005f22 = _jspx_th_c_005fotherwise_005f22.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f22 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");

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
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f31 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f31.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f31.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f22);
                              int _jspx_eval_c_005fchoose_005f31 = _jspx_th_c_005fchoose_005f31.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f31 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f43 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f43.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f43.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f31);
                              // /html/common/themes/portlet_content.jspf(18,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f43.setTest( Validator.isNull(tilesPortletContent) );
                              int _jspx_eval_c_005fwhen_005f43 = _jspx_th_c_005fwhen_005f43.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f43 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");

		pageContext.getOut().print(renderRequest.getAttribute(WebKeys.PORTLET_CONTENT));
		
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f43.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f43.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f43);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f43);
                              out.write('\n');
                              out.write('	');
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f23 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f23.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f31);
                              int _jspx_eval_c_005fotherwise_005f23 = _jspx_th_c_005fotherwise_005f23.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f23 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  liferay-util:include
                              com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f14 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
                              _jspx_th_liferay_002dutil_005finclude_005f14.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dutil_005finclude_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f23);
                              // /html/common/themes/portlet_content.jspf(26,2) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dutil_005finclude_005f14.setPage( StrutsUtil.TEXT_HTML_DIR + tilesPortletContent );
                              int _jspx_eval_liferay_002dutil_005finclude_005f14 = _jspx_th_liferay_002dutil_005finclude_005f14.doStartTag();
                              if (_jspx_th_liferay_002dutil_005finclude_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f14);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f14);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f23.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fotherwise_005f23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f23);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f23);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f31.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f31.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f31);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f31);
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f22.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fotherwise_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f22);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f22);
                              out.write("\n");
                              out.write("\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f30.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f30.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f30);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f30);
                              out.write("\n");
                              out.write("\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f38.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f38.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f38);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f38);
                              out.write("\n");
                              out.write("\t\t\t");
                              if (_jspx_meth_c_005fotherwise_005f24(_jspx_th_c_005fchoose_005f26, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f26.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f26);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f26);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f35.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f35.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f35);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f35);
                              out.write('\n');
                              out.write('	');
                              if (_jspx_meth_c_005fotherwise_005f25(_jspx_th_c_005fchoose_005f25, _jspx_page_context))
                              return;
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f25.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fchoose_005f25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f25);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f25);
                          out.write('\n');
                          out.write('\n');
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f24 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f24.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dtheme_005fwrap_002dportlet_005f0);
                          // /html/common/themes/portlet_content_wrapper.jspf(53,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f24.setTest( !themeDisplay.isStateExclusive() );
                          int _jspx_eval_c_005fif_005f24 = _jspx_th_c_005fif_005f24.doStartTag();
                          if (_jspx_eval_c_005fif_005f24 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t</div>\n");
                              int evalDoAfterBody = _jspx_th_c_005fif_005f24.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fif_005f24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f24);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f24);
                          out.write("\n");
                          out.write("\t\t\t\t\t</div>\n");
                          out.write("\t\t\t\t");
                          int evalDoAfterBody = _jspx_th_liferay_002dtheme_005fwrap_002dportlet_005f0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_liferay_002dtheme_005fwrap_002dportlet_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.popBody();
                        }
                      }
                      if (_jspx_th_liferay_002dtheme_005fwrap_002dportlet_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dtheme_005fwrap_002dportlet_0026_005fpage.reuse(_jspx_th_liferay_002dtheme_005fwrap_002dportlet_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dtheme_005fwrap_002dportlet_0026_005fpage.reuse(_jspx_th_liferay_002dtheme_005fwrap_002dportlet_005f0);
                      out.write("\n");
                      out.write("\t\t\t");
                      int evalDoAfterBody = _jspx_th_c_005fwhen_005f34.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fwhen_005f34.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f34);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f34);
                  out.write("\n");
                  out.write("\t\t\t");
                  //  c:otherwise
                  org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f26 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                  _jspx_th_c_005fotherwise_005f26.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fotherwise_005f26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f24);
                  int _jspx_eval_c_005fotherwise_005f26 = _jspx_th_c_005fotherwise_005f26.doStartTag();
                  if (_jspx_eval_c_005fotherwise_005f26 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t<div class=\"portlet-borderless-container\" ");
                      out.print( containerStyles );
                      out.write(">\n");
                      out.write("\t\t\t\t\t");
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f25 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f25.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f26);
                      // /html/common/themes/portlet.jsp(153,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f25.setTest( (tilesPortletDecorateBoolean && portletDisplay.isShowConfigurationIcon()) || portletDisplay.isShowBackIcon() );
                      int _jspx_eval_c_005fif_005f25 = _jspx_th_c_005fif_005f25.doStartTag();
                      if (_jspx_eval_c_005fif_005f25 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t<div class=\"portlet-borderless-bar\">\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f26 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f26.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f25);
                          // /html/common/themes/portlet.jsp(155,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f26.setTest( tilesPortletDecorateBoolean && portletDisplay.isShowConfigurationIcon() );
                          int _jspx_eval_c_005fif_005f26 = _jspx_th_c_005fif_005f26.doStartTag();
                          if (_jspx_eval_c_005fif_005f26 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t<span class=\"portlet-title-default\">");
                              out.print( portletDisplay.getTitle() );
                              out.write("</span>\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t<span class=\"portlet-actions\">\n");
                              out.write("\t\t\t\t\t\t\t\t\t<span class=\"portlet-action portlet-options\">\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t<span class=\"portlet-action-separator\">-</span>\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
                              if (_jspx_meth_liferay_002dportlet_005ficon_002doptions_005f0(_jspx_th_c_005fif_005f26, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t</span>\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f27 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f27.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f26);
                              // /html/common/themes/portlet.jsp(165,9) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f27.setTest( portletDisplay.isShowCloseIcon() );
                              int _jspx_eval_c_005fif_005f27 = _jspx_th_c_005fif_005f27.doStartTag();
                              if (_jspx_eval_c_005fif_005f27 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t<span class=\"portlet-action portlet-close\">\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t<span class=\"portlet-action-separator\">-</span>\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t<a href=\"");
                              out.print( portletDisplay.getURLClose() );
                              out.write("\" title=\"");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f19(_jspx_th_c_005fif_005f27, _jspx_page_context))
                              return;
                              out.write('"');
                              out.write('>');
                              if (_jspx_meth_liferay_002dui_005fmessage_005f20(_jspx_th_c_005fif_005f27, _jspx_page_context))
                              return;
                              out.write("</a>\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t</span>\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fif_005f27.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fif_005f27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f27);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f27);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t</span>\n");
                              out.write("\t\t\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fif_005f26.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fif_005f26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f26);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f26);
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f28 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f28.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f25);
                          // /html/common/themes/portlet.jsp(175,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f28.setTest( portletDisplay.isShowBackIcon() );
                          int _jspx_eval_c_005fif_005f28 = _jspx_th_c_005fif_005f28.doStartTag();
                          if (_jspx_eval_c_005fif_005f28 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t<span class=\"portlet-action portlet-back\">\n");
                              out.write("\t\t\t\t\t\t\t\t\t<span class=\"portlet-action-separator\">-</span>\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t<a href=\"");
                              out.print( portletDisplay.getURLBack() );
                              out.write("\" title=\"");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f21(_jspx_th_c_005fif_005f28, _jspx_page_context))
                              return;
                              out.write('"');
                              out.write('>');
                              if (_jspx_meth_liferay_002dui_005fmessage_005f22(_jspx_th_c_005fif_005f28, _jspx_page_context))
                              return;
                              out.write("</a>\n");
                              out.write("\t\t\t\t\t\t\t\t</span>\n");
                              out.write("\t\t\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fif_005f28.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fif_005f28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f28);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f28);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t</div>\n");
                          out.write("\t\t\t\t\t");
                          int evalDoAfterBody = _jspx_th_c_005fif_005f25.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                      }
                      if (_jspx_th_c_005fif_005f25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f25);
                        return;
                      }
                      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f25);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t");

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
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f29 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f29.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f29.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f26);
                      // /html/common/themes/portlet_content_wrapper.jspf(17,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f29.setTest( !themeDisplay.isStateExclusive() );
                      int _jspx_eval_c_005fif_005f29 = _jspx_th_c_005fif_005f29.doStartTag();
                      if (_jspx_eval_c_005fif_005f29 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t<div class=\"portlet-body\">\n");
                          int evalDoAfterBody = _jspx_th_c_005fif_005f29.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                      }
                      if (_jspx_th_c_005fif_005f29.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f29);
                        return;
                      }
                      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f29);
                      out.write('\n');
                      out.write('\n');
                      //  c:choose
                      org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f32 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                      _jspx_th_c_005fchoose_005f32.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fchoose_005f32.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f26);
                      int _jspx_eval_c_005fchoose_005f32 = _jspx_th_c_005fchoose_005f32.doStartTag();
                      if (_jspx_eval_c_005fchoose_005f32 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write('\n');
                          out.write('	');
                          //  c:when
                          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f44 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                          _jspx_th_c_005fwhen_005f44.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fwhen_005f44.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f32);
                          // /html/common/themes/portlet_content_wrapper.jspf(22,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fwhen_005f44.setTest( portletDisplay.isActive() );
                          int _jspx_eval_c_005fwhen_005f44 = _jspx_th_c_005fwhen_005f44.doStartTag();
                          if (_jspx_eval_c_005fwhen_005f44 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f33 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f33.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f33.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f44);
                              int _jspx_eval_c_005fchoose_005f33 = _jspx_th_c_005fchoose_005f33.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f33 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f45 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f45.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f45.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f33);
                              // /html/common/themes/portlet_content_wrapper.jspf(24,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f45.setTest( themeDisplay.isStateExclusive() );
                              int _jspx_eval_c_005fwhen_005f45 = _jspx_th_c_005fwhen_005f45.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f45 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t");

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
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f34 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f34.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f34.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f45);
                              int _jspx_eval_c_005fchoose_005f34 = _jspx_th_c_005fchoose_005f34.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f34 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f46 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f46.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f46.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f34);
                              // /html/common/themes/portlet_content.jspf(18,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f46.setTest( Validator.isNull(tilesPortletContent) );
                              int _jspx_eval_c_005fwhen_005f46 = _jspx_th_c_005fwhen_005f46.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f46 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");

		pageContext.getOut().print(renderRequest.getAttribute(WebKeys.PORTLET_CONTENT));
		
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f46.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f46.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f46);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f46);
                              out.write('\n');
                              out.write('	');
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f27 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f27.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f34);
                              int _jspx_eval_c_005fotherwise_005f27 = _jspx_th_c_005fotherwise_005f27.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f27 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  liferay-util:include
                              com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f17 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
                              _jspx_th_liferay_002dutil_005finclude_005f17.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dutil_005finclude_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f27);
                              // /html/common/themes/portlet_content.jspf(26,2) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dutil_005finclude_005f17.setPage( StrutsUtil.TEXT_HTML_DIR + tilesPortletContent );
                              int _jspx_eval_liferay_002dutil_005finclude_005f17 = _jspx_th_liferay_002dutil_005finclude_005f17.doStartTag();
                              if (_jspx_th_liferay_002dutil_005finclude_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f17);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f17);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f27.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fotherwise_005f27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f27);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f27);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f34.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f34.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f34);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f34);
                              out.write("\n");
                              out.write("\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f45.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f45.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f45);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f45);
                              out.write("\n");
                              out.write("\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f47 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f47.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f47.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f33);
                              // /html/common/themes/portlet_content_wrapper.jspf(27,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f47.setTest( portletDisplay.isAccess() );
                              int _jspx_eval_c_005fwhen_005f47 = _jspx_th_c_005fwhen_005f47.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f47 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f30 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f30.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f30.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f47);
                              // /html/common/themes/portlet_content_wrapper.jspf(28,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f30.setTest( !tilesPortletContent.endsWith("/error.jsp") );
                              int _jspx_eval_c_005fif_005f30 = _jspx_th_c_005fif_005f30.doStartTag();
                              if (_jspx_eval_c_005fif_005f30 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t");

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

Group liveGroup = group;

boolean inStaging = false;

if (group.isControlPanel()) {
	long doAsGroupId = ParamUtil.getLong(request, "doAsGroupId");

	if (doAsGroupId > 0) {
		try {
			liveGroup = GroupLocalServiceUtil.getGroup(doAsGroupId);

			if (liveGroup.isStagingGroup()) {
				liveGroup = liveGroup.getLiveGroup();

				inStaging = true;
			}
		}
		catch (Exception e) {
		}
	}
}
else if (group.isStagingGroup()) {
	liveGroup = group.getLiveGroup();

	inStaging = true;
}

                              out.write('\n');
                              out.write('\n');
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f31 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f31.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f31.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f30);
                              // /html/common/themes/portlet_messages.jspf(46,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f31.setTest( liveGroup.isStaged() && !liveGroup.isStagedPortlet(portlet.getRootPortletId()) );
                              int _jspx_eval_c_005fif_005f31 = _jspx_th_c_005fif_005f31.doStartTag();
                              if (_jspx_eval_c_005fif_005f31 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f35 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f35.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f35.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f31);
                              int _jspx_eval_c_005fchoose_005f35 = _jspx_th_c_005fchoose_005f35.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f35 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f48 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f48.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f48.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f35);
                              // /html/common/themes/portlet_messages.jspf(48,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f48.setTest( !liveGroup.isStagedRemotely() && inStaging );
                              int _jspx_eval_c_005fwhen_005f48 = _jspx_th_c_005fwhen_005f48.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f48 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t<div class=\"portlet-msg-alert\">\n");
                              out.write("\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f23(_jspx_th_c_005fwhen_005f48, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t</div>\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f48.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f48.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f48);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f48);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f49 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f49.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f49.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f35);
                              // /html/common/themes/portlet_messages.jspf(53,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f49.setTest( liveGroup.isStagedRemotely() && themeDisplay.isSignedIn() );
                              int _jspx_eval_c_005fwhen_005f49 = _jspx_th_c_005fwhen_005f49.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f49 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t<div class=\"portlet-msg-alert\">\n");
                              out.write("\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f24(_jspx_th_c_005fwhen_005f49, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t</div>\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f49.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f49.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f49);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f49);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f35.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f35.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f35);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f35);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fif_005f31.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fif_005f31.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f31);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f31);
                              out.write('\n');
                              out.write('\n');
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f32 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f32.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f32.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f30);
                              // /html/common/themes/portlet_messages.jspf(61,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f32.setTest( SessionMessages.contains(renderRequest, "request_processed") );
                              int _jspx_eval_c_005fif_005f32 = _jspx_th_c_005fif_005f32.doStartTag();
                              if (_jspx_eval_c_005fif_005f32 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t<div class=\"portlet-msg-success\">\n");
                              out.write("\n");
                              out.write("\t\t");

		String successMessage = (String)SessionMessages.get(renderRequest, "request_processed");
		
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f36 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f36.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f36.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f32);
                              int _jspx_eval_c_005fchoose_005f36 = _jspx_th_c_005fchoose_005f36.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f36 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f50 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f50.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f50.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f36);
                              // /html/common/themes/portlet_messages.jspf(69,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f50.setTest( Validator.isNotNull(successMessage) && !successMessage.equals("request_processed") );
                              int _jspx_eval_c_005fwhen_005f50 = _jspx_th_c_005fwhen_005f50.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f50 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t");
                              out.print( successMessage );
                              out.write("\n");
                              out.write("\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f50.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f50.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f50);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f50);
                              out.write("\n");
                              out.write("\t\t\t");
                              if (_jspx_meth_c_005fotherwise_005f28(_jspx_th_c_005fchoose_005f36, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f36.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f36.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f36);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f36);
                              out.write("\n");
                              out.write("\t</div>\n");
                              int evalDoAfterBody = _jspx_th_c_005fif_005f32.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fif_005f32.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f32);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f32);
                              out.write('\n');
                              out.write('\n');
                              //  liferay-ui:success
                              com.liferay.taglib.ui.SuccessTag _jspx_th_liferay_002dui_005fsuccess_005f10 = (com.liferay.taglib.ui.SuccessTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.SuccessTag.class);
                              _jspx_th_liferay_002dui_005fsuccess_005f10.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsuccess_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f30);
                              // /html/common/themes/portlet_messages.jspf(79,0) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f10.setKey( portletName + ".doConfigure" );
                              // /html/common/themes/portlet_messages.jspf(79,0) name = message type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f10.setMessage("you-have-successfully-updated-the-setup");
                              int _jspx_eval_liferay_002dui_005fsuccess_005f10 = _jspx_th_liferay_002dui_005fsuccess_005f10.doStartTag();
                              if (_jspx_th_liferay_002dui_005fsuccess_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f10);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f10);
                              out.write('\n');
                              //  liferay-ui:success
                              com.liferay.taglib.ui.SuccessTag _jspx_th_liferay_002dui_005fsuccess_005f11 = (com.liferay.taglib.ui.SuccessTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.get(com.liferay.taglib.ui.SuccessTag.class);
                              _jspx_th_liferay_002dui_005fsuccess_005f11.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsuccess_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f30);
                              // /html/common/themes/portlet_messages.jspf(80,0) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f11.setKey( portletName + ".doEdit" );
                              // /html/common/themes/portlet_messages.jspf(80,0) name = message type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsuccess_005f11.setMessage("you-have-successfully-updated-your-preferences");
                              int _jspx_eval_liferay_002dui_005fsuccess_005f11 = _jspx_th_liferay_002dui_005fsuccess_005f11.doStartTag();
                              if (_jspx_th_liferay_002dui_005fsuccess_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f11);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsuccess_0026_005fmessage_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fsuccess_005f11);
                              out.write('\n');
                              out.write('\n');
                              //  liferay-ui:error
                              com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f5 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                              _jspx_th_liferay_002dui_005ferror_005f5.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ferror_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f30);
                              int _jspx_eval_liferay_002dui_005ferror_005f5 = _jspx_th_liferay_002dui_005ferror_005f5.doStartTag();
                              if (_jspx_th_liferay_002dui_005ferror_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f5);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ferror_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f5);
                              out.write("\n");
                              out.write("\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fif_005f30.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fif_005f30.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f30);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f30);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t");
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f37 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f37.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f37.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f47);
                              int _jspx_eval_c_005fchoose_005f37 = _jspx_th_c_005fchoose_005f37.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f37 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f51 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f51.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f51.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f37);
                              // /html/common/themes/portlet_content_wrapper.jspf(33,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f51.setTest( group.isLayoutPrototype() && layoutTypePortlet.hasPortletId(portletDisplay.getId()) );
                              int _jspx_eval_c_005fwhen_005f51 = _jspx_th_c_005fwhen_005f51.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f51 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t<div class=\"portlet-msg-info\">\n");
                              out.write("\t\t\t\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f26(_jspx_th_c_005fwhen_005f51, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t\t\t\t</div>\n");
                              out.write("\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f51.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f51.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f51);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f51);
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f29 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f29.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f29.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f37);
                              int _jspx_eval_c_005fotherwise_005f29 = _jspx_th_c_005fotherwise_005f29.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f29 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");

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
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f38 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f38.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f38.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f29);
                              int _jspx_eval_c_005fchoose_005f38 = _jspx_th_c_005fchoose_005f38.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f38 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f52 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f52.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f52.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f38);
                              // /html/common/themes/portlet_content.jspf(18,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f52.setTest( Validator.isNull(tilesPortletContent) );
                              int _jspx_eval_c_005fwhen_005f52 = _jspx_th_c_005fwhen_005f52.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f52 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");

		pageContext.getOut().print(renderRequest.getAttribute(WebKeys.PORTLET_CONTENT));
		
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f52.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f52.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f52);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f52);
                              out.write('\n');
                              out.write('	');
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f30 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f30.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f30.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f38);
                              int _jspx_eval_c_005fotherwise_005f30 = _jspx_th_c_005fotherwise_005f30.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f30 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  liferay-util:include
                              com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f18 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
                              _jspx_th_liferay_002dutil_005finclude_005f18.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dutil_005finclude_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f30);
                              // /html/common/themes/portlet_content.jspf(26,2) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dutil_005finclude_005f18.setPage( StrutsUtil.TEXT_HTML_DIR + tilesPortletContent );
                              int _jspx_eval_liferay_002dutil_005finclude_005f18 = _jspx_th_liferay_002dutil_005finclude_005f18.doStartTag();
                              if (_jspx_th_liferay_002dutil_005finclude_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f18);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f18);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f30.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fotherwise_005f30.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f30);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f30);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f38.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f38.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f38);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f38);
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f29.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fotherwise_005f29.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f29);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f29);
                              out.write("\n");
                              out.write("\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f37.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f37.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f37);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f37);
                              out.write("\n");
                              out.write("\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f47.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fwhen_005f47.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f47);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f47);
                              out.write("\n");
                              out.write("\t\t\t");
                              if (_jspx_meth_c_005fotherwise_005f31(_jspx_th_c_005fchoose_005f33, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fchoose_005f33.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fchoose_005f33.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f33);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f33);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_c_005fwhen_005f44.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fwhen_005f44.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f44);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f44);
                          out.write('\n');
                          out.write('	');
                          if (_jspx_meth_c_005fotherwise_005f32(_jspx_th_c_005fchoose_005f32, _jspx_page_context))
                            return;
                          out.write('\n');
                          int evalDoAfterBody = _jspx_th_c_005fchoose_005f32.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                      }
                      if (_jspx_th_c_005fchoose_005f32.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f32);
                        return;
                      }
                      _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f32);
                      out.write('\n');
                      out.write('\n');
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f33 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f33.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f33.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f26);
                      // /html/common/themes/portlet_content_wrapper.jspf(53,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f33.setTest( !themeDisplay.isStateExclusive() );
                      int _jspx_eval_c_005fif_005f33 = _jspx_th_c_005fif_005f33.doStartTag();
                      if (_jspx_eval_c_005fif_005f33 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t</div>\n");
                          int evalDoAfterBody = _jspx_th_c_005fif_005f33.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                      }
                      if (_jspx_th_c_005fif_005f33.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f33);
                        return;
                      }
                      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f33);
                      out.write("\n");
                      out.write("\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f34 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f34.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f34.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f26);
                      // /html/common/themes/portlet.jsp(188,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f34.setTest( freeformPortlet );
                      int _jspx_eval_c_005fif_005f34 = _jspx_th_c_005fif_005f34.doStartTag();
                      if (_jspx_eval_c_005fif_005f34 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t<div class=\"portlet-resize-container\">\n");
                          out.write("\t\t\t\t\t\t<div class=\"portlet-resize-handle\"></div>\n");
                          out.write("\t\t\t\t\t</div>\n");
                          out.write("\t\t\t\t");
                          int evalDoAfterBody = _jspx_th_c_005fif_005f34.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                      }
                      if (_jspx_th_c_005fif_005f34.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f34);
                        return;
                      }
                      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f34);
                      out.write("\n");
                      out.write("\t\t\t");
                      int evalDoAfterBody = _jspx_th_c_005fotherwise_005f26.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fotherwise_005f26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f26);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f26);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_c_005fchoose_005f24.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fchoose_005f24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f24);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f24);
              out.write('\n');
              out.write('	');
              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f19.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fotherwise_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f19);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f19);
          out.write('\n');
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

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
    // /html/common/themes/portlet_messages.jspf(50,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("this-portlet-is-not-staged-local-alert");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f1 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f6);
    // /html/common/themes/portlet_messages.jspf(55,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f1.setKey("this-portlet-is-not-staged-remote-alert");
    int _jspx_eval_liferay_002dui_005fmessage_005f1 = _jspx_th_liferay_002dui_005fmessage_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f1 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f1.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f5);
    int _jspx_eval_c_005fotherwise_005f1 = _jspx_th_c_005fotherwise_005f1.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_liferay_002dui_005fmessage_005f2(_jspx_th_c_005fotherwise_005f1, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f1);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f2 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f2.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
    // /html/common/themes/portlet_messages.jspf(73,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f2.setKey("your-request-processed-successfully");
    int _jspx_eval_liferay_002dui_005fmessage_005f2 = _jspx_th_liferay_002dui_005fmessage_005f2.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f8, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f3 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f3.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f8);
    // /html/common/themes/portlet_content_wrapper.jspf(35,7) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f3.setKey("configure-this-application-and-place-it-where-desired-on-the-page");
    int _jspx_eval_liferay_002dui_005fmessage_005f3 = _jspx_th_liferay_002dui_005fmessage_005f3.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f4 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f4.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
    int _jspx_eval_c_005fotherwise_005f4 = _jspx_th_c_005fotherwise_005f4.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_liferay_002dutil_005finclude_005f2(_jspx_th_c_005fotherwise_005f4, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f4);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f2 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f2.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f4);
    // /html/common/themes/portlet_content_wrapper.jspf(44,4) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f2.setPage("/html/portal/portlet_access_denied.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f2 = _jspx_th_liferay_002dutil_005finclude_005f2.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f2);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f5 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f5.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
    int _jspx_eval_c_005fotherwise_005f5 = _jspx_th_c_005fotherwise_005f5.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write('\n');
        out.write('	');
        out.write('	');
        if (_jspx_meth_liferay_002dutil_005finclude_005f3(_jspx_th_c_005fotherwise_005f5, _jspx_page_context))
          return true;
        out.write('\n');
        out.write('	');
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f5.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f5);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f3 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f3.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f5);
    // /html/common/themes/portlet_content_wrapper.jspf(49,2) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f3.setPage("/html/portal/portlet_inactive.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f3 = _jspx_th_liferay_002dutil_005finclude_005f3.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f3);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f15, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f4 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f4.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f15);
    // /html/common/themes/portlet_messages.jspf(50,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f4.setKey("this-portlet-is-not-staged-local-alert");
    int _jspx_eval_liferay_002dui_005fmessage_005f4 = _jspx_th_liferay_002dui_005fmessage_005f4.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f16, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f5 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f5.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f16);
    // /html/common/themes/portlet_messages.jspf(55,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f5.setKey("this-portlet-is-not-staged-remote-alert");
    int _jspx_eval_liferay_002dui_005fmessage_005f5 = _jspx_th_liferay_002dui_005fmessage_005f5.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f12, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f7 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f7.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f12);
    int _jspx_eval_c_005fotherwise_005f7 = _jspx_th_c_005fotherwise_005f7.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_liferay_002dui_005fmessage_005f6(_jspx_th_c_005fotherwise_005f7, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f7.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f7);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f6 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f6.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f7);
    // /html/common/themes/portlet_messages.jspf(73,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f6.setKey("your-request-processed-successfully");
    int _jspx_eval_liferay_002dui_005fmessage_005f6 = _jspx_th_liferay_002dui_005fmessage_005f6.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f18, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f7 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f7.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f18);
    // /html/common/themes/portlet_content_wrapper.jspf(35,7) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f7.setKey("configure-this-application-and-place-it-where-desired-on-the-page");
    int _jspx_eval_liferay_002dui_005fmessage_005f7 = _jspx_th_liferay_002dui_005fmessage_005f7.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f9, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f10 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f10.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f9);
    int _jspx_eval_c_005fotherwise_005f10 = _jspx_th_c_005fotherwise_005f10.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_liferay_002dutil_005finclude_005f6(_jspx_th_c_005fotherwise_005f10, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f10.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f10);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f10, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f6 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f6.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f10);
    // /html/common/themes/portlet_content_wrapper.jspf(44,4) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f6.setPage("/html/portal/portlet_access_denied.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f6 = _jspx_th_liferay_002dutil_005finclude_005f6.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f6);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f8, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f11 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f11.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f8);
    int _jspx_eval_c_005fotherwise_005f11 = _jspx_th_c_005fotherwise_005f11.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write('\n');
        out.write('	');
        out.write('	');
        if (_jspx_meth_liferay_002dutil_005finclude_005f7(_jspx_th_c_005fotherwise_005f11, _jspx_page_context))
          return true;
        out.write('\n');
        out.write('	');
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f11.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f11);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f11, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f7 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f7.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f11);
    // /html/common/themes/portlet_content_wrapper.jspf(49,2) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f7.setPage("/html/portal/portlet_inactive.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f7 = _jspx_th_liferay_002dutil_005finclude_005f7.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f7);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f25, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f8 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f8.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f25);
    // /html/common/themes/portlet_messages.jspf(50,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f8.setKey("this-portlet-is-not-staged-local-alert");
    int _jspx_eval_liferay_002dui_005fmessage_005f8 = _jspx_th_liferay_002dui_005fmessage_005f8.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f8);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f26, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f9 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f9.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f26);
    // /html/common/themes/portlet_messages.jspf(55,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f9.setKey("this-portlet-is-not-staged-remote-alert");
    int _jspx_eval_liferay_002dui_005fmessage_005f9 = _jspx_th_liferay_002dui_005fmessage_005f9.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f9);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f19, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f13 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f13.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f19);
    int _jspx_eval_c_005fotherwise_005f13 = _jspx_th_c_005fotherwise_005f13.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_liferay_002dui_005fmessage_005f10(_jspx_th_c_005fotherwise_005f13, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f13.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f13);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f13, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f10 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f10.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f13);
    // /html/common/themes/portlet_messages.jspf(73,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f10.setKey("your-request-processed-successfully");
    int _jspx_eval_liferay_002dui_005fmessage_005f10 = _jspx_th_liferay_002dui_005fmessage_005f10.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f10);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f28, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f11 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f11.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f28);
    // /html/common/themes/portlet_content_wrapper.jspf(35,7) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f11.setKey("configure-this-application-and-place-it-where-desired-on-the-page");
    int _jspx_eval_liferay_002dui_005fmessage_005f11 = _jspx_th_liferay_002dui_005fmessage_005f11.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f11);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f16(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f16, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f16 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f16.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f16);
    int _jspx_eval_c_005fotherwise_005f16 = _jspx_th_c_005fotherwise_005f16.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f16 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_liferay_002dutil_005finclude_005f10(_jspx_th_c_005fotherwise_005f16, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f16.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f16);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f16);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f16, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f10 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f10.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f16);
    // /html/common/themes/portlet_content_wrapper.jspf(44,4) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f10.setPage("/html/portal/portlet_access_denied.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f10 = _jspx_th_liferay_002dutil_005finclude_005f10.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f10);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f17(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f15, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f17 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f17.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f15);
    int _jspx_eval_c_005fotherwise_005f17 = _jspx_th_c_005fotherwise_005f17.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f17 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write('\n');
        out.write('	');
        out.write('	');
        if (_jspx_meth_liferay_002dutil_005finclude_005f11(_jspx_th_c_005fotherwise_005f17, _jspx_page_context))
          return true;
        out.write('\n');
        out.write('	');
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f17.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f17);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f17);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f17, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f11 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f11.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f17);
    // /html/common/themes/portlet_content_wrapper.jspf(49,2) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f11.setPage("/html/portal/portlet_inactive.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f11 = _jspx_th_liferay_002dutil_005finclude_005f11.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f11);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f31, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f12 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f12.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f31);
    // /html/common/themes/portlet_messages.jspf(50,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f12.setKey("this-portlet-is-not-staged-local-alert");
    int _jspx_eval_liferay_002dui_005fmessage_005f12 = _jspx_th_liferay_002dui_005fmessage_005f12.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f12);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f32, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f13 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f13.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f32);
    // /html/common/themes/portlet_messages.jspf(55,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f13.setKey("this-portlet-is-not-staged-remote-alert");
    int _jspx_eval_liferay_002dui_005fmessage_005f13 = _jspx_th_liferay_002dui_005fmessage_005f13.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f13);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f18(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f23, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f18 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f18.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f23);
    int _jspx_eval_c_005fotherwise_005f18 = _jspx_th_c_005fotherwise_005f18.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f18 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_liferay_002dui_005fmessage_005f14(_jspx_th_c_005fotherwise_005f18, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f18.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f18);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f18);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f18, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f14 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f14.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f18);
    // /html/common/themes/portlet_messages.jspf(73,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f14.setKey("your-request-processed-successfully");
    int _jspx_eval_liferay_002dui_005fmessage_005f14 = _jspx_th_liferay_002dui_005fmessage_005f14.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f14);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f39, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f15 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f15.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f39);
    // /html/common/themes/portlet_messages.jspf(50,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f15.setKey("this-portlet-is-not-staged-local-alert");
    int _jspx_eval_liferay_002dui_005fmessage_005f15 = _jspx_th_liferay_002dui_005fmessage_005f15.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f15);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f16(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f40, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f16 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f16.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f40);
    // /html/common/themes/portlet_messages.jspf(55,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f16.setKey("this-portlet-is-not-staged-remote-alert");
    int _jspx_eval_liferay_002dui_005fmessage_005f16 = _jspx_th_liferay_002dui_005fmessage_005f16.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f16);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f16);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f21(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f29, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f21 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f21.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f29);
    int _jspx_eval_c_005fotherwise_005f21 = _jspx_th_c_005fotherwise_005f21.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f21 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_liferay_002dui_005fmessage_005f17(_jspx_th_c_005fotherwise_005f21, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f21.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f21);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f21);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f17(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f21, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f17 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f17.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f21);
    // /html/common/themes/portlet_messages.jspf(73,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f17.setKey("your-request-processed-successfully");
    int _jspx_eval_liferay_002dui_005fmessage_005f17 = _jspx_th_liferay_002dui_005fmessage_005f17.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f17);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f17);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f18(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f42, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f18 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f18.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f42);
    // /html/common/themes/portlet_content_wrapper.jspf(35,7) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f18.setKey("configure-this-application-and-place-it-where-desired-on-the-page");
    int _jspx_eval_liferay_002dui_005fmessage_005f18 = _jspx_th_liferay_002dui_005fmessage_005f18.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f18);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f18);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f24(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f26, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f24 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f24.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f26);
    int _jspx_eval_c_005fotherwise_005f24 = _jspx_th_c_005fotherwise_005f24.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f24 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_liferay_002dutil_005finclude_005f15(_jspx_th_c_005fotherwise_005f24, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f24.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f24);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f24);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f24, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f15 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f15.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f24);
    // /html/common/themes/portlet_content_wrapper.jspf(44,4) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f15.setPage("/html/portal/portlet_access_denied.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f15 = _jspx_th_liferay_002dutil_005finclude_005f15.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f15);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f25(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f25, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f25 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f25.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f25);
    int _jspx_eval_c_005fotherwise_005f25 = _jspx_th_c_005fotherwise_005f25.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f25 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write('\n');
        out.write('	');
        out.write('	');
        if (_jspx_meth_liferay_002dutil_005finclude_005f16(_jspx_th_c_005fotherwise_005f25, _jspx_page_context))
          return true;
        out.write('\n');
        out.write('	');
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f25.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f25);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f25);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f16(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f25, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f16 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f16.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f25);
    // /html/common/themes/portlet_content_wrapper.jspf(49,2) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f16.setPage("/html/portal/portlet_inactive.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f16 = _jspx_th_liferay_002dutil_005finclude_005f16.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f16);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f16);
    return false;
  }

  private boolean _jspx_meth_liferay_002dportlet_005ficon_002doptions_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f26, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-portlet:icon-options
    com.liferay.taglib.portletext.IconOptionsTag _jspx_th_liferay_002dportlet_005ficon_002doptions_005f0 = (com.liferay.taglib.portletext.IconOptionsTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002doptions_005fnobody.get(com.liferay.taglib.portletext.IconOptionsTag.class);
    _jspx_th_liferay_002dportlet_005ficon_002doptions_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dportlet_005ficon_002doptions_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f26);
    int _jspx_eval_liferay_002dportlet_005ficon_002doptions_005f0 = _jspx_th_liferay_002dportlet_005ficon_002doptions_005f0.doStartTag();
    if (_jspx_th_liferay_002dportlet_005ficon_002doptions_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002doptions_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002doptions_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dportlet_005ficon_002doptions_005fnobody.reuse(_jspx_th_liferay_002dportlet_005ficon_002doptions_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f19(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f27, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f19 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f19.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f27);
    // /html/common/themes/portlet.jsp(169,64) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f19.setKey("close");
    int _jspx_eval_liferay_002dui_005fmessage_005f19 = _jspx_th_liferay_002dui_005fmessage_005f19.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f19);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f19);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f20(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f27, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f20 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f20.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f27);
    // /html/common/themes/portlet.jsp(169,100) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f20.setKey("close");
    int _jspx_eval_liferay_002dui_005fmessage_005f20 = _jspx_th_liferay_002dui_005fmessage_005f20.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f20);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f20);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f21(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f28, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f21 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f21.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f28);
    // /html/common/themes/portlet.jsp(179,61) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f21.setKey("back");
    int _jspx_eval_liferay_002dui_005fmessage_005f21 = _jspx_th_liferay_002dui_005fmessage_005f21.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f21);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f21);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f22(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f28, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f22 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f22.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f28);
    // /html/common/themes/portlet.jsp(179,96) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f22.setKey("back");
    int _jspx_eval_liferay_002dui_005fmessage_005f22 = _jspx_th_liferay_002dui_005fmessage_005f22.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f22);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f22);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f23(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f48, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f23 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f23.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f48);
    // /html/common/themes/portlet_messages.jspf(50,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f23.setKey("this-portlet-is-not-staged-local-alert");
    int _jspx_eval_liferay_002dui_005fmessage_005f23 = _jspx_th_liferay_002dui_005fmessage_005f23.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f23);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f23);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f24(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f49, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f24 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f24.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f49);
    // /html/common/themes/portlet_messages.jspf(55,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f24.setKey("this-portlet-is-not-staged-remote-alert");
    int _jspx_eval_liferay_002dui_005fmessage_005f24 = _jspx_th_liferay_002dui_005fmessage_005f24.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f24);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f24);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f28(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f36, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f28 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f28.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f36);
    int _jspx_eval_c_005fotherwise_005f28 = _jspx_th_c_005fotherwise_005f28.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f28 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_liferay_002dui_005fmessage_005f25(_jspx_th_c_005fotherwise_005f28, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f28.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f28);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f28);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f25(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f28, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f25 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f25.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f28);
    // /html/common/themes/portlet_messages.jspf(73,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f25.setKey("your-request-processed-successfully");
    int _jspx_eval_liferay_002dui_005fmessage_005f25 = _jspx_th_liferay_002dui_005fmessage_005f25.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f25);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f25);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f26(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f51, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f26 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f26.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f51);
    // /html/common/themes/portlet_content_wrapper.jspf(35,7) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f26.setKey("configure-this-application-and-place-it-where-desired-on-the-page");
    int _jspx_eval_liferay_002dui_005fmessage_005f26 = _jspx_th_liferay_002dui_005fmessage_005f26.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f26);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f26);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f31(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f33, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f31 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f31.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f31.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f33);
    int _jspx_eval_c_005fotherwise_005f31 = _jspx_th_c_005fotherwise_005f31.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f31 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_liferay_002dutil_005finclude_005f19(_jspx_th_c_005fotherwise_005f31, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f31.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f31.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f31);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f31);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f19(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f31, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f19 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f19.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f31);
    // /html/common/themes/portlet_content_wrapper.jspf(44,4) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f19.setPage("/html/portal/portlet_access_denied.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f19 = _jspx_th_liferay_002dutil_005finclude_005f19.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f19);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f19);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f32(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f32, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f32 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f32.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f32.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f32);
    int _jspx_eval_c_005fotherwise_005f32 = _jspx_th_c_005fotherwise_005f32.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f32 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write('\n');
        out.write('	');
        out.write('	');
        if (_jspx_meth_liferay_002dutil_005finclude_005f20(_jspx_th_c_005fotherwise_005f32, _jspx_page_context))
          return true;
        out.write('\n');
        out.write('	');
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f32.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f32.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f32);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f32);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f20(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f32, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f20 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f20.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f32);
    // /html/common/themes/portlet_content_wrapper.jspf(49,2) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f20.setPage("/html/portal/portlet_inactive.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f20 = _jspx_th_liferay_002dutil_005finclude_005f20.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f20);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f20);
    return false;
  }
}
