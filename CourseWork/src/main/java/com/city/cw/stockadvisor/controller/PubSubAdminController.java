package com.city.cw.stockadvisor.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.city.cw.stockadvisor.gateway.OutboundChanel;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.StatusCode;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.spring.pubsub.support.PubSubSubscriptionUtils;
import com.google.cloud.spring.pubsub.support.PubSubTopicUtils;
import com.google.pubsub.v1.ProjectName;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PushConfig;
import com.google.pubsub.v1.Subscription;
import com.google.pubsub.v1.Topic;
import com.google.pubsub.v1.TopicName;

@Controller
public class PubSubAdminController {
	
	private final String projectId = "mini-share-market-advisor";
	
	private final TopicAdminClient topicAdminClient;
	
	private final SubscriptionAdminClient subscriptionAdminClient;
	
	protected static final int MIN_ACK_DEADLINE_SECONDS = 10;

	protected static final int MAX_ACK_DEADLINE_SECONDS = 600;
	
	/** Default inspired in the subscription creation web UI. */
	private int defaultAckDeadline = MIN_ACK_DEADLINE_SECONDS;
	
	private static final String NO_TOPIC_SPECIFIED_ERROR_MSG = "No topic name was specified.";

	
	
	public PubSubAdminController() throws IOException {
		 topicAdminClient = TopicAdminClient.create();
		 subscriptionAdminClient = SubscriptionAdminClient.create();
	}
	
	@Autowired
	OutboundChanel gateway;
	
	
	//To get topic from pub/sub.....
	public Topic getTopic(String topicName) {
		Assert.hasText(topicName, NO_TOPIC_SPECIFIED_ERROR_MSG);

		try {
			return this.topicAdminClient.getTopic(PubSubTopicUtils.toTopicName(topicName, this.projectId));
		}
		catch (ApiException aex) {
			if (aex.getStatusCode().getCode() == StatusCode.Code.NOT_FOUND) {
				return null;
			}

			throw aex;
		}
	}
	
// To create New Topic in pub/sub....
	
	public Topic createTopic(String topicName) {
		return this.topicAdminClient.createTopic(PubSubTopicUtils.toTopicName(topicName, this.projectId));
	}
//To delete Topic from Pub/sub....
	
	public void deleteTopic(String topicName) {
		this.topicAdminClient.deleteTopic(PubSubTopicUtils.toTopicName(topicName, this.projectId));
	}
//To list all the pub/sub topic from GCP.....
	
	public  List<Topic> listTopics() throws IOException {
			
		 List<Topic> topics = new ArrayList<>();
	        ProjectName projectName = ProjectName.of(projectId);

	        for (Topic topic : topicAdminClient.listTopics(projectName).iterateAll()) {
	            topics.add(topic);
	        }
	        return topics; 
			 
			}
	
	/**
	 * Create a new subscription on Google Cloud Pub/Sub.
	 *
	 * @param subscriptionName canonical subscription name, e.g., "subscriptionName", or the fully-qualified
	 * subscription name in the {@code projects/<project_name>/subscriptions/<subscription_name>} format
	 * @param topicName canonical topic name, e.g., "topicName", or the fully-qualified topic name in the
	 * {@code projects/<project_name>/topics/<topic_name>} format
	 * @param ackDeadline deadline in seconds before a message is resent, must be between 10
	 * and 600 seconds. If not provided, set to default of 10 seconds
	 * @param pushEndpoint the URL of the service receiving the push messages. If not
	 * provided, uses message pulling by default
	 * @return the created subscription
	 */
	@SuppressWarnings("deprecation")
	public Subscription createSubscription(String subscriptionName, String topicName,
			Integer ackDeadline, String pushEndpoint) {
		Assert.hasText(subscriptionName, "No subscription name was specified.");
		//Assert.hasText(topicName, NO_TOPIC_SPECIFIED_ERROR_MSG);

		int finalAckDeadline = this.defaultAckDeadline;
		if (ackDeadline != null) {
			validateAckDeadline(ackDeadline);
			finalAckDeadline = ackDeadline;
		}

		PushConfig.Builder pushConfigBuilder = PushConfig.newBuilder();
		if (pushEndpoint != null) {
			pushConfigBuilder.setPushEndpoint(pushEndpoint);
		}
		ProjectSubscriptionName subName = PubSubSubscriptionUtils.toProjectSubscriptionName(subscriptionName, this.projectId);
		TopicName topicname = PubSubTopicUtils.toTopicName(topicName, this.projectId);
		
		return this.subscriptionAdminClient.createSubscription(subName, topicname,	pushConfigBuilder.build(),
				finalAckDeadline);
	
	}

