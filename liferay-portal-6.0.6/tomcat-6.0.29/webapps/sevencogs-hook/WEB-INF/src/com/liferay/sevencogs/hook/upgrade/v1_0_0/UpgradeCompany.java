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

package com.liferay.sevencogs.hook.upgrade.v1_0_0;

import com.liferay.documentlibrary.DuplicateFileException;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.OrganizationConstants;
import com.liferay.portal.model.PortletConstants;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.PermissionLocalServiceUtil;
import com.liferay.portal.service.ResourceLocalServiceUtil;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.WorkflowDefinitionLinkLocalServiceUtil;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;
import com.liferay.portlet.imagegallery.model.IGFolder;
import com.liferay.portlet.imagegallery.model.IGImage;
import com.liferay.portlet.imagegallery.service.IGFolderLocalServiceUtil;
import com.liferay.portlet.imagegallery.service.IGImageLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleConstants;
import com.liferay.portlet.journal.model.JournalStructure;
import com.liferay.portlet.journal.model.JournalTemplate;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalStructureLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalTemplateLocalServiceUtil;
import com.liferay.portlet.messageboards.model.MBCategory;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.service.MBCategoryLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.social.model.SocialRequest;
import com.liferay.portlet.social.model.SocialRequestConstants;
import com.liferay.portlet.social.service.SocialRequestLocalServiceUtil;
import com.liferay.portlet.wiki.model.WikiNode;
import com.liferay.portlet.wiki.model.WikiPage;
import com.liferay.portlet.wiki.service.WikiNodeLocalServiceUtil;
import com.liferay.portlet.wiki.service.WikiPageLocalServiceUtil;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletPreferences;

/**
 * @author Brian Wing Shun Chan
 * @author Ryan Park
 */
public class UpgradeCompany extends UpgradeProcess {

	protected AssetCategory addAssetCategory(
			long userId, long parentCategoryId, String title, long vocabularyId,
			ServiceContext serviceContext)
		throws Exception {

		Map<Locale, String> titleMap = new HashMap<Locale, String>();

		titleMap.put(Locale.US, title);

		return AssetCategoryLocalServiceUtil.addCategory(
			userId, parentCategoryId, titleMap, vocabularyId, null,
			serviceContext);
	}

	protected AssetVocabulary addAssetVocabulary(
			long userId, String title, ServiceContext serviceContext)
		throws Exception {

		Map<Locale, String> titleMap = new HashMap<Locale, String>();

		titleMap.put(Locale.US, title);

		return AssetVocabularyLocalServiceUtil.addVocabulary(
			userId, titleMap, null, null, serviceContext);
	}

	protected BlogsEntry addBlogsEntry(
			long userId, String title, String fileName,
			ServiceContext serviceContext)
		throws Exception {

		String content = getString(fileName);

		return BlogsEntryLocalServiceUtil.addEntry(
			userId, title, content, 1, 1, 2008, 0, 0, false, false,
			new String[0], serviceContext);
	}

	protected DLFileEntry addDLFileEntry(
			long userId, long groupId, long folderId, String fileName,
			String name, String title, String description,
			ServiceContext serviceContext)
		throws Exception {

		byte[] bytes = getBytes(fileName);

		serviceContext.setAddCommunityPermissions(true);
		serviceContext.setAddGuestPermissions(false);

		try {
			return DLFileEntryLocalServiceUtil.addFileEntry(
				userId, groupId, folderId, name, title, description,
				StringPool.BLANK, StringPool.BLANK, bytes, serviceContext);
		}
		catch (DuplicateFileException dfe) {
			return DLFileEntryLocalServiceUtil.updateFileEntry(
				userId, groupId, folderId, name, null, title, description,
				StringPool.BLANK, true, StringPool.BLANK, bytes,
				serviceContext);
		}
	}

	protected DLFolder addDLFolder(
			long userId, long groupId, String name, String description)
		throws Exception {

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddCommunityPermissions(true);
		serviceContext.setAddGuestPermissions(false);

		return DLFolderLocalServiceUtil.addFolder(
			userId, groupId, 0, name, description, serviceContext);
	}

	protected IGImage addIGImage(
			long userId, long folderId, String name, String fileName,
			ServiceContext serviceContext)
		throws Exception {

		InputStream is = getInputStream(fileName);

		return IGImageLocalServiceUtil.addImage(
			userId, serviceContext.getScopeGroupId(), folderId, name,
			StringPool.BLANK, name, is, "image/png", serviceContext);
	}

	protected JournalArticle addJournalArticle(
			long userId, long groupId, String title, String fileName,
			ServiceContext serviceContext)
		throws Exception {

		return addJournalArticle(
			userId, groupId, title, fileName, StringPool.BLANK,
			StringPool.BLANK, serviceContext);
	}

	protected JournalArticle addJournalArticle(
			long userId, long groupId, String title, String fileName,
			String structureId, String templateId,
			ServiceContext serviceContext)
		throws Exception {

		String content = getString(fileName);

		serviceContext.setAddCommunityPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		JournalArticle journalArticle =
			JournalArticleLocalServiceUtil.addArticle(
				userId, groupId, StringPool.BLANK, true,
				JournalArticleConstants.DEFAULT_VERSION, title,
				StringPool.BLANK, content, "general", structureId, templateId,
				1, 1, 2008, 0, 0, 0, 0, 0, 0, 0, true, 0, 0, 0, 0, 0, true,
				true, false, StringPool.BLANK, null, null, StringPool.BLANK,
				serviceContext);

		JournalArticleLocalServiceUtil.updateStatus(
			userId, groupId, journalArticle.getArticleId(),
			journalArticle.getVersion(), WorkflowConstants.STATUS_APPROVED,
			StringPool.BLANK, serviceContext);

		return journalArticle;
	}

	protected JournalStructure addJournalStructure(
			long userId, long groupId, String fileName)
		throws Exception {

		String xsd = getString(fileName);

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddCommunityPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		return JournalStructureLocalServiceUtil.addStructure(
			userId, groupId, "SINGLE-IMAGE", false, StringPool.BLANK,
			"Single Image", "A single image, optional link", xsd,
			serviceContext);
	}

	protected JournalTemplate addJournalTemplate(
			long userId, long groupId, String fileName)
		throws Exception {

		String xsl = getString(fileName);

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddCommunityPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		return JournalTemplateLocalServiceUtil.addTemplate (
			userId, groupId, "SINGLE-IMAGE", false, "SINGLE-IMAGE",
			"Single Image", "A single image, optional URL", xsl, true, "vm",
			true, false, StringPool.BLANK,	null, serviceContext) ;
	}

	protected Layout addLayout(
			Group group, String name, boolean privateLayout, String friendlyURL,
			String layouteTemplateId)
		throws Exception {

		ServiceContext serviceContext = new ServiceContext();

		Layout layout = LayoutLocalServiceUtil.addLayout(
			group.getCreatorUserId(), group.getGroupId(), privateLayout,
			LayoutConstants.DEFAULT_PARENT_LAYOUT_ID, name, StringPool.BLANK,
			StringPool.BLANK, LayoutConstants.TYPE_PORTLET, false, friendlyURL,
			serviceContext);

		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet)layout.getLayoutType();

