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

import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.stat.Statistics;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * @author lgathy
 */
public abstract class AbstractInsertManyTestCase extends BaseCoreFunctionalTestCase {

	protected static final String RECORD = Record.class.getName();

	private StatelessSession session;
	private JdbcStatementCounter jdbcStatementCounter;

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { Record.class };
	}

	@Override
	protected void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( Environment.FORMAT_SQL, "false" );
	}

	@Override
	protected void prepareBasicRegistryBuilder(StandardServiceRegistryBuilder serviceRegistryBuilder) {
		super.prepareBasicRegistryBuilder( serviceRegistryBuilder );
		jdbcStatementCounter = new JdbcStatementCounter();
		serviceRegistryBuilder.addInitiator( jdbcStatementCounter.getInitiator() );
	}

	@Override
	protected void prepareTest() throws Exception {
		super.prepareTest();
		session = sessionFactory().openStatelessSession();
		session.beginTransaction();
		jdbcStatementCounter.resetStatementCounter();
	}

	@Override
	protected void cleanupTest() throws Exception {
		if ( session != null ) {
			Transaction tx = session.getTransaction();
			if ( tx != null ) {
				try {
					if ( isRollbackTransactions() ) {
						tx.rollback();
					}
					else {
						tx.commit();
					}
				}
				catch (RuntimeException e) {
					// nothing to do
				}
			}
			try {
				session.close();
			}
			catch (RuntimeException e) {
				// nothing to do
			}
			finally {
				session = null;
			}
		}
		super.cleanupTest();
	}

	protected boolean isRollbackTransactions() {
		return true;
	}

	protected final Record[] generateRecords(int count) {
		Record[] records = new Record[count];
		for ( int i = 0; i < count; ++i ) {
			records[i] = new Record( new Long( i ), "0123456789abcdefghijklmnopqrstuvwxyz" );
		}
		return records;
	}

	protected final StatelessSession session() {
		return session;
	}

	protected final JdbcStatementCounter jdbcStatementCounter() {
		return jdbcStatementCounter;
	}

}
