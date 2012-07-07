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

public final class view_jsp extends org.apache.jasper.runtime.HttpJspBase
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


private static Log _log = LogFactoryUtil.getLog("portal-web.docroot.html.portlet.workflow_tasks.view.jsp");

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(30);
    _jspx_dependants.add("/html/portlet/workflow_tasks/init.jsp");
    _jspx_dependants.add("/html/portlet/init.jsp");
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/workflow_tasks/view_workflow_tasks.jspf");
    _jspx_dependants.add("/html/portlet/workflow_tasks/workflow_tasks.jspf");
    _jspx_dependants.add("/html/portlet/workflow_tasks/workflow_search_results.jspf");
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
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005fportletURL_005fnames_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fpage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_0026_005fsearchContainer;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dresults_0026_005ftotal_005fresults_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_0026_005fstringKey_005fmodelVar_005fclassName;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fbuffer;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005forderable_005fname_005fhref_005fbuffer;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_0026_005fpath_005falign_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005fportletURL_005fnames_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fpage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_0026_005fsearchContainer = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dresults_0026_005ftotal_005fresults_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_0026_005fstringKey_005fmodelVar_005fclassName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fbuffer = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005forderable_005fname_005fhref_005fbuffer = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_0026_005fpath_005falign_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005fportletURL_005fnames_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fpage_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_0026_005fsearchContainer.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dresults_0026_005ftotal_005fresults_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_0026_005fstringKey_005fmodelVar_005fclassName.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.release();
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fbuffer.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005forderable_005fname_005fhref_005fbuffer.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_0026_005fpath_005falign_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fotherwise.release();
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
          // /html/portlet/workflow_tasks/view.jsp(20,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fwhen_005f0.setTest( WorkflowEngineManagerUtil.isDeployed() );
          int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
          if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
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

String tabs1 = ParamUtil.getString(request, "tabs1", "pending");

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("tabs1", tabs1);

              out.write('\n');
              out.write('\n');
              //  liferay-ui:tabs
              com.liferay.taglib.ui.TabsTag _jspx_th_liferay_002dui_005ftabs_005f0 = (com.liferay.taglib.ui.TabsTag) _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005fportletURL_005fnames_005fnobody.get(com.liferay.taglib.ui.TabsTag.class);
              _jspx_th_liferay_002dui_005ftabs_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005ftabs_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(25,0) name = names type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ftabs_005f0.setNames("pending,completed");
              // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(25,0) name = portletURL type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ftabs_005f0.setPortletURL( portletURL );
              int _jspx_eval_liferay_002dui_005ftabs_005f0 = _jspx_th_liferay_002dui_005ftabs_005f0.doStartTag();
              if (_jspx_th_liferay_002dui_005ftabs_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005fportletURL_005fnames_005fnobody.reuse(_jspx_th_liferay_002dui_005ftabs_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005fportletURL_005fnames_005fnobody.reuse(_jspx_th_liferay_002dui_005ftabs_005f0);
              out.write('\n');
              out.write('\n');

try {
	String type = "completed";

              out.write('\n');
              out.write('\n');
              out.write('	');
              //  aui:form
              com.liferay.taglib.aui.FormTag _jspx_th_aui_005fform_005f0 = (com.liferay.taglib.aui.FormTag) _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction.get(com.liferay.taglib.aui.FormTag.class);
              _jspx_th_aui_005fform_005f0.setPageContext(_jspx_page_context);
              _jspx_th_aui_005fform_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(35,1) name = action type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005fform_005f0.setAction( portletURL.toString() );
              // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(35,1) null
              _jspx_th_aui_005fform_005f0.setDynamicAttribute(null, "method", new String("post"));
              // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(35,1) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
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
                  out.write('	');
                  //  c:choose
                  org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                  _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fchoose_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
                  int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
                  if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t");
                      //  c:when
                      org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f1 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                      _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                      // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(37,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fwhen_005f1.setTest( portletName.equals(PortletKeys.MY_WORKFLOW_TASKS) && tabs1.equals("pending") );
                      int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
                      if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t");
                          if (_jspx_meth_liferay_002dui_005fsearch_002dform_005f0(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t<div class=\"separator\" />\n");
                          out.write("\n");
                          out.write("\t\t\t\t");
                          //  liferay-ui:panel-container
                          com.liferay.taglib.ui.PanelContainerTag _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0 = (com.liferay.taglib.ui.PanelContainerTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended.get(com.liferay.taglib.ui.PanelContainerTag.class);
                          _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                          // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(44,4) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setExtended( false );
                          // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(44,4) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setId("workflowTasksPanelContainer");
                          // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(44,4) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
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
                              out.write("\t\t\t\t\t");
                              //  liferay-ui:panel
                              com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f0 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.get(com.liferay.taglib.ui.PanelTag.class);
                              _jspx_th_liferay_002dui_005fpanel_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fpanel_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
                              // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(45,5) name = collapsible type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fpanel_005f0.setCollapsible( true );
                              // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(45,5) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fpanel_005f0.setExtended( false );
                              // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(45,5) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fpanel_005f0.setId("myTasksPanel");
                              // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(45,5) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fpanel_005f0.setPersistState( true );
                              // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(45,5) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fpanel_005f0.setTitle( LanguageUtil.get(pageContext, "assigned-to-me") );
                              int _jspx_eval_liferay_002dui_005fpanel_005f0 = _jspx_th_liferay_002dui_005fpanel_005f0.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fpanel_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_liferay_002dui_005fpanel_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fpanel_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fpanel_005f0.doInitBody();
                              }
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");

						type = "assigned-to-me";
						
                              out.write("\n");
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
                              //  liferay-ui:search-container
                              com.liferay.taglib.ui.SearchContainerTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f0 = (com.liferay.taglib.ui.SearchContainerTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_0026_005fsearchContainer.get(com.liferay.taglib.ui.SearchContainerTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f0);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(17,0) name = searchContainer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f0.setSearchContainer( new WorkflowTaskSearch(renderRequest, portletURL) );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_005f0 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f0.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              com.liferay.portal.kernel.dao.search.SearchContainer searchContainer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f0.doInitBody();
                              }
                              searchContainer = (com.liferay.portal.kernel.dao.search.SearchContainer) _jspx_page_context.findAttribute("searchContainer");
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

                              out.write('\n');
                              out.write('\n');

WorkflowTaskSearchTerms searchTerms = (WorkflowTaskSearchTerms)searchContainer.getSearchTerms();

boolean allTasks = false;

List<WorkflowTask> resultsTasks = null;

int totalTasks = 0;

if (portletName.equals(PortletKeys.WORKFLOW_TASKS)) {
	searchContainer.setEmptyResultsMessage("there-are-no-tasks");

	allTasks = true;

	if (searchTerms.isAdvancedSearch()){
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(), 0, searchTerms.getName(), searchTerms.getType(), null, null, null, null, searchTerms.isAndOperator(), searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(), 0, searchTerms.getName(), searchTerms.getType(), null, null, null, null, searchTerms.isAndOperator());
	}
	else {
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(), 0, searchTerms.getKeywords(), null, null, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(), 0, searchTerms.getKeywords(), null, null);
	}
}
else if (type.equals("assigned-to-my-roles")) {
	searchContainer.setEmptyResultsMessage("there-are-no-pending-tasks-assigned-to-your-roles");

	if (searchTerms.isAdvancedSearch()){
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(),  user.getUserId(), searchTerms.getName(), searchTerms.getType(), null, null, false, true, searchTerms.isAndOperator(), searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(),  user.getUserId(), searchTerms.getName(), searchTerms.getType(), null, null, false, true, searchTerms.isAndOperator());
	}
	else {
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(), user.getUserId(), searchTerms.getKeywords(), false, true, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(), user.getUserId(), searchTerms.getKeywords(), false, true);
	}
}
else {
	boolean completedTasks = false;

	if (type.equals("completed")) {
		completedTasks = true;

		searchContainer.setEmptyResultsMessage("there-are-no-completed-tasks");
	}
	else {
		searchContainer.setEmptyResultsMessage("there-are-no-pending-tasks-assigned-to-you");
	}

	if (searchTerms.isAdvancedSearch()){
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(),  user.getUserId(), searchTerms.getName(), searchTerms.getType(), null, null, completedTasks, false, searchTerms.isAndOperator(), searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(), user.getUserId(), searchTerms.getName(), searchTerms.getType(), null, null, completedTasks, false, searchTerms.isAndOperator());
	}
	else {
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(), user.getUserId(), searchTerms.getKeywords(), completedTasks, false, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(), user.getUserId(), searchTerms.getKeywords(), completedTasks, false);
	}
}

