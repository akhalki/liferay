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
import com.liferay.portal.monitoring.statistics.portal.PortalRequestDataSample;

public final class top_005fhead_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


private static final String _BOTTOM_KEY = "bottom";

private static final String _LEFT_KEY = "left";

private static final String _RIGHT_KEY = "right";

private static final String _SAME_FOR_ALL_KEY = "sameForAll";

private static final String _TOP_KEY = "top";

private static final String _UNIT_KEY = "unit";

private static final String _VALUE_KEY = "value";

private static final Set _unitSet = new HashSet();

static {
	_unitSet.add("px");
	_unitSet.add("em");
	_unitSet.add("%");
}


private static Log _log = LogFactoryUtil.getLog("portal-web.docroot.html.common.themes.top_head.jsp");

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(30);
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/common/themes/top_monitoring.jspf");
    _jspx_dependants.add("/html/common/themes/top_meta.jspf");
    _jspx_dependants.add("/html/common/themes/top_meta-ext.jsp");
    _jspx_dependants.add("/html/common/themes/top_js.jspf");
    _jspx_dependants.add("/html/common/themes/top_js-ext.jspf");
    _jspx_dependants.add("/html/common/themes/portlet_css.jspf");
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
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dtheme_005fmeta_002dtags_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fmeta_002dtags_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fmeta_002dtags_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
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
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f0.setParent(null);
      // /html/common/themes/top_head.jsp(19,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f0.setTest( PropsValues.MONITORING_PORTAL_REQUEST );
      int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
      if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('\n');
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
          out.write("\n");
          out.write("\n");

PortalRequestDataSample portalRequestDataSample = new PortalRequestDataSample(company.getCompanyId(), request.getRemoteUser(), request.getRequestURI(), request.getRequestURL().toString() + ".jsp_display");

portalRequestDataSample.setDescription("Portal Request");

portalRequestDataSample.prepare();

request.setAttribute(WebKeys.PORTAL_REQUEST_DATA_SAMPLE, portalRequestDataSample);

          out.write('\n');
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
      out.write("<meta content=\"");
      out.print( ContentTypes.TEXT_HTML_UTF8 );
      out.write("\" http-equiv=\"content-type\" />\n");
      out.write("\n");

String refreshRate = request.getParameter("refresh_rate");

      out.write('\n');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f1.setParent(null);
      // /html/common/themes/top_meta.jspf(23,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f1.setTest( (refreshRate != null) && (!refreshRate.equals("0")) );
      int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
      if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\n");
          out.write("\t<meta content=\"");
          out.print( refreshRate );
          out.write(";\" http-equiv=\"Refresh\" />\n");
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
      out.write('\n');

String cacheControl = request.getParameter("cache_control");

      out.write('\n');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f2.setParent(null);
      // /html/common/themes/top_meta.jspf(31,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f2.setTest( (cacheControl != null) && (cacheControl.equals("0")) );
      int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
      if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\n");
          out.write("\t<meta content=\"no-cache\" http-equiv=\"Cache-Control\" />\n");
          out.write("\t<meta content=\"no-cache\" http-equiv=\"Pragma\" />\n");
          out.write("\t<meta content=\"0\" http-equiv=\"Expires\" />\n");
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
      if (_jspx_meth_liferay_002dtheme_005fmeta_002dtags_005f0(_jspx_page_context))
        return;
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
      out.write("<link rel=\"Shortcut Icon\" href=\"");
      out.print( themeDisplay.getPathThemeImages() );
      out.write('/');
      out.print( PropsValues.THEME_SHORTCUT_ICON );
      out.write("\" />\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<link href=\"");
      out.print( HtmlUtil.escape(PortalUtil.getStaticResourceURL(request, themeDisplay.getCDNHost() + themeDisplay.getPathContext() + "/html/portal/css.jsp")) );
      out.write("\" rel=\"stylesheet\" type=\"text/css\" />\n");
      out.write("\n");

List<Portlet> portlets = null;

