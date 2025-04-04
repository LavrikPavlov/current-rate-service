package ru.kazan.currencyrateservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import ru.kazan.currencyrateservice.handler.KafkaHandler;

import java.util.HashMap;
import java.util.UUID;

@Configuration
public class KafkaConfig {

    /**
     * Дефолтный сервер кафки
     */
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String defaultServer;

    @Value(value = "${spring.kafka.batch}")
    private int maxBatchSize;

    @Value(value = "${app.name}")
    private String serviceName;

    public static final String KAFKA_TOPIC_NAME_RATE_REQUEST = "current.rate.info.rq";
    public static final String KAFKA_TOPIC_NAME_RATE_RESPONSE = "current.rate.info.rs";

    public static final String KAFKA_TOPIC_NAME_USER_REQUEST = "current.rate.user.rq";
    public static final String KAFKA_TOPIC_NAME_USER_RESPONSE = "current.rate.user.rs";

    public static final String DEFAULT_KAFKA = "kafkaTemplate";

    @Bean
    public StringJsonMessageConverter kafkaMessageConverter(){
        return new StringJsonMessageConverter();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> defaultConsumerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(getDefault(defaultServer, serviceName));
        factory.setCommonErrorHandler(new KafkaHandler());
        factory.setRecordMessageConverter(kafkaMessageConverter());
        return factory;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return getKafkaTemplate(defaultServer, false, maxBatchSize);
    }

    private ConsumerFactory<String, String> getDefault(String server, String groupId) {
        var consumerFactoryConfig = new HashMap<String, Object>();
        consumerFactoryConfig.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerFactoryConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerFactoryConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerFactoryConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerFactoryConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        consumerFactoryConfig.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        return new DefaultKafkaConsumerFactory<>(consumerFactoryConfig);
    }

    private KafkaTemplate<String, String> getKafkaTemplate(String server, boolean toggleTranslation, int batchSize) {
        var config = new HashMap<String, Object>();

        if (toggleTranslation) {
            config.put(ProducerConfig.ACKS_CONFIG, "all");
            config.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, UUID.randomUUID().toString());
        }

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, toggleTranslation);
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        var producerFactory = new DefaultKafkaProducerFactory<String, String>(config);
        return new KafkaTemplate<>(producerFactory);
    }
}
