/*
 * SalaryValidator.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.datatypes.Money;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class SalaryValidator extends AbstractValidator<ValidSalary, Money> {

	private static final double	MIN	= 0.00;
	private static final double	MAX	= 1000000.00;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidSalary annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Money money, final ConstraintValidatorContext context) {
		if (money == null)
			return true;

		double amount = money.getAmount();
		return amount >= SalaryValidator.MIN && amount <= SalaryValidator.MAX;
	}

}
