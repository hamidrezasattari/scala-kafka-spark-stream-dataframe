package com.hrsat.streaming.kafka

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory
import org.apache.spark.sql.functions._
import org.apache.spark.sql.functions.to_json

import scala.util.Random

/**
 * Created by hamid on 27/02/18.
 */
object Producer extends  App {
 val logger = LoggerFactory.getLogger(this.getClass)



  val props = new Properties()
  props.put("bootstrap.servers", config.getString("kafka.brokers"))
  props.put("client.id", config.getString("kafka.clientid"))
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val streamingDF =spark.read.option("header", true).option("inferSchema", "true").
    json(config.getString("filesystem.source.events"))

  val producer = new KafkaProducer[String, String](props)


  streamingDF.selectExpr("CAST(id AS INTEGER) AS key", "to_json(struct(*)) AS value").foreach(row=>{

    val data = new ProducerRecord[String, String]( config.getString("kafka.topic"), row.getInt(0).toString, row.getString(1))
    producer.send(data)
  } )



  producer.close()
  spark.stop()


}
