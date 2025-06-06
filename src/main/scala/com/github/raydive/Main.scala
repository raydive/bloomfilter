package com.github.raydive

import com.github.raydive.bloomfilter.BloomFilter

import scala.collection.mutable
import scala.util.{Random, Using}

object Main {
  def main(args: Array[String]): Unit = {
    val filter = BloomFilter(1000, 0.01)
    val set = mutable.Set.empty[String]
    val keys = (1 to 1000).map { _ =>
      Random.alphanumeric.filter(_.isLetter).take(30).mkString
    }

    // compare the size of the bloom filter and the set
    println("i filter set")
    keys.zipWithIndex.foreach { case (key, i) =>
      filter.set(key)
      set += key

      println("%d %d %d".format(i+1, sizeOf(filter), sizeOf(set)))
    }

    // check if the filter might contain the key
    // This result must be true for all keys in the set
    keys.foreach { key =>
      assert(filter.mightContain(key) == set.contains(key))
    }
  }

  // Get the size of the object
  private def sizeOf(obj: AnyRef): Long = {
    import java.io._

    Using.Manager { use =>
      val baos = use(new ByteArrayOutputStream())
      val oos = use(new ObjectOutputStream(baos))
      oos.writeObject(obj)
      oos.flush()
      baos.size()
    }.get
  }
}
