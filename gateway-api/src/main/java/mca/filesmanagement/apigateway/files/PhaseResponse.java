package mca.filesmanagement.apigateway.files;

import java.util.Date;

public class PhaseResponse {
	private Long id;
	private String phaseCode;
	private String phaseDescription;
	private Date date;
	private Date dateFinished;	
	private String user;
	private String userFinished;
	
	public PhaseResponse() {
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
	 * @return the phaseCode
	 */
	public String getPhaseCode() {
		return phaseCode;
	}

	/**
	 * @param phaseCode the phaseCode to set
	 */
	public void setPhaseCode(String phaseCode) {
		this.phaseCode = phaseCode;
	}

	/**
	 * @return the phaseDescription
	 */
	public String getPhaseDescription() {
		return phaseDescription;
	}

	/**
	 * @param phaseDescription the phaseDescription to set
	 */
	public void setPhaseDescription(String phaseDescription) {
		this.phaseDescription = phaseDescription;
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
	 * @return the dateFinished
	 */
	public Date getDateFinished() {
		return dateFinished;
	}

	/**
	 * @param dateFinished the dateFinished to set
	 */
	public void setDateFinished(Date dateFinished) {
		this.dateFinished = dateFinished;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the userFinished
	 */
	public String getUserFinished() {
		return userFinished;
	}

	/**
	 * @param userFinished the userFinished to set
	 */
	public void setUserFinished(String userFinished) {
		this.userFinished = userFinished;
	}
}
