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

public final class view_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(27);
    _jspx_dependants.add("/html/portlet/journal_content/init.jsp");
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
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dlist;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005flanguage_0026_005flanguageIds_005fdisplayStyle_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fpage_002diterator_0026_005furl_005ftype_005ftotal_005fmaxPages_005fdelta_005fcurParam_005fcur_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fratings_0026_005fclassPK_005fclassName_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fdiscussion_0026_005fuserId_005fsubject_005fredirect_005fratingsEnabled_005fformAction_005fclassPK_005fclassName_005fnobody;

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
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dlist = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005flanguage_0026_005flanguageIds_005fdisplayStyle_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fpage_002diterator_0026_005furl_005ftype_005ftotal_005fmaxPages_005fdelta_005fcurParam_005fcur_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fratings_0026_005fclassPK_005fclassName_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fdiscussion_0026_005fuserId_005fsubject_005fredirect_005fratingsEnabled_005fformAction_005fclassPK_005fclassName_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005fc_005fotherwise.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fscript.release();
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dlist.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005flanguage_0026_005flanguageIds_005fdisplayStyle_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fpage_002diterator_0026_005furl_005ftype_005ftotal_005fmaxPages_005fdelta_005fcurParam_005fcur_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName.release();
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fratings_0026_005fclassPK_005fclassName_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fdiscussion_0026_005fuserId_005fsubject_005fredirect_005fratingsEnabled_005fformAction_005fclassPK_005fclassName_005fnobody.release();
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

JournalArticleDisplay articleDisplay = (JournalArticleDisplay)request.getAttribute(WebKeys.JOURNAL_ARTICLE_DISPLAY);

boolean print = ParamUtil.getString(request, "viewMode").equals(Constants.PRINT);

