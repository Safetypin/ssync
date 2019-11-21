package com.ssync.controllers

import java.io.FileNotFoundException
import java.nio.file.FileAlreadyExistsException

import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FlatSpec}
import com.ssync.controllers.DataUtils._
import org.scalatest.Matchers._

import scala.util.Failure

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
  it should "throw an FileAlreadyExistsException if file already exist" in {
    val randomFileName = randomizedDestinationFilePath
    createFile(randomFileName,"")
    intercept[FileAlreadyExistsException](createFile(randomFileName,""))
    deleteFile(randomFileName)
  }
  it should "throw an FileNotFoundException if directory doesn't exist" in {
    val randomFileName = randomizedDestinationDirectoryFilePath
    intercept[FileNotFoundException](createFile(randomFileName,""))
    deleteFile(randomFileName)
  }

  "openAndReadFile" should "be able to read text file" in {
    val randomFileName = randomizedDestinationFilePath
    createFile(randomFileName,"test")
    val result = openAndReadFile(randomFileName)
    result.isSuccess shouldEqual true
    result.get shouldEqual "test"
  }
  it should "throw an FileNotFoundException if file does not exist" in {
    val randomFileName = randomizedDestinationFilePath
    val result = openAndReadFile(randomFileName)
    result.isFailure shouldEqual true
    result.failed.get.isInstanceOf[FileNotFoundException]
  }
  it should "throw an FileNotFoundException if file is directory" in {
    val result = openAndReadFile(source)
    result.isFailure shouldEqual true
    result.failed.get.isInstanceOf[FileNotFoundException]
  }

  "doesDirectoryExist" should "be return true if directory exists" in {
    doesDirectoryExist(source) shouldEqual true
  }
  it should "be return false if directory does not exists" in {
    doesDirectoryExist(s"$source$randomString") shouldEqual false
  }
  it should "be return false if file" in {
    val randomFileName = randomizedDestinationFilePath
    createFile(randomFileName,"test")
    doesDirectoryExist(randomFileName) shouldEqual false
  }
}
