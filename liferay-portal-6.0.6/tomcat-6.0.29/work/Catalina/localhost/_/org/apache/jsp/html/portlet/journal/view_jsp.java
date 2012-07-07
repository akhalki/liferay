package org.apache.jsp.html.portlet.journal;

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
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.XPath;
import com.liferay.portal.util.LayoutLister;
import com.liferay.portal.util.LayoutView;
import com.liferay.portlet.asset.NoSuchEntryException;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.util.DLUtil;
import com.liferay.portlet.journal.ArticleContentException;
import com.liferay.portlet.journal.ArticleDisplayDateException;
import com.liferay.portlet.journal.ArticleExpirationDateException;
import com.liferay.portlet.journal.ArticleIdException;
import com.liferay.portlet.journal.ArticleSmallImageNameException;
import com.liferay.portlet.journal.ArticleSmallImageSizeException;
import com.liferay.portlet.journal.ArticleTitleException;
import com.liferay.portlet.journal.ArticleTypeException;
import com.liferay.portlet.journal.ArticleVersionException;
import com.liferay.portlet.journal.DuplicateArticleIdException;
import com.liferay.portlet.journal.DuplicateFeedIdException;
import com.liferay.portlet.journal.DuplicateStructureIdException;
import com.liferay.portlet.journal.DuplicateTemplateIdException;
import com.liferay.portlet.journal.FeedContentFieldException;
import com.liferay.portlet.journal.FeedDescriptionException;
import com.liferay.portlet.journal.FeedIdException;
import com.liferay.portlet.journal.FeedNameException;
import com.liferay.portlet.journal.FeedTargetLayoutFriendlyUrlException;
import com.liferay.portlet.journal.FeedTargetPortletIdException;
import com.liferay.portlet.journal.NoSuchArticleException;
import com.liferay.portlet.journal.NoSuchStructureException;
import com.liferay.portlet.journal.NoSuchTemplateException;
import com.liferay.portlet.journal.RequiredStructureException;
import com.liferay.portlet.journal.RequiredTemplateException;
import com.liferay.portlet.journal.StructureDescriptionException;
import com.liferay.portlet.journal.StructureIdException;
import com.liferay.portlet.journal.StructureInheritanceException;
import com.liferay.portlet.journal.StructureNameException;
import com.liferay.portlet.journal.StructureXsdException;
import com.liferay.portlet.journal.TemplateDescriptionException;
import com.liferay.portlet.journal.TemplateIdException;
import com.liferay.portlet.journal.TemplateNameException;
import com.liferay.portlet.journal.TemplateSmallImageNameException;
import com.liferay.portlet.journal.TemplateSmallImageSizeException;
import com.liferay.portlet.journal.TemplateXslException;
import com.liferay.portlet.journal.action.EditArticleAction;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleConstants;
import com.liferay.portlet.journal.model.JournalArticleDisplay;
import com.liferay.portlet.journal.model.JournalArticleResource;
import com.liferay.portlet.journal.model.JournalFeed;
import com.liferay.portlet.journal.model.JournalFeedConstants;
import com.liferay.portlet.journal.model.JournalStructure;
import com.liferay.portlet.journal.model.JournalStructureConstants;
import com.liferay.portlet.journal.model.JournalTemplate;
import com.liferay.portlet.journal.model.JournalTemplateConstants;
import com.liferay.portlet.journal.model.impl.JournalArticleImpl;
import com.liferay.portlet.journal.search.ArticleDisplayTerms;
import com.liferay.portlet.journal.search.ArticleSearch;
import com.liferay.portlet.journal.search.ArticleSearchTerms;
import com.liferay.portlet.journal.search.FeedDisplayTerms;
import com.liferay.portlet.journal.search.FeedSearch;
import com.liferay.portlet.journal.search.FeedSearchTerms;
import com.liferay.portlet.journal.search.StructureDisplayTerms;
import com.liferay.portlet.journal.search.StructureSearch;
import com.liferay.portlet.journal.search.StructureSearchTerms;
import com.liferay.portlet.journal.search.TemplateDisplayTerms;
import com.liferay.portlet.journal.search.TemplateSearch;
import com.liferay.portlet.journal.search.TemplateSearchTerms;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleResourceLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalFeedLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalStructureLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalTemplateLocalServiceUtil;
import com.liferay.portlet.journal.service.permission.JournalArticlePermission;
import com.liferay.portlet.journal.service.permission.JournalFeedPermission;
import com.liferay.portlet.journal.service.permission.JournalPermission;
import com.liferay.portlet.journal.service.permission.JournalStructurePermission;
import com.liferay.portlet.journal.service.permission.JournalTemplatePermission;
import com.liferay.portlet.journal.util.JournalUtil;
import com.liferay.portlet.journal.webdav.JournalWebDAVStorageImpl;
import com.liferay.portlet.journalcontent.util.JournalContentUtil;
import com.liferay.util.RSSUtil;

public final class view_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(31);
    _jspx_dependants.add("/html/portlet/journal/init.jsp");
    _jspx_dependants.add("/html/portlet/init.jsp");
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/journal/article_search_results.jspf");
    _jspx_dependants.add("/html/portlet/journal/structure_search_results.jspf");
    _jspx_dependants.add("/html/portlet/journal/template_search_results.jspf");
    _jspx_dependants.add("/html/portlet/journal/feed_search_results.jspf");
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
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fnames_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_002drow;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fa_0026_005fhref;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005factionURL;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fnames_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_002drow = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fa_0026_005fhref = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005factionURL = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fnames_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005faui_005fbutton_002drow.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.release();
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.release();
    _005fjspx_005ftagPool_005faui_005fscript.release();
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005factionURL.release();
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
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");

PortalPreferences portalPrefs = PortletPreferencesFactoryUtil.getPortalPreferences(request);

Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
Format dateFormatDateTime = FastDateFormatFactoryUtil.getDateTime(locale, timeZone);

      out.write('\n');
      out.write('\n');

