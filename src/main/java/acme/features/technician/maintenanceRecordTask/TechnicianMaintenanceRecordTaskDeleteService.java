
package acme.features.technician.maintenanceRecordTask;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenancerecord.MaintenanceRecordTask;
import acme.entities.maintenancerecord.Task;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianMaintenanceRecordTaskDeleteService extends AbstractGuiService<Technician, MaintenanceRecordTask> {

	@Autowired
	private TechnicianMaintenanceRecordTaskRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int maintenanceRecordTaskId;
		MaintenanceRecordTask maintenanceRecordTask;

		maintenanceRecordTaskId = super.getRequest().getData("id", int.class);
		maintenanceRecordTask = this.repository.findOneMaintenanceRecordTaskById(maintenanceRecordTaskId);
		status = maintenanceRecordTask != null && maintenanceRecordTask.getMaintenanceRecord().isDraftMode() && super.getRequest().getPrincipal().hasRealm(maintenanceRecordTask.getMaintenanceRecord().getTechnician());

		//		if (super.getRequest().hasData("id")) {
		//			Integer taskId = super.getRequest().getData("task", Integer.class);
		//			if (taskId == null || taskId != 0) {
		//				Task task = this.repository.findOneTaskById(taskId);
		//				status = task != null && this.repository.findOneMaintenanceRecordTaskByMaintenanceRecordAndTaskId(masterId, taskId) == null;
		//			}
		//		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecordTask objects;
		int id;

		id = super.getRequest().getData("id", int.class);
		objects = this.repository.findOneMaintenanceRecordTaskById(id);

		super.getBuffer().addData(objects);
	}

	@Override
	public void bind(final MaintenanceRecordTask object) {
		assert object != null;

		super.bindObject(object, "version");
	}

	@Override
	public void validate(final MaintenanceRecordTask object) {
		assert object != null;
	}

	@Override
	public void perform(final MaintenanceRecordTask object) {
		assert object != null;
		this.repository.delete(object);
	}

	@Override
	public void unbind(final MaintenanceRecordTask object) {
		assert object != null;
		Dataset dataset;
		SelectChoices choicesTask;
		Collection<Task> tasks;

		tasks = this.repository.findManyValidTasks();
		choicesTask = SelectChoices.from(tasks, "description", object.getTask());
		dataset = super.unbindObject(object, "version");

		dataset.put("tasks", choicesTask);
		dataset.put("task", choicesTask.getSelected().getKey());
		super.getResponse().addData(dataset);
	}

}
