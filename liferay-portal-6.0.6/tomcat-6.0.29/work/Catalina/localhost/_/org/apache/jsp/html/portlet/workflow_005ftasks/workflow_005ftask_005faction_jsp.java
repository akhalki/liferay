package org.apache.jsp.html.portlet.workflow_005ftasks;

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
import com.liferay.portal.kernel.workflow.WorkflowEngineManagerUtil;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowHandler;
import com.liferay.portal.kernel.workflow.WorkflowHandlerRegistryUtil;
import com.liferay.portal.kernel.workflow.WorkflowInstance;
import com.liferay.portal.kernel.workflow.WorkflowInstanceManagerUtil;
import com.liferay.portal.kernel.workflow.WorkflowLog;
import com.liferay.portal.kernel.workflow.WorkflowLogManagerUtil;
import com.liferay.portal.kernel.workflow.WorkflowTask;
import com.liferay.portal.kernel.workflow.WorkflowTaskDueDateException;
import com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil;
import com.liferay.portal.kernel.workflow.comparator.WorkflowComparatorFactoryUtil;
import com.liferay.portal.security.permission.ResourceActionsUtil;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.workflowtasks.search.WorkflowTaskDisplayTerms;
import com.liferay.portlet.workflowtasks.search.WorkflowTaskSearch;
import com.liferay.portlet.workflowtasks.search.WorkflowTaskSearchTerms;

public final class workflow_005ftask_005faction_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


private boolean _hasOtherAssignees(long[] pooledActorsIds, WorkflowTask workflowTask, User user) {
	if (pooledActorsIds.length == 0) {
		return false;
	}

	if (workflowTask.isCompleted()) {
		return false;
	}

	if ((pooledActorsIds.length == 1) && (pooledActorsIds[0] == user.getUserId())) {
		return false;
	}

	return true;
}

private boolean isAssignedToUser(WorkflowTask workflowTask, User user) {
	if (workflowTask.getAssigneeUserId() == user.getUserId()) {
		return true;
	}

	return false;
}

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(27);
    _jspx_dependants.add("/html/portlet/workflow_tasks/init.jsp");
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
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fshowWhenSingleIcon_005fshowExpanded;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fmodel_005fbean_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005frows_005fname_005fcols_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fshowWhenSingleIcon_005fshowExpanded = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fmodel_005fbean_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005frows_005fname_005fcols_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fshowWhenSingleIcon_005fshowExpanded.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.release();
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel.release();
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fmodel_005fbean_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005frows_005fname_005fcols_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
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
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");

Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
Format dateFormatDateTime = FastDateFormatFactoryUtil.getDateTime(locale, timeZone);

      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');

String randomId = StringPool.BLANK;

ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

WorkflowTask workflowTask = null;

if (row != null) {
	randomId = PwdGenerator.getPassword(PwdGenerator.KEY3, 4);

	Object result = row.getObject();

	workflowTask = (WorkflowTask)row.getParameter("workflowTask");
}
else {
	workflowTask = (WorkflowTask)request.getAttribute(WebKeys.WORKFLOW_TASK);
}

long[] pooledActorsIds = WorkflowTaskManagerUtil.getPooledActorsIds(company.getCompanyId(), workflowTask.getWorkflowTaskId());

Calendar dueDate = CalendarFactoryUtil.getCalendar(timeZone, locale);

