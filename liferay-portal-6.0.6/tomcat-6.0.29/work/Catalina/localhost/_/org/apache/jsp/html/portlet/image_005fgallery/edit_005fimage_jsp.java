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

public final class edit_005fimage_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(27);
    _jspx_dependants.add("/html/portlet/image_gallery/init.jsp");
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
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fbackURL_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fwindowState_005fdoAsUserId;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_002dparams_0026_005fmodelName_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fform_0026_005fonSubmit_005fname_005fmethod_005fenctype_005faction;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fexception;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002derror_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffield_002dwrapper;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffieldset;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fa_0026_005fid_005fhref;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattributes_002davailable_0026_005fclassName;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_0026_005fmodelName_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_002drow;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fonClick_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005frenderURL;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fbackURL_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fwindowState_005fdoAsUserId = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_002dparams_0026_005fmodelName_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fform_0026_005fonSubmit_005fname_005fmethod_005fenctype_005faction = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fexception = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002derror_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffieldset = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fa_0026_005fid_005fhref = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattributes_002davailable_0026_005fclassName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_0026_005fmodelName_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_002drow = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fonClick_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005frenderURL = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fbackURL_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.release();
    _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fwindowState_005fdoAsUserId.release();
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_002dparams_0026_005fmodelName_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.release();
    _005fjspx_005ftagPool_005faui_005fform_0026_005fonSubmit_005fname_005fmethod_005fenctype_005faction.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fexception.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002derror_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper.release();
    _005fjspx_005ftagPool_005faui_005ffieldset.release();
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.release();
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.release();
    _005fjspx_005ftagPool_005faui_005fa_0026_005fid_005fhref.release();
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattributes_002davailable_0026_005fclassName.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_0026_005fmodelName_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fbutton_002drow.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fonClick_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fscript.release();
    _005fjspx_005ftagPool_005fportlet_005frenderURL.release();
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

String redirect = ParamUtil.getString(request, "redirect");

String referringPortletResource = ParamUtil.getString(request, "referringPortletResource");

String uploadProgressId = "igImageUploadProgress";

IGImage image = (IGImage)request.getAttribute(WebKeys.IMAGE_GALLERY_IMAGE);

long imageId = BeanParamUtil.getLong(image, request, "imageId");

long folderId = BeanParamUtil.getLong(image, request, "folderId");
String name = BeanParamUtil.getString(image, request, "name");

String extension = StringPool.BLANK;

if (image != null) {
	extension = StringPool.PERIOD + image.getImageType();
}

String assetTagNames = ParamUtil.getString(request, "assetTagNames");

IGFolder folder = null;
Image largeImage = null;

