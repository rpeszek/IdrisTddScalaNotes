package part2

/**
  * This is very much TODO
  */
object Sec8_1_eqproof {
  sealed trait Equality[A, B]

  case class Refl[A]() extends Equality[A, A]

  case object Equality {
    implicit def refl[B]: Equality[B, B] = Refl[B]()
  }

  sealed trait Bool
  case object True extends Bool
  case object False extends Bool

  def trivial1 : Equality[True.type, True.type] = Equality.refl
  
  // def shouldNotCompile : Equality[True.type, False.type] = Equality.refl
}

/*
  Refs 
  https://stackoverflow.com/questions/40710707/dependent-typing-style-equality-proofs-in-scala
 */
