package org.apache.jsp.html.portlet.journal_005fcontent;

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
import com.liferay.portlet.asset.service.AssetEntryServiceUtil;
import com.liferay.portlet.documentlibrary.util.DocumentConversionUtil;
import com.liferay.portlet.journal.NoSuchArticleException;
import com.liferay.portlet.journal.NoSuchTemplateException;
import com.liferay.portlet.journal.action.EditArticleAction;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleDisplay;
import com.liferay.portlet.journal.model.JournalTemplate;
import com.liferay.portlet.journal.model.JournalArticleConstants;
import com.liferay.portlet.journal.search.ArticleSearch;
import com.liferay.portlet.journal.search.ArticleSearchTerms;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalTemplateLocalServiceUtil;
import com.liferay.portlet.journal.service.permission.JournalArticlePermission;
import com.liferay.portlet.journal.service.permission.JournalPermission;
import com.liferay.portlet.journal.service.permission.JournalTemplatePermission;
import com.liferay.portlet.journal.util.JournalUtil;
import com.liferay.portlet.layoutconfiguration.util.RuntimePortletUtil;
import com.liferay.portlet.layoutconfiguration.util.xml.ActionURLLogic;
import com.liferay.portlet.layoutconfiguration.util.xml.PortletLogic;
import com.liferay.portlet.layoutconfiguration.util.xml.RenderURLLogic;
import com.liferay.portlet.layoutconfiguration.util.xml.RuntimeLogic;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;

public final class configuration_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(28);
    _jspx_dependants.add("/html/portlet/journal_content/init.jsp");
    _jspx_dependants.add("/html/portlet/init.jsp");
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/journal/article_search_results.jspf");
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
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletConfiguration_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvarImpl_005fportletConfiguration_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffieldset;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ftable_002diterator_0026_005frowPadding_005frowLength_005flistType_005flist;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvar_005fportletName;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fa_0026_005fid_005fhref;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonChange_005fname_005flabel_005finlineLabel_005fchecked_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fhelpMessage;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005finlineLabel_005finlineField;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_002drow;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletConfiguration_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvarImpl_005fportletConfiguration_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffieldset = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ftable_002diterator_0026_005frowPadding_005frowLength_005flistType_005flist = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvar_005fportletName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fa_0026_005fid_005fhref = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonChange_005fname_005flabel_005finlineLabel_005fchecked_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fhelpMessage = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005finlineLabel_005finlineField = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_002drow = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletConfiguration_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvarImpl_005fportletConfiguration_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005faui_005ffieldset.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ftable_002diterator_0026_005frowPadding_005frowLength_005flistType_005flist.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvar_005fportletName.release();
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar.release();
    _005fjspx_005ftagPool_005faui_005fa_0026_005fid_005fhref.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonChange_005fname_005flabel_005finlineLabel_005fchecked_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass.release();
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fhelpMessage.release();
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005finlineLabel_005finlineField.release();
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fbutton_002drow.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody.release();
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

PortletPreferences preferences = renderRequest.getPreferences();

String portletResource = ParamUtil.getString(request, "portletResource");

if (Validator.isNotNull(portletResource)) {
	preferences = PortletPreferencesFactoryUtil.getPortletSetup(request, portletResource);
}

long groupId = GetterUtil.getLong(preferences.getValue("group-id", scopeGroupId.toString()));
String articleId = GetterUtil.getString(preferences.getValue("article-id", StringPool.BLANK));
String templateId = GetterUtil.getString(preferences.getValue("template-id", StringPool.BLANK));
boolean showAvailableLocales = GetterUtil.getBoolean(preferences.getValue("show-available-locales", StringPool.BLANK));
String[] extensions = preferences.getValues("extensions", null);
boolean enablePrint = GetterUtil.getBoolean(preferences.getValue("enable-print", null));
boolean enableRatings = GetterUtil.getBoolean(preferences.getValue("enable-ratings", null));
boolean enableComments = PropsValues.JOURNAL_ARTICLE_COMMENTS_ENABLED && GetterUtil.getBoolean(preferences.getValue("enable-comments", null));
boolean enableCommentRatings = GetterUtil.getBoolean(preferences.getValue("enable-comment-ratings", null));

String[] conversions = DocumentConversionUtil.getConversions("html");

boolean openOfficeServerEnabled = PrefsPropsUtil.getBoolean(PropsKeys.OPENOFFICE_SERVER_ENABLED, PropsValues.OPENOFFICE_SERVER_ENABLED);
boolean enableConversions = openOfficeServerEnabled && (extensions != null) && (extensions.length > 0);

Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);

      out.write('\n');
      out.write('\n');

int cur = ParamUtil.getInteger(request, SearchContainer.DEFAULT_CUR_PARAM);

String redirect = ParamUtil.getString(request, "redirect");

JournalArticle article = null;

String type = StringPool.BLANK;

try {
	if (Validator.isNotNull(articleId)) {
		article = JournalArticleLocalServiceUtil.getLatestArticle(groupId, articleId);

		groupId = article.getGroupId();
		type = article.getType();
	}
}
catch (NoSuchArticleException nsae) {
}