if (workflowTask.getDueDate() != null) {
	dueDate.setTime(workflowTask.getDueDate());
}

      out.write('\n');
      out.write('\n');
      //  liferay-ui:icon-menu
      com.liferay.taglib.ui.IconMenuTag _jspx_th_liferay_002dui_005ficon_002dmenu_005f0 = (com.liferay.taglib.ui.IconMenuTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fshowWhenSingleIcon_005fshowExpanded.get(com.liferay.taglib.ui.IconMenuTag.class);
      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setParent(null);
      // /html/portlet/workflow_tasks/workflow_task_action.jsp(46,0) name = showExpanded type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setShowExpanded( (row == null) );
      // /html/portlet/workflow_tasks/workflow_task_action.jsp(46,0) name = showWhenSingleIcon type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setShowWhenSingleIcon( (row == null) );
      int _jspx_eval_liferay_002dui_005ficon_002dmenu_005f0 = _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.doStartTag();
      if (_jspx_eval_liferay_002dui_005ficon_002dmenu_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_liferay_002dui_005ficon_002dmenu_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.doInitBody();
        }
        do {
          out.write('\n');
          out.write('	');
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
          // /html/portlet/workflow_tasks/workflow_task_action.jsp(47,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f0.setTest( !workflowTask.isCompleted() && isAssignedToUser(workflowTask, user) );
          int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
          if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

		List<String> transitionNames = WorkflowTaskManagerUtil.getNextTransitionNames(company.getCompanyId(), user.getUserId(), workflowTask.getWorkflowTaskId());

		for (String transitionName : transitionNames) {
			String message = "proceed";

			if (Validator.isNotNull(transitionName)) {
				message = transitionName;
			}
		
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t");
              //  portlet:actionURL
              com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f0 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.get(com.liferay.taglib.portlet.ActionURLTag.class);
              _jspx_th_portlet_005factionURL_005f0.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005factionURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(60,3) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005factionURL_005f0.setVar("editURL");
              int _jspx_eval_portlet_005factionURL_005f0 = _jspx_th_portlet_005factionURL_005f0.doStartTag();
              if (_jspx_eval_portlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_portlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_portlet_005factionURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_portlet_005factionURL_005f0.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t\t");
                  if (_jspx_meth_portlet_005fparam_005f0(_jspx_th_portlet_005factionURL_005f0, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f1 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(62,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f1.setName( Constants.CMD );
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(62,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f1.setValue( Constants.SAVE );
                  int _jspx_eval_portlet_005fparam_005f1 = _jspx_th_portlet_005fparam_005f1.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                  out.write("\n");
                  out.write("\t\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f2 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(63,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f2.setName("redirect");
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(63,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f2.setValue( currentURL );
                  int _jspx_eval_portlet_005fparam_005f2 = _jspx_th_portlet_005fparam_005f2.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                  out.write("\n");
                  out.write("\t\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f3 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(64,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f3.setName("workflowTaskId");
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(64,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f3.setValue( StringUtil.valueOf(workflowTask.getWorkflowTaskId()) );
                  int _jspx_eval_portlet_005fparam_005f3 = _jspx_th_portlet_005fparam_005f3.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
                  out.write("\n");
                  out.write("\t\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f4 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f4.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(65,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f4.setName("assigneeUserId");
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(65,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f4.setValue( StringUtil.valueOf(workflowTask.getAssigneeUserId()) );
                  int _jspx_eval_portlet_005fparam_005f4 = _jspx_th_portlet_005fparam_005f4.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(67,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f1.setTest( transitionName != null );
                  int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
                  if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t");
                      //  portlet:param
                      com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f5 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                      _jspx_th_portlet_005fparam_005f5.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005fparam_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
                      // /html/portlet/workflow_tasks/workflow_task_action.jsp(68,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f5.setName("transitionName");
                      // /html/portlet/workflow_tasks/workflow_task_action.jsp(68,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f5.setValue( transitionName );
                      int _jspx_eval_portlet_005fparam_005f5 = _jspx_th_portlet_005fparam_005f5.doStartTag();
                      if (_jspx_th_portlet_005fparam_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
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
                  out.write("\t\t\t");
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
              java.lang.String editURL = null;
              editURL = (java.lang.String) _jspx_page_context.findAttribute("editURL");
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t");
              //  liferay-ui:icon
              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f0 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
              _jspx_th_liferay_002dui_005ficon_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005ficon_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(72,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f0.setCssClass( "workflow-task-" + randomId + " task-change-status-link" );
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(72,3) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f0.setImage("../aui/shuffle");
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(72,3) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f0.setMessage( message );
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(72,3) name = method type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f0.setMethod("get");
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(72,3) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f0.setUrl( editURL );
              int _jspx_eval_liferay_002dui_005ficon_005f0 = _jspx_th_liferay_002dui_005ficon_005f0.doStartTag();
              if (_jspx_th_liferay_002dui_005ficon_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

		}
		
              out.write('\n');
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
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
          // /html/portlet/workflow_tasks/workflow_task_action.jsp(86,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f2.setTest( !workflowTask.isCompleted() && !isAssignedToUser(workflowTask, user) );
          int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
          if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  portlet:actionURL
              com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f1 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.get(com.liferay.taglib.portlet.ActionURLTag.class);
              _jspx_th_portlet_005factionURL_005f1.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005factionURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(87,2) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005factionURL_005f1.setVar("assignToMeURL");
              int _jspx_eval_portlet_005factionURL_005f1 = _jspx_th_portlet_005factionURL_005f1.doStartTag();
              if (_jspx_eval_portlet_005factionURL_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_portlet_005factionURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_portlet_005factionURL_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_portlet_005factionURL_005f1.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_portlet_005fparam_005f6(_jspx_th_portlet_005factionURL_005f1, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f7 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f7.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(89,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f7.setName( Constants.CMD );
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(89,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f7.setValue( Constants.ASSIGN );
                  int _jspx_eval_portlet_005fparam_005f7 = _jspx_th_portlet_005fparam_005f7.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f8 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f8.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(90,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f8.setName("redirect");
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(90,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f8.setValue( currentURL );
                  int _jspx_eval_portlet_005fparam_005f8 = _jspx_th_portlet_005fparam_005f8.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f9 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f9.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(91,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f9.setName("workflowTaskId");
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(91,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f9.setValue( String.valueOf(workflowTask.getWorkflowTaskId()) );
                  int _jspx_eval_portlet_005fparam_005f9 = _jspx_th_portlet_005fparam_005f9.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f10 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f10.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(92,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f10.setName("assigneeUserId");
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(92,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f10.setValue( String.valueOf(user.getUserId()) );
                  int _jspx_eval_portlet_005fparam_005f10 = _jspx_th_portlet_005fparam_005f10.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f10);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f10);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_portlet_005factionURL_005f1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_portlet_005factionURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_portlet_005factionURL_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.reuse(_jspx_th_portlet_005factionURL_005f1);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.reuse(_jspx_th_portlet_005factionURL_005f1);
              java.lang.String assignToMeURL = null;
              assignToMeURL = (java.lang.String) _jspx_page_context.findAttribute("assignToMeURL");
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  liferay-ui:icon
              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f1 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
              _jspx_th_liferay_002dui_005ficon_005f1.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005ficon_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(95,2) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f1.setCssClass( "workflow-task-" + randomId + " task-assign-to-me-link" );
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(95,2) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f1.setImage("assign");
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(95,2) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f1.setMessage("assign-to-me");
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(95,2) name = method type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f1.setMethod("get");
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(95,2) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f1.setUrl( assignToMeURL );
              int _jspx_eval_liferay_002dui_005ficon_005f1 = _jspx_th_liferay_002dui_005ficon_005f1.doStartTag();
              if (_jspx_th_liferay_002dui_005ficon_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
              out.write('\n');
              out.write('	');
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
          out.write('	');
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
          // /html/portlet/workflow_tasks/workflow_task_action.jsp(104,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f3.setTest( _hasOtherAssignees(pooledActorsIds, workflowTask, user) );
          int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
          if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  portlet:actionURL
              com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f2 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.get(com.liferay.taglib.portlet.ActionURLTag.class);
              _jspx_th_portlet_005factionURL_005f2.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005factionURL_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(105,2) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005factionURL_005f2.setVar("assignURL");
              int _jspx_eval_portlet_005factionURL_005f2 = _jspx_th_portlet_005factionURL_005f2.doStartTag();
              if (_jspx_eval_portlet_005factionURL_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_portlet_005factionURL_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_portlet_005factionURL_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_portlet_005factionURL_005f2.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_portlet_005fparam_005f11(_jspx_th_portlet_005factionURL_005f2, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f12 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f12.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f2);
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(107,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f12.setName( Constants.CMD );
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(107,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f12.setValue( Constants.ASSIGN );
                  int _jspx_eval_portlet_005fparam_005f12 = _jspx_th_portlet_005fparam_005f12.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f12);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f12);
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f13 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f13.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f2);
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(108,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f13.setName("redirect");
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(108,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f13.setValue( currentURL );
                  int _jspx_eval_portlet_005fparam_005f13 = _jspx_th_portlet_005fparam_005f13.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f13);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f13);
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f14 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f14.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f2);
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(109,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f14.setName("workflowTaskId");
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(109,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f14.setValue( String.valueOf(workflowTask.getWorkflowTaskId()) );
                  int _jspx_eval_portlet_005fparam_005f14 = _jspx_th_portlet_005fparam_005f14.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f14);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f14);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_portlet_005factionURL_005f2.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_portlet_005factionURL_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_portlet_005factionURL_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.reuse(_jspx_th_portlet_005factionURL_005f2);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.reuse(_jspx_th_portlet_005factionURL_005f2);
              java.lang.String assignURL = null;
              assignURL = (java.lang.String) _jspx_page_context.findAttribute("assignURL");
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  liferay-ui:icon
              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f2 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
              _jspx_th_liferay_002dui_005ficon_005f2.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005ficon_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(112,2) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f2.setCssClass( "workflow-task-" + randomId + " task-assign-link" );
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(112,2) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f2.setImage("assign");
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(112,2) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f2.setMessage("assign-to-...");
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(112,2) name = method type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f2.setMethod("get");
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(112,2) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f2.setUrl( assignURL );
              int _jspx_eval_liferay_002dui_005ficon_005f2 = _jspx_th_liferay_002dui_005ficon_005f2.doStartTag();
              if (_jspx_th_liferay_002dui_005ficon_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f2);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f2);
              out.write('\n');
              out.write('	');
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
          out.write('	');
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
          // /html/portlet/workflow_tasks/workflow_task_action.jsp(121,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f4.setTest( !workflowTask.isCompleted() );
          int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
          if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  portlet:actionURL
              com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f3 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.get(com.liferay.taglib.portlet.ActionURLTag.class);
              _jspx_th_portlet_005factionURL_005f3.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005factionURL_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(122,2) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005factionURL_005f3.setVar("updateDueDateURL");
              int _jspx_eval_portlet_005factionURL_005f3 = _jspx_th_portlet_005factionURL_005f3.doStartTag();
              if (_jspx_eval_portlet_005factionURL_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_portlet_005factionURL_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_portlet_005factionURL_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_portlet_005factionURL_005f3.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_portlet_005fparam_005f15(_jspx_th_portlet_005factionURL_005f3, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f16 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f16.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f3);
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(124,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f16.setName( Constants.CMD );
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(124,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f16.setValue( Constants.UPDATE );
                  int _jspx_eval_portlet_005fparam_005f16 = _jspx_th_portlet_005fparam_005f16.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f16);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f16);
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f17 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f17.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f3);
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(125,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f17.setName("redirect");
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(125,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f17.setValue( currentURL );
                  int _jspx_eval_portlet_005fparam_005f17 = _jspx_th_portlet_005fparam_005f17.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f17);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f17);
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f18 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f18.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f3);
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(126,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f18.setName("workflowTaskId");
                  // /html/portlet/workflow_tasks/workflow_task_action.jsp(126,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f18.setValue( StringUtil.valueOf(workflowTask.getWorkflowTaskId()) );
                  int _jspx_eval_portlet_005fparam_005f18 = _jspx_th_portlet_005fparam_005f18.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f18);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f18);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_portlet_005factionURL_005f3.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_portlet_005factionURL_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_portlet_005factionURL_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.reuse(_jspx_th_portlet_005factionURL_005f3);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.reuse(_jspx_th_portlet_005factionURL_005f3);
              java.lang.String updateDueDateURL = null;
              updateDueDateURL = (java.lang.String) _jspx_page_context.findAttribute("updateDueDateURL");
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  liferay-ui:icon
              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f3 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
              _jspx_th_liferay_002dui_005ficon_005f3.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005ficon_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(129,2) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f3.setCssClass( "workflow-task-" + randomId + " task-due-date-link" );
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(129,2) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f3.setImage("time");
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(129,2) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f3.setMessage("update-due-date");
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(129,2) name = method type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f3.setMethod("get");
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(129,2) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f3.setUrl( updateDueDateURL );
              int _jspx_eval_liferay_002dui_005ficon_005f3 = _jspx_th_liferay_002dui_005ficon_005f3.doStartTag();
              if (_jspx_th_liferay_002dui_005ficon_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f3);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f3);
              out.write('\n');
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
          out.write('\n');
          int evalDoAfterBody = _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_liferay_002dui_005ficon_002dmenu_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
        }
      }
      if (_jspx_th_liferay_002dui_005ficon_002dmenu_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fshowWhenSingleIcon_005fshowExpanded.reuse(_jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu_0026_005fshowWhenSingleIcon_005fshowExpanded.reuse(_jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
      out.write("\n");
      out.write("\n");
      out.write("<div class=\"aui-helper-hidden\" id=\"");
      out.print( randomId );
      out.write("updateAsignee\">\n");
      out.write("\t");
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f5 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f5.setParent(null);
      // /html/portlet/workflow_tasks/workflow_task_action.jsp(140,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f5.setTest( _hasOtherAssignees(pooledActorsIds, workflowTask, user) );
      int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
      if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('\n');
          out.write('	');
          out.write('	');
          //  aui:select
          com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f0 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel.get(com.liferay.taglib.aui.SelectTag.class);
          _jspx_th_aui_005fselect_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fselect_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
          // /html/portlet/workflow_tasks/workflow_task_action.jsp(141,2) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fselect_005f0.setLabel("assign-to");
          // /html/portlet/workflow_tasks/workflow_task_action.jsp(141,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fselect_005f0.setName("assigneeUserId");
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
              out.write("\t\t\t");

			for (long pooledActorId : pooledActorsIds) {
				if (pooledActorId == user.getUserId()) {
					continue;
				}
			
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t");
              //  aui:option
              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f0 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
              _jspx_th_aui_005foption_005f0.setPageContext(_jspx_page_context);
              _jspx_th_aui_005foption_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f0);
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(150,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005foption_005f0.setLabel( HtmlUtil.escape(PortalUtil.getUserName(pooledActorId, StringPool.BLANK)) );
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(150,4) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005foption_005f0.setSelected( workflowTask.getAssigneeUserId() == pooledActorId );
              // /html/portlet/workflow_tasks/workflow_task_action.jsp(150,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005foption_005f0.setValue( String.valueOf(pooledActorId) );
              int _jspx_eval_aui_005foption_005f0 = _jspx_th_aui_005foption_005f0.doStartTag();
              if (_jspx_th_aui_005foption_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f0);
                return;
              }
              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f0);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t");

			}
			
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              int evalDoAfterBody = _jspx_th_aui_005fselect_005f0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_aui_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_aui_005fselect_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel.reuse(_jspx_th_aui_005fselect_005f0);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel.reuse(_jspx_th_aui_005fselect_005f0);
          out.write('\n');
          out.write('	');
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
      out.write("</div>\n");
      out.write("\n");
      out.write("<div class=\"aui-helper-hidden\" id=\"");
      out.print( randomId );
      out.write("updateAsigneeToMe\">\n");
      out.write("\t");
      //  aui:input
      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f0 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
      _jspx_th_aui_005finput_005f0.setPageContext(_jspx_page_context);
      _jspx_th_aui_005finput_005f0.setParent(null);
      // /html/portlet/workflow_tasks/workflow_task_action.jsp(161,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005finput_005f0.setName("asigneeUserId");
      // /html/portlet/workflow_tasks/workflow_task_action.jsp(161,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005finput_005f0.setType("hidden");
      // /html/portlet/workflow_tasks/workflow_task_action.jsp(161,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005finput_005f0.setValue( user.getUserId() );
      int _jspx_eval_aui_005finput_005f0 = _jspx_th_aui_005finput_005f0.doStartTag();
      if (_jspx_th_aui_005finput_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
        return;
      }
      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
      out.write("\n");
      out.write("</div>\n");
      out.write("\n");
      out.write("<div class=\"aui-helper-hidden\" id=\"");
      out.print( randomId );
      out.write("updateDueDate\">\n");
      out.write("\t");
      //  aui:input
      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f1 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fmodel_005fbean_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
      _jspx_th_aui_005finput_005f1.setPageContext(_jspx_page_context);
      _jspx_th_aui_005finput_005f1.setParent(null);
      // /html/portlet/workflow_tasks/workflow_task_action.jsp(165,1) name = bean type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005finput_005f1.setBean( workflowTask );
      // /html/portlet/workflow_tasks/workflow_task_action.jsp(165,1) name = model type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005finput_005f1.setModel( WorkflowTask.class );
      // /html/portlet/workflow_tasks/workflow_task_action.jsp(165,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005finput_005f1.setName("dueDate");
      // /html/portlet/workflow_tasks/workflow_task_action.jsp(165,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005finput_005f1.setValue( dueDate );
      int _jspx_eval_aui_005finput_005f1 = _jspx_th_aui_005finput_005f1.doStartTag();
      if (_jspx_th_aui_005finput_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fmodel_005fbean_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
        return;
      }
      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fmodel_005fbean_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
      out.write("\n");
      out.write("</div>\n");
      out.write("\n");
      out.write("<div class=\"aui-helper-hidden\" id=\"");
      out.print( randomId );
      out.write("updateComments\">\n");
      out.write("\t");
      if (_jspx_meth_aui_005finput_005f2(_jspx_page_context))
        return;
      out.write("\n");
      out.write("</div>\n");
      out.write("\n");
      //  aui:script
      com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
      _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fscript_005f0.setParent(null);
      // /html/portlet/workflow_tasks/workflow_task_action.jsp(172,0) name = use type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fscript_005f0.setUse("aui-dialog");
      int _jspx_eval_aui_005fscript_005f0 = _jspx_th_aui_005fscript_005f0.doStartTag();
      if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_aui_005fscript_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_aui_005fscript_005f0.doInitBody();
        }
        do {
          out.write("\n");
          out.write("\tvar showPopup = function(url, content, title) {\n");
          out.write("\t\tvar form = A.Node.create('<form />');\n");
          out.write("\n");
          out.write("\t\tform.setAttribute('action', url);\n");
          out.write("\t\tform.setAttribute('method', 'POST');\n");
          out.write("\n");
          out.write("\t\tvar comments = A.one('#");
          out.print( randomId );
          out.write("updateComments');\n");
          out.write("\n");
          out.write("\t\tif (content) {\n");
          out.write("\t\t\tform.append(content);\n");
          out.write("\t\t\tcontent.show();\n");
          out.write("\t\t}\n");
          out.write("\n");
          out.write("\t\tif (comments) {\n");
          out.write("\t\t\tform.append(comments);\n");
          out.write("\t\t\tcomments.show();\n");
          out.write("\t\t}\n");
          out.write("\n");
          out.write("\t\tvar dialog = new A.Dialog(\n");
          out.write("\t\t\t{\n");
          out.write("\t\t\t\tbodyContent: form,\n");
          out.write("\t\t\t\tbuttons: [\n");
          out.write("\t\t\t\t\t{\n");
          out.write("\t\t\t\t\t\thandler: function() {\n");
          out.write("\t\t\t\t\t\t\tsubmitForm(form);\n");
          out.write("\t\t\t\t\t\t},\n");
          out.write("\t\t\t\t\t\ttext: '");
          if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("'\n");
          out.write("\t\t\t\t\t},\n");
          out.write("\t\t\t\t\t{\n");
          out.write("\t\t\t\t\t\thandler: function() {\n");
          out.write("\t\t\t\t\t\t\tthis.close();\n");
          out.write("\t\t\t\t\t\t},\n");
          out.write("\t\t\t\t\t\ttext: '");
          if (_jspx_meth_liferay_002dui_005fmessage_005f1(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("'\n");
          out.write("\t\t\t\t\t}\n");
          out.write("\t\t\t\t],\n");
          out.write("\t\t\t\tcentered: true,\n");
          out.write("\t\t\t\tmodal: true,\n");
          out.write("\t\t\t\ttitle: title,\n");
          out.write("\t\t\t\twidth: 400\n");
          out.write("\t\t\t}\n");
          out.write("\t\t).render();\n");
          out.write("\t};\n");
          out.write("\n");
          out.write("\tA.all('.workflow-task-");
          out.print( randomId );
          out.write(" a').on(\n");
          out.write("\t\t'click',\n");
          out.write("\t\tfunction(event) {\n");
          out.write("\t\t\tvar icon = event.currentTarget;\n");
          out.write("\t\t\tvar li = icon.get('parentNode');\n");
          out.write("\n");
          out.write("\t\t\tevent.preventDefault();\n");
          out.write("\n");
          out.write("\t\t\tvar content = null;\n");
          out.write("\n");
          out.write("\t\t\tif (li.hasClass('task-due-date-link')) {\n");
          out.write("\t\t\t\tcontent = '#");
          out.print( randomId );
          out.write("updateDueDate';\n");
          out.write("\t\t\t}\n");
          out.write("\t\t\telse if (li.hasClass('task-assign-to-me-link')) {\n");
          out.write("\t\t\t\tcontent = '#");
          out.print( randomId );
          out.write("updateAsigneeToMe';\n");
          out.write("\t\t\t}\n");
          out.write("\t\t\telse if (li.hasClass('task-assign-link')) {\n");
          out.write("\t\t\t\tcontent = '#");
          out.print( randomId );
          out.write("updateAsignee';\n");
          out.write("\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\ttitle = icon.text();\n");
          out.write("\n");
          out.write("\t\t\tshowPopup(icon.attr('href'), A.one(content), title);\n");
          out.write("\t\t}\n");
          out.write("\t);\n");
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

  private boolean _jspx_meth_portlet_005fparam_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f0 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
    // /html/portlet/workflow_tasks/workflow_task_action.jsp(61,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setName("struts_action");
    // /html/portlet/workflow_tasks/workflow_task_action.jsp(61,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setValue("/workflow_tasks/edit_workflow_task");
    int _jspx_eval_portlet_005fparam_005f0 = _jspx_th_portlet_005fparam_005f0.doStartTag();
    if (_jspx_th_portlet_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f6 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f6.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
    // /html/portlet/workflow_tasks/workflow_task_action.jsp(88,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f6.setName("struts_action");
    // /html/portlet/workflow_tasks/workflow_task_action.jsp(88,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f6.setValue("/workflow_tasks/edit_workflow_task");
    int _jspx_eval_portlet_005fparam_005f6 = _jspx_th_portlet_005fparam_005f6.doStartTag();
    if (_jspx_th_portlet_005fparam_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f11 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f11.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f2);
    // /html/portlet/workflow_tasks/workflow_task_action.jsp(106,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f11.setName("struts_action");
    // /html/portlet/workflow_tasks/workflow_task_action.jsp(106,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f11.setValue("/workflow_tasks/edit_workflow_task");
    int _jspx_eval_portlet_005fparam_005f11 = _jspx_th_portlet_005fparam_005f11.doStartTag();
    if (_jspx_th_portlet_005fparam_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f11);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f15 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f15.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f3);
    // /html/portlet/workflow_tasks/workflow_task_action.jsp(123,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f15.setName("struts_action");
    // /html/portlet/workflow_tasks/workflow_task_action.jsp(123,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f15.setValue("/workflow_tasks/edit_workflow_task");
    int _jspx_eval_portlet_005fparam_005f15 = _jspx_th_portlet_005fparam_005f15.doStartTag();
    if (_jspx_th_portlet_005fparam_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f15);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f2 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005frows_005fname_005fcols_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f2.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f2.setParent(null);
    // /html/portlet/workflow_tasks/workflow_task_action.jsp(169,1) null
    _jspx_th_aui_005finput_005f2.setDynamicAttribute(null, "cols", new String("55"));
    // /html/portlet/workflow_tasks/workflow_task_action.jsp(169,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f2.setName("comment");
    // /html/portlet/workflow_tasks/workflow_task_action.jsp(169,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f2.setType("textarea");
    // /html/portlet/workflow_tasks/workflow_task_action.jsp(169,1) null
    _jspx_th_aui_005finput_005f2.setDynamicAttribute(null, "rows", new String("10"));
    int _jspx_eval_aui_005finput_005f2 = _jspx_th_aui_005finput_005f2.doStartTag();
    if (_jspx_th_aui_005finput_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005frows_005fname_005fcols_005fnobody.reuse(_jspx_th_aui_005finput_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005frows_005fname_005fcols_005fnobody.reuse(_jspx_th_aui_005finput_005f2);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    // /html/portlet/workflow_tasks/workflow_task_action.jsp(199,13) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("ok");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
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
    // /html/portlet/workflow_tasks/workflow_task_action.jsp(205,13) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f1.setKey("cancel");
    int _jspx_eval_liferay_002dui_005fmessage_005f1 = _jspx_th_liferay_002dui_005fmessage_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
    return false;
  }
}
