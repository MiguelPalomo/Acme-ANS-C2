
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.flightassignment.ActivityLog;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	// Internal state ---------------------------------------------------------------------

	// ConstraintValidator interface ------------------------------------------------------

	@Override
	protected void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ActivityLog activityLog, final ConstraintValidatorContext context) {

		assert context != null;
		boolean result;

		if (activityLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean fechaTrasLeg;

			fechaTrasLeg = MomentHelper.isAfterOrEqual(activityLog.getRegistrationMoment(), activityLog.getFlightAssignment().getLeg().getScheduledDeparture());
			super.state(context, fechaTrasLeg, "fechaActivityLog", "acme.validation.activityLog.beforeLeg");

			boolean isLegPublished = activityLog.getFlightAssignment().getLeg().isDraftMode();
			super.state(context, !isLegPublished, "leg", "acme.validation.flightAssignment.legNotPublished");

			boolean hasLegStarted = activityLog.getFlightAssignment().getLeg().getScheduledDeparture().before(MomentHelper.getCurrentMoment());
			super.state(context, hasLegStarted, "leg", "acme.validation.activityLog.leg.not-finished");
		}
		result = !super.hasErrors(context);

		return result;
	}
}
