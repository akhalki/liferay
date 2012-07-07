package org.apache.jsp.html.portlet.image_005fgallery;

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
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetEntryServiceUtil;
import com.liferay.portlet.asset.service.AssetTagServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;
import com.liferay.portlet.asset.util.AssetUtil;
import com.liferay.portlet.imagegallery.DuplicateFolderNameException;
import com.liferay.portlet.imagegallery.DuplicateImageNameException;
import com.liferay.portlet.imagegallery.FolderNameException;
import com.liferay.portlet.imagegallery.ImageNameException;
import com.liferay.portlet.imagegallery.ImageSizeException;
import com.liferay.portlet.imagegallery.NoSuchFolderException;
import com.liferay.portlet.imagegallery.NoSuchImageException;
import com.liferay.portlet.imagegallery.model.IGFolder;
import com.liferay.portlet.imagegallery.model.IGFolderConstants;
import com.liferay.portlet.imagegallery.model.IGImage;
import com.liferay.portlet.imagegallery.service.IGFolderLocalServiceUtil;
import com.liferay.portlet.imagegallery.service.IGFolderServiceUtil;
import com.liferay.portlet.imagegallery.service.IGImageLocalServiceUtil;
import com.liferay.portlet.imagegallery.service.IGImageServiceUtil;
import com.liferay.portlet.imagegallery.service.permission.IGFolderPermission;
import com.liferay.portlet.imagegallery.service.permission.IGImagePermission;
import com.liferay.portlet.imagegallery.service.permission.IGPermission;
import com.liferay.portlet.imagegallery.util.IGUtil;
import com.liferay.portlet.imagegallery.webdav.IGWebDAVStorageImpl;

public final class view_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(29);
    _jspx_dependants.add("/html/portlet/image_gallery/init.jsp");
    _jspx_dependants.add("/html/portlet/init.jsp");
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/image_gallery/view_images.jspf");
    _jspx_dependants.add("/html/portlet/image_gallery/image_action.jspf");
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
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fratings_002dscore_0026_005fscore_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dcategories_002dsummary_0026_005fclassPK_005fclassName_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002dsummary_0026_005fclassPK_005fclassName_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005flayout;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fbackURL_005fbackLabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fcolumn_0026_005ffirst_005fcssClass_005fcolumnWidth;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattributes_002davailable_0026_005fclassName;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fcolumn_0026_005flast_005fcssClass_005fcolumnWidth;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fmessage_005fimage_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fratings_002dscore_0026_005fscore_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dcategories_002dsummary_0026_005fclassPK_005fclassName_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002dsummary_0026_005fclassPK_005fclassName_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005flayout = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fbackURL_005fbackLabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fcolumn_0026_005ffirst_005fcssClass_005fcolumnWidth = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattributes_002davailable_0026_005fclassName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fcolumn_0026_005flast_005fcssClass_005fcolumnWidth = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fmessage_005fimage_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fc_005fotherwise.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fratings_002dscore_0026_005fscore_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.release();
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dcategories_002dsummary_0026_005fclassPK_005fclassName_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002dsummary_0026_005fclassPK_005fclassName_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.release();
    _005fjspx_005ftagPool_005faui_005flayout.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fbackURL_005fbackLabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fcolumn_0026_005ffirst_005fcssClass_005fcolumnWidth.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattributes_002davailable_0026_005fclassName.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.release();
    _005fjspx_005ftagPool_005faui_005fcolumn_0026_005flast_005fcssClass_005fcolumnWidth.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fmessage_005fimage_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fnobody.release();
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

PortletPreferences preferences = renderRequest.getPreferences();

String portletResource = ParamUtil.getString(request, "portletResource");

if (Validator.isNotNull(portletResource)) {
	preferences = PortletPreferencesFactoryUtil.getPortletSetup(request, portletResource);
}
else if (layout.isTypeControlPanel()) {
	preferences = PortletPreferencesLocalServiceUtil.getPreferences(themeDisplay.getCompanyId(), scopeGroupId, PortletKeys.PREFS_OWNER_TYPE_GROUP, 0, PortletKeys.IMAGE_GALLERY, null);
}

long rootFolderId = PrefsParamUtil.getLong(preferences, request, "rootFolderId", IGFolderConstants.DEFAULT_PARENT_FOLDER_ID);

Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);

      out.write('\n');
      out.write('\n');

String topLink = ParamUtil.getString(request, "topLink", "images-home");

IGFolder folder = (IGFolder)request.getAttribute(WebKeys.IMAGE_GALLERY_FOLDER);

long defaultFolderId = GetterUtil.getLong(preferences.getValue("rootFolderId", StringPool.BLANK), IGFolderConstants.DEFAULT_PARENT_FOLDER_ID);

long folderId = BeanParamUtil.getLong(folder, request, "folderId", defaultFolderId);

if ((folder == null) && (defaultFolderId != IGFolderConstants.DEFAULT_PARENT_FOLDER_ID)) {
	try {
		folder = IGFolderLocalServiceUtil.getFolder(folderId);
	}
	catch (NoSuchFolderException nsfe) {
		folderId = IGFolderConstants.DEFAULT_PARENT_FOLDER_ID;
	}
}

int foldersCount = IGFolderServiceUtil.getFoldersCount(scopeGroupId, folderId);
int imagesCount = IGImageServiceUtil.getImagesCount(scopeGroupId, folderId);

long categoryId = ParamUtil.getLong(request, "categoryId");
String tagName = ParamUtil.getString(request, "tag");

String categoryName = null;
String vocabularyName = null;

if (categoryId != 0) {
	AssetCategory assetCategory = AssetCategoryLocalServiceUtil.getAssetCategory(categoryId);

	categoryName = assetCategory.getName();

	AssetVocabulary assetVocabulary = AssetVocabularyLocalServiceUtil.getAssetVocabulary(assetCategory.getVocabularyId());

	vocabularyName = assetVocabulary.getName();
}

boolean useAssetEntryQuery = (categoryId > 0) || Validator.isNotNull(tagName);

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/image_gallery/view");
portletURL.setParameter("topLink", topLink);
portletURL.setParameter("folderId", String.valueOf(folderId));

request.setAttribute("view.jsp-folder", folder);

request.setAttribute("view.jsp-folderId", String.valueOf(folderId));

request.setAttribute("view.jsp-portletURL", portletURL);

request.setAttribute("view.jsp-viewFolder", Boolean.TRUE.toString());

