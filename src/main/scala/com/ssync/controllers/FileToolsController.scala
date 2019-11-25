package com.ssync.controllers

import java.io.{BufferedWriter, File, FileInputStream, FileWriter}
import java.nio.file.FileAlreadyExistsException

import better.files.{File => BFile}
import com.ssync.controllers.FileToolUtils._
import com.ssync.models.FileState._
import com.ssync.models.SyncFileItem
import com.typesafe.scalalogging.LazyLogging

import scala.io.Source
import scala.util.{Failure, Success, Try}

trait FileToolsController extends LazyLogging {

  def createDestination(rootDestination: String, parentDestination: String) = rootDestination + getSeparator + parentDestination

  def createFile(filename: String, text: String) = {
    if (doesFileExist(filename) equals false) {
      val file = new File(filename)
      val bw = new BufferedWriter(new FileWriter(file))
      bw.write(text)
      bw.close()
    }
    else {
      logger.error(s"$filename already exists")
      throw new FileAlreadyExistsException(filename)
    }
  }

  def doesFileExist(path: String): Boolean = {
    val file = new File(path)
    file.exists && !file.isDirectory
  }

  def openAndReadFile(filename: String): Try[String] = {
    Try(new FileInputStream(filename)) match {
      case Success(stream) => readFile(stream)
      case Failure(exception) => Failure(exception)
    }
  }

  private def readFile(stream: FileInputStream): Try[String] = {
    Try(Source.fromInputStream(stream).mkString)
  }

  def doesDirectoryExist(path: String): Boolean = {
    val dir = new File(path)
    dir.exists && dir.isDirectory
  }

  def collectFilesBasedOnExtensions(path: String, extensions: List[String]):
  List[BFile] = {
    val directory = BFile(path)
    extensions.contains("*") match {
      case true => directory
        .listRecursively.filter(!_.isDirectory).toList
      case false =>
        directory.listRecursively
          .filter(!_.isDirectory)
          .filter(
            file => {
              val ext = file.extension(false, false, true).get
              extensions.exists(_ == ext)
            }
          ).toList
    }
  }

  def moveFiles(syncFileItems: List[SyncFileItem]) = {
    syncFileItems.map {
      item =>
        moveFile(item) match {
          case Success(file) => SyncFileItem(file, item.Destination, MOVED, None)
          case Failure(error) =>
            logger.error(error.getMessage)
            SyncFileItem(item.FileItem, item.Destination, UNABLE_TO_MOVE, Some(error))
        }
    }
  }

  def renameFileBecauseItAlreadyExists(syncFileItem: SyncFileItem) = {
    val file = syncFileItem.FileItem
    val name = file.nameWithoutExtension
    val extension = file.extension(true)
    val renamed = name + "_" + randomString + extension.get
    val renamedFile = file.renameTo(renamed)
    logger.error(s"Renaming file from $name to $renamed")
    SyncFileItem(renamedFile, syncFileItem.Destination, RENAMED, None)
  }

  def collectSourceDirectoriesInOrder(path: String, protectedDirectories: List[String]): Seq[BFile] = {
    val directory = BFile(path)
    directory
      .listRecursively
      .filter(_.isDirectory)
      .filterNot(
        file => {
          val name = file.name
          protectedDirectories.exists(_ == name)
        }
      )
      .toList
      .sorted(BFile.Order.byName).reverse
  }

  def deleteEmptySourceDirectories(directories: Seq[BFile]) = {
    directories
      .foreach(dir => {
        deleteEmptySourceDirectory(dir)
      })
  }

  private def deleteEmptySourceDirectory(directory: BFile): Unit = {
    if (directory.isEmpty) {
      directory.delete()
    }
  }

  private def moveFile(syncFileItem: SyncFileItem): Try[BFile] = {
    doesSyncFileItemFileExist(syncFileItem) match {
      case false =>
        Try {
          syncFileItem.Destination.createIfNotExists(asDirectory = true)
          syncFileItem.FileItem.moveToDirectory(syncFileItem.Destination)
        }
      case true =>
        val renamedSyncFileItem = renameFileBecauseItAlreadyExists(syncFileItem)
        moveFile(renamedSyncFileItem)
    }
  }

  private def doesSyncFileItemFileExist(syncFileItem: SyncFileItem) = {
    val path = syncFileItem.Destination + getSeparator + syncFileItem.FileItem.name
    doesFileExist(path)
  }
}
