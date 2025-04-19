package com.github.raydive.bloomfilter

import scalaprops.{Gen, Property, Scalaprops}

object BloomFilterProps extends Scalaprops {
  // Generators for property-based tests
  private val genPositiveInt: Gen[Int] = Gen.choose(1, 10000)
  private val genFalsePositiveRate: Gen[Double] = Gen.choose(1, 99).map(_ / 100.0) // 0.01 to 0.99
  private val genString: Gen[String] = Gen.alphaNumString
  private val genStringList: Gen[List[String]] = Gen.listOf(genString)
  private val genUnsetStringList: Gen[List[String]] = Gen.listOf(genString)

  // Property: Any element added to the filter must be found (no false negatives)
  val noFalseNegativesProperty: Property = Property.forAllG(
    genPositiveInt,
    genFalsePositiveRate,
    genStringList
  ) { (n, fp, strs) =>
    val bf = BloomFilter(n, fp)
    strs.foreach(bf.set)
    strs.forall(bf.mightContain)
  }


}