if (Validator.isNotNull(searchTerms.getKeywords()) || Validator.isNotNull(searchTerms.getName()) || Validator.isNotNull(searchTerms.getType())) {
	searchContainer.setEmptyResultsMessage(searchContainer.getEmptyResultsMessage() + "-with-the-specified-search-criteria");
}

                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              //  liferay-ui:search-container-results
                              java.util.List results = null;
                              java.lang.Integer total = null;
                              com.liferay.taglib.ui.SearchContainerResultsTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f0 = (com.liferay.taglib.ui.SearchContainerResultsTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dresults_0026_005ftotal_005fresults_005fnobody.get(com.liferay.taglib.ui.SearchContainerResultsTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f0);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(22,1) name = results type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f0.setResults( resultsTasks );
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(22,1) name = total type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f0.setTotal( totalTasks );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dresults_005f0 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f0.doStartTag();
                              results = (java.util.List) _jspx_page_context.findAttribute("results");
                              total = (java.lang.Integer) _jspx_page_context.findAttribute("total");
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dresults_0026_005ftotal_005fresults_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f0);
                              return;
                              }
                              results = (java.util.List) _jspx_page_context.findAttribute("results");
                              total = (java.lang.Integer) _jspx_page_context.findAttribute("total");
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dresults_0026_005ftotal_005fresults_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f0);
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              //  liferay-ui:search-container-row
                              com.liferay.taglib.ui.SearchContainerRowTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0 = (com.liferay.taglib.ui.SearchContainerRowTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_0026_005fstringKey_005fmodelVar_005fclassName.get(com.liferay.taglib.ui.SearchContainerRowTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f0);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(27,1) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0.setClassName("com.liferay.portal.kernel.workflow.WorkflowTask");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(27,1) name = modelVar type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0.setModelVar("workflowTask");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(27,1) name = stringKey type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0.setStringKey( true );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002drow_005f0 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002drow_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.Integer index = null;
                              com.liferay.portal.kernel.workflow.WorkflowTask workflowTask = null;
                              com.liferay.portal.kernel.dao.search.ResultRow row = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002drow_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0.doInitBody();
                              }
                              index = (java.lang.Integer) _jspx_page_context.findAttribute("index");
                              workflowTask = (com.liferay.portal.kernel.workflow.WorkflowTask) _jspx_page_context.findAttribute("workflowTask");
                              row = (com.liferay.portal.kernel.dao.search.ResultRow) _jspx_page_context.findAttribute("row");
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  liferay-ui:search-container-row-parameter
                              com.liferay.taglib.ui.SearchContainerRowParameterTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f0 = (com.liferay.taglib.ui.SearchContainerRowParameterTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.ui.SearchContainerRowParameterTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(32,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f0.setName("workflowTask");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(32,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f0.setValue( workflowTask );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f0 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f0.doStartTag();
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f0);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f0);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");

		WorkflowInstance workflowInstance = WorkflowInstanceManagerUtil.getWorkflowInstance(company.getCompanyId(), workflowTask.getWorkflowInstanceId());

		Map<String, Serializable> workflowContext = workflowInstance.getWorkflowContext();

		long companyId = GetterUtil.getLong((String)workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID));
		long groupId = GetterUtil.getLong((String)workflowContext.get(WorkflowConstants.CONTEXT_GROUP_ID));
		String className = (String)workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_NAME);
		long classPK = GetterUtil.getLong((String)workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK));

		WorkflowHandler workflowHandler = WorkflowHandlerRegistryUtil.getWorkflowHandler(className);
		
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  portlet:renderURL
                              com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f0 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                              _jspx_th_portlet_005frenderURL_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005frenderURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(50,2) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005frenderURL_005f0.setVar("rowURL");
                              int _jspx_eval_portlet_005frenderURL_005f0 = _jspx_th_portlet_005frenderURL_005f0.doStartTag();
                              if (_jspx_eval_portlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_portlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_portlet_005frenderURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_portlet_005frenderURL_005f0.doInitBody();
                              }
                              do {
                              out.write("\n");
                              out.write("\t\t\t");
                              if (_jspx_meth_portlet_005fparam_005f0(_jspx_th_portlet_005frenderURL_005f0, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t");
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f1 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(52,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f1.setName("redirect");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(52,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f1.setValue( currentURL );
                              int _jspx_eval_portlet_005fparam_005f1 = _jspx_th_portlet_005fparam_005f1.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                              out.write("\n");
                              out.write("\t\t\t");
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f2 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(53,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f2.setName("workflowTaskId");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(53,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f2.setValue( String.valueOf(workflowTask.getWorkflowTaskId()) );
                              int _jspx_eval_portlet_005fparam_005f2 = _jspx_th_portlet_005fparam_005f2.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f0.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_portlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_portlet_005frenderURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f0);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f0);
                              java.lang.String rowURL = null;
                              rowURL = (java.lang.String) _jspx_page_context.findAttribute("rowURL");
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(56,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f0.setTest( allTasks );
                              int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
                              if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t");
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f0 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(57,3) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f0.setBuffer("buffer");
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f0 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f0.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f0.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t");

				if (workflowTask.isAssignedToSingleUser() && (workflowTask.getAssigneeUserId() > 0)) {
					User assigneeUser = UserLocalServiceUtil.getUser(workflowTask.getAssigneeUserId());

					buffer.append("<img alt=\"");
					buffer.append(HtmlUtil.escape(assigneeUser.getFullName()));
					buffer.append("\" class=\"user-avatar\" src=\"");
					buffer.append(themeDisplay.getPathImage());
					buffer.append("/user_");

					if (assigneeUser.isFemale()) {
						buffer.append("female");
					}
					else {
						buffer.append("male");
					}

					buffer.append("_portrait?img_id=");
					buffer.append(assigneeUser.getPortraitId());
					buffer.append("&t=");
					buffer.append(ImageServletTokenUtil.getToken(assigneeUser.getPortraitId()));
					buffer.append("\" title=\"");
					buffer.append(HtmlUtil.escape(assigneeUser.getFullName()));
					buffer.append("\" />");
				}
				
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f0.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f0);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f0);
                              out.write('\n');
                              out.write('	');
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
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f1 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(91,2) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f1.setBuffer("buffer");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(91,2) name = href type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f1.setHref( rowURL );
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(91,2) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f1.setName("task");
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f1 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f1.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f1.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

			buffer.append("<span class=\"task-name\" id=\"");
			buffer.append(workflowTask.getWorkflowTaskId());
			buffer.append("\">");
			buffer.append(LanguageUtil.get(pageContext, workflowTask.getName()));
			buffer.append("</span>");
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f1.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f1);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f2 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(107,2) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f2.setBuffer("buffer");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(107,2) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f2.setName("asset-title");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(107,2) name = href type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f2.setHref( rowURL );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f2 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f2.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f2.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

			buffer.append(workflowHandler.getTitle(classPK));
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f2.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f2);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f3 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(119,2) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f3.setBuffer("buffer");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(119,2) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f3.setName("asset-type");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(119,2) name = href type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f3.setHref( rowURL );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f3 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f3.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f3.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

			buffer.append(workflowHandler.getType(locale));
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f3.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f3);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f3);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f4 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(131,2) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f4.setBuffer("buffer");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(131,2) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f4.setName("last-activity-date");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(131,2) name = href type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f4.setHref( rowURL );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f4 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f4.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f4.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

			List<WorkflowLog> workflowLogs = WorkflowLogManagerUtil.getWorkflowLogsByWorkflowInstance(company.getCompanyId(), workflowTask.getWorkflowInstanceId(), null, 0, 1, WorkflowComparatorFactoryUtil.getLogCreateDateComparator());

			if (workflowLogs.isEmpty()) {
				buffer.append(LanguageUtil.get(pageContext, "never"));
			}
			else {
				WorkflowLog workflowLog = workflowLogs.get(0);

				buffer.append(dateFormatDateTime.format(workflowLog.getCreateDate()));
			}
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f4.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f4);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f4);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005forderable_005fname_005fhref_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(152,2) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5.setBuffer("buffer");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(152,2) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5.setName("due-date");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(152,2) name = href type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5.setHref( rowURL );
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(152,2) name = orderable type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5.setOrderable( true );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

			if (workflowTask.getDueDate() == null) {
				buffer.append(LanguageUtil.get(pageContext, "never"));
			}
			else {
				buffer.append(dateFormatDateTime.format(workflowTask.getDueDate()));
			}
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005forderable_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005forderable_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f5);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(170,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f1.setTest( !allTasks );
                              int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
                              if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f0(_jspx_th_c_005fif_005f1, _jspx_page_context))
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
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0.doAfterBody();
                              index = (java.lang.Integer) _jspx_page_context.findAttribute("index");
                              workflowTask = (com.liferay.portal.kernel.workflow.WorkflowTask) _jspx_page_context.findAttribute("workflowTask");
                              row = (com.liferay.portal.kernel.dao.search.ResultRow) _jspx_page_context.findAttribute("row");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002drow_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_0026_005fstringKey_005fmodelVar_005fclassName.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_0026_005fstringKey_005fmodelVar_005fclassName.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f0);
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              if (_jspx_meth_liferay_002dui_005fsearch_002diterator_005f0(_jspx_th_liferay_002dui_005fsearch_002dcontainer_005f0, _jspx_page_context))
                              return;
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f0.doAfterBody();
                              searchContainer = (com.liferay.portal.kernel.dao.search.SearchContainer) _jspx_page_context.findAttribute("searchContainer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_0026_005fsearchContainer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_005f0);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_0026_005fsearchContainer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_005f0);
                              out.write("\n");
                              out.write("\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t");
                              //  liferay-ui:panel
                              com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f1 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.get(com.liferay.taglib.ui.PanelTag.class);
                              _jspx_th_liferay_002dui_005fpanel_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fpanel_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
                              // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(54,5) name = collapsible type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fpanel_005f1.setCollapsible( true );
                              // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(54,5) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fpanel_005f1.setExtended( false );
                              // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(54,5) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fpanel_005f1.setId("myRolesTasksPanel");
                              // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(54,5) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fpanel_005f1.setPersistState( true );
                              // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(54,5) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fpanel_005f1.setTitle( LanguageUtil.get(pageContext, "assigned-to-my-roles") );
                              int _jspx_eval_liferay_002dui_005fpanel_005f1 = _jspx_th_liferay_002dui_005fpanel_005f1.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fpanel_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_liferay_002dui_005fpanel_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fpanel_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fpanel_005f1.doInitBody();
                              }
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");

						type = "assigned-to-my-roles";
						
                              out.write("\n");
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
                              //  liferay-ui:search-container
                              com.liferay.taglib.ui.SearchContainerTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f1 = (com.liferay.taglib.ui.SearchContainerTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_0026_005fsearchContainer.get(com.liferay.taglib.ui.SearchContainerTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f1);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(17,0) name = searchContainer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f1.setSearchContainer( new WorkflowTaskSearch(renderRequest, portletURL) );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_005f1 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f1.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              com.liferay.portal.kernel.dao.search.SearchContainer searchContainer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f1.doInitBody();
                              }
                              searchContainer = (com.liferay.portal.kernel.dao.search.SearchContainer) _jspx_page_context.findAttribute("searchContainer");
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

                              out.write('\n');
                              out.write('\n');

