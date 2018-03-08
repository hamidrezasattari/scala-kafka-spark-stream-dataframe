package com.hrsat.streaming

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._



/**
  * Created by hamid on 01/03/18.
  */
package object kafka {

  object Config {
    val env = if (System.getenv("SCALA_ENV") == null) "development" else System.getenv("SCALA_ENV")

    val conf = ConfigFactory.load()
    def apply() = conf.getConfig(env)
  }

  val config=Config()

  val spark = SparkSession

    .builder

    .appName("spark-kafka-stream")

    .master(config.getString("master"))

    .getOrCreate()



  val schema = StructType(
    Array(
      StructField("customer_id", StringType, false),
      StructField("id", IntegerType, false),
      StructField("page_viewed", StringType, true),
      StructField("orders", ArrayType(StructType(Array(
        StructField("order_id", StringType),
        StructField("price", IntegerType)
      ))),true),
      StructField("ts", StringType, false),
      StructField("type", StringType, false)
    )
  )

}
