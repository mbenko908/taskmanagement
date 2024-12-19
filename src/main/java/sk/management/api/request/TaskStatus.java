package sk.management.api.request;

public enum TaskStatus {
	NEW("new"), IN_PROGRESS("in_progress"), DONE("done");

	private final String value;

	TaskStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
}
