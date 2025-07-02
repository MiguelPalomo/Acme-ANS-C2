
package acme.features.crewmember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightassignment.ActivityLog;
import acme.entities.flightassignment.FlightAssignment;
import acme.features.crewmember.flightAssignment.CrewMemberFlightAssignmentRepository;
import acme.realms.crewMember.CrewMember;

@GuiService
public class CrewMemberActivityLogListService extends AbstractGuiService<CrewMember, ActivityLog> {

	@Autowired
	private CrewMemberActivityLogRepository			repository;

	@Autowired
	private CrewMemberFlightAssignmentRepository	flightAssignmentRepository;


	@Override
	public void authorise() {
		int assignmentId = super.getRequest().getData("assignmentId", int.class);
		FlightAssignment flightAssignment = this.flightAssignmentRepository.findFlightAssignmentById(assignmentId);

		boolean status = flightAssignment != null && !flightAssignment.getDraftMode() && super.getRequest().getPrincipal().hasRealm(flightAssignment.getCrewMember())
			&& flightAssignment.getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int assignmentId = super.getRequest().getData("assignmentId", int.class);
		Collection<ActivityLog> activityLogs = this.repository.findActivityLogsByFlightAssignmentId(assignmentId);
		super.getBuffer().addData(activityLogs);

		FlightAssignment fa = this.flightAssignmentRepository.findFlightAssignmentById(assignmentId);
		if (fa.getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment()))
			super.getResponse().addGlobal("showAction", true);

		boolean draftModeFlightAssignment = fa.getDraftMode();
		super.getResponse().addGlobal("draftModeFlightAssignment", draftModeFlightAssignment);

		super.getResponse().addGlobal("assignmentId", assignmentId);
	}

	@Override
	public void unbind(final ActivityLog object) {
		Dataset dataset = super.unbindObject(object, "registrationMoment", "incidentType", "description", "severityLevel", "draftMode");
		super.getResponse().addData(dataset);
	}

}
