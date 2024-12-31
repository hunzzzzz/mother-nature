package com.nature.mother.common.utility

import com.fasterxml.jackson.databind.ObjectMapper
import com.nature.mother.common.variables.Logs.getKafkaLog
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(KafkaProducer::class.java)

    fun send(topic: String, data: Any) {
        val str = objectMapper.writeValueAsString(data)

        kafkaTemplate.send(topic, str)
        logger.info(getKafkaLog(topic = topic))
    }
}