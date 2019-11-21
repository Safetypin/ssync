package com.ssync.controllers

import java.io.FileNotFoundException
import java.nio.file.FileAlreadyExistsException

import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FlatSpec}
import com.ssync.controllers.DataUtils._
import org.scalatest.Matchers._

class FileToolsControllerTest extends FlatSpec
  with BeforeAndAfter
  with BeforeAndAfterEach
  with FileToolsController {

  "createFile" should "be able to create a file" in {
    val randomFileName = randomizedDestinationFilePath
    createFile(randomFileName,"")
    doesFileExist(randomFileName) shouldEqual true
    deleteFile(randomFileName)
  }
  it should "throw an exception if file already exist" in {
    val randomFileName = randomizedDestinationFilePath
    createFile(randomFileName,"")
    intercept[FileAlreadyExistsException](createFile(randomFileName,""))
    deleteFile(randomFileName)
  }
  it should "throw an exception if directory doesn't exist" in {
    val randomFileName = randomizedDestinationDirectoryFilePath
    intercept[FileNotFoundException](createFile(randomFileName,""))
    deleteFile(randomFileName)
  }
}
