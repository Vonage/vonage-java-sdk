package com.nexmo.client.conversation;

public class SpecificMemberRequest {
    private String member_id;
    private String conversation_id;

    public SpecificMemberRequest(Builder builder){
        this.member_id = builder.member_id;
        this.conversation_id = builder.conversation_id;

    }
    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public static Builder builder(){return new Builder();}
    public static class Builder{
        private String member_id;
        private String conversation_id;

        public Builder member_id(String member_id){
            this.member_id = member_id;
            return this;
        }

        public Builder conversation_id(String conversation_id){
            this.conversation_id = conversation_id;
            return this;
        }

        public SpecificMemberRequest build(){
            return new SpecificMemberRequest(this);
        }
    }
}
