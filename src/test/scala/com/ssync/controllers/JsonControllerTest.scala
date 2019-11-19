package com.ssync.controllers

import com.ssync.controllers.DataUtils._
import org.scalatest.Matchers._
import org.scalatest._

class JsonControllerTest extends FlatSpec
  with BeforeAndAfter
  with JsonController {

  "ConvertSettingsToJson" should "be able to convert Settings to JSON" in {
    ConvertSettingsToJson(defaultSettings) shouldEqual testJSON
  }

  "ConvertJsonToSettings" should "be able to convert JSON to PodcastSettings" in {
    val result = ConvertJsonToSettings(testJSON.toString)
    result.destination shouldEqual defaultSettings.destination
    result.source shouldBe defaultSettings.source
    result.extensions shouldBe defaultSettings.extensions
  }
}