request.setAttribute("view.jsp-useAssetEntryQuery", String.valueOf(useAssetEntryQuery));

      out.write('\n');
      out.write('\n');
      if (_jspx_meth_liferay_002dutil_005finclude_005f0(_jspx_page_context))
        return;
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
          // /html/portlet/image_gallery/view.jsp(78,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fwhen_005f0.setTest( useAssetEntryQuery );
          int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
          if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              // /html/portlet/image_gallery/view.jsp(79,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f0.setTest( Validator.isNotNull(categoryName) );
              int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
              if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t<h1 class=\"entry-title\">\n");
                  out.write("\t\t\t\t");
                  out.print( LanguageUtil.format(pageContext, "images-with-x-x", new String[] {vocabularyName, categoryName}) );
                  out.write("\n");
                  out.write("\t\t\t</h1>\n");
                  out.write("\t\t");
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
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              // /html/portlet/image_gallery/view.jsp(85,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f1.setTest( Validator.isNotNull(tagName) );
              int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
              if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t<h1 class=\"entry-title\">\n");
                  out.write("\t\t\t\t");
                  out.print( HtmlUtil.escape(LanguageUtil.format(pageContext, "images-with-tag-x", tagName)) );
                  out.write("\n");
                  out.write("\t\t\t</h1>\n");
                  out.write("\t\t");
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

		SearchContainer searchContainer = new SearchContainer(renderRequest, null, null, "cur2", SearchContainer.DEFAULT_DELTA, portletURL, null, null);

		AssetEntryQuery assetEntryQuery = new AssetEntryQuery(IGImage.class.getName(), searchContainer);

		assetEntryQuery.setExcludeZeroViewCount(false);

		int total = AssetEntryServiceUtil.getEntriesCount(assetEntryQuery);

		searchContainer.setTotal(total);

		List results = AssetEntryServiceUtil.getEntries(assetEntryQuery);

		searchContainer.setResults(results);

		List scores = null;
		
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

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
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
              if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write('\n');
                  out.write('	');
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f1 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                  // /html/portlet/image_gallery/view_images.jspf(18,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f1.setTest( results.isEmpty() );
                  int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t<div class=\"portlet-msg-info\">\n");
                      out.write("\t\t\t");
                      out.print( LanguageUtil.get(pageContext, "there-are-no-images-in-this-folder") );
                      out.write("\n");
                      out.write("\t\t</div>\n");
                      out.write("\t");
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
                  org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                  _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                  int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
                  if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t<div class=\"taglib-search-iterator-page-iterator-top\">\n");
                      out.write("\t\t\t");
                      //  liferay-ui:search-paginator
                      com.liferay.taglib.ui.SearchPaginatorTag _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0 = (com.liferay.taglib.ui.SearchPaginatorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.get(com.liferay.taglib.ui.SearchPaginatorTag.class);
                      _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
                      // /html/portlet/image_gallery/view_images.jspf(25,3) name = searchContainer type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0.setSearchContainer( searchContainer );
                      int _jspx_eval_liferay_002dui_005fsearch_002dpaginator_005f0 = _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0.doStartTag();
                      if (_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0);
                      out.write("\n");
                      out.write("\t\t</div>\n");
                      out.write("\t");
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
              out.write("<div>\n");
              out.write("\n");
              out.write("\t");

	for (int i = 0; i < results.size(); i++) {
		IGImage image = null;

		if (useAssetEntryQuery) {
			AssetEntry assetEntry = (AssetEntry)results.get(i);

			image = IGImageLocalServiceUtil.getIGImage(assetEntry.getClassPK());
		}
		else {
			image = (IGImage)results.get(i);
		}
	
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f2 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f2.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              int _jspx_eval_c_005fchoose_005f2 = _jspx_th_c_005fchoose_005f2.doStartTag();
              if (_jspx_eval_c_005fchoose_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f2 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
                  // /html/portlet/image_gallery/view_images.jspf(47,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f2.setTest( IGImagePermission.contains(permissionChecker, image, ActionKeys.VIEW) );
                  int _jspx_eval_c_005fwhen_005f2 = _jspx_th_c_005fwhen_005f2.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");

				Image largeImage = ImageLocalServiceUtil.getImage(image.getLargeImageId());
				Image smallImage = ImageLocalServiceUtil.getImage(image.getSmallImageId());

				long smallImageId = 0;
				int smallImageHeight = 100;
				int smallImageWidth = 100;

				if (smallImage != null) {
					smallImageId = smallImage.getImageId();
					smallImageHeight = smallImage.getHeight();
					smallImageWidth = smallImage.getWidth();
				}

				int topMargin = PrefsPropsUtil.getInteger(PropsKeys.IG_IMAGE_THUMBNAIL_MAX_DIMENSION) - smallImageHeight;
				int sideMargin = (PrefsPropsUtil.getInteger(PropsKeys.IG_IMAGE_THUMBNAIL_MAX_DIMENSION) - smallImageWidth) / 2;
				
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t<a class=\"image-thumbnail\" href=\"");
                      out.print( themeDisplay.getPathImage() );
                      out.write("/image_gallery?img_id=");
                      out.print( largeImage.getImageId() );
                      out.write('&');
                      out.write('t');
                      out.write('=');
                      out.print( ImageServletTokenUtil.getToken(largeImage.getImageId()) );
                      out.write("\" largeImageId=\"");
                      out.print( largeImage.getImageId() );
                      out.write("\" title=\"");
                      out.print( HtmlUtil.escape(image.getName()) + " - " + HtmlUtil.escape(image.getDescription()) );
                      out.write("\">\n");
                      out.write("\t\t\t\t\t<img alt=\"");
                      out.print( HtmlUtil.escape(image.getName()) + " - " + HtmlUtil.escape(image.getDescription()) );
                      out.write("\" border=\"no\" src=\"");
                      out.print( themeDisplay.getPathImage() );
                      out.write("/image_gallery?img_id=");
                      out.print( smallImageId );
                      out.write("&igImageId=");
                      out.print( image.getImageId() );
                      out.write("&igSmallImage=1&t=");
                      out.print( ImageServletTokenUtil.getToken(smallImageId) );
                      out.write("\" style=\"height: ");
                      out.print( smallImageHeight );
                      out.write("; margin: ");
                      out.print( topMargin );
                      out.write('p');
                      out.write('x');
                      out.write(' ');
                      out.print( sideMargin );
                      out.write("px 0px ");
                      out.print( sideMargin );
                      out.write("px; width: ");
                      out.print( smallImageWidth );
                      out.write(";\" />\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t<span class=\"image-title\">");
                      out.print( HtmlUtil.escape(image.getName()) );
                      out.write("</span>\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t");
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                      // /html/portlet/image_gallery/view_images.jspf(72,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f2.setTest( scores != null );
                      int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
                      if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t<span class=\"image-score\">\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");

							double score = ((Double)scores.get(i)).doubleValue();

							score = MathUtils.round((score * 10) / 2, 1, BigDecimal.ROUND_HALF_UP);
							
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t");
                          //  liferay-ui:ratings-score
                          com.liferay.taglib.ui.RatingsScoreTag _jspx_th_liferay_002dui_005fratings_002dscore_005f0 = (com.liferay.taglib.ui.RatingsScoreTag) _005fjspx_005ftagPool_005fliferay_002dui_005fratings_002dscore_0026_005fscore_005fnobody.get(com.liferay.taglib.ui.RatingsScoreTag.class);
                          _jspx_th_liferay_002dui_005fratings_002dscore_005f0.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dui_005fratings_002dscore_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
                          // /html/portlet/image_gallery/view_images.jspf(81,7) name = score type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fratings_002dscore_005f0.setScore( score );
                          int _jspx_eval_liferay_002dui_005fratings_002dscore_005f0 = _jspx_th_liferay_002dui_005fratings_002dscore_005f0.doStartTag();
                          if (_jspx_th_liferay_002dui_005fratings_002dscore_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fliferay_002dui_005fratings_002dscore_0026_005fscore_005fnobody.reuse(_jspx_th_liferay_002dui_005fratings_002dscore_005f0);
                            return;
                          }
                          _005fjspx_005ftagPool_005fliferay_002dui_005fratings_002dscore_0026_005fscore_005fnobody.reuse(_jspx_th_liferay_002dui_005fratings_002dscore_005f0);
                          out.write("\n");
                          out.write("\t\t\t\t\t\t</span>\n");
                          out.write("\t\t\t\t\t");
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
                      out.write("\t\t\t\t</a>\n");
                      out.write("\n");
                      out.write("\t\t\t\t<div class=\"aui-helper-hidden\" id=\"");
                      if (_jspx_meth_portlet_005fnamespace_005f0(_jspx_th_c_005fwhen_005f2, _jspx_page_context))
                        return;
                      out.write("buttonsContainer_");
                      out.print( largeImage.getImageId() );
                      out.write("\">\n");
                      out.write("\t\t\t\t\t<div class=\"buttons-container float-container\" id=\"");
                      if (_jspx_meth_portlet_005fnamespace_005f1(_jspx_th_c_005fwhen_005f2, _jspx_page_context))
                        return;
                      out.write("buttons_");
                      out.print( largeImage.getImageId() );
                      out.write("\">\n");
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

boolean view = GetterUtil.getBoolean((String)request.getAttribute("view_image.jsp-view"));

                      out.write('\n');
                      out.write('\n');
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                      // /html/portlet/image_gallery/image_action.jspf(21,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f3.setTest( IGImagePermission.contains(permissionChecker, image, ActionKeys.VIEW) );
                      int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
                      if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write('\n');
                          out.write('\n');
                          out.write('	');

	String downloadURL = themeDisplay.getPathImage() + "/image_gallery?img_id=" + image.getLargeImageId() + "&fileName=" + HttpUtil.encodeURL(image.getNameWithExtension()) + "&t=" + ImageServletTokenUtil.getToken(image.getLargeImageId());
	
                          out.write('\n');
                          out.write('\n');
                          out.write('	');
                          //  liferay-ui:icon
                          com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f0 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                          _jspx_th_liferay_002dui_005ficon_005f0.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dui_005ficon_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
                          // /html/portlet/image_gallery/image_action.jspf(27,1) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005ficon_005f0.setImage("download");
                          // /html/portlet/image_gallery/image_action.jspf(27,1) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005ficon_005f0.setMessage( LanguageUtil.get(pageContext, "download") + " (" + TextFormatter.formatKB(image.getImageSize(), locale) + "k)" );
                          // /html/portlet/image_gallery/image_action.jspf(27,1) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005ficon_005f0.setUrl( downloadURL );
                          int _jspx_eval_liferay_002dui_005ficon_005f0 = _jspx_th_liferay_002dui_005ficon_005f0.doStartTag();
                          if (_jspx_th_liferay_002dui_005ficon_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
                            return;
                          }
                          _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
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
                      //  c:if
                      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                      _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                      // /html/portlet/image_gallery/image_action.jspf(34,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f4.setTest( !view && IGImagePermission.contains(permissionChecker, image, ActionKeys.VIEW) );
                      int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
                      if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write('\n');
                          out.write('	');
                          //  portlet:renderURL
                          com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f0 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                          _jspx_th_portlet_005frenderURL_005f0.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005frenderURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
                          // /html/portlet/image_gallery/image_action.jspf(35,1) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005frenderURL_005f0.setVar("viewURL");
                          int _jspx_eval_portlet_005frenderURL_005f0 = _jspx_th_portlet_005frenderURL_005f0.doStartTag();
                          if (_jspx_eval_portlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_portlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_portlet_005frenderURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_portlet_005frenderURL_005f0.doInitBody();
                            }
                            do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              if (_jspx_meth_portlet_005fparam_005f0(_jspx_th_portlet_005frenderURL_005f0, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f1 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
                              // /html/portlet/image_gallery/image_action.jspf(37,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f1.setName("redirect");
                              // /html/portlet/image_gallery/image_action.jspf(37,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f1.setValue( currentURL );
                              int _jspx_eval_portlet_005fparam_005f1 = _jspx_th_portlet_005fparam_005f1.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f2 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
                              // /html/portlet/image_gallery/image_action.jspf(38,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f2.setName("imageId");
                              // /html/portlet/image_gallery/image_action.jspf(38,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f2.setValue( String.valueOf(image.getImageId()) );
                              int _jspx_eval_portlet_005fparam_005f2 = _jspx_th_portlet_005fparam_005f2.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                              out.write('\n');
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
                          java.lang.String viewURL = null;
                          viewURL = (java.lang.String) _jspx_page_context.findAttribute("viewURL");
                          out.write('\n');
                          out.write('\n');
                          out.write('	');
                          //  liferay-ui:icon
                          com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f1 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                          _jspx_th_liferay_002dui_005ficon_005f1.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dui_005ficon_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
                          // /html/portlet/image_gallery/image_action.jspf(41,1) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005ficon_005f1.setImage("view");
                          // /html/portlet/image_gallery/image_action.jspf(41,1) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005ficon_005f1.setUrl( viewURL );
                          int _jspx_eval_liferay_002dui_005ficon_005f1 = _jspx_th_liferay_002dui_005ficon_005f1.doStartTag();
                          if (_jspx_th_liferay_002dui_005ficon_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
                            return;
                          }
                          _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
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
                      _jspx_th_c_005fif_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                      // /html/portlet/image_gallery/image_action.jspf(47,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f5.setTest( IGImagePermission.contains(permissionChecker, image, ActionKeys.UPDATE) );
                      int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
                      if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write('\n');
                          out.write('	');
                          //  portlet:renderURL
                          com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f1 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                          _jspx_th_portlet_005frenderURL_005f1.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005frenderURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
                          // /html/portlet/image_gallery/image_action.jspf(48,1) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005frenderURL_005f1.setVar("editURL");
                          int _jspx_eval_portlet_005frenderURL_005f1 = _jspx_th_portlet_005frenderURL_005f1.doStartTag();
                          if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_portlet_005frenderURL_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_portlet_005frenderURL_005f1.doInitBody();
                            }
                            do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              if (_jspx_meth_portlet_005fparam_005f3(_jspx_th_portlet_005frenderURL_005f1, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f4 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
                              // /html/portlet/image_gallery/image_action.jspf(50,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f4.setName("redirect");
                              // /html/portlet/image_gallery/image_action.jspf(50,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f4.setValue( currentURL );
                              int _jspx_eval_portlet_005fparam_005f4 = _jspx_th_portlet_005fparam_005f4.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f5 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f5.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
                              // /html/portlet/image_gallery/image_action.jspf(51,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f5.setName("imageId");
                              // /html/portlet/image_gallery/image_action.jspf(51,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f5.setValue( String.valueOf(image.getImageId()) );
                              int _jspx_eval_portlet_005fparam_005f5 = _jspx_th_portlet_005fparam_005f5.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
                              out.write('\n');
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
                          java.lang.String editURL = null;
                          editURL = (java.lang.String) _jspx_page_context.findAttribute("editURL");
                          out.write('\n');
                          out.write('\n');
                          out.write('	');
                          //  liferay-ui:icon
                          com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f2 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                          _jspx_th_liferay_002dui_005ficon_005f2.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dui_005ficon_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
                          // /html/portlet/image_gallery/image_action.jspf(54,1) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005ficon_005f2.setImage("edit");
                          // /html/portlet/image_gallery/image_action.jspf(54,1) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005ficon_005f2.setUrl( editURL );
                          int _jspx_eval_liferay_002dui_005ficon_005f2 = _jspx_th_liferay_002dui_005ficon_005f2.doStartTag();
                          if (_jspx_th_liferay_002dui_005ficon_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f2);
                            return;
                          }
                          _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f2);
                          out.write('\n');
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
                      _jspx_th_c_005fif_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                      // /html/portlet/image_gallery/image_action.jspf(60,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f6.setTest( IGImagePermission.contains(permissionChecker, image, ActionKeys.PERMISSIONS) );
                      int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
                      if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write('\n');
                          out.write('	');
                          //  liferay-security:permissionsURL
                          com.liferay.taglib.security.PermissionsURLTag _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0 = (com.liferay.taglib.security.PermissionsURLTag) _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.get(com.liferay.taglib.security.PermissionsURLTag.class);
                          _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
                          // /html/portlet/image_gallery/image_action.jspf(61,1) name = modelResource type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setModelResource( IGImage.class.getName() );
                          // /html/portlet/image_gallery/image_action.jspf(61,1) name = modelResourceDescription type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setModelResourceDescription( String.valueOf(image.getName()) );
                          // /html/portlet/image_gallery/image_action.jspf(61,1) name = resourcePrimKey type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setResourcePrimKey( String.valueOf(image.getImageId()) );
                          // /html/portlet/image_gallery/image_action.jspf(61,1) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setVar("permissionsURL");
                          int _jspx_eval_liferay_002dsecurity_005fpermissionsURL_005f0 = _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.doStartTag();
                          if (_jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.reuse(_jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0);
                            return;
                          }
                          _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.reuse(_jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0);
                          java.lang.String permissionsURL = null;
                          permissionsURL = (java.lang.String) _jspx_page_context.findAttribute("permissionsURL");
                          out.write('\n');
                          out.write('\n');
                          out.write('	');
                          //  liferay-ui:icon
                          com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f3 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                          _jspx_th_liferay_002dui_005ficon_005f3.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dui_005ficon_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
                          // /html/portlet/image_gallery/image_action.jspf(68,1) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005ficon_005f3.setImage("permissions");
                          // /html/portlet/image_gallery/image_action.jspf(68,1) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005ficon_005f3.setUrl( permissionsURL );
                          int _jspx_eval_liferay_002dui_005ficon_005f3 = _jspx_th_liferay_002dui_005ficon_005f3.doStartTag();
                          if (_jspx_th_liferay_002dui_005ficon_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f3);
                            return;
                          }
                          _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f3);
                          out.write('\n');
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
                      _jspx_th_c_005fif_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                      // /html/portlet/image_gallery/image_action.jspf(74,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fif_005f7.setTest( IGImagePermission.contains(permissionChecker, image, ActionKeys.DELETE) );
                      int _jspx_eval_c_005fif_005f7 = _jspx_th_c_005fif_005f7.doStartTag();
                      if (_jspx_eval_c_005fif_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write('\n');
                          out.write('	');
                          //  portlet:actionURL
                          com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f0 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.get(com.liferay.taglib.portlet.ActionURLTag.class);
                          _jspx_th_portlet_005factionURL_005f0.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005factionURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
                          // /html/portlet/image_gallery/image_action.jspf(75,1) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005factionURL_005f0.setVar("deleteURL");
                          int _jspx_eval_portlet_005factionURL_005f0 = _jspx_th_portlet_005factionURL_005f0.doStartTag();
                          if (_jspx_eval_portlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_portlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_portlet_005factionURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_portlet_005factionURL_005f0.doInitBody();
                            }
                            do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              if (_jspx_meth_portlet_005fparam_005f6(_jspx_th_portlet_005factionURL_005f0, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f7 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f7.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
                              // /html/portlet/image_gallery/image_action.jspf(77,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f7.setName( Constants.CMD );
                              // /html/portlet/image_gallery/image_action.jspf(77,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f7.setValue( Constants.DELETE );
                              int _jspx_eval_portlet_005fparam_005f7 = _jspx_th_portlet_005fparam_005f7.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f8 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f8.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
                              // /html/portlet/image_gallery/image_action.jspf(78,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f8.setName("redirect");
                              // /html/portlet/image_gallery/image_action.jspf(78,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f8.setValue( currentURL );
                              int _jspx_eval_portlet_005fparam_005f8 = _jspx_th_portlet_005fparam_005f8.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f9 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f9.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
                              // /html/portlet/image_gallery/image_action.jspf(79,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f9.setName("imageId");
                              // /html/portlet/image_gallery/image_action.jspf(79,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f9.setValue( String.valueOf(image.getImageId()) );
                              int _jspx_eval_portlet_005fparam_005f9 = _jspx_th_portlet_005fparam_005f9.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
                              out.write('\n');
                              out.write('	');
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
                          java.lang.String deleteURL = null;
                          deleteURL = (java.lang.String) _jspx_page_context.findAttribute("deleteURL");
                          out.write('\n');
                          out.write('\n');
                          out.write('	');
                          //  liferay-ui:icon-delete
                          com.liferay.taglib.ui.IconDeleteTag _jspx_th_liferay_002dui_005ficon_002ddelete_005f0 = (com.liferay.taglib.ui.IconDeleteTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody.get(com.liferay.taglib.ui.IconDeleteTag.class);
                          _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
                          // /html/portlet/image_gallery/image_action.jspf(82,1) name = url type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.setUrl( deleteURL );
                          int _jspx_eval_liferay_002dui_005ficon_002ddelete_005f0 = _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.doStartTag();
                          if (_jspx_th_liferay_002dui_005ficon_002ddelete_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002ddelete_005f0);
                            return;
                          }
                          _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002ddelete_005f0);
                          out.write('\n');
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
                      out.write("\t\t\t\t\t</div>\n");
                      out.write("\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t");

				List assetTags = AssetTagServiceUtil.getTags(IGImage.class.getName(), image.getImageId());
				
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t<div id=\"");
                      if (_jspx_meth_portlet_005fnamespace_005f2(_jspx_th_c_005fwhen_005f2, _jspx_page_context))
                        return;
                      out.write("categorizacionContainer_");
                      out.print( largeImage.getImageId() );
                      out.write("\" style=\"display: none;\">\n");
                      out.write("\t\t\t\t\t<span ");
                      out.print( !assetTags.isEmpty() ? "class=\"has-tags\"" : "" );
                      out.write(">\n");
                      out.write("\t\t\t\t\t\t");
                      //  liferay-ui:asset-categories-summary
                      com.liferay.taglib.ui.AssetCategoriesSummaryTag _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f0 = (com.liferay.taglib.ui.AssetCategoriesSummaryTag) _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dcategories_002dsummary_0026_005fclassPK_005fclassName_005fnobody.get(com.liferay.taglib.ui.AssetCategoriesSummaryTag.class);
                      _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                      // /html/portlet/image_gallery/view_images.jspf(98,6) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f0.setClassName( IGImage.class.getName() );
                      // /html/portlet/image_gallery/view_images.jspf(98,6) name = classPK type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f0.setClassPK( image.getImageId() );
                      int _jspx_eval_liferay_002dui_005fasset_002dcategories_002dsummary_005f0 = _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f0.doStartTag();
                      if (_jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dcategories_002dsummary_0026_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dcategories_002dsummary_0026_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f0);
                      out.write("\n");
                      out.write("\t\t\t\t\t</span>\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t");
                      //  liferay-ui:asset-tags-summary
                      com.liferay.taglib.ui.AssetTagsSummaryTag _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f0 = (com.liferay.taglib.ui.AssetTagsSummaryTag) _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002dsummary_0026_005fclassPK_005fclassName_005fnobody.get(com.liferay.taglib.ui.AssetTagsSummaryTag.class);
                      _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                      // /html/portlet/image_gallery/view_images.jspf(104,5) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f0.setClassName( IGImage.class.getName() );
                      // /html/portlet/image_gallery/view_images.jspf(104,5) name = classPK type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f0.setClassPK( image.getImageId() );
                      int _jspx_eval_liferay_002dui_005fasset_002dtags_002dsummary_005f0 = _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f0.doStartTag();
                      if (_jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002dsummary_0026_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002dsummary_0026_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f0);
                      out.write("\n");
                      out.write("\t\t\t\t</div>\n");
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
                      out.write("\t\t\t\t<div style=\"float: left; margin: 100px 10px 0px;\">\n");
                      out.write("\t\t\t\t\t<img alt=\"");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_th_c_005fotherwise_005f1, _jspx_page_context))
                        return;
                      out.write("\" border=\"no\" src=\"");
                      out.print( themeDisplay.getPathThemeImages() );
                      out.write("/application/forbidden_action.png\" />\n");
                      out.write("\t\t\t\t</div>\n");
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
              out.write('\n');
              out.write('	');

	}
	
              out.write("\n");
              out.write("\n");
              out.write("</div>\n");
              out.write("\n");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f8 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f8.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              // /html/portlet/image_gallery/view_images.jspf(123,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f8.setTest( !results.isEmpty() );
              int _jspx_eval_c_005fif_005f8 = _jspx_th_c_005fif_005f8.doStartTag();
              if (_jspx_eval_c_005fif_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t<div class=\"taglib-search-iterator-page-iterator-bottom\">\n");
                  out.write("\t\t");
                  //  liferay-ui:search-paginator
                  com.liferay.taglib.ui.SearchPaginatorTag _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f1 = (com.liferay.taglib.ui.SearchPaginatorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.get(com.liferay.taglib.ui.SearchPaginatorTag.class);
                  _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
                  // /html/portlet/image_gallery/view_images.jspf(125,2) name = searchContainer type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f1.setSearchContainer( searchContainer );
                  int _jspx_eval_liferay_002dui_005fsearch_002dpaginator_005f1 = _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f1.doStartTag();
                  if (_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f1);
                  out.write("\n");
                  out.write("\t</div>\n");
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
              out.write('\n');
              out.write('\n');
              if (_jspx_meth_aui_005fscript_005f0(_jspx_th_c_005fwhen_005f0, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

		if (portletName.equals(PortletKeys.IMAGE_GALLERY)) {
			PortalUtil.addPageKeywords(tagName, request);
			PortalUtil.addPageKeywords(categoryName, request);
		}
		
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
          //  c:when
          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f3 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
          _jspx_th_c_005fwhen_005f3.setPageContext(_jspx_page_context);
          _jspx_th_c_005fwhen_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
          // /html/portlet/image_gallery/view.jsp(119,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fwhen_005f3.setTest( topLink.equals("images-home") );
          int _jspx_eval_c_005fwhen_005f3 = _jspx_th_c_005fwhen_005f3.doStartTag();
          if (_jspx_eval_c_005fwhen_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  aui:layout
              com.liferay.taglib.aui.LayoutTag _jspx_th_aui_005flayout_005f0 = (com.liferay.taglib.aui.LayoutTag) _005fjspx_005ftagPool_005faui_005flayout.get(com.liferay.taglib.aui.LayoutTag.class);
              _jspx_th_aui_005flayout_005f0.setPageContext(_jspx_page_context);
              _jspx_th_aui_005flayout_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
              int _jspx_eval_aui_005flayout_005f0 = _jspx_th_aui_005flayout_005f0.doStartTag();
              if (_jspx_eval_aui_005flayout_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_aui_005flayout_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_aui_005flayout_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_aui_005flayout_005f0.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f9 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f9.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005flayout_005f0);
                  // /html/portlet/image_gallery/view.jsp(121,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f9.setTest( folder != null );
                  int _jspx_eval_c_005fif_005f9 = _jspx_th_c_005fif_005f9.doStartTag();
                  if (_jspx_eval_c_005fif_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");

				long parentFolderId = defaultFolderId;
				String parentFolderName = LanguageUtil.get(pageContext, "images-home");

				if (!folder.isRoot()) {
					IGFolder parentFolder = folder.getParentFolder();

					parentFolderId = parentFolder.getFolderId();
					parentFolderName = parentFolder.getName();
				}
				
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  portlet:renderURL
                      com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f2 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                      _jspx_th_portlet_005frenderURL_005f2.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005frenderURL_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f9);
                      // /html/portlet/image_gallery/view.jsp(135,4) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005frenderURL_005f2.setVar("backURL");
                      int _jspx_eval_portlet_005frenderURL_005f2 = _jspx_th_portlet_005frenderURL_005f2.doStartTag();
                      if (_jspx_eval_portlet_005frenderURL_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_portlet_005frenderURL_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_portlet_005frenderURL_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_portlet_005frenderURL_005f2.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          if (_jspx_meth_portlet_005fparam_005f10(_jspx_th_portlet_005frenderURL_005f2, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f11 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f11.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f2);
                          // /html/portlet/image_gallery/view.jsp(137,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f11.setName("folderId");
                          // /html/portlet/image_gallery/view.jsp(137,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f11.setValue( String.valueOf(parentFolderId) );
                          int _jspx_eval_portlet_005fparam_005f11 = _jspx_th_portlet_005fparam_005f11.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f11);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f11);
                          out.write("\n");
                          out.write("\t\t\t\t");
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
                      java.lang.String backURL = null;
                      backURL = (java.lang.String) _jspx_page_context.findAttribute("backURL");
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  liferay-ui:header
                      com.liferay.taglib.ui.HeaderTag _jspx_th_liferay_002dui_005fheader_005f0 = (com.liferay.taglib.ui.HeaderTag) _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fbackURL_005fbackLabel_005fnobody.get(com.liferay.taglib.ui.HeaderTag.class);
                      _jspx_th_liferay_002dui_005fheader_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fheader_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f9);
                      // /html/portlet/image_gallery/view.jsp(140,4) name = backLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fheader_005f0.setBackLabel( "&laquo; " + LanguageUtil.format(pageContext, "back-to-x", HtmlUtil.escape(parentFolderName)) );
                      // /html/portlet/image_gallery/view.jsp(140,4) name = backURL type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fheader_005f0.setBackURL( backURL.toString() );
                      // /html/portlet/image_gallery/view.jsp(140,4) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fheader_005f0.setTitle( folder.getName() );
                      int _jspx_eval_liferay_002dui_005fheader_005f0 = _jspx_th_liferay_002dui_005fheader_005f0.doStartTag();
                      if (_jspx_th_liferay_002dui_005fheader_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fbackURL_005fbackLabel_005fnobody.reuse(_jspx_th_liferay_002dui_005fheader_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fbackURL_005fbackLabel_005fnobody.reuse(_jspx_th_liferay_002dui_005fheader_005f0);
                      out.write("\n");
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
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:column
                  com.liferay.taglib.aui.ColumnTag _jspx_th_aui_005fcolumn_005f0 = (com.liferay.taglib.aui.ColumnTag) _005fjspx_005ftagPool_005faui_005fcolumn_0026_005ffirst_005fcssClass_005fcolumnWidth.get(com.liferay.taglib.aui.ColumnTag.class);
                  _jspx_th_aui_005fcolumn_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fcolumn_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005flayout_005f0);
                  // /html/portlet/image_gallery/view.jsp(147,3) name = columnWidth type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fcolumn_005f0.setColumnWidth( 75 );
                  // /html/portlet/image_gallery/view.jsp(147,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fcolumn_005f0.setCssClass("lfr-asset-column lfr-asset-column-details");
                  // /html/portlet/image_gallery/view.jsp(147,3) name = first type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fcolumn_005f0.setFirst( true );
                  int _jspx_eval_aui_005fcolumn_005f0 = _jspx_th_aui_005fcolumn_005f0.doStartTag();
                  if (_jspx_eval_aui_005fcolumn_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005fcolumn_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005fcolumn_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005fcolumn_005f0.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  liferay-ui:panel-container
                      com.liferay.taglib.ui.PanelContainerTag _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0 = (com.liferay.taglib.ui.PanelContainerTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended.get(com.liferay.taglib.ui.PanelContainerTag.class);
                      _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fcolumn_005f0);
                      // /html/portlet/image_gallery/view.jsp(148,4) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setExtended( false );
                      // /html/portlet/image_gallery/view.jsp(148,4) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setId("imageGalleryPanelContainer");
                      // /html/portlet/image_gallery/view.jsp(148,4) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
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
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f10 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f10.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
                          // /html/portlet/image_gallery/view.jsp(149,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f10.setTest( folder != null );
                          int _jspx_eval_c_005fif_005f10 = _jspx_th_c_005fif_005f10.doStartTag();
                          if (_jspx_eval_c_005fif_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t<div class=\"lfr-asset-description\">\n");
                              out.write("\t\t\t\t\t\t\t");
                              out.print( HtmlUtil.escape(folder.getDescription()) );
                              out.write("\n");
                              out.write("\t\t\t\t\t\t</div>\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t<div class=\"lfr-asset-metadata\">\n");
                              out.write("\t\t\t\t\t\t\t<div class=\"lfr-asset-icon lfr-asset-date\">\n");
                              out.write("\t\t\t\t\t\t\t\t");
                              out.print( LanguageUtil.format(pageContext, "last-updated-x", dateFormatDate.format(folder.getModifiedDate())) );
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t</div>\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t<div class=\"lfr-asset-icon lfr-asset-subfolders\">\n");
                              out.write("\t\t\t\t\t\t\t\t");
                              out.print( foldersCount );
                              out.write(' ');
                              //  liferay-ui:message
                              com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f2 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
                              _jspx_th_liferay_002dui_005fmessage_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fmessage_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f10);
                              // /html/portlet/image_gallery/view.jsp(160,28) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fmessage_005f2.setKey( (foldersCount == 1) ? "subfolder" : "subfolders" );
                              int _jspx_eval_liferay_002dui_005fmessage_005f2 = _jspx_th_liferay_002dui_005fmessage_005f2.doStartTag();
                              if (_jspx_th_liferay_002dui_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t</div>\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t<div class=\"lfr-asset-icon lfr-asset-items last\">\n");
                              out.write("\t\t\t\t\t\t\t\t");
                              out.print( imagesCount );
                              out.write(' ');
                              //  liferay-ui:message
                              com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f3 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
                              _jspx_th_liferay_002dui_005fmessage_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fmessage_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f10);
                              // /html/portlet/image_gallery/view.jsp(164,27) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fmessage_005f3.setKey( (imagesCount == 1) ? "image" : "images" );
                              int _jspx_eval_liferay_002dui_005fmessage_005f3 = _jspx_th_liferay_002dui_005fmessage_005f3.doStartTag();
                              if (_jspx_th_liferay_002dui_005fmessage_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t</div>\n");
                              out.write("\t\t\t\t\t\t</div>\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
                              //  liferay-ui:custom-attributes-available
                              com.liferay.taglib.ui.CustomAttributesAvailableTag _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0 = (com.liferay.taglib.ui.CustomAttributesAvailableTag) _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattributes_002davailable_0026_005fclassName.get(com.liferay.taglib.ui.CustomAttributesAvailableTag.class);
                              _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f10);
                              // /html/portlet/image_gallery/view.jsp(168,6) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.setClassName( IGFolder.class.getName() );
                              int _jspx_eval_liferay_002dui_005fcustom_002dattributes_002davailable_005f0 = _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fcustom_002dattributes_002davailable_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_liferay_002dui_005fcustom_002dattributes_002davailable_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.doInitBody();
                              }
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t");
                              //  liferay-ui:custom-attribute-list
                              com.liferay.taglib.ui.CustomAttributeListTag _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0 = (com.liferay.taglib.ui.CustomAttributeListTag) _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody.get(com.liferay.taglib.ui.CustomAttributeListTag.class);
                              _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0);
                              // /html/portlet/image_gallery/view.jsp(169,7) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setClassName( IGFolder.class.getName() );
                              // /html/portlet/image_gallery/view.jsp(169,7) name = classPK type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setClassPK( (folder != null) ? folder.getFolderId() : 0 );
                              // /html/portlet/image_gallery/view.jsp(169,7) name = editable type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setEditable( false );
                              // /html/portlet/image_gallery/view.jsp(169,7) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setLabel( true );
                              int _jspx_eval_liferay_002dui_005fcustom_002dattribute_002dlist_005f0 = _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.doStartTag();
                              if (_jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_liferay_002dui_005fcustom_002dattributes_002davailable_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattributes_002davailable_0026_005fclassName.reuse(_jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattributes_002davailable_0026_005fclassName.reuse(_jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0);
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
                          _jspx_th_c_005fif_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
                          // /html/portlet/image_gallery/view.jsp(178,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f11.setTest( foldersCount > 0 );
                          int _jspx_eval_c_005fif_005f11 = _jspx_th_c_005fif_005f11.doStartTag();
                          if (_jspx_eval_c_005fif_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
                              //  liferay-ui:panel
                              com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f0 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.get(com.liferay.taglib.ui.PanelTag.class);
                              _jspx_th_liferay_002dui_005fpanel_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fpanel_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f11);
                              // /html/portlet/image_gallery/view.jsp(179,6) name = collapsible type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fpanel_005f0.setCollapsible( true );
                              // /html/portlet/image_gallery/view.jsp(179,6) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fpanel_005f0.setExtended( true );
                              // /html/portlet/image_gallery/view.jsp(179,6) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fpanel_005f0.setId("subFoldersPanel");
                              // /html/portlet/image_gallery/view.jsp(179,6) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fpanel_005f0.setPersistState( true );
                              // /html/portlet/image_gallery/view.jsp(179,6) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fpanel_005f0.setTitle( LanguageUtil.get(pageContext, (folder != null) ? "subfolders" : "folders") );
                              int _jspx_eval_liferay_002dui_005fpanel_005f0 = _jspx_th_liferay_002dui_005fpanel_005f0.doStartTag();
                              if (_jspx_eval_liferay_002dui_005fpanel_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_liferay_002dui_005fpanel_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dui_005fpanel_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dui_005fpanel_005f0.doInitBody();
                              }
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t");
                              if (_jspx_meth_liferay_002dutil_005finclude_005f1(_jspx_th_liferay_002dui_005fpanel_005f0, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
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
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  liferay-ui:panel
                          com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f1 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fcollapsible.get(com.liferay.taglib.ui.PanelTag.class);
                          _jspx_th_liferay_002dui_005fpanel_005f1.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dui_005fpanel_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
                          // /html/portlet/image_gallery/view.jsp(184,5) name = collapsible type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fpanel_005f1.setCollapsible( true );
                          // /html/portlet/image_gallery/view.jsp(184,5) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fpanel_005f1.setExtended( true );
                          // /html/portlet/image_gallery/view.jsp(184,5) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fpanel_005f1.setId("entriesPanel");
                          // /html/portlet/image_gallery/view.jsp(184,5) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fpanel_005f1.setPersistState( true );
                          // /html/portlet/image_gallery/view.jsp(184,5) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fpanel_005f1.setTitle( LanguageUtil.get(pageContext, "images") );
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

						SearchContainer searchContainer = new SearchContainer(renderRequest, null, null, "cur2", SearchContainer.DEFAULT_DELTA, portletURL, null, null);

						int total = IGImageServiceUtil.getImagesCount(scopeGroupId, folderId);

						searchContainer.setTotal(total);

						List results = IGImageServiceUtil.getImages(scopeGroupId, folderId, searchContainer.getStart(), searchContainer.getEnd());

						searchContainer.setResults(results);

						List scores = null;
						
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
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f3 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f1);
                              int _jspx_eval_c_005fchoose_005f3 = _jspx_th_c_005fchoose_005f3.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f4 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
                              // /html/portlet/image_gallery/view_images.jspf(18,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f4.setTest( results.isEmpty() );
                              int _jspx_eval_c_005fwhen_005f4 = _jspx_th_c_005fwhen_005f4.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t<div class=\"portlet-msg-info\">\n");
                              out.write("\t\t\t");
                              out.print( LanguageUtil.get(pageContext, "there-are-no-images-in-this-folder") );
                              out.write("\n");
                              out.write("\t\t</div>\n");
                              out.write("\t");
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
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f2 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
                              int _jspx_eval_c_005fotherwise_005f2 = _jspx_th_c_005fotherwise_005f2.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t<div class=\"taglib-search-iterator-page-iterator-top\">\n");
                              out.write("\t\t\t");
                              //  liferay-ui:search-paginator
                              com.liferay.taglib.ui.SearchPaginatorTag _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f2 = (com.liferay.taglib.ui.SearchPaginatorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.get(com.liferay.taglib.ui.SearchPaginatorTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f2);
                              // /html/portlet/image_gallery/view_images.jspf(25,3) name = searchContainer type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f2.setSearchContainer( searchContainer );
                              int _jspx_eval_liferay_002dui_005fsearch_002dpaginator_005f2 = _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f2.doStartTag();
                              if (_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f2);
                              out.write("\n");
                              out.write("\t\t</div>\n");
                              out.write("\t");
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
                              out.write("\n");
                              out.write("<div>\n");
                              out.write("\n");
                              out.write("\t");

	for (int i = 0; i < results.size(); i++) {
		IGImage image = null;

		if (useAssetEntryQuery) {
			AssetEntry assetEntry = (AssetEntry)results.get(i);

			image = IGImageLocalServiceUtil.getIGImage(assetEntry.getClassPK());
		}
		else {
			image = (IGImage)results.get(i);
		}
	
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t");
                              //  c:choose
                              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f4 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                              _jspx_th_c_005fchoose_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fchoose_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f1);
                              int _jspx_eval_c_005fchoose_005f4 = _jspx_th_c_005fchoose_005f4.doStartTag();
                              if (_jspx_eval_c_005fchoose_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f5 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f5.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f4);
                              // /html/portlet/image_gallery/view_images.jspf(47,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f5.setTest( IGImagePermission.contains(permissionChecker, image, ActionKeys.VIEW) );
                              int _jspx_eval_c_005fwhen_005f5 = _jspx_th_c_005fwhen_005f5.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t");

				Image largeImage = ImageLocalServiceUtil.getImage(image.getLargeImageId());
				Image smallImage = ImageLocalServiceUtil.getImage(image.getSmallImageId());

				long smallImageId = 0;
				int smallImageHeight = 100;
				int smallImageWidth = 100;

				if (smallImage != null) {
					smallImageId = smallImage.getImageId();
					smallImageHeight = smallImage.getHeight();
					smallImageWidth = smallImage.getWidth();
				}

				int topMargin = PrefsPropsUtil.getInteger(PropsKeys.IG_IMAGE_THUMBNAIL_MAX_DIMENSION) - smallImageHeight;
				int sideMargin = (PrefsPropsUtil.getInteger(PropsKeys.IG_IMAGE_THUMBNAIL_MAX_DIMENSION) - smallImageWidth) / 2;
				
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t<a class=\"image-thumbnail\" href=\"");
                              out.print( themeDisplay.getPathImage() );
                              out.write("/image_gallery?img_id=");
                              out.print( largeImage.getImageId() );
                              out.write('&');
                              out.write('t');
                              out.write('=');
                              out.print( ImageServletTokenUtil.getToken(largeImage.getImageId()) );
                              out.write("\" largeImageId=\"");
                              out.print( largeImage.getImageId() );
                              out.write("\" title=\"");
                              out.print( HtmlUtil.escape(image.getName()) + " - " + HtmlUtil.escape(image.getDescription()) );
                              out.write("\">\n");
                              out.write("\t\t\t\t\t<img alt=\"");
                              out.print( HtmlUtil.escape(image.getName()) + " - " + HtmlUtil.escape(image.getDescription()) );
                              out.write("\" border=\"no\" src=\"");
                              out.print( themeDisplay.getPathImage() );
                              out.write("/image_gallery?img_id=");
                              out.print( smallImageId );
                              out.write("&igImageId=");
                              out.print( image.getImageId() );
                              out.write("&igSmallImage=1&t=");
                              out.print( ImageServletTokenUtil.getToken(smallImageId) );
                              out.write("\" style=\"height: ");
                              out.print( smallImageHeight );
                              out.write("; margin: ");
                              out.print( topMargin );
                              out.write('p');
                              out.write('x');
                              out.write(' ');
                              out.print( sideMargin );
                              out.write("px 0px ");
                              out.print( sideMargin );
                              out.write("px; width: ");
                              out.print( smallImageWidth );
                              out.write(";\" />\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t<span class=\"image-title\">");
                              out.print( HtmlUtil.escape(image.getName()) );
                              out.write("</span>\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f12 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f12.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
                              // /html/portlet/image_gallery/view_images.jspf(72,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f12.setTest( scores != null );
                              int _jspx_eval_c_005fif_005f12 = _jspx_th_c_005fif_005f12.doStartTag();
                              if (_jspx_eval_c_005fif_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t<span class=\"image-score\">\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t");

							double score = ((Double)scores.get(i)).doubleValue();

							score = MathUtils.round((score * 10) / 2, 1, BigDecimal.ROUND_HALF_UP);
							
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t");
                              //  liferay-ui:ratings-score
                              com.liferay.taglib.ui.RatingsScoreTag _jspx_th_liferay_002dui_005fratings_002dscore_005f1 = (com.liferay.taglib.ui.RatingsScoreTag) _005fjspx_005ftagPool_005fliferay_002dui_005fratings_002dscore_0026_005fscore_005fnobody.get(com.liferay.taglib.ui.RatingsScoreTag.class);
                              _jspx_th_liferay_002dui_005fratings_002dscore_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fratings_002dscore_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f12);
                              // /html/portlet/image_gallery/view_images.jspf(81,7) name = score type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fratings_002dscore_005f1.setScore( score );
                              int _jspx_eval_liferay_002dui_005fratings_002dscore_005f1 = _jspx_th_liferay_002dui_005fratings_002dscore_005f1.doStartTag();
                              if (_jspx_th_liferay_002dui_005fratings_002dscore_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fratings_002dscore_0026_005fscore_005fnobody.reuse(_jspx_th_liferay_002dui_005fratings_002dscore_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fratings_002dscore_0026_005fscore_005fnobody.reuse(_jspx_th_liferay_002dui_005fratings_002dscore_005f1);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t</span>\n");
                              out.write("\t\t\t\t\t");
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
                              out.write("\t\t\t\t</a>\n");
                              out.write("\n");
                              out.write("\t\t\t\t<div class=\"aui-helper-hidden\" id=\"");
                              if (_jspx_meth_portlet_005fnamespace_005f4(_jspx_th_c_005fwhen_005f5, _jspx_page_context))
                              return;
                              out.write("buttonsContainer_");
                              out.print( largeImage.getImageId() );
                              out.write("\">\n");
                              out.write("\t\t\t\t\t<div class=\"buttons-container float-container\" id=\"");
                              if (_jspx_meth_portlet_005fnamespace_005f5(_jspx_th_c_005fwhen_005f5, _jspx_page_context))
                              return;
                              out.write("buttons_");
                              out.print( largeImage.getImageId() );
                              out.write("\">\n");
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

boolean view = GetterUtil.getBoolean((String)request.getAttribute("view_image.jsp-view"));

                              out.write('\n');
                              out.write('\n');
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f13 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f13.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
                              // /html/portlet/image_gallery/image_action.jspf(21,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f13.setTest( IGImagePermission.contains(permissionChecker, image, ActionKeys.VIEW) );
                              int _jspx_eval_c_005fif_005f13 = _jspx_th_c_005fif_005f13.doStartTag();
                              if (_jspx_eval_c_005fif_005f13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('\n');
                              out.write('	');

	String downloadURL = themeDisplay.getPathImage() + "/image_gallery?img_id=" + image.getLargeImageId() + "&fileName=" + HttpUtil.encodeURL(image.getNameWithExtension()) + "&t=" + ImageServletTokenUtil.getToken(image.getLargeImageId());
	
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              //  liferay-ui:icon
                              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f4 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                              _jspx_th_liferay_002dui_005ficon_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ficon_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f13);
                              // /html/portlet/image_gallery/image_action.jspf(27,1) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f4.setImage("download");
                              // /html/portlet/image_gallery/image_action.jspf(27,1) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f4.setMessage( LanguageUtil.get(pageContext, "download") + " (" + TextFormatter.formatKB(image.getImageSize(), locale) + "k)" );
                              // /html/portlet/image_gallery/image_action.jspf(27,1) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f4.setUrl( downloadURL );
                              int _jspx_eval_liferay_002dui_005ficon_005f4 = _jspx_th_liferay_002dui_005ficon_005f4.doStartTag();
                              if (_jspx_th_liferay_002dui_005ficon_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f4);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f4);
                              out.write('\n');
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
                              out.write('\n');
                              out.write('\n');
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f14 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f14.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
                              // /html/portlet/image_gallery/image_action.jspf(34,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f14.setTest( !view && IGImagePermission.contains(permissionChecker, image, ActionKeys.VIEW) );
                              int _jspx_eval_c_005fif_005f14 = _jspx_th_c_005fif_005f14.doStartTag();
                              if (_jspx_eval_c_005fif_005f14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  portlet:renderURL
                              com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f3 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                              _jspx_th_portlet_005frenderURL_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005frenderURL_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f14);
                              // /html/portlet/image_gallery/image_action.jspf(35,1) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005frenderURL_005f3.setVar("viewURL");
                              int _jspx_eval_portlet_005frenderURL_005f3 = _jspx_th_portlet_005frenderURL_005f3.doStartTag();
                              if (_jspx_eval_portlet_005frenderURL_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_portlet_005frenderURL_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_portlet_005frenderURL_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_portlet_005frenderURL_005f3.doInitBody();
                              }
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              if (_jspx_meth_portlet_005fparam_005f12(_jspx_th_portlet_005frenderURL_005f3, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f13 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f13.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f3);
                              // /html/portlet/image_gallery/image_action.jspf(37,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f13.setName("redirect");
                              // /html/portlet/image_gallery/image_action.jspf(37,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f13.setValue( currentURL );
                              int _jspx_eval_portlet_005fparam_005f13 = _jspx_th_portlet_005fparam_005f13.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f13);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f13);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f14 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f14.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f3);
                              // /html/portlet/image_gallery/image_action.jspf(38,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f14.setName("imageId");
                              // /html/portlet/image_gallery/image_action.jspf(38,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f14.setValue( String.valueOf(image.getImageId()) );
                              int _jspx_eval_portlet_005fparam_005f14 = _jspx_th_portlet_005fparam_005f14.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f14);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f14);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f3.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_portlet_005frenderURL_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_portlet_005frenderURL_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f3);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f3);
                              java.lang.String viewURL = null;
                              viewURL = (java.lang.String) _jspx_page_context.findAttribute("viewURL");
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              //  liferay-ui:icon
                              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f5 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                              _jspx_th_liferay_002dui_005ficon_005f5.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ficon_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f14);
                              // /html/portlet/image_gallery/image_action.jspf(41,1) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f5.setImage("view");
                              // /html/portlet/image_gallery/image_action.jspf(41,1) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f5.setUrl( viewURL );
                              int _jspx_eval_liferay_002dui_005ficon_005f5 = _jspx_th_liferay_002dui_005ficon_005f5.doStartTag();
                              if (_jspx_th_liferay_002dui_005ficon_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f5);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f5);
                              out.write('\n');
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
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f15 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f15.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
                              // /html/portlet/image_gallery/image_action.jspf(47,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f15.setTest( IGImagePermission.contains(permissionChecker, image, ActionKeys.UPDATE) );
                              int _jspx_eval_c_005fif_005f15 = _jspx_th_c_005fif_005f15.doStartTag();
                              if (_jspx_eval_c_005fif_005f15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  portlet:renderURL
                              com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f4 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                              _jspx_th_portlet_005frenderURL_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005frenderURL_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f15);
                              // /html/portlet/image_gallery/image_action.jspf(48,1) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005frenderURL_005f4.setVar("editURL");
                              int _jspx_eval_portlet_005frenderURL_005f4 = _jspx_th_portlet_005frenderURL_005f4.doStartTag();
                              if (_jspx_eval_portlet_005frenderURL_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_portlet_005frenderURL_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_portlet_005frenderURL_005f4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_portlet_005frenderURL_005f4.doInitBody();
                              }
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              if (_jspx_meth_portlet_005fparam_005f15(_jspx_th_portlet_005frenderURL_005f4, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f16 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f16.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f4);
                              // /html/portlet/image_gallery/image_action.jspf(50,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f16.setName("redirect");
                              // /html/portlet/image_gallery/image_action.jspf(50,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f16.setValue( currentURL );
                              int _jspx_eval_portlet_005fparam_005f16 = _jspx_th_portlet_005fparam_005f16.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f16);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f16);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f17 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f17.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f4);
                              // /html/portlet/image_gallery/image_action.jspf(51,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f17.setName("imageId");
                              // /html/portlet/image_gallery/image_action.jspf(51,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f17.setValue( String.valueOf(image.getImageId()) );
                              int _jspx_eval_portlet_005fparam_005f17 = _jspx_th_portlet_005fparam_005f17.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f17);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f17);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f4.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_portlet_005frenderURL_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_portlet_005frenderURL_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f4);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f4);
                              java.lang.String editURL = null;
                              editURL = (java.lang.String) _jspx_page_context.findAttribute("editURL");
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              //  liferay-ui:icon
                              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f6 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                              _jspx_th_liferay_002dui_005ficon_005f6.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ficon_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f15);
                              // /html/portlet/image_gallery/image_action.jspf(54,1) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f6.setImage("edit");
                              // /html/portlet/image_gallery/image_action.jspf(54,1) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f6.setUrl( editURL );
                              int _jspx_eval_liferay_002dui_005ficon_005f6 = _jspx_th_liferay_002dui_005ficon_005f6.doStartTag();
                              if (_jspx_th_liferay_002dui_005ficon_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f6);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f6);
                              out.write('\n');
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
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f16 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f16.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
                              // /html/portlet/image_gallery/image_action.jspf(60,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f16.setTest( IGImagePermission.contains(permissionChecker, image, ActionKeys.PERMISSIONS) );
                              int _jspx_eval_c_005fif_005f16 = _jspx_th_c_005fif_005f16.doStartTag();
                              if (_jspx_eval_c_005fif_005f16 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  liferay-security:permissionsURL
                              com.liferay.taglib.security.PermissionsURLTag _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f1 = (com.liferay.taglib.security.PermissionsURLTag) _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.get(com.liferay.taglib.security.PermissionsURLTag.class);
                              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f16);
                              // /html/portlet/image_gallery/image_action.jspf(61,1) name = modelResource type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f1.setModelResource( IGImage.class.getName() );
                              // /html/portlet/image_gallery/image_action.jspf(61,1) name = modelResourceDescription type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f1.setModelResourceDescription( String.valueOf(image.getName()) );
                              // /html/portlet/image_gallery/image_action.jspf(61,1) name = resourcePrimKey type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f1.setResourcePrimKey( String.valueOf(image.getImageId()) );
                              // /html/portlet/image_gallery/image_action.jspf(61,1) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f1.setVar("permissionsURL");
                              int _jspx_eval_liferay_002dsecurity_005fpermissionsURL_005f1 = _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f1.doStartTag();
                              if (_jspx_th_liferay_002dsecurity_005fpermissionsURL_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.reuse(_jspx_th_liferay_002dsecurity_005fpermissionsURL_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.reuse(_jspx_th_liferay_002dsecurity_005fpermissionsURL_005f1);
                              java.lang.String permissionsURL = null;
                              permissionsURL = (java.lang.String) _jspx_page_context.findAttribute("permissionsURL");
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              //  liferay-ui:icon
                              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f7 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                              _jspx_th_liferay_002dui_005ficon_005f7.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ficon_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f16);
                              // /html/portlet/image_gallery/image_action.jspf(68,1) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f7.setImage("permissions");
                              // /html/portlet/image_gallery/image_action.jspf(68,1) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f7.setUrl( permissionsURL );
                              int _jspx_eval_liferay_002dui_005ficon_005f7 = _jspx_th_liferay_002dui_005ficon_005f7.doStartTag();
                              if (_jspx_th_liferay_002dui_005ficon_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f7);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f7);
                              out.write('\n');
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
                              out.write('\n');
                              out.write('\n');
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f17 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f17.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
                              // /html/portlet/image_gallery/image_action.jspf(74,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f17.setTest( IGImagePermission.contains(permissionChecker, image, ActionKeys.DELETE) );
                              int _jspx_eval_c_005fif_005f17 = _jspx_th_c_005fif_005f17.doStartTag();
                              if (_jspx_eval_c_005fif_005f17 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write('\n');
                              out.write('	');
                              //  portlet:actionURL
                              com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f1 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.get(com.liferay.taglib.portlet.ActionURLTag.class);
                              _jspx_th_portlet_005factionURL_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005factionURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f17);
                              // /html/portlet/image_gallery/image_action.jspf(75,1) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005factionURL_005f1.setVar("deleteURL");
                              int _jspx_eval_portlet_005factionURL_005f1 = _jspx_th_portlet_005factionURL_005f1.doStartTag();
                              if (_jspx_eval_portlet_005factionURL_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_portlet_005factionURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_portlet_005factionURL_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_portlet_005factionURL_005f1.doInitBody();
                              }
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              if (_jspx_meth_portlet_005fparam_005f18(_jspx_th_portlet_005factionURL_005f1, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f19 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f19.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
                              // /html/portlet/image_gallery/image_action.jspf(77,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f19.setName( Constants.CMD );
                              // /html/portlet/image_gallery/image_action.jspf(77,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f19.setValue( Constants.DELETE );
                              int _jspx_eval_portlet_005fparam_005f19 = _jspx_th_portlet_005fparam_005f19.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f19);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f19);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f20 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f20.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
                              // /html/portlet/image_gallery/image_action.jspf(78,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f20.setName("redirect");
                              // /html/portlet/image_gallery/image_action.jspf(78,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f20.setValue( currentURL );
                              int _jspx_eval_portlet_005fparam_005f20 = _jspx_th_portlet_005fparam_005f20.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f20);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f20);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f21 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f21.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
                              // /html/portlet/image_gallery/image_action.jspf(79,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f21.setName("imageId");
                              // /html/portlet/image_gallery/image_action.jspf(79,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f21.setValue( String.valueOf(image.getImageId()) );
                              int _jspx_eval_portlet_005fparam_005f21 = _jspx_th_portlet_005fparam_005f21.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f21);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f21);
                              out.write('\n');
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
                              java.lang.String deleteURL = null;
                              deleteURL = (java.lang.String) _jspx_page_context.findAttribute("deleteURL");
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              //  liferay-ui:icon-delete
                              com.liferay.taglib.ui.IconDeleteTag _jspx_th_liferay_002dui_005ficon_002ddelete_005f1 = (com.liferay.taglib.ui.IconDeleteTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody.get(com.liferay.taglib.ui.IconDeleteTag.class);
                              _jspx_th_liferay_002dui_005ficon_002ddelete_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ficon_002ddelete_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f17);
                              // /html/portlet/image_gallery/image_action.jspf(82,1) name = url type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_002ddelete_005f1.setUrl( deleteURL );
                              int _jspx_eval_liferay_002dui_005ficon_002ddelete_005f1 = _jspx_th_liferay_002dui_005ficon_002ddelete_005f1.doStartTag();
                              if (_jspx_th_liferay_002dui_005ficon_002ddelete_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002ddelete_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002ddelete_005f1);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fif_005f17.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fif_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f17);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f17);
                              out.write("\n");
                              out.write("\t\t\t\t\t</div>\n");
                              out.write("\t\t\t\t</div>\n");
                              out.write("\n");
                              out.write("\t\t\t\t");

				List assetTags = AssetTagServiceUtil.getTags(IGImage.class.getName(), image.getImageId());
				
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t<div id=\"");
                              if (_jspx_meth_portlet_005fnamespace_005f6(_jspx_th_c_005fwhen_005f5, _jspx_page_context))
                              return;
                              out.write("categorizacionContainer_");
                              out.print( largeImage.getImageId() );
                              out.write("\" style=\"display: none;\">\n");
                              out.write("\t\t\t\t\t<span ");
                              out.print( !assetTags.isEmpty() ? "class=\"has-tags\"" : "" );
                              out.write(">\n");
                              out.write("\t\t\t\t\t\t");
                              //  liferay-ui:asset-categories-summary
                              com.liferay.taglib.ui.AssetCategoriesSummaryTag _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f1 = (com.liferay.taglib.ui.AssetCategoriesSummaryTag) _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dcategories_002dsummary_0026_005fclassPK_005fclassName_005fnobody.get(com.liferay.taglib.ui.AssetCategoriesSummaryTag.class);
                              _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
                              // /html/portlet/image_gallery/view_images.jspf(98,6) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f1.setClassName( IGImage.class.getName() );
                              // /html/portlet/image_gallery/view_images.jspf(98,6) name = classPK type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f1.setClassPK( image.getImageId() );
                              int _jspx_eval_liferay_002dui_005fasset_002dcategories_002dsummary_005f1 = _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f1.doStartTag();
                              if (_jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dcategories_002dsummary_0026_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dcategories_002dsummary_0026_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f1);
                              out.write("\n");
                              out.write("\t\t\t\t\t</span>\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              //  liferay-ui:asset-tags-summary
                              com.liferay.taglib.ui.AssetTagsSummaryTag _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f1 = (com.liferay.taglib.ui.AssetTagsSummaryTag) _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002dsummary_0026_005fclassPK_005fclassName_005fnobody.get(com.liferay.taglib.ui.AssetTagsSummaryTag.class);
                              _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
                              // /html/portlet/image_gallery/view_images.jspf(104,5) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f1.setClassName( IGImage.class.getName() );
                              // /html/portlet/image_gallery/view_images.jspf(104,5) name = classPK type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f1.setClassPK( image.getImageId() );
                              int _jspx_eval_liferay_002dui_005fasset_002dtags_002dsummary_005f1 = _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f1.doStartTag();
                              if (_jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002dsummary_0026_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002dsummary_0026_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f1);
                              out.write("\n");
                              out.write("\t\t\t\t</div>\n");
                              out.write("\t\t\t");
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
                              out.write("\t\t\t");
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f3 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f4);
                              int _jspx_eval_c_005fotherwise_005f3 = _jspx_th_c_005fotherwise_005f3.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t<div style=\"float: left; margin: 100px 10px 0px;\">\n");
                              out.write("\t\t\t\t\t<img alt=\"");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f4(_jspx_th_c_005fotherwise_005f3, _jspx_page_context))
                              return;
                              out.write("\" border=\"no\" src=\"");
                              out.print( themeDisplay.getPathThemeImages() );
                              out.write("/application/forbidden_action.png\" />\n");
                              out.write("\t\t\t\t</div>\n");
                              out.write("\t\t\t");
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
                              out.write('\n');
                              out.write('	');
                              out.write('	');
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
                              out.write('\n');
                              out.write('\n');
                              out.write('	');

	}
	
                              out.write("\n");
                              out.write("\n");
                              out.write("</div>\n");
                              out.write("\n");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f18 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f18.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f1);
                              // /html/portlet/image_gallery/view_images.jspf(123,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f18.setTest( !results.isEmpty() );
                              int _jspx_eval_c_005fif_005f18 = _jspx_th_c_005fif_005f18.doStartTag();
                              if (_jspx_eval_c_005fif_005f18 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t<div class=\"taglib-search-iterator-page-iterator-bottom\">\n");
                              out.write("\t\t");
                              //  liferay-ui:search-paginator
                              com.liferay.taglib.ui.SearchPaginatorTag _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f3 = (com.liferay.taglib.ui.SearchPaginatorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.get(com.liferay.taglib.ui.SearchPaginatorTag.class);
                              _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f18);
                              // /html/portlet/image_gallery/view_images.jspf(125,2) name = searchContainer type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f3.setSearchContainer( searchContainer );
                              int _jspx_eval_liferay_002dui_005fsearch_002dpaginator_005f3 = _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f3.doStartTag();
                              if (_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f3);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f3);
                              out.write("\n");
                              out.write("\t</div>\n");
                              int evalDoAfterBody = _jspx_th_c_005fif_005f18.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              }
                              if (_jspx_th_c_005fif_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f18);
                              return;
                              }
                              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f18);
                              out.write('\n');
                              out.write('\n');
                              if (_jspx_meth_aui_005fscript_005f1(_jspx_th_liferay_002dui_005fpanel_005f1, _jspx_page_context))
                              return;
                              out.write("\n");
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
                      int evalDoAfterBody = _jspx_th_aui_005fcolumn_005f0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_aui_005fcolumn_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_aui_005fcolumn_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fcolumn_0026_005ffirst_005fcssClass_005fcolumnWidth.reuse(_jspx_th_aui_005fcolumn_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fcolumn_0026_005ffirst_005fcssClass_005fcolumnWidth.reuse(_jspx_th_aui_005fcolumn_005f0);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:column
                  com.liferay.taglib.aui.ColumnTag _jspx_th_aui_005fcolumn_005f1 = (com.liferay.taglib.aui.ColumnTag) _005fjspx_005ftagPool_005faui_005fcolumn_0026_005flast_005fcssClass_005fcolumnWidth.get(com.liferay.taglib.aui.ColumnTag.class);
                  _jspx_th_aui_005fcolumn_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fcolumn_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005flayout_005f0);
                  // /html/portlet/image_gallery/view.jsp(206,3) name = columnWidth type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fcolumn_005f1.setColumnWidth( 25 );
                  // /html/portlet/image_gallery/view.jsp(206,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fcolumn_005f1.setCssClass("lfr-asset-column lfr-asset-column-actions");
                  // /html/portlet/image_gallery/view.jsp(206,3) name = last type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fcolumn_005f1.setLast( true );
                  int _jspx_eval_aui_005fcolumn_005f1 = _jspx_th_aui_005fcolumn_005f1.doStartTag();
                  if (_jspx_eval_aui_005fcolumn_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005fcolumn_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005fcolumn_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005fcolumn_005f1.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t<div class=\"lfr-asset-summary\">\n");
                      out.write("\t\t\t\t\t");
                      //  liferay-ui:icon
                      com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f8 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fmessage_005fimage_005fcssClass_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                      _jspx_th_liferay_002dui_005ficon_005f8.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ficon_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fcolumn_005f1);
                      // /html/portlet/image_gallery/view.jsp(208,5) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f8.setCssClass("lfr-asset-avatar");
                      // /html/portlet/image_gallery/view.jsp(208,5) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f8.setImage( "../file_system/large/" + (((foldersCount + imagesCount) > 0) ? "folder_full_image" : "folder_empty") );
                      // /html/portlet/image_gallery/view.jsp(208,5) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f8.setMessage( (folder != null) ? folder.getName() : LanguageUtil.get(pageContext, "images-home") );
                      int _jspx_eval_liferay_002dui_005ficon_005f8 = _jspx_th_liferay_002dui_005ficon_005f8.doStartTag();
                      if (_jspx_th_liferay_002dui_005ficon_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fmessage_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f8);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fmessage_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f8);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t<div class=\"lfr-asset-name\">\n");
                      out.write("\t\t\t\t\t\t<h4>");
                      out.print( (folder != null) ? folder.getName() : LanguageUtil.get(pageContext, "images-home") );
                      out.write("</h4>\n");
                      out.write("\t\t\t\t\t</div>\n");
                      out.write("\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t");

				request.removeAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
				
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      if (_jspx_meth_liferay_002dutil_005finclude_005f2(_jspx_th_aui_005fcolumn_005f1, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t");
                      int evalDoAfterBody = _jspx_th_aui_005fcolumn_005f1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_aui_005fcolumn_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_aui_005fcolumn_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fcolumn_0026_005flast_005fcssClass_005fcolumnWidth.reuse(_jspx_th_aui_005fcolumn_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fcolumn_0026_005flast_005fcssClass_005fcolumnWidth.reuse(_jspx_th_aui_005fcolumn_005f1);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_aui_005flayout_005f0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_aui_005flayout_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_aui_005flayout_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005flayout.reuse(_jspx_th_aui_005flayout_005f0);
                return;
              }
              _005fjspx_005ftagPool_005faui_005flayout.reuse(_jspx_th_aui_005flayout_005f0);
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

		if (folder != null) {
			IGUtil.addPortletBreadcrumbEntries(folder, request, renderResponse);

			if (portletName.equals(PortletKeys.IMAGE_GALLERY)) {
				PortalUtil.setPageSubtitle(folder.getName(), request);
				PortalUtil.setPageDescription(folder.getDescription(), request);
			}
		}
		
              out.write('\n');
              out.write('\n');
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
          //  c:when
          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f6 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
          _jspx_th_c_005fwhen_005f6.setPageContext(_jspx_page_context);
          _jspx_th_c_005fwhen_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
          // /html/portlet/image_gallery/view.jsp(239,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fwhen_005f6.setTest( topLink.equals("my-images") || topLink.equals("recent-images") );
          int _jspx_eval_c_005fwhen_005f6 = _jspx_th_c_005fwhen_005f6.doStartTag();
          if (_jspx_eval_c_005fwhen_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

		long groupImagesUserId = 0;

		if (topLink.equals("my-images") && themeDisplay.isSignedIn()) {
			groupImagesUserId = user.getUserId();
		}

		SearchContainer searchContainer = new SearchContainer(renderRequest, null, null, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_DELTA, portletURL, null, null);

		int total = IGImageServiceUtil.getGroupImagesCount(scopeGroupId, groupImagesUserId);

		searchContainer.setTotal(total);

		List results = IGImageServiceUtil.getGroupImages(scopeGroupId, groupImagesUserId, searchContainer.getStart(), searchContainer.getEnd());

		searchContainer.setResults(results);
		
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  aui:layout
              com.liferay.taglib.aui.LayoutTag _jspx_th_aui_005flayout_005f1 = (com.liferay.taglib.aui.LayoutTag) _005fjspx_005ftagPool_005faui_005flayout.get(com.liferay.taglib.aui.LayoutTag.class);
              _jspx_th_aui_005flayout_005f1.setPageContext(_jspx_page_context);
              _jspx_th_aui_005flayout_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f6);
              int _jspx_eval_aui_005flayout_005f1 = _jspx_th_aui_005flayout_005f1.doStartTag();
              if (_jspx_eval_aui_005flayout_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_aui_005flayout_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_aui_005flayout_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_aui_005flayout_005f1.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  liferay-ui:header
                  com.liferay.taglib.ui.HeaderTag _jspx_th_liferay_002dui_005fheader_005f1 = (com.liferay.taglib.ui.HeaderTag) _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fnobody.get(com.liferay.taglib.ui.HeaderTag.class);
                  _jspx_th_liferay_002dui_005fheader_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fheader_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005flayout_005f1);
                  // /html/portlet/image_gallery/view.jsp(260,3) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fheader_005f1.setTitle( topLink );
                  int _jspx_eval_liferay_002dui_005fheader_005f1 = _jspx_th_liferay_002dui_005fheader_005f1.doStartTag();
                  if (_jspx_th_liferay_002dui_005fheader_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fnobody.reuse(_jspx_th_liferay_002dui_005fheader_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fnobody.reuse(_jspx_th_liferay_002dui_005fheader_005f1);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");

			List scores = null;
			
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
                  //  c:choose
                  org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f5 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                  _jspx_th_c_005fchoose_005f5.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fchoose_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005flayout_005f1);
                  int _jspx_eval_c_005fchoose_005f5 = _jspx_th_c_005fchoose_005f5.doStartTag();
                  if (_jspx_eval_c_005fchoose_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write('\n');
                      out.write('	');
                      //  c:when
                      org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f7 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                      _jspx_th_c_005fwhen_005f7.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fwhen_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f5);
                      // /html/portlet/image_gallery/view_images.jspf(18,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fwhen_005f7.setTest( results.isEmpty() );
                      int _jspx_eval_c_005fwhen_005f7 = _jspx_th_c_005fwhen_005f7.doStartTag();
                      if (_jspx_eval_c_005fwhen_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t<div class=\"portlet-msg-info\">\n");
                          out.write("\t\t\t");
                          out.print( LanguageUtil.get(pageContext, "there-are-no-images-in-this-folder") );
                          out.write("\n");
                          out.write("\t\t</div>\n");
                          out.write("\t");
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
                      out.write('\n');
                      out.write('	');
                      //  c:otherwise
                      org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f4 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                      _jspx_th_c_005fotherwise_005f4.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fotherwise_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f5);
                      int _jspx_eval_c_005fotherwise_005f4 = _jspx_th_c_005fotherwise_005f4.doStartTag();
                      if (_jspx_eval_c_005fotherwise_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t<div class=\"taglib-search-iterator-page-iterator-top\">\n");
                          out.write("\t\t\t");
                          //  liferay-ui:search-paginator
                          com.liferay.taglib.ui.SearchPaginatorTag _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f4 = (com.liferay.taglib.ui.SearchPaginatorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.get(com.liferay.taglib.ui.SearchPaginatorTag.class);
                          _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f4.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f4);
                          // /html/portlet/image_gallery/view_images.jspf(25,3) name = searchContainer type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f4.setSearchContainer( searchContainer );
                          int _jspx_eval_liferay_002dui_005fsearch_002dpaginator_005f4 = _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f4.doStartTag();
                          if (_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f4);
                            return;
                          }
                          _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f4);
                          out.write("\n");
                          out.write("\t\t</div>\n");
                          out.write("\t");
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
                      out.write('\n');
                      int evalDoAfterBody = _jspx_th_c_005fchoose_005f5.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fchoose_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f5);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f5);
                  out.write("\n");
                  out.write("\n");
                  out.write("<div>\n");
                  out.write("\n");
                  out.write("\t");

	for (int i = 0; i < results.size(); i++) {
		IGImage image = null;

		if (useAssetEntryQuery) {
			AssetEntry assetEntry = (AssetEntry)results.get(i);

			image = IGImageLocalServiceUtil.getIGImage(assetEntry.getClassPK());
		}
		else {
			image = (IGImage)results.get(i);
		}
	
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t");
                  //  c:choose
                  org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f6 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                  _jspx_th_c_005fchoose_005f6.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fchoose_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005flayout_005f1);
                  int _jspx_eval_c_005fchoose_005f6 = _jspx_th_c_005fchoose_005f6.doStartTag();
                  if (_jspx_eval_c_005fchoose_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t");
                      //  c:when
                      org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f8 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                      _jspx_th_c_005fwhen_005f8.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fwhen_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f6);
                      // /html/portlet/image_gallery/view_images.jspf(47,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fwhen_005f8.setTest( IGImagePermission.contains(permissionChecker, image, ActionKeys.VIEW) );
                      int _jspx_eval_c_005fwhen_005f8 = _jspx_th_c_005fwhen_005f8.doStartTag();
                      if (_jspx_eval_c_005fwhen_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t");

				Image largeImage = ImageLocalServiceUtil.getImage(image.getLargeImageId());
				Image smallImage = ImageLocalServiceUtil.getImage(image.getSmallImageId());

				long smallImageId = 0;
				int smallImageHeight = 100;
				int smallImageWidth = 100;

				if (smallImage != null) {
					smallImageId = smallImage.getImageId();
					smallImageHeight = smallImage.getHeight();
					smallImageWidth = smallImage.getWidth();
				}

				int topMargin = PrefsPropsUtil.getInteger(PropsKeys.IG_IMAGE_THUMBNAIL_MAX_DIMENSION) - smallImageHeight;
				int sideMargin = (PrefsPropsUtil.getInteger(PropsKeys.IG_IMAGE_THUMBNAIL_MAX_DIMENSION) - smallImageWidth) / 2;
				
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t<a class=\"image-thumbnail\" href=\"");
                          out.print( themeDisplay.getPathImage() );
                          out.write("/image_gallery?img_id=");
                          out.print( largeImage.getImageId() );
                          out.write('&');
                          out.write('t');
                          out.write('=');
                          out.print( ImageServletTokenUtil.getToken(largeImage.getImageId()) );
                          out.write("\" largeImageId=\"");
                          out.print( largeImage.getImageId() );
                          out.write("\" title=\"");
                          out.print( HtmlUtil.escape(image.getName()) + " - " + HtmlUtil.escape(image.getDescription()) );
                          out.write("\">\n");
                          out.write("\t\t\t\t\t<img alt=\"");
                          out.print( HtmlUtil.escape(image.getName()) + " - " + HtmlUtil.escape(image.getDescription()) );
                          out.write("\" border=\"no\" src=\"");
                          out.print( themeDisplay.getPathImage() );
                          out.write("/image_gallery?img_id=");
                          out.print( smallImageId );
                          out.write("&igImageId=");
                          out.print( image.getImageId() );
                          out.write("&igSmallImage=1&t=");
                          out.print( ImageServletTokenUtil.getToken(smallImageId) );
                          out.write("\" style=\"height: ");
                          out.print( smallImageHeight );
                          out.write("; margin: ");
                          out.print( topMargin );
                          out.write('p');
                          out.write('x');
                          out.write(' ');
                          out.print( sideMargin );
                          out.write("px 0px ");
                          out.print( sideMargin );
                          out.write("px; width: ");
                          out.print( smallImageWidth );
                          out.write(";\" />\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t<span class=\"image-title\">");
                          out.print( HtmlUtil.escape(image.getName()) );
                          out.write("</span>\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f19 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f19.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f8);
                          // /html/portlet/image_gallery/view_images.jspf(72,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f19.setTest( scores != null );
                          int _jspx_eval_c_005fif_005f19 = _jspx_th_c_005fif_005f19.doStartTag();
                          if (_jspx_eval_c_005fif_005f19 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t<span class=\"image-score\">\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t");

							double score = ((Double)scores.get(i)).doubleValue();

							score = MathUtils.round((score * 10) / 2, 1, BigDecimal.ROUND_HALF_UP);
							
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t");
                              //  liferay-ui:ratings-score
                              com.liferay.taglib.ui.RatingsScoreTag _jspx_th_liferay_002dui_005fratings_002dscore_005f2 = (com.liferay.taglib.ui.RatingsScoreTag) _005fjspx_005ftagPool_005fliferay_002dui_005fratings_002dscore_0026_005fscore_005fnobody.get(com.liferay.taglib.ui.RatingsScoreTag.class);
                              _jspx_th_liferay_002dui_005fratings_002dscore_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fratings_002dscore_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f19);
                              // /html/portlet/image_gallery/view_images.jspf(81,7) name = score type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fratings_002dscore_005f2.setScore( score );
                              int _jspx_eval_liferay_002dui_005fratings_002dscore_005f2 = _jspx_th_liferay_002dui_005fratings_002dscore_005f2.doStartTag();
                              if (_jspx_th_liferay_002dui_005fratings_002dscore_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fratings_002dscore_0026_005fscore_005fnobody.reuse(_jspx_th_liferay_002dui_005fratings_002dscore_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fratings_002dscore_0026_005fscore_005fnobody.reuse(_jspx_th_liferay_002dui_005fratings_002dscore_005f2);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t</span>\n");
                              out.write("\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_c_005fif_005f19.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fif_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f19);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f19);
                          out.write("\n");
                          out.write("\t\t\t\t</a>\n");
                          out.write("\n");
                          out.write("\t\t\t\t<div class=\"aui-helper-hidden\" id=\"");
                          if (_jspx_meth_portlet_005fnamespace_005f8(_jspx_th_c_005fwhen_005f8, _jspx_page_context))
                            return;
                          out.write("buttonsContainer_");
                          out.print( largeImage.getImageId() );
                          out.write("\">\n");
                          out.write("\t\t\t\t\t<div class=\"buttons-container float-container\" id=\"");
                          if (_jspx_meth_portlet_005fnamespace_005f9(_jspx_th_c_005fwhen_005f8, _jspx_page_context))
                            return;
                          out.write("buttons_");
                          out.print( largeImage.getImageId() );
                          out.write("\">\n");
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

boolean view = GetterUtil.getBoolean((String)request.getAttribute("view_image.jsp-view"));

                          out.write('\n');
                          out.write('\n');
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f20 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f20.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f8);
                          // /html/portlet/image_gallery/image_action.jspf(21,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f20.setTest( IGImagePermission.contains(permissionChecker, image, ActionKeys.VIEW) );
                          int _jspx_eval_c_005fif_005f20 = _jspx_th_c_005fif_005f20.doStartTag();
                          if (_jspx_eval_c_005fif_005f20 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write('\n');
                              out.write('\n');
                              out.write('	');

	String downloadURL = themeDisplay.getPathImage() + "/image_gallery?img_id=" + image.getLargeImageId() + "&fileName=" + HttpUtil.encodeURL(image.getNameWithExtension()) + "&t=" + ImageServletTokenUtil.getToken(image.getLargeImageId());
	
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              //  liferay-ui:icon
                              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f9 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                              _jspx_th_liferay_002dui_005ficon_005f9.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ficon_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f20);
                              // /html/portlet/image_gallery/image_action.jspf(27,1) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f9.setImage("download");
                              // /html/portlet/image_gallery/image_action.jspf(27,1) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f9.setMessage( LanguageUtil.get(pageContext, "download") + " (" + TextFormatter.formatKB(image.getImageSize(), locale) + "k)" );
                              // /html/portlet/image_gallery/image_action.jspf(27,1) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f9.setUrl( downloadURL );
                              int _jspx_eval_liferay_002dui_005ficon_005f9 = _jspx_th_liferay_002dui_005ficon_005f9.doStartTag();
                              if (_jspx_th_liferay_002dui_005ficon_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f9);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f9);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fif_005f20.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fif_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f20);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f20);
                          out.write('\n');
                          out.write('\n');
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f21 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f21.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f8);
                          // /html/portlet/image_gallery/image_action.jspf(34,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f21.setTest( !view && IGImagePermission.contains(permissionChecker, image, ActionKeys.VIEW) );
                          int _jspx_eval_c_005fif_005f21 = _jspx_th_c_005fif_005f21.doStartTag();
                          if (_jspx_eval_c_005fif_005f21 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write('\n');
                              out.write('	');
                              //  portlet:renderURL
                              com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f5 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                              _jspx_th_portlet_005frenderURL_005f5.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005frenderURL_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f21);
                              // /html/portlet/image_gallery/image_action.jspf(35,1) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005frenderURL_005f5.setVar("viewURL");
                              int _jspx_eval_portlet_005frenderURL_005f5 = _jspx_th_portlet_005frenderURL_005f5.doStartTag();
                              if (_jspx_eval_portlet_005frenderURL_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_portlet_005frenderURL_005f5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_portlet_005frenderURL_005f5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_portlet_005frenderURL_005f5.doInitBody();
                              }
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              if (_jspx_meth_portlet_005fparam_005f22(_jspx_th_portlet_005frenderURL_005f5, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f23 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f23.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f5);
                              // /html/portlet/image_gallery/image_action.jspf(37,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f23.setName("redirect");
                              // /html/portlet/image_gallery/image_action.jspf(37,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f23.setValue( currentURL );
                              int _jspx_eval_portlet_005fparam_005f23 = _jspx_th_portlet_005fparam_005f23.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f23);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f23);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f24 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f24.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f5);
                              // /html/portlet/image_gallery/image_action.jspf(38,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f24.setName("imageId");
                              // /html/portlet/image_gallery/image_action.jspf(38,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f24.setValue( String.valueOf(image.getImageId()) );
                              int _jspx_eval_portlet_005fparam_005f24 = _jspx_th_portlet_005fparam_005f24.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f24);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f24);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f5.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_portlet_005frenderURL_005f5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_portlet_005frenderURL_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f5);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f5);
                              java.lang.String viewURL = null;
                              viewURL = (java.lang.String) _jspx_page_context.findAttribute("viewURL");
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              //  liferay-ui:icon
                              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f10 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                              _jspx_th_liferay_002dui_005ficon_005f10.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ficon_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f21);
                              // /html/portlet/image_gallery/image_action.jspf(41,1) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f10.setImage("view");
                              // /html/portlet/image_gallery/image_action.jspf(41,1) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f10.setUrl( viewURL );
                              int _jspx_eval_liferay_002dui_005ficon_005f10 = _jspx_th_liferay_002dui_005ficon_005f10.doStartTag();
                              if (_jspx_th_liferay_002dui_005ficon_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f10);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f10);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fif_005f21.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fif_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f21);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f21);
                          out.write('\n');
                          out.write('\n');
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f22 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f22.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f8);
                          // /html/portlet/image_gallery/image_action.jspf(47,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f22.setTest( IGImagePermission.contains(permissionChecker, image, ActionKeys.UPDATE) );
                          int _jspx_eval_c_005fif_005f22 = _jspx_th_c_005fif_005f22.doStartTag();
                          if (_jspx_eval_c_005fif_005f22 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write('\n');
                              out.write('	');
                              //  portlet:renderURL
                              com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f6 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                              _jspx_th_portlet_005frenderURL_005f6.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005frenderURL_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f22);
                              // /html/portlet/image_gallery/image_action.jspf(48,1) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005frenderURL_005f6.setVar("editURL");
                              int _jspx_eval_portlet_005frenderURL_005f6 = _jspx_th_portlet_005frenderURL_005f6.doStartTag();
                              if (_jspx_eval_portlet_005frenderURL_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_portlet_005frenderURL_005f6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_portlet_005frenderURL_005f6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_portlet_005frenderURL_005f6.doInitBody();
                              }
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              if (_jspx_meth_portlet_005fparam_005f25(_jspx_th_portlet_005frenderURL_005f6, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f26 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f26.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f6);
                              // /html/portlet/image_gallery/image_action.jspf(50,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f26.setName("redirect");
                              // /html/portlet/image_gallery/image_action.jspf(50,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f26.setValue( currentURL );
                              int _jspx_eval_portlet_005fparam_005f26 = _jspx_th_portlet_005fparam_005f26.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f26);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f26);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f27 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f27.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f6);
                              // /html/portlet/image_gallery/image_action.jspf(51,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f27.setName("imageId");
                              // /html/portlet/image_gallery/image_action.jspf(51,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f27.setValue( String.valueOf(image.getImageId()) );
                              int _jspx_eval_portlet_005fparam_005f27 = _jspx_th_portlet_005fparam_005f27.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f27);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f27);
                              out.write('\n');
                              out.write('	');
                              int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f6.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_portlet_005frenderURL_005f6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_portlet_005frenderURL_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f6);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f6);
                              java.lang.String editURL = null;
                              editURL = (java.lang.String) _jspx_page_context.findAttribute("editURL");
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              //  liferay-ui:icon
                              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f11 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                              _jspx_th_liferay_002dui_005ficon_005f11.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ficon_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f22);
                              // /html/portlet/image_gallery/image_action.jspf(54,1) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f11.setImage("edit");
                              // /html/portlet/image_gallery/image_action.jspf(54,1) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f11.setUrl( editURL );
                              int _jspx_eval_liferay_002dui_005ficon_005f11 = _jspx_th_liferay_002dui_005ficon_005f11.doStartTag();
                              if (_jspx_th_liferay_002dui_005ficon_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f11);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f11);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fif_005f22.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fif_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f22);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f22);
                          out.write('\n');
                          out.write('\n');
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f23 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f23.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f8);
                          // /html/portlet/image_gallery/image_action.jspf(60,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f23.setTest( IGImagePermission.contains(permissionChecker, image, ActionKeys.PERMISSIONS) );
                          int _jspx_eval_c_005fif_005f23 = _jspx_th_c_005fif_005f23.doStartTag();
                          if (_jspx_eval_c_005fif_005f23 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write('\n');
                              out.write('	');
                              //  liferay-security:permissionsURL
                              com.liferay.taglib.security.PermissionsURLTag _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f2 = (com.liferay.taglib.security.PermissionsURLTag) _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.get(com.liferay.taglib.security.PermissionsURLTag.class);
                              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f23);
                              // /html/portlet/image_gallery/image_action.jspf(61,1) name = modelResource type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f2.setModelResource( IGImage.class.getName() );
                              // /html/portlet/image_gallery/image_action.jspf(61,1) name = modelResourceDescription type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f2.setModelResourceDescription( String.valueOf(image.getName()) );
                              // /html/portlet/image_gallery/image_action.jspf(61,1) name = resourcePrimKey type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f2.setResourcePrimKey( String.valueOf(image.getImageId()) );
                              // /html/portlet/image_gallery/image_action.jspf(61,1) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f2.setVar("permissionsURL");
                              int _jspx_eval_liferay_002dsecurity_005fpermissionsURL_005f2 = _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f2.doStartTag();
                              if (_jspx_th_liferay_002dsecurity_005fpermissionsURL_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.reuse(_jspx_th_liferay_002dsecurity_005fpermissionsURL_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.reuse(_jspx_th_liferay_002dsecurity_005fpermissionsURL_005f2);
                              java.lang.String permissionsURL = null;
                              permissionsURL = (java.lang.String) _jspx_page_context.findAttribute("permissionsURL");
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              //  liferay-ui:icon
                              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f12 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                              _jspx_th_liferay_002dui_005ficon_005f12.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ficon_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f23);
                              // /html/portlet/image_gallery/image_action.jspf(68,1) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f12.setImage("permissions");
                              // /html/portlet/image_gallery/image_action.jspf(68,1) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_005f12.setUrl( permissionsURL );
                              int _jspx_eval_liferay_002dui_005ficon_005f12 = _jspx_th_liferay_002dui_005ficon_005f12.doStartTag();
                              if (_jspx_th_liferay_002dui_005ficon_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f12);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f12);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fif_005f23.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fif_005f23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f23);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f23);
                          out.write('\n');
                          out.write('\n');
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f24 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f24.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f8);
                          // /html/portlet/image_gallery/image_action.jspf(74,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f24.setTest( IGImagePermission.contains(permissionChecker, image, ActionKeys.DELETE) );
                          int _jspx_eval_c_005fif_005f24 = _jspx_th_c_005fif_005f24.doStartTag();
                          if (_jspx_eval_c_005fif_005f24 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write('\n');
                              out.write('	');
                              //  portlet:actionURL
                              com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f2 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.get(com.liferay.taglib.portlet.ActionURLTag.class);
                              _jspx_th_portlet_005factionURL_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005factionURL_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f24);
                              // /html/portlet/image_gallery/image_action.jspf(75,1) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005factionURL_005f2.setVar("deleteURL");
                              int _jspx_eval_portlet_005factionURL_005f2 = _jspx_th_portlet_005factionURL_005f2.doStartTag();
                              if (_jspx_eval_portlet_005factionURL_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_portlet_005factionURL_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_portlet_005factionURL_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_portlet_005factionURL_005f2.doInitBody();
                              }
                              do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              if (_jspx_meth_portlet_005fparam_005f28(_jspx_th_portlet_005factionURL_005f2, _jspx_page_context))
                              return;
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f29 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f29.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f29.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f2);
                              // /html/portlet/image_gallery/image_action.jspf(77,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f29.setName( Constants.CMD );
                              // /html/portlet/image_gallery/image_action.jspf(77,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f29.setValue( Constants.DELETE );
                              int _jspx_eval_portlet_005fparam_005f29 = _jspx_th_portlet_005fparam_005f29.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f29.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f29);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f29);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f30 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f30.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f30.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f2);
                              // /html/portlet/image_gallery/image_action.jspf(78,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f30.setName("redirect");
                              // /html/portlet/image_gallery/image_action.jspf(78,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f30.setValue( currentURL );
                              int _jspx_eval_portlet_005fparam_005f30 = _jspx_th_portlet_005fparam_005f30.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f30.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f30);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f30);
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f31 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f31.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f31.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f2);
                              // /html/portlet/image_gallery/image_action.jspf(79,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f31.setName("imageId");
                              // /html/portlet/image_gallery/image_action.jspf(79,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f31.setValue( String.valueOf(image.getImageId()) );
                              int _jspx_eval_portlet_005fparam_005f31 = _jspx_th_portlet_005fparam_005f31.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f31.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f31);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f31);
                              out.write('\n');
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
                              java.lang.String deleteURL = null;
                              deleteURL = (java.lang.String) _jspx_page_context.findAttribute("deleteURL");
                              out.write('\n');
                              out.write('\n');
                              out.write('	');
                              //  liferay-ui:icon-delete
                              com.liferay.taglib.ui.IconDeleteTag _jspx_th_liferay_002dui_005ficon_002ddelete_005f2 = (com.liferay.taglib.ui.IconDeleteTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody.get(com.liferay.taglib.ui.IconDeleteTag.class);
                              _jspx_th_liferay_002dui_005ficon_002ddelete_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ficon_002ddelete_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f24);
                              // /html/portlet/image_gallery/image_action.jspf(82,1) name = url type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ficon_002ddelete_005f2.setUrl( deleteURL );
                              int _jspx_eval_liferay_002dui_005ficon_002ddelete_005f2 = _jspx_th_liferay_002dui_005ficon_002ddelete_005f2.doStartTag();
                              if (_jspx_th_liferay_002dui_005ficon_002ddelete_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002ddelete_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002ddelete_005f2);
                              out.write('\n');
                              int evalDoAfterBody = _jspx_th_c_005fif_005f24.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                          }
                          if (_jspx_th_c_005fif_005f24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f24);
                            return;
                          }
                          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f24);
                          out.write("\n");
                          out.write("\t\t\t\t\t</div>\n");
                          out.write("\t\t\t\t</div>\n");
                          out.write("\n");
                          out.write("\t\t\t\t");

				List assetTags = AssetTagServiceUtil.getTags(IGImage.class.getName(), image.getImageId());
				
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t<div id=\"");
                          if (_jspx_meth_portlet_005fnamespace_005f10(_jspx_th_c_005fwhen_005f8, _jspx_page_context))
                            return;
                          out.write("categorizacionContainer_");
                          out.print( largeImage.getImageId() );
                          out.write("\" style=\"display: none;\">\n");
                          out.write("\t\t\t\t\t<span ");
                          out.print( !assetTags.isEmpty() ? "class=\"has-tags\"" : "" );
                          out.write(">\n");
                          out.write("\t\t\t\t\t\t");
                          //  liferay-ui:asset-categories-summary
                          com.liferay.taglib.ui.AssetCategoriesSummaryTag _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f2 = (com.liferay.taglib.ui.AssetCategoriesSummaryTag) _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dcategories_002dsummary_0026_005fclassPK_005fclassName_005fnobody.get(com.liferay.taglib.ui.AssetCategoriesSummaryTag.class);
                          _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f2.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f8);
                          // /html/portlet/image_gallery/view_images.jspf(98,6) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f2.setClassName( IGImage.class.getName() );
                          // /html/portlet/image_gallery/view_images.jspf(98,6) name = classPK type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f2.setClassPK( image.getImageId() );
                          int _jspx_eval_liferay_002dui_005fasset_002dcategories_002dsummary_005f2 = _jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f2.doStartTag();
                          if (_jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dcategories_002dsummary_0026_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f2);
                            return;
                          }
                          _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dcategories_002dsummary_0026_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fasset_002dcategories_002dsummary_005f2);
                          out.write("\n");
                          out.write("\t\t\t\t\t</span>\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  liferay-ui:asset-tags-summary
                          com.liferay.taglib.ui.AssetTagsSummaryTag _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f2 = (com.liferay.taglib.ui.AssetTagsSummaryTag) _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002dsummary_0026_005fclassPK_005fclassName_005fnobody.get(com.liferay.taglib.ui.AssetTagsSummaryTag.class);
                          _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f2.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f8);
                          // /html/portlet/image_gallery/view_images.jspf(104,5) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f2.setClassName( IGImage.class.getName() );
                          // /html/portlet/image_gallery/view_images.jspf(104,5) name = classPK type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f2.setClassPK( image.getImageId() );
                          int _jspx_eval_liferay_002dui_005fasset_002dtags_002dsummary_005f2 = _jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f2.doStartTag();
                          if (_jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002dsummary_0026_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f2);
                            return;
                          }
                          _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002dsummary_0026_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fasset_002dtags_002dsummary_005f2);
                          out.write("\n");
                          out.write("\t\t\t\t</div>\n");
                          out.write("\t\t\t");
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
                      out.write("\t\t\t");
                      //  c:otherwise
                      org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f5 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                      _jspx_th_c_005fotherwise_005f5.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fotherwise_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f6);
                      int _jspx_eval_c_005fotherwise_005f5 = _jspx_th_c_005fotherwise_005f5.doStartTag();
                      if (_jspx_eval_c_005fotherwise_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t<div style=\"float: left; margin: 100px 10px 0px;\">\n");
                          out.write("\t\t\t\t\t<img alt=\"");
                          if (_jspx_meth_liferay_002dui_005fmessage_005f6(_jspx_th_c_005fotherwise_005f5, _jspx_page_context))
                            return;
                          out.write("\" border=\"no\" src=\"");
                          out.print( themeDisplay.getPathThemeImages() );
                          out.write("/application/forbidden_action.png\" />\n");
                          out.write("\t\t\t\t</div>\n");
                          out.write("\t\t\t");
                          int evalDoAfterBody = _jspx_th_c_005fotherwise_005f5.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                      }
                      if (_jspx_th_c_005fotherwise_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f5);
                        return;
                      }
                      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f5);
                      out.write('\n');
                      out.write('	');
                      out.write('	');
                      int evalDoAfterBody = _jspx_th_c_005fchoose_005f6.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fchoose_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f6);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f6);
                  out.write('\n');
                  out.write('\n');
                  out.write('	');

	}
	
                  out.write("\n");
                  out.write("\n");
                  out.write("</div>\n");
                  out.write("\n");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f25 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f25.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005flayout_005f1);
                  // /html/portlet/image_gallery/view_images.jspf(123,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f25.setTest( !results.isEmpty() );
                  int _jspx_eval_c_005fif_005f25 = _jspx_th_c_005fif_005f25.doStartTag();
                  if (_jspx_eval_c_005fif_005f25 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t<div class=\"taglib-search-iterator-page-iterator-bottom\">\n");
                      out.write("\t\t");
                      //  liferay-ui:search-paginator
                      com.liferay.taglib.ui.SearchPaginatorTag _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f5 = (com.liferay.taglib.ui.SearchPaginatorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.get(com.liferay.taglib.ui.SearchPaginatorTag.class);
                      _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f5.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f25);
                      // /html/portlet/image_gallery/view_images.jspf(125,2) name = searchContainer type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f5.setSearchContainer( searchContainer );
                      int _jspx_eval_liferay_002dui_005fsearch_002dpaginator_005f5 = _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f5.doStartTag();
                      if (_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f5);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f5);
                      out.write("\n");
                      out.write("\t</div>\n");
                      int evalDoAfterBody = _jspx_th_c_005fif_005f25.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fif_005f25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f25);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f25);
                  out.write('\n');
                  out.write('\n');
                  if (_jspx_meth_aui_005fscript_005f2(_jspx_th_aui_005flayout_005f1, _jspx_page_context))
                    return;
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_aui_005flayout_005f1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_aui_005flayout_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_aui_005flayout_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005flayout.reuse(_jspx_th_aui_005flayout_005f1);
                return;
              }
              _005fjspx_005ftagPool_005faui_005flayout.reuse(_jspx_th_aui_005flayout_005f1);
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

		PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(pageContext, topLink), currentURL);

		PortalUtil.setPageSubtitle(LanguageUtil.get(pageContext, topLink), request);
		
              out.write('\n');
              out.write('\n');
              out.write('	');
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

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f0 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f0.setParent(null);
    // /html/portlet/image_gallery/view.jsp(75,0) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f0.setPage("/html/portlet/image_gallery/top_links.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f0 = _jspx_th_liferay_002dutil_005finclude_005f0.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f0 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
    int _jspx_eval_portlet_005fnamespace_005f0 = _jspx_th_portlet_005fnamespace_005f0.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f1 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f1.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
    int _jspx_eval_portlet_005fnamespace_005f1 = _jspx_th_portlet_005fnamespace_005f1.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f1);
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
    // /html/portlet/image_gallery/image_action.jspf(36,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setName("struts_action");
    // /html/portlet/image_gallery/image_action.jspf(36,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setValue("/image_gallery/view_image");
    int _jspx_eval_portlet_005fparam_005f0 = _jspx_th_portlet_005fparam_005f0.doStartTag();
    if (_jspx_th_portlet_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
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
    // /html/portlet/image_gallery/image_action.jspf(49,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f3.setName("struts_action");
    // /html/portlet/image_gallery/image_action.jspf(49,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f3.setValue("/image_gallery/edit_image");
    int _jspx_eval_portlet_005fparam_005f3 = _jspx_th_portlet_005fparam_005f3.doStartTag();
    if (_jspx_th_portlet_005fparam_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f6 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f6.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
    // /html/portlet/image_gallery/image_action.jspf(76,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f6.setName("struts_action");
    // /html/portlet/image_gallery/image_action.jspf(76,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f6.setValue("/image_gallery/edit_image");
    int _jspx_eval_portlet_005fparam_005f6 = _jspx_th_portlet_005fparam_005f6.doStartTag();
    if (_jspx_th_portlet_005fparam_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f2 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f2.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
    int _jspx_eval_portlet_005fnamespace_005f2 = _jspx_th_portlet_005fnamespace_005f2.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f2);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
    // /html/portlet/image_gallery/view_images.jspf(112,15) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("image");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_aui_005fscript_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:script
    com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
    _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fscript_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
    // /html/portlet/image_gallery/view_images.jspf(129,0) name = use type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fscript_005f0.setUse("aui-image-viewer-gallery");
    int _jspx_eval_aui_005fscript_005f0 = _jspx_th_aui_005fscript_005f0.doStartTag();
    if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_005fscript_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_005fscript_005f0.doInitBody();
      }
      do {
        out.write("\n");
        out.write("\tvar viewportRegion = A.getDoc().get('viewportRegion');\n");
        out.write("\tvar maxHeight = (viewportRegion.height * 0.5);\n");
        out.write("\tvar maxWidth = (viewportRegion.width * 0.5);\n");
        out.write("\n");
        out.write("\tvar imageGallery = new A.ImageGallery(\n");
        out.write("\t\t{\n");
        out.write("\t\t\tafter: {\n");
        out.write("\t\t\t\trender: function(event) {\n");
        out.write("\t\t\t\t\tvar instance = this;\n");
        out.write("\t\t\t\t\tvar footerNode = instance.footerNode;\n");
        out.write("\n");
        out.write("\t\t\t\t\tinstance._actions = A.Node.create('<div class=\"lfr-image-gallery-actions\"></div>');\n");
        out.write("\n");
        out.write("\t\t\t\t\tif (footerNode) {\n");
        out.write("\t\t\t\t\t\tfooterNode.append(\n");
        out.write("\t\t\t\t\t\t\tinstance._actions\n");
        out.write("\t\t\t\t\t\t);\n");
        out.write("\t\t\t\t\t}\n");
        out.write("\t\t\t\t},\n");
        out.write("\t\t\t\trequest: function(event) {\n");
        out.write("\t\t\t\t\tvar instance = this;\n");
        out.write("\n");
        out.write("\t\t\t\t\tvar currentLink = instance.getCurrentLink();\n");
        out.write("\t\t\t\t\tvar largeImageId = currentLink.attr('largeImageId');\n");
        out.write("\t\t\t\t\tvar actions = instance._actions;\n");
        out.write("\n");
        out.write("\t\t\t\t\tif (actions) {\n");
        out.write("\t\t\t\t\t\tvar action = A.one('#");
        if (_jspx_meth_portlet_005fnamespace_005f3(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
          return true;
        out.write("buttonsContainer_' + largeImageId);\n");
        out.write("\n");
        out.write("\t\t\t\t\t\tactions.empty();\n");
        out.write("\n");
        out.write("\t\t\t\t\t\tactions.append(\n");
        out.write("\t\t\t\t\t\t\taction.clone().show()\n");
        out.write("\t\t\t\t\t\t);\n");
        out.write("\t\t\t\t\t}\n");
        out.write("\t\t\t\t}\n");
        out.write("\t\t\t},\n");
        out.write("\t\t\tdelay: 5000,\n");
        out.write("\t\t\tlinks: '#imageGalleryPanelContainer .image-thumbnail',\n");
        out.write("\t\t\tmaxHeight: maxHeight,\n");
        out.write("\t\t\tmaxWidth: maxWidth,\n");
        out.write("\t\t\tplayingLabel: '(");
        if (_jspx_meth_liferay_002dui_005fmessage_005f1(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
          return true;
        out.write(")'\n");
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
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f0);
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

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f1 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    // /html/portlet/image_gallery/view_images.jspf(171,19) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f1.setKey("playing");
    int _jspx_eval_liferay_002dui_005fmessage_005f1 = _jspx_th_liferay_002dui_005fmessage_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f10 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f10.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f2);
    // /html/portlet/image_gallery/view.jsp(136,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f10.setName("struts_action");
    // /html/portlet/image_gallery/view.jsp(136,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f10.setValue("/image_gallery/view");
    int _jspx_eval_portlet_005fparam_005f10 = _jspx_th_portlet_005fparam_005f10.doStartTag();
    if (_jspx_th_portlet_005fparam_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f10);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f1 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f0);
    // /html/portlet/image_gallery/view.jsp(180,7) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f1.setPage("/html/portlet/image_gallery/view_folders.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f1 = _jspx_th_liferay_002dutil_005finclude_005f1.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f1);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f4 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f4.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
    int _jspx_eval_portlet_005fnamespace_005f4 = _jspx_th_portlet_005fnamespace_005f4.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f4);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f5 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f5.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
    int _jspx_eval_portlet_005fnamespace_005f5 = _jspx_th_portlet_005fnamespace_005f5.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f5);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f12 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f12.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f3);
    // /html/portlet/image_gallery/image_action.jspf(36,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f12.setName("struts_action");
    // /html/portlet/image_gallery/image_action.jspf(36,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f12.setValue("/image_gallery/view_image");
    int _jspx_eval_portlet_005fparam_005f12 = _jspx_th_portlet_005fparam_005f12.doStartTag();
    if (_jspx_th_portlet_005fparam_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f12);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f15 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f15.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f4);
    // /html/portlet/image_gallery/image_action.jspf(49,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f15.setName("struts_action");
    // /html/portlet/image_gallery/image_action.jspf(49,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f15.setValue("/image_gallery/edit_image");
    int _jspx_eval_portlet_005fparam_005f15 = _jspx_th_portlet_005fparam_005f15.doStartTag();
    if (_jspx_th_portlet_005fparam_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f15);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f18(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f18 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f18.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
    // /html/portlet/image_gallery/image_action.jspf(76,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f18.setName("struts_action");
    // /html/portlet/image_gallery/image_action.jspf(76,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f18.setValue("/image_gallery/edit_image");
    int _jspx_eval_portlet_005fparam_005f18 = _jspx_th_portlet_005fparam_005f18.doStartTag();
    if (_jspx_th_portlet_005fparam_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f18);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f18);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f6 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f6.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
    int _jspx_eval_portlet_005fnamespace_005f6 = _jspx_th_portlet_005fnamespace_005f6.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f4 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f4.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f3);
    // /html/portlet/image_gallery/view_images.jspf(112,15) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f4.setKey("image");
    int _jspx_eval_liferay_002dui_005fmessage_005f4 = _jspx_th_liferay_002dui_005fmessage_005f4.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
    return false;
  }

  private boolean _jspx_meth_aui_005fscript_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:script
    com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f1 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
    _jspx_th_aui_005fscript_005f1.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fscript_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f1);
    // /html/portlet/image_gallery/view_images.jspf(129,0) name = use type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fscript_005f1.setUse("aui-image-viewer-gallery");
    int _jspx_eval_aui_005fscript_005f1 = _jspx_th_aui_005fscript_005f1.doStartTag();
    if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_005fscript_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_005fscript_005f1.doInitBody();
      }
      do {
        out.write("\n");
        out.write("\tvar viewportRegion = A.getDoc().get('viewportRegion');\n");
        out.write("\tvar maxHeight = (viewportRegion.height * 0.5);\n");
        out.write("\tvar maxWidth = (viewportRegion.width * 0.5);\n");
        out.write("\n");
        out.write("\tvar imageGallery = new A.ImageGallery(\n");
        out.write("\t\t{\n");
        out.write("\t\t\tafter: {\n");
        out.write("\t\t\t\trender: function(event) {\n");
        out.write("\t\t\t\t\tvar instance = this;\n");
        out.write("\t\t\t\t\tvar footerNode = instance.footerNode;\n");
        out.write("\n");
        out.write("\t\t\t\t\tinstance._actions = A.Node.create('<div class=\"lfr-image-gallery-actions\"></div>');\n");
        out.write("\n");
        out.write("\t\t\t\t\tif (footerNode) {\n");
        out.write("\t\t\t\t\t\tfooterNode.append(\n");
        out.write("\t\t\t\t\t\t\tinstance._actions\n");
        out.write("\t\t\t\t\t\t);\n");
        out.write("\t\t\t\t\t}\n");
        out.write("\t\t\t\t},\n");
        out.write("\t\t\t\trequest: function(event) {\n");
        out.write("\t\t\t\t\tvar instance = this;\n");
        out.write("\n");
        out.write("\t\t\t\t\tvar currentLink = instance.getCurrentLink();\n");
        out.write("\t\t\t\t\tvar largeImageId = currentLink.attr('largeImageId');\n");
        out.write("\t\t\t\t\tvar actions = instance._actions;\n");
        out.write("\n");
        out.write("\t\t\t\t\tif (actions) {\n");
        out.write("\t\t\t\t\t\tvar action = A.one('#");
        if (_jspx_meth_portlet_005fnamespace_005f7(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
          return true;
        out.write("buttonsContainer_' + largeImageId);\n");
        out.write("\n");
        out.write("\t\t\t\t\t\tactions.empty();\n");
        out.write("\n");
        out.write("\t\t\t\t\t\tactions.append(\n");
        out.write("\t\t\t\t\t\t\taction.clone().show()\n");
        out.write("\t\t\t\t\t\t);\n");
        out.write("\t\t\t\t\t}\n");
        out.write("\t\t\t\t}\n");
        out.write("\t\t\t},\n");
        out.write("\t\t\tdelay: 5000,\n");
        out.write("\t\t\tlinks: '#imageGalleryPanelContainer .image-thumbnail',\n");
        out.write("\t\t\tmaxHeight: maxHeight,\n");
        out.write("\t\t\tmaxWidth: maxWidth,\n");
        out.write("\t\t\tplayingLabel: '(");
        if (_jspx_meth_liferay_002dui_005fmessage_005f5(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
          return true;
        out.write(")'\n");
        out.write("\t\t}\n");
        out.write("\t).render();\n");
        int evalDoAfterBody = _jspx_th_aui_005fscript_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_aui_005fscript_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f1);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f7 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f7.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f7 = _jspx_th_portlet_005fnamespace_005f7.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f7);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f5 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f5.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    // /html/portlet/image_gallery/view_images.jspf(171,19) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f5.setKey("playing");
    int _jspx_eval_liferay_002dui_005fmessage_005f5 = _jspx_th_liferay_002dui_005fmessage_005f5.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fcolumn_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f2 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f2.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fcolumn_005f1);
    // /html/portlet/image_gallery/view.jsp(223,4) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f2.setPage("/html/portlet/image_gallery/folder_action.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f2 = _jspx_th_liferay_002dutil_005finclude_005f2.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f2);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f8, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f8 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f8.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f8);
    int _jspx_eval_portlet_005fnamespace_005f8 = _jspx_th_portlet_005fnamespace_005f8.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f8);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f8, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f9 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f9.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f8);
    int _jspx_eval_portlet_005fnamespace_005f9 = _jspx_th_portlet_005fnamespace_005f9.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f9);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f22(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f22 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f22.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f5);
    // /html/portlet/image_gallery/image_action.jspf(36,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f22.setName("struts_action");
    // /html/portlet/image_gallery/image_action.jspf(36,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f22.setValue("/image_gallery/view_image");
    int _jspx_eval_portlet_005fparam_005f22 = _jspx_th_portlet_005fparam_005f22.doStartTag();
    if (_jspx_th_portlet_005fparam_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f22);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f22);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f25(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f25 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f25.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f6);
    // /html/portlet/image_gallery/image_action.jspf(49,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f25.setName("struts_action");
    // /html/portlet/image_gallery/image_action.jspf(49,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f25.setValue("/image_gallery/edit_image");
    int _jspx_eval_portlet_005fparam_005f25 = _jspx_th_portlet_005fparam_005f25.doStartTag();
    if (_jspx_th_portlet_005fparam_005f25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f25);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f25);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f28(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f28 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f28.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f2);
    // /html/portlet/image_gallery/image_action.jspf(76,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f28.setName("struts_action");
    // /html/portlet/image_gallery/image_action.jspf(76,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f28.setValue("/image_gallery/edit_image");
    int _jspx_eval_portlet_005fparam_005f28 = _jspx_th_portlet_005fparam_005f28.doStartTag();
    if (_jspx_th_portlet_005fparam_005f28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f28);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f28);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f8, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f10 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f10.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f8);
    int _jspx_eval_portlet_005fnamespace_005f10 = _jspx_th_portlet_005fnamespace_005f10.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f10);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f6 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f6.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f5);
    // /html/portlet/image_gallery/view_images.jspf(112,15) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f6.setKey("image");
    int _jspx_eval_liferay_002dui_005fmessage_005f6 = _jspx_th_liferay_002dui_005fmessage_005f6.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
    return false;
  }

  private boolean _jspx_meth_aui_005fscript_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005flayout_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:script
    com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f2 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
    _jspx_th_aui_005fscript_005f2.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fscript_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005flayout_005f1);
    // /html/portlet/image_gallery/view_images.jspf(129,0) name = use type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fscript_005f2.setUse("aui-image-viewer-gallery");
    int _jspx_eval_aui_005fscript_005f2 = _jspx_th_aui_005fscript_005f2.doStartTag();
    if (_jspx_eval_aui_005fscript_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_005fscript_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_005fscript_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_005fscript_005f2.doInitBody();
      }
      do {
        out.write("\n");
        out.write("\tvar viewportRegion = A.getDoc().get('viewportRegion');\n");
        out.write("\tvar maxHeight = (viewportRegion.height * 0.5);\n");
        out.write("\tvar maxWidth = (viewportRegion.width * 0.5);\n");
        out.write("\n");
        out.write("\tvar imageGallery = new A.ImageGallery(\n");
        out.write("\t\t{\n");
        out.write("\t\t\tafter: {\n");
        out.write("\t\t\t\trender: function(event) {\n");
        out.write("\t\t\t\t\tvar instance = this;\n");
        out.write("\t\t\t\t\tvar footerNode = instance.footerNode;\n");
        out.write("\n");
        out.write("\t\t\t\t\tinstance._actions = A.Node.create('<div class=\"lfr-image-gallery-actions\"></div>');\n");
        out.write("\n");
        out.write("\t\t\t\t\tif (footerNode) {\n");
        out.write("\t\t\t\t\t\tfooterNode.append(\n");
        out.write("\t\t\t\t\t\t\tinstance._actions\n");
        out.write("\t\t\t\t\t\t);\n");
        out.write("\t\t\t\t\t}\n");
        out.write("\t\t\t\t},\n");
        out.write("\t\t\t\trequest: function(event) {\n");
        out.write("\t\t\t\t\tvar instance = this;\n");
        out.write("\n");
        out.write("\t\t\t\t\tvar currentLink = instance.getCurrentLink();\n");
        out.write("\t\t\t\t\tvar largeImageId = currentLink.attr('largeImageId');\n");
        out.write("\t\t\t\t\tvar actions = instance._actions;\n");
        out.write("\n");
        out.write("\t\t\t\t\tif (actions) {\n");
        out.write("\t\t\t\t\t\tvar action = A.one('#");
        if (_jspx_meth_portlet_005fnamespace_005f11(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
          return true;
        out.write("buttonsContainer_' + largeImageId);\n");
        out.write("\n");
        out.write("\t\t\t\t\t\tactions.empty();\n");
        out.write("\n");
        out.write("\t\t\t\t\t\tactions.append(\n");
        out.write("\t\t\t\t\t\t\taction.clone().show()\n");
        out.write("\t\t\t\t\t\t);\n");
        out.write("\t\t\t\t\t}\n");
        out.write("\t\t\t\t}\n");
        out.write("\t\t\t},\n");
        out.write("\t\t\tdelay: 5000,\n");
        out.write("\t\t\tlinks: '#imageGalleryPanelContainer .image-thumbnail',\n");
        out.write("\t\t\tmaxHeight: maxHeight,\n");
        out.write("\t\t\tmaxWidth: maxWidth,\n");
        out.write("\t\t\tplayingLabel: '(");
        if (_jspx_meth_liferay_002dui_005fmessage_005f7(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
          return true;
        out.write(")'\n");
        out.write("\t\t}\n");
        out.write("\t).render();\n");
        int evalDoAfterBody = _jspx_th_aui_005fscript_005f2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_aui_005fscript_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_aui_005fscript_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f2);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f11 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f11.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f11 = _jspx_th_portlet_005fnamespace_005f11.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f11);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f7 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f7.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    // /html/portlet/image_gallery/view_images.jspf(171,19) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f7.setKey("playing");
    int _jspx_eval_liferay_002dui_005fmessage_005f7 = _jspx_th_liferay_002dui_005fmessage_005f7.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
    return false;
  }
}
