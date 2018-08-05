package part1

/**
  * TODO documentation is work-in-progress
  * Idris' `the` in Scala
  * 
  * Scala version of
  * https://github.com/rpeszek/IdrisTddNotes/wiki/Part1_Sec2_2_2_the
*/
object Sec2_2_2_the {
  val x = (implicitly[Int](2), implicitly[Double](2), implicitly[Float](2))
  //scala> x
  //res0: (Int, Double, Float) = (2,2.0,2.0)
  
  import shapeless._
  val y = (the[Int](2), the[Double](2), the[Float](2))
  //scala> y
  //res1: (Int, Double, Float) = (2,2.0,2.0)
}
