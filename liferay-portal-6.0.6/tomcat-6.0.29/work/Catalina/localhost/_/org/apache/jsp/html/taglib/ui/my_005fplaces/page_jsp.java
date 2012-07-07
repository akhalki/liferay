package org.apache.jsp.html.taglib.ui.my_005fplaces;

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
import com.liferay.portal.service.permission.OrganizationPermissionUtil;

public final class page_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


public static final String BUTTON_INPUT_PREFIX = "aui-button-input";

public static final String BUTTON_PREFIX = "aui-button";

public static final String FIELD_PREFIX = "aui-field";

public static final String INPUT_PREFIX = "aui-field-input";

public static final String LABEL_PREFIX = "aui-field-label";

private static String _buildCss(String prefix, String baseTypeCss, boolean inlineField, boolean disabled, boolean choiceField, boolean first, boolean last, String cssClass) {
	StringBundler sb = new StringBundler();

	sb.append(prefix);

	if (choiceField) {
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-choice");
	}
	else if (baseTypeCss.equals("textarea") || baseTypeCss.equals("password") || baseTypeCss.equals("string")) {
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-text");
	}
	else if (baseTypeCss.equals("select")) {
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-select");
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-menu");
	}
	else if (baseTypeCss.equals("button")) {
	}
	else {
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-");
		sb.append(baseTypeCss);
	}

	if (inlineField) {
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-inline");
	}

	if (disabled) {
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-disabled");
	}

	if (first) {
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-first");
	}
	else if (last) {
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-last");
	}

	if (Validator.isNotNull(cssClass)) {
		sb.append(StringPool.SPACE);
		sb.append(cssClass);
	}

	return sb.toString();
}

private static String _buildData(Map<String, Object> data) {
	if ((data == null) || (data.isEmpty())) {
		return StringPool.BLANK;
	}

	StringBundler sb = new StringBundler(data.size() * 5);

	for (Map.Entry<String, Object> entry : data.entrySet()) {
		String dataKey = entry.getKey();
		String dataValue = String.valueOf(entry.getValue());

		sb.append("data-");
		sb.append(dataKey);
		sb.append("=\"");
		sb.append(dataValue);
		sb.append("\" ");
	}

	return sb.toString();
}

private static String _buildDynamicAttributes(Map<String, Object> dynamicAttributes) {
	if ((dynamicAttributes == null) || dynamicAttributes.isEmpty()) {
		return StringPool.BLANK;
	}

	StringBundler sb = new StringBundler(dynamicAttributes.size() * 4);

	for (Map.Entry<String, Object> entry : dynamicAttributes.entrySet()) {
		String key = entry.getKey();
		String value = String.valueOf(entry.getValue());

		if (!key.equals("class")) {
			sb.append(key);
			sb.append("=\"");
			sb.append(value);
			sb.append("\" ");
		}
	}

	return sb.toString();
}

private static String _buildLabel(String inlineLabel, boolean showForLabel, String forLabel) {
	StringBundler sb = new StringBundler();

	sb.append("class=\"" + LABEL_PREFIX);

	if (Validator.isNotNull(inlineLabel) && !inlineLabel.equals("right")) {
		sb.append("-inline-label");
	}

	sb.append("\"");

	if (showForLabel) {
		sb.append(" for=\"" + forLabel + "\"");
	}

	return sb.toString();
}

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(26);
    _jspx_dependants.add("/html/taglib/init.jsp");
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/taglib/init-ext.jsp");
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
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
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

PortletRequest portletRequest = (PortletRequest)request.getAttribute(JavaConstants.JAVAX_PORTLET_REQUEST);

PortletResponse portletResponse = (PortletResponse)request.getAttribute(JavaConstants.JAVAX_PORTLET_RESPONSE);

String namespace = StringPool.BLANK;

boolean useNamespace = GetterUtil.getBoolean((String)request.getAttribute("aui:form:useNamespace"), true);

if ((portletResponse != null) && useNamespace) {
	namespace = portletResponse.getNamespace();
}

String currentURL = PortalUtil.getCurrentURL(request);

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
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");

int max = GetterUtil.getInteger((String)request.getAttribute("liferay-ui:my_places:max"));

if (max <= 0) {
	max = PropsValues.MY_PLACES_MAX_ELEMENTS;
}

