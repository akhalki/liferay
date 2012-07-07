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

public final class article_005fsearch_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(27);
    _jspx_dependants.add("/html/portlet/journal/init.jsp");
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
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dtoggle_0026_005fid_005fdisplayTerms_005fbuttonLabel;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffieldset;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fcolumn;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fsize_005fname_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fselect_0026_005fname;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_002drow_0026_005fcssClass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fportletName;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dtoggle_0026_005fid_005fdisplayTerms_005fbuttonLabel = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffieldset = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fcolumn = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fsize_005fname_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_002drow_0026_005fcssClass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fportletName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dtoggle_0026_005fid_005fdisplayTerms_005fbuttonLabel.release();
    _005fjspx_005ftagPool_005faui_005ffieldset.release();
    _005fjspx_005ftagPool_005faui_005fcolumn.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fsize_005fname_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.release();
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005fc_005fotherwise.release();
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel.release();
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005faui_005fbutton_002drow_0026_005fcssClass.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.release();
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fscript.release();
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fportletName.release();
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

ArticleSearch searchContainer = (ArticleSearch)request.getAttribute("liferay-ui:search:searchContainer");

ArticleDisplayTerms displayTerms = (ArticleDisplayTerms)searchContainer.getDisplayTerms();

      out.write('\n');
      out.write('\n');
      //  liferay-ui:search-toggle
      com.liferay.taglib.ui.SearchToggleTag _jspx_th_liferay_002dui_005fsearch_002dtoggle_005f0 = (com.liferay.taglib.ui.SearchToggleTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dtoggle_0026_005fid_005fdisplayTerms_005fbuttonLabel.get(com.liferay.taglib.ui.SearchToggleTag.class);
      _jspx_th_liferay_002dui_005fsearch_002dtoggle_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dui_005fsearch_002dtoggle_005f0.setParent(null);
      // /html/portlet/journal/article_search.jsp(25,0) name = id type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dui_005fsearch_002dtoggle_005f0.setId("toggle_id_journal_article_search");
      // /html/portlet/journal/article_search.jsp(25,0) name = displayTerms type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dui_005fsearch_002dtoggle_005f0.setDisplayTerms( displayTerms );
      // /html/portlet/journal/article_search.jsp(25,0) name = buttonLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dui_005fsearch_002dtoggle_005f0.setButtonLabel("search");
      int _jspx_eval_liferay_002dui_005fsearch_002dtoggle_005f0 = _jspx_th_liferay_002dui_005fsearch_002dtoggle_005f0.doStartTag();
      if (_jspx_eval_liferay_002dui_005fsearch_002dtoggle_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_liferay_002dui_005fsearch_002dtoggle_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_liferay_002dui_005fsearch_002dtoggle_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_liferay_002dui_005fsearch_002dtoggle_005f0.doInitBody();
        }
        do {
          out.write('\n');
          out.write('	');
          //  aui:fieldset
          com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f0 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset.get(com.liferay.taglib.aui.FieldsetTag.class);
          _jspx_th_aui_005ffieldset_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005ffieldset_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fsearch_002dtoggle_005f0);
          int _jspx_eval_aui_005ffieldset_005f0 = _jspx_th_aui_005ffieldset_005f0.doStartTag();
          if (_jspx_eval_aui_005ffieldset_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_aui_005ffieldset_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_aui_005ffieldset_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_aui_005ffieldset_005f0.doInitBody();
            }
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  aui:column
              com.liferay.taglib.aui.ColumnTag _jspx_th_aui_005fcolumn_005f0 = (com.liferay.taglib.aui.ColumnTag) _005fjspx_005ftagPool_005faui_005fcolumn.get(com.liferay.taglib.aui.ColumnTag.class);
              _jspx_th_aui_005fcolumn_005f0.setPageContext(_jspx_page_context);
              _jspx_th_aui_005fcolumn_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
              int _jspx_eval_aui_005fcolumn_005f0 = _jspx_th_aui_005fcolumn_005f0.doStartTag();
              if (_jspx_eval_aui_005fcolumn_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_aui_005fcolumn_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_aui_005fcolumn_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_aui_005fcolumn_005f0.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:input
                  com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f0 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fsize_005fname_005flabel_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                  _jspx_th_aui_005finput_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005finput_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fcolumn_005f0);
                  // /html/portlet/journal/article_search.jsp(32,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f0.setLabel("id");
                  // /html/portlet/journal/article_search.jsp(32,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f0.setName( displayTerms.ARTICLE_ID );
                  // /html/portlet/journal/article_search.jsp(32,3) null
                  _jspx_th_aui_005finput_005f0.setDynamicAttribute(null, "size", new String("20"));
                  // /html/portlet/journal/article_search.jsp(32,3) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f0.setValue( displayTerms.getArticleId() );
                  int _jspx_eval_aui_005finput_005f0 = _jspx_th_aui_005finput_005f0.doStartTag();
                  if (_jspx_th_aui_005finput_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fsize_005fname_005flabel_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fsize_005fname_005flabel_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:input
                  com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f1 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                  _jspx_th_aui_005finput_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005finput_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fcolumn_005f0);
                  // /html/portlet/journal/article_search.jsp(34,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f1.setName( displayTerms.CONTENT );
                  // /html/portlet/journal/article_search.jsp(34,3) null
                  _jspx_th_aui_005finput_005f1.setDynamicAttribute(null, "size", new String("20"));
                  // /html/portlet/journal/article_search.jsp(34,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f1.setType("text");
                  // /html/portlet/journal/article_search.jsp(34,3) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f1.setValue( displayTerms.getContent() );
                  int _jspx_eval_aui_005finput_005f1 = _jspx_th_aui_005finput_005f1.doStartTag();
                  if (_jspx_th_aui_005finput_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_aui_005fcolumn_005f0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_aui_005fcolumn_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_aui_005fcolumn_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005fcolumn.reuse(_jspx_th_aui_005fcolumn_005f0);
                return;
              }
              _005fjspx_005ftagPool_005faui_005fcolumn.reuse(_jspx_th_aui_005fcolumn_005f0);
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  aui:column
              com.liferay.taglib.aui.ColumnTag _jspx_th_aui_005fcolumn_005f1 = (com.liferay.taglib.aui.ColumnTag) _005fjspx_005ftagPool_005faui_005fcolumn.get(com.liferay.taglib.aui.ColumnTag.class);
              _jspx_th_aui_005fcolumn_005f1.setPageContext(_jspx_page_context);
              _jspx_th_aui_005fcolumn_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
              int _jspx_eval_aui_005fcolumn_005f1 = _jspx_th_aui_005fcolumn_005f1.doStartTag();
              if (_jspx_eval_aui_005fcolumn_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_aui_005fcolumn_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_aui_005fcolumn_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_aui_005fcolumn_005f1.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:input
                  com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f2 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                  _jspx_th_aui_005finput_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005finput_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fcolumn_005f1);
                  // /html/portlet/journal/article_search.jsp(38,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f2.setName( displayTerms.VERSION );
                  // /html/portlet/journal/article_search.jsp(38,3) null
                  _jspx_th_aui_005finput_005f2.setDynamicAttribute(null, "size", new String("20"));
                  // /html/portlet/journal/article_search.jsp(38,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f2.setType("text");
                  // /html/portlet/journal/article_search.jsp(38,3) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f2.setValue( displayTerms.getVersionString() );
                  int _jspx_eval_aui_005finput_005f2 = _jspx_th_aui_005finput_005f2.doStartTag();
                  if (_jspx_th_aui_005finput_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f2);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f2);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:select
                  com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f0 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.get(com.liferay.taglib.aui.SelectTag.class);
                  _jspx_th_aui_005fselect_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fselect_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fcolumn_005f1);
                  // /html/portlet/journal/article_search.jsp(40,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fselect_005f0.setName( displayTerms.TYPE );
                  int _jspx_eval_aui_005fselect_005f0 = _jspx_th_aui_005fselect_005f0.doStartTag();
                  if (_jspx_eval_aui_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005fselect_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005fselect_005f0.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t");
                      if (_jspx_meth_aui_005foption_005f0(_jspx_th_aui_005fselect_005f0, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");

				for (int i = 0; i < JournalArticleConstants.TYPES.length; i++) {
				
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t");
                      //  aui:option
                      com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f1 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                      _jspx_th_aui_005foption_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005foption_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f0);
                      // /html/portlet/journal/article_search.jsp(47,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005foption_005f1.setLabel( JournalArticleConstants.TYPES[i] );
                      // /html/portlet/journal/article_search.jsp(47,5) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005foption_005f1.setSelected( displayTerms.getType().equals(JournalArticleConstants.TYPES[i]) );
                      int _jspx_eval_aui_005foption_005f1 = _jspx_th_aui_005foption_005f1.doStartTag();
                      if (_jspx_th_aui_005foption_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f1);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f1);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");

				}
				
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t");
                      int evalDoAfterBody = _jspx_th_aui_005fselect_005f0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_aui_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_aui_005fselect_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.reuse(_jspx_th_aui_005fselect_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.reuse(_jspx_th_aui_005fselect_005f0);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_aui_005fcolumn_005f1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_aui_005fcolumn_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_aui_005fcolumn_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005fcolumn.reuse(_jspx_th_aui_005fcolumn_005f1);
                return;
              }
              _005fjspx_005ftagPool_005faui_005fcolumn.reuse(_jspx_th_aui_005fcolumn_005f1);
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  aui:column
              com.liferay.taglib.aui.ColumnTag _jspx_th_aui_005fcolumn_005f2 = (com.liferay.taglib.aui.ColumnTag) _005fjspx_005ftagPool_005faui_005fcolumn.get(com.liferay.taglib.aui.ColumnTag.class);
              _jspx_th_aui_005fcolumn_005f2.setPageContext(_jspx_page_context);
              _jspx_th_aui_005fcolumn_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
              int _jspx_eval_aui_005fcolumn_005f2 = _jspx_th_aui_005fcolumn_005f2.doStartTag();
              if (_jspx_eval_aui_005fcolumn_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_aui_005fcolumn_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_aui_005fcolumn_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_aui_005fcolumn_005f2.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:input
                  com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f3 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                  _jspx_th_aui_005finput_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005finput_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fcolumn_005f2);
                  // /html/portlet/journal/article_search.jsp(57,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f3.setLabel("name");
                  // /html/portlet/journal/article_search.jsp(57,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f3.setName( displayTerms.TITLE );
                  // /html/portlet/journal/article_search.jsp(57,3) null
                  _jspx_th_aui_005finput_005f3.setDynamicAttribute(null, "size", new String("20"));
                  // /html/portlet/journal/article_search.jsp(57,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f3.setType("text");
                  // /html/portlet/journal/article_search.jsp(57,3) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f3.setValue( displayTerms.getTitle() );
                  int _jspx_eval_aui_005finput_005f3 = _jspx_th_aui_005finput_005f3.doStartTag();
                  if (_jspx_th_aui_005finput_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fnobody.reuse(_jspx_th_aui_005finput_005f3);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005fnobody.reuse(_jspx_th_aui_005finput_005f3);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  c:choose
                  org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f0 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                  _jspx_th_c_005fchoose_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fchoose_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fcolumn_005f2);
                  int _jspx_eval_c_005fchoose_005f0 = _jspx_th_c_005fchoose_005f0.doStartTag();
                  if (_jspx_eval_c_005fchoose_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  c:when
                      org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f0 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                      _jspx_th_c_005fwhen_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fwhen_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
                      // /html/portlet/journal/article_search.jsp(60,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fwhen_005f0.setTest( portletName.equals(PortletKeys.JOURNAL) );
                      int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
                      if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  aui:select
                          com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f1 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.get(com.liferay.taglib.aui.SelectTag.class);
                          _jspx_th_aui_005fselect_005f1.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005fselect_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
                          // /html/portlet/journal/article_search.jsp(61,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005fselect_005f1.setName( displayTerms.STATUS );
                          int _jspx_eval_aui_005fselect_005f1 = _jspx_th_aui_005fselect_005f1.doStartTag();
                          if (_jspx_eval_aui_005fselect_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_aui_005fselect_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_aui_005fselect_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_aui_005fselect_005f1.doInitBody();
                            }
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
                              if (_jspx_meth_aui_005foption_005f2(_jspx_th_aui_005fselect_005f1, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
                              //  aui:option
                              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f3 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                              _jspx_th_aui_005foption_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005foption_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f1);
                              // /html/portlet/journal/article_search.jsp(63,6) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f3.setLabel(new String("draft"));
                              // /html/portlet/journal/article_search.jsp(63,6) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f3.setSelected( displayTerms.getStatus().equals("draft") );
                              int _jspx_eval_aui_005foption_005f3 = _jspx_th_aui_005foption_005f3.doStartTag();
                              if (_jspx_th_aui_005foption_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f3);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f3);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
                              //  aui:option
                              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f4 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                              _jspx_th_aui_005foption_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005foption_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f1);
                              // /html/portlet/journal/article_search.jsp(64,6) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f4.setLabel(new String("pending"));
                              // /html/portlet/journal/article_search.jsp(64,6) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f4.setSelected( displayTerms.getStatus().equals("pending") );
                              int _jspx_eval_aui_005foption_005f4 = _jspx_th_aui_005foption_005f4.doStartTag();
                              if (_jspx_th_aui_005foption_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f4);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f4);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
                              //  aui:option
                              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f5 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                              _jspx_th_aui_005foption_005f5.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005foption_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f1);
                              // /html/portlet/journal/article_search.jsp(65,6) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f5.setLabel(new String("approved"));
                              // /html/portlet/journal/article_search.jsp(65,6) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f5.setSelected( displayTerms.getStatus().equals("approved") );
                              int _jspx_eval_aui_005foption_005f5 = _jspx_th_aui_005foption_005f5.doStartTag();
                              if (_jspx_th_aui_005foption_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f5);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f5);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
                              //  aui:option
                              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f6 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                              _jspx_th_aui_005foption_005f6.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005foption_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f1);
                              // /html/portlet/journal/article_search.jsp(66,6) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f6.setLabel(new String("expired"));
                              // /html/portlet/journal/article_search.jsp(66,6) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f6.setSelected( displayTerms.getStatus().equals("expired") );
                              int _jspx_eval_aui_005foption_005f6 = _jspx_th_aui_005foption_005f6.doStartTag();
                              if (_jspx_th_aui_005foption_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f6);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f6);
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_aui_005fselect_005f1.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_aui_005fselect_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                            }
                          }
                          if (_jspx_th_aui_005fselect_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.reuse(_jspx_th_aui_005fselect_005f1);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005fselect_0026_005fname.reuse(_jspx_th_aui_005fselect_005f1);
                          out.write("\n");
                          out.write("\t\t\t\t");
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
                      out.write("\t\t\t\t");
                      //  c:otherwise
                      org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                      _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
                      int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
                      if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t");

					List<Group> myPlaces = user.getMyPlaces();
					
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  aui:select
                          com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f2 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel.get(com.liferay.taglib.aui.SelectTag.class);
                          _jspx_th_aui_005fselect_005f2.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005fselect_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
                          // /html/portlet/journal/article_search.jsp(75,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005fselect_005f2.setLabel("my-places");
                          // /html/portlet/journal/article_search.jsp(75,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005fselect_005f2.setName( displayTerms.GROUP_ID );
                          int _jspx_eval_aui_005fselect_005f2 = _jspx_th_aui_005fselect_005f2.doStartTag();
                          if (_jspx_eval_aui_005fselect_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_aui_005fselect_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_aui_005fselect_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_aui_005fselect_005f2.doInitBody();
                            }
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
                              //  aui:option
                              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f7 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                              _jspx_th_aui_005foption_005f7.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005foption_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f2);
                              // /html/portlet/journal/article_search.jsp(76,6) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f7.setLabel(new String("global"));
                              // /html/portlet/journal/article_search.jsp(76,6) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f7.setSelected( displayTerms.getGroupId() == themeDisplay.getCompanyGroupId() );
                              // /html/portlet/journal/article_search.jsp(76,6) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f7.setValue( themeDisplay.getCompanyGroupId() );
                              int _jspx_eval_aui_005foption_005f7 = _jspx_th_aui_005foption_005f7.doStartTag();
                              if (_jspx_th_aui_005foption_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f7);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f7);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");

						for (Group myPlace : myPlaces) {
							if (myPlace.hasStagingGroup()) {
								myPlace = myPlace.getStagingGroup();
							}
						
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t");
                              //  aui:option
                              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f8 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                              _jspx_th_aui_005foption_005f8.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005foption_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f2);
                              // /html/portlet/journal/article_search.jsp(85,7) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f8.setLabel( myPlace.isUser() ? "my-community" : HtmlUtil.escape(myPlace.getDescriptiveName()) );
                              // /html/portlet/journal/article_search.jsp(85,7) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f8.setSelected( displayTerms.getGroupId() == myPlace.getGroupId() );
                              // /html/portlet/journal/article_search.jsp(85,7) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f8.setValue( myPlace.getGroupId() );
                              int _jspx_eval_aui_005foption_005f8 = _jspx_th_aui_005foption_005f8.doStartTag();
                              if (_jspx_th_aui_005foption_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f8);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f8);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");

						}
						
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f2);
                              // /html/portlet/journal/article_search.jsp(91,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f0.setTest( layout.hasScopeGroup() );
                              int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
                              if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t");
                              //  aui:option
                              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f9 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                              _jspx_th_aui_005foption_005f9.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005foption_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
                              // /html/portlet/journal/article_search.jsp(92,7) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f9.setLabel( LanguageUtil.get(pageContext,"current-page") + " (" + HtmlUtil.escape(layout.getName(locale)) + ")" );
                              // /html/portlet/journal/article_search.jsp(92,7) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f9.setSelected( displayTerms.getGroupId() == layout.getScopeGroup().getGroupId() );
                              // /html/portlet/journal/article_search.jsp(92,7) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f9.setValue( layout.getScopeGroup().getGroupId() );
                              int _jspx_eval_aui_005foption_005f9 = _jspx_th_aui_005foption_005f9.doStartTag();
                              if (_jspx_th_aui_005foption_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f9);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f9);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_aui_005fselect_005f2.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_aui_005fselect_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                            }
                          }
                          if (_jspx_th_aui_005fselect_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel.reuse(_jspx_th_aui_005fselect_005f2);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel.reuse(_jspx_th_aui_005fselect_005f2);
                          out.write("\n");
                          out.write("\t\t\t\t");
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
                      out.write("\t\t\t");
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
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_aui_005fcolumn_005f2.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_aui_005fcolumn_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_aui_005fcolumn_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005fcolumn.reuse(_jspx_th_aui_005fcolumn_005f2);
                return;
              }
              _005fjspx_005ftagPool_005faui_005fcolumn.reuse(_jspx_th_aui_005fcolumn_005f2);
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  aui:column
              com.liferay.taglib.aui.ColumnTag _jspx_th_aui_005fcolumn_005f3 = (com.liferay.taglib.aui.ColumnTag) _005fjspx_005ftagPool_005faui_005fcolumn.get(com.liferay.taglib.aui.ColumnTag.class);
              _jspx_th_aui_005fcolumn_005f3.setPageContext(_jspx_page_context);
              _jspx_th_aui_005fcolumn_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
              int _jspx_eval_aui_005fcolumn_005f3 = _jspx_th_aui_005fcolumn_005f3.doStartTag();
              if (_jspx_eval_aui_005fcolumn_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_aui_005fcolumn_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_aui_005fcolumn_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_aui_005fcolumn_005f3.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:input
                  com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f4 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                  _jspx_th_aui_005finput_005f4.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005finput_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fcolumn_005f3);
                  // /html/portlet/journal/article_search.jsp(100,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f4.setName( displayTerms.DESCRIPTION );
                  // /html/portlet/journal/article_search.jsp(100,3) null
                  _jspx_th_aui_005finput_005f4.setDynamicAttribute(null, "size", new String("20"));
                  // /html/portlet/journal/article_search.jsp(100,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f4.setType("text");
                  // /html/portlet/journal/article_search.jsp(100,3) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f4.setValue( displayTerms.getDescription() );
                  int _jspx_eval_aui_005finput_005f4 = _jspx_th_aui_005finput_005f4.doStartTag();
                  if (_jspx_th_aui_005finput_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f4);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f4);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_aui_005fcolumn_005f3.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_aui_005fcolumn_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_aui_005fcolumn_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005fcolumn.reuse(_jspx_th_aui_005fcolumn_005f3);
                return;
              }
              _005fjspx_005ftagPool_005faui_005fcolumn.reuse(_jspx_th_aui_005fcolumn_005f3);
              out.write('\n');
              out.write('	');
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
          out.write('\n');
          int evalDoAfterBody = _jspx_th_liferay_002dui_005fsearch_002dtoggle_005f0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_liferay_002dui_005fsearch_002dtoggle_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
        }
      }
      if (_jspx_th_liferay_002dui_005fsearch_002dtoggle_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dtoggle_0026_005fid_005fdisplayTerms_005fbuttonLabel.reuse(_jspx_th_liferay_002dui_005fsearch_002dtoggle_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dtoggle_0026_005fid_005fdisplayTerms_005fbuttonLabel.reuse(_jspx_th_liferay_002dui_005fsearch_002dtoggle_005f0);
      out.write('\n');
      out.write('\n');

boolean showAddArticleButtonButton = false;
boolean showPermissionsButton = false;
boolean showSubscribeLink = false;

if (portletName.equals(PortletKeys.JOURNAL)) {
	showAddArticleButtonButton = JournalPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_ARTICLE);
	showPermissionsButton = GroupPermissionUtil.contains(permissionChecker, scopeGroupId, ActionKeys.PERMISSIONS);
	showSubscribeLink = JournalPermission.contains(permissionChecker, scopeGroupId, ActionKeys.SUBSCRIBE);
}

      out.write('\n');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f1.setParent(null);
      // /html/portlet/journal/article_search.jsp(117,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f1.setTest( showAddArticleButtonButton || showPermissionsButton );
      int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
      if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('\n');
          out.write('	');
          //  aui:button-row
          com.liferay.taglib.aui.ButtonRowTag _jspx_th_aui_005fbutton_002drow_005f0 = (com.liferay.taglib.aui.ButtonRowTag) _005fjspx_005ftagPool_005faui_005fbutton_002drow_0026_005fcssClass.get(com.liferay.taglib.aui.ButtonRowTag.class);
          _jspx_th_aui_005fbutton_002drow_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fbutton_002drow_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
          // /html/portlet/journal/article_search.jsp(118,1) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fbutton_002drow_005f0.setCssClass("add-permission-button-row");
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
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f0);
              // /html/portlet/journal/article_search.jsp(119,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f2.setTest( showAddArticleButtonButton );
              int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
              if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:button
                  com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f0 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                  _jspx_th_aui_005fbutton_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fbutton_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
                  // /html/portlet/journal/article_search.jsp(120,3) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f0.setOnClick( renderResponse.getNamespace() + "addArticle();" );
                  // /html/portlet/journal/article_search.jsp(120,3) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f0.setValue("add-web-content");
                  int _jspx_eval_aui_005fbutton_005f0 = _jspx_th_aui_005fbutton_005f0.doStartTag();
                  if (_jspx_th_aui_005fbutton_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
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
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f0);
              // /html/portlet/journal/article_search.jsp(123,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f3.setTest( showPermissionsButton );
              int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
              if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  liferay-security:permissionsURL
                  com.liferay.taglib.security.PermissionsURLTag _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0 = (com.liferay.taglib.security.PermissionsURLTag) _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.get(com.liferay.taglib.security.PermissionsURLTag.class);
                  _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
                  // /html/portlet/journal/article_search.jsp(124,3) name = modelResource type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setModelResource("com.liferay.portlet.journal");
                  // /html/portlet/journal/article_search.jsp(124,3) name = modelResourceDescription type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setModelResourceDescription( HtmlUtil.escape(themeDisplay.getScopeGroupName()) );
                  // /html/portlet/journal/article_search.jsp(124,3) name = resourcePrimKey type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setResourcePrimKey( String.valueOf(scopeGroupId) );
                  // /html/portlet/journal/article_search.jsp(124,3) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setVar("permissionsURL");
                  int _jspx_eval_liferay_002dsecurity_005fpermissionsURL_005f0 = _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.doStartTag();
                  if (_jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.reuse(_jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.reuse(_jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0);
                  java.lang.String permissionsURL = null;
                  permissionsURL = (java.lang.String) _jspx_page_context.findAttribute("permissionsURL");
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:button
                  com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f1 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                  _jspx_th_aui_005fbutton_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fbutton_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
                  // /html/portlet/journal/article_search.jsp(131,3) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f1.setOnClick( permissionsURL );
                  // /html/portlet/journal/article_search.jsp(131,3) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f1.setValue("permissions");
                  int _jspx_eval_aui_005fbutton_005f1 = _jspx_th_aui_005fbutton_005f1.doStartTag();
                  if (_jspx_th_aui_005fbutton_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f1);
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
              int evalDoAfterBody = _jspx_th_aui_005fbutton_002drow_005f0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_aui_005fbutton_002drow_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_aui_005fbutton_002drow_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fbutton_002drow_0026_005fcssClass.reuse(_jspx_th_aui_005fbutton_002drow_005f0);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fbutton_002drow_0026_005fcssClass.reuse(_jspx_th_aui_005fbutton_002drow_005f0);
          out.write('\n');
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
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f4.setParent(null);
      // /html/portlet/journal/article_search.jsp(136,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f4.setTest( showSubscribeLink );
      int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
      if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('\n');
          out.write('	');
          //  c:choose
          org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
          _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
          _jspx_th_c_005fchoose_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
          int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
          if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  c:when
              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f1 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
              _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
              // /html/portlet/journal/article_search.jsp(138,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fwhen_005f1.setTest( SubscriptionLocalServiceUtil.isSubscribed(company.getCompanyId(), user.getUserId(), JournalArticle.class.getName(), scopeGroupId) );
              int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
              if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:actionURL
                  com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f0 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.get(com.liferay.taglib.portlet.ActionURLTag.class);
                  _jspx_th_portlet_005factionURL_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005factionURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                  // /html/portlet/journal/article_search.jsp(139,3) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005factionURL_005f0.setVar("unsubscribeURL");
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
                      // /html/portlet/journal/article_search.jsp(141,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f1.setName( Constants.CMD );
                      // /html/portlet/journal/article_search.jsp(141,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f1.setValue( Constants.UNSUBSCRIBE );
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
                      // /html/portlet/journal/article_search.jsp(142,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f2.setName("redirect");
                      // /html/portlet/journal/article_search.jsp(142,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f2.setValue( currentURL );
                      int _jspx_eval_portlet_005fparam_005f2 = _jspx_th_portlet_005fparam_005f2.doStartTag();
                      if (_jspx_th_portlet_005fparam_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
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
                  java.lang.String unsubscribeURL = null;
                  unsubscribeURL = (java.lang.String) _jspx_page_context.findAttribute("unsubscribeURL");
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  liferay-ui:icon
                  com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f0 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fcssClass_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                  _jspx_th_liferay_002dui_005ficon_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005ficon_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                  // /html/portlet/journal/article_search.jsp(145,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ficon_005f0.setCssClass("subscribe-link");
                  // /html/portlet/journal/article_search.jsp(145,3) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ficon_005f0.setImage("unsubscribe");
                  // /html/portlet/journal/article_search.jsp(145,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ficon_005f0.setLabel( true );
                  // /html/portlet/journal/article_search.jsp(145,3) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ficon_005f0.setUrl( unsubscribeURL );
                  int _jspx_eval_liferay_002dui_005ficon_005f0 = _jspx_th_liferay_002dui_005ficon_005f0.doStartTag();
                  if (_jspx_th_liferay_002dui_005ficon_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
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
              //  c:otherwise
              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f1 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
              _jspx_th_c_005fotherwise_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fotherwise_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
              int _jspx_eval_c_005fotherwise_005f1 = _jspx_th_c_005fotherwise_005f1.doStartTag();
              if (_jspx_eval_c_005fotherwise_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:actionURL
                  com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f1 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.get(com.liferay.taglib.portlet.ActionURLTag.class);
                  _jspx_th_portlet_005factionURL_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005factionURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
                  // /html/portlet/journal/article_search.jsp(148,3) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005factionURL_005f1.setVar("subscribeURL");
                  int _jspx_eval_portlet_005factionURL_005f1 = _jspx_th_portlet_005factionURL_005f1.doStartTag();
                  if (_jspx_eval_portlet_005factionURL_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_portlet_005factionURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_portlet_005factionURL_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_portlet_005factionURL_005f1.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t");
                      if (_jspx_meth_portlet_005fparam_005f3(_jspx_th_portlet_005factionURL_005f1, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  portlet:param
                      com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f4 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                      _jspx_th_portlet_005fparam_005f4.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005fparam_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
                      // /html/portlet/journal/article_search.jsp(150,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f4.setName( Constants.CMD );
                      // /html/portlet/journal/article_search.jsp(150,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f4.setValue( Constants.SUBSCRIBE );
                      int _jspx_eval_portlet_005fparam_005f4 = _jspx_th_portlet_005fparam_005f4.doStartTag();
                      if (_jspx_th_portlet_005fparam_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  portlet:param
                      com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f5 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                      _jspx_th_portlet_005fparam_005f5.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005fparam_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
                      // /html/portlet/journal/article_search.jsp(151,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f5.setName("redirect");
                      // /html/portlet/journal/article_search.jsp(151,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f5.setValue( currentURL );
                      int _jspx_eval_portlet_005fparam_005f5 = _jspx_th_portlet_005fparam_005f5.doStartTag();
                      if (_jspx_th_portlet_005fparam_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
                      out.write("\n");
                      out.write("\t\t\t");
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
                  java.lang.String subscribeURL = null;
                  subscribeURL = (java.lang.String) _jspx_page_context.findAttribute("subscribeURL");
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  liferay-ui:icon
                  com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f1 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fcssClass_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                  _jspx_th_liferay_002dui_005ficon_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005ficon_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
                  // /html/portlet/journal/article_search.jsp(154,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ficon_005f1.setCssClass("subscribe-link");
                  // /html/portlet/journal/article_search.jsp(154,3) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ficon_005f1.setImage("subscribe");
                  // /html/portlet/journal/article_search.jsp(154,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ficon_005f1.setLabel( true );
                  // /html/portlet/journal/article_search.jsp(154,3) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ficon_005f1.setUrl( subscribeURL );
                  int _jspx_eval_liferay_002dui_005ficon_005f1 = _jspx_th_liferay_002dui_005ficon_005f1.doStartTag();
                  if (_jspx_th_liferay_002dui_005ficon_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
                  out.write('\n');
                  out.write('	');
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
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f5 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f5.setParent(null);
      // /html/portlet/journal/article_search.jsp(159,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f5.setTest( Validator.isNotNull(displayTerms.getStructureId()) );
      int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
      if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f5 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f5.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
          // /html/portlet/journal/article_search.jsp(160,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f5.setName( displayTerms.STRUCTURE_ID );
          // /html/portlet/journal/article_search.jsp(160,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f5.setType("hidden");
          // /html/portlet/journal/article_search.jsp(160,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f5.setValue( displayTerms.getStructureId() );
          int _jspx_eval_aui_005finput_005f5 = _jspx_th_aui_005finput_005f5.doStartTag();
          if (_jspx_th_aui_005finput_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f5);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f5);
          out.write("\n");
          out.write("\n");
          out.write("\t<div class=\"portlet-msg-info\">\n");
          out.write("\t\t");
          if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_th_c_005fif_005f5, _jspx_page_context))
            return;
          out.write(':');
          out.write(' ');
          out.print( displayTerms.getStructureId() );
          out.write("<br />\n");
          out.write("\t</div>\n");
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
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f6 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f6.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f6.setParent(null);
      // /html/portlet/journal/article_search.jsp(167,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f6.setTest( Validator.isNotNull(displayTerms.getTemplateId()) );
      int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
      if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f6 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f6.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
          // /html/portlet/journal/article_search.jsp(168,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f6.setName( displayTerms.TEMPLATE_ID );
          // /html/portlet/journal/article_search.jsp(168,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f6.setType("hidden");
          // /html/portlet/journal/article_search.jsp(168,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f6.setValue( displayTerms.getTemplateId() );
          int _jspx_eval_aui_005finput_005f6 = _jspx_th_aui_005finput_005f6.doStartTag();
          if (_jspx_th_aui_005finput_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f6);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f6);
          out.write("\n");
          out.write("\n");
          out.write("\t<div class=\"portlet-msg-info\">\n");
          out.write("\t\t");
          if (_jspx_meth_liferay_002dui_005fmessage_005f1(_jspx_th_c_005fif_005f6, _jspx_page_context))
            return;
          out.write(':');
          out.write(' ');
          out.print( displayTerms.getTemplateId() );
          out.write("<br />\n");
          out.write("\t</div>\n");
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
          out.write("\tfunction ");
          if (_jspx_meth_portlet_005fnamespace_005f0(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("addArticle() {\n");
          out.write("\t\tvar url = '");
          //  liferay-portlet:renderURL
          com.liferay.taglib.portlet.RenderURLTag _jspx_th_liferay_002dportlet_005frenderURL_005f0 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fportletName.get(com.liferay.taglib.portlet.RenderURLTag.class);
          _jspx_th_liferay_002dportlet_005frenderURL_005f0.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dportlet_005frenderURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          // /html/portlet/journal/article_search.jsp(177,13) name = windowState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dportlet_005frenderURL_005f0.setWindowState( WindowState.MAXIMIZED.toString() );
          // /html/portlet/journal/article_search.jsp(177,13) name = portletName type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dportlet_005frenderURL_005f0.setPortletName( PortletKeys.JOURNAL );
          int _jspx_eval_liferay_002dportlet_005frenderURL_005f0 = _jspx_th_liferay_002dportlet_005frenderURL_005f0.doStartTag();
          if (_jspx_eval_liferay_002dportlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_liferay_002dportlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_liferay_002dportlet_005frenderURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_liferay_002dportlet_005frenderURL_005f0.doInitBody();
            }
            do {
              if (_jspx_meth_portlet_005fparam_005f6(_jspx_th_liferay_002dportlet_005frenderURL_005f0, _jspx_page_context))
                return;
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f7 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f7.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f0);
              // /html/portlet/journal/article_search.jsp(177,203) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f7.setName("redirect");
              // /html/portlet/journal/article_search.jsp(177,203) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f7.setValue( currentURL );
              int _jspx_eval_portlet_005fparam_005f7 = _jspx_th_portlet_005fparam_005f7.doStartTag();
              if (_jspx_th_portlet_005fparam_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f8 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f8.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f0);
              // /html/portlet/journal/article_search.jsp(177,262) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f8.setName("structureId");
              // /html/portlet/journal/article_search.jsp(177,262) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f8.setValue( displayTerms.getStructureId() );
              int _jspx_eval_portlet_005fparam_005f8 = _jspx_th_portlet_005fparam_005f8.doStartTag();
              if (_jspx_th_portlet_005fparam_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f9 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f9.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f0);
              // /html/portlet/journal/article_search.jsp(177,343) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f9.setName("templateId");
              // /html/portlet/journal/article_search.jsp(177,343) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f9.setValue( displayTerms.getTemplateId() );
              int _jspx_eval_portlet_005fparam_005f9 = _jspx_th_portlet_005fparam_005f9.doStartTag();
              if (_jspx_th_portlet_005fparam_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
              int evalDoAfterBody = _jspx_th_liferay_002dportlet_005frenderURL_005f0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_liferay_002dportlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_liferay_002dportlet_005frenderURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fportletName.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dportlet_005frenderURL_0026_005fwindowState_005fportletName.reuse(_jspx_th_liferay_002dportlet_005frenderURL_005f0);
          out.write("';\n");
          out.write("\n");
          out.write("\t\tif (toggle_id_journal_article_searchcurClickValue == 'basic') {\n");
          out.write("\t\t\turl += '&");
          if (_jspx_meth_portlet_005fnamespace_005f1(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.print( displayTerms.TITLE );
          out.write("=' + document.");
          if (_jspx_meth_portlet_005fnamespace_005f2(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f3(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.print( displayTerms.KEYWORDS );
          out.write(".value;\n");
          out.write("\n");
          out.write("\t\t\tsubmitForm(document.hrefFm, url);\n");
          out.write("\t\t}\n");
          out.write("\t\telse {\n");
          out.write("\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f4(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("fm.method = 'post';\n");
          out.write("\t\t\tsubmitForm(document.");
          if (_jspx_meth_portlet_005fnamespace_005f5(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
            return;
          out.write("fm, url);\n");
          out.write("\t\t}\n");
          out.write("\t}\n");
          out.write("\n");
          out.write("\t");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f7 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f7.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
          // /html/portlet/journal/article_search.jsp(190,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f7.setTest( windowState.equals(WindowState.MAXIMIZED) );
          int _jspx_eval_c_005fif_005f7 = _jspx_th_c_005fif_005f7.doStartTag();
          if (_jspx_eval_c_005fif_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\tLiferay.Util.focusFormField(document.");
              if (_jspx_meth_portlet_005fnamespace_005f6(_jspx_th_c_005fif_005f7, _jspx_page_context))
                return;
              out.write('f');
              out.write('m');
              out.write('.');
              if (_jspx_meth_portlet_005fnamespace_005f7(_jspx_th_c_005fif_005f7, _jspx_page_context))
                return;
              out.print( displayTerms.ARTICLE_ID );
              out.write(");\n");
              out.write("\t\tLiferay.Util.focusFormField(document.");
              if (_jspx_meth_portlet_005fnamespace_005f8(_jspx_th_c_005fif_005f7, _jspx_page_context))
                return;
              out.write('f');
              out.write('m');
              out.write('.');
              if (_jspx_meth_portlet_005fnamespace_005f9(_jspx_th_c_005fif_005f7, _jspx_page_context))
                return;
              out.print( displayTerms.KEYWORDS );
              out.write(");\n");
              out.write("\t");
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

  private boolean _jspx_meth_aui_005foption_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f0 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f0.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f0);
    // /html/portlet/journal/article_search.jsp(41,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f0.setValue(new String(""));
    int _jspx_eval_aui_005foption_005f0 = _jspx_th_aui_005foption_005f0.doStartTag();
    if (_jspx_th_aui_005foption_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fnobody.reuse(_jspx_th_aui_005foption_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fnobody.reuse(_jspx_th_aui_005foption_005f0);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f2 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f2.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f1);
    // /html/portlet/journal/article_search.jsp(62,6) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f2.setValue(new String(""));
    int _jspx_eval_aui_005foption_005f2 = _jspx_th_aui_005foption_005f2.doStartTag();
    if (_jspx_th_aui_005foption_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fnobody.reuse(_jspx_th_aui_005foption_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fnobody.reuse(_jspx_th_aui_005foption_005f2);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f0 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
    // /html/portlet/journal/article_search.jsp(140,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setName("struts_action");
    // /html/portlet/journal/article_search.jsp(140,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setValue("/journal/edit_article");
    int _jspx_eval_portlet_005fparam_005f0 = _jspx_th_portlet_005fparam_005f0.doStartTag();
    if (_jspx_th_portlet_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f3 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f3.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
    // /html/portlet/journal/article_search.jsp(149,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f3.setName("struts_action");
    // /html/portlet/journal/article_search.jsp(149,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f3.setValue("/journal/edit_article");
    int _jspx_eval_portlet_005fparam_005f3 = _jspx_th_portlet_005fparam_005f3.doStartTag();
    if (_jspx_th_portlet_005fparam_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
    // /html/portlet/journal/article_search.jsp(163,2) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("filter-by-structure");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f1 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
    // /html/portlet/journal/article_search.jsp(171,2) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f1.setKey("filter-by-template");
    int _jspx_eval_liferay_002dui_005fmessage_005f1 = _jspx_th_liferay_002dui_005fmessage_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
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

  private boolean _jspx_meth_portlet_005fparam_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dportlet_005frenderURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f6 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f6.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005frenderURL_005f0);
    // /html/portlet/journal/article_search.jsp(177,135) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f6.setName("struts_action");
    // /html/portlet/journal/article_search.jsp(177,135) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f6.setValue("/journal/edit_article");
    int _jspx_eval_portlet_005fparam_005f6 = _jspx_th_portlet_005fparam_005f6.doStartTag();
    if (_jspx_th_portlet_005fparam_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
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

  private boolean _jspx_meth_portlet_005fnamespace_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f6 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f6.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
    int _jspx_eval_portlet_005fnamespace_005f6 = _jspx_th_portlet_005fnamespace_005f6.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f7 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f7.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
    int _jspx_eval_portlet_005fnamespace_005f7 = _jspx_th_portlet_005fnamespace_005f7.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f7);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f8 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f8.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
    int _jspx_eval_portlet_005fnamespace_005f8 = _jspx_th_portlet_005fnamespace_005f8.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f8);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f9 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f9.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
    int _jspx_eval_portlet_005fnamespace_005f9 = _jspx_th_portlet_005fnamespace_005f9.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f9);
    return false;
  }
}
