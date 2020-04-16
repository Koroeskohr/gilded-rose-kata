package com.gildedrose

class Item(val name: String, var sellIn: Int, var quality: Int) {

}

object Item {
  val minQuality = 0
  val maxQuality = 50

  val sulfuras = "Sulfuras, Hand of Ragnaros"
  val agedBrie = "Aged Brie"
  val backstagePass = "Backstage passes to a TAFKAL80ETC concert"
}