if (articleDisplay != null) {
	AssetEntryServiceUtil.incrementViewCounter(JournalArticle.class.getName(), articleDisplay.getResourcePrimKey());
}

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
          // /html/portlet/journal_content/view.jsp(30,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fwhen_005f0.setTest( themeDisplay.isStateExclusive() );
          int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
          if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

		RuntimeLogic portletLogic = new PortletLogic(application, request, response, renderRequest, renderResponse);
		RuntimeLogic actionURLLogic = new ActionURLLogic(renderResponse);
		RuntimeLogic renderURLLogic = new RenderURLLogic(renderResponse);

		String content = articleDisplay.getContent();

		content = RuntimePortletUtil.processXML(request, content, portletLogic);
		content = RuntimePortletUtil.processXML(request, content, actionURLLogic);
		content = RuntimePortletUtil.processXML(request, content, renderURLLogic);
		
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              out.print( content );
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
          //  c:otherwise
          org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
          _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
          _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
          int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
          if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
              int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
              if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f1 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                  // /html/portlet/journal_content/view.jsp(48,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f1.setTest( articleDisplay != null );
                  int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");

				RuntimeLogic portletLogic = new PortletLogic(application, request, response, renderRequest, renderResponse);
				RuntimeLogic actionURLLogic = new ActionURLLogic(renderResponse);
				RuntimeLogic renderURLLogic = new RenderURLLogic(renderResponse);

				String content = articleDisplay.getContent();

				content = RuntimePortletUtil.processXML(request, content, portletLogic);
				content = RuntimePortletUtil.processXML(request, content, actionURLLogic);
				content = RuntimePortletUtil.processXML(request, content, renderURLLogic);

				PortletURL portletURL = renderResponse.createRenderURL();
				
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                      // /html/portlet/journal_content/view.jsp(64,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f0.setTest( enableConversions || enablePrint || (showAvailableLocales && (articleDisplay.getAvailableLocales().length > 1)) );
                      int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
                      if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t<div class=\"user-actions\">\n");
                          out.write("\t\t\t\t\t\t");
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
                          // /html/portlet/journal_content/view.jsp(66,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f1.setTest( enablePrint );
                          int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
                          if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t");
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f2 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
                              int _jspx_eval_c_005fchoose_005f2 = _jspx_th_c_005fchoose_005f2.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f2 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
                              // /html/portlet/journal_content/view.jsp(68,8) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f2.setTest( print );
                              int _jspx_eval_c_005fwhen_005f2 = _jspx_th_c_005fwhen_005f2.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t<div class=\"print-action\">\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
                              //  liferay-ui:icon
                              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f0 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                              _jspx_th_liferay_002dui_005ficon_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ficon_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                              // /html/portlet/journal_content/view.jsp(70,10) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f0.setImage("print");
                              // /html/portlet/journal_content/view.jsp(70,10) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f0.setLabel( true );
                              // /html/portlet/journal_content/view.jsp(70,10) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f0.setMessage( LanguageUtil.format(pageContext, "print-x-x", new Object[] {"aui-helper-hidden-accessible", articleDisplay.getTitle()}) );
                              // /html/portlet/journal_content/view.jsp(70,10) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f0.setUrl("javascript:print();");
                              int _jspx_eval_liferay_002dui_005ficon_005f0 = _jspx_th_liferay_002dui_005ficon_005f0.doStartTag();
                              if (_jspx_th_liferay_002dui_005ficon_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t</div>\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
                              if (_jspx_meth_aui_005fscript_005f0(_jspx_th_c_005fwhen_005f2, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t\t\t");
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f1 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
                              int _jspx_eval_c_005fotherwise_005f1 = _jspx_th_c_005fotherwise_005f1.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t");

									PortletURL printPageURL = renderResponse.createRenderURL();

									printPageURL.setWindowState(LiferayWindowState.POP_UP);

									printPageURL.setParameter("struts_action", "/journal_content/view");
									printPageURL.setParameter("groupId", String.valueOf(articleDisplay.getGroupId()));
									printPageURL.setParameter("articleId", articleDisplay.getArticleId());
									printPageURL.setParameter("viewMode", Constants.PRINT);
									
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t<div class=\"print-action\">\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
                              //  liferay-ui:icon
                              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f1 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                              _jspx_th_liferay_002dui_005ficon_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ficon_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
                              // /html/portlet/journal_content/view.jsp(96,10) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f1.setImage("print");
                              // /html/portlet/journal_content/view.jsp(96,10) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f1.setLabel( true );
                              // /html/portlet/journal_content/view.jsp(96,10) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f1.setMessage( LanguageUtil.format(pageContext, "print-x-x", new Object[] {"aui-helper-hidden-accessible", articleDisplay.getTitle()}) );
                              // /html/portlet/journal_content/view.jsp(96,10) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f1.setUrl( "javascript:" + renderResponse.getNamespace() + "printPage();" );
                              int _jspx_eval_liferay_002dui_005ficon_005f1 = _jspx_th_liferay_002dui_005ficon_005f1.doStartTag();
                              if (_jspx_th_liferay_002dui_005ficon_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t</div>\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t\t\t\t\tfunction ");
                              if (_jspx_meth_portlet_005fnamespace_005f0(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
                              return;
                              out.write("printPage() {\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\twindow.open('");
                              out.print( printPageURL );
                              out.write("', '', \"directories=0,height=480,left=80,location=1,menubar=1,resizable=1,scrollbars=yes,status=0,toolbar=0,top=180,width=640\");\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t}\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
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
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t\t");
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
                          out.write("\n");
                          out.write("\t\t\t\t\t\t");
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
                          // /html/portlet/journal_content/view.jsp(113,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f2.setTest( enableConversions && !print );
                          int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
                          if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t");

							PortletURL exportArticleURL = renderResponse.createActionURL();

							exportArticleURL.setWindowState(LiferayWindowState.EXCLUSIVE);

							exportArticleURL.setParameter("struts_action", "/journal_content/export_article");
							exportArticleURL.setParameter("groupId", String.valueOf(articleDisplay.getGroupId()));
							exportArticleURL.setParameter("articleId", articleDisplay.getArticleId());
							
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t<div class=\"export-actions\">\n");
                              out.write("\t\t\t\t\t\t\t\t");
                              //  liferay-ui:icon-list
                              com.liferay.taglib.ui.IconListTag _jspx_th_liferay_002dui_005ficon_002dlist_005f0 = (com.liferay.taglib.ui.IconListTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dlist.get(com.liferay.taglib.ui.IconListTag.class);
                              _jspx_th_liferay_002dui_005ficon_002dlist_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ficon_002dlist_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
                              int _jspx_eval_liferay_002dui_005ficon_002dlist_005f0 = _jspx_th_liferay_002dui_005ficon_002dlist_005f0.doStartTag();
                              if (_jspx_eval_liferay_002dui_005ficon_002dlist_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_liferay_002dui_005ficon_002dlist_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005ficon_002dlist_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005ficon_002dlist_005f0.doInitBody();
                              }
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t");

									for (String extension : extensions) {
										exportArticleURL.setParameter("targetExtension", extension);
									
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
                              //  liferay-ui:icon
                              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f2 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                              _jspx_th_liferay_002dui_005ficon_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ficon_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dlist_005f0);
                              // /html/portlet/journal_content/view.jsp(133,10) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f2.setImage( "../file_system/small/" + extension );
                              // /html/portlet/journal_content/view.jsp(133,10) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f2.setLabel( true );
                              // /html/portlet/journal_content/view.jsp(133,10) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f2.setMessage( LanguageUtil.format(pageContext, "x-convert-x-to-x", new Object[] {"aui-helper-hidden-accessible", articleDisplay.getTitle(), extension.toUpperCase()}) );
                              // /html/portlet/journal_content/view.jsp(133,10) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f2.setUrl( exportArticleURL.toString() );
                              // /html/portlet/journal_content/view.jsp(133,10) name = method type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f2.setMethod("get");
                              int _jspx_eval_liferay_002dui_005ficon_005f2 = _jspx_th_liferay_002dui_005ficon_005f2.doStartTag();
                              if (_jspx_th_liferay_002dui_005ficon_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f2);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t");

									}
									
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005ficon_002dlist_005f0.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005ficon_002dlist_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005ficon_002dlist_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dlist.reuse(_jspx_th_liferay_002dui_005ficon_002dlist_005f0);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dlist.reuse(_jspx_th_liferay_002dui_005ficon_002dlist_005f0);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t</div>\n");
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
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
                          // /html/portlet/journal_content/view.jsp(149,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f3.setTest( showAvailableLocales && !print );
                          int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
                          if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t");

							String[] availableLocales = articleDisplay.getAvailableLocales();
							
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
                              // /html/portlet/journal_content/view.jsp(155,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f4.setTest( availableLocales.length > 1 );
                              int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
                              if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f5 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
                              // /html/portlet/journal_content/view.jsp(156,8) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f5.setTest( enableConversions || enablePrint );
                              int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
                              if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t<div class=\"locale-separator\"> </div>\n");
                              out.write("\t\t\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t\t\t<div class=\"locale-actions\">\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
                              //  liferay-ui:language
                              com.liferay.taglib.ui.LanguageTag _jspx_th_liferay_002dui_005flanguage_005f0 = (com.liferay.taglib.ui.LanguageTag) _005fjspx_005ftagPool_005fliferay_002dui_005flanguage_0026_005flanguageIds_005fdisplayStyle_005fnobody.get(com.liferay.taglib.ui.LanguageTag.class);
                              _jspx_th_liferay_002dui_005flanguage_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005flanguage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
                              // /html/portlet/journal_content/view.jsp(161,9) name = languageIds type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005flanguage_005f0.setLanguageIds( availableLocales );
                              // /html/portlet/journal_content/view.jsp(161,9) name = displayStyle type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005flanguage_005f0.setDisplayStyle( 0 );
                              int _jspx_eval_liferay_002dui_005flanguage_005f0 = _jspx_th_liferay_002dui_005flanguage_005f0.doStartTag();
                              if (_jspx_th_liferay_002dui_005flanguage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005flanguage_0026_005flanguageIds_005fdisplayStyle_005fnobody.reuse(_jspx_th_liferay_002dui_005flanguage_005f0);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005flanguage_0026_005flanguageIds_005fdisplayStyle_005fnobody.reuse(_jspx_th_liferay_002dui_005flanguage_005f0);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t</div>\n");
                              out.write("\t\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t");
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
                          out.write("\t\t\t\t\t</div>\n");
                          out.write("\t\t\t\t");
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
                      out.write("\t\t\t\t<div class=\"journal-content-article\" id=\"article_");
                      out.print( articleDisplay.getCompanyId() );
                      out.write('_');
                      out.print( articleDisplay.getGroupId() );
                      out.write('_');
                      out.print( articleDisplay.getArticleId() );
                      out.write('_');
                      out.print( articleDisplay.getVersion() );
                      out.write("\">\n");
                      out.write("\t\t\t\t\t");
                      out.print( content );
                      out.write("\n");
                      out.write("\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f6 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f6.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                      // /html/portlet/journal_content/view.jsp(172,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f6.setTest( articleDisplay.isPaginate() );
                      int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
                      if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  liferay-ui:page-iterator
                          com.liferay.taglib.ui.PageIteratorTag _jspx_th_liferay_002dui_005fpage_002diterator_005f0 = (com.liferay.taglib.ui.PageIteratorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpage_002diterator_0026_005furl_005ftype_005ftotal_005fmaxPages_005fdelta_005fcurParam_005fcur_005fnobody.get(com.liferay.taglib.ui.PageIteratorTag.class);
                          _jspx_th_liferay_002dui_005fpage_002diterator_005f0.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dui_005fpage_002diterator_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
                          // /html/portlet/journal_content/view.jsp(173,5) name = cur type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fpage_002diterator_005f0.setCur( articleDisplay.getCurrentPage() );
                          // /html/portlet/journal_content/view.jsp(173,5) name = curParam type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fpage_002diterator_005f0.setCurParam( "page" );
                          // /html/portlet/journal_content/view.jsp(173,5) name = delta type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fpage_002diterator_005f0.setDelta( 1 );
                          // /html/portlet/journal_content/view.jsp(173,5) name = maxPages type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fpage_002diterator_005f0.setMaxPages( 25 );
                          // /html/portlet/journal_content/view.jsp(173,5) name = total type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fpage_002diterator_005f0.setTotal( articleDisplay.getNumberOfPages() );
                          // /html/portlet/journal_content/view.jsp(173,5) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fpage_002diterator_005f0.setType("article");
                          // /html/portlet/journal_content/view.jsp(173,5) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fpage_002diterator_005f0.setUrl( portletURL.toString() );
                          int _jspx_eval_liferay_002dui_005fpage_002diterator_005f0 = _jspx_th_liferay_002dui_005fpage_002diterator_005f0.doStartTag();
                          if (_jspx_th_liferay_002dui_005fpage_002diterator_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fliferay_002dui_005fpage_002diterator_0026_005furl_005ftype_005ftotal_005fmaxPages_005fdelta_005fcurParam_005fcur_005fnobody.reuse(_jspx_th_liferay_002dui_005fpage_002diterator_005f0);
                            return;
                          }
                          _005fjspx_005ftagPool_005fliferay_002dui_005fpage_002diterator_0026_005furl_005ftype_005ftotal_005fmaxPages_005fdelta_005fcurParam_005fcur_005fnobody.reuse(_jspx_th_liferay_002dui_005fpage_002diterator_005f0);
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t<br />\n");
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
                  org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f2 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                  _jspx_th_c_005fotherwise_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fotherwise_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                  int _jspx_eval_c_005fotherwise_005f2 = _jspx_th_c_005fotherwise_005f2.doStartTag();
                  if (_jspx_eval_c_005fotherwise_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");

				renderRequest.setAttribute(WebKeys.PORTLET_CONFIGURATOR_VISIBILITY, Boolean.TRUE);
				
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t<br />\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  c:choose
                      org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f3 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                      _jspx_th_c_005fchoose_005f3.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fchoose_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f2);
                      int _jspx_eval_c_005fchoose_005f3 = _jspx_th_c_005fchoose_005f3.doStartTag();
                      if (_jspx_eval_c_005fchoose_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  c:when
                          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f3 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                          _jspx_th_c_005fwhen_005f3.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fwhen_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
                          // /html/portlet/journal_content/view.jsp(195,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fwhen_005f3.setTest( Validator.isNull(articleId) );
                          int _jspx_eval_c_005fwhen_005f3 = _jspx_th_c_005fwhen_005f3.doStartTag();
                          if (_jspx_eval_c_005fwhen_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t<div class=\"portlet-msg-info\">\n");
                              out.write("\t\t\t\t\t\t\t");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_th_c_005fwhen_005f3, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t\t\t\t</div>\n");
                              out.write("\t\t\t\t\t");
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
                          out.write("\t\t\t\t\t");
                          //  c:otherwise
                          org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f3 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                          _jspx_th_c_005fotherwise_005f3.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fotherwise_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
                          int _jspx_eval_c_005fotherwise_005f3 = _jspx_th_c_005fotherwise_005f3.doStartTag();
                          if (_jspx_eval_c_005fotherwise_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");

						JournalArticle article = null;

						try {
							article = JournalArticleLocalServiceUtil.getLatestArticle(scopeGroupId, articleId, WorkflowConstants.STATUS_ANY);

							if (article.isExpired()) {
						
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t<div class=\"portlet-msg-alert\">\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
                              out.print( LanguageUtil.format(pageContext, "x-is-expired", article.getTitle()) );
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t</div>\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");

							}
							else if (!article.isApproved()) {
						
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t");
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f4 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f3);
                              int _jspx_eval_c_005fchoose_005f4 = _jspx_th_c_005fchoose_005f4.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f4 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f4);
                              // /html/portlet/journal_content/view.jsp(221,9) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f4.setTest( JournalArticlePermission.contains(permissionChecker, article, ActionKeys.UPDATE) );
                              int _jspx_eval_c_005fwhen_005f4 = _jspx_th_c_005fwhen_005f4.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
                              //  liferay-portlet:renderURL
                              com.liferay.taglib.portlet.RenderURLTag _jspx_th_liferay_002dportlet_005frenderURL_005f0 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName.get(com.liferay.taglib.portlet.RenderURLTag.class);
                              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                              // /html/portlet/journal_content/view.jsp(222,10) name = windowState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setWindowState( WindowState.MAXIMIZED.toString() );
                              // /html/portlet/journal_content/view.jsp(222,10) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setVar("editURL");
                              // /html/portlet/journal_content/view.jsp(222,10) name = portletName type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setPortletName( PortletKeys.JOURNAL );
                              int _jspx_eval_liferay_002dportlet_005frenderURL_005f0 = _jspx_th_liferay_002dportlet_005frenderURL_005f0.doStartTag();
                              if (_jspx_eval_liferay_002dportlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_liferay_002dportlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dportlet_005frenderURL_005f0.doInitBody();
                              }
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              if (_jspx_meth_portlet_005fparam_005f0(_jspx_th_liferay_002dportlet_005frenderURL_005f0, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f1 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f0);
                              // /html/portlet/journal_content/view.jsp(224,11) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f1.setName("redirect");
                              // /html/portlet/journal_content/view.jsp(224,11) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f1.setValue( currentURL );
                              int _jspx_eval_portlet_005fparam_005f1 = _jspx_th_portlet_005fparam_005f1.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f2 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f0);
                              // /html/portlet/journal_content/view.jsp(225,11) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f2.setName("referringPortletResource");
                              // /html/portlet/journal_content/view.jsp(225,11) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f2.setValue( PortletKeys.JOURNAL_CONTENT );
                              int _jspx_eval_portlet_005fparam_005f2 = _jspx_th_portlet_005fparam_005f2.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f3 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f0);
                              // /html/portlet/journal_content/view.jsp(226,11) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f3.setName("groupId");
                              // /html/portlet/journal_content/view.jsp(226,11) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f3.setValue( String.valueOf(article.getGroupId()) );
                              int _jspx_eval_portlet_005fparam_005f3 = _jspx_th_portlet_005fparam_005f3.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f4 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f0);
                              // /html/portlet/journal_content/view.jsp(227,11) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f4.setName("articleId");
                              // /html/portlet/journal_content/view.jsp(227,11) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f4.setValue( article.getArticleId() );
                              int _jspx_eval_portlet_005fparam_005f4 = _jspx_th_portlet_005fparam_005f4.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f5 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f5.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f0);
                              // /html/portlet/journal_content/view.jsp(228,11) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f5.setName("version");
                              // /html/portlet/journal_content/view.jsp(228,11) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f5.setValue( String.valueOf(article.getVersion()) );
                              int _jspx_eval_portlet_005fparam_005f5 = _jspx_th_portlet_005fparam_005f5.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dportlet_005frenderURL_005f0.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dportlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dportlet_005frenderURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f0);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f0);
                              java.lang.String editURL = null;
                              editURL = (java.lang.String) _jspx_page_context.findAttribute("editURL");
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t<div class=\"portlet-msg-alert\">\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t<a href=\"");
                              out.print( editURL );
                              out.write("\">\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
                              out.print( LanguageUtil.format(pageContext, "x-is-not-approved", article.getTitle()) );
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t</a>\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t</div>\n");
                              out.write("\t\t\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t\t\t");
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f4 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f4);
                              int _jspx_eval_c_005fotherwise_005f4 = _jspx_th_c_005fotherwise_005f4.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t<div class=\"portlet-msg-alert\">\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
                              out.print( LanguageUtil.format(pageContext, "x-is-not-approved", article.getTitle()) );
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t</div>\n");
                              out.write("\t\t\t\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f4.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fotherwise_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f4);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f4);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t");
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
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");

							}
						}
						catch (NoSuchArticleException nsae) {
						
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t<div class=\"portlet-msg-error\">\n");
                              out.write("\t\t\t\t\t\t\t\t");
                              out.print( LanguageUtil.get(pageContext, "the-selected-web-content-no-longer-exists") );
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t</div>\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");

						}
						
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t");
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
                          out.write("\n");
                          out.write("\t\t\t\t");
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
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

		Group stageableGroup = themeDisplay.getScopeGroup();

		if (themeDisplay.getScopeGroup().isLayout()) {
			stageableGroup = layout.getGroup();
		}

		JournalTemplate template = null;

		if (articleDisplay != null && Validator.isNotNull(articleDisplay.getTemplateId())) {
			try {
				template = JournalTemplateLocalServiceUtil.getTemplate(articleDisplay.getGroupId(), articleDisplay.getTemplateId());
			}
			catch (NoSuchTemplateException nste) {
				template = JournalTemplateLocalServiceUtil.getTemplate(themeDisplay.getCompanyGroupId(), articleDisplay.getTemplateId());
			}
		}

		boolean staged = stageableGroup.hasStagingGroup();

		boolean showEditArticleIcon = (articleDisplay != null) && JournalArticlePermission.contains(permissionChecker, articleDisplay.getGroupId(), articleDisplay.getArticleId(), ActionKeys.UPDATE);
		boolean showEditTemplateIcon = (template != null) && JournalTemplatePermission.contains(permissionChecker, template.getGroupId(), template.getTemplateId(), ActionKeys.UPDATE);
		boolean showSelectArticleIcon = PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), ActionKeys.CONFIGURATION);
		boolean showAddArticleIcon = PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), ActionKeys.CONFIGURATION) && JournalPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_ARTICLE);
		boolean showIconsActions = themeDisplay.isSignedIn() && ((showEditArticleIcon || showEditTemplateIcon || showSelectArticleIcon || showAddArticleIcon) && !staged);
		
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f7 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f7.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
              // /html/portlet/journal_content/view.jsp(290,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f7.setTest( showIconsActions && !print );
              int _jspx_eval_c_005fif_005f7 = _jspx_th_c_005fif_005f7.doStartTag();
              if (_jspx_eval_c_005fif_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t<div class=\"lfr-meta-actions icons-container\">\n");
                  out.write("\t\t\t\t<div class=\"icon-actions\">\n");
                  out.write("\t\t\t\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f8 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f8.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
                  // /html/portlet/journal_content/view.jsp(293,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f8.setTest( showEditArticleIcon );
                  int _jspx_eval_c_005fif_005f8 = _jspx_th_c_005fif_005f8.doStartTag();
                  if (_jspx_eval_c_005fif_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  liferay-portlet:renderURL
                      com.liferay.taglib.portlet.RenderURLTag _jspx_th_liferay_002dportlet_005frenderURL_005f1 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName.get(com.liferay.taglib.portlet.RenderURLTag.class);
                      _jspx_th_liferay_002dportlet_005frenderURL_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dportlet_005frenderURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
                      // /html/portlet/journal_content/view.jsp(294,6) name = windowState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dportlet_005frenderURL_005f1.setWindowState( WindowState.MAXIMIZED.toString() );
                      // /html/portlet/journal_content/view.jsp(294,6) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dportlet_005frenderURL_005f1.setVar("editURL");
                      // /html/portlet/journal_content/view.jsp(294,6) name = portletName type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dportlet_005frenderURL_005f1.setPortletName( PortletKeys.JOURNAL );
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
                          if (_jspx_meth_portlet_005fparam_005f6(_jspx_th_liferay_002dportlet_005frenderURL_005f1, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f7 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f7.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f1);
                          // /html/portlet/journal_content/view.jsp(296,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f7.setName("redirect");
                          // /html/portlet/journal_content/view.jsp(296,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f7.setValue( currentURL );
                          int _jspx_eval_portlet_005fparam_005f7 = _jspx_th_portlet_005fparam_005f7.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f8 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f8.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f1);
                          // /html/portlet/journal_content/view.jsp(297,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f8.setName("referringPortletResource");
                          // /html/portlet/journal_content/view.jsp(297,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f8.setValue( PortletKeys.JOURNAL_CONTENT );
                          int _jspx_eval_portlet_005fparam_005f8 = _jspx_th_portlet_005fparam_005f8.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f9 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f9.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f1);
                          // /html/portlet/journal_content/view.jsp(298,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f9.setName("groupId");
                          // /html/portlet/journal_content/view.jsp(298,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f9.setValue( String.valueOf(articleDisplay.getGroupId()) );
                          int _jspx_eval_portlet_005fparam_005f9 = _jspx_th_portlet_005fparam_005f9.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f10 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f10.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f1);
                          // /html/portlet/journal_content/view.jsp(299,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f10.setName("articleId");
                          // /html/portlet/journal_content/view.jsp(299,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f10.setValue( articleDisplay.getArticleId() );
                          int _jspx_eval_portlet_005fparam_005f10 = _jspx_th_portlet_005fparam_005f10.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f10);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f10);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f11 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f11.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f1);
                          // /html/portlet/journal_content/view.jsp(300,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f11.setName("version");
                          // /html/portlet/journal_content/view.jsp(300,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f11.setValue( String.valueOf(articleDisplay.getVersion()) );
                          int _jspx_eval_portlet_005fparam_005f11 = _jspx_th_portlet_005fparam_005f11.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f11);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f11);
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
                        _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f1);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f1);
                      java.lang.String editURL = null;
                      editURL = (java.lang.String) _jspx_page_context.findAttribute("editURL");
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  liferay-ui:icon
                      com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f3 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                      _jspx_th_liferay_002dui_005ficon_005f3.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ficon_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
                      // /html/portlet/journal_content/view.jsp(303,6) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f3.setImage("edit");
                      // /html/portlet/journal_content/view.jsp(303,6) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f3.setMessage("edit-web-content");
                      // /html/portlet/journal_content/view.jsp(303,6) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f3.setUrl( editURL );
                      int _jspx_eval_liferay_002dui_005ficon_005f3 = _jspx_th_liferay_002dui_005ficon_005f3.doStartTag();
                      if (_jspx_th_liferay_002dui_005ficon_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f3);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f3);
                      out.write("\n");
                      out.write("\t\t\t\t\t");
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
                  out.write("\t\t\t\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f9 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f9.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
                  // /html/portlet/journal_content/view.jsp(310,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f9.setTest( showEditTemplateIcon );
                  int _jspx_eval_c_005fif_005f9 = _jspx_th_c_005fif_005f9.doStartTag();
                  if (_jspx_eval_c_005fif_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  liferay-portlet:renderURL
                      com.liferay.taglib.portlet.RenderURLTag _jspx_th_liferay_002dportlet_005frenderURL_005f2 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName.get(com.liferay.taglib.portlet.RenderURLTag.class);
                      _jspx_th_liferay_002dportlet_005frenderURL_005f2.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dportlet_005frenderURL_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f9);
                      // /html/portlet/journal_content/view.jsp(311,6) name = windowState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dportlet_005frenderURL_005f2.setWindowState( WindowState.MAXIMIZED.toString() );
                      // /html/portlet/journal_content/view.jsp(311,6) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dportlet_005frenderURL_005f2.setVar("editTemplateURL");
                      // /html/portlet/journal_content/view.jsp(311,6) name = portletName type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dportlet_005frenderURL_005f2.setPortletName( PortletKeys.JOURNAL );
                      int _jspx_eval_liferay_002dportlet_005frenderURL_005f2 = _jspx_th_liferay_002dportlet_005frenderURL_005f2.doStartTag();
                      if (_jspx_eval_liferay_002dportlet_005frenderURL_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_liferay_002dportlet_005frenderURL_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_liferay_002dportlet_005frenderURL_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_liferay_002dportlet_005frenderURL_005f2.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          if (_jspx_meth_portlet_005fparam_005f12(_jspx_th_liferay_002dportlet_005frenderURL_005f2, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f13 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f13.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f2);
                          // /html/portlet/journal_content/view.jsp(313,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f13.setName("redirect");
                          // /html/portlet/journal_content/view.jsp(313,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f13.setValue( currentURL );
                          int _jspx_eval_portlet_005fparam_005f13 = _jspx_th_portlet_005fparam_005f13.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f13);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f13);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f14 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f14.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f2);
                          // /html/portlet/journal_content/view.jsp(314,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f14.setName("referringPortletResource");
                          // /html/portlet/journal_content/view.jsp(314,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f14.setValue( PortletKeys.JOURNAL_CONTENT );
                          int _jspx_eval_portlet_005fparam_005f14 = _jspx_th_portlet_005fparam_005f14.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f14);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f14);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f15 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f15.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f2);
                          // /html/portlet/journal_content/view.jsp(315,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f15.setName("groupId");
                          // /html/portlet/journal_content/view.jsp(315,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f15.setValue( String.valueOf(template.getGroupId()) );
                          int _jspx_eval_portlet_005fparam_005f15 = _jspx_th_portlet_005fparam_005f15.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f15);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f15);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f16 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f16.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f2);
                          // /html/portlet/journal_content/view.jsp(316,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f16.setName("templateId");
                          // /html/portlet/journal_content/view.jsp(316,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f16.setValue( template.getTemplateId() );
                          int _jspx_eval_portlet_005fparam_005f16 = _jspx_th_portlet_005fparam_005f16.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f16);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f16);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t");
                          int evalDoAfterBody = _jspx_th_liferay_002dportlet_005frenderURL_005f2.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_liferay_002dportlet_005frenderURL_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.popBody();
                        }
                      }
                      if (_jspx_th_liferay_002dportlet_005frenderURL_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f2);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f2);
                      java.lang.String editTemplateURL = null;
                      editTemplateURL = (java.lang.String) _jspx_page_context.findAttribute("editTemplateURL");
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  liferay-ui:icon
                      com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f4 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                      _jspx_th_liferay_002dui_005ficon_005f4.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ficon_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f9);
                      // /html/portlet/journal_content/view.jsp(319,6) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f4.setImage("../file_system/small/xml");
                      // /html/portlet/journal_content/view.jsp(319,6) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f4.setMessage("edit-template");
                      // /html/portlet/journal_content/view.jsp(319,6) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f4.setUrl( editTemplateURL );
                      int _jspx_eval_liferay_002dui_005ficon_005f4 = _jspx_th_liferay_002dui_005ficon_005f4.doStartTag();
                      if (_jspx_th_liferay_002dui_005ficon_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f4);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f4);
                      out.write("\n");
                      out.write("\t\t\t\t\t");
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
                  out.write("\t\t\t\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f10 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f10.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
                  // /html/portlet/journal_content/view.jsp(326,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f10.setTest( showSelectArticleIcon );
                  int _jspx_eval_c_005fif_005f10 = _jspx_th_c_005fif_005f10.doStartTag();
                  if (_jspx_eval_c_005fif_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  liferay-ui:icon
                      com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f5 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                      _jspx_th_liferay_002dui_005ficon_005f5.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ficon_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f10);
                      // /html/portlet/journal_content/view.jsp(327,6) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f5.setCssClass("portlet-configuration");
                      // /html/portlet/journal_content/view.jsp(327,6) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f5.setImage("configuration");
                      // /html/portlet/journal_content/view.jsp(327,6) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f5.setMessage("select-web-content");
                      // /html/portlet/journal_content/view.jsp(327,6) name = method type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f5.setMethod("get");
                      // /html/portlet/journal_content/view.jsp(327,6) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f5.setUrl( portletDisplay.getURLConfiguration() );
                      int _jspx_eval_liferay_002dui_005ficon_005f5 = _jspx_th_liferay_002dui_005ficon_005f5.doStartTag();
                      if (_jspx_th_liferay_002dui_005ficon_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f5);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmethod_005fmessage_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f5);
                      out.write("\n");
                      out.write("\t\t\t\t\t");
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
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f11 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f11.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
                  // /html/portlet/journal_content/view.jsp(336,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f11.setTest( showAddArticleIcon );
                  int _jspx_eval_c_005fif_005f11 = _jspx_th_c_005fif_005f11.doStartTag();
                  if (_jspx_eval_c_005fif_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  liferay-portlet:renderURL
                      com.liferay.taglib.portlet.RenderURLTag _jspx_th_liferay_002dportlet_005frenderURL_005f3 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName.get(com.liferay.taglib.portlet.RenderURLTag.class);
                      _jspx_th_liferay_002dportlet_005frenderURL_005f3.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dportlet_005frenderURL_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f11);
                      // /html/portlet/journal_content/view.jsp(337,6) name = windowState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dportlet_005frenderURL_005f3.setWindowState( WindowState.MAXIMIZED.toString() );
                      // /html/portlet/journal_content/view.jsp(337,6) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dportlet_005frenderURL_005f3.setVar("addArticleURL");
                      // /html/portlet/journal_content/view.jsp(337,6) name = portletName type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dportlet_005frenderURL_005f3.setPortletName( PortletKeys.JOURNAL );
                      int _jspx_eval_liferay_002dportlet_005frenderURL_005f3 = _jspx_th_liferay_002dportlet_005frenderURL_005f3.doStartTag();
                      if (_jspx_eval_liferay_002dportlet_005frenderURL_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_liferay_002dportlet_005frenderURL_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_liferay_002dportlet_005frenderURL_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_liferay_002dportlet_005frenderURL_005f3.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          if (_jspx_meth_portlet_005fparam_005f17(_jspx_th_liferay_002dportlet_005frenderURL_005f3, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f18 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f18.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f3);
                          // /html/portlet/journal_content/view.jsp(339,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f18.setName("portletResource");
                          // /html/portlet/journal_content/view.jsp(339,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f18.setValue( portletDisplay.getId() );
                          int _jspx_eval_portlet_005fparam_005f18 = _jspx_th_portlet_005fparam_005f18.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f18);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f18);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f19 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f19.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f3);
                          // /html/portlet/journal_content/view.jsp(340,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f19.setName("redirect");
                          // /html/portlet/journal_content/view.jsp(340,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f19.setValue( currentURL );
                          int _jspx_eval_portlet_005fparam_005f19 = _jspx_th_portlet_005fparam_005f19.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f19);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f19);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f20 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f20.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f3);
                          // /html/portlet/journal_content/view.jsp(341,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f20.setName("referringPortletResource");
                          // /html/portlet/journal_content/view.jsp(341,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f20.setValue( PortletKeys.JOURNAL_CONTENT );
                          int _jspx_eval_portlet_005fparam_005f20 = _jspx_th_portlet_005fparam_005f20.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f20);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f20);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t");
                          int evalDoAfterBody = _jspx_th_liferay_002dportlet_005frenderURL_005f3.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_liferay_002dportlet_005frenderURL_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.popBody();
                        }
                      }
                      if (_jspx_th_liferay_002dportlet_005frenderURL_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f3);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fvar_005fportletName.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f3);
                      java.lang.String addArticleURL = null;
                      addArticleURL = (java.lang.String) _jspx_page_context.findAttribute("addArticleURL");
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  liferay-ui:icon
                      com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f6 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                      _jspx_th_liferay_002dui_005ficon_005f6.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ficon_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f11);
                      // /html/portlet/journal_content/view.jsp(344,6) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f6.setImage("add_article");
                      // /html/portlet/journal_content/view.jsp(344,6) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f6.setMessage("add-web-content");
                      // /html/portlet/journal_content/view.jsp(344,6) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f6.setUrl( addArticleURL );
                      int _jspx_eval_liferay_002dui_005ficon_005f6 = _jspx_th_liferay_002dui_005ficon_005f6.doStartTag();
                      if (_jspx_th_liferay_002dui_005ficon_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f6);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f6);
                      out.write("\n");
                      out.write("\t\t\t\t\t");
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
                  out.write("\t\t\t\t</div>\n");
                  out.write("\t\t\t</div>\n");
                  out.write("\t\t");
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
              out.write("\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f12 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f12.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
              // /html/portlet/journal_content/view.jsp(354,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f12.setTest( (articleDisplay != null) );
              int _jspx_eval_c_005fif_005f12 = _jspx_th_c_005fif_005f12.doStartTag();
              if (_jspx_eval_c_005fif_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f13 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f13.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f12);
                  // /html/portlet/journal_content/view.jsp(355,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f13.setTest( enableRatings );
                  int _jspx_eval_c_005fif_005f13 = _jspx_th_c_005fif_005f13.doStartTag();
                  if (_jspx_eval_c_005fif_005f13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t<div class=\"taglib-ratings-wrapper\">\n");
                      out.write("\t\t\t\t\t");
                      //  liferay-ui:ratings
                      com.liferay.taglib.ui.RatingsTag _jspx_th_liferay_002dui_005fratings_005f0 = (com.liferay.taglib.ui.RatingsTag) _005fjspx_005ftagPool_005fliferay_002dui_005fratings_0026_005fclassPK_005fclassName_005fnobody.get(com.liferay.taglib.ui.RatingsTag.class);
                      _jspx_th_liferay_002dui_005fratings_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fratings_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f13);
                      // /html/portlet/journal_content/view.jsp(357,5) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fratings_005f0.setClassName( JournalArticle.class.getName() );
                      // /html/portlet/journal_content/view.jsp(357,5) name = classPK type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fratings_005f0.setClassPK( articleDisplay.getResourcePrimKey() );
                      int _jspx_eval_liferay_002dui_005fratings_005f0 = _jspx_th_liferay_002dui_005fratings_005f0.doStartTag();
                      if (_jspx_th_liferay_002dui_005fratings_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005fratings_0026_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fratings_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005fratings_0026_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fratings_005f0);
                      out.write("\n");
                      out.write("\t\t\t\t</div>\n");
                      out.write("\t\t\t");
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
                  out.write("\n");
                  out.write("\t\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f14 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f14.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f12);
                  // /html/portlet/journal_content/view.jsp(364,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f14.setTest( enableComments );
                  int _jspx_eval_c_005fif_005f14 = _jspx_th_c_005fif_005f14.doStartTag();
                  if (_jspx_eval_c_005fif_005f14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");

				int discussionMessagesCount = MBMessageLocalServiceUtil.getDiscussionMessagesCount(PortalUtil.getClassNameId(JournalArticle.class.getName()), articleDisplay.getResourcePrimKey(), WorkflowConstants.STATUS_APPROVED);
				
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f15 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f15.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f14);
                      // /html/portlet/journal_content/view.jsp(370,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f15.setTest( discussionMessagesCount > 0 );
                      int _jspx_eval_c_005fif_005f15 = _jspx_th_c_005fif_005f15.doStartTag();
                      if (_jspx_eval_c_005fif_005f15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          if (_jspx_meth_liferay_002dui_005fheader_005f0(_jspx_th_c_005fif_005f15, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\t\t\t\t");
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
                      out.write("\t\t\t\t");
                      //  portlet:actionURL
                      com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f0 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.get(com.liferay.taglib.portlet.ActionURLTag.class);
                      _jspx_th_portlet_005factionURL_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005factionURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f14);
                      // /html/portlet/journal_content/view.jsp(376,4) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005factionURL_005f0.setVar("discussionURL");
                      int _jspx_eval_portlet_005factionURL_005f0 = _jspx_th_portlet_005factionURL_005f0.doStartTag();
                      if (_jspx_eval_portlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_portlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_portlet_005factionURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_portlet_005factionURL_005f0.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          if (_jspx_meth_portlet_005fparam_005f21(_jspx_th_portlet_005factionURL_005f0, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\t\t\t\t");
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
                      java.lang.String discussionURL = null;
                      discussionURL = (java.lang.String) _jspx_page_context.findAttribute("discussionURL");
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  liferay-ui:discussion
                      com.liferay.taglib.ui.DiscussionTag _jspx_th_liferay_002dui_005fdiscussion_005f0 = (com.liferay.taglib.ui.DiscussionTag) _005fjspx_005ftagPool_005fliferay_002dui_005fdiscussion_0026_005fuserId_005fsubject_005fredirect_005fratingsEnabled_005fformAction_005fclassPK_005fclassName_005fnobody.get(com.liferay.taglib.ui.DiscussionTag.class);
                      _jspx_th_liferay_002dui_005fdiscussion_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fdiscussion_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f14);
                      // /html/portlet/journal_content/view.jsp(380,4) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fdiscussion_005f0.setClassName( JournalArticle.class.getName() );
                      // /html/portlet/journal_content/view.jsp(380,4) name = classPK type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fdiscussion_005f0.setClassPK( articleDisplay.getResourcePrimKey() );
                      // /html/portlet/journal_content/view.jsp(380,4) name = formAction type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fdiscussion_005f0.setFormAction( discussionURL );
                      // /html/portlet/journal_content/view.jsp(380,4) name = ratingsEnabled type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fdiscussion_005f0.setRatingsEnabled( enableCommentRatings );
                      // /html/portlet/journal_content/view.jsp(380,4) name = redirect type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fdiscussion_005f0.setRedirect( currentURL );
                      // /html/portlet/journal_content/view.jsp(380,4) name = subject type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fdiscussion_005f0.setSubject( articleDisplay.getTitle() );
                      // /html/portlet/journal_content/view.jsp(380,4) name = userId type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fdiscussion_005f0.setUserId( articleDisplay.getUserId() );
                      int _jspx_eval_liferay_002dui_005fdiscussion_005f0 = _jspx_th_liferay_002dui_005fdiscussion_005f0.doStartTag();
                      if (_jspx_th_liferay_002dui_005fdiscussion_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005fdiscussion_0026_005fuserId_005fsubject_005fredirect_005fratingsEnabled_005fformAction_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fdiscussion_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005fdiscussion_0026_005fuserId_005fsubject_005fredirect_005fratingsEnabled_005fformAction_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fdiscussion_005f0);
                      out.write("\n");
                      out.write("\t\t\t");
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
                  out.write('	');
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

  private boolean _jspx_meth_aui_005fscript_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:script
    com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript.get(com.liferay.taglib.aui.ScriptTag.class);
    _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fscript_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
    int _jspx_eval_aui_005fscript_005f0 = _jspx_th_aui_005fscript_005f0.doStartTag();
    if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_005fscript_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_005fscript_005f0.doInitBody();
      }
      do {
        out.write("\n");
        out.write("\t\t\t\t\t\t\t\t\t\tprint();\n");
        out.write("\t\t\t\t\t\t\t\t\t");
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
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f0 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f0 = _jspx_th_portlet_005fnamespace_005f0.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
    // /html/portlet/journal_content/view.jsp(197,7) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("select-existing-web-content-or-add-some-web-content-to-be-displayed-in-this-portlet");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dportlet_005frenderURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f0 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f0);
    // /html/portlet/journal_content/view.jsp(223,11) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setName("struts_action");
    // /html/portlet/journal_content/view.jsp(223,11) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setValue("/journal/edit_article");
    int _jspx_eval_portlet_005fparam_005f0 = _jspx_th_portlet_005fparam_005f0.doStartTag();
    if (_jspx_th_portlet_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dportlet_005frenderURL_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f6 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f6.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f1);
    // /html/portlet/journal_content/view.jsp(295,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f6.setName("struts_action");
    // /html/portlet/journal_content/view.jsp(295,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f6.setValue("/journal/edit_article");
    int _jspx_eval_portlet_005fparam_005f6 = _jspx_th_portlet_005fparam_005f6.doStartTag();
    if (_jspx_th_portlet_005fparam_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dportlet_005frenderURL_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f12 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f12.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f2);
    // /html/portlet/journal_content/view.jsp(312,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f12.setName("struts_action");
    // /html/portlet/journal_content/view.jsp(312,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f12.setValue("/journal/edit_template");
    int _jspx_eval_portlet_005fparam_005f12 = _jspx_th_portlet_005fparam_005f12.doStartTag();
    if (_jspx_th_portlet_005fparam_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f12);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f17(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dportlet_005frenderURL_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f17 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f17.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f3);
    // /html/portlet/journal_content/view.jsp(338,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f17.setName("struts_action");
    // /html/portlet/journal_content/view.jsp(338,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f17.setValue("/journal/edit_article");
    int _jspx_eval_portlet_005fparam_005f17 = _jspx_th_portlet_005fparam_005f17.doStartTag();
    if (_jspx_th_portlet_005fparam_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f17);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f17);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fheader_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f15, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:header
    com.liferay.taglib.ui.HeaderTag _jspx_th_liferay_002dui_005fheader_005f0 = (com.liferay.taglib.ui.HeaderTag) _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fnobody.get(com.liferay.taglib.ui.HeaderTag.class);
    _jspx_th_liferay_002dui_005fheader_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fheader_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f15);
    // /html/portlet/journal_content/view.jsp(371,5) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fheader_005f0.setTitle("comments");
    int _jspx_eval_liferay_002dui_005fheader_005f0 = _jspx_th_liferay_002dui_005fheader_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fheader_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fnobody.reuse(_jspx_th_liferay_002dui_005fheader_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fnobody.reuse(_jspx_th_liferay_002dui_005fheader_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f21(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f21 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f21.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
    // /html/portlet/journal_content/view.jsp(377,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f21.setName("struts_action");
    // /html/portlet/journal_content/view.jsp(377,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f21.setValue("/journal_content/edit_article_discussion");
    int _jspx_eval_portlet_005fparam_005f21 = _jspx_th_portlet_005fparam_005f21.doStartTag();
    if (_jspx_th_portlet_005fparam_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f21);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f21);
    return false;
  }
}
