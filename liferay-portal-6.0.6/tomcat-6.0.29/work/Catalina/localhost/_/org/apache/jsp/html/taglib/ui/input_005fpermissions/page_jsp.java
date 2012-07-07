package org.apache.jsp.html.taglib.ui.input_005fpermissions;

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
import com.liferay.portal.security.permission.ResourceActionsUtil;
import com.liferay.portal.servlet.taglib.ui.InputPermissionsParamsTagUtil;

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
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dhelp_0026_005fmessage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dhelp_0026_005fmessage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fotherwise.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dhelp_0026_005fmessage_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fscript.release();
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
      out.write("\n");

String randomNamespace = PortalUtil.generateRandomKey(request, "taglib_ui_input_permissions_page") + StringPool.UNDERLINE;

String formName = namespace + request.getAttribute("liferay-ui:input-permissions:formName");
String modelName = (String)request.getAttribute("liferay-ui:input-permissions:modelName");

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
          // /html/taglib/ui/input_permissions/page.jsp(30,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fwhen_005f0.setTest( user.getDefaultUser() );
          int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
          if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_th_c_005fwhen_005f0, _jspx_page_context))
                return;
              out.write('\n');
              out.write('	');
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
          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f1 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
          _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
          _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
          // /html/taglib/ui/input_permissions/page.jsp(33,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fwhen_005f1.setTest( modelName != null );
          int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
          if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

		Group parentGroup = GroupLocalServiceUtil.getGroup(themeDisplay.getParentGroupId());

		Role defaultGroupRole = RoleLocalServiceUtil.getDefaultGroupRole(parentGroup.getGroupId());
		Role guestRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(),  RoleConstants.GUEST);
		Role ownerRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(),  RoleConstants.OWNER);

		String[] roleNames = new String[] {RoleConstants.GUEST, defaultGroupRole.getName()};

		List communityPermissions = ListUtil.fromArray(request.getParameterValues("communityPermissions"));
		List guestPermissions = ListUtil.fromArray(request.getParameterValues("guestPermissions"));

		List supportedActions = (List)request.getAttribute("liferay-ui:input-permissions:supportedActions");
		List communityDefaultActions = (List)request.getAttribute("liferay-ui:input-permissions:communityDefaultActions");
		List guestDefaultActions = (List)request.getAttribute("liferay-ui:input-permissions:guestDefaultActions");
		List guestUnsupportedActions = (List)request.getAttribute("liferay-ui:input-permissions:guestUnsupportedActions");

		boolean submitted = (request.getParameter("communityPermissions") != null);

		boolean inputPermissionsShowOptions = ParamUtil.getBoolean(request, "inputPermissionsShowOptions");

		String inputPermissionsViewRole = ParamUtil.getString(request, "inputPermissionsViewRole", InputPermissionsParamsTagUtil.getDefaultViewRole(modelName, themeDisplay));
		
              out.write("\n");
              out.write("\n");
              out.write("\t\t<input id=\"");
              out.print( randomNamespace );
              out.write("inputPermissionsShowOptions\" name=\"");
              out.print( namespace );
              out.write("inputPermissionsShowOptions\" type=\"hidden\" value=\"");
              out.print( inputPermissionsShowOptions );
              out.write("\" />\n");
              out.write("\n");
              out.write("\t\t<p>\n");
              out.write("\t\t\t<label class=\"inline-label\" for=\"");
              out.print( namespace );
              out.write("inputPermissionsViewRole\">\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_liferay_002dui_005fmessage_005f1(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t<select id=\"");
              out.print( namespace );
              out.write("inputPermissionsViewRole\" name=\"");
              out.print( namespace );
              out.write("inputPermissionsViewRole\" onChange=\"");
              out.print( randomNamespace + "updatePermissionsView();" );
              out.write("\">\n");
              out.write("\t\t\t\t\t<option ");
              out.print( (inputPermissionsViewRole.equals(RoleConstants.GUEST)) ? "selected=\"selected\"" : "" );
              out.write(" value=\"");
              out.print( RoleConstants.GUEST );
              out.write('"');
              out.write('>');
              if (_jspx_meth_liferay_002dui_005fmessage_005f2(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                return;
              out.write(' ');
              out.write('(');
              //  liferay-ui:message
              com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f3 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
              _jspx_th_liferay_002dui_005fmessage_005f3.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005fmessage_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
              // /html/taglib/ui/input_permissions/page.jsp(66,178) name = arguments type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fmessage_005f3.setArguments( guestRole.getTitle(themeDisplay.getLocale()) );
              // /html/taglib/ui/input_permissions/page.jsp(66,178) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fmessage_005f3.setKey("x-role");
              int _jspx_eval_liferay_002dui_005fmessage_005f3 = _jspx_th_liferay_002dui_005fmessage_005f3.doStartTag();
              if (_jspx_th_liferay_002dui_005fmessage_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
              out.write(")</option>\n");
              out.write("\t\t\t\t\t<option ");
              out.print( (inputPermissionsViewRole.equals(defaultGroupRole.getName())) ? "selected=\"selected\"" : "" );
              out.write(" value=\"");
              out.print( defaultGroupRole.getName() );
              out.write("\">\n");
              out.write("\t\t\t\t\t\t");
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
              int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
              if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f2 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                  // /html/taglib/ui/input_permissions/page.jsp(69,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f2.setTest( defaultGroupRole.getName().equals(RoleConstants.COMMUNITY_MEMBER) );
                  int _jspx_eval_c_005fwhen_005f2 = _jspx_th_c_005fwhen_005f2.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f4(_jspx_th_c_005fwhen_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t");
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
                  out.write("\t\t\t\t\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f3 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                  // /html/taglib/ui/input_permissions/page.jsp(72,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f3.setTest( defaultGroupRole.getName().equals(RoleConstants.ORGANIZATION_MEMBER) );
                  int _jspx_eval_c_005fwhen_005f3 = _jspx_th_c_005fwhen_005f3.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f5(_jspx_th_c_005fwhen_005f3, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t");
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
              out.write("\t\t\t\t\t</option>\n");
              out.write("\t\t\t\t\t<option ");
              out.print( (inputPermissionsViewRole.equals(RoleConstants.OWNER)) ? "selected=\"selected\"" : "" );
              out.write(" value=\"");
              out.print( RoleConstants.OWNER );
              out.write('"');
              out.write('>');
              if (_jspx_meth_liferay_002dui_005fmessage_005f7(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                return;
              out.write("</option>\n");
              out.write("\t\t\t\t</select>\n");
              out.write("\t\t\t</label>\n");
              out.write("\n");
              out.write("\t\t\t<span ");
              out.print( inputPermissionsShowOptions ? "class=\"aui-helper-hidden\"" : "" );
              out.write(" id=\"");
              out.print( randomNamespace );
              out.write("inputPermissionsShowOptionsLink\">\n");
              out.write("\t\t\t\t<a href=\"javascript:");
              out.print( randomNamespace );
              out.write("inputPermissionsShowOptions();\" style=\"margin-left: 10px;\">");
              if (_jspx_meth_liferay_002dui_005fmessage_005f8(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                return;
              out.write(" &raquo;</a> ");
              if (_jspx_meth_liferay_002dui_005ficon_002dhelp_005f0(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t</span>\n");
              out.write("\n");
              out.write("\t\t\t<a ");
              out.print( inputPermissionsShowOptions ? "" : "class=\"aui-helper-hidden\"" );
              out.write(" href=\"javascript:");
              out.print( randomNamespace );
              out.write("inputPermissionsHideOptions();\" id=\"");
              out.print( randomNamespace );
              out.write("inputPermissionsHideOptionsLink\" style=\"margin-left: 10px;\">&laquo; ");
              if (_jspx_meth_liferay_002dui_005fmessage_005f9(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                return;
              out.write("</a>\n");
              out.write("\t\t</p>\n");
              out.write("\n");
              out.write("\t\t<table class=\"lfr-table ");
              out.print( inputPermissionsShowOptions ? "" : "aui-helper-hidden" );
              out.write("\" id=\"");
              out.print( randomNamespace );
              out.write("inputPermissionsTable\">\n");
              out.write("\t\t<tr>\n");
              out.write("\t\t\t<th>\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_liferay_002dui_005fmessage_005f10(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t</th>\n");
              out.write("\n");
              out.write("\t\t\t");

			for (int i = 0; i < supportedActions.size(); i++) {
				String action = (String)supportedActions.get(i);
			
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t<th ");
              out.print( (action.equals(ActionKeys.VIEW)) ? "class=\"aui-helper-hidden\"" : "" );
              out.write(" style=\"text-align: center;\">\n");
              out.write("\t\t\t\t\t");
              out.print( ResourceActionsUtil.getAction(pageContext, action) );
              out.write("\n");
              out.write("\t\t\t\t</th>\n");
              out.write("\n");
              out.write("\t\t\t");

			}
			
              out.write("\n");
              out.write("\n");
              out.write("\t\t</tr>\n");
              out.write("\n");
              out.write("\t\t");

		for (String roleName : roleNames) {
			Role role = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), roleName);
		
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t<tr>\n");
              out.write("\t\t\t\t<td>\n");
              out.write("\t\t\t\t\t");
              out.print( role.getTitle(themeDisplay.getLocale()) );
              out.write("\n");
              out.write("\t\t\t\t</td>\n");
              out.write("\n");
              out.write("\t\t\t\t");

				for (int i = 0; i < supportedActions.size(); i++) {
					String action = (String)supportedActions.get(i);

					boolean checked = false;
					boolean disabled = false;

					if (roleName.equals(RoleConstants.GUEST)) {
						disabled = guestUnsupportedActions.contains(action);

						if (disabled) {
							checked = false;
						}
						else if (submitted) {
							checked = guestPermissions.contains(action);
						}
						else {
							checked = guestDefaultActions.contains(action) && (inputPermissionsViewRole.equals(RoleConstants.GUEST));
						}
					}
					else if (roleName.equals(defaultGroupRole.getName())) {
						if (submitted) {
							checked = communityPermissions.contains(action);
						}
						else {
							checked = communityDefaultActions.contains(action);
						}
					}

					String checkboxFieldName = null;

					if (roleName.equals(RoleConstants.GUEST)) {
						checkboxFieldName = namespace + "guestPermissions";
					}
					else {
						checkboxFieldName = namespace + "communityPermissions";
					}

					String checkboxFieldId = checkboxFieldName + StringPool.UNDERLINE + action;
				
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t<td style=\"text-align: center;\" ");
              out.print( (action.equals(ActionKeys.VIEW)) ? "class=\"aui-helper-hidden-accessible\"" : "" );
              out.write(">\n");
              out.write("\t\t\t\t\t\t<label class=\"hidden-label\" for=\"");
              out.print( checkboxFieldId );
              out.write('"');
              out.write('>');
              //  liferay-ui:message
              com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f11 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
              _jspx_th_liferay_002dui_005fmessage_005f11.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005fmessage_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
              // /html/taglib/ui/input_permissions/page.jsp(164,63) name = arguments type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fmessage_005f11.setArguments( new Object[] {ResourceActionsUtil.getAction(pageContext, action), role.getTitle(themeDisplay.getLocale())} );
              // /html/taglib/ui/input_permissions/page.jsp(164,63) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fmessage_005f11.setKey("give-x-permission-to-users-with-role-x");
              int _jspx_eval_liferay_002dui_005fmessage_005f11 = _jspx_th_liferay_002dui_005fmessage_005f11.doStartTag();
              if (_jspx_th_liferay_002dui_005fmessage_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f11);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f11);
              out.write("</label>\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t<input ");
              out.print( checked ? "checked" : "" );
              out.write(' ');
              out.print( disabled ? "disabled" : "" );
              out.write("  id=\"");
              out.print( checkboxFieldId );
              out.write("\" name=\"");
              out.print( checkboxFieldName );
              out.write("\" type=\"checkbox\" value=\"");
              out.print( action );
              out.write("\" />\n");
              out.write("\t\t\t\t\t</td>\n");
              out.write("\n");
              out.write("\t\t\t\t");

				}
				
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t</tr>\n");
              out.write("\n");
              out.write("\t\t");

		}
		
              out.write("\n");
              out.write("\n");
              out.write("\t\t</table>\n");
              out.write("\n");
              out.write("\t\t");
              //  aui:script
              com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript.get(com.liferay.taglib.aui.ScriptTag.class);
              _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
              _jspx_th_aui_005fscript_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
              int _jspx_eval_aui_005fscript_005f0 = _jspx_th_aui_005fscript_005f0.doStartTag();
              if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_aui_005fscript_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_aui_005fscript_005f0.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\tLiferay.provide(\n");
                  out.write("\t\t\t\twindow,\n");
                  out.write("\t\t\t\t'");
                  out.print( randomNamespace );
                  out.write("inputPermissionsShowOptions',\n");
                  out.write("\t\t\t\tfunction() {\n");
                  out.write("\t\t\t\t\tvar A = AUI();\n");
                  out.write("\n");
                  out.write("\t\t\t\t\tA.one(\"#");
                  out.print( randomNamespace );
                  out.write("inputPermissionsHideOptionsLink\").show();\n");
                  out.write("\t\t\t\t\tA.one(\"#");
                  out.print( randomNamespace );
                  out.write("inputPermissionsTable\").show();\n");
                  out.write("\n");
                  out.write("\t\t\t\t\tA.one(\"#");
                  out.print( randomNamespace );
                  out.write("inputPermissionsShowOptionsLink\").hide();\n");
                  out.write("\t\t\t\t\tA.one(\"#");
                  out.print( randomNamespace );
                  out.write("inputPermissionsShowOptions\").val(\"true\");\n");
                  out.write("\t\t\t\t},\n");
                  out.write("\t\t\t\t['aui-base']\n");
                  out.write("\t\t\t);\n");
                  out.write("\n");
                  out.write("\t\t\tLiferay.provide(\n");
                  out.write("\t\t\t\twindow,\n");
                  out.write("\t\t\t\t'");
                  out.print( randomNamespace );
                  out.write("inputPermissionsHideOptions',\n");
                  out.write("\t\t\t\tfunction() {\n");
                  out.write("\t\t\t\t\tvar A = AUI();\n");
                  out.write("\n");
                  out.write("\t\t\t\t\tA.one(\"#");
                  out.print( randomNamespace );
                  out.write("inputPermissionsShowOptionsLink\").show();\n");
                  out.write("\t\t\t\t\tA.one(\"#");
                  out.print( randomNamespace );
                  out.write("inputPermissionsTable\").hide();\n");
                  out.write("\n");
                  out.write("\t\t\t\t\tA.one(\"#");
                  out.print( randomNamespace );
                  out.write("inputPermissionsHideOptionsLink\").hide();\n");
                  out.write("\t\t\t\t\tA.one(\"#");
                  out.print( randomNamespace );
                  out.write("inputPermissionsShowOptions\").val(\"false\");\n");
                  out.write("\t\t\t\t},\n");
                  out.write("\t\t\t\t['aui-base']\n");
                  out.write("\t\t\t);\n");
                  out.write("\n");
                  out.write("\t\t\tLiferay.provide(\n");
                  out.write("\t\t\t\twindow,\n");
                  out.write("\t\t\t\t'");
                  out.print( randomNamespace );
                  out.write("updatePermissionsView',\n");
                  out.write("\t\t\t\tfunction() {\n");
                  out.write("\t\t\t\t\tvar A = AUI();\n");
                  out.write("\n");
                  out.write("\t\t\t\t\tvar viewableBySelect = A.one(\"#");
                  out.print( namespace );
                  out.write("inputPermissionsViewRole\");\n");
                  out.write("\t\t\t\t\tvar guestViewCheckbox = A.one('input[name=\"");
                  out.print( namespace );
                  out.write("guestPermissions\"][value=\"VIEW\"]');\n");
                  out.write("\t\t\t\t\tvar communityViewCheckbox = A.one('input[name=\"");
                  out.print( namespace );
                  out.write("communityPermissions\"][value=\"VIEW\"]');\n");
                  out.write("\n");
                  out.write("\t\t\t\t\tif (viewableBySelect.val() == '");
                  out.print( RoleConstants.GUEST );
                  out.write("') {\n");
                  out.write("\t\t\t\t\t\tguestViewCheckbox.set(\"checked\", true);\n");
                  out.write("\t\t\t\t\t\tcommunityViewCheckbox.set(\"checked\", false);\n");
                  out.write("\t\t\t\t\t}\n");
                  out.write("\t\t\t\t\telse if (viewableBySelect.val() == '");
                  out.print( defaultGroupRole.getName() );
                  out.write("') {\n");
                  out.write("\t\t\t\t\t\tguestViewCheckbox.set(\"checked\", false);\n");
                  out.write("\t\t\t\t\t\tcommunityViewCheckbox.set(\"checked\", true);\n");
                  out.write("\t\t\t\t\t}\n");
                  out.write("\t\t\t\t\telse {\n");
                  out.write("\t\t\t\t\t\tguestViewCheckbox.set(\"checked\", false);\n");
                  out.write("\t\t\t\t\t\tcommunityViewCheckbox.set(\"checked\", false);\n");
                  out.write("\t\t\t\t\t}\n");
                  out.write("\t\t\t\t},\n");
                  out.write("\t\t\t\t['aui-base']\n");
                  out.write("\t\t\t);\n");
                  out.write("\t\t");
                  int evalDoAfterBody = _jspx_th_aui_005fscript_005f0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_aui_005fscript_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f0);
                return;
              }
              _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f0);
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
          org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f1 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
          _jspx_th_c_005fotherwise_005f1.setPageContext(_jspx_page_context);
          _jspx_th_c_005fotherwise_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
          int _jspx_eval_c_005fotherwise_005f1 = _jspx_th_c_005fotherwise_005f1.doStartTag();
          if (_jspx_eval_c_005fotherwise_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

		boolean addCommunityPermissions = ParamUtil.getBoolean(request, "addCommunityPermissions", true);
		boolean addGuestPermissions = ParamUtil.getBoolean(request, "addGuestPermissions", true);
		
              out.write("\n");
              out.write("\n");
              out.write("\t\t<input name=\"");
              out.print( namespace );
              out.write("addCommunityPermissions\" type=\"hidden\" value=\"");
              out.print( addCommunityPermissions );
              out.write("\" />\n");
              out.write("\t\t<input name=\"");
              out.print( namespace );
              out.write("addGuestPermissions\" type=\"hidden\" value=\"");
              out.print( addGuestPermissions );
              out.write("\" />\n");
              out.write("\n");
              out.write("\t\t<input ");
              out.print( addCommunityPermissions ? "checked" : "" );
              out.write(" name=\"");
              out.print( namespace );
              out.write("addCommunityPermissionsBox\" type=\"checkbox\" onClick=\"document.");
              out.print( formName );
              out.write('.');
              out.print( namespace );
              out.write("addCommunityPermissions.value = this.checked; ");
              out.print( namespace );
              out.write("checkCommunityAndGuestPermissions();\"> ");
              if (_jspx_meth_liferay_002dui_005fmessage_005f12(_jspx_th_c_005fotherwise_005f1, _jspx_page_context))
                return;
              out.write("<br />\n");
              out.write("\t\t<input ");
              out.print( addGuestPermissions ? "checked" : "" );
              out.write(" name=\"");
              out.print( namespace );
              out.write("addGuestPermissionsBox\" type=\"checkbox\" onClick=\"document.");
              out.print( formName );
              out.write('.');
              out.print( namespace );
              out.write("addGuestPermissions.value = this.checked; ");
              out.print( namespace );
              out.write("checkCommunityAndGuestPermissions();\"> ");
              if (_jspx_meth_liferay_002dui_005fmessage_005f13(_jspx_th_c_005fotherwise_005f1, _jspx_page_context))
                return;
              out.write("<br />\n");
              out.write("\t\t<input ");
              out.print( !addCommunityPermissions && !addGuestPermissions ? "checked" : "" );
              out.write(" name=\"");
              out.print( namespace );
              out.write("addUserPermissionsBox\" type=\"checkbox\" onClick=\"document.");
              out.print( formName );
              out.write('.');
              out.print( namespace );
              out.write("addCommunityPermissions.value = !this.checked; document.");
              out.print( formName );
              out.write('.');
              out.print( namespace );
              out.write("addGuestPermissions.value = !this.checked; ");
              out.print( namespace );
              out.write("checkUserPermissions();\" /> ");
              if (_jspx_meth_liferay_002dui_005fmessage_005f14(_jspx_th_c_005fotherwise_005f1, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  aui:script
              com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f1 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript.get(com.liferay.taglib.aui.ScriptTag.class);
              _jspx_th_aui_005fscript_005f1.setPageContext(_jspx_page_context);
              _jspx_th_aui_005fscript_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
              int _jspx_eval_aui_005fscript_005f1 = _jspx_th_aui_005fscript_005f1.doStartTag();
              if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_aui_005fscript_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_aui_005fscript_005f1.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\tfunction ");
                  out.print( namespace );
                  out.write("checkCommunityAndGuestPermissions() {\n");
                  out.write("\t\t\t\tif (document.");
                  out.print( formName );
                  out.write('.');
                  out.print( namespace );
                  out.write("addCommunityPermissionsBox.checked ||\n");
                  out.write("\t\t\t\t\tdocument.");
                  out.print( formName );
                  out.write('.');
                  out.print( namespace );
                  out.write("addGuestPermissionsBox.checked) {\n");
                  out.write("\n");
                  out.write("\t\t\t\t\tdocument.");
                  out.print( formName );
                  out.write('.');
                  out.print( namespace );
                  out.write("addUserPermissionsBox.checked = false;\n");
                  out.write("\t\t\t\t}\n");
                  out.write("\t\t\t\telse if (!document.");
                  out.print( formName );
                  out.write('.');
                  out.print( namespace );
                  out.write("addCommunityPermissionsBox.checked &&\n");
                  out.write("\t\t\t\t\t\t !document.");
                  out.print( formName );
                  out.write('.');
                  out.print( namespace );
                  out.write("addGuestPermissionsBox.checked) {\n");
                  out.write("\n");
                  out.write("\t\t\t\t\tdocument.");
                  out.print( formName );
                  out.write('.');
                  out.print( namespace );
                  out.write("addUserPermissionsBox.checked = true;\n");
                  out.write("\t\t\t\t}\n");
                  out.write("\t\t\t}\n");
                  out.write("\n");
                  out.write("\t\t\tfunction ");
                  out.print( namespace );
                  out.write("checkUserPermissions() {\n");
                  out.write("\t\t\t\tif (document.");
                  out.print( formName );
                  out.write('.');
                  out.print( namespace );
                  out.write("addUserPermissionsBox.checked) {\n");
                  out.write("\t\t\t\t\tdocument.");
                  out.print( formName );
                  out.write('.');
                  out.print( namespace );
                  out.write("addCommunityPermissionsBox.checked = false;\n");
                  out.write("\t\t\t\t\tdocument.");
                  out.print( formName );
                  out.write('.');
                  out.print( namespace );
                  out.write("addGuestPermissionsBox.checked = false;\n");
                  out.write("\t\t\t\t}\n");
                  out.write("\t\t\t\telse {\n");
                  out.write("\t\t\t\t\tdocument.");
                  out.print( formName );
                  out.write('.');
                  out.print( namespace );
                  out.write("addCommunityPermissionsBox.checked = true;\n");
                  out.write("\t\t\t\t\tdocument.");
                  out.print( formName );
                  out.write('.');
                  out.print( namespace );
                  out.write("addGuestPermissionsBox.checked = true;\n");
                  out.write("\t\t\t\t}\n");
                  out.write("\t\t\t}\n");
                  out.write("\t\t");
                  int evalDoAfterBody = _jspx_th_aui_005fscript_005f1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_aui_005fscript_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f1);
                return;
              }
              _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f1);
              out.write('\n');
              out.write('	');
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

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
    // /html/taglib/ui/input_permissions/page.jsp(31,2) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("not-available");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f1 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/taglib/ui/input_permissions/page.jsp(63,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f1.setKey("viewable-by");
    int _jspx_eval_liferay_002dui_005fmessage_005f1 = _jspx_th_liferay_002dui_005fmessage_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f2 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f2.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/taglib/ui/input_permissions/page.jsp(66,141) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f2.setKey("anyone");
    int _jspx_eval_liferay_002dui_005fmessage_005f2 = _jspx_th_liferay_002dui_005fmessage_005f2.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f4 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f4.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
    // /html/taglib/ui/input_permissions/page.jsp(70,8) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f4.setKey("community-members");
    int _jspx_eval_liferay_002dui_005fmessage_005f4 = _jspx_th_liferay_002dui_005fmessage_005f4.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f5 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f5.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
    // /html/taglib/ui/input_permissions/page.jsp(73,8) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f5.setKey("organization-members");
    int _jspx_eval_liferay_002dui_005fmessage_005f5 = _jspx_th_liferay_002dui_005fmessage_005f5.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
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
        out.write("\t\t\t\t\t\t\t\t");
        if (_jspx_meth_liferay_002dui_005fmessage_005f6(_jspx_th_c_005fotherwise_005f0, _jspx_page_context))
          return true;
        out.write("\n");
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

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f6 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f6.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
    // /html/taglib/ui/input_permissions/page.jsp(76,8) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f6.setKey("power-users");
    int _jspx_eval_liferay_002dui_005fmessage_005f6 = _jspx_th_liferay_002dui_005fmessage_005f6.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f7 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f7.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/taglib/ui/input_permissions/page.jsp(80,141) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f7.setKey("owner");
    int _jspx_eval_liferay_002dui_005fmessage_005f7 = _jspx_th_liferay_002dui_005fmessage_005f7.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f8 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f8.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/taglib/ui/input_permissions/page.jsp(85,105) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f8.setKey("more-options");
    int _jspx_eval_liferay_002dui_005fmessage_005f8 = _jspx_th_liferay_002dui_005fmessage_005f8.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f8);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005ficon_002dhelp_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:icon-help
    com.liferay.taglib.ui.IconHelpTag _jspx_th_liferay_002dui_005ficon_002dhelp_005f0 = (com.liferay.taglib.ui.IconHelpTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dhelp_0026_005fmessage_005fnobody.get(com.liferay.taglib.ui.IconHelpTag.class);
    _jspx_th_liferay_002dui_005ficon_002dhelp_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005ficon_002dhelp_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/taglib/ui/input_permissions/page.jsp(85,159) name = message type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005ficon_002dhelp_005f0.setMessage("input-permissions-more-options-help");
    int _jspx_eval_liferay_002dui_005ficon_002dhelp_005f0 = _jspx_th_liferay_002dui_005ficon_002dhelp_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005ficon_002dhelp_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dhelp_0026_005fmessage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002dhelp_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dhelp_0026_005fmessage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002dhelp_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f9 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f9.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/taglib/ui/input_permissions/page.jsp(88,243) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f9.setKey("hide-options");
    int _jspx_eval_liferay_002dui_005fmessage_005f9 = _jspx_th_liferay_002dui_005fmessage_005f9.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f9);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f10 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f10.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/taglib/ui/input_permissions/page.jsp(94,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f10.setKey("roles");
    int _jspx_eval_liferay_002dui_005fmessage_005f10 = _jspx_th_liferay_002dui_005fmessage_005f10.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f10);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f12 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f12.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
    // /html/taglib/ui/input_permissions/page.jsp(249,274) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f12.setKey("assign-default-permissions-to-community");
    int _jspx_eval_liferay_002dui_005fmessage_005f12 = _jspx_th_liferay_002dui_005fmessage_005f12.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f12);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f13 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f13.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
    // /html/taglib/ui/input_permissions/page.jsp(250,262) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f13.setKey("assign-default-permissions-to-guest");
    int _jspx_eval_liferay_002dui_005fmessage_005f13 = _jspx_th_liferay_002dui_005fmessage_005f13.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f13);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f14 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f14.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
    // /html/taglib/ui/input_permissions/page.jsp(251,368) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f14.setKey("only-assign-permissions-to-me");
    int _jspx_eval_liferay_002dui_005fmessage_005f14 = _jspx_th_liferay_002dui_005fmessage_005f14.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f14);
    return false;
  }
}
