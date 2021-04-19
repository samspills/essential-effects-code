package com.innerproduct.ee.io

import cats.effect._

import scala.concurrent.duration._

object TickingClock extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    tickingClockForComp
      .guaranteeCase {
        case ExitCase.Canceled =>
          IO(println("TickingClock was cancelled beep boop"))
        case ExitCase.Completed =>
          IO(println("TickingClock completed (we should never see this?)"))
        case ExitCase.Error(e) =>
          IO(println(s"TickingClock failure with error: $e"))
      }
      .as(ExitCode.Success)

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
