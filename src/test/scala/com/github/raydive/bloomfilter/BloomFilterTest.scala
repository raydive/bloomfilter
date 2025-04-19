package com.github.raydive.bloomfilter

import org.scalatest.funsuite.AnyFunSuiteLike

class BloomFilterTest extends AnyFunSuiteLike {

  test("ブルームフィルターにhogeをセットした場合、必ずtrueを返す") {
    val bf = BloomFilter(1000, 0.01)
    val hoge = "hoge"

    bf.set(hoge)

    assert(bf.mightContain(hoge))
  }

  test("ブルームフィルターにhogeをセットしていない場合、必ずfalseを返す") {
    val bf = BloomFilter(1000, 0.01)
    val hoge = "hoge"

    assert(!bf.mightContain(hoge))
  }

  test("ブルームフィルターにたくさんのキーをセットした場合、セットされたキーは必ずtrueを返す") {
    val bf = BloomFilter(1000, 0.01)
    val keys = (0 until 1000).map(_.toString)

    keys.foreach(bf.set)

    keys.foreach { key =>
      assert(bf.mightContain(key))
    }
  }
}