WorkflowTaskSearchTerms searchTerms = (WorkflowTaskSearchTerms)searchContainer.getSearchTerms();

boolean allTasks = false;

List<WorkflowTask> resultsTasks = null;

int totalTasks = 0;

if (portletName.equals(PortletKeys.WORKFLOW_TASKS)) {
	searchContainer.setEmptyResultsMessage("there-are-no-tasks");

	allTasks = true;

	if (searchTerms.isAdvancedSearch()){
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(), 0, searchTerms.getName(), searchTerms.getType(), null, null, null, null, searchTerms.isAndOperator(), searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(), 0, searchTerms.getName(), searchTerms.getType(), null, null, null, null, searchTerms.isAndOperator());
	}
	else {
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(), 0, searchTerms.getKeywords(), null, null, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(), 0, searchTerms.getKeywords(), null, null);
	}
}
else if (type.equals("assigned-to-my-roles")) {
	searchContainer.setEmptyResultsMessage("there-are-no-pending-tasks-assigned-to-your-roles");

	if (searchTerms.isAdvancedSearch()){
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(),  user.getUserId(), searchTerms.getName(), searchTerms.getType(), null, null, false, true, searchTerms.isAndOperator(), searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(),  user.getUserId(), searchTerms.getName(), searchTerms.getType(), null, null, false, true, searchTerms.isAndOperator());
	}
	else {
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(), user.getUserId(), searchTerms.getKeywords(), false, true, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(), user.getUserId(), searchTerms.getKeywords(), false, true);
	}
}
else {
	boolean completedTasks = false;

	if (type.equals("completed")) {
		completedTasks = true;

		searchContainer.setEmptyResultsMessage("there-are-no-completed-tasks");
	}
	else {
		searchContainer.setEmptyResultsMessage("there-are-no-pending-tasks-assigned-to-you");
	}

	if (searchTerms.isAdvancedSearch()){
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(),  user.getUserId(), searchTerms.getName(), searchTerms.getType(), null, null, completedTasks, false, searchTerms.isAndOperator(), searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(), user.getUserId(), searchTerms.getName(), searchTerms.getType(), null, null, completedTasks, false, searchTerms.isAndOperator());
	}
	else {
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(), user.getUserId(), searchTerms.getKeywords(), completedTasks, false, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(), user.getUserId(), searchTerms.getKeywords(), completedTasks, false);
	}
}

