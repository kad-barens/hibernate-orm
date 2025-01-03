/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.annotations;

import org.hibernate.jdbc.Expectation;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies a custom SQL DML statement to be used in place of the default SQL
 * generated by Hibernate when an entity or collection row is inserted in the
 * database.
 * <p>
 * The given {@linkplain #sql SQL statement} must have exactly the number of JDBC
 * {@code ?} parameters that Hibernate expects, that is, one for each column
 * mapped by the entity, in the exact order Hibernate expects. In particular,
 * the {@linkplain jakarta.persistence.Id primary key} columns must come last.
 * <p>
 * If a column should <em>not</em> be written as part of the insert statement,
 * and has no corresponding JDBC parameter in the custom SQL, it must be mapped
 * using {@link jakarta.persistence.Column#insertable insertable=false}.
 * <p>
 * A custom SQL insert statement might assign a value to a mapped column as it
 * is written. In this case, the corresponding property of the entity remains
 * unassigned after the insert is executed unless
 * {@link Generated @Generated} is specified, forcing Hibernate to reread the
 * state of the entity after each insert.
 * <p>
 * Similarly, a custom insert statement might transform a mapped column value
 * as it is written. In this case, the state of the entity held in memory
 * loses synchronization with the database after the insert is executed unless
 * {@link Generated @Generated(writable=true)} is specified, again forcing
 * Hibernate to reread the state of the entity after each insert.
 * <p>
 * If an entity has {@linkplain jakarta.persistence.SecondaryTable secondary
 * tables}, it may have a {@code @SQLInsert} annotation for each secondary table.
 * The {@link #table} member must specify the name of the secondary table.
 *
 * @author Laszlo Benke
 */
@Target({TYPE, FIELD, METHOD})
@Retention(RUNTIME)
@Repeatable(SQLInserts.class)
public @interface SQLInsert {
	/**
	 * Procedure name or SQL {@code INSERT} statement.
	 */
	String sql();

	/**
	 * Is the statement callable (aka a {@link java.sql.CallableStatement})?
	 */
	boolean callable() default false;

	/**
	 * An {@link Expectation} class used to verify that the operation was successful.
	 *
	 * @see Expectation.None
	 * @see Expectation.RowCount
	 * @see Expectation.OutParameter
	 */
	Class<? extends Expectation> verify() default Expectation.class;

	/**
	 * A {@link ResultCheckStyle} used to verify that the operation was successful.
	 *
	 * @deprecated use {@link #verify()} with an {@link Expectation} class
	 */
	@Deprecated(since = "6.5", forRemoval = true)
	ResultCheckStyle check() default ResultCheckStyle.NONE;

	/**
	 * The name of the table affected by the insert statement. Required when the
	 * statement affects a {@linkplain jakarta.persistence.SecondaryTable secondary
	 * table} of an entity. Not required for collections nor when the insert statement
	 * affects the primary table of an entity.
	 *
	 * @return the name of the secondary table
	 *
	 * @since 6.2
	 */
	String table() default "";
}
