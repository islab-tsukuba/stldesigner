package jp.ac.tsukuba.islab.stldesigner.optimizer

import java.io.{File, FilenameFilter}

import jp.ac.tsukuba.islab.stldesigner.evaluator.EyeSizeEvaluator
import jp.ac.tsukuba.islab.stldesigner.spice.{HspiceServer, LisFile, SPFile, STLElement}
import jp.ac.tsukuba.islab.stldesigner.util.{Config, StateLogger}

import scala.collection.mutable
import scala.util.Random

case class STLState(var spFile: SPFile, conf: Config, var id: Int) {
  val dirPath = "/dev/shm/"
  var score: Double = Double.MaxValue
  var evaluator: EyeSizeEvaluator = null
  var firstScore: Double = Double.MaxValue

  def calcScore(server: HspiceServer): Double = {
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
    val newState = this.copy(spFile = spFile.deepCopy())
    newState.firstScore = firstScore
    newState.shiftSegment()
  }

  private def shiftSegment(): STLState = {
    val stlElements: List[STLElement] = spFile.getSTLElements()
    val totalSegNum = stlElements.map(_.sepNum).sum
    val shiftSegmentList = createShiftSegmentList(totalSegNum, conf.saConf.shiftSegmentNum)
    var begin = 0
    val newStlElements = stlElements.map {
      stlElement => {
        stlElement.shiftElements(shiftSegmentList.slice(begin, begin + stlElement.sepNum))
        begin = stlElement.sepNum
        stlElement
      }
    }
    spFile.setSTLElements(newStlElements)
    this
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

  def createRandom(): STLState = {
    val newState = this.copy(spFile = spFile.deepCopy())
    newState.firstScore = firstScore
    newState.assignRandomSegment()
  }

  def assignRandomSegment(): STLState = {
    val stlElements: List[STLElement] = spFile.getSTLElements()
    val newStlElements = stlElements.map(stlElement => stlElement.assignRandom())
    spFile.setSTLElements(newStlElements)
    this
  }

  def writeData(logger: StateLogger, gen: Int, score: Double): Unit = {
    if (evaluator == null) {
      throw new Exception("Evaluator is not created. You should run calcScore Function.")
    }
    logger.writeData(spFile, evaluator, gen, score)
  }

  def calcFirstScore(server: HspiceServer): Double = {
    val outputName = "first"
    val spFilePath = new File(dirPath, outputName + ".sp")
    val lisFilePath = new File(dirPath, outputName + ".lis")
    spFile.writeFirstToFile(spFilePath)
    server.runSpiceFile(spFilePath.getPath)
    val lisFile = LisFile(lisFilePath.getPath, conf)
    val evaluator = new EyeSizeEvaluator(lisFile, conf, spFile.getTran())
    firstScore = evaluator.evaluate()
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
