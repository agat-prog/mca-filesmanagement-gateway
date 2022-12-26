package mca.filesmanagement.apigateway.files;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ProcessResponse {

	private Long id;
	private String code;
	private boolean active;
	private Date date;
	
	private List<PhaseResponse> phases = new ArrayList<>(0);
	
	public ProcessResponse() {
		super();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the phases
	 */
	public List<PhaseResponse> getPhases() {
		return phases;
	}
	
	public void addPhase(PhaseResponse phaseDto) {
		if (Objects.nonNull(phaseDto)) {
			this.phases.add(phaseDto);
		}
	}

	@Override
	public String toString() {
		return String.format("ProcessResponse [id=%s, code=%s, active=%s, date=%s, phases=%s]", id, code, active, date,
				phases);
	}
}
