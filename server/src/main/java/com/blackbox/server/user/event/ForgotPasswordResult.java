package com.blackbox.server.user.event;

import java.io.Serializable;

import com.blackbox.foundation.user.User;

public class ForgotPasswordResult implements Serializable {

	private static final long serialVersionUID = -164823584272878322L;

	private String temporaryPassword;
	private User updatedUser;

	public String getTemporaryPassword() {
		return temporaryPassword;
	}

	public void setTemporaryPassword(String temporaryPassword) {
		this.temporaryPassword = temporaryPassword;
	}

	public User getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(User updatedUser) {
		this.updatedUser = updatedUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((temporaryPassword == null) ? 0 : temporaryPassword
						.hashCode());
		result = prime * result
				+ ((updatedUser == null) ? 0 : updatedUser.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ForgotPasswordResult other = (ForgotPasswordResult) obj;
		if (temporaryPassword == null) {
			if (other.temporaryPassword != null)
				return false;
		} else if (!temporaryPassword.equals(other.temporaryPassword))
			return false;
		if (updatedUser == null) {
			if (other.updatedUser != null)
				return false;
		} else if (!updatedUser.equals(other.updatedUser))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ForgotPasswordResult [temporaryPassword=" + temporaryPassword
				+ ", updatedUser=" + updatedUser + "]";
	}

}
