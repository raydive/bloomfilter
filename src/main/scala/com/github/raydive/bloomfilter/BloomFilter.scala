package com.github.raydive.bloomfilter

import com.google.common.hash.Hashing

/**
 * A BloomFilter is a probabilistic data structure that is used to test whether an element is a member of a set.
 * False positive matches are possible, but false negatives are not.
 * Elements can be added to the set, but not removed.
 * The more elements that are added to the set, the larger the probability of false positives.
 *
 * @param n the number of elements expected to be stored in the filter (> 0)
 * @param m the number of bits in the filter (> 0)
 * @param k the number of hash functions (> 0)
 * @param bitSet the bit set used to store the filter
 */
class BloomFilter(val n: Int,
                  val m: Int,
                  val k: Int,
                  val bitSet: java.util.BitSet) extends Serializable {
  assert(n > 0, "n must be greater than 0")
  assert(m > 0, "m must be greater than 0")
  assert(k > 0, "k must be greater than 0")

  /**
   * Add a key to the filter
   * @param key the key to add
   */
  def set(key: String): Unit = {
    for (i <- 0 until k) {
      var h = hash(key, i)
      if (h < 0) h = ~h // Convert negative hash to non-negative
      bitSet.set(h % m)
    }
  }

  /**
   * Check if the filter might contain the key
   * @param key the key to check
   * @return true if the filter might contain the key, false if the filter definitely does not contain the key
   */
  def mightContain(key: String): Boolean = {
    (0 until k).forall { i =>
      var h = hash(key, i)
      if (h < 0) h = ~h // Convert negative hash to non-negative
      bitSet.get(h % m)
    }
  }

  /**
   * Calculate the hash of a key (using double hashing)
   * This algorithm comes from Guava's BloomFilter implementation
   * https://github.com/google/guava/blob/ba42c96029643420d1de2e0359ea393c466a131f/guava/src/com/google/common/hash/BloomFilterStrategies.java#L53-L67
   * @param key the key to hash
   * @param index the index of the hash
   * @return the hash
   */
  private def hash(key: String, index: Int): Int = {
    val hash = Hashing.murmur3_128().hashUnencodedChars(key).asLong()
    val hash1 = hash.toInt
    val hash2 = hash >>> 32
    (hash1 + index * hash2).toInt
  }
}

object BloomFilter {
  /**
   * Create a new BloomFilter
   * @param n the number of elements expected to be stored in the filter(> 0)
   * @param m the number of bits in the filter(> 0)
   * @param k the number of hash functions(> 0)
   * @return the new BloomFilter
   */
  def apply(n: Int, m: Int, k: Int): BloomFilter = {
    assert(n > 0, "n must be greater than 0")
    assert(m > 0, "m must be greater than 0")
    assert(k > 0, "k must be greater than 0")

    new BloomFilter(n, m, k, new java.util.BitSet(m))
  }

  /**
   * Create a new BloomFilter
   * @param n the number of elements expected to be stored in the filter(> 0)
   * @param falsePositive the desired false positive rate of the filter 0.1 to 1.0
   * @return the new BloomFilter
   */
  def apply(n: Int, falsePositive: Double): BloomFilter = {
    assert(n > 0, "n must be greater than 0")
    assert(falsePositive > 0.0 && falsePositive <= 1.0, "falsePositive must be between 0.0 and 1.0")

    val m = calculateOptimalM(n, falsePositive)
    val k = calculateOptimalK(n, m)
    new BloomFilter(n, m, k, new java.util.BitSet(m))
  }

  private def calculateOptimalM(n: Int, falsePositive: Double): Int = {
    val m = -1 * n * math.log(falsePositive) / math.pow(math.log(2), 2)
    math.ceil(m).toInt
  }

  private def calculateOptimalK(n: Int, m: Int): Int = {
    val k = math.ceil(m / n * math.log(2)).toInt
    if (k < 1) 1 else k
  }
}
