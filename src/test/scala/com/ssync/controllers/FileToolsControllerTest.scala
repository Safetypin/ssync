package com.ssync.controllers

import java.io.FileNotFoundException
import java.nio.file.FileAlreadyExistsException

import better.files.File
import com.ssync.controllers.DataUtils._
import com.ssync.controllers.FileToolUtils._
import com.ssync.models.FileState._
import com.ssync.models.{SyncFileItem, SyncItem}
import org.scalatest.Matchers._
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FlatSpec}

class FileToolsControllerTest extends FlatSpec
  with BeforeAndAfter
  with BeforeAndAfterEach
  with FileToolsController {

  override def beforeEach {
    setupTestResources
  }

  override def afterEach {
    teardownTestResources
  }

  "createFile" should "be able to create a file" in {
    val randomFileName = randomizedDestinationFilePath
    createFile(randomFileName, "")
    doesFileExist(randomFileName) shouldEqual true
    deleteFile(randomFileName)
  }
  it should "throw an FileAlreadyExistsException if file already exist" in {
    val randomFileName = randomizedDestinationFilePath
    createFile(randomFileName, "")
    intercept[FileAlreadyExistsException](createFile(randomFileName, ""))
    deleteFile(randomFileName)
  }
  it should "throw an FileNotFoundException if directory doesn't exist" in {
    val randomFileName = randomizedDestinationDirectoryFilePath
    intercept[FileNotFoundException](createFile(randomFileName, ""))
//    deleteFile(randomFileName)
  }

  "openAndReadFile" should "be able to read text file" in {
    val randomFileName = randomizedDestinationFilePath
    createFile(randomFileName, "test")
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
    val result = openAndReadFile(sourcePath)
    result.isFailure shouldEqual true
    result.failed.get.isInstanceOf[FileNotFoundException]
  }

  "doesDirectoryExist" should "be return true if directory exists" in {
    doesDirectoryExist(sourcePath) shouldEqual true
  }
  it should "be return false if directory does not exists" in {
    doesDirectoryExist(s"$sourcePath$randomString") shouldEqual false
  }
  it should "be return false if file" in {
    val randomFileName = randomizedDestinationFilePath
    createFile(randomFileName, "test")
    doesDirectoryExist(randomFileName) shouldEqual false
  }

  "collectFilesBasedOnExtensions" should "return 1 txt file based on txt extension" in {
    val subFolder1 = s"$sourcePath$getSeparator" + "sub 1"
    val returnedFiles = collectFilesBasedOnExtensions(subFolder1, List("txt"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 1
    returnedFiles.head.name shouldEqual "first.txt"
  }
  it should "return 1 txt and 1 TXT file based on txt extension" in {
    val subFolder2 = s"$sourcePath$getSeparator" + "sub 2"
    val returnedFiles = collectFilesBasedOnExtensions(subFolder2, List("txt"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 2
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
  }
  it should "return 1 txt, 1 TXT and 1 jpg file based on txt, jpg extension" in {
    val subFolder2 = s"$sourcePath$getSeparator" + "sub 2"
    val returnedFiles = collectFilesBasedOnExtensions(subFolder2, List("txt", "jpg"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 3
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
  }
  it should "return 1 txt, 1 TXT and 1 jpg file based on * extension" in {
    val subFolder2 = s"$sourcePath$getSeparator" + "sub 2"
    val returnedFiles = collectFilesBasedOnExtensions(subFolder2, List("*"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 3
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
  }
  it should "return 1 txt, 1 TXT and 1 jpg file based on * in a list of extensions" in {
    val subFolder2 = s"$sourcePath$getSeparator" + "sub 2"
    val returnedFiles = collectFilesBasedOnExtensions(subFolder2, List("*", "txt"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 3
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
  }
  it should "return 2 txt, 2 TXT and 2 jpg file based on * extension with a sub folder" in {
    val subFolder3 = s"$sourcePath$getSeparator" + "sub 3"
    val returnedFiles = collectFilesBasedOnExtensions(subFolder3, List("*", "txt"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 6
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.filter(f => f.name.equals("first.txt")).length shouldEqual 2
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    returnedFiles.filter(f => f.name.equals("second.TXT")).length shouldEqual 2
    returnedFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    returnedFiles.filter(f => f.name.equals("test.jpg")).length shouldEqual 2
  }
  it should "return 2 txt, 2 TXT and 2 jpg file based on txt, jpg extensions with a sub folder" in {
    val subFolder3 = s"$sourcePath$getSeparator" + "sub 3"
    val returnedFiles = collectFilesBasedOnExtensions(subFolder3, List("jpg", "txt"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 6
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.filter(f => f.name.equals("first.txt")).length shouldEqual 2
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    returnedFiles.filter(f => f.name.equals("second.TXT")).length shouldEqual 2
    returnedFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    returnedFiles.filter(f => f.name.equals("test.jpg")).length shouldEqual 2
  }

  "moveFiles" should "move file from the source folder to destination" in {
    val file = File(s"$sourcePath$getSeparator" + "testfile2.txt")
    val syncFileItems = List(SyncFileItem(file, File(destinationPath)))
    val results = moveFiles(syncFileItems).head
    results.State shouldEqual MOVED
    results.MoveError shouldEqual None
    results.FileItem.parent.pathAsString shouldEqual destinationPath
    results.FileItem.exists shouldEqual true
    file.exists shouldEqual false
  }
  it should "return MOVED and rename file if file already exists in the destination" in {
    val file = File(s"$sourcePath$getSeparator" + "testfile.txt")
    val syncFileItems = List(SyncFileItem(file, File(destinationPath)))
    val results = moveFiles(syncFileItems).head
    results.State shouldEqual MOVED
    results.MoveError shouldEqual None
    results.FileItem.parent.pathAsString shouldEqual destinationPath
    results.FileItem.exists shouldEqual true
    file.exists shouldEqual false
    results.FileItem.nameWithoutExtension should not equal file.nameWithoutExtension
  }
  it should "move file from the source folder to destination if destination doesn't exist" in {
    val file = File(s"$sourcePath$getSeparator" + "sub 1" + getSeparator + "firstj.jpg")
    val destinationSubPath = s"$destinationPath$getSeparator" + "sub 1"
    val syncFileItems = List(SyncFileItem(file, File(destinationSubPath)))
    val results = moveFiles(syncFileItems).head
    results.State shouldEqual MOVED
    results.MoveError shouldEqual None
    results.FileItem.parent.pathAsString shouldEqual destinationSubPath
  }
  it should "move file from the source folder to destination if destination sub sub folder doesn't exist" in {
    val file = File(s"$sourcePath$getSeparator" + "sub 3" + getSeparator + "sub" + getSeparator + "test.jpg")
    val destinationSubPath = s"$destinationPath$getSeparator" + "sub 3" + getSeparator + "sub"
    val syncFileItems = List(SyncFileItem(file, File(destinationSubPath)))
    val results = moveFiles(syncFileItems).head
    results.State shouldEqual MOVED
    results.MoveError shouldEqual None
    results.FileItem.parent.pathAsString shouldEqual destinationSubPath
  }
}
