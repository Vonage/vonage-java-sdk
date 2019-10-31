package com.nexmo.client.conversation;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;

public class UserEndpoint {
    private final CreateUserMethod createUser;
    private final GetUserMethod getUser;
    private final ListUsersMethod listUsers;
    private final UpdateUserMethod updateUser;
    private final DeleteUserMethod deleteUser;

    UserEndpoint(HttpWrapper wrapper){
        this.createUser = new CreateUserMethod(wrapper);
        this.getUser = new GetUserMethod(wrapper);
        this.listUsers = new ListUsersMethod(wrapper);
        this.updateUser = new UpdateUserMethod(wrapper);
        this.deleteUser = new DeleteUserMethod(wrapper);
    }

    User post(User user)throws NexmoClientException{
        return this.createUser.execute(user);
    }

    User get(String userId) throws NexmoClientException{
        return this.getUser.execute(userId);
    }

    UserPage get(CursorListFilter filter) throws NexmoClientException{
        return this.listUsers.execute(filter);
    }

    User put(User user) throws NexmoClientException{
        return this.updateUser.execute(user);
    }

    Integer delete(String id) throws NexmoClientException{
        return this.deleteUser.execute(id);
    }
}
