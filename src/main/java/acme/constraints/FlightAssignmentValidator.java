
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flightassignment.FlightAssignment;
import acme.features.crewmember.flightAssignment.CrewMemberFlightAssignmentRepository;

@Validator
public class FlightAssignmentValidator extends AbstractValidator<ValidFlightAssignment, FlightAssignment> {

	// Internal State ----------------------------------------------------

	@Autowired
	private CrewMemberFlightAssignmentRepository repository;

	// Initialiser ------------------------------------------------------------


	@Override
	public void initialise(final ValidFlightAssignment annotation) {
		assert annotation != null;
	}

	// AbstractValidator interface --------------------------------------------

	@Override
	public boolean isValid(final FlightAssignment flightAssignment, final ConstraintValidatorContext context) {
		assert context != null;

		if (flightAssignment.getLeg() != null) {
			if (flightAssignment.getDuty() != null) {
				boolean isDutyAlreadyAssigned = this.repository.hasDutyAssignedExcludingSelf(flightAssignment.getLeg(), flightAssignment.getDuty(), flightAssignment.getId()) && flightAssignment.getDraftMode() == false;
				super.state(context, !isDutyAlreadyAssigned, "duty", "acme.validation.flightAssignment.duty");
			}

			boolean overlaps = this.repository.isOverlappingAssignmentExcludingSelf(flightAssignment.getCrewMember(), flightAssignment.getLeg().getScheduledDeparture(), flightAssignment.getLeg().getScheduledArrival(), flightAssignment.getId())
				&& flightAssignment.getDraftMode() == false;
			super.state(context, !overlaps, "leg", "acme.validation.flightAssignment.crewMember.multipleLegs");

			boolean isLegDraft = flightAssignment.getLeg().isDraftMode();
			super.state(context, !isLegDraft, "leg", "acme.validation.flightAssignment.legNotPublished");
		}

		return !super.hasErrors(context);
	}

}