if (Validator.isNotNull(searchTerms.getKeywords()) || Validator.isNotNull(searchTerms.getName()) || Validator.isNotNull(searchTerms.getType())) {
	searchContainer.setEmptyResultsMessage(searchContainer.getEmptyResultsMessage() + "-with-the-specified-search-criteria");
}

                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              //  liferay-ui:search-container-results
                              java.util.List results = null;
                              java.lang.Integer total = null;
                              com.liferay.taglib.ui.SearchContainerResultsTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f1 = (com.liferay.taglib.ui.SearchContainerResultsTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dresults_0026_005ftotal_005fresults_005fnobody.get(com.liferay.taglib.ui.SearchContainerResultsTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f1);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(22,1) name = results type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f1.setResults( resultsTasks );
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(22,1) name = total type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f1.setTotal( totalTasks );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dresults_005f1 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f1.doStartTag();
                              results = (java.util.List) _jspx_page_context.findAttribute("results");
                              total = (java.lang.Integer) _jspx_page_context.findAttribute("total");
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dresults_0026_005ftotal_005fresults_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f1);
                              return;
                              }
                              results = (java.util.List) _jspx_page_context.findAttribute("results");
                              total = (java.lang.Integer) _jspx_page_context.findAttribute("total");
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dresults_0026_005ftotal_005fresults_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f1);
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              //  liferay-ui:search-container-row
                              com.liferay.taglib.ui.SearchContainerRowTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1 = (com.liferay.taglib.ui.SearchContainerRowTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_0026_005fstringKey_005fmodelVar_005fclassName.get(com.liferay.taglib.ui.SearchContainerRowTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f1);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(27,1) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1.setClassName("com.liferay.portal.kernel.workflow.WorkflowTask");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(27,1) name = modelVar type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1.setModelVar("workflowTask");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(27,1) name = stringKey type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1.setStringKey( true );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002drow_005f1 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002drow_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.Integer index = null;
                              com.liferay.portal.kernel.workflow.WorkflowTask workflowTask = null;
                              com.liferay.portal.kernel.dao.search.ResultRow row = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002drow_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1.doInitBody();
                              }
                              index = (java.lang.Integer) _jspx_page_context.findAttribute("index");
                              workflowTask = (com.liferay.portal.kernel.workflow.WorkflowTask) _jspx_page_context.findAttribute("workflowTask");
                              row = (com.liferay.portal.kernel.dao.search.ResultRow) _jspx_page_context.findAttribute("row");
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  liferay-ui:search-container-row-parameter
                              com.liferay.taglib.ui.SearchContainerRowParameterTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f1 = (com.liferay.taglib.ui.SearchContainerRowParameterTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.ui.SearchContainerRowParameterTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(32,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f1.setName("workflowTask");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(32,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f1.setValue( workflowTask );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f1 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f1.doStartTag();
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f1);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");

		WorkflowInstance workflowInstance = WorkflowInstanceManagerUtil.getWorkflowInstance(company.getCompanyId(), workflowTask.getWorkflowInstanceId());

		Map<String, Serializable> workflowContext = workflowInstance.getWorkflowContext();

		long companyId = GetterUtil.getLong((String)workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID));
		long groupId = GetterUtil.getLong((String)workflowContext.get(WorkflowConstants.CONTEXT_GROUP_ID));
		String className = (String)workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_NAME);
		long classPK = GetterUtil.getLong((String)workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK));

		WorkflowHandler workflowHandler = WorkflowHandlerRegistryUtil.getWorkflowHandler(className);
		
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  portlet:renderURL
                              com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f1 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                              _jspx_th_portlet_005frenderURL_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005frenderURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(50,2) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005frenderURL_005f1.setVar("rowURL");
                              int _jspx_eval_portlet_005frenderURL_005f1 = _jspx_th_portlet_005frenderURL_005f1.doStartTag();
                              if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_portlet_005frenderURL_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_portlet_005frenderURL_005f1.doInitBody();
                              }
                              do {
                              out.write("\n");
                              out.write("\t\t\t");
                              if (_jspx_meth_portlet_005fparam_005f3(_jspx_th_portlet_005frenderURL_005f1, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t");
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f4 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(52,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f4.setName("redirect");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(52,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f4.setValue( currentURL );
                              int _jspx_eval_portlet_005fparam_005f4 = _jspx_th_portlet_005fparam_005f4.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
                              out.write("\n");
                              out.write("\t\t\t");
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f5 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f5.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(53,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f5.setName("workflowTaskId");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(53,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f5.setValue( String.valueOf(workflowTask.getWorkflowTaskId()) );
                              int _jspx_eval_portlet_005fparam_005f5 = _jspx_th_portlet_005fparam_005f5.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f1.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_portlet_005frenderURL_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f1);
                              java.lang.String rowURL = null;
                              rowURL = (java.lang.String) _jspx_page_context.findAttribute("rowURL");
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(56,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f2.setTest( allTasks );
                              int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
                              if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t");
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f6 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f6.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(57,3) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f6.setBuffer("buffer");
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f6 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f6.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f6.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t");

				if (workflowTask.isAssignedToSingleUser() && (workflowTask.getAssigneeUserId() > 0)) {
					User assigneeUser = UserLocalServiceUtil.getUser(workflowTask.getAssigneeUserId());

					buffer.append("<img alt=\"");
					buffer.append(HtmlUtil.escape(assigneeUser.getFullName()));
					buffer.append("\" class=\"user-avatar\" src=\"");
					buffer.append(themeDisplay.getPathImage());
					buffer.append("/user_");

					if (assigneeUser.isFemale()) {
						buffer.append("female");
					}
					else {
						buffer.append("male");
					}

					buffer.append("_portrait?img_id=");
					buffer.append(assigneeUser.getPortraitId());
					buffer.append("&t=");
					buffer.append(ImageServletTokenUtil.getToken(assigneeUser.getPortraitId()));
					buffer.append("\" title=\"");
					buffer.append(HtmlUtil.escape(assigneeUser.getFullName()));
					buffer.append("\" />");
				}
				
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f6.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f6);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f6);
                              out.write('\n');
                              out.write('	');
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
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f7 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f7.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(91,2) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f7.setBuffer("buffer");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(91,2) name = href type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f7.setHref( rowURL );
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(91,2) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f7.setName("task");
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f7 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f7.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f7.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

			buffer.append("<span class=\"task-name\" id=\"");
			buffer.append(workflowTask.getWorkflowTaskId());
			buffer.append("\">");
			buffer.append(LanguageUtil.get(pageContext, workflowTask.getName()));
			buffer.append("</span>");
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f7.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f7);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f7);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f8 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f8.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(107,2) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f8.setBuffer("buffer");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(107,2) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f8.setName("asset-title");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(107,2) name = href type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f8.setHref( rowURL );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f8 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f8.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f8.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

			buffer.append(workflowHandler.getTitle(classPK));
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f8.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f8);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f8);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f9 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f9.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(119,2) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f9.setBuffer("buffer");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(119,2) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f9.setName("asset-type");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(119,2) name = href type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f9.setHref( rowURL );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f9 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f9.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f9.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

			buffer.append(workflowHandler.getType(locale));
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f9.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f9);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f9);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f10 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f10.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(131,2) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f10.setBuffer("buffer");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(131,2) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f10.setName("last-activity-date");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(131,2) name = href type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f10.setHref( rowURL );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f10 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f10.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f10.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

			List<WorkflowLog> workflowLogs = WorkflowLogManagerUtil.getWorkflowLogsByWorkflowInstance(company.getCompanyId(), workflowTask.getWorkflowInstanceId(), null, 0, 1, WorkflowComparatorFactoryUtil.getLogCreateDateComparator());

			if (workflowLogs.isEmpty()) {
				buffer.append(LanguageUtil.get(pageContext, "never"));
			}
			else {
				WorkflowLog workflowLog = workflowLogs.get(0);

				buffer.append(dateFormatDateTime.format(workflowLog.getCreateDate()));
			}
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f10.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f10);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f10);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005forderable_005fname_005fhref_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(152,2) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11.setBuffer("buffer");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(152,2) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11.setName("due-date");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(152,2) name = href type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11.setHref( rowURL );
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(152,2) name = orderable type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11.setOrderable( true );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

			if (workflowTask.getDueDate() == null) {
				buffer.append(LanguageUtil.get(pageContext, "never"));
			}
			else {
				buffer.append(dateFormatDateTime.format(workflowTask.getDueDate()));
			}
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005forderable_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005forderable_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f11);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(170,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f3.setTest( !allTasks );
                              int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
                              if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f1(_jspx_th_c_005fif_005f3, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
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
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1.doAfterBody();
                              index = (java.lang.Integer) _jspx_page_context.findAttribute("index");
                              workflowTask = (com.liferay.portal.kernel.workflow.WorkflowTask) _jspx_page_context.findAttribute("workflowTask");
                              row = (com.liferay.portal.kernel.dao.search.ResultRow) _jspx_page_context.findAttribute("row");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002drow_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_0026_005fstringKey_005fmodelVar_005fclassName.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_0026_005fstringKey_005fmodelVar_005fclassName.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f1);
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              if (_jspx_meth_liferay_002dui_005fsearch_002diterator_005f1(_jspx_th_liferay_002dui_005fsearch_002dcontainer_005f1, _jspx_page_context))
                              return;
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f1.doAfterBody();
                              searchContainer = (com.liferay.portal.kernel.dao.search.SearchContainer) _jspx_page_context.findAttribute("searchContainer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_0026_005fsearchContainer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_0026_005fsearchContainer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_005f1);
                              out.write("\n");
                              out.write("\t\t\t\t\t");
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
                              out.write("\t\t\t\t");
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
                          out.write("\t\t\t");
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
                      out.write("\t\t\t");
                      //  c:otherwise
                      org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                      _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                      int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
                      if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t");
                          if (_jspx_meth_liferay_002dui_005fsearch_002dform_005f1(_jspx_th_c_005fotherwise_005f0, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t<div class=\"separator\" />\n");
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
                          //  liferay-ui:search-container
                          com.liferay.taglib.ui.SearchContainerTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f2 = (com.liferay.taglib.ui.SearchContainerTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_0026_005fsearchContainer.get(com.liferay.taglib.ui.SearchContainerTag.class);
                          _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f2.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
                          // /html/portlet/workflow_tasks/workflow_tasks.jspf(17,0) name = searchContainer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f2.setSearchContainer( new WorkflowTaskSearch(renderRequest, portletURL) );
                          int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_005f2 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f2.doStartTag();
                          if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            com.liferay.portal.kernel.dao.search.SearchContainer searchContainer = null;
                            if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f2.doInitBody();
                            }
                            searchContainer = (com.liferay.portal.kernel.dao.search.SearchContainer) _jspx_page_context.findAttribute("searchContainer");
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

                              out.write('\n');
                              out.write('\n');

WorkflowTaskSearchTerms searchTerms = (WorkflowTaskSearchTerms)searchContainer.getSearchTerms();

boolean allTasks = false;

List<WorkflowTask> resultsTasks = null;

int totalTasks = 0;

if (portletName.equals(PortletKeys.WORKFLOW_TASKS)) {
	searchContainer.setEmptyResultsMessage("there-are-no-tasks");

	allTasks = true;

	if (searchTerms.isAdvancedSearch()){
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(), 0, searchTerms.getName(), searchTerms.getType(), null, null, null, null, searchTerms.isAndOperator(), searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(), 0, searchTerms.getName(), searchTerms.getType(), null, null, null, null, searchTerms.isAndOperator());
	}
	else {
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(), 0, searchTerms.getKeywords(), null, null, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(), 0, searchTerms.getKeywords(), null, null);
	}
}
else if (type.equals("assigned-to-my-roles")) {
	searchContainer.setEmptyResultsMessage("there-are-no-pending-tasks-assigned-to-your-roles");

	if (searchTerms.isAdvancedSearch()){
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(),  user.getUserId(), searchTerms.getName(), searchTerms.getType(), null, null, false, true, searchTerms.isAndOperator(), searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(),  user.getUserId(), searchTerms.getName(), searchTerms.getType(), null, null, false, true, searchTerms.isAndOperator());
	}
	else {
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(), user.getUserId(), searchTerms.getKeywords(), false, true, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(), user.getUserId(), searchTerms.getKeywords(), false, true);
	}
}
else {
	boolean completedTasks = false;

	if (type.equals("completed")) {
		completedTasks = true;

		searchContainer.setEmptyResultsMessage("there-are-no-completed-tasks");
	}
	else {
		searchContainer.setEmptyResultsMessage("there-are-no-pending-tasks-assigned-to-you");
	}

	if (searchTerms.isAdvancedSearch()){
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(),  user.getUserId(), searchTerms.getName(), searchTerms.getType(), null, null, completedTasks, false, searchTerms.isAndOperator(), searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(), user.getUserId(), searchTerms.getName(), searchTerms.getType(), null, null, completedTasks, false, searchTerms.isAndOperator());
	}
	else {
		resultsTasks = WorkflowTaskManagerUtil.search(company.getCompanyId(), user.getUserId(), searchTerms.getKeywords(), completedTasks, false, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
		totalTasks = WorkflowTaskManagerUtil.searchCount(company.getCompanyId(), user.getUserId(), searchTerms.getKeywords(), completedTasks, false);
	}
}

