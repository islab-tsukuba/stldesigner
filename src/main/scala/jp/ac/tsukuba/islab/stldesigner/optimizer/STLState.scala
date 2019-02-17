package jp.ac.tsukuba.islab.stldesigner.optimizer

import java.io.{File, FilenameFilter}

import jp.ac.tsukuba.islab.stldesigner.evaluator.EyeSizeEvaluator
import jp.ac.tsukuba.islab.stldesigner.circuit.{HspiceServer, LisFile, SPFile, STLElement}
import jp.ac.tsukuba.islab.stldesigner.util.{Config, StateLogger}

import scala.collection.mutable
import scala.util.Random

case class STLState(var spFile: SPFile, conf: Config, server: HspiceServer, var id: Int, firstScore: Double = Double.MaxValue) {
  val dirPath = "/dev/shm/"
  var score: Double = Double.MaxValue
  var evaluator: EyeSizeEvaluator = null

  def calcScore(): Double = {
    if (score != Double.MaxValue && firstScore != Double.MaxValue) {
      return score / firstScore
    }
    if (firstScore == Double.MaxValue) {
      throw new Exception("FirstScore is not calculated. You should run calcFirstScore Function.")
    }
    val hash = spFile.md5Hash
    val outputName = hash + "_" + id
    val spFilePath = new File(dirPath, outputName + ".sp")
    val lisFilePath = new File(dirPath, outputName + ".lis")
    spFile.writeToFile(spFilePath)
    server.runSpiceFile(spFilePath.getPath)
    val lisFile = LisFile(lisFilePath.getPath, conf)
    evaluator = new EyeSizeEvaluator(lisFile, conf, spFile.getTran())
    score = evaluator.evaluate()
    deleteFileByPrefix(dirPath, outputName)
    score / firstScore
  }

  def createNeighbour(): STLState = {
    val stlElements: List[STLElement] = spFile.getSTLElements()
    val totalSegNum = stlElements.map(_.sepNum).sum
    val shiftSegmentList = createShiftSegmentList(totalSegNum, conf.saConf.shiftSegmentNum)
    var begin = 0
    val newStlElements = stlElements.map {
      stlElement => {
        val shifted = stlElement.createShifted(shiftSegmentList.slice(begin, begin + stlElement.sepNum))
        begin = shifted.sepNum
        shifted
      }
    }
    val newSPFile = spFile.copy(initSTLElements = newStlElements)
    this.copy(spFile = newSPFile)
  }

  private def createShiftSegmentList(totalSegment: Int, shiftSegment: Int): Seq[Boolean] = {
    if (shiftSegment == -1 || totalSegment <= shiftSegment) {
      return Seq.fill(totalSegment)(true)
    }
    val ret = mutable.Seq.fill(totalSegment)(false)
    for (i <- 0 until shiftSegment) {
      val position = Random.nextInt(totalSegment - i)
      var skip = 0
      for (j <- 0 to position) {
        while (ret(j + skip)) skip += 1
      }
      ret(position + skip) = true
    }
    ret
  }

  def createCross(state: STLState): STLState = {
    val parent1: List[STLElement] = spFile.getSTLElements()
    val parent2: List[STLElement] = state.spFile.getSTLElements()

    val newSTLElements = for (i <- parent1.indices) yield {
      parent1(i).cross(parent2(i))
    }
    val newSPFile = spFile.copy(initSTLElements = newSTLElements.toList)
    this.copy(spFile = newSPFile)
  }

  def createRandom(): STLState = {
    val stlElements: List[STLElement] = spFile.getSTLElements()
    val newStlElements = stlElements.map(stlElement => stlElement.createRandom())
    val newSPFile = spFile.copy(initSTLElements = newStlElements)
    this.copy(spFile = newSPFile)
  }

  def writeData(logger: StateLogger, gen: Int, score: Double): Unit = {
    if (evaluator == null) {
      throw new Exception("Evaluator is not created. You should run calcScore Function.")
    }
    logger.writeData(spFile, evaluator, gen, score)
  }

  def calcFirstScore(outputName: String = "first"): Double = {
    val spFilePath = new File(dirPath, outputName + ".sp")
    val lisFilePath = new File(dirPath, outputName + ".lis")
    spFile.writeFirstToFile(spFilePath)
    server.runSpiceFile(spFilePath.getPath)
    val lisFile = LisFile(lisFilePath.getPath, conf)
    val evaluator = new EyeSizeEvaluator(lisFile, conf, spFile.getTran())
    val firstScore = evaluator.evaluate()
    deleteFileByPrefix(dirPath, outputName)
    firstScore
  }

  def deleteFileByPrefix(path: String, prefix: String): Unit = {
    val dir = new File(path)
    val files = dir.listFiles(
      new FilenameFilter() {
        def accept(dir: File, name: String): Boolean = name.matches(prefix + ".*")
      })
    files.foreach(file => {
      file.delete()
    })
  }
}