groupId = ParamUtil.getLong(request, "groupId", groupId);
type = ParamUtil.getString(request, "type", type);

      out.write('\n');
      out.write('\n');
      //  liferay-portlet:actionURL
      com.liferay.taglib.portlet.ActionURLTag _jspx_th_liferay_002dportlet_005factionURL_005f0 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fvar_005fportletConfiguration_005fnobody.get(com.liferay.taglib.portlet.ActionURLTag.class);
      _jspx_th_liferay_002dportlet_005factionURL_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dportlet_005factionURL_005f0.setParent(null);
      // /html/portlet/journal_content/configuration.jsp(43,0) name = portletConfiguration type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dportlet_005factionURL_005f0.setPortletConfiguration(true);
      // /html/portlet/journal_content/configuration.jsp(43,0) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
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
      //  liferay-portlet:renderURL
      com.liferay.taglib.portlet.RenderURLTag _jspx_th_liferay_002dportlet_005frenderURL_005f0 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvarImpl_005fportletConfiguration_005fnobody.get(com.liferay.taglib.portlet.RenderURLTag.class);
      _jspx_th_liferay_002dportlet_005frenderURL_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dportlet_005frenderURL_005f0.setParent(null);
      // /html/portlet/journal_content/configuration.jsp(44,0) name = portletConfiguration type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dportlet_005frenderURL_005f0.setPortletConfiguration(true);
      // /html/portlet/journal_content/configuration.jsp(44,0) name = varImpl type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dportlet_005frenderURL_005f0.setVarImpl("portletURL");
      int _jspx_eval_liferay_002dportlet_005frenderURL_005f0 = _jspx_th_liferay_002dportlet_005frenderURL_005f0.doStartTag();
      if (_jspx_th_liferay_002dportlet_005frenderURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvarImpl_005fportletConfiguration_005fnobody.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvarImpl_005fportletConfiguration_005fnobody.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f0);
      javax.portlet.PortletURL portletURL = null;
      portletURL = (javax.portlet.PortletURL) _jspx_page_context.findAttribute("portletURL");
      out.write('\n');
      out.write('\n');
      //  aui:form
      com.liferay.taglib.aui.FormTag _jspx_th_aui_005fform_005f0 = (com.liferay.taglib.aui.FormTag) _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction.get(com.liferay.taglib.aui.FormTag.class);
      _jspx_th_aui_005fform_005f0.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fform_005f0.setParent(null);
      // /html/portlet/journal_content/configuration.jsp(46,0) name = action type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fform_005f0.setAction( configurationURL );
      // /html/portlet/journal_content/configuration.jsp(46,0) null
      _jspx_th_aui_005fform_005f0.setDynamicAttribute(null, "method", new String("post"));
      // /html/portlet/journal_content/configuration.jsp(46,0) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fform_005f0.setName("fm1");
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
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f0 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /html/portlet/journal_content/configuration.jsp(47,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f0.setName( Constants.CMD );
          // /html/portlet/journal_content/configuration.jsp(47,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f0.setType("hidden");
          int _jspx_eval_aui_005finput_005f0 = _jspx_th_aui_005finput_005f0.doStartTag();
          if (_jspx_th_aui_005finput_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f1 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f1.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /html/portlet/journal_content/configuration.jsp(48,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f1.setName("redirect");
          // /html/portlet/journal_content/configuration.jsp(48,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f1.setType("hidden");
          // /html/portlet/journal_content/configuration.jsp(48,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f1.setValue( portletURL.toString() + StringPool.AMPERSAND + renderResponse.getNamespace() + "cur=" + cur );
          int _jspx_eval_aui_005finput_005f1 = _jspx_th_aui_005finput_005f1.doStartTag();
          if (_jspx_th_aui_005finput_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  liferay-ui:error
          com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f0 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
          _jspx_th_liferay_002dui_005ferror_005f0.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005ferror_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /html/portlet/journal_content/configuration.jsp(50,1) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f0.setException( NoSuchArticleException.class );
          // /html/portlet/journal_content/configuration.jsp(50,1) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f0.setMessage("the-web-content-could-not-be-found");
          int _jspx_eval_liferay_002dui_005ferror_005f0 = _jspx_th_liferay_002dui_005ferror_005f0.doStartTag();
          if (_jspx_th_liferay_002dui_005ferror_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f0);
          out.write("\n");
          out.write("\n");
          out.write("\t<div class=\"portlet-msg-info\">\n");
          out.write("\t\t<span class=\"displaying-help-message-holder ");
          out.print( article == null ? StringPool.BLANK : "aui-helper-hidden" );
          out.write("\">\n");
          out.write("\t\t\t");
          if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_th_aui_005fform_005f0, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t</span>\n");
          out.write("\n");
          out.write("\t\t<span class=\"displaying-article-id-holder ");
          out.print( article == null ? "aui-helper-hidden" : StringPool.BLANK );
          out.write("\">\n");
          out.write("\t\t\t");
          if (_jspx_meth_liferay_002dui_005fmessage_005f1(_jspx_th_aui_005fform_005f0, _jspx_page_context))
            return;
          out.write(": <span class=\"displaying-article-id\">");
          out.print( article != null ? HtmlUtil.escape(article.getTitle()) : StringPool.BLANK );
          out.write("</span>\n");
          out.write("\t\t</span>\n");
          out.write("\t</div>\n");
          out.write("\n");
          out.write("\t");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /html/portlet/journal_content/configuration.jsp(62,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f0.setTest( article != null );
          int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
          if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

		String structureId = article.getStructureId();

		if (Validator.isNotNull(structureId)) {
			List templates = JournalTemplateLocalServiceUtil.getStructureTemplates(groupId, structureId);

			if (!templates.isEmpty()) {
				if (Validator.isNull(templateId)) {
					templateId = article.getTemplateId();
				}
		
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t");
              //  aui:fieldset
              com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f0 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset.get(com.liferay.taglib.aui.FieldsetTag.class);
              _jspx_th_aui_005ffieldset_005f0.setPageContext(_jspx_page_context);
              _jspx_th_aui_005ffieldset_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
              int _jspx_eval_aui_005ffieldset_005f0 = _jspx_th_aui_005ffieldset_005f0.doStartTag();
              if (_jspx_eval_aui_005ffieldset_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_aui_005ffieldset_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_aui_005ffieldset_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_aui_005ffieldset_005f0.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  if (_jspx_meth_liferay_002dui_005fmessage_005f2(_jspx_th_aui_005ffieldset_005f0, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  //  liferay-ui:table-iterator
                  com.liferay.taglib.ui.TableIteratorTag _jspx_th_liferay_002dui_005ftable_002diterator_005f0 = (com.liferay.taglib.ui.TableIteratorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ftable_002diterator_0026_005frowPadding_005frowLength_005flistType_005flist.get(com.liferay.taglib.ui.TableIteratorTag.class);
                  _jspx_th_liferay_002dui_005ftable_002diterator_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005ftable_002diterator_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
                  // /html/portlet/journal_content/configuration.jsp(79,5) name = list type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ftable_002diterator_005f0.setList( templates );
                  // /html/portlet/journal_content/configuration.jsp(79,5) name = listType type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ftable_002diterator_005f0.setListType("com.liferay.portlet.journal.model.JournalTemplate");
                  // /html/portlet/journal_content/configuration.jsp(79,5) name = rowLength type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ftable_002diterator_005f0.setRowLength("3");
                  // /html/portlet/journal_content/configuration.jsp(79,5) name = rowPadding type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ftable_002diterator_005f0.setRowPadding("30");
                  int _jspx_eval_liferay_002dui_005ftable_002diterator_005f0 = _jspx_th_liferay_002dui_005ftable_002diterator_005f0.doStartTag();
                  if (_jspx_eval_liferay_002dui_005ftable_002diterator_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    com.liferay.portlet.journal.model.JournalTemplate tableIteratorObj = null;
                    java.lang.Integer tableIteratorPos = null;
                    tableIteratorObj = (com.liferay.portlet.journal.model.JournalTemplate) _jspx_page_context.findAttribute("tableIteratorObj");
                    tableIteratorPos = (java.lang.Integer) _jspx_page_context.findAttribute("tableIteratorPos");
                    do {
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");

						boolean templateChecked = false;

						if (templateId.equals(tableIteratorObj.getTemplateId())) {
							templateChecked = true;
						}

						if ((tableIteratorPos.intValue() == 0) && Validator.isNull(templateId)) {
							templateChecked = true;
						}
						
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  liferay-portlet:renderURL
                      com.liferay.taglib.portlet.RenderURLTag _jspx_th_liferay_002dportlet_005frenderURL_005f1 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvar_005fportletName.get(com.liferay.taglib.portlet.RenderURLTag.class);
                      _jspx_th_liferay_002dportlet_005frenderURL_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dportlet_005frenderURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ftable_002diterator_005f0);
                      // /html/portlet/journal_content/configuration.jsp(98,6) name = portletName type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dportlet_005frenderURL_005f1.setPortletName( PortletKeys.JOURNAL );
                      // /html/portlet/journal_content/configuration.jsp(98,6) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dportlet_005frenderURL_005f1.setVar("editTemplateURL");
                      int _jspx_eval_liferay_002dportlet_005frenderURL_005f1 = _jspx_th_liferay_002dportlet_005frenderURL_005f1.doStartTag();
                      if (_jspx_eval_liferay_002dportlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_liferay_002dportlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_liferay_002dportlet_005frenderURL_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_liferay_002dportlet_005frenderURL_005f1.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          if (_jspx_meth_portlet_005fparam_005f0(_jspx_th_liferay_002dportlet_005frenderURL_005f1, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f1 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f1.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f1);
                          // /html/portlet/journal_content/configuration.jsp(100,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f1.setName("redirect");
                          // /html/portlet/journal_content/configuration.jsp(100,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f1.setValue( currentURL );
                          int _jspx_eval_portlet_005fparam_005f1 = _jspx_th_portlet_005fparam_005f1.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f2 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f2.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f1);
                          // /html/portlet/journal_content/configuration.jsp(101,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f2.setName("groupId");
                          // /html/portlet/journal_content/configuration.jsp(101,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f2.setValue( String.valueOf(tableIteratorObj.getGroupId()) );
                          int _jspx_eval_portlet_005fparam_005f2 = _jspx_th_portlet_005fparam_005f2.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f3 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f3.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f1);
                          // /html/portlet/journal_content/configuration.jsp(102,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f3.setName("templateId");
                          // /html/portlet/journal_content/configuration.jsp(102,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f3.setValue( tableIteratorObj.getTemplateId() );
                          int _jspx_eval_portlet_005fparam_005f3 = _jspx_th_portlet_005fparam_005f3.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t");
                          int evalDoAfterBody = _jspx_th_liferay_002dportlet_005frenderURL_005f1.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_liferay_002dportlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.popBody();
                        }
                      }
                      if (_jspx_th_liferay_002dportlet_005frenderURL_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f1);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f1);
                      java.lang.String editTemplateURL = null;
                      editTemplateURL = (java.lang.String) _jspx_page_context.findAttribute("editTemplateURL");
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  liferay-util:buffer
                      com.liferay.taglib.util.BufferTag _jspx_th_liferay_002dutil_005fbuffer_005f0 = (com.liferay.taglib.util.BufferTag) _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar.get(com.liferay.taglib.util.BufferTag.class);
                      _jspx_th_liferay_002dutil_005fbuffer_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dutil_005fbuffer_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ftable_002diterator_005f0);
                      // /html/portlet/journal_content/configuration.jsp(105,6) name = var type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dutil_005fbuffer_005f0.setVar("linkContent");
                      int _jspx_eval_liferay_002dutil_005fbuffer_005f0 = _jspx_th_liferay_002dutil_005fbuffer_005f0.doStartTag();
                      if (_jspx_eval_liferay_002dutil_005fbuffer_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_liferay_002dutil_005fbuffer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_liferay_002dutil_005fbuffer_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_liferay_002dutil_005fbuffer_005f0.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  aui:a
                          com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f0 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005fid_005fhref.get(com.liferay.taglib.aui.ATag.class);
                          _jspx_th_aui_005fa_005f0.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005fa_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dutil_005fbuffer_005f0);
                          // /html/portlet/journal_content/configuration.jsp(106,7) name = href type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005fa_005f0.setHref( editTemplateURL );
                          // /html/portlet/journal_content/configuration.jsp(106,7) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005fa_005f0.setId("tableIteratorObjName");
                          int _jspx_eval_aui_005fa_005f0 = _jspx_th_aui_005fa_005f0.doStartTag();
                          if (_jspx_eval_aui_005fa_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_aui_005fa_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_aui_005fa_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_aui_005fa_005f0.doInitBody();
                            }
                            do {
                              out.print( tableIteratorObj.getName() );
                              int evalDoAfterBody = _jspx_th_aui_005fa_005f0.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_aui_005fa_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                            }
                          }
                          if (_jspx_th_aui_005fa_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005fa_0026_005fid_005fhref.reuse(_jspx_th_aui_005fa_005f0);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005fa_0026_005fid_005fhref.reuse(_jspx_th_aui_005fa_005f0);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t");
                          int evalDoAfterBody = _jspx_th_liferay_002dutil_005fbuffer_005f0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_liferay_002dutil_005fbuffer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.popBody();
                        }
                      }
                      if (_jspx_th_liferay_002dutil_005fbuffer_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar.reuse(_jspx_th_liferay_002dutil_005fbuffer_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar.reuse(_jspx_th_liferay_002dutil_005fbuffer_005f0);
                      java.lang.String linkContent = null;
                      linkContent = (java.lang.String) _jspx_page_context.findAttribute("linkContent");
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  aui:input
                      com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f2 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonChange_005fname_005flabel_005finlineLabel_005fchecked_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                      _jspx_th_aui_005finput_005f2.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005finput_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ftable_002diterator_005f0);
                      // /html/portlet/journal_content/configuration.jsp(109,6) name = checked type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f2.setChecked( templateChecked );
                      // /html/portlet/journal_content/configuration.jsp(109,6) name = inlineLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f2.setInlineLabel("right");
                      // /html/portlet/journal_content/configuration.jsp(109,6) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f2.setName("overideTemplateId");
                      // /html/portlet/journal_content/configuration.jsp(109,6) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f2.setLabel( linkContent );
                      // /html/portlet/journal_content/configuration.jsp(109,6) name = onChange type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f2.setOnChange( "if (this.checked) {document." + renderResponse.getNamespace() + "fm." + renderResponse.getNamespace() + "templateId.value = this.value;}" );
                      // /html/portlet/journal_content/configuration.jsp(109,6) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f2.setType("radio");
                      // /html/portlet/journal_content/configuration.jsp(109,6) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005finput_005f2.setValue( tableIteratorObj.getTemplateId() );
                      int _jspx_eval_aui_005finput_005f2 = _jspx_th_aui_005finput_005f2.doStartTag();
                      if (_jspx_th_aui_005finput_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonChange_005fname_005flabel_005finlineLabel_005fchecked_005fnobody.reuse(_jspx_th_aui_005finput_005f2);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonChange_005fname_005flabel_005finlineLabel_005fchecked_005fnobody.reuse(_jspx_th_aui_005finput_005f2);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ftable_002diterator_005f0);
                      // /html/portlet/journal_content/configuration.jsp(111,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f1.setTest( tableIteratorObj.isSmallImage() );
                      int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
                      if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t<br />\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t<img border=\"0\" hspace=\"0\" src=\"");
                          out.print( Validator.isNotNull(tableIteratorObj.getSmallImageURL()) ? tableIteratorObj.getSmallImageURL() : themeDisplay.getPathImage() + "/journal/template?img_id=" + tableIteratorObj.getSmallImageId() + "&t=" + ImageServletTokenUtil.getToken(tableIteratorObj.getSmallImageId()) );
                          out.write("\" vspace=\"0\" />\n");
                          out.write("\t\t\t\t\t\t");
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
                      out.write("\t\t\t\t\t");
                      int evalDoAfterBody = _jspx_th_liferay_002dui_005ftable_002diterator_005f0.doAfterBody();
                      tableIteratorObj = (com.liferay.portlet.journal.model.JournalTemplate) _jspx_page_context.findAttribute("tableIteratorObj");
                      tableIteratorPos = (java.lang.Integer) _jspx_page_context.findAttribute("tableIteratorPos");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_liferay_002dui_005ftable_002diterator_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005ftable_002diterator_0026_005frowPadding_005frowLength_005flistType_005flist.reuse(_jspx_th_liferay_002dui_005ftable_002diterator_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005ftable_002diterator_0026_005frowPadding_005frowLength_005flistType_005flist.reuse(_jspx_th_liferay_002dui_005ftable_002diterator_005f0);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t<br />\n");
                  out.write("\t\t\t\t");
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
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

			}
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

	DynamicRenderRequest dynamicRenderRequest = new DynamicRenderRequest(renderRequest);

	dynamicRenderRequest.setParameter("type", type);
	dynamicRenderRequest.setParameter("groupId", String.valueOf(groupId));

	ArticleSearch searchContainer = new ArticleSearch(dynamicRenderRequest, portletURL);
	
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  liferay-ui:search-form
          com.liferay.taglib.ui.SearchFormTag _jspx_th_liferay_002dui_005fsearch_002dform_005f0 = (com.liferay.taglib.ui.SearchFormTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage.get(com.liferay.taglib.ui.SearchFormTag.class);
          _jspx_th_liferay_002dui_005fsearch_002dform_005f0.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005fsearch_002dform_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /html/portlet/journal_content/configuration.jsp(137,1) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fsearch_002dform_005f0.setPage("/html/portlet/journal/article_search.jsp");
          // /html/portlet/journal_content/configuration.jsp(137,1) name = searchContainer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fsearch_002dform_005f0.setSearchContainer( searchContainer );
          int _jspx_eval_liferay_002dui_005fsearch_002dform_005f0 = _jspx_th_liferay_002dui_005fsearch_002dform_005f0.doStartTag();
          if (_jspx_eval_liferay_002dui_005fsearch_002dform_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_liferay_002dui_005fsearch_002dform_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_liferay_002dui_005fsearch_002dform_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_liferay_002dui_005fsearch_002dform_005f0.doInitBody();
            }
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  liferay-ui:param
              com.liferay.taglib.util.ParamTag _jspx_th_liferay_002dui_005fparam_005f0 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fliferay_002dui_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_liferay_002dui_005fparam_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005fparam_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dform_005f0);
              // /html/portlet/journal_content/configuration.jsp(141,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fparam_005f0.setName("groupId");
              // /html/portlet/journal_content/configuration.jsp(141,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fparam_005f0.setValue( String.valueOf(groupId) );
              int _jspx_eval_liferay_002dui_005fparam_005f0 = _jspx_th_liferay_002dui_005fparam_005f0.doStartTag();
              if (_jspx_th_liferay_002dui_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_liferay_002dui_005fparam_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_liferay_002dui_005fparam_005f0);
              out.write('\n');
              out.write('	');
              out.write('	');
              //  liferay-ui:param
              com.liferay.taglib.util.ParamTag _jspx_th_liferay_002dui_005fparam_005f1 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fliferay_002dui_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_liferay_002dui_005fparam_005f1.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005fparam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dform_005f0);
              // /html/portlet/journal_content/configuration.jsp(142,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fparam_005f1.setName("type");
              // /html/portlet/journal_content/configuration.jsp(142,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fparam_005f1.setValue( type );
              int _jspx_eval_liferay_002dui_005fparam_005f1 = _jspx_th_liferay_002dui_005fparam_005f1.doStartTag();
              if (_jspx_th_liferay_002dui_005fparam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_liferay_002dui_005fparam_005f1);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_liferay_002dui_005fparam_005f1);
              out.write('\n');
              out.write('	');
              int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dform_005f0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_liferay_002dui_005fsearch_002dform_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_liferay_002dui_005fsearch_002dform_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage.reuse(_jspx_th_liferay_002dui_005fsearch_002dform_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage.reuse(_jspx_th_liferay_002dui_005fsearch_002dform_005f0);
          out.write("\n");
          out.write("\n");
          out.write("\t<br />\n");
          out.write("\n");
          out.write("\t");

	OrderByComparator orderByComparator = JournalUtil.getArticleOrderByComparator(searchContainer.getOrderByCol(), searchContainer.getOrderByType());

	ArticleSearchTerms searchTerms = (ArticleSearchTerms)searchContainer.getSearchTerms();

	searchTerms.setVersion(-1);
	
          out.write('\n');
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

int total = 0;

if (searchTerms.isAdvancedSearch()) {
	total = JournalArticleLocalServiceUtil.searchCount(company.getCompanyId(), searchTerms.getGroupId(), searchTerms.getArticleId(), searchTerms.getVersionObj(), searchTerms.getTitle(), searchTerms.getDescription(), searchTerms.getContent(), searchTerms.getType(), searchTerms.getStructureId(), searchTerms.getTemplateId(), searchTerms.getDisplayDateGT(), searchTerms.getDisplayDateLT(), searchTerms.getStatusCode(), searchTerms.getReviewDate(), searchTerms.isAndOperator());
}
else {
	total = JournalArticleLocalServiceUtil.searchCount(company.getCompanyId(), searchTerms.getGroupId(), searchTerms.getKeywords(), searchTerms.getVersionObj(), null, searchTerms.getStructureId(), searchTerms.getTemplateId(), searchTerms.getDisplayDateGT(), searchTerms.getDisplayDateLT(), searchTerms.getStatusCode(), searchTerms.getReviewDate());
}

searchContainer.setTotal(total);

List results = null;

if (searchTerms.isAdvancedSearch()) {
	results = JournalArticleLocalServiceUtil.search(company.getCompanyId(), searchTerms.getGroupId(), searchTerms.getArticleId(), searchTerms.getVersionObj(), searchTerms.getTitle(), searchTerms.getDescription(), searchTerms.getContent(), searchTerms.getType(), searchTerms.getStructureId(), searchTerms.getTemplateId(), searchTerms.getDisplayDateGT(), searchTerms.getDisplayDateLT(), searchTerms.getStatusCode(), searchTerms.getReviewDate(), searchTerms.isAndOperator(), searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
}
else {
	results = JournalArticleLocalServiceUtil.search(company.getCompanyId(), searchTerms.getGroupId(), searchTerms.getKeywords(), searchTerms.getVersionObj(), null, searchTerms.getStructureId(), searchTerms.getTemplateId(), searchTerms.getDisplayDateGT(), searchTerms.getDisplayDateLT(), searchTerms.getStatusCode(), searchTerms.getReviewDate(), searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
}

searchContainer.setResults(results);

          out.write('\n');
          out.write('\n');
          out.write('	');

	List resultRows = searchContainer.getResultRows();

	for (int i = 0; i < results.size(); i++) {
		JournalArticle curArticle = (JournalArticle)results.get(i);

		curArticle = curArticle.toEscapedModel();

		ResultRow row = new ResultRow(null, curArticle.getArticleId() + EditArticleAction.VERSION_SEPARATOR + curArticle.getVersion(), i);

		StringBundler sb = new StringBundler(7);

		sb.append("javascript:");
		sb.append(renderResponse.getNamespace());
		sb.append("selectArticle('");
		sb.append(curArticle.getArticleId());
		sb.append("','");
		sb.append(curArticle.getTitle());
		sb.append("');");

		String rowHREF = sb.toString();

		// Article id

		row.addText(curArticle.getArticleId(), rowHREF);

		// Title

		row.addText(curArticle.getTitle(), rowHREF);

		// Version

		row.addText(String.valueOf(curArticle.getVersion()), rowHREF);

		// Modified date

		row.addText(dateFormatDate.format(curArticle.getModifiedDate()), rowHREF);

		// Display date

		row.addText(dateFormatDate.format(curArticle.getDisplayDate()), rowHREF);

		// Author

		row.addText(HtmlUtil.escape(PortalUtil.getUserName(curArticle.getUserId(), curArticle.getUserName())), rowHREF);

		// Add result row

		resultRows.add(row);
	}
	
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  liferay-ui:search-iterator
          com.liferay.taglib.ui.SearchIteratorTag _jspx_th_liferay_002dui_005fsearch_002diterator_005f0 = (com.liferay.taglib.ui.SearchIteratorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.get(com.liferay.taglib.ui.SearchIteratorTag.class);
          _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /html/portlet/journal_content/configuration.jsp(209,1) name = searchContainer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.setSearchContainer( searchContainer );
          int _jspx_eval_liferay_002dui_005fsearch_002diterator_005f0 = _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.doStartTag();
          if (_jspx_th_liferay_002dui_005fsearch_002diterator_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f0);
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
      //  aui:form
      com.liferay.taglib.aui.FormTag _jspx_th_aui_005fform_005f1 = (com.liferay.taglib.aui.FormTag) _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction.get(com.liferay.taglib.aui.FormTag.class);
      _jspx_th_aui_005fform_005f1.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fform_005f1.setParent(null);
      // /html/portlet/journal_content/configuration.jsp(212,0) name = action type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fform_005f1.setAction( configurationURL );
      // /html/portlet/journal_content/configuration.jsp(212,0) null
      _jspx_th_aui_005fform_005f1.setDynamicAttribute(null, "method", new String("post"));
      // /html/portlet/journal_content/configuration.jsp(212,0) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fform_005f1.setName("fm");
      int _jspx_eval_aui_005fform_005f1 = _jspx_th_aui_005fform_005f1.doStartTag();
      if (_jspx_eval_aui_005fform_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_aui_005fform_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_aui_005fform_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_aui_005fform_005f1.doInitBody();
        }
        do {
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f3 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f3.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal_content/configuration.jsp(213,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f3.setName( Constants.CMD );
          // /html/portlet/journal_content/configuration.jsp(213,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f3.setType("hidden");
          // /html/portlet/journal_content/configuration.jsp(213,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f3.setValue( Constants.UPDATE );
          int _jspx_eval_aui_005finput_005f3 = _jspx_th_aui_005finput_005f3.doStartTag();
          if (_jspx_th_aui_005finput_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f3);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f3);
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f4 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f4.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal_content/configuration.jsp(214,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f4.setName("redirect");
          // /html/portlet/journal_content/configuration.jsp(214,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f4.setType("hidden");
          // /html/portlet/journal_content/configuration.jsp(214,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f4.setValue( portletURL.toString() + StringPool.AMPERSAND + renderResponse.getNamespace() + "cur" + cur );
          int _jspx_eval_aui_005finput_005f4 = _jspx_th_aui_005finput_005f4.doStartTag();
          if (_jspx_th_aui_005finput_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f4);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f4);
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f5 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f5.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal_content/configuration.jsp(215,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f5.setName("groupId");
          // /html/portlet/journal_content/configuration.jsp(215,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f5.setType("hidden");
          // /html/portlet/journal_content/configuration.jsp(215,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f5.setValue( groupId );
          int _jspx_eval_aui_005finput_005f5 = _jspx_th_aui_005finput_005f5.doStartTag();
          if (_jspx_th_aui_005finput_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f5);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f5);
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f6 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f6.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal_content/configuration.jsp(216,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f6.setName("articleId");
          // /html/portlet/journal_content/configuration.jsp(216,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f6.setType("hidden");
          // /html/portlet/journal_content/configuration.jsp(216,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f6.setValue( articleId );
          int _jspx_eval_aui_005finput_005f6 = _jspx_th_aui_005finput_005f6.doStartTag();
          if (_jspx_th_aui_005finput_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f6);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f6);
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f7 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f7.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal_content/configuration.jsp(217,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f7.setName("templateId");
          // /html/portlet/journal_content/configuration.jsp(217,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f7.setType("hidden");
          // /html/portlet/journal_content/configuration.jsp(217,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f7.setValue( templateId );
          int _jspx_eval_aui_005finput_005f7 = _jspx_th_aui_005finput_005f7.doStartTag();
          if (_jspx_th_aui_005finput_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f7);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f7);
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  aui:fieldset
          com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f1 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass.get(com.liferay.taglib.aui.FieldsetTag.class);
          _jspx_th_aui_005ffieldset_005f1.setPageContext(_jspx_page_context);
          _jspx_th_aui_005ffieldset_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal_content/configuration.jsp(219,1) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005ffieldset_005f1.setCssClass("aui-helper-hidden");
          int _jspx_eval_aui_005ffieldset_005f1 = _jspx_th_aui_005ffieldset_005f1.doStartTag();
          if (_jspx_eval_aui_005ffieldset_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_aui_005ffieldset_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_aui_005ffieldset_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_aui_005ffieldset_005f1.doInitBody();
            }
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  aui:field-wrapper
              com.liferay.taglib.aui.FieldWrapperTag _jspx_th_aui_005ffield_002dwrapper_005f0 = (com.liferay.taglib.aui.FieldWrapperTag) _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.get(com.liferay.taglib.aui.FieldWrapperTag.class);
              _jspx_th_aui_005ffield_002dwrapper_005f0.setPageContext(_jspx_page_context);
              _jspx_th_aui_005ffield_002dwrapper_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f1);
              // /html/portlet/journal_content/configuration.jsp(220,2) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005ffield_002dwrapper_005f0.setLabel("portlet-id");
              int _jspx_eval_aui_005ffield_002dwrapper_005f0 = _jspx_th_aui_005ffield_002dwrapper_005f0.doStartTag();
              if (_jspx_eval_aui_005ffield_002dwrapper_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_aui_005ffield_002dwrapper_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_aui_005ffield_002dwrapper_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_aui_005ffield_002dwrapper_005f0.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  out.print( portletResource );
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_aui_005ffield_002dwrapper_005f0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_aui_005ffield_002dwrapper_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_aui_005ffield_002dwrapper_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.reuse(_jspx_th_aui_005ffield_002dwrapper_005f0);
                return;
              }
              _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.reuse(_jspx_th_aui_005ffield_002dwrapper_005f0);
              out.write('\n');
              out.write('	');
              int evalDoAfterBody = _jspx_th_aui_005ffieldset_005f1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_aui_005ffieldset_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_aui_005ffieldset_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass.reuse(_jspx_th_aui_005ffieldset_005f1);
            return;
          }
          _005fjspx_005ftagPool_005faui_005ffieldset_0026_005fcssClass.reuse(_jspx_th_aui_005ffieldset_005f1);
          out.write("\n");
          out.write("\n");
          out.write("\t<br />\n");
          out.write("\n");
          out.write("\t");
          //  aui:fieldset
          com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f2 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset.get(com.liferay.taglib.aui.FieldsetTag.class);
          _jspx_th_aui_005ffieldset_005f2.setPageContext(_jspx_page_context);
          _jspx_th_aui_005ffieldset_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          int _jspx_eval_aui_005ffieldset_005f2 = _jspx_th_aui_005ffieldset_005f2.doStartTag();
          if (_jspx_eval_aui_005ffieldset_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_aui_005ffieldset_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_aui_005ffieldset_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_aui_005ffieldset_005f2.doInitBody();
            }
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  aui:input
              com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f8 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
              _jspx_th_aui_005finput_005f8.setPageContext(_jspx_page_context);
              _jspx_th_aui_005finput_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f2);
              // /html/portlet/journal_content/configuration.jsp(228,2) name = inlineLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f8.setInlineLabel("left");
              // /html/portlet/journal_content/configuration.jsp(228,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f8.setName("showAvailableLocales");
              // /html/portlet/journal_content/configuration.jsp(228,2) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f8.setType("checkbox");
              // /html/portlet/journal_content/configuration.jsp(228,2) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f8.setValue( showAvailableLocales );
              int _jspx_eval_aui_005finput_005f8 = _jspx_th_aui_005finput_005f8.doStartTag();
              if (_jspx_th_aui_005finput_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f8);
                return;
              }
              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f8);
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  aui:field-wrapper
              com.liferay.taglib.aui.FieldWrapperTag _jspx_th_aui_005ffield_002dwrapper_005f1 = (com.liferay.taglib.aui.FieldWrapperTag) _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fhelpMessage.get(com.liferay.taglib.aui.FieldWrapperTag.class);
              _jspx_th_aui_005ffield_002dwrapper_005f1.setPageContext(_jspx_page_context);
              _jspx_th_aui_005ffield_002dwrapper_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f2);
              // /html/portlet/journal_content/configuration.jsp(230,2) name = helpMessage type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005ffield_002dwrapper_005f1.setHelpMessage( !openOfficeServerEnabled ? "enabling-openoffice-integration-provides-document-conversion-functionality" : StringPool.BLANK );
              // /html/portlet/journal_content/configuration.jsp(230,2) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005ffield_002dwrapper_005f1.setLabel("enable-conversion-to");
              int _jspx_eval_aui_005ffield_002dwrapper_005f1 = _jspx_th_aui_005ffield_002dwrapper_005f1.doStartTag();
              if (_jspx_eval_aui_005ffield_002dwrapper_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_aui_005ffield_002dwrapper_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_aui_005ffield_002dwrapper_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_aui_005ffield_002dwrapper_005f1.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");

			for (String conversion : conversions) {
			
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t");
                  //  aui:field-wrapper
                  com.liferay.taglib.aui.FieldWrapperTag _jspx_th_aui_005ffield_002dwrapper_005f2 = (com.liferay.taglib.aui.FieldWrapperTag) _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005finlineLabel_005finlineField.get(com.liferay.taglib.aui.FieldWrapperTag.class);
                  _jspx_th_aui_005ffield_002dwrapper_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005ffield_002dwrapper_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f1);
                  // /html/portlet/journal_content/configuration.jsp(236,4) name = inlineField type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005ffield_002dwrapper_005f2.setInlineField( true );
                  // /html/portlet/journal_content/configuration.jsp(236,4) name = inlineLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005ffield_002dwrapper_005f2.setInlineLabel("left");
                  // /html/portlet/journal_content/configuration.jsp(236,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005ffield_002dwrapper_005f2.setLabel( conversion.toUpperCase() );
                  int _jspx_eval_aui_005ffield_002dwrapper_005f2 = _jspx_th_aui_005ffield_002dwrapper_005f2.doStartTag();
                  if (_jspx_eval_aui_005ffield_002dwrapper_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005ffield_002dwrapper_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005ffield_002dwrapper_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005ffield_002dwrapper_005f2.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t<input ");
                      out.print( ArrayUtil.contains(extensions, conversion) ? "checked": "" );
                      out.write(' ');
                      out.print( !openOfficeServerEnabled ? "disabled" : "" );
                      out.write(" name=\"");
                      if (_jspx_meth_portlet_005fnamespace_005f0(_jspx_th_aui_005ffield_002dwrapper_005f2, _jspx_page_context))
                        return;
                      out.write("extensions\" type=\"checkbox\" value=\"");
                      out.print( conversion );
                      out.write("\" />\n");
                      out.write("\t\t\t\t");
                      int evalDoAfterBody = _jspx_th_aui_005ffield_002dwrapper_005f2.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_aui_005ffield_002dwrapper_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_aui_005ffield_002dwrapper_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005finlineLabel_005finlineField.reuse(_jspx_th_aui_005ffield_002dwrapper_005f2);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005finlineLabel_005finlineField.reuse(_jspx_th_aui_005ffield_002dwrapper_005f2);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");

			}
			
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t");
                  int evalDoAfterBody = _jspx_th_aui_005ffield_002dwrapper_005f1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_aui_005ffield_002dwrapper_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_aui_005ffield_002dwrapper_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fhelpMessage.reuse(_jspx_th_aui_005ffield_002dwrapper_005f1);
                return;
              }
              _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fhelpMessage.reuse(_jspx_th_aui_005ffield_002dwrapper_005f1);
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  aui:input
              com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f9 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
              _jspx_th_aui_005finput_005f9.setPageContext(_jspx_page_context);
              _jspx_th_aui_005finput_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f2);
              // /html/portlet/journal_content/configuration.jsp(246,2) name = inlineLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f9.setInlineLabel("left");
              // /html/portlet/journal_content/configuration.jsp(246,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f9.setName("enablePrint");
              // /html/portlet/journal_content/configuration.jsp(246,2) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f9.setType("checkbox");
              // /html/portlet/journal_content/configuration.jsp(246,2) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f9.setValue( enablePrint );
              int _jspx_eval_aui_005finput_005f9 = _jspx_th_aui_005finput_005f9.doStartTag();
              if (_jspx_th_aui_005finput_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f9);
                return;
              }
              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f9);
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  aui:input
              com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f10 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
              _jspx_th_aui_005finput_005f10.setPageContext(_jspx_page_context);
              _jspx_th_aui_005finput_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f2);
              // /html/portlet/journal_content/configuration.jsp(248,2) name = inlineLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f10.setInlineLabel("left");
              // /html/portlet/journal_content/configuration.jsp(248,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f10.setName("enableRatings");
              // /html/portlet/journal_content/configuration.jsp(248,2) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f10.setType("checkbox");
              // /html/portlet/journal_content/configuration.jsp(248,2) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f10.setValue( enableRatings );
              int _jspx_eval_aui_005finput_005f10 = _jspx_th_aui_005finput_005f10.doStartTag();
              if (_jspx_th_aui_005finput_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f10);
                return;
              }
              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f10);
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f2);
              // /html/portlet/journal_content/configuration.jsp(250,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f2.setTest( PropsValues.JOURNAL_ARTICLE_COMMENTS_ENABLED );
              int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
              if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:input
                  com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f11 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                  _jspx_th_aui_005finput_005f11.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005finput_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
                  // /html/portlet/journal_content/configuration.jsp(251,3) name = inlineLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f11.setInlineLabel("left");
                  // /html/portlet/journal_content/configuration.jsp(251,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f11.setName("enableComments");
                  // /html/portlet/journal_content/configuration.jsp(251,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f11.setType("checkbox");
                  // /html/portlet/journal_content/configuration.jsp(251,3) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f11.setValue( enableComments );
                  int _jspx_eval_aui_005finput_005f11 = _jspx_th_aui_005finput_005f11.doStartTag();
                  if (_jspx_th_aui_005finput_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f11);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f11);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:input
                  com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f12 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                  _jspx_th_aui_005finput_005f12.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005finput_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
                  // /html/portlet/journal_content/configuration.jsp(253,3) name = inlineLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f12.setInlineLabel("left");
                  // /html/portlet/journal_content/configuration.jsp(253,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f12.setName("enableCommentRatings");
                  // /html/portlet/journal_content/configuration.jsp(253,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f12.setType("checkbox");
                  // /html/portlet/journal_content/configuration.jsp(253,3) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f12.setValue( enableCommentRatings );
                  int _jspx_eval_aui_005finput_005f12 = _jspx_th_aui_005finput_005f12.doStartTag();
                  if (_jspx_th_aui_005finput_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f12);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f12);
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
              out.write('\n');
              out.write('	');
              int evalDoAfterBody = _jspx_th_aui_005ffieldset_005f2.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_aui_005ffieldset_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_aui_005ffieldset_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f2);
            return;
          }
          _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f2);
          out.write('\n');
          out.write('\n');
          out.write('	');
          if (_jspx_meth_aui_005fbutton_002drow_005f0(_jspx_th_aui_005fform_005f1, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_aui_005fform_005f1.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_aui_005fform_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
        }
      }
      if (_jspx_th_aui_005fform_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction.reuse(_jspx_th_aui_005fform_005f1);
        return;
      }
      _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction.reuse(_jspx_th_aui_005fform_005f1);
      out.write('\n');
      out.write('\n');
      //  aui:script
      com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript.get(com.liferay.taglib.aui.ScriptTag.class);
      _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fscript_005f0.setParent(null);
      int _jspx_eval_aui_005fscript_005f0 = _jspx_th_aui_005fscript_005f0.doStartTag();
      if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_aui_005fscript_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_aui_005fscript_005f0.doInitBody();
        }
        do {
          out.write("\n");
          out.write("\tLiferay.provide(\n");
          out.write("\t\twindow,\n");
          out.write("\t\t'");
          if (_jspx_meth_portlet_005fnamespace_005f1(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("selectArticle',\n");
          out.write("\t\tfunction(articleId, articletTitle) {\n");
          out.write("\t\t\tvar A = AUI();\n");
          out.write("\n");
          out.write("\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f2(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f3(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("articleId.value = articleId;\n");
          out.write("\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f4(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f5(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("templateId.value = \"\";\n");
          out.write("\n");
          out.write("\t\t\tA.one('.displaying-article-id-holder').show();\n");
          out.write("\t\t\tA.one('.displaying-help-message-holder').hide();\n");
          out.write("\n");
          out.write("\t\t\tvar displayArticleId = A.one('.displaying-article-id');\n");
          out.write("\n");
          out.write("\t\t\tdisplayArticleId.set('innerHTML', articletTitle + ' (");
          out.print( LanguageUtil.get(pageContext, "modified") );
          out.write(")');\n");
          out.write("\t\t\tdisplayArticleId.addClass('modified');\n");
          out.write("\t\t},\n");
          out.write("\t\t['aui-base']\n");
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
        _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f0);
        return;
      }
      _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f0);
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

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
    // /html/portlet/journal_content/configuration.jsp(54,3) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("please-select-a-web-content-from-the-list-below");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f1 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
    // /html/portlet/journal_content/configuration.jsp(58,3) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f1.setKey("displaying-content");
    int _jspx_eval_liferay_002dui_005fmessage_005f1 = _jspx_th_liferay_002dui_005fmessage_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f2 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f2.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
    // /html/portlet/journal_content/configuration.jsp(77,5) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f2.setKey("override-default-template");
    int _jspx_eval_liferay_002dui_005fmessage_005f2 = _jspx_th_liferay_002dui_005fmessage_005f2.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dportlet_005frenderURL_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f0 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f1);
    // /html/portlet/journal_content/configuration.jsp(99,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setName("struts_action");
    // /html/portlet/journal_content/configuration.jsp(99,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setValue("/journal/edit_template");
    int _jspx_eval_portlet_005fparam_005f0 = _jspx_th_portlet_005fparam_005f0.doStartTag();
    if (_jspx_th_portlet_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffield_002dwrapper_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f0 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f2);
    int _jspx_eval_portlet_005fnamespace_005f0 = _jspx_th_portlet_005fnamespace_005f0.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_002drow_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button-row
    com.liferay.taglib.aui.ButtonRowTag _jspx_th_aui_005fbutton_002drow_005f0 = (com.liferay.taglib.aui.ButtonRowTag) _005fjspx_005ftagPool_005faui_005fbutton_002drow.get(com.liferay.taglib.aui.ButtonRowTag.class);
    _jspx_th_aui_005fbutton_002drow_005f0.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_002drow_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
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
        if (_jspx_meth_aui_005fbutton_005f0(_jspx_th_aui_005fbutton_002drow_005f0, _jspx_page_context))
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

  private boolean _jspx_meth_aui_005fbutton_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fbutton_002drow_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button
    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f0 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
    _jspx_th_aui_005fbutton_005f0.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f0);
    // /html/portlet/journal_content/configuration.jsp(258,2) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f0.setType("submit");
    int _jspx_eval_aui_005fbutton_005f0 = _jspx_th_aui_005fbutton_005f0.doStartTag();
    if (_jspx_th_aui_005fbutton_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f1 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f1.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
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

  private boolean _jspx_meth_portlet_005fnamespace_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f4 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f4.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f4 = _jspx_th_portlet_005fnamespace_005f4.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f4);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f5 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f5.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f5 = _jspx_th_portlet_005fnamespace_005f5.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f5);
    return false;
  }
}
