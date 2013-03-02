/**
 * Copyright (c) 2000-2008 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portlet.documentlibrary.service.persistence;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.dao.DynamicQuery;
import com.liferay.portal.kernel.dao.DynamicQueryInitializer;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringMaker;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.spring.hibernate.FinderCache;
import com.liferay.portal.spring.hibernate.HibernateUtil;
import com.liferay.portal.util.PropsUtil;

import com.liferay.portlet.documentlibrary.NoSuchFolderException;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.model.impl.DLFolderImpl;
import com.liferay.portlet.documentlibrary.model.impl.DLFolderModelImpl;

import com.liferay.util.dao.hibernate.QueryUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <a href="DLFolderPersistenceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class DLFolderPersistenceImpl extends BasePersistence
	implements DLFolderPersistence {
	public DLFolder create(long folderId) {
		DLFolder dlFolder = new DLFolderImpl();

		dlFolder.setNew(true);
		dlFolder.setPrimaryKey(folderId);

		String uuid = PortalUUIDUtil.generate();

		dlFolder.setUuid(uuid);

		return dlFolder;
	}

	public DLFolder remove(long folderId)
		throws NoSuchFolderException, SystemException {
		Session session = null;

		try {
			session = openSession();

			DLFolder dlFolder = (DLFolder)session.get(DLFolderImpl.class,
					new Long(folderId));

			if (dlFolder == null) {
				if (_log.isWarnEnabled()) {
					_log.warn("No DLFolder exists with the primary key " +
						folderId);
				}

				throw new NoSuchFolderException(
					"No DLFolder exists with the primary key " + folderId);
			}

			return remove(dlFolder);
		}
		catch (NoSuchFolderException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw HibernateUtil.processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public DLFolder remove(DLFolder dlFolder) throws SystemException {
		ModelListener listener = _getListener();

		if (listener != null) {
			listener.onBeforeRemove(dlFolder);
		}

		dlFolder = removeImpl(dlFolder);

		if (listener != null) {
			listener.onAfterRemove(dlFolder);
		}

		return dlFolder;
	}

	protected DLFolder removeImpl(DLFolder dlFolder) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			session.delete(dlFolder);

			session.flush();

			return dlFolder;
		}
		catch (Exception e) {
			throw HibernateUtil.processException(e);
		}
		finally {
			closeSession(session);

			FinderCache.clearCache(DLFolder.class.getName());
		}
	}

	public DLFolder update(DLFolder dlFolder) throws SystemException {
		return update(dlFolder, false);
	}

	public DLFolder update(DLFolder dlFolder, boolean merge)
		throws SystemException {
		ModelListener listener = _getListener();

		boolean isNew = dlFolder.isNew();

		if (listener != null) {
			if (isNew) {
				listener.onBeforeCreate(dlFolder);
			}
			else {
				listener.onBeforeUpdate(dlFolder);
			}
		}

		dlFolder = updateImpl(dlFolder, merge);

		if (listener != null) {
			if (isNew) {
				listener.onAfterCreate(dlFolder);
			}
			else {
				listener.onAfterUpdate(dlFolder);
			}
		}

		return dlFolder;
	}

	public DLFolder updateImpl(
		com.liferay.portlet.documentlibrary.model.DLFolder dlFolder,
		boolean merge) throws SystemException {
		if (Validator.isNull(dlFolder.getUuid())) {
			String uuid = PortalUUIDUtil.generate();

			dlFolder.setUuid(uuid);
		}

		Session session = null;

		try {
			session = openSession();

			if (merge) {
				session.merge(dlFolder);
			}
			else {
				if (dlFolder.isNew()) {
					session.save(dlFolder);
				}
			}

			session.flush();

			dlFolder.setNew(false);

			return dlFolder;
		}
		catch (Exception e) {
			throw HibernateUtil.processException(e);
		}
		finally {
			closeSession(session);

			FinderCache.clearCache(DLFolder.class.getName());
		}
	}

	public DLFolder findByPrimaryKey(long folderId)
		throws NoSuchFolderException, SystemException {
		DLFolder dlFolder = fetchByPrimaryKey(folderId);

		if (dlFolder == null) {
			if (_log.isWarnEnabled()) {
				_log.warn("No DLFolder exists with the primary key " +
					folderId);
			}

			throw new NoSuchFolderException(
				"No DLFolder exists with the primary key " + folderId);
		}

		return dlFolder;
	}

	public DLFolder fetchByPrimaryKey(long folderId) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			return (DLFolder)session.get(DLFolderImpl.class, new Long(folderId));
		}
		catch (Exception e) {
			throw HibernateUtil.processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List findByUuid(String uuid) throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "findByUuid";
		String[] finderParams = new String[] { String.class.getName() };
		Object[] finderArgs = new Object[] { uuid };

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				if (uuid == null) {
					query.append("uuid_ IS NULL");
				}
				else {
					query.append("uuid_ = ?");
				}

				query.append(" ");

				query.append("ORDER BY ");

				query.append("parentFolderId ASC, ");
				query.append("name ASC");

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				if (uuid != null) {
					q.setString(queryPos++, uuid);
				}

				List list = q.list();

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, list);

				return list;
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return (List)result;
		}
	}

	public List findByUuid(String uuid, int begin, int end)
		throws SystemException {
		return findByUuid(uuid, begin, end, null);
	}

	public List findByUuid(String uuid, int begin, int end,
		OrderByComparator obc) throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "findByUuid";
		String[] finderParams = new String[] {
				String.class.getName(),
				
				"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			};
		Object[] finderArgs = new Object[] {
				uuid,
				
				String.valueOf(begin), String.valueOf(end), String.valueOf(obc)
			};

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				if (uuid == null) {
					query.append("uuid_ IS NULL");
				}
				else {
					query.append("uuid_ = ?");
				}

				query.append(" ");

				if (obc != null) {
					query.append("ORDER BY ");
					query.append(obc.getOrderBy());
				}

				else {
					query.append("ORDER BY ");

					query.append("parentFolderId ASC, ");
					query.append("name ASC");
				}

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				if (uuid != null) {
					q.setString(queryPos++, uuid);
				}

				List list = QueryUtil.list(q, getDialect(), begin, end);

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, list);

				return list;
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return (List)result;
		}
	}

	public DLFolder findByUuid_First(String uuid, OrderByComparator obc)
		throws NoSuchFolderException, SystemException {
		List list = findByUuid(uuid, 0, 1, obc);

		if (list.size() == 0) {
			StringMaker msg = new StringMaker();

			msg.append("No DLFolder exists with the key {");

			msg.append("uuid=" + uuid);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			throw new NoSuchFolderException(msg.toString());
		}
		else {
			return (DLFolder)list.get(0);
		}
	}

	public DLFolder findByUuid_Last(String uuid, OrderByComparator obc)
		throws NoSuchFolderException, SystemException {
		int count = countByUuid(uuid);

		List list = findByUuid(uuid, count - 1, count, obc);

		if (list.size() == 0) {
			StringMaker msg = new StringMaker();

			msg.append("No DLFolder exists with the key {");

			msg.append("uuid=" + uuid);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			throw new NoSuchFolderException(msg.toString());
		}
		else {
			return (DLFolder)list.get(0);
		}
	}

	public DLFolder[] findByUuid_PrevAndNext(long folderId, String uuid,
		OrderByComparator obc) throws NoSuchFolderException, SystemException {
		DLFolder dlFolder = findByPrimaryKey(folderId);

		int count = countByUuid(uuid);

		Session session = null;

		try {
			session = openSession();

			StringMaker query = new StringMaker();

			query.append(
				"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

			if (uuid == null) {
				query.append("uuid_ IS NULL");
			}
			else {
				query.append("uuid_ = ?");
			}

			query.append(" ");

			if (obc != null) {
				query.append("ORDER BY ");
				query.append(obc.getOrderBy());
			}

			else {
				query.append("ORDER BY ");

				query.append("parentFolderId ASC, ");
				query.append("name ASC");
			}

			Query q = session.createQuery(query.toString());

			int queryPos = 0;

			if (uuid != null) {
				q.setString(queryPos++, uuid);
			}

			Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, dlFolder);

			DLFolder[] array = new DLFolderImpl[3];

			array[0] = (DLFolder)objArray[0];
			array[1] = (DLFolder)objArray[1];
			array[2] = (DLFolder)objArray[2];

			return array;
		}
		catch (Exception e) {
			throw HibernateUtil.processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public DLFolder findByUUID_G(String uuid, long groupId)
		throws NoSuchFolderException, SystemException {
		DLFolder dlFolder = fetchByUUID_G(uuid, groupId);

		if (dlFolder == null) {
			StringMaker msg = new StringMaker();

			msg.append("No DLFolder exists with the key {");

			msg.append("uuid=" + uuid);

			msg.append(", ");
			msg.append("groupId=" + groupId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchFolderException(msg.toString());
		}

		return dlFolder;
	}

	public DLFolder fetchByUUID_G(String uuid, long groupId)
		throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "fetchByUUID_G";
		String[] finderParams = new String[] {
				String.class.getName(), Long.class.getName()
			};
		Object[] finderArgs = new Object[] { uuid, new Long(groupId) };

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				if (uuid == null) {
					query.append("uuid_ IS NULL");
				}
				else {
					query.append("uuid_ = ?");
				}

				query.append(" AND ");

				query.append("groupId = ?");

				query.append(" ");

				query.append("ORDER BY ");

				query.append("parentFolderId ASC, ");
				query.append("name ASC");

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				if (uuid != null) {
					q.setString(queryPos++, uuid);
				}

				q.setLong(queryPos++, groupId);

				List list = q.list();

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, list);

				if (list.size() == 0) {
					return null;
				}
				else {
					return (DLFolder)list.get(0);
				}
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			List list = (List)result;

			if (list.size() == 0) {
				return null;
			}
			else {
				return (DLFolder)list.get(0);
			}
		}
	}

	public List findByGroupId(long groupId) throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "findByGroupId";
		String[] finderParams = new String[] { Long.class.getName() };
		Object[] finderArgs = new Object[] { new Long(groupId) };

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				query.append("groupId = ?");

				query.append(" ");

				query.append("ORDER BY ");

				query.append("parentFolderId ASC, ");
				query.append("name ASC");

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				q.setLong(queryPos++, groupId);

				List list = q.list();

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, list);

				return list;
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return (List)result;
		}
	}

	public List findByGroupId(long groupId, int begin, int end)
		throws SystemException {
		return findByGroupId(groupId, begin, end, null);
	}

	public List findByGroupId(long groupId, int begin, int end,
		OrderByComparator obc) throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "findByGroupId";
		String[] finderParams = new String[] {
				Long.class.getName(),
				
				"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			};
		Object[] finderArgs = new Object[] {
				new Long(groupId),
				
				String.valueOf(begin), String.valueOf(end), String.valueOf(obc)
			};

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				query.append("groupId = ?");

				query.append(" ");

				if (obc != null) {
					query.append("ORDER BY ");
					query.append(obc.getOrderBy());
				}

				else {
					query.append("ORDER BY ");

					query.append("parentFolderId ASC, ");
					query.append("name ASC");
				}

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				q.setLong(queryPos++, groupId);

				List list = QueryUtil.list(q, getDialect(), begin, end);

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, list);

				return list;
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return (List)result;
		}
	}

	public DLFolder findByGroupId_First(long groupId, OrderByComparator obc)
		throws NoSuchFolderException, SystemException {
		List list = findByGroupId(groupId, 0, 1, obc);

		if (list.size() == 0) {
			StringMaker msg = new StringMaker();

			msg.append("No DLFolder exists with the key {");

			msg.append("groupId=" + groupId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			throw new NoSuchFolderException(msg.toString());
		}
		else {
			return (DLFolder)list.get(0);
		}
	}

	public DLFolder findByGroupId_Last(long groupId, OrderByComparator obc)
		throws NoSuchFolderException, SystemException {
		int count = countByGroupId(groupId);

		List list = findByGroupId(groupId, count - 1, count, obc);

		if (list.size() == 0) {
			StringMaker msg = new StringMaker();

			msg.append("No DLFolder exists with the key {");

			msg.append("groupId=" + groupId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			throw new NoSuchFolderException(msg.toString());
		}
		else {
			return (DLFolder)list.get(0);
		}
	}

	public DLFolder[] findByGroupId_PrevAndNext(long folderId, long groupId,
		OrderByComparator obc) throws NoSuchFolderException, SystemException {
		DLFolder dlFolder = findByPrimaryKey(folderId);

		int count = countByGroupId(groupId);

		Session session = null;

		try {
			session = openSession();

			StringMaker query = new StringMaker();

			query.append(
				"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

			query.append("groupId = ?");

			query.append(" ");

			if (obc != null) {
				query.append("ORDER BY ");
				query.append(obc.getOrderBy());
			}

			else {
				query.append("ORDER BY ");

				query.append("parentFolderId ASC, ");
				query.append("name ASC");
			}

			Query q = session.createQuery(query.toString());

			int queryPos = 0;

			q.setLong(queryPos++, groupId);

			Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, dlFolder);

			DLFolder[] array = new DLFolderImpl[3];

			array[0] = (DLFolder)objArray[0];
			array[1] = (DLFolder)objArray[1];
			array[2] = (DLFolder)objArray[2];

			return array;
		}
		catch (Exception e) {
			throw HibernateUtil.processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List findByCompanyId(long companyId) throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "findByCompanyId";
		String[] finderParams = new String[] { Long.class.getName() };
		Object[] finderArgs = new Object[] { new Long(companyId) };

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				query.append("companyId = ?");

				query.append(" ");

				query.append("ORDER BY ");

				query.append("parentFolderId ASC, ");
				query.append("name ASC");

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				q.setLong(queryPos++, companyId);

				List list = q.list();

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, list);

				return list;
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return (List)result;
		}
	}

	public List findByCompanyId(long companyId, int begin, int end)
		throws SystemException {
		return findByCompanyId(companyId, begin, end, null);
	}

	public List findByCompanyId(long companyId, int begin, int end,
		OrderByComparator obc) throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "findByCompanyId";
		String[] finderParams = new String[] {
				Long.class.getName(),
				
				"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			};
		Object[] finderArgs = new Object[] {
				new Long(companyId),
				
				String.valueOf(begin), String.valueOf(end), String.valueOf(obc)
			};

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				query.append("companyId = ?");

				query.append(" ");

				if (obc != null) {
					query.append("ORDER BY ");
					query.append(obc.getOrderBy());
				}

				else {
					query.append("ORDER BY ");

					query.append("parentFolderId ASC, ");
					query.append("name ASC");
				}

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				q.setLong(queryPos++, companyId);

				List list = QueryUtil.list(q, getDialect(), begin, end);

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, list);

				return list;
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return (List)result;
		}
	}

	public DLFolder findByCompanyId_First(long companyId, OrderByComparator obc)
		throws NoSuchFolderException, SystemException {
		List list = findByCompanyId(companyId, 0, 1, obc);

		if (list.size() == 0) {
			StringMaker msg = new StringMaker();

			msg.append("No DLFolder exists with the key {");

			msg.append("companyId=" + companyId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			throw new NoSuchFolderException(msg.toString());
		}
		else {
			return (DLFolder)list.get(0);
		}
	}

	public DLFolder findByCompanyId_Last(long companyId, OrderByComparator obc)
		throws NoSuchFolderException, SystemException {
		int count = countByCompanyId(companyId);

		List list = findByCompanyId(companyId, count - 1, count, obc);

		if (list.size() == 0) {
			StringMaker msg = new StringMaker();

			msg.append("No DLFolder exists with the key {");

			msg.append("companyId=" + companyId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			throw new NoSuchFolderException(msg.toString());
		}
		else {
			return (DLFolder)list.get(0);
		}
	}

	public DLFolder[] findByCompanyId_PrevAndNext(long folderId,
		long companyId, OrderByComparator obc)
		throws NoSuchFolderException, SystemException {
		DLFolder dlFolder = findByPrimaryKey(folderId);

		int count = countByCompanyId(companyId);

		Session session = null;

		try {
			session = openSession();

			StringMaker query = new StringMaker();

			query.append(
				"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

			query.append("companyId = ?");

			query.append(" ");

			if (obc != null) {
				query.append("ORDER BY ");
				query.append(obc.getOrderBy());
			}

			else {
				query.append("ORDER BY ");

				query.append("parentFolderId ASC, ");
				query.append("name ASC");
			}

			Query q = session.createQuery(query.toString());

			int queryPos = 0;

			q.setLong(queryPos++, companyId);

			Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, dlFolder);

			DLFolder[] array = new DLFolderImpl[3];

			array[0] = (DLFolder)objArray[0];
			array[1] = (DLFolder)objArray[1];
			array[2] = (DLFolder)objArray[2];

			return array;
		}
		catch (Exception e) {
			throw HibernateUtil.processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List findByG_P(long groupId, long parentFolderId)
		throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "findByG_P";
		String[] finderParams = new String[] {
				Long.class.getName(), Long.class.getName()
			};
		Object[] finderArgs = new Object[] {
				new Long(groupId), new Long(parentFolderId)
			};

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				query.append("groupId = ?");

				query.append(" AND ");

				query.append("parentFolderId = ?");

				query.append(" ");

				query.append("ORDER BY ");

				query.append("parentFolderId ASC, ");
				query.append("name ASC");

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				q.setLong(queryPos++, groupId);

				q.setLong(queryPos++, parentFolderId);

				List list = q.list();

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, list);

				return list;
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return (List)result;
		}
	}

	public List findByG_P(long groupId, long parentFolderId, int begin, int end)
		throws SystemException {
		return findByG_P(groupId, parentFolderId, begin, end, null);
	}

	public List findByG_P(long groupId, long parentFolderId, int begin,
		int end, OrderByComparator obc) throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "findByG_P";
		String[] finderParams = new String[] {
				Long.class.getName(), Long.class.getName(),
				
				"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			};
		Object[] finderArgs = new Object[] {
				new Long(groupId), new Long(parentFolderId),
				
				String.valueOf(begin), String.valueOf(end), String.valueOf(obc)
			};

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				query.append("groupId = ?");

				query.append(" AND ");

				query.append("parentFolderId = ?");

				query.append(" ");

				if (obc != null) {
					query.append("ORDER BY ");
					query.append(obc.getOrderBy());
				}

				else {
					query.append("ORDER BY ");

					query.append("parentFolderId ASC, ");
					query.append("name ASC");
				}

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				q.setLong(queryPos++, groupId);

				q.setLong(queryPos++, parentFolderId);

				List list = QueryUtil.list(q, getDialect(), begin, end);

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, list);

				return list;
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return (List)result;
		}
	}

	public DLFolder findByG_P_First(long groupId, long parentFolderId,
		OrderByComparator obc) throws NoSuchFolderException, SystemException {
		List list = findByG_P(groupId, parentFolderId, 0, 1, obc);

		if (list.size() == 0) {
			StringMaker msg = new StringMaker();

			msg.append("No DLFolder exists with the key {");

			msg.append("groupId=" + groupId);

			msg.append(", ");
			msg.append("parentFolderId=" + parentFolderId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			throw new NoSuchFolderException(msg.toString());
		}
		else {
			return (DLFolder)list.get(0);
		}
	}

	public DLFolder findByG_P_Last(long groupId, long parentFolderId,
		OrderByComparator obc) throws NoSuchFolderException, SystemException {
		int count = countByG_P(groupId, parentFolderId);

		List list = findByG_P(groupId, parentFolderId, count - 1, count, obc);

		if (list.size() == 0) {
			StringMaker msg = new StringMaker();

			msg.append("No DLFolder exists with the key {");

			msg.append("groupId=" + groupId);

			msg.append(", ");
			msg.append("parentFolderId=" + parentFolderId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			throw new NoSuchFolderException(msg.toString());
		}
		else {
			return (DLFolder)list.get(0);
		}
	}

	public DLFolder[] findByG_P_PrevAndNext(long folderId, long groupId,
		long parentFolderId, OrderByComparator obc)
		throws NoSuchFolderException, SystemException {
		DLFolder dlFolder = findByPrimaryKey(folderId);

		int count = countByG_P(groupId, parentFolderId);

		Session session = null;

		try {
			session = openSession();

			StringMaker query = new StringMaker();

			query.append(
				"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

			query.append("groupId = ?");

			query.append(" AND ");

			query.append("parentFolderId = ?");

			query.append(" ");

			if (obc != null) {
				query.append("ORDER BY ");
				query.append(obc.getOrderBy());
			}

			else {
				query.append("ORDER BY ");

				query.append("parentFolderId ASC, ");
				query.append("name ASC");
			}

			Query q = session.createQuery(query.toString());

			int queryPos = 0;

			q.setLong(queryPos++, groupId);

			q.setLong(queryPos++, parentFolderId);

			Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, dlFolder);

			DLFolder[] array = new DLFolderImpl[3];

			array[0] = (DLFolder)objArray[0];
			array[1] = (DLFolder)objArray[1];
			array[2] = (DLFolder)objArray[2];

			return array;
		}
		catch (Exception e) {
			throw HibernateUtil.processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List findByP_N(long parentFolderId, String name)
		throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "findByP_N";
		String[] finderParams = new String[] {
				Long.class.getName(), String.class.getName()
			};
		Object[] finderArgs = new Object[] { new Long(parentFolderId), name };

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				query.append("parentFolderId = ?");

				query.append(" AND ");

				if (name == null) {
					query.append("name IS NULL");
				}
				else {
					query.append("name = ?");
				}

				query.append(" ");

				query.append("ORDER BY ");

				query.append("parentFolderId ASC, ");
				query.append("name ASC");

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				q.setLong(queryPos++, parentFolderId);

				if (name != null) {
					q.setString(queryPos++, name);
				}

				List list = q.list();

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, list);

				return list;
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return (List)result;
		}
	}

	public List findByP_N(long parentFolderId, String name, int begin, int end)
		throws SystemException {
		return findByP_N(parentFolderId, name, begin, end, null);
	}

	public List findByP_N(long parentFolderId, String name, int begin, int end,
		OrderByComparator obc) throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "findByP_N";
		String[] finderParams = new String[] {
				Long.class.getName(), String.class.getName(),
				
				"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			};
		Object[] finderArgs = new Object[] {
				new Long(parentFolderId),
				
				name,
				
				String.valueOf(begin), String.valueOf(end), String.valueOf(obc)
			};

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				query.append("parentFolderId = ?");

				query.append(" AND ");

				if (name == null) {
					query.append("name IS NULL");
				}
				else {
					query.append("name = ?");
				}

				query.append(" ");

				if (obc != null) {
					query.append("ORDER BY ");
					query.append(obc.getOrderBy());
				}

				else {
					query.append("ORDER BY ");

					query.append("parentFolderId ASC, ");
					query.append("name ASC");
				}

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				q.setLong(queryPos++, parentFolderId);

				if (name != null) {
					q.setString(queryPos++, name);
				}

				List list = QueryUtil.list(q, getDialect(), begin, end);

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, list);

				return list;
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return (List)result;
		}
	}

	public DLFolder findByP_N_First(long parentFolderId, String name,
		OrderByComparator obc) throws NoSuchFolderException, SystemException {
		List list = findByP_N(parentFolderId, name, 0, 1, obc);

		if (list.size() == 0) {
			StringMaker msg = new StringMaker();

			msg.append("No DLFolder exists with the key {");

			msg.append("parentFolderId=" + parentFolderId);

			msg.append(", ");
			msg.append("name=" + name);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			throw new NoSuchFolderException(msg.toString());
		}
		else {
			return (DLFolder)list.get(0);
		}
	}

	public DLFolder findByP_N_Last(long parentFolderId, String name,
		OrderByComparator obc) throws NoSuchFolderException, SystemException {
		int count = countByP_N(parentFolderId, name);

		List list = findByP_N(parentFolderId, name, count - 1, count, obc);

		if (list.size() == 0) {
			StringMaker msg = new StringMaker();

			msg.append("No DLFolder exists with the key {");

			msg.append("parentFolderId=" + parentFolderId);

			msg.append(", ");
			msg.append("name=" + name);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			throw new NoSuchFolderException(msg.toString());
		}
		else {
			return (DLFolder)list.get(0);
		}
	}

	public DLFolder[] findByP_N_PrevAndNext(long folderId, long parentFolderId,
		String name, OrderByComparator obc)
		throws NoSuchFolderException, SystemException {
		DLFolder dlFolder = findByPrimaryKey(folderId);

		int count = countByP_N(parentFolderId, name);

		Session session = null;

		try {
			session = openSession();

			StringMaker query = new StringMaker();

			query.append(
				"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

			query.append("parentFolderId = ?");

			query.append(" AND ");

			if (name == null) {
				query.append("name IS NULL");
			}
			else {
				query.append("name = ?");
			}

			query.append(" ");

			if (obc != null) {
				query.append("ORDER BY ");
				query.append(obc.getOrderBy());
			}

			else {
				query.append("ORDER BY ");

				query.append("parentFolderId ASC, ");
				query.append("name ASC");
			}

			Query q = session.createQuery(query.toString());

			int queryPos = 0;

			q.setLong(queryPos++, parentFolderId);

			if (name != null) {
				q.setString(queryPos++, name);
			}

			Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, dlFolder);

			DLFolder[] array = new DLFolderImpl[3];

			array[0] = (DLFolder)objArray[0];
			array[1] = (DLFolder)objArray[1];
			array[2] = (DLFolder)objArray[2];

			return array;
		}
		catch (Exception e) {
			throw HibernateUtil.processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public DLFolder findByG_P_N(long groupId, long parentFolderId, String name)
		throws NoSuchFolderException, SystemException {
		DLFolder dlFolder = fetchByG_P_N(groupId, parentFolderId, name);

		if (dlFolder == null) {
			StringMaker msg = new StringMaker();

			msg.append("No DLFolder exists with the key {");

			msg.append("groupId=" + groupId);

			msg.append(", ");
			msg.append("parentFolderId=" + parentFolderId);

			msg.append(", ");
			msg.append("name=" + name);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchFolderException(msg.toString());
		}

		return dlFolder;
	}

	public DLFolder fetchByG_P_N(long groupId, long parentFolderId, String name)
		throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "fetchByG_P_N";
		String[] finderParams = new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName()
			};
		Object[] finderArgs = new Object[] {
				new Long(groupId), new Long(parentFolderId),
				
				name
			};

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				query.append("groupId = ?");

				query.append(" AND ");

				query.append("parentFolderId = ?");

				query.append(" AND ");

				if (name == null) {
					query.append("name IS NULL");
				}
				else {
					query.append("name = ?");
				}

				query.append(" ");

				query.append("ORDER BY ");

				query.append("parentFolderId ASC, ");
				query.append("name ASC");

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				q.setLong(queryPos++, groupId);

				q.setLong(queryPos++, parentFolderId);

				if (name != null) {
					q.setString(queryPos++, name);
				}

				List list = q.list();

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, list);

				if (list.size() == 0) {
					return null;
				}
				else {
					return (DLFolder)list.get(0);
				}
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			List list = (List)result;

			if (list.size() == 0) {
				return null;
			}
			else {
				return (DLFolder)list.get(0);
			}
		}
	}

	public List findWithDynamicQuery(DynamicQueryInitializer queryInitializer)
		throws SystemException {
		Session session = null;

		try {
			session = openSession();

			DynamicQuery query = queryInitializer.initialize(session);

			return query.list();
		}
		catch (Exception e) {
			throw HibernateUtil.processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List findWithDynamicQuery(DynamicQueryInitializer queryInitializer,
		int begin, int end) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			DynamicQuery query = queryInitializer.initialize(session);

			query.setLimit(begin, end);

			return query.list();
		}
		catch (Exception e) {
			throw HibernateUtil.processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	public List findAll(int begin, int end) throws SystemException {
		return findAll(begin, end, null);
	}

	public List findAll(int begin, int end, OrderByComparator obc)
		throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "findAll";
		String[] finderParams = new String[] {
				"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			};
		Object[] finderArgs = new Object[] {
				String.valueOf(begin), String.valueOf(end), String.valueOf(obc)
			};

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder ");

				if (obc != null) {
					query.append("ORDER BY ");
					query.append(obc.getOrderBy());
				}

				else {
					query.append("ORDER BY ");

					query.append("parentFolderId ASC, ");
					query.append("name ASC");
				}

				Query q = session.createQuery(query.toString());

				List list = QueryUtil.list(q, getDialect(), begin, end);

				if (obc == null) {
					Collections.sort(list);
				}

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, list);

				return list;
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return (List)result;
		}
	}

	public void removeByUuid(String uuid) throws SystemException {
		Iterator itr = findByUuid(uuid).iterator();

		while (itr.hasNext()) {
			DLFolder dlFolder = (DLFolder)itr.next();

			remove(dlFolder);
		}
	}

	public void removeByUUID_G(String uuid, long groupId)
		throws NoSuchFolderException, SystemException {
		DLFolder dlFolder = findByUUID_G(uuid, groupId);

		remove(dlFolder);
	}

	public void removeByGroupId(long groupId) throws SystemException {
		Iterator itr = findByGroupId(groupId).iterator();

		while (itr.hasNext()) {
			DLFolder dlFolder = (DLFolder)itr.next();

			remove(dlFolder);
		}
	}

	public void removeByCompanyId(long companyId) throws SystemException {
		Iterator itr = findByCompanyId(companyId).iterator();

		while (itr.hasNext()) {
			DLFolder dlFolder = (DLFolder)itr.next();

			remove(dlFolder);
		}
	}

	public void removeByG_P(long groupId, long parentFolderId)
		throws SystemException {
		Iterator itr = findByG_P(groupId, parentFolderId).iterator();

		while (itr.hasNext()) {
			DLFolder dlFolder = (DLFolder)itr.next();

			remove(dlFolder);
		}
	}

	public void removeByP_N(long parentFolderId, String name)
		throws SystemException {
		Iterator itr = findByP_N(parentFolderId, name).iterator();

		while (itr.hasNext()) {
			DLFolder dlFolder = (DLFolder)itr.next();

			remove(dlFolder);
		}
	}

	public void removeByG_P_N(long groupId, long parentFolderId, String name)
		throws NoSuchFolderException, SystemException {
		DLFolder dlFolder = findByG_P_N(groupId, parentFolderId, name);

		remove(dlFolder);
	}

	public void removeAll() throws SystemException {
		Iterator itr = findAll().iterator();

		while (itr.hasNext()) {
			remove((DLFolder)itr.next());
		}
	}

	public int countByUuid(String uuid) throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "countByUuid";
		String[] finderParams = new String[] { String.class.getName() };
		Object[] finderArgs = new Object[] { uuid };

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append("SELECT COUNT(*) ");
				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				if (uuid == null) {
					query.append("uuid_ IS NULL");
				}
				else {
					query.append("uuid_ = ?");
				}

				query.append(" ");

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				if (uuid != null) {
					q.setString(queryPos++, uuid);
				}

				Long count = null;

				Iterator itr = q.list().iterator();

				if (itr.hasNext()) {
					count = (Long)itr.next();
				}

				if (count == null) {
					count = new Long(0);
				}

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, count);

				return count.intValue();
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return ((Long)result).intValue();
		}
	}

	public int countByUUID_G(String uuid, long groupId)
		throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "countByUUID_G";
		String[] finderParams = new String[] {
				String.class.getName(), Long.class.getName()
			};
		Object[] finderArgs = new Object[] { uuid, new Long(groupId) };

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append("SELECT COUNT(*) ");
				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				if (uuid == null) {
					query.append("uuid_ IS NULL");
				}
				else {
					query.append("uuid_ = ?");
				}

				query.append(" AND ");

				query.append("groupId = ?");

				query.append(" ");

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				if (uuid != null) {
					q.setString(queryPos++, uuid);
				}

				q.setLong(queryPos++, groupId);

				Long count = null;

				Iterator itr = q.list().iterator();

				if (itr.hasNext()) {
					count = (Long)itr.next();
				}

				if (count == null) {
					count = new Long(0);
				}

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, count);

				return count.intValue();
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return ((Long)result).intValue();
		}
	}

	public int countByGroupId(long groupId) throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "countByGroupId";
		String[] finderParams = new String[] { Long.class.getName() };
		Object[] finderArgs = new Object[] { new Long(groupId) };

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append("SELECT COUNT(*) ");
				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				query.append("groupId = ?");

				query.append(" ");

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				q.setLong(queryPos++, groupId);

				Long count = null;

				Iterator itr = q.list().iterator();

				if (itr.hasNext()) {
					count = (Long)itr.next();
				}

				if (count == null) {
					count = new Long(0);
				}

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, count);

				return count.intValue();
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return ((Long)result).intValue();
		}
	}

	public int countByCompanyId(long companyId) throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "countByCompanyId";
		String[] finderParams = new String[] { Long.class.getName() };
		Object[] finderArgs = new Object[] { new Long(companyId) };

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append("SELECT COUNT(*) ");
				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				query.append("companyId = ?");

				query.append(" ");

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				q.setLong(queryPos++, companyId);

				Long count = null;

				Iterator itr = q.list().iterator();

				if (itr.hasNext()) {
					count = (Long)itr.next();
				}

				if (count == null) {
					count = new Long(0);
				}

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, count);

				return count.intValue();
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return ((Long)result).intValue();
		}
	}

	public int countByG_P(long groupId, long parentFolderId)
		throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "countByG_P";
		String[] finderParams = new String[] {
				Long.class.getName(), Long.class.getName()
			};
		Object[] finderArgs = new Object[] {
				new Long(groupId), new Long(parentFolderId)
			};

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append("SELECT COUNT(*) ");
				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				query.append("groupId = ?");

				query.append(" AND ");

				query.append("parentFolderId = ?");

				query.append(" ");

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				q.setLong(queryPos++, groupId);

				q.setLong(queryPos++, parentFolderId);

				Long count = null;

				Iterator itr = q.list().iterator();

				if (itr.hasNext()) {
					count = (Long)itr.next();
				}

				if (count == null) {
					count = new Long(0);
				}

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, count);

				return count.intValue();
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return ((Long)result).intValue();
		}
	}

	public int countByP_N(long parentFolderId, String name)
		throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "countByP_N";
		String[] finderParams = new String[] {
				Long.class.getName(), String.class.getName()
			};
		Object[] finderArgs = new Object[] { new Long(parentFolderId), name };

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append("SELECT COUNT(*) ");
				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				query.append("parentFolderId = ?");

				query.append(" AND ");

				if (name == null) {
					query.append("name IS NULL");
				}
				else {
					query.append("name = ?");
				}

				query.append(" ");

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				q.setLong(queryPos++, parentFolderId);

				if (name != null) {
					q.setString(queryPos++, name);
				}

				Long count = null;

				Iterator itr = q.list().iterator();

				if (itr.hasNext()) {
					count = (Long)itr.next();
				}

				if (count == null) {
					count = new Long(0);
				}

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, count);

				return count.intValue();
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return ((Long)result).intValue();
		}
	}

	public int countByG_P_N(long groupId, long parentFolderId, String name)
		throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "countByG_P_N";
		String[] finderParams = new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName()
			};
		Object[] finderArgs = new Object[] {
				new Long(groupId), new Long(parentFolderId),
				
				name
			};

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringMaker query = new StringMaker();

				query.append("SELECT COUNT(*) ");
				query.append(
					"FROM com.liferay.portlet.documentlibrary.model.DLFolder WHERE ");

				query.append("groupId = ?");

				query.append(" AND ");

				query.append("parentFolderId = ?");

				query.append(" AND ");

				if (name == null) {
					query.append("name IS NULL");
				}
				else {
					query.append("name = ?");
				}

				query.append(" ");

				Query q = session.createQuery(query.toString());

				int queryPos = 0;

				q.setLong(queryPos++, groupId);

				q.setLong(queryPos++, parentFolderId);

				if (name != null) {
					q.setString(queryPos++, name);
				}

				Long count = null;

				Iterator itr = q.list().iterator();

				if (itr.hasNext()) {
					count = (Long)itr.next();
				}

				if (count == null) {
					count = new Long(0);
				}

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, count);

				return count.intValue();
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return ((Long)result).intValue();
		}
	}

	public int countAll() throws SystemException {
		boolean finderClassNameCacheEnabled = DLFolderModelImpl.CACHE_ENABLED;
		String finderClassName = DLFolder.class.getName();
		String finderMethodName = "countAll";
		String[] finderParams = new String[] {  };
		Object[] finderArgs = new Object[] {  };

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCache.getResult(finderClassName, finderMethodName,
					finderParams, finderArgs, getSessionFactory());
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(
						"SELECT COUNT(*) FROM com.liferay.portlet.documentlibrary.model.DLFolder");

				Long count = null;

				Iterator itr = q.list().iterator();

				if (itr.hasNext()) {
					count = (Long)itr.next();
				}

				if (count == null) {
					count = new Long(0);
				}

				FinderCache.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, count);

				return count.intValue();
			}
			catch (Exception e) {
				throw HibernateUtil.processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return ((Long)result).intValue();
		}
	}

	protected void initDao() {
	}

	private static ModelListener _getListener() {
		if (Validator.isNotNull(_LISTENER)) {
			try {
				return (ModelListener)Class.forName(_LISTENER).newInstance();
			}
			catch (Exception e) {
				_log.error(e);
			}
		}

		return null;
	}

	private static final String _LISTENER = GetterUtil.getString(PropsUtil.get(
				"value.object.listener.com.liferay.portlet.documentlibrary.model.DLFolder"));
	private static Log _log = LogFactory.getLog(DLFolderPersistenceImpl.class);
}