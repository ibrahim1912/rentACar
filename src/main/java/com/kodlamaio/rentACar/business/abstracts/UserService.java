package com.kodlamaio.rentACar.business.abstracts;

import java.util.List;

import com.kodlamaio.rentACar.business.requests.users.CreateUserRequest;
import com.kodlamaio.rentACar.business.responses.users.GetAllUsersResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;

public interface UserService {

	Result add(CreateUserRequest createUserRequest);
	DataResult<List<GetAllUsersResponse>> getAll();
	DataResult<List<GetAllUsersResponse>> getAll(Integer pageNo,Integer pageSize);
}
