package com.nexmo.client.conversation;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;

public class MemberEndpoint {
    private final CreateMemberMethod createMember;
    private final GetMemberMethod getMember;
    private final ListMembersMethod listMembers;
    private final UpdateMemberMethod updateMember;
    private final DeleteMemberMethod deleteMember;

    MemberEndpoint(HttpWrapper wrapper){
        this.createMember = new CreateMemberMethod(wrapper);
        this.getMember = new GetMemberMethod(wrapper);
        this.listMembers = new ListMembersMethod(wrapper);
        this.updateMember = new UpdateMemberMethod(wrapper);
        this.deleteMember = new DeleteMemberMethod(wrapper);
    }

    Member post(CreateMemberRequest member) throws NexmoClientException{
        return this.createMember.execute(member);
    }

    Member get(SpecificMemberRequest member) throws NexmoClientException{
        return this.getMember.execute(member);
    }

    MemberPage get(CursorListFilter filter) throws NexmoClientException{
        return this.listMembers.execute(filter);
    }

    Member put(UpdateMemberRequest member) throws NexmoClientException{
        return this.updateMember.execute(member);
    }

    Integer delete(SpecificMemberRequest member) throws  NexmoClientException{
        return this.deleteMember.execute(member);
    }
}
