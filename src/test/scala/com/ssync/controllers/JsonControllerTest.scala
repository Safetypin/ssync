package com.ssync.controllers

import com.ssync.controllers.DataUtils._
import org.scalatest.Matchers._
import org.scalatest._

class JsonControllerTest extends FlatSpec
  with BeforeAndAfter
  with JsonController {

  "convertSettingsToJson" should "be able to convert Settings to JSON" in {
    convertSettingsToJson(defaultSettings) shouldEqual testJSON
  }

  "convertJsonToSettings" should "be able to convert JSON to PodcastSettings" in {
    val result = convertJsonToSettings(testJSON.toString)
    result.Destination shouldEqual defaultSettings.Destination
    result.Source shouldBe defaultSettings.Source
    result.Extensions shouldBe defaultSettings.Extensions
    result.IgnoredExtensions shouldEqual defaultSettings.IgnoredExtensions
    result.SyncItems == defaultSettings.SyncItems
  }
}