List<Group> myPlaces = user.getMyPlaces(max);

      out.write('\n');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f0.setParent(null);
      // /html/taglib/ui/my_places/page.jsp(31,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f0.setTest( !myPlaces.isEmpty() );
      int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
      if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\n");
          out.write("\t<ul class=\"taglib-my-places\">\n");
          out.write("\n");
          out.write("\t\t");

		PortletURL portletURL = new PortletURLImpl(request, PortletKeys.MY_PLACES, plid, PortletRequest.ACTION_PHASE);

		portletURL.setWindowState(WindowState.NORMAL);
		portletURL.setPortletMode(PortletMode.VIEW);

		portletURL.setParameter("struts_action", "/my_places/view");

		for (Group myPlace : myPlaces) {
			myPlace = myPlace.toEscapedModel();

			boolean organizationCommunity = myPlace.isOrganization();
			boolean regularCommunity = myPlace.isCommunity();
			boolean userCommunity = myPlace.isUser();
			int publicLayoutsPageCount = myPlace.getPublicLayoutsPageCount();
			int privateLayoutsPageCount = myPlace.getPrivateLayoutsPageCount();

			Organization organization = null;

			String publicAddPageHREF = null;
			String privateAddPageHREF = null;

			if (organizationCommunity) {
				organization = OrganizationLocalServiceUtil.getOrganization(myPlace.getOrganizationId());

				if (OrganizationPermissionUtil.contains(permissionChecker, organization.getOrganizationId(), ActionKeys.MANAGE_LAYOUTS)) {
					PortletURL addPageURL = new PortletURLImpl(request, PortletKeys.MY_PLACES, plid, PortletRequest.ACTION_PHASE);

					addPageURL.setWindowState(WindowState.NORMAL);
					addPageURL.setPortletMode(PortletMode.VIEW);

					addPageURL.setParameter("struts_action", "/my_places/edit_pages");
					addPageURL.setParameter("redirect", currentURL);
					addPageURL.setParameter("groupId", String.valueOf(myPlace.getGroupId()));
					addPageURL.setParameter("privateLayout", Boolean.FALSE.toString());

					publicAddPageHREF = addPageURL.toString();

					addPageURL.setParameter("privateLayout", Boolean.TRUE.toString());

					privateAddPageHREF = addPageURL.toString();
				}
			}
			else if (regularCommunity) {
				if (GroupPermissionUtil.contains(permissionChecker, myPlace.getGroupId(), ActionKeys.MANAGE_LAYOUTS)) {
					PortletURL addPageURL = new PortletURLImpl(request, PortletKeys.MY_PLACES, plid, PortletRequest.ACTION_PHASE);

					addPageURL.setWindowState(WindowState.NORMAL);
					addPageURL.setPortletMode(PortletMode.VIEW);

					addPageURL.setParameter("struts_action", "/my_places/edit_pages");
					addPageURL.setParameter("redirect", currentURL);
					addPageURL.setParameter("groupId", String.valueOf(myPlace.getGroupId()));
					addPageURL.setParameter("privateLayout", Boolean.FALSE.toString());

					publicAddPageHREF = addPageURL.toString();

					addPageURL.setParameter("privateLayout", Boolean.TRUE.toString());

					privateAddPageHREF = addPageURL.toString();
				}
			}
			else if (userCommunity) {
				PortletURL publicAddPageURL = new PortletURLImpl(request, PortletKeys.MY_ACCOUNT, plid, PortletRequest.RENDER_PHASE);

				publicAddPageURL.setWindowState(WindowState.MAXIMIZED);
				publicAddPageURL.setPortletMode(PortletMode.VIEW);

				publicAddPageURL.setParameter("struts_action", "/my_account/edit_pages");
				publicAddPageURL.setParameter("tabs1", "public-pages");
				publicAddPageURL.setParameter("redirect", currentURL);
				publicAddPageURL.setParameter("groupId", String.valueOf(myPlace.getGroupId()));

				publicAddPageHREF = publicAddPageURL.toString();

				long privateAddPagePlid = myPlace.getDefaultPrivatePlid();

				PortletURL privateAddPageURL = new PortletURLImpl(request, PortletKeys.MY_ACCOUNT, plid, PortletRequest.RENDER_PHASE);

				privateAddPageURL.setWindowState(WindowState.MAXIMIZED);
				privateAddPageURL.setPortletMode(PortletMode.VIEW);

				privateAddPageURL.setParameter("struts_action", "/my_account/edit_pages");
				privateAddPageURL.setParameter("tabs1", "private-pages");
				privateAddPageURL.setParameter("redirect", currentURL);
				privateAddPageURL.setParameter("groupId", String.valueOf(myPlace.getGroupId()));

				privateAddPageHREF = privateAddPageURL.toString();

				if (!PropsValues.LAYOUT_USER_PUBLIC_LAYOUTS_MODIFIABLE) {
					publicAddPageHREF = null;
				}

				if (!PropsValues.LAYOUT_USER_PRIVATE_LAYOUTS_MODIFIABLE) {
					privateAddPageHREF = null;
				}
			}

			boolean showPublicPlace = true;

			boolean hasPowerUserRole = RoleLocalServiceUtil.hasUserRole(user.getUserId(), user.getCompanyId(), RoleConstants.POWER_USER, true);

			if (publicLayoutsPageCount == 0) {
				if (organizationCommunity) {
					showPublicPlace = PropsValues.MY_PLACES_SHOW_ORGANIZATION_PUBLIC_SITES_WITH_NO_LAYOUTS;
				}
				else if (regularCommunity) {
					showPublicPlace = PropsValues.MY_PLACES_SHOW_COMMUNITY_PUBLIC_SITES_WITH_NO_LAYOUTS;
				}
				else if (userCommunity) {
					showPublicPlace = PropsValues.MY_PLACES_SHOW_USER_PUBLIC_SITES_WITH_NO_LAYOUTS;

					if (!PropsValues.LAYOUT_USER_PUBLIC_LAYOUTS_MODIFIABLE || (PropsValues.LAYOUT_USER_PUBLIC_LAYOUTS_POWER_USER_REQUIRED && !hasPowerUserRole)) {
						showPublicPlace = false;
					}
				}
			}

			boolean showPrivatePlace = true;

			if (privateLayoutsPageCount == 0) {
				if (organizationCommunity) {
					showPrivatePlace = PropsValues.MY_PLACES_SHOW_ORGANIZATION_PRIVATE_SITES_WITH_NO_LAYOUTS;
				}
				else if (regularCommunity) {
					showPrivatePlace = PropsValues.MY_PLACES_SHOW_COMMUNITY_PRIVATE_SITES_WITH_NO_LAYOUTS;
				}
				else if (userCommunity) {
					showPrivatePlace = PropsValues.MY_PLACES_SHOW_USER_PRIVATE_SITES_WITH_NO_LAYOUTS;

					if (!PropsValues.LAYOUT_USER_PRIVATE_LAYOUTS_MODIFIABLE || (PropsValues.LAYOUT_USER_PRIVATE_LAYOUTS_POWER_USER_REQUIRED && !hasPowerUserRole)) {
						showPrivatePlace = false;
					}
				}
			}
		
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
          // /html/taglib/ui/my_places/page.jsp(171,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f1.setTest( showPublicPlace || showPrivatePlace );
          int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
          if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t\t\t");
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f0 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f0.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
              int _jspx_eval_c_005fchoose_005f0 = _jspx_th_c_005fchoose_005f0.doStartTag();
              if (_jspx_eval_c_005fchoose_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f0 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
                  // /html/taglib/ui/my_places/page.jsp(173,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f0.setTest( PropsValues.MY_PLACES_DISPLAY_STYLE.equals("simple") );
                  int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");

						portletURL.setParameter("groupId", String.valueOf(myPlace.getGroupId()));
						portletURL.setParameter("privateLayout", Boolean.FALSE.toString());

						boolean firstCommunity = false;

						if (myPlaces.indexOf(myPlace) == 0) {
							firstCommunity = true;
						}

						boolean lastCommunity = false;

						if (myPlaces.size()	== (myPlaces.indexOf(myPlace) + 1)) {
							lastCommunity = true;
						}

						boolean selectedCommunity = false;

						if (layout != null) {
							if (layout.getGroupId() == myPlace.getGroupId()) {
								selectedCommunity = true;
							}
						}

						boolean selectedPlace = false;

						if (layout != null) {
							if (!layout.isPrivateLayout() && (layout.getGroupId() == myPlace.getGroupId())) {
								selectedPlace = true;
							}
						}

						String cssClass = "public-community";

						if (firstCommunity) {
							cssClass += " first";
						}

						if (lastCommunity) {
							cssClass += " last";
						}

						if (selectedCommunity) {
							cssClass += " current-community";
						}

						if (selectedPlace) {
							cssClass += " current-site";
						}
						
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
                      // /html/taglib/ui/my_places/page.jsp(226,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f2.setTest( showPublicPlace && publicLayoutsPageCount > 0 );
                      int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
                      if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t<li class=\"");
                          out.print( cssClass );
                          out.write("\">\n");
                          out.write("\t\t\t\t\t\t\t\t<a href=\"");
                          out.print( HtmlUtil.escape(portletURL.toString()) );
                          out.write("\" onclick=\"Liferay.Util.forcePost(this); return false;\">\n");
                          out.write("\t\t\t\t\t\t\t\t\t<span class=\"site-name\">\n");
                          out.write("\t\t\t\t\t\t\t\t\t\t");
                          //  c:choose
                          org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                          _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fchoose_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
                          int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
                          if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f1 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                              // /html/taglib/ui/my_places/page.jsp(231,11) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f1.setTest( organizationCommunity );
                              int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
                              out.print( HtmlUtil.escape(organization.getName()) );
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f2 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                              // /html/taglib/ui/my_places/page.jsp(234,11) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f2.setTest( userCommunity );
                              int _jspx_eval_c_005fwhen_005f2 = _jspx_th_c_005fwhen_005f2.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_th_c_005fwhen_005f2, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f3 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                              // /html/taglib/ui/my_places/page.jsp(237,11) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f3.setTest( myPlace.getName().equals(GroupConstants.GUEST) );
                              int _jspx_eval_c_005fwhen_005f3 = _jspx_th_c_005fwhen_005f3.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
                              out.print( HtmlUtil.escape(themeDisplay.getAccount().getName()) );
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                              int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
                              out.print( myPlace.getName() );
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t\t\t\t\t");
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
                          out.write("\t\t\t\t\t\t\t\t\t</span>\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t");
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
                          // /html/taglib/ui/my_places/page.jsp(246,9) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f3.setTest( privateLayoutsPageCount > 0 );
                          int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
                          if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t<span class=\"site-type\">");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f1(_jspx_th_c_005fif_005f3, _jspx_page_context))
                              return;
                              out.write("</span>\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
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
                          out.write("\t\t\t\t\t\t\t\t</a>\n");
                          out.write("\t\t\t\t\t\t\t</li>\n");
                          out.write("\t\t\t\t\t\t");
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
                      out.write("\t\t\t\t\t\t");

						portletURL.setParameter("privateLayout", Boolean.TRUE.toString());

						selectedPlace = false;

						if (layout != null) {
							selectedPlace = layout.isPrivateLayout() && (layout.getGroupId() == myPlace.getGroupId());
						}

						cssClass = "private-community";

						if (selectedCommunity) {
							cssClass += " current-community";
						}

						if (selectedPlace) {
							cssClass += " current-site";
						}
						
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
                      // /html/taglib/ui/my_places/page.jsp(273,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f4.setTest( showPrivatePlace && privateLayoutsPageCount > 0 );
                      int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
                      if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t<li class=\"");
                          out.print( cssClass );
                          out.write("\">\n");
                          out.write("\t\t\t\t\t\t\t\t<a href=\"");
                          out.print( HtmlUtil.escape(portletURL.toString()) );
                          out.write("\" onclick=\"Liferay.Util.forcePost(this); return false;\">\n");
                          out.write("\t\t\t\t\t\t\t\t\t<span class=\"site-name\">\n");
                          out.write("\t\t\t\t\t\t\t\t\t\t");
                          //  c:choose
                          org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f2 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                          _jspx_th_c_005fchoose_005f2.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fchoose_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
                          int _jspx_eval_c_005fchoose_005f2 = _jspx_th_c_005fchoose_005f2.doStartTag();
                          if (_jspx_eval_c_005fchoose_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f4 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
                              // /html/taglib/ui/my_places/page.jsp(278,11) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f4.setTest( organizationCommunity );
                              int _jspx_eval_c_005fwhen_005f4 = _jspx_th_c_005fwhen_005f4.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
                              out.print( HtmlUtil.escape(organization.getName()) );
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f5 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f5.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
                              // /html/taglib/ui/my_places/page.jsp(281,11) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f5.setTest( userCommunity );
                              int _jspx_eval_c_005fwhen_005f5 = _jspx_th_c_005fwhen_005f5.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f2(_jspx_th_c_005fwhen_005f5, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f6 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f6.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
                              // /html/taglib/ui/my_places/page.jsp(284,11) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f6.setTest( myPlace.getName().equals(GroupConstants.GUEST) );
                              int _jspx_eval_c_005fwhen_005f6 = _jspx_th_c_005fwhen_005f6.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
                              out.print( HtmlUtil.escape(themeDisplay.getAccount().getName()) );
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f1 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
                              int _jspx_eval_c_005fotherwise_005f1 = _jspx_th_c_005fotherwise_005f1.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
                              out.print( myPlace.getName() );
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t\t\t\t\t");
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
                          out.write("\t\t\t\t\t\t\t\t\t</span>\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t");
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f5 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
                          // /html/taglib/ui/my_places/page.jsp(293,9) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f5.setTest( publicLayoutsPageCount > 0 );
                          int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
                          if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t<span class=\"site-type\">");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f3(_jspx_th_c_005fif_005f5, _jspx_page_context))
                              return;
                              out.write("</span>\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
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
                          out.write("\t\t\t\t\t\t\t\t</a>\n");
                          out.write("\t\t\t\t\t\t\t</li>\n");
                          out.write("\t\t\t\t\t\t");
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
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f7 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f7.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
                  // /html/taglib/ui/my_places/page.jsp(300,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f7.setTest( PropsValues.MY_PLACES_DISPLAY_STYLE.equals("classic") );
                  int _jspx_eval_c_005fwhen_005f7 = _jspx_th_c_005fwhen_005f7.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");

						boolean selectedCommunity = false;

						if (layout != null) {
							if (layout.getGroupId() == myPlace.getGroupId()) {
								selectedCommunity = true;
							}
						}
						
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t<li class=\"");
                      out.print( selectedCommunity ? "current-community" : "" );
                      out.write("\">\n");
                      out.write("\t\t\t\t\t\t\t<h3>\n");
                      out.write("\t\t\t\t\t\t\t\t<a href=\"javascript:;\">\n");
                      out.write("\t\t\t\t\t\t\t\t\t");
                      //  c:choose
                      org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f3 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                      _jspx_th_c_005fchoose_005f3.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fchoose_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f7);
                      int _jspx_eval_c_005fchoose_005f3 = _jspx_th_c_005fchoose_005f3.doStartTag();
                      if (_jspx_eval_c_005fchoose_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t\t");
                          //  c:when
                          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f8 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                          _jspx_th_c_005fwhen_005f8.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fwhen_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
                          // /html/taglib/ui/my_places/page.jsp(316,10) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fwhen_005f8.setTest( organizationCommunity );
                          int _jspx_eval_c_005fwhen_005f8 = _jspx_th_c_005fwhen_005f8.doStartTag();
                          if (_jspx_eval_c_005fwhen_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              out.print( HtmlUtil.escape(organization.getName()) );
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
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
                          out.write("\t\t\t\t\t\t\t\t\t\t");
                          //  c:when
                          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f9 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                          _jspx_th_c_005fwhen_005f9.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fwhen_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
                          // /html/taglib/ui/my_places/page.jsp(319,10) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fwhen_005f9.setTest( userCommunity );
                          int _jspx_eval_c_005fwhen_005f9 = _jspx_th_c_005fwhen_005f9.doStartTag();
                          if (_jspx_eval_c_005fwhen_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f4(_jspx_th_c_005fwhen_005f9, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
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
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t\t");
                          //  c:otherwise
                          org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f2 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                          _jspx_th_c_005fotherwise_005f2.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fotherwise_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
                          int _jspx_eval_c_005fotherwise_005f2 = _jspx_th_c_005fotherwise_005f2.doStartTag();
                          if (_jspx_eval_c_005fotherwise_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              out.print( myPlace.getName() );
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
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
                          out.write("\t\t\t\t\t\t\t\t\t");
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
                      out.write("\t\t\t\t\t\t\t\t</a>\n");
                      out.write("\t\t\t\t\t\t\t</h3>\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t<ul>\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t");

								portletURL.setParameter("groupId", String.valueOf(myPlace.getGroupId()));
								portletURL.setParameter("privateLayout", Boolean.FALSE.toString());

								boolean selectedPlace = false;

								if (layout != null) {
									selectedPlace = !layout.isPrivateLayout() && (layout.getGroupId() == myPlace.getGroupId());
								}
								
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t");
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f6 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f6.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f7);
                      // /html/taglib/ui/my_places/page.jsp(342,8) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f6.setTest( showPublicPlace );
                      int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
                      if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t<li class=\"public ");
                          out.print( selectedPlace ? "current" : "" );
                          out.write("\">\n");
                          out.write("\t\t\t\t\t\t\t\t\t\t<a href=\"");
                          out.print( publicLayoutsPageCount > 0 ? HtmlUtil.escape(portletURL.toString()) : "javascript:;" );
                          out.write("\"\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t\t");
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f7 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f7.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
                          // /html/taglib/ui/my_places/page.jsp(346,10) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f7.setTest( userCommunity );
                          int _jspx_eval_c_005fif_005f7 = _jspx_th_c_005fif_005f7.doStartTag();
                          if (_jspx_eval_c_005fif_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\tid=\"my-community-public-pages\"\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
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
                          out.write("\t\t\t\t\t\t\t\t\t\t");
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f8 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f8.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
                          // /html/taglib/ui/my_places/page.jsp(350,10) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f8.setTest( publicLayoutsPageCount > 0 );
                          int _jspx_eval_c_005fif_005f8 = _jspx_th_c_005fif_005f8.doStartTag();
                          if (_jspx_eval_c_005fif_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\tonclick=\"Liferay.Util.forcePost(this); return false;\"\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
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
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t\t>");
                          if (_jspx_meth_liferay_002dui_005fmessage_005f5(_jspx_th_c_005fif_005f6, _jspx_page_context))
                            return;
                          out.write(" <span class=\"page-count\">(");
                          out.print( publicLayoutsPageCount );
                          out.write(")</span></a>\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t\t");
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f9 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f9.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
                          // /html/taglib/ui/my_places/page.jsp(356,10) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f9.setTest( publicAddPageHREF != null );
                          int _jspx_eval_c_005fif_005f9 = _jspx_th_c_005fif_005f9.doStartTag();
                          if (_jspx_eval_c_005fif_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t<a class=\"add-page\" href=\"");
                              out.print( HtmlUtil.escape(publicAddPageHREF) );
                              out.write("\" onclick=\"Liferay.Util.forcePost(this); return false;\">");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f6(_jspx_th_c_005fif_005f9, _jspx_page_context))
                              return;
                              out.write("</a>\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
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
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t");

								portletURL.setParameter("groupId", String.valueOf(myPlace.getGroupId()));
								portletURL.setParameter("privateLayout", Boolean.TRUE.toString());

								selectedPlace = false;

								if (layout != null) {
									selectedPlace = layout.isPrivateLayout() && (layout.getGroupId() == myPlace.getGroupId());
								}
								
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t");
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f10 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f10.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f7);
                      // /html/taglib/ui/my_places/page.jsp(373,8) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f10.setTest( showPrivatePlace );
                      int _jspx_eval_c_005fif_005f10 = _jspx_th_c_005fif_005f10.doStartTag();
                      if (_jspx_eval_c_005fif_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t<li class=\"private ");
                          out.print( selectedPlace ? "current" : "" );
                          out.write("\">\n");
                          out.write("\t\t\t\t\t\t\t\t\t\t<a href=\"");
                          out.print( privateLayoutsPageCount > 0 ? HtmlUtil.escape(portletURL.toString()) : "javascript:;" );
                          out.write("\"\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t\t");
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f11 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f11.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f10);
                          // /html/taglib/ui/my_places/page.jsp(377,10) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f11.setTest( userCommunity );
                          int _jspx_eval_c_005fif_005f11 = _jspx_th_c_005fif_005f11.doStartTag();
                          if (_jspx_eval_c_005fif_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\tid=\"my-community-private-pages\"\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
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
                          out.write("\t\t\t\t\t\t\t\t\t\t");
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f12 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f12.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f10);
                          // /html/taglib/ui/my_places/page.jsp(381,10) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f12.setTest( privateLayoutsPageCount > 0 );
                          int _jspx_eval_c_005fif_005f12 = _jspx_th_c_005fif_005f12.doStartTag();
                          if (_jspx_eval_c_005fif_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\tonclick=\"Liferay.Util.forcePost(this); return false;\"\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
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
                          out.write("\t\t\t\t\t\t\t\t\t\t>");
                          if (_jspx_meth_liferay_002dui_005fmessage_005f7(_jspx_th_c_005fif_005f10, _jspx_page_context))
                            return;
                          out.write(" <span class=\"page-count\">(");
                          out.print( privateLayoutsPageCount );
                          out.write(")</span></a>\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t\t");
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f13 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f13.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f10);
                          // /html/taglib/ui/my_places/page.jsp(387,10) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f13.setTest( privateAddPageHREF != null );
                          int _jspx_eval_c_005fif_005f13 = _jspx_th_c_005fif_005f13.doStartTag();
                          if (_jspx_eval_c_005fif_005f13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t<a class=\"add-page\" href=\"");
                              out.print( HtmlUtil.escape(privateAddPageHREF) );
                              out.write("\" onclick=\"Liferay.Util.forcePost(this); return false;\">");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f8(_jspx_th_c_005fif_005f13, _jspx_page_context))
                              return;
                              out.write("</a>\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
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
                          out.write("\t\t\t\t\t\t\t\t\t</li>\n");
                          out.write("\t\t\t\t\t\t\t\t");
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
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t</ul>\n");
                      out.write("\t\t\t\t\t\t</li>\n");
                      out.write("\t\t\t\t\t");
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
              out.write("\t\t\t");
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

		}
		
          out.write("\n");
          out.write("\n");
          out.write("\t</ul>\n");
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

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
    // /html/taglib/ui/my_places/page.jsp(235,12) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("my-public-pages");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f1 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
    // /html/taglib/ui/my_places/page.jsp(247,34) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f1.setKey("public");
    int _jspx_eval_liferay_002dui_005fmessage_005f1 = _jspx_th_liferay_002dui_005fmessage_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f2 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f2.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
    // /html/taglib/ui/my_places/page.jsp(282,12) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f2.setKey("my-private-pages");
    int _jspx_eval_liferay_002dui_005fmessage_005f2 = _jspx_th_liferay_002dui_005fmessage_005f2.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f3 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f3.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
    // /html/taglib/ui/my_places/page.jsp(294,34) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f3.setKey("private");
    int _jspx_eval_liferay_002dui_005fmessage_005f3 = _jspx_th_liferay_002dui_005fmessage_005f3.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f9, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f4 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f4.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f9);
    // /html/taglib/ui/my_places/page.jsp(320,11) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f4.setKey("my-community");
    int _jspx_eval_liferay_002dui_005fmessage_005f4 = _jspx_th_liferay_002dui_005fmessage_005f4.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f5 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f5.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
    // /html/taglib/ui/my_places/page.jsp(354,11) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f5.setKey("public-pages");
    int _jspx_eval_liferay_002dui_005fmessage_005f5 = _jspx_th_liferay_002dui_005fmessage_005f5.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f9, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f6 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f6.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f9);
    // /html/taglib/ui/my_places/page.jsp(357,134) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f6.setKey("manage-pages");
    int _jspx_eval_liferay_002dui_005fmessage_005f6 = _jspx_th_liferay_002dui_005fmessage_005f6.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f10, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f7 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f7.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f10);
    // /html/taglib/ui/my_places/page.jsp(385,11) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f7.setKey("private-pages");
    int _jspx_eval_liferay_002dui_005fmessage_005f7 = _jspx_th_liferay_002dui_005fmessage_005f7.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f13, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f8 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f8.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f13);
    // /html/taglib/ui/my_places/page.jsp(388,135) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f8.setKey("manage-pages");
    int _jspx_eval_liferay_002dui_005fmessage_005f8 = _jspx_th_liferay_002dui_005fmessage_005f8.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f8);
    return false;
  }
}