if (image != null) {
	folder = image.getFolder();
	largeImage = ImageLocalServiceUtil.getImage(image.getLargeImageId());
}

      out.write('\n');
      out.write('\n');
      //  liferay-ui:header
      com.liferay.taglib.ui.HeaderTag _jspx_th_liferay_002dui_005fheader_005f0 = (com.liferay.taglib.ui.HeaderTag) _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fbackURL_005fnobody.get(com.liferay.taglib.ui.HeaderTag.class);
      _jspx_th_liferay_002dui_005fheader_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dui_005fheader_005f0.setParent(null);
      // /html/portlet/image_gallery/edit_image.jsp(50,0) name = backURL type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dui_005fheader_005f0.setBackURL( redirect );
      // /html/portlet/image_gallery/edit_image.jsp(50,0) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dui_005fheader_005f0.setTitle( image != null ? image.getName() : "new-image" );
      int _jspx_eval_liferay_002dui_005fheader_005f0 = _jspx_th_liferay_002dui_005fheader_005f0.doStartTag();
      if (_jspx_th_liferay_002dui_005fheader_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fbackURL_005fnobody.reuse(_jspx_th_liferay_002dui_005fheader_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fliferay_002dui_005fheader_0026_005ftitle_005fbackURL_005fnobody.reuse(_jspx_th_liferay_002dui_005fheader_005f0);
      out.write('\n');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f0.setParent(null);
      // /html/portlet/image_gallery/edit_image.jsp(55,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f0.setTest( image == null );
      int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
      if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\n");
          out.write("\t<div class=\"lfr-dynamic-uploader\">\n");
          out.write("\t\t<div class=\"lfr-upload-container\" id=\"");
          if (_jspx_meth_portlet_005fnamespace_005f0(_jspx_th_c_005fif_005f0, _jspx_page_context))
            return;
          out.write("fileUpload\"></div>\n");
          out.write("\t</div>\n");
          out.write("\n");
          out.write("\t<div class=\"lfr-fallback aui-helper-hidden\" id=\"");
          if (_jspx_meth_portlet_005fnamespace_005f1(_jspx_th_c_005fif_005f0, _jspx_page_context))
            return;
          out.write("fallback\">\n");
          out.write("\n");
          out.write("\t");
          //  aui:script
          com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
          _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fscript_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
          // /html/portlet/image_gallery/edit_image.jsp(62,1) name = use type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fscript_005f0.setUse("liferay-upload");
          int _jspx_eval_aui_005fscript_005f0 = _jspx_th_aui_005fscript_005f0.doStartTag();
          if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_aui_005fscript_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_aui_005fscript_005f0.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\t\tnew Liferay.Upload(\n");
              out.write("\t\t\t{\n");
              out.write("\t\t\t\tallowedFileTypes: '");
              out.print( StringUtil.merge(PrefsPropsUtil.getStringArray(PropsKeys.IG_IMAGE_EXTENSIONS, StringPool.COMMA)) );
              out.write("',\n");
              out.write("\t\t\t\tcontainer: '#");
              if (_jspx_meth_portlet_005fnamespace_005f2(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
                return;
              out.write("fileUpload',\n");
              out.write("\t\t\t\tfileDescription: '");
              out.print( StringUtil.merge(PrefsPropsUtil.getStringArray(PropsKeys.IG_IMAGE_EXTENSIONS, StringPool.COMMA)) );
              out.write("',\n");
              out.write("\t\t\t\tfallbackContainer: '#");
              if (_jspx_meth_portlet_005fnamespace_005f3(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
                return;
              out.write("fallback',\n");
              out.write("\t\t\t\tmaxFileSize: ");
              out.print( PrefsPropsUtil.getLong(PropsKeys.IG_IMAGE_MAX_SIZE) );
              out.write(" / 1024,\n");
              out.write("\t\t\t\tnamespace: '");
              if (_jspx_meth_portlet_005fnamespace_005f4(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
                return;
              out.write("',\n");
              out.write("\t\t\t\tuploadFile: '");
              //  liferay-portlet:actionURL
              com.liferay.taglib.portlet.ActionURLTag _jspx_th_liferay_002dportlet_005factionURL_005f0 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fwindowState_005fdoAsUserId.get(com.liferay.taglib.portlet.ActionURLTag.class);
              _jspx_th_liferay_002dportlet_005factionURL_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dportlet_005factionURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
              // /html/portlet/image_gallery/edit_image.jsp(71,17) name = windowState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dportlet_005factionURL_005f0.setWindowState( LiferayWindowState.POP_UP.toString() );
              // /html/portlet/image_gallery/edit_image.jsp(71,17) name = doAsUserId type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dportlet_005factionURL_005f0.setDoAsUserId( user.getUserId() );
              int _jspx_eval_liferay_002dportlet_005factionURL_005f0 = _jspx_th_liferay_002dportlet_005factionURL_005f0.doStartTag();
              if (_jspx_eval_liferay_002dportlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_liferay_002dportlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_liferay_002dportlet_005factionURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_liferay_002dportlet_005factionURL_005f0.doInitBody();
                }
                do {
                  if (_jspx_meth_portlet_005fparam_005f0(_jspx_th_liferay_002dportlet_005factionURL_005f0, _jspx_page_context))
                    return;
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f1 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005factionURL_005f0);
                  // /html/portlet/image_gallery/edit_image.jsp(71,211) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f1.setName( Constants.CMD );
                  // /html/portlet/image_gallery/edit_image.jsp(71,211) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f1.setValue( Constants.ADD );
                  int _jspx_eval_portlet_005fparam_005f1 = _jspx_th_portlet_005fparam_005f1.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f2 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005factionURL_005f0);
                  // /html/portlet/image_gallery/edit_image.jsp(71,285) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f2.setName("folderId");
                  // /html/portlet/image_gallery/edit_image.jsp(71,285) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f2.setValue( String.valueOf(folderId) );
                  int _jspx_eval_portlet_005fparam_005f2 = _jspx_th_portlet_005fparam_005f2.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                  int evalDoAfterBody = _jspx_th_liferay_002dportlet_005factionURL_005f0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_liferay_002dportlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_liferay_002dportlet_005factionURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fwindowState_005fdoAsUserId.reuse(_jspx_th_liferay_002dportlet_005factionURL_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dportlet_005factionURL_0026_005fwindowState_005fdoAsUserId.reuse(_jspx_th_liferay_002dportlet_005factionURL_005f0);
              //  liferay-ui:input-permissions-params
              com.liferay.taglib.ui.InputPermissionsParamsTag _jspx_th_liferay_002dui_005finput_002dpermissions_002dparams_005f0 = (com.liferay.taglib.ui.InputPermissionsParamsTag) _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_002dparams_0026_005fmodelName_005fnobody.get(com.liferay.taglib.ui.InputPermissionsParamsTag.class);
              _jspx_th_liferay_002dui_005finput_002dpermissions_002dparams_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005finput_002dpermissions_002dparams_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
              // /html/portlet/image_gallery/edit_image.jsp(71,386) name = modelName type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005finput_002dpermissions_002dparams_005f0.setModelName( IGImage.class.getName() );
              int _jspx_eval_liferay_002dui_005finput_002dpermissions_002dparams_005f0 = _jspx_th_liferay_002dui_005finput_002dpermissions_002dparams_005f0.doStartTag();
              if (_jspx_th_liferay_002dui_005finput_002dpermissions_002dparams_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_002dparams_0026_005fmodelName_005fnobody.reuse(_jspx_th_liferay_002dui_005finput_002dpermissions_002dparams_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_002dparams_0026_005fmodelName_005fnobody.reuse(_jspx_th_liferay_002dui_005finput_002dpermissions_002dparams_005f0);
              out.write("'\n");
              out.write("\t\t\t}\n");
              out.write("\t\t);\n");
              out.write("\t");
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
          out.write('\n');
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
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f1.setParent(null);
      // /html/portlet/image_gallery/edit_image.jsp(77,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f1.setTest( Validator.isNull(referringPortletResource) );
      int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
      if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('\n');
          out.write('	');
          if (_jspx_meth_liferay_002dutil_005finclude_005f0(_jspx_th_c_005fif_005f1, _jspx_page_context))
            return;
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
      //  portlet:actionURL
      com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f0 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.get(com.liferay.taglib.portlet.ActionURLTag.class);
      _jspx_th_portlet_005factionURL_005f0.setPageContext(_jspx_page_context);
      _jspx_th_portlet_005factionURL_005f0.setParent(null);
      // /html/portlet/image_gallery/edit_image.jsp(81,0) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_portlet_005factionURL_005f0.setVar("editImageURL");
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
          if (_jspx_meth_portlet_005fparam_005f3(_jspx_th_portlet_005factionURL_005f0, _jspx_page_context))
            return;
          out.write('\n');
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
      java.lang.String editImageURL = null;
      editImageURL = (java.lang.String) _jspx_page_context.findAttribute("editImageURL");
      out.write('\n');
      out.write('\n');
      //  aui:form
      com.liferay.taglib.aui.FormTag _jspx_th_aui_005fform_005f0 = (com.liferay.taglib.aui.FormTag) _005fjspx_005ftagPool_005faui_005fform_0026_005fonSubmit_005fname_005fmethod_005fenctype_005faction.get(com.liferay.taglib.aui.FormTag.class);
      _jspx_th_aui_005fform_005f0.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fform_005f0.setParent(null);
      // /html/portlet/image_gallery/edit_image.jsp(85,0) name = action type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fform_005f0.setAction( editImageURL );
      // /html/portlet/image_gallery/edit_image.jsp(85,0) null
      _jspx_th_aui_005fform_005f0.setDynamicAttribute(null, "enctype", new String("multipart/form-data"));
      // /html/portlet/image_gallery/edit_image.jsp(85,0) null
      _jspx_th_aui_005fform_005f0.setDynamicAttribute(null, "method", new String("post"));
      // /html/portlet/image_gallery/edit_image.jsp(85,0) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fform_005f0.setName("fm");
      // /html/portlet/image_gallery/edit_image.jsp(85,0) name = onSubmit type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fform_005f0.setOnSubmit( "event.preventDefault(); " + renderResponse.getNamespace() + "saveImage();" );
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
          // /html/portlet/image_gallery/edit_image.jsp(86,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f0.setName( Constants.CMD );
          // /html/portlet/image_gallery/edit_image.jsp(86,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
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
          // /html/portlet/image_gallery/edit_image.jsp(87,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f1.setName("redirect");
          // /html/portlet/image_gallery/edit_image.jsp(87,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f1.setType("hidden");
          // /html/portlet/image_gallery/edit_image.jsp(87,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f1.setValue( redirect );
          int _jspx_eval_aui_005finput_005f1 = _jspx_th_aui_005finput_005f1.doStartTag();
          if (_jspx_th_aui_005finput_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f2 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f2.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /html/portlet/image_gallery/edit_image.jsp(88,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f2.setName("referringPortletResource");
          // /html/portlet/image_gallery/edit_image.jsp(88,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f2.setType("hidden");
          // /html/portlet/image_gallery/edit_image.jsp(88,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f2.setValue( referringPortletResource );
          int _jspx_eval_aui_005finput_005f2 = _jspx_th_aui_005finput_005f2.doStartTag();
          if (_jspx_th_aui_005finput_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f2);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f2);
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f3 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f3.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /html/portlet/image_gallery/edit_image.jsp(89,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f3.setName("uploadProgressId");
          // /html/portlet/image_gallery/edit_image.jsp(89,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f3.setType("hidden");
          // /html/portlet/image_gallery/edit_image.jsp(89,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f3.setValue( uploadProgressId );
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
          _jspx_th_aui_005finput_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /html/portlet/image_gallery/edit_image.jsp(90,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f4.setName("imageId");
          // /html/portlet/image_gallery/edit_image.jsp(90,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f4.setType("hidden");
          // /html/portlet/image_gallery/edit_image.jsp(90,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f4.setValue( imageId );
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
          _jspx_th_aui_005finput_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /html/portlet/image_gallery/edit_image.jsp(91,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f5.setName("folderId");
          // /html/portlet/image_gallery/edit_image.jsp(91,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f5.setType("hidden");
          // /html/portlet/image_gallery/edit_image.jsp(91,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f5.setValue( folderId );
          int _jspx_eval_aui_005finput_005f5 = _jspx_th_aui_005finput_005f5.doStartTag();
          if (_jspx_th_aui_005finput_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f5);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f5);
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  liferay-ui:error
          com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f0 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
          _jspx_th_liferay_002dui_005ferror_005f0.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005ferror_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /html/portlet/image_gallery/edit_image.jsp(93,1) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f0.setException( DuplicateImageNameException.class );
          // /html/portlet/image_gallery/edit_image.jsp(93,1) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f0.setMessage("please-enter-a-unique-image-name");
          int _jspx_eval_liferay_002dui_005ferror_005f0 = _jspx_th_liferay_002dui_005ferror_005f0.doStartTag();
          if (_jspx_th_liferay_002dui_005ferror_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f0);
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  liferay-ui:error
          com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f1 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fexception.get(com.liferay.taglib.ui.ErrorTag.class);
          _jspx_th_liferay_002dui_005ferror_005f1.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005ferror_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /html/portlet/image_gallery/edit_image.jsp(95,1) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f1.setException( ImageNameException.class );
          int _jspx_eval_liferay_002dui_005ferror_005f1 = _jspx_th_liferay_002dui_005ferror_005f1.doStartTag();
          if (_jspx_eval_liferay_002dui_005ferror_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            java.lang.Object errorException = null;
            errorException = (java.lang.Object) _jspx_page_context.findAttribute("errorException");
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_th_liferay_002dui_005ferror_005f1, _jspx_page_context))
                return;
              out.write(' ');
              out.print( StringUtil.merge(PrefsPropsUtil.getStringArray(PropsKeys.IG_IMAGE_EXTENSIONS, StringPool.COMMA), StringPool.COMMA_AND_SPACE) );
              out.write('.');
              out.write('\n');
              out.write('	');
              int evalDoAfterBody = _jspx_th_liferay_002dui_005ferror_005f1.doAfterBody();
              errorException = (java.lang.Object) _jspx_page_context.findAttribute("errorException");
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_liferay_002dui_005ferror_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fexception.reuse(_jspx_th_liferay_002dui_005ferror_005f1);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fexception.reuse(_jspx_th_liferay_002dui_005ferror_005f1);
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  liferay-ui:error
          com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f2 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
          _jspx_th_liferay_002dui_005ferror_005f2.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005ferror_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /html/portlet/image_gallery/edit_image.jsp(99,1) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f2.setException( ImageSizeException.class );
          // /html/portlet/image_gallery/edit_image.jsp(99,1) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f2.setMessage("please-enter-a-file-with-a-valid-file-size");
          int _jspx_eval_liferay_002dui_005ferror_005f2 = _jspx_th_liferay_002dui_005ferror_005f2.doStartTag();
          if (_jspx_th_liferay_002dui_005ferror_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f2);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f2);
          out.write('\n');
          out.write('	');
          //  liferay-ui:error
          com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f3 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
          _jspx_th_liferay_002dui_005ferror_005f3.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005ferror_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /html/portlet/image_gallery/edit_image.jsp(100,1) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f3.setException( NoSuchFolderException.class );
          // /html/portlet/image_gallery/edit_image.jsp(100,1) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f3.setMessage("please-enter-a-valid-folder");
          int _jspx_eval_liferay_002dui_005ferror_005f3 = _jspx_th_liferay_002dui_005ferror_005f3.doStartTag();
          if (_jspx_th_liferay_002dui_005ferror_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f3);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f3);
          out.write('\n');
          out.write('\n');
          out.write('	');
          if (_jspx_meth_liferay_002dui_005fasset_002dtags_002derror_005f0(_jspx_th_aui_005fform_005f0, _jspx_page_context))
            return;
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  aui:model-context
          com.liferay.taglib.aui.ModelContextTag _jspx_th_aui_005fmodel_002dcontext_005f0 = (com.liferay.taglib.aui.ModelContextTag) _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody.get(com.liferay.taglib.aui.ModelContextTag.class);
          _jspx_th_aui_005fmodel_002dcontext_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fmodel_002dcontext_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          // /html/portlet/image_gallery/edit_image.jsp(104,1) name = bean type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fmodel_002dcontext_005f0.setBean( image );
          // /html/portlet/image_gallery/edit_image.jsp(104,1) name = model type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fmodel_002dcontext_005f0.setModel( IGImage.class );
          int _jspx_eval_aui_005fmodel_002dcontext_005f0 = _jspx_th_aui_005fmodel_002dcontext_005f0.doStartTag();
          if (_jspx_th_aui_005fmodel_002dcontext_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody.reuse(_jspx_th_aui_005fmodel_002dcontext_005f0);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody.reuse(_jspx_th_aui_005fmodel_002dcontext_005f0);
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  aui:field-wrapper
          com.liferay.taglib.aui.FieldWrapperTag _jspx_th_aui_005ffield_002dwrapper_005f0 = (com.liferay.taglib.aui.FieldWrapperTag) _005fjspx_005ftagPool_005faui_005ffield_002dwrapper.get(com.liferay.taglib.aui.FieldWrapperTag.class);
          _jspx_th_aui_005ffield_002dwrapper_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005ffield_002dwrapper_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
          int _jspx_eval_aui_005ffield_002dwrapper_005f0 = _jspx_th_aui_005ffield_002dwrapper_005f0.doStartTag();
          if (_jspx_eval_aui_005ffield_002dwrapper_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_aui_005ffield_002dwrapper_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_aui_005ffield_002dwrapper_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_aui_005ffield_002dwrapper_005f0.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

		long imageMaxSize = PrefsPropsUtil.getLong(PropsKeys.IG_IMAGE_MAX_SIZE) / 1024;
		
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f0);
              // /html/portlet/image_gallery/edit_image.jsp(112,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f2.setTest( imageMaxSize != 0 );
              int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
              if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t<div class=\"portlet-msg-info\">\n");
                  out.write("\t\t\t\t");
                  out.print( LanguageUtil.format(pageContext, "upload-images-no-larger-than-x-k", String.valueOf(imageMaxSize), false) );
                  out.write("\n");
                  out.write("\t\t\t</div>\n");
                  out.write("\t\t");
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
              int evalDoAfterBody = _jspx_th_aui_005ffield_002dwrapper_005f0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_aui_005ffield_002dwrapper_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_aui_005ffield_002dwrapper_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005ffield_002dwrapper.reuse(_jspx_th_aui_005ffield_002dwrapper_005f0);
            return;
          }
          _005fjspx_005ftagPool_005faui_005ffield_002dwrapper.reuse(_jspx_th_aui_005ffield_002dwrapper_005f0);
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  aui:fieldset
          com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f0 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset.get(com.liferay.taglib.aui.FieldsetTag.class);
          _jspx_th_aui_005ffieldset_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005ffieldset_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
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
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
              // /html/portlet/image_gallery/edit_image.jsp(120,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f3.setTest( ((image != null) || (folderId <= 0) || Validator.isNotNull(referringPortletResource)) );
              int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
              if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:field-wrapper
                  com.liferay.taglib.aui.FieldWrapperTag _jspx_th_aui_005ffield_002dwrapper_005f1 = (com.liferay.taglib.aui.FieldWrapperTag) _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.get(com.liferay.taglib.aui.FieldWrapperTag.class);
                  _jspx_th_aui_005ffield_002dwrapper_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005ffield_002dwrapper_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
                  // /html/portlet/image_gallery/edit_image.jsp(121,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005ffield_002dwrapper_005f1.setLabel("folder");
                  int _jspx_eval_aui_005ffield_002dwrapper_005f1 = _jspx_th_aui_005ffield_002dwrapper_005f1.doStartTag();
                  if (_jspx_eval_aui_005ffield_002dwrapper_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005ffield_002dwrapper_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005ffield_002dwrapper_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005ffield_002dwrapper_005f1.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t");

					String folderName = StringPool.BLANK;

					if (folderId > 0) {
						folder = IGFolderLocalServiceUtil.getFolder(folderId);

						folder = folder.toEscapedModel();

						folderId = folder.getFolderId();
						folderName = folder.getName();
					}
					
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t");
                      //  portlet:renderURL
                      com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f0 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                      _jspx_th_portlet_005frenderURL_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005frenderURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f1);
                      // /html/portlet/image_gallery/edit_image.jsp(135,5) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005frenderURL_005f0.setVar("viewFolderURL");
                      int _jspx_eval_portlet_005frenderURL_005f0 = _jspx_th_portlet_005frenderURL_005f0.doStartTag();
                      if (_jspx_eval_portlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_portlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_portlet_005frenderURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_portlet_005frenderURL_005f0.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t");
                          if (_jspx_meth_portlet_005fparam_005f4(_jspx_th_portlet_005frenderURL_005f0, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\t\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f5 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f5.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
                          // /html/portlet/image_gallery/edit_image.jsp(137,6) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f5.setName("folderId");
                          // /html/portlet/image_gallery/edit_image.jsp(137,6) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f5.setValue( String.valueOf(folderId) );
                          int _jspx_eval_portlet_005fparam_005f5 = _jspx_th_portlet_005fparam_005f5.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
                          out.write("\n");
                          out.write("\t\t\t\t\t");
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
                      java.lang.String viewFolderURL = null;
                      viewFolderURL = (java.lang.String) _jspx_page_context.findAttribute("viewFolderURL");
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t");
                      //  aui:a
                      com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f0 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005fid_005fhref.get(com.liferay.taglib.aui.ATag.class);
                      _jspx_th_aui_005fa_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005fa_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f1);
                      // /html/portlet/image_gallery/edit_image.jsp(140,5) name = href type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005fa_005f0.setHref( viewFolderURL );
                      // /html/portlet/image_gallery/edit_image.jsp(140,5) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005fa_005f0.setId("folderName");
                      int _jspx_eval_aui_005fa_005f0 = _jspx_th_aui_005fa_005f0.doStartTag();
                      if (_jspx_eval_aui_005fa_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_aui_005fa_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_aui_005fa_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_aui_005fa_005f0.doInitBody();
                        }
                        do {
                          out.print( folderName );
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
                      out.write("\n");
                      out.write("\t\t\t\t\t");
                      //  portlet:renderURL
                      com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f1 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                      _jspx_th_portlet_005frenderURL_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005frenderURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f1);
                      // /html/portlet/image_gallery/edit_image.jsp(142,5) name = windowState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005frenderURL_005f1.setWindowState( LiferayWindowState.POP_UP.toString() );
                      // /html/portlet/image_gallery/edit_image.jsp(142,5) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005frenderURL_005f1.setVar("selectFolderURL");
                      int _jspx_eval_portlet_005frenderURL_005f1 = _jspx_th_portlet_005frenderURL_005f1.doStartTag();
                      if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_portlet_005frenderURL_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_portlet_005frenderURL_005f1.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t");
                          if (_jspx_meth_portlet_005fparam_005f6(_jspx_th_portlet_005frenderURL_005f1, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\t\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f7 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f7.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
                          // /html/portlet/image_gallery/edit_image.jsp(144,6) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f7.setName("folderId");
                          // /html/portlet/image_gallery/edit_image.jsp(144,6) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f7.setValue( String.valueOf(folderId) );
                          int _jspx_eval_portlet_005fparam_005f7 = _jspx_th_portlet_005fparam_005f7.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f1.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.popBody();
                        }
                      }
                      if (_jspx_th_portlet_005frenderURL_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f1);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f1);
                      java.lang.String selectFolderURL = null;
                      selectFolderURL = (java.lang.String) _jspx_page_context.findAttribute("selectFolderURL");
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t");

					String taglibOpenFolderWindow = "var folderWindow = window.open('" + selectFolderURL + "','folder', 'directories=no,height=640,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,toolbar=no,width=680'); void(''); folderWindow.focus();";
					
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t");
                      //  aui:button
                      com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f0 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                      _jspx_th_aui_005fbutton_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005fbutton_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f1);
                      // /html/portlet/image_gallery/edit_image.jsp(151,5) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005fbutton_005f0.setOnClick( taglibOpenFolderWindow );
                      // /html/portlet/image_gallery/edit_image.jsp(151,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005fbutton_005f0.setValue("select");
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
                      com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f1 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fname_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                      _jspx_th_aui_005fbutton_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005fbutton_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f1);
                      // /html/portlet/image_gallery/edit_image.jsp(153,5) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005fbutton_005f1.setName("removeFolderButton");
                      // /html/portlet/image_gallery/edit_image.jsp(153,5) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005fbutton_005f1.setOnClick( renderResponse.getNamespace() + "removeFolder();" );
                      // /html/portlet/image_gallery/edit_image.jsp(153,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005fbutton_005f1.setValue("remove");
                      int _jspx_eval_aui_005fbutton_005f1 = _jspx_th_aui_005fbutton_005f1.doStartTag();
                      if (_jspx_th_aui_005fbutton_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fname_005fnobody.reuse(_jspx_th_aui_005fbutton_005f1);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fname_005fnobody.reuse(_jspx_th_aui_005fbutton_005f1);
                      out.write("\n");
                      out.write("\t\t\t");
                      int evalDoAfterBody = _jspx_th_aui_005ffield_002dwrapper_005f1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_aui_005ffield_002dwrapper_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_aui_005ffield_002dwrapper_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.reuse(_jspx_th_aui_005ffield_002dwrapper_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.reuse(_jspx_th_aui_005ffield_002dwrapper_005f1);
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
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              if (_jspx_meth_aui_005finput_005f6(_jspx_th_aui_005ffieldset_005f0, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              if (_jspx_meth_aui_005finput_005f7(_jspx_th_aui_005ffieldset_005f0, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              if (_jspx_meth_aui_005finput_005f8(_jspx_th_aui_005ffieldset_005f0, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  liferay-ui:custom-attributes-available
              com.liferay.taglib.ui.CustomAttributesAvailableTag _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0 = (com.liferay.taglib.ui.CustomAttributesAvailableTag) _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattributes_002davailable_0026_005fclassName.get(com.liferay.taglib.ui.CustomAttributesAvailableTag.class);
              _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
              // /html/portlet/image_gallery/edit_image.jsp(163,2) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.setClassName( IGImage.class.getName() );
              int _jspx_eval_liferay_002dui_005fcustom_002dattributes_002davailable_005f0 = _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.doStartTag();
              if (_jspx_eval_liferay_002dui_005fcustom_002dattributes_002davailable_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_liferay_002dui_005fcustom_002dattributes_002davailable_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  liferay-ui:custom-attribute-list
                  com.liferay.taglib.ui.CustomAttributeListTag _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0 = (com.liferay.taglib.ui.CustomAttributeListTag) _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody.get(com.liferay.taglib.ui.CustomAttributeListTag.class);
                  _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0);
                  // /html/portlet/image_gallery/edit_image.jsp(164,3) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setClassName( IGImage.class.getName() );
                  // /html/portlet/image_gallery/edit_image.jsp(164,3) name = classPK type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setClassPK( imageId );
                  // /html/portlet/image_gallery/edit_image.jsp(164,3) name = editable type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setEditable( true );
                  // /html/portlet/image_gallery/edit_image.jsp(164,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setLabel( true );
                  int _jspx_eval_liferay_002dui_005fcustom_002dattribute_002dlist_005f0 = _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.doStartTag();
                  if (_jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
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
              out.write("\n");
              out.write("\t\t");
              if (_jspx_meth_aui_005finput_005f9(_jspx_th_aui_005ffieldset_005f0, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              if (_jspx_meth_aui_005finput_005f10(_jspx_th_aui_005ffieldset_005f0, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
              // /html/portlet/image_gallery/edit_image.jsp(176,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f4.setTest( image == null );
              int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
              if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  aui:field-wrapper
                  com.liferay.taglib.aui.FieldWrapperTag _jspx_th_aui_005ffield_002dwrapper_005f2 = (com.liferay.taglib.aui.FieldWrapperTag) _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.get(com.liferay.taglib.aui.FieldWrapperTag.class);
                  _jspx_th_aui_005ffield_002dwrapper_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005ffield_002dwrapper_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
                  // /html/portlet/image_gallery/edit_image.jsp(177,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005ffield_002dwrapper_005f2.setLabel("permissions");
                  int _jspx_eval_aui_005ffield_002dwrapper_005f2 = _jspx_th_aui_005ffield_002dwrapper_005f2.doStartTag();
                  if (_jspx_eval_aui_005ffield_002dwrapper_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005ffield_002dwrapper_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005ffield_002dwrapper_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005ffield_002dwrapper_005f2.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  liferay-ui:input-permissions
                      com.liferay.taglib.ui.InputPermissionsTag _jspx_th_liferay_002dui_005finput_002dpermissions_005f0 = (com.liferay.taglib.ui.InputPermissionsTag) _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_0026_005fmodelName_005fnobody.get(com.liferay.taglib.ui.InputPermissionsTag.class);
                      _jspx_th_liferay_002dui_005finput_002dpermissions_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005finput_002dpermissions_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f2);
                      // /html/portlet/image_gallery/edit_image.jsp(178,4) name = modelName type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005finput_002dpermissions_005f0.setModelName( IGImage.class.getName() );
                      int _jspx_eval_liferay_002dui_005finput_002dpermissions_005f0 = _jspx_th_liferay_002dui_005finput_002dpermissions_005f0.doStartTag();
                      if (_jspx_th_liferay_002dui_005finput_002dpermissions_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_0026_005fmodelName_005fnobody.reuse(_jspx_th_liferay_002dui_005finput_002dpermissions_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_0026_005fmodelName_005fnobody.reuse(_jspx_th_liferay_002dui_005finput_002dpermissions_005f0);
                      out.write("\n");
                      out.write("\t\t\t");
                      int evalDoAfterBody = _jspx_th_aui_005ffield_002dwrapper_005f2.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_aui_005ffield_002dwrapper_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_aui_005ffield_002dwrapper_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.reuse(_jspx_th_aui_005ffield_002dwrapper_005f2);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.reuse(_jspx_th_aui_005ffield_002dwrapper_005f2);
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
          out.write('\n');
          out.write('	');
          //  aui:button-row
          com.liferay.taglib.aui.ButtonRowTag _jspx_th_aui_005fbutton_002drow_005f0 = (com.liferay.taglib.aui.ButtonRowTag) _005fjspx_005ftagPool_005faui_005fbutton_002drow.get(com.liferay.taglib.aui.ButtonRowTag.class);
          _jspx_th_aui_005fbutton_002drow_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fbutton_002drow_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
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
              if (_jspx_meth_aui_005fbutton_005f2(_jspx_th_aui_005fbutton_002drow_005f0, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  aui:button
              com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f3 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
              _jspx_th_aui_005fbutton_005f3.setPageContext(_jspx_page_context);
              _jspx_th_aui_005fbutton_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f0);
              // /html/portlet/image_gallery/edit_image.jsp(188,2) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005fbutton_005f3.setOnClick( redirect );
              // /html/portlet/image_gallery/edit_image.jsp(188,2) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005fbutton_005f3.setType("cancel");
              int _jspx_eval_aui_005fbutton_005f3 = _jspx_th_aui_005fbutton_005f3.doStartTag();
              if (_jspx_th_aui_005fbutton_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f3);
                return;
              }
              _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f3);
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
            return;
          }
          _005fjspx_005ftagPool_005faui_005fbutton_002drow.reuse(_jspx_th_aui_005fbutton_002drow_005f0);
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
        _005fjspx_005ftagPool_005faui_005fform_0026_005fonSubmit_005fname_005fmethod_005fenctype_005faction.reuse(_jspx_th_aui_005fform_005f0);
        return;
      }
      _005fjspx_005ftagPool_005faui_005fform_0026_005fonSubmit_005fname_005fmethod_005fenctype_005faction.reuse(_jspx_th_aui_005fform_005f0);
      out.write('\n');
      out.write('\n');
      //  liferay-ui:upload-progress
      com.liferay.taglib.ui.UploadProgressTag _jspx_th_liferay_002dui_005fupload_002dprogress_005f0 = (com.liferay.taglib.ui.UploadProgressTag) _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody.get(com.liferay.taglib.ui.UploadProgressTag.class);
      _jspx_th_liferay_002dui_005fupload_002dprogress_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dui_005fupload_002dprogress_005f0.setParent(null);
      // /html/portlet/image_gallery/edit_image.jsp(192,0) name = id type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dui_005fupload_002dprogress_005f0.setId( uploadProgressId );
      // /html/portlet/image_gallery/edit_image.jsp(192,0) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dui_005fupload_002dprogress_005f0.setMessage("uploading");
      // /html/portlet/image_gallery/edit_image.jsp(192,0) name = redirect type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_liferay_002dui_005fupload_002dprogress_005f0.setRedirect( redirect );
      int _jspx_eval_liferay_002dui_005fupload_002dprogress_005f0 = _jspx_th_liferay_002dui_005fupload_002dprogress_005f0.doStartTag();
      if (_jspx_th_liferay_002dui_005fupload_002dprogress_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody.reuse(_jspx_th_liferay_002dui_005fupload_002dprogress_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fliferay_002dui_005fupload_002dprogress_0026_005fredirect_005fmessage_005fid_005fnobody.reuse(_jspx_th_liferay_002dui_005fupload_002dprogress_005f0);
      out.write('\n');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f5 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f5.setParent(null);
      // /html/portlet/image_gallery/edit_image.jsp(198,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f5.setTest( image == null );
      int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
      if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\n");
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
      //  aui:script
      com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f1 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript.get(com.liferay.taglib.aui.ScriptTag.class);
      _jspx_th_aui_005fscript_005f1.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fscript_005f1.setParent(null);
      int _jspx_eval_aui_005fscript_005f1 = _jspx_th_aui_005fscript_005f1.doStartTag();
      if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_aui_005fscript_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_aui_005fscript_005f1.doInitBody();
        }
        do {
          out.write("\n");
          out.write("\tfunction ");
          if (_jspx_meth_portlet_005fnamespace_005f5(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
            return;
          out.write("removeFolder() {\n");
          out.write("\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f6(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f7(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
            return;
          out.write("folderId.value = \"");
          out.print( rootFolderId );
          out.write("\";\n");
          out.write("\n");
          out.write("\t\tvar nameEl = document.getElementById(\"");
          if (_jspx_meth_portlet_005fnamespace_005f8(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
            return;
          out.write("folderName\");\n");
          out.write("\n");
          out.write("\t\tnameEl.href = \"\";\n");
          out.write("\t\tnameEl.innerHTML = \"\";\n");
          out.write("\t}\n");
          out.write("\n");
          out.write("\tfunction ");
          if (_jspx_meth_portlet_005fnamespace_005f9(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
            return;
          out.write("saveImage() {\n");
          out.write("\t\t");
          out.print( HtmlUtil.escape(uploadProgressId) );
          out.write(".startProgress();\n");
          out.write("\n");
          out.write("\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f10(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f11(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
            return;
          out.print( Constants.CMD );
          out.write(".value = \"");
          out.print( (image == null) ? Constants.ADD : Constants.UPDATE );
          out.write("\";\n");
          out.write("\t\tsubmitForm(document.");
          if (_jspx_meth_portlet_005fnamespace_005f12(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
            return;
          out.write("fm);\n");
          out.write("\t}\n");
          out.write("\n");
          out.write("\tfunction ");
          if (_jspx_meth_portlet_005fnamespace_005f13(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
            return;
          out.write("selectFolder(folderId, folderName) {\n");
          out.write("\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f14(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
            return;
          out.write('f');
          out.write('m');
          out.write('.');
          if (_jspx_meth_portlet_005fnamespace_005f15(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
            return;
          out.write("folderId.value = folderId;\n");
          out.write("\n");
          out.write("\t\tvar nameEl = document.getElementById(\"");
          if (_jspx_meth_portlet_005fnamespace_005f16(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
            return;
          out.write("folderName\");\n");
          out.write("\n");
          out.write("\t\tnameEl.href = \"");
          if (_jspx_meth_portlet_005frenderURL_005f2(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
            return;
          out.write('&');
          if (_jspx_meth_portlet_005fnamespace_005f17(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
            return;
          out.write("folderId=\" + folderId;\n");
          out.write("\t\tnameEl.innerHTML = folderName + \"&nbsp;\";\n");
          out.write("\t}\n");
          out.write("\n");
          out.write("\t");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f6 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f6.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
          // /html/portlet/image_gallery/edit_image.jsp(228,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f6.setTest( windowState.equals(WindowState.MAXIMIZED) );
          int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
          if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\tLiferay.Util.focusFormField(document.");
              if (_jspx_meth_portlet_005fnamespace_005f18(_jspx_th_c_005fif_005f6, _jspx_page_context))
                return;
              out.write('f');
              out.write('m');
              out.write('.');
              if (_jspx_meth_portlet_005fnamespace_005f19(_jspx_th_c_005fif_005f6, _jspx_page_context))
                return;
              out.write("file);\n");
              out.write("\t");
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
      out.write('\n');
      //  aui:script
      com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f2 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
      _jspx_th_aui_005fscript_005f2.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fscript_005f2.setParent(null);
      // /html/portlet/image_gallery/edit_image.jsp(233,0) name = use type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fscript_005f2.setUse("aui-base");
      int _jspx_eval_aui_005fscript_005f2 = _jspx_th_aui_005fscript_005f2.doStartTag();
      if (_jspx_eval_aui_005fscript_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_aui_005fscript_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_aui_005fscript_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_aui_005fscript_005f2.doInitBody();
        }
        do {
          out.write("\n");
          out.write("\tvar validateFile = function(fileField) {\n");
          out.write("\t\tvar value = fileField.val();\n");
          out.write("\n");
          out.write("\t\tif (value) {\n");
          out.write("\t\t\tvar extension = value.substring(value.lastIndexOf('.')).toLowerCase();\n");
          out.write("\t\t\tvar validExtensions = ['");
          out.print( StringUtil.merge(PrefsPropsUtil.getStringArray(PropsKeys.IG_IMAGE_EXTENSIONS, StringPool.COMMA), "', '") );
          out.write("'];\n");
          out.write("\n");
          out.write("\t\t\tif ((A.Array.indexOf(validExtensions, '*') == -1) &&\n");
          out.write("\t\t\t\t(A.Array.indexOf(validExtensions, extension) == -1)) {\n");
          out.write("\n");
          out.write("\t\t\t\talert('");
          out.print( UnicodeLanguageUtil.get(pageContext, "image-names-must-end-with-one-of-the-following-extensions") );
          out.write(' ');
          out.print( StringUtil.merge(PrefsPropsUtil.getStringArray(PropsKeys.IG_IMAGE_EXTENSIONS, StringPool.COMMA), StringPool.COMMA_AND_SPACE) );
          out.write("');\n");
          out.write("\n");
          out.write("\t\t\t\tfileField.val('');\n");
          out.write("\t\t\t}\n");
          out.write("\t\t}\n");
          out.write("\t};\n");
          out.write("\n");
          out.write("\tvar onFileChange = function(event) {\n");
          out.write("\t\tvalidateFile(event.currentTarget);\n");
          out.write("\t};\n");
          out.write("\n");
          out.write("\tvar fileField = A.one('#");
          if (_jspx_meth_portlet_005fnamespace_005f20(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
            return;
          out.write("file')\n");
          out.write("\n");
          out.write("\tif (fileField) {\n");
          out.write("\t\tfileField.on('change', onFileChange);\n");
          out.write("\n");
          out.write("\t\tvalidateFile(fileField);\n");
          out.write("\t}\n");
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
        return;
      }
      _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f2);
      out.write('\n');
      out.write('\n');

if (image != null) {
	IGUtil.addPortletBreadcrumbEntries(image, request, renderResponse);

	PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(pageContext, "edit"), currentURL);
}
else {
	IGUtil.addPortletBreadcrumbEntries(folderId, request, renderResponse);

	PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(pageContext, "add-image"), currentURL);
}

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

  private boolean _jspx_meth_portlet_005fnamespace_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f0 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
    int _jspx_eval_portlet_005fnamespace_005f0 = _jspx_th_portlet_005fnamespace_005f0.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f1 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f1.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
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

  private boolean _jspx_meth_portlet_005fparam_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dportlet_005factionURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f0 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dportlet_005factionURL_005f0);
    // /html/portlet/image_gallery/edit_image.jsp(71,139) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setName("struts_action");
    // /html/portlet/image_gallery/edit_image.jsp(71,139) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setValue("/image_gallery/edit_image");
    int _jspx_eval_portlet_005fparam_005f0 = _jspx_th_portlet_005fparam_005f0.doStartTag();
    if (_jspx_th_portlet_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f0 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
    // /html/portlet/image_gallery/edit_image.jsp(78,1) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f0.setPage("/html/portlet/image_gallery/top_links.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f0 = _jspx_th_liferay_002dutil_005finclude_005f0.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f3 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f3.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
    // /html/portlet/image_gallery/edit_image.jsp(82,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f3.setName("struts_action");
    // /html/portlet/image_gallery/edit_image.jsp(82,1) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f3.setValue("/image_gallery/edit_image");
    int _jspx_eval_portlet_005fparam_005f3 = _jspx_th_portlet_005fparam_005f3.doStartTag();
    if (_jspx_th_portlet_005fparam_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005ferror_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ferror_005f1);
    // /html/portlet/image_gallery/edit_image.jsp(96,2) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("image-names-must-end-with-one-of-the-following-extensions");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fasset_002dtags_002derror_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:asset-tags-error
    com.liferay.taglib.ui.AssetTagsErrorTag _jspx_th_liferay_002dui_005fasset_002dtags_002derror_005f0 = (com.liferay.taglib.ui.AssetTagsErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002derror_005fnobody.get(com.liferay.taglib.ui.AssetTagsErrorTag.class);
    _jspx_th_liferay_002dui_005fasset_002dtags_002derror_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fasset_002dtags_002derror_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
    int _jspx_eval_liferay_002dui_005fasset_002dtags_002derror_005f0 = _jspx_th_liferay_002dui_005fasset_002dtags_002derror_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fasset_002dtags_002derror_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002derror_005fnobody.reuse(_jspx_th_liferay_002dui_005fasset_002dtags_002derror_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002derror_005fnobody.reuse(_jspx_th_liferay_002dui_005fasset_002dtags_002derror_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f4 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f4.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
    // /html/portlet/image_gallery/edit_image.jsp(136,6) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f4.setName("struts_action");
    // /html/portlet/image_gallery/edit_image.jsp(136,6) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f4.setValue("/image_gallery/view");
    int _jspx_eval_portlet_005fparam_005f4 = _jspx_th_portlet_005fparam_005f4.doStartTag();
    if (_jspx_th_portlet_005fparam_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f6 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f6.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
    // /html/portlet/image_gallery/edit_image.jsp(143,6) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f6.setName("struts_action");
    // /html/portlet/image_gallery/edit_image.jsp(143,6) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f6.setValue("/image_gallery/select_folder");
    int _jspx_eval_portlet_005fparam_005f6 = _jspx_th_portlet_005fparam_005f6.doStartTag();
    if (_jspx_th_portlet_005fparam_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f6 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f6.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
    // /html/portlet/image_gallery/edit_image.jsp(157,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f6.setName("file");
    // /html/portlet/image_gallery/edit_image.jsp(157,2) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f6.setType("file");
    int _jspx_eval_aui_005finput_005f6 = _jspx_th_aui_005finput_005f6.doStartTag();
    if (_jspx_th_aui_005finput_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f6);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f7 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f7.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
    // /html/portlet/image_gallery/edit_image.jsp(159,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f7.setName("name");
    int _jspx_eval_aui_005finput_005f7 = _jspx_th_aui_005finput_005f7.doStartTag();
    if (_jspx_th_aui_005finput_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f7);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f8 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f8.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
    // /html/portlet/image_gallery/edit_image.jsp(161,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f8.setName("description");
    int _jspx_eval_aui_005finput_005f8 = _jspx_th_aui_005finput_005f8.doStartTag();
    if (_jspx_th_aui_005finput_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f8);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f9 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f9.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
    // /html/portlet/image_gallery/edit_image.jsp(172,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f9.setName("categories");
    // /html/portlet/image_gallery/edit_image.jsp(172,2) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f9.setType("assetCategories");
    int _jspx_eval_aui_005finput_005f9 = _jspx_th_aui_005finput_005f9.doStartTag();
    if (_jspx_th_aui_005finput_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f9);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f10 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f10.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
    // /html/portlet/image_gallery/edit_image.jsp(174,2) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f10.setName("tags");
    // /html/portlet/image_gallery/edit_image.jsp(174,2) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f10.setType("assetTags");
    int _jspx_eval_aui_005finput_005f10 = _jspx_th_aui_005finput_005f10.doStartTag();
    if (_jspx_th_aui_005finput_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f10);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fbutton_002drow_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button
    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f2 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
    _jspx_th_aui_005fbutton_005f2.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f0);
    // /html/portlet/image_gallery/edit_image.jsp(186,2) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f2.setType("submit");
    int _jspx_eval_aui_005fbutton_005f2 = _jspx_th_aui_005fbutton_005f2.doStartTag();
    if (_jspx_th_aui_005fbutton_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody.reuse(_jspx_th_aui_005fbutton_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fnobody.reuse(_jspx_th_aui_005fbutton_005f2);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f5 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f5.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f5 = _jspx_th_portlet_005fnamespace_005f5.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f5);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f6 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f6.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f6 = _jspx_th_portlet_005fnamespace_005f6.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
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

  private boolean _jspx_meth_portlet_005fnamespace_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f8 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f8.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f8 = _jspx_th_portlet_005fnamespace_005f8.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f8);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f9 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f9.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f9 = _jspx_th_portlet_005fnamespace_005f9.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f9);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f10 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f10.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f10 = _jspx_th_portlet_005fnamespace_005f10.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f10);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f11 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f11.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f11 = _jspx_th_portlet_005fnamespace_005f11.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f11);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f12 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f12.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f12 = _jspx_th_portlet_005fnamespace_005f12.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f12);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f13 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f13.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f13 = _jspx_th_portlet_005fnamespace_005f13.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f13);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f14 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f14.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f14 = _jspx_th_portlet_005fnamespace_005f14.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f14);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f15 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f15.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f15 = _jspx_th_portlet_005fnamespace_005f15.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f15);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f16(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f16 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f16.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f16 = _jspx_th_portlet_005fnamespace_005f16.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f16);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f16);
    return false;
  }

  private boolean _jspx_meth_portlet_005frenderURL_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:renderURL
    com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f2 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL.get(com.liferay.taglib.portlet.RenderURLTag.class);
    _jspx_th_portlet_005frenderURL_005f2.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005frenderURL_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005frenderURL_005f2 = _jspx_th_portlet_005frenderURL_005f2.doStartTag();
    if (_jspx_eval_portlet_005frenderURL_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_portlet_005frenderURL_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_portlet_005frenderURL_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_portlet_005frenderURL_005f2.doInitBody();
      }
      do {
        if (_jspx_meth_portlet_005fparam_005f8(_jspx_th_portlet_005frenderURL_005f2, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_portlet_005frenderURL_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_portlet_005frenderURL_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005frenderURL.reuse(_jspx_th_portlet_005frenderURL_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005frenderURL.reuse(_jspx_th_portlet_005frenderURL_005f2);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f8 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f8.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f2);
    // /html/portlet/image_gallery/edit_image.jsp(224,36) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f8.setName("struts_action");
    // /html/portlet/image_gallery/edit_image.jsp(224,36) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f8.setValue("/image_gallery/view");
    int _jspx_eval_portlet_005fparam_005f8 = _jspx_th_portlet_005fparam_005f8.doStartTag();
    if (_jspx_th_portlet_005fparam_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f17(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f17 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f17.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f1);
    int _jspx_eval_portlet_005fnamespace_005f17 = _jspx_th_portlet_005fnamespace_005f17.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f17);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f17);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f18(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f18 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f18.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
    int _jspx_eval_portlet_005fnamespace_005f18 = _jspx_th_portlet_005fnamespace_005f18.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f18);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f18);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f19(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f19 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f19.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
    int _jspx_eval_portlet_005fnamespace_005f19 = _jspx_th_portlet_005fnamespace_005f19.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f19);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f19);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f20(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f20 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f20.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f20 = _jspx_th_portlet_005fnamespace_005f20.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f20);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f20);
    return false;
  }
}
