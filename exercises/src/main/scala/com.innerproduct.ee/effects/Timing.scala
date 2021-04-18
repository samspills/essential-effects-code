package com.innerproduct.ee.effects

import scala.concurrent.duration.{FiniteDuration, MILLISECONDS}

object Timing extends App {
  val clock: MyIO[Long] =
    MyIO(() => System.currentTimeMillis()) // omit parens or not? parens indicate side effecting?????

  def time[A](action: MyIO[A]): MyIO[(FiniteDuration, A)] =
    for {
      start <- clock

      /** comparing this line to solution
        * soln has a <- action, which I think is functionally equivalent
        * <- is syntax for applying flatmap
        * I'm not applying flatMap I'm calling unsafeRun
        * BUT unsafeRun is being called by flatMap ....
        * I think a <- action is obviously the better choice for taking full advantage of flatMap
        * what I can't tell is if what I'm doing is WRONG in a particular way that isn't obvious in toy example?*/
      a = action.unsafeRun()
      fin <- clock
    } yield (FiniteDuration(fin - start, MILLISECONDS), a)

  val timedHello = Timing.time(MyIO.putStr("hello"))

  timedHello.unsafeRun() match {
    case (duration, _) => println(s"'hello' took $duration")
  }
}
