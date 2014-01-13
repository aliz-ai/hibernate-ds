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

import java.util.Map;

import org.hibernate.boot.registry.StandardServiceInitiator;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.hibernate.engine.jdbc.internal.JdbcServicesImpl;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.jdbc.spi.SqlStatementLogger;
import org.hibernate.service.spi.ServiceRegistryImplementor;

/**
 * @author lgathy
 */
final class JdbcStatementCounter extends JdbcServicesImpl {

	private int statementCount;
	private SqlStatementCounter sqlStatementCounter;
	private final StandardServiceInitiator<JdbcServices> initiator;

	public JdbcStatementCounter() {
		super();
		resetStatementCounter();
		initiator = new Initiator();
	}
	
	@Override
	public void configure(Map configValues) {
		super.configure( configValues );
		SqlStatementLogger sqlStatementLogger = super.getSqlStatementLogger();
		sqlStatementCounter = new SqlStatementCounter( sqlStatementLogger.isLogToStdout(), sqlStatementLogger.isFormat() );
	}

	public int getStatementCount() {
		return statementCount;
	}

	public void resetStatementCounter() {
		statementCount = 0;
	}

	@Override
	public SqlStatementLogger getSqlStatementLogger() {
		return sqlStatementCounter;
	}
	
	public StandardServiceInitiator<JdbcServices> getInitiator() {
		return initiator;
	}

	private class SqlStatementCounter extends SqlStatementLogger {

		public SqlStatementCounter() {
			super();
		}

		public SqlStatementCounter(boolean logToStdout, boolean format) {
			super( logToStdout, format );
		}

		@Override
		public void logStatement(String statement, Formatter formatter) {
			++statementCount;
			super.logStatement( statement, formatter );
		}
		
	}
	
	private class Initiator implements StandardServiceInitiator<JdbcServices> {
		
		@Override
		public Class<JdbcServices> getServiceInitiated() {
			return JdbcServices.class;
		}

		@Override
		public JdbcServices initiateService(Map configurationValues, ServiceRegistryImplementor registry) {
			return JdbcStatementCounter.this;
		}

	}

}
