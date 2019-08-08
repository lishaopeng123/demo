package cn.jiyun;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

/**
 * 
 * 点对点模式案例
 * @author lishaoping
 *
 */
public class QueueDemo {

	/**
	 * 生产者
	 * @throws 
	 */
	@Test
	public void producerMessage() throws Exception{
		
		//1.创建连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
		
		//2.创建连接
		Connection connection = connectionFactory.createConnection();
		
		//3.启动连接
		connection.start();
		
		//4.创建会话  参数1  是否开启事务  参数2 确认方式
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//5.创建一个队列（queue）
		Queue queue = session.createQueue("test_queue");
		
		//6.创建一个生产者
		MessageProducer producer = session.createProducer(queue);
		
		//7.创建一条消息
		TextMessage message = session.createTextMessage("activeMQ so easy...");
		
		//8.生产者发送消息
		producer.send(message);
		
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
		
	}
	
	/**
	 * 消费者
	 * @throws Exception 
	 */
	@Test
	public void consumerMessage() throws Exception{
		
		//1.创建创建连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
																				//windos 127.0.0.1				
		//2.创建连接
		Connection connection = connectionFactory.createConnection();
		
		//3.启动连接
		connection.start();
		
		//4.创建会话
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//5.创建队列对列对象
		Queue queue = session.createQueue("test_queue");//生产者对列名
		
		//6.创建消费者
		MessageConsumer consumer = session.createConsumer(queue);
		
		//7.消息者监听队列
		consumer.setMessageListener(new MessageListener() {//匿名内部类
			
			@Override
			public void onMessage(Message m) {
				// TODO Auto-generated method stub
				TextMessage message = (TextMessage)m;
				try {
					System.out.println("消费者收到的消息"+message.getText());
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		//8.让程序阻塞  //让程序不要走完一直监控
		System.in.read();//等待输入
		
		//9.关闭
		connection.close();
		session.close();
		consumer.close();
		
	}

}
