package org.apache.jsp.html.taglib.ui.discussion;

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
import com.liferay.portlet.messageboards.model.MBCategory;
import com.liferay.portlet.messageboards.model.MBDiscussion;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBMessageDisplay;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.model.MBThreadConstants;
import com.liferay.portlet.messageboards.model.MBTreeWalker;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.messageboards.service.permission.MBDiscussionPermission;
import com.liferay.portlet.messageboards.util.BBCodeUtil;
import com.liferay.portlet.messageboards.util.comparator.MessageCreateDateComparator;
import com.liferay.portlet.ratings.model.RatingsEntry;
import com.liferay.portlet.ratings.model.RatingsStats;
import com.liferay.portlet.ratings.service.RatingsEntryLocalServiceUtil;
import com.liferay.portlet.ratings.service.RatingsStatsLocalServiceUtil;
import com.liferay.portlet.ratings.service.persistence.RatingsEntryUtil;
import com.liferay.portlet.ratings.service.persistence.RatingsStatsUtil;

public final class page_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


public static final String BUTTON_INPUT_PREFIX = "aui-button-input";

public static final String BUTTON_PREFIX = "aui-button";

public static final String FIELD_PREFIX = "aui-field";

public static final String INPUT_PREFIX = "aui-field-input";

public static final String LABEL_PREFIX = "aui-field-label";

private static String _buildCss(String prefix, String baseTypeCss, boolean inlineField, boolean disabled, boolean choiceField, boolean first, boolean last, String cssClass) {
	StringBundler sb = new StringBundler();

	sb.append(prefix);

	if (choiceField) {
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-choice");
	}
	else if (baseTypeCss.equals("textarea") || baseTypeCss.equals("password") || baseTypeCss.equals("string")) {
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-text");
	}
	else if (baseTypeCss.equals("select")) {
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-select");
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-menu");
	}
	else if (baseTypeCss.equals("button")) {
	}
	else {
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-");
		sb.append(baseTypeCss);
	}

	if (inlineField) {
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-inline");
	}

	if (disabled) {
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-disabled");
	}

	if (first) {
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-first");
	}
	else if (last) {
		sb.append(StringPool.SPACE);
		sb.append(prefix);
		sb.append("-last");
	}

	if (Validator.isNotNull(cssClass)) {
		sb.append(StringPool.SPACE);
		sb.append(cssClass);
	}

	return sb.toString();
}

private static String _buildData(Map<String, Object> data) {
	if ((data == null) || (data.isEmpty())) {
		return StringPool.BLANK;
	}

	StringBundler sb = new StringBundler(data.size() * 5);

	for (Map.Entry<String, Object> entry : data.entrySet()) {
		String dataKey = entry.getKey();
		String dataValue = String.valueOf(entry.getValue());

		sb.append("data-");
		sb.append(dataKey);
		sb.append("=\"");
		sb.append(dataValue);
		sb.append("\" ");
	}

	return sb.toString();
}

private static String _buildDynamicAttributes(Map<String, Object> dynamicAttributes) {
	if ((dynamicAttributes == null) || dynamicAttributes.isEmpty()) {
		return StringPool.BLANK;
	}

	StringBundler sb = new StringBundler(dynamicAttributes.size() * 4);

	for (Map.Entry<String, Object> entry : dynamicAttributes.entrySet()) {
		String key = entry.getKey();
		String value = String.valueOf(entry.getValue());

		if (!key.equals("class")) {
			sb.append(key);
			sb.append("=\"");
			sb.append(value);
			sb.append("\" ");
		}
	}

	return sb.toString();
}

private static String _buildLabel(String inlineLabel, boolean showForLabel, String forLabel) {
	StringBundler sb = new StringBundler();

	sb.append("class=\"" + LABEL_PREFIX);

	if (Validator.isNotNull(inlineLabel) && !inlineLabel.equals("right")) {
		sb.append("-inline-label");
	}

	sb.append("\"");

	if (showForLabel) {
		sb.append(" for=\"" + forLabel + "\"");
	}

	return sb.toString();
}


private RatingsEntry getRatingsEntry(List<RatingsEntry> ratingEntries, long classPK) {
	for (RatingsEntry ratingsEntry : ratingEntries) {
		if (ratingsEntry.getClassPK() == classPK) {
			return ratingsEntry;
		}
	}

	return RatingsEntryUtil.create(0);
}

private RatingsStats getRatingsStats(List<RatingsStats> ratingsStatsList, long classPK) {
	for (RatingsStats ratingsStats : ratingsStatsList) {
		if (ratingsStats.getClassPK() == classPK) {
			return ratingsStats;
		}
	}

	return RatingsStatsUtil.create(0);
}

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(26);
    _jspx_dependants.add("/html/taglib/init.jsp");
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/taglib/init-ext.jsp");
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
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fuser_002ddisplay_0026_005fuserName_005fuserId_005fdisplayStyle_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fworkflow_002dstatus_0026_005fstatus_005fmodel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fratings_0026_005ftype_005fratingsStats_005fratingsEntry_005fclassPK_005fclassName_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fuser_002ddisplay_0026_005fuserName_005fuserId_005fdisplayStyle_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fworkflow_002dstatus_0026_005fstatus_005fmodel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fratings_0026_005ftype_005fratingsStats_005fratingsEntry_005fclassPK_005fclassName_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fotherwise.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fuser_002ddisplay_0026_005fuserName_005fuserId_005fdisplayStyle_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fworkflow_002dstatus_0026_005fstatus_005fmodel_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fratings_0026_005ftype_005fratingsStats_005fratingsEntry_005fclassPK_005fclassName_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fscript.release();
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

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
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

PortletRequest portletRequest = (PortletRequest)request.getAttribute(JavaConstants.JAVAX_PORTLET_REQUEST);

PortletResponse portletResponse = (PortletResponse)request.getAttribute(JavaConstants.JAVAX_PORTLET_RESPONSE);

String namespace = StringPool.BLANK;

boolean useNamespace = GetterUtil.getBoolean((String)request.getAttribute("aui:form:useNamespace"), true);

if ((portletResponse != null) && useNamespace) {
	namespace = portletResponse.getNamespace();
}

String currentURL = PortalUtil.getCurrentURL(request);

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
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
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

String randomNamespace = PortalUtil.generateRandomKey(request, "taglib_ui_discussion_page") + StringPool.UNDERLINE;

String className = (String)request.getAttribute("liferay-ui:discussion:className");
long classPK = GetterUtil.getLong((String)request.getAttribute("liferay-ui:discussion:classPK"));
String formAction = (String)request.getAttribute("liferay-ui:discussion:formAction");
String formName = namespace + request.getAttribute("liferay-ui:discussion:formName");
String permissionClassName = (String)request.getAttribute("liferay-ui:discussion:permissionClassName");
long permissionClassPK = GetterUtil.getLong((String)request.getAttribute("liferay-ui:discussion:permissionClassPK"));
boolean ratingsEnabled = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:discussion:ratingsEnabled"));
String redirect = (String)request.getAttribute("liferay-ui:discussion:redirect");
long userId = GetterUtil.getLong((String)request.getAttribute("liferay-ui:discussion:userId"));

