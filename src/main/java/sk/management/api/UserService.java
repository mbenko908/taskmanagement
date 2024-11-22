package sk.management.api;

import java.util.List;

import sk.management.api.request.UserAddRequest;
import sk.management.domain.User;

//template for User
//api definuje co sluyba robi a nie ako to robi a teda popisuje, co sluzba robi a nie ako to robi
public interface UserService {

	long add(UserAddRequest request);

	void delete(long id);

	User get(long id);

	List<User> getAll();
}
