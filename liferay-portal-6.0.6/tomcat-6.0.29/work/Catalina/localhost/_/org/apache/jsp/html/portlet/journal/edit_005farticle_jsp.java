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

public final class edit_005farticle_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


public static final String EDITOR_WYSIWYG_IMPL_KEY = "editor.wysiwyg.portal-web.docroot.html.portlet.journal.edit_article_content.jsp";

private void _format(long groupId, Element contentParentElement, Element xsdParentElement, IntegerWrapper count, Integer depth, boolean repeatablePrototype, PageContext pageContext, HttpServletRequest request) throws Exception {
	depth = new Integer(depth.intValue() + 1);

	String languageId = LanguageUtil.getLanguageId(request);

	List<Element> xsdElements = xsdParentElement.elements();

	for (Element xsdElement : xsdElements) {
		String nodeName = xsdElement.getName();

		if (nodeName.equals("meta-data") || nodeName.equals("entry")) {
			continue;
		}

		String elName = xsdElement.attributeValue("name", StringPool.BLANK);
		String elType = xsdElement.attributeValue("type", StringPool.BLANK);
		String elIndexType = xsdElement.attributeValue("index-type", StringPool.BLANK);
		String repeatable = xsdElement.attributeValue("repeatable");
		boolean elRepeatable = GetterUtil.getBoolean(repeatable);
		String elParentStructureId = xsdElement.attributeValue("parent-structure-id");

		Map<String, String> elMetaData = _getMetaData(xsdElement, elName);

		List<Element> elSiblings = null;

		List<Element> contentElements = contentParentElement.elements();

		for (Element contentElement : contentElements) {
			if (elName.equals(contentElement.attributeValue("name", StringPool.BLANK))) {
				elSiblings = _getSiblings(contentParentElement, elName);

				break;
			}
		}

		if (elSiblings == null) {
			elSiblings = new ArrayList<Element>();

			Element contentElement = SAXReaderUtil.createElement("dynamic-element");

			contentElement.addAttribute("instance-id", PwdGenerator.getPassword());
			contentElement.addAttribute("name", elName);
			contentElement.addAttribute("type", elType);
			contentElement.addAttribute("index-type", elIndexType);

			contentElement.add(SAXReaderUtil.createElement("dynamic-content"));

			elSiblings.add(contentElement);
		}

		for (int siblingIndex = 0; siblingIndex < elSiblings.size(); siblingIndex++) {
			Element contentElement = elSiblings.get(siblingIndex);

			String elInstanceId = contentElement.attributeValue("instance-id");

			String elContent = GetterUtil.getString(contentElement.elementText("dynamic-content"));

			if (!elType.equals("document_library") && !elType.equals("image_gallery") && !elType.equals("text") && !elType.equals("text_area") && !elType.equals("text_box")) {
				elContent = HtmlUtil.toInputSafe(elContent);
			}

			String elLanguageId = StringPool.BLANK;

			Element dynamicContentEl = contentElement.element("dynamic-content");

			if (dynamicContentEl != null) {
				elLanguageId = dynamicContentEl.attributeValue("language-id", StringPool.BLANK);
			}
			else {
				elLanguageId = languageId;
			}

			if (repeatablePrototype) {
				repeatablePrototype = (siblingIndex == 0);
			}

			request.setAttribute(WebKeys.JOURNAL_ARTICLE_GROUP_ID, String.valueOf(groupId));

			request.setAttribute(WebKeys.JOURNAL_ARTICLE_CONTENT_EL, contentElement);
			request.setAttribute(WebKeys.JOURNAL_STRUCTURE_EL, xsdElement);
			request.setAttribute(WebKeys.JOURNAL_STRUCTURE_EL_CONTENT, elContent);
			request.setAttribute(WebKeys.JOURNAL_STRUCTURE_EL_COUNT, count);
			request.setAttribute(WebKeys.JOURNAL_STRUCTURE_EL_DEPTH, depth);
			request.setAttribute(WebKeys.JOURNAL_STRUCTURE_EL_INSTANCE_ID, elInstanceId);
			request.setAttribute(WebKeys.JOURNAL_STRUCTURE_EL_LANGUAGE_ID, elLanguageId);
			request.setAttribute(WebKeys.JOURNAL_STRUCTURE_EL_META_DATA, elMetaData);
			request.setAttribute(WebKeys.JOURNAL_STRUCTURE_EL_NAME, elName);
			request.setAttribute(WebKeys.JOURNAL_STRUCTURE_EL_PARENT_ID, elParentStructureId);
			request.setAttribute(WebKeys.JOURNAL_STRUCTURE_EL_REPEATABLE, String.valueOf(elRepeatable));
			request.setAttribute(WebKeys.JOURNAL_STRUCTURE_EL_REPEATABLE_PROTOTYPE, String.valueOf(repeatablePrototype));
			request.setAttribute(WebKeys.JOURNAL_STRUCTURE_EL_TYPE, elType);
			request.setAttribute(WebKeys.JOURNAL_STRUCTURE_EL_INDEX_TYPE, elIndexType);

			pageContext.include("/html/portlet/journal/edit_article_content_xsd_el.jsp");

			count.increment();

			if (!elType.equals("list") && !elType.equals("multi-list") && !contentElement.elements().isEmpty()) {
				pageContext.include("/html/portlet/journal/edit_article_content_xsd_el_top.jsp");

				_format(groupId, contentElement, xsdElement, count, depth, repeatablePrototype, pageContext, request);

				request.setAttribute(WebKeys.JOURNAL_STRUCTURE_CLOSE_DROPPABLE_TAG, Boolean.TRUE.toString());

				pageContext.include("/html/portlet/journal/edit_article_content_xsd_el_bottom.jsp");
			}

			request.setAttribute(WebKeys.JOURNAL_STRUCTURE_CLOSE_DROPPABLE_TAG, Boolean.FALSE.toString());

			pageContext.include("/html/portlet/journal/edit_article_content_xsd_el_bottom.jsp");
		}
	}
}

private Map<String, String> _getMetaData(Element xsdElement, String elName) {
	Map<String, String> elMetaData = new HashMap<String, String>();

	Element metaData = xsdElement.element("meta-data");

	if (Validator.isNotNull(metaData)) {
		List<Element> elMetaDataements = metaData.elements();

		for (Element elMetaDataement : elMetaDataements) {
			String name = elMetaDataement.attributeValue("name");
			String content = elMetaDataement.getText().trim();

			elMetaData.put(name, content);
		}
	}
	else {
		elMetaData.put("label", elName);
	}

	return elMetaData;
}

private List<Element> _getSiblings(Element element, String name) {
	List<Element> elements = new ArrayList<Element>();

	Iterator<Element> itr = element.elements().iterator();

	while (itr.hasNext()) {
		Element curElement = itr.next();

		if (name.equals(curElement.attributeValue("name", StringPool.BLANK))) {
			elements.add(curElement);
		}
	}

	return elements;
}

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(29);
    _jspx_dependants.add("/html/portlet/journal/init.jsp");
    _jspx_dependants.add("/html/portlet/init.jsp");
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/journal/edit_article_extra.jspf");
    _jspx_dependants.add("/html/portlet/journal/edit_article_structure_extra.jspf");
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
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dutil_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005fenctype;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fwindowState_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005fenctype_005faction;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002derror_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005ffieldParam_005ffield_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fworkflow_002dstatus_0026_005fversion_005fstatus_005fid_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fmodel_005flabel_005fbean_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fdisabled_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005fid_005fdisabled;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005finput_002deditor_0026_005fwidth_005ftoolbarSet_005fonChangeMethod_005fname_005feditorImpl_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005finlineField_005fid_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fcssClass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_0026_005fmodelName_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fexception;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffieldset;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005finlineLabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattributes_002davailable_0026_005fclassName;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fclassPK_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_002drow_0026_005fcssClass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fdisabled_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fdisabled_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fonClick_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ftoggle_002darea_0026_005fshowMessage_005fid_005fhideMessage_005falign;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended_005fcssClass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ftable_002diterator_0026_005frowPadding_005frowLength_005flistType_005flist;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fid_005fhref_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineField_005fchecked_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fformName_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fformName_005fdisabled_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonClick_005fname_005flabel_005finlineLabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonClick_005fname_005finlineLabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005frows_005fname_005flabel_005fcssClass_005fcols_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fa_0026_005fid_005fhref;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClas_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fform_0026_005fmethod;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005fdisabled;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005foption_0026_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_002drow;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005frows_005fname_005flabel_005fcssClass_005fcols_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005finlineField_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fdata_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005finlineField_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005fmultiple_005flabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname_005flabel;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005frenderURL;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dutil_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005fenctype = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fwindowState_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005fenctype_005faction = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002derror_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005ffieldParam_005ffield_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fworkflow_002dstatus_0026_005fversion_005fstatus_005fid_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fmodel_005flabel_005fbean_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fdisabled_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005fid_005fdisabled = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005finput_002deditor_0026_005fwidth_005ftoolbarSet_005fonChangeMethod_005fname_005feditorImpl_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005finlineField_005fid_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fcssClass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_0026_005fmodelName_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fexception = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffieldset = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005finlineLabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattributes_002davailable_0026_005fclassName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fclassPK_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_002drow_0026_005fcssClass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fdisabled_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fdisabled_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fonClick_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ftoggle_002darea_0026_005fshowMessage_005fid_005fhideMessage_005falign = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended_005fcssClass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ftable_002diterator_0026_005frowPadding_005frowLength_005flistType_005flist = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fid_005fhref_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineField_005fchecked_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fformName_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fformName_005fdisabled_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonClick_005fname_005flabel_005finlineLabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonClick_005fname_005finlineLabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005frows_005fname_005flabel_005fcssClass_005fcols_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fa_0026_005fid_005fhref = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClas_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fform_0026_005fmethod = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005fdisabled = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005foption_0026_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_002drow = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005frows_005fname_005flabel_005fcssClass_005fcols_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005finlineField_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fdata_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005finlineField_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005fmultiple_005flabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname_005flabel = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005frenderURL = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage.release();
    _005fjspx_005ftagPool_005fliferay_002dutil_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005fenctype.release();
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fwindowState_005fvar.release();
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar.release();
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005fenctype_005faction.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002derror_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005fc_005fotherwise.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005ffieldParam_005ffield_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fworkflow_002dstatus_0026_005fversion_005fstatus_005fid_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fmodel_005flabel_005fbean_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fdisabled_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005fid_005fdisabled.release();
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005finput_002deditor_0026_005fwidth_005ftoolbarSet_005fonChangeMethod_005fname_005feditorImpl_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005finlineField_005fid_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fscript.release();
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fcssClass.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_0026_005fmodelName_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fexception.release();
    _005fjspx_005ftagPool_005faui_005ffieldset.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005finlineLabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattributes_002davailable_0026_005fclassName.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname.release();
    _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fclassPK_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fbutton_002drow_0026_005fcssClass.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fdisabled_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fdisabled_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fonClick_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ftoggle_002darea_0026_005fshowMessage_005fid_005fhideMessage_005falign.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended_005fcssClass.release();
    _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ftable_002diterator_0026_005frowPadding_005frowLength_005flistType_005flist.release();
    _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar.release();
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.release();
    _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fid_005fhref_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineField_005fchecked_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fformName_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fformName_005fdisabled_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonClick_005fname_005flabel_005finlineLabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonClick_005fname_005finlineLabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005frows_005fname_005flabel_005fcssClass_005fcols_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fa_0026_005fid_005fhref.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClas_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fform_0026_005fmethod.release();
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005fdisabled.release();
    _005fjspx_005ftagPool_005faui_005foption_0026_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel.release();
    _005fjspx_005ftagPool_005faui_005fbutton_002drow.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005frows_005fname_005flabel_005fcssClass_005fcols_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005finlineField_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fdata_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005finlineField_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005fmultiple_005flabel_005fnobody.release();
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname_005flabel.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005frenderURL.release();
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

String portletResource = ParamUtil.getString(request, "portletResource");

String tabs2 = ParamUtil.getString(request, "tabs2");

String redirect = ParamUtil.getString(request, "redirect");

// Make sure the redirect is correct. This is a workaround for a layout that
// has both the Journal and Journal Content portlets and the user edits an
// article through the Journal Content portlet and then hits cancel.

/*if (redirect.indexOf("p_p_id=" + PortletKeys.JOURNAL_CONTENT) != -1) {
	if (layoutTypePortlet.hasPortletId(PortletKeys.JOURNAL)) {
		PortletURL portletURL = renderResponse.createRenderURL();

		portletURL.setWindowState(WindowState.NORMAL);
		portletURL.setPortletMode(PortletMode.VIEW);

		redirect = portletURL.toString();
	}
}*/

String originalRedirect = ParamUtil.getString(request, "originalRedirect", StringPool.BLANK);

if (originalRedirect.equals(StringPool.BLANK)) {
	originalRedirect = redirect;
}
else {
	redirect = originalRedirect;
}

String referringPortletResource = ParamUtil.getString(request, "referringPortletResource");

JournalArticle article = (JournalArticle)request.getAttribute(WebKeys.JOURNAL_ARTICLE);

long groupId = BeanParamUtil.getLong(article, request, "groupId", scopeGroupId);

String articleId = BeanParamUtil.getString(article, request, "articleId");
String newArticleId = ParamUtil.getString(request, "newArticleId");
String instanceIdKey = PwdGenerator.KEY1 + PwdGenerator.KEY2 + PwdGenerator.KEY3;

double version = BeanParamUtil.getDouble(article, request, "version", JournalArticleConstants.DEFAULT_VERSION);

Calendar displayDate = CalendarFactoryUtil.getCalendar(timeZone, locale);

if (article != null) {
	if (article.getDisplayDate() != null) {
		displayDate.setTime(article.getDisplayDate());
	}
}

boolean neverExpire = ParamUtil.getBoolean(request, "neverExpire", true);

Calendar expirationDate = CalendarFactoryUtil.getCalendar(timeZone, locale);

expirationDate.add(Calendar.YEAR, 1);

if (article != null) {
	if (article.getExpirationDate() != null) {
		neverExpire = false;

		expirationDate.setTime(article.getExpirationDate());
	}
}

boolean neverReview = ParamUtil.getBoolean(request, "neverReview", true);

Calendar reviewDate = CalendarFactoryUtil.getCalendar(timeZone, locale);

reviewDate.add(Calendar.MONTH, 9);

if (article != null) {
	if (article.getReviewDate() != null) {
		neverReview = false;

		reviewDate.setTime(article.getReviewDate());
	}
}

String type = BeanParamUtil.getString(article, request, "type", "general");

String structureId = BeanParamUtil.getString(article, request, "structureId");

JournalStructure structure = null;

String parentStructureId = StringPool.BLANK;
long structureGroupdId = groupId;
String structureName = LanguageUtil.get(pageContext, "default");
String structureDescription = StringPool.BLANK;
String structureXSD = StringPool.BLANK;

if (Validator.isNotNull(structureId)) {
	try {
		structure = JournalStructureLocalServiceUtil.getStructure(groupId, structureId);
	}
	catch (NoSuchStructureException nsse1) {
		if (groupId != themeDisplay.getCompanyGroupId()) {
			try {
				structure = JournalStructureLocalServiceUtil.getStructure(themeDisplay.getCompanyGroupId(), structureId);

				structureGroupdId = themeDisplay.getCompanyGroupId();
			}
			catch (NoSuchStructureException nsse2) {
			}
		}
	}

	if (structure != null) {
		parentStructureId = structure.getParentStructureId();
		structureName = structure.getName();
		structureDescription = structure.getDescription();
		structureXSD = structure.getMergedXsd();
	}
}

List templates = new ArrayList();

if (structure != null) {
	templates = JournalTemplateLocalServiceUtil.getStructureTemplates(structureGroupdId, structureId);
}

String templateId = BeanParamUtil.getString(article, request, "templateId");

if ((structure == null) && Validator.isNotNull(templateId)) {
	JournalTemplate template = null;

	try {
		template = JournalTemplateLocalServiceUtil.getTemplate(groupId, templateId);
	}
	catch (NoSuchTemplateException nste1) {
		if (groupId != themeDisplay.getCompanyGroupId()) {
			try {
				template = JournalTemplateLocalServiceUtil.getTemplate(themeDisplay.getCompanyGroupId(), templateId);

				structureGroupdId = themeDisplay.getCompanyGroupId();
			}
			catch (NoSuchTemplateException nste2) {
			}
		}
	}

	if (template != null) {
		structureId = template.getStructureId();

		structure = JournalStructureLocalServiceUtil.getStructure(structureGroupdId, structureId);

		structureName = structure.getName();

		templates = JournalTemplateLocalServiceUtil.getStructureTemplates(structureGroupdId, structureId);
	}
}

String languageId = LanguageUtil.getLanguageId(request);

String defaultLanguageId = ParamUtil.getString(request, "defaultLanguageId");

if (article == null) {
	defaultLanguageId = languageId;
}
else {
	if (Validator.isNull(defaultLanguageId)) {
		defaultLanguageId =	article.getDefaultLocale();
	}
}

Locale defaultLocale = LocaleUtil.fromLanguageId(defaultLanguageId);

String content = null;

if (article != null) {
	content = ParamUtil.getString(request, "content");

	if (Validator.isNull(content)) {
		content = article.getContent();
	}

	content = JournalArticleImpl.getContentByLocale(content, Validator.isNotNull(structureId), languageId);
}
else {
	content = ParamUtil.getString(request, "content");
}

Document contentDoc = null;

String[] availableLocales = null;

if (Validator.isNotNull(content)) {
	try {
		contentDoc = SAXReaderUtil.read(content);

		Element contentEl = contentDoc.getRootElement();

		availableLocales = StringUtil.split(contentEl.attributeValue("available-locales"));

		if (!ArrayUtil.contains(availableLocales, defaultLanguageId)) {
			availableLocales = ArrayUtil.append(availableLocales, defaultLanguageId);
		}

		if (structure == null) {
			content = contentDoc.getRootElement().element("static-content").getText();
		}
	}
	catch (Exception e) {
		contentDoc = null;
	}
}

