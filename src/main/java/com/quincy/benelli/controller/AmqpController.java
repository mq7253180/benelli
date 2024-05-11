package com.quincy.benelli.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Controller
//@RequestMapping("/amqp")
public class AmqpController {
	private final static ConnectionFactory connectionFactory = new ConnectionFactory();
	@Value("${spring.rabbitmq.host}")
	private String host;
	@Value("${spring.rabbitmq.username}")
	private String username;
	@Value("${spring.rabbitmq.password}")
	private String password;
	private static Connection conn = null;
	private static Channel channel = null;
	private static Connection conn2 = null;
	private static Channel channel2 = null;
	private final static String EXCHANGE_NAME_PREFIX = "benelli.";
	private final static String[] EXCHANGE_NAMES = {EXCHANGE_NAME_PREFIX+"direct", EXCHANGE_NAME_PREFIX+"fanout", EXCHANGE_NAME_PREFIX+"topic"};
	private final static String QUEUE_NAME_PREFIX = "benelli.test.";
	private final static String[] QUEUE_NAMES = {QUEUE_NAME_PREFIX+"pull", QUEUE_NAME_PREFIX+"push"};
	private final static RoutingKeyGetter[] ROUTING_KEY_GETTERS = {
			new RoutingKeyGetter() {
				@Override
				public String get(String queueName) {
					return queueName.substring(queueName.lastIndexOf(".")+1, queueName.length());
				}
			}, 
			new RoutingKeyGetter() {
				@Override
				public String get(String queueName) {
					return "";
				}
			}, 
			new RoutingKeyGetter() {
				@Override
				public String get(String queueName) {
					return QUEUE_NAME_PREFIX+"#";
				}
			}
	};

	private interface RoutingKeyGetter {
		public String get(String queueName);
	}

//	@PostConstruct
	public void init() throws IOException, TimeoutException {
		connectionFactory.setHost(host);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		log.info("DEFAULT_RQBBITMQ_PORT================{}", connectionFactory.getPort());
		conn = connectionFactory.newConnection();
		channel = conn.createChannel(1);
		conn2 = connectionFactory.newConnection();
		channel2 = conn2.createChannel(2);
		for(int i=0;i<EXCHANGE_NAMES.length;i++) {
			String exchangeName = EXCHANGE_NAMES[i];
			RoutingKeyGetter routingKeyGetter = ROUTING_KEY_GETTERS[i];
			channel.exchangeDeclare(exchangeName, exchangeName.substring(exchangeName.lastIndexOf(".")+1, exchangeName.length()));
			for(String queueName:QUEUE_NAMES) {
				channel.queueDeclare(queueName, false, false, false, null);
				channel.queueBind(queueName, exchangeName, routingKeyGetter.get(queueName));
			}
		}
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
				super.handleDelivery(consumerTag, envelope, properties, body);
				log.warn("CONSUMER_MSG======================={}", new String(body, "UTF-8"));
				//发布的每一条消息都会获得一个唯一的deliveryTag，deliveryTag在channel范围内是唯一的 
                long deliveryTag = envelope.getDeliveryTag();
                //手动确认消息: 第二个参数是批量确认标志。如果值为true，则执行批量确认，此deliveryTag之前收到的消息全部进行确认; 如果值为false，则只对当前收到的消息进行确认
                channel.basicAck(deliveryTag, false);
			}
		};
		String consumerTag = channel.basicConsume(QUEUE_NAMES[1], false, consumer);//Push模式: autoAck: 是否自动确认消息, true自动确认, false不自动要手动调用
		log.info("CONSUMER_TAG================{}", consumerTag);

		consumer = new DefaultConsumer(channel2) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
				super.handleDelivery(consumerTag, envelope, properties, body);
				log.warn("CONSUMER_MSG_CHANNEL2======================={}", new String(body, "UTF-8"));
                long deliveryTag = envelope.getDeliveryTag();
                channel.basicAck(deliveryTag, false);
			}
		};
		consumerTag = channel.basicConsume(QUEUE_NAMES[1], false, consumer);
		log.info("CONSUMER_TAG_CHANNEL2================{}", consumerTag);
	}

	@RequestMapping("/pull/{index}")
	@ResponseBody
	public String pull(@PathVariable(required = true, name = "index")int index) throws IOException, TimeoutException {
		Channel channel = null;
		Long deliveryTag = null;
		try {
			channel = conn.createChannel(2);
			GetResponse response = channel.basicGet(QUEUE_NAMES[index], false);
			if(response!=null) {
				log.warn("MESSAGE_COUNT==============={}", response.getMessageCount());
				Envelope envelope = response.getEnvelope();
				deliveryTag = envelope.getDeliveryTag();
				log.warn("ENVELOPE.DeliveryTag-------------", envelope.getDeliveryTag());
				log.warn("ENVELOPE.Exchange-------------", envelope.getExchange());
				log.warn("ENVELOPE.RoutingKey-------------", envelope.getRoutingKey());
				BasicProperties properties = response.getProps();
				if(properties!=null) {
					log.warn("BASIC_PROPERTIES.AppId-------------", properties.getAppId());
					log.warn("BASIC_PROPERTIES.BodySize-------------", properties.getBodySize());
					log.warn("BASIC_PROPERTIES.ClassId-------------", properties.getClassId());
					log.warn("BASIC_PROPERTIES.ClassName-------------", properties.getClassName());
					log.warn("BASIC_PROPERTIES.ClusterId-------------", properties.getClusterId());
					log.warn("BASIC_PROPERTIES.ContentEncoding-------------", properties.getContentEncoding());
					log.warn("BASIC_PROPERTIES.ContentType-------------", properties.getContentType());
					log.warn("BASIC_PROPERTIES.CorrelationId-------------", properties.getCorrelationId());
					log.warn("BASIC_PROPERTIES.DeliveryMode-------------", properties.getDeliveryMode());
					log.warn("BASIC_PROPERTIES.Expiration-------------", properties.getExpiration());
					log.warn("BASIC_PROPERTIES.MessageId-------------", properties.getMessageId());
					log.warn("BASIC_PROPERTIES.Priority-------------", properties.getPriority());
					log.warn("BASIC_PROPERTIES.ReplyTo-------------", properties.getReplyTo());
					if(properties.getTimestamp()!=null)
						log.warn("BASIC_PROPERTIES.Timestamp-------------", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(properties.getTimestamp()));
					log.warn("BASIC_PROPERTIES.Type-------------", properties.getType());
					log.warn("BASIC_PROPERTIES.UserId-------------", properties.getUserId());
					if(properties.getHeaders()!=null) {
						Iterator<Entry<String, Object>> it = properties.getHeaders().entrySet().iterator();
						while(it.hasNext()) {
							Entry<String, Object> e = it.next();
							log.warn("{}---{}", e.getKey(), e.getValue().toString());
						}
					}
				}
				byte[] body = response.getBody();
				return new String(body, "UTF-8");
			} else
				return null;
		} finally {
			if(channel!=null) {
				if(deliveryTag!=null)
					channel.basicAck(deliveryTag, false);
				channel.close();
			}
		}
	}

	@PreDestroy
	public void destroy() {
		try {
			if(channel!=null) {
//				channel.queuePurge(QUEUE_NAME);
//				channel.exchangeDelete(EXCHANGE_NAME);
				channel.close();
			}
			if(conn!=null)
				conn.close();
			if(channel!=null)
				channel2.close();
			if(conn2!=null)
				conn2.close();
			log.warn("========================AMQP RELEASED");
		} catch (Exception e) {
			log.error("AMQP_ERR: ", e);
		}
	}
}