		layoutTypePortlet.setLayoutTemplateId(0, layouteTemplateId, false);

		addResources(layout, PortletKeys.DOCKBAR);

		return layout;
	}

	protected MBCategory addMBCategory(
			long userId, String name, String description,
			ServiceContext serviceContext)
		throws Exception {

		return MBCategoryLocalServiceUtil.addCategory(
			userId, 0, name, description, StringPool.BLANK, StringPool.BLANK,
			StringPool.BLANK, 0, false, StringPool.BLANK, StringPool.BLANK, 1,
			StringPool.BLANK, false, StringPool.BLANK, 0, false,
			StringPool.BLANK, StringPool.BLANK, false, serviceContext);
	}

	protected MBMessage addMBMessage(
			long userId, String userName, long groupId, long categoryId,
			long threadId, long parentMessageId, String subject,
			String fileName, ServiceContext serviceContext)
		throws Exception {

		String body = getString(fileName);

		return MBMessageLocalServiceUtil.addMessage(
			userId, userName, groupId, categoryId, threadId, parentMessageId,
			subject, body, new ArrayList<ObjectValuePair<String, byte[]>>(),
			false, -1.0, false, serviceContext);
	}

	protected String addPortletId(
			Layout layout, String portletId, String columnId)
		throws Exception {

		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet)layout.getLayoutType();

		portletId = layoutTypePortlet.addPortletId(
			0, portletId, columnId, -1, false);

		addResources(layout, portletId);
		updateLayout(layout);

		return portletId;
	}

	protected void addResources(Layout layout, String portletId)
		throws Exception{

		String rootPortletId = PortletConstants.getRootPortletId(portletId);

		String portletPrimaryKey = PortletPermissionUtil.getPrimaryKey(
			layout.getPlid(), portletId);

		ResourceLocalServiceUtil.addResources(
			layout.getCompanyId(), layout.getGroupId(), 0, rootPortletId,
			portletPrimaryKey, true, true, true);
	}

	protected void addSocialRequest(
			User user, User receiverUser, boolean confirm)
		throws Exception {

		SocialRequest socialRequest = SocialRequestLocalServiceUtil.addRequest(
			user.getUserId(), 0, User.class.getName(), user.getUserId(),
			_SN_FRIENDS_REQUEST_KEYS_ADD_FRIEND, StringPool.BLANK,
			receiverUser.getUserId());

		if (confirm) {
			SocialRequestLocalServiceUtil.updateRequest(
				socialRequest.getRequestId(),
				SocialRequestConstants.STATUS_CONFIRM, new ThemeDisplay());
		}
	}

	protected User addUser(
			long companyId, String screenName, String firstName,
			String lastName, boolean male, String jobTitle, long[] roleIds)
		throws Exception {

		long creatorUserId = 0;
		boolean autoPassword = false;
		String password1 = screenName;
		String password2 = password1;
		boolean autoScreenName = false;
		String emailAddress = screenName + "@7cogs.com";
		long facebookId = 0;
		String openId = StringPool.BLANK;
		Locale locale = Locale.US;
		String middleName = StringPool.BLANK;
		int prefixId = 0;
		int suffixId = 0;
		int birthdayMonth = Calendar.JANUARY;
		int birthdayDay = 1;
		int birthdayYear = 1970;

		Group guestGroup = GroupLocalServiceUtil.getGroup(
			companyId, GroupConstants.GUEST);

		long[] groupIds = new long[] {guestGroup.getGroupId()};

		Organization sevenCogsOrganization =
			OrganizationLocalServiceUtil.getOrganization(
				companyId, "7Cogs, Inc.");

		long[] organizationIds = new long[] {
			sevenCogsOrganization.getOrganizationId()
		};

		long[] userGroupIds = null;
		boolean sendEmail = false;

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setScopeGroupId(guestGroup.getGroupId());

		User user = UserLocalServiceUtil.addUser(
			creatorUserId, companyId, autoPassword, password1, password2,
			autoScreenName, screenName, emailAddress, facebookId, openId,
			locale, firstName, middleName, lastName, prefixId, suffixId, male,
			birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds,
			organizationIds, roleIds, userGroupIds, sendEmail, serviceContext);

		byte[] portrait = getBytes(
			"/users/images/" + screenName + "_portrait.jpg");

		UserLocalServiceUtil.updatePortrait(user.getUserId(), portrait);

		String[] questions = StringUtil.split(
			PropsUtil.get("users.reminder.queries.questions"));

		String question = questions[0];
		String answer = "1234";

		UserLocalServiceUtil.updateReminderQuery(
			user.getUserId(), question, answer);

		Group group = user.getGroup();

		// Profile layout

		Layout layout = addLayout(
			group, "Profile", false, "/profile", "2_columns_ii");

		String portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-2");

		highlightPortlet(layout, portletId);

		serviceContext.setAssetTagNames(new String[] {"community", "tips"});

		String fileName =
			"/users/journal/articles/my_community_" + user.getScreenName() +
				".xml";

		JournalArticle journalArticle = addJournalArticle(
			user.getUserId(), group.getGroupId(),
			"Public Pages " + user.getScreenName(), fileName, serviceContext);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		addPortletId(layout, "1_WAR_socialnetworkingportlet", "column-1");
		addPortletId(layout, PortletKeys.REQUESTS, "column-1");
		addPortletId(layout, "2_WAR_socialnetworkingportlet", "column-1");
		addPortletId(layout, PortletKeys.ACTIVITIES, "column-2");
		addPortletId(layout, "3_WAR_socialnetworkingportlet", "column-2");

		// Blog layout

		layout = addLayout(group, "Blog", false, "/blog", "2_columns_ii");

		addPortletId(layout, PortletKeys.RECENT_BLOGGERS, "column-1");
		addPortletId(layout, PortletKeys.BLOGS, "column-2");

		// Social layout

		layout = addLayout(group, "Social", true, "/social", "2_columns_ii");

		addPortletId(layout, "1_WAR_socialnetworkingportlet", "column-1");
		addPortletId(layout, PortletKeys.REQUESTS, "column-1");

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-2");

		highlightPortlet(layout, portletId);

		serviceContext.setAssetTagNames(new String[] {"social", "tips"});

		fileName =
			"/users/journal/articles/private_pages_" + user.getScreenName() +
				".xml";

		journalArticle = addJournalArticle(
			user.getUserId(), group.getGroupId(),
			"Public Pages " + user.getScreenName(), fileName, serviceContext);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		addPortletId(layout, "4_WAR_socialnetworkingportlet", "column-2");

		portletId = addPortletId(layout, PortletKeys.IFRAME, "column-1");

		Map<String, String> preferences = new HashMap<String, String>();

		preferences.put("portlet-setup-show-borders", Boolean.FALSE.toString());

		if (screenName.equals("bruno") || screenName.equals("john")) {
			preferences.put("src","http://m.digg.com");
			preferences.put("height-normal","400");
		}
		else if (screenName.equals("michelle")) {
			preferences.put("src","http://m.digg.com");
			preferences.put("height-normal","400");
		}
		else if (screenName.equals("richard")) {
			preferences.put("src","http://m.linkedin.com");
			preferences.put("height-normal","350");
		}

		updatePortletSetup(layout, portletId, preferences);

		// Workspace layout

		layout = addLayout(
			group, "Workspace", true, "/workspace", "2_columns_i");

		addPortletId(layout, PortletKeys.RECENT_DOCUMENTS, "column-1");
		addPortletId(layout, PortletKeys.DOCUMENT_LIBRARY, "column-1");
		addPortletId(layout, PortletKeys.IMAGE_GALLERY, "column-1");

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-2");

		highlightPortlet(layout, portletId);

		serviceContext.setAssetTagNames(new String[] {"documents", "tips"});

		journalArticle = addJournalArticle(
			user.getUserId(), group.getGroupId(), "My documents",
			"/users/journal/articles/workspace_docs.xml", serviceContext);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		addPortletId(layout, PortletKeys.CALENDAR, "column-2");

		// Email layout

		layout = addLayout(group, "Email", true, "/email", "1_column");

		addPortletId(layout, "1_WAR_mailportlet", "column-1");

		return user;
	}

	protected WikiPage addWikiPage(
			long userId, long nodeId, String title, String fileName,
			ServiceContext serviceContext)
		throws Exception {

		String content = getString(fileName);

		return WikiPageLocalServiceUtil.addPage(
			userId, nodeId, title, content, "New", false, serviceContext);
	}

	protected void clearData(long companyId) throws Exception {

		// Users

		List<User> users = UserLocalServiceUtil.search(
			companyId, null, null, null, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			(OrderByComparator)null);

		for (User user : users) {
			String screenName = user.getScreenName();

			if (screenName.equals("joebloggs") || screenName.equals("test")) {
				continue;
			}

			UserLocalServiceUtil.deleteUser(user.getUserId());
		}

		// Groups

		List<Group> groups = GroupLocalServiceUtil.search(
			companyId, null, null, null, QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (Group group : groups) {
			IGFolderLocalServiceUtil.deleteFolders(group.getGroupId());

			JournalArticleLocalServiceUtil.deleteArticles(group.getGroupId());
			JournalTemplateLocalServiceUtil.deleteTemplates(group.getGroupId());
			JournalStructureLocalServiceUtil.deleteStructures(
				group.getGroupId());

			if (!PortalUtil.isSystemGroup(group.getName())) {
				GroupLocalServiceUtil.deleteGroup(group.getGroupId());
			}
			else {
				LayoutLocalServiceUtil.deleteLayouts(group.getGroupId(), false);
				LayoutLocalServiceUtil.deleteLayouts(group.getGroupId(), true);
			}
		}

		// Organizations

		deleteOrganizations(
			companyId, OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID);
	}

	protected void configureJournalContent(
			Layout layout, String portletId, String articleId)
		throws Exception {

		PortletPreferences portletSetup =
			PortletPreferencesFactoryUtil.getLayoutPortletSetup(
				layout, portletId);

		portletSetup.setValue("group-id", String.valueOf(layout.getGroupId()));
		portletSetup.setValue("article-id", articleId);

		portletSetup.store();
	}

	protected void configurePortletTitle(
			Layout layout, String portletId, String title)
		throws Exception {

		PortletPreferences portletSetup =
			PortletPreferencesFactoryUtil.getLayoutPortletSetup(
				layout, portletId);

		portletSetup.setValue("portlet-setup-title", title);

		portletSetup.store();
	}

	protected void deleteOrganizations(
			long companyId, long parentOrganizationId)
		throws Exception {

		List<Organization> organizations = OrganizationLocalServiceUtil.search(
			companyId, parentOrganizationId, null, null, null, null, null,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, (OrderByComparator)null);

		for (Organization organization : organizations) {
			deleteOrganizations(companyId, organization.getOrganizationId());
		}

		if (parentOrganizationId > 0) {
			long[] userIds = UserLocalServiceUtil.getOrganizationUserIds(
				parentOrganizationId);

			UserLocalServiceUtil.unsetOrganizationUsers(
				parentOrganizationId, userIds);

			OrganizationLocalServiceUtil.deleteOrganization(
				parentOrganizationId);
		}
	}

	protected void doUpgrade() throws Exception {
		long companyId = PortalUtil.getDefaultCompanyId();

		long defaultUserId = UserLocalServiceUtil.getDefaultUserId(companyId);

		clearData(companyId);
		setupCommunities(companyId, defaultUserId);
		setupOrganizations(companyId, defaultUserId);
		setupRoles(companyId, defaultUserId);
		setupUsers(companyId);
		setupWorkflow(companyId, defaultUserId);
	}

	protected byte[] getBytes(String path) throws Exception {
		return FileUtil.getBytes(getInputStream(path));
	}

	protected InputStream getInputStream(String path) throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();

		return classLoader.getResourceAsStream("/resources" + path);
	}

	protected String getString(String path) throws Exception {
		return new String(getBytes(path));
	}

	protected void highlightPortlet(Layout layout, String portletId)
		throws Exception {

		PortletPreferences portletSetup =
			PortletPreferencesFactoryUtil.getLayoutPortletSetup(
				layout, portletId);

		portletSetup.setValue(
			"portlet-setup-show-borders", String.valueOf(Boolean.FALSE));
		portletSetup.setValue(
			"portlet-setup-css", getString("/preferences/highlight.json"));

		portletSetup.store();
	}

	protected void removePortletBorder(Layout layout, String portletId)
		throws Exception {

		PortletPreferences portletSetup =
			PortletPreferencesFactoryUtil.getLayoutPortletSetup(
				layout, portletId);

		portletSetup.setValue(
			"portlet-setup-show-borders", String.valueOf(Boolean.FALSE));

		portletSetup.store();
	}

	protected void setRolePermissions(
			Role role, String name, String[] actionIds)
		throws Exception {

		long roleId = role.getRoleId();
		long companyId = role.getCompanyId();
		int scope =	ResourceConstants.SCOPE_COMPANY;
		String primKey = String.valueOf(companyId);

		if (_PERMISSIONS_USER_CHECK_ALGORITHM == 6) {
			ResourcePermissionLocalServiceUtil.setResourcePermissions(
				companyId, name, scope, primKey, roleId, actionIds);
		}
		else {
			PermissionLocalServiceUtil.setRolePermissions(
				roleId, companyId, name, scope, primKey, actionIds);
		}
	}

	protected void setupCommunities(long companyId, long defaultUserId)
		throws Exception {

		// Guest community

		Group group = GroupLocalServiceUtil.getGroup(
			companyId, GroupConstants.GUEST);

		// Journal

		addJournalStructure(
			defaultUserId, group.getGroupId(),
			"/guest/journal/structures/single_image.xml");
		addJournalTemplate(
			defaultUserId, group.getGroupId(),
			"/guest/journal/templates/single_image.xml");

		// Image gallery

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddCommunityPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setScopeGroupId(group.getGroupId());

		IGFolder igFolder = IGFolderLocalServiceUtil.addFolder(
			defaultUserId, 0, "Web Content", "Images used for content",
			serviceContext);

		IGImage cellBgIGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "cell_bg.png",
			"/guest/images/cell_bg.png", serviceContext);

		IGImage portalMashupIGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "portal_mashup.png",
			"/guest/images/portal_mashup.png", serviceContext);

		IGImage sevenCogsAdIGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "sevencogs_ad.png",
			"/guest/images/sevencogs_ad.png", serviceContext);

		IGImage sevenCogsMobileAdIGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "sevencogs_mobile_ad.png",
			"/guest/images/sevencogs_mobile_ad.png", serviceContext);

		IGImage sharedWorkspacesIGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "shared_workspaces.png",
			"/guest/images/shared_workspaces.png", serviceContext);

		IGImage socialNetworkingIGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "social_network.png",
			"/guest/images/social_network.png", serviceContext);

		IGImage webPublishingIGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "web_publishing.png",
			"/guest/images/web_publishing.png", serviceContext);

		// Welcome layout

		Layout layout = addLayout(
			group, "Welcome", false, "/home", "2_columns_iii");

		// Welcome content portlet

		String portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-1");

		removePortletBorder(layout, portletId);

		serviceContext.setAssetTagNames(new String[] {"liferay", "welcome"});

		JournalArticle journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Welcome",
			"/guest/journal/articles/welcome.xml", serviceContext);

		String content = StringUtil.replace(
			journalArticle.getContent(),
			new String[] {
				"[$CELL_BG_IG_IMAGE_UUID$]",
				"[$GROUP_ID$]",
				"[$PORTAL_MASHUPS_IG_IMAGE_UUID$]",
				"[$SHARED_WORKSPACES_IG_IMAGE_UUID$]",
				"[$SOCIAL_NETWORKING_IG_IMAGE_UUID$]",
				"[$WEB_PUBLISHING_IG_IMAGE_UUID$]"
			},
			new String[] {
				String.valueOf(cellBgIGImage.getUuid()),
				String.valueOf(group.getGroupId()),
				String.valueOf(portalMashupIGImage.getUuid()),
				String.valueOf(sharedWorkspacesIGImage.getUuid()),
				String.valueOf(socialNetworkingIGImage.getUuid()),
				String.valueOf(webPublishingIGImage.getUuid())
			});

		JournalArticleLocalServiceUtil.updateContent(
			group.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), content);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// 7Cogs Ad content portlet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-2");

		removePortletBorder(layout, portletId);

		serviceContext.setAssetTagNames(new String[] {"liferay", "7cogs"});

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "7Cogs Ad",
			"/guest/journal/articles/sample_site_ad.xml", serviceContext);

		content = StringUtil.replace(
			journalArticle.getContent(),
			new String[] {
				"[$GROUP_ID$]",
				"[$GROUP_URL$]",
				"[$IG_IMAGE_UUID$]"
			},
			new String[] {
				String.valueOf(group.getGroupId()),
				"/web/7cogs/home",
				String.valueOf(sevenCogsAdIGImage.getUuid())
			});

		JournalArticleLocalServiceUtil.updateContent(
			group.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), content);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// 7Cogs Mobile Ad content portlet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-2");

		removePortletBorder(layout, portletId);

		serviceContext.setAssetTagNames(new String[] {"liferay", "7cogs"});

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "7Cogs Mobile Ad",
			"/guest/journal/articles/sample_site_ad.xml", serviceContext);

		content = StringUtil.replace(
			journalArticle.getContent(),
			new String[] {
				"[$GROUP_ID$]",
				"[$GROUP_URL$]",
				"[$IG_IMAGE_UUID$]"
			},
			new String[] {
				String.valueOf(group.getGroupId()),
				"/web/7cogs-mobile/home",
				String.valueOf(sevenCogsMobileAdIGImage.getUuid())
			});

		JournalArticleLocalServiceUtil.updateContent(
			group.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), content);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// Welcome Note content portlet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-2");

		removePortletBorder(layout, portletId);

		serviceContext.setAssetTagNames(new String[] {"welcome"});

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Welcome Note",
			"/guest/journal/articles/welcome_note.xml", serviceContext);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// Welcome Login content portlet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-2");

		serviceContext.setAssetTagNames(
			new String[] {"login", "users", "welcome"});

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Welcome Login",
			"/guest/journal/articles/welcome_login.xml", serviceContext);

		content = StringUtil.replace(
			journalArticle.getContent(), "[$COMPANY_ID$]",
			String.valueOf(companyId));

		JournalArticleLocalServiceUtil.updateContent(
			group.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), content);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		configurePortletTitle(layout, portletId, "Current Users");

		// Login portlet

		addPortletId(layout, PortletKeys.LOGIN, "column-2");
	}

	protected void setupOrganizations(long companyId, long defaultUserId)
		throws Exception {

		// 7Cogs, Inc. organization

		long userId = defaultUserId;
		long parentOrganizationId =
			OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID;
		String name = "7Cogs, Inc.";
		String type = OrganizationConstants.TYPE_REGULAR_ORGANIZATION;
		boolean recursable = true;
		long regionId = 0;
		long countryId = 0;
		int statusId = GetterUtil.getInteger(PropsUtil.get(
			"sql.data.com.liferay.portal.model.ListType.organization.status"));
		String comments = null;

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddCommunityPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		Organization organization =
			OrganizationLocalServiceUtil.addOrganization(
				userId, parentOrganizationId, name, type, recursable, regionId,
				countryId, statusId, comments, serviceContext);

		// Group

		Group group = organization.getGroup();

		GroupLocalServiceUtil.updateFriendlyURL(group.getGroupId(), "/7cogs");

		serviceContext.setScopeGroupId(group.getGroupId());

		// Layout set

		LayoutSetLocalServiceUtil.updateLogo(
			group.getGroupId(), false, true,
			getInputStream("/sample/images/logo.png"));

		LayoutSetLocalServiceUtil.updateLookAndFeel(
			group.getGroupId(), false, "sevencogs_WAR_sevencogstheme", "01", "",
			false);

		// Asset

		AssetVocabulary topicAssetVocabulary = addAssetVocabulary(
			defaultUserId, "Topic", serviceContext);

		AssetVocabulary imageAssetVocabulary = addAssetVocabulary(
			defaultUserId, "Image Type", serviceContext);

		AssetCategory iconAssetCategory = addAssetCategory(
			defaultUserId, 0, "Icon", imageAssetVocabulary.getVocabularyId(),
			serviceContext);

		AssetCategory bannerAssetCategory = addAssetCategory(
			defaultUserId, 0, "Banner", imageAssetVocabulary.getVocabularyId(),
			serviceContext);

		AssetCategory learningAssetCategory = addAssetCategory(
			defaultUserId, 0, "Learning",
			topicAssetVocabulary.getVocabularyId(), serviceContext);

		AssetCategory productsAssetCategory = addAssetCategory(
			defaultUserId, 0, "Products",
			topicAssetVocabulary.getVocabularyId(), serviceContext);

		AssetCategory liferayAssetCategory = addAssetCategory(
			defaultUserId, 0, "Liferay", topicAssetVocabulary.getVocabularyId(),
			serviceContext);

		_assetCategories = new HashMap<String, AssetCategory>();

		_assetCategories.put("Icon", iconAssetCategory);
		_assetCategories.put("Banner", bannerAssetCategory);
		_assetCategories.put("Learning", learningAssetCategory);
		_assetCategories.put("Products", productsAssetCategory);
		_assetCategories.put("Liferay", liferayAssetCategory);

		// Journal

		addJournalStructure(
			defaultUserId, group.getGroupId(),
			"/sample/journal/structures/single_image.xml");
		addJournalTemplate(
			defaultUserId, group.getGroupId(),
			"/sample/journal/templates/single_image.xml");

		// Image gallery

		serviceContext.setScopeGroupId(group.getGroupId());

		IGFolder igFolder = IGFolderLocalServiceUtil.addFolder(
			defaultUserId, 0, "7Cogs Web Content", "Images used for content",
			serviceContext);

		serviceContext.setAssetTagNames(new String[] {"icons"});
		serviceContext.setAssetCategoryIds(
			new long[] {iconAssetCategory.getCategoryId()});

		IGImage cogBlueIconIGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "cog_blue.png",
			"/sample/images/cog_blue.png", serviceContext);

		IGImage cogLightBlueIconIGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "cog_light_blue.png",
			"/sample/images/cog_light_blue.png", serviceContext);

		IGImage cogOrangeIconIGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "cog_orange.png",
			"/sample/images/cog_orange.png", serviceContext);

		serviceContext.setAssetTagNames(new String[] {"home page", "blogs"});
		serviceContext.setAssetCategoryIds(
			new long[] {iconAssetCategory.getCategoryId()});

		IGImage blogsIconIGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "blogs_icon.png",
			"/sample/images/blogs_icon.png", serviceContext);

		serviceContext.setAssetTagNames(new String[] {"home page"});
		serviceContext.setAssetCategoryIds(
			new long[] {productsAssetCategory.getCategoryId()});

		IGImage cogNetworkAdIGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(),
			"cog_network_advertisement.png",
			"/sample/images/cog_network_advertisement.png",
			serviceContext);

		serviceContext.setAssetTagNames(new String[] {"home page", "forums"});
		serviceContext.setAssetCategoryIds(
			new long[] {iconAssetCategory.getCategoryId()});

		IGImage forumsIconIGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "forums_icon.png",
			"/sample/images/forums_icon.png", serviceContext);

		serviceContext.setAssetTagNames(new String[] {"liferay", "logo"});
		serviceContext.setAssetCategoryIds(
			new long[] {liferayAssetCategory.getCategoryId()});

		IGImage liferayLogoIGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "liferay_logo.png",
			"/sample/images/liferay_logo.png", serviceContext);

		serviceContext.setAssetTagNames(new String[] {"home page"});
		serviceContext.setAssetCategoryIds(
			new long[] {bannerAssetCategory.getCategoryId()});

		IGImage homePageBannerIGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "home_page_banner.png",
			"/sample/images/home_page_banner.png", serviceContext);

		serviceContext.setAssetTagNames(new String[] {"home page", "products"});
		serviceContext.setAssetCategoryIds(
			new long[] {iconAssetCategory.getCategoryId()});

		IGImage productsIconIGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "products_icon.png",
			"/sample/images/products_icon.png", serviceContext);

		serviceContext.setAssetTagNames(new String[] {"products"});
		serviceContext.setAssetCategoryIds(
			new long[] {productsAssetCategory.getCategoryId()});

		IGImage productLandingIGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "product_landing.png",
			"/sample/images/product_landing.png", serviceContext);

		// Home layout

		Layout layout = addLayout(
			group, "Home", false, "/home", "home");

		// Home Page Banner content portlet

		String portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-1");

		removePortletBorder(layout, portletId);

		serviceContext.setAssetTagNames(new String[] {"liferay", "7cogs"});

		JournalArticle journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Banner",
			"/sample/journal/articles/home_page_banner.xml", serviceContext);

		String content = StringUtil.replace(
			journalArticle.getContent(),
			new String[] {
				"[$GROUP_ID$]",
				"[$HOME_PAGE_BANNER_IG_IMAGE_UUID$]"
			},
			new String[] {
				String.valueOf(group.getGroupId()),
				String.valueOf(homePageBannerIGImage.getUuid())
			});

		JournalArticleLocalServiceUtil.updateContent(
			group.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), content);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// Home Page Products Button content portlet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-2");

		removePortletBorder(layout, portletId);

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Products Button",
			"/sample/journal/articles/home_page_products_button.xml",
			serviceContext);

		content = StringUtil.replace(
			journalArticle.getContent(),
			new String[] {
				"[$GROUP_ID$]",
				"[$HOME_PAGE_PRODUCTS_IG_IMAGE_UUID$]"
			},
			new String[] {
				String.valueOf(group.getGroupId()),
				String.valueOf(productsIconIGImage.getUuid())
			});

		JournalArticleLocalServiceUtil.updateContent(
			group.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), content);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// Home Page Blogs Button content portlet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-3");

		removePortletBorder(layout, portletId);

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Blogs Button",
			"/sample/journal/articles/home_page_blogs_button.xml",
			serviceContext);

		content = StringUtil.replace(
			journalArticle.getContent(),
			new String[] {
				"[$GROUP_ID$]",
				"[$HOME_PAGE_BLOGS_IG_IMAGE_UUID$]"
			},
			new String[] {
				String.valueOf(group.getGroupId()),
				String.valueOf(blogsIconIGImage.getUuid())
			});

		JournalArticleLocalServiceUtil.updateContent(
			group.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), content);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// Home Page Forums Button content porltet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-4");

		removePortletBorder(layout, portletId);

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Forums Button",
			"/sample/journal/articles/home_page_forums_button.xml",
			serviceContext);

		content = StringUtil.replace(
			journalArticle.getContent(),
			new String[] {
				"[$GROUP_ID$]",
				"[$HOME_PAGE_FORUMS_IG_IMAGE_UUID$]"
			},
			new String[] {
				String.valueOf(group.getGroupId()),
				String.valueOf(forumsIconIGImage.getUuid())
			});

		JournalArticleLocalServiceUtil.updateContent(
			group.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), content);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// Home Page Intro content portlet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-5");

		removePortletBorder(layout, portletId);

		serviceContext.setAssetTagNames(new String[] {"front"});

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Home Page Intro",
			"/sample/journal/articles/home_page_intro.xml",
			serviceContext);

		content = StringUtil.replace(
			journalArticle.getContent(),
			new String[] {
				"[$GROUP_ID$]",
				"[$PRODUCT_LANDING_IG_IMAGE_UUID$]"
			},
			new String[] {
				String.valueOf(group.getGroupId()),
				String.valueOf(productLandingIGImage.getUuid())
			});

		JournalArticleLocalServiceUtil.updateContent(
			group.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), content);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// Cog Network Ad content portlet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-8");

		removePortletBorder(layout, portletId);

		serviceContext.setAssetTagNames(new String[] {"liferay", "enterprise"});

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Cog Network Ad",
			"/sample/journal/articles/home_cog_network_ad.xml", serviceContext);

		content = StringUtil.replace(
			journalArticle.getContent(),
			new String[] {
				"[$GROUP_ID$]",
				"[$COG_NETWORK_AD_IG_IMAGE_UUID$]"
			},
			new String[] {
				String.valueOf(group.getGroupId()),
				String.valueOf(cogNetworkAdIGImage.getUuid())
			});

		JournalArticleLocalServiceUtil.updateContent(
			group.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), content);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// Products layout

		layout = addLayout(
			group, "Products", false, "/products", "1_2_columns_ii");

		// Products Banner content portlet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-1");

		removePortletBorder(layout, portletId);

		serviceContext.setAssetTagNames(new String[] {"Vix-998", "7cogs"});

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Products Banner",
			"/sample/journal/articles/products_banner.xml", serviceContext);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// Products Landing Intro content portlet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-2");

		removePortletBorder(layout, portletId);

		serviceContext.setAssetTagNames(new String[] {"products"});

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Products Landing Intro",
			"/sample/journal/articles/products_landing_intro.xml",
			serviceContext);

		content = StringUtil.replace(
			journalArticle.getContent(),
			new String[] {
				"[$GROUP_ID$]",
				"[$PRODUCT_LANDING_IG_IMAGE_UUID$]"
			},
			new String[] {
				String.valueOf(group.getGroupId()),
				String.valueOf(productLandingIGImage.getUuid())
			});

		JournalArticleLocalServiceUtil.updateContent(
			group.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), content);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// Products Landing Intro Info content portlet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-2");

		removePortletBorder(layout, portletId);

		serviceContext.setAssetTagNames(new String[] {"products"});

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Products Landing Intro Info",
			"/sample/journal/articles/products_landing_intro_info.xml",
			serviceContext);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// Introducing Vix content portlet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-3");

		removePortletBorder(layout, portletId);

		serviceContext.setAssetTagNames(new String[] {"Vix-998", "7cogs"});

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Products Catalog",
			"/sample/journal/articles/products_catalog.xml", serviceContext);

		content = StringUtil.replace(
			journalArticle.getContent(),
			new String[] {
				"[$COG_BLUE_IG_IMAGE_UUID$]",
				"[$COG_LIGHT_BLUE_IG_IMAGE_UUID$]",
				"[$COG_ORANGE_IG_IMAGE_UUID$]",
				"[$GROUP_ID$]"
			},
			new String[] {
				String.valueOf(cogBlueIconIGImage.getUuid()),
				String.valueOf(cogLightBlueIconIGImage.getUuid()),
				String.valueOf(cogOrangeIconIGImage.getUuid()),
				String.valueOf(group.getGroupId())
			});

		JournalArticleLocalServiceUtil.updateContent(
			group.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), content);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// Blogs

		layout = addLayout(group, "Blogs", false, "/blogs", "1_2_columns_i");

		// Blogs Banner content portlet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-1");

		removePortletBorder(layout, portletId);

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Blogs Banner",
			"/sample/journal/articles/blogs_banner.xml", serviceContext);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// Recent Bloggers portlet

		addPortletId(layout, PortletKeys.RECENT_BLOGGERS, "column-2");

		// Blogs Aggregator portlet

		portletId = addPortletId(
			layout, PortletKeys.BLOGS_AGGREGATOR, "column-3");

		configurePortletTitle(layout, portletId, "Blogs");

		// Wiki layout

		layout = addLayout(group, "Wiki", false, "/wiki", "1_column");

		// Wiki Banner content portlet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-1");

		removePortletBorder(layout, portletId);

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Wiki Banner",
			"/sample/journal/articles/wiki_banner.xml", serviceContext);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// Wiki

		portletId = addPortletId(layout, PortletKeys.WIKI, "column-1");

		removePortletBorder(layout, portletId);

		WikiNode wikiNode = WikiNodeLocalServiceUtil.addNode(
			defaultUserId, "Main", StringPool.BLANK, serviceContext);

		serviceContext.setAssetTagNames(new String[] {"new", "features"});
		serviceContext.setAssetCategoryIds(
			new long[] {learningAssetCategory.getCategoryId()});

		addWikiPage(
			defaultUserId, wikiNode.getNodeId(), "FrontPage",
			"/sample/wiki/FrontPage.xml", serviceContext);

		serviceContext.setAssetTagNames(new String[] {"vix-998", "features"});
		serviceContext.setAssetCategoryIds(
			new long[] {productsAssetCategory.getCategoryId()});

		addWikiPage(
			defaultUserId, wikiNode.getNodeId(), "Vix-998",
			"/sample/wiki/Vix-998.xml", serviceContext);

		// Forums layout

		layout = addLayout(group, "Forums", false, "/forums", "1_column");

		// Forums Banner content portlet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-1");

		removePortletBorder(layout, portletId);

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Forums Banner",
			"/sample/journal/articles/forums_banner.xml", serviceContext);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// Message Boards

		portletId = addPortletId(
			layout, PortletKeys.MESSAGE_BOARDS, "column-1");

		removePortletBorder(layout, portletId);

		// About Us layout

		layout = addLayout(
			group, "About Us", false, "/about_us", "1_2_columns_ii");

		// About Us Banner content portlet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-1");

		removePortletBorder(layout, portletId);

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "About Us Banner",
			"/sample/journal/articles/about_us_banner.xml", serviceContext);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// About Us content portlet

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-2");

		highlightPortlet(layout, portletId);

		serviceContext.setAssetTagNames(new String[] {"liferay"});

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "About Us",
			"/sample/journal/articles/home_7cogs_about_us.xml", serviceContext);

		content = StringUtil.replace(
			journalArticle.getContent(),
			new String[] {
				"[$GROUP_ID$]",
				"[$LIFERAY_LOGO_IG_IMAGE_UUID$]"
			},
			new String[] {
				String.valueOf(group.getGroupId()),
				String.valueOf(liferayLogoIGImage.getUuid())
			});

		JournalArticleLocalServiceUtil.updateContent(
			group.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), content);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// Google Maps

		portletId = addPortletId(layout, "1_WAR_googlemapsportlet", "column-2");

		Map<String, String> preferences = new HashMap<String, String>();

		preferences.put("map-address", "Los Angeles, USA");
		preferences.put("height", "300");

		updatePortletSetup(layout, portletId, preferences);

		// Web Form

		addPortletId(layout, "1_WAR_webformportlet", "column-3");

		// Home layout

		layout = addLayout(group, "Home", true, "/home", "2_columns_ii");

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-1");

		highlightPortlet(layout, portletId);

		serviceContext.setAssetTagNames(new String[] {"7cogs", "tips"});

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Home",
			"/sample/journal/articles/home_7cogs_private_pages.xml",
			serviceContext);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		portletId = addPortletId(layout, PortletKeys.ACTIVITIES, "column-2");

		configurePortletTitle(
			layout, portletId, "Last Activities in 7Cogs Organization");

		// Documents layout

		layout = addLayout(
			group, "Documents", true, "/documents", "2_columns_iii");

		addPortletId(layout, PortletKeys.DOCUMENT_LIBRARY, "column-1");
		addPortletId(layout, PortletKeys.IMAGE_GALLERY, "column-1");

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-2");

		highlightPortlet(layout, portletId);

		serviceContext.setAssetTagNames(new String[] {"documents", "sharing"});

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Shared Documents",
			"/sample/journal/articles/documents_shared_docs.xml",
			serviceContext);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// 7Cogs, Inc. Mobile organization

		parentOrganizationId = organization.getOrganizationId();
		name = "7Cogs, Inc. Mobile";

		organization = OrganizationLocalServiceUtil.addOrganization(
			userId, parentOrganizationId, name, type, recursable, regionId,
			countryId, statusId, comments, serviceContext);

		// Group

		group = organization.getGroup();

		GroupLocalServiceUtil.updateFriendlyURL(
			group.getGroupId(), "/7cogs-mobile");

		serviceContext.setScopeGroupId(group.getGroupId());

		// Layout set

		LayoutSetLocalServiceUtil.updateLogo(
			group.getGroupId(), false, true,
			getInputStream("/mobile/images/seven_cogs_mobile_logo.png"));

		LayoutSetLocalServiceUtil.updateLookAndFeel(
			group.getGroupId(), false,
			"sevencogsmobile_WAR_sevencogsmobiletheme", "01", "", false);

		// Image gallery

		serviceContext.setScopeGroupId(group.getGroupId());

		igFolder = IGFolderLocalServiceUtil.addFolder(
			defaultUserId, 0, "7Cogs Mobile Content",
			"Images used for mobile content", serviceContext);

		serviceContext.setAssetTagNames(null);
		serviceContext.setAssetCategoryIds(null);

		IGImage mobileProduct1IGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "mobile_product_1.png",
			"/mobile/images/mobile_product_1.png", serviceContext);

		IGImage mobileProduct2IGImage = addIGImage(
			defaultUserId, igFolder.getFolderId(), "mobile_product_2.png",
			"/mobile/images/mobile_product_2.png", serviceContext);

		// Home layout

		layout = addLayout(group, "Home", false, "/home", "1_column");

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-1");

		removePortletBorder(layout, portletId);

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Mobile Welcome",
			"/mobile/journal/articles/mobile_welcome.xml", serviceContext);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());

		// Products layout

		layout = addLayout(group, "Products", false, "/products", "1_column");

		portletId = addPortletId(
			layout, PortletKeys.JOURNAL_CONTENT, "column-1");

		removePortletBorder(layout, portletId);

		journalArticle = addJournalArticle(
			defaultUserId, group.getGroupId(), "Mobile Products",
			"/mobile/journal/articles/mobile_products.xml", serviceContext);

		content = StringUtil.replace(
			journalArticle.getContent(),
			new String[] {
				"[$GROUP_ID$]",
				"[$IG_IMAGE_1_UUID$]",
				"[$IG_IMAGE_2_UUID$]"
			},
			new String[] {
				String.valueOf(group.getGroupId()),
				String.valueOf(mobileProduct1IGImage.getUuid()),
				String.valueOf(mobileProduct2IGImage.getUuid())
			});

		JournalArticleLocalServiceUtil.updateContent(
			group.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), content);

		configureJournalContent(
			layout, portletId, journalArticle.getArticleId());
	}

	protected void setupRoles(long companyId, long defaultUserId)
		throws Exception {

		Role publisherRole = RoleLocalServiceUtil.addRole(
			defaultUserId, companyId, "Publisher", null,
			"Publishers are responsible for publishing content.",
			RoleConstants.TYPE_REGULAR);

		setRolePermissions(
			publisherRole, PortletKeys.JOURNAL,
			new String[] {ActionKeys.ACCESS_IN_CONTROL_PANEL});

		setRolePermissions(
			publisherRole, "com.liferay.portlet.journal",
			new String[] {
				ActionKeys.ADD_ARTICLE, ActionKeys.ADD_FEED,
				ActionKeys.ADD_STRUCTURE, ActionKeys.ADD_TEMPLATE
			});

		setRolePermissions(
			publisherRole, JournalArticle.class.getName(),
			new String[] {
				ActionKeys.ADD_DISCUSSION, ActionKeys.DELETE, ActionKeys.EXPIRE,
				ActionKeys.PERMISSIONS, ActionKeys.UPDATE, ActionKeys.VIEW
			});

		Role writerRole = RoleLocalServiceUtil.addRole(
			defaultUserId, companyId, "Writer", null,
			"Writers are responsible for creating content.",
			RoleConstants.TYPE_REGULAR);

		setRolePermissions(
			writerRole, PortletKeys.JOURNAL,
			new String[] {ActionKeys.ACCESS_IN_CONTROL_PANEL});

		setRolePermissions(
			writerRole, "com.liferay.portlet.journal",
			new String[] {
				ActionKeys.ADD_ARTICLE, ActionKeys.ADD_FEED,
				ActionKeys.ADD_STRUCTURE, ActionKeys.ADD_TEMPLATE
			});

		setRolePermissions(
			writerRole, JournalArticle.class.getName(),
			new String[] {ActionKeys.UPDATE, ActionKeys.VIEW});
	}

	protected void setupUsers(long companyId) throws Exception {

		// Roles

		Role adminRole = RoleLocalServiceUtil.getRole(
			companyId, RoleConstants.ADMINISTRATOR);

		Role portalContentReviewer = RoleLocalServiceUtil.getRole(
			companyId, "Portal Content Reviewer");

		Role powerUserRole = RoleLocalServiceUtil.getRole(
			companyId, RoleConstants.POWER_USER);

		Role publisherRole = RoleLocalServiceUtil.getRole(
			companyId, "Publisher");

		Role writerRole = RoleLocalServiceUtil.getRole(
			companyId, "Writer");

		// Users

		long[] roleIds = new long[] {
			adminRole.getRoleId(), powerUserRole.getRoleId()
		};

		User brunoUser = addUser(
			companyId, "bruno", "Bruno", "Admin", true, "Administrator",
			roleIds);

		roleIds = new long[] {powerUserRole.getRoleId()};

		User johnUser = addUser(
			companyId, "john", "John", "Regular", true, "Employee", roleIds);

		roleIds = new long[] {
			powerUserRole.getRoleId(), writerRole.getRoleId()
		};

		User michelleUser = addUser(
			companyId, "michelle", "Michelle", "Writer", false, "Writer",
			roleIds);

		roleIds = new long[] {
			powerUserRole.getRoleId(), publisherRole.getRoleId(),
			portalContentReviewer.getRoleId()
		};

		User richardUser = addUser(
			companyId, "richard", "Richard", "Editor", true, "Publisher",
			roleIds);

		// Asset

		AssetCategory learningAssetCategory = _assetCategories.get("Learning");
		AssetCategory liferayAssetCategory = _assetCategories.get("Liferay");

		// Blogs

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddCommunityPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setAssetTagNames(
			new String[] {"new", "features", "control panel"});
		serviceContext.setAssetCategoryIds(
			new long[] {
				learningAssetCategory.getCategoryId(),
				liferayAssetCategory.getCategoryId()
			});
		serviceContext.setScopeGroupId(brunoUser.getGroup().getGroupId());

		addBlogsEntry(
			brunoUser.getUserId(), "New Control Panel!!",
			"/users/blogs/controlpanel.xml", serviceContext);

		serviceContext.setAssetCategoryIds(
			new long[] {
				learningAssetCategory.getCategoryId(),
				liferayAssetCategory.getCategoryId()
			});
		serviceContext.setAssetTagNames(
			new String[] {
				"configuration", "portal.properties", "customization"
			});

		addBlogsEntry(
			brunoUser.getUserId(),
			"Configuration of the portal: portal.properties",
			"/users/blogs/portalproperties.xml", serviceContext);

		serviceContext.setAssetCategoryIds(
			new long[] {
				learningAssetCategory.getCategoryId(),
				liferayAssetCategory.getCategoryId()
			});
		serviceContext.setAssetTagNames(
			new String[] {"new", "wiki", "knowledge"});
		serviceContext.setScopeGroupId(johnUser.getGroup().getGroupId());

		addBlogsEntry(
			johnUser.getUserId(), "Using the wiki", "/users/blogs/wiki.xml",
			serviceContext);

		serviceContext.setAssetCategoryIds(
			new long[] {
				learningAssetCategory.getCategoryId(),
				liferayAssetCategory.getCategoryId()
			});
		serviceContext.setAssetTagNames(
			new String[] {"new", "chat", "communications", "features"});
		serviceContext.setScopeGroupId(michelleUser.getGroup().getGroupId());

		addBlogsEntry(
			michelleUser.getUserId(), "We have an amazing Chat!",
			"/users/blogs/chat.xml", serviceContext);

		// Document library

		DLFolder dlFolder = addDLFolder(
			brunoUser.getUserId(), brunoUser.getGroup().getGroupId(),
			"Important Documents", "Documents related with the company");

		serviceContext.setAssetTagNames(
			new String[] {"document", "budget", "2009"});

		addDLFileEntry(
			brunoUser.getUserId(), dlFolder.getGroupId(),
			dlFolder.getFolderId(), "/users/document_library/Budget.xls",
			"Budget.xls", "Budget", "Budgets for the current year",
			serviceContext);

		addDLFolder(
			michelleUser.getUserId(), michelleUser.getGroup().getGroupId(),
			"My Documents", "Personal docs");

		dlFolder = addDLFolder(
			michelleUser.getUserId(), michelleUser.getGroup().getGroupId(),
			"Work Documents", "Works docs");

		serviceContext.setAssetTagNames(
			new String[] {"document", "notes", "meeting"});

		addDLFileEntry(
			michelleUser.getUserId(), dlFolder.getGroupId(),
			dlFolder.getFolderId(),
			"/users/document_library/Notes from the last meeting.doc",
			"Notes from the last meeting.doc", "Notes from the last meeting",
			"Important notes", serviceContext);

		addDLFolder(
			richardUser.getUserId(), richardUser.getGroup().getGroupId(),
			"Documentation", StringPool.BLANK);

		dlFolder = addDLFolder(
			richardUser.getUserId(),richardUser.getGroup().getGroupId(),
			"Innovation", "New things");

		serviceContext.setAssetTagNames(
			new String[] {"new", "features", "2009"});

		addDLFileEntry(
			richardUser.getUserId(), dlFolder.getGroupId(),
			dlFolder.getFolderId(), "/users/document_library/New Features.ppt",
			"New Features.ppt", "New Features",
			"Features for the current year", serviceContext);

		// Message boards

		Organization sevenCogsOrganization =
			OrganizationLocalServiceUtil.getOrganization(
			companyId, "7Cogs, Inc.");

		serviceContext.setScopeGroupId(
			sevenCogsOrganization.getGroup().getGroupId());

		MBCategory mbCategory = addMBCategory(
			brunoUser.getUserId(), "Using the forum",
			"Some advice on using the forum", serviceContext);

		serviceContext.setAssetTagNames(
			new String[] {"forums", "liferay", "7cogs"});

		addMBMessage(
			brunoUser.getUserId(), brunoUser.getFullName(),
			mbCategory.getGroupId(), mbCategory.getCategoryId(), 0, 0,
			"Nice Forums", "/sample/message_boards/general.xml",
			serviceContext);

		mbCategory = addMBCategory(
			brunoUser.getUserId(), "General Questions",
			"Product questions and more!", serviceContext);

		serviceContext.setAssetTagNames(new String[] {"vix-998", "liferay"});

		MBMessage vix1Message = addMBMessage(
			brunoUser.getUserId(), brunoUser.getFullName(),
			mbCategory.getGroupId(), mbCategory.getCategoryId(), 0, 0,
			"About the Vix-998", "/sample/message_boards/vix1.xml",
			serviceContext);

		serviceContext.setAssetTagNames(new String[] {"vix-998", "latin"});

		MBMessage vix2Message = addMBMessage(
			richardUser.getUserId(), richardUser.getFullName(),
			mbCategory.getGroupId(), mbCategory.getCategoryId(),
			vix1Message.getThreadId(), vix1Message.getMessageId(),
			"RE: About the Vix-998", "/sample/message_boards/vix2.xml",
			serviceContext);

		serviceContext.setAssetTagNames(new String[] {"vix-998", "vulgo"});

		addMBMessage(
			michelleUser.getUserId(), michelleUser.getFullName(),
			mbCategory.getGroupId(), mbCategory.getCategoryId(),
			vix1Message.getThreadId(), vix2Message.getMessageId(),
			"RE: About the Vix-998", "/sample/message_boards/vix3.xml",
			serviceContext);

		// Social

		addSocialRequest(michelleUser, brunoUser, true);
		addSocialRequest(michelleUser, johnUser, true);
		addSocialRequest(michelleUser, richardUser, true);

		addSocialRequest(johnUser, brunoUser, false);
		addSocialRequest(johnUser, richardUser, false);
	}

	protected void setupWorkflow(long companyId, long defaultUserId)
		throws Exception {

		Group group = GroupLocalServiceUtil.getGroup(
			companyId, GroupConstants.GUEST);

		String workflowDefinitionName = "Single Approver";
		int workflowDefinitionVersion = 1;

		WorkflowDefinitionLinkLocalServiceUtil.updateWorkflowDefinitionLink(
			defaultUserId, companyId, group.getGroupId(),
			JournalArticle.class.getName(), workflowDefinitionName,
			workflowDefinitionVersion);
	}

	protected void updateLayout(Layout layout) throws Exception {
		LayoutLocalServiceUtil.updateLayout(
			layout.getGroupId(), layout.isPrivateLayout(), layout.getLayoutId(),
			layout.getTypeSettings());
	}

	protected PortletPreferences updatePortletSetup(
			Layout layout, String portletId, Map<String, String> preferences)
		throws Exception {

		PortletPreferences portletSetup =
			PortletPreferencesFactoryUtil.getLayoutPortletSetup(
				layout, portletId);

		Iterator<Map.Entry<String, String>> itr =
			preferences.entrySet().iterator();

		while (itr.hasNext()) {
			Map.Entry<String, String> entry = itr.next();

			String key = entry.getKey();
			String value = entry.getValue();

			portletSetup.setValue(key, value);
		}

		portletSetup.store();

		return portletSetup;
	}

	private static int _PERMISSIONS_USER_CHECK_ALGORITHM =
		GetterUtil.getInteger(
			PropsUtil.get(PropsKeys.PERMISSIONS_USER_CHECK_ALGORITHM));

	private static final int _SN_FRIENDS_REQUEST_KEYS_ADD_FRIEND = 1;

	private Map<String, AssetCategory> _assetCategories;

}