if (layout != null) {
	String ppid = ParamUtil.getString(request, "p_p_id");

	if (ppid.equals(PortletKeys.PORTLET_CONFIGURATION)) {
		portlets = new ArrayList<Portlet>();

		portlets.add(PortletLocalServiceUtil.getPortletById(company.getCompanyId(), PortletKeys.PORTLET_CONFIGURATION));

		ppid = ParamUtil.getString(request, PortalUtil.getPortletNamespace(ppid) + "portletResource");

		if (Validator.isNotNull(ppid)) {
			portlets.add(PortletLocalServiceUtil.getPortletById(company.getCompanyId(), ppid));
		}
	}
	else if (layout.isTypePortlet()) {
		portlets = layoutTypePortlet.getAllPortlets();

		if (themeDisplay.isStateMaximized() || themeDisplay.isStatePopUp()) {
			if (Validator.isNotNull(ppid)) {
				Portlet portlet = PortletLocalServiceUtil.getPortletById(company.getCompanyId(), ppid);

				if (!portlets.contains(portlet)) {
					portlets.add(portlet);
				}
			}
		}
	}
	else if ((layout.isTypeControlPanel() || layout.isTypePanel()) && Validator.isNotNull(ppid)) {
		portlets = new ArrayList<Portlet>();

		portlets.add(PortletLocalServiceUtil.getPortletById(company.getCompanyId(), ppid));
	}

	request.setAttribute(WebKeys.LAYOUT_PORTLETS, portlets);
}

      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f3.setParent(null);
      // /html/common/themes/top_head.jsp(74,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f3.setTest( portlets != null );
      int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
      if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('\n');
          out.write('\n');
          out.write('	');

	Set<String> headerPortalCssSet = new LinkedHashSet<String>();

	for (Portlet portlet : portlets) {
		for (String headerPortalCss : portlet.getHeaderPortalCss()) {
			if (!HttpUtil.hasProtocol(headerPortalCss)) {
				headerPortalCss = PortalUtil.getStaticResourceURL(request, request.getContextPath() + headerPortalCss, portlet.getTimestamp());
			}

			if (!headerPortalCssSet.contains(headerPortalCss)) {
				headerPortalCssSet.add(headerPortalCss);
	
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t<link href=\"");
          out.print( HtmlUtil.escape(headerPortalCss) );
          out.write("\" rel=\"stylesheet\" type=\"text/css\" />\n");
          out.write("\n");
          out.write("\t");

			}
		}
	}

	Set<String> headerPortletCssSet = new LinkedHashSet<String>();

	for (Portlet portlet : portlets) {
		for (String headerPortletCss : portlet.getHeaderPortletCss()) {
			if (!HttpUtil.hasProtocol(headerPortletCss)) {
				headerPortletCss = PortalUtil.getStaticResourceURL(request, portlet.getContextPath() + headerPortletCss, portlet.getTimestamp());
			}

			if (!headerPortletCssSet.contains(headerPortletCss)) {
				headerPortletCssSet.add(headerPortletCss);
	
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t<link href=\"");
          out.print( HtmlUtil.escape(headerPortletCss) );
          out.write("\" rel=\"stylesheet\" type=\"text/css\" />\n");
          out.write("\n");
          out.write("\t");

			}
		}
	}
	
          out.write('\n');
          out.write('\n');
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
      out.write("<script type=\"text/javascript\">\n");
      out.write("\t// <![CDATA[\n");
      out.write("\t\tvar Liferay = {\n");
      out.write("\t\t\tBrowser: {\n");
      out.write("\t\t\t\tacceptsGzip: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.acceptsGzip(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetMajorVersion: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.getMajorVersion(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetRevision: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( BrowserSnifferUtil.getRevision(request) );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetVersion: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( BrowserSnifferUtil.getVersion(request) );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisAir: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isAir(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisChrome: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isChrome(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisFirefox: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isFirefox(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisGecko: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isGecko(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisIe: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isIe(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisIphone: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isIphone(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisLinux: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isLinux(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisMac: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isMac(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisMobile: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isMobile(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisMozilla: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isMozilla(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisOpera: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isOpera(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisRtf: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isRtf(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisSafari: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isSafari(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisSun: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isSun(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisWap: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isWap(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisWapXhtml: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isWapXhtml(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisWebKit: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isWebKit(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisWindows: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isWindows(request) );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisWml: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( BrowserSnifferUtil.isWml(request) );
      out.write(";\n");
      out.write("\t\t\t\t}\n");
      out.write("\t\t\t},\n");
      out.write("\n");
      out.write("\t\t\tThemeDisplay: {\n");
      out.write("\t\t\t\tgetCompanyId: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( themeDisplay.getCompanyId() );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetCompanyGroupId: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( themeDisplay.getCompanyGroupId() );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetUserId: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( themeDisplay.getUserId() );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\n");
      out.write("\t\t\t\t");
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f4.setParent(null);
      // /html/common/themes/top_js.jspf(103,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f4.setTest( themeDisplay.isSignedIn() );
      int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
      if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\n");
          out.write("\t\t\t\t\tgetUserName: function() {\n");
          out.write("\t\t\t\t\t\treturn \"");
          out.print( UnicodeFormatter.toString(user.getFullName()) );
          out.write("\";\n");
          out.write("\t\t\t\t\t},\n");
          out.write("\t\t\t\t");
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
      out.write("\t\t\t\tgetDoAsUserIdEncoded: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( UnicodeFormatter.toString(themeDisplay.getDoAsUserId()) );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetPlid: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( themeDisplay.getPlid() );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\n");
      out.write("\t\t\t\t");
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f5 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f5.setParent(null);
      // /html/common/themes/top_js.jspf(116,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f5.setTest( layout != null );
      int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
      if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\n");
          out.write("\t\t\t\t\tgetLayoutId: function() {\n");
          out.write("\t\t\t\t\t\treturn \"");
          out.print( layout.getLayoutId() );
          out.write("\";\n");
          out.write("\t\t\t\t\t},\n");
          out.write("\t\t\t\t\tgetLayoutURL: function() {\n");
          out.write("\t\t\t\t\t\treturn \"");
          out.print( PortalUtil.getLayoutURL(layout, themeDisplay) );
          out.write("\";\n");
          out.write("\t\t\t\t\t},\n");
          out.write("\t\t\t\t\tisPrivateLayout: function() {\n");
          out.write("\t\t\t\t\t\treturn \"");
          out.print( layout.isPrivateLayout() );
          out.write("\";\n");
          out.write("\t\t\t\t\t},\n");
          out.write("\t\t\t\t\tgetParentLayoutId: function() {\n");
          out.write("\t\t\t\t\t\treturn \"");
          out.print( layout.getParentLayoutId() );
          out.write("\";\n");
          out.write("\t\t\t\t\t},\n");
          out.write("\t\t\t\t");
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
      out.write("\n");
      out.write("\t\t\t\tgetScopeGroupId: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( themeDisplay.getScopeGroupId() );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetParentGroupId: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( themeDisplay.getParentGroupId() );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisImpersonated: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( themeDisplay.isImpersonated() );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisSignedIn: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( themeDisplay.isSignedIn() );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetDefaultLanguageId: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( LocaleUtil.toLanguageId(LocaleUtil.getDefault()) );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetLanguageId: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( LanguageUtil.getLanguageId(request) );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisFreeformLayout: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( themeDisplay.isFreeformLayout() );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisStateExclusive: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( themeDisplay.isStateExclusive() );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisStateMaximized: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( themeDisplay.isStateMaximized() );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tisStatePopUp: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( themeDisplay.isStatePopUp() );
      out.write(";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetPathContext: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( themeDisplay.getPathContext() );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetPathImage: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( themeDisplay.getPathImage() );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetPathJavaScript: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( themeDisplay.getPathJavaScript() );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetPathMain: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( themeDisplay.getPathMain() );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetPathThemeImages: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( themeDisplay.getPathThemeImages() );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetPathThemeRoot: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( themeDisplay.getPathThemeRoot() );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetURLHome: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( themeDisplay.getURLHome() );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetSessionId: function() {\n");
      out.write("\t\t\t\t\treturn \"");
      out.print( session.getId() );
      out.write("\";\n");
      out.write("\t\t\t\t},\n");
      out.write("\t\t\t\tgetPortletSetupShowBordersDefault: function() {\n");
      out.write("\t\t\t\t\treturn ");
      out.print( GetterUtil.getString(theme.getSetting("portlet-setup-show-borders-default"), "true") );
      out.write(";\n");
      out.write("\t\t\t\t}\n");
      out.write("\t\t\t},\n");
      out.write("\n");
      out.write("\t\t\tPropsValues: {\n");
      out.write("\t\t\t\tNTLM_AUTH_ENABLED: ");
      out.print( PropsValues.NTLM_AUTH_ENABLED );
      out.write("\n");
      out.write("\t\t\t}\n");
      out.write("\t\t};\n");
      out.write("\n");
      out.write("\t\tvar themeDisplay = Liferay.ThemeDisplay;\n");
      out.write("\n");
      out.write("\t\t");

		long javaScriptLastModified = ServletContextUtil.getLastModified(application, "/html/js/", true);

		String alloyBaseURL = themeDisplay.getPathJavaScript() + "/aui/";
		String alloyComboURL = PortalUtil.getStaticResourceURL(request, themeDisplay.getPathContext() + "/combo/", javaScriptLastModified);
		
      out.write("\n");
      out.write("\n");
      out.write("\t\tLiferay.AUI = {\n");
      out.write("\t\t\tgetBasePath: function() {\n");
      out.write("\t\t\t\treturn '");
      out.print( alloyBaseURL );
      out.write("';\n");
      out.write("\t\t\t},\n");
      out.write("\t\t\tgetCombine: function() {\n");
      out.write("\t\t\t\treturn ");
      out.print( themeDisplay.isThemeJsFastLoad() );
      out.write(";\n");
      out.write("\t\t\t},\n");
      out.write("\t\t\tgetComboPath: function() {\n");
      out.write("\t\t\t\treturn '");
      out.print( alloyComboURL );
      out.write('&');
      out.write('p');
      out.write('=');
      out.print( themeDisplay.getPathJavaScript() );
      out.write("&';\n");
      out.write("\t\t\t},\n");
      out.write("\t\t\tgetFilter: function() {\n");
      out.write("\t\t\t\t");
      //  c:choose
      org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f0 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
      _jspx_th_c_005fchoose_005f0.setPageContext(_jspx_page_context);
      _jspx_th_c_005fchoose_005f0.setParent(null);
      int _jspx_eval_c_005fchoose_005f0 = _jspx_th_c_005fchoose_005f0.doStartTag();
      if (_jspx_eval_c_005fchoose_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\n");
          out.write("\t\t\t\t\t");
          //  c:when
          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f0 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
          _jspx_th_c_005fwhen_005f0.setPageContext(_jspx_page_context);
          _jspx_th_c_005fwhen_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
          // /html/common/themes/top_js.jspf(216,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fwhen_005f0.setTest( themeDisplay.isThemeJsFastLoad() );
          int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
          if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t\t\t\t\treturn {\n");
              out.write("\t\t\t\t\t\t\treplaceStr: function(match, fragment, string) {\n");
              out.write("\t\t\t\t\t\t\t\treturn fragment + 'm=' + (match.split('");
              out.print( themeDisplay.getPathJavaScript() );
              out.write("')[1] || '');\n");
              out.write("\t\t\t\t\t\t\t},\n");
              out.write("\t\t\t\t\t\t\tsearchExp: '(\\\\?|&)/([^&]+)'\n");
              out.write("\t\t\t\t\t\t};\n");
              out.write("\t\t\t\t\t");
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
          out.write("\t\t\t\t\t");
          if (_jspx_meth_c_005fotherwise_005f0(_jspx_th_c_005fchoose_005f0, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t\t\t");
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
      out.write("\t\t\t}\n");
      out.write("\t\t};\n");
      out.write("\n");
      out.write("\t\twindow.YUI_config = {\n");
      out.write("\t\t\tcomboBase: Liferay.AUI.getComboPath(),\n");
      out.write("\t\t\tfetchCSS: false,\n");
      out.write("\t\t\tfilter: Liferay.AUI.getFilter(),\n");
      out.write("\t\t\troot: Liferay.AUI.getBasePath()\n");
      out.write("\t\t};\n");
      out.write("\n");
      out.write("\t\t");

		String currentURL = PortalUtil.getCurrentURL(request);
		
      out.write("\n");
      out.write("\n");
      out.write("\t\tLiferay.currentURL = '");
      out.print( HtmlUtil.escapeJS(currentURL) );
      out.write("';\n");
      out.write("\t\tLiferay.currentURLEncoded = '");
      out.print( HttpUtil.encodeURL(currentURL) );
      out.write("';\n");
      out.write("\t// ]]>\n");
      out.write("</script>\n");
      out.write("\n");
      //  c:choose
      org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
      _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
      _jspx_th_c_005fchoose_005f1.setParent(null);
      int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
      if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('\n');
          out.write('	');
          //  c:when
          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f1 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
          _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
          _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
          // /html/common/themes/top_js.jspf(248,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fwhen_005f1.setTest( themeDisplay.isThemeJsFastLoad() );
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
                  // /html/common/themes/top_js.jspf(250,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f2.setTest( themeDisplay.isThemeJsBarebone() );
                  int _jspx_eval_c_005fwhen_005f2 = _jspx_th_c_005fwhen_005f2.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t<script src=\"");
                      out.print( HtmlUtil.escape(PortalUtil.getStaticResourceURL(request, themeDisplay.getPathJavaScript() + "/barebone.jsp", "minifierBundleId=" + HttpUtil.encodeURL("javascript.barebone.files"), javaScriptLastModified)) );
                      out.write("\" type=\"text/javascript\"></script>\n");
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
                  //  c:otherwise
                  org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f1 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                  _jspx_th_c_005fotherwise_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fotherwise_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
                  int _jspx_eval_c_005fotherwise_005f1 = _jspx_th_c_005fotherwise_005f1.doStartTag();
                  if (_jspx_eval_c_005fotherwise_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t<script src=\"");
                      out.print( HtmlUtil.escape(PortalUtil.getStaticResourceURL(request, themeDisplay.getPathJavaScript() + "/everything.jsp", "minifierBundleId=" + HttpUtil.encodeURL("javascript.everything.files"), javaScriptLastModified)) );
                      out.write("\" type=\"text/javascript\"></script>\n");
                      out.write("\t\t\t");
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
          //  c:otherwise
          org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f2 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
          _jspx_th_c_005fotherwise_005f2.setPageContext(_jspx_page_context);
          _jspx_th_c_005fotherwise_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
          int _jspx_eval_c_005fotherwise_005f2 = _jspx_th_c_005fotherwise_005f2.doStartTag();
          if (_jspx_eval_c_005fotherwise_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

		String[] javaScriptFiles = null;

		if (themeDisplay.isThemeJsBarebone()) {
			javaScriptFiles = JavaScriptBundleUtil.getFileNames(PropsKeys.JAVASCRIPT_BAREBONE_FILES);
		}
		else {
			javaScriptFiles = JavaScriptBundleUtil.getFileNames(PropsKeys.JAVASCRIPT_EVERYTHING_FILES);
		}

		for (String javaScriptFile : javaScriptFiles) {
		
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t<script src=\"");
              out.print( themeDisplay.getPathJavaScript() );
              out.write('/');
              out.print( javaScriptFile );
              out.write('?');
              out.write('t');
              out.write('=');
              out.print( javaScriptLastModified );
              out.write("\" type=\"text/javascript\"></script>\n");
              out.write("\n");
              out.write("\t\t");

		}
		
              out.write('\n');
              out.write('\n');
              out.write('	');
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
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f6 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f6.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f6.setParent(null);
      // /html/common/themes/top_js.jspf(282,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f6.setTest( PropsValues.JAVASCRIPT_LOG_ENABLED );
      int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
      if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\n");
          out.write("\t<script src=\"");
          out.print( themeDisplay.getPathJavaScript() );
          out.write("/firebug/firebug.js\" type=\"text/javascript\"></script>\n");
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
      out.write('\n');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f7 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f7.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f7.setParent(null);
      // /html/common/themes/top_js.jspf(286,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f7.setTest( themeDisplay.isIncludeServiceJs() );
      int _jspx_eval_c_005fif_005f7 = _jspx_th_c_005fif_005f7.doStartTag();
      if (_jspx_eval_c_005fif_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\n");
          out.write("\t<script src=\"");
          out.print( HtmlUtil.escape(PortalUtil.getStaticResourceURL(request, themeDisplay.getPathJavaScript() + "/liferay/service.js", javaScriptLastModified)) );
          out.write("\" type=\"text/javascript\"></script>\n");
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
      out.write("\n");
      out.write("<script type=\"text/javascript\">\n");
      out.write("\t// <![CDATA[\n");
      out.write("\t\t");
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f8 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f8.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f8.setParent(null);
      // /html/common/themes/top_js.jspf(292,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f8.setTest( (layoutTypePortlet != null) );
      int _jspx_eval_c_005fif_005f8 = _jspx_th_c_005fif_005f8.doStartTag();
      if (_jspx_eval_c_005fif_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");

			List<String> portletIds = layoutTypePortlet.getPortletIds();
			
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f9 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f9.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
          // /html/common/themes/top_js.jspf(298,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f9.setTest( !portletIds.isEmpty() && !layoutTypePortlet.hasStateMax() );
          int _jspx_eval_c_005fif_005f9 = _jspx_th_c_005fif_005f9.doStartTag();
          if (_jspx_eval_c_005fif_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t\t\tLiferay.Portlet.list = ['");
              out.print( StringUtil.merge(portletIds.toArray(new String[portletIds.size()]), "','") );
              out.write("'];\n");
              out.write("\t\t\t");
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
          out.write('	');
          out.write('	');
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
      out.write("\t// ]]>\n");
      out.write("</script>");
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
      out.write('\n');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f10 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f10.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f10.setParent(null);
      // /html/common/themes/top_head.jsp(125,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f10.setTest( portlets != null );
      int _jspx_eval_c_005fif_005f10 = _jspx_th_c_005fif_005f10.doStartTag();
      if (_jspx_eval_c_005fif_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('\n');
          out.write('\n');
          out.write('	');

	Set<String> headerPortalJavaScriptSet = new LinkedHashSet<String>();

	for (Portlet portlet : portlets) {
		for (String headerPortalJavaScript : portlet.getHeaderPortalJavaScript()) {
			if (!HttpUtil.hasProtocol(headerPortalJavaScript)) {
				headerPortalJavaScript = PortalUtil.getStaticResourceURL(request, request.getContextPath() + headerPortalJavaScript, portlet.getTimestamp());
			}

			if (!headerPortalJavaScriptSet.contains(headerPortalJavaScript) && !themeDisplay.isIncludedJs(headerPortalJavaScript)) {
				headerPortalJavaScriptSet.add(headerPortalJavaScript);
	
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t<script src=\"");
          out.print( HtmlUtil.escape(headerPortalJavaScript) );
          out.write("\" type=\"text/javascript\"></script>\n");
          out.write("\n");
          out.write("\t");

			}
		}
	}

	Set<String> headerPortletJavaScriptSet = new LinkedHashSet<String>();

	for (Portlet portlet : portlets) {
		for (String headerPortletJavaScript : portlet.getHeaderPortletJavaScript()) {
			if (!HttpUtil.hasProtocol(headerPortletJavaScript)) {
				headerPortletJavaScript = PortalUtil.getStaticResourceURL(request, portlet.getContextPath() + headerPortletJavaScript, portlet.getTimestamp());
			}

			if (!headerPortletJavaScriptSet.contains(headerPortletJavaScript)) {
				headerPortletJavaScriptSet.add(headerPortletJavaScript);
	
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t<script src=\"");
          out.print( HtmlUtil.escape(headerPortletJavaScript) );
          out.write("\" type=\"text/javascript\"></script>\n");
          out.write("\n");
          out.write("\t");

			}
		}
	}
	
          out.write('\n');
          out.write('\n');
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
      out.write('\n');
      out.write('\n');

List<String> markupHeaders = (List<String>)request.getAttribute(MimeResponse.MARKUP_HEAD_ELEMENT);

if (markupHeaders != null) {
	for (String markupHeader : markupHeaders) {

      out.write("\n");
      out.write("\n");
      out.write("\t\t");
      out.print( markupHeader );
      out.write('\n');
      out.write('\n');

	}
}

StringBundler pageTopSB = (StringBundler)request.getAttribute(WebKeys.PAGE_TOP);

      out.write('\n');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f11 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f11.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f11.setParent(null);
      // /html/common/themes/top_head.jsp(187,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f11.setTest( pageTopSB != null );
      int _jspx_eval_c_005fif_005f11 = _jspx_th_c_005fif_005f11.doStartTag();
      if (_jspx_eval_c_005fif_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('\n');
          out.write('\n');
          out.write('	');

	pageTopSB.writeTo(out);
	
          out.write('\n');
          out.write('\n');
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
      out.write('\n');
      out.write('\n');
      out.write("\n");
      out.write("\n");
      out.write("<link class=\"lfr-css-file\" href=\"");
      out.print( HtmlUtil.escape(PortalUtil.getStaticResourceURL(request, themeDisplay.getPathThemeCss() + "/main.css")) );
      out.write("\" rel=\"stylesheet\" type=\"text/css\" />\n");
      out.write("\n");
      out.write("<style type=\"text/css\">\n");
      out.write("\t/* <![CDATA[ */\n");
      out.write("\t\t");
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f12 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f12.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f12.setParent(null);
      // /html/common/themes/top_head.jsp(201,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f12.setTest( !themeDisplay.getCompanyLogo().equals(StringPool.BLANK) );
      int _jspx_eval_c_005fif_005f12 = _jspx_th_c_005fif_005f12.doStartTag();
      if (_jspx_eval_c_005fif_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\n");
          out.write("\t\t\t#heading .logo {\n");
          out.write("\t\t\t\tbackground: url(");
          out.print( HtmlUtil.escape(themeDisplay.getCompanyLogo()) );
          out.write(") no-repeat;\n");
          out.write("\t\t\t\tdisplay: block;\n");
          out.write("\t\t\t\tfont-size: 0;\n");
          out.write("\t\t\t\theight: ");
          out.print( themeDisplay.getCompanyLogoHeight() );
          out.write("px;\n");
          out.write("\t\t\t\ttext-indent: -9999em;\n");
          out.write("\t\t\t\twidth: ");
          out.print( themeDisplay.getCompanyLogoWidth() );
          out.write("px;\n");
          out.write("\t\t\t}\n");
          out.write("\t\t");
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
      out.write("\n");
      out.write("\n");
      out.write("\t\t");
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f13 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f13.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f13.setParent(null);
      // /html/common/themes/top_head.jsp(212,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f13.setTest( BrowserSnifferUtil.isIe(request) && (BrowserSnifferUtil.getMajorVersion(request) < 7) );
      int _jspx_eval_c_005fif_005f13 = _jspx_th_c_005fif_005f13.doStartTag();
      if (_jspx_eval_c_005fif_005f13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\n");
          out.write("\t\t\timg, .png {\n");
          out.write("\t\t\t\tposition: relative;\n");
          out.write("\t\t\t\tbehavior: expression(\n");
          out.write("\t\t\t\t\t(this.runtimeStyle.behavior = \"none\") &&\n");
          out.write("\t\t\t\t\t(\n");
          out.write("\t\t\t\t\t\tthis.pngSet || (this.src && this.src.toLowerCase().indexOf('spacer.png') > -1) ?\n");
          out.write("\t\t\t\t\t\t\tthis.pngSet = true :\n");
          out.write("\t\t\t\t\t\t\t\t(\n");
          out.write("\t\t\t\t\t\t\t\t\tthis.nodeName == \"IMG\" &&\n");
          out.write("\t\t\t\t\t\t\t\t\t(\n");
          out.write("\t\t\t\t\t\t\t\t\t\t(\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\t(this.src.toLowerCase().indexOf('.png') > -1) ||\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\t(this.className && ([''].concat(this.className.split(' ')).concat(['']).join('|').indexOf('|png|')) > -1)\n");
          out.write("\t\t\t\t\t\t\t\t\t\t) &&\n");
          out.write("\t\t\t\t\t\t\t\t\t\t(this.className.indexOf('no-png-fix') == -1)\n");
          out.write("\t\t\t\t\t\t\t\t\t) ?\n");
          out.write("\t\t\t\t\t\t\t\t\t\t(\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\tthis.runtimeStyle.backgroundImage = \"none\",\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\tthis.runtimeStyle.filter = \"progid:DXImageTransform.Microsoft.AlphaImageLoader(src='\" + this.src + \"', sizingMethod='image')\",\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\tthis.src = \"");
          out.print( themeDisplay.getPathThemeImages() );
          out.write("/spacer.png\"\n");
          out.write("\t\t\t\t\t\t\t\t\t\t) :\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\t(\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\t\t(\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t(this.currentStyle.backgroundImage.toLowerCase().indexOf('.png') > -1) ||\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t(this.className && ([''].concat(this.className.split(' ')).concat(['']).join('|').indexOf('|png|')) > -1)\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\t\t) ?\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t(\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tthis.origBg = this.origBg ?\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tthis.origBg :\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tthis.currentStyle.backgroundImage.toString().replace('url(\"','').replace('\")',''),\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tthis.runtimeStyle.filter = \"progid:DXImageTransform.Microsoft.AlphaImageLoader(src='\" + this.origBg + \"', sizingMethod='crop')\",\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tthis.runtimeStyle.backgroundImage = \"none\"\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t) :\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t''\n");
          out.write("\t\t\t\t\t\t\t\t\t\t\t)\n");
          out.write("\t\t\t\t\t\t\t\t),\n");
          out.write("\t\t\t\t\t\t\t\tthis.pngSet = true\n");
          out.write("\t\t\t\t\t)\n");
          out.write("\t\t\t\t);\n");
          out.write("\t\t\t}\n");
          out.write("\t\t");
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
      out.write("\n");
      out.write("\t/* ]]> */\n");
      out.write("</style>\n");
      out.write("\n");
      out.write('\n');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f14 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f14.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f14.setParent(null);
      // /html/common/themes/top_head.jsp(259,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f14.setTest( (layout != null) && Validator.isNotNull(layout.getCssText()) );
      int _jspx_eval_c_005fif_005f14 = _jspx_th_c_005fif_005f14.doStartTag();
      if (_jspx_eval_c_005fif_005f14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\n");
          out.write("\t<style type=\"text/css\">\n");
          out.write("\t\t");
          out.print( layout.getCssText() );
          out.write("\n");
          out.write("\t</style>\n");
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
      out.write('\n');
      out.write('\n');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f15 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f15.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f15.setParent(null);
      // /html/common/themes/top_head.jsp(267,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f15.setTest( portlets != null );
      int _jspx_eval_c_005fif_005f15 = _jspx_th_c_005fif_005f15.doStartTag();
      if (_jspx_eval_c_005fif_005f15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\n");
          out.write("\t<style type=\"text/css\">\n");
          out.write("\n");
          out.write("\t\t");

		for (Portlet portlet : portlets) {
			PortletPreferences portletSetup = PortletPreferencesFactoryUtil.getLayoutPortletSetup(layout, portlet.getPortletId());

			String portletSetupCss = portletSetup.getValue("portlet-setup-css", StringPool.BLANK);
		
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f16 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f16.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f15);
          // /html/common/themes/top_head.jsp(277,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f16.setTest( Validator.isNotNull(portletSetupCss) );
          int _jspx_eval_c_005fif_005f16 = _jspx_th_c_005fif_005f16.doStartTag();
          if (_jspx_eval_c_005fif_005f16 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t");

				try {
				
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

JSONObject jsonObj = PortletSetupUtil.cssToJSON(portletSetup, portletSetupCss);

List finalCSS = new ArrayList();

// Background data

JSONObject bgData = jsonObj.getJSONObject("bgData");

String bgColor = bgData.getString("backgroundColor");
String bgImage = bgData.getString("backgroundImage");

JSONObject bgPos = bgData.getJSONObject("backgroundPosition");
JSONObject bgPosLeft = bgPos.getJSONObject(_LEFT_KEY);
JSONObject bgPosTop = bgPos.getJSONObject(_TOP_KEY);

String bgPosLeftValue = bgPosLeft.getString(_VALUE_KEY) + bgPosLeft.getString(_UNIT_KEY);
String bgPosTopValue = bgPosTop.getString(_VALUE_KEY) + bgPosTop.getString(_UNIT_KEY);
String bgPosValue = bgPosLeftValue + " " + bgPosTopValue;

boolean useBgImage = bgData.getBoolean("useBgImage");

if (Validator.isNotNull(bgColor)) {
	finalCSS.add("background-color: " + bgColor);
}

if (Validator.isNotNull(bgImage)) {
	finalCSS.add("background-image: url(" + bgImage + ")");
}

if (useBgImage) {
	finalCSS.add("background-position: " + bgPosValue);
}

// Border data

JSONObject borderData = jsonObj.getJSONObject("borderData");
JSONObject borderWidth = borderData.getJSONObject("borderWidth");
JSONObject borderStyle = borderData.getJSONObject("borderStyle");
JSONObject borderColor = borderData.getJSONObject("borderColor");

boolean ufaBorderWidth = borderWidth.getBoolean(_SAME_FOR_ALL_KEY);
boolean ufaBorderStyle = borderStyle.getBoolean(_SAME_FOR_ALL_KEY);
boolean ufaBorderColor = borderColor.getBoolean(_SAME_FOR_ALL_KEY);

// Width

JSONObject borderWidthTop = borderWidth.getJSONObject(_TOP_KEY);
JSONObject borderWidthRight = borderWidth.getJSONObject(_RIGHT_KEY);
JSONObject borderWidthBottom = borderWidth.getJSONObject(_BOTTOM_KEY);
JSONObject borderWidthLeft = borderWidth.getJSONObject(_LEFT_KEY);

String borderTopWidthValue = borderWidthTop.getString(_VALUE_KEY) + borderWidthTop.getString(_UNIT_KEY);
String borderRightWidthValue = borderWidthRight.getString(_VALUE_KEY) + borderWidthRight.getString(_UNIT_KEY);
String borderBottomWidthValue = borderWidthBottom.getString(_VALUE_KEY) + borderWidthBottom.getString(_UNIT_KEY);
String borderLeftWidthValue = borderWidthLeft.getString(_VALUE_KEY) + borderWidthLeft.getString(_UNIT_KEY);

// Style

String borderTopStyleValue = borderStyle.getString(_TOP_KEY);
String borderRightStyleValue = borderStyle.getString(_RIGHT_KEY);
String borderBottomStyleValue = borderStyle.getString(_BOTTOM_KEY);
String borderLeftStyleValue = borderStyle.getString(_LEFT_KEY);

// Color

String borderTopColorValue = borderColor.getString(_TOP_KEY);
String borderRightColorValue = borderColor.getString(_RIGHT_KEY);
String borderBottomColorValue = borderColor.getString(_BOTTOM_KEY);
String borderLeftColorValue = borderColor.getString(_LEFT_KEY);

if (ufaBorderWidth) {
	if (!_unitSet.contains(borderTopWidthValue)) {
		finalCSS.add("border-width: " + borderTopWidthValue);
	}
}
else {
	if (!_unitSet.contains(borderTopWidthValue)) {
		finalCSS.add("border-top-width: " + borderTopWidthValue);
	}

	if (!_unitSet.contains(borderRightWidthValue)) {
		finalCSS.add("border-right-width: " + borderRightWidthValue);
	}

	if (!_unitSet.contains(borderBottomWidthValue)) {
		finalCSS.add("border-bottom-width: " + borderBottomWidthValue);
	}

	if (!_unitSet.contains(borderLeftWidthValue)) {
		finalCSS.add("border-left-width: " + borderLeftWidthValue);
	}
}

if (ufaBorderStyle && !_unitSet.contains(borderTopWidthValue)) {
	finalCSS.add("border-style: " + borderTopStyleValue);
}
else {
	if (Validator.isNotNull(borderTopStyleValue)) {
		finalCSS.add("border-top-style: " + borderTopStyleValue);
	}

	if (Validator.isNotNull(borderRightStyleValue)) {
		finalCSS.add("border-right-style: " + borderRightStyleValue);
	}

	if (Validator.isNotNull(borderBottomStyleValue)) {
		finalCSS.add("border-bottom-style: " + borderBottomStyleValue);
	}

	if (Validator.isNotNull(borderLeftStyleValue)) {
		finalCSS.add("border-left-style: " + borderLeftStyleValue);
	}
}

if (ufaBorderColor) {
	if (Validator.isNotNull(borderTopColorValue)) {
		finalCSS.add("border-color: " + borderTopColorValue);
	}
}
else {
	if (Validator.isNotNull(borderTopColorValue)) {
		finalCSS.add("border-top-color: " + borderTopColorValue);
	}

	if (Validator.isNotNull(borderRightColorValue)) {
		finalCSS.add("border-right-color: " + borderRightColorValue);
	}

	if (Validator.isNotNull(borderBottomColorValue)) {
		finalCSS.add("border-bottom-color: " + borderBottomColorValue);
	}

	if (Validator.isNotNull(borderLeftColorValue)) {
		finalCSS.add("border-left-color: " + borderLeftColorValue);
	}
}

// Spacing data

JSONObject spacingData = jsonObj.getJSONObject("spacingData");
JSONObject margin = spacingData.getJSONObject("margin");
JSONObject padding = spacingData.getJSONObject("padding");

boolean ufaMargin = margin.getBoolean(_SAME_FOR_ALL_KEY);
boolean ufaPadding = padding.getBoolean(_SAME_FOR_ALL_KEY);

// Margin

JSONObject marginTop = margin.getJSONObject(_TOP_KEY);
JSONObject marginRight = margin.getJSONObject(_RIGHT_KEY);
JSONObject marginBottom = margin.getJSONObject(_BOTTOM_KEY);
JSONObject marginLeft = margin.getJSONObject(_LEFT_KEY);

String marginTopValue = marginTop.getString(_VALUE_KEY) + marginTop.getString(_UNIT_KEY);
String marginRightValue = marginRight.getString(_VALUE_KEY) + marginRight.getString(_UNIT_KEY);
String marginBottomValue = marginBottom.getString(_VALUE_KEY) + marginBottom.getString(_UNIT_KEY);
String marginLeftValue = marginLeft.getString(_VALUE_KEY) + marginLeft.getString(_UNIT_KEY);

if (ufaMargin) {
	if (!_unitSet.contains(marginTopValue)) {
		finalCSS.add("margin: " + marginTopValue);
	}
}
else {
	if (!_unitSet.contains(marginTopValue)) {
		finalCSS.add("margin-top: " + marginTopValue);
	}

	if (!_unitSet.contains(marginRightValue)) {
		finalCSS.add("margin-right: " + marginRightValue);
	}

	if (!_unitSet.contains(marginBottomValue)) {
		finalCSS.add("margin-bottom: " + marginBottomValue);
	}

	if (!_unitSet.contains(marginLeftValue)) {
		finalCSS.add("margin-left: " + marginLeftValue);
	}
}

// Padding

JSONObject paddingTop = padding.getJSONObject(_TOP_KEY);
JSONObject paddingRight = padding.getJSONObject(_RIGHT_KEY);
JSONObject paddingBottom = padding.getJSONObject(_BOTTOM_KEY);
JSONObject paddingLeft = padding.getJSONObject(_LEFT_KEY);

String paddingTopValue = paddingTop.getString(_VALUE_KEY) + paddingTop.getString(_UNIT_KEY);
String paddingRightValue = paddingRight.getString(_VALUE_KEY) + paddingRight.getString(_UNIT_KEY);
String paddingBottomValue = paddingBottom.getString(_VALUE_KEY) + paddingBottom.getString(_UNIT_KEY);
String paddingLeftValue = paddingLeft.getString(_VALUE_KEY) + paddingLeft.getString(_UNIT_KEY);

if (ufaPadding) {
	if (!_unitSet.contains(paddingTopValue)) {
		finalCSS.add("padding: " + paddingTopValue);
	}
}
else {
	if (!_unitSet.contains(paddingTopValue)) {
		finalCSS.add("padding-top: " + paddingTopValue);
	}

	if (!_unitSet.contains(paddingRightValue)) {
		finalCSS.add("padding-right: " + paddingRightValue);
	}

	if (!_unitSet.contains(paddingBottomValue)) {
		finalCSS.add("padding-bottom: " + paddingBottomValue);
	}

	if (!_unitSet.contains(paddingLeftValue)) {
		finalCSS.add("padding-left: " + paddingLeftValue);
	}
}

// Text data

JSONObject textData = jsonObj.getJSONObject("textData");

String color = textData.getString("color");
String fontFamily = textData.getString("fontFamily");
String fontSize = textData.getString("fontSize");
String fontStyle = textData.getString("fontStyle");
String fontWeight = textData.getString("fontWeight");
String letterSpacing = textData.getString("letterSpacing");
String lineHeight = textData.getString("lineHeight");
String textAlign = textData.getString("textAlign");
String textDecoration = textData.getString("textDecoration");
String wordSpacing = textData.getString("wordSpacing");

if (Validator.isNotNull(color)) {
	finalCSS.add("color: " + color);
}

if (Validator.isNotNull(fontFamily)) {
	finalCSS.add("font-family: '" + fontFamily + "'");
}

if (Validator.isNotNull(fontSize)) {
	finalCSS.add("font-size: " + fontSize);
}

if (Validator.isNotNull(fontStyle)) {
	finalCSS.add("font-style: " + fontStyle);
}

if (Validator.isNotNull(fontWeight)) {
	finalCSS.add("font-weight: " + fontWeight);
}

if (Validator.isNotNull(letterSpacing)) {
	finalCSS.add("letter-spacing: " + letterSpacing);
}

if (Validator.isNotNull(lineHeight)) {
	finalCSS.add("line-height: " + lineHeight);
}

if (Validator.isNotNull(textAlign)) {
	finalCSS.add("text-align: " + textAlign);
}

if (Validator.isNotNull(textDecoration)) {
	finalCSS.add("text-decoration: " + textDecoration);
}

if (Validator.isNotNull(wordSpacing)) {
	finalCSS.add("word-spacing: " + wordSpacing);
}

// Advanced styling

JSONObject advancedData = jsonObj.getJSONObject("advancedData");

String customCSS = advancedData.getString("customCSS");

customCSS = StringUtil.replace(
	customCSS,
	new String[] {"<", ">", "expression("},
	new String[] {"&lt;", "&gt;", ""}
);

// Portlet data

JSONObject portletData = jsonObj.getJSONObject("portletData");

boolean showBorders = portletData.getBoolean("showBorders");

// Generated CSS

out.print("#p_p_id_" + portlet.getPortletId() + "_");

if (showBorders) {
	out.print(" .portlet");
}

out.print(" {\n");

String[] finalCSSArray = (String[])finalCSS.toArray(new String[0]);

String finalCSSString = StringUtil.merge(finalCSSArray, ";\n");

out.print(finalCSSString);

out.print("\n}\n");

// Advanced CSS

if (Validator.isNotNull(customCSS)) {
	out.print(customCSS);
}

              out.write('\n');
              out.write('\n');
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t");

				}
				catch (Exception e) {
					if (_log.isWarnEnabled()) {
						_log.warn(e.getMessage());
					}
				}
				
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t");
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
          out.write("\t\t");

		}
		
          out.write("\n");
          out.write("\n");
          out.write("\t</style>\n");
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
      out.write('\n');
      out.write('\n');
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

  private boolean _jspx_meth_liferay_002dtheme_005fmeta_002dtags_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-theme:meta-tags
    com.liferay.taglib.theme.MetaTagsTag _jspx_th_liferay_002dtheme_005fmeta_002dtags_005f0 = (com.liferay.taglib.theme.MetaTagsTag) _005fjspx_005ftagPool_005fliferay_002dtheme_005fmeta_002dtags_005fnobody.get(com.liferay.taglib.theme.MetaTagsTag.class);
    _jspx_th_liferay_002dtheme_005fmeta_002dtags_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dtheme_005fmeta_002dtags_005f0.setParent(null);
    int _jspx_eval_liferay_002dtheme_005fmeta_002dtags_005f0 = _jspx_th_liferay_002dtheme_005fmeta_002dtags_005f0.doStartTag();
    if (_jspx_th_liferay_002dtheme_005fmeta_002dtags_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dtheme_005fmeta_002dtags_005fnobody.reuse(_jspx_th_liferay_002dtheme_005fmeta_002dtags_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fmeta_002dtags_005fnobody.reuse(_jspx_th_liferay_002dtheme_005fmeta_002dtags_005f0);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
    int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("\t\t\t\t\t\treturn 'raw';\n");
        out.write("\t\t\t\t\t");
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
}
