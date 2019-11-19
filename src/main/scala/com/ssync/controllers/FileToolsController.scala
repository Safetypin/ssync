package com.ssync.controllers

import java.io.{BufferedWriter, File, FileInputStream, FileWriter}

import com.ssync.controllers.FileToolUtils._

import scala.io.Source
import scala.util.{Failure, Success, Try}

trait FileToolsController {

  def createDestination(rootDestination: String, parentDestination: String) = rootDestination + getSeparator + parentDestination

  def createFile(filename: String, settings: String) = {
    val file = new File(filename)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(settings)
    bw.close()
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
}
