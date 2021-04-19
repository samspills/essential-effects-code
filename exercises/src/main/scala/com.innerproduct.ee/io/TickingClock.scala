package com.innerproduct.ee.io

import cats.effect._

import scala.concurrent.duration._

object TickingClock extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    tickingClockForComp.as(ExitCode.Success)

  val tickingClockForComp: IO[Unit] = for {
    _ <- IO(println(System.currentTimeMillis()))
    _ <- IO.sleep(1.second)
    tc <- tickingClockForComp
  } yield tc

  val tickingClockComposition: IO[Unit] =
    IO.suspend(
      IO(println(System.currentTimeMillis())) *> IO
        .sleep(1.second) *> tickingClockComposition
    )

}
