
package acme.realms.crewMember;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.constraints.ValidCrewMember;
import acme.constraints.ValidLongText;
import acme.constraints.ValidPhoneNumber;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidCrewMember
@Table(indexes = {
	@Index(columnList = "availabilityStatus")
})
public class CrewMember extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Column(unique = true)
	private String				employeeCode;

	@Mandatory
	@ValidPhoneNumber
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidLongText
	@Automapped
	private String				languageSkills;

	@Mandatory
	@Valid
	@Automapped
	private AvailabilityStatus	availabilityStatus;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				salary;

	@Optional
	@ValidNumber(min = 0, max = 120, message = "{acme.validation.years-Of-Experience}")
	@Automapped
	private int					yearsOfExperience;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

}
