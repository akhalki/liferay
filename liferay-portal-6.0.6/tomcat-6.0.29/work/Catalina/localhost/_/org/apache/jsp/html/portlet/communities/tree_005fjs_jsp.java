package org.apache.jsp.html.portlet.communities;

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
import com.liferay.portal.DuplicateGroupException;
import com.liferay.portal.DuplicateTeamException;
import com.liferay.portal.GroupNameException;
import com.liferay.portal.ImageTypeException;
import com.liferay.portal.LARFileException;
import com.liferay.portal.LARTypeException;
import com.liferay.portal.LayoutFriendlyURLException;
import com.liferay.portal.LayoutHiddenException;
import com.liferay.portal.LayoutImportException;
import com.liferay.portal.LayoutNameException;
import com.liferay.portal.LayoutParentLayoutIdException;
import com.liferay.portal.LayoutSetVirtualHostException;
import com.liferay.portal.LayoutTypeException;
import com.liferay.portal.MembershipRequestCommentsException;
import com.liferay.portal.NoSuchGroupException;
import com.liferay.portal.NoSuchLayoutException;
import com.liferay.portal.NoSuchLayoutSetException;
import com.liferay.portal.NoSuchPortletException;
import com.liferay.portal.NoSuchRoleException;
import com.liferay.portal.RemoteExportException;
import com.liferay.portal.RemoteOptionsException;
import com.liferay.portal.RequiredGroupException;
import com.liferay.portal.RequiredLayoutException;
import com.liferay.portal.TeamNameException;
import com.liferay.portal.kernel.lar.PortletDataException;
import com.liferay.portal.kernel.lar.PortletDataHandler;
import com.liferay.portal.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.portal.kernel.lar.PortletDataHandlerChoice;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.lar.UserIdStrategy;
import com.liferay.portal.kernel.plugin.PluginPackage;
import com.liferay.portal.kernel.scheduler.SchedulerEngineUtil;
import com.liferay.portal.kernel.scheduler.Trigger;
import com.liferay.portal.kernel.scheduler.messaging.SchedulerRequest;
import com.liferay.portal.kernel.staging.StagingConstants;
import com.liferay.portal.kernel.staging.StagingUtil;
import com.liferay.portal.lar.LayoutExporter;
import com.liferay.portal.liveusers.LiveUsers;
import com.liferay.portal.plugin.PluginUtil;
import com.liferay.portal.service.permission.GroupPermissionUtil;
import com.liferay.portal.service.permission.TeamPermissionUtil;
import com.liferay.portal.util.LayoutLister;
import com.liferay.portal.util.LayoutView;
import com.liferay.portal.security.permission.ResourceActionsUtil;
import com.liferay.portal.security.permission.comparator.ActionComparator;
import com.liferay.portal.security.permission.comparator.ModelResourceComparator;
import com.liferay.portal.service.permission.PortalPermissionUtil;
import com.liferay.portlet.communities.action.ActionUtil;
import com.liferay.portlet.communities.search.ExportPageChecker;
import com.liferay.portlet.communities.search.UserGroupRoleRoleChecker;
import com.liferay.portlet.communities.search.UserGroupRoleUserChecker;
import com.liferay.portlet.enterpriseadmin.search.GroupSearch;
import com.liferay.portlet.enterpriseadmin.search.GroupSearchTerms;
import com.liferay.portlet.enterpriseadmin.search.OrganizationGroupChecker;
import com.liferay.portlet.enterpriseadmin.search.OrganizationSearch;
import com.liferay.portlet.enterpriseadmin.search.OrganizationSearchTerms;
import com.liferay.portlet.enterpriseadmin.search.RoleSearch;
import com.liferay.portlet.enterpriseadmin.search.RoleSearchTerms;
import com.liferay.portlet.enterpriseadmin.search.TeamSearch;
import com.liferay.portlet.enterpriseadmin.search.TeamSearchTerms;
import com.liferay.portlet.enterpriseadmin.search.UserGroupChecker;
import com.liferay.portlet.enterpriseadmin.search.UserGroupGroupChecker;
import com.liferay.portlet.enterpriseadmin.search.UserGroupSearch;
import com.liferay.portlet.enterpriseadmin.search.UserGroupSearchTerms;
import com.liferay.portlet.enterpriseadmin.search.UserSearch;
import com.liferay.portlet.enterpriseadmin.search.UserSearchTerms;
import com.liferay.portlet.enterpriseadmin.search.UserTeamChecker;
import com.liferay.portlet.enterpriseadmin.util.EnterpriseAdminUtil;
import com.liferay.portlet.tasks.DuplicateReviewUserIdException;
import com.liferay.portlet.tasks.NoSuchProposalException;
import com.liferay.portlet.tasks.NoSuchReviewException;
import com.liferay.portlet.tasks.model.TasksProposal;
import com.liferay.portlet.tasks.model.TasksReview;
import com.liferay.portlet.tasks.service.TasksProposalLocalServiceUtil;
import com.liferay.portlet.tasks.service.TasksReviewLocalServiceUtil;
import com.liferay.portlet.tasks.service.permission.TasksProposalPermission;
import com.liferay.portlet.tasks.util.comparator.ReviewUserNameComparator;

