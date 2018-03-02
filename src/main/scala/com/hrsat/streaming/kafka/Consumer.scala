package com.hrsat.streaming.kafka


import java.sql.Timestamp

import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory
import org.apache.spark.sql.functions.from_json
import org.apache.spark.sql.functions._

/**
 * Created by hamid on 27/02/18.
 */
object Consumer extends  App {
 val logger = LoggerFactory.getLogger(this.getClass)

 val df = spark

   .readStream

   .format("kafka")

   .option("kafka.bootstrap.servers", config.getString("kafka.brokers"))

   .option("subscribe", config.getString("kafka.topic"))
 /** TODO startingOffsets: set as json string
  {"topicA":{"0":23,"1":-1},"topicB":{"0":-2}}
   */
   .option("startingOffsets", "latest")
   .load()
  import spark.implicits._


  val df1 = df.selectExpr("CAST(value AS STRING)", "CAST(timestamp AS TIMESTAMP)").as[(String, Timestamp)]


  df1.writeStream

    .format("console")

    .option("truncate","false")

    .start()

    .awaitTermination()


}
