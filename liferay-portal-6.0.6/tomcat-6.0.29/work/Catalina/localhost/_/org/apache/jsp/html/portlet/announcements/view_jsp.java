package org.apache.jsp.html.portlet.announcements;

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
import com.liferay.portal.service.permission.OrganizationPermissionUtil;
import com.liferay.portal.service.permission.RolePermissionUtil;
import com.liferay.portal.service.permission.UserGroupPermissionUtil;
import com.liferay.portlet.PortletURLUtil;
import com.liferay.portlet.announcements.EntryContentException;
import com.liferay.portlet.announcements.EntryDisplayDateException;
import com.liferay.portlet.announcements.EntryExpirationDateException;
import com.liferay.portlet.announcements.EntryTitleException;
import com.liferay.portlet.announcements.EntryURLException;
import com.liferay.portlet.announcements.NoSuchEntryException;
import com.liferay.portlet.announcements.NoSuchFlagException;
import com.liferay.portlet.announcements.model.AnnouncementsEntry;
import com.liferay.portlet.announcements.model.AnnouncementsEntryConstants;
import com.liferay.portlet.announcements.model.AnnouncementsFlagConstants;
import com.liferay.portlet.announcements.service.AnnouncementsEntryLocalServiceUtil;
import com.liferay.portlet.announcements.service.AnnouncementsFlagLocalServiceUtil;
import com.liferay.portlet.announcements.service.permission.AnnouncementsEntryPermission;
import com.liferay.portlet.announcements.util.AnnouncementsUtil;

public final class view_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(35);
    _jspx_dependants.add("/html/portlet/announcements/init.jsp");
    _jspx_dependants.add("/html/portlet/init.jsp");
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/announcements/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/announcements/view_entries.jspf");
    _jspx_dependants.add("/html/portlet/announcements/entry_iterator.jspf");
    _jspx_dependants.add("/html/portlet/announcements/view_entry.jspf");
    _jspx_dependants.add("/html/portlet/announcements/entry_action.jspf");
    _jspx_dependants.add("/html/portlet/announcements/entry_scope.jspf");
    _jspx_dependants.add("/html/portlet/announcements/view_manage_entries.jspf");
    _jspx_dependants.add("/html/portlet/announcements/entry_select_scope.jspf");
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
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005ftype_005fsearchContainer_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffieldset;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fonChange_005fname_005fchangesContext;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005frenderURL;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005ftype_005fsearchContainer_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffieldset = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fonChange_005fname_005fchangesContext = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005frenderURL = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.release();
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fotherwise.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005ftype_005fsearchContainer_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fscript.release();
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.release();
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction.release();
    _005fjspx_005ftagPool_005faui_005ffieldset.release();
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fonChange_005fname_005fchangesContext.release();
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.release();
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

int delta = 3;

Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);

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

themeDisplay.setIncludeServiceJs(true);

String tabs1 = ParamUtil.getString(request, "tabs1", "entries");

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/announcements/view");
portletURL.setParameter("tabs1", tabs1);

      out.write('\n');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f0.setParent(null);
      // /html/portlet/announcements/view.jsp(30,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f0.setTest( !portletName.equals(PortletKeys.ALERTS) || (portletName.equals(PortletKeys.ALERTS) && PortletPermissionUtil.contains(permissionChecker, plid, PortletKeys.ANNOUNCEMENTS, ActionKeys.ADD_ENTRY)) );
      int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
      if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('\n');
          out.write('	');
          if (_jspx_meth_liferay_002dutil_005finclude_005f0(_jspx_th_c_005fif_005f0, _jspx_page_context))
            return;
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
          // /html/portlet/announcements/view.jsp(35,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fwhen_005f0.setTest( tabs1.equals("entries") );
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

LinkedHashMap<Long, long[]> scopes = AnnouncementsUtil.getAnnouncementScopes(user.getUserId());

SearchContainer searchContainer = new SearchContainer(renderRequest, null, null, "cur1", delta, portletURL, null, "no-entries-were-found");

List<AnnouncementsEntry> results = null;
int total = 0;

int flagValue = AnnouncementsFlagConstants.NOT_HIDDEN;

              out.write("\n");
              out.write("\n");
              out.write("<div class=\"unread-entries\">\n");
              out.write("\t");

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

total = AnnouncementsEntryLocalServiceUtil.getEntriesCount(user.getUserId(), scopes, portletName.equals(PortletKeys.ALERTS), flagValue);

searchContainer.setTotal(total);

results = AnnouncementsEntryLocalServiceUtil.getEntries(user.getUserId(), scopes, portletName.equals(PortletKeys.ALERTS), flagValue, searchContainer.getStart(), searchContainer.getEnd());

searchContainer.setResults(results);

