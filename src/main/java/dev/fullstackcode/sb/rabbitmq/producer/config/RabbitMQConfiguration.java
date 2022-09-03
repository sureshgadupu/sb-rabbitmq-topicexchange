package dev.fullstackcode.sb.rabbitmq.producer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Bean
    Queue queueA() {
        return new Queue("queue.A", false);
    }

    @Bean
    Queue queueB() {
        return new Queue("queue.B", false);
    }

    @Bean
    Queue queueC() {
        return new Queue("queue.C", false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("exchange.topic");
    }

    @Bean
    Binding bindingA(Queue queueA, TopicExchange exchange) {
        return BindingBuilder.bind(queueA).to(exchange).with("report.monthly.#");
    }

    @Bean
    Binding bindingB(Queue queueB, TopicExchange exchange) {
        return BindingBuilder.bind(queueB).to(exchange).with("report.*.weekly");
    }

    @Bean
    Binding bindingC(Queue queueC, TopicExchange exchange) {
        return BindingBuilder.bind(queueC).to(exchange).with("report.#");
    }

    @Bean
    ApplicationRunner runner(ConnectionFactory cf) {
        return args -> cf.createConnection().close();
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

}
