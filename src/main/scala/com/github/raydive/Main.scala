package com.github.raydive

import com.github.raydive.bloomfilter.BloomFilter

object Main {
  def main(args: Array[String]): Unit = {
    val filter = BloomFilter(1000, 0.01)
    filter.set("hoge")

    println(filter.mightContain("hoge"))
    println(filter.mightContain("fuga"))
  }
}
