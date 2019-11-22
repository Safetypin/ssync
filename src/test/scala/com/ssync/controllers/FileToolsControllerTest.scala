package com.ssync.controllers

import java.io.FileNotFoundException
import java.nio.file.FileAlreadyExistsException

import com.ssync.controllers.DataUtils._
import com.ssync.controllers.FileToolUtils._
import org.scalatest.Matchers._
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FlatSpec}

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

  "collectFilesBasedOnExtensions" should "return 1 txt file based on txt extension" in {
    val subFolder1 = s"$source$getSeparator" + "sub 1"
    val returnedFiles = collectFilesBasedOnExtensions(subFolder1, List("txt"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 1
    returnedFiles.head.name shouldEqual "first.txt"
  }
  it should "return 1 txt and 1 TXT file based on txt extension" in {
    val subFolder2 = s"$source$getSeparator" + "sub 2"
    val returnedFiles = collectFilesBasedOnExtensions(subFolder2, List("txt"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 2
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
  }
  it should "return 1 txt, 1 TXT and 1 jpg file based on txt, jpg extension" in {
    val subFolder2 = s"$source$getSeparator" + "sub 2"
    val returnedFiles = collectFilesBasedOnExtensions(subFolder2, List("txt", "jpg"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 3
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
  }
  it should "return 1 txt, 1 TXT and 1 jpg file based on * extension" in {
    val subFolder2 = s"$source$getSeparator" + "sub 2"
    val returnedFiles = collectFilesBasedOnExtensions(subFolder2, List("*"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 3
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
  }
  it should "return 1 txt, 1 TXT and 1 jpg file based on * in a list of extensions" in {
    val subFolder2 = s"$source$getSeparator" + "sub 2"
    val returnedFiles = collectFilesBasedOnExtensions(subFolder2, List("*", "txt"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 3
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
  }
}
