package com.ssync.controllers

import java.io.FileNotFoundException
import java.nio.file.FileAlreadyExistsException

import better.files.File
import com.ssync.controllers.DataUtils._
import com.ssync.controllers.FileToolUtils._
import com.ssync.models.FileState._
import com.ssync.models.SyncFileItem
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

  "filterFilesBasedOnIncludedExtensions" should "return 1 txt file based on txt extension" in {
    val subDirectory1 = s"$sourcePath$getSeparator" + "sub 1"
    val subDirectory1Files = collectFiles(subDirectory1)
    val returnedFiles = filterFilesBasedOnIncludedExtensions(subDirectory1Files, List("txt"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 1
    returnedFiles.head.name shouldEqual "first.txt"
  }
  it should "return 1 txt and 1 TXT file based on txt extension" in {
    val subDirectory2 = s"$sourcePath$getSeparator" + "sub 2"
    val subDirectory2Files = collectFiles(subDirectory2)
    val returnedFiles = filterFilesBasedOnIncludedExtensions(subDirectory2Files, List("txt"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 2
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
  }
  it should "return 1 txt, 1 TXT and 1 jpg file based on txt, jpg extension" in {
    val subDirectory2 = s"$sourcePath$getSeparator" + "sub 2"
    val subDirectory2Files = collectFiles(subDirectory2)
    val returnedFiles = filterFilesBasedOnIncludedExtensions(subDirectory2Files, List("txt", "jpg"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 4
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("test1.JPG")) shouldEqual true
  }
  it should "return 1 txt, 1 TXT and 1 jpg file based on * extension" in {
    val subDirectory2 = s"$sourcePath$getSeparator" + "sub 2"
    val subDirectory2Files = collectFiles(subDirectory2)
    val returnedFiles = filterFilesBasedOnIncludedExtensions(subDirectory2Files, List("*"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 4
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("test1.JPG")) shouldEqual true
  }
  it should "return 1 txt, 1 TXT and 1 jpg file based on * in a list of extensions" in {
    val subDirectory2 = s"$sourcePath$getSeparator" + "sub 2"
    val subDirectory2Files = collectFiles(subDirectory2)
    val returnedFiles = filterFilesBasedOnIncludedExtensions(subDirectory2Files, List("*", "txt"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 4
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("test1.JPG")) shouldEqual true
  }
  it should "return 2 txt, 2 TXT and 2 jpg file based on * extension with a sub Directory" in {
    val subDirectory3 = s"$sourcePath$getSeparator" + "sub 3"
    val subDirectory3Files = collectFiles(subDirectory3)
    val returnedFiles = filterFilesBasedOnIncludedExtensions(subDirectory3Files, List("*", "txt"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 6
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.count(f => f.name.equals("first.txt")) shouldEqual 2
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    returnedFiles.count(f => f.name.equals("second.TXT")) shouldEqual 2
    returnedFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    returnedFiles.count(f => f.name.equals("test.jpg")) shouldEqual 2
  }
  it should "return 2 txt, 2 TXT and 2 jpg file based on txt, jpg extensions with a sub Directory" in {
    val subDirectory3 = s"$sourcePath$getSeparator" + "sub 3"
    val subDirectory3Files = collectFiles(subDirectory3)
    val returnedFiles = filterFilesBasedOnIncludedExtensions(subDirectory3Files, List("jpg", "txt"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 6
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.count(f => f.name.equals("first.txt")) shouldEqual 2
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    returnedFiles.count(f => f.name.equals("second.TXT")) shouldEqual 2
    returnedFiles.exists(f => f.name.equals("test.jpg")) shouldEqual true
    returnedFiles.count(f => f.name.equals("test.jpg")) shouldEqual 2
  }

  "filterFilesBasedOnIgnoredExtensions" should "return all 2 files based on empty List" in {
    val subDirectory1 = s"$sourcePath$getSeparator" + "sub 1"
    val subDirectory1Files = collectFiles(subDirectory1)
    val returnedFiles = filterFilesBasedOnIgnoredExtensions(subDirectory1Files, List())
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 2
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("firstj.jpg")) shouldEqual true
  }
  it should "return 1 txt file based on jpg extension" in {
    val subDirectory1 = s"$sourcePath$getSeparator" + "sub 1"
    val subDirectory1Files = collectFiles(subDirectory1)
    val returnedFiles = filterFilesBasedOnIgnoredExtensions(subDirectory1Files, List("jpg"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 1
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
  }
  it should "return 1 txt and 1 TXT file based on jpg extension" in {
    val subDirectory2 = s"$sourcePath$getSeparator" + "sub 2"
    val subDirectory2Files = collectFiles(subDirectory2)
    val returnedFiles = filterFilesBasedOnIgnoredExtensions(subDirectory2Files, List("jpg"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 2
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
  }
  it should "return no files based on * extension" in {
    val subDirectory2 = s"$sourcePath$getSeparator" + "sub 2"
    val subDirectory2Files = collectFiles(subDirectory2)
    val returnedFiles = filterFilesBasedOnIgnoredExtensions(subDirectory2Files, List("*"))
    returnedFiles.isEmpty shouldEqual true
    returnedFiles.length shouldEqual 0
  }
  it should "return no files based on * in a list of extensions" in {
    val subDirectory2 = s"$sourcePath$getSeparator" + "sub 2"
    val subDirectory2Files = collectFiles(subDirectory2)
    val returnedFiles = filterFilesBasedOnIgnoredExtensions(subDirectory2Files, List("*", "jpg"))
    returnedFiles.isEmpty shouldEqual true
    returnedFiles.length shouldEqual 0
  }
  it should "return 2 txt, 2 TXT  file based on jpg extension with a sub Directory" in {
    val subDirectory3 = s"$sourcePath$getSeparator" + "sub 3"
    val subDirectory3Files = collectFiles(subDirectory3)
    val returnedFiles = filterFilesBasedOnIgnoredExtensions(subDirectory3Files, List("jpg"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 4
    returnedFiles.exists(f => f.name.equals("first.txt")) shouldEqual true
    returnedFiles.count(f => f.name.equals("first.txt")) shouldEqual 2
    returnedFiles.exists(f => f.name.equals("second.TXT")) shouldEqual true
    returnedFiles.count(f => f.name.equals("second.TXT")) shouldEqual 2
  }
  it should "return no files based on txt, jpg extensions with a sub Directory" in {
    val subDirectory3 = s"$sourcePath$getSeparator" + "sub 3"
    val subDirectory3Files = collectFiles(subDirectory3)
    val returnedFiles = filterFilesBasedOnIgnoredExtensions(subDirectory3Files, List("jpg", "txt"))
    returnedFiles.isEmpty shouldEqual true
    returnedFiles.length shouldEqual 0
  }
  it should "return 2 txt file based on jpg extension" in {
    val subDirectory6 = s"$sourcePath$getSeparator" + "sub 6"
    val subDirectory6Files = collectFiles(subDirectory6)
    val returnedFiles = filterFilesBasedOnIgnoredExtensions(subDirectory6Files, List("ds_store"))
    returnedFiles.isEmpty shouldEqual false
    returnedFiles.length shouldEqual 2
    returnedFiles.exists(f => f.name.equals("testfile.txt")) shouldEqual true
    returnedFiles.exists(f => f.name.equals("testfile2.txt")) shouldEqual true
  }

  "moveFiles" should "move file from the source Directory to destination" in {
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
  it should "move file from the source Directory to destination if destination doesn't exist" in {
    val file = File(s"$sourcePath$getSeparator" + "sub 1" + getSeparator + "firstj.jpg")
    val destinationSubPath = s"$destinationPath$getSeparator" + "sub 1"
    val syncFileItems = List(SyncFileItem(file, File(destinationSubPath)))
    val results = moveFiles(syncFileItems).head
    results.State shouldEqual MOVED
    results.MoveError shouldEqual None
    results.FileItem.parent.pathAsString shouldEqual destinationSubPath
  }
  it should "move file from the source Directory to destination if destination sub sub Directory doesn't exist" in {
    val file = File(s"$sourcePath$getSeparator" + "sub 3" + getSeparator + "sub" + getSeparator + "test.jpg")
    val destinationSubPath = s"$destinationPath$getSeparator" + "sub 3" + getSeparator + "sub"
    val syncFileItems = List(SyncFileItem(file, File(destinationSubPath)))
    val results = moveFiles(syncFileItems).head
    results.State shouldEqual MOVED
    results.MoveError shouldEqual None
    results.FileItem.parent.pathAsString shouldEqual destinationSubPath
  }

  "renameFileBecauseItAlreadyExists" should "rename testfile.txt to testfile_XXXX.txt" in {
    val file = File(s"$sourcePath$getSeparator" + "testfile.txt")
    val fileName = "testfile"
    val syncFileItem = SyncFileItem(file, File(destinationPath))
    val result = renameFileBecauseItAlreadyExists(syncFileItem)
    val renamedFileName = result.FileItem.nameWithoutExtension
    renamedFileName should not equal fileName
    renamedFileName should include(fileName)
  }

  "collectSourceDirectoriesInOrder" should "return all directories from source directory" in {
    val sourceSubPath = s"$sourcePath$getSeparator" + "sub 4"

    val returnedDir = collectSourceDirectoriesInOrder(sourceSubPath, List(""))
    returnedDir.length shouldEqual 3
    returnedDir.exists(f => f.name.equals("sub")) shouldEqual true
    returnedDir.count(f => f.name.equals("sub")) shouldEqual 1
    returnedDir.exists(f => f.name.equals("sub 1")) shouldEqual true
    returnedDir.count(f => f.name.equals("sub 1")) shouldEqual 2
  }
  it should "return all directories from source directory excluding sub directory" in {
    val sourceSubPath = s"$sourcePath$getSeparator" + "sub 4"

    val returnedDir = collectSourceDirectoriesInOrder(sourceSubPath, List("sub"))
    returnedDir.length shouldEqual 2
    returnedDir.exists(f => f.name.equals("sub")) shouldEqual false
    returnedDir.exists(f => f.name.equals("sub 1")) shouldEqual true
    returnedDir.count(f => f.name.equals("sub 1")) shouldEqual 2
  }
  it should "return all directories from source directory excluding sub 1 directory" in {
    val sourceSubPath = s"$sourcePath$getSeparator" + "sub 4"

    val returnedDir = collectSourceDirectoriesInOrder(sourceSubPath, List("sub 1"))
    returnedDir.length shouldEqual 1
    returnedDir.exists(f => f.name.equals("sub 1")) shouldEqual false
    returnedDir.exists(f => f.name.equals("sub")) shouldEqual true
    returnedDir.count(f => f.name.equals("sub")) shouldEqual 1
  }

  "deleteEmptySourceDirectories" should "remove all empty sub Directorys in the correct order" in {
    val sourceSubPath = s"$sourcePath$getSeparator" + "sub 5"
    val filesToDelete = filterFilesBasedOnIncludedExtensions(collectFiles(sourceSubPath), List("*"))
    filesToDelete.foreach(_.delete(true))

    val returnedDir = collectSourceDirectoriesInOrder(sourceSubPath, List(""))
    deleteEmptySourceDirectories(returnedDir)
    val returnedDir2ndPass = collectSourceDirectoriesInOrder(sourceSubPath, List(""))
    returnedDir2ndPass.isEmpty shouldEqual true
  }
  it should "remove all empty sub Directories in the correct order except for " in {
    val sourceSubPath = s"$sourcePath$getSeparator" + "sub 5"
    val filesToDelete = filterFilesBasedOnIncludedExtensions(collectFiles(sourceSubPath), List("*"))
    filesToDelete.foreach(_.delete(true))

    val returnedDir = collectSourceDirectoriesInOrder(sourceSubPath, List("sub"))
    deleteEmptySourceDirectories(returnedDir)
    val returnedDir2ndPass = collectSourceDirectoriesInOrder(sourceSubPath, List(""))
    returnedDir2ndPass.isEmpty shouldEqual false
    returnedDir2ndPass.length shouldEqual 1
    returnedDir2ndPass.exists(f => f.name.equals("sub")) shouldEqual true
    returnedDir2ndPass.count(f => f.name.equals("sub")) shouldEqual 1
  }

}
