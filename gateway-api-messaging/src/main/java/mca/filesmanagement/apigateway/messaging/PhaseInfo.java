package mca.filesmanagement.apigateway.messaging;

import java.util.Date;

public class PhaseInfo {
	private String phaseCode;
	private String phaseDescription;
	private Date date;
	private Date dateFinished;	
	private String user;
	private String userNameCompleted;
	private String userFinished;
	private String userFinishedCompleted;
	
	public PhaseInfo() {
		super();
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

	/**
	 * @return the userNameCompleted
	 */
	public String getUserNameCompleted() {
		return userNameCompleted;
	}

	/**
	 * @param userNameCompleted the userNameCompleted to set
	 */
	public void setUserNameCompleted(String userNameCompleted) {
		this.userNameCompleted = userNameCompleted;
	}

	/**
	 * @return the userFinishedCompleted
	 */
	public String getUserFinishedCompleted() {
		return userFinishedCompleted;
	}

	/**
	 * @param userFinishedCompleted the userFinishedCompleted to set
	 */
	public void setUserFinishedCompleted(String userFinishedCompleted) {
		this.userFinishedCompleted = userFinishedCompleted;
	}

	@Override
	public String toString() {
		return String.format(
				"PhaseInfo [phaseCode=%s, phaseDescription=%s, date=%s, dateFinished=%s, user=%s, userNameCompleted=%s, userFinished=%s, userFinishedCompleted=%s]",
				phaseCode, phaseDescription, date, dateFinished, user, userNameCompleted, userFinished,
				userFinishedCompleted);
	}
}
