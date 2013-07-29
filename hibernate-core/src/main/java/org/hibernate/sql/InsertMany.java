/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2013, Red Hat Inc. or third-party contributors as
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
package org.hibernate.sql;

import org.hibernate.dialect.Dialect;

/**
 * An SQL <code>INSERT</code> statement with multiple rows.
 * 
 * @author lgathy
 */
public final class InsertMany extends Insert {

	private int valuesCount;

	public InsertMany(Dialect dialect, int valuesCount) {
		super( dialect );
		if ( valuesCount <= 0 ) {
			throw new IllegalArgumentException( "valuesCount must be positive: " + valuesCount );
		}
		this.valuesCount = valuesCount;
	}

	public int getValuesCount() {
		return valuesCount;
	}

	@Override
	public String toStatementString() {
		String insert = super.toStatementString();
		if ( valuesCount == 1 ) {
			return insert;
		}
		String values = ") values (";
		int iValues = insert.indexOf( values );
		if ( iValues < 0 ) {
			throw invalidInsert( insert );
		}
		else {
			int j = iValues + values.length();
			String row = ",\n (" + insert.substring( j );
			if ( row.contains( values ) ) {
				throw invalidInsert( insert );
			}
			int capacity = insert.length() + ( valuesCount - 1 ) * row.length();
			StringBuilder buf = new StringBuilder( capacity ).append( insert );
			for ( int i = 1; i < valuesCount; ++i ) {
				buf.append( row );
			}
			return buf.toString();
		}
	}

	private IllegalArgumentException invalidInsert(String sql) {
		return new IllegalArgumentException( "Invalid SQL statement: " + sql );
	}

}