boolean smallImage = BeanParamUtil.getBoolean(article, request, "smallImage");
String smallImageURL = BeanParamUtil.getString(article, request, "smallImageURL");

      out.write('\n');
      out.write('\n');
      if (_jspx_meth_liferay_002dutil_005finclude_005f0(_jspx_page_context))
        return;
      out.write('\n');
      out.write('\n');
      if (_jspx_meth_aui_005fform_005f0(_jspx_page_context))
        return;
      out.write('\n');
      out.write('\n');
      //  portlet:actionURL
      com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f0 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fwindowState_005fvar.get(com.liferay.taglib.portlet.ActionURLTag.class);
      _jspx_th_portlet_005factionURL_005f0.setPageContext(_jspx_page_context);
      _jspx_th_portlet_005factionURL_005f0.setParent(null);
      // /html/portlet/journal/edit_article.jsp(242,0) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_portlet_005factionURL_005f0.setVar("editArticleActionURL");
      // /html/portlet/journal/edit_article.jsp(242,0) name = windowState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_portlet_005factionURL_005f0.setWindowState( WindowState.MAXIMIZED.toString() );
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
          if (_jspx_meth_portlet_005fparam_005f0(_jspx_th_portlet_005factionURL_005f0, _jspx_page_context))
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
        _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fwindowState_005fvar.reuse(_jspx_th_portlet_005factionURL_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fwindowState_005fvar.reuse(_jspx_th_portlet_005factionURL_005f0);
      java.lang.String editArticleActionURL = null;
      editArticleActionURL = (java.lang.String) _jspx_page_context.findAttribute("editArticleActionURL");
      out.write('\n');
      out.write('\n');
      //  portlet:renderURL
      com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f0 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
      _jspx_th_portlet_005frenderURL_005f0.setPageContext(_jspx_page_context);
      _jspx_th_portlet_005frenderURL_005f0.setParent(null);
      // /html/portlet/journal/edit_article.jsp(246,0) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_portlet_005frenderURL_005f0.setVar("editArticleRenderURL");
      // /html/portlet/journal/edit_article.jsp(246,0) name = windowState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_portlet_005frenderURL_005f0.setWindowState( WindowState.MAXIMIZED.toString() );
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
          if (_jspx_meth_portlet_005fparam_005f1(_jspx_th_portlet_005frenderURL_005f0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_portlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
        }
      }
      if (_jspx_th_portlet_005frenderURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f0);
      java.lang.String editArticleRenderURL = null;
      editArticleRenderURL = (java.lang.String) _jspx_page_context.findAttribute("editArticleRenderURL");
      out.write('\n');
      out.write('\n');
      //  aui:form
      com.liferay.taglib.aui.FormTag _jspx_th_aui_005fform_005f1 = (com.liferay.taglib.aui.FormTag) _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005fenctype_005faction.get(com.liferay.taglib.aui.FormTag.class);
      _jspx_th_aui_005fform_005f1.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fform_005f1.setParent(null);
      // /html/portlet/journal/edit_article.jsp(250,0) name = action type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fform_005f1.setAction( editArticleActionURL );
      // /html/portlet/journal/edit_article.jsp(250,0) null
      _jspx_th_aui_005fform_005f1.setDynamicAttribute(null, "enctype", new String("multipart/form-data"));
      // /html/portlet/journal/edit_article.jsp(250,0) null
      _jspx_th_aui_005fform_005f1.setDynamicAttribute(null, "method", new String("post"));
      // /html/portlet/journal/edit_article.jsp(250,0) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fform_005f1.setName("fm1");
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
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f0 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(251,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f0.setName("portletResource");
          // /html/portlet/journal/edit_article.jsp(251,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f0.setType("hidden");
          // /html/portlet/journal/edit_article.jsp(251,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f0.setValue( portletResource );
          int _jspx_eval_aui_005finput_005f0 = _jspx_th_aui_005finput_005f0.doStartTag();
          if (_jspx_th_aui_005finput_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f0);
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f1 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f1.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(252,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f1.setName( Constants.CMD );
          // /html/portlet/journal/edit_article.jsp(252,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f1.setType("hidden");
          int _jspx_eval_aui_005finput_005f1 = _jspx_th_aui_005finput_005f1.doStartTag();
          if (_jspx_th_aui_005finput_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f1);
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f2 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f2.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(253,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f2.setName("tabs2");
          // /html/portlet/journal/edit_article.jsp(253,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f2.setType("hidden");
          // /html/portlet/journal/edit_article.jsp(253,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f2.setValue( tabs2 );
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
          _jspx_th_aui_005finput_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(254,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f3.setName("redirect");
          // /html/portlet/journal/edit_article.jsp(254,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f3.setType("hidden");
          // /html/portlet/journal/edit_article.jsp(254,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f3.setValue( redirect );
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
          // /html/portlet/journal/edit_article.jsp(255,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f4.setName("originalRedirect");
          // /html/portlet/journal/edit_article.jsp(255,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f4.setType("hidden");
          // /html/portlet/journal/edit_article.jsp(255,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f4.setValue( originalRedirect );
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
          // /html/portlet/journal/edit_article.jsp(256,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f5.setName("referringPortletResource");
          // /html/portlet/journal/edit_article.jsp(256,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f5.setType("hidden");
          // /html/portlet/journal/edit_article.jsp(256,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f5.setValue( referringPortletResource );
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
          // /html/portlet/journal/edit_article.jsp(257,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f6.setName("groupId");
          // /html/portlet/journal/edit_article.jsp(257,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f6.setType("hidden");
          // /html/portlet/journal/edit_article.jsp(257,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f6.setValue( groupId );
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
          // /html/portlet/journal/edit_article.jsp(258,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f7.setName("articleId");
          // /html/portlet/journal/edit_article.jsp(258,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f7.setType("hidden");
          // /html/portlet/journal/edit_article.jsp(258,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f7.setValue( articleId );
          int _jspx_eval_aui_005finput_005f7 = _jspx_th_aui_005finput_005f7.doStartTag();
          if (_jspx_th_aui_005finput_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f7);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f7);
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f8 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f8.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(259,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f8.setName("version");
          // /html/portlet/journal/edit_article.jsp(259,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f8.setType("hidden");
          // /html/portlet/journal/edit_article.jsp(259,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f8.setValue( version );
          int _jspx_eval_aui_005finput_005f8 = _jspx_th_aui_005finput_005f8.doStartTag();
          if (_jspx_th_aui_005finput_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f8);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f8);
          out.write('\n');
          out.write('	');
          if (_jspx_meth_aui_005finput_005f9(_jspx_th_aui_005fform_005f1, _jspx_page_context))
            return;
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f10 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f10.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(261,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f10.setName("defaultLocale");
          // /html/portlet/journal/edit_article.jsp(261,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f10.setType("hidden");
          // /html/portlet/journal/edit_article.jsp(261,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f10.setValue( defaultLanguageId );
          int _jspx_eval_aui_005finput_005f10 = _jspx_th_aui_005finput_005f10.doStartTag();
          if (_jspx_th_aui_005finput_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f10);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f10);
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f11 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f11.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(262,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f11.setName("parentStructureId");
          // /html/portlet/journal/edit_article.jsp(262,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f11.setType("hidden");
          // /html/portlet/journal/edit_article.jsp(262,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f11.setValue( parentStructureId );
          int _jspx_eval_aui_005finput_005f11 = _jspx_th_aui_005finput_005f11.doStartTag();
          if (_jspx_th_aui_005finput_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f11);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f11);
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f12 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f12.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(263,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f12.setName("articleURL");
          // /html/portlet/journal/edit_article.jsp(263,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f12.setType("hidden");
          // /html/portlet/journal/edit_article.jsp(263,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f12.setValue( editArticleRenderURL );
          int _jspx_eval_aui_005finput_005f12 = _jspx_th_aui_005finput_005f12.doStartTag();
          if (_jspx_th_aui_005finput_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f12);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f12);
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f13 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f13.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(264,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f13.setName("workflowAction");
          // /html/portlet/journal/edit_article.jsp(264,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f13.setType("hidden");
          // /html/portlet/journal/edit_article.jsp(264,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f13.setValue( String.valueOf(WorkflowConstants.ACTION_SAVE_DRAFT) );
          int _jspx_eval_aui_005finput_005f13 = _jspx_th_aui_005finput_005f13.doStartTag();
          if (_jspx_th_aui_005finput_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f13);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f13);
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f14 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f14.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(265,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f14.setName("deleteArticleIds");
          // /html/portlet/journal/edit_article.jsp(265,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f14.setType("hidden");
          // /html/portlet/journal/edit_article.jsp(265,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f14.setValue( articleId + EditArticleAction.VERSION_SEPARATOR + version );
          int _jspx_eval_aui_005finput_005f14 = _jspx_th_aui_005finput_005f14.doStartTag();
          if (_jspx_th_aui_005finput_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f14);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f14);
          out.write('\n');
          out.write('	');
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f15 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f15.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(266,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f15.setName("expireArticleIds");
          // /html/portlet/journal/edit_article.jsp(266,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f15.setType("hidden");
          // /html/portlet/journal/edit_article.jsp(266,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f15.setValue( articleId + EditArticleAction.VERSION_SEPARATOR + version );
          int _jspx_eval_aui_005finput_005f15 = _jspx_th_aui_005finput_005f15.doStartTag();
          if (_jspx_th_aui_005finput_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f15);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f15);
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  aui:model-context
          com.liferay.taglib.aui.ModelContextTag _jspx_th_aui_005fmodel_002dcontext_005f0 = (com.liferay.taglib.aui.ModelContextTag) _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody.get(com.liferay.taglib.aui.ModelContextTag.class);
          _jspx_th_aui_005fmodel_002dcontext_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fmodel_002dcontext_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(268,1) name = bean type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fmodel_002dcontext_005f0.setBean( article );
          // /html/portlet/journal/edit_article.jsp(268,1) name = model type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fmodel_002dcontext_005f0.setModel( JournalArticle.class );
          int _jspx_eval_aui_005fmodel_002dcontext_005f0 = _jspx_th_aui_005fmodel_002dcontext_005f0.doStartTag();
          if (_jspx_th_aui_005fmodel_002dcontext_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody.reuse(_jspx_th_aui_005fmodel_002dcontext_005f0);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fmodel_002dcontext_0026_005fmodel_005fbean_005fnobody.reuse(_jspx_th_aui_005fmodel_002dcontext_005f0);
          out.write('\n');
          out.write('\n');
          out.write('	');

	boolean localizationEnabled = GetterUtil.getBoolean(SessionClicks.get(request, "liferay_journal_localization", Boolean.TRUE.toString()));
	
          out.write("\n");
          out.write("\n");
          out.write("\t<table class=\"lfr-table ");
          out.print( localizationEnabled ? StringPool.BLANK : "localization-disabled" );
          out.write("\" id=\"");
          if (_jspx_meth_portlet_005fnamespace_005f0(_jspx_th_aui_005fform_005f1, _jspx_page_context))
            return;
          out.write("journalArticleWrapper\" width=\"100%\">\n");
          out.write("\t<tr>\n");
          out.write("\t\t<td class=\"lfr-top\">\n");
          out.write("\t\t\t");
          //  liferay-ui:error
          com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f0 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
          _jspx_th_liferay_002dui_005ferror_005f0.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005ferror_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(277,3) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f0.setException( ArticleContentException.class );
          // /html/portlet/journal/edit_article.jsp(277,3) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f0.setMessage("please-enter-valid-content");
          int _jspx_eval_liferay_002dui_005ferror_005f0 = _jspx_th_liferay_002dui_005ferror_005f0.doStartTag();
          if (_jspx_th_liferay_002dui_005ferror_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f0);
          out.write("\n");
          out.write("\t\t\t");
          //  liferay-ui:error
          com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f1 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
          _jspx_th_liferay_002dui_005ferror_005f1.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005ferror_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(278,3) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f1.setException( ArticleIdException.class );
          // /html/portlet/journal/edit_article.jsp(278,3) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f1.setMessage("please-enter-a-valid-id");
          int _jspx_eval_liferay_002dui_005ferror_005f1 = _jspx_th_liferay_002dui_005ferror_005f1.doStartTag();
          if (_jspx_th_liferay_002dui_005ferror_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f1);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f1);
          out.write("\n");
          out.write("\t\t\t");
          //  liferay-ui:error
          com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f2 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
          _jspx_th_liferay_002dui_005ferror_005f2.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005ferror_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(279,3) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f2.setException( ArticleTitleException.class );
          // /html/portlet/journal/edit_article.jsp(279,3) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f2.setMessage("please-enter-a-valid-name");
          int _jspx_eval_liferay_002dui_005ferror_005f2 = _jspx_th_liferay_002dui_005ferror_005f2.doStartTag();
          if (_jspx_th_liferay_002dui_005ferror_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f2);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f2);
          out.write("\n");
          out.write("\t\t\t");
          //  liferay-ui:error
          com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f3 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
          _jspx_th_liferay_002dui_005ferror_005f3.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005ferror_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(280,3) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f3.setException( ArticleVersionException.class );
          // /html/portlet/journal/edit_article.jsp(280,3) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f3.setMessage("another-user-has-made-changes-since-you-started-editing-please-copy-your-changes-and-try-again");
          int _jspx_eval_liferay_002dui_005ferror_005f3 = _jspx_th_liferay_002dui_005ferror_005f3.doStartTag();
          if (_jspx_th_liferay_002dui_005ferror_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f3);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f3);
          out.write("\n");
          out.write("\t\t\t");
          //  liferay-ui:error
          com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f4 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
          _jspx_th_liferay_002dui_005ferror_005f4.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005ferror_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(281,3) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f4.setException( DuplicateArticleIdException.class );
          // /html/portlet/journal/edit_article.jsp(281,3) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ferror_005f4.setMessage("please-enter-a-unique-id");
          int _jspx_eval_liferay_002dui_005ferror_005f4 = _jspx_th_liferay_002dui_005ferror_005f4.doStartTag();
          if (_jspx_th_liferay_002dui_005ferror_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f4);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f4);
          out.write("\n");
          out.write("\t\t\t");
          if (_jspx_meth_liferay_002dui_005fasset_002dtags_002derror_005f0(_jspx_th_aui_005fform_005f1, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t<table class=\"lfr-table journal-article-header-edit\" id=\"");
          if (_jspx_meth_portlet_005fnamespace_005f1(_jspx_th_aui_005fform_005f1, _jspx_page_context))
            return;
          out.write("articleHeaderEdit\">\n");
          out.write("\t\t\t<tr>\n");
          out.write("\t\t\t\t<td>\n");
          out.write("\t\t\t\t\t");
          //  c:choose
          org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f0 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
          _jspx_th_c_005fchoose_005f0.setPageContext(_jspx_page_context);
          _jspx_th_c_005fchoose_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          int _jspx_eval_c_005fchoose_005f0 = _jspx_th_c_005fchoose_005f0.doStartTag();
          if (_jspx_eval_c_005fchoose_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t\t\t\t\t");
              //  c:when
              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f0 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
              _jspx_th_c_005fwhen_005f0.setPageContext(_jspx_page_context);
              _jspx_th_c_005fwhen_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
              // /html/portlet/journal/edit_article.jsp(288,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fwhen_005f0.setTest( article == null );
              int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
              if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t");
                  //  c:choose
                  org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                  _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fchoose_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
                  int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
                  if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t");
                      //  c:when
                      org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f1 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                      _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                      // /html/portlet/journal/edit_article.jsp(290,8) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_c_005fwhen_005f1.setTest( PropsValues.JOURNAL_ARTICLE_FORCE_AUTOGENERATE_ID );
                      int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
                      if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t");
                          if (_jspx_meth_aui_005finput_005f16(_jspx_th_c_005fwhen_005f1, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t");
                          //  aui:input
                          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f17 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                          _jspx_th_aui_005finput_005f17.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005finput_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
                          // /html/portlet/journal/edit_article.jsp(292,9) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f17.setName("autoArticleId");
                          // /html/portlet/journal/edit_article.jsp(292,9) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f17.setType("hidden");
                          // /html/portlet/journal/edit_article.jsp(292,9) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f17.setValue( true );
                          int _jspx_eval_aui_005finput_005f17 = _jspx_th_aui_005finput_005f17.doStartTag();
                          if (_jspx_th_aui_005finput_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f17);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f17);
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
                      org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                      _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
                      int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
                      if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t");
                          //  aui:input
                          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f18 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005ffieldParam_005ffield_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                          _jspx_th_aui_005finput_005f18.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005finput_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
                          // /html/portlet/journal/edit_article.jsp(295,9) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f18.setCssClass("lfr-input-text-container");
                          // /html/portlet/journal/edit_article.jsp(295,9) name = field type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f18.setField("articleId");
                          // /html/portlet/journal/edit_article.jsp(295,9) name = fieldParam type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f18.setFieldParam("newArticleId");
                          // /html/portlet/journal/edit_article.jsp(295,9) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f18.setLabel("id");
                          // /html/portlet/journal/edit_article.jsp(295,9) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f18.setName("newArticleId");
                          // /html/portlet/journal/edit_article.jsp(295,9) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f18.setValue( newArticleId );
                          int _jspx_eval_aui_005finput_005f18 = _jspx_th_aui_005finput_005f18.doStartTag();
                          if (_jspx_th_aui_005finput_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005ffieldParam_005ffield_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f18);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005flabel_005ffieldParam_005ffield_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f18);
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t\t");
                          if (_jspx_meth_aui_005finput_005f19(_jspx_th_c_005fotherwise_005f0, _jspx_page_context))
                            return;
                          out.write("\n");
                          out.write("\t\t\t\t\t\t\t\t");
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
              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f1 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
              _jspx_th_c_005fotherwise_005f1.setPageContext(_jspx_page_context);
              _jspx_th_c_005fotherwise_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
              int _jspx_eval_c_005fotherwise_005f1 = _jspx_th_c_005fotherwise_005f1.doStartTag();
              if (_jspx_eval_c_005fotherwise_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t");
                  //  aui:workflow-status
                  com.liferay.taglib.aui.WorkflowStatusTag _jspx_th_aui_005fworkflow_002dstatus_005f0 = (com.liferay.taglib.aui.WorkflowStatusTag) _005fjspx_005ftagPool_005faui_005fworkflow_002dstatus_0026_005fversion_005fstatus_005fid_005fnobody.get(com.liferay.taglib.aui.WorkflowStatusTag.class);
                  _jspx_th_aui_005fworkflow_002dstatus_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fworkflow_002dstatus_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
                  // /html/portlet/journal/edit_article.jsp(302,7) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fworkflow_002dstatus_005f0.setId( String.valueOf(article.getArticleId()) );
                  // /html/portlet/journal/edit_article.jsp(302,7) name = status type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fworkflow_002dstatus_005f0.setStatus( article.getStatus() );
                  // /html/portlet/journal/edit_article.jsp(302,7) name = version type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fworkflow_002dstatus_005f0.setVersion( article.getVersion() );
                  int _jspx_eval_aui_005fworkflow_002dstatus_005f0 = _jspx_th_aui_005fworkflow_002dstatus_005f0.doStartTag();
                  if (_jspx_th_aui_005fworkflow_002dstatus_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fworkflow_002dstatus_0026_005fversion_005fstatus_005fid_005fnobody.reuse(_jspx_th_aui_005fworkflow_002dstatus_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fworkflow_002dstatus_0026_005fversion_005fstatus_005fid_005fnobody.reuse(_jspx_th_aui_005fworkflow_002dstatus_005f0);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t");
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
          out.write("\t\t\t<tr>\n");
          out.write("\t\t\t\t<td>\n");
          out.write("\t\t\t\t\t");
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f20 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fmodel_005flabel_005fbean_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f20.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(309,5) name = bean type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f20.setBean( article );
          // /html/portlet/journal/edit_article.jsp(309,5) name = model type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f20.setModel( JournalArticle.class );
          // /html/portlet/journal/edit_article.jsp(309,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f20.setLabel("name");
          // /html/portlet/journal/edit_article.jsp(309,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f20.setName("title");
          int _jspx_eval_aui_005finput_005f20 = _jspx_th_aui_005finput_005f20.doStartTag();
          if (_jspx_th_aui_005finput_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fmodel_005flabel_005fbean_005fnobody.reuse(_jspx_th_aui_005finput_005f20);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fmodel_005flabel_005fbean_005fnobody.reuse(_jspx_th_aui_005finput_005f20);
          out.write("\n");
          out.write("\t\t\t\t</td>\n");
          out.write("\t\t\t</tr>\n");
          out.write("\t\t\t<tr>\n");
          out.write("\t\t\t\t<td>\n");
          out.write("\t\t\t\t\t");
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f21 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fdisabled_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f21.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(314,5) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f21.setCssClass("journal-article-localized-checkbox");
          // /html/portlet/journal/edit_article.jsp(314,5) name = disabled type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f21.setDisabled( article == null );
          // /html/portlet/journal/edit_article.jsp(314,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f21.setLabel("localize");
          // /html/portlet/journal/edit_article.jsp(314,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f21.setName("enableLocalization");
          // /html/portlet/journal/edit_article.jsp(314,5) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f21.setType("checkbox");
          // /html/portlet/journal/edit_article.jsp(314,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f21.setValue( localizationEnabled );
          int _jspx_eval_aui_005finput_005f21 = _jspx_th_aui_005finput_005f21.doStartTag();
          if (_jspx_th_aui_005finput_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fdisabled_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f21);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fdisabled_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f21);
          out.write("\n");
          out.write("\t\t\t\t</td>\n");
          out.write("\t\t\t</tr>\n");
          out.write("\t\t\t<tr class=\"journal-article-language-options\">\n");
          out.write("\t\t\t\t<td>\n");
          out.write("\t\t\t\t\t<input name=\"");
          if (_jspx_meth_portlet_005fnamespace_005f2(_jspx_th_aui_005fform_005f1, _jspx_page_context))
            return;
          out.write("lastLanguageId\" type=\"hidden\" value=\"");
          out.print( languageId );
          out.write("\" />\n");
          out.write("\n");
          out.write("\t\t\t\t\t<table class=\"lfr-table\">\n");
          out.write("\t\t\t\t\t<tr>\n");
          out.write("\t\t\t\t\t\t<td>\n");
          out.write("\t\t\t\t\t\t\t");
          //  aui:select
          com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f0 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005fid_005fdisabled.get(com.liferay.taglib.aui.SelectTag.class);
          _jspx_th_aui_005fselect_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fselect_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(324,7) name = disabled type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fselect_005f0.setDisabled( article == null );
          // /html/portlet/journal/edit_article.jsp(324,7) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fselect_005f0.setId("languageIdSelect");
          // /html/portlet/journal/edit_article.jsp(324,7) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fselect_005f0.setLabel("language");
          // /html/portlet/journal/edit_article.jsp(324,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fselect_005f0.setName("languageId");
          int _jspx_eval_aui_005fselect_005f0 = _jspx_th_aui_005fselect_005f0.doStartTag();
          if (_jspx_eval_aui_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_aui_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_aui_005fselect_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_aui_005fselect_005f0.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t");

								Locale[] locales = LanguageUtil.getAvailableLocales();

								for (int i = 0; i < locales.length; i++) {
								
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t");
              //  aui:option
              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f0 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
              _jspx_th_aui_005foption_005f0.setPageContext(_jspx_page_context);
              _jspx_th_aui_005foption_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f0);
              // /html/portlet/journal/edit_article.jsp(332,9) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005foption_005f0.setLabel( locales[i].getDisplayName(locale) );
              // /html/portlet/journal/edit_article.jsp(332,9) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005foption_005f0.setSelected( languageId.equals(LocaleUtil.toLanguageId(locales[i])) );
              // /html/portlet/journal/edit_article.jsp(332,9) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005foption_005f0.setValue( LocaleUtil.toLanguageId(locales[i]) );
              int _jspx_eval_aui_005foption_005f0 = _jspx_th_aui_005foption_005f0.doStartTag();
              if (_jspx_th_aui_005foption_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f0);
                return;
              }
              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f0);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t");

								}
								
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t");
              int evalDoAfterBody = _jspx_th_aui_005fselect_005f0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_aui_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_aui_005fselect_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005fid_005fdisabled.reuse(_jspx_th_aui_005fselect_005f0);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005fid_005fdisabled.reuse(_jspx_th_aui_005fselect_005f0);
          out.write("\n");
          out.write("\t\t\t\t\t\t</td>\n");
          out.write("\t\t\t\t\t\t<td>\n");
          out.write("\t\t\t\t\t\t\t");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(341,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f0.setTest( (article != null) && !languageId.equals(defaultLanguageId) );
          int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
          if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t");
              //  aui:button
              com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f0 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fname_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
              _jspx_th_aui_005fbutton_005f0.setPageContext(_jspx_page_context);
              _jspx_th_aui_005fbutton_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
              // /html/portlet/journal/edit_article.jsp(342,8) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005fbutton_005f0.setName("removeArticleLocaleButton");
              // /html/portlet/journal/edit_article.jsp(342,8) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005fbutton_005f0.setOnClick( renderResponse.getNamespace() + "removeArticleLocale();" );
              // /html/portlet/journal/edit_article.jsp(342,8) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005fbutton_005f0.setType("button");
              // /html/portlet/journal/edit_article.jsp(342,8) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005fbutton_005f0.setValue("remove");
              int _jspx_eval_aui_005fbutton_005f0 = _jspx_th_aui_005fbutton_005f0.doStartTag();
              if (_jspx_th_aui_005fbutton_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fname_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
                return;
              }
              _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fname_005fnobody.reuse(_jspx_th_aui_005fbutton_005f0);
              out.write("\n");
              out.write("\t\t\t\t\t\t\t");
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
          out.write("\t\t\t\t\t\t</td>\n");
          out.write("\t\t\t\t\t\t<td>\n");
          out.write("\t\t\t\t\t\t\t<table class=\"lfr-table\">\n");
          out.write("\t\t\t\t\t\t\t<tr>\n");
          out.write("\t\t\t\t\t\t\t\t<td>\n");
          out.write("\t\t\t\t\t\t\t\t\t");
          //  aui:select
          com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f1 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005fid_005fdisabled.get(com.liferay.taglib.aui.SelectTag.class);
          _jspx_th_aui_005fselect_005f1.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fselect_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(349,9) name = disabled type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fselect_005f1.setDisabled( article == null );
          // /html/portlet/journal/edit_article.jsp(349,9) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fselect_005f1.setId("defaultLanguageIdSelect");
          // /html/portlet/journal/edit_article.jsp(349,9) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fselect_005f1.setLabel("default-language");
          // /html/portlet/journal/edit_article.jsp(349,9) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fselect_005f1.setName("defaultLanguageId");
          int _jspx_eval_aui_005fselect_005f1 = _jspx_th_aui_005fselect_005f1.doStartTag();
          if (_jspx_eval_aui_005fselect_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_aui_005fselect_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_aui_005fselect_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_aui_005fselect_005f1.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t");

										if ((availableLocales != null) && (availableLocales.length > 0)) {
											boolean wasLanguageId = false;

											for (int i = 0; i < availableLocales.length; i++) {
												if (availableLocales[i].equals(languageId)) {
													wasLanguageId = true;
												}

												Locale availableLocale = LocaleUtil.fromLanguageId(availableLocales[i]);
										
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
              //  aui:option
              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f1 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
              _jspx_th_aui_005foption_005f1.setPageContext(_jspx_page_context);
              _jspx_th_aui_005foption_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f1);
              // /html/portlet/journal/edit_article.jsp(363,12) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005foption_005f1.setLabel( availableLocale.getDisplayName(availableLocale) );
              // /html/portlet/journal/edit_article.jsp(363,12) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005foption_005f1.setSelected( availableLocales[i].equals(defaultLanguageId) );
              // /html/portlet/journal/edit_article.jsp(363,12) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005foption_005f1.setValue( availableLocales[i] );
              int _jspx_eval_aui_005foption_005f1 = _jspx_th_aui_005foption_005f1.doStartTag();
              if (_jspx_th_aui_005foption_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f1);
                return;
              }
              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f1);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t");

											}

											if (!wasLanguageId) {
												Locale languageLocale = LocaleUtil.fromLanguageId(languageId);
										
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
              //  aui:option
              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f2 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
              _jspx_th_aui_005foption_005f2.setPageContext(_jspx_page_context);
              _jspx_th_aui_005foption_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f1);
              // /html/portlet/journal/edit_article.jsp(372,12) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005foption_005f2.setLabel( languageLocale.getDisplayName(languageLocale) );
              // /html/portlet/journal/edit_article.jsp(372,12) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005foption_005f2.setValue( languageId );
              int _jspx_eval_aui_005foption_005f2 = _jspx_th_aui_005foption_005f2.doStartTag();
              if (_jspx_th_aui_005foption_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f2);
                return;
              }
              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f2);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t");

											}
										}
										else {
										
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\t");
              //  aui:option
              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f3 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
              _jspx_th_aui_005foption_005f3.setPageContext(_jspx_page_context);
              _jspx_th_aui_005foption_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f1);
              // /html/portlet/journal/edit_article.jsp(380,11) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005foption_005f3.setLabel( defaultLocale.getDisplayName(defaultLocale) );
              // /html/portlet/journal/edit_article.jsp(380,11) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005foption_005f3.setValue( defaultLanguageId );
              int _jspx_eval_aui_005foption_005f3 = _jspx_th_aui_005foption_005f3.doStartTag();
              if (_jspx_th_aui_005foption_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f3);
                return;
              }
              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f3);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t");

										}
										
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t");
              int evalDoAfterBody = _jspx_th_aui_005fselect_005f1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_aui_005fselect_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_aui_005fselect_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005fid_005fdisabled.reuse(_jspx_th_aui_005fselect_005f1);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005fid_005fdisabled.reuse(_jspx_th_aui_005fselect_005f1);
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t\t");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(388,9) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f1.setTest( article == null );
          int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
          if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t");
              //  aui:input
              com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f22 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
              _jspx_th_aui_005finput_005f22.setPageContext(_jspx_page_context);
              _jspx_th_aui_005finput_005f22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
              // /html/portlet/journal/edit_article.jsp(389,10) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f22.setName("defaultLanguageId");
              // /html/portlet/journal/edit_article.jsp(389,10) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f22.setType("hidden");
              // /html/portlet/journal/edit_article.jsp(389,10) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f22.setValue( defaultLanguageId );
              int _jspx_eval_aui_005finput_005f22 = _jspx_th_aui_005finput_005f22.doStartTag();
              if (_jspx_th_aui_005finput_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f22);
                return;
              }
              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f22);
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t");
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
          out.write("\t\t\t\t\t\t\t\t</td>\n");
          out.write("\t\t\t\t\t\t\t</tr>\n");
          out.write("\t\t\t\t\t\t\t</table>\n");
          out.write("\t\t\t\t\t\t</td>\n");
          out.write("\t\t\t\t\t</tr>\n");
          out.write("\t\t\t\t\t</table>\n");
          out.write("\t\t\t\t</td>\n");
          out.write("\t\t\t</tr>\n");
          out.write("\t\t\t</table>\n");
          out.write("\n");
          out.write("\t\t\t<div class=\"journal-article-container\" id=\"");
          if (_jspx_meth_portlet_005fnamespace_005f3(_jspx_th_aui_005fform_005f1, _jspx_page_context))
            return;
          out.write("journalArticleContainer\">\n");
          out.write("\t\t\t\t");
          //  c:choose
          org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f2 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
          _jspx_th_c_005fchoose_005f2.setPageContext(_jspx_page_context);
          _jspx_th_c_005fchoose_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          int _jspx_eval_c_005fchoose_005f2 = _jspx_th_c_005fchoose_005f2.doStartTag();
          if (_jspx_eval_c_005fchoose_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t\t\t\t");
              //  c:when
              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f2 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
              _jspx_th_c_005fwhen_005f2.setPageContext(_jspx_page_context);
              _jspx_th_c_005fwhen_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
              // /html/portlet/journal/edit_article.jsp(403,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fwhen_005f2.setTest( structure == null );
              int _jspx_eval_c_005fwhen_005f2 = _jspx_th_c_005fwhen_005f2.doStartTag();
              if (_jspx_eval_c_005fwhen_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t\t<div id=\"");
                  if (_jspx_meth_portlet_005fnamespace_005f4(_jspx_th_c_005fwhen_005f2, _jspx_page_context))
                    return;
                  out.write("structureTreeWrapper\">\n");
                  out.write("\t\t\t\t\t\t\t<ul class=\"structure-tree\" id=\"");
                  if (_jspx_meth_portlet_005fnamespace_005f5(_jspx_th_c_005fwhen_005f2, _jspx_page_context))
                    return;
                  out.write("structureTree\">\n");
                  out.write("\t\t\t\t\t\t\t\t<li class=\"structure-field\" dataName=\"content\" dataType=\"text_area\">\n");
                  out.write("\t\t\t\t\t\t\t\t\t<span class=\"journal-article-close\"></span>\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t<span class=\"folder\">\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t<div class=\"field-container\">\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t<div class=\"journal-article-move-handler\"></div>\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t<label class=\"journal-article-field-label\" for=\"\">\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t\t<span>Content</span>\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t</label>\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t<div class=\"journal-article-component-container\">\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
                  //  liferay-ui:input-editor
                  com.liferay.taglib.ui.InputEditorTag _jspx_th_liferay_002dui_005finput_002deditor_005f0 = (com.liferay.taglib.ui.InputEditorTag) _005fjspx_005ftagPool_005fliferay_002dui_005finput_002deditor_0026_005fwidth_005ftoolbarSet_005fonChangeMethod_005fname_005feditorImpl_005fnobody.get(com.liferay.taglib.ui.InputEditorTag.class);
                  _jspx_th_liferay_002dui_005finput_002deditor_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005finput_002deditor_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                  // /html/portlet/journal/edit_article.jsp(418,12) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005finput_002deditor_005f0.setName( renderResponse.getNamespace() + "structure_el_TextAreaField_content" );
                  // /html/portlet/journal/edit_article.jsp(418,12) name = editorImpl type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005finput_002deditor_005f0.setEditorImpl( EDITOR_WYSIWYG_IMPL_KEY );
                  // /html/portlet/journal/edit_article.jsp(418,12) name = toolbarSet type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005finput_002deditor_005f0.setToolbarSet("liferay-article");
                  // /html/portlet/journal/edit_article.jsp(418,12) name = onChangeMethod type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005finput_002deditor_005f0.setOnChangeMethod( renderResponse.getNamespace() + "editorContentChanged" );
                  // /html/portlet/journal/edit_article.jsp(418,12) name = width type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005finput_002deditor_005f0.setWidth("100%");
                  int _jspx_eval_liferay_002dui_005finput_002deditor_005f0 = _jspx_th_liferay_002dui_005finput_002deditor_005f0.doStartTag();
                  if (_jspx_th_liferay_002dui_005finput_002deditor_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005finput_002deditor_0026_005fwidth_005ftoolbarSet_005fonChangeMethod_005fname_005feditorImpl_005fnobody.reuse(_jspx_th_liferay_002dui_005finput_002deditor_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005finput_002deditor_0026_005fwidth_005ftoolbarSet_005fonChangeMethod_005fname_005feditorImpl_005fnobody.reuse(_jspx_th_liferay_002dui_005finput_002deditor_005f0);
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t</div>\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t");
                  //  aui:input
                  com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f23 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fdisabled_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                  _jspx_th_aui_005finput_005f23.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005finput_005f23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                  // /html/portlet/journal/edit_article.jsp(421,11) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f23.setCssClass("journal-article-localized-checkbox");
                  // /html/portlet/journal/edit_article.jsp(421,11) name = disabled type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f23.setDisabled( article == null );
                  // /html/portlet/journal/edit_article.jsp(421,11) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f23.setLabel("localized");
                  // /html/portlet/journal/edit_article.jsp(421,11) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f23.setName("localized");
                  // /html/portlet/journal/edit_article.jsp(421,11) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f23.setType("checkbox");
                  // /html/portlet/journal/edit_article.jsp(421,11) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f23.setValue( ((article != null) && (article.getAvailableLocales().length > 1)) );
                  int _jspx_eval_aui_005finput_005f23 = _jspx_th_aui_005finput_005f23.doStartTag();
                  if (_jspx_th_aui_005finput_005f23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fdisabled_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f23);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fdisabled_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f23);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t<div class=\"journal-article-required-message portlet-msg-error\">\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
                  if (_jspx_meth_liferay_002dui_005fmessage_005f0(_jspx_th_c_005fwhen_005f2, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t</div>\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t<div class=\"journal-article-buttons\">\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
                  //  aui:input
                  com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f24 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005finlineField_005fid_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                  _jspx_th_aui_005finput_005f24.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005finput_005f24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                  // /html/portlet/journal/edit_article.jsp(428,12) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f24.setCssClass("journal-article-variable-name");
                  // /html/portlet/journal/edit_article.jsp(428,12) name = inlineField type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f24.setInlineField( true );
                  // /html/portlet/journal/edit_article.jsp(428,12) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f24.setId("TextAreaFieldvariableName");
                  // /html/portlet/journal/edit_article.jsp(428,12) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f24.setLabel("variable-name");
                  // /html/portlet/journal/edit_article.jsp(428,12) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f24.setName("variableName");
                  // /html/portlet/journal/edit_article.jsp(428,12) null
                  _jspx_th_aui_005finput_005f24.setDynamicAttribute(null, "size", new String("25"));
                  // /html/portlet/journal/edit_article.jsp(428,12) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f24.setType("text");
                  // /html/portlet/journal/edit_article.jsp(428,12) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f24.setValue(new String("content"));
                  int _jspx_eval_aui_005finput_005f24 = _jspx_th_aui_005finput_005f24.doStartTag();
                  if (_jspx_th_aui_005finput_005f24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005finlineField_005fid_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f24);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fsize_005fname_005flabel_005finlineField_005fid_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f24);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
                  if (_jspx_meth_aui_005fbutton_005f1(_jspx_th_c_005fwhen_005f2, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
                  if (_jspx_meth_aui_005fbutton_005f2(_jspx_th_c_005fwhen_005f2, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t\t</div>\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t</div>\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t<ul class=\"folder-droppable\"></ul>\n");
                  out.write("\t\t\t\t\t\t\t\t\t</span>\n");
                  out.write("\t\t\t\t\t\t\t\t</li>\n");
                  out.write("\t\t\t\t\t\t\t</ul>\n");
                  out.write("\t\t\t\t\t\t</div>\n");
                  out.write("\t\t\t\t\t");
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
              out.write("\t\t\t\t\t");
              //  c:otherwise
              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f2 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
              _jspx_th_c_005fotherwise_005f2.setPageContext(_jspx_page_context);
              _jspx_th_c_005fotherwise_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
              int _jspx_eval_c_005fotherwise_005f2 = _jspx_th_c_005fotherwise_005f2.doStartTag();
              if (_jspx_eval_c_005fotherwise_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t");

						Document xsdDoc = SAXReaderUtil.read(structure.getMergedXsd());

						if (contentDoc != null) {
						
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t<input name=\"");
                  if (_jspx_meth_portlet_005fnamespace_005f6(_jspx_th_c_005fotherwise_005f2, _jspx_page_context))
                    return;
                  out.write("available_locales\" type=\"hidden\" value=\"");
                  out.print( HtmlUtil.escapeAttribute(defaultLanguageId) );
                  out.write("\" />\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t");

							boolean languageFound = false;

							if ((availableLocales != null) && (availableLocales.length > 0)) {
								for (int i = 0; i < availableLocales.length ; i++) {
									if (!availableLocales[i].equals(defaultLanguageId)) {
							
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t<input name=\"");
                  if (_jspx_meth_portlet_005fnamespace_005f7(_jspx_th_c_005fotherwise_005f2, _jspx_page_context))
                    return;
                  out.write("available_locales\" type=\"hidden\" value=\"");
                  out.print( availableLocales[i] );
                  out.write("\" />\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t");
                  //  aui:script
                  com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f0 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript.get(com.liferay.taglib.aui.ScriptTag.class);
                  _jspx_th_aui_005fscript_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fscript_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f2);
                  int _jspx_eval_aui_005fscript_005f0 = _jspx_th_aui_005fscript_005f0.doStartTag();
                  if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005fscript_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005fscript_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005fscript_005f0.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t\t\t\tdocument.");
                      if (_jspx_meth_portlet_005fnamespace_005f8(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
                        return;
                      out.write("fm1.");
                      if (_jspx_meth_portlet_005fnamespace_005f9(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
                        return;
                      out.write("languageId.options[");
                      if (_jspx_meth_portlet_005fnamespace_005f10(_jspx_th_aui_005fscript_005f0, _jspx_page_context))
                        return;
                      out.write("getChoice('");
                      out.print( availableLocales[i] );
                      out.write("')].className = 'focused';\n");
                      out.write("\t\t\t\t\t\t\t\t\t\t");
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
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t");

									}
									else{
									
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t\t\t");
                  //  aui:script
                  com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f1 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript.get(com.liferay.taglib.aui.ScriptTag.class);
                  _jspx_th_aui_005fscript_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fscript_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f2);
                  int _jspx_eval_aui_005fscript_005f1 = _jspx_th_aui_005fscript_005f1.doStartTag();
                  if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005fscript_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005fscript_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005fscript_005f1.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t\t\t\t\tdocument.");
                      if (_jspx_meth_portlet_005fnamespace_005f11(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
                        return;
                      out.write("fm1.");
                      if (_jspx_meth_portlet_005fnamespace_005f12(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
                        return;
                      out.write("languageId.options[");
                      if (_jspx_meth_portlet_005fnamespace_005f13(_jspx_th_aui_005fscript_005f1, _jspx_page_context))
                        return;
                      out.write("getChoice('");
                      out.print( availableLocales[i] );
                      out.write("')].className = 'focused';\n");
                      out.write("\t\t\t\t\t\t\t\t\t\t");
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
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t");

									}

									if (availableLocales[i].equals(languageId)) {
										languageFound = true;
									}
								}
							}

							if (!languageFound && !languageId.equals(defaultLanguageId)) {
							
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t<input name=\"");
                  if (_jspx_meth_portlet_005fnamespace_005f14(_jspx_th_c_005fotherwise_005f2, _jspx_page_context))
                    return;
                  out.write("available_locales\" type=\"hidden\" value=\"");
                  out.print( languageId );
                  out.write("\" />\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t\t");
                  if (_jspx_meth_aui_005fscript_005f2(_jspx_th_c_005fotherwise_005f2, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t");

							}
						}
						else {
							contentDoc = SAXReaderUtil.createDocument(SAXReaderUtil.createElement("root"));
						
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t<input name=\"");
                  if (_jspx_meth_portlet_005fnamespace_005f17(_jspx_th_c_005fotherwise_005f2, _jspx_page_context))
                    return;
                  out.write("available_locales\" type=\"hidden\" value=\"");
                  out.print( HtmlUtil.escapeAttribute(defaultLanguageId) );
                  out.write("\" />\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t");

						}
						
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t\t<div class=\"structure-tree-wrapper\" id=\"");
                  if (_jspx_meth_portlet_005fnamespace_005f18(_jspx_th_c_005fotherwise_005f2, _jspx_page_context))
                    return;
                  out.write("structureTreeWrapper\">\n");
                  out.write("\t\t\t\t\t\t\t<ul class=\"structure-tree\" id=\"");
                  if (_jspx_meth_portlet_005fnamespace_005f19(_jspx_th_c_005fotherwise_005f2, _jspx_page_context))
                    return;
                  out.write("structureTree\">\n");
                  out.write("\t\t\t\t\t\t\t\t");
 _format(groupId, contentDoc.getRootElement(), xsdDoc.getRootElement(), new IntegerWrapper(0), new Integer(-1), true, pageContext, request); 
                  out.write("\n");
                  out.write("\t\t\t\t\t\t\t</ul>\n");
                  out.write("\t\t\t\t\t\t</div>\n");
                  out.write("\t\t\t\t\t");
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
              out.write("\n");
              out.write("\t\t\t\t");
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
          out.write("\t\t\t\t");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(514,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f2.setTest( article == null );
          int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
          if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t\t\t\t");
              //  aui:field-wrapper
              com.liferay.taglib.aui.FieldWrapperTag _jspx_th_aui_005ffield_002dwrapper_005f0 = (com.liferay.taglib.aui.FieldWrapperTag) _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fcssClass.get(com.liferay.taglib.aui.FieldWrapperTag.class);
              _jspx_th_aui_005ffield_002dwrapper_005f0.setPageContext(_jspx_page_context);
              _jspx_th_aui_005ffield_002dwrapper_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
              // /html/portlet/journal/edit_article.jsp(515,5) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005ffield_002dwrapper_005f0.setCssClass("journal-article-permissions");
              // /html/portlet/journal/edit_article.jsp(515,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005ffield_002dwrapper_005f0.setLabel("permissions");
              int _jspx_eval_aui_005ffield_002dwrapper_005f0 = _jspx_th_aui_005ffield_002dwrapper_005f0.doStartTag();
              if (_jspx_eval_aui_005ffield_002dwrapper_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_aui_005ffield_002dwrapper_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_aui_005ffield_002dwrapper_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_aui_005ffield_002dwrapper_005f0.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t\t");
                  //  liferay-ui:input-permissions
                  com.liferay.taglib.ui.InputPermissionsTag _jspx_th_liferay_002dui_005finput_002dpermissions_005f0 = (com.liferay.taglib.ui.InputPermissionsTag) _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_0026_005fmodelName_005fnobody.get(com.liferay.taglib.ui.InputPermissionsTag.class);
                  _jspx_th_liferay_002dui_005finput_002dpermissions_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005finput_002dpermissions_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f0);
                  // /html/portlet/journal/edit_article.jsp(516,6) name = modelName type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005finput_002dpermissions_005f0.setModelName( JournalArticle.class.getName() );
                  int _jspx_eval_liferay_002dui_005finput_002dpermissions_005f0 = _jspx_th_liferay_002dui_005finput_002dpermissions_005f0.doStartTag();
                  if (_jspx_th_liferay_002dui_005finput_002dpermissions_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_0026_005fmodelName_005fnobody.reuse(_jspx_th_liferay_002dui_005finput_002dpermissions_005f0);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005finput_002dpermissions_0026_005fmodelName_005fnobody.reuse(_jspx_th_liferay_002dui_005finput_002dpermissions_005f0);
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  int evalDoAfterBody = _jspx_th_aui_005ffield_002dwrapper_005f0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_aui_005ffield_002dwrapper_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_aui_005ffield_002dwrapper_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fcssClass.reuse(_jspx_th_aui_005ffield_002dwrapper_005f0);
                return;
              }
              _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel_005fcssClass.reuse(_jspx_th_aui_005ffield_002dwrapper_005f0);
              out.write("\n");
              out.write("\t\t\t\t");
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
          out.write("\t\t\t</div>\n");
          out.write("\n");
          out.write("\t\t\t<br />\n");
          out.write("\n");
          out.write("\t\t\t");
          //  liferay-ui:panel
          com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f0 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState.get(com.liferay.taglib.ui.PanelTag.class);
          _jspx_th_liferay_002dui_005fpanel_005f0.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005fpanel_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(525,3) name = defaultState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_005f0.setDefaultState("closed");
          // /html/portlet/journal/edit_article.jsp(525,3) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_005f0.setExtended( false );
          // /html/portlet/journal/edit_article.jsp(525,3) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_005f0.setId("journalAbstractPanel");
          // /html/portlet/journal/edit_article.jsp(525,3) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_005f0.setPersistState( true );
          // /html/portlet/journal/edit_article.jsp(525,3) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_005f0.setTitle( LanguageUtil.get(pageContext, "abstract") );
          int _jspx_eval_liferay_002dui_005fpanel_005f0 = _jspx_th_liferay_002dui_005fpanel_005f0.doStartTag();
          if (_jspx_eval_liferay_002dui_005fpanel_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_liferay_002dui_005fpanel_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_liferay_002dui_005fpanel_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_liferay_002dui_005fpanel_005f0.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\t\t\t\t");
              //  liferay-ui:error
              com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f5 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fexception.get(com.liferay.taglib.ui.ErrorTag.class);
              _jspx_th_liferay_002dui_005ferror_005f5.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005ferror_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f0);
              // /html/portlet/journal/edit_article.jsp(526,4) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ferror_005f5.setException( ArticleSmallImageNameException.class );
              int _jspx_eval_liferay_002dui_005ferror_005f5 = _jspx_th_liferay_002dui_005ferror_005f5.doStartTag();
              if (_jspx_eval_liferay_002dui_005ferror_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                java.lang.Object errorException = null;
                errorException = (java.lang.Object) _jspx_page_context.findAttribute("errorException");
                do {
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t");

				String[] imageExtensions = PrefsPropsUtil.getStringArray(PropsKeys.JOURNAL_IMAGE_EXTENSIONS, StringPool.COMMA);
				
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t");
                  if (_jspx_meth_liferay_002dui_005fmessage_005f1(_jspx_th_liferay_002dui_005ferror_005f5, _jspx_page_context))
                    return;
                  out.write(' ');
                  out.print( StringUtil.merge(imageExtensions, ", ") );
                  out.write(".\n");
                  out.write("\t\t\t\t");
                  int evalDoAfterBody = _jspx_th_liferay_002dui_005ferror_005f5.doAfterBody();
                  errorException = (java.lang.Object) _jspx_page_context.findAttribute("errorException");
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_liferay_002dui_005ferror_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fexception.reuse(_jspx_th_liferay_002dui_005ferror_005f5);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fexception.reuse(_jspx_th_liferay_002dui_005ferror_005f5);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t");
              //  liferay-ui:error
              com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f6 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
              _jspx_th_liferay_002dui_005ferror_005f6.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005ferror_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f0);
              // /html/portlet/journal/edit_article.jsp(535,4) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ferror_005f6.setException( ArticleSmallImageSizeException.class );
              // /html/portlet/journal/edit_article.jsp(535,4) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ferror_005f6.setMessage("please-enter-a-small-image-with-a-valid-file-size");
              int _jspx_eval_liferay_002dui_005ferror_005f6 = _jspx_th_liferay_002dui_005ferror_005f6.doStartTag();
              if (_jspx_th_liferay_002dui_005ferror_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f6);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f6);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t");
              //  aui:fieldset
              com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f0 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset.get(com.liferay.taglib.aui.FieldsetTag.class);
              _jspx_th_aui_005ffieldset_005f0.setPageContext(_jspx_page_context);
              _jspx_th_aui_005ffieldset_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f0);
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
                  if (_jspx_meth_aui_005finput_005f25(_jspx_th_aui_005ffieldset_005f0, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  if (_jspx_meth_aui_005finput_005f26(_jspx_th_aui_005ffieldset_005f0, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  if (_jspx_meth_aui_005finput_005f27(_jspx_th_aui_005ffieldset_005f0, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t<span style=\"font-size: xx-small;\">-- ");
                  out.print( LanguageUtil.get(pageContext, "or").toUpperCase() );
                  out.write(" --</span>\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  if (_jspx_meth_aui_005finput_005f28(_jspx_th_aui_005ffieldset_005f0, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  //  liferay-ui:custom-attributes-available
                  com.liferay.taglib.ui.CustomAttributesAvailableTag _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0 = (com.liferay.taglib.ui.CustomAttributesAvailableTag) _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattributes_002davailable_0026_005fclassName.get(com.liferay.taglib.ui.CustomAttributesAvailableTag.class);
                  _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
                  // /html/portlet/journal/edit_article.jsp(548,5) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.setClassName( JournalArticle.class.getName() );
                  int _jspx_eval_liferay_002dui_005fcustom_002dattributes_002davailable_005f0 = _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.doStartTag();
                  if (_jspx_eval_liferay_002dui_005fcustom_002dattributes_002davailable_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_liferay_002dui_005fcustom_002dattributes_002davailable_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");
                      //  liferay-ui:custom-attribute-list
                      com.liferay.taglib.ui.CustomAttributeListTag _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0 = (com.liferay.taglib.ui.CustomAttributeListTag) _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody.get(com.liferay.taglib.ui.CustomAttributeListTag.class);
                      _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fcustom_002dattributes_002davailable_005f0);
                      // /html/portlet/journal/edit_article.jsp(549,6) name = className type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setClassName( JournalArticle.class.getName() );
                      // /html/portlet/journal/edit_article.jsp(549,6) name = classPK type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setClassPK( (article != null) ? article.getPrimaryKey() : 0 );
                      // /html/portlet/journal/edit_article.jsp(549,6) name = editable type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setEditable( true );
                      // /html/portlet/journal/edit_article.jsp(549,6) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.setLabel( true );
                      int _jspx_eval_liferay_002dui_005fcustom_002dattribute_002dlist_005f0 = _jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.doStartTag();
                      if (_jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005fcustom_002dattribute_002dlist_0026_005flabel_005feditable_005fclassPK_005fclassName_005fnobody.reuse(_jspx_th_liferay_002dui_005fcustom_002dattribute_002dlist_005f0);
                      out.write("\n");
                      out.write("\t\t\t\t\t");
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
              out.write("\t\t\t");
              int evalDoAfterBody = _jspx_th_liferay_002dui_005fpanel_005f0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_liferay_002dui_005fpanel_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_liferay_002dui_005fpanel_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState.reuse(_jspx_th_liferay_002dui_005fpanel_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState.reuse(_jspx_th_liferay_002dui_005fpanel_005f0);
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t<br />\n");
          out.write("\n");
          out.write("\t\t\t");
          //  liferay-ui:panel
          com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f1 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState.get(com.liferay.taglib.ui.PanelTag.class);
          _jspx_th_liferay_002dui_005fpanel_005f1.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005fpanel_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(562,3) name = defaultState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_005f1.setDefaultState("closed");
          // /html/portlet/journal/edit_article.jsp(562,3) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_005f1.setExtended( false );
          // /html/portlet/journal/edit_article.jsp(562,3) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_005f1.setId("journalCategorizationPanel");
          // /html/portlet/journal/edit_article.jsp(562,3) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_005f1.setPersistState( true );
          // /html/portlet/journal/edit_article.jsp(562,3) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005fpanel_005f1.setTitle( LanguageUtil.get(pageContext, "categorization") );
          int _jspx_eval_liferay_002dui_005fpanel_005f1 = _jspx_th_liferay_002dui_005fpanel_005f1.doStartTag();
          if (_jspx_eval_liferay_002dui_005fpanel_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_liferay_002dui_005fpanel_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_liferay_002dui_005fpanel_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_liferay_002dui_005fpanel_005f1.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\t\t\t\t");
              //  liferay-ui:error
              com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f7 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
              _jspx_th_liferay_002dui_005ferror_005f7.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005ferror_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f1);
              // /html/portlet/journal/edit_article.jsp(563,4) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ferror_005f7.setException( ArticleTypeException.class );
              // /html/portlet/journal/edit_article.jsp(563,4) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ferror_005f7.setMessage("please-select-a-type");
              int _jspx_eval_liferay_002dui_005ferror_005f7 = _jspx_th_liferay_002dui_005ferror_005f7.doStartTag();
              if (_jspx_th_liferay_002dui_005ferror_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f7);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f7);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t");
              //  aui:fieldset
              com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f1 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset.get(com.liferay.taglib.aui.FieldsetTag.class);
              _jspx_th_aui_005ffieldset_005f1.setPageContext(_jspx_page_context);
              _jspx_th_aui_005ffieldset_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f1);
              int _jspx_eval_aui_005ffieldset_005f1 = _jspx_th_aui_005ffieldset_005f1.doStartTag();
              if (_jspx_eval_aui_005ffieldset_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_aui_005ffieldset_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_aui_005ffieldset_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_aui_005ffieldset_005f1.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  //  aui:select
                  com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f2 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname.get(com.liferay.taglib.aui.SelectTag.class);
                  _jspx_th_aui_005fselect_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fselect_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f1);
                  // /html/portlet/journal/edit_article.jsp(566,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fselect_005f2.setName("type");
                  // /html/portlet/journal/edit_article.jsp(566,5) name = showEmptyOption type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fselect_005f2.setShowEmptyOption( true );
                  int _jspx_eval_aui_005fselect_005f2 = _jspx_th_aui_005fselect_005f2.doStartTag();
                  if (_jspx_eval_aui_005fselect_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_aui_005fselect_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_aui_005fselect_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_aui_005fselect_005f2.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");

						for (int i = 0; i < JournalArticleConstants.TYPES.length; i++) {
						
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t\t");
                      //  aui:option
                      com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f4 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
                      _jspx_th_aui_005foption_005f4.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005foption_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f2);
                      // /html/portlet/journal/edit_article.jsp(572,7) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005foption_005f4.setLabel( JournalArticleConstants.TYPES[i] );
                      // /html/portlet/journal/edit_article.jsp(572,7) name = selected type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005foption_005f4.setSelected( type.equals(JournalArticleConstants.TYPES[i]) );
                      int _jspx_eval_aui_005foption_005f4 = _jspx_th_aui_005foption_005f4.doStartTag();
                      if (_jspx_th_aui_005foption_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f4);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005foption_0026_005fselected_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f4);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t\t");

						}
						
                      out.write("\n");
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
                    _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname.reuse(_jspx_th_aui_005fselect_005f2);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname.reuse(_jspx_th_aui_005fselect_005f2);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");

					long classPK = 0;

					if (article != null) {
						classPK = article.getResourcePrimKey();

						if (!article.isApproved() && (article.getVersion() != JournalArticleConstants.DEFAULT_VERSION)) {
							try {
								AssetEntryLocalServiceUtil.getEntry(JournalArticle.class.getName(), article.getPrimaryKey());

								classPK = article.getPrimaryKey();
							}
							catch (NoSuchEntryException nsee) {
							}
						}
					}
					
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  //  aui:input
                  com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f29 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fclassPK_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                  _jspx_th_aui_005finput_005f29.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005finput_005f29.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f1);
                  // /html/portlet/journal/edit_article.jsp(598,5) name = classPK type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f29.setClassPK( classPK );
                  // /html/portlet/journal/edit_article.jsp(598,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f29.setName("categories");
                  // /html/portlet/journal/edit_article.jsp(598,5) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f29.setType("assetCategories");
                  int _jspx_eval_aui_005finput_005f29 = _jspx_th_aui_005finput_005f29.doStartTag();
                  if (_jspx_th_aui_005finput_005f29.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fclassPK_005fnobody.reuse(_jspx_th_aui_005finput_005f29);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fclassPK_005fnobody.reuse(_jspx_th_aui_005finput_005f29);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  //  aui:input
                  com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f30 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fclassPK_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                  _jspx_th_aui_005finput_005f30.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005finput_005f30.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f1);
                  // /html/portlet/journal/edit_article.jsp(600,5) name = classPK type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f30.setClassPK( classPK );
                  // /html/portlet/journal/edit_article.jsp(600,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f30.setName("tags");
                  // /html/portlet/journal/edit_article.jsp(600,5) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005finput_005f30.setType("assetTags");
                  int _jspx_eval_aui_005finput_005f30 = _jspx_th_aui_005finput_005f30.doStartTag();
                  if (_jspx_th_aui_005finput_005f30.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fclassPK_005fnobody.reuse(_jspx_th_aui_005finput_005f30);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fclassPK_005fnobody.reuse(_jspx_th_aui_005finput_005f30);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  if (_jspx_meth_aui_005finput_005f31(_jspx_th_aui_005ffieldset_005f1, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t\t");
                  int evalDoAfterBody = _jspx_th_aui_005ffieldset_005f1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_aui_005ffieldset_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_aui_005ffieldset_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f1);
                return;
              }
              _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f1);
              out.write("\n");
              out.write("\t\t\t");
              int evalDoAfterBody = _jspx_th_liferay_002dui_005fpanel_005f1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_liferay_002dui_005fpanel_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_liferay_002dui_005fpanel_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState.reuse(_jspx_th_liferay_002dui_005fpanel_005f1);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState.reuse(_jspx_th_liferay_002dui_005fpanel_005f1);
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t<br />\n");
          out.write("\n");
          out.write("\t\t\t");

			boolean approved = false;
			boolean pending = false;

			if (article != null) {
				approved = article.isApproved();
				pending = article.isPending();
			}
			
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(618,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f3.setTest( approved );
          int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
          if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t\t\t<div class=\"portlet-msg-info\">\n");
              out.write("\t\t\t\t\t");
              if (_jspx_meth_liferay_002dui_005fmessage_005f2(_jspx_th_c_005fif_005f3, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t</div>\n");
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
          out.write("\t\t\t");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(624,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f4.setTest( pending );
          int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
          if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t\t\t<div class=\"portlet-msg-info\">\n");
              out.write("\t\t\t\t\t");
              if (_jspx_meth_liferay_002dui_005fmessage_005f3(_jspx_th_c_005fif_005f4, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t</div>\n");
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
          out.write("\t\t\t");
          //  aui:button-row
          com.liferay.taglib.aui.ButtonRowTag _jspx_th_aui_005fbutton_002drow_005f0 = (com.liferay.taglib.aui.ButtonRowTag) _005fjspx_005ftagPool_005faui_005fbutton_002drow_0026_005fcssClass.get(com.liferay.taglib.aui.ButtonRowTag.class);
          _jspx_th_aui_005fbutton_002drow_005f0.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fbutton_002drow_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article.jsp(630,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fbutton_002drow_005f0.setCssClass("journal-article-button-row");
          int _jspx_eval_aui_005fbutton_002drow_005f0 = _jspx_th_aui_005fbutton_002drow_005f0.doStartTag();
          if (_jspx_eval_aui_005fbutton_002drow_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_aui_005fbutton_002drow_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_aui_005fbutton_002drow_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_aui_005fbutton_002drow_005f0.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t");

				boolean hasSavePermission = false;

				if (article != null) {
					hasSavePermission = JournalArticlePermission.contains(permissionChecker, article, ActionKeys.UPDATE);
				}
				else {
					hasSavePermission = JournalPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_ARTICLE);
				}

				String saveButtonLabel = "save";

				if ((article == null) || article.isDraft() || article.isApproved()) {
					saveButtonLabel = "save-as-draft";
				}

				String publishButtonLabel = "publish";

				if (WorkflowDefinitionLinkLocalServiceUtil.hasWorkflowDefinitionLink(themeDisplay.getCompanyId(), scopeGroupId, JournalArticle.class.getName())) {
					publishButtonLabel = "submit-for-publication";
				}

				String deleteButtonLabel = "delete-version";

				if ((article != null) && article.isDraft()) {
					deleteButtonLabel = "discard-draft";
				}
				
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f5 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f0);
              // /html/portlet/journal/edit_article.jsp(661,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f5.setTest( hasSavePermission );
              int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
              if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  //  aui:button
                  com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f3 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                  _jspx_th_aui_005fbutton_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fbutton_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
                  // /html/portlet/journal/edit_article.jsp(662,5) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f3.setName("saveButton");
                  // /html/portlet/journal/edit_article.jsp(662,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f3.setValue( saveButtonLabel );
                  int _jspx_eval_aui_005fbutton_005f3 = _jspx_th_aui_005fbutton_005f3.doStartTag();
                  if (_jspx_th_aui_005fbutton_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_aui_005fbutton_005f3);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_aui_005fbutton_005f3);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  //  aui:button
                  com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f4 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fdisabled_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                  _jspx_th_aui_005fbutton_005f4.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fbutton_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
                  // /html/portlet/journal/edit_article.jsp(664,5) name = disabled type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f4.setDisabled( pending );
                  // /html/portlet/journal/edit_article.jsp(664,5) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f4.setName("publishButton");
                  // /html/portlet/journal/edit_article.jsp(664,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f4.setValue( publishButtonLabel );
                  int _jspx_eval_aui_005fbutton_005f4 = _jspx_th_aui_005fbutton_005f4.doStartTag();
                  if (_jspx_th_aui_005fbutton_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fdisabled_005fnobody.reuse(_jspx_th_aui_005fbutton_005f4);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fdisabled_005fnobody.reuse(_jspx_th_aui_005fbutton_005f4);
                  out.write("\n");
                  out.write("\t\t\t\t");
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
              out.write("\t\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f6 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f6.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f0);
              // /html/portlet/journal/edit_article.jsp(667,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f6.setTest( Validator.isNotNull(structureId) );
              int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
              if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  if (_jspx_meth_aui_005fbutton_005f5(_jspx_th_c_005fif_005f6, _jspx_page_context))
                    return;
                  out.write("\n");
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
              out.write("\n");
              out.write("\t\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f7 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f7.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f0);
              // /html/portlet/journal/edit_article.jsp(671,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f7.setTest( structure != null );
              int _jspx_eval_c_005fif_005f7 = _jspx_th_c_005fif_005f7.doStartTag();
              if (_jspx_eval_c_005fif_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  if (_jspx_meth_aui_005fbutton_005f6(_jspx_th_c_005fif_005f7, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t\t");
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
              out.write("\t\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f8 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f8.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f0);
              // /html/portlet/journal/edit_article.jsp(675,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f8.setTest( (article != null) && !article.isExpired() && JournalArticlePermission.contains(permissionChecker, article, ActionKeys.EXPIRE) );
              int _jspx_eval_c_005fif_005f8 = _jspx_th_c_005fif_005f8.doStartTag();
              if (_jspx_eval_c_005fif_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  //  aui:button
                  com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f7 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fdisabled_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                  _jspx_th_aui_005fbutton_005f7.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fbutton_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
                  // /html/portlet/journal/edit_article.jsp(676,5) name = disabled type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f7.setDisabled( !article.isApproved() );
                  // /html/portlet/journal/edit_article.jsp(676,5) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f7.setOnClick( renderResponse.getNamespace() + "expireArticle();" );
                  // /html/portlet/journal/edit_article.jsp(676,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f7.setValue("expire");
                  int _jspx_eval_aui_005fbutton_005f7 = _jspx_th_aui_005fbutton_005f7.doStartTag();
                  if (_jspx_th_aui_005fbutton_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fdisabled_005fnobody.reuse(_jspx_th_aui_005fbutton_005f7);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fdisabled_005fnobody.reuse(_jspx_th_aui_005fbutton_005f7);
                  out.write("\n");
                  out.write("\t\t\t\t");
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
              out.write("\t\t\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f9 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f9.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f0);
              // /html/portlet/journal/edit_article.jsp(679,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f9.setTest( (article != null) && JournalArticlePermission.contains(permissionChecker, article, ActionKeys.DELETE) );
              int _jspx_eval_c_005fif_005f9 = _jspx_th_c_005fif_005f9.doStartTag();
              if (_jspx_eval_c_005fif_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\t\t");
                  //  aui:button
                  com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f8 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                  _jspx_th_aui_005fbutton_005f8.setPageContext(_jspx_page_context);
                  _jspx_th_aui_005fbutton_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f9);
                  // /html/portlet/journal/edit_article.jsp(680,5) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f8.setOnClick( renderResponse.getNamespace() + "deleteArticle();" );
                  // /html/portlet/journal/edit_article.jsp(680,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_aui_005fbutton_005f8.setValue( deleteButtonLabel );
                  int _jspx_eval_aui_005fbutton_005f8 = _jspx_th_aui_005fbutton_005f8.doStartTag();
                  if (_jspx_th_aui_005fbutton_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f8);
                    return;
                  }
                  _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f8);
                  out.write("\n");
                  out.write("\t\t\t\t");
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
              out.write("\t\t\t\t");
              //  aui:button
              com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f9 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fonClick_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
              _jspx_th_aui_005fbutton_005f9.setPageContext(_jspx_page_context);
              _jspx_th_aui_005fbutton_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f0);
              // /html/portlet/journal/edit_article.jsp(683,4) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005fbutton_005f9.setOnClick( redirect );
              // /html/portlet/journal/edit_article.jsp(683,4) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005fbutton_005f9.setType("cancel");
              int _jspx_eval_aui_005fbutton_005f9 = _jspx_th_aui_005fbutton_005f9.doStartTag();
              if (_jspx_th_aui_005fbutton_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f9);
                return;
              }
              _005fjspx_005ftagPool_005faui_005fbutton_0026_005ftype_005fonClick_005fnobody.reuse(_jspx_th_aui_005fbutton_005f9);
              out.write("\n");
              out.write("\t\t\t");
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
          out.write("\n");
          out.write("\t\t</td>\n");
          out.write("\n");
          out.write("\t\t<td class=\"lfr-top\">\n");
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
          //  liferay-ui:toggle-area
          com.liferay.taglib.ui.ToggleAreaTag _jspx_th_liferay_002dui_005ftoggle_002darea_005f0 = (com.liferay.taglib.ui.ToggleAreaTag) _005fjspx_005ftagPool_005fliferay_002dui_005ftoggle_002darea_0026_005fshowMessage_005fid_005fhideMessage_005falign.get(com.liferay.taglib.ui.ToggleAreaTag.class);
          _jspx_th_liferay_002dui_005ftoggle_002darea_005f0.setPageContext(_jspx_page_context);
          _jspx_th_liferay_002dui_005ftoggle_002darea_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          // /html/portlet/journal/edit_article_extra.jspf(17,0) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ftoggle_002darea_005f0.setId("toggle_id_journal_edit_article_extra");
          // /html/portlet/journal/edit_article_extra.jspf(17,0) name = showMessage type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ftoggle_002darea_005f0.setShowMessage( LanguageUtil.get(pageContext, "advanced") + " &raquo;" );
          // /html/portlet/journal/edit_article_extra.jspf(17,0) name = hideMessage type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ftoggle_002darea_005f0.setHideMessage( "&laquo; " + LanguageUtil.get(pageContext, "basic") );
          // /html/portlet/journal/edit_article_extra.jspf(17,0) name = align type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_liferay_002dui_005ftoggle_002darea_005f0.setAlign("right");
          int _jspx_eval_liferay_002dui_005ftoggle_002darea_005f0 = _jspx_th_liferay_002dui_005ftoggle_002darea_005f0.doStartTag();
          if (_jspx_eval_liferay_002dui_005ftoggle_002darea_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_liferay_002dui_005ftoggle_002darea_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_liferay_002dui_005ftoggle_002darea_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_liferay_002dui_005ftoggle_002darea_005f0.doInitBody();
            }
            do {
              out.write('\n');
              out.write('	');
              //  liferay-ui:panel-container
              com.liferay.taglib.ui.PanelContainerTag _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0 = (com.liferay.taglib.ui.PanelContainerTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended_005fcssClass.get(com.liferay.taglib.ui.PanelContainerTag.class);
              _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ftoggle_002darea_005f0);
              // /html/portlet/journal/edit_article_extra.jspf(23,1) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setCssClass("journal-extras");
              // /html/portlet/journal/edit_article_extra.jspf(23,1) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setExtended( false );
              // /html/portlet/journal/edit_article_extra.jspf(23,1) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setId("journalExtrasPanelContainer");
              // /html/portlet/journal/edit_article_extra.jspf(23,1) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setPersistState( true );
              int _jspx_eval_liferay_002dui_005fpanel_002dcontainer_005f0 = _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.doStartTag();
              if (_jspx_eval_liferay_002dui_005fpanel_002dcontainer_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_liferay_002dui_005fpanel_002dcontainer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.doInitBody();
                }
                do {
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  //  liferay-ui:panel
                  com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f2 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState.get(com.liferay.taglib.ui.PanelTag.class);
                  _jspx_th_liferay_002dui_005fpanel_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fpanel_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
                  // /html/portlet/journal/edit_article_extra.jspf(24,2) name = defaultState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fpanel_005f2.setDefaultState("open");
                  // /html/portlet/journal/edit_article_extra.jspf(24,2) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fpanel_005f2.setExtended( false );
                  // /html/portlet/journal/edit_article_extra.jspf(24,2) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fpanel_005f2.setId("journalStructurePanel");
                  // /html/portlet/journal/edit_article_extra.jspf(24,2) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fpanel_005f2.setPersistState( true );
                  // /html/portlet/journal/edit_article_extra.jspf(24,2) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fpanel_005f2.setTitle( LanguageUtil.get(pageContext, "structure") );
                  int _jspx_eval_liferay_002dui_005fpanel_005f2 = _jspx_th_liferay_002dui_005fpanel_005f2.doStartTag();
                  if (_jspx_eval_liferay_002dui_005fpanel_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_liferay_002dui_005fpanel_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_liferay_002dui_005fpanel_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_liferay_002dui_005fpanel_005f2.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t");
                      //  aui:fieldset
                      com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f2 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset.get(com.liferay.taglib.aui.FieldsetTag.class);
                      _jspx_th_aui_005ffieldset_005f2.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005ffieldset_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
                      int _jspx_eval_aui_005ffieldset_005f2 = _jspx_th_aui_005ffieldset_005f2.doStartTag();
                      if (_jspx_eval_aui_005ffieldset_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_aui_005ffieldset_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_aui_005ffieldset_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_aui_005ffieldset_005f2.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t<div class=\"journal-form-presentation-label\">\n");
                          out.write("\t\t\t\t\t");
                          //  aui:input
                          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f32 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                          _jspx_th_aui_005finput_005f32.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005finput_005f32.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f2);
                          // /html/portlet/journal/edit_article_extra.jspf(27,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f32.setName("structureId");
                          // /html/portlet/journal/edit_article_extra.jspf(27,5) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f32.setType("hidden");
                          // /html/portlet/journal/edit_article_extra.jspf(27,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f32.setValue( structureId );
                          int _jspx_eval_aui_005finput_005f32 = _jspx_th_aui_005finput_005f32.doStartTag();
                          if (_jspx_th_aui_005finput_005f32.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f32);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f32);
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  aui:input
                          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f33 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                          _jspx_th_aui_005finput_005f33.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005finput_005f33.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f2);
                          // /html/portlet/journal/edit_article_extra.jspf(28,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f33.setName("structureName");
                          // /html/portlet/journal/edit_article_extra.jspf(28,5) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f33.setType("hidden");
                          // /html/portlet/journal/edit_article_extra.jspf(28,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f33.setValue( structureName );
                          int _jspx_eval_aui_005finput_005f33 = _jspx_th_aui_005finput_005f33.doStartTag();
                          if (_jspx_th_aui_005finput_005f33.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f33);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f33);
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  aui:input
                          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f34 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                          _jspx_th_aui_005finput_005f34.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005finput_005f34.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f2);
                          // /html/portlet/journal/edit_article_extra.jspf(29,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f34.setName("structureDescription");
                          // /html/portlet/journal/edit_article_extra.jspf(29,5) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f34.setType("hidden");
                          // /html/portlet/journal/edit_article_extra.jspf(29,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f34.setValue( structureDescription );
                          int _jspx_eval_aui_005finput_005f34 = _jspx_th_aui_005finput_005f34.doStartTag();
                          if (_jspx_th_aui_005finput_005f34.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f34);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f34);
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  aui:input
                          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f35 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                          _jspx_th_aui_005finput_005f35.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005finput_005f35.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f2);
                          // /html/portlet/journal/edit_article_extra.jspf(30,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f35.setName("structureXSD");
                          // /html/portlet/journal/edit_article_extra.jspf(30,5) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f35.setType("hidden");
                          // /html/portlet/journal/edit_article_extra.jspf(30,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f35.setValue( JS.encodeURIComponent(structureXSD) );
                          int _jspx_eval_aui_005finput_005f35 = _jspx_th_aui_005finput_005f35.doStartTag();
                          if (_jspx_th_aui_005finput_005f35.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f35);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f35);
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  aui:field-wrapper
                          com.liferay.taglib.aui.FieldWrapperTag _jspx_th_aui_005ffield_002dwrapper_005f1 = (com.liferay.taglib.aui.FieldWrapperTag) _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.get(com.liferay.taglib.aui.FieldWrapperTag.class);
                          _jspx_th_aui_005ffield_002dwrapper_005f1.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005ffield_002dwrapper_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f2);
                          // /html/portlet/journal/edit_article_extra.jspf(32,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005ffield_002dwrapper_005f1.setLabel("name");
                          int _jspx_eval_aui_005ffield_002dwrapper_005f1 = _jspx_th_aui_005ffield_002dwrapper_005f1.doStartTag();
                          if (_jspx_eval_aui_005ffield_002dwrapper_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_aui_005ffield_002dwrapper_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_aui_005ffield_002dwrapper_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_aui_005ffield_002dwrapper_005f1.doInitBody();
                            }
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t<span id=\"");
                              if (_jspx_meth_portlet_005fnamespace_005f20(_jspx_th_aui_005ffield_002dwrapper_005f1, _jspx_page_context))
                              return;
                              out.write("structureNameLabel\" class=\"structure-name-label\">\n");
                              out.write("\t\t\t\t\t\t\t");
                              out.print( HtmlUtil.escape(structureName) );
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f10 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f10.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f1);
                              // /html/portlet/journal/edit_article_extra.jspf(36,7) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f10.setTest( Validator.isNotNull(structureId) );
                              int _jspx_eval_c_005fif_005f10 = _jspx_th_c_005fif_005f10.doStartTag();
                              if (_jspx_eval_c_005fif_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t<span class=\"default-link\">(<a href=\"javascript:;\" id=\"");
                              if (_jspx_meth_portlet_005fnamespace_005f21(_jspx_th_c_005fif_005f10, _jspx_page_context))
                              return;
                              out.write("loadDefaultStructure\">");
                              if (_jspx_meth_liferay_002dui_005fmessage_005f4(_jspx_th_c_005fif_005f10, _jspx_page_context))
                              return;
                              out.write("</a>)</span>\n");
                              out.write("\t\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t</span>\n");
                              out.write("\t\t\t\t\t");
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
                          out.write("\n");
                          out.write("\t\t\t\t</div>\n");
                          out.write("\t\t\t");
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
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t<div id=\"");
                      if (_jspx_meth_portlet_005fnamespace_005f22(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("structureMessage\" class=\"portlet-msg-alert structure-message aui-helper-hidden\">\n");
                      out.write("\t\t\t\t");
                      //  aui:field-wrapper
                      com.liferay.taglib.aui.FieldWrapperTag _jspx_th_aui_005ffield_002dwrapper_005f2 = (com.liferay.taglib.aui.FieldWrapperTag) _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.get(com.liferay.taglib.aui.FieldWrapperTag.class);
                      _jspx_th_aui_005ffield_002dwrapper_005f2.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005ffield_002dwrapper_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
                      // /html/portlet/journal/edit_article_extra.jspf(45,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_aui_005ffield_002dwrapper_005f2.setLabel("this-structure-has-not-been-saved");
                      int _jspx_eval_aui_005ffield_002dwrapper_005f2 = _jspx_th_aui_005ffield_002dwrapper_005f2.doStartTag();
                      if (_jspx_eval_aui_005ffield_002dwrapper_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_aui_005ffield_002dwrapper_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_aui_005ffield_002dwrapper_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_aui_005ffield_002dwrapper_005f2.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t\t");
                          //  liferay-ui:message
                          com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f5 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
                          _jspx_th_liferay_002dui_005fmessage_005f5.setPageContext(_jspx_page_context);
                          _jspx_th_liferay_002dui_005fmessage_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f2);
                          // /html/portlet/journal/edit_article_extra.jspf(46,5) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fmessage_005f5.setKey("click-here-to-save-it-now");
                          // /html/portlet/journal/edit_article_extra.jspf(46,5) name = arguments type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_liferay_002dui_005fmessage_005f5.setArguments( new Object[] {"journal-save-structure-trigger", "#"} );
                          int _jspx_eval_liferay_002dui_005fmessage_005f5 = _jspx_th_liferay_002dui_005fmessage_005f5.doStartTag();
                          if (_jspx_th_liferay_002dui_005fmessage_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
                            return;
                          }
                          _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005farguments_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f5);
                          out.write("\n");
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
                        _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.reuse(_jspx_th_aui_005ffield_002dwrapper_005f2);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.reuse(_jspx_th_aui_005ffield_002dwrapper_005f2);
                      out.write("\n");
                      out.write("\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t<div class=\"structure-controls\">\n");
                      out.write("\t\t\t\t<span class=\"structure-buttons\">\n");
                      out.write("\t\t\t\t\t");
                      if (_jspx_meth_aui_005fbutton_005f10(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t");
                      if (_jspx_meth_aui_005fbutton_005f11(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t</span>\n");
                      out.write("\n");
                      out.write("\t\t\t\t<span class=\"structure-links\">\n");
                      out.write("\t\t\t\t\t<a dataChangeStructureUrl=\"");
                      //  portlet:renderURL
                      com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f1 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState.get(com.liferay.taglib.portlet.RenderURLTag.class);
                      _jspx_th_portlet_005frenderURL_005f1.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005frenderURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
                      // /html/portlet/journal/edit_article_extra.jspf(58,32) name = windowState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005frenderURL_005f1.setWindowState( LiferayWindowState.POP_UP.toString() );
                      int _jspx_eval_portlet_005frenderURL_005f1 = _jspx_th_portlet_005frenderURL_005f1.doStartTag();
                      if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_portlet_005frenderURL_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_portlet_005frenderURL_005f1.doInitBody();
                        }
                        do {
                          if (_jspx_meth_portlet_005fparam_005f2(_jspx_th_portlet_005frenderURL_005f1, _jspx_page_context))
                            return;
                          //  portlet:param
                          com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f3 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                          _jspx_th_portlet_005fparam_005f3.setPageContext(_jspx_page_context);
                          _jspx_th_portlet_005fparam_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
                          // /html/portlet/journal/edit_article_extra.jspf(58,181) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f3.setName("groupId");
                          // /html/portlet/journal/edit_article_extra.jspf(58,181) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_portlet_005fparam_005f3.setValue( String.valueOf(themeDisplay.getParentGroupId()) );
                          int _jspx_eval_portlet_005fparam_005f3 = _jspx_th_portlet_005fparam_005f3.doStartTag();
                          if (_jspx_th_portlet_005fparam_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
                            return;
                          }
                          _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
                          int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f1.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.popBody();
                        }
                      }
                      if (_jspx_th_portlet_005frenderURL_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState.reuse(_jspx_th_portlet_005frenderURL_005f1);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState.reuse(_jspx_th_portlet_005frenderURL_005f1);
                      out.write("\" href=\"javascript:;\" id=\"");
                      if (_jspx_meth_portlet_005fnamespace_005f23(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("changeStructureBtn\">\n");
                      out.write("\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f6(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t</a>\n");
                      out.write("\t\t\t\t</span>\n");
                      out.write("\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t<div id=\"");
                      if (_jspx_meth_portlet_005fnamespace_005f24(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("journalComponentList\" class=\"journal-article-component-list aui-helper-hidden\">\n");
                      out.write("\t\t\t\t<div class=\"component-group form-controls\">\n");
                      out.write("\t\t\t\t\t<div class=\"component-group-title\">\n");
                      out.write("\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f7(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t<div class=\"journal-component journal-component-text\" dataType=\"text\">\n");
                      out.write("\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f8(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t<div class=\"journal-component journal-component-textbox\" dataType=\"text_box\">\n");
                      out.write("\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f9(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t<div class=\"journal-component journal-component-textarea\" dataType=\"text_area\">\n");
                      out.write("\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f10(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t<div class=\"journal-component journal-component-boolean\" dataType=\"boolean\">\n");
                      out.write("\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f11(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t<div class=\"journal-component journal-component-list\" dataType=\"list\">\n");
                      out.write("\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f12(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t<div class=\"journal-component journal-component-multilist\" dataType=\"multi-list\">\n");
                      out.write("\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f13(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t</div>\n");
                      out.write("\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t<br />\n");
                      out.write("\n");
                      out.write("\t\t\t\t<div class=\"component-group form-controls\">\n");
                      out.write("\t\t\t\t\t<div class=\"component-group-title\">\n");
                      out.write("\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f14(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t<div class=\"journal-component journal-component-imagegallery\" dataType=\"image_gallery\">\n");
                      out.write("\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f15(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t<div class=\"journal-component journal-component-image\" dataType=\"image\">\n");
                      out.write("\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f16(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t<div class=\"journal-component journal-component-documentlibrary\" dataType=\"document_library\">\n");
                      out.write("\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f17(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t</div>\n");
                      out.write("\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t<br />\n");
                      out.write("\n");
                      out.write("\t\t\t\t<div class=\"component-group form-controls\">\n");
                      out.write("\t\t\t\t\t<div class=\"component-group-title\">\n");
                      out.write("\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f18(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t<div class=\"journal-component journal-component-linktolayout\" dataType=\"link_to_layout\">\n");
                      out.write("\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f19(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t</div>\n");
                      out.write("\n");
                      out.write("\t\t\t\t\t<div class=\"journal-component journal-component-selectionbreak\" dataType=\"selection_break\">\n");
                      out.write("\t\t\t\t\t\t");
                      if (_jspx_meth_liferay_002dui_005fmessage_005f20(_jspx_th_liferay_002dui_005fpanel_005f2, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t\t</div>\n");
                      out.write("\t\t\t\t</div>\n");
                      out.write("\t\t\t</div>\n");
                      out.write("\t\t");
                      int evalDoAfterBody = _jspx_th_liferay_002dui_005fpanel_005f2.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_liferay_002dui_005fpanel_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_liferay_002dui_005fpanel_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState.reuse(_jspx_th_liferay_002dui_005fpanel_005f2);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState.reuse(_jspx_th_liferay_002dui_005fpanel_005f2);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t");
                  //  liferay-ui:panel
                  com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f3 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState.get(com.liferay.taglib.ui.PanelTag.class);
                  _jspx_th_liferay_002dui_005fpanel_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fpanel_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
                  // /html/portlet/journal/edit_article_extra.jspf(133,2) name = defaultState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fpanel_005f3.setDefaultState("open");
                  // /html/portlet/journal/edit_article_extra.jspf(133,2) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fpanel_005f3.setExtended( false );
                  // /html/portlet/journal/edit_article_extra.jspf(133,2) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fpanel_005f3.setId("journalTemplatePanel");
                  // /html/portlet/journal/edit_article_extra.jspf(133,2) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fpanel_005f3.setPersistState( true );
                  // /html/portlet/journal/edit_article_extra.jspf(133,2) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fpanel_005f3.setTitle( LanguageUtil.get(pageContext, "template") );
                  int _jspx_eval_liferay_002dui_005fpanel_005f3 = _jspx_th_liferay_002dui_005fpanel_005f3.doStartTag();
                  if (_jspx_eval_liferay_002dui_005fpanel_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_liferay_002dui_005fpanel_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_liferay_002dui_005fpanel_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_liferay_002dui_005fpanel_005f3.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t");
                      //  aui:fieldset
                      com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f3 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset.get(com.liferay.taglib.aui.FieldsetTag.class);
                      _jspx_th_aui_005ffieldset_005f3.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005ffieldset_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f3);
                      int _jspx_eval_aui_005ffieldset_005f3 = _jspx_th_aui_005ffieldset_005f3.doStartTag();
                      if (_jspx_eval_aui_005ffieldset_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_aui_005ffieldset_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_aui_005ffieldset_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_aui_005ffieldset_005f3.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t<div class=\"journal-form-presentation-label\">\n");
                          out.write("\t\t\t\t\t");
                          //  c:choose
                          org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f3 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
                          _jspx_th_c_005fchoose_005f3.setPageContext(_jspx_page_context);
                          _jspx_th_c_005fchoose_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f3);
                          int _jspx_eval_c_005fchoose_005f3 = _jspx_th_c_005fchoose_005f3.doStartTag();
                          if (_jspx_eval_c_005fchoose_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
                              //  c:when
                              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f3 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                              _jspx_th_c_005fwhen_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fwhen_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
                              // /html/portlet/journal/edit_article_extra.jspf(137,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fwhen_005f3.setTest( templates.isEmpty() );
                              int _jspx_eval_c_005fwhen_005f3 = _jspx_th_c_005fwhen_005f3.doStartTag();
                              if (_jspx_eval_c_005fwhen_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t");
                              //  aui:input
                              com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f36 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                              _jspx_th_aui_005finput_005f36.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005finput_005f36.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
                              // /html/portlet/journal/edit_article_extra.jspf(138,7) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005finput_005f36.setName("templateId");
                              // /html/portlet/journal/edit_article_extra.jspf(138,7) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005finput_005f36.setType("hidden");
                              // /html/portlet/journal/edit_article_extra.jspf(138,7) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005finput_005f36.setValue( templateId );
                              int _jspx_eval_aui_005finput_005f36 = _jspx_th_aui_005finput_005f36.doStartTag();
                              if (_jspx_th_aui_005finput_005f36.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f36);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f36);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t<div id=\"selectTemplateMessage\"></div>\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t");
                              //  aui:button
                              com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f12 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fname_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
                              _jspx_th_aui_005fbutton_005f12.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005fbutton_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
                              // /html/portlet/journal/edit_article_extra.jspf(142,7) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005fbutton_005f12.setName("selectTemplateBtn");
                              // /html/portlet/journal/edit_article_extra.jspf(142,7) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005fbutton_005f12.setOnClick( renderResponse.getNamespace() + "openTemplateSelector();" );
                              // /html/portlet/journal/edit_article_extra.jspf(142,7) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005fbutton_005f12.setType("button");
                              // /html/portlet/journal/edit_article_extra.jspf(142,7) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005fbutton_005f12.setValue("select");
                              int _jspx_eval_aui_005fbutton_005f12 = _jspx_th_aui_005fbutton_005f12.doStartTag();
                              if (_jspx_th_aui_005fbutton_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fname_005fnobody.reuse(_jspx_th_aui_005fbutton_005f12);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fonClick_005fname_005fnobody.reuse(_jspx_th_aui_005fbutton_005f12);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t");
                              //  c:otherwise
                              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f3 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                              _jspx_th_c_005fotherwise_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fotherwise_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
                              int _jspx_eval_c_005fotherwise_005f3 = _jspx_th_c_005fotherwise_005f3.doStartTag();
                              if (_jspx_eval_c_005fotherwise_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t");
                              //  aui:field-wrapper
                              com.liferay.taglib.aui.FieldWrapperTag _jspx_th_aui_005ffield_002dwrapper_005f3 = (com.liferay.taglib.aui.FieldWrapperTag) _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.get(com.liferay.taglib.aui.FieldWrapperTag.class);
                              _jspx_th_aui_005ffield_002dwrapper_005f3.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005ffield_002dwrapper_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f3);
                              // /html/portlet/journal/edit_article_extra.jspf(145,7) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005ffield_002dwrapper_005f3.setLabel("template");
                              int _jspx_eval_aui_005ffield_002dwrapper_005f3 = _jspx_th_aui_005ffield_002dwrapper_005f3.doStartTag();
                              if (_jspx_eval_aui_005ffield_002dwrapper_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_aui_005ffield_002dwrapper_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_aui_005ffield_002dwrapper_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_aui_005ffield_002dwrapper_005f3.doInitBody();
                              }
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t");
                              //  liferay-ui:table-iterator
                              com.liferay.taglib.ui.TableIteratorTag _jspx_th_liferay_002dui_005ftable_002diterator_005f0 = (com.liferay.taglib.ui.TableIteratorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ftable_002diterator_0026_005frowPadding_005frowLength_005flistType_005flist.get(com.liferay.taglib.ui.TableIteratorTag.class);
                              _jspx_th_liferay_002dui_005ftable_002diterator_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dui_005ftable_002diterator_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f3);
                              // /html/portlet/journal/edit_article_extra.jspf(146,8) name = list type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ftable_002diterator_005f0.setList( templates );
                              // /html/portlet/journal/edit_article_extra.jspf(146,8) name = listType type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ftable_002diterator_005f0.setListType("com.liferay.portlet.journal.model.JournalTemplate");
                              // /html/portlet/journal/edit_article_extra.jspf(146,8) name = rowLength type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dui_005ftable_002diterator_005f0.setRowLength("3");
                              // /html/portlet/journal/edit_article_extra.jspf(146,8) name = rowPadding type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
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
                              out.write("\t\t\t\t\t\t\t\t\t");

									boolean templateChecked = false;

									if (templateId.equals(tableIteratorObj.getTemplateId())) {
										templateChecked = true;
									}

									if ((tableIteratorPos.intValue() == 0) && Validator.isNull(templateId)) {
										templateChecked = true;
									}
									
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
                              //  aui:input
                              com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f37 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                              _jspx_th_aui_005finput_005f37.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005finput_005f37.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ftable_002diterator_005f0);
                              // /html/portlet/journal/edit_article_extra.jspf(165,9) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005finput_005f37.setName( "template" + tableIteratorObj.getTemplateId() + "_xsl" );
                              // /html/portlet/journal/edit_article_extra.jspf(165,9) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005finput_005f37.setType("hidden");
                              // /html/portlet/journal/edit_article_extra.jspf(165,9) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005finput_005f37.setValue( JS.encodeURIComponent(tableIteratorObj.getXsl()) );
                              int _jspx_eval_aui_005finput_005f37 = _jspx_th_aui_005finput_005f37.doStartTag();
                              if (_jspx_th_aui_005finput_005f37.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f37);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f37);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
                              //  liferay-util:buffer
                              com.liferay.taglib.util.BufferTag _jspx_th_liferay_002dutil_005fbuffer_005f0 = (com.liferay.taglib.util.BufferTag) _005fjspx_005ftagPool_005fliferay_002dutil_005fbuffer_0026_005fvar.get(com.liferay.taglib.util.BufferTag.class);
                              _jspx_th_liferay_002dutil_005fbuffer_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_liferay_002dutil_005fbuffer_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ftable_002diterator_005f0);
                              // /html/portlet/journal/edit_article_extra.jspf(167,9) name = var type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_liferay_002dutil_005fbuffer_005f0.setVar("templateLabel");
                              int _jspx_eval_liferay_002dutil_005fbuffer_005f0 = _jspx_th_liferay_002dutil_005fbuffer_005f0.doStartTag();
                              if (_jspx_eval_liferay_002dutil_005fbuffer_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_liferay_002dutil_005fbuffer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_liferay_002dutil_005fbuffer_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_liferay_002dutil_005fbuffer_005f0.doInitBody();
                              }
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
                              //  portlet:renderURL
                              com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f2 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                              _jspx_th_portlet_005frenderURL_005f2.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005frenderURL_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dutil_005fbuffer_005f0);
                              // /html/portlet/journal/edit_article_extra.jspf(168,10) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005frenderURL_005f2.setVar("templateURL");
                              int _jspx_eval_portlet_005frenderURL_005f2 = _jspx_th_portlet_005frenderURL_005f2.doStartTag();
                              if (_jspx_eval_portlet_005frenderURL_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_portlet_005frenderURL_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_portlet_005frenderURL_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_portlet_005frenderURL_005f2.doInitBody();
                              }
                              do {
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              if (_jspx_meth_portlet_005fparam_005f4(_jspx_th_portlet_005frenderURL_005f2, _jspx_page_context))
                              return;
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f5 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f5.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f2);
                              // /html/portlet/journal/edit_article_extra.jspf(170,11) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f5.setName("redirect");
                              // /html/portlet/journal/edit_article_extra.jspf(170,11) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f5.setValue( currentURL );
                              int _jspx_eval_portlet_005fparam_005f5 = _jspx_th_portlet_005fparam_005f5.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f6 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f6.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f2);
                              // /html/portlet/journal/edit_article_extra.jspf(171,11) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f6.setName("groupId");
                              // /html/portlet/journal/edit_article_extra.jspf(171,11) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f6.setValue( String.valueOf(tableIteratorObj.getGroupId()) );
                              int _jspx_eval_portlet_005fparam_005f6 = _jspx_th_portlet_005fparam_005f6.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t\t");
                              //  portlet:param
                              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f7 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                              _jspx_th_portlet_005fparam_005f7.setPageContext(_jspx_page_context);
                              _jspx_th_portlet_005fparam_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f2);
                              // /html/portlet/journal/edit_article_extra.jspf(172,11) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f7.setName("templateId");
                              // /html/portlet/journal/edit_article_extra.jspf(172,11) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_portlet_005fparam_005f7.setValue( tableIteratorObj.getTemplateId() );
                              int _jspx_eval_portlet_005fparam_005f7 = _jspx_th_portlet_005fparam_005f7.doStartTag();
                              if (_jspx_th_portlet_005fparam_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
                              return;
                              }
                              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
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
                              java.lang.String templateURL = null;
                              templateURL = (java.lang.String) _jspx_page_context.findAttribute("templateURL");
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t");
                              //  aui:a
                              com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f0 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fid_005fhref_005fnobody.get(com.liferay.taglib.aui.ATag.class);
                              _jspx_th_aui_005fa_005f0.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005fa_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dutil_005fbuffer_005f0);
                              // /html/portlet/journal/edit_article_extra.jspf(175,10) name = href type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005fa_005f0.setHref( templateURL );
                              // /html/portlet/journal/edit_article_extra.jspf(175,10) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005fa_005f0.setLabel( HtmlUtil.escape(tableIteratorObj.getName()) );
                              // /html/portlet/journal/edit_article_extra.jspf(175,10) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005fa_005f0.setId("structureName");
                              int _jspx_eval_aui_005fa_005f0 = _jspx_th_aui_005fa_005f0.doStartTag();
                              if (_jspx_th_aui_005fa_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fid_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f0);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005fa_0026_005flabel_005fid_005fhref_005fnobody.reuse(_jspx_th_aui_005fa_005f0);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
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
                              java.lang.String templateLabel = null;
                              templateLabel = (java.lang.String) _jspx_page_context.findAttribute("templateLabel");
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
                              //  aui:input
                              com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f38 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineField_005fchecked_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                              _jspx_th_aui_005finput_005f38.setPageContext(_jspx_page_context);
                              _jspx_th_aui_005finput_005f38.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ftable_002diterator_005f0);
                              // /html/portlet/journal/edit_article_extra.jspf(178,9) name = checked type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005finput_005f38.setChecked( templateChecked );
                              // /html/portlet/journal/edit_article_extra.jspf(178,9) name = inlineField type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005finput_005f38.setInlineField( true );
                              // /html/portlet/journal/edit_article_extra.jspf(178,9) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005finput_005f38.setLabel( templateLabel );
                              // /html/portlet/journal/edit_article_extra.jspf(178,9) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005finput_005f38.setName("templateId");
                              // /html/portlet/journal/edit_article_extra.jspf(178,9) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005finput_005f38.setType("radio");
                              // /html/portlet/journal/edit_article_extra.jspf(178,9) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_aui_005finput_005f38.setValue( tableIteratorObj.getTemplateId() );
                              int _jspx_eval_aui_005finput_005f38 = _jspx_th_aui_005finput_005f38.doStartTag();
                              if (_jspx_th_aui_005finput_005f38.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineField_005fchecked_005fnobody.reuse(_jspx_th_aui_005finput_005f38);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005finlineField_005fchecked_005fnobody.reuse(_jspx_th_aui_005finput_005f38);
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
                              //  c:if
                              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f11 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                              _jspx_th_c_005fif_005f11.setPageContext(_jspx_page_context);
                              _jspx_th_c_005fif_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ftable_002diterator_005f0);
                              // /html/portlet/journal/edit_article_extra.jspf(180,9) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                              _jspx_th_c_005fif_005f11.setTest( tableIteratorObj.isSmallImage() );
                              int _jspx_eval_c_005fif_005f11 = _jspx_th_c_005fif_005f11.doStartTag();
                              if (_jspx_eval_c_005fif_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              do {
                              out.write("\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t<br />\n");
                              out.write("\n");
                              out.write("\t\t\t\t\t\t\t\t\t\t<img border=\"0\" hspace=\"0\" src=\"");
                              out.print( Validator.isNotNull(tableIteratorObj.getSmallImageURL()) ? tableIteratorObj.getSmallImageURL() : themeDisplay.getPathImage() + "/journal/template?img_id=" + tableIteratorObj.getSmallImageId() + "&t=" + ImageServletTokenUtil.getToken(tableIteratorObj.getSmallImageId()) );
                              out.write("\" vspace=\"0\" />\n");
                              out.write("\t\t\t\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t\t\t");
                              int evalDoAfterBody = _jspx_th_aui_005ffield_002dwrapper_005f3.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_aui_005ffield_002dwrapper_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.popBody();
                              }
                              }
                              if (_jspx_th_aui_005ffield_002dwrapper_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                              _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.reuse(_jspx_th_aui_005ffield_002dwrapper_005f3);
                              return;
                              }
                              _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.reuse(_jspx_th_aui_005ffield_002dwrapper_005f3);
                              out.write("\n");
                              out.write("\t\t\t\t\t\t");
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
                              out.write("\t\t\t\t\t");
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
                          out.write("\t\t\t\t</div>\n");
                          out.write("\t\t\t");
                          int evalDoAfterBody = _jspx_th_aui_005ffieldset_005f3.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_aui_005ffieldset_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.popBody();
                        }
                      }
                      if (_jspx_th_aui_005ffieldset_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f3);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f3);
                      out.write('\n');
                      out.write('	');
                      out.write('	');
                      int evalDoAfterBody = _jspx_th_liferay_002dui_005fpanel_005f3.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_liferay_002dui_005fpanel_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_liferay_002dui_005fpanel_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState.reuse(_jspx_th_liferay_002dui_005fpanel_005f3);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState.reuse(_jspx_th_liferay_002dui_005fpanel_005f3);
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t");
                  //  liferay-ui:panel
                  com.liferay.taglib.ui.PanelTag _jspx_th_liferay_002dui_005fpanel_005f4 = (com.liferay.taglib.ui.PanelTag) _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState.get(com.liferay.taglib.ui.PanelTag.class);
                  _jspx_th_liferay_002dui_005fpanel_005f4.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005fpanel_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
                  // /html/portlet/journal/edit_article_extra.jspf(194,2) name = defaultState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fpanel_005f4.setDefaultState("closed");
                  // /html/portlet/journal/edit_article_extra.jspf(194,2) name = extended type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fpanel_005f4.setExtended( false );
                  // /html/portlet/journal/edit_article_extra.jspf(194,2) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fpanel_005f4.setId("journalSchedulePanel");
                  // /html/portlet/journal/edit_article_extra.jspf(194,2) name = persistState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fpanel_005f4.setPersistState( true );
                  // /html/portlet/journal/edit_article_extra.jspf(194,2) name = title type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005fpanel_005f4.setTitle( LanguageUtil.get(pageContext, "schedule") );
                  int _jspx_eval_liferay_002dui_005fpanel_005f4 = _jspx_th_liferay_002dui_005fpanel_005f4.doStartTag();
                  if (_jspx_eval_liferay_002dui_005fpanel_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_liferay_002dui_005fpanel_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_liferay_002dui_005fpanel_005f4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_liferay_002dui_005fpanel_005f4.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t");
                      //  liferay-ui:error
                      com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f8 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                      _jspx_th_liferay_002dui_005ferror_005f8.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ferror_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f4);
                      // /html/portlet/journal/edit_article_extra.jspf(195,3) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ferror_005f8.setException( ArticleDisplayDateException.class );
                      // /html/portlet/journal/edit_article_extra.jspf(195,3) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ferror_005f8.setMessage("please-enter-a-valid-display-date");
                      int _jspx_eval_liferay_002dui_005ferror_005f8 = _jspx_th_liferay_002dui_005ferror_005f8.doStartTag();
                      if (_jspx_th_liferay_002dui_005ferror_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f8);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f8);
                      out.write("\n");
                      out.write("\t\t\t");
                      //  liferay-ui:error
                      com.liferay.taglib.ui.ErrorTag _jspx_th_liferay_002dui_005ferror_005f9 = (com.liferay.taglib.ui.ErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.get(com.liferay.taglib.ui.ErrorTag.class);
                      _jspx_th_liferay_002dui_005ferror_005f9.setPageContext(_jspx_page_context);
                      _jspx_th_liferay_002dui_005ferror_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f4);
                      // /html/portlet/journal/edit_article_extra.jspf(196,3) name = exception type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ferror_005f9.setException( ArticleExpirationDateException.class );
                      // /html/portlet/journal/edit_article_extra.jspf(196,3) name = message type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_liferay_002dui_005ferror_005f9.setMessage("please-enter-a-valid-expiration-date");
                      int _jspx_eval_liferay_002dui_005ferror_005f9 = _jspx_th_liferay_002dui_005ferror_005f9.doStartTag();
                      if (_jspx_th_liferay_002dui_005ferror_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f9);
                        return;
                      }
                      _005fjspx_005ftagPool_005fliferay_002dui_005ferror_0026_005fmessage_005fexception_005fnobody.reuse(_jspx_th_liferay_002dui_005ferror_005f9);
                      out.write("\n");
                      out.write("\n");
                      out.write("\t\t\t");
                      //  aui:fieldset
                      com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f4 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset.get(com.liferay.taglib.aui.FieldsetTag.class);
                      _jspx_th_aui_005ffieldset_005f4.setPageContext(_jspx_page_context);
                      _jspx_th_aui_005ffieldset_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f4);
                      int _jspx_eval_aui_005ffieldset_005f4 = _jspx_th_aui_005ffieldset_005f4.doStartTag();
                      if (_jspx_eval_aui_005ffieldset_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_aui_005ffieldset_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_aui_005ffieldset_005f4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_aui_005ffieldset_005f4.doInitBody();
                        }
                        do {
                          out.write("\n");
                          out.write("\t\t\t\t");
                          //  aui:input
                          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f39 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fformName_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                          _jspx_th_aui_005finput_005f39.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005finput_005f39.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                          // /html/portlet/journal/edit_article_extra.jspf(199,4) null
                          _jspx_th_aui_005finput_005f39.setDynamicAttribute(null, "formName", new String("fm1"));
                          // /html/portlet/journal/edit_article_extra.jspf(199,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f39.setName("displayDate");
                          // /html/portlet/journal/edit_article_extra.jspf(199,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f39.setValue( displayDate );
                          int _jspx_eval_aui_005finput_005f39 = _jspx_th_aui_005finput_005f39.doStartTag();
                          if (_jspx_th_aui_005finput_005f39.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fformName_005fnobody.reuse(_jspx_th_aui_005finput_005f39);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fformName_005fnobody.reuse(_jspx_th_aui_005finput_005f39);
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t");
                          //  aui:input
                          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f40 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fformName_005fdisabled_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                          _jspx_th_aui_005finput_005f40.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005finput_005f40.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                          // /html/portlet/journal/edit_article_extra.jspf(201,4) name = disabled type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f40.setDisabled( neverExpire );
                          // /html/portlet/journal/edit_article_extra.jspf(201,4) null
                          _jspx_th_aui_005finput_005f40.setDynamicAttribute(null, "formName", new String("fm1"));
                          // /html/portlet/journal/edit_article_extra.jspf(201,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f40.setName("expirationDate");
                          // /html/portlet/journal/edit_article_extra.jspf(201,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f40.setValue( expirationDate );
                          int _jspx_eval_aui_005finput_005f40 = _jspx_th_aui_005finput_005f40.doStartTag();
                          if (_jspx_th_aui_005finput_005f40.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fformName_005fdisabled_005fnobody.reuse(_jspx_th_aui_005finput_005f40);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fformName_005fdisabled_005fnobody.reuse(_jspx_th_aui_005finput_005f40);
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t");

				String taglibNeverExpireOnClick = renderResponse.getNamespace() + "disableInputDate('expirationDate', this.checked);";
				
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t");
                          //  aui:input
                          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f41 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonClick_005fname_005flabel_005finlineLabel_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                          _jspx_th_aui_005finput_005f41.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005finput_005f41.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                          // /html/portlet/journal/edit_article_extra.jspf(207,4) name = inlineLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f41.setInlineLabel("left");
                          // /html/portlet/journal/edit_article_extra.jspf(207,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f41.setLabel("never-auto-expire");
                          // /html/portlet/journal/edit_article_extra.jspf(207,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f41.setName("neverExpire");
                          // /html/portlet/journal/edit_article_extra.jspf(207,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f41.setValue( neverExpire );
                          // /html/portlet/journal/edit_article_extra.jspf(207,4) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f41.setOnClick( taglibNeverExpireOnClick );
                          // /html/portlet/journal/edit_article_extra.jspf(207,4) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f41.setType("checkbox");
                          int _jspx_eval_aui_005finput_005f41 = _jspx_th_aui_005finput_005f41.doStartTag();
                          if (_jspx_th_aui_005finput_005f41.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonClick_005fname_005flabel_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f41);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonClick_005fname_005flabel_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f41);
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t");
                          //  aui:input
                          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f42 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fformName_005fdisabled_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                          _jspx_th_aui_005finput_005f42.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005finput_005f42.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                          // /html/portlet/journal/edit_article_extra.jspf(209,4) name = disabled type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f42.setDisabled( neverReview );
                          // /html/portlet/journal/edit_article_extra.jspf(209,4) null
                          _jspx_th_aui_005finput_005f42.setDynamicAttribute(null, "formName", new String("fm1"));
                          // /html/portlet/journal/edit_article_extra.jspf(209,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f42.setName("reviewDate");
                          // /html/portlet/journal/edit_article_extra.jspf(209,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f42.setValue( reviewDate );
                          int _jspx_eval_aui_005finput_005f42 = _jspx_th_aui_005finput_005f42.doStartTag();
                          if (_jspx_th_aui_005finput_005f42.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fformName_005fdisabled_005fnobody.reuse(_jspx_th_aui_005finput_005f42);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005fname_005fformName_005fdisabled_005fnobody.reuse(_jspx_th_aui_005finput_005f42);
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t");

				String taglibNeverReviewOnClick = renderResponse.getNamespace() + "disableInputDate('reviewDate', this.checked);";
				
                          out.write("\n");
                          out.write("\n");
                          out.write("\t\t\t\t");
                          //  aui:input
                          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f43 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonClick_005fname_005finlineLabel_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
                          _jspx_th_aui_005finput_005f43.setPageContext(_jspx_page_context);
                          _jspx_th_aui_005finput_005f43.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f4);
                          // /html/portlet/journal/edit_article_extra.jspf(215,4) name = inlineLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f43.setInlineLabel("left");
                          // /html/portlet/journal/edit_article_extra.jspf(215,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f43.setName("neverReview");
                          // /html/portlet/journal/edit_article_extra.jspf(215,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f43.setValue( neverReview );
                          // /html/portlet/journal/edit_article_extra.jspf(215,4) name = onClick type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f43.setOnClick( taglibNeverReviewOnClick );
                          // /html/portlet/journal/edit_article_extra.jspf(215,4) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                          _jspx_th_aui_005finput_005f43.setType("checkbox");
                          int _jspx_eval_aui_005finput_005f43 = _jspx_th_aui_005finput_005f43.doStartTag();
                          if (_jspx_th_aui_005finput_005f43.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonClick_005fname_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f43);
                            return;
                          }
                          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fonClick_005fname_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f43);
                          out.write("\n");
                          out.write("\t\t\t");
                          int evalDoAfterBody = _jspx_th_aui_005ffieldset_005f4.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_aui_005ffieldset_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.popBody();
                        }
                      }
                      if (_jspx_th_aui_005ffieldset_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f4);
                        return;
                      }
                      _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f4);
                      out.write('\n');
                      out.write('	');
                      out.write('	');
                      int evalDoAfterBody = _jspx_th_liferay_002dui_005fpanel_005f4.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_liferay_002dui_005fpanel_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_liferay_002dui_005fpanel_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState.reuse(_jspx_th_liferay_002dui_005fpanel_005f4);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_0026_005ftitle_005fpersistState_005fid_005fextended_005fdefaultState.reuse(_jspx_th_liferay_002dui_005fpanel_005f4);
                  out.write('\n');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_liferay_002dui_005fpanel_002dcontainer_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended_005fcssClass.reuse(_jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005fpanel_002dcontainer_0026_005fpersistState_005fid_005fextended_005fcssClass.reuse(_jspx_th_liferay_002dui_005fpanel_002dcontainer_005f0);
              out.write('\n');
              int evalDoAfterBody = _jspx_th_liferay_002dui_005ftoggle_002darea_005f0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_liferay_002dui_005ftoggle_002darea_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_liferay_002dui_005ftoggle_002darea_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fliferay_002dui_005ftoggle_002darea_0026_005fshowMessage_005fid_005fhideMessage_005falign.reuse(_jspx_th_liferay_002dui_005ftoggle_002darea_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fliferay_002dui_005ftoggle_002darea_0026_005fshowMessage_005fid_005fhideMessage_005falign.reuse(_jspx_th_liferay_002dui_005ftoggle_002darea_005f0);
          out.write('\n');
          out.write('\n');
          //  aui:script
          com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f3 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript.get(com.liferay.taglib.aui.ScriptTag.class);
          _jspx_th_aui_005fscript_005f3.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fscript_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
          int _jspx_eval_aui_005fscript_005f3 = _jspx_th_aui_005fscript_005f3.doStartTag();
          if (_jspx_eval_aui_005fscript_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_aui_005fscript_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_aui_005fscript_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_aui_005fscript_005f3.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\tfunction ");
              if (_jspx_meth_portlet_005fnamespace_005f25(_jspx_th_aui_005fscript_005f3, _jspx_page_context))
                return;
              out.write("openTemplateSelector() {\n");
              out.write("\t\tif (confirm('");
              out.print( UnicodeLanguageUtil.get(pageContext, "selecting-a-template-will-change-the-structure,-available-input-fields,-and-available-templates") );
              out.write("')) {\n");
              out.write("\t\t\tvar templateWindow = window.open('");
              //  portlet:renderURL
              com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f3 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState.get(com.liferay.taglib.portlet.RenderURLTag.class);
              _jspx_th_portlet_005frenderURL_005f3.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005frenderURL_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f3);
              // /html/portlet/journal/edit_article_extra.jspf(224,37) name = windowState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005frenderURL_005f3.setWindowState( LiferayWindowState.POP_UP.toString() );
              int _jspx_eval_portlet_005frenderURL_005f3 = _jspx_th_portlet_005frenderURL_005f3.doStartTag();
              if (_jspx_eval_portlet_005frenderURL_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_portlet_005frenderURL_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_portlet_005frenderURL_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_portlet_005frenderURL_005f3.doInitBody();
                }
                do {
                  if (_jspx_meth_portlet_005fparam_005f8(_jspx_th_portlet_005frenderURL_005f3, _jspx_page_context))
                    return;
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f9 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f9.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f3);
                  // /html/portlet/journal/edit_article_extra.jspf(224,185) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f9.setName("groupId");
                  // /html/portlet/journal/edit_article_extra.jspf(224,185) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f9.setValue( String.valueOf(themeDisplay.getParentGroupId()) );
                  int _jspx_eval_portlet_005fparam_005f9 = _jspx_th_portlet_005fparam_005f9.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
                  int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f3.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_portlet_005frenderURL_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_portlet_005frenderURL_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState.reuse(_jspx_th_portlet_005frenderURL_005f3);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState.reuse(_jspx_th_portlet_005frenderURL_005f3);
              out.write("', 'template', 'directories=no,height=640,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,toolbar=no,width=680');\n");
              out.write("\t\t\tvoid('');\n");
              out.write("\t\t\ttemplateWindow.focus();\n");
              out.write("\t\t}\n");
              out.write("\t}\n");
              int evalDoAfterBody = _jspx_th_aui_005fscript_005f3.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_aui_005fscript_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_aui_005fscript_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f3);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f3);
          out.write("\n");
          out.write("\t\t</td>\n");
          out.write("\t</tr>\n");
          out.write("\t</table>\n");
          int evalDoAfterBody = _jspx_th_aui_005fform_005f1.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_aui_005fform_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
        }
      }
      if (_jspx_th_aui_005fform_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005fenctype_005faction.reuse(_jspx_th_aui_005fform_005f1);
        return;
      }
      _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005fenctype_005faction.reuse(_jspx_th_aui_005fform_005f1);
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
      out.write("<div id=\"");
      if (_jspx_meth_portlet_005fnamespace_005f26(_jspx_page_context))
        return;
      out.write("saveStructureTemplateDialogWrapper\" style=\"display: none;\">\n");
      out.write("\t");
      //  aui:fieldset
      com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f5 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset.get(com.liferay.taglib.aui.FieldsetTag.class);
      _jspx_th_aui_005ffieldset_005f5.setPageContext(_jspx_page_context);
      _jspx_th_aui_005ffieldset_005f5.setParent(null);
      int _jspx_eval_aui_005ffieldset_005f5 = _jspx_th_aui_005ffieldset_005f5.doStartTag();
      if (_jspx_eval_aui_005ffieldset_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_aui_005ffieldset_005f5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_aui_005ffieldset_005f5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_aui_005ffieldset_005f5.doInitBody();
        }
        do {
          out.write("\n");
          out.write("\t\t<div id=\"");
          if (_jspx_meth_portlet_005fnamespace_005f27(_jspx_th_aui_005ffieldset_005f5, _jspx_page_context))
            return;
          out.write("saveStructureTemplateDialog\" class=\"save-structure-template-dialog\">\n");
          out.write("\t\t\t<div class=\"portlet-msg-success save-structure-message aui-helper-hidden\" id=\"");
          if (_jspx_meth_portlet_005fnamespace_005f28(_jspx_th_aui_005ffieldset_005f5, _jspx_page_context))
            return;
          out.write("saveStructureMessage\">\n");
          out.write("\t\t\t\t");
          if (_jspx_meth_liferay_002dui_005fmessage_005f21(_jspx_th_aui_005ffieldset_005f5, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t\t</div>\n");
          out.write("\n");
          out.write("\t\t\t");
          if (_jspx_meth_aui_005finput_005f44(_jspx_th_aui_005ffieldset_005f5, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          if (_jspx_meth_aui_005finput_005f45(_jspx_th_aui_005ffieldset_005f5, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          if (_jspx_meth_aui_005fa_005f1(_jspx_th_aui_005ffieldset_005f5, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t<div class=\"journal-structureid-field aui-helper-hidden\" id=\"");
          if (_jspx_meth_portlet_005fnamespace_005f29(_jspx_th_aui_005ffieldset_005f5, _jspx_page_context))
            return;
          out.write("structureIdContainer\">\n");
          out.write("\t\t\t\t");
          //  aui:field-wrapper
          com.liferay.taglib.aui.FieldWrapperTag _jspx_th_aui_005ffield_002dwrapper_005f4 = (com.liferay.taglib.aui.FieldWrapperTag) _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.get(com.liferay.taglib.aui.FieldWrapperTag.class);
          _jspx_th_aui_005ffield_002dwrapper_005f4.setPageContext(_jspx_page_context);
          _jspx_th_aui_005ffield_002dwrapper_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f5);
          // /html/portlet/journal/edit_article_structure_extra.jspf(31,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005ffield_002dwrapper_005f4.setLabel("structure-id");
          int _jspx_eval_aui_005ffield_002dwrapper_005f4 = _jspx_th_aui_005ffield_002dwrapper_005f4.doStartTag();
          if (_jspx_eval_aui_005ffield_002dwrapper_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_aui_005ffield_002dwrapper_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_aui_005ffield_002dwrapper_005f4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_aui_005ffield_002dwrapper_005f4.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\t\t\t\t\t");
              if (_jspx_meth_aui_005finput_005f46(_jspx_th_aui_005ffield_002dwrapper_005f4, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t\t");
              //  aui:input
              com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f47 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
              _jspx_th_aui_005finput_005f47.setPageContext(_jspx_page_context);
              _jspx_th_aui_005finput_005f47.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f4);
              // /html/portlet/journal/edit_article_structure_extra.jspf(33,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f47.setLabel("");
              // /html/portlet/journal/edit_article_structure_extra.jspf(33,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f47.setName("saveStructureAutogenerateId");
              // /html/portlet/journal/edit_article_structure_extra.jspf(33,5) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f47.setType("checkbox");
              // /html/portlet/journal/edit_article_structure_extra.jspf(33,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005finput_005f47.setValue( true );
              int _jspx_eval_aui_005finput_005f47 = _jspx_th_aui_005finput_005f47.doStartTag();
              if (_jspx_th_aui_005finput_005f47.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fnobody.reuse(_jspx_th_aui_005finput_005f47);
                return;
              }
              _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005flabel_005fnobody.reuse(_jspx_th_aui_005finput_005f47);
              out.write("\n");
              out.write("\t\t\t\t");
              int evalDoAfterBody = _jspx_th_aui_005ffield_002dwrapper_005f4.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_aui_005ffield_002dwrapper_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_aui_005ffield_002dwrapper_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.reuse(_jspx_th_aui_005ffield_002dwrapper_005f4);
            return;
          }
          _005fjspx_005ftagPool_005faui_005ffield_002dwrapper_0026_005flabel.reuse(_jspx_th_aui_005ffield_002dwrapper_005f4);
          out.write("\n");
          out.write("\t\t\t</div>\n");
          out.write("\t\t</div>\n");
          out.write("\t");
          int evalDoAfterBody = _jspx_th_aui_005ffieldset_005f5.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_aui_005ffieldset_005f5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
        }
      }
      if (_jspx_th_aui_005ffieldset_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f5);
        return;
      }
      _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f5);
      out.write("\n");
      out.write("</div>\n");
      out.write("\n");
      out.write("<div class=\"journal-article-edit-field-wrapper aui-helper-hidden\" id=\"");
      if (_jspx_meth_portlet_005fnamespace_005f30(_jspx_page_context))
        return;
      out.write("journalArticleEditFieldWrapper\">\n");
      out.write("\t");
      //  aui:form
      com.liferay.taglib.aui.FormTag _jspx_th_aui_005fform_005f2 = (com.liferay.taglib.aui.FormTag) _005fjspx_005ftagPool_005faui_005fform_0026_005fmethod.get(com.liferay.taglib.aui.FormTag.class);
      _jspx_th_aui_005fform_005f2.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fform_005f2.setParent(null);
      // /html/portlet/journal/edit_article_structure_extra.jspf(41,1) null
      _jspx_th_aui_005fform_005f2.setDynamicAttribute(null, "method", new String("post"));
      int _jspx_eval_aui_005fform_005f2 = _jspx_th_aui_005fform_005f2.doStartTag();
      if (_jspx_eval_aui_005fform_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_aui_005fform_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_aui_005fform_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_aui_005fform_005f2.doInitBody();
        }
        do {
          out.write("\n");
          out.write("\t\t<div class=\"journal-article-edit-field\" id=\"");
          if (_jspx_meth_portlet_005fnamespace_005f31(_jspx_th_aui_005fform_005f2, _jspx_page_context))
            return;
          out.write("journalArticleEditField\">\n");
          out.write("\t\t\t<div class=\"portlet-msg-success journal-message aui-helper-hidden\" id=\"");
          if (_jspx_meth_portlet_005fnamespace_005f32(_jspx_th_aui_005fform_005f2, _jspx_page_context))
            return;
          out.write("journalMessage\">\n");
          out.write("\t\t\t\t");
          if (_jspx_meth_liferay_002dui_005fmessage_005f23(_jspx_th_aui_005fform_005f2, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t\t</div>\n");
          out.write("\n");
          out.write("\t\t\t<strong>");
          if (_jspx_meth_liferay_002dui_005fmessage_005f24(_jspx_th_aui_005fform_005f2, _jspx_page_context))
            return;
          out.write("</strong>\n");
          out.write("\n");
          out.write("\t\t\t");
          //  aui:select
          com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f3 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005fdisabled.get(com.liferay.taglib.aui.SelectTag.class);
          _jspx_th_aui_005fselect_005f3.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fselect_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f2);
          // /html/portlet/journal/edit_article_structure_extra.jspf(49,3) name = disabled type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fselect_005f3.setDisabled( true );
          // /html/portlet/journal/edit_article_structure_extra.jspf(49,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fselect_005f3.setName("fieldType");
          int _jspx_eval_aui_005fselect_005f3 = _jspx_th_aui_005fselect_005f3.doStartTag();
          if (_jspx_eval_aui_005fselect_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_aui_005fselect_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_aui_005fselect_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_aui_005fselect_005f3.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_aui_005foption_005f5(_jspx_th_aui_005fselect_005f3, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_aui_005foption_005f6(_jspx_th_aui_005fselect_005f3, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_aui_005foption_005f7(_jspx_th_aui_005fselect_005f3, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_aui_005foption_005f8(_jspx_th_aui_005fselect_005f3, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_aui_005foption_005f9(_jspx_th_aui_005fselect_005f3, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_aui_005foption_005f10(_jspx_th_aui_005fselect_005f3, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_aui_005foption_005f11(_jspx_th_aui_005fselect_005f3, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_aui_005foption_005f12(_jspx_th_aui_005fselect_005f3, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_aui_005foption_005f13(_jspx_th_aui_005fselect_005f3, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_aui_005foption_005f14(_jspx_th_aui_005fselect_005f3, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_aui_005foption_005f15(_jspx_th_aui_005fselect_005f3, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_aui_005foption_005f16(_jspx_th_aui_005fselect_005f3, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t");
              int evalDoAfterBody = _jspx_th_aui_005fselect_005f3.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_aui_005fselect_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_aui_005fselect_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005fdisabled.reuse(_jspx_th_aui_005fselect_005f3);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005fdisabled.reuse(_jspx_th_aui_005fselect_005f3);
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          if (_jspx_meth_aui_005finput_005f48(_jspx_th_aui_005fform_005f2, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          if (_jspx_meth_aui_005fselect_005f4(_jspx_th_aui_005fform_005f2, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          if (_jspx_meth_aui_005finput_005f49(_jspx_th_aui_005fform_005f2, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t<fieldset class=\"user-instructions\">\n");
          out.write("\t\t\t\t");
          if (_jspx_meth_aui_005finput_005f50(_jspx_th_aui_005fform_005f2, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t");
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f51 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f51.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f51.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f2);
          // /html/portlet/journal/edit_article_structure_extra.jspf(77,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f51.setName("displayAsTooltip");
          // /html/portlet/journal/edit_article_structure_extra.jspf(77,4) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f51.setType("checkbox");
          // /html/portlet/journal/edit_article_structure_extra.jspf(77,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f51.setValue( true );
          int _jspx_eval_aui_005finput_005f51 = _jspx_th_aui_005finput_005f51.doStartTag();
          if (_jspx_th_aui_005finput_005f51.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f51);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f51);
          out.write("\n");
          out.write("\t\t\t</fieldset>\n");
          out.write("\n");
          out.write("\t\t\t");
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f52 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f52.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f52.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f2);
          // /html/portlet/journal/edit_article_structure_extra.jspf(80,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f52.setName("repeatable");
          // /html/portlet/journal/edit_article_structure_extra.jspf(80,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f52.setType("checkbox");
          // /html/portlet/journal/edit_article_structure_extra.jspf(80,3) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f52.setValue( true );
          int _jspx_eval_aui_005finput_005f52 = _jspx_th_aui_005finput_005f52.doStartTag();
          if (_jspx_th_aui_005finput_005f52.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f52);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f52);
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f53 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f53.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f53.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f2);
          // /html/portlet/journal/edit_article_structure_extra.jspf(82,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f53.setName("required");
          // /html/portlet/journal/edit_article_structure_extra.jspf(82,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f53.setType("checkbox");
          // /html/portlet/journal/edit_article_structure_extra.jspf(82,3) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f53.setValue( true );
          int _jspx_eval_aui_005finput_005f53 = _jspx_th_aui_005finput_005f53.doStartTag();
          if (_jspx_th_aui_005finput_005f53.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f53);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005fvalue_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f53);
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          if (_jspx_meth_aui_005fbutton_002drow_005f1(_jspx_th_aui_005fform_005f2, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t</div>\n");
          out.write("\t");
          int evalDoAfterBody = _jspx_th_aui_005fform_005f2.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_aui_005fform_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
        }
      }
      if (_jspx_th_aui_005fform_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005faui_005fform_0026_005fmethod.reuse(_jspx_th_aui_005fform_005f2);
        return;
      }
      _005fjspx_005ftagPool_005faui_005fform_0026_005fmethod.reuse(_jspx_th_aui_005fform_005f2);
      out.write("\n");
      out.write("</div>\n");
      out.write("\n");
      out.write("<div class=\"journal-fieldmodel-container\" id=\"journalFieldModelContainer\">\n");
      out.write("\t");
      //  aui:fieldset
      com.liferay.taglib.aui.FieldsetTag _jspx_th_aui_005ffieldset_005f6 = (com.liferay.taglib.aui.FieldsetTag) _005fjspx_005ftagPool_005faui_005ffieldset.get(com.liferay.taglib.aui.FieldsetTag.class);
      _jspx_th_aui_005ffieldset_005f6.setPageContext(_jspx_page_context);
      _jspx_th_aui_005ffieldset_005f6.setParent(null);
      int _jspx_eval_aui_005ffieldset_005f6 = _jspx_th_aui_005ffieldset_005f6.doStartTag();
      if (_jspx_eval_aui_005ffieldset_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_aui_005ffieldset_005f6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_aui_005ffieldset_005f6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_aui_005ffieldset_005f6.doInitBody();
        }
        do {
          out.write("\n");
          out.write("\t\t<div dataType=\"text\">\n");
          out.write("\t\t\t");
          if (_jspx_meth_aui_005finput_005f54(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t</div>\n");
          out.write("\n");
          out.write("\t\t<div dataType=\"text_area\">\n");
          out.write("\t\t\t");
          if (_jspx_meth_aui_005finput_005f55(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t</div>\n");
          out.write("\n");
          out.write("\t\t<div dataType=\"text_box\">\n");
          out.write("\t\t\t");
          if (_jspx_meth_aui_005finput_005f56(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t</div>\n");
          out.write("\n");
          out.write("\t\t<div dataType=\"image\">\n");
          out.write("\t\t\t");
          if (_jspx_meth_aui_005finput_005f57(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t</div>\n");
          out.write("\n");
          out.write("\t\t<div dataType=\"image_gallery\">\n");
          out.write("\t\t\t");
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f58 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005finlineField_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f58.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f58.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
          // /html/portlet/journal/edit_article_structure_extra.jspf(114,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f58.setCssClass("lfr-input-text-container");
          // /html/portlet/journal/edit_article_structure_extra.jspf(114,3) name = inlineField type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f58.setInlineField( true );
          // /html/portlet/journal/edit_article_structure_extra.jspf(114,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f58.setLabel("");
          // /html/portlet/journal/edit_article_structure_extra.jspf(114,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f58.setName("text");
          // /html/portlet/journal/edit_article_structure_extra.jspf(114,3) null
          _jspx_th_aui_005finput_005f58.setDynamicAttribute(null, "size", new String("55"));
          // /html/portlet/journal/edit_article_structure_extra.jspf(114,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f58.setType("text");
          int _jspx_eval_aui_005finput_005f58 = _jspx_th_aui_005finput_005f58.doStartTag();
          if (_jspx_th_aui_005finput_005f58.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005finlineField_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f58);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005finlineField_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f58);
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          //  portlet:renderURL
          com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f4 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
          _jspx_th_portlet_005frenderURL_005f4.setPageContext(_jspx_page_context);
          _jspx_th_portlet_005frenderURL_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
          // /html/portlet/journal/edit_article_structure_extra.jspf(116,3) name = windowState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_portlet_005frenderURL_005f4.setWindowState( LiferayWindowState.POP_UP.toString() );
          // /html/portlet/journal/edit_article_structure_extra.jspf(116,3) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_portlet_005frenderURL_005f4.setVar("selectIGURL");
          int _jspx_eval_portlet_005frenderURL_005f4 = _jspx_th_portlet_005frenderURL_005f4.doStartTag();
          if (_jspx_eval_portlet_005frenderURL_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_portlet_005frenderURL_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_portlet_005frenderURL_005f4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_portlet_005frenderURL_005f4.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_portlet_005fparam_005f10(_jspx_th_portlet_005frenderURL_005f4, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f11 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f11.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f4);
              // /html/portlet/journal/edit_article_structure_extra.jspf(118,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f11.setName("groupId");
              // /html/portlet/journal/edit_article_structure_extra.jspf(118,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f11.setValue( String.valueOf(groupId) );
              int _jspx_eval_portlet_005fparam_005f11 = _jspx_th_portlet_005fparam_005f11.doStartTag();
              if (_jspx_th_portlet_005fparam_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f11);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f11);
              out.write("\n");
              out.write("\t\t\t");
              int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f4.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_portlet_005frenderURL_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_portlet_005frenderURL_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f4);
            return;
          }
          _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f4);
          java.lang.String selectIGURL = null;
          selectIGURL = (java.lang.String) _jspx_page_context.findAttribute("selectIGURL");
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");

			Map<String,Object> data = new HashMap<String,Object>();

			data.put("ImagegalleryUrl", selectIGURL);
			
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          //  aui:button
          com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f16 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fdata_005fcssClass_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
          _jspx_th_aui_005fbutton_005f16.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fbutton_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
          // /html/portlet/journal/edit_article_structure_extra.jspf(127,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fbutton_005f16.setCssClass("journal-imagegallery-button");
          // /html/portlet/journal/edit_article_structure_extra.jspf(127,3) name = data type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fbutton_005f16.setData( data );
          // /html/portlet/journal/edit_article_structure_extra.jspf(127,3) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fbutton_005f16.setValue("select");
          int _jspx_eval_aui_005fbutton_005f16 = _jspx_th_aui_005fbutton_005f16.doStartTag();
          if (_jspx_th_aui_005fbutton_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fdata_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f16);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fdata_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f16);
          out.write("\n");
          out.write("\t\t</div>\n");
          out.write("\n");
          out.write("\t\t<div dataType=\"document_library\">\n");
          out.write("\t\t\t");
          //  aui:input
          com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f59 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005finlineField_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
          _jspx_th_aui_005finput_005f59.setPageContext(_jspx_page_context);
          _jspx_th_aui_005finput_005f59.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
          // /html/portlet/journal/edit_article_structure_extra.jspf(131,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f59.setCssClass("lfr-input-text-container");
          // /html/portlet/journal/edit_article_structure_extra.jspf(131,3) name = inlineField type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f59.setInlineField( true );
          // /html/portlet/journal/edit_article_structure_extra.jspf(131,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f59.setLabel("");
          // /html/portlet/journal/edit_article_structure_extra.jspf(131,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f59.setName("text");
          // /html/portlet/journal/edit_article_structure_extra.jspf(131,3) null
          _jspx_th_aui_005finput_005f59.setDynamicAttribute(null, "size", new String("55"));
          // /html/portlet/journal/edit_article_structure_extra.jspf(131,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005finput_005f59.setType("text");
          int _jspx_eval_aui_005finput_005f59 = _jspx_th_aui_005finput_005f59.doStartTag();
          if (_jspx_th_aui_005finput_005f59.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005finlineField_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f59);
            return;
          }
          _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005finlineField_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f59);
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          //  portlet:renderURL
          com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f5 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
          _jspx_th_portlet_005frenderURL_005f5.setPageContext(_jspx_page_context);
          _jspx_th_portlet_005frenderURL_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
          // /html/portlet/journal/edit_article_structure_extra.jspf(133,3) name = windowState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_portlet_005frenderURL_005f5.setWindowState( LiferayWindowState.POP_UP.toString() );
          // /html/portlet/journal/edit_article_structure_extra.jspf(133,3) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_portlet_005frenderURL_005f5.setVar("selectDLURL");
          int _jspx_eval_portlet_005frenderURL_005f5 = _jspx_th_portlet_005frenderURL_005f5.doStartTag();
          if (_jspx_eval_portlet_005frenderURL_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_portlet_005frenderURL_005f5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_portlet_005frenderURL_005f5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_portlet_005frenderURL_005f5.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_portlet_005fparam_005f12(_jspx_th_portlet_005frenderURL_005f5, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f13 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f13.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f5);
              // /html/portlet/journal/edit_article_structure_extra.jspf(135,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f13.setName("groupId");
              // /html/portlet/journal/edit_article_structure_extra.jspf(135,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f13.setValue( String.valueOf(groupId) );
              int _jspx_eval_portlet_005fparam_005f13 = _jspx_th_portlet_005fparam_005f13.doStartTag();
              if (_jspx_th_portlet_005fparam_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f13);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f13);
              out.write("\n");
              out.write("\t\t\t");
              int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f5.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_portlet_005frenderURL_005f5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_portlet_005frenderURL_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f5);
            return;
          }
          _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f5);
          java.lang.String selectDLURL = null;
          selectDLURL = (java.lang.String) _jspx_page_context.findAttribute("selectDLURL");
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");

			data = new HashMap<String,Object>();

			data.put("DocumentlibraryUrl", selectDLURL);
			
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t");
          //  aui:button
          com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f17 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fdata_005fcssClass_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
          _jspx_th_aui_005fbutton_005f17.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fbutton_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
          // /html/portlet/journal/edit_article_structure_extra.jspf(144,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fbutton_005f17.setCssClass("journal-documentlibrary-button");
          // /html/portlet/journal/edit_article_structure_extra.jspf(144,3) name = data type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fbutton_005f17.setData( data );
          // /html/portlet/journal/edit_article_structure_extra.jspf(144,3) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fbutton_005f17.setValue("select");
          int _jspx_eval_aui_005fbutton_005f17 = _jspx_th_aui_005fbutton_005f17.doStartTag();
          if (_jspx_th_aui_005fbutton_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fdata_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f17);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fdata_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f17);
          out.write("\n");
          out.write("\t\t</div>\n");
          out.write("\n");
          out.write("\t\t<div dataType=\"boolean\">\n");
          out.write("\t\t\t<div class=\"journal-subfield\">\n");
          out.write("\t\t\t\t");
          if (_jspx_meth_aui_005finput_005f60(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t\t</div>\n");
          out.write("\t\t</div>\n");
          out.write("\n");
          out.write("\t\t<div dataType=\"selection_break\">\n");
          out.write("\t\t\t<div class=\"separator\"></div>\n");
          out.write("\t\t</div>\n");
          out.write("\n");
          out.write("\t\t<div dataType=\"list\">\n");
          out.write("\t\t\t<div class=\"journal-list-subfield\">\n");
          out.write("\t\t\t\t");
          if (_jspx_meth_aui_005fselect_005f5(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t<span class=\"journal-icon-button journal-delete-field\">\n");
          out.write("\t\t\t\t\t");
          if (_jspx_meth_liferay_002dui_005ficon_005f0(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          if (_jspx_meth_liferay_002dui_005fmessage_005f25(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t\t\t</span>\n");
          out.write("\n");
          out.write("\t\t\t\t<div class=\"journal-edit-field-control\">\n");
          out.write("\t\t\t\t\t<br /><br />\n");
          out.write("\n");
          out.write("\t\t\t\t\t<input class=\"journal-list-key\" title=\"");
          if (_jspx_meth_liferay_002dui_005fmessage_005f26(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\" size=\"15\" type=\"text\" value=\"");
          if (_jspx_meth_liferay_002dui_005fmessage_005f27(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\" />\n");
          out.write("\n");
          out.write("\t\t\t\t\t<input class=\"journal-list-value\" size=\"15\" title=\"");
          if (_jspx_meth_liferay_002dui_005fmessage_005f28(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\" type=\"text\" value=\"value\" />\n");
          out.write("\n");
          out.write("\t\t\t\t\t<span class=\"journal-icon-button journal-add-field\">\n");
          out.write("\t\t\t\t\t\t");
          if (_jspx_meth_liferay_002dui_005ficon_005f1(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write(' ');
          if (_jspx_meth_liferay_002dui_005fmessage_005f29(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t\t\t\t</span>\n");
          out.write("\t\t\t\t</div>\n");
          out.write("\t\t\t</div>\n");
          out.write("\t\t</div>\n");
          out.write("\n");
          out.write("\t\t<div dataType=\"multi-list\">\n");
          out.write("\t\t\t<div class=\"journal-list-subfield\">\n");
          out.write("\t\t\t\t");
          if (_jspx_meth_aui_005fselect_005f6(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t<span class=\"journal-icon-button journal-delete-field\">\n");
          out.write("\t\t\t\t\t");
          if (_jspx_meth_liferay_002dui_005ficon_005f2(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          if (_jspx_meth_liferay_002dui_005fmessage_005f30(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t\t\t</span>\n");
          out.write("\n");
          out.write("\t\t\t\t<div class=\"journal-edit-field-control\">\n");
          out.write("\t\t\t\t\t<br /><br />\n");
          out.write("\n");
          out.write("\t\t\t\t\t<input class=\"journal-list-key\" size=\"15\" title=\"");
          if (_jspx_meth_liferay_002dui_005fmessage_005f31(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\" type=\"text\" value=\"");
          if (_jspx_meth_liferay_002dui_005fmessage_005f32(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\" />\n");
          out.write("\n");
          out.write("\t\t\t\t\t<input class=\"journal-list-value\" size=\"15\" title=\"");
          if (_jspx_meth_liferay_002dui_005fmessage_005f33(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\" type=\"text\" value=\"value\" />\n");
          out.write("\n");
          out.write("\t\t\t\t\t<span class=\"journal-icon-button journal-add-field\">\n");
          out.write("\t\t\t\t\t\t");
          if (_jspx_meth_liferay_002dui_005ficon_005f3(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write(' ');
          if (_jspx_meth_liferay_002dui_005fmessage_005f34(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t\t\t\t</span>\n");
          out.write("\t\t\t\t</div>\n");
          out.write("\t\t\t</div>\n");
          out.write("\t\t</div>\n");
          out.write("\n");
          out.write("\t\t<div dataType=\"link_to_layout\">\n");
          out.write("\t\t\t");
          //  aui:select
          com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f7 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname_005flabel.get(com.liferay.taglib.aui.SelectTag.class);
          _jspx_th_aui_005fselect_005f7.setPageContext(_jspx_page_context);
          _jspx_th_aui_005fselect_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
          // /html/portlet/journal/edit_article_structure_extra.jspf(202,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fselect_005f7.setLabel("");
          // /html/portlet/journal/edit_article_structure_extra.jspf(202,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fselect_005f7.setName("linkToLayout");
          // /html/portlet/journal/edit_article_structure_extra.jspf(202,3) name = showEmptyOption type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_aui_005fselect_005f7.setShowEmptyOption( true );
          int _jspx_eval_aui_005fselect_005f7 = _jspx_th_aui_005fselect_005f7.doStartTag();
          if (_jspx_eval_aui_005fselect_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_aui_005fselect_005f7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_aui_005fselect_005f7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_aui_005fselect_005f7.doInitBody();
            }
            do {
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t");

				LayoutLister layoutLister = new LayoutLister();

				LayoutView layoutView = layoutLister.getLayoutView(layout.getGroupId(), layout.isPrivateLayout(), "root", locale);

				List layoutList = layoutView.getList();

				for (int i = 0; i < layoutList.size(); i++) {

					// id | parentId | ls | obj id | name | img | depth

					String layoutDesc = (String)layoutList.get(i);

					String[] nodeValues = StringUtil.split(layoutDesc, "|");

					long objId = GetterUtil.getLong(nodeValues[3]);
					String name = nodeValues[4];

					int depth2 = 0;

					if (i != 0) {
						depth2 = GetterUtil.getInteger(nodeValues[6]);
					}

					for (int j = 0; j < depth2; j++) {
						name = "-&nbsp;" + name;
					}

					Layout linkableLayout = null;

					try {
						linkableLayout = LayoutLocalServiceUtil.getLayout(objId);
					}
					catch (Exception e) {
					}

					if (linkableLayout != null) {
				
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t\t\t");
              //  aui:option
              com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f20 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
              _jspx_th_aui_005foption_005f20.setPageContext(_jspx_page_context);
              _jspx_th_aui_005foption_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f7);
              // /html/portlet/journal/edit_article_structure_extra.jspf(243,6) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005foption_005f20.setLabel( name );
              // /html/portlet/journal/edit_article_structure_extra.jspf(243,6) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_aui_005foption_005f20.setValue( linkableLayout.getLayoutId() );
              int _jspx_eval_aui_005foption_005f20 = _jspx_th_aui_005foption_005f20.doStartTag();
              if (_jspx_th_aui_005foption_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f20);
                return;
              }
              _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f20);
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t\t");

				}
					}
				
              out.write("\n");
              out.write("\n");
              out.write("\t\t\t");
              int evalDoAfterBody = _jspx_th_aui_005fselect_005f7.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_aui_005fselect_005f7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_aui_005fselect_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname_005flabel.reuse(_jspx_th_aui_005fselect_005f7);
            return;
          }
          _005fjspx_005ftagPool_005faui_005fselect_0026_005fshowEmptyOption_005fname_005flabel.reuse(_jspx_th_aui_005fselect_005f7);
          out.write("\n");
          out.write("\t\t</div>\n");
          out.write("\n");
          out.write("\t\t<div id=\"repeatable-field-image-model\">\n");
          out.write("\t\t\t<span class=\"repeatable-field-image\">\n");
          out.write("\t\t\t\t");
          if (_jspx_meth_liferay_002dui_005ficon_005f4(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t");
          if (_jspx_meth_liferay_002dui_005ficon_005f5(_jspx_th_aui_005ffieldset_005f6, _jspx_page_context))
            return;
          out.write("\n");
          out.write("\t\t\t</span>\n");
          out.write("\t\t</div>\n");
          out.write("\t");
          int evalDoAfterBody = _jspx_th_aui_005ffieldset_005f6.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_aui_005ffieldset_005f6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
        }
      }
      if (_jspx_th_aui_005ffieldset_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f6);
        return;
      }
      _005fjspx_005ftagPool_005faui_005ffieldset.reuse(_jspx_th_aui_005ffieldset_005f6);
      out.write("\n");
      out.write("</div>\n");
      out.write("\n");
      out.write("<div class=\"aui-html-template\" id=\"");
      if (_jspx_meth_portlet_005fnamespace_005f33(_jspx_page_context))
        return;
      out.write("editBtnTemplate\">\n");
      out.write("\t");
      if (_jspx_meth_aui_005fbutton_005f18(_jspx_page_context))
        return;
      out.write("\n");
      out.write("</div>\n");
      out.write("\n");
      out.write("<div class=\"aui-html-template\" id=\"");
      if (_jspx_meth_portlet_005fnamespace_005f34(_jspx_page_context))
        return;
      out.write("repeatableBtnTemplate\">\n");
      out.write("\t");
      if (_jspx_meth_aui_005fbutton_005f19(_jspx_page_context))
        return;
      out.write("\n");
      out.write("</div>");
      out.write('\n');
      out.write('\n');
      //  aui:script
      com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f4 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript.get(com.liferay.taglib.aui.ScriptTag.class);
      _jspx_th_aui_005fscript_005f4.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fscript_005f4.setParent(null);
      int _jspx_eval_aui_005fscript_005f4 = _jspx_th_aui_005fscript_005f4.doStartTag();
      if (_jspx_eval_aui_005fscript_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_aui_005fscript_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_aui_005fscript_005f4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_aui_005fscript_005f4.doInitBody();
        }
        do {
          out.write("\n");
          out.write("\tvar ");
          if (_jspx_meth_portlet_005fnamespace_005f35(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("documentLibraryInput = null;\n");
          out.write("\tvar ");
          if (_jspx_meth_portlet_005fnamespace_005f36(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("imageGalleryInput = null;\n");
          out.write("\tvar ");
          if (_jspx_meth_portlet_005fnamespace_005f37(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("contentChangedFlag = false;\n");
          out.write("\n");
          out.write("\tfunction ");
          if (_jspx_meth_portlet_005fnamespace_005f38(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("contentChanged() {\n");
          out.write("\t\t");
          if (_jspx_meth_portlet_005fnamespace_005f39(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("contentChangedFlag = true;\n");
          out.write("\t}\n");
          out.write("\n");
          out.write("\tfunction ");
          if (_jspx_meth_portlet_005fnamespace_005f40(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("deleteArticle() {\n");
          out.write("\t\t");
          //  c:choose
          org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f4 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
          _jspx_th_c_005fchoose_005f4.setPageContext(_jspx_page_context);
          _jspx_th_c_005fchoose_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
          int _jspx_eval_c_005fchoose_005f4 = _jspx_th_c_005fchoose_005f4.doStartTag();
          if (_jspx_eval_c_005fchoose_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\t\t\t");
              //  c:when
              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f4 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
              _jspx_th_c_005fwhen_005f4.setPageContext(_jspx_page_context);
              _jspx_th_c_005fwhen_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f4);
              // /html/portlet/journal/edit_article.jsp(707,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fwhen_005f4.setTest( (article != null) && article.isDraft() );
              int _jspx_eval_c_005fwhen_005f4 = _jspx_th_c_005fwhen_005f4.doStartTag();
              if (_jspx_eval_c_005fwhen_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\tvar confirmationMessage = '");
                  out.print( UnicodeLanguageUtil.get(pageContext, "are-you-sure-you-want-to-discard-this-draft") );
                  out.write("';\n");
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
              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f4 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
              _jspx_th_c_005fotherwise_005f4.setPageContext(_jspx_page_context);
              _jspx_th_c_005fotherwise_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f4);
              int _jspx_eval_c_005fotherwise_005f4 = _jspx_th_c_005fotherwise_005f4.doStartTag();
              if (_jspx_eval_c_005fotherwise_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t\tvar confirmationMessage = '");
                  out.print( UnicodeLanguageUtil.get(pageContext, "are-you-sure-you-want-to-delete-this-article-version") );
                  out.write("';\n");
                  out.write("\t\t\t");
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
          out.write("\t\tif (confirm(confirmationMessage)) {\n");
          out.write("\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f41(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1.");
          if (_jspx_meth_portlet_005fnamespace_005f42(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.print( Constants.CMD );
          out.write(".value = \"");
          out.print( Constants.DELETE );
          out.write("\";\n");
          out.write("\t\t\tsubmitForm(document.");
          if (_jspx_meth_portlet_005fnamespace_005f43(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1);\n");
          out.write("\t\t}\n");
          out.write("\t}\n");
          out.write("\n");
          out.write("\tfunction ");
          if (_jspx_meth_portlet_005fnamespace_005f44(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("editorContentChanged(text) {\n");
          out.write("\t\t");
          if (_jspx_meth_portlet_005fnamespace_005f45(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("contentChanged();\n");
          out.write("\t}\n");
          out.write("\n");
          out.write("\tfunction ");
          if (_jspx_meth_portlet_005fnamespace_005f46(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("expireArticle() {\n");
          out.write("\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f47(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1.");
          if (_jspx_meth_portlet_005fnamespace_005f48(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.print( Constants.CMD );
          out.write(".value = \"");
          out.print( Constants.EXPIRE );
          out.write("\";\n");
          out.write("\t\tsubmitForm(document.");
          if (_jspx_meth_portlet_005fnamespace_005f49(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1);\n");
          out.write("\t}\n");
          out.write("\n");
          out.write("\tfunction ");
          if (_jspx_meth_portlet_005fnamespace_005f50(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("getChoice(value) {\n");
          out.write("\t\tfor (var i = 0; i < document.");
          if (_jspx_meth_portlet_005fnamespace_005f51(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1.");
          if (_jspx_meth_portlet_005fnamespace_005f52(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("languageId.length; i++) {\n");
          out.write("\t\t\tif (document.");
          if (_jspx_meth_portlet_005fnamespace_005f53(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1.");
          if (_jspx_meth_portlet_005fnamespace_005f54(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("languageId.options[i].value == value) {\n");
          out.write("\t\t\t\treturn document.");
          if (_jspx_meth_portlet_005fnamespace_005f55(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1.");
          if (_jspx_meth_portlet_005fnamespace_005f56(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("languageId.options[i].index;\n");
          out.write("\t\t\t}\n");
          out.write("\t\t}\n");
          out.write("\n");
          out.write("\t\treturn null;\n");
          out.write("\t}\n");
          out.write("\n");
          out.write("\tfunction ");
          if (_jspx_meth_portlet_005fnamespace_005f57(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("getLanguageViewURL(languageId) {\n");
          out.write("\t\treturn \"");
          //  portlet:renderURL
          com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f6 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState.get(com.liferay.taglib.portlet.RenderURLTag.class);
          _jspx_th_portlet_005frenderURL_005f6.setPageContext(_jspx_page_context);
          _jspx_th_portlet_005frenderURL_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
          // /html/portlet/journal/edit_article.jsp(741,10) name = windowState type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_portlet_005frenderURL_005f6.setWindowState( WindowState.MAXIMIZED.toString() );
          int _jspx_eval_portlet_005frenderURL_005f6 = _jspx_th_portlet_005frenderURL_005f6.doStartTag();
          if (_jspx_eval_portlet_005frenderURL_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_portlet_005frenderURL_005f6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_portlet_005frenderURL_005f6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_portlet_005frenderURL_005f6.doInitBody();
            }
            do {
              if (_jspx_meth_portlet_005fparam_005f14(_jspx_th_portlet_005frenderURL_005f6, _jspx_page_context))
                return;
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f15 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f15.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f6);
              // /html/portlet/journal/edit_article.jsp(741,151) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f15.setName("redirect");
              // /html/portlet/journal/edit_article.jsp(741,151) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f15.setValue( redirect );
              int _jspx_eval_portlet_005fparam_005f15 = _jspx_th_portlet_005fparam_005f15.doStartTag();
              if (_jspx_th_portlet_005fparam_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f15);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f15);
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f16 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f16.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f6);
              // /html/portlet/journal/edit_article.jsp(741,208) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f16.setName("groupId");
              // /html/portlet/journal/edit_article.jsp(741,208) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f16.setValue( String.valueOf(groupId) );
              int _jspx_eval_portlet_005fparam_005f16 = _jspx_th_portlet_005fparam_005f16.doStartTag();
              if (_jspx_th_portlet_005fparam_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f16);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f16);
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f17 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f17.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f6);
              // /html/portlet/journal/edit_article.jsp(741,279) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f17.setName("articleId");
              // /html/portlet/journal/edit_article.jsp(741,279) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f17.setValue( articleId );
              int _jspx_eval_portlet_005fparam_005f17 = _jspx_th_portlet_005fparam_005f17.doStartTag();
              if (_jspx_th_portlet_005fparam_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f17);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f17);
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f18 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f18.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f6);
              // /html/portlet/journal/edit_article.jsp(741,338) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f18.setName("version");
              // /html/portlet/journal/edit_article.jsp(741,338) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f18.setValue( String.valueOf(version) );
              int _jspx_eval_portlet_005fparam_005f18 = _jspx_th_portlet_005fparam_005f18.doStartTag();
              if (_jspx_th_portlet_005fparam_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f18);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f18);
              int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f6.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_portlet_005frenderURL_005f6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_portlet_005frenderURL_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState.reuse(_jspx_th_portlet_005frenderURL_005f6);
            return;
          }
          _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fwindowState.reuse(_jspx_th_portlet_005frenderURL_005f6);
          out.write('&');
          if (_jspx_meth_portlet_005fnamespace_005f58(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("languageId=\" + languageId;\n");
          out.write("\t}\n");
          out.write("\n");
          out.write("\tfunction ");
          if (_jspx_meth_portlet_005fnamespace_005f59(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("initEditor() {\n");
          out.write("\t\treturn \"");
          out.print( UnicodeFormatter.toString(content) );
          out.write("\";\n");
          out.write("\t}\n");
          out.write("\n");
          out.write("\tfunction ");
          if (_jspx_meth_portlet_005fnamespace_005f60(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("removeArticleLocale() {\n");
          out.write("\t\tif (confirm(\"");
          out.print( UnicodeLanguageUtil.get(pageContext, "are-you-sure-you-want-to-deactivate-this-language") );
          out.write("\")) {\n");
          out.write("\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f61(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1.");
          if (_jspx_meth_portlet_005fnamespace_005f62(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.print( Constants.CMD );
          out.write(".value = \"removeArticlesLocale\";\n");
          out.write("\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f63(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1.");
          if (_jspx_meth_portlet_005fnamespace_005f64(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("redirect.value = \"");
          //  portlet:renderURL
          com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f7 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL.get(com.liferay.taglib.portlet.RenderURLTag.class);
          _jspx_th_portlet_005frenderURL_005f7.setPageContext(_jspx_page_context);
          _jspx_th_portlet_005frenderURL_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
          int _jspx_eval_portlet_005frenderURL_005f7 = _jspx_th_portlet_005frenderURL_005f7.doStartTag();
          if (_jspx_eval_portlet_005frenderURL_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_portlet_005frenderURL_005f7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_portlet_005frenderURL_005f7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_portlet_005frenderURL_005f7.doInitBody();
            }
            do {
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f19 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f19.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f7);
              // /html/portlet/journal/edit_article.jsp(751,95) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f19.setName("redirect");
              // /html/portlet/journal/edit_article.jsp(751,95) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f19.setValue( redirect );
              int _jspx_eval_portlet_005fparam_005f19 = _jspx_th_portlet_005fparam_005f19.doStartTag();
              if (_jspx_th_portlet_005fparam_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f19);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f19);
              if (_jspx_meth_portlet_005fparam_005f20(_jspx_th_portlet_005frenderURL_005f7, _jspx_page_context))
                return;
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f21 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f21.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f7);
              // /html/portlet/journal/edit_article.jsp(751,220) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f21.setName("groupId");
              // /html/portlet/journal/edit_article.jsp(751,220) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f21.setValue( String.valueOf(groupId) );
              int _jspx_eval_portlet_005fparam_005f21 = _jspx_th_portlet_005fparam_005f21.doStartTag();
              if (_jspx_th_portlet_005fparam_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f21);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f21);
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f22 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f22.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f7);
              // /html/portlet/journal/edit_article.jsp(751,291) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f22.setName("articleId");
              // /html/portlet/journal/edit_article.jsp(751,291) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f22.setValue( articleId );
              int _jspx_eval_portlet_005fparam_005f22 = _jspx_th_portlet_005fparam_005f22.doStartTag();
              if (_jspx_th_portlet_005fparam_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f22);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f22);
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f23 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
              _jspx_th_portlet_005fparam_005f23.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005fparam_005f23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f7);
              // /html/portlet/journal/edit_article.jsp(751,350) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f23.setName("version");
              // /html/portlet/journal/edit_article.jsp(751,350) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005fparam_005f23.setValue( String.valueOf(version) );
              int _jspx_eval_portlet_005fparam_005f23 = _jspx_th_portlet_005fparam_005f23.doStartTag();
              if (_jspx_th_portlet_005fparam_005f23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f23);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f23);
              int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f7.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_portlet_005frenderURL_005f7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_portlet_005frenderURL_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fportlet_005frenderURL.reuse(_jspx_th_portlet_005frenderURL_005f7);
            return;
          }
          _005fjspx_005ftagPool_005fportlet_005frenderURL.reuse(_jspx_th_portlet_005frenderURL_005f7);
          out.write('&');
          if (_jspx_meth_portlet_005fnamespace_005f65(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("languageId=");
          out.print( defaultLanguageId );
          out.write("\";\n");
          out.write("\t\t\tsubmitForm(document.");
          if (_jspx_meth_portlet_005fnamespace_005f66(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1);\n");
          out.write("\t\t}\n");
          out.write("\t}\n");
          out.write("\n");
          out.write("\tfunction ");
          if (_jspx_meth_portlet_005fnamespace_005f67(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("selectDocumentLibrary(url) {\n");
          out.write("\t\tdocument.getElementById(");
          if (_jspx_meth_portlet_005fnamespace_005f68(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("documentLibraryInput).value = url;\n");
          out.write("\t}\n");
          out.write("\n");
          out.write("\tfunction ");
          if (_jspx_meth_portlet_005fnamespace_005f69(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("selectImageGallery(url) {\n");
          out.write("\t\tdocument.getElementById(");
          if (_jspx_meth_portlet_005fnamespace_005f70(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("imageGalleryInput).value = url;\n");
          out.write("\t}\n");
          out.write("\n");
          out.write("\tfunction ");
          if (_jspx_meth_portlet_005fnamespace_005f71(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("selectStructure(structureId) {\n");
          out.write("\t\tif (document.");
          if (_jspx_meth_portlet_005fnamespace_005f72(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1.");
          if (_jspx_meth_portlet_005fnamespace_005f73(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("structureId.value != structureId) {\n");
          out.write("\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f74(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1.");
          if (_jspx_meth_portlet_005fnamespace_005f75(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("structureId.value = structureId;\n");
          out.write("\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f76(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1.");
          if (_jspx_meth_portlet_005fnamespace_005f77(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("templateId.value = \"\";\n");
          out.write("\t\t\tsubmitForm(document.");
          if (_jspx_meth_portlet_005fnamespace_005f78(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1);\n");
          out.write("\t\t}\n");
          out.write("\t}\n");
          out.write("\n");
          out.write("\tfunction ");
          if (_jspx_meth_portlet_005fnamespace_005f79(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("selectTemplate(structureId, templateId) {\n");
          out.write("\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f80(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1.");
          if (_jspx_meth_portlet_005fnamespace_005f81(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("structureId.value = structureId;\n");
          out.write("\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f82(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1.");
          if (_jspx_meth_portlet_005fnamespace_005f83(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("templateId.value = templateId;\n");
          out.write("\t\tsubmitForm(document.");
          if (_jspx_meth_portlet_005fnamespace_005f84(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1);\n");
          out.write("\t}\n");
          out.write("\n");
          out.write("\tLiferay.provide(\n");
          out.write("\t\twindow,\n");
          out.write("\t\t'");
          if (_jspx_meth_portlet_005fnamespace_005f85(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("disableInputDate',\n");
          out.write("\t\tfunction(date, checked) {\n");
          out.write("\t\t\tvar A = AUI();\n");
          out.write("\n");
          out.write("\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f86(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1[\"");
          if (_jspx_meth_portlet_005fnamespace_005f87(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("\" + date + \"Hour\"].disabled = checked;\n");
          out.write("\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f88(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1[\"");
          if (_jspx_meth_portlet_005fnamespace_005f89(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("\" + date + \"Minute\"].disabled = checked;\n");
          out.write("\t\t\tdocument.");
          if (_jspx_meth_portlet_005fnamespace_005f90(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1[\"");
          if (_jspx_meth_portlet_005fnamespace_005f91(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("\" + date + \"AmPm\"].disabled = checked;\n");
          out.write("\n");
          out.write("\t\t\tvar calendarWidgetId = document.");
          if (_jspx_meth_portlet_005fnamespace_005f92(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("fm1[\"");
          if (_jspx_meth_portlet_005fnamespace_005f93(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("\" + date + \"Month\"].getAttribute('data-auiComponentID');\n");
          out.write("\n");
          out.write("\t\t\tvar calendarWidget = A.Component.getById(calendarWidgetId);\n");
          out.write("\n");
          out.write("\t\t\tif (calendarWidget) {\n");
          out.write("\t\t\t\tcalendarWidget.set('disabled', checked);\n");
          out.write("\t\t\t}\n");
          out.write("\t\t},\n");
          out.write("\t\t['aui-base']\n");
          out.write("\t);\n");
          out.write("\n");
          out.write("\tLiferay.Util.disableToggleBoxes('");
          if (_jspx_meth_portlet_005fnamespace_005f94(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("autoArticleIdCheckbox','");
          if (_jspx_meth_portlet_005fnamespace_005f95(_jspx_th_aui_005fscript_005f4, _jspx_page_context))
            return;
          out.write("newArticleId', true);\n");
          out.write("\n");
          out.write("\t");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f12 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f12.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
          // /html/portlet/journal/edit_article.jsp(801,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f12.setTest( windowState.equals(WindowState.MAXIMIZED) );
          int _jspx_eval_c_005fif_005f12 = _jspx_th_c_005fif_005f12.doStartTag();
          if (_jspx_eval_c_005fif_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f5 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f5.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f12);
              int _jspx_eval_c_005fchoose_005f5 = _jspx_th_c_005fchoose_005f5.doStartTag();
              if (_jspx_eval_c_005fchoose_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f5 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f5.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f5);
                  // /html/portlet/journal/edit_article.jsp(803,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f5.setTest( PropsValues.JOURNAL_ARTICLE_FORCE_AUTOGENERATE_ID );
                  int _jspx_eval_c_005fwhen_005f5 = _jspx_th_c_005fwhen_005f5.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\tLiferay.Util.focusFormField(document.");
                      if (_jspx_meth_portlet_005fnamespace_005f96(_jspx_th_c_005fwhen_005f5, _jspx_page_context))
                        return;
                      out.write("fm1.");
                      if (_jspx_meth_portlet_005fnamespace_005f97(_jspx_th_c_005fwhen_005f5, _jspx_page_context))
                        return;
                      out.write("title);\n");
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
                  org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f5 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                  _jspx_th_c_005fotherwise_005f5.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fotherwise_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f5);
                  int _jspx_eval_c_005fotherwise_005f5 = _jspx_th_c_005fotherwise_005f5.doStartTag();
                  if (_jspx_eval_c_005fotherwise_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\n");
                      out.write("\t\t\t\tLiferay.Util.focusFormField(document.");
                      if (_jspx_meth_portlet_005fnamespace_005f98(_jspx_th_c_005fotherwise_005f5, _jspx_page_context))
                        return;
                      out.write("fm1.");
                      if (_jspx_meth_portlet_005fnamespace_005f99(_jspx_th_c_005fotherwise_005f5, _jspx_page_context))
                        return;
                      out.print( (article == null) ? "newArticleId" : "title" );
                      out.write(");\n");
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
          int evalDoAfterBody = _jspx_th_aui_005fscript_005f4.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_aui_005fscript_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
        }
      }
      if (_jspx_th_aui_005fscript_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f4);
        return;
      }
      _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f4);
      out.write('\n');
      out.write('\n');
      //  aui:script
      com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f5 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.get(com.liferay.taglib.aui.ScriptTag.class);
      _jspx_th_aui_005fscript_005f5.setPageContext(_jspx_page_context);
      _jspx_th_aui_005fscript_005f5.setParent(null);
      // /html/portlet/journal/edit_article.jsp(813,0) name = use type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_aui_005fscript_005f5.setUse("liferay-portlet-journal");
      int _jspx_eval_aui_005fscript_005f5 = _jspx_th_aui_005fscript_005f5.doStartTag();
      if (_jspx_eval_aui_005fscript_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_aui_005fscript_005f5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_aui_005fscript_005f5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_aui_005fscript_005f5.doInitBody();
        }
        do {
          out.write('\n');
          out.write('\n');
          out.write('	');

	String doAsUserId = themeDisplay.getDoAsUserId();

	if (Validator.isNull(doAsUserId)) {
		doAsUserId = Encryptor.encrypt(company.getKeyObj(), String.valueOf(themeDisplay.getUserId()));
	}
	
          out.write("\n");
          out.write("\n");
          out.write("\tLiferay.Portlet.Journal.PROXY = {};\n");
          out.write("\tLiferay.Portlet.Journal.PROXY.doAsUserId = '");
          out.print( HttpUtil.encodeURL(doAsUserId) );
          out.write("';\n");
          out.write("\tLiferay.Portlet.Journal.PROXY.editorImpl = '");
          out.print( PropsUtil.get(EDITOR_WYSIWYG_IMPL_KEY) );
          out.write("';\n");
          out.write("\tLiferay.Portlet.Journal.PROXY.instanceIdKey = '");
          out.print( instanceIdKey );
          out.write("';\n");
          out.write("\tLiferay.Portlet.Journal.PROXY.pathThemeCss = '");
          out.print( HttpUtil.encodeURL(themeDisplay.getPathThemeCss()) );
          out.write("';\n");
          out.write("\tLiferay.Portlet.Journal.PROXY.portletNamespace = '");
          if (_jspx_meth_portlet_005fnamespace_005f100(_jspx_th_aui_005fscript_005f5, _jspx_page_context))
            return;
          out.write("';\n");
          out.write("\n");
          out.write("\tnew Liferay.Portlet.Journal(Liferay.Portlet.Journal.PROXY.portletNamespace, '");
          out.print( HtmlUtil.escape(articleId) );
          out.write("');\n");
          int evalDoAfterBody = _jspx_th_aui_005fscript_005f5.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_aui_005fscript_005f5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
        }
      }
      if (_jspx_th_aui_005fscript_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f5);
        return;
      }
      _005fjspx_005ftagPool_005faui_005fscript_0026_005fuse.reuse(_jspx_th_aui_005fscript_005f5);
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

  private boolean _jspx_meth_liferay_002dutil_005finclude_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:include
    com.liferay.taglib.util.IncludeTag _jspx_th_liferay_002dutil_005finclude_005f0 = (com.liferay.taglib.util.IncludeTag) _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage.get(com.liferay.taglib.util.IncludeTag.class);
    _jspx_th_liferay_002dutil_005finclude_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005finclude_005f0.setParent(null);
    // /html/portlet/journal/edit_article.jsp(230,0) name = page type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005finclude_005f0.setPage("/html/portlet/journal/article_tabs.jsp");
    int _jspx_eval_liferay_002dutil_005finclude_005f0 = _jspx_th_liferay_002dutil_005finclude_005f0.doStartTag();
    if (_jspx_eval_liferay_002dutil_005finclude_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_liferay_002dutil_005finclude_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_liferay_002dutil_005finclude_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_liferay_002dutil_005finclude_005f0.doInitBody();
      }
      do {
        out.write('\n');
        out.write('	');
        if (_jspx_meth_liferay_002dutil_005fparam_005f0(_jspx_th_liferay_002dutil_005finclude_005f0, _jspx_page_context))
          return true;
        out.write('\n');
        int evalDoAfterBody = _jspx_th_liferay_002dutil_005finclude_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_liferay_002dutil_005finclude_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_liferay_002dutil_005finclude_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage.reuse(_jspx_th_liferay_002dutil_005finclude_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005finclude_0026_005fpage.reuse(_jspx_th_liferay_002dutil_005finclude_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dutil_005fparam_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dutil_005finclude_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-util:param
    com.liferay.taglib.util.ParamTag _jspx_th_liferay_002dutil_005fparam_005f0 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fliferay_002dutil_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_liferay_002dutil_005fparam_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dutil_005fparam_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dutil_005finclude_005f0);
    // /html/portlet/journal/edit_article.jsp(231,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005fparam_005f0.setName("tabs1");
    // /html/portlet/journal/edit_article.jsp(231,1) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dutil_005fparam_005f0.setValue("content");
    int _jspx_eval_liferay_002dutil_005fparam_005f0 = _jspx_th_liferay_002dutil_005fparam_005f0.doStartTag();
    if (_jspx_th_liferay_002dutil_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dutil_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_liferay_002dutil_005fparam_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dutil_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_liferay_002dutil_005fparam_005f0);
    return false;
  }

  private boolean _jspx_meth_aui_005fform_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:form
    com.liferay.taglib.aui.FormTag _jspx_th_aui_005fform_005f0 = (com.liferay.taglib.aui.FormTag) _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005fenctype.get(com.liferay.taglib.aui.FormTag.class);
    _jspx_th_aui_005fform_005f0.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fform_005f0.setParent(null);
    // /html/portlet/journal/edit_article.jsp(234,0) null
    _jspx_th_aui_005fform_005f0.setDynamicAttribute(null, "enctype", new String("multipart/form-data"));
    // /html/portlet/journal/edit_article.jsp(234,0) null
    _jspx_th_aui_005fform_005f0.setDynamicAttribute(null, "method", new String("post"));
    // /html/portlet/journal/edit_article.jsp(234,0) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fform_005f0.setName("fm2");
    int _jspx_eval_aui_005fform_005f0 = _jspx_th_aui_005fform_005f0.doStartTag();
    if (_jspx_eval_aui_005fform_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_005fform_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_005fform_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_005fform_005f0.doInitBody();
      }
      do {
        out.write("\n");
        out.write("\t<input name=\"groupId\" type=\"hidden\" value=\"\" />\n");
        out.write("\t<input name=\"articleId\" type=\"hidden\" value=\"\" />\n");
        out.write("\t<input name=\"version\" type=\"hidden\" value=\"\" />\n");
        out.write("\t<input name=\"title\" type=\"hidden\" value=\"\" />\n");
        out.write("\t<input name=\"xml\" type=\"hidden\" value=\"\" />\n");
        int evalDoAfterBody = _jspx_th_aui_005fform_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_aui_005fform_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_aui_005fform_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005fenctype.reuse(_jspx_th_aui_005fform_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fform_0026_005fname_005fmethod_005fenctype.reuse(_jspx_th_aui_005fform_005f0);
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
    // /html/portlet/journal/edit_article.jsp(243,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setName("struts_action");
    // /html/portlet/journal/edit_article.jsp(243,1) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setValue("/journal/edit_article");
    int _jspx_eval_portlet_005fparam_005f0 = _jspx_th_portlet_005fparam_005f0.doStartTag();
    if (_jspx_th_portlet_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f1 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f1.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
    // /html/portlet/journal/edit_article.jsp(247,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f1.setName("struts_action");
    // /html/portlet/journal/edit_article.jsp(247,1) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f1.setValue("/journal/edit_article");
    int _jspx_eval_portlet_005fparam_005f1 = _jspx_th_portlet_005fparam_005f1.doStartTag();
    if (_jspx_th_portlet_005fparam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f9 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f9.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
    // /html/portlet/journal/edit_article.jsp(260,1) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f9.setName("content");
    // /html/portlet/journal/edit_article.jsp(260,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f9.setType("hidden");
    int _jspx_eval_aui_005finput_005f9 = _jspx_th_aui_005finput_005f9.doStartTag();
    if (_jspx_th_aui_005finput_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f9);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f0 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
    int _jspx_eval_portlet_005fnamespace_005f0 = _jspx_th_portlet_005fnamespace_005f0.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fasset_002dtags_002derror_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:asset-tags-error
    com.liferay.taglib.ui.AssetTagsErrorTag _jspx_th_liferay_002dui_005fasset_002dtags_002derror_005f0 = (com.liferay.taglib.ui.AssetTagsErrorTag) _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002derror_005fnobody.get(com.liferay.taglib.ui.AssetTagsErrorTag.class);
    _jspx_th_liferay_002dui_005fasset_002dtags_002derror_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fasset_002dtags_002derror_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
    int _jspx_eval_liferay_002dui_005fasset_002dtags_002derror_005f0 = _jspx_th_liferay_002dui_005fasset_002dtags_002derror_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fasset_002dtags_002derror_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002derror_005fnobody.reuse(_jspx_th_liferay_002dui_005fasset_002dtags_002derror_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fasset_002dtags_002derror_005fnobody.reuse(_jspx_th_liferay_002dui_005fasset_002dtags_002derror_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f1 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f1.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
    int _jspx_eval_portlet_005fnamespace_005f1 = _jspx_th_portlet_005fnamespace_005f1.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f1);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f16(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f16 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f16.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /html/portlet/journal/edit_article.jsp(291,9) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f16.setName("newArticleId");
    // /html/portlet/journal/edit_article.jsp(291,9) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f16.setType("hidden");
    int _jspx_eval_aui_005finput_005f16 = _jspx_th_aui_005finput_005f16.doStartTag();
    if (_jspx_th_aui_005finput_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f16);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f16);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f19(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f19 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f19.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
    // /html/portlet/journal/edit_article.jsp(297,9) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f19.setLabel("autogenerate-id");
    // /html/portlet/journal/edit_article.jsp(297,9) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f19.setName("autoArticleId");
    // /html/portlet/journal/edit_article.jsp(297,9) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f19.setType("checkbox");
    int _jspx_eval_aui_005finput_005f19 = _jspx_th_aui_005finput_005f19.doStartTag();
    if (_jspx_th_aui_005finput_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fnobody.reuse(_jspx_th_aui_005finput_005f19);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fnobody.reuse(_jspx_th_aui_005finput_005f19);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f2 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f2.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
    int _jspx_eval_portlet_005fnamespace_005f2 = _jspx_th_portlet_005fnamespace_005f2.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f2);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f3 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f3.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f1);
    int _jspx_eval_portlet_005fnamespace_005f3 = _jspx_th_portlet_005fnamespace_005f3.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f3);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f4 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f4.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
    int _jspx_eval_portlet_005fnamespace_005f4 = _jspx_th_portlet_005fnamespace_005f4.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f4);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f5 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f5.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
    int _jspx_eval_portlet_005fnamespace_005f5 = _jspx_th_portlet_005fnamespace_005f5.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f5);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f0 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
    // /html/portlet/journal/edit_article.jsp(424,12) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f0.setKey("this-field-is-required");
    int _jspx_eval_liferay_002dui_005fmessage_005f0 = _jspx_th_liferay_002dui_005fmessage_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button
    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f1 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fcssClass_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
    _jspx_th_aui_005fbutton_005f1.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
    // /html/portlet/journal/edit_article.jsp(430,12) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f1.setCssClass("edit-button");
    // /html/portlet/journal/edit_article.jsp(430,12) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f1.setType("button");
    // /html/portlet/journal/edit_article.jsp(430,12) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f1.setValue("edit-options");
    int _jspx_eval_aui_005fbutton_005f1 = _jspx_th_aui_005fbutton_005f1.doStartTag();
    if (_jspx_th_aui_005fbutton_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f1);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button
    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f2 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fcssClass_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
    _jspx_th_aui_005fbutton_005f2.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
    // /html/portlet/journal/edit_article.jsp(432,12) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f2.setCssClass("repeatable-button aui-helper-hidden");
    // /html/portlet/journal/edit_article.jsp(432,12) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f2.setType("button");
    // /html/portlet/journal/edit_article.jsp(432,12) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f2.setValue("repeat");
    int _jspx_eval_aui_005fbutton_005f2 = _jspx_th_aui_005fbutton_005f2.doStartTag();
    if (_jspx_th_aui_005fbutton_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f2);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f6 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f6.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f2);
    int _jspx_eval_portlet_005fnamespace_005f6 = _jspx_th_portlet_005fnamespace_005f6.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f6);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f7 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f7.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f2);
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

  private boolean _jspx_meth_portlet_005fnamespace_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f14 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f14.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f2);
    int _jspx_eval_portlet_005fnamespace_005f14 = _jspx_th_portlet_005fnamespace_005f14.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f14);
    return false;
  }

  private boolean _jspx_meth_aui_005fscript_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:script
    com.liferay.taglib.aui.ScriptTag _jspx_th_aui_005fscript_005f2 = (com.liferay.taglib.aui.ScriptTag) _005fjspx_005ftagPool_005faui_005fscript.get(com.liferay.taglib.aui.ScriptTag.class);
    _jspx_th_aui_005fscript_005f2.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fscript_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f2);
    int _jspx_eval_aui_005fscript_005f2 = _jspx_th_aui_005fscript_005f2.doStartTag();
    if (_jspx_eval_aui_005fscript_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_005fscript_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_005fscript_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_005fscript_005f2.doInitBody();
      }
      do {
        out.write("\n");
        out.write("\t\t\t\t\t\t\t\t\tdocument.");
        if (_jspx_meth_portlet_005fnamespace_005f15(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
          return true;
        out.write("fm1.");
        if (_jspx_meth_portlet_005fnamespace_005f16(_jspx_th_aui_005fscript_005f2, _jspx_page_context))
          return true;
        out.write("removeArticleLocaleButton.disabled = true;\n");
        out.write("\t\t\t\t\t\t\t\t");
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
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fscript.reuse(_jspx_th_aui_005fscript_005f2);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f15 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f15.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f15 = _jspx_th_portlet_005fnamespace_005f15.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f15);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f16(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f16 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f16.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f2);
    int _jspx_eval_portlet_005fnamespace_005f16 = _jspx_th_portlet_005fnamespace_005f16.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f16);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f16);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f17(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f17 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f17.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f2);
    int _jspx_eval_portlet_005fnamespace_005f17 = _jspx_th_portlet_005fnamespace_005f17.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f17);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f17);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f18(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f18 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f18.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f2);
    int _jspx_eval_portlet_005fnamespace_005f18 = _jspx_th_portlet_005fnamespace_005f18.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f18);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f18);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f19(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f19 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f19.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f2);
    int _jspx_eval_portlet_005fnamespace_005f19 = _jspx_th_portlet_005fnamespace_005f19.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f19);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f19);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005ferror_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f1 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ferror_005f5);
    // /html/portlet/journal/edit_article.jsp(532,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f1.setKey("image-names-must-end-with-one-of-the-following-extensions");
    int _jspx_eval_liferay_002dui_005fmessage_005f1 = _jspx_th_liferay_002dui_005fmessage_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f1);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f25(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f25 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f25.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
    // /html/portlet/journal/edit_article.jsp(538,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f25.setName("description");
    int _jspx_eval_aui_005finput_005f25 = _jspx_th_aui_005finput_005f25.doStartTag();
    if (_jspx_th_aui_005finput_005f25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f25);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f25);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f26(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f26 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005finlineLabel_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f26.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
    // /html/portlet/journal/edit_article.jsp(540,5) name = inlineLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f26.setInlineLabel("left");
    // /html/portlet/journal/edit_article.jsp(540,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f26.setLabel("use-small-image");
    // /html/portlet/journal/edit_article.jsp(540,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f26.setName("smallImage");
    int _jspx_eval_aui_005finput_005f26 = _jspx_th_aui_005finput_005f26.doStartTag();
    if (_jspx_th_aui_005finput_005f26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f26);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f26);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f27(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f27 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f27.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
    // /html/portlet/journal/edit_article.jsp(542,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f27.setLabel("small-image-url");
    // /html/portlet/journal/edit_article.jsp(542,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f27.setName("smallImageURL");
    int _jspx_eval_aui_005finput_005f27 = _jspx_th_aui_005finput_005f27.doStartTag();
    if (_jspx_th_aui_005finput_005f27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005fnobody.reuse(_jspx_th_aui_005finput_005f27);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005fnobody.reuse(_jspx_th_aui_005finput_005f27);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f28(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f28 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f28.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f0);
    // /html/portlet/journal/edit_article.jsp(546,5) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f28.setCssClass("lfr-input-text-container");
    // /html/portlet/journal/edit_article.jsp(546,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f28.setLabel("small-image");
    // /html/portlet/journal/edit_article.jsp(546,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f28.setName("smallFile");
    // /html/portlet/journal/edit_article.jsp(546,5) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f28.setType("file");
    int _jspx_eval_aui_005finput_005f28 = _jspx_th_aui_005finput_005f28.doStartTag();
    if (_jspx_th_aui_005finput_005f28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f28);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f28);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f31(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f31 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005finlineLabel_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f31.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f31.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f1);
    // /html/portlet/journal/edit_article.jsp(602,5) name = inlineLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f31.setInlineLabel("left");
    // /html/portlet/journal/edit_article.jsp(602,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f31.setLabel("searchable");
    // /html/portlet/journal/edit_article.jsp(602,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f31.setName("indexable");
    int _jspx_eval_aui_005finput_005f31 = _jspx_th_aui_005finput_005f31.doStartTag();
    if (_jspx_th_aui_005finput_005f31.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f31);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005fname_005flabel_005finlineLabel_005fnobody.reuse(_jspx_th_aui_005finput_005f31);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f2 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f2.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
    // /html/portlet/journal/edit_article.jsp(620,5) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f2.setKey("a-new-version-will-be-created-automatically-if-this-content-is-modified");
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
    // /html/portlet/journal/edit_article.jsp(626,5) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f3.setKey("there-is-a-publication-workflow-in-process");
    int _jspx_eval_liferay_002dui_005fmessage_005f3 = _jspx_th_liferay_002dui_005fmessage_005f3.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f3);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button
    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f5 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
    _jspx_th_aui_005fbutton_005f5.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
    // /html/portlet/journal/edit_article.jsp(668,5) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f5.setName("previewArticleBtn");
    // /html/portlet/journal/edit_article.jsp(668,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f5.setValue("preview");
    int _jspx_eval_aui_005fbutton_005f5 = _jspx_th_aui_005fbutton_005f5.doStartTag();
    if (_jspx_th_aui_005fbutton_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_aui_005fbutton_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_aui_005fbutton_005f5);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button
    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f6 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
    _jspx_th_aui_005fbutton_005f6.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
    // /html/portlet/journal/edit_article.jsp(672,5) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f6.setName("downloadArticleContentBtn");
    // /html/portlet/journal/edit_article.jsp(672,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f6.setValue("download");
    int _jspx_eval_aui_005fbutton_005f6 = _jspx_th_aui_005fbutton_005f6.doStartTag();
    if (_jspx_th_aui_005fbutton_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_aui_005fbutton_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_aui_005fbutton_005f6);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f20(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffield_002dwrapper_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f20 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f20.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f1);
    int _jspx_eval_portlet_005fnamespace_005f20 = _jspx_th_portlet_005fnamespace_005f20.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f20);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f20);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f21(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f10, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f21 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f21.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f10);
    int _jspx_eval_portlet_005fnamespace_005f21 = _jspx_th_portlet_005fnamespace_005f21.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f21);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f21);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f10, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f4 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f4.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f10);
    // /html/portlet/journal/edit_article_extra.jspf(37,106) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f4.setKey("use-default");
    int _jspx_eval_liferay_002dui_005fmessage_005f4 = _jspx_th_liferay_002dui_005fmessage_005f4.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f4);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f22(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f22 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f22.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    int _jspx_eval_portlet_005fnamespace_005f22 = _jspx_th_portlet_005fnamespace_005f22.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f22);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f22);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button
    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f10 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
    _jspx_th_aui_005fbutton_005f10.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(52,5) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f10.setCssClass("save-structure-button aui-helper-hidden");
    // /html/portlet/journal/edit_article_extra.jspf(52,5) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f10.setName("saveStructureBtn");
    // /html/portlet/journal/edit_article_extra.jspf(52,5) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f10.setType("button");
    // /html/portlet/journal/edit_article_extra.jspf(52,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f10.setValue("save");
    int _jspx_eval_aui_005fbutton_005f10 = _jspx_th_aui_005fbutton_005f10.doStartTag();
    if (_jspx_th_aui_005fbutton_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f10);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button
    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f11 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
    _jspx_th_aui_005fbutton_005f11.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(54,5) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f11.setCssClass("edit-structure-button");
    // /html/portlet/journal/edit_article_extra.jspf(54,5) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f11.setName("editStructureBtn");
    // /html/portlet/journal/edit_article_extra.jspf(54,5) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f11.setType("button");
    // /html/portlet/journal/edit_article_extra.jspf(54,5) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f11.setValue("edit");
    int _jspx_eval_aui_005fbutton_005f11 = _jspx_th_aui_005fbutton_005f11.doStartTag();
    if (_jspx_th_aui_005fbutton_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f11);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f2 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f2.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
    // /html/portlet/journal/edit_article_extra.jspf(58,109) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f2.setName("struts_action");
    // /html/portlet/journal/edit_article_extra.jspf(58,109) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f2.setValue("/journal/select_structure");
    int _jspx_eval_portlet_005fparam_005f2 = _jspx_th_portlet_005fparam_005f2.doStartTag();
    if (_jspx_th_portlet_005fparam_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f23(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f23 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f23.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    int _jspx_eval_portlet_005fnamespace_005f23 = _jspx_th_portlet_005fnamespace_005f23.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f23);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f23);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f6 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f6.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(59,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f6.setKey("choose");
    int _jspx_eval_liferay_002dui_005fmessage_005f6 = _jspx_th_liferay_002dui_005fmessage_005f6.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f6);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f24(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f24 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f24.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    int _jspx_eval_portlet_005fnamespace_005f24 = _jspx_th_portlet_005fnamespace_005f24.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f24);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f24);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f7 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f7.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(67,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f7.setKey("form-controls");
    int _jspx_eval_liferay_002dui_005fmessage_005f7 = _jspx_th_liferay_002dui_005fmessage_005f7.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f7);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f8 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f8.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(71,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f8.setKey("text-field");
    int _jspx_eval_liferay_002dui_005fmessage_005f8 = _jspx_th_liferay_002dui_005fmessage_005f8.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f8);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f9 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f9.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(75,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f9.setKey("text-box");
    int _jspx_eval_liferay_002dui_005fmessage_005f9 = _jspx_th_liferay_002dui_005fmessage_005f9.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f9);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f10 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f10.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(79,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f10.setKey("text-area");
    int _jspx_eval_liferay_002dui_005fmessage_005f10 = _jspx_th_liferay_002dui_005fmessage_005f10.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f10);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f11 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f11.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(83,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f11.setKey("checkbox");
    int _jspx_eval_liferay_002dui_005fmessage_005f11 = _jspx_th_liferay_002dui_005fmessage_005f11.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f11);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f12 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f12.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(87,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f12.setKey("selectbox");
    int _jspx_eval_liferay_002dui_005fmessage_005f12 = _jspx_th_liferay_002dui_005fmessage_005f12.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f12);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f13 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f13.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(91,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f13.setKey("multi-selection-list");
    int _jspx_eval_liferay_002dui_005fmessage_005f13 = _jspx_th_liferay_002dui_005fmessage_005f13.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f13);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f14 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f14.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(99,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f14.setKey("application-fields");
    int _jspx_eval_liferay_002dui_005fmessage_005f14 = _jspx_th_liferay_002dui_005fmessage_005f14.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f14);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f15 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f15.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(103,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f15.setKey("image-gallery");
    int _jspx_eval_liferay_002dui_005fmessage_005f15 = _jspx_th_liferay_002dui_005fmessage_005f15.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f15);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f16(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f16 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f16.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(107,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f16.setKey("image-uploader");
    int _jspx_eval_liferay_002dui_005fmessage_005f16 = _jspx_th_liferay_002dui_005fmessage_005f16.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f16);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f16);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f17(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f17 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f17.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(111,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f17.setKey("document-library");
    int _jspx_eval_liferay_002dui_005fmessage_005f17 = _jspx_th_liferay_002dui_005fmessage_005f17.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f17);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f17);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f18(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f18 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f18.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(119,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f18.setKey("misc");
    int _jspx_eval_liferay_002dui_005fmessage_005f18 = _jspx_th_liferay_002dui_005fmessage_005f18.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f18);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f18);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f19(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f19 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f19.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(123,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f19.setKey("link-to-page");
    int _jspx_eval_liferay_002dui_005fmessage_005f19 = _jspx_th_liferay_002dui_005fmessage_005f19.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f19);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f19);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f20(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay_002dui_005fpanel_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f20 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f20.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005fpanel_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(127,6) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f20.setKey("selection-break");
    int _jspx_eval_liferay_002dui_005fmessage_005f20 = _jspx_th_liferay_002dui_005fmessage_005f20.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f20);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f20);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f4 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f4.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f2);
    // /html/portlet/journal/edit_article_extra.jspf(169,11) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f4.setName("struts_action");
    // /html/portlet/journal/edit_article_extra.jspf(169,11) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f4.setValue("/journal/edit_template");
    int _jspx_eval_portlet_005fparam_005f4 = _jspx_th_portlet_005fparam_005f4.doStartTag();
    if (_jspx_th_portlet_005fparam_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f25(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f25 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f25.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f3);
    int _jspx_eval_portlet_005fnamespace_005f25 = _jspx_th_portlet_005fnamespace_005f25.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f25);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f25);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f8 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f8.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f3);
    // /html/portlet/journal/edit_article_extra.jspf(224,114) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f8.setName("struts_action");
    // /html/portlet/journal/edit_article_extra.jspf(224,114) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f8.setValue("/journal/select_template");
    int _jspx_eval_portlet_005fparam_005f8 = _jspx_th_portlet_005fparam_005f8.doStartTag();
    if (_jspx_th_portlet_005fparam_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f26(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f26 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f26.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f26.setParent(null);
    int _jspx_eval_portlet_005fnamespace_005f26 = _jspx_th_portlet_005fnamespace_005f26.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f26);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f26);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f27(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f27 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f27.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f5);
    int _jspx_eval_portlet_005fnamespace_005f27 = _jspx_th_portlet_005fnamespace_005f27.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f27);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f27);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f28(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f28 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f28.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f5);
    int _jspx_eval_portlet_005fnamespace_005f28 = _jspx_th_portlet_005fnamespace_005f28.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f28);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f28);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f21(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f21 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f21.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f5);
    // /html/portlet/journal/edit_article_structure_extra.jspf(21,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f21.setKey("your-request-processed-successfully");
    int _jspx_eval_liferay_002dui_005fmessage_005f21 = _jspx_th_liferay_002dui_005fmessage_005f21.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f21);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f21);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f44(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f44 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f44.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f44.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f5);
    // /html/portlet/journal/edit_article_structure_extra.jspf(24,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f44.setCssClass("save-structure-name lfr-input-text-container");
    // /html/portlet/journal/edit_article_structure_extra.jspf(24,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f44.setLabel("structure-name");
    // /html/portlet/journal/edit_article_structure_extra.jspf(24,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f44.setName("saveStructureStructureName");
    // /html/portlet/journal/edit_article_structure_extra.jspf(24,3) null
    _jspx_th_aui_005finput_005f44.setDynamicAttribute(null, "size", new String("50"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(24,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f44.setType("text");
    int _jspx_eval_aui_005finput_005f44 = _jspx_th_aui_005finput_005f44.doStartTag();
    if (_jspx_th_aui_005finput_005f44.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f44);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f44);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f45(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f45 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005frows_005fname_005flabel_005fcssClass_005fcols_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f45.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f45.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f5);
    // /html/portlet/journal/edit_article_structure_extra.jspf(26,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f45.setCssClass("save-structure-description");
    // /html/portlet/journal/edit_article_structure_extra.jspf(26,3) null
    _jspx_th_aui_005finput_005f45.setDynamicAttribute(null, "cols", new String("80"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(26,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f45.setLabel("description");
    // /html/portlet/journal/edit_article_structure_extra.jspf(26,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f45.setName("saveStructureStructureDescription");
    // /html/portlet/journal/edit_article_structure_extra.jspf(26,3) null
    _jspx_th_aui_005finput_005f45.setDynamicAttribute(null, "rows", new String("6"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(26,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f45.setType("textarea");
    int _jspx_eval_aui_005finput_005f45 = _jspx_th_aui_005finput_005f45.doStartTag();
    if (_jspx_th_aui_005finput_005f45.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005frows_005fname_005flabel_005fcssClass_005fcols_005fnobody.reuse(_jspx_th_aui_005finput_005f45);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005frows_005fname_005flabel_005fcssClass_005fcols_005fnobody.reuse(_jspx_th_aui_005finput_005f45);
    return false;
  }

  private boolean _jspx_meth_aui_005fa_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:a
    com.liferay.taglib.aui.ATag _jspx_th_aui_005fa_005f1 = (com.liferay.taglib.aui.ATag) _005fjspx_005ftagPool_005faui_005fa_0026_005fid_005fhref.get(com.liferay.taglib.aui.ATag.class);
    _jspx_th_aui_005fa_005f1.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fa_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f5);
    // /html/portlet/journal/edit_article_structure_extra.jspf(28,3) name = href type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fa_005f1.setHref("#");
    // /html/portlet/journal/edit_article_structure_extra.jspf(28,3) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fa_005f1.setId("showStructureIdContainer");
    int _jspx_eval_aui_005fa_005f1 = _jspx_th_aui_005fa_005f1.doStartTag();
    if (_jspx_eval_aui_005fa_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_005fa_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_005fa_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_005fa_005f1.doInitBody();
      }
      do {
        if (_jspx_meth_liferay_002dui_005fmessage_005f22(_jspx_th_aui_005fa_005f1, _jspx_page_context))
          return true;
        out.write(" &raquo;");
        int evalDoAfterBody = _jspx_th_aui_005fa_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_aui_005fa_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_aui_005fa_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fa_0026_005fid_005fhref.reuse(_jspx_th_aui_005fa_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fa_0026_005fid_005fhref.reuse(_jspx_th_aui_005fa_005f1);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f22(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fa_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f22 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f22.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fa_005f1);
    // /html/portlet/journal/edit_article_structure_extra.jspf(28,49) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f22.setKey("change-structure-id");
    int _jspx_eval_liferay_002dui_005fmessage_005f22 = _jspx_th_liferay_002dui_005fmessage_005f22.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f22);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f22);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f29(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f29 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f29.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f29.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f5);
    int _jspx_eval_portlet_005fnamespace_005f29 = _jspx_th_portlet_005fnamespace_005f29.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f29.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f29);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f29);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f46(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffield_002dwrapper_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f46 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClas_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f46.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f46.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffield_002dwrapper_005f4);
    // /html/portlet/journal/edit_article_structure_extra.jspf(32,5) null
    _jspx_th_aui_005finput_005f46.setDynamicAttribute(null, "cssClas", new String("lfr-input-text-container"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(32,5) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f46.setLabel("");
    // /html/portlet/journal/edit_article_structure_extra.jspf(32,5) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f46.setName("saveStructureStructureId");
    // /html/portlet/journal/edit_article_structure_extra.jspf(32,5) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f46.setType("text");
    int _jspx_eval_aui_005finput_005f46 = _jspx_th_aui_005finput_005f46.doStartTag();
    if (_jspx_th_aui_005finput_005f46.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClas_005fnobody.reuse(_jspx_th_aui_005finput_005f46);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fcssClas_005fnobody.reuse(_jspx_th_aui_005finput_005f46);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f30(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f30 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f30.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f30.setParent(null);
    int _jspx_eval_portlet_005fnamespace_005f30 = _jspx_th_portlet_005fnamespace_005f30.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f30.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f30);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f30);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f31(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f31 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f31.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f31.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f2);
    int _jspx_eval_portlet_005fnamespace_005f31 = _jspx_th_portlet_005fnamespace_005f31.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f31.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f31);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f31);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f32(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f32 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f32.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f32.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f2);
    int _jspx_eval_portlet_005fnamespace_005f32 = _jspx_th_portlet_005fnamespace_005f32.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f32.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f32);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f32);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f23(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f23 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f23.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f2);
    // /html/portlet/journal/edit_article_structure_extra.jspf(44,4) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f23.setKey("your-request-processed-successfully");
    int _jspx_eval_liferay_002dui_005fmessage_005f23 = _jspx_th_liferay_002dui_005fmessage_005f23.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f23);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f23);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f24(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f24 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f24.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f2);
    // /html/portlet/journal/edit_article_structure_extra.jspf(47,11) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f24.setKey("options");
    int _jspx_eval_liferay_002dui_005fmessage_005f24 = _jspx_th_liferay_002dui_005fmessage_005f24.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f24);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f24);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f5 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f5.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f3);
    // /html/portlet/journal/edit_article_structure_extra.jspf(50,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f5.setLabel(new String("text-field"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(50,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f5.setValue(new String("text"));
    int _jspx_eval_aui_005foption_005f5 = _jspx_th_aui_005foption_005f5.doStartTag();
    if (_jspx_th_aui_005foption_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f5);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f6 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f6.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f3);
    // /html/portlet/journal/edit_article_structure_extra.jspf(51,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f6.setLabel(new String("text-box"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(51,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f6.setValue(new String("text_box"));
    int _jspx_eval_aui_005foption_005f6 = _jspx_th_aui_005foption_005f6.doStartTag();
    if (_jspx_th_aui_005foption_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f6);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f7 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f7.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f3);
    // /html/portlet/journal/edit_article_structure_extra.jspf(52,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f7.setLabel(new String("text-area"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(52,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f7.setValue(new String("text_area"));
    int _jspx_eval_aui_005foption_005f7 = _jspx_th_aui_005foption_005f7.doStartTag();
    if (_jspx_th_aui_005foption_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f7);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f8 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f8.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f3);
    // /html/portlet/journal/edit_article_structure_extra.jspf(53,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f8.setLabel(new String("checkbox"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(53,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f8.setValue(new String("boolean"));
    int _jspx_eval_aui_005foption_005f8 = _jspx_th_aui_005foption_005f8.doStartTag();
    if (_jspx_th_aui_005foption_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f8);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f9 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f9.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f3);
    // /html/portlet/journal/edit_article_structure_extra.jspf(54,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f9.setLabel(new String("options"));
    int _jspx_eval_aui_005foption_005f9 = _jspx_th_aui_005foption_005f9.doStartTag();
    if (_jspx_th_aui_005foption_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f9);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f10 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f10.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f3);
    // /html/portlet/journal/edit_article_structure_extra.jspf(55,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f10.setLabel(new String("select-box"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(55,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f10.setValue(new String("list"));
    int _jspx_eval_aui_005foption_005f10 = _jspx_th_aui_005foption_005f10.doStartTag();
    if (_jspx_th_aui_005foption_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f10);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f11 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f11.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f3);
    // /html/portlet/journal/edit_article_structure_extra.jspf(56,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f11.setLabel(new String("multi-selection-list"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(56,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f11.setValue(new String("multi-list"));
    int _jspx_eval_aui_005foption_005f11 = _jspx_th_aui_005foption_005f11.doStartTag();
    if (_jspx_th_aui_005foption_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f11);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f12 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f12.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f3);
    // /html/portlet/journal/edit_article_structure_extra.jspf(57,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f12.setLabel(new String("image-uploader"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(57,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f12.setValue(new String("image"));
    int _jspx_eval_aui_005foption_005f12 = _jspx_th_aui_005foption_005f12.doStartTag();
    if (_jspx_th_aui_005foption_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f12);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f13 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f13.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f3);
    // /html/portlet/journal/edit_article_structure_extra.jspf(58,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f13.setLabel(new String("image-gallery"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(58,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f13.setValue(new String("image_gallery"));
    int _jspx_eval_aui_005foption_005f13 = _jspx_th_aui_005foption_005f13.doStartTag();
    if (_jspx_th_aui_005foption_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f13);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f14 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f14.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f3);
    // /html/portlet/journal/edit_article_structure_extra.jspf(59,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f14.setLabel(new String("document-library"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(59,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f14.setValue(new String("document_library"));
    int _jspx_eval_aui_005foption_005f14 = _jspx_th_aui_005foption_005f14.doStartTag();
    if (_jspx_th_aui_005foption_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f14);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f15 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f15.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f3);
    // /html/portlet/journal/edit_article_structure_extra.jspf(60,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f15.setLabel(new String("link-to-page"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(60,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f15.setValue(new String("link_to_layout"));
    int _jspx_eval_aui_005foption_005f15 = _jspx_th_aui_005foption_005f15.doStartTag();
    if (_jspx_th_aui_005foption_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f15);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f16(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f16 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f16.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f3);
    // /html/portlet/journal/edit_article_structure_extra.jspf(61,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f16.setLabel(new String("selection-break"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(61,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f16.setValue(new String("selection_break"));
    int _jspx_eval_aui_005foption_005f16 = _jspx_th_aui_005foption_005f16.doStartTag();
    if (_jspx_th_aui_005foption_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f16);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f16);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f48(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f48 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f48.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f48.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f2);
    // /html/portlet/journal/edit_article_structure_extra.jspf(64,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f48.setName("fieldLabel");
    // /html/portlet/journal/edit_article_structure_extra.jspf(64,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f48.setType("text");
    int _jspx_eval_aui_005finput_005f48 = _jspx_th_aui_005finput_005f48.doStartTag();
    if (_jspx_th_aui_005finput_005f48.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f48);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f48);
    return false;
  }

  private boolean _jspx_meth_aui_005fselect_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:select
    com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f4 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel.get(com.liferay.taglib.aui.SelectTag.class);
    _jspx_th_aui_005fselect_005f4.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fselect_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f2);
    // /html/portlet/journal/edit_article_structure_extra.jspf(66,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fselect_005f4.setLabel("index-type");
    // /html/portlet/journal/edit_article_structure_extra.jspf(66,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fselect_005f4.setName("indexType");
    int _jspx_eval_aui_005fselect_005f4 = _jspx_th_aui_005fselect_005f4.doStartTag();
    if (_jspx_eval_aui_005fselect_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_005fselect_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_005fselect_005f4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_005fselect_005f4.doInitBody();
      }
      do {
        out.write("\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_aui_005foption_005f17(_jspx_th_aui_005fselect_005f4, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_aui_005foption_005f18(_jspx_th_aui_005fselect_005f4, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_aui_005foption_005f19(_jspx_th_aui_005fselect_005f4, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_aui_005fselect_005f4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_aui_005fselect_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_aui_005fselect_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel.reuse(_jspx_th_aui_005fselect_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel.reuse(_jspx_th_aui_005fselect_005f4);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f17(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f17 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f17.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f4);
    // /html/portlet/journal/edit_article_structure_extra.jspf(67,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f17.setLabel(new String("not-searchable"));
    int _jspx_eval_aui_005foption_005f17 = _jspx_th_aui_005foption_005f17.doStartTag();
    if (_jspx_th_aui_005foption_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f17);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f17);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f18(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f18 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f18.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f4);
    // /html/portlet/journal/edit_article_structure_extra.jspf(68,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f18.setLabel(new String("searchable-keyword"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(68,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f18.setValue(new String("keyword"));
    int _jspx_eval_aui_005foption_005f18 = _jspx_th_aui_005foption_005f18.doStartTag();
    if (_jspx_th_aui_005foption_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f18);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f18);
    return false;
  }

  private boolean _jspx_meth_aui_005foption_005f19(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fselect_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:option
    com.liferay.taglib.aui.OptionTag _jspx_th_aui_005foption_005f19 = (com.liferay.taglib.aui.OptionTag) _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.get(com.liferay.taglib.aui.OptionTag.class);
    _jspx_th_aui_005foption_005f19.setPageContext(_jspx_page_context);
    _jspx_th_aui_005foption_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fselect_005f4);
    // /html/portlet/journal/edit_article_structure_extra.jspf(69,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f19.setLabel(new String("searchable-text"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(69,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005foption_005f19.setValue(new String("text"));
    int _jspx_eval_aui_005foption_005f19 = _jspx_th_aui_005foption_005f19.doStartTag();
    if (_jspx_th_aui_005foption_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f19);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005foption_0026_005fvalue_005flabel_005fnobody.reuse(_jspx_th_aui_005foption_005f19);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f49(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f49 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f49.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f49.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f2);
    // /html/portlet/journal/edit_article_structure_extra.jspf(72,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f49.setName("predefinedValue");
    // /html/portlet/journal/edit_article_structure_extra.jspf(72,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f49.setType("text");
    int _jspx_eval_aui_005finput_005f49 = _jspx_th_aui_005finput_005f49.doStartTag();
    if (_jspx_th_aui_005finput_005f49.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f49);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fnobody.reuse(_jspx_th_aui_005finput_005f49);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f50(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f50 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f50.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f50.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f2);
    // /html/portlet/journal/edit_article_structure_extra.jspf(75,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f50.setLabel("instructions-for-the-user");
    // /html/portlet/journal/edit_article_structure_extra.jspf(75,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f50.setName("instructions");
    // /html/portlet/journal/edit_article_structure_extra.jspf(75,4) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f50.setType("textarea");
    int _jspx_eval_aui_005finput_005f50 = _jspx_th_aui_005finput_005f50.doStartTag();
    if (_jspx_th_aui_005finput_005f50.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fnobody.reuse(_jspx_th_aui_005finput_005f50);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005flabel_005fnobody.reuse(_jspx_th_aui_005finput_005f50);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_002drow_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fform_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button-row
    com.liferay.taglib.aui.ButtonRowTag _jspx_th_aui_005fbutton_002drow_005f1 = (com.liferay.taglib.aui.ButtonRowTag) _005fjspx_005ftagPool_005faui_005fbutton_002drow.get(com.liferay.taglib.aui.ButtonRowTag.class);
    _jspx_th_aui_005fbutton_002drow_005f1.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_002drow_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fform_005f2);
    int _jspx_eval_aui_005fbutton_002drow_005f1 = _jspx_th_aui_005fbutton_002drow_005f1.doStartTag();
    if (_jspx_eval_aui_005fbutton_002drow_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_005fbutton_002drow_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_005fbutton_002drow_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_005fbutton_002drow_005f1.doInitBody();
      }
      do {
        out.write("\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_aui_005fbutton_005f13(_jspx_th_aui_005fbutton_002drow_005f1, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_aui_005fbutton_005f14(_jspx_th_aui_005fbutton_002drow_005f1, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_aui_005fbutton_005f15(_jspx_th_aui_005fbutton_002drow_005f1, _jspx_page_context))
          return true;
        out.write("\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_aui_005fbutton_002drow_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_aui_005fbutton_002drow_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_aui_005fbutton_002drow_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_002drow.reuse(_jspx_th_aui_005fbutton_002drow_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_002drow.reuse(_jspx_th_aui_005fbutton_002drow_005f1);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fbutton_002drow_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button
    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f13 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
    _jspx_th_aui_005fbutton_005f13.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f1);
    // /html/portlet/journal/edit_article_structure_extra.jspf(85,4) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f13.setCssClass("save-button");
    // /html/portlet/journal/edit_article_structure_extra.jspf(85,4) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f13.setName("save");
    // /html/portlet/journal/edit_article_structure_extra.jspf(85,4) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f13.setType("button");
    // /html/portlet/journal/edit_article_structure_extra.jspf(85,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f13.setValue("save");
    int _jspx_eval_aui_005fbutton_005f13 = _jspx_th_aui_005fbutton_005f13.doStartTag();
    if (_jspx_th_aui_005fbutton_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f13);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fbutton_002drow_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button
    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f14 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
    _jspx_th_aui_005fbutton_005f14.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f1);
    // /html/portlet/journal/edit_article_structure_extra.jspf(87,4) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f14.setCssClass("cancel-button");
    // /html/portlet/journal/edit_article_structure_extra.jspf(87,4) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f14.setName("cancel");
    // /html/portlet/journal/edit_article_structure_extra.jspf(87,4) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f14.setType("button");
    // /html/portlet/journal/edit_article_structure_extra.jspf(87,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f14.setValue("cancel");
    int _jspx_eval_aui_005fbutton_005f14 = _jspx_th_aui_005fbutton_005f14.doStartTag();
    if (_jspx_th_aui_005fbutton_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f14);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fbutton_002drow_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button
    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f15 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
    _jspx_th_aui_005fbutton_005f15.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fbutton_002drow_005f1);
    // /html/portlet/journal/edit_article_structure_extra.jspf(89,4) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f15.setCssClass("close-button");
    // /html/portlet/journal/edit_article_structure_extra.jspf(89,4) name = name type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f15.setName("close");
    // /html/portlet/journal/edit_article_structure_extra.jspf(89,4) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f15.setType("button");
    // /html/portlet/journal/edit_article_structure_extra.jspf(89,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f15.setValue("close");
    int _jspx_eval_aui_005fbutton_005f15 = _jspx_th_aui_005fbutton_005f15.doStartTag();
    if (_jspx_th_aui_005fbutton_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f15);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f54(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f54 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f54.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f54.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(98,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f54.setCssClass("lfr-input-text-container");
    // /html/portlet/journal/edit_article_structure_extra.jspf(98,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f54.setLabel("");
    // /html/portlet/journal/edit_article_structure_extra.jspf(98,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f54.setName("text");
    // /html/portlet/journal/edit_article_structure_extra.jspf(98,3) null
    _jspx_th_aui_005finput_005f54.setDynamicAttribute(null, "size", new String("55"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(98,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f54.setType("text");
    int _jspx_eval_aui_005finput_005f54 = _jspx_th_aui_005finput_005f54.doStartTag();
    if (_jspx_th_aui_005finput_005f54.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f54);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f54);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f55(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f55 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f55.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f55.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(102,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f55.setCssClass("lfr-input-text-container");
    // /html/portlet/journal/edit_article_structure_extra.jspf(102,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f55.setLabel("");
    // /html/portlet/journal/edit_article_structure_extra.jspf(102,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f55.setName("textArea");
    // /html/portlet/journal/edit_article_structure_extra.jspf(102,3) null
    _jspx_th_aui_005finput_005f55.setDynamicAttribute(null, "size", new String("55"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(102,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f55.setType("text");
    int _jspx_eval_aui_005finput_005f55 = _jspx_th_aui_005finput_005f55.doStartTag();
    if (_jspx_th_aui_005finput_005f55.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f55);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f55);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f56(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f56 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005frows_005fname_005flabel_005fcssClass_005fcols_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f56.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f56.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(106,3) null
    _jspx_th_aui_005finput_005f56.setDynamicAttribute(null, "cols", new String("60"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(106,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f56.setCssClass("lfr-textarea-container");
    // /html/portlet/journal/edit_article_structure_extra.jspf(106,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f56.setLabel("");
    // /html/portlet/journal/edit_article_structure_extra.jspf(106,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f56.setName("textBox");
    // /html/portlet/journal/edit_article_structure_extra.jspf(106,3) null
    _jspx_th_aui_005finput_005f56.setDynamicAttribute(null, "rows", new String("10"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(106,3) null
    _jspx_th_aui_005finput_005f56.setDynamicAttribute(null, "size", new String("55"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(106,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f56.setType("textarea");
    int _jspx_eval_aui_005finput_005f56 = _jspx_th_aui_005finput_005f56.doStartTag();
    if (_jspx_th_aui_005finput_005f56.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005frows_005fname_005flabel_005fcssClass_005fcols_005fnobody.reuse(_jspx_th_aui_005finput_005f56);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005frows_005fname_005flabel_005fcssClass_005fcols_005fnobody.reuse(_jspx_th_aui_005finput_005f56);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f57(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f57 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f57.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f57.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(110,3) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f57.setCssClass("journal-image-field lfr-input-text-container flexible");
    // /html/portlet/journal/edit_article_structure_extra.jspf(110,3) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f57.setLabel("");
    // /html/portlet/journal/edit_article_structure_extra.jspf(110,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f57.setName("text");
    // /html/portlet/journal/edit_article_structure_extra.jspf(110,3) null
    _jspx_th_aui_005finput_005f57.setDynamicAttribute(null, "size", new String("40"));
    // /html/portlet/journal/edit_article_structure_extra.jspf(110,3) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f57.setType("file");
    int _jspx_eval_aui_005finput_005f57 = _jspx_th_aui_005finput_005f57.doStartTag();
    if (_jspx_th_aui_005finput_005f57.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f57);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fsize_005fname_005flabel_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f57);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f10 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f10.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f4);
    // /html/portlet/journal/edit_article_structure_extra.jspf(117,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f10.setName("struts_action");
    // /html/portlet/journal/edit_article_structure_extra.jspf(117,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f10.setValue("/journal/select_image_gallery");
    int _jspx_eval_portlet_005fparam_005f10 = _jspx_th_portlet_005fparam_005f10.doStartTag();
    if (_jspx_th_portlet_005fparam_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f10);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f12 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f12.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f5);
    // /html/portlet/journal/edit_article_structure_extra.jspf(134,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f12.setName("struts_action");
    // /html/portlet/journal/edit_article_structure_extra.jspf(134,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f12.setValue("/journal/select_document_library");
    int _jspx_eval_portlet_005fparam_005f12 = _jspx_th_portlet_005fparam_005f12.doStartTag();
    if (_jspx_th_portlet_005fparam_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f12);
    return false;
  }

  private boolean _jspx_meth_aui_005finput_005f60(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:input
    com.liferay.taglib.aui.InputTag _jspx_th_aui_005finput_005f60 = (com.liferay.taglib.aui.InputTag) _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fcssClass_005fnobody.get(com.liferay.taglib.aui.InputTag.class);
    _jspx_th_aui_005finput_005f60.setPageContext(_jspx_page_context);
    _jspx_th_aui_005finput_005f60.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(149,4) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f60.setCssClass("journal-article-field-label");
    // /html/portlet/journal/edit_article_structure_extra.jspf(149,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f60.setName("booleanField");
    // /html/portlet/journal/edit_article_structure_extra.jspf(149,4) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005finput_005f60.setType("checkbox");
    int _jspx_eval_aui_005finput_005f60 = _jspx_th_aui_005finput_005f60.doStartTag();
    if (_jspx_th_aui_005finput_005f60.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f60);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005finput_0026_005ftype_005fname_005fcssClass_005fnobody.reuse(_jspx_th_aui_005finput_005f60);
    return false;
  }

  private boolean _jspx_meth_aui_005fselect_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:select
    com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f5 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005finlineField_005fnobody.get(com.liferay.taglib.aui.SelectTag.class);
    _jspx_th_aui_005fselect_005f5.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fselect_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(159,4) name = inlineField type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fselect_005f5.setInlineField(false);
    // /html/portlet/journal/edit_article_structure_extra.jspf(159,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fselect_005f5.setLabel("");
    // /html/portlet/journal/edit_article_structure_extra.jspf(159,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fselect_005f5.setName("list");
    int _jspx_eval_aui_005fselect_005f5 = _jspx_th_aui_005fselect_005f5.doStartTag();
    if (_jspx_th_aui_005fselect_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005finlineField_005fnobody.reuse(_jspx_th_aui_005fselect_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005flabel_005finlineField_005fnobody.reuse(_jspx_th_aui_005fselect_005f5);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005ficon_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:icon
    com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f0 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
    _jspx_th_liferay_002dui_005ficon_005f0.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005ficon_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(162,5) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005ficon_005f0.setImage("delete");
    int _jspx_eval_liferay_002dui_005ficon_005f0 = _jspx_th_liferay_002dui_005ficon_005f0.doStartTag();
    if (_jspx_th_liferay_002dui_005ficon_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f25(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f25 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f25.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(162,39) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f25.setKey("delete-selected-value");
    int _jspx_eval_liferay_002dui_005fmessage_005f25 = _jspx_th_liferay_002dui_005fmessage_005f25.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f25);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f25);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f26(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f26 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f26.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(168,44) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f26.setKey("new-item");
    int _jspx_eval_liferay_002dui_005fmessage_005f26 = _jspx_th_liferay_002dui_005fmessage_005f26.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f26);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f26);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f27(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f27 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f27.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(168,112) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f27.setKey("new-item");
    int _jspx_eval_liferay_002dui_005fmessage_005f27 = _jspx_th_liferay_002dui_005fmessage_005f27.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f27);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f27);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f28(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f28 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f28.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(170,56) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f28.setKey("item-value");
    int _jspx_eval_liferay_002dui_005fmessage_005f28 = _jspx_th_liferay_002dui_005fmessage_005f28.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f28);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f28);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005ficon_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:icon
    com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f1 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
    _jspx_th_liferay_002dui_005ficon_005f1.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005ficon_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(173,6) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005ficon_005f1.setImage("add");
    int _jspx_eval_liferay_002dui_005ficon_005f1 = _jspx_th_liferay_002dui_005ficon_005f1.doStartTag();
    if (_jspx_th_liferay_002dui_005ficon_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f29(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f29 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f29.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f29.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(173,38) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f29.setKey("add-to-list");
    int _jspx_eval_liferay_002dui_005fmessage_005f29 = _jspx_th_liferay_002dui_005fmessage_005f29.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f29.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f29);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f29);
    return false;
  }

  private boolean _jspx_meth_aui_005fselect_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:select
    com.liferay.taglib.aui.SelectTag _jspx_th_aui_005fselect_005f6 = (com.liferay.taglib.aui.SelectTag) _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005fmultiple_005flabel_005fnobody.get(com.liferay.taglib.aui.SelectTag.class);
    _jspx_th_aui_005fselect_005f6.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fselect_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(181,4) name = label type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fselect_005f6.setLabel("");
    // /html/portlet/journal/edit_article_structure_extra.jspf(181,4) name = multiple type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fselect_005f6.setMultiple(true);
    // /html/portlet/journal/edit_article_structure_extra.jspf(181,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fselect_005f6.setName("multiList");
    int _jspx_eval_aui_005fselect_005f6 = _jspx_th_aui_005fselect_005f6.doStartTag();
    if (_jspx_th_aui_005fselect_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005fmultiple_005flabel_005fnobody.reuse(_jspx_th_aui_005fselect_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fselect_0026_005fname_005fmultiple_005flabel_005fnobody.reuse(_jspx_th_aui_005fselect_005f6);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005ficon_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:icon
    com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f2 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
    _jspx_th_liferay_002dui_005ficon_005f2.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005ficon_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(184,5) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005ficon_005f2.setImage("delete");
    int _jspx_eval_liferay_002dui_005ficon_005f2 = _jspx_th_liferay_002dui_005ficon_005f2.doStartTag();
    if (_jspx_th_liferay_002dui_005ficon_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f2);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f30(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f30 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f30.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f30.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(184,39) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f30.setKey("delete-selected-value");
    int _jspx_eval_liferay_002dui_005fmessage_005f30 = _jspx_th_liferay_002dui_005fmessage_005f30.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f30.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f30);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f30);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f31(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f31 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f31.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f31.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(190,54) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f31.setKey("new-item");
    int _jspx_eval_liferay_002dui_005fmessage_005f31 = _jspx_th_liferay_002dui_005fmessage_005f31.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f31.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f31);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f31);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f32(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f32 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f32.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f32.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(190,112) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f32.setKey("new-item");
    int _jspx_eval_liferay_002dui_005fmessage_005f32 = _jspx_th_liferay_002dui_005fmessage_005f32.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f32.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f32);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f32);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f33(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f33 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f33.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f33.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(192,56) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f33.setKey("item-value");
    int _jspx_eval_liferay_002dui_005fmessage_005f33 = _jspx_th_liferay_002dui_005fmessage_005f33.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f33.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f33);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f33);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005ficon_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:icon
    com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f3 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
    _jspx_th_liferay_002dui_005ficon_005f3.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005ficon_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(195,6) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005ficon_005f3.setImage("add");
    int _jspx_eval_liferay_002dui_005ficon_005f3 = _jspx_th_liferay_002dui_005ficon_005f3.doStartTag();
    if (_jspx_th_liferay_002dui_005ficon_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f3);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005fmessage_005f34(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay_002dui_005fmessage_005f34 = (com.liferay.taglib.ui.MessageTag) _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.get(com.liferay.taglib.ui.MessageTag.class);
    _jspx_th_liferay_002dui_005fmessage_005f34.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005fmessage_005f34.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(195,38) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005fmessage_005f34.setKey("add-to-list");
    int _jspx_eval_liferay_002dui_005fmessage_005f34 = _jspx_th_liferay_002dui_005fmessage_005f34.doStartTag();
    if (_jspx_th_liferay_002dui_005fmessage_005f34.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f34);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_liferay_002dui_005fmessage_005f34);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005ficon_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:icon
    com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f4 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fcssClass_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
    _jspx_th_liferay_002dui_005ficon_005f4.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005ficon_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(255,4) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005ficon_005f4.setCssClass("repeatable-field-add");
    // /html/portlet/journal/edit_article_structure_extra.jspf(255,4) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005ficon_005f4.setImage("add");
    int _jspx_eval_liferay_002dui_005ficon_005f4 = _jspx_th_liferay_002dui_005ficon_005f4.doStartTag();
    if (_jspx_th_liferay_002dui_005ficon_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f4);
    return false;
  }

  private boolean _jspx_meth_liferay_002dui_005ficon_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005ffieldset_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:icon
    com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f5 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fcssClass_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
    _jspx_th_liferay_002dui_005ficon_005f5.setPageContext(_jspx_page_context);
    _jspx_th_liferay_002dui_005ficon_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005ffieldset_005f6);
    // /html/portlet/journal/edit_article_structure_extra.jspf(257,4) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005ficon_005f5.setCssClass("repeatable-field-delete");
    // /html/portlet/journal/edit_article_structure_extra.jspf(257,4) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_liferay_002dui_005ficon_005f5.setImage("delete");
    int _jspx_eval_liferay_002dui_005ficon_005f5 = _jspx_th_liferay_002dui_005ficon_005f5.doStartTag();
    if (_jspx_th_liferay_002dui_005ficon_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005fimage_005fcssClass_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f5);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f33(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f33 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f33.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f33.setParent(null);
    int _jspx_eval_portlet_005fnamespace_005f33 = _jspx_th_portlet_005fnamespace_005f33.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f33.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f33);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f33);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_005f18(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button
    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f18 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fcssClass_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
    _jspx_th_aui_005fbutton_005f18.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_005f18.setParent(null);
    // /html/portlet/journal/edit_article_structure_extra.jspf(264,1) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f18.setCssClass("edit-button");
    // /html/portlet/journal/edit_article_structure_extra.jspf(264,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f18.setType("button");
    // /html/portlet/journal/edit_article_structure_extra.jspf(264,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f18.setValue("edit-options");
    int _jspx_eval_aui_005fbutton_005f18 = _jspx_th_aui_005fbutton_005f18.doStartTag();
    if (_jspx_th_aui_005fbutton_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f18);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f18);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f34(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f34 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f34.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f34.setParent(null);
    int _jspx_eval_portlet_005fnamespace_005f34 = _jspx_th_portlet_005fnamespace_005f34.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f34.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f34);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f34);
    return false;
  }

  private boolean _jspx_meth_aui_005fbutton_005f19(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:button
    com.liferay.taglib.aui.ButtonTag _jspx_th_aui_005fbutton_005f19 = (com.liferay.taglib.aui.ButtonTag) _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fcssClass_005fnobody.get(com.liferay.taglib.aui.ButtonTag.class);
    _jspx_th_aui_005fbutton_005f19.setPageContext(_jspx_page_context);
    _jspx_th_aui_005fbutton_005f19.setParent(null);
    // /html/portlet/journal/edit_article_structure_extra.jspf(268,1) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f19.setCssClass("repeatable-button aui-helper-hidden");
    // /html/portlet/journal/edit_article_structure_extra.jspf(268,1) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f19.setType("button");
    // /html/portlet/journal/edit_article_structure_extra.jspf(268,1) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_aui_005fbutton_005f19.setValue("repeat");
    int _jspx_eval_aui_005fbutton_005f19 = _jspx_th_aui_005fbutton_005f19.doStartTag();
    if (_jspx_th_aui_005fbutton_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f19);
      return true;
    }
    _005fjspx_005ftagPool_005faui_005fbutton_0026_005fvalue_005ftype_005fcssClass_005fnobody.reuse(_jspx_th_aui_005fbutton_005f19);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f35(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f35 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f35.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f35.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f35 = _jspx_th_portlet_005fnamespace_005f35.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f35.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f35);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f35);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f36(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f36 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f36.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f36.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f36 = _jspx_th_portlet_005fnamespace_005f36.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f36.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f36);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f36);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f37(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f37 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f37.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f37.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f37 = _jspx_th_portlet_005fnamespace_005f37.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f37.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f37);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f37);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f38(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f38 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f38.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f38.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f38 = _jspx_th_portlet_005fnamespace_005f38.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f38.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f38);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f38);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f39(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f39 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f39.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f39.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f39 = _jspx_th_portlet_005fnamespace_005f39.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f39.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f39);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f39);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f40(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f40 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f40.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f40.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f40 = _jspx_th_portlet_005fnamespace_005f40.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f40.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f40);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f40);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f41(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f41 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f41.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f41.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f41 = _jspx_th_portlet_005fnamespace_005f41.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f41.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f41);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f41);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f42(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f42 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f42.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f42.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f42 = _jspx_th_portlet_005fnamespace_005f42.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f42.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f42);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f42);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f43(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f43 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f43.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f43.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f43 = _jspx_th_portlet_005fnamespace_005f43.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f43.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f43);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f43);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f44(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f44 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f44.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f44.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f44 = _jspx_th_portlet_005fnamespace_005f44.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f44.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f44);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f44);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f45(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f45 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f45.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f45.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f45 = _jspx_th_portlet_005fnamespace_005f45.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f45.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f45);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f45);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f46(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f46 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f46.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f46.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f46 = _jspx_th_portlet_005fnamespace_005f46.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f46.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f46);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f46);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f47(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f47 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f47.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f47.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f47 = _jspx_th_portlet_005fnamespace_005f47.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f47.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f47);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f47);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f48(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f48 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f48.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f48.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f48 = _jspx_th_portlet_005fnamespace_005f48.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f48.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f48);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f48);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f49(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f49 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f49.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f49.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f49 = _jspx_th_portlet_005fnamespace_005f49.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f49.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f49);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f49);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f50(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f50 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f50.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f50.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f50 = _jspx_th_portlet_005fnamespace_005f50.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f50.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f50);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f50);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f51(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f51 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f51.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f51.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f51 = _jspx_th_portlet_005fnamespace_005f51.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f51.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f51);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f51);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f52(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f52 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f52.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f52.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f52 = _jspx_th_portlet_005fnamespace_005f52.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f52.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f52);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f52);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f53(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f53 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f53.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f53.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f53 = _jspx_th_portlet_005fnamespace_005f53.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f53.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f53);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f53);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f54(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f54 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f54.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f54.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f54 = _jspx_th_portlet_005fnamespace_005f54.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f54.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f54);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f54);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f55(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f55 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f55.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f55.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f55 = _jspx_th_portlet_005fnamespace_005f55.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f55.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f55);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f55);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f56(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f56 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f56.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f56.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f56 = _jspx_th_portlet_005fnamespace_005f56.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f56.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f56);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f56);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f57(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f57 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f57.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f57.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f57 = _jspx_th_portlet_005fnamespace_005f57.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f57.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f57);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f57);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f14 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f14.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f6);
    // /html/portlet/journal/edit_article.jsp(741,83) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f14.setName("struts_action");
    // /html/portlet/journal/edit_article.jsp(741,83) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f14.setValue("/journal/edit_article");
    int _jspx_eval_portlet_005fparam_005f14 = _jspx_th_portlet_005fparam_005f14.doStartTag();
    if (_jspx_th_portlet_005fparam_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f14);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f58(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f58 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f58.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f58.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f58 = _jspx_th_portlet_005fnamespace_005f58.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f58.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f58);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f58);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f59(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f59 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f59.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f59.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f59 = _jspx_th_portlet_005fnamespace_005f59.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f59.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f59);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f59);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f60(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f60 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f60.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f60.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f60 = _jspx_th_portlet_005fnamespace_005f60.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f60.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f60);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f60);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f61(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f61 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f61.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f61.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f61 = _jspx_th_portlet_005fnamespace_005f61.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f61.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f61);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f61);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f62(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f62 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f62.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f62.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f62 = _jspx_th_portlet_005fnamespace_005f62.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f62.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f62);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f62);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f63(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f63 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f63.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f63.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f63 = _jspx_th_portlet_005fnamespace_005f63.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f63.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f63);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f63);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f64(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f64 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f64.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f64.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f64 = _jspx_th_portlet_005fnamespace_005f64.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f64.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f64);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f64);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f20(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f20 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f20.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f7);
    // /html/portlet/journal/edit_article.jsp(751,152) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f20.setName("struts_action");
    // /html/portlet/journal/edit_article.jsp(751,152) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f20.setValue("/journal/edit_article");
    int _jspx_eval_portlet_005fparam_005f20 = _jspx_th_portlet_005fparam_005f20.doStartTag();
    if (_jspx_th_portlet_005fparam_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f20);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f20);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f65(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f65 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f65.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f65.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f65 = _jspx_th_portlet_005fnamespace_005f65.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f65.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f65);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f65);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f66(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f66 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f66.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f66.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f66 = _jspx_th_portlet_005fnamespace_005f66.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f66.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f66);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f66);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f67(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f67 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f67.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f67.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f67 = _jspx_th_portlet_005fnamespace_005f67.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f67.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f67);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f67);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f68(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f68 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f68.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f68.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f68 = _jspx_th_portlet_005fnamespace_005f68.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f68.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f68);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f68);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f69(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f69 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f69.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f69.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f69 = _jspx_th_portlet_005fnamespace_005f69.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f69.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f69);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f69);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f70(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f70 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f70.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f70.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f70 = _jspx_th_portlet_005fnamespace_005f70.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f70.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f70);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f70);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f71(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f71 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f71.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f71.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f71 = _jspx_th_portlet_005fnamespace_005f71.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f71.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f71);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f71);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f72(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f72 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f72.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f72.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f72 = _jspx_th_portlet_005fnamespace_005f72.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f72.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f72);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f72);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f73(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f73 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f73.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f73.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f73 = _jspx_th_portlet_005fnamespace_005f73.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f73.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f73);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f73);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f74(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f74 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f74.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f74.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f74 = _jspx_th_portlet_005fnamespace_005f74.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f74.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f74);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f74);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f75(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f75 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f75.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f75.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f75 = _jspx_th_portlet_005fnamespace_005f75.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f75.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f75);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f75);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f76(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f76 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f76.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f76.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f76 = _jspx_th_portlet_005fnamespace_005f76.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f76.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f76);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f76);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f77(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f77 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f77.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f77.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f77 = _jspx_th_portlet_005fnamespace_005f77.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f77.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f77);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f77);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f78(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f78 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f78.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f78.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f78 = _jspx_th_portlet_005fnamespace_005f78.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f78.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f78);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f78);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f79(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f79 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f79.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f79.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f79 = _jspx_th_portlet_005fnamespace_005f79.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f79.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f79);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f79);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f80(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f80 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f80.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f80.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f80 = _jspx_th_portlet_005fnamespace_005f80.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f80.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f80);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f80);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f81(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f81 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f81.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f81.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f81 = _jspx_th_portlet_005fnamespace_005f81.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f81.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f81);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f81);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f82(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f82 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f82.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f82.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f82 = _jspx_th_portlet_005fnamespace_005f82.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f82.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f82);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f82);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f83(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f83 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f83.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f83.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f83 = _jspx_th_portlet_005fnamespace_005f83.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f83.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f83);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f83);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f84(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f84 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f84.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f84.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f84 = _jspx_th_portlet_005fnamespace_005f84.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f84.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f84);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f84);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f85(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f85 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f85.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f85.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f85 = _jspx_th_portlet_005fnamespace_005f85.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f85.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f85);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f85);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f86(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f86 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f86.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f86.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f86 = _jspx_th_portlet_005fnamespace_005f86.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f86.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f86);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f86);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f87(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f87 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f87.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f87.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f87 = _jspx_th_portlet_005fnamespace_005f87.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f87.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f87);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f87);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f88(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f88 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f88.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f88.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f88 = _jspx_th_portlet_005fnamespace_005f88.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f88.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f88);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f88);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f89(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f89 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f89.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f89.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f89 = _jspx_th_portlet_005fnamespace_005f89.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f89.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f89);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f89);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f90(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f90 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f90.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f90.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f90 = _jspx_th_portlet_005fnamespace_005f90.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f90.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f90);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f90);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f91(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f91 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f91.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f91.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f91 = _jspx_th_portlet_005fnamespace_005f91.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f91.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f91);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f91);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f92(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f92 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f92.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f92.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f92 = _jspx_th_portlet_005fnamespace_005f92.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f92.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f92);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f92);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f93(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f93 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f93.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f93.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f93 = _jspx_th_portlet_005fnamespace_005f93.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f93.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f93);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f93);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f94(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f94 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f94.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f94.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f94 = _jspx_th_portlet_005fnamespace_005f94.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f94.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f94);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f94);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f95(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f95 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f95.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f95.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f4);
    int _jspx_eval_portlet_005fnamespace_005f95 = _jspx_th_portlet_005fnamespace_005f95.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f95.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f95);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f95);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f96(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f96 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f96.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f96.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
    int _jspx_eval_portlet_005fnamespace_005f96 = _jspx_th_portlet_005fnamespace_005f96.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f96.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f96);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f96);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f97(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f97 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f97.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f97.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f5);
    int _jspx_eval_portlet_005fnamespace_005f97 = _jspx_th_portlet_005fnamespace_005f97.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f97.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f97);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f97);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f98(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f98 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f98.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f98.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f5);
    int _jspx_eval_portlet_005fnamespace_005f98 = _jspx_th_portlet_005fnamespace_005f98.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f98.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f98);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f98);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f99(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f99 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f99.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f99.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f5);
    int _jspx_eval_portlet_005fnamespace_005f99 = _jspx_th_portlet_005fnamespace_005f99.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f99.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f99);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f99);
    return false;
  }

  private boolean _jspx_meth_portlet_005fnamespace_005f100(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_005fscript_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_005fnamespace_005f100 = (com.liferay.taglib.portlet.NamespaceTag) _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.get(com.liferay.taglib.portlet.NamespaceTag.class);
    _jspx_th_portlet_005fnamespace_005f100.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fnamespace_005f100.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_005fscript_005f5);
    int _jspx_eval_portlet_005fnamespace_005f100 = _jspx_th_portlet_005fnamespace_005f100.doStartTag();
    if (_jspx_th_portlet_005fnamespace_005f100.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f100);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fnamespace_005fnobody.reuse(_jspx_th_portlet_005fnamespace_005f100);
    return false;
  }
}