String tabs1 = ParamUtil.getString(request, "tabs1", "web-content");

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/journal/view");
portletURL.setParameter("tabs1", tabs1);

      out.write('\n');
      out.write('\n');
      //  aui:form
      com.liferay.taglib.aui.FormTag _jspx_th_aui_005fform_005f0 = (com.liferay.taglib.aui.FormTag) _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction.get(com.liferay.taglib.aui.FormTag.class);
      _jspx_th_aui_005fform_005f0.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fform_005f0.setParent(null);
      // /html/portlet/journal/view.jsp(28,0) name = action type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fform_005f0.setAction( portletURL.toString() );
      // /html/portlet/journal/view.jsp(28,0) null
      _jspx_th_aui_005fform_005f0.setDynamicAttribute(null, "method", new String("post"));
      // /html/portlet/journal/view.jsp(28,0) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
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
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f0 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /html/portlet/journal/view.jsp(29,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f0.setName( Constants.CMD );
          // /html/portlet/journal/view.jsp(29,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f0.setType("hidden");
          int _jspx_eval_aui_005finput_005f0 = _jspx_th_aui_005finput_005f0.doStartTag();
          if (_jspx_th_aui_005finput_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  liferay-ui:tabs
          com.liferay.taglib.ui.TabsTag _jspx_th_liferay_002dui_005ftabs_005f0 = (com.liferay.taglib.ui.TabsTag) _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fnames_005fnobody.get(com.liferay.taglib.ui.TabsTag.class);
          _jspx_th_liferay_002dui_005ftabs_005f0.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005ftabs_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /html/portlet/journal/view.jsp(31,1) name = names type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ftabs_005f0.setNames("web-content,structures,templates,feeds,recent");
          // /html/portlet/journal/view.jsp(31,1) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ftabs_005f0.setUrl( portletURL.toString() );
          int _jspx_eval_liferay_002dui_005ftabs_005f0 = _jspx_th_liferay_002dui_005ftabs_005f0.doStartTag();
          if (_jspx_th_liferay_002dui_005ftabs_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fnames_005fnobody.reuse(_jspx_th_liferay_002dui_005ftabs_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005ftabs_0026_005furl_005fnames_005fnobody.reuse(_jspx_th_liferay_002dui_005ftabs_005f0);
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  c:choose
          org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f0 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
          _jspx_th_c_005fchoose_005f0.setPageContext(_jspx_page_context);
          _jspx_th_c_005fchoose_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          int _jspx_eval_c_005fchoose_005f0 = _jspx_th_c_005fchoose_005f0.doStartTag();
          if (_jspx_eval_c_005fchoose_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  c:when
              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f0 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
              _jspx_th_c_005fwhen_005f0.setPageContext(_jspx_page_context);
              _jspx_th_c_005fwhen_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
              // /html/portlet/journal/view.jsp(37,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fwhen_005f0.setTest( tabs1.equals("web-content") );
              int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
              if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_aui_005finput_005f1(_jspx_th_c_005fwhen_005f0, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_aui_005finput_005f2(_jspx_th_c_005fwhen_005f0, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_aui_005finput_005f3(_jspx_th_c_005fwhen_005f0, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");

			ArticleSearch searchContainer = new ArticleSearch(renderRequest, portletURL);

			List headerNames = searchContainer.getHeaderNames();

			headerNames.add(3, "status");
			headerNames.add(StringPool.BLANK);

			searchContainer.setRowChecker(new RowChecker(renderResponse));
			
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  liferay-ui:search-form
                  com.liferay.taglib.ui.SearchFormTag _jspx_th_liferay_002dui_005fsearch_002dform_005f0 = (com.liferay.taglib.ui.SearchFormTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage_005fnobody.get(com.liferay.taglib.ui.SearchFormTag.class);
                  _jspx_th_liferay_002dui_005fsearch_002dform_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fsearch_002dform_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
                  // /html/portlet/journal/view.jsp(53,3) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002dform_005f0.setPage("/html/portlet/journal/article_search.jsp");
                  // /html/portlet/journal/view.jsp(53,3) name = searchContainer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002dform_005f0.setSearchContainer( searchContainer );
                  int _jspx_eval_liferay_002dui_005fsearch_002dform_005f0 = _jspx_th_liferay_002dui_005fsearch_002dform_005f0.doStartTag();
                  if (_jspx_th_liferay_002dui_005fsearch_002dform_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dform_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dform_005f0);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");

			ArticleSearchTerms searchTerms = (ArticleSearchTerms)searchContainer.getSearchTerms();

			searchTerms.setVersion(-1);
			
                  out.write("\n");
                  out.write("\n");
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

                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t<div class=\"separator article-separator\"><!-- --></div>\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
                  // /html/portlet/journal/view.jsp(68,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f0.setTest( !results.isEmpty() );
                  int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
                  if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  aui:button-row
                      com.liferay.taglib.aui.ButtonRowTag _jspx_th_aui_005fbutton_002drow_005f0 = (com.liferay.taglib.aui.ButtonRowTag) _005fjspx_005ftagPool_005faui_005fbutton_002drow.get(com.liferay.taglib.aui.ButtonRowTag.class);
                      _jspx_th_aui_005fbutton_002drow_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005fbutton_002drow_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
                      int _jspx_eval_aui_005fbutton_002drow_005f0 = _jspx_th_aui_005fbutton_002drow_005f0.doStartTag();
                      if (_jspx_eval_aui_005fbutton_002drow_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_aui_005fbutton_002drow_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_aui_005fbutton_002drow_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_aui_005fbutton_002drow_005f0.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  aui:button
                          com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f0 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                          _jspx_th_aui_005fbutton_005f0.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005fbutton_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f0);
                          // /html/portlet/journal/view.jsp(70,5) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005fbutton_005f0.setOnClick( renderResponse.getNamespace() + "expireArticles();" );
                          // /html/portlet/journal/view.jsp(70,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005fbutton_005f0.setValue("expire");
                          int _jspx_eval_aui_005fbutton_005f0 = _jspx_th_aui_005fbutton_005f0.doStartTag();
                          if (_jspx_th_aui_005fbutton_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  aui:button
                          com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f1 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                          _jspx_th_aui_005fbutton_005f1.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005fbutton_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f0);
                          // /html/portlet/journal/view.jsp(72,5) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005fbutton_005f1.setOnClick( renderResponse.getNamespace() + "deleteArticles();" );
                          // /html/portlet/journal/view.jsp(72,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005fbutton_005f1.setValue("delete");
                          int _jspx_eval_aui_005fbutton_005f1 = _jspx_th_aui_005fbutton_005f1.doStartTag();
                          if (_jspx_th_aui_005fbutton_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f1);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f1);
                          out.write("\n");
                          out.write("\t\t\t\t");
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
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005fbutton_002drow.reuse(_jspx_th_aui_005fbutton_002drow_005f0);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t<br /><br />\n");
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

			List resultRows = searchContainer.getResultRows();

			for (int i = 0; i < results.size(); i++) {
				JournalArticle article = (JournalArticle)results.get(i);

				article = article.toEscapedModel();

				ResultRow row = new ResultRow(article, article.getArticleId() + EditArticleAction.VERSION_SEPARATOR + article.getVersion(), i);

				PortletURL rowURL = renderResponse.createRenderURL();

				rowURL.setParameter("struts_action", "/journal/edit_article");
				rowURL.setParameter("redirect", currentURL);
				rowURL.setParameter("groupId", String.valueOf(article.getGroupId()));
				rowURL.setParameter("articleId", article.getArticleId());
				rowURL.setParameter("version", String.valueOf(article.getVersion()));

				// Article id

				row.addText(article.getArticleId(), rowURL);

				// Title

				row.addText(article.getTitle(), rowURL);

				// Version

				row.addText(String.valueOf(article.getVersion()), rowURL);

				// Status

				row.addText(LanguageUtil.get(pageContext, WorkflowConstants.toLabel(article.getStatus())), rowURL);

				// Modified date

				row.addText(dateFormatDateTime.format(article.getModifiedDate()), rowURL);

				// Display date

				row.addText(dateFormatDateTime.format(article.getDisplayDate()), rowURL);

				// Author

				row.addText(HtmlUtil.escape(PortalUtil.getUserName(article.getUserId(), article.getUserName())), rowURL);

				// Action

				row.addJSP("right", SearchEntry.DEFAULT_VALIGN, "/html/portlet/journal/article_action.jsp");

				// Add result row

				resultRows.add(row);
			}
			
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  liferay-ui:search-iterator
                  com.liferay.taglib.ui.SearchIteratorTag _jspx_th_liferay_002dui_005fsearch_002diterator_005f0 = (com.liferay.taglib.ui.SearchIteratorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.get(com.liferay.taglib.ui.SearchIteratorTag.class);
                  _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
                  // /html/portlet/journal/view.jsp(134,3) name = searchContainer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.setSearchContainer( searchContainer );
                  int _jspx_eval_liferay_002dui_005fsearch_002diterator_005f0 = _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.doStartTag();
                  if (_jspx_th_liferay_002dui_005fsearch_002diterator_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f0);
                  out.write('\n');
                  out.write('	');
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
              out.write('	');
              //  c:when
              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f1 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
              _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
              // /html/portlet/journal/view.jsp(136,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fwhen_005f1.setTest( tabs1.equals("structures") );
              int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
              if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_aui_005finput_005f4(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_aui_005finput_005f5(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  liferay-ui:error
                  com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f0 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                  _jspx_th_liferay_002dui_005ferror_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005ferror_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                  // /html/portlet/journal/view.jsp(140,3) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ferror_005f0.setException( RequiredStructureException.class );
                  // /html/portlet/journal/view.jsp(140,3) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ferror_005f0.setMessage("required-structures-could-not-be-deleted");
                  int _jspx_eval_liferay_002dui_005ferror_005f0 = _jspx_th_liferay_002dui_005ferror_005f0.doStartTag();
                  if (_jspx_th_liferay_002dui_005ferror_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f0);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");

			StructureSearch searchContainer = new StructureSearch(renderRequest, portletURL);

			List headerNames = searchContainer.getHeaderNames();

			headerNames.add(StringPool.BLANK);

			searchContainer.setRowChecker(new RowChecker(renderResponse));
			
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  liferay-ui:search-form
                  com.liferay.taglib.ui.SearchFormTag _jspx_th_liferay_002dui_005fsearch_002dform_005f1 = (com.liferay.taglib.ui.SearchFormTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage_005fnobody.get(com.liferay.taglib.ui.SearchFormTag.class);
                  _jspx_th_liferay_002dui_005fsearch_002dform_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fsearch_002dform_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                  // /html/portlet/journal/view.jsp(152,3) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002dform_005f1.setPage("/html/portlet/journal/structure_search.jsp");
                  // /html/portlet/journal/view.jsp(152,3) name = searchContainer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002dform_005f1.setSearchContainer( searchContainer );
                  int _jspx_eval_liferay_002dui_005fsearch_002dform_005f1 = _jspx_th_liferay_002dui_005fsearch_002dform_005f1.doStartTag();
                  if (_jspx_th_liferay_002dui_005fsearch_002dform_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dform_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dform_005f1);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");

			StructureSearchTerms searchTerms = (StructureSearchTerms)searchContainer.getSearchTerms();
			
                  out.write("\n");
                  out.write("\n");
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

int total = 0;

if (searchTerms.isAdvancedSearch()) {
	total = JournalStructureLocalServiceUtil.searchCount(company.getCompanyId(), searchTerms.getGroupId(), searchTerms.getStructureId(), searchTerms.getName(), searchTerms.getDescription(), searchTerms.isAndOperator());
}
else {
	total = JournalStructureLocalServiceUtil.searchCount(company.getCompanyId(), searchTerms.getGroupId(), searchTerms.getKeywords());
}

searchContainer.setTotal(total);

List results = null;

if (searchTerms.isAdvancedSearch()) {
	results = JournalStructureLocalServiceUtil.search(company.getCompanyId(), searchTerms.getGroupId(), searchTerms.getStructureId(), searchTerms.getName(), searchTerms.getDescription(), searchTerms.isAndOperator(), searchContainer.getStart(), searchContainer.getEnd(), null);
}
else {
	results = JournalStructureLocalServiceUtil.search(company.getCompanyId(), searchTerms.getGroupId(), searchTerms.getKeywords(), searchContainer.getStart(), searchContainer.getEnd(), null);
}

searchContainer.setResults(results);

                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t<div class=\"separator\"><!-- --></div>\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:button
                  com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f2 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                  _jspx_th_aui_005fbutton_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fbutton_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                  // /html/portlet/journal/view.jsp(165,3) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f2.setOnClick( renderResponse.getNamespace() + "deleteStructures();" );
                  // /html/portlet/journal/view.jsp(165,3) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f2.setValue("delete");
                  int _jspx_eval_aui_005fbutton_005f2 = _jspx_th_aui_005fbutton_005f2.doStartTag();
                  if (_jspx_th_aui_005fbutton_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f2);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f2);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t<br /><br />\n");
                  out.write("\n");
                  out.write("\t\t\t");

			List resultRows = searchContainer.getResultRows();

			for (int i = 0; i < results.size(); i++) {
				JournalStructure structure = (JournalStructure)results.get(i);

				structure = structure.toEscapedModel();

				ResultRow row = new ResultRow(structure, structure.getStructureId(), i);

				PortletURL rowURL = renderResponse.createRenderURL();

				rowURL.setParameter("struts_action", "/journal/edit_structure");
				rowURL.setParameter("redirect", currentURL);
				rowURL.setParameter("groupId", String.valueOf(structure.getGroupId()));
				rowURL.setParameter("structureId", structure.getStructureId());

				// Structure id

				row.addText(structure.getStructureId(), rowURL);

				// Name and description

				if (Validator.isNotNull(structure.getDescription())) {
					row.addText(structure.getName().concat("<br />").concat(structure.getDescription()), rowURL);
				}
				else {
					row.addText(structure.getName(), rowURL);
				}

				// Action

				row.addJSP("right", SearchEntry.DEFAULT_VALIGN, "/html/portlet/journal/structure_action.jsp");

				// Add result row

				resultRows.add(row);
			}
			
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  liferay-ui:search-iterator
                  com.liferay.taglib.ui.SearchIteratorTag _jspx_th_liferay_002dui_005fsearch_002diterator_005f1 = (com.liferay.taglib.ui.SearchIteratorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.get(com.liferay.taglib.ui.SearchIteratorTag.class);
                  _jspx_th_liferay_002dui_005fsearch_002diterator_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fsearch_002diterator_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                  // /html/portlet/journal/view.jsp(209,3) name = searchContainer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002diterator_005f1.setSearchContainer( searchContainer );
                  int _jspx_eval_liferay_002dui_005fsearch_002diterator_005f1 = _jspx_th_liferay_002dui_005fsearch_002diterator_005f1.doStartTag();
                  if (_jspx_th_liferay_002dui_005fsearch_002diterator_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f1);
                  out.write('\n');
                  out.write('	');
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
              out.write('	');
              //  c:when
              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f2 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
              _jspx_th_c_005fwhen_005f2.setPageContext(_jspx_page_context);
              _jspx_th_c_005fwhen_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
              // /html/portlet/journal/view.jsp(211,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fwhen_005f2.setTest( tabs1.equals("templates") );
              int _jspx_eval_c_005fwhen_005f2 = _jspx_th_c_005fwhen_005f2.doStartTag();
              if (_jspx_eval_c_005fwhen_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_aui_005finput_005f6(_jspx_th_c_005fwhen_005f2, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_aui_005finput_005f7(_jspx_th_c_005fwhen_005f2, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  liferay-ui:error
                  com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f1 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                  _jspx_th_liferay_002dui_005ferror_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005ferror_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                  // /html/portlet/journal/view.jsp(215,3) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ferror_005f1.setException( RequiredTemplateException.class );
                  // /html/portlet/journal/view.jsp(215,3) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ferror_005f1.setMessage("required-templates-could-not-be-deleted");
                  int _jspx_eval_liferay_002dui_005ferror_005f1 = _jspx_th_liferay_002dui_005ferror_005f1.doStartTag();
                  if (_jspx_th_liferay_002dui_005ferror_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f1);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");

			TemplateSearch searchContainer = new TemplateSearch(renderRequest, portletURL);

			List headerNames = searchContainer.getHeaderNames();

			headerNames.add(StringPool.BLANK);

			searchContainer.setRowChecker(new RowChecker(renderResponse));
			
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  liferay-ui:search-form
                  com.liferay.taglib.ui.SearchFormTag _jspx_th_liferay_002dui_005fsearch_002dform_005f2 = (com.liferay.taglib.ui.SearchFormTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage_005fnobody.get(com.liferay.taglib.ui.SearchFormTag.class);
                  _jspx_th_liferay_002dui_005fsearch_002dform_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fsearch_002dform_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                  // /html/portlet/journal/view.jsp(227,3) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002dform_005f2.setPage("/html/portlet/journal/template_search.jsp");
                  // /html/portlet/journal/view.jsp(227,3) name = searchContainer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002dform_005f2.setSearchContainer( searchContainer );
                  int _jspx_eval_liferay_002dui_005fsearch_002dform_005f2 = _jspx_th_liferay_002dui_005fsearch_002dform_005f2.doStartTag();
                  if (_jspx_th_liferay_002dui_005fsearch_002dform_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dform_005f2);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dform_005f2);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");

			TemplateSearchTerms searchTerms = (TemplateSearchTerms)searchContainer.getSearchTerms();

			searchTerms.setStructureIdComparator(StringPool.EQUAL);
			
                  out.write("\n");
                  out.write("\n");
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

int total = 0;

if (searchTerms.isAdvancedSearch()) {
	total = JournalTemplateLocalServiceUtil.searchCount(company.getCompanyId(), searchTerms.getGroupId(), searchTerms.getTemplateId(), searchTerms.getStructureId(), searchTerms.getStructureIdComparator(), searchTerms.getName(), searchTerms.getDescription(), searchTerms.isAndOperator());
}
else {
	total = JournalTemplateLocalServiceUtil.searchCount(company.getCompanyId(), searchTerms.getGroupId(), searchTerms.getKeywords(), searchTerms.getStructureId(), searchTerms.getStructureIdComparator());
}

searchContainer.setTotal(total);

List results = null;

if (searchTerms.isAdvancedSearch()) {
	results = JournalTemplateLocalServiceUtil.search(company.getCompanyId(), searchTerms.getGroupId(), searchTerms.getTemplateId(), searchTerms.getStructureId(), searchTerms.getStructureIdComparator(), searchTerms.getName(), searchTerms.getDescription(), searchTerms.isAndOperator(), searchContainer.getStart(), searchContainer.getEnd(), null);
}
else {
	results = JournalTemplateLocalServiceUtil.search(company.getCompanyId(), searchTerms.getGroupId(), searchTerms.getKeywords(), searchTerms.getStructureId(), searchTerms.getStructureIdComparator(), searchContainer.getStart(), searchContainer.getEnd(), null);
}

searchContainer.setResults(results);

                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t<div class=\"separator\"><!-- --></div>\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:button
                  com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f3 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                  _jspx_th_aui_005fbutton_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fbutton_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                  // /html/portlet/journal/view.jsp(242,3) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f3.setOnClick( renderResponse.getNamespace() + "deleteTemplates();" );
                  // /html/portlet/journal/view.jsp(242,3) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f3.setValue("delete");
                  int _jspx_eval_aui_005fbutton_005f3 = _jspx_th_aui_005fbutton_005f3.doStartTag();
                  if (_jspx_th_aui_005fbutton_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f3);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f3);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t<br /><br />\n");
                  out.write("\n");
                  out.write("\t\t\t");

			List resultRows = searchContainer.getResultRows();

			for (int i = 0; i < results.size(); i++) {
				JournalTemplate template = (JournalTemplate)results.get(i);

				template = template.toEscapedModel();

				ResultRow row = new ResultRow(template, template.getTemplateId(), i);

				PortletURL rowURL = renderResponse.createRenderURL();

				rowURL.setParameter("struts_action", "/journal/edit_template");
				rowURL.setParameter("redirect", currentURL);
				rowURL.setParameter("groupId", String.valueOf(template.getGroupId()));
				rowURL.setParameter("templateId", template.getTemplateId());

				row.setParameter("rowHREF", rowURL.toString());

				// Template id

				row.addText(template.getTemplateId(), rowURL);

				// Name, description, and image

				row.addJSP("/html/portlet/journal/template_description.jsp");

				// Action

				row.addJSP("right", SearchEntry.DEFAULT_VALIGN, "/html/portlet/journal/template_action.jsp");

				// Add result row

				resultRows.add(row);
			}
			
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  liferay-ui:search-iterator
                  com.liferay.taglib.ui.SearchIteratorTag _jspx_th_liferay_002dui_005fsearch_002diterator_005f2 = (com.liferay.taglib.ui.SearchIteratorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.get(com.liferay.taglib.ui.SearchIteratorTag.class);
                  _jspx_th_liferay_002dui_005fsearch_002diterator_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fsearch_002diterator_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                  // /html/portlet/journal/view.jsp(283,3) name = searchContainer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002diterator_005f2.setSearchContainer( searchContainer );
                  int _jspx_eval_liferay_002dui_005fsearch_002diterator_005f2 = _jspx_th_liferay_002dui_005fsearch_002diterator_005f2.doStartTag();
                  if (_jspx_th_liferay_002dui_005fsearch_002diterator_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f2);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f2);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
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
              out.write('\n');
              out.write('	');
              out.write('	');
              //  c:when
              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f3 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
              _jspx_th_c_005fwhen_005f3.setPageContext(_jspx_page_context);
              _jspx_th_c_005fwhen_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
              // /html/portlet/journal/view.jsp(285,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fwhen_005f3.setTest( tabs1.equals("feeds") );
              int _jspx_eval_c_005fwhen_005f3 = _jspx_th_c_005fwhen_005f3.doStartTag();
              if (_jspx_eval_c_005fwhen_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_aui_005finput_005f8(_jspx_th_c_005fwhen_005f3, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_aui_005finput_005f9(_jspx_th_c_005fwhen_005f3, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");

			FeedSearch searchContainer = new FeedSearch(renderRequest, portletURL);

			List headerNames = searchContainer.getHeaderNames();

			headerNames.add(StringPool.BLANK);

			searchContainer.setRowChecker(new RowChecker(renderResponse));
			
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  liferay-ui:search-form
                  com.liferay.taglib.ui.SearchFormTag _jspx_th_liferay_002dui_005fsearch_002dform_005f3 = (com.liferay.taglib.ui.SearchFormTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage_005fnobody.get(com.liferay.taglib.ui.SearchFormTag.class);
                  _jspx_th_liferay_002dui_005fsearch_002dform_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fsearch_002dform_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
                  // /html/portlet/journal/view.jsp(299,3) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002dform_005f3.setPage("/html/portlet/journal/feed_search.jsp");
                  // /html/portlet/journal/view.jsp(299,3) name = searchContainer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002dform_005f3.setSearchContainer( searchContainer );
                  int _jspx_eval_liferay_002dui_005fsearch_002dform_005f3 = _jspx_th_liferay_002dui_005fsearch_002dform_005f3.doStartTag();
                  if (_jspx_th_liferay_002dui_005fsearch_002dform_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dform_005f3);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dform_0026_005fsearchContainer_005fpage_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dform_005f3);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");

			FeedSearchTerms searchTerms = (FeedSearchTerms)searchContainer.getSearchTerms();
			
                  out.write("\n");
                  out.write("\n");
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

int total = 0;

if (searchTerms.isAdvancedSearch()) {
	total = JournalFeedLocalServiceUtil.searchCount(company.getCompanyId(), searchTerms.getGroupId(), searchTerms.getFeedId(), searchTerms.getName(), searchTerms.getDescription(), searchTerms.isAndOperator());
}
else {
	total = JournalFeedLocalServiceUtil.searchCount(company.getCompanyId(), searchTerms.getGroupId(), searchTerms.getKeywords());
}

searchContainer.setTotal(total);

List results = null;

if (searchTerms.isAdvancedSearch()) {
	results = JournalFeedLocalServiceUtil.search(company.getCompanyId(), searchTerms.getGroupId(), searchTerms.getFeedId(), searchTerms.getName(), searchTerms.getDescription(), searchTerms.isAndOperator(), searchContainer.getStart(), searchContainer.getEnd(), null);
}
else {
	results = JournalFeedLocalServiceUtil.search(company.getCompanyId(), searchTerms.getGroupId(), searchTerms.getKeywords(), searchContainer.getStart(), searchContainer.getEnd(), null);
}

searchContainer.setResults(results);

                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t<div class=\"separator\"><!-- --></div>\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:button
                  com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f4 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                  _jspx_th_aui_005fbutton_005f4.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fbutton_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
                  // /html/portlet/journal/view.jsp(312,3) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f4.setOnClick( renderResponse.getNamespace() + "deleteFeeds();" );
                  // /html/portlet/journal/view.jsp(312,3) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f4.setValue("delete");
                  int _jspx_eval_aui_005fbutton_005f4 = _jspx_th_aui_005fbutton_005f4.doStartTag();
                  if (_jspx_th_aui_005fbutton_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f4);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f4);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t<br /><br />\n");
                  out.write("\n");
                  out.write("\t\t\t");

			List resultRows = searchContainer.getResultRows();

			for (int i = 0; i < results.size(); i++) {
				JournalFeed feed = (JournalFeed)results.get(i);

				feed = feed.toEscapedModel();

				ResultRow row = new ResultRow(feed, feed.getFeedId(), i);

				PortletURL rowURL = renderResponse.createRenderURL();

				rowURL.setParameter("struts_action", "/journal/edit_feed");
				rowURL.setParameter("redirect", currentURL);
				rowURL.setParameter("groupId", String.valueOf(feed.getGroupId()));
				rowURL.setParameter("feedId", feed.getFeedId());

				row.setParameter("rowHREF", rowURL.toString());

				// Feed id

				row.addText(feed.getFeedId(), rowURL);

				// Name and description

				if (Validator.isNotNull(feed.getDescription())) {
					row.addText(feed.getName().concat("<br />").concat(feed.getDescription()), rowURL);
				}
				else {
					row.addText(feed.getName(), rowURL);
				}

				// Action

				row.addJSP("right", SearchEntry.DEFAULT_VALIGN, "/html/portlet/journal/feed_action.jsp");

				// Add result row

				resultRows.add(row);
			}
			
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  liferay-ui:search-iterator
                  com.liferay.taglib.ui.SearchIteratorTag _jspx_th_liferay_002dui_005fsearch_002diterator_005f3 = (com.liferay.taglib.ui.SearchIteratorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.get(com.liferay.taglib.ui.SearchIteratorTag.class);
                  _jspx_th_liferay_002dui_005fsearch_002diterator_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fsearch_002diterator_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
                  // /html/portlet/journal/view.jsp(358,3) name = searchContainer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002diterator_005f3.setSearchContainer( searchContainer );
                  int _jspx_eval_liferay_002dui_005fsearch_002diterator_005f3 = _jspx_th_liferay_002dui_005fsearch_002diterator_005f3.doStartTag();
                  if (_jspx_th_liferay_002dui_005fsearch_002diterator_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f3);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f3);
                  out.write('\n');
                  out.write('	');
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
              out.write('	');
              //  c:when
              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f4 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
              _jspx_th_c_005fwhen_005f4.setPageContext(_jspx_page_context);
              _jspx_th_c_005fwhen_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
              // /html/portlet/journal/view.jsp(360,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fwhen_005f4.setTest( tabs1.equals("recent") );
              int _jspx_eval_c_005fwhen_005f4 = _jspx_th_c_005fwhen_005f4.doStartTag();
              if (_jspx_eval_c_005fwhen_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  out.print( LanguageUtil.format(pageContext, "this-page-displays-the-last-x-web-content,-structures,-and-templates-that-you-accessed", String.valueOf(JournalUtil.MAX_STACK_SIZE), false) );
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t<br /><br />\n");
                  out.write("\n");
                  out.write("\t\t\t<table class=\"lfr-table\" width=\"100%\">\n");
                  out.write("\t\t\t<tr>\n");
                  out.write("\t\t\t\t<td class=\"lfr-top\" width=\"33%\">\n");
                  out.write("\t\t\t\t\t<table border=\"0\" cellpadding=\"4\" cellspacing=\"0\" width=\"100%\">\n");
                  out.write("\t\t\t\t\t<tr class=\"portlet-section-header results-header\" style=\"font-size: x-small; font-weight: bold;\">\n");
                  out.write("\t\t\t\t\t\t<td colspan=\"2\">\n");
                  out.write("\t\t\t\t\t\t\t");
                  out.print( LanguageUtil.format(pageContext, "last-x-web-content", String.valueOf(JournalUtil.MAX_STACK_SIZE), false) );
                  out.write("\n");
                  out.write("\t\t\t\t\t\t</td>\n");
                  out.write("\t\t\t\t\t</tr>\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");

					Stack recentArticles = JournalUtil.getRecentArticles(renderRequest);

					int recentArticlesSize = recentArticles.size();

					for (int i = recentArticlesSize - 1; i >= 0; i--) {
						JournalArticle article = (JournalArticle)recentArticles.get(i);

						article = article.toEscapedModel();

						String className = "portlet-section-body results-row";
						String classHoverName = "portlet-section-body-hover results-row hover";

						if (MathUtil.isEven(i)) {
							className = "portlet-section-alternate results-row alt";
							classHoverName = "portlet-section-alternate-hover results-row alt hover";
						}
					
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t");
                  //  portlet:renderURL
                  com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f0 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                  _jspx_th_portlet_005frenderURL_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005frenderURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                  // /html/portlet/journal/view.jsp(394,6) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005frenderURL_005f0.setVar("editArticleURL");
                  int _jspx_eval_portlet_005frenderURL_005f0 = _jspx_th_portlet_005frenderURL_005f0.doStartTag();
                  if (_jspx_eval_portlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_portlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_portlet_005frenderURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_portlet_005frenderURL_005f0.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t");
                      if (_jspx_meth_portlet_005fparam_005f0(_jspx_th_portlet_005frenderURL_005f0, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t");
                      //  portlet:param
                      com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f1 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                      _jspx_th_portlet_005fparam_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005fparam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
                      // /html/portlet/journal/view.jsp(396,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f1.setName("redirect");
                      // /html/portlet/journal/view.jsp(396,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
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
                      _jspx_th_portlet_005fparam_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
                      // /html/portlet/journal/view.jsp(397,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f2.setName("groupId");
                      // /html/portlet/journal/view.jsp(397,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f2.setValue( String.valueOf(article.getGroupId()) );
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
                      _jspx_th_portlet_005fparam_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
                      // /html/portlet/journal/view.jsp(398,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f3.setName("articleId");
                      // /html/portlet/journal/view.jsp(398,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f3.setValue( article.getArticleId() );
                      int _jspx_eval_portlet_005fparam_005f3 = _jspx_th_portlet_005fparam_005f3.doStartTag();
                      if (_jspx_th_portlet_005fparam_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t");
                      //  portlet:param
                      com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f4 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                      _jspx_th_portlet_005fparam_005f4.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005fparam_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
                      // /html/portlet/journal/view.jsp(399,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f4.setName("version");
                      // /html/portlet/journal/view.jsp(399,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f4.setValue( String.valueOf(article.getVersion()) );
                      int _jspx_eval_portlet_005fparam_005f4 = _jspx_th_portlet_005fparam_005f4.doStartTag();
                      if (_jspx_th_portlet_005fparam_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
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
                  java.lang.String editArticleURL = null;
                  editArticleURL = (java.lang.String) _jspx_page_context.findAttribute("editArticleURL");
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t<tr class=\"");
                  out.print( className );
                  out.write("\" style=\"font-size: x-small;\" onMouseEnter=\"this.className = '");
                  out.print( classHoverName );
                  out.write("';\" onMouseLeave=\"this.className = '");
                  out.print( className );
                  out.write("';\">\n");
                  out.write("\t\t\t\t\t\t\t<td>\n");
                  out.write("\t\t\t\t\t\t\t\t");
                  //  aui:a
                  com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f0 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.get(com.liferay.taglib.aui.ATag.class);
                  _jspx_th_aui_005fa_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fa_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                  // /html/portlet/journal/view.jsp(404,8) name = href type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fa_005f0.setHref( editArticleURL );
                  int _jspx_eval_aui_005fa_005f0 = _jspx_th_aui_005fa_005f0.doStartTag();
                  if (_jspx_eval_aui_005fa_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005fa_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005fa_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005fa_005f0.doInitBody();
                    }
                    do {
                      out.print( article.getArticleId() );
                      int evalDoAfterBody = _jspx_th_aui_005fa_005f0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_aui_005fa_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_aui_005fa_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.reuse(_jspx_th_aui_005fa_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.reuse(_jspx_th_aui_005fa_005f0);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t</td>\n");
                  out.write("\t\t\t\t\t\t\t<td>\n");
                  out.write("\t\t\t\t\t\t\t\t");
                  //  aui:a
                  com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f1 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.get(com.liferay.taglib.aui.ATag.class);
                  _jspx_th_aui_005fa_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fa_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                  // /html/portlet/journal/view.jsp(407,8) name = href type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fa_005f1.setHref( editArticleURL );
                  int _jspx_eval_aui_005fa_005f1 = _jspx_th_aui_005fa_005f1.doStartTag();
                  if (_jspx_eval_aui_005fa_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005fa_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005fa_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005fa_005f1.doInitBody();
                    }
                    do {
                      out.print( article.getTitle() );
                      int evalDoAfterBody = _jspx_th_aui_005fa_005f1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_aui_005fa_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_aui_005fa_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.reuse(_jspx_th_aui_005fa_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.reuse(_jspx_th_aui_005fa_005f1);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t</td>\n");
                  out.write("\t\t\t\t\t\t</tr>\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");

					}
					
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t</table>\n");
                  out.write("\t\t\t\t</td>\n");
                  out.write("\t\t\t\t<td class=\"lfr-top\" width=\"33%\">\n");
                  out.write("\t\t\t\t\t<table border=\"0\" cellpadding=\"4\" cellspacing=\"0\" width=\"100%\">\n");
                  out.write("\t\t\t\t\t<tr class=\"portlet-section-header results-header\" style=\"font-size: x-small; font-weight: bold;\">\n");
                  out.write("\t\t\t\t\t\t<td colspan=\"2\">\n");
                  out.write("\t\t\t\t\t\t\t");
                  out.print( LanguageUtil.format(pageContext, "last-x-structures", String.valueOf(JournalUtil.MAX_STACK_SIZE), false) );
                  out.write("\n");
                  out.write("\t\t\t\t\t\t</td>\n");
                  out.write("\t\t\t\t\t</tr>\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");

					Stack recentStructures = JournalUtil.getRecentStructures(renderRequest);

					int recentStructuresSize = recentStructures.size();

					for (int i = recentStructuresSize - 1; i >= 0; i--) {
						JournalStructure structure = (JournalStructure)recentStructures.get(i);

						structure = structure.toEscapedModel();

						String className = "portlet-section-body results-row";
						String classHoverName = "portlet-section-body-hover results-row hover";

						if (MathUtil.isEven(i)) {
							className = "portlet-section-alternate results-row alt";
							classHoverName = "portlet-section-alternate-hover results-row alt hover";
						}
					
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t");
                  //  portlet:renderURL
                  com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f1 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                  _jspx_th_portlet_005frenderURL_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005frenderURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                  // /html/portlet/journal/view.jsp(444,6) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005frenderURL_005f1.setVar("editStructureURL");
                  int _jspx_eval_portlet_005frenderURL_005f1 = _jspx_th_portlet_005frenderURL_005f1.doStartTag();
                  if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_portlet_005frenderURL_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_portlet_005frenderURL_005f1.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t");
                      if (_jspx_meth_portlet_005fparam_005f5(_jspx_th_portlet_005frenderURL_005f1, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t");
                      //  portlet:param
                      com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f6 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                      _jspx_th_portlet_005fparam_005f6.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005fparam_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
                      // /html/portlet/journal/view.jsp(446,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f6.setName("redirect");
                      // /html/portlet/journal/view.jsp(446,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f6.setValue( currentURL );
                      int _jspx_eval_portlet_005fparam_005f6 = _jspx_th_portlet_005fparam_005f6.doStartTag();
                      if (_jspx_th_portlet_005fparam_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t");
                      //  portlet:param
                      com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f7 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                      _jspx_th_portlet_005fparam_005f7.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005fparam_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
                      // /html/portlet/journal/view.jsp(447,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f7.setName("groupId");
                      // /html/portlet/journal/view.jsp(447,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f7.setValue( String.valueOf(structure.getGroupId()) );
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
                      _jspx_th_portlet_005fparam_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
                      // /html/portlet/journal/view.jsp(448,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f8.setName("structureId");
                      // /html/portlet/journal/view.jsp(448,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f8.setValue( structure.getStructureId() );
                      int _jspx_eval_portlet_005fparam_005f8 = _jspx_th_portlet_005fparam_005f8.doStartTag();
                      if (_jspx_th_portlet_005fparam_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
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
                  java.lang.String editStructureURL = null;
                  editStructureURL = (java.lang.String) _jspx_page_context.findAttribute("editStructureURL");
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t<tr class=\"");
                  out.print( className );
                  out.write("\" style=\"font-size: x-small;\" onMouseEnter=\"this.className = '");
                  out.print( classHoverName );
                  out.write("';\" onMouseLeave=\"this.className = '");
                  out.print( className );
                  out.write("';\">\n");
                  out.write("\t\t\t\t\t\t\t<td>\n");
                  out.write("\t\t\t\t\t\t\t\t");
                  //  aui:a
                  com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f2 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.get(com.liferay.taglib.aui.ATag.class);
                  _jspx_th_aui_005fa_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fa_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                  // /html/portlet/journal/view.jsp(453,8) name = href type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fa_005f2.setHref( editStructureURL );
                  int _jspx_eval_aui_005fa_005f2 = _jspx_th_aui_005fa_005f2.doStartTag();
                  if (_jspx_eval_aui_005fa_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005fa_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005fa_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005fa_005f2.doInitBody();
                    }
                    do {
                      out.print( structure.getId() );
                      int evalDoAfterBody = _jspx_th_aui_005fa_005f2.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_aui_005fa_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_aui_005fa_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.reuse(_jspx_th_aui_005fa_005f2);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.reuse(_jspx_th_aui_005fa_005f2);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t</td>\n");
                  out.write("\t\t\t\t\t\t\t<td>\n");
                  out.write("\t\t\t\t\t\t\t\t");
                  //  aui:a
                  com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f3 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.get(com.liferay.taglib.aui.ATag.class);
                  _jspx_th_aui_005fa_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fa_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                  // /html/portlet/journal/view.jsp(456,8) name = href type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fa_005f3.setHref( editStructureURL );
                  int _jspx_eval_aui_005fa_005f3 = _jspx_th_aui_005fa_005f3.doStartTag();
                  if (_jspx_eval_aui_005fa_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005fa_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005fa_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005fa_005f3.doInitBody();
                    }
                    do {
                      out.print( structure.getName() );
                      int evalDoAfterBody = _jspx_th_aui_005fa_005f3.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_aui_005fa_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_aui_005fa_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.reuse(_jspx_th_aui_005fa_005f3);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.reuse(_jspx_th_aui_005fa_005f3);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t</td>\n");
                  out.write("\t\t\t\t\t\t</tr>\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");

					}
					
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t</table>\n");
                  out.write("\t\t\t\t</td>\n");
                  out.write("\t\t\t\t<td class=\"lfr-top\" width=\"33%\">\n");
                  out.write("\t\t\t\t\t<table border=\"0\" cellpadding=\"4\" cellspacing=\"0\" width=\"100%\">\n");
                  out.write("\t\t\t\t\t<tr class=\"portlet-section-header results-header\" style=\"font-size: x-small; font-weight: bold;\">\n");
                  out.write("\t\t\t\t\t\t<td colspan=\"2\">\n");
                  out.write("\t\t\t\t\t\t\t");
                  out.print( LanguageUtil.format(pageContext, "last-x-templates", String.valueOf(JournalUtil.MAX_STACK_SIZE), false) );
                  out.write("\n");
                  out.write("\t\t\t\t\t\t</td>\n");
                  out.write("\t\t\t\t\t</tr>\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");

					Stack recentTemplates = JournalUtil.getRecentTemplates(renderRequest);

					int recentTemplatesSize = recentTemplates.size();

					for (int i = recentTemplatesSize - 1; i >= 0; i--) {
						JournalTemplate template = (JournalTemplate)recentTemplates.get(i);

						template = template.toEscapedModel();

						String className = "portlet-section-body results-row";
						String classHoverName = "portlet-section-body-hover results-row hover";

						if (MathUtil.isEven(recentTemplatesSize - i - 1)) {
							className = "portlet-section-alternate results-row alt";
							classHoverName = "portlet-section-alternate-hover results-row alt hover";
						}
					
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t");
                  //  portlet:renderURL
                  com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f2 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                  _jspx_th_portlet_005frenderURL_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005frenderURL_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                  // /html/portlet/journal/view.jsp(493,6) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005frenderURL_005f2.setVar("editTemplateURL");
                  int _jspx_eval_portlet_005frenderURL_005f2 = _jspx_th_portlet_005frenderURL_005f2.doStartTag();
                  if (_jspx_eval_portlet_005frenderURL_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_portlet_005frenderURL_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_portlet_005frenderURL_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_portlet_005frenderURL_005f2.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t");
                      if (_jspx_meth_portlet_005fparam_005f9(_jspx_th_portlet_005frenderURL_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t");
                      //  portlet:param
                      com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f10 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                      _jspx_th_portlet_005fparam_005f10.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005fparam_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f2);
                      // /html/portlet/journal/view.jsp(495,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f10.setName("redirect");
                      // /html/portlet/journal/view.jsp(495,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f10.setValue( currentURL );
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
                      _jspx_th_portlet_005fparam_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f2);
                      // /html/portlet/journal/view.jsp(496,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f11.setName("groupId");
                      // /html/portlet/journal/view.jsp(496,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f11.setValue( String.valueOf(template.getGroupId()) );
                      int _jspx_eval_portlet_005fparam_005f11 = _jspx_th_portlet_005fparam_005f11.doStartTag();
                      if (_jspx_th_portlet_005fparam_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f11);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f11);
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t");
                      //  portlet:param
                      com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f12 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                      _jspx_th_portlet_005fparam_005f12.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005fparam_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f2);
                      // /html/portlet/journal/view.jsp(497,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f12.setName("templateId");
                      // /html/portlet/journal/view.jsp(497,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f12.setValue( template.getTemplateId() );
                      int _jspx_eval_portlet_005fparam_005f12 = _jspx_th_portlet_005fparam_005f12.doStartTag();
                      if (_jspx_th_portlet_005fparam_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f12);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f12);
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
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
                  java.lang.String editTemplateURL = null;
                  editTemplateURL = (java.lang.String) _jspx_page_context.findAttribute("editTemplateURL");
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t<tr class=\"");
                  out.print( className );
                  out.write("\" style=\"font-size: x-small;\" onMouseEnter=\"this.className = '");
                  out.print( classHoverName );
                  out.write("';\" onMouseLeave=\"this.className = '");
                  out.print( className );
                  out.write("';\">\n");
                  out.write("\t\t\t\t\t\t\t<td>\n");
                  out.write("\t\t\t\t\t\t\t\t");
                  //  aui:a
                  com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f4 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.get(com.liferay.taglib.aui.ATag.class);
                  _jspx_th_aui_005fa_005f4.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fa_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                  // /html/portlet/journal/view.jsp(502,8) name = href type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fa_005f4.setHref( editTemplateURL );
                  int _jspx_eval_aui_005fa_005f4 = _jspx_th_aui_005fa_005f4.doStartTag();
                  if (_jspx_eval_aui_005fa_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005fa_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005fa_005f4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005fa_005f4.doInitBody();
                    }
                    do {
                      out.print( template.getId() );
                      int evalDoAfterBody = _jspx_th_aui_005fa_005f4.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_aui_005fa_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_aui_005fa_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.reuse(_jspx_th_aui_005fa_005f4);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.reuse(_jspx_th_aui_005fa_005f4);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t</td>\n");
                  out.write("\t\t\t\t\t\t\t<td>\n");
                  out.write("\t\t\t\t\t\t\t\t");
                  //  aui:a
                  com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f5 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.get(com.liferay.taglib.aui.ATag.class);
                  _jspx_th_aui_005fa_005f5.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fa_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);
                  // /html/portlet/journal/view.jsp(505,8) name = href type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fa_005f5.setHref( editTemplateURL );
                  int _jspx_eval_aui_005fa_005f5 = _jspx_th_aui_005fa_005f5.doStartTag();
                  if (_jspx_eval_aui_005fa_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005fa_005f5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005fa_005f5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005fa_005f5.doInitBody();
                    }
                    do {
                      out.print( template.getName() );
                      int evalDoAfterBody = _jspx_th_aui_005fa_005f5.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_aui_005fa_005f5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_aui_005fa_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.reuse(_jspx_th_aui_005fa_005f5);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fa_0026_005fhref.reuse(_jspx_th_aui_005fa_005f5);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t</td>\n");
                  out.write("\t\t\t\t\t\t</tr>\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");

					}
					
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t</table>\n");
                  out.write("\t\t\t\t</td>\n");
                  out.write("\t\t\t</tr>\n");
                  out.write("\t\t\t</table>\n");
                  out.write("\t\t");
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
              out.write('\n');
              out.write('	');
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
          if (_jspx_meth_portlet_005fnamespace_005f0(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("deleteArticles',\n");
          out.write("\t\tfunction() {\n");
          out.write("\t\t\tif (confirm('");
          out.print( UnicodeLanguageUtil.get(pageContext, "are-you-sure-you-want-to-delete-the-selected-web-content") );
          out.write("')) {\n");
          out.write("\t\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f1(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f2(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.print( Constants.CMD );
          out.write(".value = \"");
          out.print( Constants.DELETE );
          out.write("\";\n");
          out.write("\t\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f3(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f4(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("groupId.value = \"");
          out.print( scopeGroupId );
          out.write("\";\n");
          out.write("\t\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f5(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f6(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("deleteArticleIds.value = Liferay.Util.listCheckedExcept(document.");
          if (_jspx_meth_portlet_005fnamespace_005f7(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("fm, \"");
          if (_jspx_meth_portlet_005fnamespace_005f8(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("allRowIds\");\n");
          out.write("\t\t\t\tsubmitForm(document.");
          if (_jspx_meth_portlet_005fnamespace_005f9(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("fm, \"");
          //  portlet:actionURL
          com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f0 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL.get(com.liferay.taglib.portlet.ActionURLTag.class);
          _jspx_th_portlet_005factionURL_005f0.setPageContext(_jspx_page_context);
          _jspx_th_portlet_005factionURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          int _jspx_eval_portlet_005factionURL_005f0 = _jspx_th_portlet_005factionURL_005f0.doStartTag();
          if (_jspx_eval_portlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_portlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_portlet_005factionURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_portlet_005factionURL_005f0.doInitBody();
            }
            do {
              if (_jspx_meth_portlet_005fparam_005f13(_jspx_th_portlet_005factionURL_005f0, _jspx_page_context))
                return;
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f14 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f14.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
              // /html/portlet/journal/view.jsp(530,137) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f14.setName("redirect");
              // /html/portlet/journal/view.jsp(530,137) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f14.setValue( currentURL );
              int _jspx_eval_portlet_005fparam_005f14 = _jspx_th_portlet_005fparam_005f14.doStartTag();
              if (_jspx_th_portlet_005fparam_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f14);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f14);
              int evalDoAfterBody = _jspx_th_portlet_005factionURL_005f0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_portlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_portlet_005factionURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fportlet_005factionURL.reuse(_jspx_th_portlet_005factionURL_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fportlet_005factionURL.reuse(_jspx_th_portlet_005factionURL_005f0);
          out.write("\");\n");
          out.write("\t\t\t}\n");
          out.write("\t\t},\n");
          out.write("\t\t['liferay-util-list-fields']\n");
          out.write("\t);\n");
          out.write("\n");
          out.write("\tLiferay.provide(\n");
          out.write("\t\twindow,\n");
          out.write("\t\t'");
          if (_jspx_meth_portlet_005fnamespace_005f10(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("deleteFeeds',\n");
          out.write("\t\tfunction() {\n");
          out.write("\t\t\tif (confirm('");
          out.print( UnicodeLanguageUtil.get(pageContext, "are-you-sure-you-want-to-delete-the-selected-feeds") );
          out.write("')) {\n");
          out.write("\t\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f11(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f12(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.print( Constants.CMD );
          out.write(".value = \"");
          out.print( Constants.DELETE );
          out.write("\";\n");
          out.write("\t\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f13(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f14(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("groupId.value = \"");
          out.print( scopeGroupId );
          out.write("\";\n");
          out.write("\t\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f15(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f16(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("deleteFeedIds.value = Liferay.Util.listCheckedExcept(document.");
          if (_jspx_meth_portlet_005fnamespace_005f17(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("fm, \"");
          if (_jspx_meth_portlet_005fnamespace_005f18(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("allRowIds\");\n");
          out.write("\t\t\t\tsubmitForm(document.");
          if (_jspx_meth_portlet_005fnamespace_005f19(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("fm, \"");
          //  portlet:actionURL
          com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f1 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL.get(com.liferay.taglib.portlet.ActionURLTag.class);
          _jspx_th_portlet_005factionURL_005f1.setPageContext(_jspx_page_context);
          _jspx_th_portlet_005factionURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          int _jspx_eval_portlet_005factionURL_005f1 = _jspx_th_portlet_005factionURL_005f1.doStartTag();
          if (_jspx_eval_portlet_005factionURL_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_portlet_005factionURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_portlet_005factionURL_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_portlet_005factionURL_005f1.doInitBody();
            }
            do {
              if (_jspx_meth_portlet_005fparam_005f15(_jspx_th_portlet_005factionURL_005f1, _jspx_page_context))
                return;
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f16 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f16.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
              // /html/portlet/journal/view.jsp(544,134) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f16.setName("redirect");
              // /html/portlet/journal/view.jsp(544,134) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f16.setValue( currentURL );
              int _jspx_eval_portlet_005fparam_005f16 = _jspx_th_portlet_005fparam_005f16.doStartTag();
              if (_jspx_th_portlet_005fparam_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f16);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f16);
              int evalDoAfterBody = _jspx_th_portlet_005factionURL_005f1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_portlet_005factionURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_portlet_005factionURL_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fportlet_005factionURL.reuse(_jspx_th_portlet_005factionURL_005f1);
            return;
          }
          _005fjspx_005ftagPool_005fportlet_005factionURL.reuse(_jspx_th_portlet_005factionURL_005f1);
          out.write("\");\n");
          out.write("\t\t\t}\n");
          out.write("\t\t},\n");
          out.write("\t\t['liferay-util-list-fields']\n");
          out.write("\t);\n");
          out.write("\n");
          out.write("\tLiferay.provide(\n");
          out.write("\t\twindow,\n");
          out.write("\t\t'");
          if (_jspx_meth_portlet_005fnamespace_005f20(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("deleteStructures',\n");
          out.write("\t\tfunction() {\n");
          out.write("\t\t\tif (confirm('");
          out.print( UnicodeLanguageUtil.get(pageContext, "are-you-sure-you-want-to-delete-the-selected-structures") );
          out.write("')) {\n");
          out.write("\t\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f21(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f22(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.print( Constants.CMD );
          out.write(".value = \"");
          out.print( Constants.DELETE );
          out.write("\";\n");
          out.write("\t\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f23(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f24(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("groupId.value = \"");
          out.print( scopeGroupId );
          out.write("\";\n");
          out.write("\t\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f25(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f26(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("deleteStructureIds.value = Liferay.Util.listCheckedExcept(document.");
          if (_jspx_meth_portlet_005fnamespace_005f27(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("fm, \"");
          if (_jspx_meth_portlet_005fnamespace_005f28(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("allRowIds\");\n");
          out.write("\t\t\t\tsubmitForm(document.");
          if (_jspx_meth_portlet_005fnamespace_005f29(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("fm, \"");
          //  portlet:actionURL
          com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f2 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL.get(com.liferay.taglib.portlet.ActionURLTag.class);
          _jspx_th_portlet_005factionURL_005f2.setPageContext(_jspx_page_context);
          _jspx_th_portlet_005factionURL_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          int _jspx_eval_portlet_005factionURL_005f2 = _jspx_th_portlet_005factionURL_005f2.doStartTag();
          if (_jspx_eval_portlet_005factionURL_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_portlet_005factionURL_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_portlet_005factionURL_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_portlet_005factionURL_005f2.doInitBody();
            }
            do {
              if (_jspx_meth_portlet_005fparam_005f17(_jspx_th_portlet_005factionURL_005f2, _jspx_page_context))
                return;
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f18 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f18.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f2);
              // /html/portlet/journal/view.jsp(558,139) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f18.setName("redirect");
              // /html/portlet/journal/view.jsp(558,139) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f18.setValue( currentURL );
              int _jspx_eval_portlet_005fparam_005f18 = _jspx_th_portlet_005fparam_005f18.doStartTag();
              if (_jspx_th_portlet_005fparam_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f18);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f18);
              int evalDoAfterBody = _jspx_th_portlet_005factionURL_005f2.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_portlet_005factionURL_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_portlet_005factionURL_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fportlet_005factionURL.reuse(_jspx_th_portlet_005factionURL_005f2);
            return;
          }
          _005fjspx_005ftagPool_005fportlet_005factionURL.reuse(_jspx_th_portlet_005factionURL_005f2);
          out.write("\");\n");
          out.write("\t\t\t}\n");
          out.write("\t\t},\n");
          out.write("\t\t['liferay-util-list-fields']\n");
          out.write("\t);\n");
          out.write("\n");
          out.write("\tLiferay.provide(\n");
          out.write("\t\twindow,\n");
          out.write("\t\t'");
          if (_jspx_meth_portlet_005fnamespace_005f30(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("deleteTemplates',\n");
          out.write("\t\tfunction() {\n");
          out.write("\t\t\tif (confirm('");
          out.print( UnicodeLanguageUtil.get(pageContext, "are-you-sure-you-want-to-delete-the-selected-templates") );
          out.write("')) {\n");
          out.write("\t\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f31(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f32(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.print( Constants.CMD );
          out.write(".value = \"");
          out.print( Constants.DELETE );
          out.write("\";\n");
          out.write("\t\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f33(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f34(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("groupId.value = \"");
          out.print( scopeGroupId );
          out.write("\";\n");
          out.write("\t\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f35(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f36(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("deleteTemplateIds.value = Liferay.Util.listCheckedExcept(document.");
          if (_jspx_meth_portlet_005fnamespace_005f37(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("fm, \"");
          if (_jspx_meth_portlet_005fnamespace_005f38(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("allRowIds\");\n");
          out.write("\t\t\t\tsubmitForm(document.");
          if (_jspx_meth_portlet_005fnamespace_005f39(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("fm, \"");
          //  portlet:actionURL
          com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f3 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL.get(com.liferay.taglib.portlet.ActionURLTag.class);
          _jspx_th_portlet_005factionURL_005f3.setPageContext(_jspx_page_context);
          _jspx_th_portlet_005factionURL_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          int _jspx_eval_portlet_005factionURL_005f3 = _jspx_th_portlet_005factionURL_005f3.doStartTag();
          if (_jspx_eval_portlet_005factionURL_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_portlet_005factionURL_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_portlet_005factionURL_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_portlet_005factionURL_005f3.doInitBody();
            }
            do {
              if (_jspx_meth_portlet_005fparam_005f19(_jspx_th_portlet_005factionURL_005f3, _jspx_page_context))
                return;
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f20 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f20.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f3);
              // /html/portlet/journal/view.jsp(572,138) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f20.setName("redirect");
              // /html/portlet/journal/view.jsp(572,138) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f20.setValue( currentURL );
              int _jspx_eval_portlet_005fparam_005f20 = _jspx_th_portlet_005fparam_005f20.doStartTag();
              if (_jspx_th_portlet_005fparam_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f20);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f20);
              int evalDoAfterBody = _jspx_th_portlet_005factionURL_005f3.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_portlet_005factionURL_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_portlet_005factionURL_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fportlet_005factionURL.reuse(_jspx_th_portlet_005factionURL_005f3);
            return;
          }
          _005fjspx_005ftagPool_005fportlet_005factionURL.reuse(_jspx_th_portlet_005factionURL_005f3);
          out.write("\");\n");
          out.write("\t\t\t}\n");
          out.write("\t\t},\n");
          out.write("\t\t['liferay-util-list-fields']\n");
          out.write("\t);\n");
          out.write("\n");
          out.write("\tLiferay.provide(\n");
          out.write("\t\twindow,\n");
          out.write("\t\t'");
          if (_jspx_meth_portlet_005fnamespace_005f40(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("expireArticles',\n");
          out.write("\t\tfunction() {\n");
          out.write("\t\t\tif (confirm('");
          out.print( UnicodeLanguageUtil.get(pageContext, "are-you-sure-you-want-to-expire-the-selected-web-content") );
          out.write("')) {\n");
          out.write("\t\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f41(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f42(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.print( Constants.CMD );
          out.write(".value = \"");
          out.print( Constants.EXPIRE );
          out.write("\";\n");
          out.write("\t\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f43(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f44(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("groupId.value = \"");
          out.print( scopeGroupId );
          out.write("\";\n");
          out.write("\t\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f45(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f46(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("expireArticleIds.value = Liferay.Util.listCheckedExcept(document.");
          if (_jspx_meth_portlet_005fnamespace_005f47(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("fm, \"");
          if (_jspx_meth_portlet_005fnamespace_005f48(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("allRowIds\");\n");
          out.write("\t\t\t\tsubmitForm(document.");
          if (_jspx_meth_portlet_005fnamespace_005f49(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("fm, \"");
          //  portlet:actionURL
          com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f4 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL.get(com.liferay.taglib.portlet.ActionURLTag.class);
          _jspx_th_portlet_005factionURL_005f4.setPageContext(_jspx_page_context);
          _jspx_th_portlet_005factionURL_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          int _jspx_eval_portlet_005factionURL_005f4 = _jspx_th_portlet_005factionURL_005f4.doStartTag();
          if (_jspx_eval_portlet_005factionURL_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_portlet_005factionURL_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_portlet_005factionURL_005f4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_portlet_005factionURL_005f4.doInitBody();
            }
            do {
              if (_jspx_meth_portlet_005fparam_005f21(_jspx_th_portlet_005factionURL_005f4, _jspx_page_context))
                return;
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f22 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f22.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f4);
              // /html/portlet/journal/view.jsp(586,137) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f22.setName("redirect");
              // /html/portlet/journal/view.jsp(586,137) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f22.setValue( currentURL );
              int _jspx_eval_portlet_005fparam_005f22 = _jspx_th_portlet_005fparam_005f22.doStartTag();
              if (_jspx_th_portlet_005fparam_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f22);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f22);
              int evalDoAfterBody = _jspx_th_portlet_005factionURL_005f4.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_portlet_005factionURL_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_portlet_005factionURL_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fportlet_005factionURL.reuse(_jspx_th_portlet_005factionURL_005f4);
            return;
          }
          _005fjspx_005ftagPool_005fportlet_005factionURL.reuse(_jspx_th_portlet_005factionURL_005f4);
          out.write("\");\n");
          out.write("\t\t\t}\n");
          out.write("\t\t},\n");
          out.write("\t\t['liferay-util-list-fields']\n");
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

  private boolean _jspx_meth_aui_005finput_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f1 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f1.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
    // /html/portlet/journal/view.jsp(38,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f1.setName("groupId");
    // /html/portlet/journal/view.jsp(38,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f1.setType("hidden");
    int _jspx_eval_aui_005finput_005f1 = _jspx_th_aui_005finput_005f1.doStartTag();
    if (_jspx_th_aui_005finput_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f2 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f2.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
    // /html/portlet/journal/view.jsp(39,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f2.setName("deleteArticleIds");
    // /html/portlet/journal/view.jsp(39,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f2.setType("hidden");
    int _jspx_eval_aui_005finput_005f2 = _jspx_th_aui_005finput_005f2.doStartTag();
    if (_jspx_th_aui_005finput_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f2);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f3 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f3.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
    // /html/portlet/journal/view.jsp(40,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f3.setName("expireArticleIds");
    // /html/portlet/journal/view.jsp(40,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f3.setType("hidden");
    int _jspx_eval_aui_005finput_005f3 = _jspx_th_aui_005finput_005f3.doStartTag();
    if (_jspx_th_aui_005finput_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f3);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f4 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f4.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/portlet/journal/view.jsp(137,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f4.setName("groupId");
    // /html/portlet/journal/view.jsp(137,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f4.setType("hidden");
    int _jspx_eval_aui_005finput_005f4 = _jspx_th_aui_005finput_005f4.doStartTag();
    if (_jspx_th_aui_005finput_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f4);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f5 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f5.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/portlet/journal/view.jsp(138,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f5.setName("deleteStructureIds");
    // /html/portlet/journal/view.jsp(138,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f5.setType("hidden");
    int _jspx_eval_aui_005finput_005f5 = _jspx_th_aui_005finput_005f5.doStartTag();
    if (_jspx_th_aui_005finput_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f5);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f6 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f6.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
    // /html/portlet/journal/view.jsp(212,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f6.setName("groupId");
    // /html/portlet/journal/view.jsp(212,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f6.setType("hidden");
    int _jspx_eval_aui_005finput_005f6 = _jspx_th_aui_005finput_005f6.doStartTag();
    if (_jspx_th_aui_005finput_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f6);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f7 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f7.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
    // /html/portlet/journal/view.jsp(213,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f7.setName("deleteTemplateIds");
    // /html/portlet/journal/view.jsp(213,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f7.setType("hidden");
    int _jspx_eval_aui_005finput_005f7 = _jspx_th_aui_005finput_005f7.doStartTag();
    if (_jspx_th_aui_005finput_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f7);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f8 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f8.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
    // /html/portlet/journal/view.jsp(286,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f8.setName("groupId");
    // /html/portlet/journal/view.jsp(286,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f8.setType("hidden");
    int _jspx_eval_aui_005finput_005f8 = _jspx_th_aui_005finput_005f8.doStartTag();
    if (_jspx_th_aui_005finput_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f8);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f9 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f9.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
    // /html/portlet/journal/view.jsp(287,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f9.setName("deleteFeedIds");
    // /html/portlet/journal/view.jsp(287,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f9.setType("hidden");
    int _jspx_eval_aui_005finput_005f9 = _jspx_th_aui_005finput_005f9.doStartTag();
    if (_jspx_th_aui_005finput_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f9);
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
    // /html/portlet/journal/view.jsp(395,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setName("struts_action");
    // /html/portlet/journal/view.jsp(395,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setValue("/journal/edit_article");
    int _jspx_eval_portlet_005fparam_005f0 = _jspx_th_portlet_005fparam_005f0.doStartTag();
    if (_jspx_th_portlet_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f5 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f5.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
    // /html/portlet/journal/view.jsp(445,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f5.setName("struts_action");
    // /html/portlet/journal/view.jsp(445,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f5.setValue("/journal/edit_structure");
    int _jspx_eval_portlet_005fparam_005f5 = _jspx_th_portlet_005fparam_005f5.doStartTag();
    if (_jspx_th_portlet_005fparam_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f9 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f9.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f2);
    // /html/portlet/journal/view.jsp(494,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f9.setName("struts_action");
    // /html/portlet/journal/view.jsp(494,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f9.setValue("/journal/edit_template");
    int _jspx_eval_portlet_005fparam_005f9 = _jspx_th_portlet_005fparam_005f9.doStartTag();
    if (_jspx_th_portlet_005fparam_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f0 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f0 = _jspx_th_portlet_005fnamespace_005f0.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
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

  private boolean _jspx_meth_portlet_005fnamespace_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f6 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f6.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f6 = _jspx_th_portlet_005fnamespace_005f6.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f7 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f7.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f7 = _jspx_th_portlet_005fnamespace_005f7.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f7);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f8 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f8.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f8 = _jspx_th_portlet_005fnamespace_005f8.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f8);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f9 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f9.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f9 = _jspx_th_portlet_005fnamespace_005f9.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f9);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f13 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f13.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
    // /html/portlet/journal/view.jsp(530,69) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f13.setName("struts_action");
    // /html/portlet/journal/view.jsp(530,69) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f13.setValue("/journal/edit_article");
    int _jspx_eval_portlet_005fparam_005f13 = _jspx_th_portlet_005fparam_005f13.doStartTag();
    if (_jspx_th_portlet_005fparam_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f13);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f10 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f10.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f10 = _jspx_th_portlet_005fnamespace_005f10.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f10);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f11 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f11.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f11 = _jspx_th_portlet_005fnamespace_005f11.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f11);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f12 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f12.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f12 = _jspx_th_portlet_005fnamespace_005f12.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f12);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f13 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f13.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f13 = _jspx_th_portlet_005fnamespace_005f13.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f13);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f14 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f14.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f14 = _jspx_th_portlet_005fnamespace_005f14.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f14);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f15 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f15.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f15 = _jspx_th_portlet_005fnamespace_005f15.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f15);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f16(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f16 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f16.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f16 = _jspx_th_portlet_005fnamespace_005f16.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f16);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f16);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f17(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f17 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f17.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f17 = _jspx_th_portlet_005fnamespace_005f17.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f17);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f17);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f18(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f18 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f18.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f18 = _jspx_th_portlet_005fnamespace_005f18.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f18);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f18);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f19(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f19 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f19.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f19 = _jspx_th_portlet_005fnamespace_005f19.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f19);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f19);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f15 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f15.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
    // /html/portlet/journal/view.jsp(544,69) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f15.setName("struts_action");
    // /html/portlet/journal/view.jsp(544,69) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f15.setValue("/journal/edit_feed");
    int _jspx_eval_portlet_005fparam_005f15 = _jspx_th_portlet_005fparam_005f15.doStartTag();
    if (_jspx_th_portlet_005fparam_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f15);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f20(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f20 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f20.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f20 = _jspx_th_portlet_005fnamespace_005f20.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f20);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f20);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f21(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f21 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f21.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f21 = _jspx_th_portlet_005fnamespace_005f21.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f21);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f21);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f22(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f22 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f22.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f22 = _jspx_th_portlet_005fnamespace_005f22.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f22);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f22);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f23(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f23 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f23.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f23 = _jspx_th_portlet_005fnamespace_005f23.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f23);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f23);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f24(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f24 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f24.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f24 = _jspx_th_portlet_005fnamespace_005f24.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f24);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f24);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f25(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f25 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f25.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f25 = _jspx_th_portlet_005fnamespace_005f25.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f25);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f25);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f26(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f26 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f26.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f26 = _jspx_th_portlet_005fnamespace_005f26.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f26);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f26);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f27(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f27 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f27.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f27 = _jspx_th_portlet_005fnamespace_005f27.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f27);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f27);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f28(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f28 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f28.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f28 = _jspx_th_portlet_005fnamespace_005f28.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f28);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f28);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f29(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f29 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f29.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f29.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f29 = _jspx_th_portlet_005fnamespace_005f29.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f29.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f29);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f29);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f17(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f17 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f17.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f2);
    // /html/portlet/journal/view.jsp(558,69) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f17.setName("struts_action");
    // /html/portlet/journal/view.jsp(558,69) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f17.setValue("/journal/edit_structure");
    int _jspx_eval_portlet_005fparam_005f17 = _jspx_th_portlet_005fparam_005f17.doStartTag();
    if (_jspx_th_portlet_005fparam_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f17);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f17);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f30(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f30 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f30.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f30.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f30 = _jspx_th_portlet_005fnamespace_005f30.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f30.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f30);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f30);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f31(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f31 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f31.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f31.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f31 = _jspx_th_portlet_005fnamespace_005f31.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f31.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f31);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f31);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f32(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f32 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f32.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f32.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f32 = _jspx_th_portlet_005fnamespace_005f32.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f32.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f32);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f32);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f33(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f33 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f33.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f33.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f33 = _jspx_th_portlet_005fnamespace_005f33.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f33.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f33);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f33);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f34(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f34 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f34.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f34.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f34 = _jspx_th_portlet_005fnamespace_005f34.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f34.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f34);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f34);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f35(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f35 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f35.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f35.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f35 = _jspx_th_portlet_005fnamespace_005f35.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f35.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f35);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f35);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f36(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f36 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f36.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f36.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f36 = _jspx_th_portlet_005fnamespace_005f36.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f36.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f36);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f36);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f37(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f37 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f37.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f37.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f37 = _jspx_th_portlet_005fnamespace_005f37.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f37.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f37);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f37);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f38(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f38 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f38.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f38.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f38 = _jspx_th_portlet_005fnamespace_005f38.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f38.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f38);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f38);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f39(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f39 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f39.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f39.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f39 = _jspx_th_portlet_005fnamespace_005f39.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f39.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f39);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f39);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f19(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f19 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f19.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f3);
    // /html/portlet/journal/view.jsp(572,69) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f19.setName("struts_action");
    // /html/portlet/journal/view.jsp(572,69) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f19.setValue("/journal/edit_template");
    int _jspx_eval_portlet_005fparam_005f19 = _jspx_th_portlet_005fparam_005f19.doStartTag();
    if (_jspx_th_portlet_005fparam_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f19);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f19);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f40(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f40 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f40.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f40.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f40 = _jspx_th_portlet_005fnamespace_005f40.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f40.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f40);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f40);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f41(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f41 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f41.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f41.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f41 = _jspx_th_portlet_005fnamespace_005f41.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f41.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f41);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f41);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f42(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f42 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f42.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f42.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f42 = _jspx_th_portlet_005fnamespace_005f42.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f42.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f42);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f42);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f43(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f43 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f43.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f43.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f43 = _jspx_th_portlet_005fnamespace_005f43.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f43.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f43);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f43);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f44(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f44 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f44.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f44.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f44 = _jspx_th_portlet_005fnamespace_005f44.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f44.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f44);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f44);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f45(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f45 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f45.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f45.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f45 = _jspx_th_portlet_005fnamespace_005f45.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f45.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f45);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f45);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f46(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f46 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f46.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f46.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f46 = _jspx_th_portlet_005fnamespace_005f46.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f46.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f46);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f46);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f47(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f47 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f47.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f47.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f47 = _jspx_th_portlet_005fnamespace_005f47.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f47.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f47);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f47);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f48(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f48 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f48.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f48.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f48 = _jspx_th_portlet_005fnamespace_005f48.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f48.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f48);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f48);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f49(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f49 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f49.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f49.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    int _jspx_eval_portlet_005fnamespace_005f49 = _jspx_th_portlet_005fnamespace_005f49.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f49.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f49);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f49);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f21(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f21 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f21.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f4);
    // /html/portlet/journal/view.jsp(586,69) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f21.setName("struts_action");
    // /html/portlet/journal/view.jsp(586,69) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f21.setValue("/journal/edit_article");
    int _jspx_eval_portlet_005fparam_005f21 = _jspx_th_portlet_005fparam_005f21.doStartTag();
    if (_jspx_th_portlet_005fparam_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f21);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f21);
    return false;
  }
}
