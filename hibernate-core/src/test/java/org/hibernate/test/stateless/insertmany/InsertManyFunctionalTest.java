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
package org.hibernate.test.stateless.insertmany;

import junit.framework.Assert;

import org.hibernate.StatelessSession;
import org.junit.Test;

/**
 * @author lgathy
 */
public final class InsertManyFunctionalTest extends AbstractInsertManyTestCase {

	@Test
	public void testInsertManyZero() {
		session().insertMany( RECORD, 10, generateRecords( 0 ) );
		Assert.assertEquals( 0, jdbcStatementCounter().getStatementCount() );
	}

	@Test
	public void testInsertManyOne() {
		session().insertMany( RECORD, 10, generateRecords( 1 ) );
		Assert.assertEquals( 1, jdbcStatementCounter().getStatementCount() );
	}

	@Test
	public void testInsertManyLessThanLimit() {
		session().insertMany( RECORD, 10, generateRecords( 9 ) );
		Assert.assertEquals( 1, jdbcStatementCounter().getStatementCount() );
	}

	@Test
	public void testInsertManyExactlyTheLimit() {
		session().insertMany( RECORD, 10, generateRecords( 10 ) );
		Assert.assertEquals( 1, jdbcStatementCounter().getStatementCount() );
	}

	@Test
	public void testInsertManyMoreThanLimit() {
		session().insertMany( RECORD, 10, generateRecords( 11 ) );
		Assert.assertEquals( 2, jdbcStatementCounter().getStatementCount() );
	}
	
	@Test
	public void testInsertManyManyStatements() {
		session().insertMany( RECORD, 10, generateRecords( 50 ) );
		Assert.assertEquals( 5, jdbcStatementCounter().getStatementCount() );
	}
	
//	@Test
//	public void testInsertManyLargeBatch() {
//		session().insertMany( RECORD, 1000, generateRecords( 2455 ) );
//		Assert.assertEquals( 3, jdbcStatementCounter().getStatementCount() );
//	}

}