	/**
	 * Get the configuration of a Google Cloud Pub/Sub subscription.
	 *
	 * @param subscriptionName canonical subscription name, e.g., "subscriptionName", or the fully-qualified
	 * subscription name in the {@code projects/<project_name>/subscriptions/<subscription_name>} format
	 * @return subscription configuration or {@code null} if subscription doesn't exist
	 */
	@SuppressWarnings("deprecation")
	public Subscription getSubscription(String subscriptionName) {
		Assert.hasText(subscriptionName, "No subscription name was specified");
		
		ProjectSubscriptionName subName = PubSubSubscriptionUtils.toProjectSubscriptionName(subscriptionName, this.projectId);
		
		try {
			return this.subscriptionAdminClient.getSubscription(subName);
		}
		catch (ApiException aex) {
			if (aex.getStatusCode().getCode() == StatusCode.Code.NOT_FOUND) {
				return null;
			}

			throw aex;
		}
	}

	/**
	 * Delete a subscription from Google Cloud Pub/Sub.
	 *
	 * @param subscriptionName canonical subscription name, e.g., "subscriptionName", or the fully-qualified
	 * subscription name in the {@code projects/<project_name>/subscriptions/<subscription_name>} format
	 */
	@SuppressWarnings("deprecation")
	public void deleteSubscription(String subscriptionName) {
		Assert.hasText(subscriptionName, "No subscription name was specified");
		
		ProjectSubscriptionName subName = PubSubSubscriptionUtils.toProjectSubscriptionName(subscriptionName, this.projectId);
		
		this.subscriptionAdminClient.deleteSubscription(
			subName);
	}
	
	/**
	 * Return every subscription in a project.
	 * <p>If there are multiple pages, they will all be merged into the same result.
	 * @return a list of subscriptions
	 */
	public List<Subscription> listSubscriptions() {
		SubscriptionAdminClient.ListSubscriptionsPagedResponse subscriptionsPage =
				this.subscriptionAdminClient.listSubscriptions(ProjectName.of(this.projectId));

		List<Subscription> subscriptions = new ArrayList<>();
		subscriptionsPage.iterateAll().forEach(subscriptions::add);
		System.out.println(subscriptions);
		return Collections.unmodifiableList(subscriptions);
	}

	/**
	 * Get the default ack deadline.
	 * @return the default acknowledgement deadline value in seconds
	 */
	public int getDefaultAckDeadline() {
		return this.defaultAckDeadline;
	}

	/**
	 * Set the default acknowledgement deadline value.
	 *
	 * @param defaultAckDeadline default acknowledgement deadline value in seconds, must be between 10 and 600 seconds.
	 */
	public void setDefaultAckDeadline(int defaultAckDeadline) {
		validateAckDeadline(defaultAckDeadline);

		this.defaultAckDeadline = defaultAckDeadline;
	}
	
	private void validateAckDeadline(int ackDeadline) {
		Assert.isTrue(ackDeadline >= MIN_ACK_DEADLINE_SECONDS
						&& ackDeadline <= MAX_ACK_DEADLINE_SECONDS,
				"The acknowledgement deadline must be between "
						+ MIN_ACK_DEADLINE_SECONDS
						+ " and "
						+ MAX_ACK_DEADLINE_SECONDS
						+ " seconds.");
	}


}