if (Validator.isNotNull(searchTerms.getKeywords()) || Validator.isNotNull(searchTerms.getName()) || Validator.isNotNull(searchTerms.getType())) {
	searchContainer.setEmptyResultsMessage(searchContainer.getEmptyResultsMessage() + "-with-the-specified-search-criteria");
}

                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              //  liferay-ui:search-container-results
                              java.util.List results = null;
                              java.lang.Integer total = null;
                              com.liferay.taglib.ui.SearchContainerResultsTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f2 = (com.liferay.taglib.ui.SearchContainerResultsTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dresults_0026_005ftotal_005fresults_005fnobody.get(com.liferay.taglib.ui.SearchContainerResultsTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f2);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(22,1) name = results type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f2.setResults( resultsTasks );
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(22,1) name = total type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f2.setTotal( totalTasks );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dresults_005f2 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f2.doStartTag();
                              results = (java.util.List) _jspx_page_context.findAttribute("results");
                              total = (java.lang.Integer) _jspx_page_context.findAttribute("total");
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dresults_0026_005ftotal_005fresults_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f2);
                              return;
                              }
                              results = (java.util.List) _jspx_page_context.findAttribute("results");
                              total = (java.lang.Integer) _jspx_page_context.findAttribute("total");
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dresults_0026_005ftotal_005fresults_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dresults_005f2);
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              //  liferay-ui:search-container-row
                              com.liferay.taglib.ui.SearchContainerRowTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2 = (com.liferay.taglib.ui.SearchContainerRowTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_0026_005fstringKey_005fmodelVar_005fclassName.get(com.liferay.taglib.ui.SearchContainerRowTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f2);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(27,1) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2.setClassName("com.liferay.portal.kernel.workflow.WorkflowTask");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(27,1) name = modelVar type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2.setModelVar("workflowTask");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(27,1) name = stringKey type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2.setStringKey( true );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002drow_005f2 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002drow_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.Integer index = null;
                              com.liferay.portal.kernel.workflow.WorkflowTask workflowTask = null;
                              com.liferay.portal.kernel.dao.search.ResultRow row = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002drow_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2.doInitBody();
                              }
                              index = (java.lang.Integer) _jspx_page_context.findAttribute("index");
                              workflowTask = (com.liferay.portal.kernel.workflow.WorkflowTask) _jspx_page_context.findAttribute("workflowTask");
                              row = (com.liferay.portal.kernel.dao.search.ResultRow) _jspx_page_context.findAttribute("row");
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  liferay-ui:search-container-row-parameter
                              com.liferay.taglib.ui.SearchContainerRowParameterTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f2 = (com.liferay.taglib.ui.SearchContainerRowParameterTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.ui.SearchContainerRowParameterTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(32,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f2.setName("workflowTask");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(32,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f2.setValue( workflowTask );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f2 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f2.doStartTag();
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_002dparameter_005f2);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");

		WorkflowInstance workflowInstance = WorkflowInstanceManagerUtil.getWorkflowInstance(company.getCompanyId(), workflowTask.getWorkflowInstanceId());

		Map<String, Serializable> workflowContext = workflowInstance.getWorkflowContext();

		long companyId = GetterUtil.getLong((String)workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID));
		long groupId = GetterUtil.getLong((String)workflowContext.get(WorkflowConstants.CONTEXT_GROUP_ID));
		String className = (String)workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_NAME);
		long classPK = GetterUtil.getLong((String)workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK));

		WorkflowHandler workflowHandler = WorkflowHandlerRegistryUtil.getWorkflowHandler(className);
		
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  portlet:renderURL
                              com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f2 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                              _jspx_th_portlet_005frenderURL_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005frenderURL_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(50,2) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005frenderURL_005f2.setVar("rowURL");
                              int _jspx_eval_portlet_005frenderURL_005f2 = _jspx_th_portlet_005frenderURL_005f2.doStartTag();
                              if (_jspx_eval_portlet_005frenderURL_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_portlet_005frenderURL_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_portlet_005frenderURL_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_portlet_005frenderURL_005f2.doInitBody();
                              }
                              do {
                              out.write("\n");
                              out.write("\t\t\t");
                              if (_jspx_meth_portlet_005fparam_005f6(_jspx_th_portlet_005frenderURL_005f2, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t");
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f7 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f7.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f2);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(52,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f7.setName("redirect");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(52,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f7.setValue( currentURL );
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
                              _jspx_th_portlet_005fparam_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f2);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(53,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f8.setName("workflowTaskId");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(53,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f8.setValue( String.valueOf(workflowTask.getWorkflowTaskId()) );
                              int _jspx_eval_portlet_005fparam_005f8 = _jspx_th_portlet_005fparam_005f8.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f2.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_portlet_005frenderURL_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_portlet_005frenderURL_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f2);
                              java.lang.String rowURL = null;
                              rowURL = (java.lang.String) _jspx_page_context.findAttribute("rowURL");
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(56,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f4.setTest( allTasks );
                              int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
                              if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t");
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f12 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f12.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(57,3) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f12.setBuffer("buffer");
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f12 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f12.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f12.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f12.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t");

				if (workflowTask.isAssignedToSingleUser() && (workflowTask.getAssigneeUserId() > 0)) {
					User assigneeUser = UserLocalServiceUtil.getUser(workflowTask.getAssigneeUserId());

					buffer.append("<img alt=\"");
					buffer.append(HtmlUtil.escape(assigneeUser.getFullName()));
					buffer.append("\" class=\"user-avatar\" src=\"");
					buffer.append(themeDisplay.getPathImage());
					buffer.append("/user_");

					if (assigneeUser.isFemale()) {
						buffer.append("female");
					}
					else {
						buffer.append("male");
					}

					buffer.append("_portrait?img_id=");
					buffer.append(assigneeUser.getPortraitId());
					buffer.append("&t=");
					buffer.append(ImageServletTokenUtil.getToken(assigneeUser.getPortraitId()));
					buffer.append("\" title=\"");
					buffer.append(HtmlUtil.escape(assigneeUser.getFullName()));
					buffer.append("\" />");
				}
				
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f12.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f12);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f12);
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
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f13 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f13.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(91,2) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f13.setBuffer("buffer");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(91,2) name = href type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f13.setHref( rowURL );
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(91,2) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f13.setName("task");
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f13 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f13.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f13.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f13.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

			buffer.append("<span class=\"task-name\" id=\"");
			buffer.append(workflowTask.getWorkflowTaskId());
			buffer.append("\">");
			buffer.append(LanguageUtil.get(pageContext, workflowTask.getName()));
			buffer.append("</span>");
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f13.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f13);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f13);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f14 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f14.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(107,2) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f14.setBuffer("buffer");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(107,2) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f14.setName("asset-title");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(107,2) name = href type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f14.setHref( rowURL );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f14 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f14.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f14.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f14.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

			buffer.append(workflowHandler.getTitle(classPK));
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f14.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f14);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f14);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f15 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f15.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(119,2) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f15.setBuffer("buffer");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(119,2) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f15.setName("asset-type");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(119,2) name = href type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f15.setHref( rowURL );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f15 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f15.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f15 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f15.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f15.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

			buffer.append(workflowHandler.getType(locale));
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f15.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f15 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f15);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f15);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f16 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f16.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(131,2) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f16.setBuffer("buffer");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(131,2) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f16.setName("last-activity-date");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(131,2) name = href type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f16.setHref( rowURL );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f16 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f16.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f16 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f16 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f16.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f16.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

			List<WorkflowLog> workflowLogs = WorkflowLogManagerUtil.getWorkflowLogsByWorkflowInstance(company.getCompanyId(), workflowTask.getWorkflowInstanceId(), null, 0, 1, WorkflowComparatorFactoryUtil.getLogCreateDateComparator());

			if (workflowLogs.isEmpty()) {
				buffer.append(LanguageUtil.get(pageContext, "never"));
			}
			else {
				WorkflowLog workflowLog = workflowLogs.get(0);

				buffer.append(dateFormatDateTime.format(workflowLog.getCreateDate()));
			}
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f16.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f16 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f16);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f16);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  liferay-ui:search-container-column-text
                              com.liferay.taglib.ui.SearchContainerColumnTextTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17 = (com.liferay.taglib.ui.SearchContainerColumnTextTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005forderable_005fname_005fhref_005fbuffer.get(com.liferay.taglib.ui.SearchContainerColumnTextTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(152,2) name = buffer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17.setBuffer("buffer");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(152,2) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17.setName("due-date");
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(152,2) name = href type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17.setHref( rowURL );
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(152,2) name = orderable type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17.setOrderable( true );
                              int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              java.lang.StringBuilder buffer = null;
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17.doInitBody();
                              }
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

			if (workflowTask.getDueDate() == null) {
				buffer.append(LanguageUtil.get(pageContext, "never"));
			}
			else {
				buffer.append(dateFormatDateTime.format(workflowTask.getDueDate()));
			}
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17.doAfterBody();
                              buffer = (java.lang.StringBuilder) _jspx_page_context.findAttribute("buffer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005forderable_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_0026_005forderable_005fname_005fhref_005fbuffer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002dtext_005f17);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f5 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2);
                              // /html/portlet/workflow_tasks/workflow_tasks.jspf(170,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f5.setTest( !allTasks );
                              int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
                              if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f2(_jspx_th_c_005fif_005f5, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
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
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2.doAfterBody();
                              index = (java.lang.Integer) _jspx_page_context.findAttribute("index");
                              workflowTask = (com.liferay.portal.kernel.workflow.WorkflowTask) _jspx_page_context.findAttribute("workflowTask");
                              row = (com.liferay.portal.kernel.dao.search.ResultRow) _jspx_page_context.findAttribute("row");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_002drow_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_0026_005fstringKey_005fmodelVar_005fclassName.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002drow_0026_005fstringKey_005fmodelVar_005fclassName.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002drow_005f2);
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              if (_jspx_meth_liferay_002dui_005fsearch_002diterator_005f2(_jspx_th_liferay_002dui_005fsearch_002dcontainer_005f2, _jspx_page_context))
                              return;
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f2.doAfterBody();
                              searchContainer = (com.liferay.portal.kernel.dao.search.SearchContainer) _jspx_page_context.findAttribute("searchContainer");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_liferay_002dui_005fsearch_002dcontainer_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                            }
                          }
                          if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_0026_005fsearchContainer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_005f2);
                            return;
                          }
                          _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_0026_005fsearchContainer.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_005f2);
                          out.write("\n");
                          out.write("\t\t\t");
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
                      out.write('	');
                      out.write('	');
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
                  out.write('	');
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

}
catch (Exception e) {
	if (_log.isWarnEnabled()) {
		_log.warn("Error retrieving tasks for user " + user.getUserId(), e);
	}

              out.write("\n");
              out.write("\n");
              out.write("\t<div class=\"portlet-msg-error\">\n");
              out.write("\t\t");
              if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_th_c_005fwhen_005f0, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t</div>\n");
              out.write("\n");

}

PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(pageContext, tabs1), currentURL);

              out.write('\n');
              out.write('\n');
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
          if (_jspx_meth_c_005fotherwise_005f1(_jspx_th_c_005fchoose_005f0, _jspx_page_context))
            return;
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

  private boolean _jspx_meth_liferay_002dui_005fsearch_002dform_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:search-form
    com.liferay.taglib.ui.SearchFormTag _jspx_th_liferay_002dui_005fsearch_002dform_005f0 = (com.liferay.taglib.ui.SearchFormTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fpage_005fnobody.get(com.liferay.taglib.ui.SearchFormTag.class);
    _jspx_th_liferay_002dui_005fsearch_002dform_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fsearch_002dform_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(38,4) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fsearch_002dform_005f0.setPage("/html/portlet/workflow_tasks/workflow_search_tasks.jsp");
    int _jspx_eval_liferay_002dui_005fsearch_002dform_005f0 = _jspx_th_liferay_002dui_005fsearch_002dform_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fsearch_002dform_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dform_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dform_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f0 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
    // /html/portlet/workflow_tasks/workflow_tasks.jspf(51,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setName("struts_action");
    // /html/portlet/workflow_tasks/workflow_tasks.jspf(51,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setValue("/workflow_tasks/edit_workflow_task");
    int _jspx_eval_portlet_005fparam_005f0 = _jspx_th_portlet_005fparam_005f0.doStartTag();
    if (_jspx_th_portlet_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:search-container-column-jsp
    com.liferay.taglib.ui.SearchContainerColumnJSPTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f0 = (com.liferay.taglib.ui.SearchContainerColumnJSPTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_0026_005fpath_005falign_005fnobody.get(com.liferay.taglib.ui.SearchContainerColumnJSPTag.class);
    _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
    // /html/portlet/workflow_tasks/workflow_tasks.jspf(171,3) name = align type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f0.setAlign("right");
    // /html/portlet/workflow_tasks/workflow_tasks.jspf(171,3) name = path type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f0.setPath("/html/portlet/workflow_tasks/workflow_task_action.jsp");
    int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f0 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_0026_005fpath_005falign_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_0026_005fpath_005falign_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fsearch_002diterator_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:search-iterator
    com.liferay.taglib.ui.SearchIteratorTag _jspx_th_liferay_002dui_005fsearch_002diterator_005f0 = (com.liferay.taglib.ui.SearchIteratorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_005fnobody.get(com.liferay.taglib.ui.SearchIteratorTag.class);
    _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f0);
    int _jspx_eval_liferay_002dui_005fsearch_002diterator_005f0 = _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fsearch_002diterator_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f3 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f3.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
    // /html/portlet/workflow_tasks/workflow_tasks.jspf(51,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f3.setName("struts_action");
    // /html/portlet/workflow_tasks/workflow_tasks.jspf(51,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f3.setValue("/workflow_tasks/edit_workflow_task");
    int _jspx_eval_portlet_005fparam_005f3 = _jspx_th_portlet_005fparam_005f3.doStartTag();
    if (_jspx_th_portlet_005fparam_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:search-container-column-jsp
    com.liferay.taglib.ui.SearchContainerColumnJSPTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f1 = (com.liferay.taglib.ui.SearchContainerColumnJSPTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_0026_005fpath_005falign_005fnobody.get(com.liferay.taglib.ui.SearchContainerColumnJSPTag.class);
    _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
    // /html/portlet/workflow_tasks/workflow_tasks.jspf(171,3) name = align type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f1.setAlign("right");
    // /html/portlet/workflow_tasks/workflow_tasks.jspf(171,3) name = path type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f1.setPath("/html/portlet/workflow_tasks/workflow_task_action.jsp");
    int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f1 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_0026_005fpath_005falign_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_0026_005fpath_005falign_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f1);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fsearch_002diterator_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:search-iterator
    com.liferay.taglib.ui.SearchIteratorTag _jspx_th_liferay_002dui_005fsearch_002diterator_005f1 = (com.liferay.taglib.ui.SearchIteratorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_005fnobody.get(com.liferay.taglib.ui.SearchIteratorTag.class);
    _jspx_th_liferay_002dui_005fsearch_002diterator_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fsearch_002diterator_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f1);
    int _jspx_eval_liferay_002dui_005fsearch_002diterator_005f1 = _jspx_th_liferay_002dui_005fsearch_002diterator_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fsearch_002diterator_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f1);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fsearch_002dform_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:search-form
    com.liferay.taglib.ui.SearchFormTag _jspx_th_liferay_002dui_005fsearch_002dform_005f1 = (com.liferay.taglib.ui.SearchFormTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fpage_005fnobody.get(com.liferay.taglib.ui.SearchFormTag.class);
    _jspx_th_liferay_002dui_005fsearch_002dform_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fsearch_002dform_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
    // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(65,4) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fsearch_002dform_005f1.setPage("/html/portlet/workflow_tasks/workflow_search_tasks.jsp");
    int _jspx_eval_liferay_002dui_005fsearch_002dform_005f1 = _jspx_th_liferay_002dui_005fsearch_002dform_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fsearch_002dform_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dform_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dform_005f1);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f6 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f6.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f2);
    // /html/portlet/workflow_tasks/workflow_tasks.jspf(51,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f6.setName("struts_action");
    // /html/portlet/workflow_tasks/workflow_tasks.jspf(51,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f6.setValue("/workflow_tasks/edit_workflow_task");
    int _jspx_eval_portlet_005fparam_005f6 = _jspx_th_portlet_005fparam_005f6.doStartTag();
    if (_jspx_th_portlet_005fparam_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:search-container-column-jsp
    com.liferay.taglib.ui.SearchContainerColumnJSPTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f2 = (com.liferay.taglib.ui.SearchContainerColumnJSPTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_0026_005fpath_005falign_005fnobody.get(com.liferay.taglib.ui.SearchContainerColumnJSPTag.class);
    _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f2.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
    // /html/portlet/workflow_tasks/workflow_tasks.jspf(171,3) name = align type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f2.setAlign("right");
    // /html/portlet/workflow_tasks/workflow_tasks.jspf(171,3) name = path type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f2.setPath("/html/portlet/workflow_tasks/workflow_task_action.jsp");
    int _jspx_eval_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f2 = _jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f2.doStartTag();
    if (_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_0026_005fpath_005falign_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_0026_005fpath_005falign_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dcontainer_002dcolumn_002djsp_005f2);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fsearch_002diterator_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:search-iterator
    com.liferay.taglib.ui.SearchIteratorTag _jspx_th_liferay_002dui_005fsearch_002diterator_005f2 = (com.liferay.taglib.ui.SearchIteratorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_005fnobody.get(com.liferay.taglib.ui.SearchIteratorTag.class);
    _jspx_th_liferay_002dui_005fsearch_002diterator_005f2.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fsearch_002diterator_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dcontainer_005f2);
    int _jspx_eval_liferay_002dui_005fsearch_002diterator_005f2 = _jspx_th_liferay_002dui_005fsearch_002diterator_005f2.doStartTag();
    if (_jspx_th_liferay_002dui_005fsearch_002diterator_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f2);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
    // /html/portlet/workflow_tasks/view_workflow_tasks.jspf(85,2) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("an-error-occurred-while-retrieving-the-list-of-tasks");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f1 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f1.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
    int _jspx_eval_c_005fotherwise_005f1 = _jspx_th_c_005fotherwise_005f1.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("\t\t<div class=\"portlet-msg-info\">\n");
        out.write("\t\t\t");
        if (_jspx_meth_liferay_002dui_005fmessage_005f1(_jspx_th_c_005fotherwise_005f1, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\t\t</div>\n");
        out.write("\t");
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

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f1 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
    // /html/portlet/workflow_tasks/view.jsp(25,3) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f1.setKey("no-workflow-engine-is-deployed");
    int _jspx_eval_liferay_002dui_005fmessage_005f1 = _jspx_th_liferay_002dui_005fmessage_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
    return false;
  }
}
