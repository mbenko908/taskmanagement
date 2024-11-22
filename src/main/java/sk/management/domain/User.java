package sk.management.domain;

import lombok.Value;

//define User
@Value
public class User {
	private final long Id;
	private final String name;
	private final String email;

}
