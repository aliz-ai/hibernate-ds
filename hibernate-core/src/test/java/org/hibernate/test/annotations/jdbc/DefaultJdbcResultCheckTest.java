/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2011, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.test.annotations.jdbc;

import static org.junit.Assert.*;

import org.hibernate.annotations.DefaultJdbcResultCheck;
import org.hibernate.engine.spi.ExecuteUpdateResultCheckStyle;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.EntityPersisterAssert;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

/**
 * Tests functionality of {@link DefaultJdbcResultCheck}.
 *
 * @author lgathy
 */
public class DefaultJdbcResultCheckTest extends BaseCoreFunctionalTestCase {

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { NormalResultCheck.class, OverriddenResultCheck.class };
	}

	@Test
	public void testNormalResultCheck() throws Exception {
		persisterAssert( NormalResultCheck.class ).assertAllResultCheckStyleEqual( ExecuteUpdateResultCheckStyle.COUNT );
	}

	@Test
	public void testOverriddenResultCheck() throws Exception {
		persisterAssert( OverriddenResultCheck.class ).assertAllResultCheckStyleEqual( ExecuteUpdateResultCheckStyle.NONE );
	}

	private EntityPersisterAssert persisterAssert(final Class entityClass) {
		EntityPersister persister = sessionFactory().getEntityPersister( entityClass.getName() );
		EntityPersisterAssert persisterAssert = new EntityPersisterAssert( persister );
		return persisterAssert;
	}

}
