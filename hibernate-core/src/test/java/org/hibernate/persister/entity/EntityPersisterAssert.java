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
package org.hibernate.persister.entity;

import org.hibernate.engine.spi.ExecuteUpdateResultCheckStyle;
import org.junit.Assert;

/**
 * This class is needed here in this package to access the protected fields of {@link AbstractEntityPersister}.
 *
 * @author lgathy
 */
public final class EntityPersisterAssert {

	private final AbstractEntityPersister entityPersister;
	private final String entityName;

	public EntityPersisterAssert(final EntityPersister entityPersister) {
		super();
		this.entityPersister = (AbstractEntityPersister) entityPersister;
		this.entityName = entityPersister.getEntityName();
	}

	public EntityPersisterAssert assertAllResultCheckStyleEqual(final ExecuteUpdateResultCheckStyle expected) {
		assertDmlResultCheckStyleEquals( DmlType.INSERT, expected );
		assertDmlResultCheckStyleEquals( DmlType.UPDATE, expected );
		assertDmlResultCheckStyleEquals( DmlType.DELETE, expected );
		return this;
	}

	private void assertDmlResultCheckStyleEquals(final DmlType dml, final ExecuteUpdateResultCheckStyle expected) {
		final String dmlName = dml.name();
		final ExecuteUpdateResultCheckStyle[] styles = getStyles( dml );

		final int ln = styles.length;
		for ( int i = 0; i < ln; ++i ) {
			Assert.assertEquals( dmlName + " ResultCheckStyle for entity: " + entityName, expected, styles[i] );
		}
	}

	public static enum DmlType {
		INSERT, UPDATE, DELETE
	}

	private ExecuteUpdateResultCheckStyle[] getStyles(final DmlType dml) {
		switch ( dml ) {
			case INSERT:
				return entityPersister.insertResultCheckStyles;
			case UPDATE:
				return entityPersister.updateResultCheckStyles;
			case DELETE:
				return entityPersister.deleteResultCheckStyles;
		}
		throw new IllegalArgumentException( "Invalid DML: " + dml );
	}

}