public final class tree_005fjs_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(27);
    _jspx_dependants.add("/html/portlet/communities/init.jsp");
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
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.release();
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
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
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

long groupId = ((Long)request.getAttribute("edit_pages.jsp-groupId")).longValue();
long selPlid = ((Long)request.getAttribute("edit_pages.jsp-selPlid")).longValue();
boolean privateLayout = ((Boolean)request.getAttribute("edit_pages.jsp-privateLayout")).booleanValue();

String rootNodeName = (String)request.getAttribute("edit_pages.jsp-rootNodeName");

boolean selectableTree = ParamUtil.getBoolean(request, "selectableTree");
String treeId = ParamUtil.getString(request, "treeId");

PortletURL portletURL = (PortletURL)request.getAttribute("edit_pages.jsp-portletURL");

      out.write("\n");
      out.write("\n");
      out.write("<div class=\"lfr-tree-loading\" id=\"");
      if (_jspx_meth_portlet_005fnamespace_005f0(_jspx_page_context))
        return;
      out.write("treeLoading\">\n");
      out.write("\t<span class=\"aui-icon aui-icon-loading lfr-tree-loading-icon\"></span>\n");
      out.write("</div>\n");
      out.write("\n");
      out.write("<div class=\"lfr-tree\" id=\"");
      if (_jspx_meth_portlet_005fnamespace_005f1(_jspx_page_context))
        return;
      out.print( HtmlUtil.escape(treeId) );
      out.write("Output\"></div>\n");
      out.write("\n");
      //  aui:script
      com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
      _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fscript_005f0.setParent(null);
      // /html/portlet/communities/tree_js.jsp(38,0) name = use type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fscript_005f0.setUse("aui-io-request,aui-tree-view,dataschema-xml,datatype-xml");
      int _jspx_eval_aui_005fscript_005f0 = _jspx_th_aui_005fscript_005f0.doStartTag();
      if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_aui_005fscript_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_aui_005fscript_005f0.doInitBody();
        }
        do {
          out.write("\n");
          out.write("\tvar TreeUtil = {\n");
          out.write("\t\tDEFAULT_PARENT_LAYOUT_ID: ");
          out.print( LayoutConstants.DEFAULT_PARENT_LAYOUT_ID );
          out.write(",\n");
          out.write("\t\tOPEN_NODES: '");
          out.print( SessionTreeJSClicks.getOpenNodes(request, treeId) );
          out.write("'.split(','),\n");
          out.write("\t\tPREFIX_LAYOUT_ID: '_layoutId_',\n");
          out.write("\t\tPREFIX_PLID: '_plid_',\n");
          out.write("\t\tSELECTED_NODES: '");
          out.print( SessionTreeJSClicks.getOpenNodes(request, treeId + "SelectedNode") );
          out.write("'.split(','),\n");
          out.write("\n");
          out.write("\t\tafterRenderTree: function(event) {\n");
          out.write("\t\t\tvar treeInstance = event.target;\n");
          out.write("\n");
          out.write("\t\t\tvar rootNode = treeInstance.item(0);\n");
          out.write("\t\t\tvar loadingEl = A.one('#");
          if (_jspx_meth_portlet_005fnamespace_005f2(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("treeLoading');\n");
          out.write("\n");
          out.write("\t\t\tloadingEl.hide();\n");
          out.write("\n");
          out.write("\t\t\tTreeUtil.restoreNodeState(rootNode);\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\tcreateId: function(layoutId, plid) {\n");
          out.write("\t\t\treturn '");
          out.print( HtmlUtil.escape(treeId) );
          out.write("' + TreeUtil.PREFIX_LAYOUT_ID + layoutId + TreeUtil.PREFIX_PLID + plid;\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\tcreateLink: function(label, plid) {\n");
          out.write("\t\t\treturn '<a href=\"");
          out.print( portletURL + StringPool.AMPERSAND + portletDisplay.getNamespace() + "selPlid=" );
          out.write("'+ plid +'\">'+ Liferay.Util.escapeHTML(label) +'</a>';\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\textractLayoutId: function(node) {\n");
          out.write("\t\t\treturn node.get('id').match(/layoutId_(\\d+)/)[1];\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\textractPlid: function(node) {\n");
          out.write("\t\t\treturn node.get('id').match(/plid_(\\d+)/)[1];\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\tformatJSONResults: function(json) {\n");
          out.write("\t\t\tvar output = [];\n");
          out.write("\n");
          out.write("\t\t\tA.each(\n");
          out.write("\t\t\t\tjson,\n");
          out.write("\t\t\t\tfunction(node) {\n");
          out.write("\t\t\t\t\tvar nameXML = A.DataType.XML.parse(node.name);\n");
          out.write("\n");
          out.write("\t\t\t\t\tvar schema = {\n");
          out.write("\t\t\t\t\t\tresultListLocator: 'root',\n");
          out.write("\t\t\t\t\t\tresultFields: [\n");
          out.write("\t\t\t\t\t\t\t{\n");
          out.write("\t\t\t\t\t\t\t\tkey: 'label',\n");
          out.write("\t\t\t\t\t\t\t\tlocator: 'name'\n");
          out.write("\t\t\t\t\t\t\t}\n");
          out.write("\t\t\t\t\t\t]\n");
          out.write("\t\t\t\t\t};\n");
          out.write("\n");
          out.write("\t\t\t\t\tvar nodeBase = A.DataSchema.XML.apply(schema, nameXML).results[0];\n");
          out.write("\n");
          out.write("\t\t\t\t\tvar newNode = A.mix(\n");
          out.write("\t\t\t\t\t\tnodeBase,\n");
          out.write("\t\t\t\t\t\t{\n");
          out.write("\t\t\t\t\t\t\tafter: {\n");
          out.write("\t\t\t\t\t\t\t\tcheck: function(event) {\n");
          out.write("\t\t\t\t\t\t\t\t\tvar plid = TreeUtil.extractPlid(event.target);\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t\tTreeUtil.updateSessionTreeClick(plid, true, '");
          out.print( HtmlUtil.escape(treeId) );
          out.write("SelectedNode');\n");
          out.write("\t\t\t\t\t\t\t\t},\n");
          out.write("\t\t\t\t\t\t\t\tuncheck: function(event) {\n");
          out.write("\t\t\t\t\t\t\t\t\tvar plid = TreeUtil.extractPlid(event.target);\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t\tTreeUtil.updateSessionTreeClick(plid, false, '");
          out.print( HtmlUtil.escape(treeId) );
          out.write("SelectedNode');\n");
          out.write("\t\t\t\t\t\t\t\t}\n");
          out.write("\t\t\t\t\t\t\t},\n");
          out.write("\t\t\t\t\t\t\talwaysShowHitArea: node.hasChildren,\n");
          out.write("\t\t\t\t\t\t\tid: TreeUtil.createId(node.layoutId, node.plid),\n");
          out.write("\t\t\t\t\t\t\ttype: '");
          out.print( selectableTree ? "task" : "io" );
          out.write("'\n");
          out.write("\t\t\t\t\t\t}\n");
          out.write("\t\t\t\t\t);\n");
          out.write("\n");
          out.write("\t\t\t\t\tif (!");
          out.print( selectableTree );
          out.write(") {\n");
          out.write("\t\t\t\t\t\tnewNode.label = TreeUtil.createLink(newNode.label, node.plid);\n");
          out.write("\t\t\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\t\t\toutput.push(newNode);\n");
          out.write("\t\t\t\t}\n");
          out.write("\t\t\t);\n");
          out.write("\n");
          out.write("\t\t\treturn output;\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\trestoreNodeState: function(node) {\n");
          out.write("\t\t\tvar instance = this;\n");
          out.write("\n");
          out.write("\t\t\tvar id = node.get('id');\n");
          out.write("\t\t\tvar plid = TreeUtil.extractPlid(node);\n");
          out.write("\n");
          out.write("\t\t\tif (plid == '");
          out.print( selPlid );
          out.write("') {\n");
          out.write("\t\t\t\tnode.select();\n");
          out.write("\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\tif (A.Array.indexOf(TreeUtil.OPEN_NODES, id) > -1) {\n");
          out.write("\t\t\t\tnode.expand();\n");
          out.write("\t\t\t}\n");
          out.write("\n");
          out.write("\t\t\tif (A.Array.indexOf(TreeUtil.SELECTED_NODES, plid) > -1) {\n");
          out.write("\t\t\t\tif (node.check) {\n");
          out.write("\t\t\t\t\tnode.check();\n");
          out.write("\t\t\t\t}\n");
          out.write("\t\t\t}\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\tupdateLayout: function(data) {\n");
          out.write("\t\t\tvar updateURL = themeDisplay.getPathMain() + '/layout_management/update_page';\n");
          out.write("\n");
          out.write("\t\t\tA.io.request(\n");
          out.write("\t\t\t\tupdateURL,\n");
          out.write("\t\t\t\t{\n");
          out.write("\t\t\t\t\tdata: data\n");
          out.write("\t\t\t\t}\n");
          out.write("\t\t\t);\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\tupdateLayoutParent: function(dragPlid, dropPlid) {\n");
          out.write("\t\t\tTreeUtil.updateLayout(\n");
          out.write("\t\t\t\t{\n");
          out.write("\t\t\t\t\tcmd: 'parent_layout_id',\n");
          out.write("\t\t\t\t\tparentPlid: dropPlid,\n");
          out.write("\t\t\t\t\tplid: dragPlid\n");
          out.write("\t\t\t\t}\n");
          out.write("\t\t\t);\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\tupdateLayoutPriority: function(dragPlid, index) {\n");
          out.write("\t\t\tTreeUtil.updateLayout(\n");
          out.write("\t\t\t\t{\n");
          out.write("\t\t\t\t\tcmd: 'priority',\n");
          out.write("\t\t\t\t\tplid: dragPlid,\n");
          out.write("\t\t\t\t\tpriority: index\n");
          out.write("\t\t\t\t}\n");
          out.write("\t\t\t);\n");
          out.write("\t\t},\n");
          out.write("\n");
          out.write("\t\tupdateSessionTreeClick: function(id, open, treeId) {\n");
          out.write("\t\t\tvar sessionClickURL = themeDisplay.getPathMain() + '/portal/session_tree_js_click';\n");
          out.write("\n");
          out.write("\t\t\tvar data = {\n");
          out.write("\t\t\t\tnodeId: id,\n");
          out.write("\t\t\t\topenNode: open || false,\n");
          out.write("\t\t\t\ttreeId: treeId\n");
          out.write("\t\t\t};\n");
          out.write("\n");
          out.write("\t\t\tA.io.request(\n");
          out.write("\t\t\t\tsessionClickURL,\n");
          out.write("\t\t\t\t{\n");
          out.write("\t\t\t\t\tdata: data\n");
          out.write("\t\t\t\t}\n");
          out.write("\t\t\t);\n");
          out.write("\t\t}\n");
          out.write("\t};\n");
          out.write("\n");
          out.write("\tvar getLayoutsURL = themeDisplay.getPathMain() + '/layout_management/get_layouts';\n");
          out.write("\tvar rootId = TreeUtil.createId(TreeUtil.DEFAULT_PARENT_LAYOUT_ID, 0);\n");
          out.write("\tvar rootLabel = '");
          out.print( HtmlUtil.escape(rootNodeName) );
          out.write("';\n");
          out.write("\tvar treeElId = '");
          if (_jspx_meth_portlet_005fnamespace_005f3(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.print( HtmlUtil.escape(treeId) );
          out.write("Output';\n");
          out.write("\n");
          out.write("\tvar RootNodeType = A.TreeNodeTask;\n");
          out.write("\tvar TreeViewType = A.TreeView;\n");
          out.write("\n");
          out.write("\tif (!");
          out.print( selectableTree );
          out.write(") {\n");
          out.write("\t\tRootNodeType = A.TreeNodeIO;\n");
          out.write("\t\tTreeViewType = A.TreeViewDD;\n");
          out.write("\n");
          out.write("\t\trootLabel = TreeUtil.createLink(rootLabel, TreeUtil.DEFAULT_PARENT_LAYOUT_ID);\n");
          out.write("\t}\n");
          out.write("\n");
          out.write("\tvar rootNode = new RootNodeType(\n");
          out.write("\t\t{\n");
          out.write("\t\t\talwaysShowHitArea: true,\n");
          out.write("\t\t\tdraggable: false,\n");
          out.write("\t\t\tid: rootId,\n");
          out.write("\t\t\tlabel: rootLabel,\n");
          out.write("\t\t\tleaf: false\n");
          out.write("\t\t}\n");
          out.write("\t);\n");
          out.write("\n");
          out.write("\trootNode.get('contentBox').addClass('lfr-root-node');\n");
          out.write("\n");
          out.write("\tvar treeview = new TreeViewType(\n");
          out.write("\t\t{\n");
          out.write("\t\t\tafter: {\n");
          out.write("\t\t\t\tcollapse: function(event) {\n");
          out.write("\t\t\t\t\tvar id = event.tree.node.get('id');\n");
          out.write("\n");
          out.write("\t\t\t\t\tTreeUtil.updateSessionTreeClick(id, false, '");
          out.print( HtmlUtil.escape(treeId) );
          out.write("');\n");
          out.write("\t\t\t\t},\n");
          out.write("\t\t\t\texpand: function(event) {\n");
          out.write("\t\t\t\t\tvar id = event.tree.node.get('id');\n");
          out.write("\n");
          out.write("\t\t\t\t\tTreeUtil.updateSessionTreeClick(id, true, '");
          out.print( HtmlUtil.escape(treeId) );
          out.write("');\n");
          out.write("\t\t\t\t},\n");
          out.write("\t\t\t\trender: TreeUtil.afterRenderTree\n");
          out.write("\t\t\t},\n");
          out.write("\t\t\tboundingBox: '#' + treeElId,\n");
          out.write("\t\t\tchildren: [rootNode],\n");
          out.write("\t\t\tio: {\n");
          out.write("\t\t\t\tcfg: {\n");
          out.write("\t\t\t\t\tdata: function(node) {\n");
          out.write("\t\t\t\t\t\tvar parentLayoutId = TreeUtil.extractLayoutId(node);\n");
          out.write("\n");
          out.write("\t\t\t\t\t\treturn {\n");
          out.write("\t\t\t\t\t\t\tgroupId: ");
          out.print( groupId );
          out.write(",\n");
          out.write("\t\t\t\t\t\t\tprivateLayout: ");
          out.print( privateLayout );
          out.write(",\n");
          out.write("\t\t\t\t\t\t\tparentLayoutId: parentLayoutId\n");
          out.write("\t\t\t\t\t\t};\n");
          out.write("\t\t\t\t\t},\n");
          out.write("\t\t\t\t\tmethod: AUI.defaults.io.method\n");
          out.write("\t\t\t\t},\n");
          out.write("\t\t\t\tformatter: TreeUtil.formatJSONResults,\n");
          out.write("\t\t\t\turl: getLayoutsURL\n");
          out.write("\t\t\t},\n");
          out.write("\t\t\ton: {\n");
          out.write("\t\t\t\tappend: function(event) {\n");
          out.write("\t\t\t\t\tTreeUtil.restoreNodeState(event.tree.node);\n");
          out.write("\t\t\t\t},\n");
          out.write("\t\t\t\tdrop: function(event) {\n");
          out.write("\t\t\t\t\tvar tree = event.tree;\n");
          out.write("\n");
          out.write("\t\t\t\t\tvar index = tree.dragNode.get('parentNode').indexOf(tree.dragNode);\n");
          out.write("\n");
          out.write("\t\t\t\t\tTreeUtil.updateLayoutPriority(\n");
          out.write("\t\t\t\t\t\tTreeUtil.extractPlid(tree.dragNode),\n");
          out.write("\t\t\t\t\t\tindex\n");
          out.write("\t\t\t\t\t);\n");
          out.write("\t\t\t\t},\n");
          out.write("\t\t\t\tdropAppend: function(event) {\n");
          out.write("\t\t\t\t\tvar tree = event.tree;\n");
          out.write("\n");
          out.write("\t\t\t\t\tTreeUtil.updateLayoutParent(\n");
          out.write("\t\t\t\t\t\tTreeUtil.extractPlid(tree.dragNode),\n");
          out.write("\t\t\t\t\t\tTreeUtil.extractPlid(tree.dropNode)\n");
          out.write("\t\t\t\t\t);\n");
          out.write("\t\t\t\t},\n");
          out.write("\t\t\t\tdropInsert: function(event) {\n");
          out.write("\t\t\t\t\tvar tree = event.tree;\n");
          out.write("\n");
          out.write("\t\t\t\t\tTreeUtil.updateLayoutParent(\n");
          out.write("\t\t\t\t\t\tTreeUtil.extractPlid(tree.dragNode),\n");
          out.write("\t\t\t\t\t\tTreeUtil.extractPlid(tree.dropNode.get('parentNode'))\n");
          out.write("\t\t\t\t\t);\n");
          out.write("\t\t\t\t}\n");
          out.write("\t\t\t},\n");
          out.write("\t\t\ttype: 'pages'\n");
          out.write("\t\t}\n");
          out.write("\t).render();\n");
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

  private boolean _jspx_meth_portlet_005fnamespace_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f0 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f0.setParent(null);
    int _jspx_eval_portlet_005fnamespace_005f0 = _jspx_th_portlet_005fnamespace_005f0.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f1 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f1.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f1.setParent(null);
    int _jspx_eval_portlet_005fnamespace_005f1 = _jspx_th_portlet_005fnamespace_005f1.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f1);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f2 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f2.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f2 = _jspx_th_portlet_005fnamespace_005f2.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f2);
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
}