String threadView = PropsValues.DISCUSSION_THREAD_VIEW;

MBMessageDisplay messageDisplay = MBMessageLocalServiceUtil.getDiscussionMessageDisplay(userId, scopeGroupId, className, classPK, WorkflowConstants.STATUS_ANY, threadView);

MBCategory category = messageDisplay.getCategory();
MBThread thread = messageDisplay.getThread();
MBTreeWalker treeWalker = messageDisplay.getTreeWalker();
MBMessage rootMessage = null;
List<MBMessage> messages = null;
int messagesCount = 0;

if (treeWalker != null) {
	rootMessage = treeWalker.getRoot();
	messages = treeWalker.getMessages();
	messagesCount = messages.size();
}
else {
	rootMessage = MBMessageLocalServiceUtil.getMessage(thread.getRootMessageId());
	messagesCount = MBMessageLocalServiceUtil.getThreadMessagesCount(rootMessage.getThreadId(), WorkflowConstants.STATUS_ANY);
}

Format dateFormatDateTime = FastDateFormatFactoryUtil.getDateTime(locale, timeZone);

      out.write('\n');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f0.setParent(null);
      // /html/taglib/ui/discussion/page.jsp(76,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f0.setTest( (messagesCount > 1) || MBDiscussionPermission.contains(permissionChecker, company.getCompanyId(), scopeGroupId, permissionClassName, permissionClassPK, ActionKeys.VIEW) );
      int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
      if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\n");
          out.write("\t<div class=\"taglib-discussion\">\n");
          out.write("\t\t<form action=\"");
          out.print( HtmlUtil.escape(formAction) );
          out.write("\" method=\"post\" name=\"");
          out.print( formName );
          out.write("\">\n");
          out.write("\t\t<input name=\"");
          out.print( namespace );
          out.print( Constants.CMD );
          out.write("\" type=\"hidden\" value=\"\" />\n");
          out.write("\t\t<input name=\"");
          out.print( namespace );
          out.write("redirect\" type=\"hidden\" value=\"");
          out.print( HtmlUtil.escapeAttribute(redirect) );
          out.write("\" />\n");
          out.write("\t\t<input name=\"");
          out.print( namespace );
          out.write("className\" type=\"hidden\" value=\"");
          out.print( className );
          out.write("\" />\n");
          out.write("\t\t<input name=\"");
          out.print( namespace );
          out.write("classPK\" type=\"hidden\" value=\"");
          out.print( classPK );
          out.write("\" />\n");
          out.write("\t\t<input name=\"");
          out.print( namespace );
          out.write("permissionClassName\" type=\"hidden\" value=\"");
          out.print( permissionClassName );
          out.write("\" />\n");
          out.write("\t\t<input name=\"");
          out.print( namespace );
          out.write("permissionClassPK\" type=\"hidden\" value=\"");
          out.print( permissionClassPK );
          out.write("\" />\n");
          out.write("\t\t<input name=\"");
          out.print( namespace );
          out.write("messageId\" type=\"hidden\" value=\"\" />\n");
          out.write("\t\t<input name=\"");
          out.print( namespace );
          out.write("threadId\" type=\"hidden\" value=\"");
          out.print( thread.getThreadId() );
          out.write("\" />\n");
          out.write("\t\t<input name=\"");
          out.print( namespace );
          out.write("parentMessageId\" type=\"hidden\" value=\"\" />\n");
          out.write("\t\t<input name=\"");
          out.print( namespace );
          out.write("body\" type=\"hidden\" value=\"\" />\n");
          out.write("\n");
          out.write("\t\t");

		int i = 0;

		MBMessage message = rootMessage;
		
          out.write("\n");
          out.write("\n");
          out.write("\t\t");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
          // /html/taglib/ui/discussion/page.jsp(96,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f1.setTest( MBDiscussionPermission.contains(permissionChecker, company.getCompanyId(), scopeGroupId, permissionClassName, permissionClassPK, ActionKeys.ADD_DISCUSSION) );
          int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
          if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t\t<table class=\"add-comment\" id=\"");
              out.print( randomNamespace );
              out.write("messageScroll0\">\n");
              out.write("\t\t\t<tr>\n");
              out.write("\t\t\t\t<td id=\"");
              out.print( randomNamespace );
              out.write("messageScroll");
              out.print( message.getMessageId() );
              out.write("\">\n");
              out.write("\t\t\t\t\t<input name=\"");
              out.print( namespace );
              out.write("messageId");
              out.print( i );
              out.write("\" type=\"hidden\" value=\"");
              out.print( message.getMessageId() );
              out.write("\" />\n");
              out.write("\t\t\t\t\t<input name=\"");
              out.print( namespace );
              out.write("parentMessageId");
              out.print( i );
              out.write("\" type=\"hidden\" value=\"");
              out.print( message.getMessageId() );
              out.write("\" />\n");
              out.write("\t\t\t\t</td>\n");
              out.write("\t\t\t</tr>\n");
              out.write("\t\t\t<tr>\n");
              out.write("\t\t\t\t<td>\n");
              out.write("\n");
              out.write("\t\t\t\t\t");

					String taglibPostReplyURL = "javascript:" + randomNamespace + "showForm('" + randomNamespace + "postReplyForm" + i + "', '" + randomNamespace + "postReplyBody" + i + "');";
					
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t");
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f0 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f0.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
              int _jspx_eval_c_005fchoose_005f0 = _jspx_th_c_005fchoose_005f0.doStartTag();
              if (_jspx_eval_c_005fchoose_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f0 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
                  // /html/taglib/ui/discussion/page.jsp(112,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f0.setTest( messagesCount == 1 );
                  int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_th_c_005fwhen_005f0, _jspx_page_context))
                        return;
                      out.write(" <a href=\"");
                      out.print( taglibPostReplyURL );
                      out.write('"');
                      out.write('>');
                      if (_jspx_meth_liferay_002dui_005fmessage_005f1(_jspx_th_c_005fwhen_005f0, _jspx_page_context))
                        return;
                      out.write("</a>\n");
                      out.write("\t\t\t\t\t\t");
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
                  out.write("\t\t\t\t\t\t");
                  //  c:otherwise
                  org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                  _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
                  int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
                  if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t");
                      //  liferay-ui:icon
                      com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f0 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                      _jspx_th_liferay_002dui_005ficon_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ficon_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
                      // /html/taglib/ui/discussion/page.jsp(116,7) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f0.setImage("reply");
                      // /html/taglib/ui/discussion/page.jsp(116,7) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f0.setLabel( true );
                      // /html/taglib/ui/discussion/page.jsp(116,7) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f0.setMessage("add-comment");
                      // /html/taglib/ui/discussion/page.jsp(116,7) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f0.setUrl( taglibPostReplyURL );
                      int _jspx_eval_liferay_002dui_005ficon_005f0 = _jspx_th_liferay_002dui_005ficon_005f0.doStartTag();
                      if (_jspx_th_liferay_002dui_005ficon_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
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
                  out.write("\t\t\t\t\t");
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
              out.write("\n");
              out.write("\t\t\t\t</td>\n");
              out.write("\t\t\t</tr>\n");
              out.write("\t\t\t<tr id=\"");
              out.print( randomNamespace );
              out.write("postReplyForm");
              out.print( i );
              out.write("\" style=\"display: none;\">\n");
              out.write("\t\t\t\t<td>\n");
              out.write("\t\t\t\t\t<br />\n");
              out.write("\n");
              out.write("\t\t\t\t\t<div>\n");
              out.write("\t\t\t\t\t\t<textarea id=\"");
              out.print( randomNamespace );
              out.write("postReplyBody");
              out.print( i );
              out.write("\" name=\"");
              out.print( namespace );
              out.write("postReplyBody");
              out.print( i );
              out.write("\" style=\"height: ");
              out.print( ModelHintsConstants.TEXTAREA_DISPLAY_HEIGHT );
              out.write("px; width: ");
              out.print( ModelHintsConstants.TEXTAREA_DISPLAY_WIDTH );
              out.write("px;\" wrap=\"soft\"></textarea>\n");
              out.write("\t\t\t\t\t</div>\n");
              out.write("\n");
              out.write("\t\t\t\t\t<br />\n");
              out.write("\n");
              out.write("\t\t\t\t\t");

					String postReplyButtonLabel = LanguageUtil.get(pageContext, "reply");

					if (WorkflowDefinitionLinkLocalServiceUtil.hasWorkflowDefinitionLink(themeDisplay.getCompanyId(), scopeGroupId, MBDiscussion.class.getName())) {
						postReplyButtonLabel = LanguageUtil.get(pageContext, "submit-for-publication");
					}
					
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t<input disabled=\"disabled\" id=\"");
              out.print( randomNamespace );
              out.write("postReplyButton");
              out.print( i );
              out.write("\" type=\"button\" value=\"");
              out.print( postReplyButtonLabel );
              out.write("\" onClick=\"");
              out.print( randomNamespace );
              out.write("postReply(");
              out.print( i );
              out.write(");\" />\n");
              out.write("\n");
              out.write("\t\t\t\t\t<input type=\"button\" value=\"");
              if (_jspx_meth_liferay_002dui_005fmessage_005f2(_jspx_th_c_005fif_005f1, _jspx_page_context))
                return;
              out.write("\" onClick=\"document.getElementById('");
              out.print( randomNamespace );
              out.write("postReplyForm");
              out.print( i );
              out.write("').style.display = 'none'; void('');\" />\n");
              out.write("\t\t\t\t</td>\n");
              out.write("\t\t\t</tr>\n");
              out.write("\t\t\t</table>\n");
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
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
          // /html/taglib/ui/discussion/page.jsp(152,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f2.setTest( messagesCount > 1 );
          int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
          if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
              // /html/taglib/ui/discussion/page.jsp(153,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f3.setTest( MBDiscussionPermission.contains(permissionChecker, company.getCompanyId(), scopeGroupId, permissionClassName, permissionClassPK, ActionKeys.ADD_DISCUSSION) );
              int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
              if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t<br />\n");
                  out.write("\t\t\t");
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
              out.write("\t\t\t<a name=\"");
              out.print( randomNamespace );
              out.write("messages_top\"></a>\n");
              out.write("\n");
              out.write("\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
              // /html/taglib/ui/discussion/page.jsp(159,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f4.setTest( treeWalker != null );
              int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
              if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t<table class=\"tree-walker\">\n");
                  out.write("\t\t\t\t<tr class=\"portlet-section-header results-header\" style=\"font-size: x-small; font-weight: bold;\">\n");
                  out.write("\t\t\t\t\t<td colspan=\"2\">\n");
                  out.write("\t\t\t\t\t\t");
                  if (_jspx_meth_liferay_002dui_005fmessage_005f3(_jspx_th_c_005fif_005f4, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t\t\t</td>\n");
                  out.write("\t\t\t\t\t<td colspan=\"2\">\n");
                  out.write("\t\t\t\t\t\t");
                  if (_jspx_meth_liferay_002dui_005fmessage_005f4(_jspx_th_c_005fif_005f4, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t\t\t</td>\n");
                  out.write("\t\t\t\t\t<td>\n");
                  out.write("\t\t\t\t\t\t");
                  if (_jspx_meth_liferay_002dui_005fmessage_005f5(_jspx_th_c_005fif_005f4, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t\t\t</td>\n");
                  out.write("\t\t\t\t</tr>\n");
                  out.write("\n");
                  out.write("\t\t\t\t");

				int[] range = treeWalker.getChildrenRange(rootMessage);

				for (i = range[0]; i < range[1]; i++) {
					message = (MBMessage)messages.get(i);

					boolean lastChildNode = false;

					if ((i + 1) == range[1]) {
						lastChildNode = true;
					}

					request.setAttribute(WebKeys.MESSAGE_BOARDS_TREE_WALKER, treeWalker);
					request.setAttribute(WebKeys.MESSAGE_BOARDS_TREE_WALKER_SEL_MESSAGE, rootMessage);
					request.setAttribute(WebKeys.MESSAGE_BOARDS_TREE_WALKER_CUR_MESSAGE, message);
					request.setAttribute(WebKeys.MESSAGE_BOARDS_TREE_WALKER_CATEGORY, category);
					request.setAttribute(WebKeys.MESSAGE_BOARDS_TREE_WALKER_THREAD, thread);
					request.setAttribute(WebKeys.MESSAGE_BOARDS_TREE_WALKER_LAST_NODE, Boolean.valueOf(lastChildNode));
					request.setAttribute(WebKeys.MESSAGE_BOARDS_TREE_WALKER_DEPTH, new Integer(0));
				
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  if (_jspx_meth_liferay_002dutil_005finclude_005f0(_jspx_th_c_005fif_005f4, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t");

				}
				
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t</table>\n");
                  out.write("\n");
                  out.write("\t\t\t\t<br />\n");
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
              out.write("\n");
              out.write("\t\t\t<table class=\"lfr-grid lfr-table\">\n");
              out.write("\n");
              out.write("\t\t\t");

			SearchContainer searchContainer = null;

			if (messages != null) {
				messages = ListUtil.sort(messages, new MessageCreateDateComparator(true));

				messages = ListUtil.copy(messages);

				messages.remove(0);
			}
			else {
				PortletURL currentURLObj = PortletURLUtil.getCurrent(renderRequest, renderResponse);

				searchContainer = new SearchContainer(renderRequest, null, null, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_DELTA, currentURLObj, null, null);

				searchContainer.setTotal(messagesCount - 1);

				messages = MBMessageLocalServiceUtil.getThreadRepliesMessages(message.getThreadId(), WorkflowConstants.STATUS_ANY, searchContainer.getStart(), searchContainer.getEnd());

				searchContainer.setResults(messages);
			}

			List<Long> classPKs = new ArrayList<Long>();

			for (MBMessage curMessage : messages) {
				classPKs.add(curMessage.getMessageId());
			}

			List<RatingsEntry> ratingsEntries = RatingsEntryLocalServiceUtil.getEntries(themeDisplay.getUserId(), MBMessage.class.getName(), classPKs);
			List<RatingsStats> ratingsStatsList = RatingsStatsLocalServiceUtil.getStats(MBMessage.class.getName(), classPKs);

			for (i = 1; i <= messages.size(); i++) {
				message = messages.get(i - 1);

				if ((!message.isApproved() && (message.getUserId() != user.getUserId()) && !permissionChecker.isCommunityAdmin(scopeGroupId)) || !MBDiscussionPermission.contains(permissionChecker, company.getCompanyId(), scopeGroupId, permissionClassName, permissionClassPK, ActionKeys.VIEW)) {
					continue;
				}
			
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t<tr>\n");
              out.write("\t\t\t\t\t<td colspan=\"2\" id=\"");
              out.print( randomNamespace );
              out.write("messageScroll");
              out.print( message.getMessageId() );
              out.write("\">\n");
              out.write("\t\t\t\t\t\t<a name=\"");
              out.print( randomNamespace );
              out.write("message_");
              out.print( message.getMessageId() );
              out.write("\"></a>\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t<input name=\"");
              out.print( namespace );
              out.write("messageId");
              out.print( i );
              out.write("\" type=\"hidden\" value=\"");
              out.print( message.getMessageId() );
              out.write("\" />\n");
              out.write("\t\t\t\t\t\t<input name=\"");
              out.print( namespace );
              out.write("parentMessageId");
              out.print( i );
              out.write("\" type=\"hidden\" value=\"");
              out.print( message.getMessageId() );
              out.write("\" />\n");
              out.write("\t\t\t\t\t</td>\n");
              out.write("\t\t\t\t</tr>\n");
              out.write("\t\t\t\t<tr>\n");
              out.write("\t\t\t\t\t<td class=\"lfr-center lfr-top\">\n");
              out.write("\t\t\t\t\t\t");
              //  liferay-ui:user-display
              com.liferay.portal.model.User userDisplay = null;
              com.liferay.taglib.ui.UserDisplayTag _jspx_th_liferay_002dui_005fuser_002ddisplay_005f0 = (com.liferay.taglib.ui.UserDisplayTag) _005fjspx_005ftagPool_005fliferay_002dui_005fuser_002ddisplay_0026_005fuserName_005fuserId_005fdisplayStyle_005fnobody.get(com.liferay.taglib.ui.UserDisplayTag.class);
              _jspx_th_liferay_002dui_005fuser_002ddisplay_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005fuser_002ddisplay_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
              // /html/taglib/ui/discussion/page.jsp(256,6) name = userId type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fuser_002ddisplay_005f0.setUserId( message.getUserId() );
              // /html/taglib/ui/discussion/page.jsp(256,6) name = userName type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fuser_002ddisplay_005f0.setUserName( HtmlUtil.escape(message.getUserName()) );
              // /html/taglib/ui/discussion/page.jsp(256,6) name = displayStyle type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fuser_002ddisplay_005f0.setDisplayStyle( 2 );
              int _jspx_eval_liferay_002dui_005fuser_002ddisplay_005f0 = _jspx_th_liferay_002dui_005fuser_002ddisplay_005f0.doStartTag();
              userDisplay = (com.liferay.portal.model.User) _jspx_page_context.findAttribute("userDisplay");
              if (_jspx_th_liferay_002dui_005fuser_002ddisplay_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005fuser_002ddisplay_0026_005fuserName_005fuserId_005fdisplayStyle_005fnobody.reuse(_jspx_th_liferay_002dui_005fuser_002ddisplay_005f0);
                return;
              }
              userDisplay = (com.liferay.portal.model.User) _jspx_page_context.findAttribute("userDisplay");
              _005fjspx_005ftagPool_005fliferay_002dui_005fuser_002ddisplay_0026_005fuserName_005fuserId_005fdisplayStyle_005fnobody.reuse(_jspx_th_liferay_002dui_005fuser_002ddisplay_005f0);
              out.write("\n");
              out.write("\t\t\t\t\t</td>\n");
              out.write("\t\t\t\t\t<td class=\"lfr-top stretch\">\n");
              out.write("\t\t\t\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f5 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
              // /html/taglib/ui/discussion/page.jsp(263,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f5.setTest( (message != null) && !message.isApproved() );
              int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
              if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t");
                  //  aui:model-context
                  com.liferay.taglib.aui.ModelContextTag _jspx_th_aui_005fmodel_002dcontext_005f0 = (com.liferay.taglib.aui.ModelContextTag) _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody.get(com.liferay.taglib.aui.ModelContextTag.class);
                  _jspx_th_aui_005fmodel_002dcontext_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fmodel_002dcontext_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
                  // /html/taglib/ui/discussion/page.jsp(264,7) name = bean type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fmodel_002dcontext_005f0.setBean( message );
                  // /html/taglib/ui/discussion/page.jsp(264,7) name = model type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fmodel_002dcontext_005f0.setModel( MBMessage.class );
                  int _jspx_eval_aui_005fmodel_002dcontext_005f0 = _jspx_th_aui_005fmodel_002dcontext_005f0.doStartTag();
                  if (_jspx_th_aui_005fmodel_002dcontext_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody.reuse(_jspx_th_aui_005fmodel_002dcontext_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody.reuse(_jspx_th_aui_005fmodel_002dcontext_005f0);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t<div>\n");
                  out.write("\t\t\t\t\t\t\t\t");
                  //  aui:workflow-status
                  com.liferay.taglib.aui.WorkflowStatusTag _jspx_th_aui_005fworkflow_002dstatus_005f0 = (com.liferay.taglib.aui.WorkflowStatusTag) _005fjspx_005ftagPool_005faui_005fworkflow_002dstatus_0026_005fstatus_005fmodel_005fnobody.get(com.liferay.taglib.aui.WorkflowStatusTag.class);
                  _jspx_th_aui_005fworkflow_002dstatus_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fworkflow_002dstatus_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
                  // /html/taglib/ui/discussion/page.jsp(267,8) name = model type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fworkflow_002dstatus_005f0.setModel( MBDiscussion.class );
                  // /html/taglib/ui/discussion/page.jsp(267,8) name = status type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fworkflow_002dstatus_005f0.setStatus( message.getStatus() );
                  int _jspx_eval_aui_005fworkflow_002dstatus_005f0 = _jspx_th_aui_005fworkflow_002dstatus_005f0.doStartTag();
                  if (_jspx_th_aui_005fworkflow_002dstatus_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fworkflow_002dstatus_0026_005fstatus_005fmodel_005fnobody.reuse(_jspx_th_aui_005fworkflow_002dstatus_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fworkflow_002dstatus_0026_005fstatus_005fmodel_005fnobody.reuse(_jspx_th_aui_005fworkflow_002dstatus_005f0);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t</div>\n");
                  out.write("\t\t\t\t\t\t");
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
              out.write("\t\t\t\t\t\t<div>\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t");

							String msgBody = BBCodeUtil.getHTML(message);

							msgBody = StringUtil.replace(msgBody, "@theme_images_path@/emoticons", themeDisplay.getPathThemeImages() + "/emoticons");
							msgBody = HtmlUtil.wordBreak(msgBody, 80);
							
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t");
              out.print( msgBody );
              out.write("\n");
              out.write("\t\t\t\t\t\t</div>\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t<br />\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t<div>\n");
              out.write("\t\t\t\t\t\t\t");
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
              int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
              if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f1 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                  // /html/taglib/ui/discussion/page.jsp(287,8) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f1.setTest( message.getParentMessageId() == rootMessage.getMessageId() );
                  int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t\t");
                      out.print( LanguageUtil.format(pageContext, "posted-on-x", dateFormatDateTime.format(message.getModifiedDate())) );
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t");
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
                  out.write("\t\t\t\t\t\t\t\t");
                  //  c:otherwise
                  org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f1 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                  _jspx_th_c_005fotherwise_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fotherwise_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                  int _jspx_eval_c_005fotherwise_005f1 = _jspx_th_c_005fotherwise_005f1.doStartTag();
                  if (_jspx_eval_c_005fotherwise_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t\t");

									MBMessage parentMessage = MBMessageLocalServiceUtil.getMessage(message.getParentMessageId());

									StringBundler sb = new StringBundler(7);

									sb.append("<a href=\"#");
									sb.append(randomNamespace);
									sb.append("message_");
									sb.append(parentMessage.getMessageId());
									sb.append("\">");
									sb.append(HtmlUtil.escape(parentMessage.getUserName()));
									sb.append("</a>");
									
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t\t");
                      out.print( LanguageUtil.format(pageContext, "posted-on-x-in-reply-to-x", new Object[] {dateFormatDateTime.format(message.getModifiedDate()), sb.toString()}) );
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
              out.write("\t\t\t\t\t\t</div>\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t<br />\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t<table class=\"lfr-table\">\n");
              out.write("\t\t\t\t\t\t<tr>\n");
              out.write("\t\t\t\t\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f6 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f6.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
              // /html/taglib/ui/discussion/page.jsp(315,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f6.setTest( ratingsEnabled );
              int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
              if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t");

								RatingsEntry ratingsEntry = getRatingsEntry(ratingsEntries, message.getMessageId());
								RatingsStats ratingStats = getRatingsStats(ratingsStatsList, message.getMessageId());
								
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t<td>\n");
                  out.write("\t\t\t\t\t\t\t\t\t");
                  //  liferay-ui:ratings
                  com.liferay.taglib.ui.RatingsTag _jspx_th_liferay_002dui_005fratings_005f0 = (com.liferay.taglib.ui.RatingsTag) _005fjspx_005ftagPool_005fliferay_002dui_005fratings_0026_005ftype_005fratingsStats_005fratingsEntry_005fclassPK_005fclassName_005fnobody.get(com.liferay.taglib.ui.RatingsTag.class);
                  _jspx_th_liferay_002dui_005fratings_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fratings_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
                  // /html/taglib/ui/discussion/page.jsp(323,9) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fratings_005f0.setClassName( MBMessage.class.getName() );
                  // /html/taglib/ui/discussion/page.jsp(323,9) name = classPK type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fratings_005f0.setClassPK( message.getMessageId() );
                  // /html/taglib/ui/discussion/page.jsp(323,9) name = ratingsEntry type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fratings_005f0.setRatingsEntry( ratingsEntry );
                  // /html/taglib/ui/discussion/page.jsp(323,9) name = ratingsStats type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fratings_005f0.setRatingsStats( ratingStats );
                  // /html/taglib/ui/discussion/page.jsp(323,9) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fratings_005f0.setType("thumbs");
                  int _jspx_eval_liferay_002dui_005fratings_005f0 = _jspx_th_liferay_002dui_005fratings_005f0.doStartTag();
                  if (_jspx_th_liferay_002dui_005fratings_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fratings_0026_005ftype_005fratingsStats_005fratingsEntry_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fratings_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fratings_0026_005ftype_005fratingsStats_005fratingsEntry_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fratings_005f0);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t</td>\n");
                  out.write("\t\t\t\t\t\t\t");
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
              out.write("\n");
              out.write("\t\t\t\t\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f7 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f7.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
              // /html/taglib/ui/discussion/page.jsp(333,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f7.setTest( MBDiscussionPermission.contains(permissionChecker, company.getCompanyId(), scopeGroupId, permissionClassName, permissionClassPK, ActionKeys.ADD_DISCUSSION) );
              int _jspx_eval_c_005fif_005f7 = _jspx_th_c_005fif_005f7.doStartTag();
              if (_jspx_eval_c_005fif_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t<td>\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t");

									String taglibPostReplyURL = "javascript:" + randomNamespace + "showForm('" + randomNamespace + "postReplyForm" + i + "', '" + randomNamespace + "postReplyBody" + i + "');";
									
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t");
                  //  liferay-ui:icon
                  com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f1 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                  _jspx_th_liferay_002dui_005ficon_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005ficon_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
                  // /html/taglib/ui/discussion/page.jsp(340,9) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ficon_005f1.setImage("reply");
                  // /html/taglib/ui/discussion/page.jsp(340,9) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ficon_005f1.setLabel( true );
                  // /html/taglib/ui/discussion/page.jsp(340,9) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ficon_005f1.setMessage("post-reply");
                  // /html/taglib/ui/discussion/page.jsp(340,9) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ficon_005f1.setUrl( taglibPostReplyURL );
                  int _jspx_eval_liferay_002dui_005ficon_005f1 = _jspx_th_liferay_002dui_005ficon_005f1.doStartTag();
                  if (_jspx_th_liferay_002dui_005ficon_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fmessage_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t</td>\n");
                  out.write("\t\t\t\t\t\t\t");
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
              out.write("\t\t\t\t\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f8 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f8.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
              // /html/taglib/ui/discussion/page.jsp(349,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f8.setTest( i > 0 );
              int _jspx_eval_c_005fif_005f8 = _jspx_th_c_005fif_005f8.doStartTag();
              if (_jspx_eval_c_005fif_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t");

								String taglibTopURL = "#" + randomNamespace + "messages_top";
								
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t<td>\n");
                  out.write("\t\t\t\t\t\t\t\t\t");
                  //  liferay-ui:icon
                  com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f2 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                  _jspx_th_liferay_002dui_005ficon_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005ficon_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
                  // /html/taglib/ui/discussion/page.jsp(356,9) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ficon_005f2.setImage("top");
                  // /html/taglib/ui/discussion/page.jsp(356,9) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ficon_005f2.setLabel( true );
                  // /html/taglib/ui/discussion/page.jsp(356,9) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ficon_005f2.setUrl( taglibTopURL );
                  int _jspx_eval_liferay_002dui_005ficon_005f2 = _jspx_th_liferay_002dui_005ficon_005f2.doStartTag();
                  if (_jspx_th_liferay_002dui_005ficon_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f2);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f2);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t</td>\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f9 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f9.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
                  // /html/taglib/ui/discussion/page.jsp(363,8) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f9.setTest( MBDiscussionPermission.contains(permissionChecker, company.getCompanyId(), scopeGroupId, permissionClassName, permissionClassPK, message.getMessageId(), ActionKeys.UPDATE_DISCUSSION) );
                  int _jspx_eval_c_005fif_005f9 = _jspx_th_c_005fif_005f9.doStartTag();
                  if (_jspx_eval_c_005fif_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t\t");

									String taglibEditURL = "javascript:" + randomNamespace + "showForm('" + randomNamespace + "editForm" + i + "', '" + randomNamespace + "editReplyBody" + i + "');";
									
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t\t<td>\n");
                      out.write("\t\t\t\t\t\t\t\t\t\t");
                      //  liferay-ui:icon
                      com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f3 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                      _jspx_th_liferay_002dui_005ficon_005f3.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ficon_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f9);
                      // /html/taglib/ui/discussion/page.jsp(370,10) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f3.setImage("edit");
                      // /html/taglib/ui/discussion/page.jsp(370,10) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f3.setLabel( true );
                      // /html/taglib/ui/discussion/page.jsp(370,10) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_005f3.setUrl( taglibEditURL );
                      int _jspx_eval_liferay_002dui_005ficon_005f3 = _jspx_th_liferay_002dui_005ficon_005f3.doStartTag();
                      if (_jspx_th_liferay_002dui_005ficon_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f3);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005flabel_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f3);
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t\t</td>\n");
                      out.write("\t\t\t\t\t\t\t\t");
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
                  out.write("\t\t\t\t\t\t\t\t");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f10 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f10.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
                  // /html/taglib/ui/discussion/page.jsp(378,8) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f10.setTest( MBDiscussionPermission.contains(permissionChecker, company.getCompanyId(), scopeGroupId, permissionClassName, permissionClassPK, message.getMessageId(), ActionKeys.DELETE_DISCUSSION) );
                  int _jspx_eval_c_005fif_005f10 = _jspx_th_c_005fif_005f10.doStartTag();
                  if (_jspx_eval_c_005fif_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t\t");

									String taglibDeleteURL = "javascript:" + randomNamespace + "deleteMessage(" + i + ");";
									
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t\t<td>\n");
                      out.write("\t\t\t\t\t\t\t\t\t\t");
                      //  liferay-ui:icon-delete
                      com.liferay.taglib.ui.IconDeleteTag _jspx_th_liferay_002dui_005ficon_002ddelete_005f0 = (com.liferay.taglib.ui.IconDeleteTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005flabel_005fnobody.get(com.liferay.taglib.ui.IconDeleteTag.class);
                      _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f10);
                      // /html/taglib/ui/discussion/page.jsp(385,10) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.setLabel( true );
                      // /html/taglib/ui/discussion/page.jsp(385,10) name = url type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.setUrl( taglibDeleteURL );
                      int _jspx_eval_liferay_002dui_005ficon_002ddelete_005f0 = _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.doStartTag();
                      if (_jspx_th_liferay_002dui_005ficon_002ddelete_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005flabel_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002ddelete_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005flabel_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002ddelete_005f0);
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t\t</td>\n");
                      out.write("\t\t\t\t\t\t\t\t");
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
                  out.write("\t\t\t\t\t\t\t");
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
              out.write("\t\t\t\t\t\t</tr>\n");
              out.write("\t\t\t\t\t\t</table>\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t<table class=\"lfr-table\">\n");
              out.write("\t\t\t\t\t\t<tr id=\"");
              out.print( randomNamespace );
              out.write("postReplyForm");
              out.print( i );
              out.write("\" style=\"display: none;\">\n");
              out.write("\t\t\t\t\t\t\t<td>\n");
              out.write("\t\t\t\t\t\t\t\t<br />\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t<div>\n");
              out.write("\t\t\t\t\t\t\t\t\t<textarea id=\"");
              out.print( randomNamespace );
              out.write("postReplyBody");
              out.print( i );
              out.write("\" name=\"");
              out.print( namespace );
              out.write("postReplyBody");
              out.print( i );
              out.write("\" style=\"height: ");
              out.print( ModelHintsConstants.TEXTAREA_DISPLAY_HEIGHT );
              out.write("px; width: ");
              out.print( ModelHintsConstants.TEXTAREA_DISPLAY_WIDTH );
              out.write("px;\" wrap=\"soft\"></textarea>\n");
              out.write("\t\t\t\t\t\t\t\t</div>\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t<br />\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t<input disabled=\"disabled\" id=\"");
              out.print( randomNamespace );
              out.write("postReplyButton");
              out.print( i );
              out.write("\" type=\"button\" value=\"");
              if (_jspx_meth_liferay_002dui_005fmessage_005f6(_jspx_th_c_005fif_005f2, _jspx_page_context))
                return;
              out.write("\" onClick=\"");
              out.print( randomNamespace );
              out.write("postReply(");
              out.print( i );
              out.write(");\" />\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t<input type=\"button\" value=\"");
              if (_jspx_meth_liferay_002dui_005fmessage_005f7(_jspx_th_c_005fif_005f2, _jspx_page_context))
                return;
              out.write("\" onClick=\"document.getElementById('");
              out.print( randomNamespace );
              out.write("postReplyForm");
              out.print( i );
              out.write("').style.display = 'none'; void('');\" />\n");
              out.write("\t\t\t\t\t\t\t</td>\n");
              out.write("\t\t\t\t\t\t</tr>\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f11 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f11.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
              // /html/taglib/ui/discussion/page.jsp(412,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f11.setTest( MBDiscussionPermission.contains(permissionChecker, company.getCompanyId(), scopeGroupId, permissionClassName, permissionClassPK, message.getMessageId(), ActionKeys.UPDATE_DISCUSSION) );
              int _jspx_eval_c_005fif_005f11 = _jspx_th_c_005fif_005f11.doStartTag();
              if (_jspx_eval_c_005fif_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t<tr id=\"");
                  out.print( randomNamespace );
                  out.write("editForm");
                  out.print( i );
                  out.write("\" style=\"display: none;\">\n");
                  out.write("\t\t\t\t\t\t\t\t<td>\n");
                  out.write("\t\t\t\t\t\t\t\t\t<br />\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t<div>\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t<textarea id=\"");
                  out.print( randomNamespace );
                  out.write("editReplyBody");
                  out.print( i );
                  out.write("\" name=\"");
                  out.print( namespace );
                  out.write("editReplyBody");
                  out.print( i );
                  out.write("\" style=\"height: ");
                  out.print( ModelHintsConstants.TEXTAREA_DISPLAY_HEIGHT );
                  out.write("px; width: ");
                  out.print( ModelHintsConstants.TEXTAREA_DISPLAY_WIDTH );
                  out.write("px;\" wrap=\"soft\">");
                  out.print( HtmlUtil.escape(message.getBody()) );
                  out.write("</textarea>\n");
                  out.write("\t\t\t\t\t\t\t\t\t</div>\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t<br />\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t");

									String publishButtonLabel = LanguageUtil.get(pageContext, "publish");

									if (WorkflowDefinitionLinkLocalServiceUtil.hasWorkflowDefinitionLink(themeDisplay.getCompanyId(), scopeGroupId, MBDiscussion.class.getName())) {
										publishButtonLabel = LanguageUtil.get(pageContext, "submit-for-publication");
									}
									
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t<input id=\"");
                  out.print( randomNamespace );
                  out.write("editReplyButton");
                  out.print( i );
                  out.write("\" onClick=\"");
                  out.print( randomNamespace );
                  out.write("updateMessage(");
                  out.print( i );
                  out.write(");\" type=\"button\" value=\"");
                  //  liferay-ui:message
                  com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f8 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
                  _jspx_th_liferay_002dui_005fmessage_005f8.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fmessage_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f11);
                  // /html/taglib/ui/discussion/page.jsp(431,145) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fmessage_005f8.setKey( publishButtonLabel );
                  int _jspx_eval_liferay_002dui_005fmessage_005f8 = _jspx_th_liferay_002dui_005fmessage_005f8.doStartTag();
                  if (_jspx_th_liferay_002dui_005fmessage_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f8);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f8);
                  out.write("\" />\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t<input type=\"button\" value=\"");
                  if (_jspx_meth_liferay_002dui_005fmessage_005f9(_jspx_th_c_005fif_005f11, _jspx_page_context))
                    return;
                  out.write("\" onClick=\"document.getElementById('");
                  out.print( randomNamespace );
                  out.write("editForm");
                  out.print( i );
                  out.write("').style.display = 'none'; void('');\" />\n");
                  out.write("\t\t\t\t\t\t\t\t</td>\n");
                  out.write("\t\t\t\t\t\t\t</tr>\n");
                  out.write("\t\t\t\t\t\t");
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
              out.write("\t\t\t\t\t\t</table>\n");
              out.write("\t\t\t\t\t</td>\n");
              out.write("\t\t\t\t</tr>\n");
              out.write("\n");
              out.write("\t\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f12 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f12.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
              // /html/taglib/ui/discussion/page.jsp(442,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f12.setTest( i < messages.size() );
              int _jspx_eval_c_005fif_005f12 = _jspx_th_c_005fif_005f12.doStartTag();
              if (_jspx_eval_c_005fif_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t<tr>\n");
                  out.write("\t\t\t\t\t\t<td colspan=\"2\">\n");
                  out.write("\t\t\t\t\t\t\t<div class=\"separator\"><!-- --></div>\n");
                  out.write("\t\t\t\t\t\t</td>\n");
                  out.write("\t\t\t\t\t</tr>\n");
                  out.write("\t\t\t\t");
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
              out.write("\n");
              out.write("\t\t\t");

			}
			
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t</table>\n");
              out.write("\n");
              out.write("\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f13 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f13.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
              // /html/taglib/ui/discussion/page.jsp(456,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f13.setTest( (searchContainer != null) && (searchContainer.getTotal() > searchContainer.getDelta()) );
              int _jspx_eval_c_005fif_005f13 = _jspx_th_c_005fif_005f13.doStartTag();
              if (_jspx_eval_c_005fif_005f13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t");
                  //  liferay-ui:search-paginator
                  com.liferay.taglib.ui.SearchPaginatorTag _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0 = (com.liferay.taglib.ui.SearchPaginatorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.get(com.liferay.taglib.ui.SearchPaginatorTag.class);
                  _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f13);
                  // /html/taglib/ui/discussion/page.jsp(457,4) name = searchContainer type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0.setSearchContainer( searchContainer );
                  int _jspx_eval_liferay_002dui_005fsearch_002dpaginator_005f0 = _jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0.doStartTag();
                  if (_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fsearch_002dpaginator_0026_005fsearchContainer_005fnobody.reuse(_jspx_th_liferay_002dui_005fsearch_002dpaginator_005f0);
                  out.write("\n");
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
          out.write("\t\t</form>\n");
          out.write("\t</div>\n");
          out.write("\n");
          out.write("\t");
          //  aui:script
          com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript.get(com.liferay.taglib.aui.ScriptTag.class);
          _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fscript_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
          int _jspx_eval_aui_005fscript_005f0 = _jspx_th_aui_005fscript_005f0.doStartTag();
          if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_aui_005fscript_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_aui_005fscript_005f0.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\t\tfunction ");
              out.print( randomNamespace );
              out.write("deleteMessage(i) {\n");
              out.write("\t\t\teval(\"var messageId = document.");
              out.print( formName );
              out.write('.');
              out.print( namespace );
              out.write("messageId\" + i + \".value;\");\n");
              out.write("\n");
              out.write("\t\t\tdocument.");
              out.print( formName );
              out.write('.');
              out.print( namespace );
              out.print( Constants.CMD );
              out.write(".value = \"");
              out.print( Constants.DELETE );
              out.write("\";\n");
              out.write("\t\t\tdocument.");
              out.print( formName );
              out.write('.');
              out.print( namespace );
              out.write("messageId.value = messageId;\n");
              out.write("\t\t\tsubmitForm(document.");
              out.print( formName );
              out.write(");\n");
              out.write("\t\t}\n");
              out.write("\n");
              out.write("\t\tfunction ");
              out.print( randomNamespace );
              out.write("postReply(i) {\n");
              out.write("\t\t\teval(\"var parentMessageId = document.");
              out.print( formName );
              out.write('.');
              out.print( namespace );
              out.write("parentMessageId\" + i + \".value;\");\n");
              out.write("\t\t\teval(\"var body = document.");
              out.print( formName );
              out.write('.');
              out.print( namespace );
              out.write("postReplyBody\" + i + \".value;\");\n");
              out.write("\n");
              out.write("\t\t\tdocument.");
              out.print( formName );
              out.write('.');
              out.print( namespace );
              out.print( Constants.CMD );
              out.write(".value = \"");
              out.print( Constants.ADD );
              out.write("\";\n");
              out.write("\t\t\tdocument.");
              out.print( formName );
              out.write('.');
              out.print( namespace );
              out.write("parentMessageId.value = parentMessageId;\n");
              out.write("\t\t\tdocument.");
              out.print( formName );
              out.write('.');
              out.print( namespace );
              out.write("body.value = body;\n");
              out.write("\t\t\tsubmitForm(document.");
              out.print( formName );
              out.write(");\n");
              out.write("\t\t}\n");
              out.write("\n");
              out.write("\t\tfunction ");
              out.print( randomNamespace );
              out.write("scrollIntoView(messageId) {\n");
              out.write("\t\t\tdocument.getElementById(\"");
              out.print( namespace );
              out.write("messageScroll\" + messageId).scrollIntoView();\n");
              out.write("\t\t}\n");
              out.write("\n");
              out.write("\t\tfunction ");
              out.print( randomNamespace );
              out.write("showForm(rowId, textAreaId) {\n");
              out.write("\t\t\tdocument.getElementById(rowId).style.display = \"\";\n");
              out.write("\t\t\tdocument.getElementById(textAreaId).focus();\n");
              out.write("\t\t}\n");
              out.write("\n");
              out.write("\t\tfunction ");
              out.print( randomNamespace );
              out.write("updateMessage(i) {\n");
              out.write("\t\t\teval(\"var messageId = document.");
              out.print( formName );
              out.write('.');
              out.print( namespace );
              out.write("messageId\" + i + \".value;\");\n");
              out.write("\t\t\teval(\"var body = document.");
              out.print( formName );
              out.write('.');
              out.print( namespace );
              out.write("editReplyBody\" + i + \".value;\");\n");
              out.write("\n");
              out.write("\t\t\tdocument.");
              out.print( formName );
              out.write('.');
              out.print( namespace );
              out.print( Constants.CMD );
              out.write(".value = \"");
              out.print( Constants.UPDATE );
              out.write("\";\n");
              out.write("\t\t\tdocument.");
              out.print( formName );
              out.write('.');
              out.print( namespace );
              out.write("messageId.value = messageId;\n");
              out.write("\t\t\tdocument.");
              out.print( formName );
              out.write('.');
              out.print( namespace );
              out.write("body.value = body;\n");
              out.write("\t\t\tsubmitForm(document.");
              out.print( formName );
              out.write(");\n");
              out.write("\t\t}\n");
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
            _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f0);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f0);
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  aui:script
          com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f1 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
          _jspx_th_aui_005fscript_005f1.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fscript_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
          // /html/taglib/ui/discussion/page.jsp(503,1) name = use type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fscript_005f1.setUse("aui-event-input");
          int _jspx_eval_aui_005fscript_005f1 = _jspx_th_aui_005fscript_005f1.doStartTag();
          if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_aui_005fscript_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_aui_005fscript_005f1.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\t\tvar form = A.one(document.");
              out.print( formName );
              out.write(");\n");
              out.write("\n");
              out.write("\t\tif (form) {\n");
              out.write("\t\t\tvar textareas = form.all('textarea');\n");
              out.write("\n");
              out.write("\t\t\tif (textareas) {\n");
              out.write("\t\t\t\ttextareas.on(\n");
              out.write("\t\t\t\t\t'input',\n");
              out.write("\t\t\t\t\tfunction(event) {\n");
              out.write("\t\t\t\t\t\tvar textarea = event.currentTarget;\n");
              out.write("\t\t\t\t\t\tvar currentValue = A.Lang.trim(textarea.val());\n");
              out.write("\n");
              out.write("\t\t\t\t\t\tvar id = textarea.get('id');\n");
              out.write("\t\t\t\t\t\tvar buttonId = id.replace(/Body/, 'Button');\n");
              out.write("\t\t\t\t\t\tvar button = A.one('#' + buttonId);\n");
              out.write("\n");
              out.write("\t\t\t\t\t\tif (button) {\n");
              out.write("\t\t\t\t\t\t\tbutton.set('disabled', !currentValue.length);\n");
              out.write("\t\t\t\t\t\t}\n");
              out.write("\t\t\t\t\t}\n");
              out.write("\t\t\t\t);\n");
              out.write("\t\t\t}\n");
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
            return;
          }
          _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f1);
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

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
    // /html/taglib/ui/discussion/page.jsp(113,7) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("no-comments-yet");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f1 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
    // /html/taglib/ui/discussion/page.jsp(113,88) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f1.setKey("be-the-first");
    int _jspx_eval_liferay_002dui_005fmessage_005f1 = _jspx_th_liferay_002dui_005fmessage_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f2 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f2.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
    // /html/taglib/ui/discussion/page.jsp(146,33) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f2.setKey("cancel");
    int _jspx_eval_liferay_002dui_005fmessage_005f2 = _jspx_th_liferay_002dui_005fmessage_005f2.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f2);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f3 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f3.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
    // /html/taglib/ui/discussion/page.jsp(163,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f3.setKey("threaded-replies");
    int _jspx_eval_liferay_002dui_005fmessage_005f3 = _jspx_th_liferay_002dui_005fmessage_005f3.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f4 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f4.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
    // /html/taglib/ui/discussion/page.jsp(166,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f4.setKey("author");
    int _jspx_eval_liferay_002dui_005fmessage_005f4 = _jspx_th_liferay_002dui_005fmessage_005f4.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f5 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f5.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
    // /html/taglib/ui/discussion/page.jsp(169,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f5.setKey("date");
    int _jspx_eval_liferay_002dui_005fmessage_005f5 = _jspx_th_liferay_002dui_005fmessage_005f5.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f0 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
    // /html/taglib/ui/discussion/page.jsp(194,5) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f0.setPage("/html/taglib/ui/discussion/view_message_thread.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f0 = _jspx_th_liferay_002dutil_005finclude_005f0.doStartTag();
    if (_jspx_th_liferay_002dutil_005finclude_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage_005fnobody.reuse(_jspx_th_liferay_002dutil_005finclude_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f6 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f6.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
    // /html/taglib/ui/discussion/page.jsp(406,107) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f6.setKey("reply");
    int _jspx_eval_liferay_002dui_005fmessage_005f6 = _jspx_th_liferay_002dui_005fmessage_005f6.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f7 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f7.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
    // /html/taglib/ui/discussion/page.jsp(408,36) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f7.setKey("cancel");
    int _jspx_eval_liferay_002dui_005fmessage_005f7 = _jspx_th_liferay_002dui_005fmessage_005f7.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f11, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f9 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f9.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f11);
    // /html/taglib/ui/discussion/page.jsp(433,37) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f9.setKey("cancel");
    int _jspx_eval_liferay_002dui_005fmessage_005f9 = _jspx_th_liferay_002dui_005fmessage_005f9.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f9);
    return false;
  }
}
