package com.hrsat.streaming

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SparkSession


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





}