for (int i = 0; i < results.size(); i++) {
	AnnouncementsEntry entry = results.get(i);

	String className = StringPool.BLANK;

	if (i == 0) {
		className += " first";
	}

	if ((i + 1) == results.size()) {
		className += " last";
	}

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

boolean readEntry = false;

try {
	AnnouncementsFlagLocalServiceUtil.getFlag(user.getUserId(), entry.getEntryId(), AnnouncementsFlagConstants.READ);

	readEntry = true;
}
catch (NoSuchFlagException nsfe1) {
	try {
		AnnouncementsFlagLocalServiceUtil.getFlag(user.getUserId(), entry.getEntryId(), AnnouncementsFlagConstants.HIDDEN);

		readEntry = true;
	}
	catch (NoSuchFlagException nsfe2) {
	}
}

if (readEntry) {
	className += " read";
}

if (entry.getPriority() > 0) {
	className += " important";
}

              out.write("\n");
              out.write("\n");
              out.write("<div class=\"entry");
              out.print( className );
              out.write("\" id=\"");
              if (_jspx_meth_portlet_005fnamespace_005f0(_jspx_th_c_005fwhen_005f0, _jspx_page_context))
                return;
              out.print( entry.getEntryId() );
              out.write("\">\n");
              out.write("\t<h3 class=\"entry-title\">\n");
              out.write("\t\t");
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
              if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f1 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                  // /html/portlet/announcements/view_entry.jspf(47,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f1.setTest( Validator.isNotNull(entry.getUrl()) );
                  int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t<a class=\"entry-url\" href=\"");
                      out.print( entry.getUrl() );
                      out.write('"');
                      out.write('>');
                      out.print( HtmlUtil.escape(entry.getTitle()) );
                      out.write("</a>\n");
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
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f2 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                  // /html/portlet/announcements/view_entry.jspf(50,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f2.setTest( Validator.isNull(entry.getUrl()) );
                  int _jspx_eval_c_005fwhen_005f2 = _jspx_th_c_005fwhen_005f2.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t");
                      out.print( HtmlUtil.escape(entry.getTitle()) );
                      out.write("\n");
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
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              // /html/portlet/announcements/view_entry.jspf(55,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f1.setTest( !tabs1.equals("preview") );
              int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
              if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
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

                  out.write("\n");
                  out.write("\n");
                  out.write("<div class=\"edit-actions\">\n");
                  out.write("\t<table class=\"lfr-table\">\n");
                  out.write("\t<tr>\n");
                  out.write("\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
                  // /html/portlet/announcements/entry_action.jspf(20,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f2.setTest( AnnouncementsEntryPermission.contains(permissionChecker, entry, ActionKeys.UPDATE) );
                  int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
                  if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t<td class=\"edit-entry\">\n");
                      out.write("\t\t\t\t");
                      //  portlet:renderURL
                      com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f0 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                      _jspx_th_portlet_005frenderURL_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005frenderURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
                      // /html/portlet/announcements/entry_action.jspf(22,4) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005frenderURL_005f0.setVar("editURL");
                      int _jspx_eval_portlet_005frenderURL_005f0 = _jspx_th_portlet_005frenderURL_005f0.doStartTag();
                      if (_jspx_eval_portlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_portlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_portlet_005frenderURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_portlet_005frenderURL_005f0.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          if (_jspx_meth_portlet_005fparam_005f0(_jspx_th_portlet_005frenderURL_005f0, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f1 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f1.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
                          // /html/portlet/announcements/entry_action.jspf(24,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f1.setName("redirect");
                          // /html/portlet/announcements/entry_action.jspf(24,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f1.setValue( currentURL );
                          int _jspx_eval_portlet_005fparam_005f1 = _jspx_th_portlet_005fparam_005f1.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f2 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f2.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
                          // /html/portlet/announcements/entry_action.jspf(25,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f2.setName("entryId");
                          // /html/portlet/announcements/entry_action.jspf(25,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f2.setValue( String.valueOf(entry.getEntryId()) );
                          int _jspx_eval_portlet_005fparam_005f2 = _jspx_th_portlet_005fparam_005f2.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                          out.write("\n");
                          out.write("\t\t\t\t");
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
                      java.lang.String editURL = null;
                      editURL = (java.lang.String) _jspx_page_context.findAttribute("editURL");
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  liferay-ui:icon
                      com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f0 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                      _jspx_th_liferay_002dui_005ficon_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ficon_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
                      // /html/portlet/announcements/entry_action.jspf(28,4) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f0.setImage("edit");
                      // /html/portlet/announcements/entry_action.jspf(28,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f0.setLabel( true );
                      // /html/portlet/announcements/entry_action.jspf(28,4) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f0.setUrl( editURL );
                      int _jspx_eval_liferay_002dui_005ficon_005f0 = _jspx_th_liferay_002dui_005ficon_005f0.doStartTag();
                      if (_jspx_th_liferay_002dui_005ficon_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
                      out.write("\n");
                      out.write("\t\t\t</td>\n");
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
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
                  // /html/portlet/announcements/entry_action.jspf(36,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f3.setTest( AnnouncementsEntryPermission.contains(permissionChecker, entry, ActionKeys.DELETE) );
                  int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
                  if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t<td class=\"delete-entry\">\n");
                      out.write("\t\t\t\t");
                      //  portlet:actionURL
                      com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f0 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.get(com.liferay.taglib.portlet.ActionURLTag.class);
                      _jspx_th_portlet_005factionURL_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005factionURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
                      // /html/portlet/announcements/entry_action.jspf(38,4) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005factionURL_005f0.setVar("deleteURL");
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
                          if (_jspx_meth_portlet_005fparam_005f3(_jspx_th_portlet_005factionURL_005f0, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f4 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f4.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
                          // /html/portlet/announcements/entry_action.jspf(40,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f4.setName( Constants.CMD );
                          // /html/portlet/announcements/entry_action.jspf(40,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f4.setValue( Constants.DELETE );
                          int _jspx_eval_portlet_005fparam_005f4 = _jspx_th_portlet_005fparam_005f4.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f5 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f5.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
                          // /html/portlet/announcements/entry_action.jspf(41,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f5.setName("redirect");
                          // /html/portlet/announcements/entry_action.jspf(41,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f5.setValue( currentURL );
                          int _jspx_eval_portlet_005fparam_005f5 = _jspx_th_portlet_005fparam_005f5.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f6 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f6.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
                          // /html/portlet/announcements/entry_action.jspf(42,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f6.setName("entryId");
                          // /html/portlet/announcements/entry_action.jspf(42,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f6.setValue( String.valueOf(entry.getEntryId()) );
                          int _jspx_eval_portlet_005fparam_005f6 = _jspx_th_portlet_005fparam_005f6.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
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
                      java.lang.String deleteURL = null;
                      deleteURL = (java.lang.String) _jspx_page_context.findAttribute("deleteURL");
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  liferay-ui:icon-delete
                      com.liferay.taglib.ui.IconDeleteTag _jspx_th_liferay_002dui_005ficon_002ddelete_005f0 = (com.liferay.taglib.ui.IconDeleteTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005flabel_005fnobody.get(com.liferay.taglib.ui.IconDeleteTag.class);
                      _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
                      // /html/portlet/announcements/entry_action.jspf(45,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.setLabel( true );
                      // /html/portlet/announcements/entry_action.jspf(45,4) name = url type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.setUrl( deleteURL );
                      int _jspx_eval_liferay_002dui_005ficon_002ddelete_005f0 = _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.doStartTag();
                      if (_jspx_th_liferay_002dui_005ficon_002ddelete_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005flabel_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002ddelete_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005flabel_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002ddelete_005f0);
                      out.write("\n");
                      out.write("\t\t\t</td>\n");
                      out.write("\t\t");
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
                  out.write("\t\t<td class=\"control-entry\">\n");
                  out.write("\t\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
                  // /html/portlet/announcements/entry_action.jspf(53,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f4.setTest( tabs1.equals("entries") && themeDisplay.isSignedIn() );
                  int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
                  if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t<a href=\"");
                      out.print( "javascript:" + renderResponse.getNamespace() + "handleEntry(" + entry.getEntryId() + ");" );
                      out.write('"');
                      out.write('>');
                      //  liferay-ui:message
                      com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
                      _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
                      // /html/portlet/announcements/entry_action.jspf(54,112) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fmessage_005f0.setKey( ((flagValue == AnnouncementsFlagConstants.NOT_HIDDEN) ? "mark-as-read" : "show") );
                      int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
                      if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
                      out.write("</a>\n");
                      out.write("\t\t\t");
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
                  out.write("\t\t</td>\n");
                  out.write("\t</tr>\n");
                  out.write("\t</table>\n");
                  out.write("</div>");
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
              out.write("\n");
              out.write("\t</h3>\n");
              out.write("\n");
              out.write("\t");

	boolean showScopeName = false;
	
              out.write("\n");
              out.write("\n");
              out.write("\t<p class=\"");
              out.print( readEntry ? "aui-helper-hidden" : "" );
              out.write(" entry-content entry-type-");
              out.print( entry.getType() );
              out.write("\">\n");
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

String entryClassName = entry.getClassName();
long entryClassPK = entry.getClassPK();

String scopeName = StringPool.BLANK;
String scopeClassName = StringPool.BLANK;

if (entryClassName.equals(Group.class.getName())) {
	Group group = GroupLocalServiceUtil.getGroup(entryClassPK);

	scopeName = group.getDescriptiveName();
	scopeClassName = "community";
}
else if (entryClassName.equals(Organization.class.getName())) {
	Organization organization = OrganizationLocalServiceUtil.getOrganization(entryClassPK);

	scopeName = organization.getName();
	scopeClassName = "organization";
}
else if (entryClassName.equals(Role.class.getName())) {
	Role role = RoleLocalServiceUtil.getRole(entryClassPK);

	scopeName = role.getTitle(locale);
	scopeClassName = "role";
}
else if (entryClassName.equals(User.class.getName())) {
	User curUser = UserLocalServiceUtil.getUser(entryClassPK);

	scopeName = curUser.getFullName();
	scopeClassName = "personal";
}
else if (entryClassName.equals(UserGroup.class.getName())) {
	UserGroup userGroup = UserGroupLocalServiceUtil.getUserGroup(entryClassPK);

	scopeName = userGroup.getName();
	scopeClassName = "user-group";
}
else {
	scopeName = LanguageUtil.get(locale, "general");
	scopeClassName = scopeName;
}

scopeName = HtmlUtil.escape(scopeName);

              out.write('\n');
              out.write('\n');
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f2 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f2.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              int _jspx_eval_c_005fchoose_005f2 = _jspx_th_c_005fchoose_005f2.doStartTag();
              if (_jspx_eval_c_005fchoose_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write('\n');
                  out.write('	');
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f3 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
                  // /html/portlet/announcements/entry_scope.jspf(63,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f3.setTest( showScopeName );
                  int _jspx_eval_c_005fwhen_005f3 = _jspx_th_c_005fwhen_005f3.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write('\n');
                      out.write('	');
                      out.write('	');
                      //  c:choose
                      org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f3 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                      _jspx_th_c_005fchoose_005f3.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fchoose_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
                      int _jspx_eval_c_005fchoose_005f3 = _jspx_th_c_005fchoose_005f3.doStartTag();
                      if (_jspx_eval_c_005fchoose_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t");
                          //  c:when
                          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f4 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                          _jspx_th_c_005fwhen_005f4.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fwhen_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
                          // /html/portlet/announcements/entry_scope.jspf(65,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fwhen_005f4.setTest( scopeName.equals(scopeClassName) );
                          int _jspx_eval_c_005fwhen_005f4 = _jspx_th_c_005fwhen_005f4.doStartTag();
                          if (_jspx_eval_c_005fwhen_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t");
                              out.print( scopeName );
                              out.write("\n");
                              out.write("\t\t\t");
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
                          out.write("\t\t\t");
                          //  c:otherwise
                          org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                          _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
                          int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
                          if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t");
                              //  liferay-ui:message
                              com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f1 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
                              _jspx_th_liferay_002dui_005fmessage_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
                              // /html/portlet/announcements/entry_scope.jspf(69,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fmessage_005f1.setKey( scopeClassName );
                              int _jspx_eval_liferay_002dui_005fmessage_005f1 = _jspx_th_liferay_002dui_005fmessage_005f1.doStartTag();
                              if (_jspx_th_liferay_002dui_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
                              out.write(" &raquo; ");
                              out.print( scopeName );
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
                  //  c:otherwise
                  org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f1 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                  _jspx_th_c_005fotherwise_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fotherwise_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
                  int _jspx_eval_c_005fotherwise_005f1 = _jspx_th_c_005fotherwise_005f1.doStartTag();
                  if (_jspx_eval_c_005fotherwise_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t<span class=\"entry-scope\">");
                      out.print( scopeName );
                      out.write("</span>\n");
                      out.write("\t");
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
              out.write("\n");
              out.write("\t\t");
              out.print( HtmlUtil.escape(entry.getContent()) );
              out.write("\n");
              out.write("\t</p>\n");
              out.write("</div>");
              out.write('\n');
              out.write('\n');

}

              out.write("\n");
              out.write("</div>\n");
              out.write("\n");

boolean hasResults = total > 0;

              out.write('\n');
              out.write('\n');
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f5 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              // /html/portlet/announcements/view_entries.jspf(36,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f5.setTest( hasResults );
              int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
              if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write('\n');
                  out.write('	');
                  //  liferay-ui:search-paginator
                  com.liferay.taglib.ui.SearchPaginatorTag _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0 = (com.liferay.taglib.ui.SearchPaginatorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005ftype_005fsearchContainer_005fnobody.get(com.liferay.taglib.ui.SearchPaginatorTag.class);
                  _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
                  // /html/portlet/announcements/view_entries.jspf(37,1) name = searchContainer type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0.setSearchContainer( searchContainer );
                  // /html/portlet/announcements/view_entries.jspf(37,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0.setType("article");
                  int _jspx_eval_liferay_002dui_005fsearch_002dpaginator_005f0 = _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0.doStartTag();
                  if (_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005ftype_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005ftype_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0);
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

flagValue = AnnouncementsFlagConstants.HIDDEN;

searchContainer = new SearchContainer(renderRequest, null, null, "cur2", delta, portletURL, null, "no-entries-were-found");

              out.write("\n");
              out.write("\n");
              out.write("<div class=\"read-entries\">\n");
              out.write("\t");

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

total = AnnouncementsEntryLocalServiceUtil.getEntriesCount(user.getUserId(), scopes, portletName.equals(PortletKeys.ALERTS), flagValue);

searchContainer.setTotal(total);

results = AnnouncementsEntryLocalServiceUtil.getEntries(user.getUserId(), scopes, portletName.equals(PortletKeys.ALERTS), flagValue, searchContainer.getStart(), searchContainer.getEnd());

searchContainer.setResults(results);

for (int i = 0; i < results.size(); i++) {
	AnnouncementsEntry entry = results.get(i);

	String className = StringPool.BLANK;

	if (i == 0) {
		className += " first";
	}

	if ((i + 1) == results.size()) {
		className += " last";
	}

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

boolean readEntry = false;

try {
	AnnouncementsFlagLocalServiceUtil.getFlag(user.getUserId(), entry.getEntryId(), AnnouncementsFlagConstants.READ);

	readEntry = true;
}
catch (NoSuchFlagException nsfe1) {
	try {
		AnnouncementsFlagLocalServiceUtil.getFlag(user.getUserId(), entry.getEntryId(), AnnouncementsFlagConstants.HIDDEN);

		readEntry = true;
	}
	catch (NoSuchFlagException nsfe2) {
	}
}

if (readEntry) {
	className += " read";
}

if (entry.getPriority() > 0) {
	className += " important";
}

              out.write("\n");
              out.write("\n");
              out.write("<div class=\"entry");
              out.print( className );
              out.write("\" id=\"");
              if (_jspx_meth_portlet_005fnamespace_005f1(_jspx_th_c_005fwhen_005f0, _jspx_page_context))
                return;
              out.print( entry.getEntryId() );
              out.write("\">\n");
              out.write("\t<h3 class=\"entry-title\">\n");
              out.write("\t\t");
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f4 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f4.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              int _jspx_eval_c_005fchoose_005f4 = _jspx_th_c_005fchoose_005f4.doStartTag();
              if (_jspx_eval_c_005fchoose_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f5 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f5.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f4);
                  // /html/portlet/announcements/view_entry.jspf(47,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f5.setTest( Validator.isNotNull(entry.getUrl()) );
                  int _jspx_eval_c_005fwhen_005f5 = _jspx_th_c_005fwhen_005f5.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t<a class=\"entry-url\" href=\"");
                      out.print( entry.getUrl() );
                      out.write('"');
                      out.write('>');
                      out.print( HtmlUtil.escape(entry.getTitle()) );
                      out.write("</a>\n");
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
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f6 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f6.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f4);
                  // /html/portlet/announcements/view_entry.jspf(50,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f6.setTest( Validator.isNull(entry.getUrl()) );
                  int _jspx_eval_c_005fwhen_005f6 = _jspx_th_c_005fwhen_005f6.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t");
                      out.print( HtmlUtil.escape(entry.getTitle()) );
                      out.write("\n");
                      out.write("\t\t\t");
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
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f6 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f6.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              // /html/portlet/announcements/view_entry.jspf(55,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f6.setTest( !tabs1.equals("preview") );
              int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
              if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
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

                  out.write("\n");
                  out.write("\n");
                  out.write("<div class=\"edit-actions\">\n");
                  out.write("\t<table class=\"lfr-table\">\n");
                  out.write("\t<tr>\n");
                  out.write("\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f7 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f7.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
                  // /html/portlet/announcements/entry_action.jspf(20,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f7.setTest( AnnouncementsEntryPermission.contains(permissionChecker, entry, ActionKeys.UPDATE) );
                  int _jspx_eval_c_005fif_005f7 = _jspx_th_c_005fif_005f7.doStartTag();
                  if (_jspx_eval_c_005fif_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t<td class=\"edit-entry\">\n");
                      out.write("\t\t\t\t");
                      //  portlet:renderURL
                      com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f1 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                      _jspx_th_portlet_005frenderURL_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005frenderURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
                      // /html/portlet/announcements/entry_action.jspf(22,4) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005frenderURL_005f1.setVar("editURL");
                      int _jspx_eval_portlet_005frenderURL_005f1 = _jspx_th_portlet_005frenderURL_005f1.doStartTag();
                      if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_portlet_005frenderURL_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_portlet_005frenderURL_005f1.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          if (_jspx_meth_portlet_005fparam_005f7(_jspx_th_portlet_005frenderURL_005f1, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f8 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f8.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
                          // /html/portlet/announcements/entry_action.jspf(24,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f8.setName("redirect");
                          // /html/portlet/announcements/entry_action.jspf(24,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f8.setValue( currentURL );
                          int _jspx_eval_portlet_005fparam_005f8 = _jspx_th_portlet_005fparam_005f8.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f9 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f9.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
                          // /html/portlet/announcements/entry_action.jspf(25,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f9.setName("entryId");
                          // /html/portlet/announcements/entry_action.jspf(25,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f9.setValue( String.valueOf(entry.getEntryId()) );
                          int _jspx_eval_portlet_005fparam_005f9 = _jspx_th_portlet_005fparam_005f9.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
                          out.write("\n");
                          out.write("\t\t\t\t");
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
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  liferay-ui:icon
                      com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f1 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                      _jspx_th_liferay_002dui_005ficon_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ficon_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
                      // /html/portlet/announcements/entry_action.jspf(28,4) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f1.setImage("edit");
                      // /html/portlet/announcements/entry_action.jspf(28,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f1.setLabel( true );
                      // /html/portlet/announcements/entry_action.jspf(28,4) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f1.setUrl( editURL );
                      int _jspx_eval_liferay_002dui_005ficon_005f1 = _jspx_th_liferay_002dui_005ficon_005f1.doStartTag();
                      if (_jspx_th_liferay_002dui_005ficon_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
                      out.write("\n");
                      out.write("\t\t\t</td>\n");
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
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f8 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f8.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
                  // /html/portlet/announcements/entry_action.jspf(36,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f8.setTest( AnnouncementsEntryPermission.contains(permissionChecker, entry, ActionKeys.DELETE) );
                  int _jspx_eval_c_005fif_005f8 = _jspx_th_c_005fif_005f8.doStartTag();
                  if (_jspx_eval_c_005fif_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t<td class=\"delete-entry\">\n");
                      out.write("\t\t\t\t");
                      //  portlet:actionURL
                      com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f1 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.get(com.liferay.taglib.portlet.ActionURLTag.class);
                      _jspx_th_portlet_005factionURL_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005factionURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
                      // /html/portlet/announcements/entry_action.jspf(38,4) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005factionURL_005f1.setVar("deleteURL");
                      int _jspx_eval_portlet_005factionURL_005f1 = _jspx_th_portlet_005factionURL_005f1.doStartTag();
                      if (_jspx_eval_portlet_005factionURL_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_portlet_005factionURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_portlet_005factionURL_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_portlet_005factionURL_005f1.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          if (_jspx_meth_portlet_005fparam_005f10(_jspx_th_portlet_005factionURL_005f1, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f11 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f11.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
                          // /html/portlet/announcements/entry_action.jspf(40,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f11.setName( Constants.CMD );
                          // /html/portlet/announcements/entry_action.jspf(40,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f11.setValue( Constants.DELETE );
                          int _jspx_eval_portlet_005fparam_005f11 = _jspx_th_portlet_005fparam_005f11.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f11);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f11);
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f12 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f12.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
                          // /html/portlet/announcements/entry_action.jspf(41,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f12.setName("redirect");
                          // /html/portlet/announcements/entry_action.jspf(41,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f12.setValue( currentURL );
                          int _jspx_eval_portlet_005fparam_005f12 = _jspx_th_portlet_005fparam_005f12.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f12);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f12);
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f13 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f13.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
                          // /html/portlet/announcements/entry_action.jspf(42,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f13.setName("entryId");
                          // /html/portlet/announcements/entry_action.jspf(42,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f13.setValue( String.valueOf(entry.getEntryId()) );
                          int _jspx_eval_portlet_005fparam_005f13 = _jspx_th_portlet_005fparam_005f13.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f13);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f13);
                          out.write("\n");
                          out.write("\t\t\t\t");
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
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  liferay-ui:icon-delete
                      com.liferay.taglib.ui.IconDeleteTag _jspx_th_liferay_002dui_005ficon_002ddelete_005f1 = (com.liferay.taglib.ui.IconDeleteTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005flabel_005fnobody.get(com.liferay.taglib.ui.IconDeleteTag.class);
                      _jspx_th_liferay_002dui_005ficon_002ddelete_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ficon_002ddelete_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
                      // /html/portlet/announcements/entry_action.jspf(45,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_002ddelete_005f1.setLabel( true );
                      // /html/portlet/announcements/entry_action.jspf(45,4) name = url type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_002ddelete_005f1.setUrl( deleteURL );
                      int _jspx_eval_liferay_002dui_005ficon_002ddelete_005f1 = _jspx_th_liferay_002dui_005ficon_002ddelete_005f1.doStartTag();
                      if (_jspx_th_liferay_002dui_005ficon_002ddelete_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005flabel_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002ddelete_005f1);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005flabel_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002ddelete_005f1);
                      out.write("\n");
                      out.write("\t\t\t</td>\n");
                      out.write("\t\t");
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
                  out.write("\t\t<td class=\"control-entry\">\n");
                  out.write("\t\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f9 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f9.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
                  // /html/portlet/announcements/entry_action.jspf(53,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f9.setTest( tabs1.equals("entries") && themeDisplay.isSignedIn() );
                  int _jspx_eval_c_005fif_005f9 = _jspx_th_c_005fif_005f9.doStartTag();
                  if (_jspx_eval_c_005fif_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t<a href=\"");
                      out.print( "javascript:" + renderResponse.getNamespace() + "handleEntry(" + entry.getEntryId() + ");" );
                      out.write('"');
                      out.write('>');
                      //  liferay-ui:message
                      com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f2 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
                      _jspx_th_liferay_002dui_005fmessage_005f2.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fmessage_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f9);
                      // /html/portlet/announcements/entry_action.jspf(54,112) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fmessage_005f2.setKey( ((flagValue == AnnouncementsFlagConstants.NOT_HIDDEN) ? "mark-as-read" : "show") );
                      int _jspx_eval_liferay_002dui_005fmessage_005f2 = _jspx_th_liferay_002dui_005fmessage_005f2.doStartTag();
                      if (_jspx_th_liferay_002dui_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
                      out.write("</a>\n");
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
                  out.write("\t\t</td>\n");
                  out.write("\t</tr>\n");
                  out.write("\t</table>\n");
                  out.write("</div>");
                  out.write('\n');
                  out.write('	');
                  out.write('	');
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
              out.write("\t</h3>\n");
              out.write("\n");
              out.write("\t");

	boolean showScopeName = false;
	
              out.write("\n");
              out.write("\n");
              out.write("\t<p class=\"");
              out.print( readEntry ? "aui-helper-hidden" : "" );
              out.write(" entry-content entry-type-");
              out.print( entry.getType() );
              out.write("\">\n");
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

String entryClassName = entry.getClassName();
long entryClassPK = entry.getClassPK();

String scopeName = StringPool.BLANK;
String scopeClassName = StringPool.BLANK;

if (entryClassName.equals(Group.class.getName())) {
	Group group = GroupLocalServiceUtil.getGroup(entryClassPK);

	scopeName = group.getDescriptiveName();
	scopeClassName = "community";
}
else if (entryClassName.equals(Organization.class.getName())) {
	Organization organization = OrganizationLocalServiceUtil.getOrganization(entryClassPK);

	scopeName = organization.getName();
	scopeClassName = "organization";
}
else if (entryClassName.equals(Role.class.getName())) {
	Role role = RoleLocalServiceUtil.getRole(entryClassPK);

	scopeName = role.getTitle(locale);
	scopeClassName = "role";
}
else if (entryClassName.equals(User.class.getName())) {
	User curUser = UserLocalServiceUtil.getUser(entryClassPK);

	scopeName = curUser.getFullName();
	scopeClassName = "personal";
}
else if (entryClassName.equals(UserGroup.class.getName())) {
	UserGroup userGroup = UserGroupLocalServiceUtil.getUserGroup(entryClassPK);

	scopeName = userGroup.getName();
	scopeClassName = "user-group";
}
else {
	scopeName = LanguageUtil.get(locale, "general");
	scopeClassName = scopeName;
}

scopeName = HtmlUtil.escape(scopeName);

              out.write('\n');
              out.write('\n');
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f5 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f5.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              int _jspx_eval_c_005fchoose_005f5 = _jspx_th_c_005fchoose_005f5.doStartTag();
              if (_jspx_eval_c_005fchoose_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write('\n');
                  out.write('	');
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f7 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f7.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f5);
                  // /html/portlet/announcements/entry_scope.jspf(63,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f7.setTest( showScopeName );
                  int _jspx_eval_c_005fwhen_005f7 = _jspx_th_c_005fwhen_005f7.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write('\n');
                      out.write('	');
                      out.write('	');
                      //  c:choose
                      org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f6 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                      _jspx_th_c_005fchoose_005f6.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fchoose_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f7);
                      int _jspx_eval_c_005fchoose_005f6 = _jspx_th_c_005fchoose_005f6.doStartTag();
                      if (_jspx_eval_c_005fchoose_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t");
                          //  c:when
                          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f8 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                          _jspx_th_c_005fwhen_005f8.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fwhen_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f6);
                          // /html/portlet/announcements/entry_scope.jspf(65,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fwhen_005f8.setTest( scopeName.equals(scopeClassName) );
                          int _jspx_eval_c_005fwhen_005f8 = _jspx_th_c_005fwhen_005f8.doStartTag();
                          if (_jspx_eval_c_005fwhen_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t");
                              out.print( scopeName );
                              out.write("\n");
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
                          org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f2 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                          _jspx_th_c_005fotherwise_005f2.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fotherwise_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f6);
                          int _jspx_eval_c_005fotherwise_005f2 = _jspx_th_c_005fotherwise_005f2.doStartTag();
                          if (_jspx_eval_c_005fotherwise_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t");
                              //  liferay-ui:message
                              com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f3 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
                              _jspx_th_liferay_002dui_005fmessage_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005fmessage_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f2);
                              // /html/portlet/announcements/entry_scope.jspf(69,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005fmessage_005f3.setKey( scopeClassName );
                              int _jspx_eval_liferay_002dui_005fmessage_005f3 = _jspx_th_liferay_002dui_005fmessage_005f3.doStartTag();
                              if (_jspx_th_liferay_002dui_005fmessage_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
                              return;
                              }
                              _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
                              out.write(" &raquo; ");
                              out.print( scopeName );
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
                      out.write('	');
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
                  org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f3 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                  _jspx_th_c_005fotherwise_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fotherwise_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f5);
                  int _jspx_eval_c_005fotherwise_005f3 = _jspx_th_c_005fotherwise_005f3.doStartTag();
                  if (_jspx_eval_c_005fotherwise_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t<span class=\"entry-scope\">");
                      out.print( scopeName );
                      out.write("</span>\n");
                      out.write("\t");
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
              out.write("\t\t");
              out.print( HtmlUtil.escape(entry.getContent()) );
              out.write("\n");
              out.write("\t</p>\n");
              out.write("</div>");
              out.write('\n');
              out.write('\n');

}

              out.write("\n");
              out.write("</div>\n");
              out.write("\n");

hasResults = hasResults || (total > 0);

              out.write('\n');
              out.write('\n');
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f10 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f10.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              // /html/portlet/announcements/view_entries.jspf(54,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f10.setTest( total > 0 );
              int _jspx_eval_c_005fif_005f10 = _jspx_th_c_005fif_005f10.doStartTag();
              if (_jspx_eval_c_005fif_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write('\n');
                  out.write('	');
                  //  liferay-ui:search-paginator
                  com.liferay.taglib.ui.SearchPaginatorTag _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f1 = (com.liferay.taglib.ui.SearchPaginatorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005ftype_005fsearchContainer_005fnobody.get(com.liferay.taglib.ui.SearchPaginatorTag.class);
                  _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f10);
                  // /html/portlet/announcements/view_entries.jspf(55,1) name = searchContainer type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f1.setSearchContainer( searchContainer );
                  // /html/portlet/announcements/view_entries.jspf(55,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f1.setType("article");
                  int _jspx_eval_liferay_002dui_005fsearch_002dpaginator_005f1 = _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f1.doStartTag();
                  if (_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005ftype_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005ftype_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f1);
                  out.write('\n');
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
              out.write('\n');
              out.write('\n');
              //  aui:script
              com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript.get(com.liferay.taglib.aui.ScriptTag.class);
              _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
              _jspx_th_aui_005fscript_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
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
                  if (_jspx_meth_portlet_005fnamespace_005f2(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
                    return;
                  out.write("handleEntry',\n");
                  out.write("\t\tfunction(entryId) {\n");
                  out.write("\t\t\tvar A = AUI();\n");
                  out.write("\n");
                  out.write("\t\t\tvar entry = A.one('#");
                  if (_jspx_meth_portlet_005fnamespace_005f3(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
                    return;
                  out.write("' + entryId);\n");
                  out.write("\n");
                  out.write("\t\t\tif (entry) {\n");
                  out.write("\t\t\t\tvar container = entry.get('parentNode');\n");
                  out.write("\n");
                  out.write("\t\t\t\tif (container) {\n");
                  out.write("\t\t\t\t\tif (container.hasClass('unread-entries')) {\n");
                  out.write("\t\t\t\t\t\t");
                  if (_jspx_meth_portlet_005fnamespace_005f4(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
                    return;
                  out.write("markEntry(entry, entryId);\n");
                  out.write("\t\t\t\t\t}\n");
                  out.write("\t\t\t\t\telse {\n");
                  out.write("\t\t\t\t\t\t");
                  if (_jspx_meth_portlet_005fnamespace_005f5(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
                    return;
                  out.write("toggleContent(entry);\n");
                  out.write("\t\t\t\t\t}\n");
                  out.write("\t\t\t\t}\n");
                  out.write("\t\t\t}\n");
                  out.write("\t\t},\n");
                  out.write("\t\t['aui-base']\n");
                  out.write("\t);\n");
                  out.write("\n");
                  out.write("\tLiferay.provide(\n");
                  out.write("\t\twindow,\n");
                  out.write("\t\t'");
                  if (_jspx_meth_portlet_005fnamespace_005f6(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
                    return;
                  out.write("markEntry',\n");
                  out.write("\t\tfunction(entry, entryId) {\n");
                  out.write("\t\t\tvar A = AUI();\n");
                  out.write("\n");
                  out.write("\t\t\tLiferay.Service.Announcements.AnnouncementsFlag.addFlag({entryId : entryId, flag: ");
                  out.print( AnnouncementsFlagConstants.HIDDEN );
                  out.write("});\n");
                  out.write("\n");
                  out.write("\t\t\tLiferay.Portlet.refresh('#p_p_id");
                  if (_jspx_meth_portlet_005fnamespace_005f7(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
                    return;
                  out.write("');\n");
                  out.write("\t\t},\n");
                  out.write("\t\t['aui-base']\n");
                  out.write("\t);\n");
                  out.write("\n");
                  out.write("\tLiferay.provide(\n");
                  out.write("\t\twindow,\n");
                  out.write("\t\t'");
                  if (_jspx_meth_portlet_005fnamespace_005f8(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
                    return;
                  out.write("toggleContent',\n");
                  out.write("\t\tfunction(entry) {\n");
                  out.write("\t\t\tvar A = AUI();\n");
                  out.write("\n");
                  out.write("\t\t\tvar content = entry.all('.entry-content');\n");
                  out.write("\t\t\tvar control = entry.all('.control-entry a');\n");
                  out.write("\n");
                  out.write("\t\t\tif (entry.hasClass('visible')) {\n");
                  out.write("\t\t\t\tentry.removeClass('visible');\n");
                  out.write("\n");
                  out.write("\t\t\t\tif (content) {\n");
                  out.write("\t\t\t\t\tcontent.hide();\n");
                  out.write("\t\t\t\t}\n");
                  out.write("\n");
                  out.write("\t\t\t\tif (control) {\n");
                  out.write("\t\t\t\t\tcontrol.html('");
                  if (_jspx_meth_liferay_002dui_005fmessage_005f4(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
                    return;
                  out.write("');\n");
                  out.write("\t\t\t\t}\n");
                  out.write("\t\t\t}\n");
                  out.write("\t\t\telse {\n");
                  out.write("\t\t\t\tentry.addClass('visible');\n");
                  out.write("\n");
                  out.write("\t\t\t\tif (content) {\n");
                  out.write("\t\t\t\t\tcontent.show();\n");
                  out.write("\t\t\t\t}\n");
                  out.write("\n");
                  out.write("\t\t\t\tif (control) {\n");
                  out.write("\t\t\t\t\tcontrol.html('");
                  if (_jspx_meth_liferay_002dui_005fmessage_005f5(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
                    return;
                  out.write("');\n");
                  out.write("\t\t\t\t}\n");
                  out.write("\t\t\t}\n");
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
              out.write('\n');
              out.write('\n');
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f11 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f11.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
              // /html/portlet/announcements/view_entries.jspf(132,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f11.setTest( !hasResults && portletName.equals(PortletKeys.ALERTS) && !PortletPermissionUtil.contains(permissionChecker, plid, PortletKeys.ANNOUNCEMENTS, ActionKeys.ADD_ENTRY) );
              int _jspx_eval_c_005fif_005f11 = _jspx_th_c_005fif_005f11.doStartTag();
              if (_jspx_eval_c_005fif_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write('\n');
                  out.write('	');
                  if (_jspx_meth_aui_005fscript_005f1(_jspx_th_c_005fif_005f11, _jspx_page_context))
                    return;
                  out.write('\n');
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
          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f9 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
          _jspx_th_c_005fwhen_005f9.setPageContext(_jspx_page_context);
          _jspx_th_c_005fwhen_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
          // /html/portlet/announcements/view.jsp(38,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fwhen_005f9.setTest( tabs1.equals("manage-entries") );
          int _jspx_eval_c_005fwhen_005f9 = _jspx_th_c_005fwhen_005f9.doStartTag();
          if (_jspx_eval_c_005fwhen_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
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

String distributionScope = ParamUtil.getString(request, "distributionScope");

long classNameId = -1;
long classPK = -1;

String[] distributionScopeArray = StringUtil.split(distributionScope);

if (distributionScopeArray.length == 2) {
	classNameId = GetterUtil.getLong(distributionScopeArray[0]);
	classPK = GetterUtil.getLong(distributionScopeArray[1]);
}

if ((classNameId == 0) && (classPK == 0) && !permissionChecker.isOmniadmin()) {
	throw new PrincipalException();
}

              out.write('\n');
              out.write('\n');
              //  aui:form
              com.liferay.taglib.aui.FormTag _jspx_th_aui_005fform_005f0 = (com.liferay.taglib.aui.FormTag) _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005faction.get(com.liferay.taglib.aui.FormTag.class);
              _jspx_th_aui_005fform_005f0.setPageContext(_jspx_page_context);
              _jspx_th_aui_005fform_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f9);
              // /html/portlet/announcements/view_manage_entries.jspf(35,0) name = action type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005fform_005f0.setAction( portletURL.toString() );
              // /html/portlet/announcements/view_manage_entries.jspf(35,0) null
              _jspx_th_aui_005fform_005f0.setDynamicAttribute(null, "method", new String("post"));
              // /html/portlet/announcements/view_manage_entries.jspf(35,0) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
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
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t");

		boolean submitOnChange = true;
		
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
                      //  aui:select
                      com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f0 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fonChange_005fname_005fchangesContext.get(com.liferay.taglib.aui.SelectTag.class);
                      _jspx_th_aui_005fselect_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005fselect_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
                      // /html/portlet/announcements/entry_select_scope.jspf(17,0) name = changesContext type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005fselect_005f0.setChangesContext( submitOnChange );
                      // /html/portlet/announcements/entry_select_scope.jspf(17,0) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005fselect_005f0.setName("distributionScope");
                      // /html/portlet/announcements/entry_select_scope.jspf(17,0) name = onChange type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005fselect_005f0.setOnChange( submitOnChange ? renderResponse.getNamespace() + "selectDistributionScope(this.value);" : "" );
                      // /html/portlet/announcements/entry_select_scope.jspf(17,0) name = showEmptyOption type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005fselect_005f0.setShowEmptyOption( true );
                      int _jspx_eval_aui_005fselect_005f0 = _jspx_th_aui_005fselect_005f0.doStartTag();
                      if (_jspx_eval_aui_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_aui_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_aui_005fselect_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_aui_005fselect_005f0.doInitBody();
                        }
                        do {
                          out.write('\n');
                          out.write('	');
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f12 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f12.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f0);
                          // /html/portlet/announcements/entry_select_scope.jspf(18,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f12.setTest( permissionChecker.isOmniadmin() );
                          int _jspx_eval_c_005fif_005f12 = _jspx_th_c_005fif_005f12.doStartTag();
                          if (_jspx_eval_c_005fif_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write('\n');
                              out.write('	');
                              out.write('	');
                              //  aui:option
                              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f0 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                              _jspx_th_aui_005foption_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005foption_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f12);
                              // /html/portlet/announcements/entry_select_scope.jspf(19,2) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f0.setLabel(new String("general"));
                              // /html/portlet/announcements/entry_select_scope.jspf(19,2) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f0.setSelected( (classNameId == 0) && (classPK == 0) );
                              // /html/portlet/announcements/entry_select_scope.jspf(19,2) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f0.setValue(new String("0,0"));
                              int _jspx_eval_aui_005foption_005f0 = _jspx_th_aui_005foption_005f0.doStartTag();
                              if (_jspx_th_aui_005foption_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f0);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f0);
                              out.write('\n');
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
                          out.write('\n');
                          out.write('	');

	List<Group> groups = GroupLocalServiceUtil.getUserGroups(user.getUserId(), true);
	
                          out.write('\n');
                          out.write('\n');
                          out.write('	');
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f13 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f13.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f0);
                          // /html/portlet/announcements/entry_select_scope.jspf(26,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f13.setTest( !groups.isEmpty() );
                          int _jspx_eval_c_005fif_005f13 = _jspx_th_c_005fif_005f13.doStartTag();
                          if (_jspx_eval_c_005fif_005f13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t<optgroup label=\"");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f6(_jspx_th_c_005fif_005f13, _jspx_page_context))
                              return;
                              out.write("\">\n");
                              out.write("\n");
                              out.write("\t\t\t");

			for (Group group : groups) {
				if (group.isCommunity() && GroupPermissionUtil.contains(permissionChecker, group.getGroupId(), ActionKeys.MANAGE_ANNOUNCEMENTS)) {
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              //  aui:option
                              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f1 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                              _jspx_th_aui_005foption_005f1.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005foption_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f13);
                              // /html/portlet/announcements/entry_select_scope.jspf(34,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f1.setLabel( group.getDescriptiveName() );
                              // /html/portlet/announcements/entry_select_scope.jspf(34,5) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f1.setSelected( classPK == group.getGroupId() );
                              // /html/portlet/announcements/entry_select_scope.jspf(34,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f1.setValue( PortalUtil.getClassNameId(Group.class) + StringPool.COMMA + group.getGroupId() );
                              int _jspx_eval_aui_005foption_005f1 = _jspx_th_aui_005foption_005f1.doStartTag();
                              if (_jspx_th_aui_005foption_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f1);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f1);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

				}
			}
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t</optgroup>\n");
                              out.write("\t");
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
                          out.write('	');

	List<Organization> organizations = OrganizationLocalServiceUtil.getUserOrganizations(user.getUserId());
	
                          out.write('\n');
                          out.write('\n');
                          out.write('	');
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f14 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f14.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f0);
                          // /html/portlet/announcements/entry_select_scope.jspf(48,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f14.setTest( !organizations.isEmpty() );
                          int _jspx_eval_c_005fif_005f14 = _jspx_th_c_005fif_005f14.doStartTag();
                          if (_jspx_eval_c_005fif_005f14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t<optgroup label=\"");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f7(_jspx_th_c_005fif_005f14, _jspx_page_context))
                              return;
                              out.write("\">\n");
                              out.write("\n");
                              out.write("\t\t\t");

			for (Organization organization : organizations) {
				if (OrganizationPermissionUtil.contains(permissionChecker, organization.getOrganizationId(), ActionKeys.MANAGE_ANNOUNCEMENTS)) {
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              //  aui:option
                              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f2 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                              _jspx_th_aui_005foption_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005foption_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f14);
                              // /html/portlet/announcements/entry_select_scope.jspf(56,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f2.setLabel( HtmlUtil.escape(organization.getName()) );
                              // /html/portlet/announcements/entry_select_scope.jspf(56,5) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f2.setSelected( classPK == organization.getOrganizationId() );
                              // /html/portlet/announcements/entry_select_scope.jspf(56,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f2.setValue( PortalUtil.getClassNameId(Organization.class) + StringPool.COMMA + organization.getOrganizationId() );
                              int _jspx_eval_aui_005foption_005f2 = _jspx_th_aui_005foption_005f2.doStartTag();
                              if (_jspx_th_aui_005foption_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f2);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f2);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

				}
			}
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t</optgroup>\n");
                              out.write("\t");
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
                          out.write('	');

	List<Role> roles = RoleLocalServiceUtil.getRoles(themeDisplay.getCompanyId());
	
                          out.write('\n');
                          out.write('\n');
                          out.write('	');
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f15 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f15.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f0);
                          // /html/portlet/announcements/entry_select_scope.jspf(70,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f15.setTest( !roles.isEmpty() );
                          int _jspx_eval_c_005fif_005f15 = _jspx_th_c_005fif_005f15.doStartTag();
                          if (_jspx_eval_c_005fif_005f15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t<optgroup label=\"");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f8(_jspx_th_c_005fif_005f15, _jspx_page_context))
                              return;
                              out.write("\">\n");
                              out.write("\n");
                              out.write("\t\t\t");

			for (Role role : roles) {
				if (RolePermissionUtil.contains(permissionChecker, role.getRoleId(), ActionKeys.MANAGE_ANNOUNCEMENTS)) {
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              //  aui:option
                              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f3 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                              _jspx_th_aui_005foption_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005foption_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f15);
                              // /html/portlet/announcements/entry_select_scope.jspf(78,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f3.setLabel( HtmlUtil.escape(role.getTitle(locale)) );
                              // /html/portlet/announcements/entry_select_scope.jspf(78,5) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f3.setSelected( classPK == role.getRoleId() );
                              // /html/portlet/announcements/entry_select_scope.jspf(78,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f3.setValue( PortalUtil.getClassNameId(Role.class) + StringPool.COMMA + role.getRoleId() );
                              int _jspx_eval_aui_005foption_005f3 = _jspx_th_aui_005foption_005f3.doStartTag();
                              if (_jspx_th_aui_005foption_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f3);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f3);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

				}
			}
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t</optgroup>\n");
                              out.write("\t");
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
                          out.write('	');

	List<UserGroup> userGroups = UserGroupLocalServiceUtil.getUserGroups(themeDisplay.getCompanyId());
	
                          out.write('\n');
                          out.write('\n');
                          out.write('	');
                          //  c:if
                          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f16 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                          _jspx_th_c_005fif_005f16.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fif_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f0);
                          // /html/portlet/announcements/entry_select_scope.jspf(92,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_c_005fif_005f16.setTest( !userGroups.isEmpty() );
                          int _jspx_eval_c_005fif_005f16 = _jspx_th_c_005fif_005f16.doStartTag();
                          if (_jspx_eval_c_005fif_005f16 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t<optgroup label=\"");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f9(_jspx_th_c_005fif_005f16, _jspx_page_context))
                              return;
                              out.write("\">\n");
                              out.write("\n");
                              out.write("\t\t\t");

			for (UserGroup userGroup : userGroups) {
				if (UserGroupPermissionUtil.contains(permissionChecker, userGroup.getUserGroupId(), ActionKeys.MANAGE_ANNOUNCEMENTS)) {
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t");
                              //  aui:option
                              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f4 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                              _jspx_th_aui_005foption_005f4.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005foption_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f16);
                              // /html/portlet/announcements/entry_select_scope.jspf(100,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f4.setLabel( HtmlUtil.escape(userGroup.getName()) );
                              // /html/portlet/announcements/entry_select_scope.jspf(100,5) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f4.setSelected( classPK == userGroup.getUserGroupId() );
                              // /html/portlet/announcements/entry_select_scope.jspf(100,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005foption_005f4.setValue( PortalUtil.getClassNameId(UserGroup.class) + StringPool.COMMA + userGroup.getUserGroupId() );
                              int _jspx_eval_aui_005foption_005f4 = _jspx_th_aui_005foption_005f4.doStartTag();
                              if (_jspx_th_aui_005foption_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f4);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f4);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t");

				}
			}
			
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t</optgroup>\n");
                              out.write("\t");
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
                          int evalDoAfterBody = _jspx_th_aui_005fselect_005f0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_aui_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.popBody();
                        }
                      }
                      if (_jspx_th_aui_005fselect_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fonChange_005fname_005fchangesContext.reuse(_jspx_th_aui_005fselect_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fonChange_005fname_005fchangesContext.reuse(_jspx_th_aui_005fselect_005f0);
                      out.write('\n');
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
                  //  aui:button
                  com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f0 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                  _jspx_th_aui_005fbutton_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fbutton_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
                  // /html/portlet/announcements/view_manage_entries.jspf(46,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f0.setType("button");
                  // /html/portlet/announcements/view_manage_entries.jspf(46,1) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f0.setOnClick( renderResponse.getNamespace() + "addEntry()" );
                  // /html/portlet/announcements/view_manage_entries.jspf(46,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f0.setValue("add-entry");
                  int _jspx_eval_aui_005fbutton_005f0 = _jspx_th_aui_005fbutton_005f0.doStartTag();
                  if (_jspx_th_aui_005fbutton_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
                  out.write('\n');
                  out.write('\n');
                  out.write('	');
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f17 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f17.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f0);
                  // /html/portlet/announcements/view_manage_entries.jspf(48,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f17.setTest( Validator.isNotNull(distributionScope) );
                  int _jspx_eval_c_005fif_005f17 = _jspx_th_c_005fif_005f17.doStartTag();
                  if (_jspx_eval_c_005fif_005f17 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t<div class=\"separator\"><!-- --></div>\n");
                      out.write("\n");
                      out.write("\t\t");

		PortletURL iteratorURL = PortletURLUtil.clone(portletURL, renderResponse);

		iteratorURL.setParameter("distributionScope", distributionScope);

		List<String> headerNames = new ArrayList<String>();

		headerNames.add("title");
		headerNames.add("type");
		headerNames.add("modified-date");
		headerNames.add("display-date");
		headerNames.add("expiration-date");
		headerNames.add(StringPool.BLANK);

		SearchContainer searchContainer = new SearchContainer(renderRequest, null, null, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_DELTA, iteratorURL, headerNames, "no-entries-were-found");

		int total = AnnouncementsEntryLocalServiceUtil.getEntriesCount(classNameId, classPK, portletName.equals(PortletKeys.ALERTS));

		searchContainer.setTotal(total);

		List<AnnouncementsEntry> results = AnnouncementsEntryLocalServiceUtil.getEntries(classNameId, classPK, portletName.equals(PortletKeys.ALERTS), searchContainer.getStart(), searchContainer.getEnd());

		searchContainer.setResults(results);

		List resultRows = searchContainer.getResultRows();

		for (int i = 0; i < results.size(); i++) {
			AnnouncementsEntry entry = results.get(i);

			entry = entry.toEscapedModel();

			ResultRow row = new ResultRow(entry, entry.getEntryId(), i);

			PortletURL rowURL = renderResponse.createRenderURL();

			rowURL.setParameter("struts_action", "/announcements/edit_entry");
			rowURL.setParameter("redirect", currentURL);
			rowURL.setParameter("entryId", String.valueOf(entry.getEntryId()));

			// Title

			row.addText(entry.getTitle(), rowURL);

			// Type

			row.addText(LanguageUtil.get(pageContext, entry.getType()), rowURL);

			// Modified date

			row.addText(dateFormatDate.format(entry.getModifiedDate()), rowURL);

			// Display date

			row.addText(dateFormatDate.format(entry.getDisplayDate()), rowURL);

			// Expiration date

			row.addText(dateFormatDate.format(entry.getExpirationDate()), rowURL);

			// Action

			row.addJSP("right", SearchEntry.DEFAULT_VALIGN, "/html/portlet/announcements/view_manage_entries_entry_action.jsp");

			// Add result row

			resultRows.add(row);
		}
		
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t");
                      //  liferay-ui:search-iterator
                      com.liferay.taglib.ui.SearchIteratorTag _jspx_th_liferay_002dui_005fsearch_002diterator_005f0 = (com.liferay.taglib.ui.SearchIteratorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.get(com.liferay.taglib.ui.SearchIteratorTag.class);
                      _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f17);
                      // /html/portlet/announcements/view_manage_entries.jspf(120,2) name = searchContainer type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.setSearchContainer( searchContainer );
                      int _jspx_eval_liferay_002dui_005fsearch_002diterator_005f0 = _jspx_th_liferay_002dui_005fsearch_002diterator_005f0.doStartTag();
                      if (_jspx_th_liferay_002dui_005fsearch_002diterator_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002diterator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002diterator_005f0);
                      out.write('\n');
                      out.write('	');
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
              com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f2 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript.get(com.liferay.taglib.aui.ScriptTag.class);
              _jspx_th_aui_005fscript_005f2.setPageContext(_jspx_page_context);
              _jspx_th_aui_005fscript_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f9);
              int _jspx_eval_aui_005fscript_005f2 = _jspx_th_aui_005fscript_005f2.doStartTag();
              if (_jspx_eval_aui_005fscript_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_aui_005fscript_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_aui_005fscript_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_aui_005fscript_005f2.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\tfunction ");
                  if (_jspx_meth_portlet_005fnamespace_005f10(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                    return;
                  out.write("addEntry() {\n");
                  out.write("\t\tlocation.href = '");
                  //  portlet:renderURL
                  com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f2 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL.get(com.liferay.taglib.portlet.RenderURLTag.class);
                  _jspx_th_portlet_005frenderURL_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005frenderURL_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
                  int _jspx_eval_portlet_005frenderURL_005f2 = _jspx_th_portlet_005frenderURL_005f2.doStartTag();
                  if (_jspx_eval_portlet_005frenderURL_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_portlet_005frenderURL_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_portlet_005frenderURL_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_portlet_005frenderURL_005f2.doInitBody();
                    }
                    do {
                      if (_jspx_meth_portlet_005fparam_005f14(_jspx_th_portlet_005frenderURL_005f2, _jspx_page_context))
                        return;
                      //  portlet:param
                      com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f15 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                      _jspx_th_portlet_005fparam_005f15.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005fparam_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f2);
                      // /html/portlet/announcements/view_manage_entries.jspf(126,110) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f15.setName("redirect");
                      // /html/portlet/announcements/view_manage_entries.jspf(126,110) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f15.setValue( currentURL );
                      int _jspx_eval_portlet_005fparam_005f15 = _jspx_th_portlet_005fparam_005f15.doStartTag();
                      if (_jspx_th_portlet_005fparam_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f15);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f15);
                      //  portlet:param
                      com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f16 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                      _jspx_th_portlet_005fparam_005f16.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005fparam_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f2);
                      // /html/portlet/announcements/view_manage_entries.jspf(126,169) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f16.setName("distributionScope");
                      // /html/portlet/announcements/view_manage_entries.jspf(126,169) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f16.setValue( distributionScope );
                      int _jspx_eval_portlet_005fparam_005f16 = _jspx_th_portlet_005fparam_005f16.doStartTag();
                      if (_jspx_th_portlet_005fparam_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f16);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f16);
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
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005frenderURL.reuse(_jspx_th_portlet_005frenderURL_005f2);
                  out.write("';\n");
                  out.write("\t}\n");
                  out.write("\n");
                  out.write("\tfunction ");
                  if (_jspx_meth_portlet_005fnamespace_005f11(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                    return;
                  out.write("selectDistributionScope(distributionScope) {\n");
                  out.write("\t\tvar url = \"");
                  out.print( portletURL.toString() );
                  out.write('&');
                  if (_jspx_meth_portlet_005fnamespace_005f12(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                    return;
                  out.write("distributionScope=\" + distributionScope;\n");
                  out.write("\t\tsubmitForm(document.");
                  if (_jspx_meth_portlet_005fnamespace_005f13(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
                    return;
                  out.write("fm, url);\n");
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
                _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f2);
                return;
              }
              _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f2);
              out.write('\n');
              out.write('	');
              int evalDoAfterBody = _jspx_th_c_005fwhen_005f9.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fwhen_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f9);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f9);
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

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f0 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
    // /html/portlet/announcements/view.jsp(31,1) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f0.setPage("/html/portlet/announcements/tabs1.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f0 = _jspx_th_liferay_002dutil_005finclude_005f0.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f0 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
    int _jspx_eval_portlet_005fnamespace_005f0 = _jspx_th_portlet_005fnamespace_005f0.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
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
    // /html/portlet/announcements/entry_action.jspf(23,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setName("struts_action");
    // /html/portlet/announcements/entry_action.jspf(23,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setValue("/announcements/edit_entry");
    int _jspx_eval_portlet_005fparam_005f0 = _jspx_th_portlet_005fparam_005f0.doStartTag();
    if (_jspx_th_portlet_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
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
    // /html/portlet/announcements/entry_action.jspf(39,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f3.setName("struts_action");
    // /html/portlet/announcements/entry_action.jspf(39,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f3.setValue("/announcements/edit_entry");
    int _jspx_eval_portlet_005fparam_005f3 = _jspx_th_portlet_005fparam_005f3.doStartTag();
    if (_jspx_th_portlet_005fparam_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f1 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f1.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
    int _jspx_eval_portlet_005fnamespace_005f1 = _jspx_th_portlet_005fnamespace_005f1.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f1);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f7 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f7.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
    // /html/portlet/announcements/entry_action.jspf(23,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f7.setName("struts_action");
    // /html/portlet/announcements/entry_action.jspf(23,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f7.setValue("/announcements/edit_entry");
    int _jspx_eval_portlet_005fparam_005f7 = _jspx_th_portlet_005fparam_005f7.doStartTag();
    if (_jspx_th_portlet_005fparam_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f10 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f10.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f1);
    // /html/portlet/announcements/entry_action.jspf(39,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f10.setName("struts_action");
    // /html/portlet/announcements/entry_action.jspf(39,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f10.setValue("/announcements/edit_entry");
    int _jspx_eval_portlet_005fparam_005f10 = _jspx_th_portlet_005fparam_005f10.doStartTag();
    if (_jspx_th_portlet_005fparam_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f10);
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

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f4 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f4.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    // /html/portlet/announcements/view_entries.jspf(113,19) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f4.setKey("show");
    int _jspx_eval_liferay_002dui_005fmessage_005f4 = _jspx_th_liferay_002dui_005fmessage_005f4.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f5 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f5.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f0);
    // /html/portlet/announcements/view_entries.jspf(124,19) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f5.setKey("hide");
    int _jspx_eval_liferay_002dui_005fmessage_005f5 = _jspx_th_liferay_002dui_005fmessage_005f5.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
    return false;
  }

  private boolean _jspx_meth_aui_005fscript_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f11, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:script
    com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f1 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
    _jspx_th_aui_005fscript_005f1.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fscript_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f11);
    // /html/portlet/announcements/view_entries.jspf(133,1) name = use type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fscript_005f1.setUse("aui-base");
    int _jspx_eval_aui_005fscript_005f1 = _jspx_th_aui_005fscript_005f1.doStartTag();
    if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_005fscript_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_005fscript_005f1.doInitBody();
      }
      do {
        out.write("\n");
        out.write("\t\tvar portlet = A.one('#p_p_id");
        if (_jspx_meth_portlet_005fnamespace_005f9(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
          return true;
        out.write("');\n");
        out.write("\n");
        out.write("\t\tif (portlet) {\n");
        out.write("\t\t\tportlet.hide();\n");
        out.write("\t\t}\n");
        out.write("\t");
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

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f13, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f6 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f6.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f13);
    // /html/portlet/announcements/entry_select_scope.jspf(27,19) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f6.setKey("communities");
    int _jspx_eval_liferay_002dui_005fmessage_005f6 = _jspx_th_liferay_002dui_005fmessage_005f6.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f14, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f7 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f7.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f14);
    // /html/portlet/announcements/entry_select_scope.jspf(49,19) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f7.setKey("organizations");
    int _jspx_eval_liferay_002dui_005fmessage_005f7 = _jspx_th_liferay_002dui_005fmessage_005f7.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f15, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f8 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f8.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f15);
    // /html/portlet/announcements/entry_select_scope.jspf(71,19) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f8.setKey("roles");
    int _jspx_eval_liferay_002dui_005fmessage_005f8 = _jspx_th_liferay_002dui_005fmessage_005f8.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f8);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f16, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f9 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f9.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f16);
    // /html/portlet/announcements/entry_select_scope.jspf(93,19) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f9.setKey("user-groups");
    int _jspx_eval_liferay_002dui_005fmessage_005f9 = _jspx_th_liferay_002dui_005fmessage_005f9.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f9);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f10 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f10.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f10 = _jspx_th_portlet_005fnamespace_005f10.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f10);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f14 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f14.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f2);
    // /html/portlet/announcements/view_manage_entries.jspf(126,38) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f14.setName("struts_action");
    // /html/portlet/announcements/view_manage_entries.jspf(126,38) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f14.setValue("/announcements/edit_entry");
    int _jspx_eval_portlet_005fparam_005f14 = _jspx_th_portlet_005fparam_005f14.doStartTag();
    if (_jspx_th_portlet_005fparam_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f14);
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

  private boolean _jspx_meth_portlet_005fnamespace_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f12 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f12.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f12 = _jspx_th_portlet_005fnamespace_005f12.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f12);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f13 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f13.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f13 = _jspx_th_portlet_005fnamespace_005f13.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f13);
    return false;
  }
}
