/*
 *   Copyright 2024 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client.conversations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.ClientTest;
import com.vonage.client.RestEndpoint;
import static com.vonage.client.TestUtils.testJsonableBaseObject;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.common.ChannelType;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.common.SortOrder;
import com.vonage.client.users.BaseUser;
import com.vonage.client.users.channels.Channel;
import com.vonage.client.users.channels.Mms;
import com.vonage.client.users.channels.Sms;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

public class ConversationsClientTest extends ClientTest<ConversationsClient> {
	static final Random RANDOM = new Random();
	static final boolean IS_SYSTEM = false, EXCLUDE_DELETED_EVENTS = true,
			AUDIO = true, AUDIO_EARMUFFED = false, AUDIO_MUTED = true, AUDIO_ENABLED = true;
	static final int PAGE_SIZE = 30,
			EVENT_ID = RANDOM.nextInt(100),
			EVENT_START_ID = RANDOM.nextInt(EVENT_ID),
			EVENT_END_ID = RANDOM.nextInt(EVENT_ID, EVENT_ID * 12),
			CONVERSATION_SEQUENCE_NUMBER = 159, CONVERSATION_TTL = 60;
	static final double USER_SESSION_TTL = 1.6;
	static final SortOrder ORDER = SortOrder.DESCENDING;
	static final ConversationStatus CONVERSATION_STATE = ConversationStatus.INACTIVE;
	static final MemberState MEMBER_STATE = MemberState.JOINED;
	static final Class<? extends Event> KNOWN_EVENT_CLASS = AudioSayDoneEvent.class;
	static final EventType KNOWN_EVENT_TYPE = EventType.AUDIO_SAY_DONE, CUSTOM_EVENT_TYPE = EventType.CUSTOM;
	static final ChannelType CHANNEL_TYPE = ChannelType.PHONE,
			CHANNEL_TYPE_TO = ChannelType.MMS, CHANNEL_TYPE_FROM = ChannelType.SMS;
	static final Map<String, Object> CONVERSATION_CUSTOM_DATA = Map.of(
			"property1", "value1",
			"prop2", "Val 2"
	);

	static final String
			KNOCKING_ID_STR = "ccc86f37-0a18-4f2e-9bee-da5dce04f601",
			INVALID_UUID_STR = "12345678-9abc-defg-hijk-lmnopqrstuvw",
			INVITED_BY = "MEM-7bda03b5-5d1b-4734-bf7b-bc83e37f2420",
			MEMBER_ID_INVITING = "MEM-7b941a4a-122e-4d9a-868c-d641d185f98c",
			MEMBER_ID = "MEM-df8e57d8-1c8e-4573-bf4d-29d5414dcb42",
			CONVERSATION_ID = "CON-d66d47de-5bcb-4300-94f0-0c9d4b948e9a",
			USER_ID = "USR-82e028d9-5201-4f1e-8188-604b2d3471ec",
			INVITING_USER_ID = "USR-c051865e-ef59-4533-b58a-22cc6c4e962d",
			SESSION_ID = "SES-63f61863-4a51-4f6b-86e1-46edebio0391",
			START_DATE_STR = "2017-12-30 10:08:59",
			END_DATE_STR = "2018-01-03 12:00:00",
			TIMESTAMP_CREATED_STR = "2019-08-10T11:29:24.997Z",
			TIMESTAMP_UPDATED_STR = "2019-09-03T18:40:24.324Z",
			TIMESTAMP_DESTROYED_STR = "2022-02-03T04:58:59.601Z",
			TIMESTAMP_INVITED_STR = "2019-07-03T18:52:24.301Z",
			TIMESTAMP_JOINED_STR = "2019-09-03T17:02:01.342Z",
			TIMESTAMP_LEFT_STR = "2020-10-30T04:59:57.106Z",
			TO_NUMBER = "447900000001",
			FROM_NUMBER = "491711234567",
			MEMBER_FROM = "MEM-67bf6977-3eb8-40b8-b581-204fb4df33b1",
			REASON_CODE = "test_code",
			REASON_TEXT = "Because I said so",
			PHONE_NUMBER = "447900000001",
			USER_NAME = "my_user_name",
			USER_DISPLAY_NAME = "My User Name",
			USER_IMAGE_URL_STR = "https://example.com/profile.jpg",
			CONVERSATION_NAME = "customer_chat",
			CONVERSATION_DISPLAY_NAME = "Chat with Customer",
			CONVERSATION_IMAGE_URL_STR = "https://example.com/image.png",
			CONVERSATION_STATE_STR = "INACTIVE",
			MEMBER_STATE_STR = "JOINED",
			CHANNEL_TYPE_STR = "phone",
			CHANNEL_TYPE_FROM_STR = "sms",
			CHANNEL_TYPE_TO_STR = "mms",
			ORDER_STR = "desc",
			CUSTOM_EVENT_TYPE_STR = "custom",
			KNOWN_EVENT_TYPE_STR = "audio:say:done",
			CONVERSATION_TYPE = "quick_chat",
			CONVERSATION_CUSTOM_SORT_KEY = "CSK_1",
			CONVERSATION_CUSTOM_DATA_STR = "{\"property1\":\"value1\",\"prop2\":\"Val 2\"}",
			REQUEST_CURSOR = "7EjDNQrAcipmOnc0HCzpQRkhBULzY44ljGUX4lXKyUIVfiZay5pv9wg=",
			SAMPLE_BASE_CONVERSATION_RESPONSE_PARTIAL = STR."""
				{
				   "id": "\{CONVERSATION_ID}",
				   "name": "\{CONVERSATION_NAME}",
				   "display_name": "\{CONVERSATION_DISPLAY_NAME}",
				   "image_url": "\{CONVERSATION_IMAGE_URL_STR}",
				   "timestamp": {
					  "created": "\{TIMESTAMP_CREATED_STR}",
					  "updated": "\{TIMESTAMP_UPDATED_STR}",
					  "destroyed": "\{TIMESTAMP_DESTROYED_STR}"
				   },""",
			SAMPLE_BASE_CONVERSATION_RESPONSE = STR."\{SAMPLE_BASE_CONVERSATION_RESPONSE_PARTIAL
                    .substring(0, SAMPLE_BASE_CONVERSATION_RESPONSE_PARTIAL.length() - 1)}\n\t}",
			SAMPLE_CONVERSATION_RESPONSE_PARTIAL = SAMPLE_BASE_CONVERSATION_RESPONSE_PARTIAL + STR."""
				   "state": "\{CONVERSATION_STATE_STR}",
				   "sequence_number": \{CONVERSATION_SEQUENCE_NUMBER},
				   "properties": {
					  "ttl": \{CONVERSATION_TTL},
					  "type": "\{CONVERSATION_TYPE}",
					  "custom_sort_key": "\{CONVERSATION_CUSTOM_SORT_KEY}",
					  "custom_data": \{CONVERSATION_CUSTOM_DATA_STR}
				   },
				   "_links": {
					  "self": {
						 "href": "https://api.nexmo.com/v1/conversations/\{CONVERSATION_ID}"
					  }
				   }""",
			SAMPLE_CONVERSATION_RESPONSE = SAMPLE_CONVERSATION_RESPONSE_PARTIAL + "\n}",
			SAMPLE_LIST_CONVERSATIONS_RESPONSE = STR."""
   				{
				   "page_size": \{PAGE_SIZE},
				   "_embedded": {
					  "conversations": [
						 {}, \{SAMPLE_BASE_CONVERSATION_RESPONSE}, {"id":null}
					  ]
				   },
				   "_links": {
					  "first": {
						 "href": "https://api.nexmo.com/v1/conversations?order=desc&page_size=10"
					  },
					  "self": {
						 "href": "https://api.nexmo.com/v1/conversations?order=desc&page_size=10&cursor=7EjDNQrAcipmOnc0HCzpQRkhBULzY44ljGUX4lXKyUIVfiZay5pv9wg="
					  },
					  "next": {
						 "href": "https://api.nexmo.com/v1/conversations?order=desc&page_size=10&cursor=7EjDNQrAcipmOnc0HCzpQRkhBULzY44ljGUX4lXKyUIVfiZay5pv9wg="
					  },
					  "prev": {
						 "href": "https://api.nexmo.com/v1/conversations?order=desc&page_size=10&cursor=7EjDNQrAcipmOnc0HCzpQRkhBULzY44ljGUX4lXKyUIVfiZay5pv9wg="
					  }
				   }
				}
			""",
			SAMPLE_USER_CONVERSATION_RESPONSE = SAMPLE_CONVERSATION_RESPONSE_PARTIAL + STR."""
   			,
   			"_embedded": {
				  "id": "\{MEMBER_ID}",
				  "state": "\{MEMBER_STATE_STR}"
			   }
			}
			""",
			SAMPLE_LIST_USER_CONVERSATIONS_RESPONSE = STR."""
   				{
				   "page_size": \{PAGE_SIZE},
				   "_embedded": {
					  "conversations": [
						 \{SAMPLE_USER_CONVERSATION_RESPONSE}, {"_embedded": {}}
					  ]
				   },
				   "_links": {
					  "first": {
						 "href": "https://api.nexmo.com/v1/conversations?order=desc&page_size=10"
					  },
					  "self": {
						 "href": "https://api.nexmo.com/v1/conversations?order=desc&page_size=10&cursor=7EjDNQrAcipmOnc0HCzpQRkhBULzY44ljGUX4lXKyUIVfiZay5pv9wg="
					  },
					  "next": {
						 "href": "https://api.nexmo.com/v1/conversations?order=desc&page_size=10&cursor=7EjDNQrAcipmOnc0HCzpQRkhBULzY44ljGUX4lXKyUIVfiZay5pv9wg="
					  },
					  "prev": {
						 "href": "https://api.nexmo.com/v1/conversations?order=desc&page_size=10&cursor=7EjDNQrAcipmOnc0HCzpQRkhBULzY44ljGUX4lXKyUIVfiZay5pv9wg="
					  }
				   }
				}
			""",
			SAMPLE_USER_SESSION_RESPONSE = STR."""
   				{
					"id": "\{SESSION_ID}",
					"_embedded": {
					   "user": {
						  "id": "\{USER_ID}",
						  "name": "\{USER_NAME}"
					   },
					   "api_key": "\{API_KEY}"
					},
					"properties": {
					   "ttl": \{USER_SESSION_TTL}
					},
					"_links": {
					   "self": {
						  "href": "https://api.nexmo.com/v1/users/\{USER_ID}/sessions"
					   }
					}
			 	}
			""",
			SAMPLE_LIST_USER_SESSIONS_RESPONSE = STR."""
   				{
				   "page_size": \{PAGE_SIZE},
				   "_embedded": {
					  "sessions": [
						 {}, \{SAMPLE_USER_SESSION_RESPONSE}, {"_embedded":{"user":{}}}
					  ]
				   },
				   "_links": {
					  "first": {
						 "href": "https://api.nexmo.com/v1/users/USR-82e028d9-5201-4f1e-8188-604b2d3471ec/sessions?order=desc&page_size=10"
					  },
					  "self": {
						 "href": "https://api.nexmo.com/v1/users/USR-82e028d9-5201-4f1e-8188-604b2d3471ec/sessions?order=desc&page_size=10&cursor=7EjDNQrAcipmOnc0HCzpQRkhBULzY44ljGUX4lXKyUIVfiZay5pv9wg="
					  },
					  "next": {
						 "href": "https://api.nexmo.com/v1/users/USR-82e028d9-5201-4f1e-8188-604b2d3471ec/sessions?order=desc&page_size=10&cursor=7EjDNQrAcipmOnc0HCzpQRkhBULzY44ljGUX4lXKyUIVfiZay5pv9wg="
					  },
					  "prev": {
						 "href": "https://api.nexmo.com/v1/users/USR-82e028d9-5201-4f1e-8188-604b2d3471ec/sessions?order=desc&page_size=10&cursor=7EjDNQrAcipmOnc0HCzpQRkhBULzY44ljGUX4lXKyUIVfiZay5pv9wg="
					  }
				   }
				}
			""",
			SAMPLE_BASE_MEMBER_RESPONSE_PARTIAL = STR."""
				{
					"id": "\{MEMBER_ID}",
					"conversation_id": "\{CONVERSATION_ID}",
					"_embedded": {
						 "user": {
							"id": "\{USER_ID}",
							"name": "\{USER_NAME}",
							"display_name": "\{USER_DISPLAY_NAME}",
							"_links": {
							   "self": {
								  "href": "https://api.nexmo.com/v1/users/\{USER_ID}"
							   }
							}
						}
					},
					"state": "\{MEMBER_STATE_STR}",
					"_links": {
					 "href": "https://api.nexmo.com/v1/conversations/\{CONVERSATION_ID}/members/\{MEMBER_ID}"
					}""",
			SAMPLE_BASE_MEMBER_RESPONSE = STR."\{SAMPLE_BASE_MEMBER_RESPONSE_PARTIAL}\n\t}",
			SAMPLE_MEMBER_RESPONSE = SAMPLE_BASE_MEMBER_RESPONSE_PARTIAL + STR."""
				  ,
				  "timestamp": {
					 "invited": "\{TIMESTAMP_INVITED_STR}",
					 "joined": "\{TIMESTAMP_JOINED_STR}",
					 "left": "\{TIMESTAMP_LEFT_STR}"
				  },
				  "initiator": {
					 "joined": {
						"is_system": \{IS_SYSTEM},
						"user_id": "\{INVITING_USER_ID}",
						"member_id": "\{MEMBER_ID_INVITING}"
					 }
				  },
				  "channel": {
					 "type": "\{CHANNEL_TYPE_STR}",
					 "from": {
						"type": "\{CHANNEL_TYPE_FROM_STR}",
						"number": "\{FROM_NUMBER}"
					 },
					 "to": {
						"type": "\{CHANNEL_TYPE_TO_STR}",
						"number": "\{TO_NUMBER}"
					 }
				  },
				  "media": {
					 "audio_settings": {
						"enabled": \{AUDIO_ENABLED},
						"earmuffed": \{AUDIO_EARMUFFED},
						"muted": \{AUDIO_MUTED}
					 },
					 "audio": \{AUDIO}
				  },
				  "knocking_id": "\{KNOCKING_ID_STR}",
				  "invited_by": "\{INVITED_BY}"
			   }
			""",
			SAMPLE_LIST_MEMBERS_RESPONSE = STR."""
               {
                  "page_size": \{PAGE_SIZE},
                  "_embedded": {
                     "members": [
                        {},
                        \{SAMPLE_BASE_MEMBER_RESPONSE},
                        {"state": "LEFT", "id": "\{MEMBER_ID_INVITING}"},
                        {"_embedded": {"user": {}}},
                        {"_embedded": {}, "_links": {}}
                     ]
                  },
                  "_links": {
                     "first": {
                        "href": "https://api.nexmo.com/v1/conversations/CON-d66d47de-5bcb-4300-94f0-0c9d4b948e9a/members?order=desc&page_size=10"
                     },
                     "self": {
                        "href": "https://api.nexmo.com/v1/conversations/CON-d66d47de-5bcb-4300-94f0-0c9d4b948e9a/members?order=desc&page_size=10&cursor=88b395c167da4d94e929705cbd63b82973771e7d390d274a58e301386d5762600a3ffd799bfb3fc5190c5a0d124cdd0fc72fe6e450506b18e4e2edf9fe84c7a0"
                     },
                     "next": {
                        "href": "https://api.nexmo.com/v1/conversations/CON-d66d47de-5bcb-4300-94f0-0c9d4b948e9a/members?order=desc&page_size=10&cursor=88b395c167da4d94e929705cbd63b829a650e69a39197bfd4c949f4243f60dc4babb696afa404d2f44e7775e32b967f2a1a0bb8fb259c0999ba5a4e501eaab55"
                     },
                     "prev": {
                        "href": "https://api.nexmo.com/v1/conversations/CON-d66d47de-5bcb-4300-94f0-0c9d4b948e9a/members?order=desc&page_size=10&cursor=069626a3de11d2ec900dff5042197bd75f1ce41dafc3f2b2481eb9151086e59aae9dba3e3a8858dc355232d499c310fbfbec43923ff657c0de8d49ffed9f7edb"
                     }
                  }
               }
            """,
			SAMPLE_EVENT_RESPONSE = STR."""
   				{
   					"id": \{EVENT_ID},
					"type": "\{CUSTOM_EVENT_TYPE_STR}",
				    "from": "\{MEMBER_ID}",
				    "body": \{CONVERSATION_CUSTOM_DATA_STR},
				    "timestamp": "\{TIMESTAMP_CREATED_STR}",
				    "_embedded": {
				 	  "from_user": {
				 		 "id": "\{USER_ID}",
				 		 "name": "\{USER_NAME}",
				 		 "display_name": "\{USER_DISPLAY_NAME}",
				 		 "image_url": "\{USER_IMAGE_URL_STR}",
				 		 "custom_data": {}
				 	  },
				 	  "from_member": {
				 		 "id": "\{MEMBER_ID_INVITING}"
				 	  }
				    },
				    "_links": {
				 	  "self": {
				 		 "href": "https://api.nexmo.com/v1/conversations/\{CONVERSATION_ID}/events/\{EVENT_ID}"
				 	  }
				    }
				}
			""",
			SAMPLE_LIST_EVENTS_RESPONSE = STR."""
   				{
					"page_size": \{PAGE_SIZE},
					"_links": {
					  "first": {},"self": {},"next": {},"prev": {}
					},
					"_embedded": [
						\{SAMPLE_EVENT_RESPONSE},
						{}, {"type": "\{KNOWN_EVENT_TYPE_STR}"}
					]
			   }
			""";

	static final Channel CHANNEL_FROM = new Sms(FROM_NUMBER), CHANNEL_TO = new Mms(TO_NUMBER);
	static final UUID KNOCKING_ID = UUID.fromString(KNOCKING_ID_STR), RANDOM_UUID = UUID.randomUUID();
	static final URI
			CONVERSATION_IMAGE_URL = URI.create(CONVERSATION_IMAGE_URL_STR),
			USER_IMAGE_URL = URI.create(USER_IMAGE_URL_STR);
	static final Instant
			START_DATE = Instant.parse(START_DATE_STR.replace(' ','T')+'Z'),
			END_DATE = Instant.parse(END_DATE_STR.replace(' ','T')+'Z'),
			TIMESTAMP_CREATED = Instant.parse(TIMESTAMP_CREATED_STR),
			TIMESTAMP_UPDATED = Instant.parse(TIMESTAMP_UPDATED_STR),
			TIMESTAMP_DESTROYED = Instant.parse(TIMESTAMP_DESTROYED_STR),
			TIMESTAMP_INVITED = Instant.parse(TIMESTAMP_INVITED_STR),
			TIMESTAMP_JOINED = Instant.parse(TIMESTAMP_JOINED_STR),
			TIMESTAMP_LEFT = Instant.parse(TIMESTAMP_LEFT_STR);

	static final Supplier<Member.Builder> MEMBER_REQUEST_BUILDER_FACTORY = () ->
			Member.builder().state(MEMBER_STATE).user(USER_ID)
			.channelType(CHANNEL_TYPE).fromChannel(CHANNEL_FROM).toChannel(CHANNEL_TO);

	public ConversationsClientTest() {
		client = new ConversationsClient(wrapper);
	}

	void assert401ResponseException(Executable invocation) throws Exception {
		assert401ApiResponseException(ConversationsResponseException.class, invocation);
	}

	void assert404ResponseException(Executable invocation) throws Exception {
		stubResponseAndAssertThrows(404, invocation, ConversationsResponseException.class);
	}

	void assertResponseExceptions(Executable invocation) throws Exception {
		assert404ResponseException(invocation);
		assert401ResponseException(invocation);
	}

	static void assertEqualsSampleBaseConversation(BaseConversation parsed) {
		testJsonableBaseObject(parsed);
		assertEquals(CONVERSATION_ID, parsed.getId());
		assertEquals(CONVERSATION_NAME, parsed.getName());
		assertEquals(CONVERSATION_DISPLAY_NAME, parsed.getDisplayName());
		assertEquals(CONVERSATION_IMAGE_URL, parsed.getImageUrl());
		var timestamp = parsed.getTimestamp();
		assertNotNull(timestamp);
		assertEquals(TIMESTAMP_CREATED, timestamp.getCreated());
		assertEquals(TIMESTAMP_UPDATED, timestamp.getUpdated());
		assertEquals(TIMESTAMP_DESTROYED, timestamp.getDestroyed());
	}

	static void assertEqualsSampleConversation(Conversation parsed) {
		assertEqualsSampleBaseConversation(parsed);
		assertEquals(CONVERSATION_STATE, parsed.getState());
		assertEquals(CONVERSATION_SEQUENCE_NUMBER, parsed.getSequenceNumber());
		var properties = parsed.getProperties();
		assertNotNull(properties);
		assertEquals(CONVERSATION_TTL, properties.getTtl());
		assertEquals(CONVERSATION_TYPE, properties.getType());
		assertEquals(CONVERSATION_CUSTOM_SORT_KEY, properties.getCustomSortKey());
		assertEquals(CONVERSATION_CUSTOM_DATA, properties.getCustomData());
	}

	static void assertEqualsEmptyConversation(Conversation parsed) {
		assertNotNull(parsed);
		assertNull(parsed.getProperties());
		assertNull(parsed.getId());
		assertNull(parsed.getCallback());
		assertNull(parsed.getName());
		assertNull(parsed.getState());
		assertNull(parsed.getImageUrl());
		assertNull(parsed.getDisplayName());
		assertNull(parsed.getNumbers());
		assertNull(parsed.getSequenceNumber());
		assertNull(parsed.getTimestamp());
	}

	static void assertEqualsSampleUserConversation(UserConversation parsed) {
		assertEqualsSampleConversation(parsed);
		var member = parsed.getMember();
		testJsonableBaseObject(member);
		assertEquals(MEMBER_ID, member.getId());
		assertEquals(MEMBER_STATE, member.getState());
	}
	
	static void assertEmptyBaseConversation(BaseConversation parsed) {
		testJsonableBaseObject(parsed);
		assertNull(parsed.getId());
		assertNull(parsed.getName());
		assertNull(parsed.getDisplayName());
		assertNull(parsed.getImageUrl());
		assertNull(parsed.getTimestamp());
	}

	static void assertEqualsSampleListConversations(ListConversationsResponse parsed) {
		testJsonableBaseObject(parsed);
		assertEquals(PAGE_SIZE, parsed.getPageSize());
		var conversations = parsed.getConversations();
		assertNotNull(conversations);
		assertEquals(3, conversations.size());
		assertEmptyBaseConversation(conversations.getFirst());
		assertEqualsSampleBaseConversation(conversations.get(1));
		assertEmptyBaseConversation(conversations.getLast());
	}

	static void assertEqualsSampleListUserConversations(ListUserConversationsResponse parsed) {
		testJsonableBaseObject(parsed);
		assertEquals(PAGE_SIZE, parsed.getPageSize());
		var conversations = parsed.getConversations();
		assertNotNull(conversations);
		assertEquals(2, conversations.size());
		assertEqualsSampleUserConversation(conversations.getFirst());
		var last = conversations.getLast();
		testJsonableBaseObject(last);
		var member = last.getMember();
		assertNotNull(member);
		assertNull(member.getId());
		assertNull(member.getState());
	}

	static void assertEqualsBaseUser(BaseUser parsed) {
		testJsonableBaseObject(parsed);
		assertEquals(USER_ID, parsed.getId());
		assertEquals(USER_NAME, parsed.getName());
	}

	static void assertEqualsSampleBaseMember(BaseMember parsed) {
		testJsonableBaseObject(parsed);
		assertEquals(MEMBER_STATE, parsed.getState());
		assertEquals(MEMBER_ID, parsed.getId());
		assertEqualsBaseUser(parsed.getUser());
	}

	static void assertEqualsSampleMember(Member parsed) {
		assertNotNull(parsed);
		assertEquals(CONVERSATION_ID, parsed.getConversationId());
		parsed.setConversationId(null);
		assertEqualsSampleBaseMember(parsed);

		var timestamp = parsed.getTimestamp();
		testJsonableBaseObject(timestamp);
		assertEquals(TIMESTAMP_INVITED, timestamp.getInvited());
		assertEquals(TIMESTAMP_JOINED, timestamp.getJoined());
		assertEquals(TIMESTAMP_LEFT, timestamp.getLeft());

		var initiator = parsed.getInitiator();
		testJsonableBaseObject(initiator);
		assertEquals(IS_SYSTEM, initiator.invitedByAdmin());
		assertEquals(MEMBER_ID_INVITING, initiator.getMemberId());
		assertEquals(INVITING_USER_ID, initiator.getUserId());

		var channel = parsed.getChannel();
		testJsonableBaseObject(channel);
		assertEquals(CHANNEL_TYPE, channel.getType());
		assertEquals(CHANNEL_FROM, channel.getFrom());
		assertEquals(CHANNEL_TYPE_FROM, CHANNEL_FROM.getType());
		assertEquals(CHANNEL_TO, channel.getTo());
		assertEquals(CHANNEL_TYPE_TO, CHANNEL_TO.getType());

		var media = parsed.getMedia();
		testJsonableBaseObject(media);
		assertEquals(AUDIO, media.getAudio());
		var audioSettings = media.getAudioSettings();
		testJsonableBaseObject(audioSettings);
		assertEquals(AUDIO_ENABLED, audioSettings.getEnabled());
		assertEquals(AUDIO_EARMUFFED, audioSettings.getEarmuffed());
		assertEquals(AUDIO_MUTED, audioSettings.getMuted());

		assertEquals(KNOCKING_ID, parsed.getKnockingId());
		assertEquals(INVITED_BY, parsed.getInvitedBy());
		var inviting = parsed.getMemberIdInviting();
		if (inviting != null) {
			assertEquals(MEMBER_ID_INVITING, inviting);
		}
	}

	static void assertEqualsEmptyBaseMember(BaseMember parsed) {
		testJsonableBaseObject(parsed);
		assertNull(parsed.getUser());
		assertNull(parsed.getId());
		assertNull(parsed.getState());
	}

	static void assertEqualsEmptyMember(Member parsed) {
		assertEqualsEmptyBaseMember(parsed);
		assertNull(parsed.getConversationId());
		assertNull(parsed.getMedia());
		assertNull(parsed.getMemberIdInviting());
		assertNull(parsed.getKnockingId());
		assertNull(parsed.getInvitedBy());
		assertNull(parsed.getInitiator());
		assertNull(parsed.getChannel());
		assertNull(parsed.getFrom());
		assertNull(parsed.getTimestamp());
	}

	static void assertEqualsEmptyBaseUser(BaseUser parsed) {
		testJsonableBaseObject(parsed);
		assertNull(parsed.getName());
		assertNull(parsed.getId());
	}

	static void assertEqualsSampleListMembers(ListMembersResponse parsed) {
		testJsonableBaseObject(parsed);
		assertEquals(PAGE_SIZE, parsed.getPageSize());
		var members = parsed.getMembers();
		assertNotNull(members);
		assertEquals(5, members.size());
		assertEqualsEmptyBaseMember(members.getFirst());
		assertEqualsSampleBaseMember(members.get(1));
		var third = members.get(2);
		testJsonableBaseObject(third);
		assertEquals(MemberState.LEFT, third.getState());
		assertEquals(MEMBER_ID_INVITING, third.getId());
		assertNull(third.getUser());
		var fourth = members.get(3);
		testJsonableBaseObject(fourth);
		assertNull(fourth.getId());
		assertNull(fourth.getState());
		assertEqualsEmptyBaseUser(fourth.getUser());
		assertEqualsEmptyBaseMember(members.get(4));
	}

	static void assertEqualsMinimalMember(Member request) {
		assertNotNull(request);
		assertEquals(MEMBER_STATE, request.getState());
		var user = request.getUser();
		assertNotNull(user);
		assertEquals(USER_ID, user.getId());
		var channel = request.getChannel();
		assertNotNull(channel);
		assertEquals(CHANNEL_TYPE, channel.getType());
		var fromChannel = channel.getFrom();
		assertNotNull(fromChannel);
		assertEquals(CHANNEL_FROM, fromChannel);
		assertEquals(CHANNEL_TYPE_FROM, fromChannel.getType());
		var toChannel = channel.getTo();
		assertNotNull(toChannel);
		assertEquals(CHANNEL_TO, toChannel);
		assertEquals(CHANNEL_TYPE_TO, toChannel.getType());
		assertNull(request.getFrom());
		assertNull(request.getConversationId());
		assertNull(request.getInvitedBy());
		assertNull(request.getMemberIdInviting());
		assertNull(request.getKnockingId());
		assertNull(request.getInitiator());
		assertNull(request.getMedia());
		assertNull(request.getTimestamp());
	}

	static void assertEqualsSampleEvent(Event parsed) {
		assertNotNull(parsed);
		String convId = parsed.conversationId;
		parsed.conversationId = null;
		testJsonableBaseObject(parsed);
		parsed.conversationId = convId;
		assertEquals(CUSTOM_EVENT_TYPE, parsed.getType());
		assertEquals(CustomEvent.class, parsed.getClass());
		assertEquals(CONVERSATION_CUSTOM_DATA, ((CustomEvent) parsed).getBody());
		assertEquals(EVENT_ID, parsed.getId());
		assertEquals(MEMBER_ID, parsed.getFrom());
		assertEquals(TIMESTAMP_CREATED, parsed.getTimestamp());
		var fromMember = parsed.getFromMember();
		testJsonableBaseObject(fromMember);
		assertEquals(MEMBER_ID_INVITING, fromMember.getId());
		assertNull(fromMember.getUser());
		assertNull(fromMember.getState());
		var fromUser = parsed.getFromUser();
		testJsonableBaseObject(fromUser);
		assertEquals(USER_ID, fromUser.getId());
		assertEquals(USER_NAME, fromUser.getName());
		assertEquals(USER_DISPLAY_NAME, fromUser.getDisplayName());
		assertEquals(USER_IMAGE_URL, fromUser.getImageUrl());
		assertEquals(Map.of(), fromUser.getCustomData());
	}

	static void assertEqualsEmptyEvent(Event parsed) {
		testJsonableBaseObject(parsed);
		assertEquals(GenericEvent.class, parsed.getClass());
		assertNull(parsed.getType());
		assertNull(parsed.getId());
		assertNull(parsed.getTimestamp());
		assertNull(parsed.getFrom());
		assertNull(parsed.getFromMember());
		assertNull(parsed.getFromUser());
	}

	static void assertEqualsSampleListEvents(ListEventsResponse parsed) {
		testJsonableBaseObject(parsed);
		assertEquals(PAGE_SIZE, parsed.getPageSize());
		assertNotNull(parsed.getLinks());
		var events = parsed.getEvents();
		assertNotNull(events);
		assertEquals(3, events.size());
		assertEqualsSampleEvent(events.getFirst());
		assertEqualsEmptyEvent(events.get(1));
		var last = events.getLast();
		testJsonableBaseObject(last);
		assertEquals(KNOWN_EVENT_TYPE, last.getType());
		assertEquals(KNOWN_EVENT_CLASS, last.getClass());
	}
	
	// CONVERSATIONS

	@Test
	public void testListConversations() throws Exception {
		ListConversationsRequest request = ListConversationsRequest.builder().build();
		stubResponse(200, SAMPLE_LIST_CONVERSATIONS_RESPONSE);
		var response = client.listConversations(request);
		assertEqualsSampleListConversations(response);

		var listOnly = stubResponseAndGet(SAMPLE_LIST_CONVERSATIONS_RESPONSE, client::listConversations);
		assertEquals(response.getConversations(), listOnly);

		stubResponseAndAssertThrows(200,
				() -> client.listConversations(null), NullPointerException.class
		);
		stubResponseAndAssertThrows(409,
				() -> client.listConversations(request),
				ConversationsResponseException.class
		);
		assert401ResponseException(() -> client.listConversations(request));
	}

	@Test
	public void testListConversationsEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<ListConversationsRequest, ListConversationsResponse>() {

			@Override
			protected RestEndpoint<ListConversationsRequest, ListConversationsResponse> endpoint() {
				return client.listConversations;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ListConversationsRequest request) {
				return "/v1/conversations/";
			}

			@Override
			protected ListConversationsRequest sampleRequest() {
				return ListConversationsRequest.builder()
						.pageSize(PAGE_SIZE).order(ORDER).cursor(REQUEST_CURSOR)
						.startDate(START_DATE).endDate(END_DATE).build();
			}

			@Override
			protected Map<String, String> sampleQueryParams() {
				return Map.of(
						"cursor", REQUEST_CURSOR,
						"page_size", String.valueOf(PAGE_SIZE),
						"order", String.valueOf(ORDER),
						"date_start", START_DATE_STR,
						"date_end", END_DATE_STR
				);
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testSampleRequestGetters();
				testEmptyRequest();
				testPageSizeLimit();
			}

			void testSampleRequestGetters() {
				var request = sampleRequest();
				assertEquals(REQUEST_CURSOR, request.getCursor());
				assertEquals(START_DATE, request.getStartDate());
				assertEquals(END_DATE, request.getEndDate());
				assertEquals(PAGE_SIZE, request.getPageSize());
				assertEquals(ORDER, request.getOrder());
			}

			void testEmptyRequest() {
				var request =  ListConversationsRequest.builder().build();
				assertNull(request.getCursor());
				assertNull(request.getPageSize());
				assertNull(request.getOrder());
				assertNull(request.getStartDate());
				assertNull(request.getEndDate());
				var toMap = request.makeParams();
				assertNotNull(toMap);
				assertEquals(0, toMap.size());
			}

			void testPageSizeLimit() {
				int limit = 100;
				assertEquals(limit, ListConversationsRequest.builder().pageSize(limit).build().getPageSize());
				assertThrows(IllegalArgumentException.class,
						() -> ListConversationsRequest.builder().pageSize(limit + 1).build()
				);
				assertThrows(IllegalArgumentException.class,
						() -> ListConversationsRequest.builder().pageSize(0).build()
				);
			}
		}
		.runTests();
	}


	@Test
	public void testCreateConversation() throws Exception {
		var request = Conversation.builder().build();
		stubResponse(201, SAMPLE_CONVERSATION_RESPONSE);
		assertEqualsSampleConversation(client.createConversation(request));
		stubResponseAndAssertThrows(201,
				() -> client.createConversation(null),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(409,
				() -> client.createConversation(request),
				ConversationsResponseException.class
		);
		assert401ResponseException(() -> client.createConversation(request));
	}

	@Test
	public void testCreateConversationEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<Conversation, Conversation>() {
			final Callback callback = Callback.builder().url("http://example.com/callback")
					.eventMask("Test value").applicationId(APPLICATION_ID)
					.nccoUrl("http://example.com/ncco").method(HttpMethod.POST).build();

			@Override
			protected RestEndpoint<Conversation, Conversation> endpoint() {
				return client.createConversation;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(Conversation request) {
				return "/v1/conversations/";
			}

			@Override
			protected Conversation sampleRequest() {
				return Conversation.builder()
						.name(CONVERSATION_NAME)
						.displayName(CONVERSATION_DISPLAY_NAME)
						.imageUrl(CONVERSATION_IMAGE_URL_STR)
						.properties(ConversationProperties.builder()
							.ttl(CONVERSATION_TTL).type(CONVERSATION_TYPE)
							.customSortKey(CONVERSATION_CUSTOM_SORT_KEY)
							.customData(CONVERSATION_CUSTOM_DATA).build()
						)
						.callback(callback).phone(PHONE_NUMBER).build();
			}

			@Override
			protected String sampleRequestBodyString() {
				var request = sampleRequest();
				var customData = request.getProperties().getCustomData();

				try {
					return STR."""
					{"name":"\{request.getName()}","display_name":"\{request.getDisplayName()}",\
					"image_url":"\{request.getImageUrl()}","properties":{\
					"ttl":\{request.getProperties().getTtl()},\
					"type":"\{request.getProperties().getType()}",\
					"custom_sort_key":"\{request.getProperties().getCustomSortKey()}",\
					"custom_data":\{new ObjectMapper().writeValueAsString(customData)}},\
					"numbers":[{"type":"phone","number":"\{PHONE_NUMBER}"}],"callback":{\
					"url":"\{request.getCallback().getUrl()}","event_mask":\
					"\{request.getCallback().getEventMask()}","params":{\
					"applicationId":"\{request.getCallback().getParams().getApplicationId()}",\
					"ncco_url":"\{request.getCallback().getParams().getNccoUrl()}"},\
					"method":"\{request.getCallback().getMethod()}"}}""";
				}
				catch (JsonProcessingException impossible) {
					throw new IllegalStateException(impossible);
				}
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testNameLength();
				testDisplayNameLength();
				testEventMaskLength();
				testCallbackMethod();
				testTypeLength();
				testCustomSortKeyLength();
				testJsonableBaseObject(callback);
			}

			void testNameLength() {
				String limit = "Conv".repeat(25);
				assertEquals(100, Conversation.builder().name(limit).build().getName().length());
				assertThrows(IllegalArgumentException.class,
						() -> Conversation.builder().name(limit+'1').build()
				);
			}

			void testDisplayNameLength() {
				String limit = "dN".repeat(25);
				assertEquals(50, Conversation.builder().displayName(limit).build().getDisplayName().length());
				assertThrows(IllegalArgumentException.class,
						() -> Conversation.builder().displayName(limit+'1').build()
				);
			}

			void testEventMaskLength() {
				String limit = "Event_Mask".repeat(20);
				assertEquals(200, Conversation.builder()
						.callback(Callback.builder().eventMask(limit).build())
						.build().getCallback().getEventMask().length()
				);
				assertThrows(IllegalArgumentException.class, () -> Conversation.builder()
						.callback(Callback.builder().eventMask(limit+'1').build()).build()
				);
			}

			void testCallbackMethod() {
				for (var method : HttpMethod.values()) {
					switch (method) {
						default -> assertThrows(IllegalArgumentException.class, () -> Conversation.builder()
								.callback(Callback.builder().method(method).build()).build()
						);
						case GET, POST -> assertEquals(method, Conversation.builder()
                                .callback(Callback.builder().method(method).build())
                                .build().getCallback().getMethod()
                        );
					}
				}
			}

			void testTypeLength() {
				String limit = CONVERSATION_TYPE.repeat(20);
				assertEquals(200, Conversation.builder()
						.properties(ConversationProperties.builder().type(limit).build())
						.build().getProperties().getType().length()
				);
				assertThrows(IllegalArgumentException.class, () ->
						ConversationProperties.builder().type(limit+'1').build()
				);
			}

			void testCustomSortKeyLength() {
				String limit = CONVERSATION_CUSTOM_SORT_KEY.repeat(40);
				assertEquals(200, Conversation.builder()
						.properties(ConversationProperties.builder().customSortKey(limit).build())
						.build().getProperties().getCustomSortKey().length()
				);
				assertThrows(IllegalArgumentException.class, () ->
						ConversationProperties.builder().customSortKey(limit+'0').build()
				);
			}
		}
		.runTests();
	}


	@Test
	public void testGetConversation() throws Exception {
		stubResponse(200, SAMPLE_CONVERSATION_RESPONSE);
		assertEqualsSampleConversation(client.getConversation(CONVERSATION_ID));

		stubResponseAndAssertThrows(200,
				() -> client.getConversation(null),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(200,
				() -> client.getConversation(MEMBER_ID),
				IllegalArgumentException.class
		);
		assertResponseExceptions(() -> client.getConversation(CONVERSATION_ID));

		stubResponse(200, "{\"state\": \"limbo\"}");
		assertEqualsEmptyConversation(client.getConversation(CONVERSATION_ID));
	}

	@Test
	public void testGetConversationEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<String, Conversation>() {

			@Override
			protected RestEndpoint<String, Conversation> endpoint() {
				return client.getConversation;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(String request) {
				return "/v1/conversations/"+request;
			}

			@Override
			protected String sampleRequest() {
				return CONVERSATION_ID;
			}
		}
		.runTests();
	}


	@Test
	public void testUpdateConversation() throws Exception {
		var request = Conversation.builder()
				.imageUrl("ftp:///path/to/local/image.tiff")
				.displayName("Support").build();
		stubResponse(200, SAMPLE_CONVERSATION_RESPONSE);
		assertEqualsSampleConversation(client.updateConversation(CONVERSATION_ID, request));

		stubResponseAndAssertThrows(200,
				() -> client.updateConversation(null, request),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(200,
				() -> client.updateConversation(CONVERSATION_ID, null),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(200,
				() -> client.updateConversation("CON-"+SESSION_ID, request),
				IllegalArgumentException.class
		);
		assertResponseExceptions(() -> client.updateConversation(CONVERSATION_ID, request));
	}

	@Test
	public void testUpdateConversationEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<Conversation, Conversation>() {

			@Override
			protected RestEndpoint<Conversation, Conversation> endpoint() {
				return client.updateConversation;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.PUT;
			}

			@Override
			protected String expectedEndpointUri(Conversation request) {
				return "/v1/conversations/"+request.getId();
			}

			@Override
			protected Conversation sampleRequest() {
				var request = Conversation.builder()
						.imageUrl(CONVERSATION_IMAGE_URL_STR)
						.displayName(CONVERSATION_DISPLAY_NAME)
						.name(CONVERSATION_NAME).build();
				request.id = CONVERSATION_ID;
				return request;
			}

			@Override
			protected String sampleRequestBodyString() {
				return STR."""
					{"id":"\{CONVERSATION_ID}","name":"\{CONVERSATION_NAME}",\
					"display_name":"\{CONVERSATION_DISPLAY_NAME}",\
					"image_url":"\{CONVERSATION_IMAGE_URL_STR}"}\
					""";
			}
		}
		.runTests();
	}


	@Test
	public void testDeleteConversation() throws Exception {
		stubResponseAndRun(204, SAMPLE_CONVERSATION_RESPONSE,
				() -> client.deleteConversation(CONVERSATION_ID)
		);
		stubResponseAndAssertThrows(204,
				() -> client.deleteConversation(null),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(204,
				() -> client.deleteConversation("CON-"+INVALID_UUID_STR),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(404,
				() -> client.deleteConversation(CONVERSATION_ID),
				ConversationsResponseException.class
		);
		assertResponseExceptions(() -> client.deleteConversation(CONVERSATION_ID));
	}

	@Test
	public void testDeleteConversationEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<String, Void>() {

			@Override
			protected RestEndpoint<String, Void> endpoint() {
				return client.deleteConversation;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.DELETE;
			}

			@Override
			protected String expectedEndpointUri(String request) {
				return "/v1/conversations/"+request;
			}

			@Override
			protected String sampleRequest() {
				return CONVERSATION_ID;
			}
		}
		.runTests();
	}

	// USERS

	@Test
	public void testListUserConversations() throws Exception {
		var request = ListUserConversationsRequest.builder().build();
		stubResponse(200, SAMPLE_LIST_USER_CONVERSATIONS_RESPONSE);
		assertEqualsSampleListUserConversations(client.listUserConversations(USER_ID, request));

		var userConversations = stubResponseAndGet(SAMPLE_LIST_USER_CONVERSATIONS_RESPONSE,
				() -> client.listUserConversations(USER_ID)
		);
		assertNotNull(userConversations);
		assertEquals(2, userConversations.size());
		assertEqualsSampleUserConversation(userConversations.getFirst());

		stubResponseAndAssertThrows(200,
				() -> client.listUserConversations(null, request),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(200,
				() -> client.listUserConversations(INVALID_UUID_STR+"-RUS", request),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(200,
				() -> client.listUserConversations(SESSION_ID),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(200,
				() -> client.listUserConversations(USER_ID, null),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(404,
				() -> client.listUserConversations(USER_ID, request),
				ConversationsResponseException.class
		);
		assertResponseExceptions(() -> client.listUserConversations(USER_ID, request));
	}

	@Test
	public void testListUserConversationsEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<ListUserConversationsRequest, ListUserConversationsResponse>() {

			@Override
			protected RestEndpoint<ListUserConversationsRequest, ListUserConversationsResponse> endpoint() {
				return client.listUserConversations;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ListUserConversationsRequest request) {
				return "/v1/users/"+request.userId+"/conversations";
			}

			@Override
			protected ListUserConversationsRequest sampleRequest() {
				var request = ListUserConversationsRequest.builder()
						.state(MemberState.INVITED).includeCustomData(true)
						.orderBy(OrderBy.CUSTOM_SORT_KEY).startDate(START_DATE)
						.cursor(REQUEST_CURSOR).build();

				assertEquals(REQUEST_CURSOR, request.getCursor());
				assertEquals(MemberState.INVITED, request.getState());
				assertEquals(OrderBy.CUSTOM_SORT_KEY, request.getOrderBy());
				assertTrue(request.getIncludeCustomData());
				assertEquals(START_DATE, request.getStartDate());

				request.userId = USER_ID;
				return request;
			}

			@Override
			protected Map<String, String> sampleQueryParams() {
				return Map.of(
						"cursor", REQUEST_CURSOR,
						"state", "INVITED",
						"order_by", "custom_sort_key",
						"include_custom_data", "true",
						"date_start", START_DATE_STR
				);
			}
		}
		.runTests();
	}

	// MEMBERS

	@Test
	public void testListMembers() throws Exception {
		var request = ListMembersRequest.builder().build();
		stubResponse(200, SAMPLE_LIST_MEMBERS_RESPONSE);
		var fullResponse = client.listMembers(CONVERSATION_ID, request);
		assertEqualsSampleListMembers(fullResponse);
		stubResponse(200, SAMPLE_LIST_MEMBERS_RESPONSE);
		assertEquals(fullResponse.getMembers(), client.listMembers(CONVERSATION_ID));

		stubResponseAndAssertThrows(SAMPLE_LIST_MEMBERS_RESPONSE,
				() -> client.listMembers(CONVERSATION_ID, null),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(SAMPLE_LIST_MEMBERS_RESPONSE,
				() -> client.listMembers(null, request),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(SAMPLE_LIST_MEMBERS_RESPONSE,
				() -> client.listMembers(MEMBER_ID, request),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(SAMPLE_LIST_MEMBERS_RESPONSE,
				() -> client.listMembers(KNOCKING_ID_STR),
				IllegalArgumentException.class
		);
		assertResponseExceptions(() -> client.listMembers(CONVERSATION_ID));
	}

	@Test
	public void testListMembersEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<ListMembersRequest, ListMembersResponse>() {

			@Override
			protected RestEndpoint<ListMembersRequest, ListMembersResponse> endpoint() {
				return client.listMembers;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ListMembersRequest request) {
				return "/v1/conversations/"+request.conversationId+"/members/";
			}

			@Override
			protected ListMembersRequest sampleRequest() {
				var request = ListMembersRequest.builder()
						.pageSize(PAGE_SIZE).order(ORDER).cursor(REQUEST_CURSOR).build();
				request.conversationId = CONVERSATION_ID;
				return request;
			}

			@Override
			protected Map<String, String> sampleQueryParams() {
				return Map.of(
					"cursor", REQUEST_CURSOR,
					"page_size", String.valueOf(PAGE_SIZE),
					"order", ORDER_STR
				);
			}
		}
		.runTests();
	}


	@Test
	public void testGetMember() throws Exception {
		stubResponse(200, SAMPLE_MEMBER_RESPONSE);
		assertEqualsSampleMember(client.getMember(CONVERSATION_ID, MEMBER_ID));
		stubResponseAndAssertThrows(SAMPLE_MEMBER_RESPONSE,
				() -> client.getMember(MEMBER_ID, CONVERSATION_ID),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(SAMPLE_MEMBER_RESPONSE,
				() -> client.getMember(INVALID_UUID_STR, KNOCKING_ID_STR),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(SAMPLE_MEMBER_RESPONSE,
				() -> client.getMember(null, MEMBER_ID),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(SAMPLE_MEMBER_RESPONSE,
				() -> client.getMember(CONVERSATION_ID, null),
				IllegalArgumentException.class
		);
		assertResponseExceptions(() -> client.getMember(CONVERSATION_ID, MEMBER_ID));

		stubResponse(200, "{\"state\":\"limbo\"}");
		assertEqualsEmptyMember(client.getMember(CONVERSATION_ID, MEMBER_ID));

		stubResponse(200, "{\"channel\":{\"type\":\"app\",\"from\":{\"type\":\"pigeon\"}}}");
		try {
			fail(STR."Expected exception but got: \{client.getMember(CONVERSATION_ID, MEMBER_ID)}");
		}
		catch (VonageResponseParseException ex) {
			assertEquals(IllegalStateException.class, ex.getCause().getCause().getClass());
		}

		for (var ct : ChannelType.values()) {
			stubResponse(200, STR."{\"channel\":{\"type\":\"\{ct}\",\"from\":{}}}");
			try {
				var member = client.getMember(CONVERSATION_ID, MEMBER_ID);
				var fromChannel = member.getChannel().getFrom();
				assertEquals(fromChannel.getClass(), Channel.getConcreteClass(ct));
				assertNull(fromChannel.getType());
				fromChannel.setTypeField();
				assertEquals(ct, fromChannel.getType());
				fromChannel.removeTypeField();
				assertNull(fromChannel.getType());
			}
			catch (VonageResponseParseException ex) {
                if (ct != ChannelType.APP) {
                    fail(ex);
                }
				assertEquals(IllegalStateException.class, ex.getCause().getCause().getClass());
            }
		}
	}

	@Test
	public void testGetMemberEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<ConversationResourceRequestWrapper, Member>() {

			@Override
			protected RestEndpoint<ConversationResourceRequestWrapper, Member> endpoint() {
				return client.getMember;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ConversationResourceRequestWrapper request) {
				return "/v1/conversations/"+request.conversationId+"/members/"+request.resourceId;
			}

			@Override
			protected ConversationResourceRequestWrapper sampleRequest() {
				return new ConversationResourceRequestWrapper(CONVERSATION_ID, MEMBER_ID);
			}
		}
		.runTests();
	}


	@Test
	public void testCreateMember() throws Exception {
		var request = MEMBER_REQUEST_BUILDER_FACTORY.get().build();
		assertEqualsMinimalMember(request);
		stubResponse(201, SAMPLE_MEMBER_RESPONSE);
		var response = client.createMember(CONVERSATION_ID, request);
		assertEqualsSampleMember(response);
		assertEquals(request, response);

		stubResponseAndAssertThrows(SAMPLE_MEMBER_RESPONSE,
				() -> client.createMember(INVALID_UUID_STR, MEMBER_REQUEST_BUILDER_FACTORY.get().build()),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(SAMPLE_MEMBER_RESPONSE,
				() -> client.createMember(CONVERSATION_ID, null),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(SAMPLE_MEMBER_RESPONSE,
				() -> client.createMember(MEMBER_ID, MEMBER_REQUEST_BUILDER_FACTORY.get().build()),
				IllegalArgumentException.class
		);
		assertResponseExceptions(() ->
				client.createMember(CONVERSATION_ID, MEMBER_REQUEST_BUILDER_FACTORY.get().build())
		);
	}

	@Test
	public void testCreateMemberEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<Member, Member>() {

			@Override
			protected RestEndpoint<Member, Member> endpoint() {
				return client.createMember;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(Member request) {
				return "/v1/conversations/"+request.getConversationId()+"/members/";
			}

			@Override
			protected Member sampleRequest() {
				var request = MEMBER_REQUEST_BUILDER_FACTORY.get()
						.user(USER_NAME).knockingId(KNOCKING_ID_STR)
						.memberIdInviting(MEMBER_ID_INVITING).from(MEMBER_FROM)
						.media(MemberMedia.builder()
								.audio(AUDIO).audioEnabled(AUDIO_ENABLED)
								.muted(AUDIO_MUTED).earmuffed(AUDIO_EARMUFFED)
								.build()
						)
						.build();
				request.setConversationId(CONVERSATION_ID);
				return request;
			}

			@Override
			protected String sampleRequestBodyString() {
				return STR."""
    				{"state":"\{MEMBER_STATE}","user":{"name":"\{USER_NAME}"},\
    				"member_id_inviting":"\{MEMBER_ID_INVITING}","from":"\{MEMBER_FROM}",\
    				"knocking_id":"\{KNOCKING_ID_STR}","channel":{"type":"\{CHANNEL_TYPE_STR}",\
    				"from":{"type":"\{CHANNEL_TYPE_FROM_STR}","number":"\{FROM_NUMBER}"},\
    				"to":{"type":"\{CHANNEL_TYPE_TO_STR}","number":"\{TO_NUMBER}"}},\
    				"media":{"audio":\{AUDIO},"audio_settings":{"enabled":\{AUDIO_ENABLED},\
    				"earmuffed":\{AUDIO_EARMUFFED},"muted":\{AUDIO_MUTED}}}}""";
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testInvalidUser();
				testUserNameIsSelectedWhenPassingInvalidUserId();
				testStateIsRequired();
				assertRequestUriAndBody(MEMBER_REQUEST_BUILDER_FACTORY.get().build(), getMinimalRequestBodyString());
			}

			String getMinimalRequestBodyString() {
				return STR."""
					{"state":"\{MEMBER_STATE}","user":{"id":"\{USER_ID}"},"channel":{"type":\
					"\{CHANNEL_TYPE}","from":{"type":"\{CHANNEL_TYPE_FROM}","number":"\{FROM_NUMBER}"},\
					"to":{"type":"\{CHANNEL_TYPE_TO}","number":"\{TO_NUMBER}"}}}""";
			}

			void testInvalidUser() {
				assertThrows(IllegalArgumentException.class, () ->
						MEMBER_REQUEST_BUILDER_FACTORY.get().user(null).build()
				);
				assertThrows(IllegalArgumentException.class, () ->
						MEMBER_REQUEST_BUILDER_FACTORY.get().user(" \t").build()
				);
			}

			void testUserNameIsSelectedWhenPassingInvalidUserId() {
				var minimal = MEMBER_REQUEST_BUILDER_FACTORY.get().user(SESSION_ID).build();
				assertEquals(SESSION_ID, minimal.getUser().getName());
				assertNull(minimal.getUser().getId());
			}

			void testStateIsRequired() {
				var minimalBuilder = MEMBER_REQUEST_BUILDER_FACTORY.get().state(null);
				assertThrows(NullPointerException.class, minimalBuilder::build);
			}
		}
		.runTests();
	}


	@Test
	public void testUpdateMember() throws Exception {
		final var builder = UpdateMemberRequest.builder()
				.conversationId(CONVERSATION_ID)
				.memberId(MEMBER_ID)
				.state(MemberState.JOINED)
				.from(MEMBER_FROM);

		stubResponse(200, SAMPLE_MEMBER_RESPONSE);
		assertEqualsSampleMember(client.updateMember(builder.build()));

		stubResponseAndAssertThrows(SAMPLE_MEMBER_RESPONSE,
				() -> client.updateMember(null),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(SAMPLE_MEMBER_RESPONSE,
				() -> client.updateMember(builder.memberId(KNOCKING_ID_STR).build()),
				IllegalArgumentException.class
		);
		builder.memberId(MEMBER_ID);
		stubResponseAndAssertThrows(SAMPLE_MEMBER_RESPONSE,
				() -> client.updateMember(builder.conversationId(MEMBER_ID_INVITING).build()),
				IllegalArgumentException.class
		);
		builder.conversationId(CONVERSATION_ID);
		assertResponseExceptions(() -> client.updateMember(builder.build()));
	}

	@Test
	public void testUpdateMemberEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<UpdateMemberRequest, Member>() {
			final Supplier<UpdateMemberRequest.Builder> builderFactoryNoState = () ->
					UpdateMemberRequest.builder().conversationId(CONVERSATION_ID).memberId(MEMBER_ID);

			@Override
			protected RestEndpoint<UpdateMemberRequest, Member> endpoint() {
				return client.updateMember;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.PATCH;
			}

			@Override
			protected String expectedEndpointUri(UpdateMemberRequest request) {
				return "/v1/conversations/"+request.conversationId+"/members/"+request.resourceId;
			}

			@Override
			protected UpdateMemberRequest sampleRequest() {
				return builderFactoryNoState.get()
						.state(MemberState.LEFT).from(MEMBER_FROM)
						.code(REASON_CODE).text(REASON_TEXT).build();
			}

			@Override
			protected String sampleRequestBodyString() {
				var req = sampleRequest();
				return STR."""
					{"state":"LEFT","from":"\{req.getFrom()}",\
					"reason":{"code":"\{req.getCode()}","text":"\{req.getText()}"}}""";
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testInvalidState();
				testReasonCodeIsValidForLeftStateOnly();
				testReasonTextIsValidForLeftStateOnly();
			}

			void testInvalidState() {
				for (var state : MemberState.values()) {
					var builder = builderFactoryNoState.get().state(state);
					switch (state) {
						case LEFT, JOINED -> assertEquals(state, builder.build().getState());
						default -> assertThrows(IllegalArgumentException.class, builder::build);
					}
				}
			}

			void testReasonCodeIsValidForLeftStateOnly() {
				var builder = builderFactoryNoState.get().code(REASON_CODE);
				assertThrows(NullPointerException.class, builder::build);
				var valid = builder.state(MemberState.LEFT).build();
				assertEquals(REASON_CODE, valid.getCode());
				assertEquals(MemberState.LEFT, valid.getState());
				assertEquals(MEMBER_ID, valid.getMemberId());
				assertEquals(CONVERSATION_ID, valid.getConversationId());
				assertNull(valid.getFrom());
				assertNull(valid.getText());
				builder.state(MemberState.JOINED);
				assertThrows(IllegalStateException.class, builder::build);
			}

			void testReasonTextIsValidForLeftStateOnly() {
				var builder = builderFactoryNoState.get().text(REASON_TEXT);
				assertThrows(NullPointerException.class, builder::build);
				var valid = builder.state(MemberState.LEFT).build();
				assertEquals(REASON_TEXT, valid.getText());
				assertEquals(MemberState.LEFT, valid.getState());
				assertEquals(MEMBER_ID, valid.getMemberId());
				assertEquals(CONVERSATION_ID, valid.getConversationId());
				assertNull(valid.getFrom());
				assertNull(valid.getCode());
				builder.state(MemberState.JOINED);
				assertThrows(IllegalStateException.class, builder::build);
			}
		}
		.runTests();
	}


	@Test
	public void testDeleteEvent() throws Exception {
		stubResponseAndRun(204, () -> client.deleteEvent(CONVERSATION_ID, EVENT_ID));
		stubResponseAndAssertThrows(204,
				() -> client.deleteEvent(null, EVENT_ID),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(204,
				() -> client.deleteEvent(MEMBER_ID, EVENT_ID),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(204,
				() -> client.deleteEvent(CONVERSATION_ID, -1),
				IllegalArgumentException.class
		);
		assert401ResponseException(() -> client.deleteEvent(CONVERSATION_ID, EVENT_ID));
	}

	@Test
	public void testDeleteEventEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<ConversationResourceRequestWrapper, Void>() {

			@Override
			protected RestEndpoint<ConversationResourceRequestWrapper, Void> endpoint() {
				return client.deleteEvent;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.DELETE;
			}

			@Override
			protected String expectedEndpointUri(ConversationResourceRequestWrapper request) {
				return "/v1/conversations/"+request.conversationId+"/events/"+request.resourceId;
			}

			@Override
			protected ConversationResourceRequestWrapper sampleRequest() {
				return new ConversationResourceRequestWrapper(CONVERSATION_ID, String.valueOf(EVENT_ID));
			}
		}
		.runTests();
	}


	@Test
	public void testGetEvent() throws Exception {
		stubResponse(200, SAMPLE_EVENT_RESPONSE);
		assertEqualsSampleEvent(client.getEvent(CONVERSATION_ID, EVENT_ID));
		stubResponseAndAssertThrows(200,
				() -> client.getEvent(null, EVENT_ID),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(200,
				() -> client.getEvent(USER_ID, EVENT_ID),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(200,
				() -> client.getEvent(CONVERSATION_ID, -EVENT_ID),
				IllegalArgumentException.class
		);
		assert401ResponseException(() -> client.getEvent(CONVERSATION_ID, EVENT_ID));
	}

	@Test
	public void testGetEventEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<ConversationResourceRequestWrapper, Event>() {

			@Override
			protected RestEndpoint<ConversationResourceRequestWrapper, Event> endpoint() {
				return client.getEvent;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ConversationResourceRequestWrapper request) {
				return "/v1/conversations/"+request.conversationId+"/events/"+request.resourceId;
			}

			@Override
			protected ConversationResourceRequestWrapper sampleRequest() {
				return new ConversationResourceRequestWrapper(CONVERSATION_ID, String.valueOf(EVENT_ID));
			}
		}
		.runTests();
	}

	@Test
	public void testListEvents() throws Exception {
		var request = ListEventsRequest.builder().build();
		stubResponse(200, SAMPLE_LIST_EVENTS_RESPONSE);
		var response = client.listEvents(CONVERSATION_ID, request);
		assertEqualsSampleListEvents(response);
		stubResponse(200, SAMPLE_LIST_EVENTS_RESPONSE);
		var events = client.listEvents(CONVERSATION_ID);
		assertEquals(response.getEvents(), events);

		stubResponseAndAssertThrows(200, SAMPLE_LIST_EVENTS_RESPONSE,
				() -> client.listEvents(null, request),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(200, SAMPLE_LIST_EVENTS_RESPONSE,
				() -> client.listEvents(CONVERSATION_ID, null),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(200, SAMPLE_LIST_EVENTS_RESPONSE,
				() -> client.listEvents(MEMBER_ID_INVITING, request),
				IllegalArgumentException.class
		);
		assert401ResponseException(() -> client.listEvents(CONVERSATION_ID));
	}

	@Test
	public void testListEventsEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<ListEventsRequest, ListEventsResponse>() {

			@Override
			protected RestEndpoint<ListEventsRequest, ListEventsResponse> endpoint() {
				return client.listEvents;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ListEventsRequest request) {
				return "/v1/conversations/"+request.conversationId+"/events/";
			}

			@Override
			protected ListEventsRequest sampleRequest() {
				var request = ListEventsRequest.builder()
						.excludeDeletedEvents(EXCLUDE_DELETED_EVENTS)
						.startId(EVENT_START_ID).endId(EVENT_END_ID)
						.eventType(KNOWN_EVENT_TYPE).build();

				assertEquals(EXCLUDE_DELETED_EVENTS, request.getExcludeDeletedEvents());
				assertEquals(EVENT_START_ID, request.getStartId());
				assertEquals(EVENT_END_ID, request.getEndId());
				assertEquals(KNOWN_EVENT_TYPE, request.getEventType());

				return request;
			}

			@Override
			protected Map<String, String> sampleQueryParams() {
				return Map.of(
					"exclude_deleted_events", String.valueOf(EXCLUDE_DELETED_EVENTS),
					"start_id", String.valueOf(EVENT_START_ID),
					"end_id", String.valueOf(EVENT_END_ID),
					"event_type", KNOWN_EVENT_TYPE_STR
				);
			}
		}
		.runTests();
	}

	@Test
	public void testCreateEvent() throws Exception {
		Event request = CustomEvent.builder().from(MEMBER_FROM).body(CONVERSATION_CUSTOM_DATA).build();
		stubResponse(201, SAMPLE_EVENT_RESPONSE);
		assertEqualsSampleEvent(client.createEvent(CONVERSATION_ID, request));

		stubResponseAndAssertThrows(201, SAMPLE_EVENT_RESPONSE,
				() -> client.createEvent(USER_ID, request),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(201, SAMPLE_EVENT_RESPONSE,
				() -> client.createEvent(CONVERSATION_ID, null),
				NullPointerException.class
		);
		assert401ResponseException(() -> client.createEvent(CONVERSATION_ID, request));
	}

	@Test
	public void testCreateEventEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<Event, Event>() {

			@Override
			protected RestEndpoint<Event, Event> endpoint() {
				return client.createEvent;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(Event request) {
				return "/v1/conversations/"+request.conversationId+"/events/";
			}

			@Override
			protected AudioPlayStopEvent sampleRequest() {
				return AudioPlayStopEvent.builder().from(MEMBER_FROM).playId(RANDOM_UUID).build();
			}

			@Override
			protected String sampleRequestBodyString() {
				var request = sampleRequest();
				return STR."""
					{"type":"\{request.getType()}","from":"\{request.getFrom()}","body":\
					{"play_id":"\{request.getPlayId()}"}}\
					""";
			}
		}
		.runTests();
	}
}