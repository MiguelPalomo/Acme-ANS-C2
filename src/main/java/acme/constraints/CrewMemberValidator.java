
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.crewMember.CrewMember;
import acme.realms.crewMember.CrewMemberRepository;

@Validator
public class CrewMemberValidator extends AbstractValidator<ValidCrewMember, CrewMember> {

	@Autowired
	private CrewMemberRepository repository;


	@Override
	protected void initialise(final ValidCrewMember annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final CrewMember crewMember, final ConstraintValidatorContext context) {
		assert context != null;

		if (crewMember == null || crewMember.getUserAccount() == null || crewMember.getEmployeeCode() == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			return false;
		}

		final String code = crewMember.getEmployeeCode().trim().toUpperCase();

		final String name = crewMember.getUserAccount().getIdentity().getName().trim();
		final String surname = crewMember.getUserAccount().getIdentity().getSurname().trim().replaceAll("\\s+", " ");
		final String[] surnameParts = surname.trim().split(" +");

		final String initials2 = name.substring(0, 1).toUpperCase() + surnameParts[0].substring(0, 1).toUpperCase();
		String initials3 = null;

		if (surnameParts.length >= 2)
			initials3 = initials2 + surnameParts[1].substring(0, 1).toUpperCase();

		boolean codeStartsCorrectly;
		if (surnameParts.length == 1)
			codeStartsCorrectly = code.startsWith(initials2);
		else
			codeStartsCorrectly = code.startsWith(initials2) || code.startsWith(initials3);

		final boolean matchesPattern = code.matches("^([A-Z]{2,3})(\\d{6})$");

		final CrewMember sameCode = this.repository.findMemberSameCode(code);
		final boolean uniqueCode = sameCode == null || sameCode.equals(crewMember);

		super.state(context, codeStartsCorrectly, "employeeCode", "validation.CrewMember.codePattern");
		super.state(context, matchesPattern, "employeeCode", "validation.CrewMember.codePattern");
		super.state(context, uniqueCode, "employeeCode", "validation.CrewMember.codeNotUnique");

		return !super.hasErrors(context);
	}

}
